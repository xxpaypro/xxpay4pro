package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.*;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.entity.IsvInfo;
import org.xxpay.core.entity.IsvSettConfig;
import org.xxpay.core.entity.IsvWx3rdInfo;
import org.xxpay.core.entity.SysUser;
import org.xxpay.core.service.IIsvInfoService;
import org.xxpay.core.service.IIsvSettConfigService;
import org.xxpay.core.service.IIsvWx3rdInfoService;
import org.xxpay.core.service.ISysService;
import org.xxpay.service.dao.mapper.IsvInfoMapper;
import org.xxpay.service.dao.mapper.SysUserMapper;

import java.util.Date;

/**
 * <p>
 * 服务商信息表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-27
 */
@Service
public class IsvInfoServiceImpl extends ServiceImpl<IsvInfoMapper, IsvInfo> implements IIsvInfoService {

    @Autowired
    private ISysService sysService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private IIsvSettConfigService isvSettConfigService;

    @Autowired
    private IIsvWx3rdInfoService isvWx3rdInfoService;


    @Override
    public IsvInfo findByLoginName(String loginName) {
        if(StringUtils.isBlank(loginName)) return null;

        LambdaQueryWrapper<IsvInfo> lambdaQueryWrapper = new QueryWrapper<IsvInfo>().lambda();
        if(StrUtil.checkEmail(loginName)) {
//            lambdaQueryWrapper.eq(IsvInfo::getEmail, loginName);
        } else if(StrUtil.checkMobileNumber(loginName)) {
//            lambdaQueryWrapper.eq(IsvInfo::getMobile, loginName);
        } else if(NumberUtils.isDigits(loginName)) {
            lambdaQueryWrapper.eq(IsvInfo::getIsvId, loginName);
        }else{
//            lambdaQueryWrapper.eq(IsvInfo::getUserName, loginName);
        }
        return this.getOne(lambdaQueryWrapper);
    }


    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void updateIsv(IsvInfo isvInfo, SysUser isvUser){

        //更新商户的登录信息
        if(isvUser != null){
            LambdaUpdateWrapper<SysUser> lambda = new LambdaUpdateWrapper();
            lambda.eq(SysUser::getBelongInfoId, isvInfo.getIsvId());
            lambda.eq(SysUser::getBelongInfoType, MchConstant.INFO_TYPE_ISV);
            lambda.eq(SysUser::getIsSuperAdmin, MchConstant.PUB_YES); //服务商本身
            sysUserMapper.update(isvUser, lambda);
        }

        //更新商户主体信息
        updateById(isvInfo);
    }



    //添加isv信息
    public void addIsv(IsvInfo isvInfo){

        //1. 插入服务商信息主表
        boolean insertSuccess = save(isvInfo);
        if(!insertSuccess){
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL); //向上抛出异常， 事务回滚
        }

        //2. 判断状态
        if(isvInfo.getStatus() == MchConstant.STATUS_AUDIT_ING){  //待审核状态：  仅插入当前表
            return;
        }

        //3. 初始化其他表数据
        initMchOthersInfo(isvInfo);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void auditMch(Long isvId, Byte auditStatus){

        IsvInfo updateRecord = new IsvInfo();
        updateRecord.setIsvId(isvId);
        updateRecord.setStatus(auditStatus);
        this.updateById(updateRecord);

        //审核通过， 需初始化其他信息
        if(auditStatus == MchConstant.STATUS_OK){
            initMchOthersInfo(getById(isvId));
        }
    }

    private void initMchOthersInfo(IsvInfo isvInfo){

        int userCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getIsSuperAdmin, MchConstant.PUB_YES)
                .eq(SysUser::getBelongInfoType, MchConstant.INFO_TYPE_ISV)
                .eq(SysUser::getBelongInfoId, isvInfo.getIsvId())
        );

        //当服务商存在超管信息时， 说明已经初始化完成， 无需再次初始化
        if(userCount > 0) return ;

        //1. 插入服务商的 登录认证表（服务商主体超管用户）
        SysUser isvSU = new SysUser();
        isvSU.setNickName(isvInfo.getIsvName()); //用户昵称
        isvSU.setLoginUserName(isvInfo.getLoginUserName()); //登录名称
        isvSU.setEmail(isvInfo.getEmail());  //登录邮箱
        isvSU.setMobile(isvInfo.getMobile());  //登录手机号
        String loginPassword = StringUtils.isNotBlank(isvInfo.getRegisterPassword()) ? isvInfo.getRegisterPassword() : SpringSecurityUtil.generateSSPassword(MchConstant.MCH_DEFAULT_PASSWORD);
        isvSU.setLoginPassword(loginPassword);  //登录默认密码
        isvSU.setStatus(MchConstant.PUB_YES);  //状态, 默认登录状态可用
        isvSU.setIsSuperAdmin(MchConstant.PUB_YES);  //是否超管（拥有当前角色的所有权限） ： 是
        isvSU.setBelongInfoType(MchConstant.INFO_TYPE_ISV);  //所属类型： 服务商isv
        isvSU.setBelongInfoId(isvInfo.getIsvId()); //isvId
        isvSU.setSex(MchConstant.SEX_UNKNOWN); //性别 未知

        //插入服务商的结算默认配置数据
        IsvSettConfig isvSettConfig = new IsvSettConfig();
        isvSettConfig.setIsvId(isvInfo.getIsvId());
        isvSettConfig.setSettDateType(MchConstant.ISV_SETT_DATE_TYPE_DAY);  //默认 15天为周期 结算
        isvSettConfig.setSettSetDay(15);

        //下次执行分润计划的时间
        Date nextProfitDate = XXPayUtil.nextProfitTaskTime(isvSettConfig.getSettDateType(), isvSettConfig.getSettSetDay(), new Date());
        isvSettConfig.setNextSettDate(nextProfitDate);
        isvSettConfigService.save(isvSettConfig);

        JWTBaseUser jwtBaseUser = SpringSecurityUtil.getCurrentJWTUser();  //获取当前登录用户信息
        isvSU.setCreateUserId(jwtBaseUser.getUserId()); //创建人，  在context中获取
        int updateRow = sysService.add(isvSU);
        if( updateRow <= 0 ){
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL); //向上抛出异常， 事务回滚
        }

        //插入服务商 第三方配置表
        IsvWx3rdInfo isvWx3rdInfo = new IsvWx3rdInfo();
        isvWx3rdInfo.setIsvId(isvInfo.getIsvId());
        isvWx3rdInfo.setStatus(MchConstant.ISV3RD_STATUS_WAIT_UPLOAD_INFO);  // 待录入信息
        isvWx3rdInfo.setConfigAuthHost("isv.f.xxpay.org");  //服务商配置项：授权发起页域名
        isvWx3rdInfo.setConfigTestMpAccount("");  //服务商配置项：授权测试公众号列表
        isvWx3rdInfo.setConfigAuthMsgUrl("isv.f.xxpay.org/api/wxCallback/componentTicket/" + isvInfo.getIsvId());  //服务商配置项：授权事件接收URL
        isvWx3rdInfo.setConfigMsgToken(MySeq.getUUID());  //服务商配置项：消息校验Token

        isvWx3rdInfo.setConfigAesKey((MySeq.getUUID() + MySeq.getUUID()).substring(0, 43));   //服务商配置项：消息加解密Key, 务必是43位
        isvWx3rdInfo.setConfigNormalMsgUrl("isv.f.xxpay.org/api/wxCallback/normalMsg/" + isvInfo.getIsvId()); //服务商配置项：消息与事件接收URL
        isvWx3rdInfo.setConfigMiniHost("isv.f.xxpay.org"); //服务商配置项：小程序服务器域名
        isvWx3rdInfo.setConfigMiniBizHost("isv.f.xxpay.org");  //服务商配置项：小程序业务域名
        isvWx3rdInfo.setConfigWhiteIp("");  //服务商配置项：白名单IP地址列表
        isvWx3rdInfoService.save(isvWx3rdInfo);
    }

}

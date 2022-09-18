package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.*;
import org.xxpay.service.dao.mapper.MchAppConfigMapper;
import org.xxpay.service.dao.mapper.MchInfoMapper;
import org.xxpay.service.dao.mapper.MchStoreMapper;
import org.xxpay.service.dao.mapper.SysUserMapper;

import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 17/9/8
 * @description:
 */
@Service
public class MchInfoServiceImpl extends ServiceImpl<MchInfoMapper, MchInfo> implements IMchInfoService {

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IFeeScaleService feeScaleService;

    @Autowired
    private ISettBankAccountService settBankAccountService;

    @Autowired
    private ISysService sysService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private MchStoreMapper mchStoreMapper;

    @Autowired
    private MchAppConfigMapper mchAppConfigMapper;

    @Autowired
    private ISysProvinceCodeService sysProvinceCodeService;

    @Autowired
    private ISysCityCodeService sysCityCodeService;

    @Autowired
    private ISysAreaCodeService sysAreaCodeService;

    @Override
    public IPage<MchInfo> selectPage(IPage page, MchInfo mchInfo){

        LambdaQueryWrapper<MchInfo> queryWrapper = getQueryWrapper(mchInfo);
        queryWrapper.orderByDesc(MchInfo::getCreateTime);
        return page(page, queryWrapper);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void add(MchInfo mchInfo){

        if(StringUtils.isEmpty(mchInfo.getLoginEmail())){
            mchInfo.setLoginEmail(null);
        }

        //0. 校验是否用户名等重复
        checkInfoRepeat(mchInfo);

        // 1. 初始化商户基本信息
        mchInfo.setPrivateKey(RandomStringUtils.randomAlphanumeric(128).toUpperCase()); // 生成商户默认的私钥
        mchInfo.setSignStatus(MchConstant.MCH_SIGN_STATUS_WAIT_FILL_INFO); //签约状态： 待补充资料

        // 2. 持久化
        if(!this.save(mchInfo)){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        //3. 判断状态
        if(mchInfo.getStatus() == MchConstant.STATUS_AUDIT_ING){  //待审核状态：  仅插入当前表
            return;
        }

        //4. 初始化其他表数据
        initMchOthersInfo(mchInfo);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void updateMch(MchInfo mchInfo){

        Long updateMchId = mchInfo.getMchId();

        if(updateMchId == null){
            throw new ServiceException(RetEnum.RET_COMM_PARAM_ERROR);
        }

        if(StringUtils.isEmpty(mchInfo.getLoginEmail())){
            mchInfo.setLoginEmail(null);
        }

        //0. 校验是否用户名等重复
        checkInfoRepeat(mchInfo);

        //1. 如果修改为开启， 需判断关联表是否存在数据并初始化
        if(mchInfo.getStatus() != null && mchInfo.getStatus() == MchConstant.STATUS_OK){
            initMchOthersInfo(mchInfo);
        }

        //2. 更新当前对象
        if(!updateById(mchInfo)){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        //3. 如果email为空， 需要更新为null
        if( mchInfo.getLoginEmail() == null ){
            LambdaUpdateWrapper<MchInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(MchInfo::getLoginEmail, null).eq(MchInfo::getMchId, updateMchId);
            this.update(updateWrapper);
        }

        //4. 更新超管信息
        LambdaUpdateWrapper<SysUser> sysUserUpdateWrapper = new LambdaUpdateWrapper<>();
        sysUserUpdateWrapper
                .eq(SysUser::getBelongInfoType, MchConstant.INFO_TYPE_MCH)
                .eq(SysUser::getBelongInfoId, updateMchId)
                .eq(SysUser::getIsSuperAdmin, MchConstant.PUB_YES);

        sysUserUpdateWrapper.set(SysUser::getLoginUserName, mchInfo.getLoginUserName());
        sysUserUpdateWrapper.set(SysUser::getMobile, mchInfo.getLoginMobile());
        sysUserUpdateWrapper.set(SysUser::getEmail, mchInfo.getLoginEmail());
        sysUserUpdateWrapper.set(SysUser::getNickName, mchInfo.getMchName());
        sysUserMapper.update(null, sysUserUpdateWrapper);    //当不存在信息  || 未发生变化时， 返回false , 无需判断即可
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void auditMch(Long mchId, Byte auditStatus){

        MchInfo updateRecord = new MchInfo();
        updateRecord.setMchId(mchId);
        updateRecord.setStatus(auditStatus);
        this.updateById(updateRecord);

        //审核通过， 需初始化其他信息
        if(auditStatus == MchConstant.STATUS_OK){
            initMchOthersInfo(getById(mchId));
        }
    }

    /** 初始化商户其他信息 **/
    private void initMchOthersInfo(MchInfo mchInfo){

        int userCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getIsSuperAdmin, MchConstant.PUB_YES)
                .eq(SysUser::getBelongInfoType, MchConstant.INFO_TYPE_MCH)
                .eq(SysUser::getBelongInfoId, mchInfo.getMchId())
        );

        //当商户存在超管信息时， 说明已经初始化完成， 无需再次初始化
        if(userCount > 0) return ;

        //正常状态： 以下插入各种默认信息及认证信息
        accountBalanceService.initAccount(MchConstant.INFO_TYPE_MCH, mchInfo.getMchId(), mchInfo.getMchName(),
                MchConstant.ACCOUNT_TYPE_BALANCE, MchConstant.ACCOUNT_TYPE_AGPAY_BALANCE, MchConstant.ACCOUNT_TYPE_SECURITY_MONEY);

        //给商户添加默认门店
        MchStore mchStore = new MchStore();
        mchStore.setStoreNo(String.valueOf(mchInfo.getMchId()));
        mchStore.setStoreName(mchInfo.getMchName());

        mchStore.setProvinceCode(mchInfo.getProvinceCode());
        mchStore.setCityCode(mchInfo.getCityCode());
        mchStore.setAreaCode(mchInfo.getAreaCode());
        mchStore.setAreaInfo(mchInfo.getAreaInfo());

        mchStore.setAddress(mchInfo.getAddress());
        mchStore.setStatus(MchConstant.PUB_YES);
        mchStore.setTel(mchInfo.getLoginMobile());
        mchStore.setMchId(mchInfo.getMchId());
        mchStore.setRefundPassword(SpringSecurityUtil.generateSSPassword(MchConstant.MCH_DEFAULT_PASSWORD));
        mchStore.setStoreImgPath(MchConstant.MCH_STORE_DEFAULT_AVATAR);
        mchStore.setMiniImgPath(MchConstant.MCH_STORE_MINI_TOP_IMAGEPATH);

        if(mchStoreMapper.insert(mchStore) <= 0 ){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        //2. 插入商户的 登录认证表（商户主体超管用户）
        SysUser mchSU = new SysUser();
        mchSU.setNickName(mchInfo.getMchName()); //用户昵称
        mchSU.setLoginUserName(mchInfo.getLoginUserName()); //登录名称
        mchSU.setEmail(mchInfo.getLoginEmail());  //登录邮箱
        mchSU.setMobile(mchInfo.getLoginMobile());  //登录手机号
        String loginPassword = StringUtils.isNotBlank(mchInfo.getRegisterPassword()) ? mchInfo.getRegisterPassword() : SpringSecurityUtil.generateSSPassword(MchConstant.MCH_DEFAULT_PASSWORD);
        mchSU.setLoginPassword(loginPassword);  //登录默认密码
        mchSU.setStatus(MchConstant.PUB_YES);  //状态, 默认登录状态可用
        mchSU.setIsSuperAdmin(MchConstant.PUB_YES);  //是否超管（拥有当前角色的所有权限） ： 是
        mchSU.setBelongInfoType(MchConstant.INFO_TYPE_MCH);  //所属类型： 商户
        mchSU.setBelongInfoId(mchInfo.getMchId()); //mchId
        mchSU.setSex(MchConstant.SEX_UNKNOWN); //性别 未知
        mchSU.setStoreId(mchStore.getStoreId());

        JWTBaseUser jwtBaseUser = SpringSecurityUtil.getCurrentJWTUser();  //获取当前登录用户信息
        mchSU.setCreateUserId(jwtBaseUser.getUserId()); //创建人，  在context中获取
        if(sysUserMapper.insertSelective(mchSU) <= 0){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        //3.插入用户app配置
        MchAppConfig appConfig = new MchAppConfig();
        appConfig.setUserId(mchSU.getUserId());
        appConfig.setAppPush(MchConstant.PUB_YES);
        appConfig.setAppVoice(MchConstant.PUB_YES);

        if(mchAppConfigMapper.insert(appConfig) <= 0){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int updateMch(MchInfo record, SettBankAccount mchSettBankAccount, SysUser mchUser) {

        //更新商户的登录信息
        if(mchUser != null){
            LambdaUpdateWrapper<SysUser> lambda = new LambdaUpdateWrapper();
            lambda.eq(SysUser::getBelongInfoId, record.getMchId());
            lambda.eq(SysUser::getBelongInfoType, MchConstant.INFO_TYPE_MCH);
            lambda.eq(SysUser::getIsSuperAdmin, MchConstant.PUB_YES); //商户本身
            sysUserMapper.update(mchUser, lambda);
        }

        if(mchSettBankAccount != null && StringUtils.isNotBlank(mchSettBankAccount.getAccountNo())
                            && StringUtils.isNotBlank(mchSettBankAccount.getName()) && StringUtils.isNotBlank(mchSettBankAccount.getAccountName())){
            settBankAccountService.saveOrUpdate(mchSettBankAccount);
        }

        //更新商户主体信息
        return mchInfoMapper.updateById(record);
    }

    @Override
    public MchInfo getOneMch(Long mchId, Long isvId, Long agentId){
        MchInfo mchInfo = new MchInfo();
        mchInfo.setMchId(mchId);
        mchInfo.setIsvId(isvId);
        mchInfo.setAgentId(agentId);
        return getOne(getQueryWrapper(mchInfo));
    }

    @Override
    public MchInfo findByMchId(Long mchId) {
        return mchInfoMapper.selectById(mchId);
    }

    @Override
    public MchInfo findByLoginName(String loginName) {
        if(StringUtils.isBlank(loginName)) return null;
        MchInfo mchInfo;
        if(StrUtil.checkEmail(loginName)) {
            mchInfo = findByEmail(loginName);
            if(mchInfo != null) return mchInfo;
        }
        if(StrUtil.checkMobileNumber(loginName)) {
            mchInfo = findByMobile(Long.parseLong(loginName));
            if(mchInfo != null) return mchInfo;
        }
        if(NumberUtils.isDigits(loginName)) {
            mchInfo = findByMchId(Long.parseLong(loginName));
            if(mchInfo != null) return mchInfo;
        }
        mchInfo = findByUserName(loginName);
        return mchInfo;
    }

    @Override
    public MchInfo findByMobile(Long mobile) {
        MchInfo mchInfo = new MchInfo(); mchInfo.setLoginMobile(mobile + "");
        return getOne(getQueryWrapper(mchInfo));
    }

    @Override
    public MchInfo findByEmail(String email) {

        MchInfo mchInfo = new MchInfo(); mchInfo.setLoginEmail(email);
        return getOne(getQueryWrapper(mchInfo));
    }

    @Override
    public MchInfo findByUserName(String userName) {

        MchInfo mchInfo = new MchInfo(); mchInfo.setLoginUserName(userName);
        return getOne(getQueryWrapper(mchInfo));
    }

    @Override
    public Integer count(MchInfo mchInfo) {
        return mchInfoMapper.selectCount(getQueryWrapper(mchInfo));
    }


    @Override
    public Map count4Mch() {
        Map param = new HashMap<>();
        return mchInfoMapper.count4Mch(param);
    }

    @Override
    public Long countMchByAgentLevel(Date createTimeStart, Date createTimeEnd, Integer agentLevel, Long isvId, List<Long> agentIdList){
        return mchInfoMapper.countMchByAgentLevel(createTimeStart, createTimeEnd, agentLevel, isvId, agentIdList);
    }


    @Override
    public Integer countAllMch(Date createTimeStart, Date createTimeEnd, Long isvId, boolean onlySubMch){

        LambdaQueryWrapper<MchInfo> queryWrapper = new LambdaQueryWrapper();
        if(createTimeStart != null) queryWrapper.ge(MchInfo::getCreateTime, createTimeStart);
        if(createTimeEnd != null) queryWrapper.le(MchInfo::getCreateTime, createTimeEnd);
        if(isvId != null) queryWrapper.eq(MchInfo::getIsvId, isvId);
        if(onlySubMch){ //仅查询直接子商户
            queryWrapper.isNull(MchInfo::getAgentId);
        }
        return mchInfoMapper.selectCount(queryWrapper);
    }

    @Override
    public List<MchInfo> selectSnashotPage(int offset, int limit, MchInfo mchInfo, Byte applyStatus, Byte applyType) {
        Map param = new HashMap<>();
        param.put("offset", offset);
        param.put("limit", limit);
        param.put("isvId", mchInfo.getIsvId());
        param.put("applyStatus", applyStatus);

        if(applyType == MchConstant.MCH_APPLY_TYPE_ALIPAY){
            return mchInfoMapper.selectAliSnashotPage(param);
        }

        return mchInfoMapper.selectWxSnashotPage(param);
    }

    @Override
    public int countSnashot(MchInfo mchInfo, Byte applyStatus, Byte applyType) {
        Map param = new HashMap<>();
        param.put("isvId", mchInfo.getIsvId());
        param.put("applyStatus", applyStatus);
        param.put("applyStatus", applyStatus);
        if(applyType == MchConstant.MCH_APPLY_TYPE_ALIPAY){
            return mchInfoMapper.countAliSnashot(param);
        }

        return mchInfoMapper.countWxSnashot(param);
    }

    @Override
    public List<Long> selectMchIdForNc(List<Long> mchIds) {
        if (CollectionUtils.isEmpty(mchIds)) {
            return null;
        }
        return mchInfoMapper.selectMchIdForNc(mchIds);
    }

    /** 生成[wrapper]查询条件 **/
    private LambdaQueryWrapper<MchInfo> getQueryWrapper(MchInfo mchInfo){

        LambdaQueryWrapper<MchInfo> queryWrapper = new LambdaQueryWrapper();

        if(mchInfo != null) {
            if(mchInfo.getMchId() != null) queryWrapper.eq(MchInfo::getMchId, mchInfo.getMchId());
            if(mchInfo.getType() != null && mchInfo.getType() != -99) queryWrapper.eq(MchInfo::getType, mchInfo.getType());
            if(StringUtils.isNotEmpty(mchInfo.getLoginEmail())) queryWrapper.eq(MchInfo::getLoginEmail, mchInfo.getLoginEmail());
            if(StringUtils.isNotEmpty(mchInfo.getLoginMobile())) queryWrapper.eq(MchInfo::getLoginMobile, mchInfo.getLoginMobile());
            if(mchInfo.getAgentId() != null) queryWrapper.eq(MchInfo::getAgentId, mchInfo.getAgentId());
            if(mchInfo.getIsvId() != null) queryWrapper.eq(MchInfo::getIsvId, mchInfo.getIsvId());
            if(mchInfo.getStatus() != null) queryWrapper.eq(MchInfo::getStatus, mchInfo.getStatus());
            if(mchInfo.getSignStatus() != null) queryWrapper.eq(MchInfo::getSignStatus, mchInfo.getSignStatus());
            if(mchInfo.getMiniRole() != null) queryWrapper.eq(MchInfo::getMiniRole, mchInfo.getMiniRole());
            if (mchInfo.getParentId() != null) queryWrapper.eq(MchInfo::getParentId, mchInfo.getParentId());
            if(StringUtils.isNotEmpty(mchInfo.getHospitalId())) queryWrapper.eq(MchInfo::getHospitalId, mchInfo.getHospitalId());

            if(mchInfo.getPs() != null){
                if(StringUtils.isNotEmpty(mchInfo.getPsStringVal("mchNameLike"))){
                    queryWrapper.like(MchInfo::getMchName, mchInfo.getPsStringVal("mchNameLike"));
                }

                if("1".equals(mchInfo.getPsStringVal("agentIdIsNull"))){
                    queryWrapper.isNull(MchInfo::getAgentId);
                }

                if(mchInfo.getPsVal("agentIdIn") != null){
                    queryWrapper.in(MchInfo::getAgentId, mchInfo.getPsListVal("agentIdIn"));
                }


            }
        }
        return queryWrapper;
    }

    @Override
    public void checkInfoRepeat(MchInfo record){

        // 确认手机不能重复
        LambdaQueryWrapper<MchInfo> mobileQueryWrapper = record.lambda().eq(MchInfo::getLoginMobile, record.getLoginMobile());
        if(record.getMchId() != null) mobileQueryWrapper.ne(MchInfo::getMchId, record.getMchId());
        if(count(mobileQueryWrapper) > 0) throw new ServiceException(RetEnum.RET_MCH_MOBILE_USED);


        // 确认邮箱不能重复
        if(record.getLoginEmail() != null){
            LambdaQueryWrapper<MchInfo> emailQueryWrapper = record.lambda().eq(MchInfo::getLoginEmail, record.getLoginEmail());
            if(record.getMchId() != null) emailQueryWrapper.ne(MchInfo::getMchId, record.getMchId());
            if(count(emailQueryWrapper) > 0) throw new ServiceException(RetEnum.RET_MCH_EMAIL_USED);
        }



        //设置了用户名 并且不合法
        if(record.getLoginUserName() != null && !XXPayUtil.checkLoginUserName(record.getLoginUserName())){
            throw new ServiceException(RetEnum.RET_SERVICE_LOGINUSERNAME_ERROR);
        }

        // 确认用户名不能重复
        LambdaQueryWrapper<MchInfo> usernameQueryWrapper = record.lambda().eq(MchInfo::getLoginUserName, record.getLoginUserName());
        if(record.getMchId() != null) usernameQueryWrapper.ne(MchInfo::getMchId, record.getMchId());
        if(count(usernameQueryWrapper) > 0) throw new ServiceException(RetEnum.RET_MCH_USERNAME_USED);
    }

}

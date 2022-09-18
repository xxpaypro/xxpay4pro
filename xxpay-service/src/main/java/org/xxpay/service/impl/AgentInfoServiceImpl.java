package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.AgentInfoExample;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.core.entity.SysUser;
import org.xxpay.core.service.*;
import org.xxpay.service.dao.mapper.AgentInfoMapper;
import org.xxpay.service.dao.mapper.MchInfoMapper;
import org.xxpay.service.dao.mapper.SysUserMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 2018/4/27
 * @description:
 */
@Service
public class AgentInfoServiceImpl extends ServiceImpl<AgentInfoMapper, AgentInfo> implements IAgentInfoService {

    @Autowired
    private AgentInfoMapper agentInfoMapper;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private IFeeScaleService feeScaleService;

    @Autowired
    private ISysService sysService;

    @Autowired
    private SysUserMapper sysUserMapper;



    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    @Override
    public int add(AgentInfo agentInfo) {
        // 插入代理商基本信息
        agentInfoMapper.insertSelective(agentInfo);

        accountBalanceService.initAccount(MchConstant.INFO_TYPE_AGENT, agentInfo.getAgentId(), agentInfo.getAgentName(),
                MchConstant.ACCOUNT_TYPE_BALANCE, MchConstant.ACCOUNT_TYPE_SECURITY_MONEY);

        //2. 插入商户的 登录认证表（代理商主体超管用户）
        SysUser agentSU = new SysUser();
        agentSU.setNickName(agentInfo.getAgentName()); //用户昵称
        agentSU.setLoginUserName(agentInfo.getLoginUserName()); //登录名称
        agentSU.setEmail(agentInfo.getEmail());  //登录邮箱
        agentSU.setMobile(agentInfo.getMobile());  //登录手机号
        agentSU.setLoginPassword(SpringSecurityUtil.generateSSPassword(MchConstant.MCH_DEFAULT_PASSWORD));  //登录默认密码
        agentSU.setStatus(MchConstant.PUB_YES);  //状态, 默认登录状态可用
        agentSU.setIsSuperAdmin(MchConstant.PUB_YES);  //是否超管（拥有当前角色的所有权限） ： 是
        agentSU.setBelongInfoType(MchConstant.INFO_TYPE_AGENT);  //所属类型： 代理商
        agentSU.setBelongInfoId(agentInfo.getAgentId()); //agentId
        agentSU.setSex(MchConstant.SEX_UNKNOWN); //性别 未知

        JWTBaseUser jwtBaseUser = SpringSecurityUtil.getCurrentJWTUser();  //获取当前登录用户信息
        agentSU.setCreateUserId(jwtBaseUser.getUserId()); //创建人，  在context中获取
        int updateRow = sysService.add(agentSU);

        if(updateRow <= 0){
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        return 1;
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int updateAgent(AgentInfo agentInfo, SysUser agentUser) {

        //更新商户的登录信息
        if(agentUser != null){
            LambdaUpdateWrapper<SysUser> lambda = new LambdaUpdateWrapper();
            lambda.eq(SysUser::getBelongInfoId, agentInfo.getAgentId());
            lambda.eq(SysUser::getBelongInfoType, MchConstant.INFO_TYPE_AGENT);
            lambda.eq(SysUser::getIsSuperAdmin, MchConstant.PUB_YES); //代理商本身
            sysUserMapper.update(agentUser, lambda);
        }

        return agentInfoMapper.updateByPrimaryKeySelective(agentInfo);
    }

    @Override
    public AgentInfo find(AgentInfo agentInfo) {
        AgentInfoExample example = new AgentInfoExample();
        AgentInfoExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentInfo);
        List<AgentInfo> agentInfoList = agentInfoMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(agentInfoList)) return null;
        return agentInfoList.get(0);
    }

    @Override
    public AgentInfo findByLoginName(String loginName) {
        if(StringUtils.isBlank(loginName)) return null;
        AgentInfo agentInfo;
        if(StrUtil.checkEmail(loginName)) {
            agentInfo = findByEmail(loginName);
            if(agentInfo != null) return agentInfo;
        }
        if(StrUtil.checkMobileNumber(loginName)) {
            agentInfo = findByMobile(Long.parseLong(loginName));
            if(agentInfo != null) return agentInfo;
        }
        if(NumberUtils.isDigits(loginName)) {
            agentInfo = findByAgentId(Long.parseLong(loginName));
            if(agentInfo != null) return agentInfo;
        }
        agentInfo = findByUserName(loginName);
        return agentInfo;
    }

    @Override
    public AgentInfo findByAgentId(Long agentId) {
        return agentInfoMapper.selectByPrimaryKey(agentId);
    }

    @Override
    public AgentInfo findByUserName(String userName) {
        AgentInfo agentInfo = new AgentInfo();
//        agentInfo.setUserName(userName);
        return find(agentInfo);
    }

    @Override
    public AgentInfo findByMobile(Long mobile) {
        AgentInfo agentInfo = new AgentInfo();
//        agentInfo.setMobile(mobile);
        return find(agentInfo);
    }

    @Override
    public AgentInfo findByEmail(String email) {
        AgentInfo agentInfo = new AgentInfo();
//        agentInfo.setEmail(email);
        return find(agentInfo);
    }

    @Override
    public List<AgentInfo> select(int offset, int limit, AgentInfo agentInfo) {
        AgentInfoExample example = new AgentInfoExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        AgentInfoExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentInfo);
        return agentInfoMapper.selectByExample(example);
    }

    @Override
    public Long count(AgentInfo agentInfo) {
        AgentInfoExample example = new AgentInfoExample();
        AgentInfoExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentInfo);
        return agentInfoMapper.countByExample(example);
    }

    @Override
    public Map count4Agent() {
        Map param = new HashMap<>();
        return agentInfoMapper.count4Agent(param);
    }

    @Override
    public List<AgentInfo> selectAgentsProfitRate(Long agentId) {
        return agentInfoMapper.selectAgentsProfitRate(agentId);
    }

    @Override
    public Long countByAgentLevel(Date createTimeStart, Date createTimeEnd, Integer agentLevel, Long isvId, Long agentPid) {
        AgentInfoExample example = new AgentInfoExample();
        AgentInfoExample.Criteria criteria = example.createCriteria();

        if(createTimeStart != null){
            criteria.andCreateTimeGreaterThanOrEqualTo(createTimeStart);
        }

        if(createTimeEnd != null){
            criteria.andCreateTimeLessThanOrEqualTo(createTimeEnd);
        }

        if(agentLevel != null) criteria.andAgentLevelEqualTo(agentLevel);
        if(isvId != null) criteria.andIsvIdEqualTo(isvId);
        if(agentPid != null) criteria.andPidEqualTo(agentPid);


        return agentInfoMapper.countByExample(example);
    }

    @Override
    public List<Long> queryAllSubAgentIds(Long currentAgentId){
        return agentInfoMapper.queryAllSubAgentIds(currentAgentId);
    }

    @Override
    public List<AgentInfo> queryAllSubAgentBaseInfo(Long currentAgentId){
        return agentInfoMapper.queryAllSubAgentBaseInfo(currentAgentId);
    }

    @Override
    public Long countByAgentIdAndTime(Date createTimeStart, Date createTimeEnd, List<Long> agentIdList){

        AgentInfoExample exa = new AgentInfoExample();
        AgentInfoExample.Criteria c = exa.createCriteria();

        c.andAgentIdIn(agentIdList);
        if(createTimeStart != null) c.andCreateTimeGreaterThanOrEqualTo(createTimeStart);
        if(createTimeEnd != null) c.andCreateTimeLessThanOrEqualTo(createTimeEnd);
        return agentInfoMapper.countByExample(exa);
    }

    @Override
    public BigDecimal selectSubAgentsMaxProfitRate(Long agentId) {
        return baseMapper.selectSubAgentsMaxProfitRate(agentId);
    }


    void setCriteria(AgentInfoExample.Criteria criteria, AgentInfo agentInfo) {
        if(agentInfo != null) {
            if(agentInfo.getAgentId() != null) criteria.andAgentIdEqualTo(agentInfo.getAgentId());
//            if(agentInfo.getEmail() != null) criteria.andEmailEqualTo(agentInfo.getEmail());
//            if(agentInfo.getMobile() != null) criteria.andMobileEqualTo(agentInfo.getMobile());
//            if(agentInfo.getUserName() != null) criteria.andUserNameEqualTo(agentInfo.getUserName());
            if(agentInfo.getStatus() != null && agentInfo.getStatus().byteValue() != -99) criteria.andStatusEqualTo(agentInfo.getStatus());
            if(agentInfo.getPid() != null) criteria.andPidEqualTo(agentInfo.getPid());
            if(agentInfo.getAgentLevel() != null && agentInfo.getAgentLevel().byteValue() != -99) criteria.andAgentLevelEqualTo(agentInfo.getAgentLevel());
            if(agentInfo.getIsvId() != null) criteria.andIsvIdEqualTo(agentInfo.getIsvId());
            if(StringUtils.isNotEmpty(agentInfo.getAgentName())) criteria.andAgentNameLike("%"+agentInfo.getAgentName()+"%");

            if(agentInfo.getPsVal("statusIn") != null){
            	criteria.andStatusIn( (List<Byte>) agentInfo.getPsVal("statusIn"));
            }
        }
    }

}

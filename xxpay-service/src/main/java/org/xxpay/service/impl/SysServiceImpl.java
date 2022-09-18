package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import org.xxpay.core.entity.*;
import org.xxpay.core.service.ISysService;
import org.xxpay.service.dao.mapper.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/1/2
 * @description:
 */
@Service
public class SysServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private MchAppConfigMapper mchAppConfigMapper;

    @Override
    public int add(SysUser sysUser) {
        int count = baseMapper.insertSelective(sysUser);
        if (count !=1) return 0;
        return 1;
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int addSysUserAndAppconfig(SysUser sysUser) {
        int count = sysUserMapper.insertSelective(sysUser);
        if (count != 1) return 0;

        //插入用户app配置
        MchAppConfig appConfig = new MchAppConfig();
        appConfig.setUserId(sysUser.getUserId());
        appConfig.setAppPush(MchConstant.PUB_YES);
        appConfig.setAppVoice(MchConstant.PUB_YES);
        count = mchAppConfigMapper.insert(appConfig);
        if (count !=1) return 0;

        return 1;
    }

    @Override
    public SysUser findByUserMobile(String mobile) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<SysUser> sysUserList = sysUserMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(sysUserList)) return null;
        return sysUserList.get(0);
    }

    @Override
    public SysUser findByUserEmail(String email) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(email);
        List<SysUser> sysUserList = sysUserMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(sysUserList)) return null;
        return sysUserList.get(0);
    }

    @Override
    public int update(SysUser sysUser) {
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public SysUser findByUserId(Long userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public SysUser findByUserId(byte belongInfoType, Long belongInfoId, Long userId) {

        SysUserExample exa = new SysUserExample();
        exa.createCriteria().andUserIdEqualTo(userId).andBelongInfoIdEqualTo(belongInfoId)
                .andBelongInfoTypeEqualTo(belongInfoType);
        List<SysUser> result = this.sysUserMapper.selectByExample(exa);
        if (result == null || result.isEmpty()) return null;
        return result.get(0);
    }

    @Override
    public SysUser login(byte belongInfoType, Long belongInfoId, String loginUsername) {

        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andLoginUserNameEqualTo(loginUsername).andBelongInfoIdEqualTo(belongInfoId).andBelongInfoTypeEqualTo(belongInfoType);
        List<SysUser> sysUserList = sysUserMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(sysUserList)) return null;
        return sysUserList.get(0);
    }


    @Override
    public SysUser findByUserName(String userName) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andLoginUserNameEqualTo(userName);
        List<SysUser> sysUserList = sysUserMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(sysUserList)) return null;
        return sysUserList.get(0);
    }

    @Override
    public List<SysUser> select(int offset, int limit, SysUser sysUser) {
        SysUserExample example = new SysUserExample();
        example.setOffset(offset);
        example.setLimit(limit);
        example.setOrderByClause("createTime DESC");
        SysUserExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, sysUser);
        return sysUserMapper.selectByExample(example);
    }

    @Override
    public Long count(SysUser sysUser) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, sysUser);
        return sysUserMapper.countByExample(example);
    }

    @Override
    public Integer batchDelete(List<Long> userIds) {
        for (Long userId : userIds) {
            sysUserMapper.deleteByPrimaryKey(userId);
        }
        return userIds.size();
    }

    @Override
    public Integer batchDelete(byte belongInfoType, Long belongInfoId, List<Long> userIds) {
        for (Long userId : userIds) {

            SysUserExample exa = new SysUserExample();
            exa.createCriteria().andUserIdEqualTo(userId).andBelongInfoIdEqualTo(belongInfoId)
                    .andBelongInfoTypeEqualTo(belongInfoType);
            int row = sysUserMapper.deleteByExample(exa);

            //删除成功，将用户角色对应表删除
            if (row > 0) {
                SysUserRoleExample relaExa = new SysUserRoleExample();
                relaExa.createCriteria().andUserIdEqualTo(userId);
                sysUserRoleMapper.deleteByExample(relaExa);
            }

        }
        return userIds.size();
    }

    @Override
    public List<SysResource> selectAllResource(Byte system) {
        SysResourceExample example = new SysResourceExample();
        example.setOrderByClause("orderNum asc");
        SysResourceExample.Criteria criteria = example.createCriteria();
        if (system != null) criteria.andSystemEqualTo(system);
        criteria.andStatusEqualTo(MchConstant.PUB_YES);
        return sysResourceMapper.selectByExample(example);
    }

    @Override
    public List<SysResource> selectResourceByUserId(Long userId) {
        return sysResourceMapper.selectResourceByUserId(userId);
    }

    @Override
    public SysResource findResourceById(Long resourceId) {
        return sysResourceMapper.selectByPrimaryKey(resourceId);
    }

    @Override
    public int addResource(SysResource sysResource) {
        return sysResourceMapper.insertSelective(sysResource);
    }

    @Override
    public int updateResource(SysResource sysResource) {
        return sysResourceMapper.updateByPrimaryKeySelective(sysResource);
    }

    @Override
    public int batchDeleteResource(List<Long> resourceIds) {
        if (CollectionUtils.isEmpty(resourceIds)) return 0;
        int deleteCount = 0;
        for (Long resourceId : resourceIds) {
            deleteCount = deleteCount + sysResourceMapper.deleteByPrimaryKey(resourceId);
        }
        return deleteCount;
    }

    @Override
    public int addRole(SysRole sysRole) {
        return sysRoleMapper.insertSelective(sysRole);
    }

    @Override
    public int updateRole(SysRole sysRole) {
        return sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public int batchDeleteRole(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) return 0;
        int deleteCount = 0;
        for (Long roleId : roleIds) {
            deleteCount = deleteCount + sysRoleMapper.deleteByPrimaryKey(roleId);
        }
        return deleteCount;
    }

    @Override
    public int batchDeleteRole(byte belongInfoType, Long belongInfoId, List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) return 0;

        SysUserRoleExample queryExa = new SysUserRoleExample();
        queryExa.createCriteria().andRoleIdIn(roleIds);
        int hasRow = sysUserRoleMapper.countByExample(queryExa);
        if (hasRow > 0) throw new ServiceException(RetEnum.RET_SERVICE_DEL_EXISTS_USER_ROLE);

        int deleteCount = 0;
        for (Long roleId : roleIds) {

            SysRoleExample exa = new SysRoleExample();
            exa.createCriteria().andRoleIdEqualTo(roleId).andBelongInfoTypeEqualTo(belongInfoType).andBelongInfoIdEqualTo(belongInfoId);

            deleteCount = deleteCount + sysRoleMapper.deleteByExample(exa);
        }
        return deleteCount;
    }


    @Override
    public SysRole findRoleById(Long roleId) {
        return sysRoleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public SysRole findRoleById(byte belongInfoType, Long belongInfoId, Long roleId) {

        SysRoleExample exa = new SysRoleExample();
        exa.createCriteria().andBelongInfoIdEqualTo(belongInfoId).andBelongInfoTypeEqualTo(belongInfoType)
                .andRoleIdEqualTo(roleId);
        List<SysRole> result = sysRoleMapper.selectByExample(exa);
        if (result == null || result.isEmpty()) return null;
        return result.get(0);
    }


    @Override
    public List<SysRole> selectRole(int offset, int limit, SysRole sysRole) {
        SysRoleExample example = new SysRoleExample();
        example.setLimit(limit);
        example.setOffset(offset);
        example.setOrderByClause("createTime desc");
        SysRoleExample.Criteria criteria = example.createCriteria();
        setCriteriaRole(criteria, sysRole);
        return sysRoleMapper.selectByExample(example);
    }

    @Override
    public List<SysRole> selectAllRole() {
        SysRoleExample example = new SysRoleExample();
        example.setOrderByClause("createTime desc");
        return sysRoleMapper.selectByExample(example);
    }

    @Override
    public List<SysRole> selectAllRole(byte belongInfoType, Long belongInfoId) {

        SysRoleExample example = new SysRoleExample();
        example.createCriteria().andBelongInfoIdEqualTo(belongInfoId).andBelongInfoTypeEqualTo(belongInfoType);
        example.setOrderByClause("createTime desc");
        return sysRoleMapper.selectByExample(example);

    }


    @Override
    public Integer countRole(SysRole sysRole) {
        SysRoleExample example = new SysRoleExample();
        SysRoleExample.Criteria criteria = example.createCriteria();
        setCriteriaRole(criteria, sysRole);
        return sysRoleMapper.countByExample(example);
    }

    @Override
    public List<SysPermission> selectPermissionByRoleId(Long roleId) {
        SysPermissionExample example = new SysPermissionExample();
        SysPermissionExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        return sysPermissionMapper.selectByExample(example);
    }

    @Override
    public List<SysUserRole> selectUserRoleByUserId(Long userId) {
        SysUserRoleExample example = new SysUserRoleExample();
        SysUserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return sysUserRoleMapper.selectByExample(example);
    }

    @Override
    public List<SysResource> selectResource(int offset, int limit, SysResource sysResource) {
        SysResourceExample example = new SysResourceExample();
        example.setLimit(limit);
        example.setOffset(offset);
        example.setOrderByClause("createTime desc");
        SysResourceExample.Criteria criteria = example.createCriteria();
        setCriteriaResource(criteria, sysResource);
        return sysResourceMapper.selectByExample(example);
    }

    @Override
    public Integer countResource(SysResource sysResource) {
        SysResourceExample example = new SysResourceExample();
        SysResourceExample.Criteria criteria = example.createCriteria();
        setCriteriaResource(criteria, sysResource);
        return sysResourceMapper.countByExample(example);
    }

    // 保证事务
    @Override
    public void savePermission(Long roleId, List<Long> resourceIdList) {
        // 先删除该角色所有资源
        SysPermissionExample example = new SysPermissionExample();
        SysPermissionExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        sysPermissionMapper.deleteByExample(example);
        // 保存该角色新的资源
        for (Long resourceId : resourceIdList) {
            SysPermission sysPermission = new SysPermission();
            sysPermission.setRoleId(roleId);
            sysPermission.setResourceId(resourceId);
            sysPermission.setCreateTime(new Date());
            sysPermission.setUpdateTime(new Date());
            sysPermissionMapper.insert(sysPermission);
        }
    }

    @Override
    public void saveUserRole(Long userId, List<Long> roleIds) {
        // 先删除该用户的所有角色
        SysUserRoleExample example = new SysUserRoleExample();
        SysUserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        sysUserRoleMapper.deleteByExample(example);
        // 保存该用户的新角色
        for (Long roleId : roleIds) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            sysUserRole.setCreateTime(new Date());
            sysUserRole.setUpdateTime(new Date());
            sysUserRoleMapper.insert(sysUserRole);
        }
    }

    void setCriteria(SysUserExample.Criteria criteria, SysUser sysUser) {
        if (sysUser != null) {
            if (sysUser.getUserId() != null) criteria.andUserIdEqualTo(sysUser.getUserId());
            if (sysUser.getLoginUserName() != null) criteria.andLoginUserNameLike("%" + sysUser.getLoginUserName() + "%");
            if (sysUser.getNickName() != null) criteria.andNickNameLike("%" + sysUser.getNickName() + "%");
            if (sysUser.getStoreId() != null) criteria.andStoreIdEqualTo(sysUser.getStoreId());
            if (sysUser.getStatus() != null && sysUser.getStatus().byteValue() != -99)
                criteria.andStatusEqualTo(sysUser.getStatus());
            if (sysUser.getBelongInfoId() != null) criteria.andBelongInfoIdEqualTo(sysUser.getBelongInfoId());
            if (sysUser.getBelongInfoType() != null) criteria.andBelongInfoTypeEqualTo(sysUser.getBelongInfoType());
            if(sysUser.getIsSuperAdmin() != null) criteria.andIsSuperAdminEqualTo(sysUser.getIsSuperAdmin());
        }
    }

    void setCriteriaRole(SysRoleExample.Criteria criteria, SysRole sysRole) {
        if (sysRole != null) {
            if (sysRole.getRoleName() != null) criteria.andRoleNameLike("%" + sysRole.getRoleName() + "%");
            if (sysRole.getBelongInfoId() != null) criteria.andBelongInfoIdEqualTo(sysRole.getBelongInfoId());
            if (sysRole.getBelongInfoType() != null) criteria.andBelongInfoTypeEqualTo(sysRole.getBelongInfoType());
        }
    }

    void setCriteriaResource(SysResourceExample.Criteria criteria, SysResource sysResource) {
        if (sysResource != null) {
            if (sysResource.getSystem() != null && sysResource.getSystem().byteValue() != -99)
                criteria.andSystemEqualTo(sysResource.getSystem());
            if (sysResource.getTitle() != null) criteria.andTitleLike("%" + sysResource.getTitle() + "%");
            if (sysResource.getStatus() != null && sysResource.getStatus().byteValue() != -99)
                criteria.andStatusEqualTo(sysResource.getStatus());
        }
    }


    @Override
    public List<String> getEffectivePerms(byte infoType, SysUser sysUser) {

        List<SysResource> sysResourceList;
        if (sysUser.getIsSuperAdmin() == MchConstant.PUB_YES ) {
            // 如果是 超管/商户/代理商 /服务商 登录,则得到所有权限资源
            sysResourceList = this.selectAllResource(infoType);
        } else {
            // 得到用户的权限资源
            sysResourceList = this.selectResourceByUserId(sysUser.getUserId());
        }

        List<String> result = new LinkedList<>();
        if (CollectionUtils.isNotEmpty(sysResourceList)) {
            for (SysResource sysResource : sysResourceList) {
                if (sysResource == null) continue;
                if (StringUtils.isBlank(sysResource.getPermName())) continue;
                result.add(sysResource.getPermName());
            }
        }

        return result;

    }

    @Override
    public SysUser findByMchIdAndMobile(byte belongInfoType, String mobile) {
        LambdaQueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().lambda();

        queryWrapper.eq(SysUser::getBelongInfoType, belongInfoType);
        queryWrapper.eq(SysUser::getMobile, mobile);
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public List<SysUser> subUserList(Long belongInfoId) {
        List<SysUser> list = sysUserMapper.selectList(new QueryWrapper<SysUser>().lambda()
        .eq(SysUser::getBelongInfoId, belongInfoId)
        .eq(SysUser::getStatus, MchConstant.PUB_YES));
        return list;
    }

    @Override
    public SysUser findByLoginStr(String loginStr, Byte belongInfoType){

        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(SysUser::getBelongInfoType, belongInfoType);

        if(StringUtils.isBlank(loginStr)) return null;

        if(StrUtil.checkEmail(loginStr)) { //根据邮箱查询
            lambdaQueryWrapper.eq(SysUser::getEmail, loginStr);
        }
        else if(StrUtil.checkMobileNumber(loginStr)) { //根据手机号登录
            lambdaQueryWrapper.eq(SysUser::getMobile, loginStr);
        }
        else if(NumberUtils.isDigits(loginStr)) {  //根据belongInfoId 登录
            lambdaQueryWrapper.eq(SysUser::getBelongInfoId, loginStr);
            lambdaQueryWrapper.eq(SysUser::getIsSuperAdmin, MchConstant.PUB_YES);
        }else{ //根据登录用户名查询
            lambdaQueryWrapper.eq(SysUser::getLoginUserName, loginStr);
        }
        return sysUserMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public boolean resetLoginPwd(Byte belongInfoType, Long belongInfoId){

        SysUser updateRecord = new SysUser();
        updateRecord.setLastPasswordResetTime(new Date());
        updateRecord.setLoginPassword(SpringSecurityUtil.generateSSPassword(MchConstant.MCH_DEFAULT_PASSWORD));  //重置为默认密码

        int updateRow = sysUserMapper.update(updateRecord, new UpdateWrapper<SysUser>()
        .lambda().eq(SysUser::getBelongInfoType, belongInfoType)
        .eq(SysUser::getBelongInfoId, belongInfoId)
        .eq(SysUser::getIsSuperAdmin, MchConstant.PUB_YES));

        return updateRow > 0;
    }

    @Override
    public boolean modifyPwdByUserId(Long userId, String rawPassword){

        SysUser updateRecord = new SysUser();
        updateRecord.setLastPasswordResetTime(new Date());
        updateRecord.setLoginPassword(SpringSecurityUtil.generateSSPassword(rawPassword));
        updateRecord.setUserId(userId);
        int updateRow = this.update(updateRecord);
        return updateRow > 0;
    }

    @Override
    public void startWork(Long userId){

        SysUser updateRecord = new SysUser();
        updateRecord.setWorkStartTime(new Date());
        updateRecord.setWorkStatus(MchConstant.PUB_YES);

        sysUserMapper.update(updateRecord,
                new UpdateWrapper<SysUser>().lambda()
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getWorkStatus, MchConstant.PUB_NO)  //未工作状态
        );
    }

}

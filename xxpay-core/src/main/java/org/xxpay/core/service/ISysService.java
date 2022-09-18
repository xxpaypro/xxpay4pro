package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/1/2
 * @description: 系统管理接口
 */
public interface ISysService extends IService<SysUser>{

    int add(SysUser sysUser);

    int update(SysUser sysUser);

    SysUser findByUserId(Long userId);

    SysUser findByUserId(byte belongInfoType, Long belongInfoId, Long userId);

    SysUser login(byte belongInfoType, Long belongInfoId, String loginUsername);

    SysUser findByUserName(String userName);

    List<SysUser> select(int offset, int limit, SysUser sysUser);

    Long count(SysUser sysUser);

    Integer batchDelete(List<Long> userIds);

    Integer batchDelete(byte belongInfoType, Long belongInfoId, List<Long> userIds);

    List<SysResource> selectAllResource(Byte system);

    List<SysResource> selectResourceByUserId(Long userId);

    List<SysResource> selectResource(int offset, int limit, SysResource sysResource);

    Integer countResource(SysResource sysResource);

    SysResource findResourceById(Long resourceId);

    int addResource(SysResource sysResource);

    int updateResource(SysResource sysResource);

    int batchDeleteResource(List<Long> resourceIds);

    int addRole(SysRole sysRole);

    int updateRole(SysRole sysRole);

    int batchDeleteRole(List<Long> roleIds);

    int batchDeleteRole(byte belongInfoType, Long belongInfoId, List<Long> roleIds);

    SysRole findRoleById(Long roleId);

    SysRole findRoleById(byte belongInfoType, Long belongInfoId, Long roleId);

    List<SysRole> selectRole(int offset, int limit, SysRole sysRole);

    List<SysRole> selectAllRole();

    List<SysRole> selectAllRole(byte belongInfoType, Long belongInfoId);

    Integer countRole(SysRole sysRole);

    List<SysPermission> selectPermissionByRoleId(Long roleId);

    List<SysUserRole> selectUserRoleByUserId(Long userId);

    /**
     * 保存角色拥有的资源
     * @param roleId
     * @param resourceIdList
     */
    void savePermission(Long roleId, List<Long> resourceIdList);

    /**
     * 保存用户拥有的角色
     * @param userId
     * @param roleIds
     */
    void saveUserRole(Long userId, List<Long> roleIds);


    /**
     * 获取有效的资源集合
     * @param infoType
     * @param sysUser
     * @return
     */
    List<String> getEffectivePerms(byte infoType, SysUser sysUser);

    /**
     * 根据商户ID和手机号获取商户操作员
     * @param belongInfoType
     * @param mobile
     * @return
     */
    SysUser findByMchIdAndMobile(byte belongInfoType, String mobile);

    /**
     * 查询所有可用员工
     * @return
     */
    List<SysUser> subUserList(Long belongInfoId);


    /** 根据登录串 & 系统查询,  自动判断登录类型 **/
    SysUser findByLoginStr(String loginStr, Byte belongInfoType);


    int addSysUserAndAppconfig(SysUser sysUser);

    SysUser findByUserMobile(String mobile);

    SysUser findByUserEmail(String email);

    /** 重置商户/代理商/服务商 超管密码 **/
    boolean resetLoginPwd(Byte belongInfoType, Long belongInfoId);

    /** 根据用户ID 更新密码  **/
    boolean modifyPwdByUserId(Long userId, String rawPassword);

    /** 更新操作员， 开始工作状态 **/
    void startWork(Long userId);
}

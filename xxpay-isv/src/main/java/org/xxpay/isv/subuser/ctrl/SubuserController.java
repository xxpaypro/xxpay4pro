package org.xxpay.isv.subuser.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.SysRole;
import org.xxpay.core.entity.SysUser;
import org.xxpay.core.entity.SysUserRole;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/subuser")
public class SubuserController extends BaseController {

    private final static MyLog _log = MyLog.getLog(SubuserController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询用户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {


        Long userId = getValLongRequired( "userId");
        SysUser querySysUser = rpcCommonService.rpcSysService.findByUserId(MchConstant.INFO_TYPE_ISV, getUser().getBelongInfoId(), userId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(querySysUser));

    }

    /**
     * 新增用户信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增操作员" )
    public ResponseEntity<?> add() {

        SysUser sysUser = getObject( SysUser.class);

        sysUser.setBelongInfoType(MchConstant.INFO_TYPE_ISV);
        sysUser.setBelongInfoId(getUser().getBelongInfoId());
        sysUser.setIsSuperAdmin(MchConstant.PUB_NO);

        // 判断密码
        if(!StrUtil.checkPassword(sysUser.getLoginPassword())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        // 判断用户名是否被使用
        SysUser querySysUser = rpcCommonService.rpcSysService.findByUserName(sysUser.getLoginUserName());
        if(querySysUser != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_USERNAME_USED));
        }
        //判断手机号是否被使用
        SysUser mobileByUser = rpcCommonService.rpcSysService.findByUserMobile(sysUser.getMobile());
        if (mobileByUser != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }
        //判断邮箱是否被使用
        SysUser emailByUser = rpcCommonService.rpcSysService.findByUserEmail(sysUser.getEmail());
        if (emailByUser != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = sysUser.getLoginPassword();
        sysUser.setLoginPassword(encoder.encode(rawPassword));
        sysUser.setLastPasswordResetTime(new Date());
        sysUser.setCreateUserId(getUser().getBelongInfoId());
        int count = rpcCommonService.rpcSysService.add(sysUser);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改用户信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改操作员" )
    public ResponseEntity<?> update() {

        SysUser sysUser = getObject( SysUser.class);

        SysUser dbRecord = rpcCommonService.rpcSysService.findByUserId(MchConstant.INFO_TYPE_ISV, getUser().getBelongInfoId(), sysUser.getUserId());
        if(dbRecord == null){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        //设置默认值，避免前端传值将数据篡改
        sysUser.setIsSuperAdmin(MchConstant.PUB_NO);
        sysUser.setBelongInfoType(MchConstant.INFO_TYPE_ISV);
        sysUser.setBelongInfoId(getUser().getBelongInfoId());

        if(StringUtils.isBlank(sysUser.getLoginPassword())) {
            sysUser.setLoginPassword(null);
        }else {
            // 判断密码
            if(!StrUtil.checkPassword(sysUser.getLoginPassword())) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
            }
            String rawPassword = sysUser.getLoginPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            sysUser.setLoginPassword(encoder.encode(rawPassword));
            sysUser.setLastPasswordResetTime(new Date());
        }

        // 判断用户名是否被使用
        String userName = sysUser.getLoginUserName();
        if(StringUtils.isNotEmpty(userName)){
            SysUser querySysUser = rpcCommonService.rpcSysService.findByUserName(userName);
            if(querySysUser != null && !querySysUser.getLoginUserName().equals(sysUser.getLoginUserName())) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_USERNAME_USED));
            }
        }

        int count = rpcCommonService.rpcSysService.update(sysUser);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        SysUser sysUser = getObject( SysUser.class);

        //默认查询当前系统的子操作员
        sysUser.setBelongInfoType(MchConstant.INFO_TYPE_ISV);
        sysUser.setBelongInfoId(getUser().getBelongInfoId());
        sysUser.setIsSuperAdmin(MchConstant.PUB_NO); //仅查询子操作员

        long count = rpcCommonService.rpcSysService.count(sysUser);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SysUser> sysUserList = rpcCommonService.rpcSysService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), sysUser);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(sysUserList, count));
    }

    /**
     * 删除用户信息
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除操作员" )
    public ResponseEntity<?> delete() {

        String userIds = getValStringRequired( "userIds");
        String[] ids = userIds.split(",");
        List<Long> uids = new LinkedList<>();
        for(String userId : ids) {
            if(NumberUtils.isDigits(userId)) uids.add(Long.parseLong(userId));
        }
        rpcCommonService.rpcSysService.batchDelete(MchConstant.INFO_TYPE_ISV, getUser().getBelongInfoId(), uids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 查看用户所有角色
     * @return
     */
    @RequestMapping("/user_role_view")
    @ResponseBody
    public ResponseEntity<?> viewPermission() {

        Long userId = getValLongRequired( "userId");

        SysUser dbRecord = rpcCommonService.rpcSysService.findByUserId(MchConstant.INFO_TYPE_ISV, getUser().getBelongInfoId(), userId);
        if(dbRecord == null){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        // 该用户分配的角色
        List<SysUserRole> sysUserRoleList = rpcCommonService.rpcSysService.selectUserRoleByUserId(userId);
        // 得到所有角色
        List<SysRole> sysRoleList = rpcCommonService.rpcSysService.selectAllRole(MchConstant.INFO_TYPE_ISV, getUser().getBelongInfoId());
        JSONArray array = new JSONArray();
        for(SysRole sysRole : sysRoleList) {
            JSONObject object = new JSONObject();
            object.put("title", sysRole.getRoleName());
            object.put("value", sysRole.getRoleId());
            object.put("disabled", false);
            object.put("data", new ArrayList<>());
            object.put("checked", false);
            // 设置是否被选中
            for(SysUserRole sysUserRole : sysUserRoleList) {
                if(sysRole.getRoleId().longValue() == sysUserRole.getRoleId().longValue()) {
                    object.put("checked", true);
                    break;
                }
            }
            array.add(object);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(array));
    }

    /**
     * 保存用户的角色
     * @return
     */
    @RequestMapping("/user_role_save")
    @ResponseBody
    @MethodLog( remark = "修改操作员角色" )
    public ResponseEntity<?> saveUserRole() {

        Long userId = getValLongRequired( "userId");
        String roleIds = getValStringRequired( "roleIds");
        String[] ids = roleIds.split(",");
        List<Long> rids = new LinkedList<>();
        for(String roleId : ids) {
            if(NumberUtils.isDigits(roleId)) rids.add(Long.parseLong(roleId));
        }
        rpcCommonService.rpcSysService.saveUserRole(userId, rids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}
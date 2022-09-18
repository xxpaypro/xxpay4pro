package org.xxpay.mch.subuser.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.JsonUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

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
        //查询员工信息
        SysUser querySysUser = rpcCommonService.rpcSysService.findByUserId(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId(), userId);
        JSONObject json = JsonUtil.getJSONObjectFromObj(querySysUser);
        //查询员工所在门店信息
        if (querySysUser.getStoreId() != null){
            MchStore mchStore = rpcCommonService.rpcMchStoreService.getById(querySysUser.getStoreId());
            json.put("storeName", mchStore.getStoreName());
        }
        //查询所属商户信息
        if (querySysUser.getBelongInfoId() != null) {
            MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(querySysUser.getBelongInfoId());
            json.put("mchName", mchInfo.getMchName());
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(json));

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

        sysUser.setBelongInfoType(MchConstant.INFO_TYPE_MCH);
        sysUser.setBelongInfoId(getUser().getBelongInfoId());
        sysUser.setIsSuperAdmin(MchConstant.PUB_NO); //仅查询子操作员

        // 判断密码
        if(!StrUtil.checkPassword(sysUser.getLoginPassword())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        // 判断用户名是否被使用
        SysUser querySysUser = rpcCommonService.rpcSysService.findByUserName(sysUser.getLoginUserName());
        if(querySysUser != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_USERNAME_USED));
        }        //判断手机号是否被使用
        SysUser mobileByUser = rpcCommonService.rpcSysService.findByUserMobile(sysUser.getMobile());
        if (mobileByUser != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }
        //判断邮箱是否被使用
        SysUser emailByUser = rpcCommonService.rpcSysService.findByUserEmail(sysUser.getEmail());
        if (emailByUser != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
        }
        //判断所选门店是否存在
        MchStore store = rpcCommonService.rpcMchStoreService.getById(sysUser.getStoreId());
        if (store == null) {
            _log.info("当前门店不存在！");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STORE_NOT_EXIST));
        }
        //判断所选门店是否为当前登录商户下的门店
        if (!store.getMchId().equals(getUser().getBelongInfoId())){
            _log.info("当前门店非当前商户门店！");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STORE_NOT_EXIST));
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = sysUser.getLoginPassword();
        sysUser.setLoginPassword(encoder.encode(rawPassword));
        sysUser.setLastPasswordResetTime(new Date());
        sysUser.setCreateUserId(getUser().getBelongInfoId());
        int count = rpcCommonService.rpcSysService.addSysUserAndAppconfig(sysUser);
        if(count != 1) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
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

        SysUser sysUser = getObject(SysUser.class);

        SysUser dbRecord = rpcCommonService.rpcSysService.findByUserId(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId(), sysUser.getUserId());
        if(dbRecord == null){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        //设置默认值，避免前端传值将数据篡改
        sysUser.setIsSuperAdmin(MchConstant.PUB_NO);
        sysUser.setBelongInfoType(MchConstant.INFO_TYPE_MCH);
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

    /**
     * 修改用户信息
     * @return
     */
    @RequestMapping("/updateUser")
    @ResponseBody
    public ResponseEntity<?> updateUser() {

        SysUser sysUser = getObject(SysUser.class);

        SysUser dbRecord = rpcCommonService.rpcSysService.findByUserId(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId(), sysUser.getUserId());
        if (dbRecord == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        if (getUser().getIsSuperAdmin() == MchConstant.PUB_YES ) {
            MchInfo info = new MchInfo();
            info.setMchId(getUser().getBelongInfoId());
            info.setMchName(sysUser.getNickName());
            rpcCommonService.rpcMchInfoService.updateById(info);
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
        sysUser.setBelongInfoType(MchConstant.INFO_TYPE_MCH);
        sysUser.setBelongInfoId(getUser().getBelongInfoId());
        sysUser.setIsSuperAdmin(MchConstant.PUB_NO); //仅查询子操作员

        long count = rpcCommonService.rpcSysService.count(sysUser);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SysUser> sysUserList = rpcCommonService.rpcSysService.select((getPageIndex() - 1) * getPageSize(true), getPageSize(true), sysUser);
        List<MchStore> storeList = rpcCommonService.rpcMchStoreService.list();
        LinkedList<Object> list = new LinkedList<>();
        //遍历user列表将storeName放入
        for (SysUser user : sysUserList) {
            JSONObject json = JsonUtil.getJSONObjectFromObj(user);
            json.put("storeName", "");
            for (MchStore store: storeList) {
                if (user.getStoreId() != null && user.getStoreId().equals(store.getStoreId())){
                    json.put("storeName", store.getStoreName());
                }
            }
            list.add(json);
        }

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(list, count));
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
        rpcCommonService.rpcSysService.batchDelete(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId(), uids);
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

        SysUser dbRecord = rpcCommonService.rpcSysService.findByUserId(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId(), userId);
        if(dbRecord == null){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        // 该用户分配的角色
        List<SysUserRole> sysUserRoleList = rpcCommonService.rpcSysService.selectUserRoleByUserId(userId);
        // 得到所有角色
        List<SysRole> sysRoleList = rpcCommonService.rpcSysService.selectAllRole(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId());
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

    /**
     * 查询可用店员列表
     */
    @RequestMapping("/subUserList")
    @ResponseBody
    public ResponseEntity<?> subUserList() {
        Long belongInfoId = getUser().getBelongInfoId();
        List<SysUser> list = rpcCommonService.rpcSysService.subUserList(belongInfoId);
        return ResponseEntity.ok(PageRes.buildSuccess(list));
    }

}
package org.xxpay.manage.sys.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.xxpay.core.common.vo.PermTreeBuilder;
import org.xxpay.core.entity.SysPermission;
import org.xxpay.core.entity.SysResource;
import org.xxpay.core.entity.SysRole;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/role")
public class SysRoleController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long roleId = getValLongRequired( "roleId");
        SysRole sysRole = rpcCommonService.rpcSysService.findRoleById(roleId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(sysRole));
    }


    /**
     * 新增
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增角色" )
    public ResponseEntity<?> add() {

        SysRole sysRole = getObject( SysRole.class);

        sysRole.setBelongInfoType(MchConstant.INFO_TYPE_PLAT);
        sysRole.setBelongInfoId(0L);

        int count = rpcCommonService.rpcSysService.addRole(sysRole);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改角色" )
    public ResponseEntity<?> update() {

        SysRole sysRole = getObject( SysRole.class);

        sysRole.setBelongInfoType(MchConstant.INFO_TYPE_PLAT);
        sysRole.setBelongInfoId(0L);
        int count = rpcCommonService.rpcSysService.updateRole(sysRole);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 列表
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        SysRole sysRole = getObject( SysRole.class);

        sysRole.setBelongInfoType(MchConstant.INFO_TYPE_PLAT);
        sysRole.setBelongInfoId(0L);

        int count = rpcCommonService.rpcSysService.countRole(sysRole);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SysRole> sysRoleList = rpcCommonService.rpcSysService.selectRole((getPageIndex() - 1) * getPageSize(), getPageSize(), sysRole);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(sysRoleList, count));
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除角色" )
    public ResponseEntity<?> delete() {

        String roleIds = getValStringRequired( "roleIds");
        String[] ids = roleIds.split(",");
        List<Long> rids = new LinkedList<>();
        for(String roleId : ids) {
            if(NumberUtils.isDigits(roleId)) rids.add(Long.parseLong(roleId));
        }
        rpcCommonService.rpcSysService.batchDeleteRole(rids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 所有角色列表
     * @param request
     * @return
     */
    @RequestMapping("/all")
    @ResponseBody
    public ResponseEntity<?> all() {
        List<SysRole> sysRoleList = rpcCommonService.rpcSysService.selectAllRole();
        return ResponseEntity.ok(XxPayResponse.buildSuccess(sysRoleList));
    }

    /**
     * 查看角色所有资源
     * @param request
     * @return
     */
    @RequestMapping("/permission_view")
    @ResponseBody
    public ResponseEntity<?> viewPermission() {

        Long roleId = getValLongRequired( "roleId");
        // 该角色对应的所有权限
        List<SysPermission> sysPermissionList = rpcCommonService.rpcSysService.selectPermissionByRoleId(roleId);
        // 得到系统下所有资源
        List<SysResource> sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.INFO_TYPE_PLAT);
        List<PermTreeBuilder.Node> nodeList = new LinkedList<>();
        for(SysResource sysResource : sysResourceList) {
            PermTreeBuilder.Node node = new PermTreeBuilder.Node();
            node.setResourceId(sysResource.getResourceId());
            node.setTitle(sysResource.getTitle());
            node.setValue(sysResource.getResourceId()+"");
            // 设置是否被选中
            for(SysPermission sysPermission : sysPermissionList) {
                if(sysResource.getResourceId().longValue() == sysPermission.getResourceId().longValue()) {
                    node.setChecked(true);
                    break;
                }
            }
            node.setParentId(sysResource.getParentId());
            nodeList.add(node);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(PermTreeBuilder.buildListTree(nodeList)));
    }

    /**
     * 保存角色的资源
     * @param request
     * @return
     */
    @RequestMapping("/permission_save")
    @ResponseBody
    @MethodLog( remark = "角色分配权限" )
    public ResponseEntity<?> savePermission() {

        Long roleId = getValLongRequired( "roleId");
        String resourceIds = getValStringRequired( "resourceIds");
        String[] ids = resourceIds.split(",");
        List<Long> rids = new LinkedList<>();
        for(String resourceId : ids) {
            if(NumberUtils.isDigits(resourceId)) rids.add(Long.parseLong(resourceId));
        }
        rpcCommonService.rpcSysService.savePermission(roleId, rids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}
package org.xxpay.manage.sys.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.MenuTreeBuilder;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.SysResource;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/resource")
public class SysResourceController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long resourceId = getValLongRequired( "resourceId");
        SysResource sysResource = rpcCommonService.rpcSysService.findResourceById(resourceId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(sysResource));
    }


    /**
     * 新增
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增系统菜单" )
    public ResponseEntity<?> add() {

        SysResource sysResource = getObject( SysResource.class);
        int count = rpcCommonService.rpcSysService.addResource(sysResource);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改系统菜单" )
    public ResponseEntity<?> update() {

        SysResource sysResource = getObject( SysResource.class);
        int count = rpcCommonService.rpcSysService.updateResource(sysResource);
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

        SysResource sysResource = getObject( SysResource.class);
        int count = rpcCommonService.rpcSysService.countResource(sysResource);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SysResource> sysResourceList = rpcCommonService.rpcSysService.selectResource((getPageIndex() - 1) * getPageSize(), getPageSize(), sysResource);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(sysResourceList, count));
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除系统菜单" )
    public ResponseEntity<?> delete() {

        String resourceIds = getValStringRequired( "resourceIds");
        String[] ids = resourceIds.split(",");
        List<Long> rids = new LinkedList<>();
        for(String resourceId : ids) {
            if(NumberUtils.isDigits(resourceId)) rids.add(Long.parseLong(resourceId));
        }
        rpcCommonService.rpcSysService.batchDeleteResource(rids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    @RequestMapping("/all")
    @ResponseBody
    public ResponseEntity<?> all() {
        List<SysResource> sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.INFO_TYPE_PLAT);
        List<MenuTreeBuilder.Node> nodeList = new LinkedList<>();
        for(SysResource sysResource : sysResourceList) {
            MenuTreeBuilder.Node node = new MenuTreeBuilder.Node();
            node.setResourceId(sysResource.getResourceId());
            node.setTitle(sysResource.getTitle());
            node.setParentId(sysResource.getParentId());
            nodeList.add(node);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(MenuTreeBuilder.buildTree(nodeList)));
    }

}
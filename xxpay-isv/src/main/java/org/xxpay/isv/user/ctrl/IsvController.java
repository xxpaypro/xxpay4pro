package org.xxpay.isv.user.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.MenuTreeBuilder;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyAES;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.entity.IsvInfo;
import org.xxpay.core.entity.IsvSettConfig;
import org.xxpay.core.entity.SysResource;
import org.xxpay.core.entity.SysUser;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;
import org.xxpay.isv.user.service.UserService;

import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/isv")
@PreAuthorize("hasRole('"+ MchConstant.ISV_ROLE_NORMAL+"')")
public class IsvController extends BaseController {

    private final static MyLog _log = MyLog.getLog(IsvController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询服务商信息
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {
        IsvInfo isvInfo = userService.findByIsvId(getUser().getBelongInfoId());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(isvInfo));
    }

    /**
     * 查询菜单
     * @return
     */
    @RequestMapping("/menu_get")
    public ResponseEntity<?> getMenu() {



        List<SysResource> sysResourceList = null;

        if(getUser().getIsSuperAdmin() == MchConstant.PUB_YES){ //查询所有菜单
            sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.INFO_TYPE_ISV);
        }else{
            sysResourceList = rpcCommonService.rpcSysService.selectResourceByUserId(getUser().getUserId());
        }

        List<MenuTreeBuilder.Node> nodeList = new LinkedList<>();
        for(SysResource sysResource : sysResourceList) {
            MenuTreeBuilder.Node node = new MenuTreeBuilder.Node();
            node.setResourceId(sysResource.getResourceId());
            node.setName(sysResource.getName());
            node.setTitle(sysResource.getTitle());
            if(StringUtils.isNotBlank(sysResource.getJump())) node.setJump(sysResource.getJump());
            if(StringUtils.isNotBlank(sysResource.getIcon())) node.setIcon(sysResource.getIcon());
            node.setParentId(sysResource.getParentId());
            nodeList.add(node);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(JSONArray.parseArray(MenuTreeBuilder.buildTree(nodeList))));
    }

    /**
     * 修改服务商信息
     * @return
     */
    @RequestMapping("/update")
    @MethodLog( remark = "修改服务商信息" )
    public ResponseEntity<?> update() {

        IsvInfo isvInfo = new IsvInfo();
        isvInfo.setIsvId(getUser().getBelongInfoId());
        isvInfo.setRemark(getValString("remark"));
        boolean isTrue = rpcCommonService.rpcIsvInfoService.updateById(isvInfo);
        if(!isTrue) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 修改登录密码
     * @return
     */
    @RequestMapping("/pwd_update")
    @MethodLog( remark = "修改登录密码" )
    public ResponseEntity<?> updatePassword() {

        String oldRawPassword = getValStringRequired( "oldPassword");  // 旧密码
        String newPassword = getValStringRequired( "password");  // 新密码

        //验证当前密码（就密码）是否正确
        if(!SpringSecurityUtil.passwordMatch(oldRawPassword, getUser().getLoginPassword())){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_OLDPASSWORD_NOT_MATCH));
        }

        //新密码入库
        if(!rpcCommonService.rpcSysService.modifyPwdByUserId(getUser().getUserId(), newPassword)){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }


    /**
     * 查询下级商户入驻地址
     * @return
     */
    @RequestMapping("/getRegisterUrl")
    public ResponseEntity<?> getRegisterUrl() {
    	
    	String agentIdAES = MyAES.getInstance().encrypt(getUser().getBelongInfoId()+"");
    	agentIdAES = URLEncoder.encode(agentIdAES);
    	
    	String url = mainConfig.getMchRegUrl() + "/aid=" + agentIdAES;
        return ResponseEntity.ok(XxPayResponse.buildSuccess(url));
    }

    /** 查询上一次结算周期的日期范围, 一般用于统计页面的查询 **/
    @RequestMapping("/getPrevSettDate")
    @ResponseBody
    public XxPayResponse getPrevSettDate() {

       IsvSettConfig config = rpcCommonService.rpcIsvSettConfigService.getById(getUser().getBelongInfoId());

       Date prevSettDate = config.getPrevSettDate();
       if(prevSettDate == null){ //无上次结算日， 统计无结果
           return XxPayResponse.buildSuccess();
       }

       String queryStartDate = "";
        if(MchConstant.ISV_SETT_DATE_TYPE_DAY == config.getSettDateType()) {  //结算周期类型： 固定天数
            queryStartDate = DateUtil.date2Str(DateUtil.addDay(prevSettDate, (0- config.getSettSetDay())), "yyyy-MM-dd");
        }else if(MchConstant.ISV_SETT_DATE_TYPE_MONTH == config.getSettDateType()){ //结算周期类型： 指定月的如期
            queryStartDate = DateUtil.date2Str(DateUtil.addMonth(prevSettDate, -1), "yyyy-MM-"+config.getSettSetDay());
        }

        JSONObject result = new JSONObject();
        result.put("queryStartDate", queryStartDate);
        result.put("queryEndDate", DateUtil.date2Str(DateUtil.addDay(prevSettDate, -1), "yyyy-MM-dd"));
        return XxPayResponse.buildSuccess(result);
    }



}
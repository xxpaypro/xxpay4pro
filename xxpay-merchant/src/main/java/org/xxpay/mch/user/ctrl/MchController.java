package org.xxpay.mch.user.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.MenuTreeBuilder;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;
import org.xxpay.mch.user.service.UserService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mch")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchController extends BaseController {

    private final static MyLog _log = MyLog.getLog(MchController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询商户信息
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {
        MchInfo mchInfo = userService.findByMchId(getUser().getBelongInfoId());
        mchInfo.setPrivateKey("--"); //隐藏商户秘钥
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchInfo));
    }

    /**
     * 查询商户秘钥信息
     * @return
     */
    @RequestMapping("/get_prikey")
    public ResponseEntity<?> getPrikey() {

        MchInfo mchInfo = userService.findByMchId(getUser().getBelongInfoId());

        String pwdOrCode = getValString( "pwdOrCode");
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchInfo.getPrivateKey()));
    }

    /**
     * 查询商户菜单
     * @return
     */
    @RequestMapping("/menu_get")
    public ResponseEntity<?> getMenu() {
        Byte mchType = 0;  //TODO 商户类型

        List<SysResource> sysResourceList = null;
        if(getUser().getIsSuperAdmin() == MchConstant.PUB_YES){ //查询所有
            sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.INFO_TYPE_MCH);
        }else{
            sysResourceList = rpcCommonService.rpcSysService.selectResourceByUserId(getUser().getUserId());
        }

        List<MenuTreeBuilder.Node> nodeList = new LinkedList<>();
        for(SysResource sysResource : sysResourceList) {
            // 判断是否显示该菜单,平台账户和私有账户显示的菜单可能不同
            // 用资源表中的property区分,该值为空都可见.否则对应商户类型,如1 表示平台账户可见, 1,2 表示平台账户和私有账户都可见
            boolean isShow = true;
            String property = sysResource.getProperty();
            if(StringUtils.isNotBlank(property)) {
                isShow = false;
                String[] propertys = property.split(",");
                for(String str : propertys) {
                    if(str.equalsIgnoreCase(mchType.toString())) {
                        isShow = true;
                        break;
                    }
                }
            }
            if(!isShow) continue;
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
     * 修改商户信息
     * @return
     */
    @RequestMapping("/update")
    @MethodLog( remark = "修改商户信息" )
    public ResponseEntity<?> update() {

        MchInfo mchInfo = new MchInfo();
        mchInfo.setMchId(getUser().getBelongInfoId());
        mchInfo.setRemark(getValString("remark"));
        int count = rpcCommonService.rpcMchInfoService.updateMch(mchInfo, null, null);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }


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
     * 修改私钥
     * @return
     */
    @RequestMapping("/key_update")
    @MethodLog( remark = "修改商户私钥" )
    public ResponseEntity<?> updateKey() {

        // 私钥
        String privateKey = getValStringRequired( "privateKey");
        
        String pwdOrCode = getValStringRequired( "pwdOrCode");
        MchInfo mchInfo = userService.findByMchId(getUser().getBelongInfoId());
        
        // 修改密钥
        mchInfo = new MchInfo();
        mchInfo.setMchId(getUser().getBelongInfoId());
        mchInfo.setPrivateKey(privateKey);
        int count = rpcCommonService.rpcMchInfoService.updateMch(mchInfo, null, null);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }
    
    /**
     * 查询当前用户信息
     * @return
     */
    @RequestMapping("/current")
    public ResponseEntity<?> current() {
        SysUser sysUser = rpcCommonService.rpcSysService.findByUserId(getUser().getUserId());

        JSONObject object = (JSONObject) JSONObject.toJSON(sysUser);

        //所属门店信息
        MchStore mchStore = rpcCommonService.rpcMchStoreService.getById(sysUser.getStoreId());
        object.put("storeName", mchStore == null ? "" : mchStore.getStoreName());  //所属门店名称

        //判断角色类型  superAdmin  = 商户超管，  storeManage = 店长 ， operator = 门店操作员
        if(getUser().getIsSuperAdmin() == MchConstant.PUB_YES){
            object.put("currentUserRoleType", "superAdmin");
        }else{

            if(true){  //TODO 判断是否店长角色， 目前无判断条件
                object.put("currentUserRoleType", "storeManage");
            }else{
                object.put("currentUserRoleType", "operator");
            }
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /** 查询当前商户广告 */
    @RequestMapping("/advert")
    public ResponseEntity<?> advert() {

        Byte showType = getValByte( "showType"); //显示位置

        //商户ID
        Long mchId = getUser().getBelongInfoId();

        //根据商户ID查询当前商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(mchId);
        if (mchInfo == null) {
            _log.info("商户不存在id={}", mchId);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }

        JSONArray result = rpcCommonService.rpcIsvAdvertConfigService.selectAdListByMch(showType, mchInfo);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }

    // ================================================================开始纳呈新增修改==============================================================

    /**
     * 查询商户费率
     * @return
     */
    @RequestMapping("/getMchFee")
    public ResponseEntity<?> getFeeScale() {
        FeeScale aliPayFeeScale = rpcCommonService.rpcFeeScaleService.findOne(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId(), MchConstant.FEE_SCALE_PTYPE_PAY, PayConstant.PAY_PRODUCT_ALIPAY_BAR);
        FeeScale wxFeeScale = rpcCommonService.rpcFeeScaleService.findOne(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId(), MchConstant.FEE_SCALE_PTYPE_PAY, PayConstant.PAY_PRODUCT_WX_BAR);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("aliPayMchFee", aliPayFeeScale != null ? aliPayFeeScale.getFee() : 0);
        resultMap.put("wxMchFee", wxFeeScale != null ? wxFeeScale.getFee() : 0);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(resultMap));
    }


    // ================================================================结束纳呈新增修改================================================================

}
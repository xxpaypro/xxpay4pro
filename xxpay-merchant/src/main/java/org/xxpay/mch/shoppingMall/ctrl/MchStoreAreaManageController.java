package org.xxpay.mch.shoppingMall.ctrl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchStoreAreaManage;
import org.xxpay.core.entity.MchWxauthInfo;
import org.xxpay.mch.common.ctrl.BaseController;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * <p>
 * 商户餐饮店区域管理表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mchStoreAreaManage")
public class MchStoreAreaManageController extends BaseController {

    private static final MyLog _log = MyLog.getLog(MchStoreAreaManageController.class);

    /**
     * 商品列表
     */
    @MethodLog( remark = "列表" )
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {
        MchStoreAreaManage mchStoreAreaManage = getObject( MchStoreAreaManage.class);
        //当前登录商户ID
        mchStoreAreaManage.setMchId(getUser().getBelongInfoId());
        IPage<MchStoreAreaManage> mchStoreAreaManageList = rpcCommonService.rpcMchStoreAreaManageService.list(mchStoreAreaManage, getIPage());
        return ResponseEntity.ok(PageRes.buildSuccess(mchStoreAreaManageList));
    }

    /**
     * 新增
     */
    @MethodLog(remark = "新增")
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {
        Integer idStart = getValIntegerRequired("idStart");
        Integer idEnd = getValIntegerRequired("idEnd");
        String areaName = getValStringRequired("areaName");
        Long storeId = getValLongRequired("storeId");

        // 校验起始ID和结束ID是否符合条件
        if (idStart <= 0|| idEnd <= 0 || idEnd < idStart) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_AREA_ID_ERROR));
        }
        // 查询当前商户所属服务商
        Long mchId = getUser().getBelongInfoId();
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);

        ArrayList<MchStoreAreaManage> list = new ArrayList<>();
        for (int i = idStart; i <= idEnd; i++) {
            MchStoreAreaManage manage = new MchStoreAreaManage();
            manage.setIsvId(mchInfo.getIsvId());
            manage.setMchId(mchId);
            manage.setAreaName(i + areaName);
            manage.setStoreId(storeId);
            list.add(manage);
        }
        boolean result = rpcCommonService.rpcMchStoreAreaManageService.saveBatch(list);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 新增单个
     */
    @MethodLog(remark = "新增单个")
    @RequestMapping("/addOne")
    @ResponseBody
    public ResponseEntity<?> addOne() {
        String areaName = getValStringRequired("areaName");
        Long storeId = getValLongRequired("storeId");
        // 查询当前商户所属服务商
        Long mchId = getUser().getBelongInfoId();
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        ArrayList<MchStoreAreaManage> list = new ArrayList<>();
        MchStoreAreaManage manage = new MchStoreAreaManage();
        manage.setIsvId(mchInfo.getIsvId());
        manage.setMchId(mchId);
        manage.setAreaName(areaName);
        manage.setStoreId(storeId);
        list.add(manage);
        boolean result = rpcCommonService.rpcMchStoreAreaManageService.saveBatch(list);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 删除
     */
    @MethodLog(remark = "删除")
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> delete() {
        String id = request.getParameter("id");
        boolean result = false;
        if (!StringUtils.isEmpty(id)) {
            result = rpcCommonService.rpcMchStoreAreaManageService.removeById(id);
        }
        String ids = request.getParameter("ids");
        if (!StringUtils.isEmpty(ids)) {
            String[] idStr = ids.split(",");
            ArrayList<String> idList = new ArrayList<String>();
            for (int i = 0; i < idStr.length; i++) {
                idList.add(idStr[i]);
            }
            result = rpcCommonService.rpcMchStoreAreaManageService.removeByIds(idList);
        }
        if (result) return ResponseEntity.ok(XxPayResponse.buildSuccess());
        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 生成点餐码
     */
    @MethodLog(remark = "生成点餐码")
    @RequestMapping("/create_wxacode")
    @ResponseBody
    public ResponseEntity<?> createWxacode() {
        Long id = getValLongRequired("id");
        Long mchId = getUser().getBelongInfoId();

        MchStoreAreaManage storeAreaManage = rpcCommonService.rpcMchStoreAreaManageService.getById(id);
        if (storeAreaManage == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        MchWxauthInfo mchWxauthInfo = rpcCommonService.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, MchConstant.MCH_INDUSTRY_TYPE_FOOD);
        if (mchWxauthInfo == null || StringUtils.isBlank(mchWxauthInfo.getPrefix())) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MINI_QRCODEJUMP_NOT_RELEASE));
        }

        //生成小程序码url
        String qrcodeUrl = mchWxauthInfo.getPrefix() + id;

        JSONObject data = new JSONObject();
        data.put("qrcodeUrl", qrcodeUrl);
        data.put("codeImgUrl", mainConfig.getMchApiUrl() +  "/payment/qrcode_img_get?url=" + URLEncoder.encode(qrcodeUrl) + "&widht=200&height=200");

        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }


}

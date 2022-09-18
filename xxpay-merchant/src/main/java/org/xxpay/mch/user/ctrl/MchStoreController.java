package org.xxpay.mch.user.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AddressUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.MchStore;
import org.xxpay.core.entity.MchStorePrinter;
import org.xxpay.core.entity.MchStoreSpeaker;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: pangxiaoyu
 * @date: 19/08/22
 * @description: 商户门店
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/store")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchStoreController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(MchStoreController.class);

    /**
     * 门店列表
     * @return
     */
    @MethodLog( remark = "门店列表" )
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list( Integer page, Integer limit) {

        MchStore mchStore = getObject( MchStore.class);
        if(mchStore == null) mchStore = new MchStore();
        //当前登录商户ID
        mchStore.setMchId(getUser().getBelongInfoId());
        IPage<MchStore> mchStoreList = rpcCommonService.rpcMchStoreService.list(mchStore, getIPage(true));
        return ResponseEntity.ok(PageRes.buildSuccess(mchStoreList));
    }

    /**
     * 查询可用门店列表
     */
    @RequestMapping("/storeList")
    @ResponseBody
    public ResponseEntity<?> storeList() {
        //当前商户下、状态为正常营业的门店
        List<MchStore> list = rpcCommonService.rpcMchStoreService.list(
                new QueryWrapper<MchStore>().lambda()
                .eq(MchStore::getMchId, getUser().getBelongInfoId())
                .eq(MchStore::getStatus, MchConstant.MCH_STORE_NORMAL)
        );

        return ResponseEntity.ok(PageRes.buildSuccess(list));
    }

    /**
     * 门店信息查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long storeId = getValLongRequired( "storeId");
        //查询符合条件的商户
        MchStore store = rpcCommonService.rpcMchStoreService.getOne(
                new QueryWrapper<MchStore>().lambda()
                .eq(MchStore::getMchId, getUser().getBelongInfoId())
                .eq(MchStore::getStoreId, storeId)
        );

        return ResponseEntity.ok(XxPayResponse.buildSuccess(store));
    }

    /**
     * 新增门店
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {

        MchStore mchStore = getObject(MchStore.class);
        MchStore store = rpcCommonService.rpcMchStoreService.getOne(new QueryWrapper<MchStore>().lambda()
                        .eq(MchStore::getStoreNo, mchStore.getStoreNo())
        );
        // 门店编号已存在
        if (store != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STORENO_USED));
        }
        mchStore.setStoreImgPath(StringUtils.isNotBlank(mchStore.getStoreImgPath()) ? mchStore.getStoreImgPath() : MchConstant.MCH_STORE_DEFAULT_AVATAR);
        mchStore.setMiniImgPath(StringUtils.isNotBlank(mchStore.getMiniImgPath()) ? mchStore.getMiniImgPath() : MchConstant.MCH_STORE_MINI_TOP_IMAGEPATH);
        // 退款密码不能为空
        if (StringUtils.isEmpty(mchStore.getRefundPassword())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_REFUNDS_PASSWORD_NULL));
        }
        checkAddress(mchStore);
        // 当前操作商户ID
        mchStore.setMchId(getUser().getBelongInfoId());
        // 退款密码加密
        mchStore.setRefundPassword(SpringSecurityUtil.generateSSPassword(mchStore.getRefundPassword()));
        boolean save = rpcCommonService.rpcMchStoreService.save(mchStore);
        if(save) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 门店信息修改
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        MchStore mchStore = getObject(MchStore.class);
        MchStore store = rpcCommonService.rpcMchStoreService.getById(mchStore.getStoreId());
        //当前门店是否存在
        if (store == null) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        //当前操作人ID与门店所属商户ID是否相同
        if (!getUser().getBelongInfoId().equals(store.getMchId())){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }
        checkAddress(mchStore);
        boolean update = rpcCommonService.rpcMchStoreService.updateById(mchStore);
        if(update) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 门店退款密码修改
     * @return
     */
    @RequestMapping("/updateRefund")
    @ResponseBody
    public ResponseEntity<?> updateRefund() {

        Long storeId = getValLongRequired("storeId");
        String refundPassword = getValStringRequired("refundPassword");
        MchStore store = rpcCommonService.rpcMchStoreService.getById(storeId);
        //当前门店是否存在
        if (store == null) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        //当前操作人ID与门店所属商户ID是否相同
        if (!getUser().getBelongInfoId().equals(store.getMchId())){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }
        // 判断密码
        if(!StrUtil.checkPassword(refundPassword)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }

        MchStore updateRecord = new MchStore();
        updateRecord.setStoreId(storeId);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        updateRecord.setRefundPassword(encoder.encode(refundPassword));

        boolean update = rpcCommonService.rpcMchStoreService.updateById(updateRecord);
        if(update) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 门店删除
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> delete() {

        Long storeId = getValLongRequired( "storeId");
        MchStore store = rpcCommonService.rpcMchStoreService.getById(storeId);
        //当前门店是否存在
        if (store == null) ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        //当前操作人ID与门店所属商户ID是否相同
        if (!getUser().getBelongInfoId().equals(store.getMchId())){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }
        //判断当前门店是否绑定设备，若绑定设备返回解除绑定
        MchStoreSpeaker speaker = rpcCommonService.rpcMchStoreSpeakerService.getById(storeId);
        if (speaker != null) {
            if (speaker.getStatus() == MchConstant.MCH_STORE_SPEAKER_STATUS_USED) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STORE_DEVICE_STATUS_BINDING));
            }
        }
        MchStorePrinter printer = rpcCommonService.rpcMchStorePrinterService.getById(storeId);
        if (printer != null) {
            if (printer.getStatus() == MchConstant.PUB_YES) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STORE_DEVICE_STATUS_BINDING));
            }
        }
        boolean remove = rpcCommonService.rpcMchStoreService.removeById(storeId);
        if(remove) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }


    public static void checkAddress(MchStore mchStore){
        // 判断点击了地图获取经纬度
        if (StringUtils.isEmpty(mchStore.getLat()) || StringUtils.isEmpty(mchStore.getLot())) {
            // 门店地址拼接
            String address = mchStore.getAreaInfo() + mchStore.getAddress();
            Map<String, BigDecimal> map = AddressUtil.getLatAndLngByAddress(address);
            mchStore.setLat(map.get("lat").toString());
            mchStore.setLot(map.get("lng").toString());
        }
    }

}

package org.xxpay.mch.shoppingMall.ctrl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchGoods;
import org.xxpay.core.entity.MchMiniConfig;
import org.xxpay.mch.common.ctrl.BaseController;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 商户小程序配置信息
 * </p>
 *
 * @author zx
 * @since 2020-08-17
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mini_config")
public class MchMiniConfigController extends BaseController {

    /**
     * 商城精品推荐配置
     * @return
     */
    @RequestMapping("/recommend")
    @ResponseBody
    public ResponseEntity<?> recommend() {

        Byte authFrom = getValByteRequired("authFrom");//1-点餐小程序  2-商城小程序

        MchMiniConfig mchMiniConfig = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, getUser().getBelongInfoId())
                .eq(MchMiniConfig::getConfigCode, "recommend")
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
        );

        if (mchMiniConfig == null || StringUtils.isBlank(mchMiniConfig.getValue())) {
            return ResponseEntity.ok(XxPayResponse.buildSuccess());
        }

        String value = mchMiniConfig.getValue();
        List goodsId = Arrays.asList(value);

        IPage<MchGoods> page = rpcCommonService.rpcMchGoodsService.page(getIPage(), new QueryWrapper<MchGoods>().lambda()
                .in(MchGoods::getGoodsId, goodsId)
        );

        return ResponseEntity.ok(PageRes.buildSuccess(page));
    }

    /**
     * 小程序售后申请原因列表
     * @return
     */
    @RequestMapping("/reason_list")
    @ResponseBody
    public ResponseEntity<?> reasonList() {

        Byte authFrom = getValByteRequired("authFrom");//1-点餐小程序  2-商城小程序
        String configCode = getValStringRequired("configCode");

        MchMiniConfig mchMiniConfig = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, getUser().getBelongInfoId())
                .eq(MchMiniConfig::getConfigCode, configCode)
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
        );

        if (mchMiniConfig == null || StringUtils.isBlank(mchMiniConfig.getValue())) {
            return ResponseEntity.ok(XxPayResponse.buildSuccess());
        }

        String value = mchMiniConfig.getValue();
        JSONArray jsonArray = JSONArray.parseArray(value);

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(jsonArray, jsonArray.size()));
    }

    /**
     * 小程序售后申请原因列表
     * @return
     */
    @RequestMapping("/reason_save")
    @ResponseBody
    public ResponseEntity<?> reasonSave() {

        Byte authFrom = getValByteRequired("authFrom");//1-点餐小程序  2-商城小程序
        String configValue = getValStringRequired("configValue");
        String configCode = getValStringRequired("configCode");
        String configName = getValStringRequired("configName");

        MchMiniConfig mchMiniConfig = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, getUser().getBelongInfoId())
                .eq(MchMiniConfig::getConfigCode, configCode)
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
        );

        //新添加的原因json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("configValue", configValue);
        jsonObject.put("authFrom", authFrom);

        JSONArray jsonArray = new JSONArray();
        //新增
        if (mchMiniConfig == null) {
            jsonObject.put("id", jsonArray.size() + 1);
            jsonArray.add(jsonObject);

            //保存
            MchMiniConfig record = new MchMiniConfig();
            record.setMchId(getUser().getBelongInfoId());
            record.setAuthFrom(authFrom);
            record.setConfigCode(configCode);
            record.setConfigName(configName);
            record.setValue(jsonArray.toJSONString());
            record.setStatus(MchConstant.PUB_YES);
            boolean save = rpcCommonService.rpcMchMiniConfigService.save(record);
            if (save) return ResponseEntity.ok(XxPayResponse.buildSuccess());
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }

        //追加
        String value = mchMiniConfig.getValue();
        jsonArray = JSONArray.parseArray(value);
        jsonObject.put("id", jsonArray.size() + 1);
        jsonArray.add(jsonObject);

        MchMiniConfig updateRecord = new MchMiniConfig();
        updateRecord.setConfigId(mchMiniConfig.getConfigId());
        updateRecord.setValue(jsonArray.toJSONString());
        boolean updateResult = rpcCommonService.rpcMchMiniConfigService.updateById(updateRecord);
        if (updateResult) return ResponseEntity.ok(XxPayResponse.buildSuccess());
        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 小程序售后申请原因列表
     * @return
     */
    @RequestMapping("/reason_update")
    @ResponseBody
    public ResponseEntity<?> reasonUpdate() {
        Byte authFrom = getValByteRequired("authFrom");//1-点餐小程序  2-商城小程序
        String configValue = getValStringRequired("configValue");
        String configCode = getValStringRequired("configCode");
        Integer id = getValInteger("id");

        MchMiniConfig mchMiniConfig = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, getUser().getBelongInfoId())
                .eq(MchMiniConfig::getConfigCode, configCode)
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
        );

        //修改
        JSONArray updateJsonArray = new JSONArray();
        String value = mchMiniConfig.getValue();
        JSONArray jsonArray = JSONArray.parseArray(value);
        for (Object object : jsonArray) {
            JSONObject reasonJson = (JSONObject) object;
            if (reasonJson.getIntValue("id") == id) {
                reasonJson.put("configValue", configValue);
            }
            updateJsonArray.add(reasonJson);
        }

        MchMiniConfig updateRecord = new MchMiniConfig();
        updateRecord.setConfigId(mchMiniConfig.getConfigId());
        updateRecord.setValue(updateJsonArray.toJSONString());
        boolean updateResult = rpcCommonService.rpcMchMiniConfigService.updateById(updateRecord);
        if (updateResult) return ResponseEntity.ok(XxPayResponse.buildSuccess());
        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 小程序配置信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> hotSearchGet() {

        Byte authFrom = getValByteRequired("authFrom");//1-点餐小程序  2-商城小程序
        String configCode = getValStringRequired("configCode");
        Long mchId = getValLongDefault("mchId", getUser().getBelongInfoId());

        MchMiniConfig mchMiniConfig = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, mchId)
                .eq(MchMiniConfig::getConfigCode, configCode)
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
        );

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchMiniConfig));
    }

    /**
     * 小程序保存配置信息
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResponseEntity<?> save() {

        MchMiniConfig mchMiniConfig = getObject(MchMiniConfig.class);
        mchMiniConfig.setMchId(getUser().getBelongInfoId());
        mchMiniConfig.setStatus(MchConstant.PUB_YES);

        MchMiniConfig dbRecord = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, getUser().getBelongInfoId())
                .eq(MchMiniConfig::getConfigCode, mchMiniConfig.getConfigCode())
                .eq(MchMiniConfig::getAuthFrom, mchMiniConfig.getAuthFrom())
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
        );

        boolean result;
        if (dbRecord == null) {
            result = rpcCommonService.rpcMchMiniConfigService.save(mchMiniConfig);
            if (result) return ResponseEntity.ok(XxPayResponse.buildSuccess());
        }

        mchMiniConfig.setConfigId(dbRecord.getConfigId());
        result = rpcCommonService.rpcMchMiniConfigService.updateById(mchMiniConfig);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 小程序可视化--发布配置信息
     * @return
     */
    @RequestMapping(value = "/visualable/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> visualableSave() {

        String componentList = getValStringRequired("componentList");
        String componentIndex = getValStringRequired("componentIndex");
        Byte authFrom = getValByteRequired("authFrom");

        int count = rpcCommonService.rpcMchMiniConfigService.visualableSaveOrUpdate(getUser().getBelongInfoId(),
                        componentList, componentIndex, authFrom, MchConstant.PUB_NO);

        if (count == 0) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 小程序可视化--保存草稿
     * @return
     */
    @RequestMapping(value = "/visualable/draft/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> visualableDraftSave() {

        String componentList = getValStringRequired("componentList");
        String componentIndex = getValStringRequired("componentIndex");
        Byte authFrom = getValByteRequired("authFrom");

        int count = rpcCommonService.rpcMchMiniConfigService.visualableSaveOrUpdate(getUser().getBelongInfoId(),
                componentList, componentIndex, authFrom, MchConstant.PUB_YES);

        if (count == 0) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 小程序可视化--获取草稿
     * @return
     */
    @RequestMapping("/visualable/get")
    @ResponseBody
    public ResponseEntity<?> visualableGet() {

        String componentIndex = getValStringRequired("componentIndex");
        Byte authFrom = getValByteRequired("authFrom");
        Byte isDraft = getValByteDefault("isDraft", MchConstant.PUB_YES);

        MchMiniConfig mchMiniConfig = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, getUser().getBelongInfoId())
                .eq(MchMiniConfig::getConfigCode, componentIndex)
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
                .eq(MchMiniConfig::getIsDraft, isDraft)
        );

        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchMiniConfig));
    }


}

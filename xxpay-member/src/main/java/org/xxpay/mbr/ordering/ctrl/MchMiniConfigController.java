package org.xxpay.mbr.ordering.ctrl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchGoods;
import org.xxpay.core.entity.MchMiniConfig;
import org.xxpay.core.entity.MchMiniVajra;
import org.xxpay.mbr.common.ctrl.BaseController;

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
     * 小程序配置，返回存储值
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Byte authFrom = getValByteRequired("authFrom");//1-点餐小程序  2-商城小程序
        String configCode = getValStringRequired("configCode");//配置键名

        MchMiniConfig mchMiniConfig = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, getUser().getMchId())
                .eq(MchMiniConfig::getConfigCode, configCode)
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
        );

        return ResponseEntity.ok(PageRes.buildSuccess(mchMiniConfig));
    }


    /**
     * 小程序金刚区配置
     * @return
     */
    @RequestMapping("/vajra")
    @ResponseBody
    public ResponseEntity<?> vajra() {
        Byte authFrom = getValByteRequired("authFrom");//1-点餐小程序  2-商城小程序

        List<MchMiniVajra> vajraList = rpcCommonService.rpcMchMiniVajraService.list(new QueryWrapper<MchMiniVajra>().lambda()
                .eq(MchMiniVajra::getMchId, getUser().getMchId())
                .eq(MchMiniVajra::getAuthFrom, authFrom)
                .eq(MchMiniVajra::getStatus, MchConstant.PUB_YES)
                .orderByAsc(MchMiniVajra::getSortNum)
        );
        return ResponseEntity.ok(XxPayResponse.buildSuccess(vajraList));
    }


    /**
     * 商城热搜列表
     * @return
     */
    @RequestMapping("/hot_search_list")
    @ResponseBody
    public ResponseEntity<?> hotSearchList() {

        Byte authFrom = getValByteRequired("authFrom");//1-点餐小程序  2-商城小程序

        MchMiniConfig dbRecord = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, getUser().getMchId())
                .eq(MchMiniConfig::getConfigCode, "hotSearch")
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
        );

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(dbRecord));
    }

    /**
     * 退换货配置信息
     * @return
     */
    @RequestMapping("/after_sale_info")
    @ResponseBody
    public ResponseEntity<?> afterSaleInfo() {

        Byte authFrom = getValByteRequired("authFrom");//1-点餐小程序  2-商城小程序
        String configCode = getValStringRequired("configCode");

        MchMiniConfig mchMiniConfig = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, getUser().getMchId())
                .eq(MchMiniConfig::getConfigCode, configCode)
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
        );

        if (mchMiniConfig == null || StringUtils.isBlank(mchMiniConfig.getValue())) {
            return ResponseEntity.ok(XxPayResponse.buildSuccess());
        }

        //修改
        if (configCode.equals("reason")) {
            String value = mchMiniConfig.getValue();
            JSONArray jsonArray = JSONArray.parseArray(value);
            return ResponseEntity.ok(XxPayPageRes.buildSuccess(jsonArray));
        }

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(JSON.parse(mchMiniConfig.getValue())));
    }


    /**
     * 获取小程序可视化配置
     * @return
     */
    @RequestMapping("/getVisualable")
    @ResponseBody
    public ResponseEntity<?> getVisualable() {

        Byte authFrom = getValByteRequired("authFrom");//1-点餐小程序  2-商城小程序
        String configCode = getValStringRequired("componentIndex");//配置键名

        MchMiniConfig mchMiniConfig = rpcCommonService.rpcMchMiniConfigService.getOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, getUser().getMchId())
                .eq(MchMiniConfig::getConfigCode, configCode)
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
                .eq(MchMiniConfig::getIsDraft, MchConstant.PUB_NO)
        );

        return ResponseEntity.ok(PageRes.buildSuccess(mchMiniConfig));
    }

}

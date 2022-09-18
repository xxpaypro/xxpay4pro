package org.xxpay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.*;
import org.xxpay.service.dao.mapper.MchTradeOrderAfterSaleMapper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商城订单售后表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-19
 */
@Service
public class MchTradeOrderAfterSaleServiceImpl extends ServiceImpl<MchTradeOrderAfterSaleMapper, MchTradeOrderAfterSale> implements IMchTradeOrderAfterSaleService {

    @Autowired
    public IMchGoodsService mchGoodsService;

    @Autowired
    public IMchGoodsPropsService mchGoodsPropsService;

    @Autowired
    public IMchReceiveAddressService mchReceiveAddressService;

    @Autowired
    public IMchTradeOrderService mchTradeOrderService;

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public boolean apply(MchTradeOrderAfterSale afterSale) {

        //校验参数是否为空
        if (StringUtils.isBlank(afterSale.getGoodsDesc()) || afterSale.getAuthFrom() == null
                || afterSale.getAfterSaleType() == null || afterSale.getReason() == null || afterSale.getTradeOrderId() == null) {
            throw new ServiceException(RetEnum.RET_COMM_PARAM_ERROR);
        }

        //查询订单
        MchTradeOrder tradeOrder = mchTradeOrderService.getById(afterSale.getTradeOrderId());
        if (tradeOrder == null) throw new ServiceException(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST);
        JSONArray tradeOrderGoodsArray = JSONArray.parseArray(tradeOrder.getGoodsDesc());//订单中的商品信息

        //解析商品信息，goodsDesc格式：[{"goodsId": "1000", "goodsNum": "2", "goodsProps": "1,2"}]
        String goodsDesc = afterSale.getGoodsDesc();
        //申请售后的商品
        JSONArray jsonArray = JSONArray.parseArray(goodsDesc);
        //更新商品信息到申请售后的商品载体
        JSONArray goodsJSONArray = new JSONArray();
        //计算申请售后商品总价
        Long afterSaleAmount = 0L;

        //遍历申请售后的商品信息
        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject goodsJson = jsonArray.getJSONObject(i);
            //申请售后的商品信息
            Long afterSaleGoodsId = goodsJson.getLong("goodsId");//商品ID
            String afterSaleGoodsProps = goodsJson.getString("goodsProps");//属性ID
            MchGoods mchGoods = mchGoodsService.getById(afterSaleGoodsId);//申请售后的商品
            if (mchGoods == null) throw new ServiceException(RetEnum.RET_MBR_NO_SUCH_GOODS);

            //遍历订单商品信息，判断可退数量
            for (int k = 0; k < tradeOrderGoodsArray.size(); k++) {
                JSONObject tradeOrderGoods = (JSONObject) tradeOrderGoodsArray.get(k);//订单商品
                //原订单商品信息
                Long tradeOrderGoodsId = tradeOrderGoods.getLong("goodsId");//商品ID
                String tradeOrderGoodsProps = tradeOrderGoods.getString("goodsProps");//属性ID

                //申请售后的商品ID和商品属性 与 原订单的商品ID和商品属性 完全一致
                if (afterSaleGoodsId.equals(tradeOrderGoodsId) && StringUtils.isNoneBlank(afterSaleGoodsProps, tradeOrderGoodsProps)
                    && afterSaleGoodsProps.equals(tradeOrderGoodsProps)
                    ) {
                    if ((tradeOrderGoods.getIntValue("goodsNum") - tradeOrderGoods.getIntValue("frozenNum") < goodsJson.getIntValue("goodsNum"))) {
                        throw new ServiceException(RetEnum.RET_MBR_AFTER_SALE_NUM_OVER);//超出可售后数量
                    }
                    //更新商品不可售后数量
                    tradeOrderGoodsArray.remove(k);//删除原有元素
                    tradeOrderGoods.put("frozenNum", tradeOrderGoods.getIntValue("frozenNum") + goodsJson.getIntValue("goodsNum"));
                    tradeOrderGoodsArray.add(k, tradeOrderGoods);//添加更新后的元素
                }else if (afterSaleGoodsId.equals(tradeOrderGoodsId) && StringUtils.isAnyBlank(afterSaleGoodsProps, tradeOrderGoodsProps)){
                    if ((tradeOrderGoods.getIntValue("goodsNum") - tradeOrderGoods.getIntValue("frozenNum") < goodsJson.getIntValue("goodsNum"))) {
                        throw new ServiceException(RetEnum.RET_MBR_AFTER_SALE_NUM_OVER);//超出可售后数量
                    }
                    //更新商品不可售后数量
                    tradeOrderGoodsArray.remove(k);//删除原有元素
                    tradeOrderGoods.put("frozenNum", tradeOrderGoods.getIntValue("frozenNum") + goodsJson.getIntValue("goodsNum"));
                    tradeOrderGoodsArray.add(k, tradeOrderGoods);//添加更新后的元素
                }
            }

            //添加商品信息
            goodsJson.put("goodsName", mchGoods.getGoodsName());
            goodsJson.put("imgPathMain", mchGoods.getImgPathMain());
            goodsJson.put("cellingPrice", mchGoods.getCellingPrice());

            //计算申请售后商品总价
            afterSaleAmount = afterSaleAmount + new BigDecimal(mchGoods.getCellingPrice()).multiply(new BigDecimal(goodsJson.getInteger("goodsNum"))).longValue();

            //添加商品属性信息
            if(StringUtils.isBlank(goodsJson.getString("goodsProps"))){
                goodsJSONArray.add(goodsJson);
                continue;
            }

            String idList[] = goodsJson.getString("goodsProps").split(",");

            StringBuffer goodsPropsName = new StringBuffer();
            StringBuffer goodsPropsValue = new StringBuffer();
            for(int j = 0; j < idList.length; j++) {
                MchGoodsProps mchGoodsProps = mchGoodsPropsService.getById(Long.valueOf(idList[j]));
                if (mchGoodsProps == null) throw new ServiceException(RetEnum.RET_MBR_GOODS_PROPS_ERROR);

                if (j == idList.length - 1){
                    goodsPropsName.append(mchGoodsProps.getPropsName());
                    goodsPropsValue.append(mchGoodsProps.getPropsValue());
                }else {
                    goodsPropsName.append(mchGoodsProps.getPropsName() + ",");
                    goodsPropsValue.append(mchGoodsProps.getPropsValue() + ", ");
                }
            }
            goodsJson.put("goodsPropsName", goodsPropsName.toString());
            goodsJson.put("goodsPropsValue", goodsPropsValue.toString());

            goodsJSONArray.add(goodsJson);
        }

        //更新订单不可申请数量
        MchTradeOrder updateRecord = new MchTradeOrder();
        updateRecord.setTradeOrderId(afterSale.getTradeOrderId());
        updateRecord.setGoodsDesc(tradeOrderGoodsArray.toJSONString());
        int updateCount = mchTradeOrderService.update(updateRecord);
        if (updateCount <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

        //商品信息
        afterSale.setGoodsDesc(goodsJSONArray.toJSONString());
        afterSale.setAfterSaleAmount(afterSaleAmount);

        //地址信息
        MchReceiveAddress address = mchReceiveAddressService.getById(afterSale.getAddressId());
        if (address == null) throw new ServiceException(RetEnum.RET_MBR_RECEIVE_ADDRESS_ERROR);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("contactName", address.getContactName());
        jsonObject.put("tel", address.getTel());
        jsonObject.put("areaInfo", address.getAreaInfo());
        jsonObject.put("address", address.getAddress());
        afterSale.setAddressInfo(jsonObject.toJSONString());

        Long afterSaleId = getApplyId();
        afterSale.setAfterSaleId(afterSaleId);

        return baseMapper.insert(afterSale) > 0;
    }

    @Override
    public boolean updateComplete(Long afterOrderId) {
        return this.update(new LambdaUpdateWrapper<MchTradeOrderAfterSale>()
                .eq(MchTradeOrderAfterSale::getAfterSaleId, afterOrderId)
                .set(MchTradeOrderAfterSale::getStatus, MchConstant.MCH_AFTER_SALE_STATUS_COMPLETE)
                .set(MchTradeOrderAfterSale::getCompleteTime, new Date())
        );
    }

    @Override
    public int countAfterSaleIng(Long memberId, Byte authFrom) {

        return this.count(new QueryWrapper<MchTradeOrderAfterSale>().lambda()
                .eq(MchTradeOrderAfterSale::getMemberId, memberId)
                .eq(MchTradeOrderAfterSale::getAuthFrom, authFrom)
        );
    }

    //生成9位随机数
    public Long getApplyId() {
        int randomId = (int) ((Math.random()*9+1)*100000000);
        if(baseMapper.selectById(randomId) != null) {
            getApplyId();
        }
        return Long.valueOf(randomId);
    }
}

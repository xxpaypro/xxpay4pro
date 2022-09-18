package org.xxpay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.entity.MchGoods;
import org.xxpay.core.entity.MchGoodsProps;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.entity.MchTradeOrderDetail;
import org.xxpay.core.service.*;
import org.xxpay.service.dao.mapper.MchTradeOrderDetailMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城订单详情表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@Service
public class MchTradeOrderDetailServiceImpl extends ServiceImpl<MchTradeOrderDetailMapper, MchTradeOrderDetail> implements IMchTradeOrderDetailService {

    @Autowired
    private IMchStoreAreaManageService mchStoreAreaManageService;

    @Autowired
    private IMchReceiveAddressService mchReceiveAddressService;

    @Autowired
    private IMchGoodsService mchGoodsService;

    @Autowired
    private IMchGoodsPropsService mchGoodsPropsService;

    @Override
    public IPage<MchTradeOrderDetail> list(MchTradeOrderDetail mchTradeOrderDetail, Page page, JSONObject queryObj) {
        LambdaQueryWrapper<MchTradeOrderDetail> lambda = new QueryWrapper<MchTradeOrderDetail>().lambda();
        lambda.groupBy(MchTradeOrderDetail::getTradeOrderId);
        lambda.orderByDesc(MchTradeOrderDetail::getCreateTime);
        setCriteria(lambda, mchTradeOrderDetail, queryObj);
        return page(page, lambda);
    }

    @Override
    public void insertTradeOrderDetail(MchTradeOrder mchTradeOrder){

        String goodsDesc = mchTradeOrder.getGoodsDesc();//商品信息
        JSONArray jsonArray = JSONArray.parseArray(goodsDesc);

        //解析商品信息，goodsDesc格式：[{"cartId": "1", "goodsId": "1000", "goodsNum": "2", "goodsProps": "1,2"}]
        jsonArray.stream().forEach(jsonObject -> {
            MchTradeOrderDetail mchTradeOrderDetail = new MchTradeOrderDetail();

            mchTradeOrderDetail.setTradeOrderId(mchTradeOrder.getTradeOrderId());//交易单号
            mchTradeOrderDetail.setMchId(mchTradeOrder.getMchId());
            mchTradeOrderDetail.setIsvId(mchTradeOrder.getIsvId());
            mchTradeOrderDetail.setMemberId(mchTradeOrder.getMemberId());//会员ID

            JSONObject goodsJson = (JSONObject) jsonObject;
            mchTradeOrderDetail.setGoodsId(goodsJson.getLong("goodsId"));//商品ID
            MchGoods mchGoods = mchGoodsService.getById(goodsJson.getLong("goodsId"));
            mchTradeOrderDetail.setGoodsName(mchGoods.getGoodsName());//商品名称
            mchTradeOrderDetail.setImgPathMain(mchGoods.getImgPathMain());//商品主图
            mchTradeOrderDetail.setGoodsDesc(mchGoods.getGoodsDesc());//商品描述
            mchTradeOrderDetail.setCellingPrice(mchGoods.getCellingPrice());//商品售价
            mchTradeOrderDetail.setBuyingPrice(mchGoods.getBuyingPrice());//商品进价
            mchTradeOrderDetail.setMemberPrice(mchGoods.getMemberPrice());//商品会员价

            mchTradeOrderDetail.setGoodsNum(goodsJson.getInteger("goodsNum"));//商品数量

            if(StringUtils.isNotBlank(goodsJson.getString("goodsProps"))){
                mchTradeOrderDetail.setGoodsProps(goodsJson.getString("goodsProps"));//商品属性ID

                String idList[] = goodsJson.getString("goodsProps").split(",");

                StringBuffer goodsPropsName = new StringBuffer();
                StringBuffer goodsPropsValue = new StringBuffer();
                for(int i = 0; i < idList.length; i++) {
                    MchGoodsProps mchGoodsProps = mchGoodsPropsService.getById(Long.valueOf(idList[i]));
                    if (mchGoodsProps == null) throw new ServiceException(RetEnum.RET_MBR_GOODS_PROPS_ERROR);

                    if (i == idList.length - 1){
                        goodsPropsName.append(mchGoodsProps.getPropsName());
                        goodsPropsValue.append(mchGoodsProps.getPropsName() + "：" + mchGoodsProps.getPropsValue());
                    }else {
                        goodsPropsName.append(mchGoodsProps.getPropsName() + ",");
                        goodsPropsValue.append(mchGoodsProps.getPropsName() + "：" + mchGoodsProps.getPropsValue() + ", ");
                    }
                }
                mchTradeOrderDetail.setGoodsPropsName(goodsPropsName.toString());
                mchTradeOrderDetail.setGoodsPropsValue(goodsPropsValue.toString());
            }

            baseMapper.insert(mchTradeOrderDetail);
        });
    }

    @Override
    public List<Map> selectList(int offset, int limit, MchTradeOrderDetail mchTradeOrderDetail) {

        /*Map params = new HashMap();
        params.put("offser", offset);
        params.put("limit", limit);
        if (mchTradeOrderDetail.getOrderId() != null) params.put("orderId", mchTradeOrderDetail.getOrderId());
        if (StringUtils.isNotBlank(mchTradeOrderDetail.getTradeOrderId())) params.put("tradeOrderId", mchTradeOrderDetail.getTradeOrderId());
        if (mchTradeOrderDetail.getMchId() != null) params.put("mchId", mchTradeOrderDetail.getMchId());
        if (mchTradeOrderDetail.getMemberId() != null) params.put("memberId", mchTradeOrderDetail.getMemberId());*/

        return null;
    }


    void setCriteria(LambdaQueryWrapper<MchTradeOrderDetail> lambda, MchTradeOrderDetail mchTradeOrderDetail, JSONObject queryObj) {
        if(mchTradeOrderDetail != null) {
            if (mchTradeOrderDetail.getMemberId() != null) lambda.eq(MchTradeOrderDetail::getMemberId, mchTradeOrderDetail.getMemberId());
            if (StringUtils.isNotBlank(mchTradeOrderDetail.getGoodsName())) lambda.like(MchTradeOrderDetail::getGoodsName, "%" + mchTradeOrderDetail.getGoodsName() + "%");
            if (mchTradeOrderDetail.getMchId() != null) lambda.eq(MchTradeOrderDetail::getMchId, mchTradeOrderDetail.getMchId());
            if (mchTradeOrderDetail.getIsvId() != null) lambda.eq(MchTradeOrderDetail::getIsvId, mchTradeOrderDetail.getIsvId());
            if(queryObj != null) {
                if(queryObj.getDate("createTimeStart") != null) lambda.ge(MchTradeOrderDetail::getCreateTime, queryObj.getDate("createTimeStart"));
                if(queryObj.getDate("createTimeEnd") != null) lambda.le(MchTradeOrderDetail::getCreateTime, queryObj.getDate("createTimeEnd"));
            }
        }
    }

}

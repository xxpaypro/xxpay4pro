package org.xxpay.core.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.entity.MchTradeOrderDetail;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城订单详情表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
public interface IMchTradeOrderDetailService extends IService<MchTradeOrderDetail> {

    /**
     * 订单详情列表
     * @param mchTradeOrderDetail
     * @param page
     * @param queryObj
     * @return
     */
    IPage<MchTradeOrderDetail> list(MchTradeOrderDetail mchTradeOrderDetail, Page page, JSONObject queryObj);

    /**
     * 订单详情列表
     * @param mchTradeOrder
     * @return
     */
    void insertTradeOrderDetail(MchTradeOrder mchTradeOrder);

    /**
     * 订单详情列表
     * @param mchTradeOrderDetail
     * @return
     */
    List<Map> selectList(int offset, int limit, MchTradeOrderDetail mchTradeOrderDetail);
}

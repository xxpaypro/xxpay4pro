package org.xxpay.core.service;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.SxfMchSnapshot;

/**
 * 随行付进件相关接口
 */
public interface IXxPaySxfpayApiService {

    /** 随行付进件接口 **/
    void sxfMicroApply(SxfMchSnapshot sxfMchSnapshot);

    /** 查询进件状态 **/
    JSONObject sxfQueryMicroApply(Long applyNo);

}

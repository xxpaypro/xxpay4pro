package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.common.vo.ExtConfigVO;
import org.xxpay.core.common.vo.PayExtConfigVO;
import org.xxpay.core.entity.PayPassage;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/5/3
 * @description: 支付通道
 */
public interface IPayPassageService extends IService<PayPassage> {

    int add(PayPassage payPassage);

    int update(PayPassage payPassage);

    int updateRate(PayPassage payPassage);

    PayPassage findById(Integer id);

    List<PayPassage> select(int offset, int limit, PayPassage payPassage);

    Integer count(PayPassage payPassage);

    List<PayPassage> selectAll(PayPassage payPassage);

    /**
     * 根据支付类型查询所有支付通道列表 (查询平台通道)
     * @param payType
     * @return
     */
    List<PayPassage> selectPlatAllByPayType(String payType);

    /**
     *

     */

    /**
     * @description: 根据支付类型查询所有支付通道列表 (查询私有商户的支付通道)
     * @Author terrfly
     * @Date 2018/11/21 17:51
     * @param payType
     * @param mchId
     * @return
     */
    List<PayPassage> selectMchAllByPayType(String payType, Long mchId);


    /**
     * 根据配置信息 查询最大通道费率
     * @param payExtConfigVO
     * @return
     */
     BigDecimal getMaxChannelFee(PayExtConfigVO payExtConfigVO);


     /** 查询服务商通道配置信息 **/
    PayPassage selectByIsv(Long isvId, String ifTypeCode);

    /** 查询商户通道配置信息 **/
    PayPassage selectByMch(Long mchId, String ifTypeCode);




}

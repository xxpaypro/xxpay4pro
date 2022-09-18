package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.FeeScale;

import java.util.List;

public interface IFeeScaleService extends IService<FeeScale> {

    /** 根据ID查询 **/
    FeeScale findById(Integer id);

    /** 根据条件获取唯一的一条数据 **/
    FeeScale findOne(byte infoType, Long infoId, byte productType, Integer refProductId) ;

    /** 查询【商户】的【支付产品】配置费率信息 **/
    FeeScale getPayFeeByMch(Long mchId, Integer productId) ;

    /**查询【代理商】的【支付产品】配置费率信息  **/
    FeeScale getPayFeeByAgent(Long agentId, Integer productId) ;

    /** 查询【商户】的【代付产品】配置费率信息 **/
    FeeScale getAgpayFeeByMch(Long mchId, Integer passageId) ;

    /** 查询【代理商】的【线下充值产品】配置费率信息 **/
    FeeScale getOffRechargeFeeByAgent(Long agentId) ;

    /** 查询【商户】的【线下充值产品】配置费率信息 **/
    FeeScale getOffRechargeFeeByMch(Long mchId) ;

    /** 查询【代理商】的【代付产品】配置费率信息 **/
    FeeScale getAgpayFeeByAgent(Long agentId, Integer passageId) ;

    /** 查询【服务商】的【支付产品】配置费率信息 **/
    FeeScale getPayFeeByIsv(Long isvId, Integer productId) ;

    /**
     * 得到商户/代理商 代付业务可用通道信息（优先使用默认通道）
     * @param infoType
     * @param infoId
     * @return
     */
    FeeScale selectAgpayAvailable(byte infoType, Long infoId);

    /**
     * 连接查询支付产品信息
     * @param infoType
     * @param infoId
     * @param leftOrInner leftJoin | innerJoin  || 左连接（以t_pay_product为主）   | 内连接查询（以feeScale为主）
     * @param showExtInfo 是否显示 配置, 查询角色的上级代理商费率 信息
     * @return
     */
    List<FeeScale> productJoinFeeScale(byte infoType,  Long infoId, String leftOrInner, boolean showExtInfo);



     /**
     * t_agentpay_passage表连接查询feeScale
     * @param infoType
     * @param infoId
      *@param leftOrInner   leftJoin | innerJoin  || 左连接（以t_agentpay_passage为主）   | 内连接查询（以feeScale为主）
      *@param showExtInfo 是否显示 配置, 查询角色的上级代理商费率 信息
     * @return
     */
    List<FeeScale> agpayPassageJoinFeeScale(byte infoType, Long infoId, String leftOrInner, boolean showExtInfo);


    List<FeeScale> selectAll(byte infoType, Long infoId, byte productType);

    List<FeeScale> select(int offset, int limit, FeeScale feeScale);

    Integer count(FeeScale feeScale) ;


    Integer update(FeeScale feeScale) ;

    Integer add(FeeScale feeScale) ;

    Integer updateNotDefault(byte infoType, Long infoId, byte productType);

    void privateMchFeeConfig(List<FeeScale> feeScales);


    /**
     * 查询当前角色的费率/费用 配置信息，和平台上游渠道的 费用配置
     * 一般用于订单分润的计算
     * @param infoType
     * @param infoId
     * @param feeScaleProductType  计费类型
     * @param refProductId refProductId
     * @param bizOrderId 订单ID, 由于支付订单无法直接获取到使用的通道，需要根据订单id反查
     * @return
     */
    FeeScale selectInfoAndChannelFeeScale(byte infoType, Long infoId, byte feeScaleProductType, Integer refProductId, String bizOrderId);

    /**
     * 更新服务商下商户费率
     * @param feeScale
     * @param isvFeeScale
     * @return
     */
    Boolean updateMchFeeScale(FeeScale feeScale, FeeScale isvFeeScale);
}

package org.xxpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.callback.AddTradeOrderCallBack;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.PayEnum;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.*;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.*;
import org.xxpay.service.dao.mapper.MchGoodsMapper;
import org.xxpay.service.dao.mapper.MchTradeOrderMapper;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 17/12/21
 * @description:
 */
@Service
public class MchTradeOrderServiceImpl extends ServiceImpl<MchTradeOrderMapper, MchTradeOrder> implements IMchTradeOrderService {

    private static final MyLog logger = MyLog.getLog(MchTradeOrderServiceImpl.class);

    @Autowired
    private MchTradeOrderMapper mchTradeOrderMapper;

    @Autowired
    private MchGoodsMapper mchGoodsMapper;

    @Autowired
    IMchMemberConfigService mchMemberConfigService;

    @Autowired
    IMemberService memberService;

    @Autowired
    IMchFansService mchFansService;

    @Autowired
    IMemberPointsHistoryService memberPointsHistoryService;

    @Autowired
    IMemberPointsService memberPointsService;

    @Autowired
    IMemberBalanceHistoryService memberBalanceHistoryService;

    @Autowired
    IMemberBalanceService memberBalanceService;

    @Autowired
    IMchMemberRechargeRuleService mchMemberRechargeRuleService;

    @Autowired
    IMemberCouponService memberCouponService;

    @Autowired
    IMchInfoService mchInfoService;

    @Autowired
    IAgentInfoService agentInfoService;

    @Autowired
    IFeeScaleService feeScaleService;

    @Autowired
    IOrderProfitDetailService orderProfitDetailService;

    @Autowired
    IMchStoreService mchStoreService;

    @Autowired
    ISysService sysService;

    @Autowired
    private IPayOrderService payOrderService;

    @Autowired
    private IMchReceiveAddressService mchReceiveAddressService;

    @Autowired
    private IMchStoreAreaManageService mchStoreAreaManageService;

    @Autowired
    private IMchTradeOrderDetailService mchTradeOrderDetailService;

    @Autowired
    private IMchGoodsService mchGoodsService;

    @Autowired
    private IMchShoppingCartService mchShoppingCartService;

    @Autowired
    private IMchGoodsPropsService mchGoodsPropsService;


    @Override
    public List<MchTradeOrder> select(int pageIndex, int pageSize, MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd) {
        MchTradeOrderExample example = new MchTradeOrderExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(pageIndex);
        example.setLimit(pageSize);
        MchTradeOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchTradeOrder, createTimeStart, createTimeEnd);
        return mchTradeOrderMapper.selectByExample(example);
    }

    /** 纳呈修改  **/
    @Override
    public List<MchTradeOrder> selectPlus(int pageIndex, int pageSize, MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd) {
        MchTradeOrderExample example = new MchTradeOrderExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(pageIndex);
        example.setLimit(pageSize);
        MchTradeOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchTradeOrder, createTimeStart, createTimeEnd);
        return mchTradeOrderMapper.selectPlusByExample(example);
    }

    @Override
    public List<MchTradeOrder> selectByExampleWithBLOBs(int pageIndex, int pageSize, MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd) {
        MchTradeOrderExample example = new MchTradeOrderExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(pageIndex);
        example.setLimit(pageSize);
        MchTradeOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchTradeOrder, createTimeStart, createTimeEnd);
        return mchTradeOrderMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public Long count(MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd) {
        MchTradeOrderExample example = new MchTradeOrderExample();
        MchTradeOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchTradeOrder, createTimeStart, createTimeEnd);
        return mchTradeOrderMapper.countByExample(example);
    }

    @Override
    public int add(MchTradeOrder mchTradeOrder) {
        return mchTradeOrderMapper.insertSelective(mchTradeOrder);
    }

    @Override
    public MchTradeOrder insertTradeOrder(MchInfo mchInfo, Byte tradeType, Long requiredAmount, Long realAmount,
                                          Byte tradeProductType, Integer productId, String ip,
                                           Long operatorId, Long storeId, AddTradeOrderCallBack callback) {

        MchTradeOrder mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setTradeType(tradeType); //交易类型 1-收款 2-充值
        mchTradeOrder.setAppId("");   //appId
        mchTradeOrder.setDevice(""); //设备
        mchTradeOrder.setGoodsId(""); //商品ID
        mchTradeOrder.setProductId(productId); //支付产品ID
        mchTradeOrder.setProductType(tradeProductType); //支付产品类型

        mchTradeOrder.setStatus(MchConstant.TRADE_ORDER_STATUS_ING); //订单状态, 默认支付中
        mchTradeOrder.setSettTaskStatus(MchConstant.ISV_SETT_STATUS_WAIT);  //结算任务状态： 待跑批

        mchTradeOrder.setMchId(mchInfo.getMchId());  //MchId
        mchTradeOrder.setAgentId(mchInfo.getAgentId()); //agentId
        mchTradeOrder.setIsvId(mchInfo.getIsvId()); //isvId

        mchTradeOrder.setProvinceCode(mchInfo.getProvinceCode()); //省编号
        mchTradeOrder.setCityCode(mchInfo.getCityCode()); //市编号
        mchTradeOrder.setAreaCode(mchInfo.getAreaCode()); //县编号
        mchTradeOrder.setAreaInfo(mchInfo.getAreaInfo()); //省市县信息

        mchTradeOrder.setClientIp(ip); //客户端IP
        mchTradeOrder.setOrderAmount(requiredAmount); //订单金额
        mchTradeOrder.setAmount(realAmount);  //实际支付金额
        mchTradeOrder.setDiscountAmount(requiredAmount - realAmount); //优惠金额

        String prefix = MchConstant.TRADE_TYPE_PAY == tradeType ? "消费|" : "充值|";

        if(productId == null){ //会员卡支付
            mchTradeOrder.setSubject(prefix + "会员卡支付商品"); //商品标题
            mchTradeOrder.setBody(prefix + "会员卡支付商品"); //商品描述信息
        }else{

            if(PayConstant.PAY_PRODUCT_WX_JSAPI == productId){
                mchTradeOrder.setSubject(prefix + "微信扫码支付");
                mchTradeOrder.setBody(prefix + "微信扫码支付");
            }else if(PayConstant.PAY_PRODUCT_ALIPAY_JSAPI == productId){
                mchTradeOrder.setSubject(prefix + "支付宝扫码支付");
                mchTradeOrder.setBody(prefix + "支付宝扫码支付");

            }else if(PayConstant.PAY_PRODUCT_WX_BAR == productId){
                mchTradeOrder.setSubject(prefix + "微信条码支付");
                mchTradeOrder.setBody(prefix + "微信条码支付");

            }else if(PayConstant.PAY_PRODUCT_ALIPAY_BAR == productId){
                mchTradeOrder.setSubject(prefix + "支付宝条码支付");
                mchTradeOrder.setBody(prefix + "支付宝条码支付");
            }else if(PayConstant.PAY_PRODUCT_UNIONPAY_BAR == productId){
                mchTradeOrder.setSubject(prefix + "云闪付条码支付");
                mchTradeOrder.setBody(prefix + "云闪付条码支付");
            }else if(PayConstant.PAY_PRODUCT_WX_MINI_PROGRAM == productId){
                mchTradeOrder.setSubject(prefix + "微信小程序支付");
                mchTradeOrder.setBody(prefix + "微信小程序支付");
            }else if(PayConstant.PAY_PRODUCT_JD_H5 == productId){
                mchTradeOrder.setSubject(prefix + "京东支付");
                mchTradeOrder.setBody(prefix + "京东支付");
            }
        }


        mchTradeOrder.setStoreId(storeId);
        if(storeId != null){
            MchStore mchStore = mchStoreService.getById(storeId);
            mchTradeOrder.setStoreNo(mchStore.getStoreNo()); //门店No
            mchTradeOrder.setStoreName(mchStore.getStoreName()); //门店名称
        }

        mchTradeOrder.setOperatorId(operatorId != null ? operatorId + "" : null);
        if(operatorId != null){
            SysUser subUser = sysService.findByUserId(operatorId);
            mchTradeOrder.setOperatorId(operatorId + ""); //操作员ID
            mchTradeOrder.setOperatorName(subUser.getLoginUserName()); //操作员姓名
        }

        if(callback != null){
            callback.setRecordBeforeInsert(mchTradeOrder);
        }

        if(StringUtils.isEmpty(mchTradeOrder.getTradeOrderId())){
            mchTradeOrder.setTradeOrderId(MySeq.getTrade());  ////生成订单号
        }

        mchTradeOrderMapper.insertSelective(mchTradeOrder); //入库

        return mchTradeOrder;
    }

    @Override
    public MchTradeOrder insertTradeOrderHis(MchInfo mchInfo, Byte tradeType, Long requiredAmount, Long realAmount,
                                          Byte tradeProductType, Integer productId, String ip, String mchOrderNo,
                                          Long operatorId, Long storeId, AddTradeOrderCallBack callback) {

        MchTradeOrder mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setTradeType(tradeType); //交易类型 1-收款 2-充值
        mchTradeOrder.setAppId("");   //appId
        mchTradeOrder.setDevice(""); //设备
        mchTradeOrder.setGoodsId(""); //商品ID
        mchTradeOrder.setProductId(productId); //支付产品ID
        mchTradeOrder.setProductType(tradeProductType); //支付产品类型

        mchTradeOrder.setStatus(MchConstant.TRADE_ORDER_STATUS_ING); //订单状态, 默认支付中
        mchTradeOrder.setSettTaskStatus(MchConstant.ISV_SETT_STATUS_WAIT);  //结算任务状态： 待跑批

        mchTradeOrder.setMchId(mchInfo.getMchId());  //MchId
        mchTradeOrder.setAgentId(mchInfo.getAgentId()); //agentId
        mchTradeOrder.setIsvId(mchInfo.getIsvId()); //isvId

        mchTradeOrder.setProvinceCode(mchInfo.getProvinceCode()); //省编号
        mchTradeOrder.setCityCode(mchInfo.getCityCode()); //市编号
        mchTradeOrder.setAreaCode(mchInfo.getAreaCode()); //县编号
        mchTradeOrder.setAreaInfo(mchInfo.getAreaInfo()); //省市县信息

        mchTradeOrder.setClientIp(ip); //客户端IP
        mchTradeOrder.setOrderAmount(requiredAmount); //订单金额
        mchTradeOrder.setAmount(realAmount);  //实际支付金额
        mchTradeOrder.setDiscountAmount(requiredAmount - realAmount); //优惠金额

        String prefix = MchConstant.TRADE_TYPE_PAY == tradeType ? "消费|" : "充值|";

        if(productId == null){ //会员卡支付
            mchTradeOrder.setSubject(prefix + "会员卡支付商品"); //商品标题
            mchTradeOrder.setBody(prefix + "会员卡支付商品"); //商品描述信息
        }else{

            if(PayConstant.PAY_PRODUCT_WX_JSAPI == productId){
                mchTradeOrder.setSubject(prefix + "微信扫码支付");
                mchTradeOrder.setBody(prefix + "微信扫码支付");
            }else if(PayConstant.PAY_PRODUCT_ALIPAY_JSAPI == productId){
                mchTradeOrder.setSubject(prefix + "支付宝扫码支付");
                mchTradeOrder.setBody(prefix + "支付宝扫码支付");

            }else if(PayConstant.PAY_PRODUCT_WX_BAR == productId){
                mchTradeOrder.setSubject(prefix + "微信条码支付");
                mchTradeOrder.setBody(prefix + "微信条码支付");

            }else if(PayConstant.PAY_PRODUCT_ALIPAY_BAR == productId){
                mchTradeOrder.setSubject(prefix + "支付宝条码支付");
                mchTradeOrder.setBody(prefix + "支付宝条码支付");
            }else if(PayConstant.PAY_PRODUCT_UNIONPAY_BAR == productId){
                mchTradeOrder.setSubject(prefix + "云闪付条码支付");
                mchTradeOrder.setBody(prefix + "云闪付条码支付");
            }else if(PayConstant.PAY_PRODUCT_WX_MINI_PROGRAM == productId){
                mchTradeOrder.setSubject(prefix + "微信小程序支付");
                mchTradeOrder.setBody(prefix + "微信小程序支付");
            }else if(PayConstant.PAY_PRODUCT_JD_H5 == productId){
                mchTradeOrder.setSubject(prefix + "京东支付");
                mchTradeOrder.setBody(prefix + "京东支付");
            }
        }


        mchTradeOrder.setStoreId(storeId);
        if(storeId != null){
            MchStore mchStore = mchStoreService.getById(storeId);
            mchTradeOrder.setStoreNo(mchStore.getStoreNo()); //门店No
            mchTradeOrder.setStoreName(mchStore.getStoreName()); //门店名称
        }

        mchTradeOrder.setOperatorId(operatorId != null ? operatorId + "" : null);
        if(operatorId != null){
            SysUser subUser = sysService.findByUserId(operatorId);
            mchTradeOrder.setOperatorId(operatorId + ""); //操作员ID
            mchTradeOrder.setOperatorName(subUser.getLoginUserName()); //操作员姓名
        }

        if(callback != null){
            callback.setRecordBeforeInsert(mchTradeOrder);
        }

        if(StringUtils.isEmpty(mchTradeOrder.getTradeOrderId())){
            mchTradeOrder.setTradeOrderId(mchOrderNo);  ////生成订单号
        }

        mchTradeOrderMapper.insertSelective(mchTradeOrder); //入库

        return mchTradeOrder;
    }


    @Override
    public MchTradeOrder insertTradeOrderJWT(MchInfo mchInfo, Byte tradeType, Long requiredAmount, Long realAmount, Byte tradeProductType, Integer productId, String ip, AddTradeOrderCallBack callback) {

        JWTBaseUser jwtBaseUser = SpringSecurityUtil.getCurrentJWTUser();
        return insertTradeOrder(mchInfo, tradeType, requiredAmount, realAmount, tradeProductType, productId, ip,
                jwtBaseUser.getUserId(), jwtBaseUser.getStoreId(), callback);
    }


    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public MchTradeOrder insertTradeOrderMiniProgram(MchInfo mchInfo, Byte tradeType, Long requiredAmount, Long realAmount, Byte postType, Long postPrice,
                                                     Byte tradeProductType, Integer productId, String ip, Long storeId, Long addressId, Long storeAreaId, String goodsDesc, AddTradeOrderCallBack callBack) {

        MchTradeOrder mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setTradeType(tradeType); //交易类型 1-收款 2-充值
        mchTradeOrder.setAppId("");   //appId
        mchTradeOrder.setDevice(""); //设备
        mchTradeOrder.setGoodsId(""); //商品ID
        mchTradeOrder.setProductId(productId); //支付产品ID
        mchTradeOrder.setProductType(tradeProductType); //支付产品类型

        mchTradeOrder.setStatus(MchConstant.TRADE_ORDER_STATUS_ING); //订单状态, 默认支付中
        mchTradeOrder.setSettTaskStatus(MchConstant.ISV_SETT_STATUS_WAIT);  //结算任务状态： 待跑批

        mchTradeOrder.setMchId(mchInfo.getMchId());  //MchId
        mchTradeOrder.setAgentId(mchInfo.getAgentId()); //agentId
        mchTradeOrder.setIsvId(mchInfo.getIsvId()); //isvId

        mchTradeOrder.setClientIp(ip); //客户端IP

        List<Long> cartIdList = new LinkedList();
        List<String> goodsImageList = new LinkedList();
        Long orderAmount = 0L;
        Long goodsPrice = 0L;

        //解析商品信息，goodsDesc格式：[{"cartId": "1000", "goodsId": "1000", "goodsNum": "2", "goodsProps": "1,2"}]
        JSONArray jsonArray = JSONArray.parseArray(goodsDesc);
        JSONArray goodsJSONArray = new JSONArray();
        //遍历商品信息，计算金额，筛选出购物车ID，添加其他商品信息到json
        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject goodsJson = jsonArray.getJSONObject(i);
            MchGoods mchGoods = mchGoodsService.getById(goodsJson.getLong("goodsId"));
            if (mchGoods == null) throw new ServiceException(RetEnum.RET_MBR_NO_SUCH_GOODS);

                goodsImageList.add(mchGoods.getImgPathMain());

                if (goodsJson.getLong("cartId") != null) {
                    cartIdList.add(goodsJson.getLong("cartId"));
                }

            goodsPrice = new BigDecimal(mchGoods.getCellingPrice()).multiply(new BigDecimal(goodsJson.getString("goodsNum"))).longValue();
            orderAmount += goodsPrice;

            //添加商品信息
            goodsJson.put("goodsName", mchGoods.getGoodsName());
            goodsJson.put("imgPathMain", mchGoods.getImgPathMain());
            goodsJson.put("cellingPrice", mchGoods.getCellingPrice());
            goodsJson.put("frozenNum", 0);

            //添加商品属性信息
            if(StringUtils.isNotBlank(goodsJson.getString("goodsProps"))){
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
            }
            goodsJSONArray.add(goodsJson);
        }

        //商品信息
        mchTradeOrder.setGoodsDesc(goodsJSONArray.toJSONString());

        orderAmount += postPrice;

        //校验前端传来的价格与商品价格是否相等， 应付金额 = 实付金额 + 优惠金额 = 商品总价 + 运费
        if (!requiredAmount.equals(orderAmount)) {
            throw ServiceException.build(RetEnum.RET_MCH_STORE_SPEAKER_CODE_ERROR);
        }

        mchTradeOrder.setOrderAmount(requiredAmount); //订单金额
        mchTradeOrder.setAmount(realAmount);  //实际支付金额
        mchTradeOrder.setDiscountAmount(requiredAmount - realAmount); //优惠金额
        mchTradeOrder.setImgPathMain(goodsImageList.get(0));

        mchTradeOrder.setPostType(postType);//1-堂食 2-外卖
        mchTradeOrder.setPostPrice(postPrice); //运费

        String prefix = MchConstant.TRADE_TYPE_PAY == tradeType ? "消费|" : "充值|";

        mchTradeOrder.setSubject(prefix + "微信小程序支付");
        mchTradeOrder.setBody(prefix + "微信小程序支付");

        //没有门店时，添加商户默认门店。退款时，需要门店退款密码，所以门店ID理应不为空
        if (storeId == null) {
            SysUser mchUser = sysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_MCH, mchInfo.getLoginMobile());
            storeId = mchUser.getStoreId();
        }

        mchTradeOrder.setStoreId(storeId);
        if(storeId != null){
            MchStore mchStore = mchStoreService.getById(storeId);
            if (mchStore == null || !mchStore.getMchId().equals(mchInfo.getMchId())) throw ServiceException.build(RetEnum.RET_MCH_STORE_NOT_EXIST);
            mchTradeOrder.setStoreNo(mchStore.getStoreNo()); //门店No
            mchTradeOrder.setStoreName(mchStore.getStoreName()); //门店名称
        }

        mchTradeOrder.setStoreAreaId(storeAreaId);//堂食区域ID
        if (storeAreaId != null && postType == MchConstant.MCH_POST_TYPE_TANGSHI) {
            MchStoreAreaManage mchStoreArea = mchStoreAreaManageService.getById(storeAreaId);
            if (mchStoreArea == null || !mchStoreArea.getMchId().equals(mchInfo.getMchId())) throw ServiceException.build(RetEnum.RET_MBR_AREA_ERROR);
            mchTradeOrder.setAreaName(mchStoreArea.getAreaName());
        }

        mchTradeOrder.setAddressId(addressId);//收货地址ID
        if(addressId != null && postType == MchConstant.MCH_POST_TYPE_WAIMAI) {
            MchReceiveAddress receiveAddress = mchReceiveAddressService.getById(addressId);
            if (receiveAddress == null || !receiveAddress.getMchId().equals(mchInfo.getMchId())) throw ServiceException.build(RetEnum.RET_MBR_RECEIVE_ADDRESS_ERROR);
            mchTradeOrder.setProvinceCode(receiveAddress.getProvinceCode()); //省编号
            mchTradeOrder.setCityCode(receiveAddress.getCityCode()); //市编号
            mchTradeOrder.setAreaCode(receiveAddress.getAreaCode()); //县编号
            mchTradeOrder.setAreaInfo(receiveAddress.getAreaInfo()); //省市县信息
            mchTradeOrder.setAddress(receiveAddress.getAddress());//详细地址
            mchTradeOrder.setReceiveName(receiveAddress.getContactName());//收货人
            mchTradeOrder.setReceiveTel(receiveAddress.getTel());//收货人手机
        }


        if (callBack != null) {
            callBack.setRecordBeforeInsert(mchTradeOrder);
        }

        if(StringUtils.isEmpty(mchTradeOrder.getTradeOrderId())){
            mchTradeOrder.setTradeOrderId(MySeq.getTrade());  ////生成订单号
        }

        mchTradeOrderMapper.insertSelective(mchTradeOrder); //入库

        mchTradeOrderDetailService.insertTradeOrderDetail(mchTradeOrder);  //插入订单详情

        if(cartIdList.size() > 0) {
            mchShoppingCartService.removeByIds(cartIdList);//购物车清空已购买商品
        }

        return mchTradeOrder;
    }


    /** 调起支付接口 **/
    @Override
    public JSONObject callPayInterface(MchTradeOrder mchTradeOrder, MchInfo mchInfo, String payUrl, String notifyUrl) {

        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchTradeOrder.getMchId());                    //商户ID
        paramMap.put("appId", mchTradeOrder.getAppId());                    // 应用ID
        paramMap.put("mchOrderNo", mchTradeOrder.getTradeOrderId());        // 商户交易单号
        paramMap.put("productId", mchTradeOrder.getProductId());            // 支付产品ID
        paramMap.put("amount", mchTradeOrder.getAmount());                  // 支付金额,单位分
        paramMap.put("currency", "cny");                                    // 币种, cny-人民币
        paramMap.put("clientIp", mchTradeOrder.getClientIp());              // 用户地址,IP或手机号
        paramMap.put("device", mchTradeOrder.getDevice());                  // 设备
        paramMap.put("subject", mchTradeOrder.getSubject());                // 标题
        paramMap.put("body", mchTradeOrder.getBody());                      // 商品描述
        paramMap.put("notifyUrl", notifyUrl);                               // 回调URL
        paramMap.put("channelUserId", StringUtils.defaultString(mchTradeOrder.getChannelUserId(), ""));    // 渠道用户ID
        paramMap.put("param1", "");                                         // 扩展参数1
        paramMap.put("param2", "");                                         // 扩展参数2
        paramMap.put("extra", StringUtils.defaultString(mchTradeOrder.getUserId(), ""));                   // 二维码条码

        //押金支付 模式
        if(mchTradeOrder.getDepositMode() != null && mchTradeOrder.getDepositMode() == MchConstant.PUB_YES){
            paramMap.put("amount", mchTradeOrder.getDepositAmount());
            paramMap.put("depositMode", 1);
        }

        paramMap.put("sign", PayDigestUtil.getSign(paramMap, mchInfo.getPrivateKey()));   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        logger.info("xxpay_req:{}", reqData);
        String result = XXPayUtil.call4Post(payUrl + "/pay/create_order?" + reqData);
        logger.info("xxpay_res:{}", result);

        if(StringUtils.isEmpty(result)){ //返回消息为空
            logger.error("调起支付接口，返回结果过null result = {}", result);
            return null;
        }

        JSONObject retMsg = JSON.parseObject(result);

        if(retMsg == null || !PayEnum.OK.getCode().equals(retMsg.getString("retCode"))) { //支付网关返回结果验证失败
            logger.error("调起支付接口失败， retCode={}, retMsg={}", retMsg.getString("retCode"), retMsg.getString("retMsg"));
            return null;
        }

        // 验签失败
        String iSign = PayDigestUtil.getSign(retMsg, mchInfo.getPrivateKey(), "sign");
        if(!iSign.equals(retMsg.getString("sign"))) {
            logger.error("验签失败, isign={}, retSign={}", iSign, retMsg.getString("sign"));
           return null;
        }
        return retMsg;
    }

    @Override
    public MchTradeOrder findByTradeOrderId(String tradeOrderId) {
        return mchTradeOrderMapper.selectByPrimaryKey(tradeOrderId);
    }

    @Override
    public MchTradeOrder findByMchIdAndTradeOrderId(Long mchId, String tradeOrderId) {
        MchTradeOrderExample example = new MchTradeOrderExample();
        MchTradeOrderExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andTradeOrderIdEqualTo(tradeOrderId);
        List<MchTradeOrder> mchTradeOrderList = mchTradeOrderMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(mchTradeOrderList)) return null;
        return mchTradeOrderList.get(0);
    }

    @Override
    public MchTradeOrder findByMchIdAndPayOrderId(Long mchId, String payOrderId) {
        MchTradeOrderExample example = new MchTradeOrderExample();
        MchTradeOrderExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andPayOrderIdEqualTo(payOrderId);
        List<MchTradeOrder> mchTradeOrderList = mchTradeOrderMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(mchTradeOrderList)) return null;
        return mchTradeOrderList.get(0);
    }

    @Override
    public int updateStatus4Ing(String tradeOrderId) {
        MchTradeOrder mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setStatus(MchConstant.TRADE_ORDER_STATUS_ING);
        MchTradeOrderExample example = new MchTradeOrderExample();
        MchTradeOrderExample.Criteria criteria = example.createCriteria();
        criteria.andTradeOrderIdEqualTo(tradeOrderId);
        criteria.andStatusEqualTo(MchConstant.TRADE_ORDER_STATUS_INIT);
        return mchTradeOrderMapper.updateByExampleSelective(mchTradeOrder, example);
    }

    @Override
    public int updateStatus4Success(String tradeOrderId, Long income, Long givePoints) {
        return updateStatus4Success(tradeOrderId, null, income, givePoints);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStatus4Success(String tradeOrderId, String payOrderId, Long income, Long givePoints) {

        MchTradeOrder mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setTradeSuccTime(new Date());
        if(StringUtils.isNotBlank(payOrderId)) mchTradeOrder.setPayOrderId(payOrderId);
        mchTradeOrder.setMchIncome(income);
        mchTradeOrder.setStatus(MchConstant.TRADE_ORDER_STATUS_SUCCESS);
        mchTradeOrder.setPostStatus(MchConstant.TRADE_ORDER_POST_STATUS_INIT);
        mchTradeOrder.setGivePoints(givePoints);  //赠送积分
        MchTradeOrderExample example = new MchTradeOrderExample();
        MchTradeOrderExample.Criteria criteria = example.createCriteria();
        criteria.andTradeOrderIdEqualTo(tradeOrderId);
        criteria.andStatusEqualTo(MchConstant.TRADE_ORDER_STATUS_ING);

        int count = mchTradeOrderMapper.updateByExampleSelective(mchTradeOrder, example);
        if (count < 1) return 0;

        /*
        //更新商品销量
        MchTradeOrder dbTradeOrder = baseMapper.selectById(tradeOrderId);
        if (StringUtils.isNotBlank(dbTradeOrder.getGoodsDesc())) {
            JSONArray jsonArray = JSONArray.parseArray(dbTradeOrder.getGoodsDesc());

            Long newSaleNum;
            MchGoods mchGoods;
            MchGoods updateRecord = new MchGoods();

            //解析商品信息，goodsDesc格式：[{"cartId": "1", "goodsId": "1000", "goodsNum": "2", "goodsProps": "1,2"}]
            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                mchGoods = mchGoodsService.getById(object.getLong("goodsId"));
                newSaleNum =  mchGoods.getActualSaleNum() + object.getLong("goodsNum");

                updateRecord.setGoodsId(mchGoods.getGoodsId());
                updateRecord.setActualSaleNum(newSaleNum);
                count = mchGoodsMapper.updateById(updateRecord);
                if (count < 1) return 0;
            }
        }

        //更新优惠券状态
        if(StringUtils.isNotEmpty(dbTradeOrder.getMemberCouponNo())){

            MemberCoupon memberCouponUpdateRecord = new MemberCoupon();
            memberCouponUpdateRecord.setCouponNo(dbTradeOrder.getMemberCouponNo());
            memberCouponUpdateRecord.setStatus((byte)1);  //更新为【1-已使用】

            //更新失败
            if(!memberCouponService.use(dbTradeOrder.getMemberCouponNo())){
                throw ServiceException.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR);
            }
        }

        //处理订单分润逻辑
        if (dbTradeOrder.getProductType() != null && dbTradeOrder.getProductType() != MchConstant.MCH_TRADE_PRODUCT_TYPE_COD) {
            this.calTradeOrderProfit(getById(tradeOrderId));
        }
        */

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSuccess4Member(String tradeOrderId, String payOrderId, Long income, String openId) {

        MchTradeOrder mchTradeOrder = mchTradeOrderMapper.selectByPrimaryKey(tradeOrderId);
        Long mchId = mchTradeOrder.getMchId();

        Member member = memberService.getOne(new QueryWrapper<Member>().lambda().eq(Member::getMchId, mchId).eq(Member::getWxOpenId, openId));
        if(member == null){ //当会员不存在时，仅插入商户粉丝表
            MchFans mchFans = new MchFans();
            mchFans.setMchId(mchId);
            mchFans.setWxOpenId(openId);
            mchFansService.saveOrUpdate(mchFans);
            return this.updateStatus4Success(tradeOrderId, payOrderId, income, 0L);
        }

        if(member.getStatus() != 1) { //状态非 '正常' 状态, 不做任何处理
            return this.updateStatus4Success(tradeOrderId, payOrderId, income, 0L);
        }

        //查询赠送条件 ，赠送积分/余额
        MchMemberConfig mchMemberConfig = mchMemberConfigService.getById(mchId);

        //计算赠送积分 = （消费金额 / 每消费x元 [取整]）  * 赠送积分基数
        Long givePoints = (mchTradeOrder.getAmount() / mchMemberConfig.getConsumeAmount()) * mchMemberConfig.getConsumeGivePoints();

        if(givePoints <= 0) {   //无赠送积分 直接返回
            return this.updateStatus4Success(tradeOrderId, payOrderId, income, 0L);
        }

        //给会员累加积分
        MemberPoints updateRecord = new MemberPoints();
        updateRecord.setMemberId(member.getMemberId());
        updateRecord.setPoints(givePoints);
        updateRecord.setTotalConsumePoints(givePoints);
        int updateRow = memberPointsService.updatePointByMemberId(updateRecord);
        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //操作失败

        //查询最新会员余额
        MemberPoints dbMemberPoints = memberPointsService.getById(member.getMemberId());

        //记录积分流水
        MemberPointsHistory history = new MemberPointsHistory();
        history.setMemberId(member.getMemberId());  //会员ID
        history.setMemberNo(member.getMemberNo());  //会员No
        history.setChangePoints(givePoints);  //变动积分
        history.setPoints(dbMemberPoints.getPoints() - givePoints);  //变更前积分
        history.setAfterPoints(dbMemberPoints.getPoints());  //变更后积分
        history.setMchId(mchId);  //mchId
        history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //业务单号
        history.setBizType((byte)3);  //业务类型3-消费
        history.setOperatorId(mchTradeOrder.getOperatorId());  //操作员ID
        history.setOperatorName(mchTradeOrder.getOperatorName());  //操作员姓名
        memberPointsHistoryService.save(history);

        if(StringUtils.isNotEmpty(mchTradeOrder.getMemberCouponNo())){ //当包含优惠券核销码
            memberCouponService.use(mchTradeOrder.getMemberCouponNo());
        }

        updateRow = this.updateStatus4Success(tradeOrderId, payOrderId, income, givePoints);
        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //操作失败
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSuccess4MemberBalance(String tradeOrderId, Long memberId, String couponNo) {

        MchTradeOrder mchTradeOrder = mchTradeOrderMapper.selectByPrimaryKey(tradeOrderId);
        Long mchId = mchTradeOrder.getMchId();

        Member member = memberService.getById(memberId);
        if(member.getStatus() != 1) { //状态非 '正常' 状态, 不做任何处理
            return this.updateStatus4Success(tradeOrderId, null, 0L);
        }

        //查询赠送条件 ，赠送积分/余额
        MchMemberConfig mchMemberConfig = mchMemberConfigService.getById(mchId);


        Long givePoints = 0L;
        if(mchMemberConfig != null && mchMemberConfig.getBalancePointsType() == MchConstant.PUB_YES){ //储值消费是否赠送积分
            //计算赠送积分 = （消费金额 / 每消费x元 [取整]）  * 赠送积分基数
            givePoints = (mchTradeOrder.getAmount() / mchMemberConfig.getConsumeAmount()) * mchMemberConfig.getConsumeGivePoints();
        }

        //记录积分规则
        if(givePoints > 0) {
            //给会员累加积分
            MemberPoints updateRecord = new MemberPoints();
            updateRecord.setMemberId(member.getMemberId());
            updateRecord.setPoints(givePoints);
            updateRecord.setTotalConsumePoints(givePoints);
            int updateRow = memberPointsService.updatePointByMemberId(updateRecord);
            if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //操作失败

            //查询最新会员积分
            MemberPoints dbMemberPoints = memberPointsService.getById(member.getMemberId());

            //记录积分流水
            MemberPointsHistory history = new MemberPointsHistory();
            history.setMemberId(member.getMemberId());  //会员ID
            history.setMemberNo(member.getMemberNo());  //会员No
            history.setChangePoints(givePoints);  //变动积分
            history.setPoints(dbMemberPoints.getPoints() - givePoints);  //变更前积分
            history.setAfterPoints(dbMemberPoints.getPoints());  //变更后积分
            history.setMchId(mchId);  //mchId
            history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //业务单号
            history.setBizType((byte)3);  //业务类型3-消费
            history.setOperatorId(mchTradeOrder.getOperatorId());  //操作员ID
            history.setOperatorName(mchTradeOrder.getOperatorName());  //操作员姓名
            memberPointsHistoryService.save(history);
        }

        //给会员扣款
        Long changeAmount = 0 - mchTradeOrder.getAmount();  //消费金额：负数形式
        MemberBalance updateRecord = new MemberBalance();
        updateRecord.setMemberId(member.getMemberId());
        updateRecord.setBalance(changeAmount);
        updateRecord.setTotalConsumeAmount(mchTradeOrder.getAmount());
        int updateRow = memberBalanceService.updateBalanceByMemberId(updateRecord);

        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_SERVICE_SETT_AMOUNT_NOT_SETT);  //操作失败


        //查询最新会员储值余额
        MemberBalance dbMemberBalance = memberBalanceService.getById(member.getMemberId());

        //记录储值流水
        MemberBalanceHistory history = new MemberBalanceHistory();
        history.setMemberId(member.getMemberId());  //会员ID
        history.setMemberNo(member.getMemberNo());  //会员No
        history.setChangeAmount(changeAmount);  //变动余额
        history.setBalance(dbMemberBalance.getBalance() - changeAmount);  //变更前账户余额
        history.setAfterBalance(dbMemberBalance.getBalance());  //变更后账户余额
        history.setGiveAmount(0L);
        history.setMchId(mchId);  //mchId
        history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //业务单号
        history.setBizType((byte)2);  //业务类型2-消费
        history.setPayType((byte)3); //支付类型 3-储值卡
        history.setPageOrigin((byte)4); //来源
        history.setOperatorId(mchTradeOrder.getOperatorId());  //操作员ID
        history.setOperatorName(mchTradeOrder.getOperatorName());  //操作员姓名
        memberBalanceHistoryService.save(history);

        //更新优惠券状态
        if(StringUtils.isNotEmpty(couponNo)){

            MemberCoupon memberCouponUpdateRecord = new MemberCoupon();
            memberCouponUpdateRecord.setCouponNo(couponNo);
            memberCouponUpdateRecord.setStatus(MchConstant.MEMBER_COUPON_USED);  //更新为【1-已使用】

            //更新失败
            if(!memberCouponService.use(couponNo)){
                throw ServiceException.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR);
            }
        }

        updateRow = this.updateStatus4Success(tradeOrderId, null, givePoints);
        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //操作失败
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSuccess4Cod(String tradeOrderId, Long memberId, String couponNo) {

        MchTradeOrder mchTradeOrder = mchTradeOrderMapper.selectByPrimaryKey(tradeOrderId);
        Long mchId = mchTradeOrder.getMchId();

        Member member = memberService.getById(memberId);
        if(member.getStatus() != 1) { //状态非 '正常' 状态, 不做任何处理
            return this.updateStatus4Success(tradeOrderId, null, 0L);
        }

        //查询赠送条件 ，赠送积分/余额
        MchMemberConfig mchMemberConfig = mchMemberConfigService.getById(mchId);

        Long givePoints = 0L;
        if(mchMemberConfig != null && mchMemberConfig.getBalancePointsType() == MchConstant.PUB_YES){ //储值消费是否赠送积分
            //计算赠送积分 = （消费金额 / 每消费x元 [取整]）  * 赠送积分基数
            givePoints = (mchTradeOrder.getAmount() / mchMemberConfig.getConsumeAmount()) * mchMemberConfig.getConsumeGivePoints();
        }

        //记录积分规则
        if(givePoints > 0) {
            //给会员累加积分
            MemberPoints updateRecord = new MemberPoints();
            updateRecord.setMemberId(member.getMemberId());
            updateRecord.setPoints(givePoints);
            updateRecord.setTotalConsumePoints(givePoints);
            int updateRow = memberPointsService.updatePointByMemberId(updateRecord);
            if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //操作失败

            //查询最新会员积分
            MemberPoints dbMemberPoints = memberPointsService.getById(member.getMemberId());

            //记录积分流水
            MemberPointsHistory history = new MemberPointsHistory();
            history.setMemberId(member.getMemberId());  //会员ID
            history.setMemberNo(member.getMemberNo());  //会员No
            history.setChangePoints(givePoints);  //变动积分
            history.setPoints(dbMemberPoints.getPoints() - givePoints);  //变更前积分
            history.setAfterPoints(dbMemberPoints.getPoints());  //变更后积分
            history.setMchId(mchId);  //mchId
            history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //业务单号
            history.setBizType((byte)3);  //业务类型3-消费
            history.setOperatorId(mchTradeOrder.getOperatorId());  //操作员ID
            history.setOperatorName(mchTradeOrder.getOperatorName());  //操作员姓名
            memberPointsHistoryService.save(history);
        }

        //更新优惠券状态
        if(StringUtils.isNotEmpty(couponNo)){
            MemberCoupon memberCouponUpdateRecord = new MemberCoupon();
            memberCouponUpdateRecord.setCouponNo(couponNo);
            memberCouponUpdateRecord.setStatus(MchConstant.MEMBER_COUPON_USED);  //更新为【1-已使用】

            //更新失败
            if(!memberCouponService.use(couponNo)){
                throw ServiceException.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR);
            }
        }

        int updateRow = this.updateStatus4Success(tradeOrderId, null, givePoints);
        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //操作失败
        return 1;
    }

    @Override
    public int updateStatus4Fail(String tradeOrderId) {
        MchTradeOrder mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setStatus(MchConstant.TRADE_ORDER_STATUS_FAIL);
        MchTradeOrderExample example = new MchTradeOrderExample();
        MchTradeOrderExample.Criteria criteria = example.createCriteria();
        criteria.andTradeOrderIdEqualTo(tradeOrderId);
        criteria.andStatusEqualTo(MchConstant.TRADE_ORDER_STATUS_ING);
        return mchTradeOrderMapper.updateByExampleSelective(mchTradeOrder, example);
    }

    @Override
    public Map count4All(Long mchId, Long storeId, String tradeOrderId, String payOrderId, Byte tradeType, Byte status, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(mchId != null) param.put("mchId", mchId);
        if(storeId != null) param.put("storeId", storeId);
        if(StringUtils.isNotBlank(tradeOrderId)) param.put("tradeOrderId", tradeOrderId);
        if(StringUtils.isNotBlank(payOrderId)) param.put("payOrderId", payOrderId);
        if(tradeType != null && tradeType != -99) param.put("tradeType", tradeType);
        if(status != null && status != -99) param.put("status", status);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return mchTradeOrderMapper.count4All(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSuccess4MemberRecharge(String tradeOrderId, String payOrderId, Long income) {

        //获取充值订单
        MchTradeOrder mchTradeOrder = mchTradeOrderMapper.selectByPrimaryKey(tradeOrderId);

        //会员账号ID
        Long memberId = mchTradeOrder.getMemberId();
        //充值规则ID
        Long ruleId = mchTradeOrder.getRuleId();

        Member member = memberService.getById(memberId);
        if(member.getStatus() != 1) { //状态非 '正常' 状态, 不做任何处理
            return this.updateStatus4Success(tradeOrderId, payOrderId, income, 0L);
        }

        Long givePoints = 0L;  //赠送积分
        Long giveBalance = 0L;  //赠送金额

        //若ruleId为null，则不做任何赠送
        if (ruleId != null) {
            MchMemberRechargeRule rechargeRule = mchMemberRechargeRuleService.getById(ruleId);
            if (rechargeRule == null || rechargeRule.getStatus() != MchConstant.PUB_YES) {
                return this.updateStatus4Success(tradeOrderId, payOrderId, income, 0L);  //优惠券规则不可用，不赠送
            }

            Byte ruleType = rechargeRule.getRuleType();

            //根据充值规则赠送余额或积分
            if (ruleType == MchConstant.MEMBER_RECHARGE_RULE_TYPE_BALANCE) {  //赠余额
                //充值赠送金额
                giveBalance = rechargeRule.getGivePoints();//赠送金额：正数形式
                //给会员加款：赠送金额
                MemberBalance updateGiveRecord = new MemberBalance();
                updateGiveRecord.setMemberId(memberId);
                updateGiveRecord.setBalance(giveBalance);
                updateGiveRecord.setTotalGiveAmount(giveBalance);
                int updateRow = memberBalanceService.updateBalanceByMemberId(updateGiveRecord);

                if (updateRow <= 0) throw new ServiceException(RetEnum.RET_SERVICE_SETT_AMOUNT_NOT_SETT);  //操作失败

                //查询最新会员储值余额
                MemberBalance dbMemberBalance = memberBalanceService.getById(memberId);

                //记录储值流水
                MemberBalanceHistory history = new MemberBalanceHistory();
                history.setMemberId(memberId);  //会员ID
                history.setMemberNo(member.getMemberNo());  //会员No
                history.setChangeAmount(giveBalance);  //变动余额
                history.setBalance(dbMemberBalance.getBalance() - giveBalance);  //变更前账户余额
                history.setAfterBalance(dbMemberBalance.getBalance());  //变更后账户余额
                history.setGiveAmount(giveBalance);
                history.setMchId(member.getMchId());  //mchId
                history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //业务单号
                history.setBizType(MchConstant.MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_GIVE);  //业务类型  5-赠送
                history.setPayType(null); //支付类型 3-储值卡
                history.setPageOrigin(null); //来源
                history.setOperatorId(mchTradeOrder.getOperatorId());  //操作员ID
                history.setOperatorName(mchTradeOrder.getOperatorName());  //操作员姓名
                memberBalanceHistoryService.save(history);

            } else if (ruleType == MchConstant.MEMBER_RECHARGE_RULE_TYPE_POINT) {  //赠积分
                //充值赠送积分
                givePoints = rechargeRule.getGivePoints();
                //给会员累加积分
                MemberPoints updateRecord = new MemberPoints();
                updateRecord.setMemberId(memberId);
                updateRecord.setPoints(givePoints);
                updateRecord.setTotalGivePoints(givePoints);
                int updateRow = memberPointsService.updatePointByMemberId(updateRecord);
                if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //操作失败

                //查询最新会员积分
                MemberPoints dbMemberPoints = memberPointsService.getById(member.getMemberId());

                //记录积分流水
                MemberPointsHistory history = new MemberPointsHistory();
                history.setMemberId(member.getMemberId());  //会员ID
                history.setMemberNo(member.getMemberNo());  //会员No
                history.setChangePoints(givePoints);  //变动积分
                history.setPoints(dbMemberPoints.getPoints() - givePoints);  //变更前积分
                history.setAfterPoints(dbMemberPoints.getPoints());  //变更后积分
                history.setMchId(member.getMchId());  //mchId
                history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //业务单号
                history.setBizType(MchConstant.MCH_VIP_POINTS_HISTORY_BIZ_TYPE_RECHARGE);  //业务类型2-充值赠送
                history.setOperatorId(mchTradeOrder.getOperatorId());  //操作员ID
                history.setOperatorName(mchTradeOrder.getOperatorName());  //操作员姓名
                memberPointsHistoryService.save(history);

            }
        }
        /*else if (ruleType == MchConstant.MEMBER_RECHARGE_RULE_TYPE_COUPON){
        }*/

        //给会员加款：充值金额
        Long changeAmount = mchTradeOrder.getAmount();  //充值金额：正数形式
        MemberBalance updateRecord = new MemberBalance();
        updateRecord.setMemberId(memberId);
        updateRecord.setBalance(changeAmount);
        updateRecord.setTotalRechargeAmount(mchTradeOrder.getAmount());
        int updateRow = memberBalanceService.updateBalanceByMemberId(updateRecord);

        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_SERVICE_SETT_AMOUNT_NOT_SETT);  //操作失败


        //查询最新会员储值余额
        MemberBalance dbMemberBalance = memberBalanceService.getById(memberId);

        //记录储值流水
        MemberBalanceHistory history = new MemberBalanceHistory();
        history.setMemberId(memberId);  //会员ID
        history.setMemberNo(member.getMemberNo());  //会员No
        history.setChangeAmount(changeAmount);  //变动余额
        history.setBalance(dbMemberBalance.getBalance() - changeAmount);  //变更前账户余额
        history.setAfterBalance(dbMemberBalance.getBalance());  //变更后账户余额
        history.setGiveAmount(0L);
        history.setMchId(member.getMchId());  //mchId
        history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //业务单号
        history.setBizType(MchConstant.MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_RECHARGE);  //业务类型  1-充值
        history.setPayType(null); //支付类型 3-储值卡
        history.setPageOrigin(null); //来源
        history.setOperatorId(mchTradeOrder.getOperatorId());  //操作员ID
        history.setOperatorName(mchTradeOrder.getOperatorName());  //操作员姓名
        memberBalanceHistoryService.save(history);

        updateRow = this.updateStatus4Success(tradeOrderId, payOrderId, income, givePoints);
        if(updateRow <= 0) throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL); //操作失败

        if(giveBalance >= 0){ //充值赠送余额,  将优惠金额更新至discountAmount字段
            MchTradeOrder updateOrderRecord = new MchTradeOrder();
            updateOrderRecord.setTradeOrderId(tradeOrderId);
            updateOrderRecord.setDiscountAmount(giveBalance);
            this.updateById(updateOrderRecord);
        }

        return 1;
    }

    @Override
    public int update(MchTradeOrder mchTradeOrder) {
        return mchTradeOrderMapper.updateByPrimaryKeySelective(mchTradeOrder);
    }


    @Override
    public BigDecimal sumTradeAmount(Map condition) {
        return baseMapper.sumTradeAmount(condition);
    }

    @Override
    public BigDecimal sumRealAmount(Map condition) {
        return baseMapper.sumRealAmount(condition);
    }

    @Override
    public BigDecimal sumMemberAmount(Map condition) {
        return baseMapper.sumMemberAmount(condition);
    }

    @Override
    public BigDecimal sumNotMemberAmount(Map condition) {
        return baseMapper.sumNotMemberAmount(condition);
    }

    @Override
    public BigDecimal sumDiscountAmount(Map condition) {
        return baseMapper.sumDiscountAmount(condition);
    }

    @Override
    public BigDecimal sumRefundAmount(Map condition) {
        return baseMapper.sumRefundAmount(condition);
    }

    @Override
    public BigDecimal sumRechargeAmount(Map condition) {
        return baseMapper.sumRechargeAmount(condition);
    }

    @Override
    public BigDecimal sumGivePoints(Map condition) {
        return baseMapper.sumGivePoints(condition);
    }

    @Override
    public BigDecimal sumRechargeGiveAmount(Map condition) {
        return baseMapper.sumRechargeGiveAmount(condition);
    }

    @Override
    public Long countTrade(Map condition) {
        return baseMapper.countTrade(condition);
    }

    @Override
    public Long countRefund(Map condition) {
        return baseMapper.countRefund(condition);
    }

    @Override
    public Long countRecharge(Map condition) {
        return baseMapper.countRecharge(condition);
    }

    @Override
    public List<Map> countByGroupProductType(Map condition) {
        return baseMapper.countByGroupProductType(condition);
    }

    @Override
    public List<Integer> countTradeOrderByMember(Map condition) {
        return baseMapper.countTradeOrderByMember(condition);
    }




    @Override
    public Long countMemberActive(Date thisStartQueryTime, Date thisEndQueryTime, Long queryMchId) {
        return baseMapper.countMemberActive(thisStartQueryTime, thisEndQueryTime, queryMchId);
    }


    /** 计算服务商 / 代理商的分润 **/
    private void calTradeOrderProfit(MchTradeOrder mchTradeOrder){

        //类型为： [现金收款] | [会员卡余额支付] 无需计算分润逻辑
        if(mchTradeOrder.getProductType() == MchConstant.MCH_TRADE_PRODUCT_TYPE_CASH ||
                mchTradeOrder.getProductType() == MchConstant.MCH_TRADE_PRODUCT_TYPE_MEMBER_CARD ){
            logger.info("订单[{}], 支付类型为：{}, 无需计算分润。", mchTradeOrder.getTradeOrderId(), MchConstant.MCH_TRADE_PRODUCT_TYPE_MAP.get(mchTradeOrder.getProductType()));
            return ;
        }

        Integer productId = mchTradeOrder.getProductId();
        if(mchTradeOrder.getProductId() == null ||productId <= 0 ){ //如果不存在产品信息， 数据有误
            logger.info("订单[{}], 支付类型为：{}, product = {}, 数据有误， 无法计算订单分润！", mchTradeOrder.getTradeOrderId(), mchTradeOrder.getProductType(), productId);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        Long mchId = mchTradeOrder.getMchId();
        MchInfo mchInfo = mchInfoService.findByMchId(mchTradeOrder.getMchId());
        if(mchInfo.getType() == MchConstant.MCH_TYPE_PRIVATE) return ; //私有商户， 不计算分润

        //获取商户的费率
        FeeScale mchFeeScale = feeScaleService.getPayFeeByMch(mchId, productId);
        FeeScale isvFeeScale = feeScaleService.getPayFeeByIsv(mchTradeOrder.getIsvId(), productId);


        List<AgentInfo> allAgent = new ArrayList<>();
        if(mchTradeOrder.getAgentId() != null){
            allAgent = agentInfoService.selectAgentsProfitRate(mchTradeOrder.getAgentId());
        }

        Map<String, OrderProfitDetail> calResult = orderProfitDetailService.recalculateTradeOrderProfit(mchTradeOrder, mchFeeScale.getFee(), isvFeeScale.getFee(), allAgent);

        for(OrderProfitDetail item : calResult.values()){
            orderProfitDetailService.insert(item);
        }
    }

    @Override
    public Map<String, Long[]> selectByIsvDevice(Long isvId, List<String> isvDeviceNoList){

        Map<String, Long[]> result = new HashMap<>();

        List<Map> dbResult = baseMapper.selectByIsvDevice(isvId, isvDeviceNoList);
        dbResult.stream().forEach(map -> {

            JSONObject json = (JSONObject) JSONObject.toJSON(map);

            String isvDeviceNo = json.getString("isvDeviceNo");
            Long orderCount = json.getLong("orderCount");
            Long orderTotalAmount = json.getBigDecimal("orderTotalAmount").longValue();
            result.put(isvDeviceNo, new Long[]{orderCount, orderTotalAmount});
        });

        return result;
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void depositConsumeFinish(String mchTradeOrderId, String payOrderId, Long consumeAmount){

        MchTradeOrder updateRecord = new MchTradeOrder();
        updateRecord.setStatus(MchConstant.TRADE_ORDER_STATUS_SUCCESS);  //支付完成
        updateRecord.setOrderAmount(consumeAmount);  //订单金额
        updateRecord.setAmount(consumeAmount);  //实际支付金额

        LambdaUpdateWrapper<MchTradeOrder> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MchTradeOrder::getTradeOrderId, mchTradeOrderId);
        lambdaUpdateWrapper.eq(MchTradeOrder::getStatus, MchConstant.TRADE_ORDER_STATUS_DEPOSIT_ING);
        lambdaUpdateWrapper.eq(MchTradeOrder::getDepositMode, MchConstant.PUB_YES);

        boolean isSuccess = this.update(updateRecord, lambdaUpdateWrapper);
        if(!isSuccess) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

        //变更支付订单状态
        PayOrder updateRecordPayOrder = new PayOrder();
        updateRecordPayOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);  //支付完成
        updateRecordPayOrder.setAmount(consumeAmount);  //实际支付金额

        LambdaUpdateWrapper<PayOrder> lambdaUpdateWrapperPayOrder = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapperPayOrder.eq(PayOrder::getPayOrderId, payOrderId);
        lambdaUpdateWrapperPayOrder.eq(PayOrder::getStatus, PayConstant.PAY_STATUS_DEPOSIT_ING);
        lambdaUpdateWrapperPayOrder.eq(PayOrder::getDepositMode, MchConstant.PUB_YES);
        isSuccess = payOrderService.update(updateRecordPayOrder, lambdaUpdateWrapperPayOrder);
        if(!isSuccess) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void depositReverse(String mchTradeOrderId, String payOrderId){

        MchTradeOrder updateRecord = new MchTradeOrder();
        updateRecord.setStatus(MchConstant.TRADE_ORDER_STATUS_DEPOSIT_REVERSE);  //押金退还（撤销订单）

        LambdaUpdateWrapper<MchTradeOrder> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MchTradeOrder::getTradeOrderId, mchTradeOrderId);
        lambdaUpdateWrapper.eq(MchTradeOrder::getStatus, MchConstant.TRADE_ORDER_STATUS_DEPOSIT_ING);
        lambdaUpdateWrapper.eq(MchTradeOrder::getDepositMode, MchConstant.PUB_YES);

        boolean isSuccess = this.update(updateRecord, lambdaUpdateWrapper);
        if(!isSuccess) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

        //变更支付订单状态
        PayOrder updateRecordPayOrder = new PayOrder();
        updateRecordPayOrder.setStatus(PayConstant.PAY_STATUS_DEPOSIT_REVERSE);  //押金退还（撤销订单）

        LambdaUpdateWrapper<PayOrder> lambdaUpdateWrapperPayOrder = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapperPayOrder.eq(PayOrder::getPayOrderId, payOrderId);
        lambdaUpdateWrapperPayOrder.eq(PayOrder::getStatus, PayConstant.PAY_STATUS_DEPOSIT_ING);
        lambdaUpdateWrapperPayOrder.eq(PayOrder::getDepositMode, MchConstant.PUB_YES);
        isSuccess = payOrderService.update(updateRecordPayOrder, lambdaUpdateWrapperPayOrder);
        if(!isSuccess) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
    }

    @Override
    public BigDecimal sumMbrAmount(Map condition) {
        return baseMapper.sumMbrAmount(condition);
    }

    @Override
    public BigDecimal sumMbrRefundAmount(Map condition) {
        return baseMapper.sumMbrRefundAmount(condition);
    }

    @Override
    public Long countMbrTrade(Map condition) {
        return baseMapper.countMbrTrade(condition);
    }

    @Override
    public Long countMbrRecharge(Map condition) {
        return baseMapper.countMbrRecharge(condition);
    }

    @Override
    public Long countMbrRefund(Map condition) {
        return baseMapper.countMbrRefund(condition);
    }

    @Override
    public int countByMemberAndStatus(Long memberId, Byte authFrom, Byte status, Byte postStatus) {
        LambdaQueryWrapper<MchTradeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchTradeOrder::getMemberId, memberId);
        queryWrapper.eq(MchTradeOrder::getIndustryType, authFrom);
        if (status != null) queryWrapper.eq(MchTradeOrder::getStatus, status);
        if (postStatus != null) queryWrapper.eq(MchTradeOrder::getPostStatus, postStatus);
        queryWrapper.eq(MchTradeOrder::getDeleteFlag, MchConstant.PUB_NO);

        return baseMapper.selectCount(queryWrapper);
    }

    void setCriteria(MchTradeOrderExample.Criteria criteria, MchTradeOrder mchTradeOrder) {
        setCriteria(criteria, mchTradeOrder, null, null);
    }

    void setCriteria(MchTradeOrderExample.Criteria criteria, MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd) {
        if(mchTradeOrder != null) {
            if(mchTradeOrder.getMchId() != null) criteria.andMchIdEqualTo(mchTradeOrder.getMchId());
            if(mchTradeOrder.getIsvId() != null) criteria.andIsvIdEqualTo(mchTradeOrder.getIsvId());
            if(mchTradeOrder.getAgentId() != null) criteria.andAgentIdEqualTo(mchTradeOrder.getAgentId());
            if(mchTradeOrder.getTradeType() != null && mchTradeOrder.getTradeType() != -99) criteria.andTradeTypeEqualTo(mchTradeOrder.getTradeType());
            if(mchTradeOrder.getDepositMode() != null && mchTradeOrder.getDepositMode() != -99) criteria.andDepositModeEqualTo(mchTradeOrder.getDepositMode());
            if(StringUtils.isNotBlank(mchTradeOrder.getAppId())) criteria.andAppIdEqualTo(mchTradeOrder.getAppId());
            if(StringUtils.isNotBlank(mchTradeOrder.getTradeOrderId())) criteria.andTradeOrderIdEqualTo(mchTradeOrder.getTradeOrderId());
            if(mchTradeOrder.getStatus() != null && mchTradeOrder.getStatus() != -99) criteria.andStatusEqualTo(mchTradeOrder.getStatus());
            if(StringUtils.isNotBlank(mchTradeOrder.getPayOrderId())) criteria.andPayOrderIdEqualTo(mchTradeOrder.getPayOrderId());
            if(mchTradeOrder.getStoreId() != null && mchTradeOrder.getStoreId() != -99) criteria.andStoreIdEqualTo(mchTradeOrder.getStoreId());
            if(StringUtils.isNotEmpty(mchTradeOrder.getStoreNo())) criteria.andStoreNoEqualTo(mchTradeOrder.getStoreNo());
            if(mchTradeOrder.getMemberId() != null) criteria.andMemberIdEqualTo(mchTradeOrder.getMemberId());
            if(StringUtils.isNotEmpty(mchTradeOrder.getOperatorId())) criteria.andOperatorIdEqualTo(mchTradeOrder.getOperatorId());
            if(mchTradeOrder.getProductId() != null) criteria.andProductIdEqualTo(mchTradeOrder.getProductId());
            if(mchTradeOrder.getProductType() != null) criteria.andProductTypeEqualTo(mchTradeOrder.getProductType());
            if(mchTradeOrder.getProvinceCode() != null) criteria.andProvinceCodeEqualTo(mchTradeOrder.getProvinceCode());
            if(mchTradeOrder.getCityCode() != null) criteria.andCityCodeEqualTo(mchTradeOrder.getCityCode());
            if(mchTradeOrder.getPostType() != null) criteria.andPostTypeEqualTo(mchTradeOrder.getPostType());
        }
        if(createTimeStart != null) {
            criteria.andCreateTimeGreaterThanOrEqualTo(createTimeStart);
        }
        if(createTimeEnd != null) {
            criteria.andCreateTimeLessThanOrEqualTo(createTimeEnd);
        }

        if(mchTradeOrder.getPs() != null){
            if(StringUtils.isNotEmpty(mchTradeOrder.getPsStringVal("statusInString"))){

                List<Byte> queryStatusIn = new ArrayList<>();
                String[] arr = mchTradeOrder.getPsStringVal("statusInString").split(",");
                for (String s : arr) {
                    queryStatusIn.add(Byte.parseByte(s));
                }
                criteria.andStatusIn(queryStatusIn);
            }

            if(mchTradeOrder.getPsListVal("agentIdIn") != null){
                criteria.andAgentIdIn(mchTradeOrder.getPsListVal("agentIdIn"));
            }
        }


    }

    //===================================================================开始纳呈支付新增=====================================================================

    @Override
    public List<Map> countByGroupProductTypeForNc(Map condition) {
        return baseMapper.countByGroupProductTypeForNc(condition);
    }

    @Override
    public Long countMchForTrade(List<Long> mchIds) {
        if (CollectionUtils.isEmpty(mchIds)) {
            return null;
        }
        return mchTradeOrderMapper.countMchForTrade(mchIds);
    }

    //===================================================================结束纳呈支付新增=====================================================================

}

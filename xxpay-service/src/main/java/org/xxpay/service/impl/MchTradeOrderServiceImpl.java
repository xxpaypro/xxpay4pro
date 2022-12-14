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

    /** ????????????  **/
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
        mchTradeOrder.setTradeType(tradeType); //???????????? 1-?????? 2-??????
        mchTradeOrder.setAppId("");   //appId
        mchTradeOrder.setDevice(""); //??????
        mchTradeOrder.setGoodsId(""); //??????ID
        mchTradeOrder.setProductId(productId); //????????????ID
        mchTradeOrder.setProductType(tradeProductType); //??????????????????

        mchTradeOrder.setStatus(MchConstant.TRADE_ORDER_STATUS_ING); //????????????, ???????????????
        mchTradeOrder.setSettTaskStatus(MchConstant.ISV_SETT_STATUS_WAIT);  //????????????????????? ?????????

        mchTradeOrder.setMchId(mchInfo.getMchId());  //MchId
        mchTradeOrder.setAgentId(mchInfo.getAgentId()); //agentId
        mchTradeOrder.setIsvId(mchInfo.getIsvId()); //isvId

        mchTradeOrder.setProvinceCode(mchInfo.getProvinceCode()); //?????????
        mchTradeOrder.setCityCode(mchInfo.getCityCode()); //?????????
        mchTradeOrder.setAreaCode(mchInfo.getAreaCode()); //?????????
        mchTradeOrder.setAreaInfo(mchInfo.getAreaInfo()); //???????????????

        mchTradeOrder.setClientIp(ip); //?????????IP
        mchTradeOrder.setOrderAmount(requiredAmount); //????????????
        mchTradeOrder.setAmount(realAmount);  //??????????????????
        mchTradeOrder.setDiscountAmount(requiredAmount - realAmount); //????????????

        String prefix = MchConstant.TRADE_TYPE_PAY == tradeType ? "??????|" : "??????|";

        if(productId == null){ //???????????????
            mchTradeOrder.setSubject(prefix + "?????????????????????"); //????????????
            mchTradeOrder.setBody(prefix + "?????????????????????"); //??????????????????
        }else{

            if(PayConstant.PAY_PRODUCT_WX_JSAPI == productId){
                mchTradeOrder.setSubject(prefix + "??????????????????");
                mchTradeOrder.setBody(prefix + "??????????????????");
            }else if(PayConstant.PAY_PRODUCT_ALIPAY_JSAPI == productId){
                mchTradeOrder.setSubject(prefix + "?????????????????????");
                mchTradeOrder.setBody(prefix + "?????????????????????");

            }else if(PayConstant.PAY_PRODUCT_WX_BAR == productId){
                mchTradeOrder.setSubject(prefix + "??????????????????");
                mchTradeOrder.setBody(prefix + "??????????????????");

            }else if(PayConstant.PAY_PRODUCT_ALIPAY_BAR == productId){
                mchTradeOrder.setSubject(prefix + "?????????????????????");
                mchTradeOrder.setBody(prefix + "?????????????????????");
            }else if(PayConstant.PAY_PRODUCT_UNIONPAY_BAR == productId){
                mchTradeOrder.setSubject(prefix + "?????????????????????");
                mchTradeOrder.setBody(prefix + "?????????????????????");
            }else if(PayConstant.PAY_PRODUCT_WX_MINI_PROGRAM == productId){
                mchTradeOrder.setSubject(prefix + "?????????????????????");
                mchTradeOrder.setBody(prefix + "?????????????????????");
            }else if(PayConstant.PAY_PRODUCT_JD_H5 == productId){
                mchTradeOrder.setSubject(prefix + "????????????");
                mchTradeOrder.setBody(prefix + "????????????");
            }
        }


        mchTradeOrder.setStoreId(storeId);
        if(storeId != null){
            MchStore mchStore = mchStoreService.getById(storeId);
            mchTradeOrder.setStoreNo(mchStore.getStoreNo()); //??????No
            mchTradeOrder.setStoreName(mchStore.getStoreName()); //????????????
        }

        mchTradeOrder.setOperatorId(operatorId != null ? operatorId + "" : null);
        if(operatorId != null){
            SysUser subUser = sysService.findByUserId(operatorId);
            mchTradeOrder.setOperatorId(operatorId + ""); //?????????ID
            mchTradeOrder.setOperatorName(subUser.getLoginUserName()); //???????????????
        }

        if(callback != null){
            callback.setRecordBeforeInsert(mchTradeOrder);
        }

        if(StringUtils.isEmpty(mchTradeOrder.getTradeOrderId())){
            mchTradeOrder.setTradeOrderId(MySeq.getTrade());  ////???????????????
        }

        mchTradeOrderMapper.insertSelective(mchTradeOrder); //??????

        return mchTradeOrder;
    }

    @Override
    public MchTradeOrder insertTradeOrderHis(MchInfo mchInfo, Byte tradeType, Long requiredAmount, Long realAmount,
                                          Byte tradeProductType, Integer productId, String ip, String mchOrderNo,
                                          Long operatorId, Long storeId, AddTradeOrderCallBack callback) {

        MchTradeOrder mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setTradeType(tradeType); //???????????? 1-?????? 2-??????
        mchTradeOrder.setAppId("");   //appId
        mchTradeOrder.setDevice(""); //??????
        mchTradeOrder.setGoodsId(""); //??????ID
        mchTradeOrder.setProductId(productId); //????????????ID
        mchTradeOrder.setProductType(tradeProductType); //??????????????????

        mchTradeOrder.setStatus(MchConstant.TRADE_ORDER_STATUS_ING); //????????????, ???????????????
        mchTradeOrder.setSettTaskStatus(MchConstant.ISV_SETT_STATUS_WAIT);  //????????????????????? ?????????

        mchTradeOrder.setMchId(mchInfo.getMchId());  //MchId
        mchTradeOrder.setAgentId(mchInfo.getAgentId()); //agentId
        mchTradeOrder.setIsvId(mchInfo.getIsvId()); //isvId

        mchTradeOrder.setProvinceCode(mchInfo.getProvinceCode()); //?????????
        mchTradeOrder.setCityCode(mchInfo.getCityCode()); //?????????
        mchTradeOrder.setAreaCode(mchInfo.getAreaCode()); //?????????
        mchTradeOrder.setAreaInfo(mchInfo.getAreaInfo()); //???????????????

        mchTradeOrder.setClientIp(ip); //?????????IP
        mchTradeOrder.setOrderAmount(requiredAmount); //????????????
        mchTradeOrder.setAmount(realAmount);  //??????????????????
        mchTradeOrder.setDiscountAmount(requiredAmount - realAmount); //????????????

        String prefix = MchConstant.TRADE_TYPE_PAY == tradeType ? "??????|" : "??????|";

        if(productId == null){ //???????????????
            mchTradeOrder.setSubject(prefix + "?????????????????????"); //????????????
            mchTradeOrder.setBody(prefix + "?????????????????????"); //??????????????????
        }else{

            if(PayConstant.PAY_PRODUCT_WX_JSAPI == productId){
                mchTradeOrder.setSubject(prefix + "??????????????????");
                mchTradeOrder.setBody(prefix + "??????????????????");
            }else if(PayConstant.PAY_PRODUCT_ALIPAY_JSAPI == productId){
                mchTradeOrder.setSubject(prefix + "?????????????????????");
                mchTradeOrder.setBody(prefix + "?????????????????????");

            }else if(PayConstant.PAY_PRODUCT_WX_BAR == productId){
                mchTradeOrder.setSubject(prefix + "??????????????????");
                mchTradeOrder.setBody(prefix + "??????????????????");

            }else if(PayConstant.PAY_PRODUCT_ALIPAY_BAR == productId){
                mchTradeOrder.setSubject(prefix + "?????????????????????");
                mchTradeOrder.setBody(prefix + "?????????????????????");
            }else if(PayConstant.PAY_PRODUCT_UNIONPAY_BAR == productId){
                mchTradeOrder.setSubject(prefix + "?????????????????????");
                mchTradeOrder.setBody(prefix + "?????????????????????");
            }else if(PayConstant.PAY_PRODUCT_WX_MINI_PROGRAM == productId){
                mchTradeOrder.setSubject(prefix + "?????????????????????");
                mchTradeOrder.setBody(prefix + "?????????????????????");
            }else if(PayConstant.PAY_PRODUCT_JD_H5 == productId){
                mchTradeOrder.setSubject(prefix + "????????????");
                mchTradeOrder.setBody(prefix + "????????????");
            }
        }


        mchTradeOrder.setStoreId(storeId);
        if(storeId != null){
            MchStore mchStore = mchStoreService.getById(storeId);
            mchTradeOrder.setStoreNo(mchStore.getStoreNo()); //??????No
            mchTradeOrder.setStoreName(mchStore.getStoreName()); //????????????
        }

        mchTradeOrder.setOperatorId(operatorId != null ? operatorId + "" : null);
        if(operatorId != null){
            SysUser subUser = sysService.findByUserId(operatorId);
            mchTradeOrder.setOperatorId(operatorId + ""); //?????????ID
            mchTradeOrder.setOperatorName(subUser.getLoginUserName()); //???????????????
        }

        if(callback != null){
            callback.setRecordBeforeInsert(mchTradeOrder);
        }

        if(StringUtils.isEmpty(mchTradeOrder.getTradeOrderId())){
            mchTradeOrder.setTradeOrderId(mchOrderNo);  ////???????????????
        }

        mchTradeOrderMapper.insertSelective(mchTradeOrder); //??????

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
        mchTradeOrder.setTradeType(tradeType); //???????????? 1-?????? 2-??????
        mchTradeOrder.setAppId("");   //appId
        mchTradeOrder.setDevice(""); //??????
        mchTradeOrder.setGoodsId(""); //??????ID
        mchTradeOrder.setProductId(productId); //????????????ID
        mchTradeOrder.setProductType(tradeProductType); //??????????????????

        mchTradeOrder.setStatus(MchConstant.TRADE_ORDER_STATUS_ING); //????????????, ???????????????
        mchTradeOrder.setSettTaskStatus(MchConstant.ISV_SETT_STATUS_WAIT);  //????????????????????? ?????????

        mchTradeOrder.setMchId(mchInfo.getMchId());  //MchId
        mchTradeOrder.setAgentId(mchInfo.getAgentId()); //agentId
        mchTradeOrder.setIsvId(mchInfo.getIsvId()); //isvId

        mchTradeOrder.setClientIp(ip); //?????????IP

        List<Long> cartIdList = new LinkedList();
        List<String> goodsImageList = new LinkedList();
        Long orderAmount = 0L;
        Long goodsPrice = 0L;

        //?????????????????????goodsDesc?????????[{"cartId": "1000", "goodsId": "1000", "goodsNum": "2", "goodsProps": "1,2"}]
        JSONArray jsonArray = JSONArray.parseArray(goodsDesc);
        JSONArray goodsJSONArray = new JSONArray();
        //??????????????????????????????????????????????????????ID??????????????????????????????json
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

            //??????????????????
            goodsJson.put("goodsName", mchGoods.getGoodsName());
            goodsJson.put("imgPathMain", mchGoods.getImgPathMain());
            goodsJson.put("cellingPrice", mchGoods.getCellingPrice());
            goodsJson.put("frozenNum", 0);

            //????????????????????????
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

        //????????????
        mchTradeOrder.setGoodsDesc(goodsJSONArray.toJSONString());

        orderAmount += postPrice;

        //????????????????????????????????????????????????????????? ???????????? = ???????????? + ???????????? = ???????????? + ??????
        if (!requiredAmount.equals(orderAmount)) {
            throw ServiceException.build(RetEnum.RET_MCH_STORE_SPEAKER_CODE_ERROR);
        }

        mchTradeOrder.setOrderAmount(requiredAmount); //????????????
        mchTradeOrder.setAmount(realAmount);  //??????????????????
        mchTradeOrder.setDiscountAmount(requiredAmount - realAmount); //????????????
        mchTradeOrder.setImgPathMain(goodsImageList.get(0));

        mchTradeOrder.setPostType(postType);//1-?????? 2-??????
        mchTradeOrder.setPostPrice(postPrice); //??????

        String prefix = MchConstant.TRADE_TYPE_PAY == tradeType ? "??????|" : "??????|";

        mchTradeOrder.setSubject(prefix + "?????????????????????");
        mchTradeOrder.setBody(prefix + "?????????????????????");

        //????????????????????????????????????????????????????????????????????????????????????????????????ID???????????????
        if (storeId == null) {
            SysUser mchUser = sysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_MCH, mchInfo.getLoginMobile());
            storeId = mchUser.getStoreId();
        }

        mchTradeOrder.setStoreId(storeId);
        if(storeId != null){
            MchStore mchStore = mchStoreService.getById(storeId);
            if (mchStore == null || !mchStore.getMchId().equals(mchInfo.getMchId())) throw ServiceException.build(RetEnum.RET_MCH_STORE_NOT_EXIST);
            mchTradeOrder.setStoreNo(mchStore.getStoreNo()); //??????No
            mchTradeOrder.setStoreName(mchStore.getStoreName()); //????????????
        }

        mchTradeOrder.setStoreAreaId(storeAreaId);//????????????ID
        if (storeAreaId != null && postType == MchConstant.MCH_POST_TYPE_TANGSHI) {
            MchStoreAreaManage mchStoreArea = mchStoreAreaManageService.getById(storeAreaId);
            if (mchStoreArea == null || !mchStoreArea.getMchId().equals(mchInfo.getMchId())) throw ServiceException.build(RetEnum.RET_MBR_AREA_ERROR);
            mchTradeOrder.setAreaName(mchStoreArea.getAreaName());
        }

        mchTradeOrder.setAddressId(addressId);//????????????ID
        if(addressId != null && postType == MchConstant.MCH_POST_TYPE_WAIMAI) {
            MchReceiveAddress receiveAddress = mchReceiveAddressService.getById(addressId);
            if (receiveAddress == null || !receiveAddress.getMchId().equals(mchInfo.getMchId())) throw ServiceException.build(RetEnum.RET_MBR_RECEIVE_ADDRESS_ERROR);
            mchTradeOrder.setProvinceCode(receiveAddress.getProvinceCode()); //?????????
            mchTradeOrder.setCityCode(receiveAddress.getCityCode()); //?????????
            mchTradeOrder.setAreaCode(receiveAddress.getAreaCode()); //?????????
            mchTradeOrder.setAreaInfo(receiveAddress.getAreaInfo()); //???????????????
            mchTradeOrder.setAddress(receiveAddress.getAddress());//????????????
            mchTradeOrder.setReceiveName(receiveAddress.getContactName());//?????????
            mchTradeOrder.setReceiveTel(receiveAddress.getTel());//???????????????
        }


        if (callBack != null) {
            callBack.setRecordBeforeInsert(mchTradeOrder);
        }

        if(StringUtils.isEmpty(mchTradeOrder.getTradeOrderId())){
            mchTradeOrder.setTradeOrderId(MySeq.getTrade());  ////???????????????
        }

        mchTradeOrderMapper.insertSelective(mchTradeOrder); //??????

        mchTradeOrderDetailService.insertTradeOrderDetail(mchTradeOrder);  //??????????????????

        if(cartIdList.size() > 0) {
            mchShoppingCartService.removeByIds(cartIdList);//??????????????????????????????
        }

        return mchTradeOrder;
    }


    /** ?????????????????? **/
    @Override
    public JSONObject callPayInterface(MchTradeOrder mchTradeOrder, MchInfo mchInfo, String payUrl, String notifyUrl) {

        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchTradeOrder.getMchId());                    //??????ID
        paramMap.put("appId", mchTradeOrder.getAppId());                    // ??????ID
        paramMap.put("mchOrderNo", mchTradeOrder.getTradeOrderId());        // ??????????????????
        paramMap.put("productId", mchTradeOrder.getProductId());            // ????????????ID
        paramMap.put("amount", mchTradeOrder.getAmount());                  // ????????????,?????????
        paramMap.put("currency", "cny");                                    // ??????, cny-?????????
        paramMap.put("clientIp", mchTradeOrder.getClientIp());              // ????????????,IP????????????
        paramMap.put("device", mchTradeOrder.getDevice());                  // ??????
        paramMap.put("subject", mchTradeOrder.getSubject());                // ??????
        paramMap.put("body", mchTradeOrder.getBody());                      // ????????????
        paramMap.put("notifyUrl", notifyUrl);                               // ??????URL
        paramMap.put("channelUserId", StringUtils.defaultString(mchTradeOrder.getChannelUserId(), ""));    // ????????????ID
        paramMap.put("param1", "");                                         // ????????????1
        paramMap.put("param2", "");                                         // ????????????2
        paramMap.put("extra", StringUtils.defaultString(mchTradeOrder.getUserId(), ""));                   // ???????????????

        //???????????? ??????
        if(mchTradeOrder.getDepositMode() != null && mchTradeOrder.getDepositMode() == MchConstant.PUB_YES){
            paramMap.put("amount", mchTradeOrder.getDepositAmount());
            paramMap.put("depositMode", 1);
        }

        paramMap.put("sign", PayDigestUtil.getSign(paramMap, mchInfo.getPrivateKey()));   // ??????
        String reqData = "params=" + paramMap.toJSONString();
        logger.info("xxpay_req:{}", reqData);
        String result = XXPayUtil.call4Post(payUrl + "/pay/create_order?" + reqData);
        logger.info("xxpay_res:{}", result);

        if(StringUtils.isEmpty(result)){ //??????????????????
            logger.error("????????????????????????????????????null result = {}", result);
            return null;
        }

        JSONObject retMsg = JSON.parseObject(result);

        if(retMsg == null || !PayEnum.OK.getCode().equals(retMsg.getString("retCode"))) { //????????????????????????????????????
            logger.error("??????????????????????????? retCode={}, retMsg={}", retMsg.getString("retCode"), retMsg.getString("retMsg"));
            return null;
        }

        // ????????????
        String iSign = PayDigestUtil.getSign(retMsg, mchInfo.getPrivateKey(), "sign");
        if(!iSign.equals(retMsg.getString("sign"))) {
            logger.error("????????????, isign={}, retSign={}", iSign, retMsg.getString("sign"));
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
        mchTradeOrder.setGivePoints(givePoints);  //????????????
        MchTradeOrderExample example = new MchTradeOrderExample();
        MchTradeOrderExample.Criteria criteria = example.createCriteria();
        criteria.andTradeOrderIdEqualTo(tradeOrderId);
        criteria.andStatusEqualTo(MchConstant.TRADE_ORDER_STATUS_ING);

        int count = mchTradeOrderMapper.updateByExampleSelective(mchTradeOrder, example);
        if (count < 1) return 0;

        /*
        //??????????????????
        MchTradeOrder dbTradeOrder = baseMapper.selectById(tradeOrderId);
        if (StringUtils.isNotBlank(dbTradeOrder.getGoodsDesc())) {
            JSONArray jsonArray = JSONArray.parseArray(dbTradeOrder.getGoodsDesc());

            Long newSaleNum;
            MchGoods mchGoods;
            MchGoods updateRecord = new MchGoods();

            //?????????????????????goodsDesc?????????[{"cartId": "1", "goodsId": "1000", "goodsNum": "2", "goodsProps": "1,2"}]
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

        //?????????????????????
        if(StringUtils.isNotEmpty(dbTradeOrder.getMemberCouponNo())){

            MemberCoupon memberCouponUpdateRecord = new MemberCoupon();
            memberCouponUpdateRecord.setCouponNo(dbTradeOrder.getMemberCouponNo());
            memberCouponUpdateRecord.setStatus((byte)1);  //????????????1-????????????

            //????????????
            if(!memberCouponService.use(dbTradeOrder.getMemberCouponNo())){
                throw ServiceException.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR);
            }
        }

        //????????????????????????
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
        if(member == null){ //????????????????????????????????????????????????
            MchFans mchFans = new MchFans();
            mchFans.setMchId(mchId);
            mchFans.setWxOpenId(openId);
            mchFansService.saveOrUpdate(mchFans);
            return this.updateStatus4Success(tradeOrderId, payOrderId, income, 0L);
        }

        if(member.getStatus() != 1) { //????????? '??????' ??????, ??????????????????
            return this.updateStatus4Success(tradeOrderId, payOrderId, income, 0L);
        }

        //?????????????????? ???????????????/??????
        MchMemberConfig mchMemberConfig = mchMemberConfigService.getById(mchId);

        //?????????????????? = ??????????????? / ?????????x??? [??????]???  * ??????????????????
        Long givePoints = (mchTradeOrder.getAmount() / mchMemberConfig.getConsumeAmount()) * mchMemberConfig.getConsumeGivePoints();

        if(givePoints <= 0) {   //??????????????? ????????????
            return this.updateStatus4Success(tradeOrderId, payOrderId, income, 0L);
        }

        //?????????????????????
        MemberPoints updateRecord = new MemberPoints();
        updateRecord.setMemberId(member.getMemberId());
        updateRecord.setPoints(givePoints);
        updateRecord.setTotalConsumePoints(givePoints);
        int updateRow = memberPointsService.updatePointByMemberId(updateRecord);
        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //????????????

        //????????????????????????
        MemberPoints dbMemberPoints = memberPointsService.getById(member.getMemberId());

        //??????????????????
        MemberPointsHistory history = new MemberPointsHistory();
        history.setMemberId(member.getMemberId());  //??????ID
        history.setMemberNo(member.getMemberNo());  //??????No
        history.setChangePoints(givePoints);  //????????????
        history.setPoints(dbMemberPoints.getPoints() - givePoints);  //???????????????
        history.setAfterPoints(dbMemberPoints.getPoints());  //???????????????
        history.setMchId(mchId);  //mchId
        history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //????????????
        history.setBizType((byte)3);  //????????????3-??????
        history.setOperatorId(mchTradeOrder.getOperatorId());  //?????????ID
        history.setOperatorName(mchTradeOrder.getOperatorName());  //???????????????
        memberPointsHistoryService.save(history);

        if(StringUtils.isNotEmpty(mchTradeOrder.getMemberCouponNo())){ //???????????????????????????
            memberCouponService.use(mchTradeOrder.getMemberCouponNo());
        }

        updateRow = this.updateStatus4Success(tradeOrderId, payOrderId, income, givePoints);
        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //????????????
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSuccess4MemberBalance(String tradeOrderId, Long memberId, String couponNo) {

        MchTradeOrder mchTradeOrder = mchTradeOrderMapper.selectByPrimaryKey(tradeOrderId);
        Long mchId = mchTradeOrder.getMchId();

        Member member = memberService.getById(memberId);
        if(member.getStatus() != 1) { //????????? '??????' ??????, ??????????????????
            return this.updateStatus4Success(tradeOrderId, null, 0L);
        }

        //?????????????????? ???????????????/??????
        MchMemberConfig mchMemberConfig = mchMemberConfigService.getById(mchId);


        Long givePoints = 0L;
        if(mchMemberConfig != null && mchMemberConfig.getBalancePointsType() == MchConstant.PUB_YES){ //??????????????????????????????
            //?????????????????? = ??????????????? / ?????????x??? [??????]???  * ??????????????????
            givePoints = (mchTradeOrder.getAmount() / mchMemberConfig.getConsumeAmount()) * mchMemberConfig.getConsumeGivePoints();
        }

        //??????????????????
        if(givePoints > 0) {
            //?????????????????????
            MemberPoints updateRecord = new MemberPoints();
            updateRecord.setMemberId(member.getMemberId());
            updateRecord.setPoints(givePoints);
            updateRecord.setTotalConsumePoints(givePoints);
            int updateRow = memberPointsService.updatePointByMemberId(updateRecord);
            if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //????????????

            //????????????????????????
            MemberPoints dbMemberPoints = memberPointsService.getById(member.getMemberId());

            //??????????????????
            MemberPointsHistory history = new MemberPointsHistory();
            history.setMemberId(member.getMemberId());  //??????ID
            history.setMemberNo(member.getMemberNo());  //??????No
            history.setChangePoints(givePoints);  //????????????
            history.setPoints(dbMemberPoints.getPoints() - givePoints);  //???????????????
            history.setAfterPoints(dbMemberPoints.getPoints());  //???????????????
            history.setMchId(mchId);  //mchId
            history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //????????????
            history.setBizType((byte)3);  //????????????3-??????
            history.setOperatorId(mchTradeOrder.getOperatorId());  //?????????ID
            history.setOperatorName(mchTradeOrder.getOperatorName());  //???????????????
            memberPointsHistoryService.save(history);
        }

        //???????????????
        Long changeAmount = 0 - mchTradeOrder.getAmount();  //???????????????????????????
        MemberBalance updateRecord = new MemberBalance();
        updateRecord.setMemberId(member.getMemberId());
        updateRecord.setBalance(changeAmount);
        updateRecord.setTotalConsumeAmount(mchTradeOrder.getAmount());
        int updateRow = memberBalanceService.updateBalanceByMemberId(updateRecord);

        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_SERVICE_SETT_AMOUNT_NOT_SETT);  //????????????


        //??????????????????????????????
        MemberBalance dbMemberBalance = memberBalanceService.getById(member.getMemberId());

        //??????????????????
        MemberBalanceHistory history = new MemberBalanceHistory();
        history.setMemberId(member.getMemberId());  //??????ID
        history.setMemberNo(member.getMemberNo());  //??????No
        history.setChangeAmount(changeAmount);  //????????????
        history.setBalance(dbMemberBalance.getBalance() - changeAmount);  //?????????????????????
        history.setAfterBalance(dbMemberBalance.getBalance());  //?????????????????????
        history.setGiveAmount(0L);
        history.setMchId(mchId);  //mchId
        history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //????????????
        history.setBizType((byte)2);  //????????????2-??????
        history.setPayType((byte)3); //???????????? 3-?????????
        history.setPageOrigin((byte)4); //??????
        history.setOperatorId(mchTradeOrder.getOperatorId());  //?????????ID
        history.setOperatorName(mchTradeOrder.getOperatorName());  //???????????????
        memberBalanceHistoryService.save(history);

        //?????????????????????
        if(StringUtils.isNotEmpty(couponNo)){

            MemberCoupon memberCouponUpdateRecord = new MemberCoupon();
            memberCouponUpdateRecord.setCouponNo(couponNo);
            memberCouponUpdateRecord.setStatus(MchConstant.MEMBER_COUPON_USED);  //????????????1-????????????

            //????????????
            if(!memberCouponService.use(couponNo)){
                throw ServiceException.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR);
            }
        }

        updateRow = this.updateStatus4Success(tradeOrderId, null, givePoints);
        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //????????????
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSuccess4Cod(String tradeOrderId, Long memberId, String couponNo) {

        MchTradeOrder mchTradeOrder = mchTradeOrderMapper.selectByPrimaryKey(tradeOrderId);
        Long mchId = mchTradeOrder.getMchId();

        Member member = memberService.getById(memberId);
        if(member.getStatus() != 1) { //????????? '??????' ??????, ??????????????????
            return this.updateStatus4Success(tradeOrderId, null, 0L);
        }

        //?????????????????? ???????????????/??????
        MchMemberConfig mchMemberConfig = mchMemberConfigService.getById(mchId);

        Long givePoints = 0L;
        if(mchMemberConfig != null && mchMemberConfig.getBalancePointsType() == MchConstant.PUB_YES){ //??????????????????????????????
            //?????????????????? = ??????????????? / ?????????x??? [??????]???  * ??????????????????
            givePoints = (mchTradeOrder.getAmount() / mchMemberConfig.getConsumeAmount()) * mchMemberConfig.getConsumeGivePoints();
        }

        //??????????????????
        if(givePoints > 0) {
            //?????????????????????
            MemberPoints updateRecord = new MemberPoints();
            updateRecord.setMemberId(member.getMemberId());
            updateRecord.setPoints(givePoints);
            updateRecord.setTotalConsumePoints(givePoints);
            int updateRow = memberPointsService.updatePointByMemberId(updateRecord);
            if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //????????????

            //????????????????????????
            MemberPoints dbMemberPoints = memberPointsService.getById(member.getMemberId());

            //??????????????????
            MemberPointsHistory history = new MemberPointsHistory();
            history.setMemberId(member.getMemberId());  //??????ID
            history.setMemberNo(member.getMemberNo());  //??????No
            history.setChangePoints(givePoints);  //????????????
            history.setPoints(dbMemberPoints.getPoints() - givePoints);  //???????????????
            history.setAfterPoints(dbMemberPoints.getPoints());  //???????????????
            history.setMchId(mchId);  //mchId
            history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //????????????
            history.setBizType((byte)3);  //????????????3-??????
            history.setOperatorId(mchTradeOrder.getOperatorId());  //?????????ID
            history.setOperatorName(mchTradeOrder.getOperatorName());  //???????????????
            memberPointsHistoryService.save(history);
        }

        //?????????????????????
        if(StringUtils.isNotEmpty(couponNo)){
            MemberCoupon memberCouponUpdateRecord = new MemberCoupon();
            memberCouponUpdateRecord.setCouponNo(couponNo);
            memberCouponUpdateRecord.setStatus(MchConstant.MEMBER_COUPON_USED);  //????????????1-????????????

            //????????????
            if(!memberCouponService.use(couponNo)){
                throw ServiceException.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR);
            }
        }

        int updateRow = this.updateStatus4Success(tradeOrderId, null, givePoints);
        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //????????????
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

        //??????????????????
        MchTradeOrder mchTradeOrder = mchTradeOrderMapper.selectByPrimaryKey(tradeOrderId);

        //????????????ID
        Long memberId = mchTradeOrder.getMemberId();
        //????????????ID
        Long ruleId = mchTradeOrder.getRuleId();

        Member member = memberService.getById(memberId);
        if(member.getStatus() != 1) { //????????? '??????' ??????, ??????????????????
            return this.updateStatus4Success(tradeOrderId, payOrderId, income, 0L);
        }

        Long givePoints = 0L;  //????????????
        Long giveBalance = 0L;  //????????????

        //???ruleId???null????????????????????????
        if (ruleId != null) {
            MchMemberRechargeRule rechargeRule = mchMemberRechargeRuleService.getById(ruleId);
            if (rechargeRule == null || rechargeRule.getStatus() != MchConstant.PUB_YES) {
                return this.updateStatus4Success(tradeOrderId, payOrderId, income, 0L);  //????????????????????????????????????
            }

            Byte ruleType = rechargeRule.getRuleType();

            //???????????????????????????????????????
            if (ruleType == MchConstant.MEMBER_RECHARGE_RULE_TYPE_BALANCE) {  //?????????
                //??????????????????
                giveBalance = rechargeRule.getGivePoints();//???????????????????????????
                //??????????????????????????????
                MemberBalance updateGiveRecord = new MemberBalance();
                updateGiveRecord.setMemberId(memberId);
                updateGiveRecord.setBalance(giveBalance);
                updateGiveRecord.setTotalGiveAmount(giveBalance);
                int updateRow = memberBalanceService.updateBalanceByMemberId(updateGiveRecord);

                if (updateRow <= 0) throw new ServiceException(RetEnum.RET_SERVICE_SETT_AMOUNT_NOT_SETT);  //????????????

                //??????????????????????????????
                MemberBalance dbMemberBalance = memberBalanceService.getById(memberId);

                //??????????????????
                MemberBalanceHistory history = new MemberBalanceHistory();
                history.setMemberId(memberId);  //??????ID
                history.setMemberNo(member.getMemberNo());  //??????No
                history.setChangeAmount(giveBalance);  //????????????
                history.setBalance(dbMemberBalance.getBalance() - giveBalance);  //?????????????????????
                history.setAfterBalance(dbMemberBalance.getBalance());  //?????????????????????
                history.setGiveAmount(giveBalance);
                history.setMchId(member.getMchId());  //mchId
                history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //????????????
                history.setBizType(MchConstant.MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_GIVE);  //????????????  5-??????
                history.setPayType(null); //???????????? 3-?????????
                history.setPageOrigin(null); //??????
                history.setOperatorId(mchTradeOrder.getOperatorId());  //?????????ID
                history.setOperatorName(mchTradeOrder.getOperatorName());  //???????????????
                memberBalanceHistoryService.save(history);

            } else if (ruleType == MchConstant.MEMBER_RECHARGE_RULE_TYPE_POINT) {  //?????????
                //??????????????????
                givePoints = rechargeRule.getGivePoints();
                //?????????????????????
                MemberPoints updateRecord = new MemberPoints();
                updateRecord.setMemberId(memberId);
                updateRecord.setPoints(givePoints);
                updateRecord.setTotalGivePoints(givePoints);
                int updateRow = memberPointsService.updatePointByMemberId(updateRecord);
                if(updateRow <= 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);  //????????????

                //????????????????????????
                MemberPoints dbMemberPoints = memberPointsService.getById(member.getMemberId());

                //??????????????????
                MemberPointsHistory history = new MemberPointsHistory();
                history.setMemberId(member.getMemberId());  //??????ID
                history.setMemberNo(member.getMemberNo());  //??????No
                history.setChangePoints(givePoints);  //????????????
                history.setPoints(dbMemberPoints.getPoints() - givePoints);  //???????????????
                history.setAfterPoints(dbMemberPoints.getPoints());  //???????????????
                history.setMchId(member.getMchId());  //mchId
                history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //????????????
                history.setBizType(MchConstant.MCH_VIP_POINTS_HISTORY_BIZ_TYPE_RECHARGE);  //????????????2-????????????
                history.setOperatorId(mchTradeOrder.getOperatorId());  //?????????ID
                history.setOperatorName(mchTradeOrder.getOperatorName());  //???????????????
                memberPointsHistoryService.save(history);

            }
        }
        /*else if (ruleType == MchConstant.MEMBER_RECHARGE_RULE_TYPE_COUPON){
        }*/

        //??????????????????????????????
        Long changeAmount = mchTradeOrder.getAmount();  //???????????????????????????
        MemberBalance updateRecord = new MemberBalance();
        updateRecord.setMemberId(memberId);
        updateRecord.setBalance(changeAmount);
        updateRecord.setTotalRechargeAmount(mchTradeOrder.getAmount());
        int updateRow = memberBalanceService.updateBalanceByMemberId(updateRecord);

        if(updateRow <= 0) throw new ServiceException(RetEnum.RET_SERVICE_SETT_AMOUNT_NOT_SETT);  //????????????


        //??????????????????????????????
        MemberBalance dbMemberBalance = memberBalanceService.getById(memberId);

        //??????????????????
        MemberBalanceHistory history = new MemberBalanceHistory();
        history.setMemberId(memberId);  //??????ID
        history.setMemberNo(member.getMemberNo());  //??????No
        history.setChangeAmount(changeAmount);  //????????????
        history.setBalance(dbMemberBalance.getBalance() - changeAmount);  //?????????????????????
        history.setAfterBalance(dbMemberBalance.getBalance());  //?????????????????????
        history.setGiveAmount(0L);
        history.setMchId(member.getMchId());  //mchId
        history.setBizOrderId(mchTradeOrder.getTradeOrderId());  //????????????
        history.setBizType(MchConstant.MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_RECHARGE);  //????????????  1-??????
        history.setPayType(null); //???????????? 3-?????????
        history.setPageOrigin(null); //??????
        history.setOperatorId(mchTradeOrder.getOperatorId());  //?????????ID
        history.setOperatorName(mchTradeOrder.getOperatorName());  //???????????????
        memberBalanceHistoryService.save(history);

        updateRow = this.updateStatus4Success(tradeOrderId, payOrderId, income, givePoints);
        if(updateRow <= 0) throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL); //????????????

        if(giveBalance >= 0){ //??????????????????,  ????????????????????????discountAmount??????
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


    /** ??????????????? / ?????????????????? **/
    private void calTradeOrderProfit(MchTradeOrder mchTradeOrder){

        //???????????? [????????????] | [?????????????????????] ????????????????????????
        if(mchTradeOrder.getProductType() == MchConstant.MCH_TRADE_PRODUCT_TYPE_CASH ||
                mchTradeOrder.getProductType() == MchConstant.MCH_TRADE_PRODUCT_TYPE_MEMBER_CARD ){
            logger.info("??????[{}], ??????????????????{}, ?????????????????????", mchTradeOrder.getTradeOrderId(), MchConstant.MCH_TRADE_PRODUCT_TYPE_MAP.get(mchTradeOrder.getProductType()));
            return ;
        }

        Integer productId = mchTradeOrder.getProductId();
        if(mchTradeOrder.getProductId() == null ||productId <= 0 ){ //?????????????????????????????? ????????????
            logger.info("??????[{}], ??????????????????{}, product = {}, ??????????????? ???????????????????????????", mchTradeOrder.getTradeOrderId(), mchTradeOrder.getProductType(), productId);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        Long mchId = mchTradeOrder.getMchId();
        MchInfo mchInfo = mchInfoService.findByMchId(mchTradeOrder.getMchId());
        if(mchInfo.getType() == MchConstant.MCH_TYPE_PRIVATE) return ; //??????????????? ???????????????

        //?????????????????????
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
        updateRecord.setStatus(MchConstant.TRADE_ORDER_STATUS_SUCCESS);  //????????????
        updateRecord.setOrderAmount(consumeAmount);  //????????????
        updateRecord.setAmount(consumeAmount);  //??????????????????

        LambdaUpdateWrapper<MchTradeOrder> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MchTradeOrder::getTradeOrderId, mchTradeOrderId);
        lambdaUpdateWrapper.eq(MchTradeOrder::getStatus, MchConstant.TRADE_ORDER_STATUS_DEPOSIT_ING);
        lambdaUpdateWrapper.eq(MchTradeOrder::getDepositMode, MchConstant.PUB_YES);

        boolean isSuccess = this.update(updateRecord, lambdaUpdateWrapper);
        if(!isSuccess) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

        //????????????????????????
        PayOrder updateRecordPayOrder = new PayOrder();
        updateRecordPayOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);  //????????????
        updateRecordPayOrder.setAmount(consumeAmount);  //??????????????????

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
        updateRecord.setStatus(MchConstant.TRADE_ORDER_STATUS_DEPOSIT_REVERSE);  //??????????????????????????????

        LambdaUpdateWrapper<MchTradeOrder> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MchTradeOrder::getTradeOrderId, mchTradeOrderId);
        lambdaUpdateWrapper.eq(MchTradeOrder::getStatus, MchConstant.TRADE_ORDER_STATUS_DEPOSIT_ING);
        lambdaUpdateWrapper.eq(MchTradeOrder::getDepositMode, MchConstant.PUB_YES);

        boolean isSuccess = this.update(updateRecord, lambdaUpdateWrapper);
        if(!isSuccess) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

        //????????????????????????
        PayOrder updateRecordPayOrder = new PayOrder();
        updateRecordPayOrder.setStatus(PayConstant.PAY_STATUS_DEPOSIT_REVERSE);  //??????????????????????????????

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

    //===================================================================????????????????????????=====================================================================

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

    //===================================================================????????????????????????=====================================================================

}

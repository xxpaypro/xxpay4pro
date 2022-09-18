package org.xxpay.pay.channel.suixingpay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchInfoExt;
import org.xxpay.core.entity.SxfMchSnapshot;
import org.xxpay.pay.channel.suixingpay.utils.SuixingpayUtil;
import org.xxpay.pay.channel.suixingpay.utils.UpLoadPicture;

@Service
public class SuixingpayApiService {

    private static final MyLog _log = MyLog.getLog(SuixingpayApiService.class);
    private static String logPrefix = "【随行付商户进件】";

    /** 撤销订单接口  **/
    public boolean reverse(String mainPayParam, String subPayParam, String payOrderId) {
        String logPrefix = "【随行付撤销订单】";

        _log.info("{}, payOrderId={}", logPrefix, payOrderId);
        try {

            // 获取主发起参数
            JSONObject mainParam = toDefaultJSONObject(mainPayParam);
            _log.debug("{}mainParam={}", logPrefix, mainParam);

            // 获取子商户参数
            JSONObject subMchParam = toJSONObject(subPayParam);
            _log.debug("{}subMchParam={}", logPrefix, subMchParam);

            //业务参数
            JSONObject reqData = new JSONObject();
            reqData.put("mno", mainParam.getString("mno")); //商户编号
            reqData.put("origOrderNo", payOrderId); //商户订单号

            //请求接口
            JSONObject resData = SuixingpayUtil.req("/query/cancel", null, mainParam, reqData);

            if("0000".equals(resData.getString("bizCode"))){
                _log.error("{}撤单成功, resData={}", logPrefix, resData);
                return true;
            }

            _log.error("撤单失败, resData={}", resData);
            return false;

        } catch (Exception e) {

            _log.error("撤单异常", e);
            return false;
        }
    }

    /**
     * 将json串转为一个json对象，如果json串为空则返回一个默认对象
     * @param json
     * @return
     */
    private JSONObject toDefaultJSONObject(String json) {
        if(StringUtils.isEmpty(json)) return new JSONObject();
        return JSONObject.parseObject(json);
    }


    /**
     * 将json串转为一个json对象，如果json串为空则返回空
     * @param json
     * @return
     */
    private JSONObject toJSONObject(String json) {
        if(StringUtils.isEmpty(json)) return null;
        return JSONObject.parseObject(json);
    }

    /**
     * 随行付发起进件接口
     * @param sxfMchSnapshot
     * @param paramJSON
     * @return
     */
    public JSONObject sxfApplymentSubmit(SxfMchSnapshot sxfMchSnapshot, JSONObject paramJSON) {
        try {
            // 1.进件参数格式化
            JSONObject paramsJsonStr = getParamsJsonStr(sxfMchSnapshot, paramJSON);
            if (paramsJsonStr == null) return null;
            // 随行付进件接口URL
            String postUrl = "/merchant/income";
            // 2.发送参数
            _log.info("{}请求地址：{}， 请求参数：{}", logPrefix, postUrl, paramsJsonStr);
            JSONObject result = SuixingpayUtil.req(postUrl, null, paramJSON, paramsJsonStr);
            _log.info("{}请求结果：{}", logPrefix, result);
            if (result == null) return null;
            // 3.返回结果
            return result;
        }catch (Exception e) {
            _log.error("{}接口异常", logPrefix, e);
        }
        return null;
    }

    /**
     * 查询进件结果接口
     * @param sxfMchSnapshot
     * @param paramJSON
     * @return
     */
    public JSONObject sxfQueryMerchantInfo(SxfMchSnapshot sxfMchSnapshot, JSONObject paramJSON) {
        JSONObject reqData = new JSONObject();
        try{
            reqData.put("applicationId", sxfMchSnapshot.getApplicationId());
            // 随行付进件状态查询URL
            String postUrl = "/merchant/queryMerchantInfo";
            // 发送参数
            _log.info("{}状态查询请求地址：{}， 请求参数：{}", logPrefix, postUrl, reqData);
            JSONObject result = SuixingpayUtil.req(postUrl, null, paramJSON, reqData);
            _log.info("{}状态查询请求结果：{}", logPrefix, result);
            return result;
        }catch (Exception e) {
            _log.error("{}状态查询接口异常", logPrefix, e);
        }
        return reqData;
    }

    private JSONObject getParamsJsonStr(SxfMchSnapshot sxfMchSnapshot, JSONObject paramJSON) {
            _log.info("{}参数格式化开始", logPrefix);
        try{
            JSONObject reqData = new JSONObject();
            reqData.put("mecDisNm", sxfMchSnapshot.getMecDisNm());                  // 商户简称
            reqData.put("mblNo", sxfMchSnapshot.getMblNo());                        // 商户联系手机
            reqData.put("operationalType", sxfMchSnapshot.getOperationalType());    // 经营类型
            reqData.put("haveLicenseNo", sxfMchSnapshot.getHaveLicenseNo());        // 资质类型
            reqData.put("mecTypeFlag", sxfMchSnapshot.getMecTypeFlag());            // 商户类型
            if (!"00".equals(sxfMchSnapshot.getMecTypeFlag())) {         // 商户非普通单店商户时
                reqData.put("parentMno", sxfMchSnapshot.getParentMno());            // 所属总店商户编号
            }
            reqData.put("qrcodeList", JSONArray.parseArray(sxfMchSnapshot.getQrcodeList()));              // 二维码费率
            if ("02".equals(sxfMchSnapshot.getOperationalType())) {     // 线上类型商户时
                reqData.put("onlineType", sxfMchSnapshot.getOnlineType());          // 线上产品类型
                reqData.put("onlineName", sxfMchSnapshot.getOnlineName());          // app/网站/公众号/小程序名称
                reqData.put("onlineTypeInfo", sxfMchSnapshot.getOnlineTypeInfo());  // app下载地址及账号信息
                reqData.put("icpLicence", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getIcpLicence(), "29", paramJSON));    // ICP许可证或公众号主体信息截图
            }
            if (!"01".equals(sxfMchSnapshot.getHaveLicenseNo())) {      // 企业/个体户必传
                reqData.put("cprRegNmCn", sxfMchSnapshot.getCprRegNmCn());  // 营业执照注册名称
                reqData.put("registCode", sxfMchSnapshot.getRegistCode());  // 营业执照注册号
                reqData.put("licensePic", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getLicensePic(), "13", paramJSON));       // 营业执照照片
                if ("03".equals(sxfMchSnapshot.getHaveLicenseNo())) {
                    reqData.put("licenseMatch", sxfMchSnapshot.getLicenseMatch());  // 是否三证合一
                    if ("01".equals(sxfMchSnapshot.getLicenseMatch())) {        // 非三证合一必传
                        reqData.put("taxRegistLicensePic",  UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getTaxRegistLicensePic(), "01", paramJSON));  // 税务登记照
                        reqData.put("orgCodePic",  UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getOrgCodePic(), "14", paramJSON));                    // 组织机构代码证
                    }
                }
            }
            reqData.put("cprRegAddr", sxfMchSnapshot.getCprRegAddr());              // 商户经营详细地址
            reqData.put("regProvCd", sxfMchSnapshot.getRegProvCd()+"0000000000");   // 商户经营地址省编码 【补'0'匹配随行付编码】
            reqData.put("regCityCd", sxfMchSnapshot.getRegCityCd()+"00000000");     // 商户经营地址市编码 【补'0'匹配随行付编码】
            reqData.put("regDistCd", sxfMchSnapshot.getRegDistCd()+"000000");       // 商户经营地址区编码 【补'0'匹配随行付编码】
            reqData.put("mccCd", sxfMchSnapshot.getMccCd());                        // 经营类目
            reqData.put("csTelNo", sxfMchSnapshot.getCsTelNo());                    // 客服电话
            reqData.put("identityName", sxfMchSnapshot.getIdentityName());          // 法人姓名
            reqData.put("identityTyp", sxfMchSnapshot.getIdentityTyp());            // 法人证件类型
            reqData.put("identityNo", sxfMchSnapshot.getIdentityNo());              // 法人证件号

            reqData.put("actNm", sxfMchSnapshot.getActNm());                        // 结算账户名
            reqData.put("actTyp", sxfMchSnapshot.getActTyp() == MchConstant.PUB_NO?"00":sxfMchSnapshot.getActTyp() == MchConstant.PUB_YES?"01":"");                      // 结算账户类型【对公/对私】

            if ("01".equals(reqData.get("actTyp"))) {                   // 对私结算必传
                reqData.put("stmManIdNo", sxfMchSnapshot.getStmManIdNo());          // 结算人证件号
                reqData.put("bankCardPositivePic", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getBankCardPositivePic(), "05", paramJSON));          // 银行卡正面照
                reqData.put("settlePersonIdcardOpposite", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getSettlePersonIdcardOpposite(), "08", paramJSON));          // 结算人身份证正面照
                reqData.put("settlePersonIdcardPositive", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getSettlePersonIdcardPositive(), "07", paramJSON));          // 结算人身份证反面照
            }else {                                                     // 对公结算必传
                reqData.put("openingAccountLicensePic", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getOpeningAccountLicensePic(), "04", paramJSON));          // 开户许可证
            }
            reqData.put("actNo", sxfMchSnapshot.getActNo());                             // 结算卡号
            reqData.put("depoBank", sxfMchSnapshot.getDepoBank());                       // 开户银行
            reqData.put("depoProvCd", sxfMchSnapshot.getDepoProvCd());                   // 开户省份
            reqData.put("depoCityCd", sxfMchSnapshot.getDepoCityCd());                   // 开户城市
            reqData.put("lbnkNo", sxfMchSnapshot.getLbnkNo());                           // 银行行号
            reqData.put("legalPersonidPositivePic", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getLegalPersonidPositivePic(), "02", paramJSON)); // 法人身份证正面照
            reqData.put("legalPersonidOppositePic", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getLegalPersonidOppositePic(), "03", paramJSON)); // 法人身份证反面照
            reqData.put("storePic", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getStorePic(), "10", paramJSON));                   // 门头照
            reqData.put("insideScenePic", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getStorePic(), "11", paramJSON));             // 室内照
            if (!sxfMchSnapshot.getIdentityNo().equals(sxfMchSnapshot.getStmManIdNo())) {          // 法人身份证与结算人身份证不一致时
                reqData.put("letterOfAuthPic", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getLetterOfAuthPic(), "26", paramJSON));         // 非法人对私授权函
            }
            if (StringUtils.isNotEmpty(sxfMchSnapshot.getOtherMaterialPictureOne())) reqData.put("otherMaterialPictureOne", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getOtherMaterialPictureOne(), "19", paramJSON)); // 补充资料1
            if (StringUtils.isNotEmpty(sxfMchSnapshot.getOtherMaterialPictureTwo())) reqData.put("otherMaterialPictureTwo", UpLoadPicture.getUploadPictureId(sxfMchSnapshot.getOtherMaterialPictureTwo(), "20", paramJSON)); // 补充资料2
            _log.info("{}参数格式化结束，参数：{}", logPrefix, reqData);
            return reqData;
        }catch (Exception e) {
            _log.error("{}参数格式化失败", logPrefix, e);
        }
        return null;
    }

}

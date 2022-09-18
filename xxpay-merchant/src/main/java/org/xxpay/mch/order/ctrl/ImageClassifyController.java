package org.xxpay.mch.order.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.HttpClient;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 图片识别api */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/imageClassify")
public class ImageClassifyController extends BaseController {

    private static final MyLog _log = MyLog.getLog(ImageClassifyController.class);

    @Autowired
    private RpcCommonService rpc;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /** 返回图片中的金额 */
    @RequestMapping("/amount")
    public XxPayResponse amount(@RequestParam("imgFile") MultipartFile file) {

        try {
            if(file == null) throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);

            //临时保存上传图片， 用于测试
            //this.saveFile(file, "/home/xxpay/upload/testFile/" + DateUtil.getCurrentDate() + ".jpg");

            //第一步， 获取百度api  access_token
            String accessToken = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_BAIDU_OCR_ACCESS_TOKEN);
            if(StringUtils.isEmpty(accessToken)){
                accessToken = refBaiduToken();
            }

            String baiduOcrType = rpcCommonService.rpcSysConfigService.getVal("baiduOcrType");
            String reqUrl = "1".equalsIgnoreCase(baiduOcrType) ?   //1-普通接口, 2-高精度接口
                    "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic" :
                    "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic" ;

            String baiduUrl = String.format("%s?access_token=%s&image=%s",
                    reqUrl, accessToken, URLEncoder.encode(Base64.getEncoder().encodeToString(file.getBytes())));
            JSONObject baiduResultJSON = JSONObject.parseObject(HttpClient.callHttpsPost(baiduUrl));
            logger.info("百度图片ocr识别, 返回报文：{}", baiduResultJSON);

            String errCode = baiduResultJSON.getString("error_code");
            if(StringUtils.isNotEmpty(errCode)){
                if( "100".equals(errCode) || "110".equals(errCode) || "111".equals(errCode) ){ //accessToken无效， 需重新获取
                    refBaiduToken();
                }
                logger.error("百度识别有误, errCode = {}", errCode);
                throw ServiceException.build(RetEnum.RET_IMG_CLASSIFY_ERROR);
            }

            String amount = null;
            if(baiduResultJSON.getJSONArray("words_result") != null ){
                for(Object item : baiduResultJSON.getJSONArray("words_result")){
                    JSONObject itemJSON = (JSONObject)item;
                    String words = itemJSON.getString("words");
                    amount = matcherAmount(words);
                    if(amount != null){
                        break;
                    }
                }
            }

            if(StringUtils.isEmpty(amount)){
                throw ServiceException.build(RetEnum.RET_IMG_CLASSIFY_ERROR);
            }

           JSONObject result = new JSONObject();
           result.put("amount", amount);
           return XxPayResponse.buildSuccess(result);

        } catch (ServiceException e) {
            return XxPayResponse.build(e.getRetEnum());
        }catch (Exception e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 刷新百度Token **/
    private String refBaiduToken(){

        Map<String,String> baiduConfig = rpcCommonService.rpcSysConfigService.selectByCodes("baiduOcrClientId", "baiduOcrClientSecret");

        //刷新token
        String baiduUrl = String.format("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=%s&client_secret=%s",
                baiduConfig.get("baiduOcrClientId"), baiduConfig.get("baiduOcrClientSecret"));

        JSONObject resultJSON = JSONObject.parseObject(HttpClient.callHttpsPost(baiduUrl));
        logger.info("获取百度accessToeken, 返回报文：{}", resultJSON);

        String errCode = resultJSON.getString("error_code");
        if(StringUtils.isNotEmpty(errCode)){
            logger.error("获取百度accessToeken有误, errCode = {}", errCode);
            throw ServiceException.build(RetEnum.RET_IMG_CLASSIFY_ERROR);
        }

        String accessToken = resultJSON.getString("access_token");
        Integer expiresIn = resultJSON.getInteger("expires_in") - 60; //过期时间， 单位：s // -60s, 避免redis未过期， token已过期问题
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_BAIDU_OCR_ACCESS_TOKEN, accessToken, expiresIn, TimeUnit.SECONDS);
        return accessToken;
    }


    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(([1-9]\\d*|0)(\\.\\d+)?)");  //返回 纯数字 | 小数类型

    /** 传入待匹配的问题， 返回第一个金额， 如没有返回null */
    private static String matcherAmount(String originStr) {
        Matcher m = AMOUNT_PATTERN.matcher(originStr);
        if(m.find()){
           return m.group(0);
        }
        return null;
    }
}

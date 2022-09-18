package org.xxpay.pay.channel.maxpay.example;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.pay.channel.maxpay.config.PaymaxConfig;
import org.xxpay.pay.channel.maxpay.config.SignConfig;
import org.xxpay.pay.channel.maxpay.exception.AuthorizationException;
import org.xxpay.pay.channel.maxpay.exception.InvalidRequestException;
import org.xxpay.pay.channel.maxpay.exception.InvalidResponseException;
import org.xxpay.pay.channel.maxpay.model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 实时代付交易接口调用示例,仅供参考。
 * Created by wxw on 2017/3/15.
 */
public class PayRealTimeExample {

    public static void main(String[] args)
            throws AuthorizationException, InvalidResponseException, InvalidRequestException, IOException {

        SignConfig signConfig = new SignConfig("+///vdJUPtt+CFKDwH9F+MoX1AhO5yhOlfBJ7Vt/4S4zL5lFF/IhZQAC8VhqgffzN8dIuqZNzFTbVPI8ARagF/eLeatC9XSrtPwsw+ODloSvbxxLDNcteV7SQJBAPyMHCuqcmrxtZx2XaqXP8bMjjBvyY9Y2VA/gqvlzMf1rkyu0kPK49igTEN706nn1AnIKvmVzAdJHzW2mVNe7HUCQQCEM15EvV6rXe1mpw7sEbggLPh5z8jMY0t1GNGMCcy+7elj9ze/7k/btAzo+rpKyHKGVu0Joy2BxUtOGEBvFdsTAkAoI1yW1BA7Tw8+PtHEOyW3wusWZ63xSn2/c1ihWXSikzmQlnh0rdpoo5F7aELLzjy1EUtDP7udrNF5B/x2c6/BAkB3d9hz9FJOOQamIshoa6biZOFza2QB2KXDP1d23xBJZsTokuutphZC7JZSIOsjU7uzTXDOqckhIgNoop/wA2dDAkEA2AnsyazTgMHr7Xr8tvuMAONkTkaeHpRicf6Wg+rqkU6AX5Jfxvcz+G3HIUCvsgqJ74LnMBzjfrzfr/DAP9AdUw==",
                "",
                "/wuDuu0SlkfBdsMO0INOlzzZh6oe7HYyM/d1yy1BKl5gHO623f1PrfxaMsPjGSYEnduAHmClritySjG3DfSmV6dGyBCCYLlGToZmpq/vP1FMHo0IYoQkXKUZ0kT9EeIFUKMigwIDAQAB");

        //doPayRealTime(signConfig, "1");
        doPayRealTimeBalance(signConfig);
        /*doPayRealTimeQuery();
        doPayRealTimeBalance();
        doDownloadPayBillFile();
        doDownloadPayReturnFile();*/
    }

    /**
     * 下载回盘文件
     * @throws AuthorizationException
     * @throws InvalidResponseException
     * @throws InvalidRequestException
     * @throws IOException
     */
    private static void doDownloadPayReturnFile(SignConfig signConfig)
            throws AuthorizationException, InvalidResponseException, InvalidRequestException, IOException {
        Map<String, Object> statementMap = new HashMap<String, Object>();
        statementMap.put("appointDay", "20171109");
        statementMap.put("channelCategory", "LAKALA");
        statementMap.put("statementType", "PAY_RETURN");

        String responseData = Paymax.request(signConfig,
                PaymaxConfig.API_BASE_URL + PaymaxConfig.STATEMENT_URI,
                JSONObject.toJSONString(statementMap),String.class);
        System.out.println("===============================");
        System.out.println(responseData);
        System.out.println("===============================");
    }

    /**
     * 下载对账单
     * @throws AuthorizationException
     * @throws InvalidResponseException
     * @throws InvalidRequestException
     * @throws IOException
     */
    private static void doDownloadPayBillFile(SignConfig signConfig)
            throws AuthorizationException, InvalidResponseException, InvalidRequestException, IOException {
        Map<String, Object> statementMap = new HashMap<String, Object>();
        statementMap.put("appointDay", "20171226");
        statementMap.put("channelCategory", "LAKALA");
        statementMap.put("statementType", "PAY_BILL");
        String responseData = Paymax.request(signConfig,
                PaymaxConfig.API_BASE_URL + PaymaxConfig.STATEMENT_URI,
                JSONObject.toJSONString(statementMap),String.class);
        System.out.println("===============================");
        System.out.println(responseData);
        System.out.println("===============================");

    }
    /**
     * 查询头寸
     * @throws AuthorizationException
     * @throws InvalidResponseException
     * @throws InvalidRequestException
     * @throws IOException
     */
    private static void doPayRealTimeBalance(SignConfig signConfig)
            throws AuthorizationException, InvalidResponseException, InvalidRequestException, IOException {
        PayBalanceResponse responseData = Paymax.request(signConfig,
                PaymaxConfig.API_BASE_URL + PaymaxConfig.PAY_REAL_TIME_BALANCE ,
                null,PayBalanceResponse.class);
        System.out.println("===============================");
        System.out.println(JSONObject.toJSONString(responseData));
        System.out.println("===============================");
    }


    /**
     * 查询实时交易结果
     * @throws AuthorizationException
     * @throws InvalidResponseException
     * @throws InvalidRequestException
     * @throws IOException
     */
    private static void doPayRealTimeQuery(SignConfig signConfig)
            throws AuthorizationException, InvalidResponseException, InvalidRequestException, IOException {

        String orderNo = "6f33b7382d0942b0a965560228a6e82b";//成功：e5ec7eeb0d654b7a843d266a98e82db3  失败：5e204c8fb1a8477483eb577223613488

        PayRealTimeQueryResponse responseData = Paymax.request(signConfig,
                PaymaxConfig.API_BASE_URL + PaymaxConfig.PAY_REAL_TIME +"/" + orderNo,
                null,PayRealTimeQueryResponse.class);
        System.out.println("===============================");
        System.out.println(JSONObject.toJSONString(responseData));
        System.out.println("===============================");
    }

    /**
     * 发起实时代付交易请求
     * @throws AuthorizationException
     * @throws InvalidResponseException
     * @throws InvalidRequestException
     * @throws IOException
     */
    public static void doPayRealTime(SignConfig signConfig, String amount)
            throws AuthorizationException, InvalidResponseException, InvalidRequestException, IOException {
        PayRealTimeRequest requestData = packagePayRealTimeRequestData(amount);
        PayRealTimeResponse responseData = Paymax.request(signConfig,
                PaymaxConfig.API_BASE_URL + PaymaxConfig.PAY_REAL_TIME,
                JSONObject.toJSONString(requestData),PayRealTimeResponse.class);
        System.out.println("===============================");
        System.out.println(JSONObject.toJSONString(responseData));
        System.out.println("===============================");
    }

    private static PayRealTimeRequest packagePayRealTimeRequestData(String amount){
        PayRealTimeRequest requestData = new PayRealTimeRequest();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        requestData.setOrderNo(uuid);
        requestData.setMobileNo("18610582396");
        requestData.setAccountType("1");
        requestData.setBankAccountNo("6228480018859194476");
        requestData.setBankAccountName("曹洪香");
        requestData.setAmount(new BigDecimal(AmountUtil.convertCent2Dollar(amount)));

        requestData.setComment("00");

        return requestData;
    }
}

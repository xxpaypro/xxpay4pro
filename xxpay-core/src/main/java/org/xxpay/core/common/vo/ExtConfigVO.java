package org.xxpay.core.common.vo;

import java.io.Serializable;

/**
 * @Author terrfly
 * @Date 2018/12/9 16:55
 * @Description 配置通道扩展信息
 **/
public class ExtConfigVO implements Serializable {

    //根据extConfig 返回对象信息,
    public static ExtConfigVO getExtConfigVO(String extConfigStr, Byte productType){

//        if(StringUtils.isEmpty(extConfigStr) || productType == null) return null;
//
//        if(productType == MchConstant.FEE_SCALE_PTYPE_PAY){
//
//            return new PayExtConfigVO(extConfigStr);
//
//        }else if(productType == MchConstant.FEE_SCALE_PTYPE_AGPAY){
//
//            return new AgpayExtConfigVO(extConfigStr);
//        }
        return null;  //TODO 不在使用以前 轮询/单独接口的方式；
    }

    public static int getRealId(String idStr){
        return Integer.parseInt(idStr.substring(2, idStr.length()-1));
    }

}

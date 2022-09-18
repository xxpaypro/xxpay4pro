package org.xxpay.manage.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.entity.PayInterface;
import org.xxpay.core.entity.PayInterfaceType;
import org.xxpay.core.entity.PayType;
import org.xxpay.manage.common.service.RpcCommonService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 2018/5/4
 * @description:
 */
@Service
public class CommonConfigService {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 得到支付接口类型Map,key为接口类型代码,value为接口类型名称
     * @return
     */
    public Map getPayInterfaceTypeMap() {
        PayInterfaceType payInterfaceType = new PayInterfaceType();
        List<PayInterfaceType> payInterfaceTypeList = rpcCommonService.rpcPayInterfaceTypeService.selectAll(payInterfaceType);
        Map<String, String> map = new HashMap<>();
        for(PayInterfaceType pit : payInterfaceTypeList) {
            map.put(pit.getIfTypeCode(), pit.getIfTypeName());
        }
        return map;
    }

    /**
     * 得到支付接口Map,key为接口代码,value为接口名称
     * @return
     */
    public Map getPayInterfaceMap() {
        PayInterface payInterface = new PayInterface();
        List<PayInterface>  payInterfaceList = rpcCommonService.rpcPayInterfaceService.selectAll(payInterface);
        Map<String, String> map = new HashMap<>();
        for(PayInterface pi : payInterfaceList) {
            map.put(pi.getIfCode(), pi.getIfName());
        }
        return map;
    }

    /**
     * 得到支付类型Map,key为类型代码,value为类型名称
     * @return
     */
    public Map getPayTypeMap() {
        PayType payType = new PayType();
        List<PayType> payTypeList = rpcCommonService.rpcPayTypeService.selectAll(payType);
        Map<String, String> map = new HashMap<>();
        for(PayType py : payTypeList) {
            map.put(py.getPayTypeCode(), py.getPayTypeName());
        }
        return map;
    }

}

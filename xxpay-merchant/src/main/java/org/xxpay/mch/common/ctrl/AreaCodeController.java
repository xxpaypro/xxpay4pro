package org.xxpay.mch.common.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.SysAreaCode;
import org.xxpay.core.entity.SysCityCode;
import org.xxpay.core.entity.SysProvinceCode;
import org.xxpay.mch.common.service.RpcCommonService;

import java.util.List;

@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/area_code")
public class AreaCodeController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /** 查询省 **/
    @RequestMapping("/provinces")
    public XxPayResponse provinceList() {
        List<SysProvinceCode> list = rpc.rpcSysProvinceCodeService.list(new QueryWrapper<SysProvinceCode>().lambda().orderByAsc(SysProvinceCode::getProvinceCode));
        return XxPayResponse.buildSuccess(list);
    }

    /** 根据省code, 查询下属市 **/
    @RequestMapping("/cities")
    public XxPayResponse cityList() {

        Integer provinceCode = getValIntegerRequired("provinceCode");
        List<SysCityCode> list = rpc.rpcSysCityCodeService.list(
                new QueryWrapper<SysCityCode>().lambda().eq(SysCityCode::getProvinceCode, provinceCode));
        return XxPayResponse.buildSuccess(list);
    }

    /** 根据市code, 查询下属 地区/县 **/
    @RequestMapping("/areas")
    public XxPayResponse areaList() {

        Integer cityCode = getValIntegerRequired("cityCode");
        List<SysAreaCode> list = rpc.rpcSysAreaCodeService.list(
                new QueryWrapper<SysAreaCode>().lambda().eq(SysAreaCode::getCityCode, cityCode));
        return XxPayResponse.buildSuccess(list);
    }


    /** 根据地区详细信息 **/
    @RequestMapping("/get")
    public XxPayResponse getAreaInfo() {
        Integer areaCode = getValIntegerRequired("areaCode");
        return XxPayResponse.buildSuccess(rpc.rpcSysAreaCodeService.getById(areaCode));
    }

}
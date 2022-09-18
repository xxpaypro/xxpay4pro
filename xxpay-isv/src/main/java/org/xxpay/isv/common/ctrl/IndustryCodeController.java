package org.xxpay.isv.common.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.SysIndustryCode;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.List;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/industry_code")
public class IndustryCodeController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /** 查询下级分类 **/
    @RequestMapping("/list")
    public XxPayResponse list() {

        Integer currentCode = getValIntegerRequired("currentCode");
        List<SysIndustryCode> list = rpc.rpcSysIndustryCodeService.list(
                new QueryWrapper<SysIndustryCode>().lambda().eq(SysIndustryCode::getParentCode, currentCode));
        return XxPayResponse.buildSuccess(list);
    }

    /** 查询详细信息 **/
    @RequestMapping("/get")
    public XxPayResponse get() {
        Integer currentCode = getValIntegerRequired("currentCode");
        return XxPayResponse.buildSuccess(rpc.rpcSysIndustryCodeService.getById(currentCode));
    }

}
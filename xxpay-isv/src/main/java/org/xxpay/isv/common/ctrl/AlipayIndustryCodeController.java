package org.xxpay.isv.common.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.SysAlipayIndustryCode;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.List;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/alipay_industry_code")
public class AlipayIndustryCodeController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /** 查询下级分类 **/
    @RequestMapping("/list")
    public XxPayResponse list() {

        Integer currentCode = getValIntegerRequired("currentCode");
        List<SysAlipayIndustryCode> list = rpc.rpcSysAlipayIndustryCodeService.list(
                new QueryWrapper<SysAlipayIndustryCode>().lambda().eq(SysAlipayIndustryCode::getParentCode, currentCode));
        return XxPayResponse.buildSuccess(list);
    }

    /** 查询详细信息 **/
    @RequestMapping("/get")
    public XxPayResponse get() {
        Integer id = getValIntegerRequired("id");
        SysAlipayIndustryCode alipayIndustryCode = rpc.rpcSysAlipayIndustryCodeService.getById(id);
        return XxPayResponse.buildSuccess(alipayIndustryCode);
    }


    /** 通过查询mcc信息 **/
    @RequestMapping("/getByMccCode")
    public XxPayResponse getByMccCode() {
        String mccCode = getValStringRequired("mccCode");
        JSONObject json = new JSONObject();
        SysAlipayIndustryCode industryCode = rpc.rpcSysAlipayIndustryCodeService.getOne(new QueryWrapper<SysAlipayIndustryCode>().lambda().eq(SysAlipayIndustryCode::getMccCode, mccCode));
        if (industryCode == null) return XxPayResponse.build(RetEnum.RET_ISV_ALIPAY_INDUSTRY_CODE_ERROR);
        SysAlipayIndustryCode parantCode = rpc.rpcSysAlipayIndustryCodeService.getById(industryCode.getParentCode());
        if (parantCode == null) return XxPayResponse.build(RetEnum.RET_ISV_ALIPAY_INDUSTRY_CODE_ERROR);
        json.put("firstId", parantCode.getParentCode());
        json.put("secondId", industryCode.getParentCode());
        json.put("thirdId", industryCode.getId());
        json.put("industryName", industryCode.getIndustryName());
        return XxPayResponse.buildSuccess(json);
    }

}
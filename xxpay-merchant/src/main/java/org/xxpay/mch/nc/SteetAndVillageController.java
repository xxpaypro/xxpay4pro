package org.xxpay.mch.nc;

import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.service.nc.repository.StreetRepository;
import org.xxpay.service.nc.repository.VillageRepository;
import org.xxpay.service.nc.utils.PageUtil;

import javax.annotation.Resource;

//@RestController
//@RequestMapping("div")
public class SteetAndVillageController extends BaseController {

    /*
    @Resource
    private StreetRepository streetRepository;

    @Resource
    private VillageRepository villageRepository;
    */

    /**
     * 通过街道编码查找街道
     * @return

    @RequestMapping("/street/findStreetByCode")
    public XxPayResponse findStreetByCode() {
        String code = getValStringRequired("code");
        if (StringUtils.isEmpty(code)) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_STREET_STREET_CODE_REQUIRED);
        }
        return XxPayResponse.buildSuccess(streetRepository.findByCode(code));
    }
    */

    /**
     * 通过区县码分页查找街道
     * @return

    @RequestMapping("/street/findAllByAreaCode")
    public XxPayResponse findStreetByAreaCode() {

        String areaCode = getValStringRequired("areaCode");
        if (StringUtils.isEmpty(areaCode)) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_STREET_CODE_REQUIRED);
        }
        int currentPage = getValInteger("currentPage");  //当前页码, 从0开始
        int pageSize = getValInteger("pageSize");        //分页页面大小
        Pageable pageable = PageUtil.getPageable(currentPage, pageSize, "areaCode");
        return XxPayResponse.buildSuccess(streetRepository.findByAreaCode(pageable, areaCode));
    }
    */

    /**
     * 通过村委社区编码查找村委社区
     * @return

    @RequestMapping("/village/findVillageByCode")
    public XxPayResponse findVillageByCode() {
        String code = getValStringRequired("code");
        if (StringUtils.isEmpty(code)) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_VILLAGE_CODE_REQUIRED);
        }
        return XxPayResponse.buildSuccess(villageRepository.findByCode(code));
    }
    */

    /**
     * 通过街道编码分页查找村委社区
     * @return

    @RequestMapping("/village/findAllByStreetCode")
    public XxPayResponse findAllByStreetCode() {
        String streetCode = getValStringRequired("streetCode");
        if (StringUtils.isEmpty(streetCode)) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_STREET_STREET_CODE_REQUIRED);
        }
        int currentPage = getValInteger("currentPage");     //当前页码, 从0开始
        int pageSize = getValInteger("pageSize");     //分页页面大小
        Pageable pageable = PageUtil.getPageable(currentPage, pageSize, "streetCode");
        return XxPayResponse.buildSuccess(villageRepository.findByStreetCode(pageable, streetCode));
    }
    */
}

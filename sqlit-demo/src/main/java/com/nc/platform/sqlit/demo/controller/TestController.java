package com.nc.platform.sqlit.demo.controller;

import com.nc.platform.sqlit.demo.model.Street;
import com.nc.platform.sqlit.demo.repository.StreetRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("div")
public class TestController {

    @Resource
    private StreetRepository streetRepository;

    /**
     * 根据街道地址码查找街道
     * @param code
     * @return
     */
    @RequestMapping("/findByCode")
    Street findByCode(@RequestParam(value = "code") String code) {
        return streetRepository.findByCode(code);
    }

    @RequestMapping("/findByAreaCode")
    List<Street> findByCityCode(@RequestParam(value = "areaCode") String areaCode) {

        return streetRepository.findByAreaCode(areaCode);
    }

}

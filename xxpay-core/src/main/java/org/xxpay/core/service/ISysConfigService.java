package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.SysConfig;

import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/08/22
 * @description: 系统参数配置接口
 */
public interface ISysConfigService extends IService<SysConfig> {


    List<SysConfig> select(String type);

    JSONObject getSysConfigObj(String type);

    int updateAll(List<SysConfig> sysConfigList);

    int update(JSONObject obj);
    
    String getVal(String code);

    SysConfig selectByPrimaryKey(String code);

    int updateSmsConfig(SysConfig sysConfig);

    /** 获取配置信息 根据code集合 **/
    Map<String, String> selectByCodes(String... code);
}

package org.xxpay.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.SysConfig;
import org.xxpay.core.entity.SysConfigExample;
import org.xxpay.core.service.ISysConfigService;
import org.xxpay.service.dao.mapper.SysConfigMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: dingzhiwei
 * @date: 2018/4/27
 * @description:
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public List<SysConfig> select(String type) {
        SysConfigExample example = new SysConfigExample();
        example.setOrderByClause("orderNum ASC");
        SysConfigExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(type);
        return sysConfigMapper.selectByExample(example);
    }

    @Override
    public JSONObject getSysConfigObj(String type) {
        JSONObject obj = new JSONObject();
        List<SysConfig> sysConfigList = select(type);
        if(CollectionUtils.isEmpty(sysConfigList)) return obj;
        for(SysConfig sysConfig : sysConfigList) {
            obj.put(sysConfig.getCode(), sysConfig.getValue());
        }
        return obj;
    }

    @Override
    public int updateAll(List<SysConfig> sysConfigList) {
        if(CollectionUtils.isEmpty(sysConfigList)) return 0;
        int count = 0;
        for(SysConfig sysConfig : sysConfigList) {
            int result = sysConfigMapper.updateByPrimaryKeySelective(sysConfig);
            count += result;
        }
        return count;
    }

    @Override
    public int update(JSONObject obj) {
        int count = 0;
        Set<String> set = obj.keySet();
        for(String k : set) {
            SysConfig sysConfig = new SysConfig();
            sysConfig.setCode(k);
            sysConfig.setValue(obj.getString(k));
            int result = sysConfigMapper.updateByPrimaryKeySelective(sysConfig);
            count += result;
        }
        return count;
    }

	@Override
	public String getVal(String code) {
		SysConfig sysConfig = sysConfigMapper.selectByPrimaryKey(code);
		return sysConfig == null ? null : sysConfig.getValue();
	}

    @Override
    public SysConfig selectByPrimaryKey(String code) {
        return sysConfigMapper.selectByPrimaryKey(code);
    }

    @Override
    public Map<String, String> selectByCodes(String... code) {
        List<SysConfig> dbResult =  this.list(new QueryWrapper<SysConfig>().lambda().in(SysConfig::getCode, code));
        Map<String, String> result = new HashMap<>();
        dbResult.stream().forEach(item -> result.put(item.getCode(), item.getValue()));
        return result;
    }

    @Override
    public int updateSmsConfig(SysConfig sysConfig) {
        return sysConfigMapper.updateByPrimaryKeySelective(sysConfig);
    }
}

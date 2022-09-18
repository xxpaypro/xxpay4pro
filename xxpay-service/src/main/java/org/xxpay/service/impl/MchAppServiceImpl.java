package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xxpay.core.entity.MchApp;
import org.xxpay.core.entity.MchAppExample;
import org.xxpay.core.service.IMchAppService;
import org.xxpay.service.dao.mapper.MchAppMapper;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/13
 * @description:
 */
@Service
public class MchAppServiceImpl extends ServiceImpl<MchAppMapper, MchApp> implements IMchAppService {

    @Autowired
    private MchAppMapper mchAppMapper;

    @Override
    public List<MchApp> select(int pageIndex, int pageSize, MchApp mchApp) {
        MchAppExample example = new MchAppExample();
        example.setOrderByClause("createTime desc");
        example.setOffset(pageIndex);
        example.setLimit(pageSize);
        MchAppExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchApp);
        return mchAppMapper.selectByExample(example);
    }

    @Override
    public int count(MchApp mchApp) {
        MchAppExample example = new MchAppExample();
        MchAppExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchApp);
        return mchAppMapper.countByExample(example);
    }

    @Override
    public MchApp findById(String appId) {
        return mchAppMapper.selectByPrimaryKey(appId);
    }

    @Override
    public MchApp findByMchIdAndAppId(Long mchId, String appId) {
        MchAppExample example = new MchAppExample();
        MchAppExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andAppIdEqualTo(appId);
        List<MchApp> mchAppList = mchAppMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(mchAppList)) return null;
        return mchAppList.get(0);
    }

    @Override
    public int add(MchApp mchApp) {
        return mchAppMapper.insertSelective(mchApp);
    }

    @Override
    public int update(MchApp mchApp) {
        return mchAppMapper.updateByPrimaryKeySelective(mchApp);
    }

    @Override
    public int updateByMchIdAndAppId(Long mchId, String appId, MchApp mchApp) {
        MchAppExample example = new MchAppExample();
        MchAppExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andAppIdEqualTo(appId);
        return mchAppMapper.updateByExampleSelective(mchApp, example);
    }

    @Override
    public int delete(String appId) {
        return mchAppMapper.deleteByPrimaryKey(appId);
    }

    void setCriteria(MchAppExample.Criteria criteria, MchApp mchApp) {
        if(mchApp != null) {
            if(mchApp.getMchId() != null) criteria.andMchIdEqualTo(mchApp.getMchId());
            if(StringUtils.isNotBlank(mchApp.getAppId())) criteria.andAppIdEqualTo(mchApp.getAppId());
        }
    }

}

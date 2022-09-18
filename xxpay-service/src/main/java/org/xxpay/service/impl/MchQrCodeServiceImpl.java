package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xxpay.core.entity.MchQrCode;
import org.xxpay.core.entity.MchQrCodeExample;
import org.xxpay.core.service.IMchQrCodeService;
import org.xxpay.service.dao.mapper.MchQrCodeMapper;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/21
 * @description:
 */
@Service
public class MchQrCodeServiceImpl extends ServiceImpl<MchQrCodeMapper, MchQrCode> implements IMchQrCodeService {

    @Autowired
    private MchQrCodeMapper mchQrCodeMapper;

    @Override
    public List<MchQrCode> select(int pageIndex, int pageSize, MchQrCode mchQrCode) {
        MchQrCodeExample example = new MchQrCodeExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(pageIndex);
        example.setLimit(pageSize);
        MchQrCodeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchQrCode);
        return mchQrCodeMapper.selectByExample(example);
    }

    @Override
    public int count(MchQrCode mchQrCode) {
        MchQrCodeExample example = new MchQrCodeExample();
        MchQrCodeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchQrCode);
        return mchQrCodeMapper.countByExample(example);
    }

    @Override
    public MchQrCode findById(Long id) {
        return mchQrCodeMapper.selectByPrimaryKey(id);
    }

    @Override
    public MchQrCode find(MchQrCode mchQrCode) {
        MchQrCodeExample example = new MchQrCodeExample();
        MchQrCodeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchQrCode);
        List<MchQrCode> mchQrCodeList = mchQrCodeMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(mchQrCodeList)) return null;
        return mchQrCodeList.get(0);
    }

    @Override
    public MchQrCode findByMchIdAndAppId(Long mchId, String appId) {
        MchQrCodeExample example = new MchQrCodeExample();
        MchQrCodeExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        List<MchQrCode> mchQrCodeList = mchQrCodeMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(mchQrCodeList)) return null;
        return mchQrCodeList.get(0);
    }

    @Override
    public int add(MchQrCode mchQrCode) {
        return mchQrCodeMapper.insertSelective(mchQrCode);
    }

    @Override
    public int update(MchQrCode mchQrCode) {
        return mchQrCodeMapper.updateByPrimaryKeySelective(mchQrCode);
    }

    @Override
    public int delete(Long id) {
        return mchQrCodeMapper.deleteByPrimaryKey(id);
    }

    void setCriteria(MchQrCodeExample.Criteria criteria, MchQrCode mchQrCode) {
        if(mchQrCode != null) {
            if(mchQrCode.getMchId() != null) criteria.andMchIdEqualTo(mchQrCode.getMchId());
            if(mchQrCode.getId() != null) criteria.andIdEqualTo(mchQrCode.getId());
            if(mchQrCode.getPayAmount() != null) criteria.andPayAmountEqualTo(mchQrCode.getPayAmount());
        }
    }

}

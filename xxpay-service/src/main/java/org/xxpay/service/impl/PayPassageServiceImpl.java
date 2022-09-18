package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.vo.PayExtConfigVO;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.core.entity.PayPassageExample;
import org.xxpay.core.service.IPayPassageService;
import org.xxpay.service.dao.mapper.PayPassageMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 2018/5/3
 * @description: 支付通道
 */
@Service
public class PayPassageServiceImpl extends ServiceImpl<PayPassageMapper, PayPassage> implements IPayPassageService {

    @Autowired
    private PayPassageMapper payPassageMapper;

    @Override
    public int add(PayPassage payPassage) {
        return payPassageMapper.insertSelective(payPassage);
    }

    @Override
    public int update(PayPassage payPassage) {
        return payPassageMapper.updateByPrimaryKeySelective(payPassage);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int updateRate(PayPassage payPassage) {
        // 修改通道费率
        int count = payPassageMapper.updateByPrimaryKeySelective(payPassage);
        return count;
    }

    @Override
    public PayPassage findById(Integer id) {
        return payPassageMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PayPassage> select(int offset, int limit, PayPassage payPassage) {
        PayPassageExample example = new PayPassageExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        PayPassageExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payPassage);
        return payPassageMapper.selectByExample(example);
    }

    @Override
    public Integer count(PayPassage payPassage) {
        PayPassageExample example = new PayPassageExample();
        PayPassageExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payPassage);
        return payPassageMapper.countByExample(example);
    }

    @Override
    public List<PayPassage> selectAll(PayPassage payPassage) {
        PayPassageExample example = new PayPassageExample();
        example.setOrderByClause("createTime DESC");
        PayPassageExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payPassage);
        return payPassageMapper.selectByExample(example);
    }

    @Override
    public List<PayPassage> selectPlatAllByPayType(String payType) {
        PayPassage payPassage = new PayPassage();
        payPassage.setBelongInfoType(MchConstant.INFO_TYPE_PLAT);
        payPassage.setBelongInfoId(0L);
        return selectAll(payPassage);
    }

    @Override
    public List<PayPassage> selectMchAllByPayType(String payType, Long mchId) {
        PayPassage payPassage = new PayPassage();
        payPassage.setBelongInfoType(MchConstant.INFO_TYPE_MCH);
        payPassage.setBelongInfoId(mchId);
        return selectAll(payPassage);
    }

    @Override
    public PayPassage selectByIsv(Long isvId, String ifTypeCode){

        return this.getOne(new QueryWrapper<PayPassage>().lambda()
                .eq(PayPassage::getBelongInfoId, isvId)
                .eq(PayPassage::getBelongInfoType, MchConstant.INFO_TYPE_ISV)
                .eq(PayPassage::getIfTypeCode, ifTypeCode)
        );

    }

    @Override
    public PayPassage selectByMch(Long mchId, String ifTypeCode){

        return this.getOne(new QueryWrapper<PayPassage>().lambda()
                .eq(PayPassage::getBelongInfoId, mchId)
                .eq(PayPassage::getBelongInfoType, MchConstant.INFO_TYPE_MCH)
                .eq(PayPassage::getIfTypeCode, ifTypeCode)
        );

    }






    void setCriteria(PayPassageExample.Criteria criteria, PayPassage obj) {
        if(obj != null) {
            if(obj.getRiskStatus() != null && obj.getRiskStatus().byteValue() != -99) criteria.andRiskStatusEqualTo(obj.getRiskStatus());
            if(obj.getStatus() != null && obj.getStatus().byteValue() != -99) criteria.andStatusEqualTo(obj.getStatus());
            if(obj.getBelongInfoId() != null) criteria.andBelongInfoIdEqualTo(obj.getBelongInfoId());
            if(obj.getBelongInfoType() != null) criteria.andBelongInfoTypeEqualTo(obj.getBelongInfoType());
            if(obj.getPsListVal("ids") != null) criteria.andIdIn(obj.getPsListVal("ids"));
        }
    }

    public BigDecimal getMaxChannelFee(PayExtConfigVO payExtConfigVO){

        List<Integer> ids = new ArrayList<>();

        if(payExtConfigVO.getVisiblePassageList() != null){
            ids = payExtConfigVO.getVisiblePassageList();
        }

        if(payExtConfigVO.getCurPayPollParam() != null){
            for(PayExtConfigVO.PollParam pollParam : payExtConfigVO.getCurPayPollParam()){
                ids.add(pollParam.getPayPassageId() );
            }
        }

        if(ids.isEmpty()) return null;
        PayPassageExample exa = new PayPassageExample();
        exa.createCriteria().andIdIn(ids);
        return payPassageMapper.selectMaxFeeByExample(exa);

    }

}

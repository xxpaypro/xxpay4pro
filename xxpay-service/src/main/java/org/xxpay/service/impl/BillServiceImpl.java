package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.Bill;
import org.xxpay.core.entity.BillExample;
import org.xxpay.core.service.IBillService;
import org.xxpay.service.dao.mapper.BillMapper;

import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/2/6
 * @description:
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {

    @Autowired
    private BillMapper mchBillMapper;

    @Override
    public Bill findById(Long id) {
        return mchBillMapper.selectByPrimaryKey(id);
    }

    @Override
    public Bill findByInfoIdAndId(Long infoId, Byte infoType, Long id) {
        BillExample example = new BillExample();
        BillExample.Criteria criteria = example.createCriteria();
        criteria.andInfoIdEqualTo(infoId);
        criteria.andInfoTypeEqualTo(infoType);
        criteria.andIdEqualTo(id);
        List<Bill> mchBillList = mchBillMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(mchBillList)) return null;
        return mchBillList.get(0);
    }

    @Override
    public List<Bill> select(int offset, int limit, Bill mchBill) {
        BillExample example = new BillExample();
        example.setOrderByClause("billDate desc");
        example.setOffset(offset);
        example.setLimit(limit);
        BillExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchBill);
        return mchBillMapper.selectByExample(example);
    }

    @Override
    public Integer count(Bill mchBill) {
        BillExample example = new BillExample();
        BillExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchBill);
        return mchBillMapper.countByExample(example);
    }

    @Override
    public Bill findByInfoIdAndBillDate(Long infoId, Byte infoType, Date billDate, Byte billType) {
        BillExample example = new BillExample();
        BillExample.Criteria criteria = example.createCriteria();
        criteria.andInfoIdEqualTo(infoId);
        criteria.andInfoTypeEqualTo(infoType);
        criteria.andBillDateEqualTo(billDate);
        criteria.andBillTypeEqualTo(billType);
        List<Bill> mchBillList = mchBillMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(mchBillList)) return null;
        return mchBillList.get(0);
    }

    @Override
    public int add(Bill mchBill) {
        return mchBillMapper.insertSelective(mchBill);
    }

    @Override
    public int updateComplete(Long infoId, Byte infoType, Date billDate) {
        BillExample example = new BillExample();
        BillExample.Criteria criteria = example.createCriteria();
        criteria.andInfoIdEqualTo(infoId);
        criteria.andInfoTypeEqualTo(infoType);
        criteria.andBillDateEqualTo(billDate);
        Bill mchBill = new Bill();
        mchBill.setStatus(MchConstant.MCH_BILL_STATUS_COMPLETE);
        return mchBillMapper.updateByExampleSelective(mchBill, example);
    }

    void setCriteria(BillExample.Criteria criteria, Bill mchBill) {
        if(mchBill != null) {
            if(mchBill.getInfoId() != null) criteria.andInfoIdEqualTo(mchBill.getInfoId());
            if(mchBill.getInfoType() != null) criteria.andInfoTypeEqualTo(mchBill.getInfoType());
            if(mchBill.getBillDate() != null) criteria.andBillDateEqualTo(mchBill.getBillDate());
            if(mchBill.getStatus() != null && mchBill.getStatus() != -99) criteria.andStatusEqualTo(mchBill.getStatus());
            if(mchBill.getBillType() != null) criteria.andBillTypeEqualTo(mchBill.getBillType());
        }
    }

	@Override
	public List<Long> findMchIds(Bill condition) {
		BillExample example = new BillExample();
		BillExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, condition);
		return mchBillMapper.findMchIds(example);
	}


    @Override
    public Integer countBySub(Bill mchBill) {

        return mchBillMapper.countBySub(mchBill);
    }

    @Override
    public List<Bill> selectBySub(int offset, int limit, Bill mchBill) {
        mchBill.setPsVal("offset", offset);
        mchBill.setPsVal("limit", limit);
        return mchBillMapper.selectBySub(mchBill);
    }

}

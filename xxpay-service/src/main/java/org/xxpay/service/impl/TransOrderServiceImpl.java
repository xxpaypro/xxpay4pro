package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.core.entity.TransOrderExample;
import org.xxpay.core.service.ITransOrderService;
import org.xxpay.service.dao.mapper.TransOrderMapper;

import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 17/10/30
 * @description:
 */
@Service
public class TransOrderServiceImpl extends ServiceImpl<TransOrderMapper, TransOrder> implements ITransOrderService {

    private static final MyLog _log = MyLog.getLog(TransOrderServiceImpl.class);

    @Autowired
    private TransOrderMapper transOrderMapper;

    @Override
    public TransOrder findByTransOrderId(String transOrderId) {
        return transOrderMapper.selectByPrimaryKey(transOrderId);
    }

    public int createTransOrder(TransOrder transOrder) {
        return transOrderMapper.insertSelective(transOrder);
    }

    public TransOrder selectByMchIdAndTransOrderId(Long mchId, String transOrderId) {
        TransOrderExample example = new TransOrderExample();
        TransOrderExample.Criteria criteria = example.createCriteria();
        criteria.andInfoIdEqualTo(mchId);
        criteria.andInfoTypeEqualTo(MchConstant.INFO_TYPE_MCH);
        criteria.andTransOrderIdEqualTo(transOrderId);
        List<TransOrder> transOrderList = transOrderMapper.selectByExample(example);
        return org.springframework.util.CollectionUtils.isEmpty(transOrderList) ? null : transOrderList.get(0);
    }


    public TransOrder selectByInfoIdAndMchTransNo(Long infoId, Byte infoType, String mchTransNo) {
        TransOrderExample example = new TransOrderExample();
        TransOrderExample.Criteria criteria = example.createCriteria();
        criteria.andInfoIdEqualTo(infoId);
        criteria.andInfoTypeEqualTo(infoType);
        criteria.andMchTransNoEqualTo(mchTransNo);
        List<TransOrder> transOrderList = transOrderMapper.selectByExample(example);
        return org.springframework.util.CollectionUtils.isEmpty(transOrderList) ? null : transOrderList.get(0);
    }



    public int updateStatus4Ing(String transOrderId, String channelOrderNo) {
        TransOrder transOrder = new TransOrder();
        transOrder.setStatus(PayConstant.TRANS_STATUS_TRANING);
        if(channelOrderNo != null) transOrder.setChannelOrderNo(channelOrderNo);
        TransOrderExample example = new TransOrderExample();
        TransOrderExample.Criteria criteria = example.createCriteria();
        criteria.andTransOrderIdEqualTo(transOrderId);
        List<Byte> list = new LinkedList<>();
        list.add(PayConstant.TRANS_STATUS_INIT);
        list.add(PayConstant.TRANS_STATUS_FAIL);
        criteria.andStatusIn(list);
        return transOrderMapper.updateByExampleSelective(transOrder, example);
    }

    public int updateStatus4Success(String transOrderId) {
        return updateStatus4Success(transOrderId, null);
    }

    public int updateStatus4Success(String transOrderId, String channelOrderNo) {
        TransOrder transOrder = new TransOrder();
        transOrder.setTransOrderId(transOrderId);
        transOrder.setStatus(PayConstant.TRANS_STATUS_SUCCESS);
        transOrder.setResult(PayConstant.TRANS_RESULT_SUCCESS);
        transOrder.setTransSuccTime(new Date());
        if(StringUtils.isNotBlank(channelOrderNo)) transOrder.setChannelOrderNo(channelOrderNo);
        TransOrderExample example = new TransOrderExample();
        TransOrderExample.Criteria criteria = example.createCriteria();
        criteria.andTransOrderIdEqualTo(transOrderId);
        criteria.andStatusEqualTo(PayConstant.TRANS_STATUS_TRANING);
        return transOrderMapper.updateByExampleSelective(transOrder, example);
    }

    public int updateStatus4Fail(String transOrderId, String channelErrCode, String channelErrMsg) {
        TransOrder transOrder = new TransOrder();
        transOrder.setStatus(PayConstant.TRANS_STATUS_FAIL);
        transOrder.setResult(PayConstant.TRANS_RESULT_FAIL);
        if(channelErrCode != null) transOrder.setChannelErrCode(channelErrCode);
        if(channelErrMsg != null) transOrder.setChannelErrMsg(channelErrMsg);
        TransOrderExample example = new TransOrderExample();
        TransOrderExample.Criteria criteria = example.createCriteria();
        criteria.andTransOrderIdEqualTo(transOrderId);
        criteria.andStatusEqualTo(PayConstant.TRANS_STATUS_TRANING);
        return transOrderMapper.updateByExampleSelective(transOrder, example);
    }

    @Override
    public int updateStatus4Fail(String transOrderId, String channelErrCode, String channelErrMsg, String channelOrderNo) {
        TransOrder transOrder = new TransOrder();
        transOrder.setStatus(PayConstant.TRANS_STATUS_FAIL);
        transOrder.setResult(PayConstant.TRANS_RESULT_FAIL);
        if(channelErrCode != null) transOrder.setChannelErrCode(channelErrCode);
        if(channelErrMsg != null) transOrder.setChannelErrMsg(channelErrMsg);
        if(channelOrderNo != null) transOrder.setChannelOrderNo(channelOrderNo);
        TransOrderExample example = new TransOrderExample();
        TransOrderExample.Criteria criteria = example.createCriteria();
        criteria.andTransOrderIdEqualTo(transOrderId);
        criteria.andStatusEqualTo(PayConstant.TRANS_STATUS_TRANING);
        return transOrderMapper.updateByExampleSelective(transOrder, example);
    }

    @Override
    public List<TransOrder> select(int offset, int limit, TransOrder transOrder, Date createTimeStart, Date createTimeEnd) {
        TransOrderExample example = new TransOrderExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        TransOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, transOrder, createTimeStart, createTimeEnd);
        return transOrderMapper.selectByExample(example);
    }

    @Override
    public Integer count(TransOrder transOrder, Date createTimeStart, Date createTimeEnd) {
        TransOrderExample example = new TransOrderExample();
        TransOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, transOrder, createTimeStart, createTimeEnd);
        return transOrderMapper.countByExample(example);
    }

    @Override
    public TransOrder find(TransOrder transOrder) {
        TransOrderExample example = new TransOrderExample();
        TransOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, transOrder);
        List<TransOrder> transOrderList = transOrderMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(transOrderList)) return null;
        return transOrderList.get(0);
    }

    @Override
    public Long sumAmount4AgentpayPassageAccount(int agentpayPassageAccountId, Date creatTimeStart, Date createTimeEnd) {
        TransOrderExample example = new TransOrderExample();
        TransOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPassageAccountIdEqualTo(agentpayPassageAccountId);
        if(creatTimeStart != null) {
            criteria.andCreateTimeGreaterThanOrEqualTo(creatTimeStart);
        }
        if(createTimeEnd != null) {
            criteria.andCreateTimeLessThanOrEqualTo(createTimeEnd);
        }
        criteria.andStatusNotEqualTo(PayConstant.TRANS_STATUS_INIT);
        return transOrderMapper.sumAmountByExample(example);
    }

    @Override
    public Map count4All(Long mchId, String transOrderId, String mchTransNo, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(mchId != null) param.put("mchId", mchId);
        if(StringUtils.isNotBlank(transOrderId)) param.put("transOrderId", transOrderId);
        if(StringUtils.isNotBlank(mchTransNo)) param.put("mchOrderNo", mchTransNo);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return transOrderMapper.count4All(param);
    }

    @Override
    public Map count4Success(Long mchId, String transOrderId, String mchTransNo, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(mchId != null) param.put("mchId", mchId);
        if(StringUtils.isNotBlank(transOrderId)) param.put("transOrderId", transOrderId);
        if(StringUtils.isNotBlank(mchTransNo)) param.put("mchTransNo", mchTransNo);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return transOrderMapper.count4Success(param);
    }

    void setCriteria(TransOrderExample.Criteria criteria, TransOrder transOrder) {
        setCriteria(criteria, transOrder, null, null);
    }

    void setCriteria(TransOrderExample.Criteria criteria, TransOrder transOrder, Date createTimeStart, Date createTimeEnd) {
        if(transOrder != null) {
            if(transOrder.getInfoId() != null) criteria.andInfoIdEqualTo(transOrder.getInfoId());
            if(transOrder.getInfoType() != null) criteria.andInfoTypeEqualTo(transOrder.getInfoType());
            if(StringUtils.isNotBlank(transOrder.getTransOrderId())) criteria.andTransOrderIdEqualTo(transOrder.getTransOrderId());
            if(StringUtils.isNotBlank(transOrder.getMchTransNo())) criteria.andMchTransNoEqualTo(transOrder.getMchTransNo());
            if(StringUtils.isNotBlank(transOrder.getChannelOrderNo())) criteria.andChannelOrderNoEqualTo(transOrder.getChannelOrderNo());
            if(transOrder.getStatus() != null && transOrder.getStatus() != -99) criteria.andStatusEqualTo(transOrder.getStatus());
        }
        if(createTimeStart != null) {
            criteria.andCreateTimeGreaterThanOrEqualTo(createTimeStart);
        }
        if(createTimeEnd != null) {
            criteria.andCreateTimeLessThanOrEqualTo(createTimeEnd);
        }
    }

}

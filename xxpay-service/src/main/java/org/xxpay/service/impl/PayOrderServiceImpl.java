package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.PayOrderExample;
import org.xxpay.core.service.IAccountBalanceService;
import org.xxpay.core.service.IFeeScaleService;
import org.xxpay.core.service.IPayOrderService;
import org.xxpay.service.dao.mapper.PayOrderMapper;

import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 17/9/8
 * @description:
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @Autowired
    private IFeeScaleService feeScaleService;

    private static final MyLog _log = MyLog.getLog(PayOrderServiceImpl.class);

    
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int createPayOrder(PayOrder payOrder) {
        return payOrderMapper.insertSelective(payOrder);
    }

    @Override
    public int closePayOrder(String payOrderId) {
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(payOrderId);
        payOrder.setStatus(PayConstant.PAY_STATUS_CLOSED);
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPayOrderIdEqualTo(payOrderId);
        criteria.andStatusEqualTo(PayConstant.PAY_STATUS_PAYING);
        int count = payOrderMapper.updateByExampleSelective(payOrder, example);
        return count;
    }

    public PayOrder selectPayOrder(String payOrderId) {
        return payOrderMapper.selectByPrimaryKey(payOrderId);
    }

    public PayOrder selectByMchIdAndPayOrderId(Long mchId, String payOrderId) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andPayOrderIdEqualTo(payOrderId);
        List<PayOrder> payOrderList = payOrderMapper.selectByExample(example);
        return org.springframework.util.CollectionUtils.isEmpty(payOrderList) ? null : payOrderList.get(0);
    }

    public PayOrder selectByMchIdAndMchOrderNo(Long mchId, String mchOrderNo) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andMchOrderNoEqualTo(mchOrderNo);
        List<PayOrder> payOrderList = payOrderMapper.selectByExample(example);
        return org.springframework.util.CollectionUtils.isEmpty(payOrderList) ? null : payOrderList.get(0);
    }

    public int updateStatus4Ing(String payOrderId, String channelOrderNo) {
        PayOrder payOrder = new PayOrder();
        payOrder.setStatus(PayConstant.PAY_STATUS_PAYING);
        if(channelOrderNo != null) payOrder.setChannelOrderNo(channelOrderNo);
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPayOrderIdEqualTo(payOrderId);
        criteria.andStatusEqualTo(PayConstant.PAY_STATUS_INIT);
        return payOrderMapper.updateByExampleSelective(payOrder, example);
    }

    public int updateStatus4Success(String payOrderId) {
        return updateStatus4Success(payOrderId, null);
    }

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int updateStatus4Success(String payOrderId, String channelOrderNo) {
        return updateStatus4Success(payOrderId, channelOrderNo, null);
    }

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int updateStatus4Success(String payOrderId, String channelOrderNo, String channelAttach) {

        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(payOrderId);
        payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
        payOrder.setPaySuccTime(new Date());
        if(StringUtils.isNotBlank(channelOrderNo)) payOrder.setChannelOrderNo(channelOrderNo);
        if(StringUtils.isNotBlank(channelAttach)) payOrder.setChannelAttach(channelAttach);
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPayOrderIdEqualTo(payOrderId);
        criteria.andStatusEqualTo(PayConstant.PAY_STATUS_PAYING);

        int count = payOrderMapper.updateByExampleSelective(payOrder, example);
        // 更新成功， 增加商户资金账户流水记录
        payOrder = selectPayOrder(payOrder.getPayOrderId());
        if(count == 1 ) {
            // accountBalanceService.calProfitAndChangeAmountAndInsertHistory(payOrder); TODO 重构分润计算
        }
        return count;

    }


    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int updateStatus4depositIng(String payOrderId, String channelOrderNo, String channelUser) {

        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(payOrderId);
        payOrder.setStatus(PayConstant.PAY_STATUS_DEPOSIT_ING);
        payOrder.setPaySuccTime(new Date());
        if(StringUtils.isNotBlank(channelOrderNo)) payOrder.setChannelOrderNo(channelOrderNo);
        payOrder.setChannelUser(channelUser);
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPayOrderIdEqualTo(payOrderId);
        criteria.andStatusEqualTo(PayConstant.PAY_STATUS_PAYING);

        int count = payOrderMapper.updateByExampleSelective(payOrder, example);
        return count;
    }

    // 更新为失败
    public int updateStatus4Fail(String payOrderId) {
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(payOrderId);
        payOrder.setStatus(PayConstant.PAY_STATUS_FAILED);
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPayOrderIdEqualTo(payOrderId);
        criteria.andStatusEqualTo(PayConstant.PAY_STATUS_PAYING);
        return payOrderMapper.updateByExampleSelective(payOrder, example);
    }

    public int updateNotify(String payOrderId, byte count) {
        PayOrder newPayOrder = new PayOrder();
        newPayOrder.setPayOrderId(payOrderId);
        return payOrderMapper.updateByPrimaryKeySelective(newPayOrder);
    }

    @Override
    public PayOrder find(PayOrder payOrder) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        List<PayOrder> payOrderList = payOrderMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(payOrderList)) return null;
        return payOrderList.get(0);
    }

    @Override
    public PayOrder findByPayOrderId(String payOrderId) {
        return payOrderMapper.selectByPrimaryKey(payOrderId);
    }

    @Override
    public PayOrder findByMchIdAndPayOrderId(Long mchId, String payOrderId) {
        return selectByMchIdAndPayOrderId(mchId, payOrderId);
    }

    @Override
    public PayOrder findByMchOrderNo(String mchOrderNo) {
        PayOrder payOrder = new PayOrder();
        payOrder.setMchOrderNo(mchOrderNo);
        return find(payOrder);
    }

    @Override
    public List<PayOrder> select(Long mchId, int offset, int limit, PayOrder payOrder, Date createTimeStart, Date createTimeEnd) {
        PayOrderExample example = new PayOrderExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        PayOrderExample.Criteria criteria = example.createCriteria();
        if(mchId != null) criteria.andMchIdEqualTo(mchId);
        setCriteria(criteria, payOrder, createTimeStart, createTimeEnd);
        return payOrderMapper.selectByExample(example);
    }

    @Override
    public List<PayOrder> select(int offset, int limit, PayOrder payOrder, Date createTimeStart, Date createTimeEnd) {
        return select(null, offset, limit, payOrder, createTimeStart, createTimeEnd);
    }

    @Override
    public Long count(Long mchId, PayOrder payOrder, Date createTimeStart, Date createTimeEnd) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        if(mchId != null) criteria.andMchIdEqualTo(mchId);
        setCriteria(criteria, payOrder, createTimeStart, createTimeEnd);
        return payOrderMapper.countByExample(example);
    }

    @Override
    public Long count(PayOrder payOrder, Date createTimeStart, Date createTimeEnd) {
        return count(null, payOrder, createTimeStart, createTimeEnd);
    }

    @Override
    public int updateByPayOrderId(String payOrderId, PayOrder payOrder) {
        payOrder.setPayOrderId(payOrderId);
        return payOrderMapper.updateByPrimaryKeySelective(payOrder);


    }

    @Override
    public Long sumAmount(PayOrder payOrder, List<Byte> statusList) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        if(CollectionUtils.isNotEmpty(statusList)) criteria.andStatusIn(statusList);
        return payOrderMapper.sumAmountByExample(example);
    }

    @Override
    public List<PayOrder> select(String channelMchId, String billDate, List<Byte> statusList) {
        PayOrderExample example = new PayOrderExample();
        example.setOrderByClause("createTime DESC");
        PayOrderExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(channelMchId)) criteria.andChannelMchIdEqualTo(channelMchId);
        if(CollectionUtils.isNotEmpty(statusList)) criteria.andStatusIn(statusList);
        if(StringUtils.isNotBlank(billDate)) {
            Date beginDate = DateUtil.str2date(billDate + " 00:00:00");
            Date endDate = DateUtil.str2date(billDate + " 23:59:59");
            criteria.andCreateTimeBetween(beginDate, endDate);
        }
        return payOrderMapper.selectByExample(example);
    }

    @Override
    public List<PayOrder> selectAllBill(int offset, int limit, String billDate) {
        PayOrderExample example = new PayOrderExample();
        example.setOrderByClause("mchId ASC");
        example.setLimit(limit);
        example.setOffset(offset);
        PayOrderExample.Criteria criteria = example.createCriteria();
        List<Byte> statusList = new LinkedList<>();
        // 查询成功
        criteria.andStatusEqualTo(PayConstant.PAY_STATUS_SUCCESS);
        if(StringUtils.isNotBlank(billDate)) {
            Date beginDate = DateUtil.str2date(billDate + " 00:00:00");
            Date endDate = DateUtil.str2date(billDate + " 23:59:59");
            criteria.andCreateTimeBetween(beginDate, endDate);
        }
        return payOrderMapper.selectByExample(example);
    }

    @Override
    public Map count4Income(Long agentId, Long mchId, Byte productType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(agentId != null) param.put("agentId", agentId);
        if(mchId != null) param.put("mchId", mchId);
        if(productType != null) param.put("productType", productType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.count4Income(param);
    }

    @Override
    public List<Map> count4MchTop(Long agentId, Long mchId, Byte productType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(agentId != null) param.put("agentId", agentId);
        if(mchId != null) param.put("mchId", mchId);
        if(productType != null && productType != -99) param.put("productType", productType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.count4MchTop(param);
    }

    /*@Override
    public List<Map> count4AgentTop(String agentId, String bizType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(StringUtils.isNotBlank(agentId)) param.put("agentId", agentId);
        if(StringUtils.isNotBlank(bizType)) param.put("bizType", bizType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.count4AgentTop(param);
    }*/

    @Override
    public List<Map> count4Pay(String idName, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(StringUtils.isBlank(idName)) return null;
        param.put("idName", idName);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.count4Pay(param);
    }

    @Override
    public List<Map> count4PayProduct(String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.count4PayProduct(param);
    }

    @Override
    public Long sumAmount4PayPassageAccount(int payPassageAccountId, Date creatTimeStart, Date createTimeEnd) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
//        criteria.andPassageAccountIdEqualTo(payPassageAccountId); //TODO 删除通道子账号ID
        if(creatTimeStart != null) {
            criteria.andCreateTimeGreaterThanOrEqualTo(creatTimeStart);
        }
        if(createTimeEnd != null) {
            criteria.andCreateTimeLessThanOrEqualTo(createTimeEnd);
        }
        criteria.andStatusNotEqualTo(PayConstant.PAY_STATUS_INIT);
        return payOrderMapper.sumAmountByExample(example);
    }

    @Override
    public Map count4All(Long agentId, Long mchId, Long productId, String payOrderId, String mchOrderNo, Byte productType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(agentId != null) param.put("agentId", agentId);
        if(mchId != null) param.put("mchId", mchId);
        if(productId != null && productId != -99) param.put("productId", productId);
        if(StringUtils.isNotBlank(payOrderId)) param.put("payOrderId", payOrderId);
        if(StringUtils.isNotBlank(mchOrderNo)) param.put("mchOrderNo", mchOrderNo);
        if(productType != null && productType != -99) param.put("productType", productType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.count4All(param);
    }

    @Override
    public Map count4Success(Long agentId, Long mchId, Long productId, String payOrderId, String mchOrderNo, Byte productType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(agentId != null) param.put("agentId", agentId);
        if(mchId != null) param.put("mchId", mchId);
        if(productId != null && productId != -99) param.put("productId", productId);
        if(StringUtils.isNotBlank(payOrderId)) param.put("payOrderId", payOrderId);
        if(StringUtils.isNotBlank(mchOrderNo)) param.put("mchOrderNo", mchOrderNo);
        if(productType != null && productType != -99) param.put("productType", productType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.count4Success(param);
    }

    @Override
    public Map count4Fail(Long agentId, Long mchId, Long productId, String payOrderId, String mchOrderNo, Byte productType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(agentId != null) param.put("agentId", agentId);
        if(mchId != null) param.put("mchId", mchId);
        if(productId != null && productId != -99) param.put("productId", productId);
        if(StringUtils.isNotBlank(payOrderId)) param.put("payOrderId", payOrderId);
        if(StringUtils.isNotBlank(mchOrderNo)) param.put("mchOrderNo", mchOrderNo);
        if(productType != null && productType != -99) param.put("productType", productType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.count4Fail(param);
    }

    @Override
    public List<Map> getAllChannel() {
        return payOrderMapper.getAllChannel();
    }

    @Override
    public List<Map> channelStatistics(Object passageId, Long productId, String ifCode, String payType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(passageId != null) param.put("passageId", passageId);
        if(productId != null && productId != -99) param.put("productId", productId);
        if(StringUtils.isNotBlank(ifCode) && !ifCode.equals("-99")) param.put("ifCode", ifCode);
        if(StringUtils.isNotBlank(payType) && !payType.equals("-99")) param.put("payType", payType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.channelStatistics(param);
    }

    @Override
    public List<Map> paymentStatistics(int offset, int limit, Long mchId, Byte productType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap();
        param.put("offset",offset);
        param.put("limit",limit);
        if (mchId != null) param.put("mchId",mchId);
        if (productType != null && productType != -99) param.put("productType",productType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.paymentStatistics(param);
    }

    @Override
    public List<Map> count4PaymentStatistics(Long mchId, Byte productType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap();
        if (mchId != null) param.put("mchId",mchId);
        if (productType != null && productType != -99) param.put("productType",productType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return payOrderMapper.count4PaymentStatistics(param);
    }


    void setCriteria(PayOrderExample.Criteria criteria, PayOrder payOrder, Date createTimeStart, Date createTimeEnd) {
        if(payOrder != null) {
            if(payOrder.getMchId() != null) criteria.andMchIdEqualTo(payOrder.getMchId());
            if(payOrder.getProductId() != null && payOrder.getProductId() != -99) criteria.andProductIdEqualTo(payOrder.getProductId());
            if((payOrder.getAgentId() != null)) criteria.andAgentIdEqualTo(payOrder.getAgentId());
            if (payOrder.getIsvId() != null) criteria.andIsvIdEqualTo(payOrder.getIsvId());
            if(StringUtils.isNotBlank(payOrder.getPayOrderId())) criteria.andPayOrderIdEqualTo(payOrder.getPayOrderId());
            if(StringUtils.isNotBlank(payOrder.getMchOrderNo())) criteria.andMchOrderNoEqualTo(payOrder.getMchOrderNo());
            if(StringUtils.isNotBlank(payOrder.getChannelOrderNo())) criteria.andChannelOrderNoEqualTo(payOrder.getChannelOrderNo());
            if(payOrder.getStatus() != null && payOrder.getStatus() != -99) criteria.andStatusEqualTo(payOrder.getStatus());
            if(payOrder.getProductType() != null && payOrder.getProductType() != -99) criteria.andProductTypeEqualTo(payOrder.getProductType());
            if(StringUtils.isNotBlank(payOrder.getChannelMchId())) criteria.andChannelMchIdEqualTo(payOrder.getChannelMchId());
        }
        if(createTimeStart != null) {
            criteria.andCreateTimeGreaterThanOrEqualTo(createTimeStart);
        }
        if(createTimeEnd != null) {
            criteria.andCreateTimeLessThanOrEqualTo(createTimeEnd);
        }
    }

    void setCriteria(PayOrderExample.Criteria criteria, PayOrder payOrder) {
        setCriteria(criteria, payOrder, null, null);
    }
    
    @Override
    public boolean hasOrderAndExpire(Long mchId, String mchOrderNo){
    	PayOrderExample exa = new PayOrderExample();
    	exa.createCriteria().andMchIdEqualTo(mchId).andMchOrderNoEqualTo(mchOrderNo).andExpireTimeLessThanOrEqualTo(new Date());
    	return payOrderMapper.countByExample(exa) > 0 ;
    }

    @Override
    public Map orderDayAmount(Long mchId, String dayStart, String dayEnd) {
        Map param = new HashMap<>();
        if (mchId != null) param.put("mchId",mchId);
        if (StringUtils.isNotBlank(dayStart)) param.put("createTimeStart", dayStart);
        if (StringUtils.isNotBlank(dayEnd)) param.put("createTimeEnd", dayEnd);
        return payOrderMapper.orderDayAmount(param);
    }

    @Override
    public List<Map> orderProductSuccess() {
        return payOrderMapper.orderProductSuccess();
    }


    public int updateStatus4Expired(String payOrderId) {
        PayOrder payOrder = new PayOrder();
        payOrder.setStatus(PayConstant.PAY_STATUS_EXPIRED);
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPayOrderIdEqualTo(payOrderId);
        criteria.andStatusEqualTo(PayConstant.PAY_STATUS_PAYING);
        return payOrderMapper.updateByExampleSelective(payOrder, example);
    }

}

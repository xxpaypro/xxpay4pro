package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.PayOrderExt;
import org.xxpay.core.service.IPayOrderExtService;
import org.xxpay.service.dao.mapper.PayOrderExtMapper;

/**
 * <p><b>Title: </b>PayOrderExtServiceImpl.java
 * <p><b>Description: </b>訂單擴展信息service
 * @author terrfly
 * @version V1.0
 * <p>
 */
@Service
public class PayOrderExtServiceImpl extends ServiceImpl<PayOrderExtMapper, PayOrderExt> implements IPayOrderExtService {

	@Autowired
	private PayOrderExtMapper recordMapper ; 
	
	
	@Override
	public void addExtInfo(String payOrderId, Long mchId, String mchOrderNo, String retData) {
		PayOrderExt ext = new PayOrderExt();
		ext.setPayOrderId(payOrderId);
		ext.setMchOrderNo(mchOrderNo);
		ext.setMchId(mchId);
		ext.setRetData(retData);
		recordMapper.insert(ext);
	}

	@Override
	public String queryCanRepeatPlaceOrderExt(Long mchId, String mchOrderNo) {
		
		return recordMapper.queryCanRepeatPlaceOrderExt(mchId, mchOrderNo);
	}
	
}

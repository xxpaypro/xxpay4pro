package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.entity.Msgcode;
import org.xxpay.core.entity.MsgcodeExample;
import org.xxpay.core.service.IMsgcodeService;
import org.xxpay.service.dao.mapper.MsgcodeMapper;

/**
 * <p><b>Title: </b>MsgcodeServiceImpl.java
 * <p><b>Description: </b>
 * @author terrfly
 * @version V1.0
 * <p>
 */
@Service
public class MsgcodeServiceImpl extends ServiceImpl<MsgcodeMapper, Msgcode> implements IMsgcodeService {

	
	@Autowired
	private MsgcodeMapper msgcodeMapper;

	@Override
	public boolean addCode(String phoneNo, String code, Byte bizType, Integer expTime, Integer todayLimit) {
		
		return msgcodeMapper.addCode(phoneNo, code, bizType, expTime, todayLimit) > 0 ;
	}

	@Override
	public boolean addCode(String phoneNo, String code, Byte bizType) {
		return addCode(phoneNo, code, bizType, MchConstant.MSGCODE_EXP_TIME, MchConstant.MSGCODE_MCH_TODAY_COUNT);
	}

	@Override
	public boolean verifyCode(String phoneNo, String code, Byte bizType) {
		
		return msgcodeMapper.verifyCode(phoneNo, code, bizType) > 0;
	}

	@Override
	public Integer countToday(String phoneNo, Byte bizType) {
		MsgcodeExample exa = new MsgcodeExample();
		String todayDate = DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYY_MM_DD);
		exa.createCriteria().andPhoneNoEqualTo(phoneNo).andBizTypeEqualTo(bizType)
		.andCreateTimeGreaterThanOrEqualTo(DateUtil.str2date(todayDate + " 00:00:00"))
		.andCreateTimeLessThanOrEqualTo(DateUtil.str2date(todayDate + " 23:59:59"));
		return msgcodeMapper.countByExample(exa);
	}



}

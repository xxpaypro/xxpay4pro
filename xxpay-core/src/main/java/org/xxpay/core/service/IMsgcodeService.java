package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.Msgcode;

/**
 * <p><b>Title: </b>IMsgcodeService.java
 * <p><b>Description: </b>短信验证码相关服务
 * @author terrfly
 * @version V1.0
 * <p>
 */
public interface IMsgcodeService extends IService<Msgcode> {
	
	/**
	 * <p><b>Description: </b>新增验证码
	 * <p>2018年9月25日 下午3:24:17
	 * @author matf
	 * @param phoneNo 手机号    
	 * @param code 验证码
	 * @param bizType 业务代码
	 * @param expTime 验证码失效时间  单位 min
	 * @param todayLimit 限制今日发送次数
	 * @return
	 */
	public boolean addCode(String phoneNo, String code, Byte bizType, Integer expTime, Integer todayLimit);
	
	/**
	 * <p><b>Description: </b>  新增验证码 
	 * 默认参数：验证码验证时间为：10分钟（MchConstant.MSG_CODE_EXP_TIME）
	 * 默认参数：验证码当天发送累计次数为 10次（MchConstant.MSG_CODE_MCH_TODAY_COUNT）
	 * <p>2018年9月25日 下午3:24:17
	 * @author matf
	 * @param phoneNo 手机号    
	 * @param code 验证码
	 * @param bizType 业务代码
	 * @return
	 */
	public boolean addCode(String phoneNo, String code, Byte bizType);
	
	
	/**
	 * <p><b>Description: </b>验证短信码值
	 * <p>2018年9月25日 下午3:28:51
	 * @author matf
	 * @param phoneNo 手机号
	 * @param code 短信验证码记录值
	 * @param bizType 业务代码
	 * @return
	 */
	public boolean verifyCode(String phoneNo, String code, Byte bizType);
	
	
	/**
	 * <p><b>Description: </b>查询 当天日累计次数
	 * <p>2018年9月25日 下午3:42:03
	 * @author matf
	 * @param phoneNo 手机号
	 * @param bizType 业务代码
	 * @return
	 */
	public Integer countToday(String phoneNo, Byte bizType);
	
	
	


}

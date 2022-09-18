package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.PayOrderExt;

/**
 * <p><b>Title: </b>IPayOrderExtService.java
 * <p><b>Description: </b>
 * @author terrfly
 * @version V1.0
 * <p>
 */
public interface IPayOrderExtService extends IService<PayOrderExt> {

	/**
	 * <p><b>Description: </b>錄入訂單擴展信息
	 * <p>2018年10月30日 下午3:38:36
	 * @author terrfly
	 * @param payOrderId
	 * @param mchId
	 * @param mchOrderNo
	 * @param retData
	 */
    public void addExtInfo(String payOrderId, Long mchId, String mchOrderNo, String retData);
    
    /**
     * <p><b>Description: </b>查詢可重複下單的訂單擴展信息
     * <p>2018年10月30日 下午3:38:53
     * @author terrfly
     * @param mchId
     * @param mchOrderNo
     * @return
     */
    public String queryCanRepeatPlaceOrderExt(Long mchId, String mchOrderNo);

}

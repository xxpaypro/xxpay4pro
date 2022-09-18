package org.xxpay.mch.utils.printerUtils;

import org.apache.commons.lang3.StringUtils;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyBase64;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.mch.utils.printerUtils.beans.DataPrinterListBean;
import org.xxpay.mch.utils.printerUtils.beans.PrinterList;
import org.xxpay.mch.utils.printerUtils.beans.ServerBean;
import org.xxpay.mch.utils.printerUtils.beans.TaskList;
import org.xxpay.mch.utils.printerUtils.consts.consts;
import org.xxpay.mch.utils.printerUtils.exception.ServerErrorException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("restriction")
public class PrintApisUtil {

	public static String serverUrl = "http://printsrv.quickticket.cn";

	/**
	 * 获取客户端连接Token
	 * @return 非空就是服务器返回的Token
	 */
	public static String GetToken(String strAccessKey, String strAccessSecret) {
		String strToken = "";

		try {
			String totUrl = serverUrl + "/api/getAccessToken?";
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("accessKey", strAccessKey);
			data.put("accessSecret", strAccessSecret);
			String strRet = PrintUtils.requestPost(data, totUrl, "");
			ServerBean<?> serverBean = JacksonUtils.json2pojo(strRet, ServerBean.class);
			if (serverBean.getCode() == 0)
				strToken = (String) serverBean.getData();
		}  catch (ServerErrorException e) {
			throw e;
		} 
		return strToken;
	}

    /**
     * 打印文本格式调整
     * @param mchTradeOrder
     * @return
     * @throws UnsupportedEncodingException
     */
	public static byte[] contentPrinter(MchTradeOrder mchTradeOrder) throws UnsupportedEncodingException {

	    String payOrderId = "交易单号："+mchTradeOrder.getTradeOrderId();
	    String orderAmount = "订单金额："+AmountUtil.convertCent2Dollar(mchTradeOrder.getOrderAmount());
        String discountAmount = "优惠金额："+AmountUtil.convertCent2Dollar(mchTradeOrder.getDiscountAmount());
        String amount = "实付金额："+AmountUtil.convertCent2Dollar(mchTradeOrder.getAmount());
        String memberTel = "会员手机号："+mchTradeOrder.getMemberTel();
        String storeName = "门店名称："+mchTradeOrder.getStoreName();
        String operatorName = "操作员："+mchTradeOrder.getOperatorName();
        String payTime = "交易时间："+mchTradeOrder.getTradeSuccTime();
        String td = "0A";

		byte[] payOrderId1 = getString(payOrderId);
		byte[] orderAmount2 = getString(orderAmount);
		byte[] discountAmount3 = getString(discountAmount);
		byte[] amount4 = getString(amount);
		byte[] memberTel5 = getString(memberTel);
		byte[] storeName6 = getString(storeName);
		byte[] operatorName7 = getString(operatorName);
		byte[] payTime8 = getString(payTime);
		//换行指令
		byte[] line = PrintUtils.String2Hex(td);
        byte[] result1 = addBytes(payOrderId1, line);
        byte[] result2 = addBytes(result1, orderAmount2);
        byte[] result3 = addBytes(result2, line);
        byte[] result4 = addBytes(result3, discountAmount3);
        byte[] result5 = addBytes(result4, line);
        byte[] result6 = addBytes(result5, amount4);
        byte[] result7 = addBytes(result6, line);
        byte[] result8 = addBytes(result7, memberTel5);
        byte[] result9 = addBytes(result8, line);
        byte[] result10 = addBytes(result9, storeName6);
        byte[] result11 = addBytes(result10, line);
        byte[] result12 = addBytes(result11, operatorName7);
        byte[] result13 = addBytes(result12, line);
        byte[] result14 = addBytes(result13, payTime8);
        byte[] result15 = addBytes(result14, line);
        return result15;
    }

	/**
	 * 数组拼接
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static byte[] addBytes(byte[] data1, byte[] data2) {
		byte[] data3 = new byte[data1.length + data2.length];
		System.arraycopy(data1,0 , data3, 0, data1.length);
		System.arraycopy(data2, 0, data3, data1.length, data2.length);
		return data3;
	}

	/**
	 * 打印数据
	 * @param sendData 要打印文本
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] getString(String sendData) throws UnsupportedEncodingException {
		return sendData.getBytes("gb2312");
	}

	/**
	 * 获取打印机状态信息
	 * 
	 * @param printerId
	 *            打印机Id号
	 * @return 打印机状态值
	 */
	public String GetPrinterStatus(String printerId, String token) throws ServerErrorException {
		String strStatus = "";
		try {
			if(StringUtils.isEmpty(token)){
					throw new ServerErrorException("获取服务器token失败");
			}
			String totUrl = serverUrl + "/api/queryPrinterList?";
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("printerId", printerId);
			data.put("page", "1");
			data.put("pageSize", "1");
			String strRet = PrintUtils.requestPost(data, totUrl, token);
			ServerBean<?> serverBean = JacksonUtils.json2pojo(strRet, ServerBean.class);
			if (serverBean.getCode() == 0) {
				DataPrinterListBean mList = JacksonUtils.getInstance().convertValue(serverBean.getData(),
						DataPrinterListBean.class);
				List<PrinterList> dlb = mList.getList();
				if (dlb.size() > 0)
					strStatus = dlb.get(0).getStatus();
			} else if (serverBean.getCode() == consts.TOKEN_EXPIRE ) {
				token = "";
				strStatus = GetPrinterStatus(printerId, token);
			}
		} catch(ServerErrorException e) {
			
		}
		return strStatus;
	}

	/**
	 * 发送打印数据给打印机
	 * @param textSend 文本打印数据
	 * @param printerId 打印机ID
	 * @param strCallback 回掉地址，打印任务成功后服务器会调用这个地址发送消息
	 * @param bussId 任务id，可以为空
	 * @return 非空值表示打印成功，并且是有效的任务id
	 */
	public String PrintString(String textSend, String printerId, String strCallback, String bussId, String token) throws ServerErrorException {
		//
		String strRetval = "";
		String totUrl = serverUrl + "/api/addPrintTask?";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("printerId", printerId);
		if(bussId==null || bussId=="")
			bussId = PrintUtils.MakeBussid();
		data.put("businessId", bussId);
		
		try {
			if(StringUtils.isEmpty(token)){
				throw new ServerErrorException("获取服务器token失败");
			}
			
			byte[] btText = textSend.getBytes("gb2312");
			String enCode = MyBase64.encode(btText);
			enCode = enCode.replaceAll("[\r\n]", "");
//			System.out.println("打印数据:" + enCode);
			data.put("data", enCode);
			data.put("notifyUrl", strCallback);
			String strRet = PrintUtils.PostData(data, totUrl, token);
			ServerBean<?> serverBean = JacksonUtils.json2pojo(strRet, ServerBean.class);
			if (serverBean.getCode() == 0) {
				strRetval = (String) serverBean.getData();
				strRetval = bussId;
			} else if (serverBean.getCode() == consts.TOKEN_EXPIRE ) {
				token = "";
				strRetval = PrintString(textSend, printerId, strCallback, bussId, token);
			}
		} catch (UnsupportedEncodingException e1) {
			throw new ServerErrorException(e1.getMessage());
		} catch (ServerErrorException se) {
			throw se;
		}
		return strRetval;
	}

	/**
	 * 发送命令数据给打印机
	 * @param btText 命令形式的打印数据
	 * @param printerId 打印机ID
	 * @param strCallback 回掉地址，打印任务成功后服务器会调用这个地址发送消息
	 * @param bussId 任务id，可以为空
	 * @return 非空值表示打印成功，并且是有效的任务id
	 * @throws ServerErrorException
	 */
	public static String PrintString(byte[] btText, String printerId, String strCallback, String bussId, String token) throws ServerErrorException {
		//
		String strRetval = "";
		String totUrl = serverUrl + "/api/addPrintTask?";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("printerId", printerId);
		if(bussId==null || bussId=="")
			bussId = PrintUtils.MakeBussid();
		data.put("businessId", bussId);
		
		try {
			if(StringUtils.isEmpty(token)){
				throw new ServerErrorException("获取服务器token失败");
			}
			String enCode = MyBase64.encode(btText);
			enCode = enCode.replaceAll("[\r\n]", "");
			data.put("data", enCode);
			data.put("notifyUrl", strCallback);
			String strRet = PrintUtils.PostData(data, totUrl, token);
			ServerBean<?> serverBean = JacksonUtils.json2pojo(strRet, ServerBean.class);
			if (serverBean.getCode() == 0) {
				strRetval = (String) serverBean.getData();
				strRetval = bussId;
			} else if (serverBean.getCode() == consts.TOKEN_EXPIRE ) {
				token = "";
				strRetval = PrintString(btText, printerId, strCallback, bussId, token);
			}
		} catch (ServerErrorException se) {
			throw se;
		}
		return strRetval;
	}

	/**
	 * 获取当前打印任务状态
	 * @param bussId 任务ID
	 * @return 任务状态字符串
	 */
	public String QueryTaskStatus(String bussId, String token) {
		// 获取打印结果
		String strStatus = "";
		String totUrl = serverUrl + "/api/searchTask?";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("businessId", bussId);
		try {
			if(token=="") {
				throw new ServerErrorException("获取服务器token失败");
			}
			
			String strRet = PrintUtils.requestPost(data, totUrl, token);
			ServerBean<?> serverBean = JacksonUtils.json2pojo(strRet, ServerBean.class);
			if (serverBean.getCode() == 0) {
				TaskList mOneObj = JacksonUtils.getInstance().convertValue(serverBean.getData(), TaskList.class);
				strStatus = mOneObj.getStatus();
			} else if (serverBean.getCode() == consts.TOKEN_EXPIRE ) {
				token = "";
				strStatus = QueryTaskStatus(bussId, token);
			}
		} catch (Exception ex) {
			ex.getStackTrace();
		}
		return strStatus;
	}

	/**
	 * 撤销打印任务
	 * @param bussId 任务id
	 * @return true 成功 false 失败
	 */
	public boolean CancelTask(String bussId, String token) {
		boolean bRet = false;
		String totUrl = serverUrl + "/api/cancelTask?";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("businessId", bussId);
		try {
			if(token=="") {
				throw new ServerErrorException("获取服务器token失败");
			}
			
			String strRet = PrintUtils.requestPost(data, totUrl, token);
			ServerBean<?> serverBean = JacksonUtils.json2pojo(strRet, ServerBean.class);
			if (serverBean.getCode() == 0) {
				bRet = true;
			} else if (serverBean.getCode() == consts.TOKEN_EXPIRE ) {
				token = "";
				bRet = CancelTask(bussId, token);
			}
		} catch (Exception ex) {
			ex.getStackTrace();
		}
		return bRet;
	}
	
}

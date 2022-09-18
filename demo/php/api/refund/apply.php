<?php
	require("../_config.php");  //公共配置文件
	require("../_utils.php");  //工具类

	if(!isset($_REQUEST["amount"]) || !isset($_REQUEST["mchOrderNo"]) || !isset($_REQUEST["mchRefundNo"])){
		echo '参数丢失';
		exit;
	}
	$amount = $_REQUEST["amount"] * 1 * 100; //元转换为分
    $paramArray = array(
		"mchId" => $mchId, //商户ID
		"mchOrderNo" => $_REQUEST["mchOrderNo"] ,  // 支付订单-商户订单号
		"mchRefundNo" => $_REQUEST["mchRefundNo"] ,  // 商户退款单号
		"amount" => $amount . "", // 支付金额
		"currency" => 'cny',  //币种
		"clientIp" => '210.73.10.148',   //客户端IP
		"device" => 'ios10.3.1',    //客户端设备
		"extra" =>  '',	 //附加参数
		"param1" => '',	 //扩展参数1
		"param2" =>  '',	 //扩展参数2

		//如果notifyUrl 不为空表示异步退款，具体退款结果以退款通知为准
        "notifyUrl" => '',	 //同步处理
        //"notifyUrl" => 'http://localhost/api/refund/notify.php',	 //异步处理
		"channelUser" =>  '',	 //渠道用户标识,如微信openId,支付宝账号
		"userName" =>  '',	 //用户姓名
		"remarkInfo" =>  '用户退款'	 //备注
    );
   
	$sign = paramArraySign($paramArray, $mchKey);  //签名
	$paramArray["sign"] = $sign;

	$paramsStr = http_build_query($paramArray); //请求参数str
	$response = httpPost($payHost . "/api/refund/create_order", $paramsStr);
	
	echo $response;

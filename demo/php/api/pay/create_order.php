<?php
	require("../_config.php");  //公共配置文件
	require("../_utils.php");  //工具类

	if(!isset($_REQUEST["amount"]) || !isset($_REQUEST["productId"]) || !isset($_REQUEST["mchOrderNo"])){
		echo '参数丢失';
		exit;
	}
	$amount = $_REQUEST["amount"] * 1 * 100; //元转换为分
    $paramArray = array(
		"mchId" => $mchId, //商户ID
		"appId" => $_REQUEST["appId"],  //商户应用ID
		"productId" => $_REQUEST["productId"],  //支付产品ID
		"mchOrderNo" => $_REQUEST["mchOrderNo"] ,  // 商户订单号
		"currency" => 'cny',  //币种
		"amount" => $amount . "", // 支付金额
		"clientIp" => '210.73.10.148',   //客户端IP
		"device" => 'ios10.3.1',    //客户端设备
		"returnUrl" => 'http://localhost/html/return_page.html',	 //支付结果前端跳转URL
		"notifyUrl" => 'http://localhost/api/pay/notify.php',	 //支付结果后台回调URL
		"subject" => '网络购物',	 //商品主题
		"body" => '网络购物',	 //商品描述信息
		"param1" => '',	 //扩展参数1
		"param2" =>  '',	 //扩展参数2
		"channelUserId" =>  $_REQUEST["channelUserId"],	 //渠道用户ID
		"extra" =>  $_REQUEST["extra"]	 //附加参数
    );
   
	$sign = paramArraySign($paramArray, $mchKey);  //签名
	$paramArray["sign"] = $sign;

	$paramsStr = http_build_query($paramArray); //请求参数str
	$response = httpPost($payHost . "/api/pay/create_order", $paramsStr);
	
	echo $response;

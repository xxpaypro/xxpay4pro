<?php
	
	require("../_config.php");  //公共配置文件
	require("../_utils.php");  //工具类

	if(!isset($_REQUEST["mchRefundOrder"]) ){
		echo '参数丢失';
		exit;
	}
	
    $paramArray = array(
		"mchId" => $mchId, //商户ID
		"appId" => '', //应用ID
		"mchRefundNo" => $_REQUEST["mchRefundOrder"],  //商户退款单号
		"refundOrderId" => '',  //支付中心退款订单号
		"executeNotify" => 'false', // 是否执行回调
		
    );
   
	$sign = paramArraySign($paramArray, $mchKey);  //签名
	$paramArray["sign"] = $sign;

	$paramsStr = http_build_query($paramArray); //请求参数str
	
	$response = httpPost($payHost . "/api/refund/query_order", $paramsStr);
	
	echo $response;

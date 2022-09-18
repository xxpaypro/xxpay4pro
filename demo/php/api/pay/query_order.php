<?php
	
	require("../_config.php");  //公共配置文件
	require("../_utils.php");  //工具类

	if(!isset($_REQUEST["mchOrderNo"]) ){
		echo '参数丢失';
		exit;
	}
	
    $paramArray = array(
		"mchId" => $mchId, //商户ID
		"payOrderId" => '',  //支付中心生成的订单号，与mchOrderNo二者传一即可
		"mchOrderNo" => $_REQUEST["mchOrderNo"],  //商户生成的订单号，与payOrderId二者传一即可
		"executeNotify" => 'false', // 是否执行回调
		
    );
   
	$sign = paramArraySign($paramArray, $mchKey);  //签名
	$paramArray["sign"] = $sign;

	$paramsStr = http_build_query($paramArray); //请求参数str
	
	$response = httpPost($payHost . "/api/pay/query_order", $paramsStr);
	
	echo $response;

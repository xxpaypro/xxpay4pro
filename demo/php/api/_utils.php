<?php

	function httpPost($url, $paramStr){
		$curl = curl_init();
		curl_setopt_array($curl, array(
		  CURLOPT_URL => $url,
		  CURLOPT_RETURNTRANSFER => 1,
		  CURLOPT_TIMEOUT => 30,
		  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
		  CURLOPT_CUSTOMREQUEST => "POST",
		  CURLOPT_POSTFIELDS => $paramStr,
		  CURLOPT_HTTPHEADER => array(
			"cache-control: no-cache",
			"content-type: application/x-www-form-urlencoded"
		  ),
		));
		$response = curl_exec($curl);
		$err = curl_error($curl);
		curl_close($curl);
		if ($err) {
		  return $err;
		}
		return $response;
	}
	
	function paramArraySign($paramArray, $mchKey){
		
		ksort($paramArray);  //字典排序
		reset($paramArray);
	
		$md5str = "";
		foreach ($paramArray as $key => $val) {
			if( strlen($key)  && strlen($val) ){
				$md5str = $md5str . $key . "=" . $val . "&";
			}
		}
		$sign = strtoupper(md5($md5str . "key=" . $mchKey));  //签名
		
		return $sign;
		
	}
    
package org.xxpay.pay.channel.transfarpay.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * httpClient4 工具类
 * @author: jiangyun
 * @since : 2016-2-17 16:17:20
 */

public class HttpClient4Utils {
	
	//设置默认超时时间为60s
	public static final int DEFAULT_TIME_OUT	= 60*1000;
	
	//http请求
	public static String sendHttpRequest(String url, Map<String, Object> paramMap, String charset, boolean isPost) {
		return sendHttpRequest(url, paramMap, charset, isPost, DEFAULT_TIME_OUT);
	}
	
	//http请求
	public static String sendHttpRequest(String url, Map<String, Object> paramMap, String charset, boolean isPost, int timeout) {
		if(isPost) {
			return httpPost(url, paramMap, charset, timeout);
		}
		
		return httpGet(url, paramMap, charset, timeout);
	}
	
	//post请求
	public static String httpPost(String url, Map<String, Object> params, String charset, int timeout) {
		
		if(url == null || url.equals("")) {
			return null;
		}
		
		String result		= null;
		
		//超时设置
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();
		
		//参数组装
		List<NameValuePair> pairs	= new ArrayList<NameValuePair>();
		for(Entry<String, Object> entry : params.entrySet()) {
			String key		= entry.getKey();
			String value	= (String) entry.getValue();
			pairs.add(new BasicNameValuePair(key, formatStr(value)));
		}

		CloseableHttpClient httpClient 	= HttpClients.createDefault();
		HttpPost httpPost 				= null;
		String responseBody 			= null;
		CloseableHttpResponse response	= null;

		try {
			httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(new UrlEncodedFormEntity(pairs));
			response = httpClient.execute(httpPost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			
			HttpEntity entity 	= response.getEntity();
			responseBody 		= EntityUtils.toString(entity, charset);
			result				= responseBody; 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源  
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	//get请求
	public static String httpGet(String url, Map<String, Object> params, String charset, int timeout) {
		
		if(url == null || url.equals("")) {
			return null;
		}
		
		String result = null;
		
		CloseableHttpClient httpClient 	= HttpClients.createDefault();
		HttpGet httpGet 				= null;
		String responseBody 			= null;
		CloseableHttpResponse response	= null;

		try {
			
			if(params != null && !params.isEmpty()) {
				List<NameValuePair> pairs	= new ArrayList<NameValuePair>();
				for(Entry<String, Object> entry : params.entrySet()) {
					String key		= entry.getKey();
					String value	= (String) entry.getValue();
					pairs.add(new BasicNameValuePair(key, formatStr(value)));
				}
				url = url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
			}
			
			httpGet		= new HttpGet(url);
			response	= httpClient.execute(httpGet);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}

			HttpEntity entity 	= response.getEntity();
			responseBody 		= EntityUtils.toString(entity, charset);
			result				= responseBody; 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源  
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	public static String formatStr(String text) {
		return (text == null ? "" : text.trim());
	}

}

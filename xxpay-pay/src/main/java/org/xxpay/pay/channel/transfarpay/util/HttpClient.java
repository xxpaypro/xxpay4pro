/**
 * Copyright © 2014-2017 TransfarPay.All Rights Reserved.
 */
package org.xxpay.pay.channel.transfarpay.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/* 
 * 利用HttpClient进行post请求的工具类 
 */
public class HttpClient {

	// utf-8字符编码
	public static final String CHARSET_UTF_8 = "utf-8";

	// HTTP内容类型。
	public static final String CONTENT_TYPE_TEXT_HTML = "text/xml;charset=utf-8";

	// HTTP内容类型。
	public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

	// HTTP内容类型。相当于form表单的形式，提交数据
	public static final String CONTENT_TYPE_FORM_URL = "application/x-www-form-urlencoded;charset=utf-8";

	// HTTP内容类型。相当于form表单的形式，提交数据
	public static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";

	// 连接管理器
	private static PoolingHttpClientConnectionManager pool;

	// 请求配置
	private static RequestConfig requestConfig;

	static {

		try {
			// System.out.println("初始化HttpClientTest~~~开始");
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			// 配置同时支持 HTTP 和 HTPPS
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
			// 初始化连接管理器
			pool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			// 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
			pool.setMaxTotal(200);
			// 设置最大路由
			pool.setDefaultMaxPerRoute(2);
			// 根据默认超时限制初始化requestConfig
			int socketTimeout = 10000;
			int connectTimeout = 10000;
			int connectionRequestTimeout = 10000;
			requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(socketTimeout)
					.setConnectTimeout(connectTimeout).build();

			// System.out.println("初始化HttpClientTest~~~结束");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		// 设置请求超时时间
		requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).setConnectionRequestTimeout(50000).build();
	}

	public static CloseableHttpClient getHttpClient() {

		CloseableHttpClient httpClient = HttpClients.custom()
				// 设置连接池管理
				.setConnectionManager(pool)
				// 设置请求配置
				.setDefaultRequestConfig(requestConfig)
				// 设置重试次数
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();

		return httpClient;
	}

	/**
	 * 发送Post请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpPost(HttpPost httpPost) {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		// 响应内容
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = getHttpClient();
			// 配置请求信息
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			// 得到响应实例
			HttpEntity entity = response.getEntity();

			// 可以获得响应头
			// Header[] headers = response.getHeaders(HttpHeaders.CONTENT_TYPE);
			// for (Header header : headers) {
			// System.out.println(header.getName());
			// }

			// 得到响应类型
			// System.out.println(ContentType.getOrDefault(response.getEntity()).getMimeType());

			// 判断响应状态
			if (response.getStatusLine().getStatusCode() >= 300) {
				throw new Exception("HTTP Request is not success, Response code is " + response.getStatusLine().getStatusCode());
			}

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				responseContent = EntityUtils.toString(entity, CHARSET_UTF_8);
				EntityUtils.consume(entity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}

	/**
	 * 发送Get请求
	 * 
	 * @param httpGet
	 * @return
	 */
	private static String sendHttpGet(HttpGet httpGet) {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		// 响应内容
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = getHttpClient();
			// 配置请求信息
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			// 得到响应实例
			HttpEntity entity = response.getEntity();

			// 可以获得响应头
			// Header[] headers = response.getHeaders(HttpHeaders.CONTENT_TYPE);
			// for (Header header : headers) {
			// System.out.println(header.getName());
			// }

			// 得到响应类型
			// System.out.println(ContentType.getOrDefault(response.getEntity()).getMimeType());

			// 判断响应状态
			if (response.getStatusLine().getStatusCode() >= 300) {
				throw new Exception("HTTP Request is not success, Response code is " + response.getStatusLine().getStatusCode());
			}

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				responseContent = EntityUtils.toString(entity, CHARSET_UTF_8);
				EntityUtils.consume(entity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl
	 *            地址
	 */
	public static String sendHttpPost(String httpUrl) {
		// 创建httpPost
		HttpPost httpPost = new HttpPost(httpUrl);
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送 get请求
	 * 
	 * @param httpUrl
	 */
	public static String sendHttpGet(String httpUrl) {
		// 创建get请求
		HttpGet httpGet = new HttpGet(httpUrl);
		return sendHttpGet(httpGet);
	}

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl
	 *            地址
	 * @param params
	 *            参数(格式:key1=value1&key2=value2)
	 * 
	 */
	public static String sendHttpPost(String httpUrl, Map<String, Object> params) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
				valuePairs.add(nameValuePair);
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(valuePairs, "utf-8");
			formEntity.setContentType(CONTENT_TYPE_FORM_URL);
			httpPost.setEntity(formEntity);
			HttpResponse resp = client.execute(httpPost);

			HttpEntity entity = resp.getEntity();
			String respContent = EntityUtils.toString(entity, "utf-8").trim();
			httpPost.abort();

			return respContent;
			// 设置参数
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 发送 post请求 发送json数据
	 * 
	 * @param httpUrl
	 *            地址
	 * @param paramsJson
	 *            参数(格式 json)
	 * 
	 */
	public static String sendHttpPostJson(String httpUrl, String paramsJson) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		try {
			// 设置参数
			if (paramsJson != null && paramsJson.trim().length() > 0) {
				StringEntity stringEntity = new StringEntity(paramsJson, "UTF-8");
				stringEntity.setContentType(CONTENT_TYPE_JSON_URL);
				httpPost.setEntity(stringEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送 post请求 发送xml数据
	 * 
	 * @param httpUrl
	 *            地址
	 * @param paramsXml
	 *            参数(格式 Xml)
	 * 
	 */
	public static String sendHttpPostXml(String httpUrl, String paramsXml) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		try {
			// 设置参数
			if (paramsXml != null && paramsXml.trim().length() > 0) {
				StringEntity stringEntity = new StringEntity(paramsXml, "UTF-8");
				stringEntity.setContentType(CONTENT_TYPE_TEXT_HTML);
				httpPost.setEntity(stringEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}

	/**
	 * 将map集合的键值对转化成：key1=value1&key2=value2 的形式
	 * 
	 * @param parameterMap
	 *            需要转化的键值对集合
	 * @return 字符串
	 */
	public static String convertStringParamter(Map<String, Object> parameterMap) {
		StringBuffer parameterBuffer = new StringBuffer();
		try {
			if (parameterMap != null) {
				Iterator iterator = parameterMap.keySet().iterator();
				String key = null;
				String value = null;
				while (iterator.hasNext()) {
					key = (String) iterator.next();
					if (parameterMap.get(key) != null) {
						value = (String) parameterMap.get(key);
					} else {
						value = "";
					}
					parameterBuffer.append(key).append("=").append(value);
					if (iterator.hasNext()) {
						parameterBuffer.append("&");
					}
				}
			}
			return parameterBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) throws Exception {
		String url = "https://openapitest.tf56.com/service/api";// 测试环境openapi
		Map<String, Object> map = new HashMap<>();
		map.put("service_id", "tf56pay.enterprise.payToCustomerAccount");
		map.put("accountnumber", "8801000078413");
		map.put("appid", "2013001");
		map.put("dog_ak", "RU3Gv6584678xf8r");
		map.put("dog_sk", "08Oe4YI71I5S3e72qYT2");
		map.put("tf_timestamp", ParamUtil.strDate("yyyyMMddHHmmss"));
		map.put("subject", "商品");
		map.put("kind", "支付");
		map.put("tradepwd", "1989122987");
		map.put("transactionamount", "10.01");
		map.put("toaccountnumber", "5688130113017659");
		map.put("businessnumber", "11118951800");
		map.put("topartyid", "567950009");
		map.put("fromaccountnumber", "1");
		map.put("tf_sign", ParamUtil.map2MD5(map));
		map.remove("dog_sk");
		String returnstr = sendHttpPost(url, map);
		System.out.println("Result :->" + returnstr);
	}
}

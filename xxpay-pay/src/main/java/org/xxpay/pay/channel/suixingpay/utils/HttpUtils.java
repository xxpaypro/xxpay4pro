package org.xxpay.pay.channel.suixingpay.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
public class HttpUtils {

	/**
	 * 创建没有证书的SSL链接工厂类
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static SSLConnectionSocketFactory getSSLConnectionSocketFactory() throws NoSuchAlgorithmException,
			KeyStoreException, KeyManagementException {
		SSLContextBuilder   context = new SSLContextBuilder().useProtocol("TLSv1.2");
		context.loadTrustMaterial(null, new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
				return true;
			}
		});
		return new SSLConnectionSocketFactory(context.build());
	}

	/**
	 * 链接GET请求
	 * @param url
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String connectGetUrl(String url) throws KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException {
		SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
		CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		RequestConfig config = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(5000).build();
		HttpGet httpGet = null;
		CloseableHttpResponse resp = null;
		String jsonString = "";
		try {
			httpGet =	new HttpGet(url);
			httpGet.setConfig(config);
			resp = client.execute(httpGet);
			HttpEntity entity = resp.getEntity();
		    jsonString = EntityUtils.toString(entity, "UTF-8");
		} finally {
        	if (resp != null) {
        		try {
        			resp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
		}
		return jsonString;
	}

	/**
	 * 链接POST请求
	 * @param url
	 * @param jsonParm
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String connectPostUrl(String url, String jsonParm) throws KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException {
		SSLConnectionSocketFactory sslsf = getSSLConnectionSocketFactory();
		CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		RequestConfig config = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(10000).build();
		HttpPost httpPost = null;
		CloseableHttpResponse resp = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.setConfig(config);
			StringEntity params = new StringEntity(jsonParm,"UTF-8");
			params.setContentType("application/json");
	        httpPost.setEntity(params);
			resp = client.execute(httpPost);
			HttpEntity entity = resp.getEntity();
			String jsonString = EntityUtils.toString(entity, "UTF-8");

			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return jsonString;
			}
		}catch (IOException e) {
			if (e instanceof org.apache.http.conn.ConnectTimeoutException) {
				throw new org.apache.http.conn.ConnectTimeoutException("connect timed out");
			}
			if (e instanceof java.net.SocketTimeoutException) {
				throw new java.net.SocketTimeoutException("Read timed out");
			}
			throw new IOException("IOException", e);
		}  finally {
        	if (resp != null) {
        		try {
        			resp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

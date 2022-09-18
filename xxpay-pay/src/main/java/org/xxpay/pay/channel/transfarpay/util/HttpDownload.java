/**
 * Copyright © 2014-2017 TransfarPay.All Rights Reserved.
 */
package org.xxpay.pay.channel.transfarpay.util;

/** 
* 描述说明
* 
* @version V1.0
* @author huzz
* @Date 2017年5月13日 下午9:08:11
* @since JDK 1.7
*/
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 说明 利用httpclient下载文件 maven依赖 <dependency> <groupId>org.apache.httpcomponents</groupId>
 * <artifactId>httpclient</artifactId> <version>4.0.1</version> </dependency> 可下载http文件、图片、压缩文件 bug：获取response
 * header中Content-Disposition中filename中文乱码问题
 * 
 * 
 * 
 */
public class HttpDownload {

	public static final int cache = 10 * 1024;

	public static final boolean isWindows;

	public static final String splash;

	public static int SUCCESS = 200;

	public static final String root;
	static {
		if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
			isWindows = true;
			splash = "\\";
			root = "D:";
		} else {
			isWindows = false;
			splash = "/";
			root = "/search";
		}
	}

	public static void downLoadFile(String url, String localFilePath) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		OutputStream out = null;
		InputStream in = null;

		try {
			HttpGet httpGet = new HttpGet(url);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			in = entity.getContent();
			long length = entity.getContentLength();
			if (length <= 0) {
				System.out.println("下载文件不存在！");
				return;
			}

			System.out.println("The response value of token:" + httpResponse.getFirstHeader("token"));
			String localFileName = localFilePath + getFileName(httpResponse);
			File file = new File(localFileName);
			if (!file.exists()) {
				file.createNewFile();
			}

			out = new FileOutputStream(file);
			byte[] buffer = new byte[4096];
			int readLength = 0;
			while ((readLength = in.read(buffer)) > 0) {
				byte[] bytes = new byte[readLength];
				System.arraycopy(buffer, 0, bytes, 0, readLength);
				out.write(bytes);
			}

			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取response要下载的文件的默认路径
	 * 
	 * @param response
	 * @return
	 */
	public static String getFilePath(HttpResponse response) {
		String filepath = root + splash;
		String filename = getFileName(response);

		if (filename != null) {
			filepath += filename;
		} else {
			filepath += getRandomFileName();
		}
		return filepath;
	}

	/**
	 * 获取response header中Content-Disposition中的filename值
	 * 
	 * @param response
	 * @return
	 */
	public static String getFileName(HttpResponse response) {
		Header contentHeader = response.getFirstHeader("Content-Disposition");
		String filename = null;
		if (contentHeader != null) {
			HeaderElement[] values = contentHeader.getElements();
			if (values.length == 1) {
				NameValuePair param = values[0].getParameterByName("filename");
				if (param != null) {
					try {
						// filename = new String(param.getValue().toString().getBytes(), "utf-8");
						// filename=URLDecoder.decode(param.getValue(),"utf-8");
						filename = param.getValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return filename;
	}

	/**
	 * 获取随机文件名
	 * 
	 * @return
	 */
	public static String getRandomFileName() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static void outHeaders(HttpResponse response) {
		Header[] headers = response.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			System.out.println(headers[i]);
		}
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

	public final static void downLoadNew(String url, String localFilePath) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url);

			System.out.println("Executing request " + httpget.getRequestLine());

			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						if (!getRequestContentFile(entity, response)) {
							return "File download Failed!";
						} else {
							return "File download success!";
						}
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			String responseBody = httpclient.execute(httpget, responseHandler);
			System.out.println("------------------downLoadNew----------------------");
			System.out.println(responseBody);
		} catch (Exception e) {
			System.out.println("Error->:" + e);
		} finally {
			httpclient.close();
		}
	}

	public static boolean getRequestContentFile(HttpEntity entity, HttpResponse response) {
		String localFilePath = "D:\\test\\";
		OutputStream out = null;
		InputStream in = null;

		try {
			in = entity.getContent();
			long length = entity.getContentLength();
			if (length <= 0) {
				System.out.println("下载文件不存在！");
				return false;
			}
			String localFileName = localFilePath + getFileName(response);
			System.out.println("FileName->:" + getFileName(response));
			File file = new File(localFileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			byte[] buffer = new byte[4096];
			int readLength = 0;
			while ((readLength = in.read(buffer)) > 0) {
				byte[] bytes = new byte[readLength];
				System.arraycopy(buffer, 0, bytes, 0, readLength);
				out.write(bytes);
			}

			out.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// String url = "http://bbs.btwuji.com/job.php?action=download&pid=tpc&tid=320678&aid=216617";
		String url = "http://www.dy1000.com/img/20120701/1999311085_150_200.JPG";
		// String filepath = "D:\\test\\a.torrent";
		String filepath = "D:\\test\\a.jpg";
		HttpDownload.downLoadFile(url, filepath);
	}
}

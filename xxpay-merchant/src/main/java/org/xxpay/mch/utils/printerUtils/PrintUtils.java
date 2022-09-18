package org.xxpay.mch.utils.printerUtils;

import org.xxpay.mch.utils.printerUtils.exception.ServerErrorException;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PrintUtils {

    /**
     * 将字符串转化成为十六进制数组
     * @param data 十六进制命令字符串
     * @return 命令数组
     */
    public static byte[] String2Hex(String data) {
        data = data.replaceAll("[,x \r\n]", "");
        StringBuffer s1 = new StringBuffer(data);
        int index;
        for (index = 2; index < s1.length(); index += 3) {
            s1.insert(index, ',');
        }
        String[] array = s1.toString().split(",");
        byte btCnt[] = new byte[array.length];
        int pos = 0;
        for (String string : array) {
            btCnt[pos++] = (byte)Integer.parseInt(string, 16);
        }
        return btCnt;
    }

    /**
     * 通过UUID方式生成打印任务Id
     * @return
     */
    public static String MakeBussid() {
		String strUUID = UUID.randomUUID().toString();
		strUUID = strUUID.replaceAll("[-]", "");
		return strUUID;
    }
    
    /**
     * FORM POST方式请求打印，适合发送数据打印情况
     * @param maps 请求参数
     * @param url 服务器地址
     * @param token 签名
     * @return 服务器返回值
     */
    public static String PostData(Map<String, Object> maps, String url, String token) {
        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL urls = new URL(url);
            connection = (HttpURLConnection) urls.openConnection();
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryY9iXRKu8KyGKPvny");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Range", "bytes="+"");
            if(!token.isEmpty())
                connection.setRequestProperty("accessToken", token);
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.setRequestMethod("POST");

            StringBuffer buffer = new StringBuffer();

            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                buffer.append("------WebKitFormBoundaryY9iXRKu8KyGKPvny\r\n");
                buffer.append("Content-Disposition: form-data; name=\"");
                buffer.append(entry.getKey());
                buffer.append("\"\r\n\r\n");
                buffer.append(entry.getValue());
                buffer.append("\r\n");
            }
            if(maps != null)
                buffer.append("------WebKitFormBoundaryY9iXRKu8KyGKPvny--\r\n");

            outputStream = connection.getOutputStream();
            // 发送请求参数
            outputStream.write(buffer.toString().getBytes());
            connection.connect();
            if(connection.getResponseCode() == 200) {
                // 获取URLConnection对象对应的输出流
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                throw new ServerErrorException("释放资源失败，请确认网络状态");
            }
        }
        return result;
    }

    /**
     * 执行URL方式的简单打印数据请求，普通请求
     * @param maps 请求参数
     * @param url 服务器地址
     * @param token 签名
     * @return 服务器返回值
     * @throws ServerErrorException
     */
    public static String requestPost(Map<String, Object> maps, String url, String token) throws ServerErrorException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            HttpURLConnection httpUrlConnection = (HttpURLConnection) conn;
            httpUrlConnection.setRequestMethod("POST");
            // 设置通用的请求属性
            httpUrlConnection.setRequestProperty("accept", "*/*");
            httpUrlConnection.setRequestProperty("connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            httpUrlConnection.setRequestProperty("Charset", "UTF-8");
            if(!token.isEmpty())
                httpUrlConnection.setRequestProperty("accessToken", token);
            // 发送POST请求必须设置如下两行
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setUseCaches(false);

            httpUrlConnection.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConnection.getOutputStream());

            // 设置请求属性
            String param = GetQueryString(maps);
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw new ServerErrorException("网络交互失败，请确认网络状态");
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new ServerErrorException("释放资源失败，请确认网络状态");
            }
        }
        return result;
    }

    /**
     * Map对象转化为请求字符串
     * @param data map对象参数
     * @return 请求url字符串
     */
    private static String GetQueryString(Map<String, Object> data) {
        String param = "";
        if (data != null && data.size() > 0) {
            Iterator<String> ite = data.keySet().iterator();
            while (ite.hasNext()) {
                String key = ite.next();// key
                String value = data.get(key).toString();
                param += key + "=" + value + "&";
            }
            param = param.substring(0, param.length() - 1);
        }
        return param;
    }
}

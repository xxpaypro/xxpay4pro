package org.xxpay.core.common.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 */
public class FileUtils {

	private static final MyLog _log = MyLog.getLog(FileUtils.class);

	/**
	 * @param downUrl
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static File saveFile(String downUrl, File file) throws IOException {

		// 判断父文件是否存在,不存在就创建
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				// 新建文件目录失败，抛异常
				throw new IOException("创建文件(父层文件夹)失败, filepath: " + file.getAbsolutePath());
			}
		}
		// 判断文件是否存在，不存在则创建
		if (!file.exists()) {
			if (!file.createNewFile()) {
				// 新建文件失败，抛异常
				throw new IOException("创建文件失败, filepath: " + file.getAbsolutePath());
			}
		}
		//将接口返回的对账单下载地址传入urlStr
		String urlStr = downUrl;
		//指定希望保存的文件路径

		HttpURLConnection httpUrlConnection = null;
		InputStream fis = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(urlStr);
			httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setConnectTimeout(5 * 1000);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setRequestMethod("GET");
			httpUrlConnection.setRequestProperty("Charsert", "UTF-8");
			httpUrlConnection.connect();
			fis = httpUrlConnection.getInputStream();
			byte[] temp = new byte[1024];
			int b;
			fos = new FileOutputStream(file);
			while ((b = fis.read(temp)) != -1) {
				fos.write(temp, 0, b);
				fos.flush();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis!=null) fis.close();
				if(fos!=null) fos.close();
				if(httpUrlConnection!=null) httpUrlConnection.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	/**
	 * 解压到指定目录
	 *
	 * @param zipPath
	 * @param descDir
	 * @author isea533
	 */
	public static List<String> unZipFiles(String zipPath, String descDir) throws IOException {
		return unZipFiles(new File(zipPath), descDir);
	}

	/**
	 * 解压文件到指定目录
	 *
	 * @param zipFile
	 * @param descDir
	 * @author isea533
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> unZipFiles(File zipFile, String descDir) throws IOException {
		List<String> result = new ArrayList<String>();
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		Charset charset = Charset.forName("GBK");
		ZipFile zip = new ZipFile(zipFile, charset);
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");

			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			// 输出文件路径信息
			result.add(outPath);

			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		return result;
	}

	/**
	 * 解析csv文件 到一个list中 每个单元个为一个String类型记录，每一行为一个list。 再将所有的行放到一个总list中
	 *
	 * @param file
	 *            要解析的cvs文件
	 * @param charsetName
	 *            指定的字符编号
	 * @return
	 * @throws IOException
	 */
	public static List<List<String>> readCSVFile(String file, String charsetName) throws IOException {
		if (file == null || !file.contains(".csv")) {
			return null;
		}
		InputStreamReader fr = new InputStreamReader(new FileInputStream(file), charsetName);
		return readCSVFile(fr, charsetName);
	}

	public static List<List<String>> readCSVFile(InputStreamReader fr, String charsetName) throws IOException {
		if (fr == null) {
			return null;
		}
		//InputStreamReader fr = new InputStreamReader(inputStream, charsetName);

		BufferedReader br = new BufferedReader(fr);
		String rec = null;// 一行
		String str;// 一个单元格
		List<List<String>> listFile = new ArrayList<List<String>>();
		try {
			// 读取一行
			while ((rec = br.readLine()) != null) {
				Pattern pCells = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
				Matcher mCells = pCells.matcher(rec);
				List<String> cells = new ArrayList<String>();// 每行记录一个list
				// 读取每个单元格
				while (mCells.find()) {
					str = mCells.group();
					str = str.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
					str = str.replaceAll("(?sm)(\"(\"))", "$2");
					cells.add(str);
				}
				listFile.add(cells);
			}
		} catch (Exception e) {
			_log.error(e, "异常");
		} finally {
			if (fr != null) {
				fr.close();
			}
			if (br != null) {
				br.close();
			}
		}
		return listFile;
	}

	/**
	 * 获取文件的后缀名
	 * @param fullFileName 文件名称
	 * @param appendDot 是否拼接.
	 * @return
	 */
	public static String getFileSuffix(String fullFileName, boolean appendDot){
		if(fullFileName == null || fullFileName.indexOf(".") < 0 || fullFileName.length() <= 1) return "";
		return (appendDot? "." : "") + fullFileName.substring(fullFileName.lastIndexOf(".") + 1);
	}

}

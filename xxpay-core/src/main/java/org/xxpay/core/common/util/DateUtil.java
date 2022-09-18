package org.xxpay.core.common.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 日期时间工具类
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
public class DateUtil {
	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FORMAT_YYYY_MM_DD2 = "yyyyMMdd";
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";


	public static void main(String[] args) {

		long a = 1521699893000l;
		String formatPattern_Short = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(formatPattern_Short);
		System.out.println(format.format(new Date(a)));

		System.out.println("相差天数："+diffDay(new Date(), addDay(new Date(), 2)));

	}

	public static String getCurrentDate() {
		String formatPattern_Short = "yyyyMMddhhmmss";
		SimpleDateFormat format = new SimpleDateFormat(formatPattern_Short);
		return format.format(new Date());
	}

	public static String getSeqString() {
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss"); // "yyyyMMdd G
		return fm.format(new Date());
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Date str2date(String str, String pattern) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date str2date(String str) {
		return str2date(str, FORMAT_YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 获取当前时间，格式为 yyyyMMddHHmmss
	 *
	 * @return
	 */
	public static String getCurrentTimeStr(String format) {
		format = StringUtils.isBlank(format) ? FORMAT_YYYY_MM_DD_HH_MM_SS : format;
		Date now = new Date();
		return date2Str(now, format);
	}

	public static String date2Str(Date date) {
		return date2Str(date, FORMAT_YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 时间转换成 Date 类型
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date, String format) {
		if ((format == null) || format.equals("")) {
			format = FORMAT_YYYY_MM_DD_HH_MM_SS;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date != null) {
			return sdf.format(date);
		}
		return "";
	}

	/**
	 * 获取批量付款预约时间
	 * @return
	 */
	public static String getRevTime() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		String dateString = new SimpleDateFormat(DateUtil.FORMAT_YYYYMMDDHHMMSS).format(cal.getTime());
		System.out.println(dateString);
		return dateString;
	}

	/**
	 * 时间比较
	 * @param date1
	 * @param date2
	 * @return DATE1>DATE2返回1，DATE1<DATE2返回-1,等于返回0
	 */
	public static int compareDate(String date1, String date2, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 把给定的时间减掉给定的分钟数
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date minusDateByMinute(Date date, int minute) {
		Date newDate = new Date(date.getTime() - (minute * 60 * 1000));
		return newDate;
	}

	/**
	 * 计算相差天数
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long diffDay(Date startDate, Date endDate) {
		long diffTime = endDate.getTime() - startDate.getTime();
		long day = diffTime/(1000*60*60*24);
		return day;
	}

	/**
	 * 计算 day 天后的时间
	 *
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	/**
	 * 下一个月
	 *
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date addMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}


	/**
	 * 计算当前时间是周几,周日返回 7
	 * @return
     */
	public static int getCurrentWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if(w == 0) w = 7;
		return w;
	}

	/**
	 * 验证字符是否为有效时间
	 * @param str
	 * @return
     */
	public static boolean isValidDateTime(String str) {
		if(StringUtils.isBlank(str)) return false;
		boolean convertSuccess = true;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			convertSuccess=false;
		}
		return convertSuccess;
	}

	/** 获取参数时间当天的开始时间  **/
	public static Date getBegin(Date date){
		if(date == null) return null;
		return DateUtil.str2date(DateUtil.date2Str(date, FORMAT_YYYY_MM_DD) + " 00:00:00");
	}

	/** 获取参数时间当天的结束时间 **/
	public static Date getEnd(Date date){
		if(date == null) return null;
		return DateUtil.str2date(DateUtil.date2Str(date, FORMAT_YYYY_MM_DD) + " 23:59:59");
	}

}

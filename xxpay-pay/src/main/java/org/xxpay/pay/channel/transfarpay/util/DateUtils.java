package org.xxpay.pay.channel.transfarpay.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

	/**
	 * 转换时间到string
	 * 
	 * @param date
	 * @param par
	 * @return
	 */
	public static String dateToString(Date date, String par) {
		if (null == date || null == par) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(par);

		return dateFormat.format(date);
	}

	// 时间处理
	public static String strDate(String par) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(par);

		return dateFormat.format(new Date());
	}

	/**
	 * 根据par时间格式化转化为date日期
	 * 
	 * @param str
	 * @param par
	 * @return
	 * @throws ParseException
	 */
	public static Date strToDate(String str, String par) {
		if (null == str || null == par) {
			return null;
		}
		DateFormat format = new SimpleDateFormat(par);
		try {
			return format.parse(str);
		} catch (ParseException e) {
			System.out.println("error :" + e);
			return null;
		}
	}

	public static Date parDate(String par) throws ParseException {
		DateFormat format = new SimpleDateFormat(par);
		return format.parse(format.format(new Date()));

	}

	/**
	 * 昨天的日期
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Date yestDate() throws ParseException {
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DAY_OF_WEEK, -1);
		return parseDate(rightNow.getTime(), "yyyyMMdd");
	}

	/**
	 * @Description: 获取T-1日
	 * @param @param
	 *            strDate
	 * @param @param
	 *            par
	 * @param @return
	 * @return String
	 * @throws @author
	 *             z.z.hu
	 * @date 2016年6月1日
	 */
	public static String getPreDate(String strDate, String par) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(par);
			Date date = dateFormat.parse(strDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);
			return dateToString(calendar.getTime(), par);
		} catch (Exception e) {
			e.printStackTrace();
			return strDate;
		}

	}

	/**
	 * @Description: 获取T日
	 * @param @param
	 *            strDate
	 * @param @param
	 *            par
	 * @param @return
	 * @return String
	 * @throws @author
	 *             z.z.hu
	 * @date 2016年6月1日
	 */
	public static String getCurrentDate(String par) {
		try {
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return dateToString(calendar.getTime(), par);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 昨天的日期
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String yestStrDate() {
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DAY_OF_WEEK, -1);
		return dateToString(rightNow.getTime(), "yyyyMMdd");
	}

	/**
	 * 查询这个月的第一天
	 * 
	 * @param str
	 * @param par
	 * @return
	 * @throws ParseException
	 */
	public static Date monthFirstDay() throws ParseException {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Calendar.DATE, 1);
		return parseDate(rightNow.getTime(), "yyyyMMdd");
	}

	public static Date parseDate(Date date, String par) throws ParseException {
		if (null == date || null == par) {
			return null;
		}
		DateFormat format = new SimpleDateFormat(par);
		return format.parse(format.format(date));
	}

	/**
	 * 计算两个日期间的间隔数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDaysBetween(Calendar d1, Calendar d2) {
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			d1 = (Calendar) d1.clone();
			do {

				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 获取上一个月的日期 yyyyMM
	 * 
	 * @param
	 */
	public static String prevMonth() {
		String month;
		if (DateUtils.strDate("MM").equals("01")) {
			month = String.valueOf(Integer.parseInt(DateUtils.strDate("yyyy")) - 1);
			month = month + "12";
		} else {
			month = String.valueOf(Integer.parseInt(DateUtils.strDate("yyyyMM")) - 1);
		}
		return month;
	}

	/* 2个日期相差月份 */
	public static int monthDiff(Date start, Date end) {
		int monthDiff = (end.getYear() - start.getYear()) * 12 + end.getMonth() - start.getMonth();
		return monthDiff;
	}

	public static String backDays(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -days);
		return dateToString(calendar.getTime(), "yyyyMMdd");
	}

	public static String nextDays(String date, String pars, int days, String pars2) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToDate(date, pars));
		calendar.add(Calendar.DATE, 3);
		return dateToString(calendar.getTime(), pars2);
	}

	/**
	 * 
	 * @param date1
	 *            <String>
	 * @param date2
	 *            <String>
	 * @return int
	 * @throws ParseException
	 */
	public static boolean isInSameMonth(String date1, String date2) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(sdf.parse(date1));
		c2.setTime(sdf.parse(date2));
		if ((c2.get(Calendar.YEAR) == c1.get(Calendar.YEAR)) && (c2.get(Calendar.MONDAY) == c1.get(Calendar.MONTH))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Description: 获取同一月中两个日期中的所有天数，返回list
	 * @param @param
	 *            date1
	 * @param @param
	 *            date2
	 * @param @return
	 * @param @throws
	 *            ParseException
	 * @return List<String>
	 * @throws @author
	 *             z.z.hu
	 * @date 2016年5月21日
	 */
	public static List<String> getStrPeriodDates(String date1, String date2) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(sdf.parse(date1));
		c2.setTime(sdf.parse(date2));
		List<String> result = new ArrayList<>();
		Calendar temp = c1;
		while (temp.before(c2)) {
			result.add(dateToString(temp.getTime(), "yyyyMMdd"));
			temp.add(Calendar.DAY_OF_YEAR, 1);
		}
		result.add(dateToString(c2.getTime(), "yyyyMMdd"));
		return result;
	}

	public static void main(String args[]) {
		try {
			System.out.println(monthDiff(DateUtils.strToDate("20171030", "yyyy-MM-dd"), DateUtils.strToDate("2011-12-10", "yyyy-MM-dd")));
		} catch (Exception w) {

		}

	}
}

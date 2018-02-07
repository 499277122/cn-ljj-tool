package tool;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateUtil {

	private DateUtil() {
	}

	// 大写H代表24小时制的小时，小写h代表12小时制的小时；小写的m代表分钟，大写的M代表月份！
	public static final String YY_MM_DD = "yy-MM-dd";
	public static final String HH_MM_SS = "HH:mm:ss";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String YYYY_year_MM_month_DD_day = "yyyy年MM月dd日";
	public static final String MM_DD = "MM月dd日";
	public static final String HH = "HH时";
	public static final String DD = "dd";
	public static final long ONE_DAY = 1000 * 60 * 60 * 24;

	/**
	 * 根据传入的输出格式，将时间转成字符串
	 * 
	 * @param date
	 * @param formatter
	 * @return
	 */
	public static String formatToStr(Date date, String formatter) {
		DateTime dt = new DateTime(date);
		return dt.toString(formatter);
	}

	/**
	 * 根据格式返回时间
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date formatToDate(String dateStr, String pattern) {
		DateTimeFormatter df = DateTimeFormat.forPattern(pattern);
		return DateTime.parse(dateStr, df).toDate();
	}

	/**
	 * 去除时分秒
	 * 
	 * @return
	 */
	public static Date getDateNotHMS() {
		return new DateTime().withMillisOfDay(0).toDate();
	}

	/**
	 * 计算时间间隔多少天，去掉时分秒
	 * 
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public static int calDateToDay(Date sdate, Date edate) {
		DateTime startDate = new DateTime(sdate).withMillisOfDay(0);
		DateTime endDate = new DateTime(edate).withMillisOfDay(0);
		LocalDate start = new LocalDate(startDate);
		LocalDate end = new LocalDate(endDate);
		int day = Days.daysBetween(start, end).getDays();
		return day;
	}

	/**
	 * 获取时区时间
	 * 
	 * @return
	 */
	public static Date getUTCDate() {
		return new DateTime().withZone(DateTimeZone.UTC).toLocalDateTime().toDate();
	}

	/**
	 * 求指定时间多少毫秒时间前
	 * 
	 * @param c
	 * @param millis
	 * @return
	 */
	public static Calendar getAgoCalendar(Calendar c, int millis) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date(c.getTimeInMillis()));
		c1.add(Calendar.MILLISECOND, millis);
		return c1;
	}

	/**
	 * @param time时间戳
	 * @return calendar对象
	 */
	public static Calendar getCalendarByTimeMillis(long time) {
		Date date = new Date(time);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		return c1;
	}

	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		Calendar c1 = getAgoCalendar(c, 1000);
		System.out.println(c.getTimeInMillis());
		System.out.println(c1.getTimeInMillis());
	}

	/**
	 * 获取walmart
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getWalmartFormatDate(long timestamp) {
		String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		return getFormatDate(timestamp, format);
	}

	/**
	 * 获取walmart时间戳
	 * 
	 * @param formatDate
	 * @return
	 */
	public static long getWalmartTimestamp(String formatDate) {
		String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		Date d = getDateByFormatString(formatDate, format);
		return d.getTime();
	}

	public static String getFormatDate(long timestamp, String format) {
		Timestamp ts = new Timestamp(timestamp);
		DateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(ts);
	}

	public static Date getDateByFormatString(String formatDate, String format) {
		DateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(formatDate);
			return date;
		} catch (ParseException e) {

		}
		return null;
	}

	/**
	 * 获取当前时间的字符串 格式为"yyyyMMddHHmmss"
	 * @return 返回获取的当前时间
	 */
	public static String getCurrentTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String curDate = sdf.format(new Date());

		return curDate;
	}

}

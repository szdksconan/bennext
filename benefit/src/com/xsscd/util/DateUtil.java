package com.xsscd.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期和字符串转化
 * @author zengcy
 *
 */
public class DateUtil {
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static Date formatDateTime(String dateTimeStr) {
		try {
			return dateTimeFormat.parse(dateTimeStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatDateTimeString(Date dateTime) {
		if (dateTime != null) {
			return dateTimeFormat.format(dateTime);
		} else {
			return "";
		}
	}
	public static String formatDateString(Date date) {
		if (date != null) {
			return dateFormat.format(date);
		} else {
			return "";
		}
	}
	public static Date formatDate(String dateStr) {
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
}

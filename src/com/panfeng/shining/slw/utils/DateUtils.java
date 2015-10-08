package com.panfeng.shining.slw.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class DateUtils {
	private final static String dateFormat = "yyyy-MM-dd HH:mm:ss";

	@SuppressLint("SimpleDateFormat")
	public static String getDateString() {
		Date date = new Date();
		// SimpleDateFormat多线程使用SimpleDateFormat时如果SimpleDateFormat为单例
		// 则会发生错误，
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	@SuppressLint("SimpleDateFormat")
	public static Date stringConversionTime(final String strDate) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			date=sdf.parse(strDate);
		} catch (ParseException e) {
			//e.printStackTrace();
			LogUtils.writeErrorLog("时间转换", "", e,null);
		}
		return date;
	}
}

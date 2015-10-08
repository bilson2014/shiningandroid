package com.panfeng.shining.tools;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract.PhoneLookup;

public class FVMetaData {
	static public final int CALL_NULL = -1;
	static public final int CALL_IN = 0;
	static public final int CALL_OUT = 1;
	

	/*
	 * 根据电话号码取得联系人姓名
	 */
	public static String getContactNameFromPhoneBook(Context context,
			String phoneNum) {
		String name = "";
		Cursor c = context.getContentResolver().query(
				Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, phoneNum),
				new String[] { BaseColumns._ID, PhoneLookup.NUMBER,
						PhoneLookup.DISPLAY_NAME, PhoneLookup.TYPE,
						PhoneLookup.LABEL }, null, null, null);
		if (c.getCount() == 0) {
			// 没找到电话号码
			name = phoneNum;
		} else if (c.getCount() > 0) {

			c.moveToFirst();
			
		   String othername = c.getString(2);
		   name = othername+"\r"+"\n"+phoneNum;
		}
		c.close();
		return name;
	}

}

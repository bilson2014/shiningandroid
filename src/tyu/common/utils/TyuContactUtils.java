package tyu.common.utils;

import java.util.List;

import com.panfeng.shining.data.TyuShinningData.ContactFriendItemData;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
import android.util.Log;

public class TyuContactUtils {
	// the selected cols for contact users
	static String[] selectCol = new String[] {
			ContactsContract.Contacts.DISPLAY_NAME,
			ContactsContract.Contacts.HAS_PHONE_NUMBER,
			ContactsContract.Contacts._ID };
	public static final int COL_NAME = 0;
	public static final int COL_HAS_PHONE = 1;
	public static final int COL_ID = 2;

	// the selected cols for phones of a user
	static String[] selPhoneCols = new String[] {
			ContactsContract.CommonDataKinds.Phone.NUMBER,
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Phone.TYPE };
	public static final int COL_PHONE_NUMBER = 0;
	public static final int COL_PHONE_NAME = 1;
	public static final int COL_PHONE_TYPE = 2;

	// sim卡
	private static final int NAME_COLUMN = 0;
	private static final int NUMBER_COLUMN = 1;
	private static final String SURI = "content://icc/adn";
	static public String filterPhoneNumber(String aNum){
		if(!TextUtils.isEmpty(aNum)){
			// 剔除+86
			if (aNum.startsWith("+86")) {
				aNum = aNum.substring(3);
			}
			aNum=aNum.replace("-", "");
			aNum=aNum.replace(" ", "");
		}
		return aNum;
	}
	static public void getContactList(Context context,
			List<ContactFriendItemData> list) {
		String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
				+ Contacts.HAS_PHONE_NUMBER + "=1) AND ("
				+ Contacts.DISPLAY_NAME + " != '' ))";

		Cursor cursor = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,
				selectCol,
				select,
				null,
				ContactsContract.Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC");
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				int contactId;
				contactId = cursor.getInt(cursor
						.getColumnIndex(BaseColumns._ID));
				if (cursor.getInt(COL_HAS_PHONE) > 0) {
					// the contact has numbers
					// 获得联系人的电话号码列表
					String displayName = cursor.getString(COL_NAME);
					Cursor phoneCursor = context.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							selPhoneCols,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ "=" + contactId, null, null);
					if (phoneCursor!=null&&phoneCursor.moveToFirst()) {
						do {
							// 遍历所有的联系人下面所有的电话号码
							String phoneNumber = phoneCursor
									.getString(COL_PHONE_NUMBER);
							ContactFriendItemData data = new ContactFriendItemData();
							String phoneFiled = new String();
							data.name = displayName;
							data.number = phoneNumber;
							if (!TextUtils.isEmpty(data.number)) {
								// 剔除+86
								if (data.number.startsWith("+86")) {
									data.number = data.number.substring(3);
								}
								data.number=data.number.replace("-", "");
								data.number=data.number.replace(" ", "");
							}
							// 过滤特殊符号

							list.add(data);
						} while (phoneCursor.moveToNext());
						phoneCursor.close();
					}
				}
				cursor.moveToNext();
			}
			cursor.close();
		}

	}

	static public void testApi() {
		Log.v("TyuContactUtils", "start test");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// getContactList(TyuContextKeeper.getInstance());
				Log.v("TyuContactUtils", "end test");
			}
		}).start();
	}

}

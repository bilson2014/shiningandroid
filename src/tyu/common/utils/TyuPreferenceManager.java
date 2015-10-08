package tyu.common.utils;

import java.util.Date;
import java.util.UUID;

import tyu.common.utils.TyuContextKeeper;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

public class TyuPreferenceManager {

	static public SharedPreferences getSharedPreferences() {
		Context context = TyuContextKeeper.getInstance();
		SharedPreferences MyPreferences = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_WORLD_READABLE
						| Context.MODE_WORLD_WRITEABLE);
		return MyPreferences;
	}

	// 得到一个随即时间已更新
	public static long getPlanUpdateTime(final Context context) {
		return getSharedPreferences().getLong("plan_update_time", 0);
	}

	// 设置随机时间
	public static void setPlanUpdateTime(final Context context, long status) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("plan_update_time", status);
		editor.commit();
	}

	public static String getLastUpdateDate(final Context context) {
		return getSharedPreferences().getString("last_update_date", null);
	}

	public static void setLastUpdateDate(final Context context,
			final String date) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("last_update_date", date);
		editor.commit();
	}

	public static long getLastShowUpdateDate(final Context context) {
		return getSharedPreferences().getLong("last_show_update_date", 0);
	}

	public static void setLastShowUpdateDate(final Context context,
			final long date) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("last_show_update_date", date);
		editor.commit();
	}

	static long DAY = 1000 * 60 * 60 * 24;
	static long MINUTE = 1000 * 60;

	public static boolean shouldShowUpdate(Context context) {
		long last = getLastShowUpdateDate(context);
		long now = System.currentTimeMillis();
		if (last <= 0) {
			return true;
		}
		if (now / DAY != last / DAY) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Title: isUpdateFlag
	 * @Description: 是否有更新
	 * @author wuxu
	 * @date 2012-5-11
	 */
	public static boolean isUpdateFlag(Context context) {
		// TODO Auto-generated method stub
		try {
			boolean value = getSharedPreferences().getBoolean("update_flag",
					false);
			return value;
		} catch (Exception e) {
			return true;
		}

	}

	public static void setUpdateFlag(final Context context, final Boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("update_flag", flag);
		editor.commit();
	}

	static public String getUserName() {

		SharedPreferences pref = getSharedPreferences();
		String res = pref.getString("user_name", "");
		return res;

	}

	static public boolean isChecked(String aKey) {
		boolean res = false;
		SharedPreferences pref = getSharedPreferences();
		if ("gprs_auto_update".equals(aKey)||"wifi_auto_play".equals(aKey)) {
			res = pref.getBoolean(aKey, false);
		} else
			res = pref.getBoolean(aKey, true);

		return res;
	}

	static public void setChecked(String aKey, boolean aState) {
		SharedPreferences pref = getSharedPreferences();
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(aKey, aState);
		editor.commit();

	}

	static public boolean isFirstOpen(String aKey) {
		boolean res = false;
		SharedPreferences pref = getSharedPreferences();
		res = pref.getBoolean("first_open_" + aKey, true);
		if (res) {
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("first_open_" + aKey, false);
			editor.commit();
		}
		return res;
	}

	static public void setOpenTime(long aTs) {
		SharedPreferences pref = getSharedPreferences();
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong("open_time", aTs);
		editor.commit();

	}

	static public long getOpenTime() {
		SharedPreferences pref = getSharedPreferences();
		long res = pref.getLong("open_time", -1);
		return res;
	}

	static public String getUUID() {
		SharedPreferences pref = getSharedPreferences();

		if (!pref.contains("app_uuid")) {
			String tmp = UUID.randomUUID().toString();
			setUUID(tmp);
		}

		String res = pref.getString("app_uuid", "");
		return res;
	}

	static public void setUUID(String aUUID) {
		SharedPreferences pref = getSharedPreferences();
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("app_uuid", aUUID);
		editor.commit();
	}

	static public boolean isAnotherDay(long last) {
		long now = System.currentTimeMillis();
		if (last <= 0) {
			setOpenTime(now);
			return false;
		}
		Date last_d = new Date(last);
		Date now_d = new Date(now);
		// 过了一天
		if ((now_d.getDay() != last_d.getDay())) {
			// if(now_d.getMinutes()-last_d.getMinutes()>=1){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Title: setWindosShowPostion
	 * @Description: 设置桌面悬浮的位置（相对屏幕左上角）
	 * @param @param context
	 * @param @param x
	 * @param @param y
	 * @return void
	 * @author anony_god
	 * @date 2013-2-19 下午04:57:29
	 * @version V1.0
	 */

	public static void setWindosShowPostion(final int x, final int y) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("windows_show_ptx", x);
		editor.putInt("windows_show_pty", y);
		editor.commit();
	}

	/**
	 * @Title: getWindosShowPostionX
	 * @Description: 得到桌面悬浮的位置坐标
	 * @param @param context
	 * @param @return
	 * @return int
	 * @author anony_god
	 * @date 2013-2-19 下午04:57:54
	 * @version V1.0
	 */

	public static int getWindosShowPostionX() {
		Context context = TyuContextKeeper.getInstance();
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int x = displayMetrics.widthPixels;

		return getSharedPreferences().getInt("windows_show_ptx", x);
	}

	/**
	 * @Title: geWindowsShowPostionY
	 * @Description:
	 * @param @param context
	 * @param @return
	 * @return int
	 * @author anony_god
	 * @date 2013-2-19 下午04:58:22
	 * @version V1.0
	 */

	public static int geWindowsShowPostionY() {

		return getSharedPreferences().getInt("windows_show_pty", 0);
	}

	public static long getLastShareTime() {
		return getSharedPreferences().getLong("last_share_time", 0);
	}

	public static void setLastShareTime() {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("last_share_time", System.currentTimeMillis());
		editor.commit();
	}

	public static boolean isMessageSended() {
		return getSharedPreferences().getBoolean("message_sended", false);
	}

	public static void setMessageSended(boolean allCount) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("message_sended", allCount);
		editor.commit();
	}

	public static String getLastCheckMessageTime() {
		return getSharedPreferences().getString("last_check_message_time", "0");
	}

	public static void setLastCheckMessageTime(String authors) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("last_check_message_time", authors);
		editor.commit();
	}

	/**
	 * 通知消息是否可以在任意地方展示
	 * 
	 * @return
	 */
	public static boolean isNotificationShowAll() {
		return getSharedPreferences().getBoolean("is_notification_show_all",
				false);
	}

	public static void setNotificationShowAll(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("is_notification_show_all", flag);
		editor.commit();
	}

	/**
	 * 为展示的消息信息
	 * 
	 * @return
	 */
	public static String getLastNotificationInfo() {
		return getSharedPreferences().getString("last_notification_info", "");
	}

	public static void setLastNotificationInfo(String authors) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("last_notification_info", authors);
		editor.commit();
	}

	public static boolean isVoiceDirayDownLoad() {
		return getSharedPreferences().getBoolean("is_voice_diary_download",
				false);
	}

	public static void setVoiceDirayDownLoad(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("is_voice_diary_download", flag);
		editor.commit();
	}

	public static boolean isLoveFateDownLoad() {
		return getSharedPreferences().getBoolean("love_fate_download", false);
	}

	public static void setLoveFateDownLoad(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("love_fate_download", flag);
		editor.commit();
	}

	public static boolean isWSNDownLoad() {
		return getSharedPreferences().getBoolean("love_wsn_download", false);
	}

	public static void setWSNDownLoad(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("love_wsn_download", flag);
		editor.commit();
	}

	public static boolean isCaptureGirlDownLoad() {
		return getSharedPreferences().getBoolean("love_capture_girl_download",
				false);
	}

	public static void setCaptureGirlDownLoad(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("love_capture_girl_download", flag);
		editor.commit();
	}

	public static boolean isHaveStall() {
		return getSharedPreferences().getBoolean("have_stall", false);
	}

	public static void setHaveStall(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("have_stall", flag);
		editor.commit();
	}

	public static void setFirstAuctionFlag(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("first_auction_flag", flag);
		editor.commit();
	}

	public static boolean isFirstAuctionFlag() {
		return getSharedPreferences().getBoolean("first_auction_flag", false);
	}

	public static boolean isFirstExchangeFlag() {
		return getSharedPreferences().getBoolean("first_exchange_flag", false);
	}

	public static void setFirstExchangeFlag(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("first_exchange_flag", flag);
		editor.commit();
	}

	public static boolean isFirstShowInfoFlag() {
		return getSharedPreferences().getBoolean("first_show_info_flag", false);
	}

	public static void setFirstShowInfoFlag(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("first_show_info_flag", flag);
		editor.commit();
	}

	public static int getAuctionBuyScore() {
		if (isAnotherDay(getOpenTime())) {
			setAuctionBuyScore(0);
		}
		return getSharedPreferences().getInt("auction_buy_score", 0);
	}

	public static void setAuctionBuyScore(int score) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("auction_buy_score", score);
		editor.commit();
	}

	/*
	 * 以下是闪屏需要的参数方法
	 */

	/**
	 * 得到上次检查闪屏的时间
	 * 
	 * @param context
	 * @return
	 */
	public static String getLastCheckSplashDate() {
		return getSharedPreferences().getString("lastchecksplash_date", "");
	}

	/**
	 * 设置上次检查闪屏的时间
	 * 
	 * @param context
	 * @param date
	 */
	public static void setLastCheckSplashDate(final String date) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("lastchecksplash_date", date);
		editor.commit();
	}

	/**
	 * 设置闪屏的有效时间
	 * 
	 * @param context
	 * @param aComplex
	 */
	public static void setValidSplashTime(String aComplex) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("valid_splash_time", aComplex);
		editor.commit();
	}

	/**
	 * 得到闪屏的有效时间
	 * 
	 * @param context
	 * @return
	 */
	public static String getValidSplashTime() {
		return getSharedPreferences().getString("valid_splash_time", null);
	}

	public static void setSplashStartTime(String aComplex) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("splash_start_time", aComplex);
		editor.commit();
	}

	/**
	 * 得到闪屏的开始时间
	 * 
	 * @param context
	 * @return
	 */
	public static String getSplashStartTime() {
		return getSharedPreferences().getString("splash_start_time", null);
	}

	public static void setSplashEndTime(String aComplex) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("splash_end_time", aComplex);
		editor.commit();
	}

	/**
	 * 得到闪屏的开始时间
	 * 
	 * @param context
	 * @return
	 */
	public static String getSplashEndTime() {
		return getSharedPreferences().getString("splash_end_time", null);
	}

	public static void setSplashFileName(String aComplex) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("splash_file_name", aComplex);
		editor.commit();
	}

	/**
	 * 得到闪屏的开始时间
	 * 
	 * @param context
	 * @return
	 */
	public static String getSplashFileName() {
		return getSharedPreferences().getString("splash_file_name", null);
	}

	public static void setSplashLastCheckTime() {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("splash_last_check_time", System.currentTimeMillis());
		editor.commit();
	}

	/**
	 * 得到上次检查时间
	 * 
	 * @param context
	 * @return
	 */
	public static long getSplashLastCheckTime() {
		return getSharedPreferences().getLong("splash_last_check_time", 0);
	}

	static public void setOpenSplitTime(long aTs) {
		SharedPreferences pref = getSharedPreferences();
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong("open_split_time", aTs);
		editor.commit();

	}

	static public long getOpenSplitTime() {
		SharedPreferences pref = getSharedPreferences();
		long res = pref.getLong("open_split_time", -1);
		return res;
	}

	public static int getLastSplitLevel() {
		if (isAnotherDay(getOpenSplitTime())) {
			setLastSpliteLevel(0);
		}
		return getSharedPreferences().getInt("last_split_level", 0);
	}

	public static void setLastSpliteLevel(int level) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("last_split_level", level);
		editor.commit();
	}

	public static int getTodayAllSplitLevel() {
		if (isAnotherDay(getOpenSplitTime())) {
			setLastSpliteLevel(0);
		}
		return getSharedPreferences().getInt("today_all_split_level", 0);
	}

	public static void setTodayAllSplitLevel(int level) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("today_all_split_level", level);
		editor.commit();
	}

	public static int getAllSplitLevel() {
		return getSharedPreferences().getInt("all_split_level", 0);
	}

	public static void setAllSpliteLevel(int level) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("all_split_level", level);
		editor.commit();
	}

	public static boolean getBeginSplit() {
		if (isAnotherDay(getOpenSplitTime())) {
			setBeginSplit(false);
		}
		return getSharedPreferences().getBoolean("begin_split", false);
	}

	public static void setBeginSplit(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("begin_split", flag);
		editor.commit();
	}

	public static boolean isFirstOpenLevelOne() {
		return getSharedPreferences().getBoolean("first_open_level_one", false);
	}

	public static void setFirstOpenLevelOne(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("first_open_level_one", flag);
		editor.commit();
	}

	public static int getMyShopId() {
		return getSharedPreferences().getInt("my_shop_id", -1);
	}

	public static void setMyShopId(int flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("my_shop_id", flag);
		editor.commit();
	}

	public static boolean getShowMetroNote() {
		return getSharedPreferences().getBoolean("show_metro_note", false);
	}

	public static void setShowMetroNote(boolean flag) {
		SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("show_metro_note", flag);
		editor.commit();
	}
}

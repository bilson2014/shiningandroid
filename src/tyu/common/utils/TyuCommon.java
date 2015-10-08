package tyu.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

public class TyuCommon {
	static String mSrc = null;
	static String imei = null;
	private static SimpleDateFormat day_format;
	static public final String null_imei = "null_imei";
	static public final String null_android_id = "null_android_id";
	static public long ts = 0;
	static public void showToast(final Context context, final String aContent) {
		TyuContextKeeper.getHandler().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, aContent, Toast.LENGTH_LONG).show();
			}
		});

	}
	@SuppressLint("SimpleDateFormat") static public boolean isAnotherDay(){
		Date now  =new Date();
		Date old = new Date(ts);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(! sdf.format(now).equals(sdf.format(old))){
			ts = now.getTime();
			return true;
		}else{
			return false;
		}
	}
	static public int getNetState() {
		Context context = TyuContextKeeper.getInstance();
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();

		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		// 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
		if (mobile == State.CONNECTED || mobile == State.CONNECTING){
			return ConnectivityManager.TYPE_MOBILE;
		}
			
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return ConnectivityManager.TYPE_WIFI;
		
		return -1;

	}

	static public String getImeiShort(String aImei) {
		if (aImei == null)
			return "";
		if (aImei.length() < 4)
			return "*" + aImei;
		return "*" + aImei.substring(aImei.length() - 4);
	}

	static public String getAndroidID() {
		Context context = TyuContextKeeper.getInstance();
		try {
			String res = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
			if (TextUtils.isEmpty(res)) {
				res = null_android_id;
			}
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null_android_id;
	}

	static public String getTyuUid() {
		if (!null_imei.equals(getImei()))
			return getImei();
		return TyuPreferenceManager.getUUID();
	}

	static public String getImei() {
		if (imei == null) {
			try {
				Context context = TyuContextKeeper.getInstance();
				TelephonyManager mTelephonyMgr = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				imei = mTelephonyMgr.getDeviceId();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (TextUtils.isEmpty(imei)) {
					imei = null_imei;
				}
			}

		}
		return imei;

	}

	static public String getDateString(long aTs) {
		if (day_format == null)
			day_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return day_format.format(aTs);
	}

	static public String getDateStringCN(long aTs) {
		if (day_format == null)
			day_format = new SimpleDateFormat("yyyy年MM月dd日  HH点mm分");
		return day_format.format(aTs);
	}

	static public String getFitableTime(long aTs) {
		// new SimpleDateFormat("yyyy年MM月dd日  hh:mm:ss")
		// SimpleDateFormat df1 = new SimpleDateFormat("ss秒");
		// SimpleDateFormat df2 = new SimpleDateFormat("mm分");
		// SimpleDateFormat df3 = new SimpleDateFormat("hh小时");
		final int second = 1000;
		final int minite = 1000 * 60;
		final int hour = 1000 * 60 * 60;

		long val = aTs;
		if (val >= second && val < minite) {
			return val / second + "秒";
		}
		if (val >= minite && val < hour) {
			return val / minite + "分钟";
		}
		if (val >= hour) {
			return val / hour + "小时";
		}
		return "<1s";
	}

	static public String getFitableSize(long aSize) {
		final int base = 1024;
		float val = aSize;
		DecimalFormat df = new DecimalFormat("###.0");
		if (val >= base && val < base * base) {
			return df.format(val / base) + "KB";
		}
		if (aSize > base * base) {
			return df.format(val / (base * base)) + "MB";
		}
		return df.format(val) + "B";
	}

	static public void showSimpleDialog(Context context, String title,
			String content, DialogInterface.OnClickListener aCallBack) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(content)
				.setPositiveButton("确定", aCallBack).show();
	}

	static public void showTwoBtnDialog(Context context, String title,
			String content, DialogInterface.OnClickListener aCallBack1,
			DialogInterface.OnClickListener aCallBack2) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(content)
				.setPositiveButton("拍照", aCallBack1)
				.setNegativeButton("图片库", aCallBack2).show();
	}

	private static int mVerCode;

	/**
	 * @methods name: getVersionCode
	 * @Descripition : 程序当前版本号
	 * @param context
	 * @return ：
	 * @date ：2012-7-23 下午04:50:16
	 * @author ：wuxu
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 */
	public static int getVersionCode(final Context context) {
		if (mVerCode == 0) {
			PackageManager pm = context.getPackageManager();
			PackageInfo pkInfo = null;
			try {
				pkInfo = pm.getPackageInfo(context.getPackageName(),
						PackageManager.GET_UNINSTALLED_PACKAGES);
			} catch (NameNotFoundException e) {
			}
			if (pkInfo == null) {
				return 0;
			}

			mVerCode = pkInfo.versionCode;
		}
		return mVerCode;
	}

	private static String mVerName = null;

	public static String getVersionName(final Context context) {
		if (mVerName == null) {
			PackageManager pm = context.getPackageManager();
			PackageInfo pkInfo = null;
			try {
				pkInfo = pm.getPackageInfo(context.getPackageName(),
						PackageManager.GET_UNINSTALLED_PACKAGES);
			} catch (NameNotFoundException e) {
			}
			if (pkInfo == null) {
				return null;
			}

			mVerName = pkInfo.versionName;
		}
		return mVerName;
	}
	public static boolean isMIUI(Context context){
		String model = TyuCommon.getPhoneModel();
		if(model!=null&&model.contains("MI")){
			return true;
		}
		return false;
	}
	public static final String getSrc(Context context) {
		if (mSrc == null) {
			AssetManager assetManager = context.getAssets();
			String[] files = null;
			try {
				files = assetManager.list("");
			} catch (IOException e) {

			}

			if (files != null) {
				for (String file : files) {
					if (file.contains("config.txt")) {
						InputStream inputStream = null;
						try {
							inputStream = assetManager.open(file);

							BufferedReader d = new BufferedReader(
									new InputStreamReader(inputStream));

							mSrc = d.readLine().trim();

							d.close();
							inputStream.close();
						} catch (IOException e) {

						}
						break;
					}
				}
			}
		}
		// Log.i("haha", "mSrc:" + mSrc);
		return mSrc;
	}

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	public static String getPhoneVesion() {
		return android.os.Build.VERSION.SDK;
	}
}

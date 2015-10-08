package com.panfeng.shining.receiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.panfeng.shining.Impl.showActivityImpl;
import com.panfeng.shining.Impl.showViewImpl;
import com.panfeng.shining.Impl.voiceImpl;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.entity.PhoneInfoEntity;
import com.panfeng.shining.interfaces.topWindowInterface;
import com.panfeng.shining.interfaces.volumnInterface;
import com.panfeng.shining.tools.FVMetaData;
import com.panfeng.shining.tools.MtaTools;

/**
 * 来去电弹窗
 * 
 * @author slt_slt
 * 
 */
public class FVPhoneStateReceiver extends BroadcastReceiver {
	private static final String TAG = "FVPhoneStateReceiver";
	private Context context;
	private Integer isrInteger;
	SharedPreferences preference;
	RoundedBitmapDisplayer d = null;
	int call_inout = FVMetaData.CALL_NULL;
	String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	String newPath = pathString + "/" + "com.panfeng.shinning" + "/"
			+ "myShinVideo/myShin.mp4";

	String videoBase = pathString + File.separator + "com.panfeng.shinning"
			+ File.separator + "baseVideo" + File.separator + "base.mp4";

	public static PhoneInfoEntity pe = new PhoneInfoEntity();

	static public final String ACTION_CHANGED_SHINNING = "tyu.action.changed.shinning";

	public static String name;
	private int time = 0;
	private String[] timeString = { "1-10", "10-20", "20-30", "30-40", "40-50",
			"50-60", ">60" };
	
	boolean isCall =false;
	// 声音

	volumnInterface vi = new voiceImpl();
	topWindowInterface twi;
	String videoPath;
	long timeStart;
	long timeEnd;

	public FVPhoneStateReceiver(Context aContext) {
		context = aContext;
		vi.initAudio(context);
		if (getSystemProperty("ro.miui.ui.version.name") == null
				|| getSystemProperty("ro.miui.ui.version.name").equals("")) {
			twi = new showViewImpl();
	//		twi = new showActivityImpl();
		} else {
			twi = new showActivityImpl();
		}

	}

	public void start() {
		IntentFilter ift = new IntentFilter();
		ift.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		ift.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		ift.addAction(Intent.ACTION_SCREEN_ON);
		ift.addAction(Intent.ACTION_SCREEN_OFF);
		ift.addAction(ACTION_CHANGED_SHINNING);
		try {
			context.registerReceiver(this, ift);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			context.unregisterReceiver(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void handleCallIn(Context context, Intent intent) {

		try {

			String phoneNumber = intent
					.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			TelephonyManager telephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			int state = telephony.getCallState();

			name = FVMetaData.getContactNameFromPhoneBook(context, phoneNumber);

			switch (state) {

			case TelephonyManager.CALL_STATE_RINGING:

				// MtaTools.answer(context);

				// MtaTools.answerBegin(context);
				timeStart = System.currentTimeMillis();
				Log.i(TAG, "[Broadcast]等待接电话=" + phoneNumber);

				MtaTools.answer(context);
				pe.setCallPhone(phoneNumber);

				// 如果目前正在通话，那么返回
				if (call_inout == FVMetaData.CALL_NULL) {

					call_inout = FVMetaData.CALL_IN;

//					SharedPreferences preference = context
//							.getSharedPreferences("configs",
//									Context.MODE_PRIVATE);
//					isrInteger = preference.getInt("isSet", 0);
					
				    DBAdapter	db = new DBAdapter(context);
					db.open();
					
					Cursor c=db.getMyVideo();
					
					if(c.moveToFirst()&&c.getInt(1)==1){
						

						videoPath = newPath;
						
					}
					
					else{
						
						videoPath = videoBase;
						
					}
					
					db.close();
					

//					if (isrInteger == 0) {
//
//						videoPath = videoBase;
//					} else {
//
//						videoPath = newPath;
//					}
					// ftw.hide(call_inout);

					// 记录弹窗之前系统音量和媒体音量
					// FVPhoneStateReceiver.initVolumn();
					// FVPhoneStateReceiver.adjustVolumn();
					// TODO:Activity

					Log.i(TAG, "showwin");
					
					vi.initVolumn();
					vi.adjustVolumn();
					
					Thread.sleep(100);
					twi.showWindow(context, name, call_inout, videoPath);
					

				}
				break;

			case TelephonyManager.CALL_STATE_IDLE:
				Log.i(TAG, "[Broadcast]电话挂断=" + phoneNumber);

				if (call_inout == FVMetaData.CALL_IN) {
					
					Log.i(TAG, "[Broadcast]电话挂断=统计");

					twi.closeWindow(call_inout);
					
					if(!isCall){
						isCall=false;
						
					timeEnd = System.currentTimeMillis();

					long time = timeEnd - timeStart;
					int MtaTime = (int) (time / 10000);

					
					if (MtaTime <= 6)
						MtaTools.callIn(context, timeString[MtaTime] + "");
					else
						MtaTools.callIn(context, timeString[6] + "");
					}
					vi.resumeVoice();

				}


				// TODO:Activity
				Log.i(TAG, "[Broadcast]电话挂断=没统计" );

				call_inout = FVMetaData.CALL_NULL;

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.i(TAG, "[Broadcast]通话中=" + phoneNumber);
				
				
				Log.i(TAG, "[Broadcast]通话中="+call_inout);
				if(call_inout != FVMetaData.CALL_IN){
					
					call_inout = FVMetaData.CALL_OUT;
					
				}
				
				else{
				
				isCall = true;
				call_inout = FVMetaData.CALL_IN;
				timeEnd = System.currentTimeMillis();
				long time = timeEnd - timeStart;
				int MtaTime = (int) (time / 10000);
				Log.i("slwslt", MtaTime + "");
				if (MtaTime <= 6)
				    MtaTools.callIn(context, timeString[MtaTime] + "");
				else
					MtaTools.callIn(context, timeString[6] + "");
				vi.resumeVoice();
				}
				break;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
//			if(call_inout == FVMetaData.CALL_IN){
//			vi.resumeVoice();
//			long time = timeEnd - timeStart;
//			int MtaTime = (int) (time / 10000);
//			if (MtaTime <= 6)
//				MtaTools.callIn(context, timeString[MtaTime] + "");
//			else
//				MtaTools.callIn(context, timeString[6] + "");
//			}

		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// lutao

		// TODO Auto-generated method stub
		Log.i(TAG, "[action" + intent.getAction());
		// 如果是去电
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			Log.d(TAG, "ACTION_SCREEN_ON");
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			Log.d(TAG, "ACTION_SCREEN_OFF");

		} else if (intent.getAction().equals(ACTION_CHANGED_SHINNING)) {
			Log.d(TAG, "ACTION_CHANGED_SHINNING");
			call_inout = FVMetaData.CALL_NULL;

		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

		} else {
			handleCallIn(context, intent);
		}
	}

	static public class CallOutStateWatcher extends Thread {
		public boolean keepGoing = false;

		public CallOutStateWatcher() {
			keepGoing = true;
		}

		@Override
		public void run() {
			Process process;
			InputStream inputstream;
			BufferedReader bufferedreader;
			try {
				Runtime.getRuntime().exec("logcat -c").waitFor();
				process = Runtime.getRuntime().exec(
						"logcat -b main CallCard:V *:S");
				process.waitFor();
				inputstream = process.getInputStream();
				InputStreamReader inputstreamreader = new InputStreamReader(
						inputstream);
				bufferedreader = new BufferedReader(inputstreamreader);
				String str = "";
				StringBuilder sb = new StringBuilder();
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("/sdcard/tyu_tmp_log.log")));
				while (keepGoing && (str = bufferedreader.readLine()) != null) {

					bw.append(str + "\n");

				}
				// Log.i("mLogcat", sb.toString());
			} catch (Exception e) {

			}
		}

	}

	public static String getSystemProperty(String propName) {
		String line;
		BufferedReader input = null;
		try {
			Process p = Runtime.getRuntime().exec("getprop " + propName);
			input = new BufferedReader(
					new InputStreamReader(p.getInputStream()), 1024);
			line = input.readLine();
			input.close();
		} catch (IOException ex) {
			Log.e("", "Unable to read sysprop " + propName, ex);
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Log.e("", "Exception while closing InputStream", e);
				}
			}
		}
		return line;
	}

}

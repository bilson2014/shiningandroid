package com.panfeng.shining.widgets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import tyu.common.utils.TyuPreferenceManager;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.shining.TyuApp;
import com.panfeng.shining.receiver.FVPhoneStateReceiver;
import com.panfeng.shining.service.CallVideoCopyService;
import com.panfeng.shining.tools.FileTools;
import com.panfeng.shining.tools.MediaPlayerTools;
import com.panfeng.shining.tools.PhoneUtil;
import com.panfeng.shining.tools.SensorVideo;
import com.panfeng.shining.tools.UserControl;
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;

public class FVTopWindow {
	View topView = null;

	String dataPath = null;
	Context context = null;
	boolean showing = false;
	int adjusted_ring;
	RelativeLayout reclose;
	boolean isUrl = false;
	private AudioManager mAudioManager;
	private WindowManager mWm;
	// private WindowManager mWm;
	private TextView info_view;
	// ImageView mMoveBtn;
	ImageView mCloseBtn;
	Button close, open;
	private LayoutParams params;
	PhoneUtil pu = new PhoneUtil();
	private int isrInteger;

	int ring_volumn;
	int music_volumn;
	SurfaceView surfaceView;
	MediaPlayer mediaPlayer;
	boolean initView = false;
	boolean initMedia = false;
	boolean first = true;

	public boolean hide = false;
	MediaPlayerTools mp = new MediaPlayerTools();

	private Vibrator mVibrator;
	private final int ROCKPOWER = 15;// 这是传感器系数

	private SensorManager mSensorManager;

	List<Float> xlist = new ArrayList<Float>();

	SensorDemo SensorDemo3;

	public FVTopWindow(Context aContext, String aDataPath, boolean aIsUrl) {
		context = aContext;

		topView = LayoutInflater.from(aContext).inflate(
				R.layout.s2nd_top_window_layout, null);
		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		surfaceView = (SurfaceView) topView.findViewById(R.id.video);
		info_view = (TextView) topView.findViewById(R.id.info);
		info_view.setVisibility(View.INVISIBLE);
		mCloseBtn = (ImageView) topView.findViewById(R.id.close);
		close = (Button) topView.findViewById(R.id.red);
		open = (Button) topView.findViewById(R.id.green);
		Log.i("lutao", "isSet?!" + isrInteger);
		mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		showing = true;

	}

	public void setInfoText(String aInfo) {
		info_view.setVisibility(View.VISIBLE);
		info_view.setText(FVPhoneStateReceiver.name);
		Log.i("dbw", aInfo);
	}

	@SuppressLint("UseValueOf")
	public void show(final int flag) {

		Properties prop = new Properties();
		prop.setProperty("call_in_out", ""
				+ UserControl.getUserInfo().getUserMbId());
		StatService.trackCustomKVEvent(context, "play_video", prop);
		mCloseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hide(flag);
			}
		});

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhoneUtil.endCall(context);
				hide(flag);
				Log.i("TOPWIN", "ok");
			}
		});

		open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				PhoneUtil.autoAnswerPhone(context);
				hide(flag);

			}
		});

		params = new WindowManager.LayoutParams();
		params.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		params.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

		if (android.os.Build.BRAND.contains("samsung")) {
			Log.e("slt", android.os.Build.BRAND);
			// 特殊
			params.type = WindowManager.LayoutParams.TYPE_TOAST;
		} else {
			params.type = WindowManager.LayoutParams.TYPE_PHONE;
		}

		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		mWm.addView(topView, params);

	}

	public void setVideoPath(String aPath) {
		dataPath = aPath;

		DisplayMetrics dm2 = context.getResources().getDisplayMetrics();

		double secWidth = dm2.widthPixels;
		double secHeight = dm2.heightPixels;

		double realHeight = FileTools
				.getRealSize(dataPath, secWidth, secHeight).first;
		double realWidth = FileTools.getRealSize(dataPath, secWidth, secHeight).second;

		Log.i("lutao", "ok" + realHeight + "\\" + realWidth);

		android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
		lp.height = (int) realHeight;
		lp.width = (int) realWidth;
		surfaceView.setLayoutParams(lp);

		// 获取传感器管理服务
		mSensorManager = (SensorManager) context
				.getSystemService(TyuApp.applicationContext.SENSOR_SERVICE);
		// 震动服务
		mVibrator = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE); // 震动需要在androidmainfest里面注册哦死老王

		Sensor sensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Log.e("xyz", sensor.getMaximumRange() + "");
		SensorDemo3 = new SensorDemo(sensor);

		try {

			SensorDemo3.register();

			Toast.makeText(context, "快来使用来电摇一摇吧！", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			// TODO: handle exception
		}

		File f = new File(aPath);
		mp.initMediaPlay(f, surfaceView);
	}

	public void hide(int flag) {
		Log.i("TOPWIN", "hide");
		if (showing) {
			hide = true;
			mp.stopMediaPlay();
			mWm.removeView(topView);
			showing = false;
			Log.i("TOPWIN", "hide");
			try {

				SensorDemo3.unregister();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (!TyuApp.lastVideo.equals("")) {

				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(context, CallVideoCopyService.class);
				context.startService(intent);

			}

		}
	}

	Lock lock = new ReentrantLock();
	float jilicishu = 30;
	float yaozuoda = 15;

	public void put(float x) {
		lock.lock();
		xlist.add(x);
		if (xlist.size() == jilicishu) {
			float zhengshu = 0;
			float fushu = 0;
			float temp;
			for (int i = 0; i < jilicishu; i++) {
				temp = xlist.get(i);
				if (temp > 0) {
					if (temp > zhengshu)
						zhengshu = temp;
				} else {
					if (temp < fushu)
						fushu = temp;
				}
			}
			if (zhengshu > yaozuoda && Math.abs(fushu) > yaozuoda) {

				try {
					Vibrator mVibrator = (Vibrator) context
							.getSystemService(Service.VIBRATOR_SERVICE);
					mVibrator.vibrate(500);
					SensorVideo sv = new SensorVideo();
					String path = sv.getVideo();
					TyuApp.lastVideo = path;
					File f = new File(path);
					mp.changeVideo(f);
					if (first) {
						android.view.ViewGroup.LayoutParams lp = surfaceView
								.getLayoutParams();
						lp.height = lp.MATCH_PARENT;
						lp.width = lp.MATCH_PARENT;
						surfaceView.setLayoutParams(lp);
						first = false;
					}

				} catch (Exception e) {
					// TODO: handle exception

				}

				xlist.clear();
			} else {
				Log.e("xyz", "差点" + +zhengshu + " " + fushu);
				xlist.clear();
			}
		}
		lock.unlock();
	}

	class SensorDemo implements SensorEventListener {
		TextView t;
		Sensor sensor;

		public SensorDemo(Sensor sensor) {

			this.sensor = sensor;
		}

		public void register() {
			mSensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		}

		public void unregister() {
			mSensorManager.unregisterListener(this);
		}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
		}

		double[] gravity = new double[1];

		public void onSensorChanged(SensorEvent event) {
			final float alpha = 0.8f;
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			put((float) (event.values[0] - gravity[0]));
		}
	}

	// private long clickTime = 0;
	//
	// // 双击重置
	// public void secondDel() {
	// if ((System.currentTimeMillis() - clickTime) > 1000) {
	// clickTime = System.currentTimeMillis();
	// left = false;
	// right = false;
	// } else {
	// if (left && right) {
	// try {
	// left = false;
	// right = false;
	// mVibrator.vibrate(500);
	// SensorVideo sv = new SensorVideo();
	// String path = sv.getVideo();
	// TyuApp.lastVideo = path;
	// File f = new File(path);
	// mp.changeVideo(f);
	// if (first) {
	// android.view.ViewGroup.LayoutParams lp = surfaceView
	// .getLayoutParams();
	// lp.height = lp.MATCH_PARENT;
	// lp.width = lp.MATCH_PARENT;
	// surfaceView.setLayoutParams(lp);
	// first = false;
	//
	// }
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// left = false;
	// right = false;
	// }
	//
	// }
	//
	// }
	// }

}

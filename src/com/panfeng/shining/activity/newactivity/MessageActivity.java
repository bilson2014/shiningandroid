package com.panfeng.shining.activity.newactivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import tyu.common.utils.TyuPreferenceManager;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.shining.TyuApp;
import com.panfeng.shining.Impl.voiceImpl;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.entity.PhoneInfoEntity;
import com.panfeng.shining.interfaces.volumnInterface;
import com.panfeng.shining.receiver.AddVideoReceive;
import com.panfeng.shining.receiver.FVPhoneStateReceiver;
import com.panfeng.shining.service.CallVideoCopyService;
import com.panfeng.shining.tools.FVMetaData;
import com.panfeng.shining.tools.FileTools;
import com.panfeng.shining.tools.MediaPlayerTools;
import com.panfeng.shining.tools.PhoneUtil;
import com.panfeng.shining.tools.SensorVideo;
import com.panfeng.shinning.R;

public class MessageActivity extends Activity implements OnClickListener {
	ImageView mCloseBtn;
	Button open, close;
	TextView textName;
	boolean unintentionalClose = true;

	MediaPlayerTools mp = new MediaPlayerTools();
	// MediaPlayer mediaPlayer = new MediaPlayer();;
	private static Activity ac;
	AddVideoReceive avr;
	String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	String newPath = pathString + "/" + "com.panfeng.shinning" + "/"
			+ "myShinVideo/myShin.mp4";
	String path = "";
	PhoneInfoEntity pe;
	SurfaceView surfaceView;
	boolean initView = false;
	boolean initMedia = false;
	boolean initStop = true;
	boolean right = false;
	boolean left = false;
	Context ctx;
	volumnInterface vi = new voiceImpl();

	boolean changeSurface = true;

	private SensorManager mSensorManager;
	private Vibrator mVibrator;

	List<Float> xlist = new ArrayList<Float>();

	SensorDemo SensorDemo3;

	public static Activity get() {
		return MessageActivity.ac;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("messageActivity", "CREATE");

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// 震动服务
		mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE); // 震动需要在androidmainfest里面注册哦亲
		mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
		Sensor sensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		SensorDemo3 = new SensorDemo(sensor);

		TyuApp.VideoShwo = true;
		// 锁屏显示，screen on时显示
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.s2nd_top_window_layout);
		MessageActivity.ac = this;
		ctx = this;
		// 改变音量
		// FVPhoneStateReceiver.adjustVolumn();
		vi.adjustVolumn();
		init();
		// SensorVideo sv= new SensorVideo();

		// Toast.makeText(ctx, sv.getVideo(), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 加速度传感器

		try {

			SensorDemo3.register();

			Toast.makeText(ctx, "快来使用来电摇一摇吧！", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("messageActivity", "PASUE");
		initStop = false;

		try {
			SensorDemo3.unregister();

		} catch (Exception e) {
			// TODO: handle exception
			messageExit();
		}
		messageExit();
	}

	private void init() {

		surfaceView = (SurfaceView) findViewById(R.id.video);
		mCloseBtn = (ImageView) findViewById(R.id.close);
		mCloseBtn.setOnClickListener(this);
		open = (Button) findViewById(R.id.green);
		open.setOnClickListener(this);
		close = (Button) findViewById(R.id.red);
		close.setOnClickListener(this);
		textName = (TextView) findViewById(R.id.info);
		String phoneNumber = FVPhoneStateReceiver.pe.getCallPhone();
		String name = FVMetaData.getContactNameFromPhoneBook(this, phoneNumber);
		textName.setText(name);
		changeSurface = true;

		DBAdapter db = new DBAdapter(ctx);
		db.open();
		Cursor c = db.getMyVideo();
		// 1=存在设置视频
		if (c.moveToFirst() && c.getInt(1) == 1) {
			path = newPath;
		} else {
			path = pathString + File.separator + "com.panfeng.shinning"
					+ File.separator + "baseVideo" + File.separator
					+ "base.mp4";
		}
		db.close();

		// TODO：转换视频
		double secWidth = this.getWindowManager().getDefaultDisplay()
				.getWidth();
		double secHeight = this.getWindowManager().getDefaultDisplay()
				.getHeight();
		double realHeight = FileTools.getRealSize(path, secWidth, secHeight).first;
		double realWidth = FileTools.getRealSize(path, secWidth, secHeight).second;

		LayoutParams lp = surfaceView.getLayoutParams();
		lp.height = (int) realHeight;
		lp.width = (int) realWidth;
		surfaceView.setLayoutParams(lp);

		// 获取传感器管理服务
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// 震动服务
		mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE); // 震动需要在androidmainfest里面注册哦死老王

		File f = new File(path);
		mp.initMediaPlay(f, surfaceView);
	}

	public void messageExit() {
		release();
		vi.resumeVoice();
		mp.stopMediaPlay();
		TyuApp.hasinitMediapalyer = false;
		TyuApp.VideoShwo = false;

		if (!initStop && !TyuApp.lastVideo.equals("")) {

			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClass(ctx, CallVideoCopyService.class);
			ctx.startService(intent);

		}

		this.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close:
			messageExit();
			TyuApp.TopWindowsShow = false;
			break;
		case R.id.red:
			try {
				PhoneUtil.endCall(MessageActivity.ac);
				messageExit();
			} catch (Exception e) {
				// FVPhoneStateReceiver.resumeVoice();
				vi.resumeVoice();
				release();
				mp.stopMediaPlay();
				messageExit();
			}
			TyuApp.TopWindowsShow = false;
			break;
		case R.id.green:
			try {
				PhoneUtil.autoAnswerPhone(ctx);
				messageExit();
			} catch (Exception e) {
				// FVPhoneStateReceiver.resumeVoice();
				vi.resumeVoice();
				release();
				mp.stopMediaPlay();
				messageExit();
			}
			TyuApp.TopWindowsShow = false;
			break;
		}
	}

	public void release() {
		MessageActivity.ac = null;
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
					Vibrator mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
					mVibrator.vibrate(500);
					SensorVideo sv = new SensorVideo();
					String path = sv.getVideo();
					TyuApp.lastVideo = path;
					File f = new File(path);
					mp.changeVideo(f);
					if (changeSurface) {
						LayoutParams lp = surfaceView.getLayoutParams();
						lp.height = lp.FILL_PARENT;
						lp.width = lp.FILL_PARENT;
						surfaceView.setLayoutParams(lp);
						changeSurface = false;
					}
				} catch (Exception e) {
					// TODO: handle exception
					right = false;
					left = false;
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

	// @Override
	// public void onSensorChanged(SensorEvent event) {
	// int sensorType = event.sensor.getType();
	// // values[0]:X轴，values[1]：Y轴，values[2]：Z轴
	// float[] values = event.values;
	//
	// if (initStop) {
	//
	// if (values[0] > 15) {
	//
	// left = true;
	// secondDel();
	// }
	//
	// if (values[0] < -15) {
	//
	// right = true;
	// secondDel();
	// }
	//
	// }
	//
	// }

	// // 双击重置
	// private long clickTime = 0;
	//
	// public void secondDel() {
	//
	// if ((System.currentTimeMillis() - clickTime) > 1000) {
	// clickTime = System.currentTimeMillis();
	// left = false;
	// right = false;
	// } else {
	// if (left && right) {
	// right = false;
	// left = false;
	// mVibrator.vibrate(500);// 设置震动。
	// try {
	// SensorVideo sv = new SensorVideo();
	// String path = sv.getVideo();
	// TyuApp.lastVideo = path;
	// Log.i("slt", "message=" + TyuApp.lastVideo);
	// File f = new File(path);
	// mp.changeVideo(f);
	// if (changeSurface) {
	// LayoutParams lp = surfaceView.getLayoutParams();
	// lp.height = lp.FILL_PARENT;
	// lp.width = lp.FILL_PARENT;
	// surfaceView.setLayoutParams(lp);
	// changeSurface = false;
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// right = false;
	// left = false;
	// }
	//
	// }
	// }
	// }

}
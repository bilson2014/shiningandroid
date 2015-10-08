package com.panfeng.shining.activity;

import java.io.File;
import java.util.Properties;

import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuPreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;
import com.panfeng.servicerestart.RestartUtil;
import com.panfeng.shining.activity.s2nd.TabHostActivity;
import com.panfeng.shining.entity.UserInfoEntity;
import com.panfeng.shining.tools.UserControl;
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;

public class SplashActivity extends Activity {
	static final int START_PROGRAM = 0;
	Bitmap bitmap = null;

	UserInfoEntity ex;
	Context context = this;
	

	final Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_PROGRAM:
				// 这里做登录跳转验证
		

				if (TyuPreferenceManager.isFirstOpen("guide")) {
					startActivity(new Intent(SplashActivity.this,
							GuideActivity.class));
				} else if (ex != null && UserControl.isLogin()) {

					Intent intent = new Intent();
					intent.putExtra("firstJion",1);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.setClass(SplashActivity.this, TabHostActivity.class);
					startActivity(intent);

				} else {
					
					Intent intent = new Intent();
					intent.putExtra("firstJion",1);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.setClass(SplashActivity.this, TabHostActivity.class);
					startActivity(intent);

				}

				// startActivity(new Intent(SplashActivity.this,
				// GuideActivity.class));
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				finish();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		ex = UserControl.getUserInfo();

		makeFile();

		// TODO:AUTO　login
		if (ex != null) {
			String name = UserControl.getUserInfo().getUserName();

			if (!TextUtils.isEmpty(name)) {

				Properties prop = new Properties();
				prop.setProperty("how", "autoLogin");
				prop.setProperty("tel", UserControl.getUserInfo()
						.getUserPhone());
				StatService.trackCustomBeginKVEvent(this, "login", prop);
			}
		}

		ImageView img = (ImageView) findViewById(R.id.splash_img);
		// ImageLoader.getInstance().displayImage(Scheme.DRAWABLE., imageView,
		// options)
		// getResources().
		String src = TyuCommon.getSrc(this);
		// if(!TextUtils.isEmpty(src)&&src.contains("360")){
		// bitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.splash_360);
		if (!TextUtils.isEmpty(src) && src.contains("meizu")) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.splash_all);
		} else
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.splash_all);
		img.setImageDrawable(new BitmapDrawable(bitmap));
		new Thread() {
			@Override
			public void run() {
				mhandler.sendEmptyMessageDelayed(START_PROGRAM, 500);
			};
		}.start();
		new Thread() {
			@Override
			public void run() {
				RestartUtil.startService(getpid());
			};
		}.start();

		new Thread() {
			@Override
			public void run() {

				UserControl.createUser(null);
			};
		}.start();

	}

	public String getpid() {
		int pid = android.os.Process.myPid();
		return pid + "";
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 如果本Activity是继承基类BaseActivity的，可注释掉此行。
		StatService.onResume(this);
	}

	public void makeFile() {

		String pathString = Environment.getExternalStorageDirectory()
				.getAbsolutePath();

		String imageString1 = pathString + File.separator
				+ "com.panfeng.shinning" + File.separator + "myProduction";

		String imageString2 = pathString + File.separator
				+ "com.panfeng.shinning" + File.separator + "videoImage";

		String imageString3 = pathString + File.separator
				+ "com.panfeng.shinning" + File.separator + "mySave";

		String imageString4 = pathString + File.separator
				+ "com.panfeng.shinning" + File.separator + "videoMusic";

		String imageString5 = pathString + File.separator
				+ "com.panfeng.shinning" + File.separator + "myShinVideo";

		String imageString6 = pathString + File.separator
				+ "com.panfeng.shinning" + File.separator + "baseVideo";

		String imageString7 = pathString + File.separator
				+ "com.panfeng.shinning" + File.separator + "cacheImage";

		File files = new File(imageString2);
		if (!files.exists()) {
			files.mkdirs();
		} else {
			String[] filearray = files.list();
			if (filearray.length > 0) {
				for (int i = 0; i < filearray.length; i++) {
					File fs = new File(filearray[i]);
					fs.delete();
				}
			}
		}

		File filess = new File(imageString1);
		if (!filess.exists()) {
			filess.mkdirs();
		} else {
			String[] filearray = filess.list();
			if (filearray.length > 0) {
				for (int i = 0; i < filearray.length; i++) {
					File fs = new File(filearray[i]);
					fs.delete();
				}
			}
		}

		File filesss = new File(imageString3);
		if (!filesss.exists()) {
			filesss.mkdirs();
		} else {
			String[] filearray = filesss.list();
			if (filearray.length > 0) {
				for (int i = 0; i < filearray.length; i++) {
					File fs = new File(filearray[i]);
					fs.delete();
				}
			}
		}

		File filesssv = new File(imageString4);
		if (!filesssv.exists()) {
			filesssv.mkdirs();
		} else {
			String[] filearray = filesssv.list();
			if (filearray.length > 0) {
				for (int i = 0; i < filearray.length; i++) {
					File fs = new File(filearray[i]);
					fs.delete();
				}
			}
		}

		File filesssvv = new File(imageString5);
		if (!filesssvv.exists()) {
			filesssvv.mkdirs();
		} else {
			String[] filearray = filesssvv.list();
			if (filearray.length > 0) {
				for (int i = 0; i < filearray.length; i++) {
					File fs = new File(filearray[i]);
					fs.delete();
				}
			}
		}

		File basefile = new File(imageString6);
		if (!basefile.exists()) {
			basefile.mkdirs();
		} else {
			String[] filearray = basefile.list();
			if (filearray.length > 0) {
				for (int i = 0; i < filearray.length; i++) {
					File fs = new File(filearray[i]);
					fs.delete();
				}
			}
		}

		File cachefile = new File(imageString7);
		if (!cachefile.exists()) {
			cachefile.mkdirs();
		} else {
			String[] filearray = cachefile.list();
			if (filearray.length > 0) {
				for (int i = 0; i < filearray.length; i++) {
					File fs = new File(filearray[i]);
					fs.delete();
				}
			}
		}

	}

}

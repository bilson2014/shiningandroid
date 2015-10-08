package com.panfeng.shining.activity.s2nd;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import tyu.common.utils.TyuPreferenceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.panfeng.shining.activity.newactivity.EveryDayActivityV2;
import com.panfeng.shining.activity.newactivity.LocalVideoActivity;
import com.panfeng.shining.activity.newactivity.MoreMediaListActivity;
import com.panfeng.shining.activity.newactivity.RankActivity;
import com.panfeng.shining.camera.ChooseMusic;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.receiver.AddVideoReceive;
import com.panfeng.shining.tools.CommonTools;
import com.panfeng.shinning.R;

public class TabHostActivity extends Activity implements OnClickListener {
	public static boolean isForeground = false;
	public static String apkUrl = "";
	JSONObject object;
	private TextView tv;
	AddVideoReceive are;
	Context context = this;
	Dialog dialog;
	SharedPreferences preference;
	int checkMode;
	ViewGroup dlgView;

	@Override
	protected void onStart() {

		// Intent intent = new
		// Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		//
		// startActivity(intent);
		//

		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();
		isDestroy = false;
		// //取消广播接收器
		// unregisterReceiver(are);

	}

	public void onPasue() {
		Log.i("TabHost", "pause");
		super.onPause();
	}

	Bitmap m;
	private String li2;
	private String li3;
	private JSONObject jsonObject;
	private AlertDialog dia, dias;
	private RadioGroup radioGroup;
	private RadioButton b;
	private Button diabtn;
	ImageView img;

	private int currentVersionCode;
	private boolean isDestroy = true;

	private boolean isBinded;
	private int Join;

	ImageLoader imageLoader;

	private ViewPager mPager;
	String id;

	private List<View> listViews;

	private LocalActivityManager manager = null;

	private MyPagerAdapter mpAdapter = null;
	private int index;
	String pic;
	String appVersion;
	private Thread sv;
	RelativeLayout re0, re1, re2, re3, re4;
	ImageView img0, img1, img2, img3, img4;

	private SurfaceHolder holder;
	private Paint paint;
	private ExpandableMenuOverlay menuOverlay;
	String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	String videoString = pathString + File.separator + "com.panfeng.shinning"
			+ File.separator + "videoMusic";
	String videoBase = pathString + File.separator + "com.panfeng.shinning"
			+ File.separator + "baseVideo";
	String basePath = videoBase + File.separator + "base.mp4";

	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}

	@Override
	public void onBackPressed() {
		Log.i("", "onBackPressed()");
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		Log.i("", "onPause()");
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		Log.i("", "onDestroy()");

		super.onDestroy();

	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i("luslw", "tab" + index);

		InitViewPager();

	}

	protected void onSaveInstanceState() {
		super.onSaveInstanceState(null);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.tabhost);

		// CommonTools.titleColor(context, "#fe5453");

		index = 1;

		SharedPreferences preference = context.getSharedPreferences("configs",
				Context.MODE_PRIVATE);
		int isrInteger = preference.getInt("isNoMore", 0);
		int isMi = preference.getInt("isMIUI", 0);
		int isMei = preference.getInt("isMeiZu", 0);
		int isHol = preference.getInt("isHol", 0);

		Log.i("xyz", "basePath" + basePath);
		File f = new File(basePath);

		if (!f.exists()) {

			copyResToSdcard();
		}
		DBAdapter db = new DBAdapter(TabHostActivity.this);
		db.open();
		Cursor c = db.getAllMusic();

		if (c.moveToFirst()) {

		} else {
			db.insertMusic("happywifi.m4a", "喜欢你", "邓紫棋");
			db.insertMusic("silver.m4a", "匆匆那年", "王菲");
			db.insertMusic("high.m4a", "1+1=小可爱", "MiKu");
			db.insertMusic("tou.m4a", "夜空中最美的星", "逃跑计划");
			db.insertMusic("morered.m4a", "See You Again",
					"Wiz Khalifa、Charlie Puth");

		}
		db.close();

		Intent intent = this.getIntent();
		Join = intent.getExtras().getInt("firstJion");

		CommonTools ct = new CommonTools();

		if (isrInteger != 1) {

			if (isMi == 1 || isMei == 1 || isHol == 1)
				ct.showDialog(context, 0);
		}

		mPager = (ViewPager) findViewById(R.id.vPager);

		re0 = (RelativeLayout) findViewById(R.id.re_0);
		img0 = (ImageView) findViewById(R.id.img_0);
		re0.setOnClickListener(this);

		re1 = (RelativeLayout) findViewById(R.id.re_1);
		img1 = (ImageView) findViewById(R.id.img_1);
		re1.setOnClickListener(this);

		re2 = (RelativeLayout) findViewById(R.id.re_2);
		img2 = (ImageView) findViewById(R.id.img_2);
		re2.setOnClickListener(this);

		re3 = (RelativeLayout) findViewById(R.id.re_3);
		img3 = (ImageView) findViewById(R.id.img_3);
		re3.setOnClickListener(this);

		re4 = (RelativeLayout) findViewById(R.id.re_4);
		img4 = (ImageView) findViewById(R.id.img_4);
		re4.setOnClickListener(this);

		mPager.setOnTouchListener(ol);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		InitViewPager();

		menuOverlay = (ExpandableMenuOverlay) findViewById(R.id.button_menu);

		menuOverlay
				.setOnMenuButtonClickListener(new ExpandableButtonMenu.OnMenuButtonClick() {
					@Override
					public void onClick(ExpandableButtonMenu.MenuButton action) {
						switch (action) {

						case LEFT:
							menuOverlay.getButtonMenu().toggle();
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										Thread.sleep(280);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									Intent intent = new Intent(
											TabHostActivity.this,
											ChooseMusic.class);
									startActivity(intent);
									overridePendingTransition(
											R.anim.push_left_in,
											R.anim.push_left_out);
								}
							}).start();
							break;
						case RIGHT:
							menuOverlay.getButtonMenu().toggle();
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										Thread.sleep(280);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									Intent intents = new Intent(
											TabHostActivity.this,
											LocalVideoActivity.class);
									startActivity(intents);
									overridePendingTransition(
											R.anim.push_left_in,
											R.anim.push_left_out);
								}
							}).start();
							break;
						}
					}
				});

	}

	public void copyResToSdcard() {// name为sd卡下制定的路径

		String pathString = Environment.getExternalStorageDirectory()
				.getAbsolutePath();

		String videoString = pathString + File.separator
				+ "com.panfeng.shinning" + File.separator + "videoMusic";
		String videoBase = pathString + File.separator + "com.panfeng.shinning"
				+ File.separator + "baseVideo";
		String basePath = videoBase + File.separator + "base.mp4";
		Log.i("xyz", "basePath" + basePath);
		File f = new File(basePath);

		Field[] raw = R.raw.class.getFields();
		for (Field r : raw) {
			try {
				// System.out.println("R.raw." + r.getName());
				int id = getResources().getIdentifier(r.getName(), "raw",
						getPackageName());
				if (!r.getName().equals("base")) {
					String path = videoString + "/" + r.getName() + ".m4a";
					Log.i("video", "you" + path);
					BufferedOutputStream bufEcrivain = new BufferedOutputStream(
							(new FileOutputStream(new File(path))));
					BufferedInputStream VideoReader = new BufferedInputStream(
							getResources().openRawResource(id));
					byte[] buff = new byte[20 * 1024];
					int len;
					while ((len = VideoReader.read(buff)) > 0) {
						bufEcrivain.write(buff, 0, len);
					}
					bufEcrivain.flush();
					bufEcrivain.close();
					VideoReader.close();
				}

				else {

					String path = videoBase + "/" + r.getName() + ".mp4";
					Log.i("video", "you" + path);
					BufferedOutputStream bufEcrivain = new BufferedOutputStream(
							(new FileOutputStream(new File(path))));
					BufferedInputStream VideoReader = new BufferedInputStream(
							getResources().openRawResource(id));
					byte[] buff = new byte[20 * 1024];
					int len;
					while ((len = VideoReader.read(buff)) > 0) {
						bufEcrivain.write(buff, 0, len);
					}
					bufEcrivain.flush();
					bufEcrivain.close();
					VideoReader.close();

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	OnTouchListener ol = new OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	private void InitViewPager() {

		Intent intent = null;
		listViews = new ArrayList<View>();
		mpAdapter = new MyPagerAdapter(listViews);

		intent = new Intent(TabHostActivity.this, RankActivity.class);
		listViews.add(getView("A", intent));
		intent = new Intent(TabHostActivity.this, EveryDayActivityV2.class);
		listViews.add(getView("B", intent));
		intent = new Intent(TabHostActivity.this, MoreMediaListActivity.class);
		listViews.add(getView("C", intent));
		intent = new Intent(TabHostActivity.this, S2ndMainActivity.class);
		listViews.add(getView("D", intent));

		img0.setBackgroundResource(R.drawable.videolist);

		mPager.setOffscreenPageLimit(0);
		mPager.setAdapter(mpAdapter);
		mPager.setCurrentItem(index);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			manager.dispatchResume();
			switch (arg0) {

			case 0:

				index = 0;

				img0.setBackgroundResource(R.drawable.videolist);
				img1.setBackgroundResource(R.drawable.rankbg);
				img2.setBackgroundResource(R.drawable.main_bar_btn);
				img3.setBackgroundResource(R.drawable.otherbg);
				img4.setBackgroundResource(R.drawable.shinsetbg);
				mpAdapter.notifyDataSetChanged();

				break;

			case 1:
				index = 1;
				img0.setBackgroundResource(R.drawable.videolistbg);
				img1.setBackgroundResource(R.drawable.rank);
				img2.setBackgroundResource(R.drawable.main_bar_btn);
				img3.setBackgroundResource(R.drawable.otherbg);
				img4.setBackgroundResource(R.drawable.shinsetbg);

				mpAdapter.notifyDataSetChanged();
				break;

			case 2:
				index = 2;
				img0.setBackgroundResource(R.drawable.videolistbg);
				img1.setBackgroundResource(R.drawable.rankbg);
				img2.setBackgroundResource(R.drawable.main_bar_btn);
				img3.setBackgroundResource(R.drawable.other);
				img4.setBackgroundResource(R.drawable.shinsetbg);

				mpAdapter.notifyDataSetChanged();
				break;

			case 3:
				index = 3;
				img0.setBackgroundResource(R.drawable.videolistbg);
				img1.setBackgroundResource(R.drawable.rankbg);
				img2.setBackgroundResource(R.drawable.main_bar_btn);
				img3.setBackgroundResource(R.drawable.otherbg);
				img4.setBackgroundResource(R.drawable.shinset);

				mpAdapter.notifyDataSetChanged();
				break;

			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@SuppressWarnings("deprecation")
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {

		case R.id.re_0:

			index = 0;
			listViews.set(
					0,
					getView("A", new Intent(TabHostActivity.this,
							RankActivity.class)));

			img0.setBackgroundResource(R.drawable.videolist);
			img1.setBackgroundResource(R.drawable.rankbg);
			img2.setBackgroundResource(R.drawable.main_bar_btn);
			img3.setBackgroundResource(R.drawable.otherbg);
			img4.setBackgroundResource(R.drawable.shinsetbg);

			mpAdapter.notifyDataSetChanged();
			mPager.setCurrentItem(0);
			break;

		case R.id.re_1:
			index = 1;
			listViews.set(
					1,

					getView("B", new Intent(TabHostActivity.this,
							EveryDayActivityV2.class)));

			img0.setBackgroundResource(R.drawable.videolistbg);
			img1.setBackgroundResource(R.drawable.rank);
			img2.setBackgroundResource(R.drawable.main_bar_btn);
			img3.setBackgroundResource(R.drawable.otherbg);
			img4.setBackgroundResource(R.drawable.shinsetbg);

			mpAdapter.notifyDataSetChanged();
			mPager.setCurrentItem(1);
			break;

		case R.id.re_2:
			// index = 2;
			// listViews.set(
			// 2,
			// getView("C", new Intent(TabHostActivity.this,
			// ChooseMusic.class)));
			//

			img2.setBackgroundResource(R.drawable.main_bar_btnbg);

			//
			// mpAdapter.notifyDataSetChanged();
			// mPager.setCurrentItem(2);
			// View
			// dlgv=LayoutInflater.from(context).inflate(R.layout.main_dialog,
			// null);
			// Dialog dialog = new Dialog(context,
			// R.style.activity_theme_transparent);
			// Window window = dialog.getWindow();
			//
			// WindowManager.LayoutParams lp = window.getAttributes();
			// window.setGravity(Gravity.LEFT | Gravity.TOP);
			//
			// lp.x = 100; // 新位置X坐标
			// lp.y = 100; // 新位置Y坐标
			// lp.width = 300; // 宽度
			// lp.height = 300; // 高度
			// lp.alpha = 0.7f; // 透明度
			// dialog.setContentView(dlgv);
			// window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
			// window.setWindowAnimations(R.style.mystyle); // 添加动画
			// dialog.show();
			// View dlgv = findViewById(R.id.show_more);
			// dlgv.setVisibility(View.VISIBLE);
			// LinearLayout topaly = (LinearLayout) dlgv
			// .findViewById(R.id.toplaymyvideolayout);
			//
			// LinearLayout tolocal = (LinearLayout) dlgv
			// .findViewById(R.id.tolocallayout);
			//
			//
			// RelativeLayout relocals = (RelativeLayout) dlgv
			// .findViewById(R.id.main_redialog);
			//
			//
			// ObjectAnimator revealAnimator = ObjectAnimator.ofFloat( // 缩放X
			// // 轴的
			// relocals, "scaleX", 0, (float)2.3);
			// ObjectAnimator revealAnimator1 = ObjectAnimator.ofFloat(// 缩放Y
			// // 轴的
			// relocals, "scaleY", 0, (float)2.3);
			// AnimatorSet set = new AnimatorSet();
			// set.setDuration(500);// 设置播放时间
			// set.setInterpolator(new CycleInterpolator(5));// 设置播放模式，这里是平常模式
			// set.playTogether(revealAnimator, revealAnimator1);// 设置一起播放
			// set.start();
			//
			// topaly.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			//
			// Intent intent = new Intent(TabHostActivity.this,
			// ChooseMusic.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.push_left_in,
			// R.anim.push_left_out);
			// }
			// });
			//
			// tolocal.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			//
			// Intent intent = new Intent(TabHostActivity.this,
			// LocalVideoActivity.class);
			// startActivity(intent);
			// overridePendingTransition(R.anim.push_left_in,
			// R.anim.push_left_out);
			// }
			// });

			break;

		case R.id.re_3:
			index = 2;
			listViews.set(
					2,
					getView("C", new Intent(TabHostActivity.this,
							MoreMediaListActivity.class)));

			img0.setBackgroundResource(R.drawable.videolistbg);
			img1.setBackgroundResource(R.drawable.rankbg);
			img2.setBackgroundResource(R.drawable.main_bar_btn);
			img3.setBackgroundResource(R.drawable.other);
			img4.setBackgroundResource(R.drawable.shinsetbg);

			mpAdapter.notifyDataSetChanged();
			mPager.setCurrentItem(2);
			break;

		case R.id.re_4:
			index = 3;
			listViews.set(
					3,
					getView("D", new Intent(TabHostActivity.this,
							S2ndMainActivity.class)));

			img0.setBackgroundResource(R.drawable.videolistbg);
			img1.setBackgroundResource(R.drawable.rankbg);
			img2.setBackgroundResource(R.drawable.main_bar_btn);
			img3.setBackgroundResource(R.drawable.otherbg);
			img4.setBackgroundResource(R.drawable.shinset);

			mpAdapter.notifyDataSetChanged();
			mPager.setCurrentItem(3);
			break;

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if (secondDel())
				Toast.makeText(TabHostActivity.this, "再按一次退出",
						Toast.LENGTH_SHORT).show();

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private long clickTime = 0;

	// 双击重置
	public boolean secondDel() {

		if ((System.currentTimeMillis() - clickTime) > 2000) {
			clickTime = System.currentTimeMillis();
			return true;
		} else {

			finish();
			return false;
		}
	}

}
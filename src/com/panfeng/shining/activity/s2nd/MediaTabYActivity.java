package com.panfeng.shining.activity.s2nd;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.panfeng.shining.camera.ChooseMusic;
import com.panfeng.shinning.R;

public class MediaTabYActivity extends Activity implements OnClickListener {
	public static boolean isForeground = false;
	public static String apkUrl = "";
	JSONObject object;
	private TextView tv;

	@Override
	protected void onStart() {
		super.onStart();
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
	RelativeLayout set, video, self;
	ImageView imgset, imgvideo, imgself;

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
	protected void onStop() {
		Log.i("", "onStop()");
		super.onStop();
		isDestroy = false;
	}

	@Override
	protected void onDestroy() {
		Log.i("", "onDestroy()");

		super.onDestroy();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getIntent() != null) {
			index = getIntent().getIntExtra("index", 0);
			mPager.setCurrentItem(index);
			setIntent(null);
		}


	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.mediahost);

		mPager = (ViewPager) findViewById(R.id.vPager);
		set = (RelativeLayout) findViewById(R.id.tabshinset);
		imgset = (ImageView) findViewById(R.id.imgshinset);
		set.setOnClickListener(this);
		video = (RelativeLayout) findViewById(R.id.tabvideolist);
		imgvideo = (ImageView) findViewById(R.id.imgvideolist);
		video.setOnClickListener(this);
		self = (RelativeLayout) findViewById(R.id.tabmain_bar_btn);
		imgself = (ImageView) findViewById(R.id.imgselfvideo);
		self.setOnClickListener(this);
		mPager.setOnTouchListener(ol);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		InitViewPager();

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
		intent = new Intent(MediaTabYActivity.this,
				S2ndShinningStoreActivity.class);
		listViews.add(getView("A", intent));
		intent = new Intent(MediaTabYActivity.this, ChooseMusic.class);
		listViews.add(getView("B", intent));
		intent = new Intent(MediaTabYActivity.this, S2ndMainActivity.class);
		listViews.add(getView("C", intent));

		mPager.setOffscreenPageLimit(0);
		mPager.setAdapter(mpAdapter);
		mPager.setCurrentItem(1);
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

		
				imgvideo.setBackgroundResource(R.drawable.videolist);
				imgset.setBackgroundResource(R.drawable.shinsetbg);
				imgself.setBackgroundResource(R.drawable.main_bar_btnbg);
				mpAdapter.notifyDataSetChanged();
				

				break;

			case 1:
				index = 1;
				imgvideo.setBackgroundResource(R.drawable.videolistbg);
				imgset.setBackgroundResource(R.drawable.shinsetbg);
				imgself.setBackgroundResource(R.drawable.main_bar_btn);
				
			
				mpAdapter.notifyDataSetChanged();
				break;
			case 2:
				index = 2;

				imgvideo.setBackgroundResource(R.drawable.videolistbg);
				imgset.setBackgroundResource(R.drawable.shinset);
				imgself.setBackgroundResource(R.drawable.main_bar_btnbg);
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

	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {

		case R.id.tabvideolist:
			index = 0;
			listViews.set(
					0,
					getView("A", new Intent(MediaTabYActivity.this,
							S2ndShinningStoreActivity.class)));

			imgvideo.setBackgroundResource(R.drawable.videolist);
			imgset.setBackgroundResource(R.drawable.shinsetbg);
			imgself.setBackgroundResource(R.drawable.main_bar_btnbg);

			mpAdapter.notifyDataSetChanged();
			mPager.setCurrentItem(0);
			break;

		case R.id.tabmain_bar_btn:
			index = 1;
			listViews.set(
					1,
					getView("B", new Intent(MediaTabYActivity.this,
							ChooseMusic.class)));

			imgvideo.setBackgroundResource(R.drawable.videolistbg);
			imgset.setBackgroundResource(R.drawable.shinsetbg);
			imgself.setBackgroundResource(R.drawable.main_bar_btn);
			mpAdapter.notifyDataSetChanged();
			mPager.setCurrentItem(1);
			break;

		case R.id.tabshinset:
			index = 2;
			listViews.set(
					2,
					getView("C", new Intent(MediaTabYActivity.this,
							S2ndMainActivity.class)));

			imgvideo.setBackgroundResource(R.drawable.videolistbg);
			imgset.setBackgroundResource(R.drawable.shinset);
			imgself.setBackgroundResource(R.drawable.main_bar_btnbg);

			mpAdapter.notifyDataSetChanged();
			mPager.setCurrentItem(2);
			break;

		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if(secondDel())
				Toast.makeText(MediaTabYActivity.this, "再按一次退出",Toast.LENGTH_SHORT).show();
				
			
			
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
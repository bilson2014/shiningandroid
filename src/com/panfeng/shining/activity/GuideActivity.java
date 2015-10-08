package com.panfeng.shining.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import tyu.common.utils.TyuContextKeeper;
import tyu.common.utils.TyuImageUtils;
import tyu.common.utils.TyuPreferenceManager;

import com.panfeng.camera.surfaceview.SurfaceAnimationViewTabHost;
import com.panfeng.shining.activity.s2nd.TabHostActivity;
import com.panfeng.shining.interfaces.callBack;
import com.panfeng.shining.tools.CommonTools;


import com.panfeng.shinning.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class GuideActivity extends Activity {
	private ArrayList<ImageView> viewList;
	private ViewPager viewPager;
	Bitmap[] bitmapCache = new Bitmap[3];
	int[] res_id = { R.drawable.p1, R.drawable.p2, R.drawable.g4 };
	Context ctx = this;
	Dialog dialog;
	SharedPreferences preference;
	int checkMode, checkMeiZu, checkHol;
	
	SurfaceAnimationViewTabHost sav ;
	 ImageView my_shinning_nomore;
	 ImageView my_shinning;
	 ImageView my_shinning_set;
	 mHander hander = new mHander();

	// private View btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fv_guide_layout);
		// btn = findViewById(R.id.start_btn);
		// btn.setVisibility(View.INVISIBLE);
		LayoutInflater lf = getLayoutInflater();
		ImageView view1 = (ImageView) lf.inflate(R.layout.fv_guide_item, null);
		view1.setImageResource(R.drawable.p1);
		ImageView view2 = (ImageView) lf.inflate(R.layout.fv_guide_item, null);
		view1.setImageResource(R.drawable.p2);
		ImageView view3 = (ImageView) lf.inflate(R.layout.fv_guide_item, null);
		view1.setImageResource(R.drawable.g4);
		// ImageView view3 = (ImageView) lf.inflate(R.layout.fv_guide_item,
		// null);
		//
		// ImageView view4 = (ImageView) lf.inflate(R.layout.fv_guide_item,
		// null);
		// view1.setImageResource(R.drawable.g3);
		viewList = new ArrayList<ImageView>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		// viewList.add(view4);
		OnClickListener clk = new OnClickListener() {

			@Override
			public void onClick(View v) {

				// startActivity(new Intent(GuideActivity.this,
				// S2ndMainActivity.class));
				// overridePendingTransition(R.anim.push_left_in,
				// R.anim.push_left_out);
				// finish();
				try {
					startImageViewAnim("s2nd_guide",
							viewList.get(res_id.length - 1));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		// btn.setOnClickListener(clk);
		view3.setOnClickListener(clk);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new MyViewPagerAdapter(viewList));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				// if (arg0 == 2) {
				// btn.setVisibility(View.VISIBLE);
				// } else {
				// btn.setVisibility(View.INVISIBLE);
				// }
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private boolean stop = false;
	private Bitmap animBitmapCache;

	public void startImageViewAnim(final String aAssetPath, final ImageView aImg)
			throws IOException {
		final String[] files = getAssets().list(aAssetPath);
		// List<String> arrays = Arrays.asList(files);
		// Collections.sort(arrays);
		new Thread() {

			@Override
			public void run() {
				int step = 0;
				while (!stop) {

					if (step >= files.length) {
						TyuContextKeeper.doUiTask(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub

								if (!getSystemProperty(
										"ro.miui.ui.version.name").equals("")) {

									preference = ctx.getSharedPreferences(
											"configs", Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = preference
											.edit();
									editor.putInt("isMIUI", 1);
									editor.commit();

								}

								if (isFlyme()) {
									preference = ctx.getSharedPreferences(
											"configs", Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = preference
											.edit();
									editor.putInt("isMeiZu", 1);
									editor.commit();

								}

							

								if (android.os.Build.MANUFACTURER.contains("HUAWEI")) {

									preference = ctx.getSharedPreferences(
											"configs", Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = preference
											.edit();
									editor.putInt("isHol", 1);
									editor.commit();

								}

//								SharedPreferences preference = ctx
//										.getSharedPreferences("configs",
//												Context.MODE_PRIVATE);
//
//								int isMi = preference.getInt("isMIUI", 0);
//								int isMei = preference.getInt("isMeiZu", 0);
//								int isHol = preference.getInt("isHol", 0);

//								if (isMi == 1 || isMei == 1 || isHol == 1) {
//									CommonTools ct = new CommonTools();
//									ct.showDialog(ctx,1);
//								}
//								else{
									

									Intent intent = new Intent();
									intent.putExtra("firstJion", 0);
									intent.setClass(GuideActivity.this, TabHostActivity.class);

									startActivity(intent);
									finish();

									overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
									
//								}

							}
						});

						return;
					}
					try {
						final Bitmap old = animBitmapCache;
						
						
							animBitmapCache = TyuImageUtils.loadBitmapImageFromAssets(getAvtivity(),aAssetPath + "/" + files[step]);
						
						TyuContextKeeper.doUiTask(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								aImg.setImageBitmap(animBitmapCache);
								if (old != null && !old.isRecycled()) {
									old.recycle();
									System.gc();
								}
							}
						});

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					step++;
					try {
						sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	Activity getAvtivity() {
		return this;
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<ImageView> mListViews;

		public MyViewPagerAdapter(List<ImageView> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			if (bitmapCache[position] != null
					&& !bitmapCache[position].isRecycled()) {
				bitmapCache[position].recycle();
				bitmapCache[position] = null;
			}
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			if (bitmapCache[position] != null
					&& !bitmapCache[position].isRecycled()) {
				bitmapCache[position].recycle();
				bitmapCache[position] = null;
			}
			bitmapCache[position] = BitmapFactory.decodeResource(
					getResources(), res_id[position]);
			mListViews.get(position).setImageDrawable(
					new BitmapDrawable(bitmapCache[position]));
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}

		public float getPageWidth(int position) {
			// TODO Auto-generated method stub
			return 1.0f;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		for (int i = 0; i < bitmapCache.length; i++) {
			if (bitmapCache[i] != null && !bitmapCache[i].isRecycled()) {
				bitmapCache[i].recycle();
				bitmapCache[i] = null;

			}
		}
		stop = true;
		if (animBitmapCache != null && !animBitmapCache.isRecycled()) {
			animBitmapCache.recycle();
			animBitmapCache = null;

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

//	public void showDialog() {
//
//		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		// 自定义布局
//
//		ViewGroup dlgView = (ViewGroup) mLayoutInflater.inflate(
//				R.layout.message, null);
//
//		preference = ctx.getSharedPreferences("configs", Context.MODE_PRIVATE);
//		checkMode = preference.getInt("isMIUI", 0);
//		checkMeiZu = preference.getInt("isMeiZu", 0);
//		checkHol = preference.getInt("isHol", 0);
//
//	sav = (SurfaceAnimationViewTabHost)dlgView.findViewById(R.id.info_cartoon);
//		
//		my_shinning_nomore = (ImageView) dlgView
//				.findViewById(R.id.info_nomore);
//		 my_shinning = (ImageView) dlgView
//				.findViewById(R.id.info_close);
//		
//		 my_shinning_set = (ImageView) dlgView
//				.findViewById(R.id.info_set);
//
//		List<Integer> list = new ArrayList<Integer>();
//
//		if (checkMode == 1) {
//			
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//			list.add(R.drawable.lin0001);
//
//			list.add(R.drawable.lin0011);
//			list.add(R.drawable.lin0012);
//			list.add(R.drawable.lin0013);
//			list.add(R.drawable.lin0014);
//			list.add(R.drawable.lin0015);
//			list.add(R.drawable.lin0016);
//			list.add(R.drawable.lin0017);
//			list.add(R.drawable.lin0018);
//			list.add(R.drawable.lin0019);
//			list.add(R.drawable.lin0020);
//			list.add(R.drawable.lin0021);
//			list.add(R.drawable.lin0022);
//			list.add(R.drawable.lin0023);
//			list.add(R.drawable.lin0024);
//			list.add(R.drawable.lin0025);
//
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//			list.add(R.drawable.lin0027);
//
//			list.add(R.drawable.lin0038);
//			list.add(R.drawable.lin0039);
//			list.add(R.drawable.lin0040);
//			list.add(R.drawable.lin0041);
//			list.add(R.drawable.lin0042);
//			list.add(R.drawable.lin0043);
//			list.add(R.drawable.lin0044);
//			list.add(R.drawable.lin0045);
//			list.add(R.drawable.lin0046);
//			list.add(R.drawable.lin0047);
//			list.add(R.drawable.lin0048);
//			list.add(R.drawable.lin0049);
//			list.add(R.drawable.lin0050);
//			list.add(R.drawable.lin0051);
//			list.add(R.drawable.lin0052);
//
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//			list.add(R.drawable.lin0054);
//
//			list.add(R.drawable.lin0065);
//			list.add(R.drawable.lin0066);
//			list.add(R.drawable.lin0067);
//			list.add(R.drawable.lin0068);
//			list.add(R.drawable.lin0069);
//			list.add(R.drawable.lin0070);
//			list.add(R.drawable.lin0071);
//			list.add(R.drawable.lin0072);
//			list.add(R.drawable.lin0073);
//			list.add(R.drawable.lin0074);
//
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//
//		} else if (checkMeiZu == 1) {
//
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//			list.add(R.drawable.mz_00001);
//
//			list.add(R.drawable.mz_00002);
//			list.add(R.drawable.mz_00003);
//			list.add(R.drawable.mz_00004);
//			list.add(R.drawable.mz_00005);
//			list.add(R.drawable.mz_00006);
//			list.add(R.drawable.mz_00007);
//			list.add(R.drawable.mz_00008);
//			list.add(R.drawable.mz_00009);
//			list.add(R.drawable.mz_00010);
//			list.add(R.drawable.mz_00011);
//			list.add(R.drawable.mz_00012);
//			list.add(R.drawable.mz_00013);
//			list.add(R.drawable.mz_00014);
//			list.add(R.drawable.mz_00015);
//			list.add(R.drawable.mz_00016);
//			list.add(R.drawable.mz_00017);
//			list.add(R.drawable.mz_00018);
//			list.add(R.drawable.mz_00019);
//			list.add(R.drawable.mz_00020);
//			list.add(R.drawable.mz_00021);
//			list.add(R.drawable.mz_00022);
//			list.add(R.drawable.mz_00023);
//			list.add(R.drawable.mz_00024);
//			list.add(R.drawable.mz_00025);
//			list.add(R.drawable.mz_00026);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00027);
//			list.add(R.drawable.mz_00028);
//			list.add(R.drawable.mz_00029);
//			list.add(R.drawable.mz_00030);
//			list.add(R.drawable.mz_00031);
//			list.add(R.drawable.mz_00032);
//			list.add(R.drawable.mz_00033);
//			list.add(R.drawable.mz_00034);
//			list.add(R.drawable.mz_00035);
//			list.add(R.drawable.mz_00036);
//			list.add(R.drawable.mz_00037);
//			list.add(R.drawable.mz_00038);
//			list.add(R.drawable.mz_00039);
//			list.add(R.drawable.mz_00040);
//			list.add(R.drawable.mz_00041);
//			list.add(R.drawable.mz_00042);
//			list.add(R.drawable.mz_00043);
//			list.add(R.drawable.mz_00044);
//			list.add(R.drawable.mz_00045);
//			list.add(R.drawable.mz_00046);
//			list.add(R.drawable.mz_00047);
//			list.add(R.drawable.mz_00048);
//			list.add(R.drawable.mz_00049);
//
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//
//
//		} else if (checkHol == 1) {
//
//			
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.tips);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//			list.add(R.drawable.lin_00000);
//
//			list.add(R.drawable.lin_00010);
//			list.add(R.drawable.lin_00011);
//			list.add(R.drawable.lin_00012);
//			list.add(R.drawable.lin_00013);
//			list.add(R.drawable.lin_00014);
//			list.add(R.drawable.lin_00015);
//			list.add(R.drawable.lin_00016);
//			list.add(R.drawable.lin_00017);
//			list.add(R.drawable.lin_00018);
//			list.add(R.drawable.lin_00019);
//			list.add(R.drawable.lin_00020);
//			list.add(R.drawable.lin_00021);
//			list.add(R.drawable.lin_00022);
//			list.add(R.drawable.lin_00023);
//			list.add(R.drawable.lin_00024);
//			list.add(R.drawable.lin_00025);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00026);
//			list.add(R.drawable.lin_00037);
//			list.add(R.drawable.lin_00038);
//			list.add(R.drawable.lin_00039);
//			list.add(R.drawable.lin_00040);
//			list.add(R.drawable.lin_00041);
//			list.add(R.drawable.lin_00042);
//			list.add(R.drawable.lin_00043);
//			list.add(R.drawable.lin_00044);
//			list.add(R.drawable.lin_00044);
//			list.add(R.drawable.lin_00044);
//			list.add(R.drawable.lin_00044);
//			list.add(R.drawable.lin_00044);
//			list.add(R.drawable.lin_00044);
//			list.add(R.drawable.lin_00044);
//			list.add(R.drawable.lin_00044);
//			list.add(R.drawable.lin_00044);
//			list.add(R.drawable.lin_00044);
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//			list.add(R.drawable.finish);
//
//
//		}
//
//		sav.setData(list);
//
//		dlgView.startLayoutAnimation();
//
//
//		dialog = new Dialog(GuideActivity.this,
//				R.style.activity_theme_transparent);
//
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.setContentView(dlgView);
//		dialog.show();
//		
//		
//	
//		
//		callBack cb=new callBack() {
//			
//			@Override
//			public void method() {
//				
//				hander.sendEmptyMessage(1);
//		
//			}
//		};
//		sav.setCallBack(cb);
//
//		// imageView1
//		my_shinning.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated
//				// method
//				// stub
//				dialog.dismiss();
//
//				Intent intent = new Intent();
//				intent.putExtra("firstJion", 0);
//				intent.setClass(GuideActivity.this, TabHostActivity.class);
//
//				startActivity(intent);
//				finish();
//
//				overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
//
//			}
//		});
//
//		my_shinning_set.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				if (checkMode == 1) {
//					try {
//						ComponentName component = new ComponentName(
//								// 08-14 14:47:05.987: I/ActivityManager(846):
//								// START u0 {act=android.intent.action.MAIN
//								// cat=[android.intent.category.LAUNCHER]
//								// flg=0x10200000
//								// cmp=com.miui.securitycenter/.MainActivity
//								// bnds=[825,1272][1017,1464]} from pid 2328
//
//								"com.miui.securitycenter",
//								"com.miui.permcenter.autostart.AutoStartManagementActivity"
//						// "com.miui.securitycenter.MainActivity"
//						);
//						Intent intent = new Intent();
//						intent.setComponent(component);
//						startActivity(intent);
//					} catch (Exception e) {
//						try {
//							ComponentName component = new ComponentName(
//							// 08-14 14:47:05.987: I/ActivityManager(846): START
//							// u0 {act=android.intent.action.MAIN
//							// cat=[android.intent.category.LAUNCHER]
//							// flg=0x10200000
//							// cmp=com.miui.securitycenter/.MainActivity
//							// bnds=[825,1272][1017,1464]} from pid 2328
//
//									"com.miui.securitycenter",
//									// "com.miui.permcenter.autostart.AutoStartManagementActivity"
//									"com.miui.securitycenter.MainActivity");
//							Intent intent = new Intent();
//							intent.setComponent(component);
//							startActivity(intent);
//						} catch (Exception ex) {
//							Toast.makeText(ctx, "请从桌面进入安全中心进行设置",
//									Toast.LENGTH_LONG).show();
//						}
//					}
//
//				}
//				
//				else if (checkMeiZu == 1) {
//					final String SCHEME = "package";
//
//					try {
//						Intent intent = new Intent(
//								Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//						Uri uri = Uri.fromParts(SCHEME,
//								ctx.getPackageName(), null);
//						intent.setData(uri);
//						ctx.startActivity(intent);
//						dialog.dismiss();
//
//					} catch (Exception e) {
//						Toast.makeText(ctx, "请从应用管理开启自启动权限",
//								Toast.LENGTH_LONG).show();
//					}
//
//				}
//
//				else if (checkHol == 1) {
//					final String SCHEME = "package";
//
//					try {
//						ComponentName component = new ComponentName(
//
//						"com.huawei.systemmanager",
//								"com.huawei.systemmanager.SystemManagerMainActivity");
//						Intent intent = new Intent();
//						intent.setComponent(component);
//						ctx.startActivity(intent);
//						dialog.dismiss();
//					} catch (Exception e) {
//
//						Toast.makeText(ctx, "请从手机管家设置闪铃自启动",
//								Toast.LENGTH_SHORT).show();
//
//					}
//
//				}
//
////				else if (!TyuPreferenceManager.isFirstOpen("checkPer")) {
////
////					try {
////						final String SCHEME = "package";
////						Intent intent = new Intent(
////								Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////						Uri uri = Uri.fromParts(SCHEME, getPackageName(), null);
////						intent.setData(uri);
////						startActivity(intent);
////						finish();
////
////					} catch (Exception e) {
////						Toast.makeText(ctx, "请从应用管理开启自启动权限", Toast.LENGTH_LONG)
////								.show();
////					}
////
////				}
//			}
//		});
//
//		my_shinning_nomore.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				SharedPreferences preference = ctx.getSharedPreferences(
//						"configs", Context.MODE_PRIVATE);
//				SharedPreferences.Editor editor = preference.edit();
//				editor.putInt("isNoMore", 1);
//				editor.commit();
//
//				dialog.dismiss();
//
//				Intent intent = new Intent();
//				intent.putExtra("firstJion", 0);
//				intent.setClass(GuideActivity.this, TabHostActivity.class);
//
//				startActivity(intent);
//				overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
//				finish();
//
//			}
//		});
//
//	}

	public static boolean isFlyme() {
		try {
			// Invoke Build.hasSmartBar()
			final Method method = Build.class.getMethod("hasSmartBar");
			return method != null;
		} catch (final Exception e) {

			return false;
		}
	}
	
	class mHander extends Handler {
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		my_shinning_nomore.setVisibility(View.VISIBLE);
		my_shinning.setVisibility(View.VISIBLE);
		my_shinning_set.setVisibility(View.VISIBLE);
	

	}

}
	

}

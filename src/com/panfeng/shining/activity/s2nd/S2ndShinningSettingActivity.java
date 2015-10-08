package com.panfeng.shining.activity.s2nd;

import java.io.IOException;

import tyu.common.utils.DensityUtil;
import tyu.common.utils.TextParser;
import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuContextKeeper;
import tyu.common.utils.TyuImageUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.TyuApp.TyuImageLoadingListener;
import com.panfeng.shining.TyuVideoPlayTask2nd;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.data.TyuShinningData.VideoItemData;
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;

public class S2ndShinningSettingActivity extends Activity implements
		OnClickListener {
	private ImageView imageIcon;
	boolean stop = false;
	Bitmap animBitmapCache = null;
	// private VideoView videoView;
	private View closebtn;
	private TextView infoText;
	private View part1;
	private TyuVideoPlayTask2nd videoTask;
	private View video_frame;
	private DisplayImageOptions options;
	private TyuImageLoadingListener simpleImageListener;
	private ImageLoader imageLoader;
	private TextView have_a_try;
	static public VideoItemData data_cache;
	Context ctx = S2ndShinningSettingActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (data_cache == null) {
			finish();
			return;
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.s2nd_shinning_setting_layout);

		options = TyuApp.getCommonConfig();
		simpleImageListener = new TyuImageLoadingListener();
		imageLoader = ImageLoader.getInstance();

		// 808684
		video_frame = findViewById(R.id.video_frame);
		LayoutParams lp = video_frame.getLayoutParams();
		if (lp != null) {
			lp.height = getResources().getDisplayMetrics().widthPixels;
			video_frame.setLayoutParams(lp);
		}
		initUI();

	}

	private void initUI() {
		// TODO Auto-generated method stub
		// 顶部
		// videoView = (VideoView) findViewById(R.id.video);

		closebtn = video_frame.findViewById(R.id.close);
		closebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				closePreview();
				
			}
		});
		infoText = (TextView) findViewById(R.id.info);
		infoText.setText("闪星人\r\n123xxxxxxxx");
		// 中部动画+文字
		imageIcon = (ImageView) findViewById(R.id.anim_icon);
		try {
			startImageViewAnim("set_shinning_anim", imageIcon);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView setting_note = (TextView) findViewById(R.id.setting_note);
		TextParser tp = new TextParser();
		tp.append("来去电时会显示在通话屏幕上耶!", DensityUtil.dip2px(this, 12), getResources()
				.getColor(R.color.s2nd_text_color_lv3));
//		tp.append("Ta", DensityUtil.dip2px(this, 18),
//				getResources().getColor(R.color.s2nd_app_main_color));
//		tp.append("的屏幕耶!", DensityUtil.dip2px(this, 18), getResources()
//				.getColor(R.color.s2nd_text_color_lv3));

		tp.parse(setting_note);
		// 底部三个操作按钮
		 have_a_try = (TextView)findViewById(R.id.have_a_try);
		have_a_try.setOnClickListener(this);
		View to_set = findViewById(R.id.to_set);
		to_set.setOnClickListener(this);
		View go_back = findViewById(R.id.go_back);
		go_back.setOnClickListener(this);

		part1 = findViewById(R.id.part1);
		ImageView img = (ImageView) part1.findViewById(R.id.img);
		if (data_cache != null && data_cache.image_url != null) {
			imageLoader.displayImage(data_cache.image_url, img, options);
		}
		// part1.setVisibility(View.INVISIBLE);
		videoTask = new TyuVideoPlayTask2nd(part1);
		videoTask.setupData(data_cache);

		closePreview();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		stop = true;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stop = true;
		if (animBitmapCache != null && !animBitmapCache.isRecycled()) {
			animBitmapCache.recycle();
		}
		closePreview();
	}

	void showPreview() {
		video_frame.setVisibility(View.VISIBLE);
		videoTask.playvideo();
	}

	void closePreview() {
		videoTask.stop();
		video_frame.setVisibility(View.INVISIBLE);
	}

	public void startImageViewAnim(final String aAssetPath, ImageView aImg)
			throws IOException {
		final String[] files = getAssets().list(aAssetPath);
		// List<String> arrays = Arrays.asList(files);
		// Collections.sort(arrays);
		new Thread() {
			@Override
			public void run() {
				int step = 0;
				while (!stop) {
					step = (step + 1) % files.length;
					try {
						final Bitmap old = animBitmapCache;
						animBitmapCache = TyuImageUtils
								.loadBitmapImageFromAssets(getAvtivity(),
										aAssetPath + "/" + files[step]);
						TyuContextKeeper.doUiTask(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								imageIcon.setImageBitmap(animBitmapCache);
								if (old != null && !old.isRecycled()) {
									old.recycle();
//									System.gc();
								}
							}
						});

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.have_a_try:
			if (video_frame.getVisibility() == View.VISIBLE) {
				closePreview();
				have_a_try.setText("看看效果");
				
				//TODO:MTA　look
				StatService.trackCustomEvent(ctx, "look", "");
				
			} else {
				showPreview();
				have_a_try.setText("关闭预览");
			}

			break;
		case R.id.to_set:
			setMyCurrentShinning();
			
			break;
		case R.id.go_back:
			finish();
			overridePendingTransition(R.anim.push_down_out,
					R.anim.push_down_out);
			
			break;
		default:
			break;
		}
	}
	
	
	//返回按键
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			/* 返回键 */
			if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_down_out,
					R.anim.push_down_out);
			}
			return false;
			}
	
	

	public void setMyCurrentShinning() {
		boolean res = TyuShinningData.getInstance().isSameShinning(data_cache);
		if (!res) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					TyuCommon.showToast(getActivity(), "设定闪铃成功");
					TyuContextKeeper.doUiTask(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							showSettingSucessDialog();
							SharedPreferences  preference = ctx.getSharedPreferences("configs", Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = preference.edit();
							editor.putInt("isSet", 0);
							editor.commit();
						}
					});
					// 更新用户数据
//					TyuUserInfo.getInstance().current_mb_id = data_cache.mb_id;
//					
//					
//					TyuShinningData.getInstance().setCurShinning(data_cache);
//					
//					
//					
//					TyuUserInfo.getInstance().updateInfo();

					// TyuCommon.showToast(getActivity(), "设定闪铃成功");

					// 下载闪铃
					int ret = TyuShinningData.getInstance().downloadMediaBase(
							data_cache);
					if (ret < 0) {
						// 重试一次
						TyuShinningData.getInstance().downloadMediaBase(
								data_cache);
					}

				}
			}).start();
			
		
		} else {
			TyuCommon.showToast(getActivity(), "您已经设定过该闪铃");
		}
	}

	Activity getActivity() {
		return this;
	}

	void showSettingSucessDialog() {
		closePreview();
		stop=true;
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.s2nd_shinning_setting_dialog_layout, null);
		final Dialog dialog = new Dialog(getActivity(), R.style.selectorDialog);

		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(dlgView);
		dialog.show();
		// imageView1
		dlgView.findViewById(R.id.imageView1).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						finish();
					}
				});
		ImageView media_image = (ImageView) dlgView
				.findViewById(R.id.media_image);
		imageLoader.displayImage(data_cache.image_url, media_image, options);
		TextView my_shinning = (TextView) dlgView
				.findViewById(R.id.my_shinning);
		my_shinning.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
//				S2ndShinningFriendDetailActivity.showMyShinning = true;
//				startActivity(new Intent(getActivity(),
//						S2ndShinningFriendDetailActivity.class));
//				finish();
			}
		});
		
		
//		Properties prop = new Properties();
//		prop.setProperty("video_id", ""+TyuUserInfo.getInstance().current_mb_id); 		
//		StatService.trackCustomKVEvent(this, "set_video", prop);

		
	}
}

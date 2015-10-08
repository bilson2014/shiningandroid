package com.panfeng.shining.activity.s2nd;

import java.io.File;
import java.util.Properties;

import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuContextKeeper;
import tyu.common.utils.TyuPreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.shining.activity.TyuAboutActivity;
import com.panfeng.shining.tools.FileTools;
import com.panfeng.shining.tools.UserControl;
import com.panfeng.shining.widgets.UpdateManager;
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;

public class s2ndSystemSettingActivity extends Activity {
	private Context ctx = s2ndSystemSettingActivity.this;
	SettingItemType2 feedback;
	
	
	String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	String imageString = pathString + File.separator
			+ ".com.panfeng.shining" + File.separator + "DownloadImage";
	
	String videoString = pathString + File.separator
			+ ".com.panfeng.shining" + File.separator + "DownloadVideo";

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fv_setting_layout);
		initTitleBar();
		SettingItemType1 wifi_auto_play = new SettingItemType1(R.id.item1_1,
				"wifi_auto_play");
		wifi_auto_play.txt1.setText("WIFI下自动播放");
		wifi_auto_play.txt2.setVisibility(View.GONE);
		wifi_auto_play.icon.setBackgroundResource(R.drawable.openwifi);
		SettingItemType1 gprs_auto_update = new SettingItemType1(R.id.item1_2,
				"gprs_auto_update");

		gprs_auto_update.txt1.setText("闪铃关闭/开启");
		gprs_auto_update.txt2.setVisibility(View.GONE);
		gprs_auto_update.icon.setBackgroundResource(R.drawable.myshin);

		SettingItemType2 clear_cache = new SettingItemType2(R.id.item2_1);
		clear_cache.txt1.setText("清除缓存");
		clear_cache.info.setText("");
		clear_cache.seticon.setBackgroundResource(R.drawable.clearcache);
		clear_cache.content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread() {
					@Override
					public void run() {
						// File dir = new File(TyuFileUtils.getValidPath(),
						// "media_cache");
						//
						// Log.i("lop", ""+dir.getAbsolutePath());
						//
						// TyuFileUtils.deleteDirectoryContent(dir.getAbsolutePath());
						// TyuFileUtils.deleteCache(dir);
						Looper.prepare();
						File imageFile = new File(imageString);
						File videoFile = new File(videoString);
						
					    long size = (FileTools.getFolderSize(imageFile)+FileTools.getFolderSize(videoFile))/1024/1024; 
						
					    
					    FileTools.deleteFolderFile(imageString, true);
					    FileTools.deleteFolderFile(videoString, true);
					
						
						TyuCommon.showToast(getActivity(), "缓存已清空"+"共"+size+"MB");
						Looper.loop();
						
					};
				}.start();
			

			}
		});
		
		
//		SettingItemType2 changeVideo = new SettingItemType2(R.id.item2_1_s);
//		changeVideo.txt1.setText("模式切换");
//		changeVideo.info.setText("");
//		changeVideo.seticon.setBackgroundResource(R.drawable.clearcache);
//		changeVideo.content.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//		
//				
//				SharedPreferences	preference = ctx.getSharedPreferences("configs",
//						Context.MODE_PRIVATE);
//				SharedPreferences.Editor editor = preference.edit();
//				editor.putInt("isHol", 1);
//				editor.commit();
//			
//
//			}
//		});
		
		
		
		
		
		
		
		
		
		
		SettingItemType2 check_update = new SettingItemType2(R.id.item2_2);
		check_update.txt1.setText("检查更新");
		check_update.seticon.setBackgroundResource(R.drawable.update);

		try {
			PackageInfo pack_info = TyuContextKeeper
					.getInstance()
					.getPackageManager()
					.getPackageInfo(
							TyuContextKeeper.getInstance().getPackageName(), 0);
			check_update.info.setText("v" + pack_info.versionName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		check_update.content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UpdateManager.showUpdateDialog(getActivity());
			}
		});

		SettingItemType2 about_us = new SettingItemType2(R.id.item2_3);
		about_us.txt1.setText("关于");
		about_us.seticon.setBackgroundResource(R.drawable.about);
		about_us.infoicon.setBackgroundResource(R.drawable.toicon);
		about_us.content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), TyuAboutActivity.class));
				StatService.trackCustomEvent(ctx, "aboutUs", "");
			}
		});

		SettingItemType2 user_log_out = new SettingItemType2(R.id.item2_4);
		user_log_out.txt1.setText("退出登录");
		user_log_out.content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (UserControl.isLogin()) {
					UserControl.exit();
					TyuCommon.showToast(getActivity(), "成功退出登录");

					// TODO:MTA logout
					StatService.trackCustomEvent(ctx, "logout", "");
					finish();
				} else {
					TyuCommon.showToast(getActivity(), "您还未登录");
				}

			}
		});

		// feedback = new SettingItemType2(R.id.item2_5);
		// feedback.txt1.setText("反馈信息");
		//
		// SharedPreferences preference = ctx.getSharedPreferences("Read",
		// ctx.MODE_PRIVATE);
		// int isrInteger = preference.getInt("isread", 0);
		//
		// if (isrInteger == 1)
		//
		// {
		//
		//
		//
		// feedback.infoiv.setVisibility(View.VISIBLE);
		//
		// }
		//
		// else{
		//
		// feedback.infoiv.setVisibility(View.GONE);
		//
		// }
		//
		// feedback.content.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// String name = TyuUserInfo.getInstance().name;
		// TelephonyManager mTelephonyMgr = (TelephonyManager) ctx
		// .getSystemService(Context.TELEPHONY_SERVICE);
		// String imei = mTelephonyMgr.getDeviceId();
		//
		// // if (name==null) {
		// //
		// // Toast.makeText(ctx, "请您先注册闪铃用户~", Toast.LENGTH_LONG).show();
		// // }
		//
		// if (imei == null || imei.length() == 0) {
		// Toast.makeText(ctx, "您的设备不支持反馈 请加QQ群49038471",
		// Toast.LENGTH_LONG).show();
		// }
		//
		// else {
		//
		// feedback.infoiv.setVisibility(View.GONE);
		// startActivity(new Intent(getActivity(), LoginActivity.class));
		// finish();
		// }
		//
		// }
		// });

		// SettingItemType2 feedback = new SettingItemType2(R.id.item2_5);
		// feedback.txt1.setText("反馈信息");
		// feedback.info.setText("");
		// feedback.content.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// FeedbackAgent agent = new FeedbackAgent(ctx);
		// agent.startFeedbackActivity();
		//
		//
		// }
		// });

		// SettingItemType2 app_recommendation = new
		// SettingItemType2(R.id.item2_5);
		// app_recommendation.txt1.setText("应用推荐");
		// //app_recommendation.info.setText("");
		// app_recommendation.content.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent();
		// intent.setAction("android.intent.action.VIEW");
		// Uri content_url = Uri.parse("http://www.wandoujia.com");
		// intent.setData(content_url);
		// startActivity(intent);
		// }
		// });
		//

		// SettingItemType2 activity = new SettingItemType2(R.id.item2_6);
		// activity.txt1.setText("点击抽奖");
		// activity.info.setText("");
		// activity.content.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent();
		// intent.setAction("android.intent.action.VIEW");
		// Uri content_url =
		// Uri.parse("http://www.shiningmovie.com/activity_1.php");
		// intent.setData(content_url);
		// startActivity(intent);
		// }
		// });

	}

	@Override
	protected void onResume() {
		super.onResume();

		// SharedPreferences preference = ctx.getSharedPreferences("Read",
		// ctx.MODE_PRIVATE);
		// int isrInteger = preference.getInt("isread", 0);
		//
		// if(isrInteger == 0){
		//
		// feedback.infoiv.setVisibility(View.GONE);
		// }
		// else{
		//
		// feedback.infoiv.setVisibility(View.VISIBLE);
		// }
	}

	class SettingItemType1 {
		public SettingItemType1(int aId, String aName) {
			View view = findViewById(aId);
			txt1 = (TextView) view.findViewById(R.id.txt1);
			txt2 = (TextView) view.findViewById(R.id.txt2);
			checkBox = (ImageView) view.findViewById(R.id.on_off);
			icon = (ImageView) view.findViewById(R.id.icon);
			key = aName;

			checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					boolean res = TyuPreferenceManager.isChecked(key);
					TyuPreferenceManager.setChecked(key, !res);

					refresh();

					// TODO:MTA
					if (key.equals("wifi_auto_play")) {
						wifeToMTA();

					}

					else {
						updateToMTA();

					}

				}
			});
			refresh();
		}

		public void wifeToMTA() {
			if (TyuPreferenceManager.isChecked(key)) {
				// TODO:MTA wifi on
				Properties prop = new Properties();
				prop.setProperty("wifi_on_off", "on");
				StatService.trackCustomKVEvent(ctx, "wifi_auto_play", prop);
			} else {
				// TODO:MTA wifi off
				Properties prop = new Properties();
				prop.setProperty("wifi_on_off", "off");
				StatService.trackCustomKVEvent(ctx, "wifi_auto_play", prop);

			}
		}

		public void updateToMTA() {
			if (TyuPreferenceManager.isChecked(key)) {
				// TODO:MTA update on
				Properties prop = new Properties();
				prop.setProperty("update_on_off", "on");
				StatService.trackCustomKVEvent(ctx, "gprs_auto_update", prop);
			} else {
				// TODO:MTA update off
				Properties prop = new Properties();
				prop.setProperty("update_on_off", "off");
				StatService.trackCustomKVEvent(ctx, "gprs_auto_update", prop);

			}
		}

		public void refresh() {
			if (TyuPreferenceManager.isChecked(key)) {
				checkBox.setImageResource(R.drawable.s2nd_on);

			} else {
				checkBox.setImageResource(R.drawable.s2nd_off);

			}
		}

		public String key;
		public TextView txt1;
		public TextView txt2;
		public ImageView icon;
		public ImageView checkBox;
	}

	Activity getActivity() {
		return this;
	}

	
		void initTitleBar() {
			View title = findViewById(R.id.newlistbar);
		
			ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
			TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
			bartxt.setText("系统设置");
			
			bartxt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
					overridePendingTransition(R.anim.push_right_in,
							R.anim.push_right_out);
				}
			});
			
			
			
			barimg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
					overridePendingTransition(R.anim.push_right_in,
							R.anim.push_right_out);
				}
			});

	}
	
	
	

	class SettingItemType2 {
		public SettingItemType2(int aId) {
			content = findViewById(aId);
			txt1 = (TextView) content.findViewById(R.id.txt1);
			info = (TextView) content.findViewById(R.id.info);
			infoiv = (ImageView) content.findViewById(R.id.imageinfo);
			seticon = (ImageView) content.findViewById(R.id.seticon);
			infoicon = (ImageView) content.findViewById(R.id.infoicon);
		}

		public View content;
		public TextView txt1;
		public TextView info;
		public ImageView seticon;
		public ImageView infoicon;
		public ImageView infoiv;

	}
}

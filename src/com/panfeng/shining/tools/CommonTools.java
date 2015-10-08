package com.panfeng.shining.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.panfeng.camera.surfaceview.SurfaceAnimationViewTabHost;
import com.panfeng.shining.activity.s2nd.TabHostActivity;
import com.panfeng.shining.interfaces.callBack;
import com.panfeng.shinning.R;

public class CommonTools  {



	 ImageView my_shinning_nomore;
	 ImageView my_shinning;
	 ImageView my_shinning_set;
	 ViewGroup dlgView;
	 SurfaceAnimationViewTabHost sav; 
	 int where;
	 
	 mHander hander = new mHander();
	
	
	
	

	public static void showToast(Context ctx, String str) {

		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();

	}
	
	
	public  void checkButton(Context context){
		
		LayoutInflater mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		dlgView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.message, null);
		
		
		 my_shinning_nomore = (ImageView) dlgView
				.findViewById(R.id.info_nomore);
		 my_shinning = (ImageView) dlgView
				.findViewById(R.id.info_close);
		 my_shinning_set = (ImageView) dlgView
				.findViewById(R.id.info_set);
		 
		 my_shinning_nomore.setVisibility(View.VISIBLE);
		 my_shinning.setVisibility(View.VISIBLE);
		 my_shinning_set.setVisibility(View.VISIBLE);
		
	}

	// 引导动画
	public  void showDialog(final Context context,int whereCome) {

		List<Integer> list = new ArrayList<Integer>();
		SharedPreferences preference;
		
		where = whereCome;

		if (!getSystemProperty("ro.miui.ui.version.name").equals("")) {

			preference = context.getSharedPreferences("configs",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = preference.edit();
			editor.putInt("isMIUI", 1);
			editor.commit();

		}

		if (isFlyme()) {
			preference = context.getSharedPreferences("configs",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = preference.edit();
			editor.putInt("isMeiZu", 1);
			editor.commit();

		}

		

		if (android.os.Build.MANUFACTURER.equals("HUAWEI")) {

			preference = context.getSharedPreferences("configs",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = preference.edit();
			editor.putInt("isHol", 1);
			editor.commit();

		}



		// 自定义布局

		preference = context.getSharedPreferences("configs",
				Context.MODE_PRIVATE);

		final int checkMode = preference.getInt("isMIUI", 0);
		final int checkMeiZu = preference.getInt("isMeiZu", 0);
		final int checkHol = preference.getInt("isHol", 0);
		
		
		LayoutInflater mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		dlgView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.message, null);
		
		sav = (SurfaceAnimationViewTabHost)dlgView.findViewById(R.id.info_cartoon);
		
		 my_shinning_nomore = (ImageView) dlgView
				.findViewById(R.id.info_nomore);
		 my_shinning = (ImageView) dlgView
				.findViewById(R.id.info_close);
		 my_shinning_set = (ImageView) dlgView
				.findViewById(R.id.info_set);
		 
//		 my_shinning_nomore.setVisibility(View.VISIBLE);
//		 my_shinning.setVisibility(View.VISIBLE);
//		 my_shinning_set.setVisibility(View.VISIBLE);
		 
		callBack cb=new callBack() {
			
			@Override
			public void method() {
				
				hander.sendEmptyMessage(1);
		
			}
		};
		sav.setCallBack(cb);
		
		 
		



		if (checkMode == 1) {
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);
			list.add(R.drawable.lin0001);

			list.add(R.drawable.lin0011);
			list.add(R.drawable.lin0012);
			list.add(R.drawable.lin0013);
			list.add(R.drawable.lin0014);
			list.add(R.drawable.lin0015);
			list.add(R.drawable.lin0016);
			list.add(R.drawable.lin0017);
			list.add(R.drawable.lin0018);
			list.add(R.drawable.lin0019);
			list.add(R.drawable.lin0020);
			list.add(R.drawable.lin0021);
			list.add(R.drawable.lin0022);
			list.add(R.drawable.lin0023);
			list.add(R.drawable.lin0024);
			list.add(R.drawable.lin0025);

			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);
			list.add(R.drawable.lin0027);

			list.add(R.drawable.lin0038);
			list.add(R.drawable.lin0039);
			list.add(R.drawable.lin0040);
			list.add(R.drawable.lin0041);
			list.add(R.drawable.lin0042);
			list.add(R.drawable.lin0043);
			list.add(R.drawable.lin0044);
			list.add(R.drawable.lin0045);
			list.add(R.drawable.lin0046);
			list.add(R.drawable.lin0047);
			list.add(R.drawable.lin0048);
			list.add(R.drawable.lin0049);
			list.add(R.drawable.lin0050);
			list.add(R.drawable.lin0051);
			list.add(R.drawable.lin0052);

			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);
			list.add(R.drawable.lin0054);

			list.add(R.drawable.lin0065);
			list.add(R.drawable.lin0066);
			list.add(R.drawable.lin0067);
			list.add(R.drawable.lin0068);
			list.add(R.drawable.lin0069);
			list.add(R.drawable.lin0070);
			list.add(R.drawable.lin0071);
			list.add(R.drawable.lin0072);
			list.add(R.drawable.lin0073);
			list.add(R.drawable.lin0074);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);


		}

		else if (checkMeiZu == 1) {

			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);
			list.add(R.drawable.mz_00001);

			list.add(R.drawable.mz_00002);
			list.add(R.drawable.mz_00003);
			list.add(R.drawable.mz_00004);
			list.add(R.drawable.mz_00005);
			list.add(R.drawable.mz_00006);
			list.add(R.drawable.mz_00007);
			list.add(R.drawable.mz_00008);
			list.add(R.drawable.mz_00009);
			list.add(R.drawable.mz_00010);
			list.add(R.drawable.mz_00011);
			list.add(R.drawable.mz_00012);
			list.add(R.drawable.mz_00013);
			list.add(R.drawable.mz_00014);
			list.add(R.drawable.mz_00015);
			list.add(R.drawable.mz_00016);
			list.add(R.drawable.mz_00017);
			list.add(R.drawable.mz_00018);
			list.add(R.drawable.mz_00019);
			list.add(R.drawable.mz_00020);
			list.add(R.drawable.mz_00021);
			list.add(R.drawable.mz_00022);
			list.add(R.drawable.mz_00023);
			list.add(R.drawable.mz_00024);
			list.add(R.drawable.mz_00025);
			list.add(R.drawable.mz_00026);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00027);
			list.add(R.drawable.mz_00028);
			list.add(R.drawable.mz_00029);
			list.add(R.drawable.mz_00030);
			list.add(R.drawable.mz_00031);
			list.add(R.drawable.mz_00032);
			list.add(R.drawable.mz_00033);
			list.add(R.drawable.mz_00034);
			list.add(R.drawable.mz_00035);
			list.add(R.drawable.mz_00036);
			list.add(R.drawable.mz_00037);
			list.add(R.drawable.mz_00038);
			list.add(R.drawable.mz_00039);
			list.add(R.drawable.mz_00040);
			list.add(R.drawable.mz_00041);
			list.add(R.drawable.mz_00042);
			list.add(R.drawable.mz_00043);
			list.add(R.drawable.mz_00044);
			list.add(R.drawable.mz_00045);
			list.add(R.drawable.mz_00046);
			list.add(R.drawable.mz_00047);
			list.add(R.drawable.mz_00048);
			list.add(R.drawable.mz_00049);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);


		}

		else if (checkHol == 1) {

			
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.tips);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			list.add(R.drawable.lin_00000);
			
			list.add(R.drawable.lin_00001);
			list.add(R.drawable.lin_00001);
			list.add(R.drawable.lin_00001);
			list.add(R.drawable.lin_00001);
			list.add(R.drawable.lin_00001);
			list.add(R.drawable.lin_00001);
			list.add(R.drawable.lin_00001);
			list.add(R.drawable.lin_00001);
			list.add(R.drawable.lin_00001);
			list.add(R.drawable.lin_00001);
			list.add(R.drawable.lin_00002);
			list.add(R.drawable.lin_00003);
			list.add(R.drawable.lin_00004);
			list.add(R.drawable.lin_00005);
			list.add(R.drawable.lin_00006);
			
			list.add(R.drawable.lin_00010);
			list.add(R.drawable.lin_00011);
			list.add(R.drawable.lin_00012);
			list.add(R.drawable.lin_00013);
			list.add(R.drawable.lin_00014);
			list.add(R.drawable.lin_00015);
			list.add(R.drawable.lin_00016);
			list.add(R.drawable.lin_00017);
			list.add(R.drawable.lin_00018);
			list.add(R.drawable.lin_00019);
			list.add(R.drawable.lin_00020);
			list.add(R.drawable.lin_00021);
			list.add(R.drawable.lin_00022);
			list.add(R.drawable.lin_00023);
			list.add(R.drawable.lin_00024);
			list.add(R.drawable.lin_00025);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00026);
			list.add(R.drawable.lin_00037);
			list.add(R.drawable.lin_00038);
			list.add(R.drawable.lin_00039);
			list.add(R.drawable.lin_00040);
			list.add(R.drawable.lin_00041);
			list.add(R.drawable.lin_00042);
			list.add(R.drawable.lin_00043);
			list.add(R.drawable.lin_00044);
			list.add(R.drawable.lin_00044);
			list.add(R.drawable.lin_00044);
			list.add(R.drawable.lin_00044);
			list.add(R.drawable.lin_00044);
			list.add(R.drawable.lin_00044);
			list.add(R.drawable.lin_00044);
			list.add(R.drawable.lin_00044);
			list.add(R.drawable.lin_00044);
			list.add(R.drawable.lin_00044);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);
			list.add(R.drawable.finish);


		}
		sav.setData(list);
		dlgView.startLayoutAnimation();
		
		

        

		
		final Dialog dialog = new Dialog(context,
				R.style.activity_theme_transparent);

		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(dlgView);
		dialog.show();
		// imageView1

		my_shinning.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated
				// method
				
				
				// stub
				
				if(where == 1){
					dialog.dismiss();
					Intent intent = new Intent();
					intent.putExtra("firstJion", 0);
					intent.setClass(context, TabHostActivity.class);
	
					context.startActivity(intent);
				
					((Activity) context).finish();
					
				}
				else{
					dialog.dismiss();
					
					
				}

			}
		});

		my_shinning_set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (checkMode == 1) {

					try {
						ComponentName component = new ComponentName(
								// 08-14 14:47:05.987: I/ActivityManager(846):
								// START u0 {act=android.intent.action.MAIN
								// cat=[android.intent.category.LAUNCHER]
								// flg=0x10200000
								// cmp=com.miui.securitycenter/.MainActivity
								// bnds=[825,1272][1017,1464]} from pid 2328

								"com.miui.securitycenter",
								"com.miui.permcenter.autostart.AutoStartManagementActivity"
						// "com.miui.securitycenter.MainActivity"
						);
						Intent intent = new Intent();
						intent.setComponent(component);
						context.startActivity(intent);
					} catch (Exception e) {

						try {
							ComponentName component = new ComponentName(
							// 08-14 14:47:05.987: I/ActivityManager(846): START
							// u0 {act=android.intent.action.MAIN
							// cat=[android.intent.category.LAUNCHER]
							// flg=0x10200000
							// cmp=com.miui.securitycenter/.MainActivity
							// bnds=[825,1272][1017,1464]} from pid 2328

									"com.miui.securitycenter",
									// "com.miui.permcenter.autostart.AutoStartManagementActivity"
									"com.miui.securitycenter.MainActivity");
							Intent intent = new Intent();
							intent.setComponent(component);
							context.startActivity(intent);
							//dialog.dismiss();
						}

						catch (Exception ex) {
							Toast.makeText(context, "请从桌面进入安全中心进行设置",
									Toast.LENGTH_LONG).show();
						}

					}
				}

				else if (checkMeiZu == 1) {
					final String SCHEME = "package";

					try {
						Intent intent = new Intent(
								Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						Uri uri = Uri.fromParts(SCHEME,
								context.getPackageName(), null);
						intent.setData(uri);
						context.startActivity(intent);
						dialog.dismiss();

					} catch (Exception e) {
						Toast.makeText(context, "请从应用管理开启自启动权限",
								Toast.LENGTH_LONG).show();
					}

				}

				else if (checkHol == 1) {
					final String SCHEME = "package";

					try {
						ComponentName component = new ComponentName(

						"com.huawei.systemmanager",
								"com.huawei.systemmanager.SystemManagerMainActivity");
						Intent intent = new Intent();
						intent.setComponent(component);
						context.startActivity(intent);
						dialog.dismiss();
					} catch (Exception e) {

						Toast.makeText(context, "请从手机管家设置闪铃自启动",
								Toast.LENGTH_SHORT).show();

					}

				}

			}
		});

		my_shinning_nomore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences preference = context.getSharedPreferences(
						"configs", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				editor.putInt("isNoMore", 1);
				editor.commit();

				if(where==1){
					dialog.dismiss();
					dialog.dismiss();
					Intent intent = new Intent();
					intent.putExtra("firstJion", 0);
					intent.setClass(context, TabHostActivity.class);
	
					context.startActivity(intent);
				
					((Activity) context).finish();
				}
				else{
					
					dialog.dismiss();
				}

			}
		});

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

	// 魅族
	public static boolean isFlyme() {
		try {
			// Invoke Build.hasSmartBar()
			final Method method = Build.class.getMethod("hasSmartBar");
			return method != null;
		} catch (final Exception e) {

			return false;
		}
	}

//	// 状态栏颜色
//	public static void titleColor(Context ctx, String value) {
//
//		// 创建状态栏的管理实例
//		SystemBarTintManager tintManager = new SystemBarTintManager(
//				(Activity) ctx);
//
//		// 激活导航栏设置
//		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setNavigationBarTintEnabled(false);
//
//		tintManager.setTintColor(Color.parseColor("#fe5453"));
//
//	}
	
	class mHander extends Handler {
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		my_shinning_nomore.setVisibility(View.VISIBLE);
		my_shinning.setVisibility(View.VISIBLE);
		my_shinning_set.setVisibility(View.VISIBLE);
	

	}

}
	

	
	
	

}

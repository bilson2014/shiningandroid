package com.panfeng.shining.service;

import com.panfeng.shining.TyuApp;
import com.panfeng.shining.activity.newactivity.MessageActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FVCallVideoShowService extends Service {
	Context context;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		Log.e("slt", "service create ");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		showWindow();
		return super.onStartCommand(intent, flags, startId);
	}

	private void showWindow() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e("slt", "thread is run");
				try {
					while (TyuApp.TopWindowsShow) {
						if (!TyuApp.VideoShwo) {
							Log.e("slt", "send startActivity");
							Intent intent1 = new Intent(context,MessageActivity.class);
							intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent1);
						
						}
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Log.e("slt", "thread kill ");
			}
		}).start();
	}
}

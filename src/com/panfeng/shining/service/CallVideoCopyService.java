package com.panfeng.shining.service;

import java.io.IOException;

import com.panfeng.shining.TyuApp;
import com.panfeng.shining.activity.newactivity.MessageActivity;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.tools.FileCopy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class CallVideoCopyService extends Service {
	Context context;
	String newPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/" + "com.panfeng.shinning" + "/"// 我的闪铃
			+ "myShinVideo/myShin.mp4";

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
				
				try {
					
					DBAdapter db = new DBAdapter(getApplicationContext());
					db.open();

					Cursor c = db.getMyVideo();

					if (c.moveToFirst() && c.getInt(0) == 1) {
						db.updateMyVideo(1, 1);
						FileCopy f = new FileCopy();
						f.fileCopy(TyuApp.lastVideo, newPath);
						TyuApp.lastVideo = "";
					}

					else {

						db.insertMyVideo(1);
						FileCopy f = new FileCopy();
						f.fileCopy(TyuApp.lastVideo, newPath);
						TyuApp.lastVideo = "";
					}

					db.close();
					
			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
	}
}

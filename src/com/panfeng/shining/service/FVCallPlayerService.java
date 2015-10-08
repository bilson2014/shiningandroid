package com.panfeng.shining.service;

import it.sauronsoftware.cron4j.Scheduler;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.panfeng.shining.activity.newactivity.MessageActivity;
import com.panfeng.shining.receiver.FVPhoneStateReceiver;

public class FVCallPlayerService extends Service {
	private FVPhoneStateReceiver psv;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		System.out.println("FVCallPlayerService::onCreate");
		psv = new FVPhoneStateReceiver(this);
		psv.start();
		setAlarm();
//		final Context context=getApplicationContext();
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(20000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				Intent it=new Intent();
//				it.setClass(context, MessageActivity.class);
//				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(it);
//			}
//		}).start();
		
		
		
	//	TyuShinningFriendSubService sss = new TyuShinningFriendSubService(this);
	//	sss.start();
		
		Log.i("FVPhoneStateReceiver1", "1");
		
		
	}

	private void setAlarm() {
		Scheduler scheduler = new Scheduler();  
		scheduler.schedule("00 18 * * *",
		new MessageNotifTask(getApplicationContext())); 
		 scheduler.start();  		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		psv.stop();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}

package com.panfeng.shining.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.panfeng.shining.activity.SplashActivity;
import com.panfeng.shinning.R;

public class MessageNotifTask implements Runnable {

	Context context;
	
	
	public MessageNotifTask(Context context) {
		super();
		this.context = context;
	}


	@Override
	public void run() {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		// 定义Notification的各种属性
		@SuppressWarnings("deprecation")
		Notification notification = new Notification(R.drawable.message_icon,
				"一大波铃声来袭！", System.currentTimeMillis());
		
	

		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		CharSequence contentTitle = " 闪铃"; // 通知栏标题
		CharSequence contentText = "闪铃库中又有新铃声啦！快去看看吧！"; // 通知栏内容
		Intent notificationIntent = new Intent(context, SplashActivity.class); // 点击该通知后要跳转的Activity

		PendingIntent contentItent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentItent);

		// 把Notification传递给NotificationManager
		notificationManager.notify(0, notification);
	}

}

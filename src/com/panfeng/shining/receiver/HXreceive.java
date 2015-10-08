package com.panfeng.shining.receiver;

import com.easemob.helpdeskdemo.activity.LoginActivity;
import com.panfeng.shinning.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class HXreceive extends BroadcastReceiver {

	SharedPreferences preference;

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		

		
	
		
		// 创建一个NotificationManager的引用
		NotificationManager notificationManager = (NotificationManager) arg0
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		

		// 定义Notification的各种属性
		Notification notification = new Notification(R.drawable.app_icon,
				"闪铃:客服来消息啦", System.currentTimeMillis());

		notification.defaults |= Notification.DEFAULT_SOUND;
		
	
		CharSequence contentTitle = "闪铃通知："; // 通知栏标题
		CharSequence contentText = "客服来消息啦"; // 通知栏内容
		Intent notificationIntent = new Intent(arg0, LoginActivity.class); // 点击该通知后要跳转的Activity
		PendingIntent contentItent = PendingIntent.getActivity(arg0, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(arg0, contentTitle, contentText,
				contentItent);

		// 把Notification传递给NotificationManager
		notificationManager.notify(0, notification);

		preference = arg0.getSharedPreferences("Read", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		editor.putInt("isread", 1);
		editor.commit();

	}

	private void showNotification() {

	}

}

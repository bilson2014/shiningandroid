package com.panfeng.shining.receiver;

import com.panfeng.shining.activity.newactivity.MessageActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AddVideoReceive extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		try {
			System.gc();
			if (MessageActivity.get() == null) {
				Intent intent1 = new Intent(arg0, MessageActivity.class);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				arg0.startActivity(intent1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 创建一个NotificationManager的引用
		// NotificationManager notificationManager = (NotificationManager) arg0
		// .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		//
		// // 定义Notification的各种属性
		// Notification notification = new Notification(R.drawable.app_icon,
		// "一大波铃声来袭！", System.currentTimeMillis());
		//
		// notification.defaults |= Notification.DEFAULT_SOUND;
		//
		// CharSequence contentTitle = " 闪铃"; // 通知栏标题
		// CharSequence contentText = "闪铃库中又有新铃声啦！快去看看吧！"; // 通知栏内容
		// Intent notificationIntent = new Intent(arg0, SplashActivity.class);
		// // 点击该通知后要跳转的Activity
		//
		// PendingIntent contentItent = PendingIntent.getActivity(arg0, 0,
		// notificationIntent, 0);
		// notification.setLatestEventInfo(arg0, contentTitle, contentText,
		// contentItent);
		//
		// // 把Notification传递给NotificationManager
		// notificationManager.notify(0, notification);
	}

}

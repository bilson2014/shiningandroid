package com.panfeng.shining.Impl;

import android.content.Context;
import android.content.Intent;

import com.panfeng.shining.TyuApp;
import com.panfeng.shining.activity.newactivity.MessageActivity;
import com.panfeng.shining.interfaces.topWindowInterface;
import com.panfeng.shining.service.FVCallVideoShowService;

public class showActivityImpl implements topWindowInterface {
	@Override
	public void closeWindow(int call) {
		TyuApp.TopWindowsShow = false;
		MessageActivity ma = (MessageActivity) MessageActivity.get();
		if (ma != null)
			ma.messageExit();
	}

	@Override
	public void showWindow(Context context, String str, int is, String path) {
		TyuApp.TopWindowsShow = true;
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
		intent.setClass(context, FVCallVideoShowService.class);
		context.startService(intent);
	}
}

package com.panfeng.shining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.data.TyuShinningData.ContactFriendItemData;
import com.panfeng.shining.data.TyuShinningData.ShinningFriendItemData;
import com.tencent.stat.StatService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import tyu.common.utils.ErrorReport;
import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuContextKeeper;

public class TyuShinningFriendSubService extends Thread {
	Context ctx=null;
	final static String TAG = "ShinningFriendSubService";
	static public final String action = "tyu.shinning.action.rec_sf_info";
	static List<ShinningFriendItemData> shinningFrData = null;
	public TyuShinningFriendSubService(Context mct) {
		setDaemon(true);
		ctx=mct;
	}
	static public List<ShinningFriendItemData> getShinningFrData(){
		if(shinningFrData==null){
			shinningFrData = new ArrayList<TyuShinningData.ShinningFriendItemData>();
		}
		return shinningFrData;
	}
	final int no_connect_scale = 5;
	final int wifi_scale = 10;
	final int gprs_scale = 30;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
//			try {
//				sleep(2*60*1000);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			int scale = wifi_scale;
			try {
				int state = TyuCommon.getNetState();
				switch (state) {
				case -1:
					scale = no_connect_scale;
					break;
				case ConnectivityManager.TYPE_MOBILE:
					scale = gprs_scale;
					try2DoTask(1);
					break;
				case ConnectivityManager.TYPE_WIFI:
					scale = wifi_scale;
					try2DoTask(2);
					break;
				default:
					break;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ErrorReport.outException(e);
			}
			// 睡眠
			int wait_count = (int) (scale / 2 + scale * Math.random());
			try {
				sleep(wait_count * 1000*60);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ErrorReport.outException(e);
			}
		}
	}
	static public void regReceiver(Context aContext,BroadcastReceiver aRec){
		IntentFilter ift = new IntentFilter();
		ift.addAction(action);
		aContext.registerReceiver(aRec, ift);
	}
	static public void unregReceiver(Context aContext,BroadcastReceiver aRec){
	
		aContext.unregisterReceiver(aRec);
	}
	// 可以设置当前任务下载文件数量
	public void try2DoTask(int length) {
		Log.v(TAG, "try2DoTask len = " + length);
		List<ContactFriendItemData> local = new ArrayList<ContactFriendItemData>();
		List<ShinningFriendItemData> shinning = new ArrayList<ShinningFriendItemData>();
		// 获取当前闪铃好友
		TyuShinningData.getInstance().getFriendsInfo(local, shinning);
		shinningFrData = shinning;
		//发送广播
		TyuContextKeeper.getInstance().sendBroadcast(new Intent(action));
		
		int index = 0;
		for (ShinningFriendItemData shinningFriendItemData : shinning) {
			if (shinningFriendItemData.mb_info != null) {
				int ret = TyuShinningData.getInstance().downloadMediaBase(
						shinningFriendItemData.mb_info);
				Log.v(TAG, "try2DoTask download:"
						+ shinningFriendItemData.mb_info.mb_id + " ret:" + ret);
				
				
				//下载成功
				if (ret >= 0) {
					Log.i("lutao", "path=0");
					
					if(ret==1){
					//TODO:MTA uodate friend
					Properties prop = new Properties();
				    prop.setProperty("video", ""+shinningFriendItemData.mb_info.mb_id);
					StatService.trackCustomKVEvent(ctx, "update_friend",
							prop);
					}
					
					index += ret;
					if (index >= length) {
						
						Log.i("lutao", "path="+ret);
						return;
					}
				}
			}
		}

	}
}

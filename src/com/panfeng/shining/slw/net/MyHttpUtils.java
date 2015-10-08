package com.panfeng.shining.slw.net;

import tyu.common.net.TyuDefine;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.slw.utils.FileUtils;
import com.panfeng.shining.slw.utils.LogUtils;


public class MyHttpUtils {
	private static String shiningVideoUrlByDate = TyuDefine.HOST
			+ "smc/get_media_base";

	public static String getVideoStringByDate() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("page_id", 1+"");
		params.addQueryStringParameter("page_size", 20 + "");
		params.addQueryStringParameter("sort", 0 + "");
		params.addQueryStringParameter("weight", 1+ "");


		HttpUtils http = new HttpUtils();
		try {
			ResponseStream responseStream = http.sendSync(HttpRequest.HttpMethod.GET, shiningVideoUrlByDate, params);
			Log.i("slt", responseStream.readString());
			return responseStream.readString();
		} catch (Exception e) {
			LogUtils.writeErrorLog("获取视频信息", "", e, null);
		}
		return null;
	}



	public static boolean isNetworkConnected(Context context) {
		if(context==null)
			return  false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if(FileUtils.stringNotEmpty(extraInfo)){
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

}

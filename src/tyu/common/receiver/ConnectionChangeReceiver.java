package tyu.common.receiver;

import tyu.common.utils.NetworkUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	public static final String ERR_TAG = "ConnectionChangeReceiver.java";

	@Override
	public void onReceive(Context context, Intent intent) {

		int state = NetworkUtils.getNetworkState(context);
		if (state == NetworkUtils.NONE) {
			// 没有网络
		} else {

		}
		Log.i("test", "网络变化");

	}
}

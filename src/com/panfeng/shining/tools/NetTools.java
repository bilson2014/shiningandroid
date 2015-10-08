package com.panfeng.shining.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetTools {

	
	public static boolean checkNetwork(Context ctx) {  
        ConnectivityManager conn = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo net = conn.getActiveNetworkInfo();  
        if (net != null && net.isConnected()) {  
            return true;  
        }  
        return false;  
    } 

}

package com.panfeng.shining.activity.s2nd;

import org.json.JSONArray;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.panfeng.shinning.R;

public class MediaMainListActivity extends Activity {
	
	int page =1;
	int pageSize =5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medialist);
		
		String url = TyuDefine.HOST
				+ "smc/get_media_base?page_id=%d&page_size=%d&sort=%d";
		
		
		String res = TyuHttpClientUtils.postInfo(
				String.format(url, page + 1, pageSize, 0), "");
		
		
		Log.i("store","store"+res );
		
		try {
			JSONArray jarray = new JSONArray(res);
			for (int i = 0; i < jarray.length(); i++){
				
			} 
		
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		
		
	}

}
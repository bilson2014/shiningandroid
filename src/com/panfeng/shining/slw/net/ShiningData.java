package com.panfeng.shining.slw.net;

import android.util.Log;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shining.slw.utils.FileUtils;
import com.panfeng.shining.slw.utils.LogUtils;


public class ShiningData {

	public List<VideoEntity> getVideoSourceList(List<VideoEntity> listadapter) {
		try {
			String reslut = MyHttpUtils.getVideoStringByDate();
			Log.i("dawn",reslut+"/reslut");
			if (FileUtils.stringNotEmpty(reslut)) {
				JSONArray videoArray = new JSONArray(reslut);
				VideoEntity veEntity;
				JSONObject json;
//{"mb_video_name":"1.4352356874881165E12.mp4",
//				"mb_content":"abc",
//				"mb_keys":"搞笑|无节操",
//				"mb_favorite":0,
//				"mb_name":"adc",
//				"mb_weight":1,
//				"mb_author":"adc",
//				"mb_id":332,
//				"mb_state":1,
//				"mb_upload_time":"2015-06-25-20-34"
				for (int i = 0; i < videoArray.length(); i++) {
					json = videoArray.getJSONObject(i);
					veEntity = new VideoEntity();
					veEntity.setVideo_id(json.getString("mb_id"));
					veEntity.setVideo_name(json.getString("mb_name"));
					veEntity.setVideo_fIleName(json.getString("mb_video_name"));
					veEntity.setVideo_content(json.getString("mb_content"));
					veEntity.setVideo_key(json.getString("mb_keys"));
					veEntity.setVideo_author(json.getString("mb_author"));
					veEntity.setVideo_weight(json.getString("mb_weight"));
					veEntity.setVideo_state("0");
					listadapter.add(veEntity);
				}
			}
		} catch (JSONException e) {
			 e.printStackTrace();
			LogUtils.writeErrorLog("转换json数组", "", e, null);
		}
		return listadapter;
	}
}

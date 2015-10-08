//package com.panfeng.shining.data;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.panfeng.shining.data.TyuShinningData.VideoLocalItemData;
//
//import android.text.TextUtils;
//import android.util.Log;
//
//import tyu.common.net.TyuDefine;
//import tyu.common.net.TyuHttpClientUtils;
//import tyu.common.utils.TyuCommon;
//import tyu.common.utils.TyuFileUtils;
//
//public class TyuUserInfo {
//	public String name;
//	public String phone;
//	public String user_image;
//	public int user_id = -1;
//	// 通过此ID进行本地路径校验
//	public int current_mb_id = -1;
//
//	public String imei;
//	public String uuid;
//	static TyuUserInfo instance;
//
//	// static final int GET_BY_IMEI = 1;
//	// static final int GET_BY_PHONE = 2;
//	// static final int GET_BY_UUID = 3;
//	// static final int GET_BY_USER_ID = 4;
//	ArrayList<VideoLocalItemData> my_videos = null;
//
//	static public TyuUserInfo getInstance() {
//		if (instance == null) {
//			instance = new TyuUserInfo();
//			instance.loadUserInfo();
//		}
//		return instance;
//	}
//
//	public boolean isLogin() {
//		if (user_id > 0 && !TextUtils.isEmpty(phone))
//			return true;
//		return false;
//	}
//
//	public void saveMyVideos() {
//		TyuShinningData.user_shinning_dir.mkdirs();
//		File file = new File(TyuShinningData.user_shinning_dir,
//				"user_videos.dat");
//		my_videos = getMyVideos();
//		JSONArray array = new JSONArray();
//		for (int i = 0; i < my_videos.size(); i++) {
//			array.put(my_videos.get(i).toJson());
//		}
//		TyuFileUtils.dumpToFile(file.getAbsolutePath(), array.toString());
//
//	}
//
//	public ArrayList<VideoLocalItemData> getMyVideos() {
//		if (my_videos == null) {
//			my_videos = loadMyVideos();
//		}
//		return my_videos;
//	}
//
//	public ArrayList<VideoLocalItemData> getMyVideosLocal() {
//		ArrayList<VideoLocalItemData> data = getMyVideos();
//		ArrayList<VideoLocalItemData> ret = new ArrayList<TyuShinningData.VideoLocalItemData>();
//		for (VideoLocalItemData videoLocalItemData : data) {
//			if (videoLocalItemData.extraData == null) {
//				ret.add(videoLocalItemData);
//			}
//		}
//		return ret;
//	}
//
//	public ArrayList<VideoLocalItemData> getMyVideosUploaded() {
//		ArrayList<VideoLocalItemData> data = getMyVideos();
//		ArrayList<VideoLocalItemData> ret = new ArrayList<TyuShinningData.VideoLocalItemData>();
//		for (VideoLocalItemData videoLocalItemData : data) {
//			if (videoLocalItemData.extraData != null) {
//				ret.add(videoLocalItemData);
//			}
//		}
//		return ret;
//	}
//
//	public void exit() {
//		File file = new File(TyuShinningData.user_shinning_dir, "user_info.dat");
//		loadUserInfo();
//		name = null;
//		phone = null;
//		user_image = null;
//		user_id = -1;
//		// 通过此ID进行本地路径校验
//		current_mb_id = -1;
//
//	}
//
//	public ArrayList<VideoLocalItemData> loadMyVideos() {
//		ArrayList<VideoLocalItemData> ret = new ArrayList<TyuShinningData.VideoLocalItemData>();
//		File file = new File(TyuShinningData.user_shinning_dir,
//				"user_videos.dat");
//		if (file.exists()) {
//			String res = TyuFileUtils.loadFromFile(file.getAbsolutePath());
//			try {
//				JSONArray array = new JSONArray(res);
//				for (int i = 0; i < array.length(); i++) {
//					JSONObject jobj = array.getJSONObject(i);
//					VideoLocalItemData data = new VideoLocalItemData();
//					if (data.initFromJson(jobj)) {
//						ret.add(data);
//					}
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return ret;
//	}
//
//	public void saveUserInfo(String aData) {
//		TyuShinningData.user_shinning_dir.mkdirs();
//		File file = new File(TyuShinningData.user_shinning_dir, "user_info.dat");
//		if (aData == null) {
//			file.delete();
//		} else {
//			TyuFileUtils.dumpToFile(file.getAbsolutePath(), aData);
//		}
//	}
//
//	public void loadUserInfo() {
//		imei = TyuCommon.getImei();
//		uuid = TyuCommon.getTyuUid();
//		
//		
//		File file = new File(TyuShinningData.user_shinning_dir, "user_info.dat");
//		if (file.exists()) {
//			try {
//				String res = TyuFileUtils.loadFromFile(file.getAbsolutePath());
//				JSONObject obj = new JSONObject(res);
//				loadData(obj);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//
//	void loadData(JSONObject obj) throws Exception {
//		if (!obj.isNull("ui_phone"))
//			phone = obj.getString("ui_phone");
//		if (!obj.isNull("ui_name"))
//			name = obj.getString("ui_name");
//		if (!obj.isNull("ui_id"))
//			user_id = obj.getInt("ui_id");
//		if (!obj.isNull("ui_mb_id"))
//			current_mb_id = obj.getInt("ui_mb_id");
//		if (!obj.isNull("ui_image"))
//			user_image = obj.getString("ui_image");
//	}
//
//	public boolean updateInfo() {
//		return updateInfo(null);
//	}
//
//	public boolean cancelShinning() {
//		imei = TyuCommon.getImei();
//		uuid = TyuCommon.getTyuUid();
//		HashMap<String, Object> valueMap = new HashMap<String, Object>();
//		valueMap.put("imei", imei);
//		valueMap.put("uuid", uuid);
//		valueMap.put("mb_id", 0 + "");
//		String url = TyuDefine.HOST + "smc/post_user_data";
//		String res = TyuHttpClientUtils.postMutiPart(url, valueMap, null);
//		try {
//			JSONObject obj = new JSONObject(res);
//			loadData(obj);
//			saveUserInfo(res);
//			return true;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	public boolean updateInfo(File image) {
//		imei = TyuCommon.getImei();
//		uuid = TyuCommon.getTyuUid();
//		
//		Log.i("video", "imei"+imei);
//		Log.i("video", "uuid"+uuid);
//		
//		String url = TyuDefine.HOST + "smc/post_user_data";
//		HashMap<String, Object> valueMap = new HashMap<String, Object>();
//		valueMap.put("imei", imei);
//		valueMap.put("uuid", uuid);
//		if (user_id > 0) {
//			valueMap.put("id", user_id + "");
//		}
//		if (current_mb_id > 0) {
//			valueMap.put("mb_id", current_mb_id + "");
//		}
//		if (!TextUtils.isEmpty(phone))
//			valueMap.put("phone", phone);
//		if (!TextUtils.isEmpty(name))
//			valueMap.put("name", name);
//		if (image != null && image.exists())
//			valueMap.put("file", image);
//		String res = TyuHttpClientUtils.postMutiPart(url, valueMap, null);
//		Log.i("video", "res"+res);
//		try {
//			JSONObject obj = new JSONObject(res);
//			loadData(obj);
//			saveUserInfo(res);
//			return true;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//
//	}
//}

package com.panfeng.shining.data;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.panfeng.shining.receiver.FVPhoneStateReceiver;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import tyu.common.utils.Coding;
import tyu.common.utils.TyuContactUtils;
import tyu.common.utils.TyuContextKeeper;
import tyu.common.utils.TyuFileUtils;
import tyu.common.utils.TyuObjectSerilizer;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class TyuShinningData {

	// ===================路径相关配置===================//
	// static final public File temp_work_dir = new File(
	// TyuFileUtils.getValidPath(), "work_temp");
	static final public File user_shinning_dir = new File(
			TyuFileUtils.getValidPath(), "user_shinning_dir");
	static final public File media_cache = new File(
			TyuFileUtils.getValidPath(), "media_cache");

	// =======================变量=====================//
	static public TyuShinningData mInstance = null;
	VideoItemData mCurrentUsedShinning = null;
	Context mContext;
	String resString;

	public TyuShinningData() {
		mContext = TyuContextKeeper.getInstance();
	}

	static public TyuShinningData getInstance() {
		if (mInstance == null) {
			mInstance = new TyuShinningData();
			mInstance.mCurrentUsedShinning = mInstance
					.loadCurrentUsedShinning();
		}
		return mInstance;
	}

	public void setCurShinning(VideoItemData aDa) {
		mCurrentUsedShinning = aDa;
		saveCurrentUsedShinning(aDa);
		Context context = TyuContextKeeper.getInstance();
		context.sendBroadcast(new Intent(
				FVPhoneStateReceiver.ACTION_CHANGED_SHINNING));
	}

	public VideoItemData getCurShinning() {
		return mCurrentUsedShinning;
	}

	public boolean isCurrentShinningDownloaded() {

		if (mCurrentUsedShinning == null)
			return false;
		File localFile = mCurrentUsedShinning.local_file;
		return localFile.exists();
	}

	public boolean isSameShinning(VideoItemData aDa) {
		if (aDa != null && mCurrentUsedShinning != null
				&& aDa.mb_id == mCurrentUsedShinning.mb_id)
			return true;
		return false;
	}

	public void saveCurrentUsedShinning(VideoItemData aData) {
		user_shinning_dir.mkdirs();
		File file = new File(user_shinning_dir, "cur_shinning.dat");
		if (aData == null) {
			file.delete();
		} else {
			TyuFileUtils.dumpToFile(file.getAbsolutePath(),
					aData.mRawData.toString());
		}

	}

	public VideoItemData loadCurrentUsedShinning() {
		File file = new File(user_shinning_dir, "cur_shinning.dat");
		if (file.exists()) {
			String res = TyuFileUtils.loadFromFile(file.getAbsolutePath());
			VideoItemData tmp = new VideoItemData();
			if (tmp.initData(res)) {
				return tmp;
			}
		}
		return null;
	}

	// -1:下载失败 0：文件已经存在 1:下载成功
	public int downloadMediaBase(VideoItemData aData) {
		if (aData == null)
			return -1;
		int ret = -1;
		try {
			File localFile = aData.local_file;
			if (localFile == null)
				return -1;
			localFile.getParentFile().mkdirs();
			// 判断是否在缓存中
			HashMap<String, VideoCacheInfo> cacheMap = getVideoCache();
			VideoCacheInfo info = cacheMap.get(aData.video_url);

			Log.i("lutao", "path=" + aData.video_url);

			if (info != null) {
				if (info.isCached()) {
					try {
						TyuFileUtils.copyFile(new File(info.cacheFile),
								localFile);
						return 0;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			ret = TyuHttpClientUtils.downLoadFile(aData.video_url,
					localFile.getAbsolutePath(), null, null);
			if (ret == 1 || ret == 0) {
				//
			} else {
				TyuFileUtils.deleteDirectory(localFile.getParentFile()
						.getAbsolutePath());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public List<SortItemData> getAllSorts() {
		ArrayList<SortItemData> ret = new ArrayList<SortItemData>();
		String url = TyuDefine.HOST + "smc/get_sort_list";
		String res = TyuHttpClientUtils.postInfo(url, "");
		try {
			JSONArray jarray = new JSONArray(res);
			for (int i = 0; i < jarray.length(); i++) {
				SortItemData tmp = new SortItemData();
				if (tmp.initData(jarray.getJSONObject(i)))
					ret.add(tmp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public void addWeightAsync(final int aId) {
		new Thread() {
			@Override
			public void run() {
				String url = TyuDefine.HOST + "smc/add_weight?id=" + aId;
				TyuHttpClientUtils.postInfo(url, "");
			};
		}.start();
	}

	// getMediaByTag
	public List<VideoItemData> getMediaS2nd(int page, int pageSize,
			int keyIndex, List<String> keys) {
		/*
		 * if (keyIndex == 0) { // 按照时间先后顺序获取 return getMediaByPage(page,
		 * pageSize, 0, false); }
		 * 
		 * else if (keyIndex == 1){
		 * 
		 * return getMediaByPage(page, pageSize, 0, true); }
		 * 
		 * 
		 * else {
		 */
		// 按照关键字获取
		return searchMediaByPage(page, pageSize, 1, keys.get(keyIndex));
		// }
	}

	public String getMediaS2nd_2(int page, int pageSize, String name) {

		String url = TyuDefine.HOST + "smc/search_media_base";
		// 服务器分页从1开始，本地分页从0开始，所以提交参数+1
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page_id", (page + 1) + "");
		map.put("page_size", pageSize + "");
		map.put("key", name);

		String res = TyuHttpClientUtils.postMutiPart(url, map, null);

		return res;
		// }
	}

	public List<VideoItemData> getMediaByPage(int page, int pageSize, int sort,
			boolean useWeight) {
		ArrayList<VideoItemData> ret = new ArrayList<VideoItemData>();
		String url = TyuDefine.HOST
				+ "smc/get_media_base?page_id=%d&page_size=%d&sort=%d";
		if (useWeight) {
			url += "&weight=1";
		}
		// 服务器分页从1开始，本地分页从0开始，所以提交参数+1
		String res = TyuHttpClientUtils.postInfo(
				String.format(url, page + 1, pageSize, sort), "");
		try {
			JSONArray jarray = new JSONArray(res);
			for (int i = 0; i < jarray.length(); i++) {
				VideoItemData tmp = new VideoItemData();
				if (tmp.initData(jarray.getJSONObject(i)))
					ret.add(tmp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("lutao", ret.toString());
		return ret;

	}

	// public String getMediaByPage_2(int page, int pageSize, int sort,
	// boolean useWeight) {
	// String url = TyuDefine.HOST
	// + "smc/get_media_base?page_id=%d&page_size=%d&sort=%d";
	// if (useWeight) {
	// url += "&weight=1";
	// }
	// // 服务器分页从1开始，本地分页从0开始，所以提交参数+1
	// String res = TyuHttpClientUtils.postInfo(
	// String.format(url, page + 1, pageSize, sort), "");
	//
	// Log.i("dawn", "adasda"+res);
	//
	// return res;
	// }

	public String getMediaByPage_2s(int page, int pageSize, int sort,
			boolean useWeight, String startID) {

		String res;
		try {
			int start = Integer.parseInt(startID);
			String url = TyuDefine.HOST
					+ "smc/get_media_base_new?page_id=%d&page_size=%d&vId=%d";
			if (useWeight) {
				url += "&weight=1";
			}
			// 服务器分页从1开始，本地分页从0开始，所以提交参数+1
			res = TyuHttpClientUtils.postInfo(
					String.format(url, page + 1, pageSize, start), "");

		} catch (Exception e) {
			// TODO: handle exception
			String url = TyuDefine.HOST
					+ "smc/get_media_base_new?page_id=%d&page_size=%d";
			if (useWeight) {
				url += "&weight=1";
			}
			// 服务器分页从1开始，本地分页从0开始，所以提交参数+1
			res = TyuHttpClientUtils.postInfo(
					String.format(url, page + 1, pageSize), "");
			Log.i("dawn", String.format(url, page + 1, pageSize));
		}

		return res;
	}

	public String getMediaByPage_3() {
		String url = TyuDefine.HOST + "smc/getOnLineVideoSortList";

		Log.i("slwslt", "res" + url);
		// 服务器分页从1开始，本地分页从0开始，所以提交参数+1
		String res = TyuHttpClientUtils.postInfo(String.format(url, null), "");

		return res;
	}

	public String getTypeList() {
		String url = TyuDefine.HOST + "smc/getVideoSortList";
		String res = TyuHttpClientUtils.postInfo(String.format(url, null), "");
		return res;
	}

	public String getMediaByPage_set(int id) {
		String url = TyuDefine.HOST + "smc/add_settingtimes?id=%d";

		String res = TyuHttpClientUtils.postInfo(String.format(url, id), "");

		Log.i("ID", id + "");

		return res;
	}

	public String getMediaByPage_play(int id) {
		String url = TyuDefine.HOST + "smc/add_settingtimes?id=%";

		String res = TyuHttpClientUtils.postInfo(String.format(url, id), "");

		return res;
	}

	public String getMediaByPage_find(int page, int pageSize, String value)
			throws UnsupportedEncodingException {
		String url = TyuDefine.HOST
				+ "smc/search_Media_Base?page_id=%d&page_size=%d&searchValue=%s";

		if (value != null) {
			resString = value.trim();
		}

		else {

			resString = value;
		}

		resString = URLEncoder.encode(resString, "UTF-8");

		// 服务器分页从1开始，本地分页从0开始，所以提交参数+1
		String res = TyuHttpClientUtils.postInfo(
				String.format(url, 1, 10, resString), "");

		Log.i("slwslt", res);
		return res;
	}

	public List<VideoItemData> getMediaByToday() {
		ArrayList<VideoItemData> ret = new ArrayList<VideoItemData>();
		String url = TyuDefine.HOST + "smc/get_media_base_by_date";
		url += "?time=" + System.currentTimeMillis();
		String res = TyuHttpClientUtils.postInfo(url, "");
		try {
			JSONArray jarray = new JSONArray(res);
			for (int i = 0; i < jarray.length(); i++) {
				VideoItemData tmp = new VideoItemData();
				if (tmp.initData(jarray.getJSONObject(i)))
					ret.add(tmp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public int getShinningUserCount() {
		int ret = -1;
		String url = TyuDefine.HOST + "smc/get_user_count";
		String res = TyuHttpClientUtils.postInfo(url, "");
		try {
			// ret = Integer.parseInt(res);
			JSONObject obj = new JSONObject(res);
			if (!obj.isNull("count(*)"))
				ret = obj.getInt("count(*)");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ret;
	}

	public List<VideoItemData> searchMediaByPage(int page, int pageSize,
			int sort, String aKey) {
		ArrayList<VideoItemData> ret = new ArrayList<VideoItemData>();
		String url = TyuDefine.HOST + "smc/search_media_base";
		// 服务器分页从1开始，本地分页从0开始，所以提交参数+1
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page_id", (page + 1) + "");
		map.put("page_size", pageSize + "");
		if (TextUtils.isEmpty(aKey))
			map.put("sort", sort + "");
		else
			map.put("key", aKey + "");

		String res = TyuHttpClientUtils.postMutiPart(url, map, null);
		// Log.i("dawn",res);
		try {
			JSONArray jarray = new JSONArray(res);
			for (int i = 0; i < jarray.length(); i++) {
				VideoItemData tmp = new VideoItemData();
				if (tmp.initData(jarray.getJSONObject(i)))
					ret.add(tmp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}

	public void getFriendsInfo(List<ContactFriendItemData> local,
			List<ShinningFriendItemData> shinning) {
		Context context = TyuContextKeeper.getInstance();
		TyuContactUtils.getContactList(context, local);
		try {
			JSONObject post_obj = new JSONObject();
			JSONArray array = new JSONArray();
			for (int i = 0; i < local.size(); i++) {
				array.put(local.get(i).number);
			}
			post_obj.put("phones", array);
			String url = TyuDefine.HOST + "smc/get_shinning_friends_2nd";
			// 做一下二级编码
			String hexString = Coding.bin2hex(post_obj.toString());
			String res = TyuHttpClientUtils.postZippedInfo(url, hexString);
			JSONArray sarray = new JSONArray(res);
			for (int i = 0; i < sarray.length(); i++) {
				ShinningFriendItemData tmp = new ShinningFriendItemData();
				VideoItemData vd = new VideoItemData();
				JSONObject oo = sarray.getJSONObject(i);
				if (vd.initData(oo)) {
					tmp.mb_info = vd;
				} else {
					tmp.mb_info = null;
				}
				if (!oo.isNull("ui_name"))
					tmp.name = oo.getString("ui_name");
				else {
					tmp.name = "";
				}
				if (!oo.isNull("ui_imei")) {
					tmp.imei = oo.getString("ui_imei");
				}
				if (!oo.isNull("ui_phone"))
					tmp.number = oo.getString("ui_phone");
				if (!oo.isNull("ui_image"))
					tmp.user_image = oo.getString("ui_image");

				shinning.add(tmp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static public class SortItemData {
		public JSONObject mRawData;
		public int ms_id = 0;
		public String name;
		public String image_url;
		public String image_shortcut_url;

		public boolean initData(JSONObject jsonObject) {
			// TODO Auto-generated method stub
			boolean res = true;
			try {
				mRawData = jsonObject;
				ms_id = jsonObject.getInt("ms_id");
				if (!jsonObject.isNull("ms_name"))
					name = jsonObject.getString("ms_name");

				image_url = jsonObject.getString("ms_image_path");
				image_shortcut_url = jsonObject.getString("ms_shortcut_path");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				res = false;
			}
			return res;
		}
	}

	static public class VideoLocalItemData {
		public String name;
		public String image_path;
		public String video_path;
		public VideoItemData extraData = null;

		public boolean initFromJson(JSONObject aObj) {
			if (aObj == null)
				return false;

			try {
				if (!aObj.isNull("name"))
					name = aObj.getString("name");
				if (!aObj.isNull("image"))
					image_path = aObj.getString("image");
				if (!aObj.isNull("video"))
					video_path = aObj.getString("video");
				if (!aObj.isNull("extra")) {
					JSONObject temp = aObj.getJSONObject("extra");
					extraData = new VideoItemData();
					extraData.initData(temp);
				}
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		}

		public JSONObject toJson() {
			JSONObject obj = new JSONObject();

			try {
				if (!TextUtils.isEmpty(name))
					obj.put("name", name);
				if (!TextUtils.isEmpty(image_path))
					obj.put("image", image_path);
				if (!TextUtils.isEmpty(video_path))
					obj.put("video", video_path);
				if (extraData != null && extraData.mRawData != null) {
					obj.put("extra", extraData.mRawData);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return obj;
		}
	}

	static public class VideoItemData {
		// public String dir;
		// public String img;
		// public String txt;
		// public String video;

		public JSONObject mRawData;
		public int mb_id = 0;
		public int mb_ms_id = 0;

		public String mb_keys;
		public String name;
		public String content;
		public String image_url;
		public String video_url;

		public File local_file = null;
		public String author;

		public boolean initData(JSONObject aData) {
			boolean res = true;
			try {
				mRawData = aData;
				if (!aData.isNull("mb_id")) {
					mb_id = aData.getInt("mb_id");
				}
				if (!aData.isNull("mb_name"))
					name = aData.getString("mb_name");
				if (!aData.isNull("mb_author"))
					author = aData.getString("mb_author");
				if (!aData.isNull("mb_content"))
					content = aData.getString("mb_content");
				if (!aData.isNull("mb_keys"))
					mb_keys = aData.getString("mb_keys");

				if (!aData.isNull("mb_video_name")) {
					String videoname = aData.getString("mb_video_name");
					image_url = TyuDefine.HOST
							+ "media_base/"
							+ videoname
									.substring(0, videoname.lastIndexOf('.'))
							+ ".jpg";
					video_url = TyuDefine.HOST + "smc/getVideo?id=" + mb_id;
					String path = mb_id + "/" + videoname;
					local_file = new File(media_cache, path);
					mb_ms_id = 0;
				} else {
					if (!aData.isNull("mb_image_path"))
						image_url = aData.getString("mb_image_path");

					if (!aData.isNull("mb_video_path"))
						video_url = aData.getString("mb_video_path");
					if (!aData.isNull("mb_ms_id"))
						mb_ms_id = aData.getInt("mb_ms_id");
					// 配置本地映射路径
					if (!TextUtils.isEmpty(video_url)) {
						int pos = video_url.lastIndexOf("/");
						String path = mb_id + "/"
								+ video_url.substring(pos + 1);
						local_file = new File(media_cache, path);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				res = false;
			}
			return res;
		}

		public boolean initData(String aStr) {
			boolean res = true;
			try {
				JSONObject aData = new JSONObject(aStr);
				res = initData(aData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				res = false;
			}
			return res;
		}
	}

	static public class ContactFriendItemData {
		public String name;
		public String number;
	}

	static public class ShinningFriendItemData {
		public String name;
		public String number;
		public String imei;
		public String user_image;
		public VideoItemData mb_info = null;
		public ContactFriendItemData local_info = null;
	}

	static public class VideoCacheInfo implements Serializable {

		private static final long serialVersionUID = -1640904493504605747L;
		// public long fileSize;
		public String cacheFile;
		public String videoUrl;

		public boolean isCached() {

			File ssi = new File(cacheFile + ".ssi");
			if (ssi.exists() && ssi.length() == 16) {
				return true;
			}
			return false;
		}

		public boolean clearCache() {
			boolean res = new File(cacheFile + ".ssi").delete();
			res = res && new File(cacheFile).delete();
			return res;
		}
	}

	static public HashMap<String, VideoCacheInfo> mVideoCache = null;
	static final String cacheName = "video_cache.dat";

	static public HashMap<String, VideoCacheInfo> getVideoCache() {
		if (mVideoCache != null) {
			return mVideoCache;
		}
		File file = new File(TyuFileUtils.getValidPath(), "config/" + cacheName);
		file.getParentFile().mkdirs();
		Object obj = TyuObjectSerilizer.readObject(file);
		if (obj != null) {
			mVideoCache = (HashMap<String, VideoCacheInfo>) obj;
		} else {
			mVideoCache = new HashMap<String, TyuShinningData.VideoCacheInfo>();
		}
		return mVideoCache;
	}

	static public void saveVideoCache() {
		File file = new File(TyuFileUtils.getValidPath(), "config/" + cacheName);
		file.getParentFile().mkdirs();
		if (mVideoCache != null) {
			TyuObjectSerilizer.writeObject(mVideoCache, file.getAbsolutePath());
		}
	}

	static final String[] fake_names = { "梦琪", "忆柳", "之桃", "慕青", "问兰", "尔岚",
			"元香", "初夏", "沛菡", "傲珊", "曼文", "乐菱", "痴珊", "恨玉", "惜文", "香寒", "新柔",
			"语蓉" };

	static public String getFakeAuthorName(String media_url) {
		if (TextUtils.isEmpty(media_url)) {
			return fake_names[0];
		}
		int len = media_url.length();
		return fake_names[len % fake_names.length];
	}
}

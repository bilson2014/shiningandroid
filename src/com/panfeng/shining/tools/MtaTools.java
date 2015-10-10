package com.panfeng.shining.tools;

import java.util.Properties;

import android.content.Context;
import android.util.Log;

import com.tencent.stat.StatService;

public class MtaTools {

	static Properties prop = new Properties();

	/*
	 * 分享
	 */
	public static void MtaShare(Context ctx, String type, String videoID) {

		prop.setProperty("type", type);
		prop.setProperty("share_id", videoID);
		StatService.trackCustomKVEvent(ctx, "share", prop);
	}

	/*
	 * 收藏
	 */
	public static void MtaSave(Context ctx, String videoID) {

		Properties prop = new Properties();
		prop.setProperty("save_Id", videoID);
		StatService.trackCustomKVEvent(ctx, "saveVideo", prop);
	}

	/*
	 * 设置
	 */
	public static void MtaSet(Context ctx, String videoID) {
		prop.setProperty("video_id", videoID);
		StatService.trackCustomKVEvent(ctx, "set_video", prop);

	}
	

	/*
	 * 本地视频设置
	 */
	public static void MtaLocalVideo(Context ctx) {
		StatService.trackCustomEvent(ctx, "localVideo_check", "");

	}
	
	
	
	
	
	/*
	 * 引导设置
	 */
	public static void MtaAutoSet(Context ctx) {
		
		StatService.trackCustomEvent(ctx, "auto_btn", "");

	}

	/**
	 * 选择音乐
	 * 
	 * @param ctx
	 * @param videoID
	 */

	public static void MtaMusic(Context ctx, String name) {

		prop.setProperty("music_name", name + "");
		StatService.trackCustomKVEvent(ctx, "music", prop);

	}

	/**
	 * 上传
	 * 
	 * @param ctx
	 * @param videoID
	 */

	public static void Sel_UpVideo(Context ctx, String videoname) {

		prop.setProperty("sel_up_name", videoname);
		StatService.trackCustomKVEvent(ctx, "sel_upVideo", prop);

	}

	/**
	 * 自拍设置
	 * 
	 * @param ctx
	 * @param videoID
	 */

	public static void Sel_SetVideo(Context ctx, String videoname) {

		prop.setProperty("sel_videoName", videoname);
		StatService.trackCustomKVEvent(ctx, "sel_setVideo", prop);

	}
	
	
	/**
	 * 接电话次数
	 * 
	 * @param ctx
	 * @param videoID
	 */

	public static void answer(Context ctx) {

		prop.setProperty("count", "");
		StatService.trackCustomKVEvent(ctx,"answer", prop);

	}
	
	public static void callIn(Context ctx,String time) {

		Properties props = new Properties();
		props.setProperty("time", time);
		StatService.trackCustomKVEvent(ctx,"callIn", props);
		Log.i("slwslt", "timecallIn"+time);

	}
	

	
	

	

	
	
	
	

}

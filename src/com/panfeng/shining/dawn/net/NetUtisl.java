package com.panfeng.shining.dawn.net;

import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestData;
import in.srain.cube.request.RequestHandler;
import in.srain.cube.request.SimpleRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import tyu.common.net.TyuDefine;
import android.util.Log;

import com.panfeng.shining.interfaces.getResouceInterface;

/**
 * 
 * @author dawn
 * 
 */
public final class NetUtisl implements getResouceInterface {

	@Override
	public void getValues(RequestHandler<JsonData> requestHandler,
			int currPageID, int size, int startID, String type) {
		Log.e("xyz", "currPageID:" + currPageID + "%%size:" + size
				+ " %%startID:" + startID+"  "+type);
		if (type == null)
			reverse(requestHandler, currPageID, size, startID);
		else
			reverse(requestHandler, currPageID, size, type);
	}

	/**
	 * 每日鲜
	 * 
	 * @param handler
	 * @param currpageid
	 * @param size
	 * @param startId
	 * @return
	 */
	private void reverse(RequestHandler<JsonData> requestHandler,
			final int currpageid, final int size, final int startId) {

		Log.e("xyz", "开始中" + currpageid + "" + size + " " + startId);
		String url = String.format(TyuDefine.HOST
				+ "smc/get_media_base_new?page_id=%d&page_size=%d&vId=%d",
				currpageid, size, startId);
		sendrequest(requestHandler, url, null);
	}

	/**
	 * 分类
	 * 
	 * @param handler
	 * @param currpageid
	 * @param size
	 * @param type
	 * @return
	 */
	private void reverse(final RequestHandler<JsonData> requestHandler,
			final int currpageid, final int size, String type) {

		String url = TyuDefine.HOST + "smc/search_media_base_2";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page_id", (currpageid));
		map.put("page_size", size );
		try {
			//map.put("key", URLEncoder.encode(type, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("key", type);
		sendrequest(requestHandler, url, map);
	}

	private void sendrequest(RequestHandler<JsonData> requestHandler,
			String url, Map<String, Object> map) {
		Log.e("xyz", "开始中 sendrequest");

		SimpleRequest<JsonData> request = new SimpleRequest<JsonData>(
				requestHandler);
		RequestData requestData = request.getRequestData();
		if (map != null && map.size() > 0)
			requestData.addPostData(map);
		
		requestData.setRequestUrl(url);
		request.send();
	}

	public void getLunBo(final RequestHandler<JsonData> requestHandler) {
		String url = TyuDefine.HOST + "smc/getVideoSortList";
		sendrequest(requestHandler, url, null);
	}
	
	public void getPaiHang(final RequestHandler<JsonData> requestHandler) {
		String url = TyuDefine.HOST + "smc/get_media_base_new?page_id=1&page_size=30";
		sendrequest(requestHandler, url, null);
	}
	

}

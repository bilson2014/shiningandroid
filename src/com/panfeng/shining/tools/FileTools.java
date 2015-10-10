package com.panfeng.shining.tools;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import com.panfeng.shining.TyuApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

public class FileTools {

	static final int needSize = 20;

	// 文件夹大小
	public static long getFolderSize(java.io.File file) {

		long size = 0;
		try {
			java.io.File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);

				} else {
					size = size + fileList[i].length();

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return size/1048576;
		return size;
	}

	/**
	 * 删除
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
		if (!TextUtils.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				if (file.isDirectory()) {// 处理目录
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFolderFile(files[i].getAbsolutePath(), true);
					}
				}
				if (deleteThisPath) {
					if (!file.isDirectory()) {// 如果是文件，删除
						file.delete();
					} else {// 目录
						if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
							file.delete();
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static boolean hasSensor() {

		String service_name = Context.SENSOR_SERVICE;
		SensorManager sensorManager = (SensorManager) TyuApp.applicationContext
				.getSystemService(service_name);
		List<Sensor> sensors = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);

		for (int i = 0; i < sensors.size(); i++) {

			Log.i("slwslt", sensors.get(i) + "");

		}

		return false;
	}

	public static Pair<Integer, Integer> getVideoSize(String paths) {

		int high;
		int width;

		try {

			MediaMetadataRetriever media = new MediaMetadataRetriever();
			media.setDataSource(paths);
			Bitmap bitmap = media.getFrameAtTime();

			high = bitmap.getHeight();
			width = bitmap.getWidth();
			bitmap.recycle();
			bitmap = null;
			return new Pair<Integer, Integer>(high, width);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// if(high>width){
		// return new Pair<Integer, Integer>(high, width);
		// }
		//
		// else{
		//
		// return new Pair<Integer, Integer>(width, high);
		// }
		
		return new Pair<Integer, Integer>(640, 480);

	}

	@SuppressWarnings("unchecked")
	public static Pair<Double, Double> getRealSize(String paths, double secW,
			double secH) {
		double secWidth = secW;
		double secHeight = secH;
		double vHeight = FileTools.getVideoSize(paths).first;
		double vWidth = FileTools.getVideoSize(paths).second;
		double realHeight = 0;
		double realWidth = 0;
		Pair<Double, Double> pair = null;

		int ourVideo = (int) ((vHeight / vWidth) * 100); // 100
		int secSize = (int) ((secHeight / secWidth) * 100);// 100
		Log.i("TabHost", secHeight + "secHeight" + secWidth + "secWidth"
				+ vHeight + "height" + vWidth + "width" + ourVideo + "ourVideo");

		Log.i("xyz", ourVideo + "ourVideo" + secSize + "secSize");
		// 如果取不到屏幕或者视频
		if ((secWidth < 1 && secHeight < 1) || (vHeight < 1 && vWidth < 1)) {

			if (secWidth < 1 || secHeight < 1) {

				pair = new Pair<Double, Double>(vHeight, vWidth);
			}

			else if (vHeight < 1 || vWidth < 1) {

				pair = new Pair<Double, Double>(secHeight, secWidth);
			}
		}
		// 都没取到的时候
		else {
			pair = new Pair<Double, Double>(640d, 480d);
		}
		int bizhi = secSize - ourVideo;
		// 允许拉伸范围

		if (Math.abs(bizhi) <= needSize) {
			return new Pair<Double, Double>(secHeight, secWidth);
		}
		// 不拉伸处理
		else {
			// 视频更胖
			if (bizhi > needSize) {
				realHeight = (secWidth / vWidth) * vHeight;
				realWidth = secWidth;
				pair = new Pair<Double, Double>(realHeight, realWidth);
			}
			// 视频更瘦
			else {
				realHeight = secHeight;
				realWidth = (secHeight / vHeight) * vWidth;
				pair = new Pair<Double, Double>(realHeight, realWidth);
			}
		}
		return pair;
	}

}

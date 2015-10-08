package com.panfeng.shining.slw.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import android.os.Environment;

/**
 * file 工具
 * 
 * @author dawn
 * 
 */
public class FileUtils {

	/**
	 * 
	 * 获取sd卡路径
	 */
	public static String getSdcardPaht() {
		String sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();// 获取跟目录
		}
		return sdDir;
	}

	public static boolean stringNotEmpty(final String src) {
		if (src != null && !src.equals(""))
			return true;
		else
			return false;
	}
	public static boolean listNotEmpty(final List list) {
		if (list != null && list.size()>0)
			return true;
		else
			return false;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param dirPath
	 *            文件夹路径
	 */
	public static void checkAndCreateDir(final String dirPath) {
		File file = new File(dirPath);
		if (!file.exists())
			file.mkdirs();
	}

	/**
	 * 读文件
	 * 
	 * @param FilePath
	 *            文件路径包括文件名
	 * @return value
	 */
	public static String readFile(final String FilePath) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			if (!stringNotEmpty(FilePath))
				return null;
			File file = new File(FilePath);
			if (!file.canRead() || !file.exists())
				return null;
			FileReader frFileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(frFileReader);
			String line;
			while ((line = br.readLine()) != null) {
				stringBuffer.append(line.trim() + "\n");
			}
			br.close();
			frFileReader.close();
		} catch (Exception e) {
			LogUtils.writeErrorLog("读取文件", "", e,null);
			//e.printStackTrace();
		}
		return stringBuffer.toString();
	}

	/**
	 * 写文件
	 * 
	 * @param FilePath
	 *            文件路径包括文件名
	 * @param value
	 *            内容
	 * @return 是否写入成功
	 */
	public static boolean writeFile(final String FilePath, final String value) {
		try {
			if (!stringNotEmpty(FilePath))
				return false;
			File file = new File(FilePath);
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(value.trim() + "\n");
			bw.flush();
			bw.close();
			fw.close();
			return true;
		} catch (Exception e) {
			//LogUtils.writeErrorLog("写入文件", "", e,null);
			//e.printStackTrace();
		}
		return false;
	}

	/**
	 * 视频名转换图片名
	 * @param VideoName
	 * @return
	 */
	public static  String videoNameConvertImageName(String VideoName){
		return VideoName.substring(0, VideoName.lastIndexOf('.')) + ".jpg";
	}
}

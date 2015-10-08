package tyu.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

public class TyuFileUtils {
	public static final String ERR_TAG = "FileOption.java";

	public static String local_diary_dir = "local_image";
	public static String net_diary_dir = "net_diary";
	
	

	
	//获取本地头像图片
	static public String getLocalDiaryPath() {
		File dir = new File(getValidPath(), local_diary_dir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir.getAbsolutePath();

	}
       
	static public String getNetDiaryPath() {
		File dir = new File(getValidPath(), net_diary_dir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir.getAbsolutePath();

	}

	static public String getValidPath() {
		Context context = TyuContextKeeper.getInstance();
		if (isExistSDCard()) {
			String path = Environment.getExternalStorageDirectory().getPath()
					+ "/" + context.getPackageName();
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return path;
		} else {
			String path = context.getFilesDir().getAbsolutePath();
			return path;
		}

	}

	static public boolean isExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	static public boolean saveImg(Bitmap aBitmap, String aFile,
			Bitmap.CompressFormat aFormat) {
		return saveImg(aBitmap, aFile, aFormat,100);
	}
	
	/**
	 * 压缩
	 * @param aBitmap
	 * @param aFile
	 * @param aFormat
	 * @param quality
	 * @return
	 */
	static public boolean saveImg(Bitmap aBitmap, String aFile,
			Bitmap.CompressFormat aFormat,int quality) {

		try {
			File file = new File(aFile);
			file.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(aFile);
			aBitmap.compress(aFormat, quality, fos);// (0-100)压缩文件
			fos.flush();
			fos.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 复制文件
	 */
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		sourceFile.getParentFile().mkdirs();
		targetFile.getParentFile().mkdirs();
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	/**
	 * 复制文件
	 */
	public static void copyFile(String sFile, String tFile) throws IOException {
		File sourceFile = new File(sFile);
		File targetFile = new File(tFile);

		copyFile(sourceFile, targetFile);
	}

	/**
	 * 复制文件夹
	 */
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();

		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	/**
	 * 删除文件
	 */
	public static void delFile(String strFileName) {
		File myFile = new File(strFileName);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	/**
	 * 检查文件或目录是否存在
	 */
	public static boolean isExist(String strFileName) {
		File myFile = new File(strFileName);
		return myFile.exists();
	}

	/**
	 * 在指定目录查找相应后缀名的文件
	 */
	public static void getFiles(String folder, String ext,
			ArrayList<String> files) {
		File[] file = (new File(folder)).listFiles();
		for (File file2 : file) {
			if (file2.getName().endsWith(ext)) {
				files.add(file2.getName());
			}
		}
	}

	static public boolean copyFileFromRaw(Context aContext, int aSrcId,
			String aTar) {
		File src = new File(aTar);
		if (src.exists()) {
			return true;
		}
		AssetManager assetManager = aContext.getAssets();
		try {
			InputStream inputStream = aContext.getResources().openRawResource(
					aSrcId);

			OutputStream outStream = new FileOutputStream(aTar);
			BufferedInputStream bin = null;
			BufferedOutputStream bout = null;

			bin = new BufferedInputStream(inputStream);

			bout = new BufferedOutputStream(outStream);

			byte[] b = new byte[1024];

			int len = bin.read(b);

			while (len != -1) {
				bout.write(b, 0, len);
				len = bin.read(b);
			}

			bin.close();
			bout.close();

		} catch (IOException e) {

			return true;
		}
		return false;
	}

	static public boolean copyDirsFromAssets(Context aContext, String aSrc,
			String aTar) {
		AssetManager asm = aContext.getAssets();
		try {
			String[] ret = asm.list(aSrc);
			for (int j = 0; j < ret.length; j++) {
				String name = aSrc + "/" + ret[j];
				// 是一个文件
				if (name.contains(".")) {
					copyFileFromAssets(aContext, name, aTar + "/" + name);
				} else {
					// 是一个文件夹,递归调用
					copyDirsFromAssets(aContext, name, aTar);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	static public boolean copyFileFromAssets(Context aContext, String aSrc,
			String aTar) {
		File src = new File(aTar);
		if (src.exists()) {
			return true;
		}

		try {
			src.getParentFile().mkdirs();
			src.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		AssetManager assetManager = aContext.getAssets();
		try {
			InputStream inputStream = assetManager.open(aSrc);

			OutputStream outStream = new FileOutputStream(aTar);
			BufferedInputStream bin = null;
			BufferedOutputStream bout = null;

			bin = new BufferedInputStream(inputStream);

			bout = new BufferedOutputStream(outStream);

			byte[] b = new byte[1024];

			int len = bin.read(b);

			while (len != -1) {
				bout.write(b, 0, len);
				len = bin.read(b);
			}

			bin.close();
			bout.close();

		} catch (IOException e) {

			return true;
		}
		return false;
	}

	public static Map<String, Drawable> getZip(String path) {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zipFile.entries(); // 获取zip文件中的目录及文件列表
		ZipEntry entry = null;
		Map<String, Drawable> drawable = new HashMap<String, Drawable>();
		while (e.hasMoreElements()) {
			entry = e.nextElement();
			if (!entry.isDirectory()) {
				// 如果文件不是目录，则添加到列表中
				try {
					drawable.put(
							entry.getName(),
							Drawable.createFromStream(
									zipFile.getInputStream(entry), null));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		try {

			zipFile.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return drawable;
	}

	public static void deleteTempNetImg(String packageName) {
		File file = new File("/sdcard/com.anguanjia.safe/guanjiantempFile/"
				+ packageName + ".png");
		if (file.exists())
			file.delete();
		file = new File("/data/data/com.anguanjia.safe/guanjiantempFile/"
				+ packageName + ".png");
		if (file.exists())
			file.delete();
		file = new File("/data/data/com.anguanjia.safe/guanjiantempFile/"
				+ packageName + ".zip");
		if (file.exists())
			file.delete();
		file = new File("/data/data/com.anguanjia.safe/guanjiantempFile/"
				+ packageName + ".zip");
		if (file.exists())
			file.delete();
	}

	/**
	 * 删除一个文件夹（不管里面有没有文件都可以删除）
	 */
	public static void deleteDirectory(String DirectoryName) {
		File file = new File(DirectoryName);

		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length && files != null; i++) {

				files[i].delete();
			}
			file.delete();
		}

	}
	
	
	//v2 清除缓存
	 public static void deleteCache(File file) {  
	        if (file.isFile()) {  
	            file.delete();  
	            return;  
	        }  
	  
	       if(file.isDirectory()){  
	            File[] childFiles = file.listFiles();  
         if (childFiles == null || childFiles.length == 0) {  
	                 file.delete();  
	               return;  
       }  
	
	           for (int i = 0; i < childFiles.length; i++) {  
	               deleteCache(childFiles[i]);  
	           }  
	            file.delete();  
	        }  
	    } 
	
	

	/**
	 * 删除一个文件夹中的内容,但保留此文件夹
	 */
	public static void deleteDirectoryContent(String DirectoryName) {
		File file = new File(DirectoryName);

		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length && files != null; i++) {
				files[i].delete();
			}
		}

	}

	public static String readAssetFile(String aFileName) {

		Context context = TyuContextKeeper.getInstance();
		byte[] buffer = new byte[1024];
		int read_len = 0;
		String res = null;
		try {
			BufferedInputStream bis = new BufferedInputStream(context
					.getAssets().open(aFileName));

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = bis.read(buffer);

			while (len != -1) {
				bos.write(buffer, 0, len);
				len = bis.read(buffer);
			}
			res = new String(bos.toByteArray());
			bis.close();
			bis = null;
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public static byte[] readFile(String aFileName) {
		File file = new File(aFileName);
		int file_len = (int) file.length();

		byte[] bs = new byte[file_len + 100];
		int read_len = 0;
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			read_len = bis.read(bs);
			bis.close();
			bis = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (read_len == file_len) {
			return bs;
		} else {
			bs = null;
			return bs;
		}
	}

	public static String readDataFromZip(File aFile) {
		String res = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			FileInputStream input = new FileInputStream(aFile);
			while ((len = input.read(buffer)) >= 0) {
				// sb.append(new String(buffer, 0, len,"UTF-8"));
				baos.write(buffer, 0, len);
			}
			// String res = new String(baos.toByteArray(), 0, baos.size());
			ZipInputStream zipin = new ZipInputStream(new ByteArrayInputStream(
					baos.toByteArray()));
			baos.close();
			ZipEntry entry;
			while ((entry = zipin.getNextEntry()) != null) {

				// long length = entry.getSize();
				// zipin.re
				// byte[] tmp_zbuffer = new byte[(int) length+100];
				// len = zipin.read(tmp_zbuffer);
				// res = new String(tmp_zbuffer, 0, len);
				baos = new ByteArrayOutputStream();
				while ((len = zipin.read(buffer)) >= 0) {
					baos.write(buffer, 0, len);
				}
				res = new String(baos.toByteArray(), 0, baos.size(),
						TyuChangeCharset.GBK);

				baos.close();
				break;
			}
			zipin.close();
			input.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return res;
	}

	public static File dump2FileZip(String data) {
		File res = null;
		Context context = TyuContextKeeper.getInstance();
		res = new File(context.getFilesDir(), System.currentTimeMillis()
				+ ".zip");
		try {
			if (!res.getParentFile().exists())
				res.getParentFile().mkdirs();
			ZipOutputStream zout = new ZipOutputStream(
					new FileOutputStream(res));
			byte[] resss = data.getBytes();
			ZipEntry entry = new ZipEntry("resp");
			entry.setSize(resss.length);
			zout.putNextEntry(entry);
			zout.write(resss);
			zout.flush();
			zout.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = null;
		}

		return res;
	}

	// 存储字符串数据到文件
	public static void dumpToFile(String aFileName, String data) {
		try {
			if (data == null || aFileName == null)
				return;

			new File(aFileName).delete();

			OutputStream outStream = new FileOutputStream(aFileName);

			BufferedOutputStream bout = null;

			bout = new BufferedOutputStream(outStream);

			byte[] b = data.getBytes();

			int len = b.length;

			bout.write(b, 0, len);

			bout.close();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// 加载文件为字符串
	public static String loadFromFile(String aFileName) {
		String res = null;
		File file = new File(aFileName);
		if (!file.exists())
			return null;

		try {
			InputStream input = new FileInputStream(aFileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];

			int len = input.read(b);

			while (len != -1) {
				baos.write(b, 0, len);
				len = input.read(b);
			}
			res = baos.toString("utf-8");
			baos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	// /**
	// * 获取文件扩展名
	// *
	// * @param fileName
	// * @return
	// */
	// public static String getFileFormat(String fileName) {
	// if (StringUtils.isBlank(fileName))
	// return "";
	//
	// int point = fileName.lastIndexOf('.');
	// return fileName.substring(point + 1).toLowerCase();
	// }

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	public static int getApkVersion(String apk_path) {
		try {
			PackageManager pm = TyuContextKeeper.getInstance()
					.getPackageManager();
			PackageInfo info = pm.getPackageArchiveInfo(apk_path,
					PackageManager.GET_ACTIVITIES);
			if (info != null) {
				Log.i("test", "info.versionCode = " + info.versionCode);
				return info.versionCode;
			}
			return 0;
		} catch (Exception e) {
			Log.i("test", "这个地方跑出异常" + e.getMessage());
			return 0;
		}

	}
}

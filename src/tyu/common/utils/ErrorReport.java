package tyu.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import android.util.Log;

public class ErrorReport {
	public static boolean ENABLE = true;

	public static final String ERR_TAG = "ErrorReport.java";

	public static final String LOG_TYPE_EXCEPTION = "Exception";

	public static final String LOG_TYPE_CRASH_EXCEPTION = "Crash_Exception";

	public static final String INDEX_NAME = "Err_Index.log";

	public static final String INFO_NAME = "Err_Info.log";

	public static final String ZIP_FILE = "Error.zip";

	public static final String CLASS_INDEX_FIELD_NAME = "ERR_TAG";

	public static final int ERR_LOG_VERSION = 1;

	public static final String SEND_URL = "";

	public static final String FilePath = TyuFileUtils.getValidPath() + "/error/";

	private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	private static final UncaughtExceptionHandler sDefUncaughtExceptionHandler = Thread
			.getDefaultUncaughtExceptionHandler();

	/**
	 * 日志概要输出流
	 */
	// private static FileOutputStream errorIndexOS = null;

	/**
	 * 日志详情输出流
	 */
	// private static FileOutputStream errorInfoOS = null;

	public static final boolean init() {

		if (!ENABLE) {
			return false;
		}

		try {
			if (sDefUncaughtExceptionHandler == Thread
					.getDefaultUncaughtExceptionHandler()) {
				Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread thread, Throwable ex) {
						outException(ex, LOG_TYPE_CRASH_EXCEPTION);
						sDefUncaughtExceptionHandler.uncaughtException(thread,
								ex);
					}
				});
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		outIndexString(DATA_FORMAT.format(new Date(System.currentTimeMillis()))
				+ "|ERR_LOG_VERSION: " + ERR_LOG_VERSION);

		return true;
	}

	/**
	 * 输出异常信息
	 * 
	 * @param e
	 *            异常信息
	 */
	public static final void outException(Throwable e) {
		if (!ENABLE) {
			return;
		}
		Log.e("ErrorReport", e.getClass().getName());
		outException(e, null);
	}

	/**
	 * 输出异常信息
	 * 
	 * @param e
	 *            异常信息
	 * @param type
	 *            异常类新
	 */
	public static final void outException(Throwable e, String type) {
		if (!ENABLE) {
			return;
		}
		if (e == null) {
			return;
		}
		if (type == null) {
			type = LOG_TYPE_EXCEPTION;
		}
		if (!init()) {
			return;
		}
		long startIndex = -1;

		outInfoString(e.toString());
		StackTraceElement[] stack = e.getStackTrace();
		for (StackTraceElement element : stack) {
			outInfoString("\tat " + element + "\t, Index:"
					+ getClassIndex(element));
		}

		StackTraceElement[] parentStack = stack;
		Throwable throwable = e.getCause();
		while (throwable != null) {
			outInfoString("Caused by: ");
			outInfoString(throwable.toString());
			StackTraceElement[] currentStack = throwable.getStackTrace();
			int duplicates = countDuplicates(currentStack, parentStack);
			for (int i = 0; i < currentStack.length - duplicates; i++) {
				outInfoString("\tat " + currentStack[i] + "\t, Index:"
						+ getClassIndex(currentStack[i]));
			}
			if (duplicates > 0) {
				outInfoString("\t... " + duplicates + " more");
			}
			parentStack = currentStack;
			throwable = throwable.getCause();
		}
		long endIndex = -1;

		StackTraceElement firstElement = null;
		if (e.getStackTrace().length > 0) {
			firstElement = e.getStackTrace()[0];
		}
		outIndexString(DATA_FORMAT.format(new Date(System.currentTimeMillis()))
				+ "|" + e.toString() + "|" + firstElement == null ? "NULL"
				: firstElement + "|" + type + "|" + startIndex + "|" + endIndex);
	}

	// public static void delLogFiles() {
	// if (!ENABLE) {
	// return;
	// }
	// getIndexLogFile().delete();
	// getInfoLogFile().delete();
	// }

	private static String getClassIndex(StackTraceElement element) {
		if (!ENABLE) {
			return null;
		}
		Class<?> clazz = null;
		try {
			clazz = Class.forName(element.getClassName());
		} catch (ClassNotFoundException ex) {
			return "UnKnowClassIndex";
		}
		while (true) {
			Class<?> declaringClass = clazz.getDeclaringClass();
			if (declaringClass == null) {
				break;
			} else {
				clazz = declaringClass;
			}
		}
		Field indexField = null;
		try {
			indexField = clazz.getField(CLASS_INDEX_FIELD_NAME);
		} catch (Exception e) {
			return "UnKnowClassIndex";
		}
		String classIndex = null;
		try {
			classIndex = (String) indexField.get(null);
		} catch (Exception e) {
			classIndex = "UnKnowClassIndex";
		}
		return classIndex;
	}

	/**
	 * Counts the number of duplicate stack frames, starting from the end of the
	 * stack.
	 * 
	 * @param currentStack
	 *            a stack to compare
	 * @param parentStack
	 *            a stack to compare
	 * 
	 * @return the number of duplicate stack frames.
	 */
	private static int countDuplicates(StackTraceElement[] currentStack,
			StackTraceElement[] parentStack) {
		if (!ENABLE) {
			return -1;
		}
		int duplicates = 0;
		int parentIndex = parentStack.length;
		for (int i = currentStack.length; --i >= 0 && --parentIndex >= 0;) {
			StackTraceElement parentFrame = parentStack[parentIndex];
			if (parentFrame.equals(currentStack[i])) {
				duplicates++;
			} else {
				break;
			}
		}
		return duplicates;
	}

	private static void outIndexString(String outStr) {
		if (!ENABLE) {
			return;
		}
		try {
			// errorIndexOS.write(outStr.getBytes());
			// errorIndexOS.write("\r\n".getBytes());
			// errorIndexOS.flush();
			File index = getIndexLogFile();
			FileWriter fw = new FileWriter(index, true);
			fw.write(outStr);
			fw.write("\r\n");
			fw.flush();
			fw.close();
		} catch (Exception e) {
		}
	}

	public static void outInfoString(String outStr) {
		if (!ENABLE) {
			return;
		}
		try {
			File info = getInfoLogFile();
			FileWriter fw = new FileWriter(info, true);
			fw.write(outStr);
			fw.write("\r\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
		}
	}

	/**
	 * 获得概要日志文件地址
	 * 
	 * @return 概要日志文件地址
	 */
	public static final File getIndexLogFile() {
		if (!ENABLE) {
			return null;
		}
		// return SafeApplication.getApp().getFileStreamPath(INDEX_NAME);
		File dirFile = new File(FilePath);
		dirFile.mkdirs();
		File indexLogFile = new File(dirFile, INDEX_NAME);
		if (!indexLogFile.exists()) {
			try {
				indexLogFile.createNewFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return indexLogFile;
	}

	/**
	 * 获得详情日志文件地址
	 * 
	 * @return 概要日志文件地址
	 */
	public static final File getInfoLogFile() {
		if (!ENABLE) {
			return null;
		}
		// return SafeApplication.getApp().getFileStreamPath(INFO_NAME);
		File dirFile = new File(FilePath);
		dirFile.mkdirs();
		File infoLogFile = new File(dirFile, INFO_NAME);
		if (!infoLogFile.exists()) {
			try {
				infoLogFile.createNewFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return infoLogFile;
	}

	/**
	 * 获得日志压缩文件
	 * 
	 * @return 日志压缩文件地址
	 */
	public static final File getZipFile() {
		if (!ENABLE) {
			return null;
		}
		File dirFile = new File(FilePath);
		dirFile.mkdirs();
		File indexLogFile = new File(dirFile, ZIP_FILE);
		return indexLogFile;
	}

	/**
	 * @Title: send
	 * @Description: 发送错误日志到服务器
	 * @param @param context
	 * @return void
	 * @author anony_god
	 * @date 2013-7-1 下午01:33:33
	 * @version V1.0
	 */

	public static final void post_error() {
		if (!ENABLE)
			return;
		if (getInfoLogFile().length() == 0) {
			ErrorReport.init();
			// 没有错误日志
			return;
		}
		new Thread() {
			@Override
			public void run() {
				HashMap<String, Object> maps = new HashMap<String, Object>();
				maps.put("error_index", getIndexLogFile());
				maps.put("error_info", getInfoLogFile());
				String url = TyuDefine.HOST + "cc/post_error";
				String res = TyuHttpClientUtils.postMutiPart(url, maps, null);
				if ("ok".equals(res)) {
					getIndexLogFile().delete();
					getInfoLogFile().delete();
				}
				ErrorReport.init();
			};
		}.start();
		// 将文件上传服务器
	}
}

package com.panfeng.shining;

import in.srain.cube.Cube;
import in.srain.cube.cache.CacheManagerFactory;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.image.impl.DefaultImageLoadHandler;
import in.srain.cube.request.RequestCacheManager;
import in.srain.cube.request.RequestManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tyu.common.utils.ErrorReport;
import tyu.common.utils.TyuContactUtils;
import tyu.common.utils.TyuContextKeeper;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.easemob.EMCallBack;
import com.easemob.helpdeskdemo.DemoHXSDKHelper;
import com.easemob.helpdeskdemo.domain.User;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.panfeng.shining.dawn.image.DemoRequestProxy;
import com.panfeng.shining.dawn.image.DuiTangImageReSizer;
import com.panfeng.shining.service.FVCallPlayerService;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.slw.utils.FileUtils;
import com.panfeng.shining.slw.utils.LogUtils;
import com.panfeng.shinning.R;

public class TyuApp extends Application {
	static {
		// OpenCVLoader.initDebug();
		// System.loadLibrary("opencv_java");
		// TyuOpenCvTools.load();
	}

	private static DbUtils dbUtils;

	private static BitmapUtils bitmapUtils;

	public static Context applicationContext;

	public static boolean VideoShwo = false;

	public static boolean TopWindowsShow = false;

	public static boolean hasinitMediapalyer = false;

	public static String lastVideo = "";

	public static boolean needCreate = true;

	private static TyuApp instance;
	// login user name
	public final String PREF_USERNAME = "username";
	public int codecount;
	public List<byte[]> originalData = new ArrayList<byte[]>();
	public List<byte[]> originalData_back = new ArrayList<byte[]>();

	static String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	static String ImagePath = pathString + File.separator
			+ "com.panfeng.shinning" + File.separator + "cacheImage";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		applicationContext = getApplicationContext();
		//createDBConnection();
		initbitmapUtils();
		FileUtils.checkAndCreateDir(DefindConstant.saveDownaLoadImagePath);

		System.setProperty("http.keepAlive", "false");
		TyuContextKeeper.init(this, new Handler());
		ErrorReport.init();
		// 初始化imageloader
		initImageLoader(this);
		startService(new Intent(this.getApplicationContext(),
				FVCallPlayerService.class));

		TyuContactUtils.testApi();

		applicationContext = this;
		instance = TyuApp.this;
		hxSDKHelper.onInit(applicationContext);

		
		Cube.onCreate(this);
		initImageLoader();

		CacheManagerFactory.initDefaultCache(this, null, -1, -1);
	}


	private void initImageLoader() {

		// File path1 =
		// Environment.getExternalStoragePublicDirectory("cube/test1/a/b/c");
		ImageLoaderFactory.customizeCache(this,
		// memory size
				1024 * 10,
				// disk cache directory
				// path1.getAbsolutePath(),
				null,
				// disk cache size
				ImageLoaderFactory.DEFAULT_FILE_CACHE_SIZE_IN_KB);

		DefaultImageLoadHandler handler = new DefaultImageLoadHandler(this);
		handler.setErrorResources(R.drawable.ifnot);
		ImageLoaderFactory.setDefaultImageLoadHandler(handler);
		ImageLoaderFactory.setDefaultImageReSizer(DuiTangImageReSizer
				.getInstance());
		
	}

	/**
	 * 利用Application制造单例DbUtils
	 * 
	 * @return DbUtils
	 */
	public static DbUtils getDbConnection() {
		if (dbUtils == null) {
			createDBConnection();
		}
		return dbUtils;
	}

	public static BitmapUtils getBitmapUtils() {
		if (bitmapUtils == null) {
			initbitmapUtils();
		}
		return bitmapUtils;
	}

	/**
	 * 打开&创建数据库
	 */
	private static void createDBConnection() {
		try {
			String dbPath = FileUtils.getSdcardPaht();
			Log.i("slt", dbPath);
			if (!FileUtils.stringNotEmpty(dbPath))
				throw new NullPointerException("内存卡不存在");
			dbPath = DefindConstant.saveDatabasePath;
			dbUtils = DbUtils.create(applicationContext, dbPath,
					"shiningDatabase.db");
			dbUtils.configAllowTransaction(true);
			dbUtils.configDebug(true);
		} catch (Exception e) {
			// e.printStackTrace();
			LogUtils.writeErrorLog("创建数据库", "", e, null);
		}
	}

	private static void initbitmapUtils() {
		bitmapUtils = new BitmapUtils(applicationContext);
		bitmapUtils.configDefaultLoadingImage(R.drawable.ifnot);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.ifnot);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
	}

	public static DisplayImageOptions getCommonConfig() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		return options;
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 1)
				.threadPoolSize(5)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(800)
				// 缓存的文件数量

				.discCache(new UnlimitedDiscCache(new File(ImagePath)))
				// 自定义缓存路径

				.memoryCacheSize(1024 * 1024 * 8)
				.memoryCache(new WeakMemoryCache()).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		ImageLoader.getInstance().handleSlowNetwork(true);
	}

	static public class TyuImageLoadingListener implements ImageLoadingListener {

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub
			Log.i("imageloader", "a====" + imageUri);

		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub

		}

	}

	public static TyuApp getInstance() {
		return instance;
	}

	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 * 
	 * @param user
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 * 
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(emCallBack);
	}

	// private String getAppName(int pID) {
	// String processName = null;
	// ActivityManager am = (ActivityManager)
	// this.getSystemService(ACTIVITY_SERVICE);
	// List l = am.getRunningAppProcesses();
	// Iterator i = l.iterator();
	// PackageManager pm = this.getPackageManager();
	// while (i.hasNext()) {
	// ActivityManager.RunningAppProcessInfo info =
	// (ActivityManager.RunningAppProcessInfo) (i.next());
	// try {
	// if (info.pid == pID) {
	// CharSequence c =
	// pm.getApplicationLabel(pm.getApplicationInfo(info.processName,
	// PackageManager.GET_META_DATA));
	// // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
	// // info.processName +"  Label: "+c.toString());
	// // processName = c.toString();
	// processName = info.processName;
	// return processName;
	// }
	// } catch (Exception e) {
	// // Log.d("Process", "Error>> :"+ e.toString());
	// }
	// }
	// return processName;
	// }

}

package com.panfeng.shining.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.camera.entity.ImageData;
import com.panfeng.shining.TyuApp;

import com.panfeng.shinning.R;

@SuppressLint("NewApi")
public class MySurface extends Activity implements SurfaceHolder.Callback,
		PreviewCallback {
	private String tag = "dawn", whatToDo;

	private SurfaceView sfv;
	private SurfaceHolder mySurfaceHolder = null;
	private Camera myCamera = null;
	private boolean isPreview = false;

	private Camera.Parameters parameters;
	private String audioPath = "";
	private Size size;
	private ExecutorService executorService;
	private int threadPoolSize = 20;
	// private int count=0;
	private static int imageCompressionRatio = 70;
	// 是否开始拍摄
	private boolean isShooting = false;
	private boolean isAudioPaly = false;
	// ---------------------
	private ReentrantLock reentrantLock = new ReentrantLock();
	private Condition concurrent = reentrantLock.newCondition();
	private LinkedBlockingQueue<ImageData> queueoriginalData;
	private List<byte[]> originalData;
	public static final int START_GENERATE = 1;
	public static final int STOP_GENERATE = 2;
	public static final int GET_FRAME = 3;
	private ProgressBar progressBar;
	private handlerslt handler;
	private TextView tv;
	private Button btnstart, cancle;
	private Button btndelete, change;
	private MediaPlayer mediaPlayer;
	private int loopcount = 0;
	private boolean hasPreview = true;
	double elseSize = 100;

	int elsePos;
	private int widths, heights, vHeight, vWidth;
	private WindowManager wm;
	Window window;
	int barHeight = 0;

	private int laywidth = 0;
	private int layheight = 0;
	private int slw = 0;
	private int xAdjust = 0;
	private int yAdjust = 0;
	private Context ctx = MySurface.this;
	private int surfaceViewWidth = 0;
	private int surfaceViewHeight = 0;
	double needPriWidth;
	double needPriHeight;

	// private int currentPacketCount=0;
	// private int currentPacketCountend=0;

	public Pair<Double, Double> priSize(double absSize, double widths) {

		// step1 . get all supported sizes & calculate absolute difference with
		// previewScale, then store in a list
		List<Size> supportedPreviewSizes = parameters
				.getSupportedPreviewSizes();

		for (int i = 0; i < supportedPreviewSizes.size(); i++) {
			Size size = supportedPreviewSizes.get(i);

			double priWidth = size.width;
			double priHeight = size.height;
			double relSize = (priWidth / priHeight);

			Log.i("lop", "priWidth" + priWidth);
			Log.i("lop", "priHeight" + priHeight);

			if (absSize == relSize && priHeight <= 720) {

				Log.i("slw", "priWidth" + priWidth);
				Log.i("slw", "priHeight" + priHeight);

				return new Pair<Double, Double>(priWidth, priHeight);

			}

		}

		if (widths <= 720) {
			// hasPreview = false;

		}

		else {

			for (int i = 0; i < supportedPreviewSizes.size(); i++) {
				Size size = supportedPreviewSizes.get(i);

				double priWidth = size.width;
				double priHeight = size.height;
				double relSize = (priWidth / priHeight);
				double needSize = Math.abs(absSize - relSize);

				Log.i("slw", "needSize=" + needSize);

				if (priHeight <= 720) {

					if (elseSize > needSize) {

						elseSize = needSize;
						elsePos = i;

					}
				}

				needPriWidth = supportedPreviewSizes.get(elsePos).width;
				needPriHeight = supportedPreviewSizes.get(elsePos).height;

			}
			// return new Pair<Double, Double>(needPriWidth, needPriHeight);

		}
		return new Pair<Double, Double>(needPriWidth, needPriHeight);

	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_v2);

		// if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
		//
		// Log.i("dbw", "open");
		// } else {
		// Log.i("dbw", "close");
		//
		// }
		//
		// boolean s = ViewConfiguration.get(ctx).hasPermanentMenuKey();

		// Log.i("slc", s + "");

		// hideNavigationBar();

		// toggleHideyBar();

		wm = this.getWindowManager();
		// getNavigationBarHeight();

		// Log.i("slc", getScreenHeight(ctx)+"");

		Size();

		laywidth = this.getWindowManager().getDefaultDisplay().getWidth();
		layheight = this.getWindowManager().getDefaultDisplay().getHeight();
		getratio();



		TyuApp application = ((TyuApp) getApplicationContext());
		queueoriginalData = new LinkedBlockingQueue<ImageData>();
		originalData = application.originalData;

		executorService = Executors.newFixedThreadPool(threadPoolSize);
		// audioPath =
		// Environment.getExternalStorageDirectory().getAbsolutePath()
		// + File.separator + "paomo.mp3";

		Intent intent = this.getIntent();
		audioPath = intent.getStringExtra("path");
		whatToDo = intent.getStringExtra("what");
		// audioPath = "android.resource://" + MySurface.this.getPackageName() +
		// "/"
		// + R.raw.paomo;

		sfv = (SurfaceView) findViewById(R.id.surfaceView);

		LayoutParams lp = sfv.getLayoutParams();
		lp.width = surfaceViewWidth;
		lp.height = surfaceViewHeight;
		sfv.setLayoutParams(lp);

		Log.i("dbw", "surfaceViewWidth" + surfaceViewWidth);
		Log.i("dbw", "surfaceViewHeight" + surfaceViewHeight);

		sfv.setX(xAdjust);
		sfv.setY(yAdjust);

		mySurfaceHolder = sfv.getHolder();
		mySurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
		mySurfaceHolder.addCallback(this);

		btnstart = (Button) findViewById(R.id.start);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		tv = (TextView) findViewById(R.id.textView1);
		btnstart.setOnTouchListener(touchListener);
		btndelete = (Button) findViewById(R.id.delete);
		btndelete.setOnClickListener(myonClickListener);
		change = (Button) findViewById(R.id.changeCamera);
		cancle = (Button) findViewById(R.id.cancle);

		new Thread(new ResourceAcquisition()).start();
		handler = new handlerslt();

		initMediaPlay(audioPath);
		DelClick();
		Cancle();

	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.heightPixels;
	}

	private void DelClick() {

		this.change.setOnClickListener(new ReButtonListener());

	}

	class ReButtonListener implements OnClickListener {

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {

			// try {
			// // myCamera.stopPreview();
			// //
			// // myCamera.release();
			// // myCamera = null;
			// // slw
			// // mySurfaceHolder = null;
			// // mySurfaceHolder = sfv.getHolder();
			// // mySurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
			// // mySurfaceHolder.addCallback(MySurface.this);
			// // myCamera.setPreviewDisplay(mySurfaceHolder);
			// //
			// // myCamera.startPreview();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

		}
	}

	private void Cancle() {

		this.cancle.setOnClickListener(new ReButtonListener5());

	}

	class ReButtonListener5 implements OnClickListener {

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {

			finish();
		}
	}

	// public void onWindowFocusChanged(boolean hasFocus) {
	// super.onWindowFocusChanged(hasFocus);
	// if( hasFocus ) {
	// hideNavigationBar();
	// }
	// }
	//
	//
	// public void hideNavigationBar() {
	// int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	// | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	// | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	// | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
	// | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar
	//
	// if( android.os.Build.VERSION.SDK_INT >= 19 ){
	// uiFlags |= 0x00001000; //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation
	// bars - compatibility: building API level is lower thatn 19, use magic
	// number directly for higher API target level
	// } else {
	// uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
	// }
	//
	// getWindow().getDecorView().setSystemUiVisibility(uiFlags);
	// }
	//
	//
	// public static boolean checkDeviceHasNavigationBar(Context activity) {
	//
	// //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
	// boolean hasMenuKey = ViewConfiguration.get(activity)
	// .hasPermanentMenuKey();
	// boolean hasBackKey = KeyCharacterMap
	// .deviceHasKey(KeyEvent.KEYCODE_BACK);
	//
	// if (!hasMenuKey && !hasBackKey) {
	// // 做任何你需要做的,这个设备有一个导航栏
	// return true;
	// }
	// return false;
	// }

	private void getratio() {
		// 设置Camera 时宽和高反过来了 ，因为android Camera 默认横屏，这里旋转90度
		double m = vHeight;
		double n = vWidth;
		double ratio = 0;

		Log.i("dbw", "m" + vHeight);
		Log.i("dbw", "n" + vWidth);

		if (m < n) {
			m = m + n;
			n = m - n;
			m = m - n;
		}
		Log.i("dbw", "m" + m);
		Log.i("dbw", "n" + n);
		// 高大宽小
		// 得出大于1的数，短边小的值乘以ratio（比值）的得出长边
		ratio = m / n;
		if (layheight > laywidth) {
			// 此为宽小，因此//宽不变
			surfaceViewHeight = (int) (laywidth * ratio);
			Log.i("dbw", "ratio" + ratio);
			surfaceViewWidth = laywidth;
			// 计算 偏移量
			// 宽不变，偏移为y轴
			if (surfaceViewHeight == layheight) {
				yAdjust = 0;
				xAdjust = 0;
			} else {
				yAdjust = (layheight - surfaceViewHeight - getstatus_bar_height()) / 2;
				xAdjust = 0;
			}
			Log.i("123", yAdjust + "      " + layheight + "  w  " + laywidth
					+ ratio + "   " + getstatus_bar_height());
		} else {
			surfaceViewWidth = (int) (layheight * ratio);
			surfaceViewHeight = layheight;

			if (surfaceViewWidth == laywidth) {
				yAdjust = 0;
				xAdjust = 0;
			} else {
				yAdjust = 0;
				xAdjust = (laywidth - surfaceViewHeight) / 2;
			}
		}
	}

	private int getstatus_bar_height() {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			// e1.printStackTrace();
		}
		return sbar;
	}

	public void toggleHideyBar() {

		int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
		// SYSTEM_UI_FLAG_IMMERSIVE_STICKY
		boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == uiOptions);

		if (isImmersiveModeEnabled) {
			Log.i("dbw", "Turning immersive mode mode off. ");

		} else {
			Log.i("dbw", "Turning immersive mode mode on."
					+ android.os.Build.VERSION.RELEASE);
			barHeight = getNavigationBarHeight();

			Log.i("dbw", "height=" + barHeight);

		}

	}

	private void Size() {

		
		try {
			
		
		myCamera = Camera.open();
		parameters = myCamera.getParameters();// 获得相机参数

		widths = wm.getDefaultDisplay().getWidth();

		heights = wm.getDefaultDisplay().getHeight();

		double vh = heights;
		double vw = widths;

		Log.i("slw", "vh=" + vh);
		Log.i("slw", "vw=" + vw);

		double needSize = (vh / vw);

		Pair<Double, Double> priviewSize = priSize(needSize, vw);

		if (hasPreview = true) {

			vHeight = (priviewSize.first).intValue();
			vWidth = (priviewSize.second).intValue();
			Log.i("slw", "vvv=" + vHeight + "///" + vWidth);

			if (vHeight == 0) {

				vHeight = heights;
				vWidth = widths;

			}

		} else {
			vHeight = heights;
			vWidth = widths;
			Log.i("slw", "vvvvvv=" + vHeight + "///" + vWidth);
		}

		myCamera.release();
		myCamera = null;
		}
		catch (Exception e) {
			Toast.makeText(ctx, "相机异常 请检查相机是否正常开启",Toast.LENGTH_SHORT ).show();
		}

	}

	// ---------------------SurfaceView----------------------
	@Override
	public void onPreviewFrame(byte[] arg0, Camera arg1) {
		// TODO :提交线程池
		// reentrantLock.lock();
		executorService.submit(new ResourceAnalysis(arg0, System
				.currentTimeMillis()));
		Log.i(tag, 1 + "");
		// reentrantLock.unlock();
	}

	// 当SurfaceView/预览界面的格式和大小发生改变时，该方法被调用
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		initCamera();
	}

	// SurfaceView启动时/初次实例化，预览界面被创建时，该方法被调用。
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {
			myCamera = Camera.open(slw);
			myCamera.setPreviewDisplay(mySurfaceHolder);
			Log.i(tag, "SurfaceHolder.Callback: surfaceCreated!");
		} catch (IOException e) {
			if (null != myCamera) {
				myCamera.release();
				myCamera = null;
			}
			Log.i(tag, "surfaceCreated error:" + e.getMessage());
			// e.printStackTrace();
		}
		initMediaPlay(audioPath);
	}

	// 销毁时被调用
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.i(tag, "SurfaceHolder.Callback：Surface Destroyed");
		if (null != myCamera) {
			myCamera.setPreviewCallback(null);
			myCamera.stopPreview();
			isPreview = false;
			myCamera.release();
			myCamera = null;
		}
		destroyMediaPlay();
	}

	private int getNavigationBarHeight() {
		Resources resources = this.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		int height = resources.getDimensionPixelSize(resourceId);
		Log.i("dbw", "Navi height:" + height);

		return height;
	}

	public void initCamera() {
		
		try {
			
		
		
		if (isPreview) {
			myCamera.stopPreview();
		}
		if (null != myCamera) {
			Camera.Parameters myParam = myCamera.getParameters();
			// myParam.setPictureSize(vHeight, vWidth);
			// myParam.setPreviewSize(vHeight, vWidth);
			myParam.setPreviewSize(vHeight, vWidth);
			Log.i("lop", vHeight + "\\" + vWidth + "");
			myCamera.setDisplayOrientation(90);

			List<String> focusModes = myParam.getSupportedFocusModes();

			if (focusModes
					.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {

				myParam.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}

			else if (focusModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {

				myParam.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
			}

			myCamera.setParameters(myParam);
			myCamera.startPreview();
			isPreview = true;
			
		}
		} catch (Exception e) {
			Toast.makeText(ctx, "相机异常 请检查相机是否能正常开启", Toast.LENGTH_SHORT).show();
		}
	}

	// 有开始

	public void initMediaPlay(String filePaht) {
		try {
			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.reset();
				mediaPlayer.setDataSource(filePaht);
				mediaPlayer.prepare();
			}
		} catch (Exception e) {
			Log.i(tag, "initMediaPlay error: " + e.getMessage());
			// e.printStackTrace();
		}
	}

	// 有结束
	public void destroyMediaPlay() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public void startMediaPaly() {
		if (mediaPlayer != null) {
			int frame = queueoriginalData.size();
			int sleepTime = frame * 125;// ms
			mediaPlayer.seekTo(sleepTime);
			mediaPlayer.start();
		}
	}

	public void stopMediaPaly() {
		if (mediaPlayer != null)
			mediaPlayer.pause();
	}

	// 无意中按返回键时要释放内存
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MySurface.this.finish();
	}

	// --------------------activity-------------------------
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyMediaPlay();
	}

	// public void pause(){
	// if(currentPacketCount==0){
	// currentPacketCount=loopcount;
	// }
	// else {
	// currentPacketCount=loopcount-currentPacketCountend;
	// currentPacketCount++;
	// }
	//
	// currentPacketCountend=loopcount;
	// while(true)
	// {
	// Log.i(tag, queueoriginalData.size()+"   "+currentPacketCount);
	// if(queueoriginalData.size()==currentPacketCount)
	// break;
	//
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// List<ImageData> list = new ArrayList<ImageData>();
	// Iterator<ImageData> iterator = queueoriginalData
	// .iterator();
	// while (iterator.hasNext()) {
	// ImageData id = iterator.next();
	// list.add(id);
	// iterator.remove();
	// }
	// /*
	// * 对图片排序，在池中数据包处理顺序不确定， 导致添加进集合的数据包，顺序是错误的。 所以需要进行排序
	// */
	// ImageOrder ioImageOrder = new ImageOrder();
	// Collections.sort(list, ioImageOrder);
	// for (int i = 0; i < list.size(); i++) {
	// originalData.add(list.get(i).getData());
	// }
	// }

	View.OnClickListener myonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {

			isShooting = false;
			isAudioPaly = false;
			stopGetVideo();
		}
	};

	View.OnTouchListener touchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			if (arg0.getId() == R.id.start) {
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 拍摄开始
					isShooting = true;
					reentrantLock.lock();
					concurrent.signalAll();
					reentrantLock.unlock();
					break;
				case MotionEvent.ACTION_UP:
					// 拍摄结束
					isShooting = false;
					isAudioPaly = false;
					// pause();
					break;
				}
			}
			return false;
		}
	};

	class ResourceAnalysis implements Runnable {
		private byte[] data;
		private long time;

		public void setData(byte[] data) {
			this.data = data;
		}

		public ResourceAnalysis(byte[] data, long time) {
			super();
			this.data = data;
			this.time = time;
		}

		public ResourceAnalysis() {
			super();
		}

		@Override
		public void run() {
			if (data == null || data.length <= 0)
				return;
			Bitmap bitmap = null;
			ByteArrayOutputStream outstream = new ByteArrayOutputStream();
			byte[] by;
			try {
				size = myCamera.getParameters().getPreviewSize();
				YuvImage image = new YuvImage(data, ImageFormat.NV21,
						size.width, size.height, null);
				if (image != null) {
					// 图片质量压缩至70%
					image.compressToJpeg(
							new Rect(0, 0, size.width, size.height),
							imageCompressionRatio, outstream);

					by = outstream.toByteArray();
					bitmap = BitmapFactory.decodeByteArray(by, 0, by.length);
					// 旋转
					bitmap = rotateBitmapByDegree(bitmap, 90);
					outstream.reset();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outstream);
					ImageData idData = new ImageData(outstream.toByteArray(),
							time);
					queueoriginalData.put(idData);

					/*
					 * String pathString = Environment
					 * .getExternalStorageDirectory().getAbsolutePath(); File
					 * file = new File(pathString,count + ".jpg");
					 * FileOutputStream fosFileOutputStream = new
					 * FileOutputStream( file);
					 * fosFileOutputStream.write(outstream1.toByteArray());
					 * fosFileOutputStream.flush(); fosFileOutputStream.close();
					 * outstream.close();
					 */
					// count++;
				}
			} catch (Exception e) {
				// e.printStackTrace();
				Log.i(tag, "ResourceAnalysis error:" + e.getMessage());
			} finally {
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
				if (outstream != null) {
					try {
						outstream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					by = null;
				}
			}
		}

		public Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
			if (bm == null)
				return null;

			Bitmap returnBm = null;

			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			try {
				returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), matrix, true);
			} catch (OutOfMemoryError e) {
				Log.i(tag, "图片旋转，至内存溢出");
			} finally {
				if (returnBm == null) {
					returnBm = bm;
				}
				if (bm != returnBm) {
					bm.recycle();
				}
			}
			return returnBm;
		}
	}

	public void stopGetVideo() {
		stopMediaPaly();// 停止音乐播放
		new Thread(new checkingexecutorService()).start();
	}

	class ResourceAcquisition implements Runnable {

		@Override
		public void run() {
			long start;
			/*
			 * 15s -->15000ms ；1s -->1000ms； 1s -->8fps； 1fps -->125ms ；15s
			 * -->120fps
			 */
			while (loopcount < 120) {
				try {
					start = System.currentTimeMillis();
					if (!isShooting) {
						// 在拍摄完后允许暂停，可以保证下次开始拍摄时拍到第一帧
						stopMediaPaly();// 停止音乐播放
						reentrantLock.lock();
						concurrent.await();
						reentrantLock.unlock();
						start = System.currentTimeMillis() + 1;// 加1毫秒 ，start =
																// System.currentTimeMillis();
																// reentrantLock.lock();if
																// (!isShooting)
																// 这三句的执行
						if (!isAudioPaly) {
							startMediaPaly();
							isAudioPaly = true;
						}
					}
					// 采集图像资源
					reentrantLock.lock();
					myCamera.setOneShotPreviewCallback(MySurface.this);
					reentrantLock.unlock();
					// 调整时间差保证每秒8帧
					long ix = 125 - (System.currentTimeMillis() - start);
					Log.i(tag, "count    :" + ix);
					Message msg = new Message();
					msg.what = MySurface.GET_FRAME;
					msg.arg1 = loopcount;
					handler.sendMessage(msg);
					Thread.sleep(Math.max(0, ix));
					loopcount++;
				} catch (Exception e) {
					// e.printStackTrace();
					Log.i(tag, "ResourceAcquisition error:" + e.getMessage());
				}
			}
			stopGetVideo();
		}
	}

	class checkingexecutorService implements Runnable {

		@Override
		public void run() {
			try {
				executorService.shutdown();
				Message msg = new Message();
				msg.what = MySurface.START_GENERATE;
				handler.sendMessage(msg);
				while (true) {
					// 如果执行完毕，进入后续处理
					if (executorService.isTerminated()) {
						List<ImageData> list = new ArrayList<ImageData>();
						Iterator<ImageData> iterator = queueoriginalData
								.iterator();
						while (iterator.hasNext()) {
							ImageData id = iterator.next();
							list.add(id);
							iterator.remove();
						}

						for (int i = 0; i < list.size(); i++) {
							originalData.add(list.get(i).getData());
						}
						/*
						 * 对图片排序，在池中数据包处理顺序不确定， 导致添加进集合的数据包，顺序是错误的。 所以需要进行排序
						 */
						ImageOrder ioImageOrder = new ImageOrder();
						Collections.sort(list, ioImageOrder);
						Log.i(tag, originalData.size() + "");
						msg = new Message();
						msg.what = MySurface.STOP_GENERATE;
						handler.sendMessage(msg);
						break;
					}
					Thread.sleep(500);
				}
			} catch (InterruptedException e) {
				Log.i(tag, "checkingexecutorService error:" + e.getMessage());
			}
		}

	}

	@SuppressLint("HandlerLeak")
	class handlerslt extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case MySurface.START_GENERATE:
				// progressBar.setVisibility(View.VISIBLE);

				btnstart.setEnabled(false);
				break;
			case MySurface.STOP_GENERATE:
				Log.i(tag, "originalData end :" + queueoriginalData.size());
				// progressBar.setVisibility(View.GONE);
				tv.setVisibility(View.GONE);
				Intent itIntent = new Intent();
				itIntent.putExtra("width", vWidth);
				itIntent.putExtra("height", vHeight);
				itIntent.putExtra("path", audioPath);
				itIntent.putExtra("what", whatToDo);
				itIntent.setClass(MySurface.this, MySurfaceView.class);

				startActivity(itIntent);
				finish();
				break;

			case MySurface.GET_FRAME:
				double frameCount = msg.arg1;
				double time = frameCount * 125 / 1000 + 0.125;// ss
				if (time >= 3.0) {
					btndelete.setEnabled(true);
					btndelete.setBackgroundResource(R.drawable.rsure);

				}
				if (time >= 15.0)
					time = 15.0;
				DecimalFormat dcmFmt = new DecimalFormat("0.00");
				tv.setText(dcmFmt.format(15 - time) + "秒");
				tv.setVisibility(View.VISIBLE);

				break;

			}
		}

	}

	class ImageOrder implements Comparator<Object> {

		@Override
		public int compare(Object arg0, Object arg1) {
			ImageData ida = (ImageData) arg0;
			ImageData idb = (ImageData) arg1;

			if (ida.getTime() > idb.getTime()) {
				return 1;
			} else {
				if (ida.getTime() == idb.getTime()) {
					return 0;
				} else {
					return -1;
				}
			}
		}

	}

}

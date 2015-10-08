package com.panfeng.camera.surfaceview;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceAnimationView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	// 图片集
	private List<byte[]> mList = null;
	// 运行状态
	public boolean mLoop = false;
	// 获取画布
	private SurfaceHolder mSurfaceHolder = null;
	// 图片索引
	private int mCount = 0;
	// 时间间隔
	private long speed = 125;
	private String tag = "dawn";
	private final static String TAG = "dawn";
	private static Matrix matrix = new Matrix();
	private MediaPlayer mediaPlayer;
	private String audioPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator + "paomo.mp3";

	/**
	 * @param context
	 *            <see>容器</see>
	 * @param list
	 *            <see>图片地址列表 </see>
	 * @param rate
	 *            <see>图片切换时间　单位:毫秒</see>
	 * 
	 */
	public SurfaceAnimationView(Context context, List<byte[]> list, long speed,
			String audioPath1) {
		super(context);
		audioPath = audioPath1;
		if (list != null && list.size() > 0) {
			this.mList = list;
			this.speed = speed;
		}
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mLoop = true;// 开始画图
		initMediaPlay(audioPath);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	// 图像创建时
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mList != null && mList.size() > 0) {
			mediaPlayer.start();
			if (mList.size() == 1) {
				Log.d(TAG, " Only one picture");
				drawImg();
			} else {
				Log.d(TAG, " run Thread.");
				new Thread(this).start();
			}
		}

	}

	// 视图销毁时
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			mLoop = false;
		}
	}

	public void stop() {
		
		if(mediaPlayer!=null){
		mLoop = false;
		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = null;
		}
	}

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

	// 画图方法
	private void drawImg() {
		long start = System.currentTimeMillis();
		Canvas canvas = mSurfaceHolder.lockCanvas();
		if (canvas == null || mSurfaceHolder == null) {
			return;
		}
		if (mCount >= mList.size()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			initMediaPlay(audioPath);
			mediaPlayer.start();
			mCount = 0;
		}
		Bitmap bitmap = null;
		try {
			byte[] bytearray = mList.get(mCount++);
			bitmap = BitmapFactory.decodeByteArray(bytearray, 0,
					bytearray.length);
			if (bitmap != null) {
				// 画布宽和高
				int height = getHeight();
				int width = getWidth();
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setStyle(Style.FILL);
				// 清屏
				paint.setColor(Color.BLACK);
				int widthx = bitmap.getWidth();
				int heightx = bitmap.getHeight();
				Matrix matrix = new Matrix();
				float wScake = ((float) width / widthx);
				float hScake = ((float) height / heightx);
				matrix.postScale(wScake, hScake);
				canvas.drawBitmap(bitmap, matrix, paint);
			}

		} catch (Exception ex) {
			// Log.e(TAG, ex.getMessage());
			return;
		} finally {
			// 资源回收
			mSurfaceHolder.unlockCanvasAndPost(canvas);
			if (bitmap != null) {
				bitmap.recycle();
			}
		}
		try {
			long x = speed - (System.currentTimeMillis() - start);
			Thread.sleep(Math.max(0, x));
			Log.i(TAG, x + "");
		} catch (InterruptedException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	// 刷新图片
	@Override
	public void run() {
		while (mLoop) {
			drawImg();
		}
		mList = null;// 消毁
	}

	// 缩放图片
	public Bitmap getReduceBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int hight = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float wScake = ((float) w / width);
		float hScake = ((float) h / hight);
		matrix.postScale(wScake, hScake);
		return Bitmap.createBitmap(bitmap, 0, 0, width, hight, matrix, true);
	}

}
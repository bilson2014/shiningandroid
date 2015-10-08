package com.panfeng.camera.surfaceview;

import java.util.List;

import com.panfeng.shining.interfaces.callBack;
import com.panfeng.shining.tools.CommonTools;
import com.renn.rennsdk.RennExecutor.CallBack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("WrongCall") public class SurfaceAnimationViewTabHost extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	// 图片集
	private  List<Integer> mList = null;
	// 运行状态
	public  boolean mLoop = false;
	// 获取画布
	private SurfaceHolder mSurfaceHolder = null;
	// 图片索引
	private int mCount = 0;
	// 时间间隔
	private long speed = 100;
	private final static String TAG = "dawn";

	
	private  callBack mCallBack;
 
	
	public  void setCallBack(callBack cb){
		mCallBack = cb;
	}

	public SurfaceAnimationViewTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);	
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mLoop = false;
	}

	public  void setData(List<Integer> list){
		if(list!=null&&list.size()>0)
		{
			mList=list;
			mLoop = true;
		}
			
		
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	// 图像创建时
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mList != null && mList.size() > 0) {
			if (mList.size() == 1) {
				Log.d(TAG, " Only one picture");
				
				onDraw();
			} else {
				Log.d(TAG, " run Thread.");
				new Thread(this).start();
			}
		}

	}

	// 视图销毁时
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mLoop = false;
	}

	// 画图方法
    protected void onDraw() {  
        
         
    	long start = System.currentTimeMillis();
		Canvas canvas = mSurfaceHolder.lockCanvas();
		if (canvas == null || mSurfaceHolder == null) {
			return;
		}
		if (mCount >= mList.size()) {
			mCount = 0;
           if(mCallBack!=null)
           {
        	   mCallBack.method();
           }
			
		
		}
		Bitmap bitmap = null;
		try {
			
			bitmap = BitmapFactory.decodeResource(getResources(), mList.get(mCount++));
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
		//	Log.i(TAG, x + "");
		} catch (InterruptedException e) {
			Log.e(TAG, e.getMessage());
		}
}
	
	
	
	// 刷新图片
	@Override
	public void run() {
		while (mLoop) {
			onDraw();
		}
		mList = null;// 消毁
	}
}
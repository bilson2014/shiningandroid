package com.panfeng.shining.drocode.swithcer;

import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class GuideGallery extends Gallery {

	private Activity activity;
	Handler handler;
	boolean timeFlag;
	boolean timeCondition;

	public GuideGallery(Context context, Activity activity) {
		super(context);
		this.activity = activity;
		// TODO Auto-generated constructor stub
	}

	public GuideGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GuideGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setActivity(Activity activity, Handler handler,boolean timeFlag,boolean timeCondition) {
		this.activity = activity;
		this.timeCondition = timeCondition;
		this.timeFlag = timeFlag;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		int kEvent;
		if (isScrollingLeft(e1, e2)) { // Check if scrolling left
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else { // Otherwise scrolling right
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);
		if (this.getSelectedItemPosition() == 0)
			System.out.println("DDD" + this.getSelectedItemPosition());
		new java.util.Timer().schedule(new TimerTask() {
			public void run() {
				timeFlag = false;
				this.cancel();
			}
		}, 3000);
		return true;

	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		System.out.println(this.getSelectedItemPosition());
		return e2.getX() > e1.getX();

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		timeCondition = false;
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

}

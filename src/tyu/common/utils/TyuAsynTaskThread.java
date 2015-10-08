package tyu.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class TyuAsynTaskThread extends Thread {
	Handler mHandler;

	public TyuAsynTaskThread(String aName) {
		setName(aName);
	}

	public void execRunnable(Runnable aTask) {
		if (mHandler != null) {
			Message msg = mHandler.obtainMessage();
			msg.obj = aTask;
			mHandler.sendMessage(msg);
		}
	}

	@Override
	public void run() {
		Looper.prepare();
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				Runnable task = (Runnable) msg.obj;
				task.run();
			}
		};
		Looper.loop();
	}
}

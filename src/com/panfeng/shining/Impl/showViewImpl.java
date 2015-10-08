package com.panfeng.shining.Impl;

import android.content.Context;
import com.panfeng.shining.interfaces.topWindowInterface;
import com.panfeng.shining.widgets.FVTopWindow;

public class showViewImpl implements topWindowInterface  {

	
	private FVTopWindow fVTopWindow;

	@Override
	public void showWindow(Context ctx, String name, int is,String path) {
		// TODO Auto-generated method stub
		
		try {
			Thread.sleep(500);
			fVTopWindow = new FVTopWindow(ctx, name, false);
			fVTopWindow.show(is);
			fVTopWindow.setInfoText(name);
			fVTopWindow.setVideoPath(path);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			
		}

		
		
	}

	@Override
	public void closeWindow(int call) {
		// TODO Auto-generated method stub
		fVTopWindow.hide(call);
	}
	






}

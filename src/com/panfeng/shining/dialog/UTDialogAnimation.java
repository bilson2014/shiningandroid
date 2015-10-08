package com.panfeng.shining.dialog;

import com.panfeng.shinning.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class UTDialogAnimation extends Dialog {

	 private Window window = null;
     
	    public UTDialogAnimation(Context context)
	    {
	        super(context);
	    }
	     
	    public void showDialog(int layoutResID, int x, int y){
	        setContentView(layoutResID);
	         
	        windowDeploy(x, y);
	         
	        setCanceledOnTouchOutside(true);
	        show();
	    } 
	     
	    public void windowDeploy(int x, int y){
	        window = getWindow(); 
	        window.setWindowAnimations(R.style.mystyle); 
	        WindowManager.LayoutParams wl = window.getAttributes();
	       
	        wl.x = x; 
	        wl.y = y; 
	        wl.alpha = 0.6f; 
	        wl.gravity = Gravity.BOTTOM; 
	        window.setAttributes(wl);
	    }
	}
package com.panfeng.shining.camera;

import com.panfeng.shinning.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;






public class SelfieWaitingBar extends Handler {
	
	
private static int postion = 1;
    
    static TextView word;
    static float ftime;
    private static Context ctx;
    static Handler handler = new Handler();  
    static Runnable runnable = new Runnable() {  
  
        @Override  
        public void run() {  
            // handler自带方法实现定时器  
                handler.postDelayed(this, 1000);  
                postion++;
                double times = (postion*1.3);
                ftime = (float)times;
                if(ftime>=100){
                word.setText("合成完毕100%");
                handler.removeCallbacks(runnable);
                }
                else {
                	 word.setText("正在合成"+ftime+"%");
                }
        }
            
    };    
	
	
	




	static public Dialog build(Context aContext) {
		View contentView = null;
		Dialog dlg = null;
		ctx = aContext;
		
		contentView = LayoutInflater.from(aContext).inflate(R.layout.selfie_waiting_bar, null);
		ImageView img = (ImageView) contentView.findViewById(R.id.imageView1);
		AnimationDrawable ad = (AnimationDrawable) img.getDrawable();
		ad.start();
//		if(!ad.isRunning()){
//			
//			ad.stop();
//			ad.selectDrawable(99);
//			
//			
//		}
		
		word = (TextView)contentView.findViewById(R.id.selfie_text);
	        
	 // handler.postDelayed(runnable, 1000);
		
		
		
	//	dlg = new Dialog(aContext, R.style.selectorDialog);
		dlg = new Dialog(aContext, R.style.activity_theme_transparent);
		// 当然也可以手动设置PopupWindow大小。
		dlg.setCanceledOnTouchOutside(false);
		dlg.setContentView(contentView);
		
		return dlg;
	}
}

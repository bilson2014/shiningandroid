package com.panfeng.shining.camera;

import com.panfeng.shinning.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelfieToLook extends Handler {

	private static int postion = 1;

	static TextView word;
	static float ftime;
	private static Context ctx;

	static public Dialog build(Context aContext) {
		View contentView = null;
		Dialog dlg = null;
		ctx = aContext;

		contentView = LayoutInflater.from(aContext).inflate(R.layout.tolook,
				null);

		RelativeLayout toLook = (RelativeLayout) contentView.findViewById(R.id.toMyShin);
		toLook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

//				Intent intent = new Intent();
//				intent.setClass(ctx, ShowMyMediaActivity.class);
//				ctx.startActivity(intent);
				

				
				
			}
		});

	

		dlg = new Dialog(aContext, R.style.selectorDialog);
		// 当然也可以手动设置PopupWindow大小。
		dlg.setCanceledOnTouchOutside(false);
		dlg.setContentView(contentView);

		return dlg;
	}
}

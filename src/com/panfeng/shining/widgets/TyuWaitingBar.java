package com.panfeng.shining.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.panfeng.shinning.R;

public class TyuWaitingBar {
	static public Dialog build(Context aContext) {
		View contentView = null;
		Dialog dlg = null;
		contentView = LayoutInflater.from(aContext).inflate(
				R.layout.tyu_waiting_bar, null);
		ImageView img = (ImageView) contentView.findViewById(R.id.imageView1);
		AnimationDrawable ad = (AnimationDrawable) img.getDrawable();
		ad.start();
		dlg = new Dialog(aContext, R.style.selectorDialog);
		// 当然也可以手动设置PopupWindow大小。
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(contentView);
		
		return dlg;
	}
}

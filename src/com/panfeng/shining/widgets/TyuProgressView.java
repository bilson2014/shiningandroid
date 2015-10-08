package com.panfeng.shining.widgets;

import com.panfeng.shinning.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TyuProgressView extends ImageView {
	int progress = 0;
	final static int[] img_res = { R.drawable.progress_1,
			R.drawable.progress_2, R.drawable.progress_3,
			R.drawable.progress_4, R.drawable.progress_5,
			R.drawable.progress_6, R.drawable.progress_7,
			R.drawable.progress_8, R.drawable.progress_9,
			R.drawable.progress_10, R.drawable.progress_11,
			R.drawable.progress_12, };

	public TyuProgressView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TyuProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public TyuProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setProgress(int aVal) {
		progress = aVal;
		int index = progress*img_res.length/100;
		if(index>=img_res.length){
			index = img_res.length-1;
		}
		setImageResource(img_res[index]);
	}
}

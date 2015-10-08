package com.panfeng.shining.widgets;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.panfeng.shining.interfaces.onItemPressed;
import com.panfeng.shinning.R;

public class TyuTabManager {
	Activity mParent = null;
	View mTabView = null;
	int tab_id[] = null;
	int img_res[][] = null;
	private onItemPressed mCallBack;

	public TyuTabManager(Activity aParent, int aResid, int[] tabIds,
			int[][] imgRes, onItemPressed aCallBack) {
		mParent = aParent;
		mTabView = mParent.findViewById(aResid);
		tab_id = tabIds;
		img_res = imgRes;
		mCallBack = aCallBack;
		initTabItems();
	}

	public void initTabItems() {
		for (int i = 0; i < tab_id.length; i++) {
			View tmp = mTabView.findViewById(tab_id[i]);
			final int item_id = i;
			tmp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mCallBack != null) {
						mCallBack.onItemPressed(item_id);
					}
				}
			});
		}
	}

	public void setCurrentId(int aId) {
		if (aId >= tab_id.length)
			return;
		for (int i = 0; i < tab_id.length; i++) {
			ViewGroup tmp = (ViewGroup) mTabView.findViewById(tab_id[i]);
			ImageView img = (ImageView) tmp.findViewById(R.id.img);
			TextView txt = (TextView) tmp.findViewById(R.id.textView1);
			if (i == aId) {
				img.setImageResource(img_res[i][1]);
				txt.setTextColor(mParent.getResources().getColor(R.color.app_main_color));
			} else {
				img.setImageResource(img_res[i][0]);
				txt.setTextColor(0xff20262a);
			}
		}
	}
}

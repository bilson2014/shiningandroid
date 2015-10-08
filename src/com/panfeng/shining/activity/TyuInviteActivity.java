package com.panfeng.shining.activity;

import com.panfeng.shinning.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TyuInviteActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fv_shinning_invite_layout);
		// View title_bar = findViewById(R.id.title_bar);
		// title_bar.findViewById(R.id.left).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// finish();
		// }
		// });
		// title_bar.findViewById(R.id.right).setVisibility(View.INVISIBLE);
		// TextView title = (TextView) title_bar.findViewById(R.id.txt);
		// title.setText("二维码邀请");

		initTitleBar();
		TextView textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText("扫描二维码下载app,即刻拥有闪铃");
	}

	void initTitleBar() {
		View title = findViewById(R.id.title_bar);
		ImageView back = (ImageView) title.findViewById(R.id.back);
		TextView title_txt = (TextView) title.findViewById(R.id.txt);
		View star = title.findViewById(R.id.star_frame);
		star.setVisibility(View.GONE);
		title_txt.setVisibility(View.VISIBLE);
		title_txt.setText("二维码邀请");
		back.setImageResource(R.drawable.back_btn);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}

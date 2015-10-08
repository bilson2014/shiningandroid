package com.panfeng.shining.activity;

import tyu.common.utils.TyuFileUtils;


import com.panfeng.shinning.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TyuAboutActivity extends Activity {
	
	Context ctx = TyuAboutActivity.this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fv_shinning_about_layout);
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
		// textView1.setText("二维码邀请\n下载app,即刻拥有闪铃");
		// textView1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		String info = TyuFileUtils.readAssetFile("about.txt");
		textView1.setText(info);

		findViewById(R.id.share).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
//				S2ndShinningFriendActivity.inviteFriend(TyuAboutActivity.this);
				
				Intent sendIntent = new Intent();  
				sendIntent.setAction(Intent.ACTION_SEND);  
				sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.shiningmovie.com/");  
				sendIntent.setType("text/plain");  
				startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.action_share)));  
				
				
			}
		});
	}

	void initTitleBar() {
		View title = findViewById(R.id.newlistbar);
	
		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
		bartxt.setText("关于我们");
		
		bartxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
		
		
		
		barimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

}
}

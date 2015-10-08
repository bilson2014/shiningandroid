package com.panfeng.shining.camera;

import com.panfeng.shinning.R;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SelfieDia extends Activity {

	Context ctx = SelfieDia.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	

		super.onCreate(savedInstanceState);
		setContentView(R.layout.videodialog);
		findViewById(R.id.videospace).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_down_out,
						R.anim.push_down_out);
			}
		});

		findViewById(R.id.videocancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_down_out,
						R.anim.push_down_out);
			}
		});
		
		findViewById(R.id.revideo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SelfieDia.this.setResult(1); 
				finish();
				overridePendingTransition(R.anim.push_down_out,
						R.anim.push_down_out);
				
				
				
			}
		});
		
		findViewById(R.id.redel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SelfieDia.this.setResult(2); 
				finish();
				overridePendingTransition(R.anim.push_down_out,
						R.anim.push_down_out);
				
				
				
			}
		});
		
		
		
	}
}
	

package com.panfeng.shining.activity.newactivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.panfeng.shining.TyuApp;
import com.panfeng.shining.Impl.localSetImpl;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.interfaces.toSet;
import com.panfeng.shining.tools.FileTools;
import com.panfeng.shining.tools.MediaPlayerTools;
import com.panfeng.shinning.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class ShowLocalVideo extends Activity  {

	String path;
	SurfaceView surfaceView;
	MediaPlayerTools mp = new MediaPlayerTools();
	boolean first=false;
	static Context ctx ;
	toSet ts;

	protected void onStop() {
		super.onStop();
		TyuApp.needCreate = false;
		
		
	}
       

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localvideo);

		Intent intent = this.getIntent();
		ts = new localSetImpl();
		ctx = ShowLocalVideo.this;
		initTitleBar();

		path = intent.getStringExtra("url");
	
		surfaceView = (SurfaceView) findViewById(R.id.localvideo);
		double secWidth = this.getWindowManager().getDefaultDisplay()
				.getWidth();
		double secHeight = this.getWindowManager().getDefaultDisplay()
				.getHeight();

		double realHeight = FileTools.getRealSize(path, secWidth, secHeight).first;
		double realWidth = FileTools.getRealSize(path, secWidth, secHeight).second;

		LayoutParams lp = surfaceView.getLayoutParams();
		lp.height = (int) realHeight;
		lp.width = (int) realWidth;
		surfaceView.setLayoutParams(lp);


	
		File f = new File(path);
		mp.initMediaPlay(f, surfaceView);
		

	}
	
	void initTitleBar() {
		View title = findViewById(R.id.localShowList);

		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		ImageView btn =(ImageView) title.findViewById(R.id.bar_img_sure);

		barimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ts.showSet(ctx,path);
			}
		});

	}

	
	public static void toClose(){
		
		((Activity) ctx).finish();
		
	}
	

	

	

}

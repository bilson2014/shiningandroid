package com.panfeng.shining.activity;

import com.panfeng.shinning.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.VideoView;

public class TyuPlayDefaultActivity extends Activity implements OnClickListener {
	private boolean showing;
	private VideoView video;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.popup_video_layout);
		video = (VideoView) findViewById(R.id.video);
		findViewById(R.id.space1).setOnClickListener(this);
		findViewById(R.id.space2).setOnClickListener(this);
		video.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (showing) {
						video.pause();
						showing = false;
					} else {
						showing = true;
						video.start();
					}
				}
				return true;
			}
		});
		video.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				stop();
			}
		});
		String path = "android.resource://" + getPackageName() + "/"
				+ R.raw.base;
		video.setVideoPath(path);
		video.start();
	}
	static public void playDefault(Activity aParent){
		aParent.startActivity(new Intent(aParent,TyuPlayDefaultActivity.class));
	}
	public void play() {
		if (showing)
			return;

		video.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				// video.start();
				stop();
			}
		});

		video.start();

		showing = true;
	}

	public void stop() {
		if (!showing)
			return;
		video.stopPlayback();
		showing = false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.space1:
		case R.id.space2:
			finish();
			break;

		default:
			break;
		}
	}
}

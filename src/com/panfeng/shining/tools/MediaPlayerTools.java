package com.panfeng.shining.tools;

import java.io.File;
import java.io.IOException;

import com.panfeng.shining.TyuApp;
import com.panfeng.shining.Impl.voiceImpl;
import com.panfeng.shining.interfaces.volumnInterface;
import com.panfeng.shinning.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MediaPlayerTools {
	MediaPlayer mediaPlayer;
	boolean initMedia = false;
	boolean initView = false;
	volumnInterface vi = new voiceImpl();
	SurfaceView surface;

	public void changeVideo(File file) {

		try {

			mediaPlayer.stop();
			mediaPlayer.reset();
			mediaPlayer.setDataSource(file.getAbsolutePath());
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void initMediaPlayLead(File file,final SurfaceView surfaceView,final ImageView close,final ImageView set,final ImageView never){


		surface = surfaceView;
		
		try {
			
				// 设置播放的视频源

			mediaPlayer = new MediaPlayer();
			surface.getHolder().addCallback(callback);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(file.getAbsolutePath());
			mediaPlayer.prepareAsync();
				mediaPlayer
						.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

							@Override
							public void onPrepared(MediaPlayer mp) {

								// 装载完成
								// 设置显示视频的SurfaceHolder
								initMedia = true;
								initView = true;
								if (initView) {

									TyuApp.hasinitMediapalyer = true;
									mediaPlayer.setDisplay(surfaceView
											.getHolder());
									mediaPlayer.start();
									
								}

							}
						});
				mediaPlayer
						.setOnErrorListener(new MediaPlayer.OnErrorListener() {

							@Override
							public boolean onError(MediaPlayer mp, int what,
									int extra) {
								// 发生错误

								if (mediaPlayer != null)
									mediaPlayer.release();
								mediaPlayer = null;
								initMedia = false;
								return false;
							}
						});
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

	                @Override
	                public void onCompletion(MediaPlayer mp) {
	                   Log.d("tag", "播放完毕");
	                   close.setVisibility(View.VISIBLE);
	                   set.setVisibility(View.VISIBLE);
	                   never.setVisibility(View.VISIBLE);
	               	   mediaPlayer.start();
	                   mediaPlayer.setLooping(true);

	                }
	            });
				
				
			
		} catch (Exception e) {

			// FVPhoneStateReceiver.resumeVoice();
			vi.resumeVoice();

		}
	}
	
	

	public void initMediaPlay(File file, final SurfaceView surfaceView) {

		Log.i("messageActivity", "file" + file);

		surface = surfaceView;

		try {
			if (file != null) {
				// 设置播放的视频源

				Log.i("messageActivity", "initview" + initView);
				mediaPlayer = new MediaPlayer();
				surface.getHolder().addCallback(callback);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setDataSource(file.getAbsolutePath());
				mediaPlayer.prepareAsync();
				mediaPlayer
						.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

							@Override
							public void onPrepared(MediaPlayer mp) {

								// 装载完成
								// 设置显示视频的SurfaceHolder
								initMedia = true;
								if (initView) {

									TyuApp.hasinitMediapalyer = true;
									mediaPlayer.setDisplay(surfaceView
											.getHolder());
									mediaPlayer.start();
									mediaPlayer.setLooping(true);
								}

							}
						});
				mediaPlayer
						.setOnErrorListener(new MediaPlayer.OnErrorListener() {

							@Override
							public boolean onError(MediaPlayer mp, int what,
									int extra) {
								// 发生错误

								if (mediaPlayer != null)
									mediaPlayer.release();
								mediaPlayer = null;
								initMedia = false;
								return false;
							}
						});
			}
		} catch (Exception e) {

			// FVPhoneStateReceiver.resumeVoice();
			vi.resumeVoice();

		}
	}

	SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
		// SurfaceHolder被修改的时候回调
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// 销毁SurfaceHolder
			initView = false;
			stopMediaPlay();

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			initView = true;
			if (initMedia&&TyuApp.needCreate) {
				mediaPlayer.setDisplay(surface.getHolder());
				mediaPlayer.start();
				mediaPlayer.setLooping(true);
				initMedia = false;
				TyuApp.needCreate = true;
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

	};

	public void stopMediaPlay() {
		try {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
			}
		} catch (IllegalStateException e) {

		}

	}

}

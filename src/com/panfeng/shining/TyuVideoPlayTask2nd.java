package com.panfeng.shining;

import tyu.common.utils.TyuAsynTaskThread;
import tyu.common.utils.TyuContextKeeper;
import tyu.common.utils.TyuFileUtils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.data.TyuShinningData.VideoCacheInfo;
import com.panfeng.shining.data.TyuShinningData.VideoItemData;
import com.panfeng.shining.widgets.TyuProgressView;
import com.panfeng.shining.widgets.TyuVideoView;
import com.panfeng.shinning.R;

public class TyuVideoPlayTask2nd {

	private TyuVideoView mVideoView;
	// private TextView tvcache;
	private String remoteUrl;
	private String localUrl;
	// private ProgressDialog progressDialog = null;

	private int curPosition = 0;
	// private long mediaLength = 0;
	// private long readSize = 0;
	private boolean stopTask = false;
	private View video_bg;
	private TyuProgressView progress;
	private TextView info;
	private View authorBar;
	VideoCacheInfo mCurrentRecord = null;
	Runnable mBufferedComplete = null;

	// Activity mParent = null;
	boolean loop = false;
	private boolean useDefault = false;
	private VideoItemData mData;

	static final Object mLock = new Object();
	static int task_count = 0;
	static TyuAsynTaskThread taskLoop = new TyuAsynTaskThread(
			TyuVideoPlayTask2nd.class.getName());
	static{
		taskLoop.start();
	}
	public void setLoop(boolean aLoop) {
		loop = aLoop;
	}

	public TyuVideoPlayTask2nd() {

	}

	public TyuVideoPlayTask2nd(View aVideoPlayerView) {
		setupUI(aVideoPlayerView);
		// init();
	}

	public void setupUI(View aVideoPlayerView) {
		TyuProgressView pro = (TyuProgressView) aVideoPlayerView
				.findViewById(R.id.progressBar1);
		TextView info = (TextView) aVideoPlayerView
				.findViewById(R.id.cache_info);
		setProgress(pro, info);

		mVideoView = (TyuVideoView) aVideoPlayerView.findViewById(R.id.video);
		// mVideoView.setVisibility(View.INVISIBLE);
		video_bg = aVideoPlayerView.findViewById(R.id.mix_img);
	}

	public void setupData(VideoItemData aData) {
		stop();
		useDefault = false;
		mData = aData;
		if (aData != null) {
			remoteUrl = aData.video_url;
			String name = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
			localUrl = TyuFileUtils.getValidPath() + "/VideoCache/" + name;

		} else {
			useDefault = true;
		}
		init();

	}

	public void playDefaultVideo() {
		stop();
		String path = "android.resource://"
				+ TyuContextKeeper.getInstance().getPackageName() + "/"
				+ R.raw.base;
		mVideoView.setVideoPath(path);
		mVideoView.start();
	}

	public TyuVideoPlayTask2nd(TyuVideoView aVideo, String aUrl, View aBtn,
			View bg) {
		video_bg = bg;
		mVideoView = aVideo;
		remoteUrl = aUrl;
		String name = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
		localUrl = TyuFileUtils.getValidPath() + "/VideoCache/" + name;

	}

	// public void setParent(Activity aParent) {
	// mParent = aParent;
	// }

	public void setProgress(TyuProgressView progress, TextView info) {
		this.progress = progress;
		this.info = info;
		this.progress.setProgress(0);
		this.progress.setVisibility(View.INVISIBLE);
		this.info.setText("0%");
		this.info.setVisibility(View.INVISIBLE);
		dismissProgressDialog();
	}

	public void setAuthorBar(View authorBar) {
		this.authorBar = authorBar;
		if (authorBar != null) {
			TextView view = (TextView) this.authorBar.findViewById(R.id.name);
			view.setText(TyuShinningData.getFakeAuthorName(remoteUrl));
			this.authorBar.setVisibility(View.INVISIBLE);
		}
	}

	public void showAuthorBar() {
		if (authorBar != null) {
			Context context = TyuContextKeeper.getInstance();
			Animation anim = AnimationUtils.loadAnimation(context,
					R.anim.alpha_in);
			authorBar.startAnimation(anim);
			authorBar.setVisibility(View.VISIBLE);
		}
	}

//	void fileCheck() {
//		// 配置数据
//		HashMap<String, VideoCacheInfo> records = TyuShinningData
//				.getVideoCache();
//		VideoCacheInfo record = records.get(remoteUrl);
//		if (record == null) {
//			record = new VideoCacheInfo();
//			record.cacheFile = localUrl;
//			record.videoUrl = remoteUrl;
//			records.put(remoteUrl, record);
//			TyuShinningData.saveVideoCache();
//		}
//		mCurrentRecord = record;
//		// if (!mCurrentRecord.isCached()) {
//		// mCurrentRecord.clearCache();
//		// }
//		File cacheFile = new File(localUrl);
//		cacheFile.getParentFile().mkdirs();
//	}

	public void setOnBufferedComplete(Runnable aRun) {
		mBufferedComplete = aRun;
	}

	public void init() {

		// mVideoView.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
		mVideoView.setVisibility(View.VISIBLE);
		// mVideoView.setBackgroundColor(0xff000000);
		video_bg.setVisibility(View.VISIBLE);
		// mVideoView.setBufferSize(READY_BUFF);
		mVideoView.requestFocus();

		// mVideoView.
		if (false) {
			mVideoView.setOnInfoListener(new OnInfoListener() {

				private boolean needResume;

				@Override
				public boolean onInfo(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					Log.v("onInfo", "onInfo:" + what + ";" + extra);
					if (stopTask)
						return true;
					switch (what) {
					case MediaPlayer.MEDIA_INFO_BUFFERING_START:
						// pause();
						// showProgressDialog();
						if (mVideoView.isPlaying()) {
							mVideoView.pause();
							needResume = true;
						}
						showProgressDialog();
						break;
					case MediaPlayer.MEDIA_INFO_BUFFERING_END:
						if (needResume) {
							mVideoView.start();
						}

						if (mBufferedComplete != null) {
							mBufferedComplete.run();
						}
						// video
						// mVideoView.setVisibility(View.VISIBLE);
						video_bg.setVisibility(View.INVISIBLE);
						mHandler.removeMessages(SCAN_STATE);
						mHandler.sendEmptyMessage(SCAN_STATE);
						dismissProgressDialog();
						break;
					}
					return true;
				}
			});
		}
		mVideoView
				.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						// TODO Auto-generated method stub
						// Log.v("onInfo", "onBufferingUpdate:" + percent +
						// ";");
						// info.setText(percent + "%");
						if (stopTask)
							return;

						progress.setProgress(percent);
						info.setText(percent + "%");
					}
				});
		mVideoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				Log.v("onInfo", "onError:" + what + ";" + extra);
				// resume();
				return true;
			}
		});
		mVideoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Log.v("onInfo", "onPrepared:" + curPosition);
				if (stopTask)
					return;

				dismissProgressDialog();
				video_bg.setVisibility(View.INVISIBLE);
				mHandler.removeMessages(SCAN_STATE);
				mHandler.sendEmptyMessage(SCAN_STATE);
			}
		});
		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if (stopTask)
					return;
				curPosition = 0;
				// mVideoView.pause();
				mHandler.removeMessages(SCAN_STATE);
				if (mCL != null) {
					mCL.onCompletion(mp);
				}
				video_bg.setVisibility(View.VISIBLE);
				if (loop) {
					// playvideo();
				}

			}
		});

	}

	private void showProgressDialog() {
		progress.setVisibility(View.VISIBLE);
		info.setVisibility(View.VISIBLE);
	}

	private void dismissProgressDialog() {
		progress.setVisibility(View.INVISIBLE);
		info.setVisibility(View.INVISIBLE);
	}

	public void pause() {
		mVideoView.pause();
	}

	public void resume() {
		// mVideoView.pause();
		mVideoView.start();
	}

	public boolean isStopped() {
		return stopTask;
	}

	public void stop() {
		if (!stopTask) {
			task_count--;
			Log.v("task_count", task_count + "");
			stopTask = true;
			if (mVideoView != null && mVideoView.isPlaying())
				curPosition = mVideoView.getCurrentPosition();
			dismissProgressDialog();
			// taskLoop.execRunnable(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			//
			// }
			// });
			if (mVideoView != null) {
				taskLoop.execRunnable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mVideoView.stopPlayback();
					}
				});

			}
			mHandler.removeMessages(SCAN_STATE);
		}

	}

	OnCompletionListener mCL = null;

	public void setOnCompleteListener(OnCompletionListener aLis) {
		mCL = aLis;
	}

	public void playvideo() {
		// synchronized (mLock) {
		TyuShinningData.getInstance().addWeightAsync(mData.mb_id);
		// stop();
		task_count++;
		Log.v("task_count", task_count + "");
		stopTask = false;
		if (authorBar != null)
			authorBar.setVisibility(View.INVISIBLE);
		// showProgressDialog();
		if (useDefault) {
			playDefaultVideo();
		} else {
			showProgressDialog();
			Uri uri = Uri.parse(String.format("cache:%s:%s", localUrl,
					remoteUrl));
			if (remoteUrl == null || remoteUrl.length() <= 0) {
				Log.v("null_path", "null_path");
			}
			
			
			// mVideoView.clearSurfaceView();
			// mVideoView.setBackgroundColor(0xff000000);

			// mVideoView.clearSurfaceView();
			mVideoView.setVideoPath(remoteUrl);
			taskLoop.execRunnable(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mVideoView.seekTo(curPosition);
					mVideoView.start();
				}
			});
			
			// taskLoop.execRunnable(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// mVideoView.stopPlayback();
			// mVideoView.setVideoPath(remoteUrl);
			// mVideoView.seekTo(curPosition);
			// mVideoView.start();
			//
			// }
			// });

		}
	}

	private final static int START_PLAY = 101;
	private final static int SCAN_STATE = 1002;
	private final static int SHOW_AUTHOR_BAR = 10;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// if (mParent != null && mParent.isFinishing())
			// return;
			if (stopTask)
				return;
			switch (msg.what) {
			case SHOW_AUTHOR_BAR:
				// showAuthorBar();
				break;
			case SCAN_STATE: {
				// 检查文件尺寸

				// 记录播放进度
				// if (mVideoView!=null&&mVideoView.isPlaying()) {
				// curPosition = (int) mVideoView.getCurrentPosition();
				// }
				// sendEmptyMessageDelayed(SCAN_STATE, 500);
				// int duration = (int) mVideoView.getDuration();
				// duration = duration == 0 ? 1 : duration;
				// double playpercent = curPosition * 100.00 / duration * 1.0;
				//
				// }
				//
			}
				break;
			case START_PLAY:
				showProgressDialog();
				Uri uri = Uri.parse(String.format("cache:%s:%s", localUrl,
						remoteUrl));
				mVideoView.setVideoURI(uri);
				mVideoView.start();
				break;

			}

			super.handleMessage(msg);
		}
	};
}

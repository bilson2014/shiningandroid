package com.panfeng.shining.activity.s2nd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.shining.TyuApp;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.tools.FileTools;
import com.panfeng.shinning.R;

public class ShowMyMediaActivity extends Activity implements OnClickListener {

	// 路劲
	String mediaPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/mylist";// 我的作品
	String cameraPath;
	String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	String newPath = pathString + "/" + "com.panfeng.shinning" + "/"// 我的闪铃
			+ "myShinVideo/myShin.mp4";

	Context ctx = ShowMyMediaActivity.this;
	String urlid;
	Dialog showset;
	File file;
	String str, name, videoname, from;
	private ImageView moreinfo;
	ImageView del, set, share;
	SurfaceView v;
	MediaPlayer mediaPlayer;

	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	ArrayList<View> itemViewList = new ArrayList<View>();

	// 关键字基础数据

	protected void onStop() {
		super.onStop();
		TyuApp.needCreate = false;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.s2nd_my_media_list_layout);

		v = (SurfaceView) findViewById(R.id.video);
		init();

		share.setVisibility(View.INVISIBLE);
		set.setVisibility(View.INVISIBLE);

		cameraPath = newPath;

		double secWidth = this.getWindowManager().getDefaultDisplay()
				.getWidth();
		double secHeight = this.getWindowManager().getDefaultDisplay()
				.getHeight();

		try {

			FileTools ft = new FileTools();

			double realHeight = ft.getRealSize(cameraPath, secWidth, secHeight).first;
			double realWidth = ft.getRealSize(cameraPath, secWidth, secHeight).second;

			LayoutParams lp = v.getLayoutParams();
			lp.height = (int) realHeight;
			lp.width = (int) realWidth;
			v.setLayoutParams(lp);

			initMediaPaly(new File(cameraPath));
		} catch (Exception e) {
			Toast.makeText(ctx, "视频准备中", Toast.LENGTH_LONG).show();
		}

	}

	private void toDel() {

		LayoutInflater mLayout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgv = (ViewGroup) mLayout.inflate(R.layout.setvideo, null);
		showset = new Dialog(ctx, R.style.activity_theme_transparent);

		showset.setCanceledOnTouchOutside(false);
		showset.setContentView(dlgv);
		showset.setCancelable(false);
		showset.show();

		ImageView sure = (ImageView) dlgv.findViewById(R.id.sure_setvideo);
		TextView tesure = (TextView) dlgv.findViewById(R.id.set_true);

		tesure.setText("客官您确定吗？");

		ImageView back = (ImageView) dlgv.findViewById(R.id.sure_back);

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				file = new File(cameraPath);
				file.delete();

				SharedPreferences preference = ctx.getSharedPreferences(
						"configs", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				editor.putInt("isSetMyVideo", 0);
				editor.commit();

				DBAdapter db = new DBAdapter(ctx);
				db.open();
				db.updateMyVideo(1, 0);
				db.close();

				finish();

				showset.dismiss();

			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showset.dismiss();

			}
		});

	}

	private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
		// SurfaceHolder被修改的时候回调
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// 销毁SurfaceHolder
			stopMediaPlay();
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

	};

	private void stopMediaPlay() {
		try {
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

	}

	private void initMediaPaly(File file) {
		try {
			if (file != null) {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				// 设置播放的视频源
				v.getHolder().addCallback(callback);
				mediaPlayer.setDataSource(file.getAbsolutePath());
				mediaPlayer.prepareAsync();
				mediaPlayer
						.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
							@Override
							public void onPrepared(MediaPlayer mp) {
								// 装载完成
								// 设置显示视频的SurfaceHolder

								Log.i("dawn", " if (oprepared && init)");
								mediaPlayer.setDisplay(v.getHolder());
								mediaPlayer.setLooping(true);
								mediaPlayer.start();

							}
						});
				mediaPlayer
						.setOnErrorListener(new MediaPlayer.OnErrorListener() {

							@Override
							public boolean onError(MediaPlayer mp, int what,
									int extra) {
								// 发生错误
								mediaPlayer.release();
								mediaPlayer = null;
								return false;
							}
						});
			}
		} catch (Exception e) {
			// e.printStackTrace();
			// LogUtils.writeErrorLog("播放视频文件", "", e, null);
		}
	}

	public void init() {

		del = (ImageView) findViewById(R.id.myvideo_del);
		del.setOnClickListener(this);
		set = (ImageView) findViewById(R.id.myvideo_set);
		set.setOnClickListener(this);
		share = (ImageView) findViewById(R.id.myvideo_share);
		share.setOnClickListener(this);
		moreinfo = (ImageView) findViewById(R.id.moreinfo);
		moreinfo.setOnClickListener(this);

	}

	private long clickTime = 0;

	// 双击重置
	public boolean secondDel() {

		if ((System.currentTimeMillis() - clickTime) > 2000) {
			clickTime = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.myvideo_del:

			toDel();

			break;

		case R.id.myvideo_set:

			if (secondDel()) {
				Toast.makeText(ShowMyMediaActivity.this, "再按一次设置为闪铃",
						Toast.LENGTH_SHORT).show();
			} else {

				Toast.makeText(ctx, "设置成功", Toast.LENGTH_LONG).show();
			}

			break;

		case R.id.myvideo_share:

			// Intent intent = new Intent(ShowMyMediaActivity.this,
			// S2ndShareVideoActivity.class);
			// if (!urlid.equals("null")) {
			// try {
			// JSONObject jo = new JSONObject(urlid);
			// intent.putExtra(
			// "url",
			// TyuDefine.URL + "share.jsp?id="
			// + jo.getString("id") + "_");
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// }
			// startActivity(intent);
			// overridePendingTransition(R.anim.bottom_in, 0);

			break;

		case R.id.moreinfo:
			finish();
			break;

		}

	}

}

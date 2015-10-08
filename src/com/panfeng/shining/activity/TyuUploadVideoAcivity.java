package com.panfeng.shining.activity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import tyu.common.net.TyuHttpClientUtils.TyuUploadListener;
import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuContextKeeper;
import tyu.common.utils.TyuFileUtils;
import android.app.Activity;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.TyuApp.TyuImageLoadingListener;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.data.TyuShinningData.VideoItemData;
import com.panfeng.shining.data.TyuShinningData.VideoLocalItemData;
import com.panfeng.shining.widgets.TileButton;
import com.panfeng.shinning.R;

public class TyuUploadVideoAcivity extends Activity implements OnClickListener {

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = TyuApp.getCommonConfig();
	ImageLoadingListener simpleImageListener = new TyuImageLoadingListener();

	String file;
	String image;
	private ImageView img;
	private VideoView video;
	private View play_btn;
	private boolean showing = false;
	boolean checked = false;
	private EditText nameEdit;
	VideoLocalItemData mLocalData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		file = getIntent().getStringExtra("file");
		image = getIntent().getStringExtra("image");
		if (TextUtils.isEmpty(file) || TextUtils.isEmpty(image)) {
			finish();
			return;
		}
		mLocalData = new VideoLocalItemData();
		mLocalData.image_path = image;
		mLocalData.video_path = file;
	//	TyuUserInfo.getInstance().getMyVideos().add(mLocalData);
		setContentView(R.layout.fv_upload_anim_layout);
		// 标题
		View tmp = findViewById(R.id.title_bar);
		View leftBtn = tmp.findViewById(R.id.left);
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TileButton rightBtn = (TileButton) tmp.findViewById(R.id.right);
		rightBtn.setText("放弃");
		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView txt = (TextView) tmp.findViewById(R.id.txt);
		txt.setText("分享视频");

		img = (ImageView) findViewById(R.id.img);
		video = (VideoView) findViewById(R.id.video);
		video.setMediaController(new MediaController(this));
		play_btn = findViewById(R.id.play_btn);
		play_btn.setOnClickListener(this);
		imageLoader.displayImage(Scheme.FILE.wrap(image), img, options);
		video.setVideoPath(file);

		// 上传并设为闪铃
		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				checked = isChecked;
			}
		});
		// 名字编辑
		nameEdit = (EditText) findViewById(R.id.editText1);
		// 上传按钮
		findViewById(R.id.upload_btn).setOnClickListener(this);
	}

	TyuTask obj = null;
	Dialog dialog = null;

	public void startUpload() {
		stop();
		final String name = nameEdit.getText().toString();
		if (TextUtils.isEmpty(name)) {
			TyuCommon.showToast(this, "请设置视频的名称");
			return;
		}
		mLocalData.name = name;
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.fv_progress_layout, null);
		dialog = new Dialog(this, R.style.selectorDialog);
		// 当然也可以手动设置PopupWindow大小。
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(menuView);

		final ProgressBar pb = (ProgressBar) menuView
				.findViewById(R.id.progressBar1);
		final TextView message = (TextView) menuView
				.findViewById(R.id.textView1);
		final View ok_btn = menuView.findViewById(R.id.bt1);

		final View cancel_btn = menuView.findViewById(R.id.bt2);
		final View divider = menuView.findViewById(R.id.divider);
		dialog.show();
		// 初始化：
		pb.setMax(100);
		pb.setProgress(0);
		message.setText("正在上传...");
		ok_btn.setVisibility(View.GONE);
		divider.setVisibility(View.GONE);
		obj = new TyuTask(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = TyuDefine.HOST + "smc/post_media_base_by_user";
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", name);
				map.put("file", new File(file));
		//		map.put("user_id", TyuUserInfo.getInstance().user_id + "");
				obj.length = new File(file).length();
				obj.curLen = 0;
				obj.stop_upload = false;
				String res = TyuHttpClientUtils.postMutiPart(url, map,
						new TyuUploadListener() {

							@Override
							public void onStart(String aUrl) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onProgressChanged(String aUrl,
									long aValue) {
								// TODO Auto-generated method stub
								obj.curLen += aValue;

								TyuContextKeeper.getHandler().post(
										new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												String msg = String
														.format("正在上传...%s/%s",
																TyuCommon
																		.getFitableSize(obj.curLen),
																TyuCommon
																		.getFitableSize(obj.length));
												message.setText(msg);
												pb.setProgress((int) (obj.curLen * 100 / obj.length));
											}
										});
							}

							@Override
							public void onError(String aUrl, String aInfo) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onEnd(String aUrl, String aInfo) {
								// TODO Auto-generated method stub
								TyuContextKeeper.getHandler().post(
										new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												message.setText("正在保存配置...");
//												ok_btn.setVisibility(View.VISIBLE);
												// divider.setVisibility(View.VISIBLE);
//												cancel_btn
//														.setVisibility(View.GONE);
											}

										});

							}

							@Override
							public boolean stopped() {
								// TODO Auto-generated method stub
								return obj.stop_upload;
							}
						});
				// 配置数据
				if (!TextUtils.isEmpty(res)) {
					VideoItemData data = new VideoItemData();
					if (data.initData(res)) {
						try {
							TyuFileUtils.copyFile(file,
									data.local_file.getAbsolutePath());
							new File(file).delete();

							if (checked) {
								TyuShinningData.getInstance().setCurShinning(
										data);
								// 更新用户数据
//								TyuUserInfo.getInstance().current_mb_id = data.mb_id;
//								TyuUserInfo.getInstance().updateInfo();
							}
							// TyuUserInfo.getInstance().getMyVideos().add(data);
							mLocalData.video_path = data.local_file
									.getAbsolutePath();
							mLocalData.extraData = data;
					//		TyuUserInfo.getInstance().saveMyVideos();
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									message.setText("上传完成");
									ok_btn.setVisibility(View.VISIBLE);
									// divider.setVisibility(View.VISIBLE);
									cancel_btn
											.setVisibility(View.GONE);
								}
							});
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		obj.start();
		ok_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		cancel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				obj.stop_upload = true;
				finish();
			}
		});
	}

	class TyuTask extends Thread {
		public boolean stop_upload = false;
		public long curLen = 0;
		public long length = 0;

		public TyuTask(Runnable aRun) {
			super(aRun);
		}
	}

	public void play() {
		if (showing)
			return;
		video.setVisibility(View.VISIBLE);
		play_btn.setVisibility(View.INVISIBLE);
		img.setVisibility(View.INVISIBLE);
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
		video.setVisibility(View.INVISIBLE);
		play_btn.setVisibility(View.VISIBLE);
		img.setVisibility(View.VISIBLE);
		showing = false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		String name = nameEdit.getText().toString();
		if (!TextUtils.isEmpty(name)) {
			mLocalData.name = name;
		}
//		
		stop();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.play_btn:
			play();
			break;
		case R.id.video:
			stop();
			break;
		case R.id.upload_btn:
//			if (TyuUserInfo.getInstance().isLogin()) {
//				startUpload();
//			} else {
//				TyuTools.showUserVerify(this, "上传操作需要用户已经验证手机号码:",
//						new Runnable() {
//
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								startUpload();
//							}
//						});
//			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		TyuUserInfo.getInstance().saveMyVideos();
//		if (dialog != null && dialog.isShowing()) {
//			dialog.dismiss();
//		}
	}
}

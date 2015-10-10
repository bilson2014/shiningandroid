package com.panfeng.shining.activity.newactivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import tyu.common.utils.TyuPreferenceManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.activity.s2nd.FindMyMediaProductionActivity;
import com.panfeng.shining.activity.s2nd.ShowMyMediaActivity;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.entity.UserInfoEntity;
import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shining.slw.net.DownloadFIle;
import com.panfeng.shining.slw.net.MyHttpUtils;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.slw.utils.FileUtils;
import com.panfeng.shining.slw.utils.LogUtils;
import com.panfeng.shining.tools.CommonTools;
import com.panfeng.shining.tools.FileCopy;
import com.panfeng.shining.tools.MtaTools;
import com.panfeng.shining.tools.NetTools;
import com.panfeng.shining.tools.ShareTools;
import com.panfeng.shining.tools.UserControl;
import com.panfeng.shining.widgets.HorizontalListView;
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

public class ShowAllMediaActivity extends Activity implements OnClickListener {
	private HorizontalListView keyList;
	
	Dialog firstDialog;

	// 路径
	String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	final String imageLocaPath = pathString + File.separator
			+ "com.panfeng.shinning" + File.separator + "videoImage/";// 图片
	final String videoLocaPath = pathString + File.separator
			+ "com.panfeng.shinning" + File.separator + "mySave/";// 我的收藏
	String newPath = pathString + "/" + "com.panfeng.shinning" + "/"// 我的闪铃
			+ "myShinVideo/myShin.mp4";
	String toSave = pathString + "/" + "com.panfeng.shinning/";
	String mediaPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/mylist";// 音乐

	// 关键字基础数据
	Context ctx = ShowAllMediaActivity.this;
	String urlid;// 分享用id
	Dialog showto, dialog;
	MediaPlayer mediaPlayer;
	String share_content = "";
	File file;
	String str, name, videoname, from;

	String share_url = "";
	ImageView save, set, share, before_start;
	SurfaceView v;
	Uri u; // 网络图片地址
	private ImageView moreinfo;
	boolean isCheck = true;// 是否选择

	// myhandler myh = new myhandler();

	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	ArrayList<View> itemViewList = new ArrayList<View>();

	// 工具
	DBAdapter db = new DBAdapter(ctx);
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = TyuApp.getCommonConfig();
	com.panfeng.shining.progress.ArcProgressLu pb;
	FileCopy fc = new FileCopy();

	// slw
	private String imagepath;
	private boolean init = false;
	private boolean display = false;
	private DownloadFIle downloadFIle;
	private static final int START_MEDIA_PALY = 0x1131;
	private Message msg;
	private MyHandler mHandler;
	private String savePath;
	private String netPath;
	private int UPDATEIMAGE = 0x123;
	private int REVIER = 0x1234;
	private int Cancle = 0x1235;
	private int SET = 0x12345;
    VideoEntityLu videoEntityLu;

	@Override
	protected void onDestroy() {
		Log.i("", "onDestroy()");

		super.onDestroy();
		stopMediaPlay();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		setContentView(R.layout.show_media);
		

		Intent intent = this.getIntent();
		mHandler = new MyHandler();
		videoEntityLu=(VideoEntityLu) getIntent().getExtras().getSerializable("obj");
		Log.e("xyz", videoEntityLu.toString());
		init();
		UserInfoEntity uie = UserControl.getUserInfo();
		if (uie == null) {
			isCheck = false;
		}
		init = true;
		display = true;

		downLoadImg();


		db.open();

		// 确认是否收藏

		Cursor c = db.getAllPeople(videoEntityLu.getVideoID() + "");

		if (c.moveToFirst()) {

			save.setBackgroundResource(R.drawable.saved_video);

		}

		db.close();

		convert(videoEntityLu);
		if (TyuPreferenceManager.isFirstOpen("firstJoinVideo")) {
			firstJoin();
		} else {
			downloadInit();
		}

	}
	
	private void firstJoin() {
		View view = LayoutInflater.from(ctx).inflate(
				R.layout.show_first_media, null);
		ImageView imgset = (ImageView) view.findViewById(R.id.firstvideo_set);
		ImageView imgclose = (ImageView) view.findViewById(R.id.closefirstview);

		firstDialog = new Dialog(ctx,
				R.style.activity_theme_transparent);

		firstDialog.setCanceledOnTouchOutside(false);
		firstDialog.setContentView(view);
		firstDialog.setCancelable(true);
		firstDialog.show();
		imgset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				toSet();
				firstDialog.dismiss();

			}
		});

		imgclose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				downloadInit();
				firstDialog.dismiss();

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
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
			}
		} catch (IllegalStateException e) {
			// e.printStackTrace();
		}

	}

	private void initMediaPaly(File file) {
		try {
			if (file != null) {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				// 设置播放的视频源
				mediaPlayer.setDisplay(v.getHolder());
				v.getHolder().addCallback(callback);
				mediaPlayer.setDataSource(file.getAbsolutePath());
				mediaPlayer.prepareAsync();
				mediaPlayer
						.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

							@Override
							public void onPrepared(MediaPlayer mp) {

								pb.setVisibility(View.GONE);
								before_start.setVisibility(View.GONE);
								// textView.setVisibility(View.GONE);
								if (display && init) {
									// 装载完成
									// 设置显示视频的SurfaceHolder

									mediaPlayer.start();
									mediaPlayer.setLooping(true);
								} else {
									stopMediaPlay();
								}

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
			LogUtils.writeErrorLog("播放视频文件", "", e, null);
		}
	}

	private boolean isForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;

			Log.i("slw", cpn.getClassName());

			if (className.equals(cpn.getClassName())) {

				return true;
			}
		}

		return false;
	}

	public void init() {

		v = (SurfaceView) findViewById(R.id.Allvideo);

		save = (ImageView) findViewById(R.id.myvideo_save);
		save.setOnClickListener(this);

		set = (ImageView) findViewById(R.id.myvideo_set);
		set.setOnClickListener(this);
		


		share = (ImageView) findViewById(R.id.myvideo_share);
		share.setOnClickListener(this);
		moreinfo = (ImageView) findViewById(R.id.moreinfo);
		moreinfo.setOnClickListener(this);

		before_start = (ImageView) findViewById(R.id.before_start);
		pb = (com.panfeng.shining.progress.ArcProgressLu) findViewById(R.id.show_video_arc_progress);
		pb.setProgress(29);

		Intent intent = this.getIntent();

		


		share_url = TyuDefine.URL + "share.jsp?id=" + videoEntityLu.getVideoID();



		netPath = videoEntityLu.getVideoUrl();
		Log.e("xyz", netPath);
		savePath = DefindConstant.saveDownaLoadVideoPath
				+ videoEntityLu.getVideoFileName();

		try {

			if (videoEntityLu == null) {
				return;
			} else {
				File image = new File(DefindConstant.saveDownaLoadImagePath
						+ FileUtils.videoNameConvertImageName(videoEntityLu
								.getVideoFileName()));

				if (image.exists()) {

					FileInputStream fis = new FileInputStream(image);
					before_start
							.setImageBitmap(BitmapFactory.decodeStream(fis));

					fis.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 缓存图片下载
	 */

	private void downLoadImg() {

		String imgtop = videoEntityLu.getImageUrl();

		if (imgtop.equals("")) {
			before_start.setImageDrawable(ctx.getResources().getDrawable(
					R.drawable.ifnot));

		} else {
			imageLoader.displayImage(imgtop, before_start, options);
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.myvideo_share:

			ShareTools.configPlatforms(ctx);
			ShareTools.setShareContent(ctx, share_url, share_content);
			toShare();
			break;

		case R.id.myvideo_set:
			
			
			


			ShareTools.configPlatforms(ctx);
			ShareTools.setShareContent(ctx, share_url, share_content);

			SharedPreferences pre = ctx.getSharedPreferences("configs",
					Context.MODE_PRIVATE);
			int isrInteger = pre.getInt("isSetMyVideo", 0);

			if (isrInteger == videoEntityLu.getVideoID()) {

				SharedPreferences preference = ctx.getSharedPreferences(
						"configs", Context.MODE_PRIVATE);
				int isrIntege = preference.getInt("isNoMore", 0);
				Intent intent = this.getIntent();
				
				int isMi = preference.getInt("isMIUI", 0);
				int isMei = preference.getInt("isMeiZu", 0);
				int isHol = preference.getInt("isHol", 0);

				CommonTools cy = new CommonTools();
			
				
				if(isrIntege!=1){
					if(isMi==1||isMei==1||isHol==1)
					cy.showDialog(ctx,0);
				}
				Toast.makeText(ctx, "已经设置过该视频", Toast.LENGTH_SHORT).show();
			} else {

				toSet();

			}

			// }

			break;

		case R.id.myvideo_save:

			db.open();
			Cursor c = db.getAllPeople(videoEntityLu.getVideoID() + "");

			if (c.moveToFirst()) {

				// 取消收藏
				db.deleteDb(c.getLong(0));
				save.setBackgroundResource(R.drawable.tosave);
				File file = new File(videoLocaPath + videoEntityLu.getVideoID() + ".mp4");
				file.delete();
				db.close();

				Message msg = new Message();
				msg = new Message();
				msg.what = Cancle;
				msg.obj = save;
				mHandler.sendMessage(msg);

			} else {
				db.close();

				Message msg = new Message();
				msg.what = UPDATEIMAGE;
				msg.obj = save;
				mHandler.sendMessage(msg);

				new Thread(new Runnable() {
					@Override
					public void run() {

						int res = TyuHttpClientUtils.downLoadFile(videoEntityLu.getVideoUrl(),
								videoLocaPath + videoEntityLu.getVideoID() + ".mp4", null, null);
						
						TyuHttpClientUtils.downLoadFile(videoEntityLu.getImageUrl(),
								imageLocaPath + videoEntityLu.getVideoID() + ".jpg", null, null);

						if (res >= 0) {

							try {
								fc.fileCopy(videoLocaPath + videoEntityLu.getVideoID() + ".mp4",
										newPath);
								DBAdapter db = new DBAdapter(
										ShowAllMediaActivity.this);
								db.open();
								long l = db.insertDb(videoEntityLu.getVideoID() + "", "", "", 0);
								db.close();
								Message msg = new Message();
								msg.what = REVIER;
								msg.obj = save;
								mHandler.sendMessage(msg);

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				}).start();

			}

			break;

		case R.id.moreinfo:

			finish();
			break;
			
	

		}
	}

	public boolean fileIsExists() {
		try {
			File f = new File(newPath);
			if (!f.exists()) {
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	private void toSet() {
		
		


		LayoutInflater mLayout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgv = (ViewGroup) mLayout.inflate(R.layout.setvideo, null);
		showto = new Dialog(ctx, R.style.activity_theme_transparent);

		showto.setCanceledOnTouchOutside(false);
		showto.setContentView(dlgv);
		showto.setCancelable(false);
		showto.show();

		ImageView sure = (ImageView) dlgv.findViewById(R.id.sure_setvideo);

		ImageView back = (ImageView) dlgv.findViewById(R.id.sure_back);

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO　ＭＴＡ

				MtaTools.MtaSet(ctx, videoEntityLu.getVideoID() + "");

				SharedPreferences preference = ctx.getSharedPreferences(
						"configs", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				editor.putInt("isSetMyVideo", videoEntityLu.getVideoID());
				
				editor.putString("cameraVideo", videoLocaPath + videoEntityLu.getVideoID()
						+ ".mp4");

				editor.commit();
				
				DBAdapter db = new DBAdapter(ctx);
				db.open();
				
                Cursor c=db.getMyVideo();
				
				if(c.moveToFirst()&&c.getInt(0)==1){
					db.updateMyVideo(1, 1);
				}
				
				else{
					
					db.insertMyVideo(1);
				}
				
				db.close();

				save.setBackgroundResource(R.drawable.saved_video);
				save.setEnabled(false);

				new Thread(new Runnable() {
					@Override
					public void run() {

						int res = TyuHttpClientUtils.downLoadFile(videoEntityLu.getVideoUrl(),
								videoLocaPath + videoEntityLu.getVideoID() + ".mp4", null, null);

						int reus = TyuHttpClientUtils.downLoadFile(videoEntityLu.getImageUrl(),
								imageLocaPath + videoEntityLu.getVideoID() + ".jpg", null, null);
						Log.i("size", "res=" + res);
						if (res >= 0) {

							DBAdapter db = new DBAdapter(
									ShowAllMediaActivity.this);
							db.open();
							long l = db.insertDb(videoEntityLu.getVideoID() + "", "", "", 0);
							
							db.close();

							try {
								fc.fileCopy(videoLocaPath + videoEntityLu.getVideoID() + ".mp4",
										newPath);



							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				}).start();

				toSetVideo();
				mHandler.sendEmptyMessage(SET);
				


			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadInit();
				showto.dismiss();

			}
		});

	}

	private void toShare() {

		final SHARE_MEDIA[] share_ids = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.SINA, };

		LayoutInflater mLayout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgv = (ViewGroup) mLayout.inflate(R.layout.sharevideo, null);
		showto = new Dialog(ctx, R.style.activity_theme_transparent);

		showto.setCanceledOnTouchOutside(false);
		showto.setContentView(dlgv);
		showto.setCancelable(false);
		showto.show();

		ImageView py = (ImageView) dlgv.findViewById(R.id.sure_py);
		ImageView wx = (ImageView) dlgv.findViewById(R.id.sure_wx);
		ImageView qq = (ImageView) dlgv.findViewById(R.id.sure_qq);
		ImageView wb = (ImageView) dlgv.findViewById(R.id.share_sure_wb);
		ImageView back = (ImageView) dlgv.findViewById(R.id.share_sure_back);

		py.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (NetTools.checkNetwork(ctx) && isCheck) {
					ShareTools.performShare(share_ids[2], ctx);
					Properties prop = new Properties();
					prop.setProperty("type", "朋友圈");
					prop.setProperty("share_id", videoEntityLu.getVideoID() + "");
					StatService.trackCustomKVEvent(ctx, "share", prop);

				} else {
					Toast.makeText(ctx, "分享失败请稍后再试", Toast.LENGTH_SHORT).show();
				}
			}
		});

		wx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (NetTools.checkNetwork(ctx) && isCheck) {
					ShareTools.performShare(share_ids[1], ctx);
					String type = "微信";
					MtaTools.MtaShare(ctx, type, videoEntityLu.getVideoID() + "");
					showto.dismiss();

				} else {
					Toast.makeText(ctx, "分享失败请稍后再试", Toast.LENGTH_SHORT).show();
				}
			}
		});

		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (NetTools.checkNetwork(ctx) && isCheck) {
					ShareTools.performShare(share_ids[0], ctx);
					String type = "QQ";
					MtaTools.MtaShare(ctx, type, videoEntityLu.getVideoID() + "");

				} else {
					Toast.makeText(ctx, "分享失败请稍后再试", Toast.LENGTH_SHORT).show();
				}
			}
		});

		wb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (NetTools.checkNetwork(ctx) && isCheck) {
					ShareTools.performShare(share_ids[3], ctx);
					String type = "微博";
					MtaTools.MtaShare(ctx, type, videoEntityLu.getVideoID() + "");
					showto.dismiss();

				} else {
					Toast.makeText(ctx, "分享失败请稍后再试", Toast.LENGTH_SHORT).show();
				}
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showto.dismiss();

			}
		});

	}

	private void toSetVideo() {
		
		
	

		final SHARE_MEDIA[] share_ids = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.SINA, };

		LayoutInflater mLayout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgv = (ViewGroup) mLayout.inflate(R.layout.tolook, null);
		dialog = new Dialog(ctx, R.style.activity_theme_transparent);

		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(dlgv);
		dialog.setCancelable(false);
		dialog.show();
		
		
		SharedPreferences preference = ctx.getSharedPreferences(
				"configs", Context.MODE_PRIVATE);
		int isrInteger = preference.getInt("isNoMore", 0);
		int isMi = preference.getInt("isMIUI", 0);
		int isMei = preference.getInt("isMeiZu", 0);
		int isHol = preference.getInt("isHol", 0);

		CommonTools ct = new CommonTools();
		
		
		
		if(isrInteger!=1){
			if(isMi==1||isMei==1||isHol==1)
			ct.showDialog(ctx,0);
		}

		TextView reToSet = (TextView) dlgv.findViewById(R.id.tolookText);
		TextView reToSets = (TextView) dlgv.findViewById(R.id.tolookText5);
		reToSet.setText("已成功设置为我的闪铃！");
		reToSets.setText("快分享给好友吧！");

		TextView reToLook = (TextView) dlgv
				.findViewById(R.id.my_surface_shinning);

		ImageView reback = (ImageView) dlgv
				.findViewById(R.id.my_surface_cancle);

		ImageView wb = (ImageView) dlgv.findViewById(R.id.wb);

		ImageView py = (ImageView) dlgv.findViewById(R.id.py);

		ImageView weixin = (ImageView) dlgv.findViewById(R.id.weixin);

		ImageView qq = (ImageView) dlgv.findViewById(R.id.qq);

		final SHARE_MEDIA[] share_idss = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.SINA, };

		reToLook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(ctx, FindMyMediaProductionActivity.class);
				startActivity(intent);
				showto.cancel();

			}
		});

		reback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
				showto.cancel();

			}
		});

		weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ShareTools.performShare(share_ids[1], ctx);

			}
		});

		py.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ShareTools.performShare(share_ids[2], ctx);

			}
		});

		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ShareTools.performShare(share_ids[0], ctx);

			}
		});

		wb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ShareTools.performShare(share_ids[3], ctx);

			}
		});

	}

	// private int UPDATEIMAGE = 0x123;
	// private int REVIER = 0x1234;
	// private int Cancle = 0x1235;
	//
	// class myhandler extends Handler {
	// @Override
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	//
	// if (msg.what == UPDATEIMAGE) {
	// Toast.makeText(ShowAllMediaActivity.this, "收藏成功",
	// Toast.LENGTH_SHORT).show();
	//
	// // TODO MTA
	//
	// MtaTools.MtaSave(ctx, videoID + "");
	//
	// ImageView im = (ImageView) msg.obj;
	// im.setBackgroundResource(R.drawable.saved_video);
	// im.setEnabled(false);
	//
	// } else if (msg.what == REVIER) {
	//
	// ImageView im = (ImageView) msg.obj;
	// im.setEnabled(true);
	// } else if (msg.what == Cancle) {
	// Toast.makeText(ShowAllMediaActivity.this, "取消收藏",
	// Toast.LENGTH_SHORT).show();
	// if (downloadFIle != null) {
	// downloadFIle.stopDownload();
	// }
	// }
	//
	// }
	// }

	// new code by slw

	public void convert(VideoEntityLu item) {

		String VideoFileNameString = item.getVideoFileName();

		String imageName = FileUtils
				.videoNameConvertImageName(VideoFileNameString);
		File f = new File(DefindConstant.saveDownaLoadImagePath + imageName);
		imagepath = f.getAbsolutePath();
		if (f.exists()) {
			// ShiningApplication.bitmapUtils.display((ImageView)
			// viewHolder.getView(R.id.imageView1), imagepath, new
			// CustomBitmapLoadLocaCallBack());
			Bitmap bm = BitmapFactory.decodeFile(imagepath);
			before_start.setImageBitmap(bm);
		} else {
			String imagePahtString = TyuDefine.HOST + "media_base/"
					+ imageName;
			TyuApp.getBitmapUtils().display(before_start, imagePahtString,
					new CustomBitmapLoadNetCallBack());
		}
	}

	public class CustomBitmapLoadNetCallBack extends
			DefaultBitmapLoadCallBack<ImageView> {

		public CustomBitmapLoadNetCallBack() {
		}

		@Override
		public void onLoading(ImageView container, String uri,
				BitmapDisplayConfig config, long total, long current) {
		}

		@Override
		public void onLoadCompleted(ImageView container, String uri,
				Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
			super.onLoadCompleted(container, uri, bitmap, config, from);
			saveMyBitmap(imagepath, bitmap);
			fadeInDisplay(container, bitmap);

		}
	}

	public void saveMyBitmap(String path, Bitmap mBitmap) {
		try {
			Log.i("dawn", "saveMyBitmap" + path);
			File f = new File(path);
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			// e.printStackTrace();
			LogUtils.writeErrorLog("保存下载图片", "", e, null);
		}
	}

	private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(
			android.R.color.transparent);

	private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
		final TransitionDrawable transitionDrawable = new TransitionDrawable(
				new Drawable[] { TRANSPARENT_DRAWABLE,
						new BitmapDrawable(imageView.getResources(), bitmap) });
		imageView.setImageDrawable(transitionDrawable);
		transitionDrawable.startTransition(500);
	}

	private void downloadInit() {
		downloadFIle = new DownloadFIle(savePath, netPath);
		downloadFIle.getVideo(new RequestCallBack<File>(10) {
			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// Intent it = new Intent();
				// it.setClass(ctx, VideoBufferService.class);
				// it.putExtra("command",
				// VideoBufferService.UPDATE_EVERYDAY_NEW);
				// it.putExtra("position", position + 1);

				// pb.setVisibility(View.GONE);

				// ctx.startService(it);
				try {
					msg = new Message();
					msg.obj = responseInfo.result;
					msg.what = START_MEDIA_PALY;
					mHandler.sendMessage(msg);
					// db.save(ve);
				} catch (Exception e) {
					LogUtils.writeErrorLog("写数据库", "", e, null);
					e.printStackTrace();
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				if (!MyHttpUtils.isNetworkConnected(ctx)) {
					Toast.makeText(ctx, "网络开小差啦", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				if (!isUploading) {
					pb.setVisibility(View.VISIBLE);
					double current_ = current;
					double total_ = total;
					pb.setProgress((int) ((current_ / total_) * 100));
				}
			}

			@Override
			public void onFailure(HttpException error, String msg1) {

				if (msg1.equals("maybe the file has downloaded completely")) {
					msg = new Message();
					msg.obj = new File(DefindConstant.saveDownaLoadVideoPath
							+ videoEntityLu.getVideoFileName());
					msg.what = START_MEDIA_PALY;
					mHandler.sendMessage(msg);
				}
				Log.i("dawn", msg1);
			}
		});
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == START_MEDIA_PALY) {
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.reset();
				}
				initMediaPaly((File) msg.obj);
			}

			else if (msg.what == UPDATEIMAGE) {
				Toast.makeText(ctx, "收藏成功", Toast.LENGTH_SHORT).show();

				// TODO MTA

				MtaTools.MtaSave(ctx, videoEntityLu.getVideoID() + "");

				ImageView im = (ImageView) msg.obj;
				im.setBackgroundResource(R.drawable.saved_video);
				im.setEnabled(false);

			} else if (msg.what == REVIER) {

				ImageView im = (ImageView) msg.obj;
				im.setEnabled(true);
			} else if (msg.what == Cancle) {
				Toast.makeText(ctx, "取消收藏", Toast.LENGTH_SHORT).show();
			}
			
			else if (msg.what == SET ){
				
				new Thread(new Runnable() {

					public void run() {
					
						try {
							TyuShinningData.getInstance().getMediaByPage_set(videoEntityLu.getVideoID());
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					
					}).start();
				
			}

		}
	}

}

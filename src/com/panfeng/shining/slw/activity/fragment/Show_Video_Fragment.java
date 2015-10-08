package com.panfeng.shining.slw.activity.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import tyu.common.utils.TyuPreferenceManager;
import android.app.Activity;
import android.app.Dialog;
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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.activity.s2nd.FindMyMediaProductionActivity;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.entity.UserInfoEntity;
import com.panfeng.shining.entity.VideoEntityLu;
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
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by dawn on 2015/7/10.
 */
public class Show_Video_Fragment extends Fragment implements OnClickListener {

	public static final String TAG_VIDEO_POSITION = "tag_video_position";
	public static final String TAG_VIDEO_POSITION_START = "tag_video_position_start";
	private static final int START_MEDIA_PALY = 0x1131;
	private static final int UPDATEIMAGE = 0x123;
	private static final int REVIER = 0x1234;
	private static final int Cancle = 0x1235;

	@ViewInject(R.id.show_video_surfaceView)
	private SurfaceView surfaceView;

	@ViewInject(R.id.show_video_imageView)
	private ImageView imageView;

	@ViewInject(R.id.show_video_arc_progress)
	private com.panfeng.shining.progress.ArcProgressLu arcProgress;

	// @ViewInject(R.id.show_video_button)
	// private Button button;

	@ViewInject(R.id.show_video_editText)
	private TextView textView;

	public static int positionx;
	private int position;
	private String savePath;
	private String netPath;
	private String tag = "dawn";
	private DownloadFIle downloadFIle;
	private VideoEntityLu ve;
	private List<VideoEntityLu> list;
	private MediaPlayer mediaPlayer;
	Dialog firstDialog;

	private Message msg;
	private MyHandler mHandler;

	private boolean init = false;
	private boolean display = false;
	private Context context;
	private String imagepath;

	String urlid;// 分享用id
	Dialog showto, dialog;

	String share_content = "";
	File file;
	String str, name, videoname, from;
	String cameraPath, imgPath, videoName;
	int videoID;
	String share_url = "";
	ImageView save, set, share, before_start;
	Uri u; // 网络图片地址
	private ImageView moreinfo;
	boolean isCheck = true;// 是否选择

	DBAdapter db;

	// 路径
	final String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	final String videoLocaPath = pathString + File.separator
			+ "com.panfeng.shinning" + File.separator + "mySave/";// 我的收藏
	final String newPath = pathString + "/" + "com.panfeng.shinning" + "/"// 我的闪铃
			+ "myShinVideo/myShin.mp4";
	final String imageLocaPath = pathString + File.separator
			+ "com.panfeng.shinning" + File.separator + "videoImage/";// 图片

	FileCopy fc = new FileCopy();

	View view;

	//
	// private DbUtils db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = DefindConstant.EVERYDAY_NEW;

		mHandler = new MyHandler();
		positionx = getArguments().getInt(
				Show_Video_Fragment.TAG_VIDEO_POSITION);
		// position =
		// getArguments().getInt(Show_Video_Fragment.TAG_VIDEO_POSITION_START);
		positionx = position + positionx;
		Log.i("LEFTID", "everyday=position" + position);
		Log.i("LEFTID", "everyday=positionx" + positionx);

		// while(positionx==0){
		// positionx =
		// getArguments().getInt(Show_Video_Fragment.TAG_VIDEO_POSITION);
		// positionx = position + positionx;
		// }

		if (positionx >= list.size())
			return;
		ve = list.get(positionx);
		context = getActivity();
		db = new DBAdapter(context);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_show_video_item, container,
				false);
		ViewUtils.inject(this, view);
		init = true;

		try {
			if (ve == null)
				return view;
			File image = new File(DefindConstant.saveDownaLoadImagePath
					+ FileUtils.videoNameConvertImageName(ve.getVideoFileName()));
			if (image.exists()) {

				FileInputStream fis = new FileInputStream(image);
				imageView.setImageBitmap(BitmapFactory.decodeStream(fis));
				// arcProgress.setVisibility(View.GONE);

				fis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (init && display) {

			init();
		}
	}

	private void init() {
		try {

			if (FileUtils.listNotEmpty(list)) {
				if (mediaPlayer != null) {
					mediaPlayer.pause();
				}
				if (ve == null)
					return;

				initUI();

				UserInfoEntity uie = UserControl.getUserInfo();

				if (uie == null) {
					isCheck = false;
				}

				netPath = ve.getVideoUrl();
				Log.i("xyz",netPath+"netPath");
				savePath = DefindConstant.saveDownaLoadVideoPath
						+ ve.getVideoFileName();

				share_url = TyuDefine.URL + "share.jsp?id=" + ve.getVideoID();
				videoID = ve.getVideoID();

				videoName = ve.getVideoFileName();

				imgPath = ve.getImageUrl();

				cameraPath = ve.getVideoUrl();

				Log.i("size", "url" + cameraPath);
				db.open();

				// 确认是否收藏

				Cursor c = db.getAllPeople(videoID + "");

				if (c.moveToFirst()) {

					save.setBackgroundResource(R.drawable.saved_video);

				}

				db.close();

				convert(ve);
				//firstJoin();
				if (TyuPreferenceManager.isFirstOpen("firstJoinVideo")) {
					firstJoin();
				} else {
					downloadInit();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void firstJoin() {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.show_first_media, null);
		ImageView imgset = (ImageView) view.findViewById(R.id.firstvideo_set);
		ImageView imgclose = (ImageView) view.findViewById(R.id.closefirstview);

		firstDialog = new Dialog(getActivity(),
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
			imageView.setImageBitmap(bm);
		} else {
			String imagePahtString =TyuDefine.HOST + "media_base/"
					+ imageName;
			TyuApp.getBitmapUtils().display(imageView, imagePahtString,
					new CustomBitmapLoadNetCallBack());
		}

	}

	public void initUI() {

		save = (ImageView) view.findViewById(R.id.myvideo_save);
		save.setOnClickListener(this);

		set = (ImageView) view.findViewById(R.id.myvideo_set);
		set.setOnClickListener(this);

		share = (ImageView) view.findViewById(R.id.myvideo_share);
		share.setOnClickListener(this);
		moreinfo = (ImageView) view.findViewById(R.id.moreinfo);
		moreinfo.setOnClickListener(this);

		before_start = (ImageView) view.findViewById(R.id.before_start);

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			display = true;
			if (init && display) {
				init();
			}
			Log.i("dd", "setUserVisibleHint true");
		} else {
			display = false;
			stopMediaPlay();

			Log.i("dd", "setUserVisibleHint fasle");
		}

	}

	private void downloadInit() {
		downloadFIle = new DownloadFIle(savePath, netPath);
		downloadFIle.getVideo(new RequestCallBack<File>(10) {
			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// Intent it = new Intent();
				// it.setClass(context, VideoBufferService.class);
				// it.putExtra("command",
				// VideoBufferService.UPDATE_EVERYDAY_NEW);
				// it.putExtra("position", position + 1);

				arcProgress.setVisibility(View.GONE);
				Log.i("size", "position" + position + 1);
				// context.startService(it);
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
				File savepath = new File(DefindConstant.saveDownaLoadVideoPath);
				if (!savepath.exists() && !savepath.canWrite()
						&& !savepath.canRead()) {
					Toast.makeText(context, "SD卡不可读", Toast.LENGTH_SHORT)
							.show();
					savepath.mkdirs();

				}

				if (!MyHttpUtils.isNetworkConnected(context)) {
					Toast.makeText(context, "网络开小差啦", Toast.LENGTH_SHORT)
							.show();
				}
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				if (!isUploading) {
					arcProgress.setVisibility(View.VISIBLE);
					double current_ = current;
					double total_ = total;
					arcProgress.setProgress((int) ((current_ / total_) * 100));
				}
			}

			@Override
			public void onFailure(HttpException error, String msg1) {

				if (msg1.equals("maybe the file has downloaded completely")) {
					msg = new Message();
					msg.obj = new File(DefindConstant.saveDownaLoadVideoPath
							+ ve.getVideoFileName());
					msg.what = START_MEDIA_PALY;
					mHandler.sendMessage(msg);
				}
				Log.i("dawn", msg1);
			}
		});
	}

	// ////////////////////////////////视频播放相关/////////////////////////////////////////////////////

	private Callback callback = new Callback() {
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

	private void initMediaPaly(File file) {
		try {
			if (file != null) {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				// 设置播放的视频源
				mediaPlayer.setDisplay(surfaceView.getHolder());
				surfaceView.getHolder().addCallback(callback);
				mediaPlayer.setDataSource(file.getAbsolutePath());
				mediaPlayer.prepareAsync();
				mediaPlayer
						.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

							@Override
							public void onPrepared(MediaPlayer mp) {

								arcProgress.setVisibility(View.GONE);
								imageView.setVisibility(View.GONE);
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

		if (downloadFIle != null) {
			downloadFIle.stopDownload();
		}

		// try {
		// VideoEntity videoEntity
		// =db.findFirst(Selector.from(VideoEntity.class).where("Video_id", "=",
		// ve.getVideo_id()));
		// } catch (DbException e) {
		// e.printStackTrace();
		// }
	}

	// ////////////////////////////////视频播放相关/////////////////////////////////////////////////////

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == START_MEDIA_PALY) {
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.reset();
				}
				if (init && display)
					initMediaPaly((File) msg.obj);
			}

			else if (msg.what == UPDATEIMAGE) {
				Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();

				// TODO MTA

				MtaTools.MtaSave(context, videoID + "");

				ImageView im = (ImageView) msg.obj;
				im.setBackgroundResource(R.drawable.saved_video);
				im.setEnabled(false);

			} else if (msg.what == REVIER) {

				ImageView im = (ImageView) msg.obj;
				im.setEnabled(true);
			} else if (msg.what == Cancle) {
				Toast.makeText(context, "取消收藏", Toast.LENGTH_SHORT).show();
			}

			else if (msg.what == 0x1234467) {

				new Thread(new Runnable() {

					public void run() {
						try {

							TyuShinningData.getInstance().getMediaByPage_set(videoID);

						} catch (Exception e) {
							// TODO: handle exception
						}
					}

				}).start();

			}

		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.myvideo_share:

			ShareTools.configPlatforms(context);
			ShareTools.setShareContent(context, share_url, share_content);
			toShare();
			break;

		case R.id.myvideo_set:

			ShareTools.configPlatforms(context);
			ShareTools.setShareContent(context, share_url, share_content);

			SharedPreferences pre = context.getSharedPreferences("configs",
					Context.MODE_PRIVATE);
			int isrInteger = pre.getInt("isSetMyVideo", 0);

			if (isrInteger == videoID) {

				SharedPreferences preference = context.getSharedPreferences(
						"configs", Context.MODE_PRIVATE);
				int isrIntege = preference.getInt("isNoMore", 0);
				int isMi = preference.getInt("isMIUI", 0);
				int isMei = preference.getInt("isMeiZu", 0);
				int isHol = preference.getInt("isHol", 0);

				CommonTools ct = new CommonTools();

				Toast.makeText(context, "已经设置过该视频", Toast.LENGTH_SHORT).show();

				if (isrIntege != 1) {

					if (isMi == 1 || isMei == 1 || isHol == 1)
						ct.showDialog(context, 0);
				}
			} else {

				toSet();

			}

			// }

			break;

		case R.id.myvideo_save:

			// Log.i("size", videoID + "save");

			db.open();
			Cursor c = db.getAllPeople(videoID + "");

			if (c.moveToFirst()) {
				// 取消收藏
				// Log.i("dawn", c.getLong(1) + "save");
				db.deleteDb(c.getLong(0));
				save.setBackgroundResource(R.drawable.tosave);
				File file = new File(videoLocaPath + videoID + ".mp4");
				if (file.exists())
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

						int res = TyuHttpClientUtils.downLoadFile(cameraPath,
								videoLocaPath + videoID + ".mp4", null, null);

						int reus = TyuHttpClientUtils.downLoadFile(imgPath,
								imageLocaPath + videoID + ".jpg", null, null);

						if (res >= 0) {

							try {
								fc.fileCopy(videoLocaPath + videoID + ".mp4",
										newPath);
								DBAdapter db = new DBAdapter(context);
								db.open();
								long l = db.insertDb(videoID + "", "", "", 0);
								Log.i("slw", videoID + "   121231   " + l);
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

			((Activity) context).finish();
			break;

		}

	}

	private void toSet() {

		View dlgv = LayoutInflater.from(context).inflate(R.layout.setvideo,
				null, false);

		showto = new Dialog(context, R.style.activity_theme_transparent);

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

				MtaTools.MtaSet(context, videoID + "");

				SharedPreferences preference = context.getSharedPreferences(
						"configs", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				editor.putInt("isSetMyVideo", videoID);

				editor.putString("cameraVideo", videoLocaPath + videoID
						+ ".mp4");

				editor.commit();

				DBAdapter db = new DBAdapter(context);
				db.open();

				Cursor c = db.getMyVideo();

				if (c.moveToFirst() && c.getInt(0) == 1) {
					db.updateMyVideo(1, 1);
				}

				else {

					db.insertMyVideo(1);
				}

				db.open();
				long l = db.insertDb(videoID + "", "", "", 0);
				db.close();

				save.setBackgroundResource(R.drawable.saved_video);
				save.setEnabled(false);

				new Thread(new Runnable() {
					@Override
					public void run() {

						int res = TyuHttpClientUtils.downLoadFile(cameraPath,
								videoLocaPath + videoID + ".mp4", null, null);

						int reus = TyuHttpClientUtils.downLoadFile(imgPath,
								imageLocaPath + videoID + ".jpg", null, null);
						// Log.i("size", "res=" + res);
						// Log.i("size", "res=" + cameraPath);
						// Log.i("size", "res=" + videoLocaPath + videoID +
						// ".mp4");
						if (res >= 0) {

							DBAdapter db = new DBAdapter(context);
							db.open();

							try {
								fc.fileCopy(videoLocaPath + videoID + ".mp4",
										newPath);

								Log.i("sizePath", videoID + "");
								Log.i("sizeNew", newPath);

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				}).start();

				toSetVideo();

				mHandler.sendEmptyMessage(0x1234467);

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

		View dlgv = LayoutInflater.from(context).inflate(R.layout.sharevideo,
				null, false);
		// 自定义布局

		showto = new Dialog(context, R.style.activity_theme_transparent);

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

				if (NetTools.checkNetwork(context) && isCheck) {
					ShareTools.performShare(share_ids[2], context);
					Properties prop = new Properties();
					prop.setProperty("type", "朋友圈");
					prop.setProperty("share_id", videoID + "");
					StatService.trackCustomKVEvent(context, "share", prop);

				} else {
					Toast.makeText(context, "分享失败请稍后再试", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		wx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (NetTools.checkNetwork(context) && isCheck) {
					ShareTools.performShare(share_ids[1], context);
					String type = "微信";
					MtaTools.MtaShare(context, type, videoID + "");
					showto.dismiss();

				} else {
					Toast.makeText(context, "分享失败请稍后再试", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (NetTools.checkNetwork(context) && isCheck) {
					ShareTools.performShare(share_ids[0], context);
					String type = "QQ";
					MtaTools.MtaShare(context, type, videoID + "");

				} else {
					Toast.makeText(context, "分享失败请稍后再试", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		wb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (NetTools.checkNetwork(context) && isCheck) {
					ShareTools.performShare(share_ids[3], context);
					String type = "微博";
					MtaTools.MtaShare(context, type, videoID + "");
					showto.dismiss();

				} else {
					Toast.makeText(context, "分享失败请稍后再试", Toast.LENGTH_SHORT)
							.show();
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

		View dlgv = LayoutInflater.from(context).inflate(R.layout.tolook, null,
				false);

		dialog = new Dialog(context, R.style.activity_theme_transparent);

		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(dlgv);
		dialog.setCancelable(false);
		dialog.show();

		SharedPreferences preference = context.getSharedPreferences("configs",
				Context.MODE_PRIVATE);
		int isrIntege = preference.getInt("isNoMore", 0);

		int isMi = preference.getInt("isMIUI", 0);
		int isMei = preference.getInt("isMeiZu", 0);
		int isHol = preference.getInt("isHol", 0);

		CommonTools ct = new CommonTools();

		if (isrIntege != 1) {

			if (isMi == 1 || isMei == 1 || isHol == 1)

				ct.showDialog(context, 0);
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
				intent.setClass(context, FindMyMediaProductionActivity.class);
				startActivity(intent);
				showto.cancel();

			}
		});

		reback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((Activity) context).finish();
				showto.cancel();

			}
		});

		weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ShareTools.performShare(share_ids[1], context);

			}
		});

		py.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ShareTools.performShare(share_ids[2], context);

			}
		});

		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ShareTools.performShare(share_ids[0], context);

			}
		});

		wb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ShareTools.performShare(share_ids[3], context);

			}
		});

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

}

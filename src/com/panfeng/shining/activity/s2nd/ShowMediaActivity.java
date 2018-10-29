package com.panfeng.shining.activity.s2nd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.maxwin.view.XListView;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import tyu.common.net.TyuHttpClientUtils.TyuUploadListener;
import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuContextKeeper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.entity.UserInfoEntity;
import com.panfeng.shining.tools.FileCopy;
import com.panfeng.shining.tools.MtaTools;
import com.panfeng.shining.tools.NetTools;
import com.panfeng.shining.tools.ShareTools;
import com.panfeng.shining.tools.UserControl;
import com.panfeng.shining.widgets.HorizontalListView;
import com.panfeng.shinning.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
/*import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;友盟*/

public class ShowMediaActivity extends Activity implements OnClickListener {
	private HorizontalListView keyList;

	private View netErrorNote;
	private View settingErrorNote;
	Context ctx = ShowMediaActivity.this;
	String urlid = null;
	MediaPlayer mediaPlayer;

	// 视频list数据

	FileCopy fc = new FileCopy();
/*友盟	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");*/
	String share_url = "";

	String share_content = "";

	int where;

	TyuTask obj = null;
	Dialog dialog, findtoset, toshare;
	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	private XListView mediaList;
	private RelativeLayout funny, starts, crazy, lovely, unlimit, daydayup,
			movie, miku;
	ArrayList<View> itemViewList = new ArrayList<View>();
	ImageView del, set, share, upVideo, moreinfo;
	// 关键字基础数据
	File file;
	boolean isCopy = false;
	String str, name, videoname;
	boolean from;
	private int keySelectedPosition = 0;
	String mediaPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/mylist";
	private Context context = ShowMediaActivity.this;
	SurfaceView v;
	boolean oprepared = false;
	boolean init = true;
	Dialog showset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.s2nd_my_media_list_layout);

		v = (SurfaceView) findViewById(R.id.video);
		init();

		Intent intent = this.getIntent();
		name = intent.getStringExtra("path");
		videoname = intent.getStringExtra("videoname");
		isCopy = intent.getBooleanExtra("isCopy", false);
		where = intent.getExtras().getInt("where");

		Log.i("add", "isCopy" + isCopy);

		from = intent.getBooleanExtra("isup", false);

		if (where != 1) {

			if (from) {
				Log.i("lutao", "true!@#!@#!$@@%");

				upVideo.setVisibility(View.VISIBLE);

			} else {
				geturl();
				upVideo.setVisibility(View.INVISIBLE);
				share.setVisibility(View.VISIBLE);

			}

		}

		initMediaPaly(new File(name));

		// v.setVideoPath(name);
		// v.seekTo(1);
		// v.start();

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

	}

	public void stopWindows() {
		init = false;
		stopMediaPlay();

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

			oprepared = true;
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
				v.getHolder().addCallback(callback);
				mediaPlayer.setDataSource(file.getAbsolutePath());
				mediaPlayer.prepareAsync();
				mediaPlayer
						.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
							@Override
							public void onPrepared(MediaPlayer mp) {
								// 装载完成
								// 设置显示视频的SurfaceHolder

								if (oprepared) {
									mediaPlayer.setDisplay(v.getHolder());
									mediaPlayer.setLooping(true);
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

	private void delNet() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String res = TyuHttpClientUtils.postInfo(TyuDefine.HOST
						+ "smc/deleteUserVideo?userId="
						+ UserControl.getUserInfo().getUserId() + "&videoName="
						+ videoname, "");
				// String res=
				// TyuHttpClientUtils.postInfo(TyuDefine.HOST+"smc/getUserVideoByFileName?userId="+TyuUserInfo.getInstance().user_id+"&videoName="+videoname,"");
				Log.i("lutao", "res=" + res);
			}
		}).start();
	}

	public void init() {

		upVideo = (ImageView) findViewById(R.id.up_cameraupvideo);
		upVideo.setOnClickListener(this);

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

	private void getNet() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String res = TyuHttpClientUtils.postInfo(TyuDefine.HOST
						+ "smc/getUserVideoByUserID"
						+ UserControl.getUserInfo().getUserId(), "");
				// String res=
				// TyuHttpClientUtils.postInfo(TyuDefine.HOST+"smc/getUserVideoByFileName?userId="+TyuUserInfo.getInstance().user_id+"&videoName="+videoname,"");
				Log.i("lutao", "res=" + res);

			}
		}).start();
	}

	private void geturl() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				// String res=
				// TyuHttpClientUtils.postInfo(TyuDefine.HOST+"smc/deleteUserVideo?userId="+TyuUserInfo.getInstance().user_id+"&videoName="+videoname,"");
				UserInfoEntity uie = UserControl.getUserInfo();

				Log.i("testfind", UserControl.getUserInfo().getUserId() + "");
				Log.i("testfind", videoname + "");
				if (uie != null) {

					urlid = TyuHttpClientUtils.postInfo(TyuDefine.HOST
							+ "smc/getUserVideoByFileName?userId="
							+ UserControl.getUserInfo().getUserId()
							+ "&videoName=" + videoname, "");
					Log.i("dirlist", "UserControl"
							+ UserControl.getUserInfo().getUserId());
					Log.i("dirlist", "urlid" + urlid);
					Log.i("dirlist", "videoname" + videoname);

				}

			}
		}).start();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.myvideo_del:

			toDel();

			break;

		case R.id.myvideo_set:

			toSet();

			break;

		case R.id.myvideo_share:

			UserInfoEntity uies = UserControl.getUserInfo();

			if (urlid != null && uies != null) {

				share_url = TyuDefine.URL + "share.jsp?id=" + urlid + "_";
				Log.i("video", "share_url" + share_url);
				ShareTools.configPlatforms(ctx);
				ShareTools.setShareContent(ctx, share_url, share_content);

				toShare();

			} else {
				Toast.makeText(ctx, "分享失败请稍后再试", Toast.LENGTH_SHORT).show();

			}

			break;

		case R.id.moreinfo:

			finish();
			break;

		case R.id.up_cameraupvideo:
			UserInfoEntity uie = UserControl.getUserInfo();

			if (NetTools.checkNetwork(ctx) && uie != null) {

				startUpload();

			} else {

				Toast.makeText(ctx, "上传失败请稍后再试", Toast.LENGTH_SHORT).show();

			}

			break;

		}

	}

	private void toShare() {

		final SHARE_MEDIA[] share_ids = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.SINA, };

		LayoutInflater mLayout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgv = (ViewGroup) mLayout.inflate(R.layout.sharevideo, null);
		toshare = new Dialog(ctx, R.style.activity_theme_transparent);

		toshare.setCanceledOnTouchOutside(false);
		toshare.setContentView(dlgv);
		toshare.setCancelable(false);
		toshare.show();

		ImageView py = (ImageView) dlgv.findViewById(R.id.sure_py);
		ImageView wx = (ImageView) dlgv.findViewById(R.id.sure_wx);
		ImageView qq = (ImageView) dlgv.findViewById(R.id.sure_qq);
		ImageView wb = (ImageView) dlgv.findViewById(R.id.share_sure_wb);
		ImageView back = (ImageView) dlgv.findViewById(R.id.share_sure_back);

		py.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			//友盟	ShareTools.performShare(share_ids[2], ctx);

			}
		});

		wx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		//友盟		ShareTools.performShare(share_ids[1], ctx);
			}
		});

		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			//友盟	ShareTools.performShare(share_ids[0], ctx);
			}
		});

		wb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			//友盟	ShareTools.performShare(share_ids[3], ctx);
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				toshare.dismiss();

			}
		});

	}

	// /**
	// * 根据不同的平台设置不同的分享内容</br>
	// */
	// private void setShareContent() {
	// String template = "您的好友 %s 给你分享一个好玩的视频，速速来看【%s】";
	// String name = "";
	// if ( UserControl.getUserInfo().getUserName() != null) {
	// name = UserControl.getUserInfo().getUserName();
	// }
	//
	// String content = String.format(template, name, share_url);
	// share_content = content;
	// //
	// mController.setShareContent(content);
	//
	// // 微信
	// Log.i("slw", "content=" + share_content);
	// Log.i("slw", "share=" + share_url);
	//
	// WeiXinShareContent weixinContent = new WeiXinShareContent();
	// weixinContent.setShareContent(content);
	// weixinContent.setTitle("闪铃");
	// weixinContent.setTargetUrl(share_url);
	// weixinContent.setShareMedia(new UMImage(this, ""));
	// mController.setShareMedia(weixinContent);
	// // 设置朋友圈分享的内容
	// CircleShareContent circleMedia = new CircleShareContent();
	// circleMedia.setShareContent(content);
	// circleMedia.setTitle("闪铃");
	// circleMedia.setTargetUrl(share_url);
	// circleMedia.setShareMedia(new UMImage(this, ""));
	// mController.setShareMedia(circleMedia);
	//
	// // 设置QQ空间分享内容
	// QZoneShareContent qzone = new QZoneShareContent();
	// qzone.setShareContent(content);
	//
	// qzone.setTargetUrl(share_url);
	// qzone.setTitle("闪铃");
	// qzone.setShareMedia(new UMImage(this, ""));
	// mController.setShareMedia(qzone);
	// // qq分享内容
	// QQShareContent qqShareContent = new QQShareContent();
	// qqShareContent.setTitle("闪铃");
	//
	// qqShareContent.setShareContent(content);
	// qqShareContent.setTargetUrl(share_url);
	// qqShareContent.setShareMedia(new UMImage(this, ""));
	// mController.setShareMedia(qqShareContent);
	// }
	//
	// /**
	// * 配置分享平台参数</br>
	// */
	// private void configPlatforms() {
	// mController.getConfig().closeToast();
	// // 添加新浪SSO授权
	// mController.getConfig().setSsoHandler(new SinaSsoHandler());
	// // 添加腾讯微博SSO授权
	// mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
	// // // 添加人人网SSO授权
	// // RenrenSsoHandler renrenSsoHandler = new
	// // RenrenSsoHandler(getActivity(),
	// // "201874", "28401c0964f04a72a14c812d6132fcef",
	// // "3bf66e42db1e4fa9829b955cc300b737");
	// // mController.getConfig().setSsoHandler(renrenSsoHandler);
	//
	// // 添加QQ、QZone平台
	// addQQQZonePlatform();
	//
	// // 添加微信、微信朋友圈平台
	// addWXPlatform();
	// }
	//
	// /**
	// * @功能描述 : 添加微信平台分享
	// * @return
	// */
	// private void addWXPlatform() {
	// // 注意：在微信授权的时候，必须传递appSecret
	// // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
	// String appId = "wxaf12b567e6be5abf";
	// String appSecret = "a60cd6661f912d1aee285b0043332875";
	// // 添加微信平台
	// UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, appSecret);
	// wxHandler.addToSocialSDK();
	//
	// // 支持微信朋友圈
	// UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId,
	// appSecret);
	// wxCircleHandler.setToCircle(true);
	// wxCircleHandler.addToSocialSDK();
	// }
	//
	// /**
	// * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title,
	// summary,
	// * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	// * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	// * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	// * @return
	// */
	// private void addQQQZonePlatform() {
	// String appId = "100424468";
	// String appKey = "c7394704798a158208a74ab60104f0ba";
	// // 添加QQ支持, 并且设置QQ分享内容的target url
	// UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId,
	// appKey);
	//
	// qqSsoHandler.addToSocialSDK();
	//
	// // 添加QZone平台
	// QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
	// appId, appKey);
	// qZoneSsoHandler.addToSocialSDK();
	// }
	//
	// private Activity getActivity() {
	// // TODO Auto-generated method stub
	// return this;
	// }
	//
	// private void performShare(SHARE_MEDIA platform) {
	// mController.postShare(this, platform, new SnsPostListener() {
	//
	// @Override
	// public void onStart() {
	//
	// }
	//
	// @Override
	// public void onComplete(SHARE_MEDIA platform, int eCode,
	// SocializeEntity entity) {
	// String showText = platform.toString();
	// if (eCode == StatusCode.ST_CODE_SUCCESSED) {
	// showText += "平台分享成功";
	//
	//
	//
	// } else {
	// showText += "平台分享失败";
	// }
	// TyuCommon.showToast(getActivity(), showText);
	// finish();
	// }
	// });
	// }

	private void toSetVideo() {

		final SHARE_MEDIA[] share_ids = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.SINA, };

		LayoutInflater mLayout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgv = (ViewGroup) mLayout.inflate(R.layout.tolook, null);
		findtoset = new Dialog(ctx, R.style.activity_theme_transparent);

		findtoset.setCanceledOnTouchOutside(false);
		findtoset.setContentView(dlgv);
		findtoset.setCancelable(false);
		findtoset.show();

		View vi = dlgv.findViewById(R.id.lookline);
		TextView looktext = (TextView) dlgv.findViewById(R.id.tolookText9);
		TextView reToSet = (TextView) dlgv.findViewById(R.id.tolookText);
		TextView reToSets = (TextView) dlgv.findViewById(R.id.tolookText5);
		reToSet.setText("已成功设置为我的闪铃！");
		reToSets.setText("下次来电就能看到啦！");

		TextView reToLook = (TextView) dlgv
				.findViewById(R.id.my_surface_shinning);
		ImageView reback = (ImageView) dlgv
				.findViewById(R.id.my_surface_cancle);
		ImageView wb = (ImageView) dlgv.findViewById(R.id.wb);
		ImageView py = (ImageView) dlgv.findViewById(R.id.py);
		ImageView weixin = (ImageView) dlgv.findViewById(R.id.weixin);
		ImageView qq = (ImageView) dlgv.findViewById(R.id.qq);
		wb.setVisibility(View.GONE);
		py.setVisibility(View.GONE);
		weixin.setVisibility(View.GONE);
		qq.setVisibility(View.GONE);
		looktext.setVisibility(View.GONE);
		vi.setVisibility(View.GONE);

		reback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				findtoset.cancel();

			}
		});

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

		ImageView back = (ImageView) dlgv.findViewById(R.id.sure_back);

		TextView tesure = (TextView) dlgv.findViewById(R.id.set_true);

		tesure.setText("客官您确定吗？");

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				file = new File(name);

				if (file.exists()) {

					Log.i("slw", "namef=" + name);
					DBAdapter db = new DBAdapter(ctx);
					db.open();

					Log.i("slw", file.getName());
					Cursor cursor = db.getAllPeople("'"
							+ file.getName().substring(0,
									file.getName().lastIndexOf('.')) + "'");
					if (cursor.moveToFirst()) {

						db.deleteDb(cursor.getLong(0));
						db.close();

						Log.i("slw", "name=" + name);

					}
					file.delete();
					delNet();

					Toast.makeText(ShowMediaActivity.this, "删除成功",
							Toast.LENGTH_SHORT).show();

					SharedPreferences preference = ctx.getSharedPreferences(
							"configs", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = preference.edit();
					editor.putInt("isDel", 1);
					editor.commit();
				}

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

	private void toSet() {

		LayoutInflater mLayout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgv = (ViewGroup) mLayout.inflate(R.layout.setvideo, null);
		showset = new Dialog(ctx, R.style.activity_theme_transparent);

		showset.setCanceledOnTouchOutside(false);
		showset.setContentView(dlgv);
		showset.setCancelable(false);
		showset.show();

		ImageView sure = (ImageView) dlgv.findViewById(R.id.sure_setvideo);

		ImageView back = (ImageView) dlgv.findViewById(R.id.sure_back);

		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 设置完提示我的闪铃
				SharedPreferences preference = ctx.getSharedPreferences(
						"configs", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				editor.putString("cameraVideo", name);
				editor.commit();

				DBAdapter db = new DBAdapter(ctx);
				db.open();

				Cursor c = db.getMyVideo();

				if (c.moveToFirst() && c.getInt(0) == 1) {
					db.updateMyVideo(1, 1);
				}

				else {

					db.insertMyVideo(1);
				}

				if (where == 0) {

					MtaTools.Sel_SetVideo(ctx, videoname);

				}

				Toast.makeText(ctx, "设置成功", Toast.LENGTH_LONG).show();

				String pathString = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				String newPath = pathString + "/" + "com.panfeng.shinning"
						+ "/" + "myShinVideo/myShin.mp4";
				String toSave = pathString + "/" + "com.panfeng.shinning"
						+ "/mySave/" + videoname;

				Log.i("add", "path-----------" + toSave);

				try {

					// copy到我的闪铃
					fc.fileCopy(name, newPath);

					// 如果不是我的收藏移动到我的收藏
					if (isCopy) {
						fc.fileCopy(name, toSave);
						Log.i("add", "path" + "true");
					}

					else {
						Log.i("add", "path" + "false");

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

				toSetVideo();
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

	public void startUpload() {

		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.fv_progress_v2_layout, null);
		dialog = new Dialog(this, R.style.activity_theme_transparent);
		// 当然也可以手动设置PopupWindow大小。
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(menuView);
		dialog.setCancelable(false);

		final com.panfeng.shining.progress.ArcProgressLu pb = (com.panfeng.shining.progress.ArcProgressLu) menuView
				.findViewById(R.id.progressBar1);
		final TextView message = (TextView) menuView
				.findViewById(R.id.textView1);
		// final View ok_btn = menuView.findViewById(R.id.bt1);

		// final View cancel_btn = menuView.findViewById(R.id.bt2);
		// final View divider = menuView.findViewById(R.id.divider);
		dialog.show();
		// 初始化：
		pb.setMax(100);
		pb.setProgress(0);
		// message.setText("正在上传...");
		// ok_btn.setVisibility(View.GONE);
		// divider.setVisibility(View.GONE);
		obj = new TyuTask(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = TyuDefine.HOST + "smc/post_media_base_by_user";
				HashMap<String, Object> map = new HashMap<String, Object>();

				Log.i("add", "videoname=" + name);

				File fx = new File(name);
				map.put("uploadfile", fx);
				map.put("filename", fx.getName());
				map.put("width", 0 + "");
				map.put("height", 0 + "");
				map.put("userId_1", UserControl.getUserInfo().getUserId() + "");

				obj.length = new File(name).length();
				obj.curLen = 0;
				obj.stop_upload = false;
				Log.i("slw", "here" + UserControl.getUserInfo().getUserId());
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
												// message.setText(msg);
												pb.setProgress((int) (obj.curLen * 100 / obj.length));

												if (obj.curLen == obj.length) {

													// message.setText("正在保存配置...");
													geturl();

												}

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

												MtaTools.Sel_UpVideo(ctx,
														videoname);

												SharedPreferences preference = ctx
														.getSharedPreferences(
																"configs",
																Context.MODE_PRIVATE);
												SharedPreferences.Editor editor = preference
														.edit();
												editor.putInt("isDel", 2);
												editor.commit();
												// TODO Auto-generated method
												// stub

												// ok_btn.setVisibility(View.VISIBLE);
												// divider.setVisibility(View.VISIBLE);
												// cancel_btn
												// .setVisibility(View.GONE);
												share.setVisibility(View.VISIBLE);
												upVideo.setVisibility(View.INVISIBLE);

											}

										});

							}

							@Override
							public boolean stopped() {
								// TODO Auto-generated method stub
								return obj.stop_upload;

							}
						});

				try {

					if (res == null || res.equals(""))
						return;
					// throw new NullPointerException();\

					// JSONObject person = new JSONObject();
					// Log.i("slw", "urlssss=" + person);
					// person = new JSONObject(res);

					// upVideo.setVisibility(View.GONE);

				} catch (Exception e) {
					// TODO Auto-generated catch block

				}

				// 配置数据

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						// message.setText("上传完成");
						// ok_btn.setVisibility(View.VISIBLE);
						//
						// cancel_btn.setVisibility(View.GONE);

						dialog.dismiss();

					}
				});

			}
		});
		obj.start();
		// ok_btn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // finish();
		// // dialog.dismiss();
		// }
		// });s

	}

	class TyuTask extends Thread {
		public boolean stop_upload = false;
		public long curLen = 0;
		public long length = 0;

		public TyuTask(Runnable aRun) {
			super(aRun);
		}
	}

}

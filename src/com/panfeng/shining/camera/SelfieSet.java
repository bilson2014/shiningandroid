package com.panfeng.shining.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import tyu.common.net.TyuHttpClientUtils.TyuUploadListener;
import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuContextKeeper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.panfeng.shining.TyuApp;
import com.panfeng.shining.tools.UserControl;
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class SelfieSet extends Activity implements OnClickListener {

	TyuApp application;
	private VideoView vvw;
	private Button setVideo;
	private ImageView moreBtn, weixin, wb, qq, py, upvideo, setmyvideo,camera_x;
	private String musicPath, oldpath, newpath, newupPath, newsetPath,
			videoname, delvideo, deltemp;
	private Long videoName = 0L, videoPs;
	private int urlId;
	private int width, height;
	myhandler myh = new myhandler();

	TyuTask obj = null;

	// String myaudioPath = Environment.getExternalStorageDirectory()
	// .getAbsolutePath() + "/com.panfeng.shinning" + "/mySave/";
	String myUpVideo = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/myProduction/";
	String Video = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/com.panfeng.shinning" + "/video/";
	String saveVideo = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/mySave/";
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private Context ctx = SelfieSet.this;
	private SharedPreferences preference;
	String share_url = "";
	Dialog dialog;
	String share_content = "";
	final static String mySdcardVideoPath = Environment
			.getExternalStorageDirectory()
			+ "/com.panfeng.shinning"
			+ "/media_cache" + "/472/no.mp4";

	final static String myVideoPath = Environment.getExternalStorageDirectory()
			+ "/com.panfeng.shinning" + "/myProduction/";

	final static String myPath = Environment.getExternalStorageDirectory()
			+ "/com.panfeng.shinning" + "/upVideoList/123.mp4";

	// protected void onDestory () {
	//
	// super.onDestroy();
	// application.originalData.clear();
	// application.originalData_back.clear();
	//
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 系统进度条调用
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.camera_set);
		init();
		updateSongList();

		playMyVideo();
		upvideo = (ImageView) findViewById(R.id.cameraupvideo);
		upvideo.setOnClickListener(this);
		setmyvideo = (ImageView) findViewById(R.id.camerasetmyvideo);
		setmyvideo.setOnClickListener(this);

		// application = ((TyuApp) getApplicationContext());
		//
		// application.originalData.clear();
		// application.originalData_back.clear();s

		Intent intent = this.getIntent();
		oldpath = intent.getStringExtra("name");
		videoname = intent.getStringExtra("videoPath");
		width = intent.getExtras().getInt("imagewidth");
		height = intent.getExtras().getInt("imageheight");
		// newpath = myaudioPath + videoname;
		newupPath = myUpVideo + videoname;
		delvideo = Video + videoname;
		newsetPath = saveVideo + videoname;

		// deltemp = Video + videoname + ".temp.mp4";
		//
		// File file = new File(deltemp);
		// file.delete();

		Log.i("dirlist", "old" + width + "height=+" + height);
		Log.i("video", "name=" + videoname);

		// setMyVideo();
		// findDialog();

	}

	class TyuTask extends Thread {
		public boolean stop_upload = false;
		public long curLen = 0;
		public long length = 0;

		public TyuTask(Runnable aRun) {
			super(aRun);
		}
	}

	public void updateSongList() {
		File home = new File(myVideoPath);
		File[] fileArray = home.listFiles();
		if (fileArray.length > 0) {

			for (int i = 0; i < fileArray.length; i++) {

				String videoFileName = fileArray[i].getName();
				String videoStringName = videoFileName.substring(0,
						videoFileName.indexOf('.'));
				Long changeName = Long.parseLong(videoStringName);

				videoPs = changeName;

				if (videoName < videoPs) {
					videoName = videoPs;
				}

			}

		}
	}

	// if (home.listFiles(new Mp4Filter()).length > 0) {
	// for (File file : home.listFiles(new Mp3Filter())) {
	//
	//
	//
	// String str =file.getName();
	//
	//
	// Log.i("lutao", str);
	// String strName=null;
	// String strh=null;
	//
	// strName=str.substring(1,str.indexOf('》'));
	//
	//
	//
	// strh=str.substring(str.indexOf('》')+1, str.lastIndexOf('.'));
	//
	//
	//
	// }
	//
	// }

	public void init() {
		// setVideo = (Button) findViewById(R.id.shining_video_set);
		// moreBtn = (ImageView) findViewById(R.id.moreback);

		if (UserControl.isLogin()) {

			wb = (ImageView) findViewById(R.id.wb);
			wb.setOnClickListener(this);
			py = (ImageView) findViewById(R.id.py);
			py.setOnClickListener(this);
			weixin = (ImageView) findViewById(R.id.weixin);
			weixin.setOnClickListener(this);
			qq = (ImageView) findViewById(R.id.qq);
			qq.setOnClickListener(this);
			camera_x = (ImageView) findViewById(R.id.camera_circlex);
			camera_x.setOnClickListener(this);
		

			wb.setEnabled(false);
			weixin.setEnabled(false);
			qq.setEnabled(false);
			py.setEnabled(false);

		} else {
			Toast.makeText(ctx, "您还未登陆", Toast.LENGTH_LONG).show();
		}

		// Intent intent = this.getIntent();
		// musicPath = intent.getStringExtra("path");

	}

	private void setMyVideo() {

		preference = ctx.getSharedPreferences("configs", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		editor.putInt("isSet", 1);
		editor.putString("cameraVideo", newsetPath);
		
		
		Log.i("lutao", newsetPath);
		
		editor.commit();

		Toast.makeText(ctx, "已设置为来去电", Toast.LENGTH_LONG).show();
		
		Log.i("lutao","v"+ oldpath);
		Log.i("lutao", "s"+newsetPath);

		try {
			fileCopy(oldpath, newsetPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// showSettingSucessDialog();

	}

	public boolean fileCopy(String oldFilePath, String newFilePath)
			throws IOException {
		// 如果原文件不存在
		if (fileExists(oldFilePath) == false) {
			return false;
		}

		// 获得原文件流
		FileInputStream inputStream = new FileInputStream(new File(oldFilePath));
		byte[] data = new byte[1024];
		// 输出流
		FileOutputStream outputStream = new FileOutputStream(new File(
				newFilePath));
		// 开始处理流
		while (inputStream.read(data) != -1) {
			outputStream.write(data);
		}
		inputStream.close();
		outputStream.close();
		return true;
	}

	public boolean fileExists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	private void upVideo() {

		if (UserControl.getUserInfo().getUserName() == null) {

			Toast.makeText(ctx, "请您先注册闪铃用户~才能上传视频", Toast.LENGTH_LONG).show();

		} else {

			startUpload();

			wb.setBackgroundResource(R.drawable.lwb);
			py.setBackgroundResource(R.drawable.lpengyou);
			qq.setBackgroundResource(R.drawable.lqq);
			weixin.setBackgroundResource(R.drawable.lweixin);

			wb.setEnabled(true);
			weixin.setEnabled(true);
			qq.setEnabled(true);
			py.setEnabled(true);

		}

	}

	private void findDialog() {

		this.moreBtn.setOnClickListener(new ReButtonListener1());

	}

	class ReButtonListener1 implements OnClickListener {

		@Override
		public void onClick(View v) {

			// Intent intent = new Intent(ctx, SelfieDia.class);
			// startActivityForResult(intent, 0);
			// overridePendingTransition(R.anim.bottom_in, 0);

		}
	}

	// 回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == 1) {

				// Intent intent = new Intent(ctx, Selfie.class);
				// intent.putExtra("path", musicPath);
				// startActivity(intent);
				// finish();

			} else if (resultCode == 2) {

				finish();
			}
		}

	}

	public void playMyVideo() {

		vvw = (VideoView) findViewById(R.id.vvw);
		vvw.setVisibility(View.VISIBLE);

		String realPath = myVideoPath + videoName + ".mp4";

		Log.i("slw", "path=" + realPath);

		vvw.setVideoURI(Uri.parse(realPath));

		vvw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {

				// vvw.start();
				mp.start();
				mp.setLooping(true);

			}
		});

		vvw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				vvw.resume();

			}
		});

	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	// Intent intent = new Intent(ctx, SelfieDia.class);
	// startActivityForResult(intent, 0);
	// overridePendingTransition(R.anim.bottom_in, 0);
	//
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	// public void showSettingSucessDialog() {
	//
	// LayoutInflater mLayoutInflater = (LayoutInflater)
	// getSystemService(LAYOUT_INFLATER_SERVICE);
	// // 自定义布局
	// ViewGroup dlgView = (ViewGroup) mLayoutInflater.inflate(
	// R.layout.show_my_video, null);
	// final Dialog dialog = new Dialog(ctx, R.style.selectorDialog);
	//
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.setContentView(dlgView);
	// dialog.show();
	// // imageView1
	// dlgView.findViewById(R.id.imagebackbtn).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// dialog.dismiss();
	// finish();
	//
	// }
	// });
	//
	// TextView my_shinning = (TextView) dlgView
	// .findViewById(R.id.my_shinning);
	// my_shinning.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// dialog.dismiss();
	//
	// Intent intent = new Intent(SelfieSet.this, SelfieSetView.class);
	//
	// startActivity(intent);
	// finish();
	// }
	// });
	//
	// ImageView my_img = (ImageView) dlgView.findViewById(R.id.media_image);
	// my_img.setVisibility(View.GONE);
	//
	// }

	@Override
	protected void onDestroy() {

		vvw.stopPlayback();

		super.onDestroy();
	}

	public void startUpload() {

		final String name = videoname;

		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.fv_progress_layout, null);
		dialog = new Dialog(this, R.style.selectorDialog);
		// 当然也可以手动设置PopupWindow大小。
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(menuView);
		dialog.setCancelable(false);

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

				File fx = new File(newupPath);
				map.put("uploadfile", fx);
				map.put("filename", fx.getName());
				map.put("width", width + "");
				map.put("height", height + "");
				map.put("userId_1", UserControl.getUserInfo().getUserId() + "");

				obj.length = new File(newupPath).length();
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
												// ok_btn.setVisibility(View.VISIBLE);
												// divider.setVisibility(View.VISIBLE);
												// cancel_btn
												// .setVisibility(View.GONE);
											}

										});

							}

							@Override
							public boolean stopped() {
								// TODO Auto-generated method stub
								return obj.stop_upload;

							}
						});

				Log.i("slw", "url=" + res);

				try {
					
					if(res == null||res.equals(""))
						throw new NullPointerException();
					JSONObject person = new JSONObject();
					
					
					
					person = new JSONObject(res);
					
					urlId = person.getInt("id");

					share_url = TyuDefine.URL + "share.jsp?id=" + urlId + "_";

					Log.i("slw", "lols=" + person.getInt("id"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block

					// dialog.dismiss();
					Message msg = new Message();
					msg.what = UPDATEIMAGE;
					myh.sendMessage(msg);
				
			
				}

				// 配置数据

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						message.setText("上传完成");
						ok_btn.setVisibility(View.VISIBLE);
						// divider.setVisibility(View.VISIBLE);
						cancel_btn.setVisibility(View.GONE);
						// dialog.dismiss();
						configPlatforms();
						setShareContent();
						// try {
						// fileCopy(oldpath, newupPath);
						//
						// } catch (IOException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
						//
						// File file = new File(delvideo);
						// file.delete();
						dialog.dismiss();

					}
				});

			}
		});
		obj.start();
		ok_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// finish();
			//	dialog.dismiss();
			}
		});
//		cancel_btn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//obj.stop_upload = true;
//				// finish();
//			//	dialog.dismiss();
//
//			}
//		});
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent() {
		String template = "您的好友 %s 给你分享一个好玩的视频，速速来看【%s】";
		String name = "";
		if (UserControl.getUserInfo().getUserName() != null) {
			name = UserControl.getUserInfo().getUserName();
		}

		String content = String.format(template, name, share_url);
		share_content = content;
		//
		mController.setShareContent(content);

		// 微信
		Log.i("slw", "content=" + share_content);
		Log.i("slw", "share=" + share_url);

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(content);
		weixinContent.setTitle("闪铃");
		weixinContent.setTargetUrl(share_url);
		weixinContent.setShareMedia(new UMImage(this, ""));
		mController.setShareMedia(weixinContent);
		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		circleMedia.setTitle("闪铃");
		circleMedia.setTargetUrl(share_url);
		circleMedia.setShareMedia(new UMImage(this, ""));
		mController.setShareMedia(circleMedia);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(content);

		qzone.setTargetUrl(share_url);
		qzone.setTitle("闪铃");
		qzone.setShareMedia(new UMImage(this, ""));
		mController.setShareMedia(qzone);
		// qq分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setTitle("闪铃");

		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(share_url);
		qqShareContent.setShareMedia(new UMImage(this, ""));
		mController.setShareMedia(qqShareContent);
	}

	/**
	 * 配置分享平台参数</br>
	 */
	private void configPlatforms() {
		mController.getConfig().closeToast();
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加腾讯微博SSO授权
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		// // 添加人人网SSO授权
		// RenrenSsoHandler renrenSsoHandler = new
		// RenrenSsoHandler(getActivity(),
		// "201874", "28401c0964f04a72a14c812d6132fcef",
		// "3bf66e42db1e4fa9829b955cc300b737");
		// mController.getConfig().setSsoHandler(renrenSsoHandler);

		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wxaf12b567e6be5abf";
		String appSecret = "a60cd6661f912d1aee285b0043332875";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "100424468";
		String appKey = "c7394704798a158208a74ab60104f0ba";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId,
				appKey);

		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
				appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	private Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

	private void performShare(SHARE_MEDIA platform) {
		mController.postShare(this, platform, new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					showText += "平台分享成功";

					// TODO:MTA
					Properties prop = new Properties();
					prop.setProperty("type", "" + platform);
					prop.setProperty("url", "");
					StatService.trackCustomKVEvent(ctx, "share", prop);

				} else {
					showText += "平台分享失败";
				}
				TyuCommon.showToast(getActivity(), showText);
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {

		final SHARE_MEDIA[] share_ids = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.SINA, };

		switch (v.getId()) {
		case R.id.qq:

			performShare(share_ids[0]);
			break;

		case R.id.weixin:

			performShare(share_ids[1]);
			break;

		case R.id.py:

			performShare(share_ids[2]);
			break;

		case R.id.wb:

			performShare(share_ids[3]);
			break;

		case R.id.cameraupvideo:

			upVideo();

			break;
		case R.id.camerasetmyvideo:

			setMyVideo();
			break;
		case R.id.camera_circlex:	
			finish();

		default:
			break;
		}

	}
	private int UPDATEIMAGE = 0x123;
	class myhandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == UPDATEIMAGE) {
				Toast.makeText(ctx, "上传失败，请重新上传", Toast.LENGTH_LONG).show();
				
			}

		}
	}

}
package com.panfeng.shining.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fucfuc.testJNIapi;
import com.panfeng.camera.surfaceview.SurfaceAnimationView;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.activity.s2nd.FindMyMediaProductionActivity;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.entity.UserInfoEntity;
import com.panfeng.shining.tools.FileCopy;
import com.panfeng.shining.tools.MtaTools;
import com.panfeng.shining.tools.NetTools;
import com.panfeng.shining.tools.ShareTools;
import com.panfeng.shining.tools.UserControl;
import com.panfeng.shinning.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

public class MySurfaceView extends Activity {
	
	
	
	private testJNIapi tj; 
	
	
	
	private Dialog dlg, ddg;
	boolean isSuccess = true;

	private int imagewidth, imageheight;
	String audioPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator + "paomo.mp3";
	String whatToDo;
	private Context ctx = MySurfaceView.this;
	TyuApp application;

	private int urlId;
	FileCopy fc = new FileCopy();
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	String share_content = "";
	String videoname;

	boolean errors = false;

	private int shareId;
	private int laywidth = 0;
	private int layheight = 0;

	private int whoCheck = 0;

	private int surfaceViewWidth = 0;
	private int surfaceViewHeight = 0;

	TextView no, yes;
	private int xAdjust = 0;
	private int yAdjust = 0;
	Dialog dialog, showto, showChoosenm, error;

	String share_url = "";

	String videoString;

	String errorup = "网络不给力啊！";
	String errorbot = "请换个环境";

	String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	String imageString = pathString + File.separator + "com.panfeng.shinning"
			+ File.separator + "Tempimage";
	String prt = pathString + File.separator + "com.panfeng.shinning"
			+ File.separator + "videoImage";

	myhandler myh = new myhandler();
	SurfaceAnimationView sav;
	TyuTask obj = null;
	boolean hasId = false;

	// msg
	private int UPDATEIMAGE = 0x123;
	private int TOSET = 0x1234;
	private int UPING = 0x1235;
	private int ERROR = 0x1236;
	private int REGISTER_FAILED = 0x1238;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		errors = false;
		
		try {
			
			tj = new testJNIapi();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(ctx, "合成准备异常", Toast.LENGTH_SHORT).show();
			
		}

		Intent intent = this.getIntent();
		imagewidth = intent.getExtras().getInt("width");
		imageheight = intent.getExtras().getInt("height");
		audioPath = intent.getStringExtra("path");
		whatToDo = intent.getStringExtra("what");

		Log.i("lttto", "w" + imagewidth);
		Log.i("lttto", "h" + imageheight);

		laywidth = this.getWindowManager().getDefaultDisplay().getWidth();
		layheight = this.getWindowManager().getDefaultDisplay().getHeight();

		getratio();

		Log.i("lttto", audioPath);

	

		FrameLayout layout = new FrameLayout(this);
		application = ((TyuApp) getApplicationContext());

		layout.setBackgroundColor(Color.BLUE);
		for (int i = 0; i < application.originalData.size(); i++) {
			application.originalData_back.add(application.originalData.get(i)
					.clone());
		}
		sav = new SurfaceAnimationView(this, application.originalData, 125,
				audioPath);
		sav.setX(xAdjust);
		sav.setY(yAdjust);

		layout.addView(sav);
		LayoutParams lp = sav.getLayoutParams();
		lp.width = surfaceViewWidth;
		lp.height = surfaceViewHeight;
		sav.setLayoutParams(lp);
		setContentView(layout);
		layout.startLayoutAnimation();

		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.showview, null);
		dialog = new Dialog(this, R.style.selectorDialog);

		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(dlgView);
		dialog.setCancelable(false);
		dialog.show();

		final ImageView button_tolook = (ImageView) dlgView
				.findViewById(R.id.button_tolook);
		final ImageView button_other = (ImageView) dlgView
				.findViewById(R.id.button_other);
		button_tolook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (sav != null) {
					sav.stop();
					whoCheck = 0;
					new Thread(new Generate(application.originalData_back))
							.start();
					button_tolook.setEnabled(false);
					button_other.setEnabled(false);
				}

			}
		});

		button_other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (sav != null) {
					sav.stop();
					button_other.setEnabled(false);
					button_tolook.setEnabled(false);
					whoCheck = 1;

					new Thread(new Generate(application.originalData_back))
							.start();
				}
			}
		});

		final Button moreinfo = (Button) dlgView.findViewById(R.id.moreinfo);
		moreinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				moreDialog();

			}
		});

	}

	private int getstatus_bar_height() {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			// e1.printStackTrace();
		}
		return sbar;
	}

	private void getratio() {
		double m = imageheight, n = imagewidth;
		double ratio = 0;

		if (m < n) {
			m = m + n;
			n = m - n;
			m = m - n;
		}
		// 高大宽小
		// 得出大于1的数，短边小的值乘以ratio（比值）的得出长边
		ratio = m / n;
		if (layheight > laywidth) {
			// 此为宽小，因此//宽不变
			surfaceViewHeight = (int) (laywidth * ratio);
			surfaceViewWidth = laywidth;

			// 计算 偏移量

			// 宽不变，偏移为y轴
			yAdjust = (layheight - surfaceViewHeight - getstatus_bar_height()) / 2;
			xAdjust = 0;
		} else {
			surfaceViewWidth = (int) (layheight * ratio);
			surfaceViewHeight = layheight;

			yAdjust = 0;
			xAdjust = (laywidth - surfaceViewHeight) / 2;
		}
		Log.i("123", ratio + "   " + imageheight + "   " + imagewidth);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			finish();

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		application.originalData.clear();
		application.originalData_back.clear();

		// if (whatToDo.equals("1") || whatToDo == "1") {
		//
		// } else {
		//
		// // File file = new File(audioPath);
		// //
		// // if (file.exists()) {
		// // file.delete();
		// // }
		//
		// }
	}

	private void toLook() {

		final SHARE_MEDIA[] share_ids = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.SINA, };

		LayoutInflater mLayout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgv = (ViewGroup) mLayout.inflate(R.layout.tolook, null);
		showto = new Dialog(ctx, R.style.activity_theme_transparent);

		showto.setCanceledOnTouchOutside(false);
		showto.setContentView(dlgv);
		showto.setCancelable(false);
		showto.show();

		TextView reToLook = (TextView) dlgv
				.findViewById(R.id.my_surface_shinning);
		reToLook.setText("上传成功");
		ImageView wb = (ImageView) dlgv.findViewById(R.id.wb);

		ImageView py = (ImageView) dlgv.findViewById(R.id.py);

		ImageView weixin = (ImageView) dlgv.findViewById(R.id.weixin);

		ImageView qq = (ImageView) dlgv.findViewById(R.id.qq);

		ImageView reback = (ImageView) dlgv
				.findViewById(R.id.my_surface_cancle);

		wb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!errors) {
					ShareTools.performShare(share_ids[3],ctx);
				} else {
					errorup = "无法分享！网络不给力啊";
					errorbot = "客官请稍后再试";

					Message msg = new Message();
					msg.what = ERROR;
					Pair<String, String> msgcontent = new Pair(errorup ,
							errorbot);
					msg.obj = msgcontent;
					myh.sendMessage(msg);

				}

			}
		});

		py.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!errors) {
					ShareTools.performShare(share_ids[2],ctx);
				} else {
					errorup = "无法分享！网络不给力啊";
					errorbot = "客官请稍后再试";

					Message msg = new Message();
					msg.what = ERROR;
					Pair<String, String> msgcontent = new Pair(errorup,
							errorbot);
					msg.obj = msgcontent;
					myh.sendMessage(msg);

				}

			}
		});

		weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!errors) {
					ShareTools.performShare(share_ids[1],ctx);
				} else {
					errorup = "无法分享！网络不给力啊";
					errorbot = "客官请稍后再试";

					Message msg = new Message();
					msg.what = ERROR;
					Pair<String, String> msgcontent = new Pair(errorup,
							errorbot);
					msg.obj = msgcontent;
					myh.sendMessage(msg);

				}

			}
		});

		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!errors) {
					ShareTools.performShare(share_ids[0],ctx);
				} else {
					errorup = "无法分享！网络不给力啊";
					errorbot = "客官请稍后再试";

					Message msg = new Message();
					msg.what = ERROR;
					Pair<String, String> msgcontent = new Pair(errorup,
							errorbot);
					msg.obj = msgcontent;
					myh.sendMessage(msg);

				}

			}
		});

		reback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
				showto.cancel();

			}
		});

	}

	private void toError(String txt1, String txt2) {

		LayoutInflater mLayout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgv = (ViewGroup) mLayout.inflate(R.layout.error, null);
		TextView up = (TextView) dlgv.findViewById(R.id.tolookText);
		TextView bot = (TextView) dlgv.findViewById(R.id.tolookText5);

		if (txt1 != null && !txt1.equals(""))
			up.setText(txt1);
		if (txt2 != null && !txt2.equals(""))
			bot.setText(txt2);

		error = new Dialog(ctx, R.style.activity_theme_transparent);
		error.setCanceledOnTouchOutside(false);
		error.setContentView(dlgv);
		error.setCancelable(false);
		error.show();

		ImageView reback = (ImageView) dlgv
				.findViewById(R.id.my_surface_cancle);

		reback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});
	}

	private void toSet() {

		LayoutInflater mLayout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup dlgv = (ViewGroup) mLayout.inflate(R.layout.tolook, null);
		showto = new Dialog(ctx, R.style.activity_theme_transparent);

		showto.setCanceledOnTouchOutside(false);
		showto.setContentView(dlgv);
		showto.setCancelable(false);
		showto.show();

		// LayoutInflater mLayoutChooses = (LayoutInflater)
		// getSystemService(LAYOUT_INFLATER_SERVICE);
		// // 自定义布局
		// ViewGroup dlgvs = (ViewGroup) mLayoutChooses.inflate(
		// R.layout.sure_dialog, null);
		// showChoose = new Dialog(ctx, R.style.selectorDialog);
		// showChoose.setCanceledOnTouchOutside(false);
		// showChoose.setContentView(dlgvs);
		// showChoose.setCancelable(false);

		// no = (TextView) dlgvs.findViewById(R.id.sure_false);
		//
		// yes = (TextView) dlgvs.findViewById(R.id.sure_true);

		TextView reToSet = (TextView) dlgv.findViewById(R.id.tolookText);
		TextView reToSets = (TextView) dlgv.findViewById(R.id.tolookText5);
		reToSet.setText("已成功设定为我的闪铃！！");

		reToSets.setText("下次来电话时就能看到啦！");

		TextView reToLook = (TextView) dlgv
				.findViewById(R.id.my_surface_shinning);
		// reToLook.setText("我的作品");

		ImageView reback = (ImageView) dlgv
				.findViewById(R.id.my_surface_cancle);

		ImageView wb = (ImageView) dlgv.findViewById(R.id.wb);

		ImageView py = (ImageView) dlgv.findViewById(R.id.py);

		ImageView weixin = (ImageView) dlgv.findViewById(R.id.weixin);

		ImageView qq = (ImageView) dlgv.findViewById(R.id.qq);
		
		
		errors= NetTools.checkNetwork(ctx);

		reToLook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(ctx, FindMyMediaProductionActivity.class);
				startActivity(intent);
				finish();
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

				if (errors) {
					shareId = 1;
					Log.i("add", "laowang");
					Message msg = new Message();
					msg.what = UPING;
					myh.sendMessage(msg);
				} else {
					errorup = "无法分享！网络不给力啊";
					errorbot = "客官请稍后再试";

					Message msg = new Message();
					msg.what = ERROR;
					Pair<String, String> msgcontent = new Pair(errorup,
							errorbot);
					msg.obj = msgcontent;
					myh.sendMessage(msg);

				}

				// performShare(share_ids[0]);

			}
		});

		py.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (errors) {
					shareId = 2;

					Message msg = new Message();
					msg.what = UPING;
					myh.sendMessage(msg);
				} else {
					errorup = "无法分享！网络不给力啊";
					errorbot = "客官请稍后再试";

					Message msg = new Message();
					msg.what = ERROR;
					Pair<String, String> msgcontent = new Pair(errorup,
							errorbot);
					msg.obj = msgcontent;
					myh.sendMessage(msg);

				}

			}
		});
		
		

		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (errors) {
					shareId = 0;

					Message msg = new Message();
					msg.what = UPING;
					myh.sendMessage(msg);
				} else {
					errorup = "无法分享！网络不给力啊";
					errorbot = "客官请稍后再试";

					Message msg = new Message();
					msg.what = ERROR;
					Pair<String, String> msgcontent = new Pair(errorup,
							errorbot);
					msg.obj = msgcontent;
					myh.sendMessage(msg);

				}

			}
		});

		wb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (errors) {
					shareId = 3;

					Message msg = new Message();
					msg.what = UPING;
					myh.sendMessage(msg);
				} else {
					errorup = "无法分享！网络不给力啊";
					errorbot = "客官请稍后再试";

					Message msg = new Message();
					msg.what = ERROR;
					Pair<String, String> msgcontent = new Pair(errorup,
							errorbot);
					msg.obj = msgcontent;
					myh.sendMessage(msg);

				}

			}
		});

	}

	private void moreDialog() {

		dialog.setCancelable(true);
		dialog.cancel();

		// Intent intent = new Intent(this, SelfieDia.class);
		// startActivityForResult(intent, 0);
		// overridePendingTransition(R.anim.bottom_in, 0);

		finish();

	}

	class myhandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == UPDATEIMAGE) {

				Thread ms = new Thread(runnableTolook);
				ms.start();

			}

			else if (msg.what == TOSET) {

				Thread ms = new Thread(runnableToSet);
				ms.start();

			}

			else if (msg.what == REGISTER_FAILED) {

				Thread ms = new Thread(runnableToSet);
				ms.start();

			}

			else if (msg.what == ERROR) {
				// Thread ms = new Thread(runnableToError);
				//
				// ms.start();

				// Log.i("dirlist", "objclass="+ msg.obj.getClass());
				// Log.i("dirlist", "obj="+ msg.obj);


				Pair<String, String> msgcontent = (Pair<String, String>) msg.obj;
			
				toError(msgcontent.first, msgcontent.second);
			}

			else if (msg.what == UPING)

			{

				UserInfoEntity uie = UserControl.getUserInfo();

				if (uie != null) {

				
					UserInfoEntity ex= UserControl.getUserInfo();
					if (ex!=null&&ex.getUserId() > 0) {
				
						
					startUpload();
					}
					else{
						
						toError("数据异常了哦！", "请稍后再试！");
						
					}

				} else {

					// Thread ms = new Thread(runnableToError);
					// ms.start();
					Pair<String, String> msgcontent = (Pair<String, String>) msg.obj;

					toError("数据异常了哦", "请稍后再试！");

				}

			}

		}
	}

	class Generate implements Runnable {
		List<byte[]> list;

		public Generate(List<byte[]> list) {
			super();
			this.list = list;
		}

		@Override
		public void run() {
			try {

				String time = System.currentTimeMillis() + "";
				File file = new File(imageString);
				if (!file.exists()) {
					file.mkdirs();
				} else {
					String[] filearray = file.list();
					if (filearray.length > 0) {
						for (int i = 0; i < filearray.length; i++) {
							File f = new File(filearray[i]);
							f.delete();
						}
					}
				}
				for (int i = 0; i < list.size(); i++) {
					File file2 = new File(imageString, i + ".jpeg");
					FileOutputStream fosFileOutputStream = new FileOutputStream(
							file2);
					fosFileOutputStream.write(list.get(i));
					fosFileOutputStream.flush();
					fosFileOutputStream.close();
				}
				videoString = pathString + File.separator
						+ "com.panfeng.shinning" + File.separator
						+ "myProduction";
				File filevideo = new File(videoString);
				if (!filevideo.exists()) {
					filevideo.mkdirs();
				}

				Thread mt = new Thread(runnable);
				mt.start();

				File srcImage = new File(imageString, "0.jpeg");

				File movImage = new File(prt, time + ".jpg");
				FileInputStream fis = new FileInputStream(srcImage);
				FileOutputStream fos = new FileOutputStream(movImage);
				byte[] by = new byte[(int) srcImage.length()];
				fis.read(by);
				fos.write(by);
				fos.flush();
				fis.close();
				fos.close();
				videoname = time + ".mp4";
				videoString = filevideo.getAbsolutePath() + File.separator
						+ videoname;

				int x = testJNIapi.SynthesisVideo(videoString, imageString
						+ File.separator, imagewidth + "", imageheight + "",
						application.originalData.size() + "");
				testJNIapi.ReplaceAudio(videoString, audioPath);

				if (x != 1) {

					errorup = "合成失败了哦！";
					errorbot = "请客官稍后再试";

					Message msg = new Message();
					msg.what = ERROR;
					Pair<String, String> msgcontent = new Pair(errorup,
							errorbot);
					msg.obj = msgcontent;
					myh.sendMessage(msg);

					errors = true;

				}

				File f = new File(videoString + ".temp.mp4");
				if (f.exists()) {
					f.delete();
				}
				// dlg.cancel();
				// dlg = null;

				SharedPreferences preference = ctx.getSharedPreferences(
						"configs", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				
				editor.putString("cameraVideo", videoString);

				
				DBAdapter db = new DBAdapter(ctx);
				db.open();
				
                Cursor c=db.getMyVideo();
				
				if(c.moveToFirst()&&c.getInt(0)==1){
					db.updateMyVideo(1, 1);
				}
				
				else{
					
					db.insertMyVideo(1);
				}

				editor.commit();

				// application.originalData.clear();
				// application.originalData_back.clear();

				// Intent intent = new Intent();
				// intent.setClass(ctx, SelfieSet.class);
				// intent.putExtra("name", videoString);
				// intent.putExtra("videoPath", videoname);
				// intent.putExtra("imagewidth", imagewidth);
				// intent.putExtra("imageheight", imageheight);

				// Log.i("video", "videoString" + videoString);
				// Log.i("video", "videoname" + videoname);
				//
				// startActivity(intent);

				if (errors != true) {

					if (whoCheck == 0) {

						Message msg = new Message();
						msg.what = TOSET;
						myh.sendMessage(msg);

					} else {

						Message msg = new Message();
						msg.what = UPING;
						myh.sendMessage(msg);
						// Message msg = new Message();
						// msg.what = UPDATEIMAGE;
						// myh.sendMessage(msg);

					}
				}

				// dialog.setCancelable(true);
				// dialog.cancel();
				// dialog=null;

				// LayoutInflater mLayoutInflater = (LayoutInflater)
				// getSystemService(LAYOUT_INFLATER_SERVICE);
				// // 自定义布局
				// ViewGroup dlgView = (ViewGroup) mLayoutInflater.inflate(
				// R.layout.tolook, null);
				// showto = new Dialog(ctx, R.style.selectorDialog);
				//
				// showto.setCanceledOnTouchOutside(false);
				// showto.setContentView(dlgView);
				// showto.setCancelable(false);
				// showto.show();

				// finish();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			Looper.prepare();

			dlg = SelfieWaitingBar.build(ctx);
			dlg.show();
			Looper.loop();
		}

	};

	Runnable runnableTolook = new Runnable() {

		@Override
		public void run() {

			Looper.prepare();

			toLook();

			Looper.loop();
		}

	};

	Runnable runnableToSet = new Runnable() {

		@Override
		public void run() {

			Looper.prepare();

			String oldPath = videoString;
			String newPath = pathString + "/" + "com.panfeng.shinning" + "/"
					+ "myShinVideo/myShin.mp4";
			String toSave = pathString + "/" + "com.panfeng.shinning" + "/"
					+ "mySave/" + videoname;
			
			
			MtaTools.Sel_SetVideo(ctx, videoname);
			
		

			toSet();
			try {
				fc.fileCopy(oldPath, newPath);
				fc.fileCopy(oldPath, toSave);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Looper.loop();
		}

	};

	Runnable runnableToError = new Runnable() {

		@Override
		public void run() {

			Looper.prepare();

			// toError();

			Looper.loop();
		}

	};

	class TyuTask extends Thread {
		public boolean stop_upload = false;
		public long curLen = 0;
		public long length = 0;

		public TyuTask(Runnable aRun) {
			super(aRun);
		}
	}

	public void startUpload() {

		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.fv_progress_v2_layout, null);
		ddg = new Dialog(this, R.style.activity_theme_transparent);
		// 当然也可以手动设置PopupWindow大小。
		ddg.setCanceledOnTouchOutside(false);
		ddg.setContentView(menuView);
		ddg.setCancelable(false);

		final com.panfeng.shining.progress.ArcProgressLu pb = (com.panfeng.shining.progress.ArcProgressLu) menuView
				.findViewById(R.id.progressBar1);

		ddg.show();
		// 初始化：
		pb.setMax(100);
		pb.setProgress(0);

		obj = new TyuTask(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = TyuDefine.HOST + "smc/post_media_base_by_user";
				HashMap<String, Object> map = new HashMap<String, Object>();

				File fx = new File(videoString);
				map.put("uploadfile", fx);
				map.put("filename", fx.getName());
				map.put("width", 0 + "");
				map.put("height", 0 + "");
				map.put("userId_1", UserControl.getUserInfo().getUserId() + "");

				obj.length = new File(videoString).length();
				obj.curLen = 0;
				obj.stop_upload = false;

				Log.i("slw", "map=" + map.toString());

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
						
								MtaTools.Sel_UpVideo(ctx, videoname);
								
							}

							@Override
							public boolean stopped() {
								// TODO Auto-generated method stub
								return obj.stop_upload;
							}
						});

				Log.i("add", "url=" + res);

				try {

					if (res == null || res.equals("")) {

						errorup = "上传失败！网络不给力哦！";
						errorbot = "已帮客官保存到我的作品";
						Message msg = new Message();
						msg.what = ERROR;
						Pair<String, String> msgcontent = new Pair(errorup,
								errorbot);

						msg.obj = msgcontent;
						myh.sendMessage(msg);
						errors = true;
					}

					JSONObject person = new JSONObject();

					person = new JSONObject(res);

					urlId = person.getInt("id");

					share_url = TyuDefine.URL + "share.jsp?id=" + urlId + "_";

					Log.i("add", "lols=" + person.getInt("id"));
				} catch (Exception e) {

					// Message msg = new Message();
					// msg.what = ERROR;
					// myh.sendMessage(msg);
					// errorup = "上传失败了！";
					// errorbot = "已保存到我的作品";
					// errors = true;
					// TODO Auto-generated catch block

					// dialog.dismiss();
					// Message msg = new Message();
					// msg.what = UPEND;
					// myh.sendMessage(msg);
					// e.printStackTrace();

				}

				// 配置数据

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// message.setText("上传完成");
						// ok_btn.setVisibility(View.VISIBLE);
						// divider.setVisibility(View.VISIBLE);
						// cancel_btn.setVisibility(View.GONE);
						// dialog.dismiss();
						ShareTools.configPlatforms(ctx);
						ShareTools.setShareContent(ctx,share_url,share_content);
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

						if (whoCheck == 0) {

							final SHARE_MEDIA[] share_ids = new SHARE_MEDIA[] {
									SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN,
									SHARE_MEDIA.WEIXIN_CIRCLE,
									SHARE_MEDIA.SINA, };

							ShareTools.performShare(share_ids[shareId],ctx);

						}

						else if (whoCheck == 1 && errors == false) {
							Message msg = new Message();
							msg.what = UPDATEIMAGE;
							myh.sendMessage(msg);

						}

						ddg.dismiss();

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
		// });
		// cancel_btn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// //obj.stop_upload = true;
		// // finish();
		// // dialog.dismiss();
		//
		// }
		// });
	}

//	/**
//	 * 根据不同的平台设置不同的分享内容</br>
//	 */
//	private void setShareContent() {
//		String template = "您的好友 %s 给你分享一个好玩的视频，速速来看【%s】";
//		String name = "";
//		if (UserControl.getUserInfo().getUserName() != null) {
//			name = UserControl.getUserInfo().getUserName();
//		}
//
//		String content = String.format(template, name, share_url);
//		share_content = content;
//		//
//		mController.setShareContent(content);
//
//		// 微信
//		Log.i("slw", "content=" + share_content);
//		Log.i("slw", "share=" + share_url);
//
//		WeiXinShareContent weixinContent = new WeiXinShareContent();
//		weixinContent.setShareContent(content);
//		weixinContent.setTitle("闪铃");
//		weixinContent.setTargetUrl(share_url);
//		weixinContent.setShareMedia(new UMImage(this, ""));
//		mController.setShareMedia(weixinContent);
//		// 设置朋友圈分享的内容
//		CircleShareContent circleMedia = new CircleShareContent();
//		circleMedia.setShareContent(content);
//		circleMedia.setTitle("闪铃");
//		circleMedia.setTargetUrl(share_url);
//		circleMedia.setShareMedia(new UMImage(this, ""));
//		mController.setShareMedia(circleMedia);
//
//		// 设置QQ空间分享内容
//		QZoneShareContent qzone = new QZoneShareContent();
//		qzone.setShareContent(content);
//
//		qzone.setTargetUrl(share_url);
//		qzone.setTitle("闪铃");
//		qzone.setShareMedia(new UMImage(this, ""));
//		mController.setShareMedia(qzone);
//		// qq分享内容
//		QQShareContent qqShareContent = new QQShareContent();
//		qqShareContent.setTitle("闪铃");
//
//		qqShareContent.setShareContent(content);
//		qqShareContent.setTargetUrl(share_url);
//		qqShareContent.setShareMedia(new UMImage(this, ""));
//		mController.setShareMedia(qqShareContent);
//	}
//
//	/**
//	 * 配置分享平台参数</br>
//	 */
//	private void configPlatforms() {
//		mController.getConfig().closeToast();
//		// 添加新浪SSO授权
//		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//		// 添加腾讯微博SSO授权
//		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//		// // 添加人人网SSO授权
//		// RenrenSsoHandler renrenSsoHandler = new
//		// RenrenSsoHandler(getActivity(),
//		// "201874", "28401c0964f04a72a14c812d6132fcef",
//		// "3bf66e42db1e4fa9829b955cc300b737");
//		// mController.getConfig().setSsoHandler(renrenSsoHandler);
//
//		// 添加QQ、QZone平台
//		addQQQZonePlatform();
//
//		// 添加微信、微信朋友圈平台
//		addWXPlatform();
//	}
//
//	/**
//	 * @功能描述 : 添加微信平台分享
//	 * @return
//	 */
//	private void addWXPlatform() {
//		// 注意：在微信授权的时候，必须传递appSecret
//		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//		String appId = "wxaf12b567e6be5abf";
//		String appSecret = "a60cd6661f912d1aee285b0043332875";
//		// 添加微信平台
//		UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, appSecret);
//		wxHandler.addToSocialSDK();
//
//		// 支持微信朋友圈
//		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId,
//				appSecret);
//		wxCircleHandler.setToCircle(true);
//		wxCircleHandler.addToSocialSDK();
//	}
//
//	/**
//	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
//	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
//	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
//	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
//	 * @return
//	 */
//	private void addQQQZonePlatform() {
//		String appId = "100424468";
//		String appKey = "c7394704798a158208a74ab60104f0ba";
//		// 添加QQ支持, 并且设置QQ分享内容的target url
//		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId,
//				appKey);
//
//		qqSsoHandler.addToSocialSDK();
//
//		// 添加QZone平台
//		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
//				appId, appKey);
//		qZoneSsoHandler.addToSocialSDK();
//	}
//
//	private Activity getActivity() {
//		// TODO Auto-generated method stub
//		return this;
//	}
//
//	private void performShare(SHARE_MEDIA platform) {
//		mController.postShare(this, platform, new SnsPostListener() {
//
//			@Override
//			public void onStart() {
//
//			}
//
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,
//					SocializeEntity entity) {
//				String showText = platform.toString();
//				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
//					showText += "平台分享成功";
//
//					// TODO:MTA
//					Properties prop = new Properties();
//					prop.setProperty("type", "" + platform);
//					prop.setProperty("url", "");
//					StatService.trackCustomKVEvent(ctx, "share", prop);
//
//				} else {
//					showText += "平台分享失败";
//				}
//				TyuCommon.showToast(getActivity(), showText);
//				finish();
//			}
//		});
//	}

}

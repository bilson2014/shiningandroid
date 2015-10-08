package com.panfeng.shining.activity;

import java.io.File;

import tyu.common.utils.CircleBitmapDisplayer;
import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuFileUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.panfeng.shining.TyuApp.TyuImageLoadingListener;
import com.panfeng.shining.entity.UserInfoEntity;
import com.panfeng.shining.tools.UserControl;
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;

public class TyuUserInfoActivity extends Activity implements OnClickListener {
	private EditText mEditPhone;
	private EditText mEditName;
	private TextView sureText;
	Context ctx = TyuUserInfoActivity.this;
	// =======================UI==================//
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new CircleBitmapDisplayer())
			.showImageForEmptyUri(R.drawable.s2nd_user_icon_contour)
			.showImageOnFail(R.drawable.s2nd_user_icon_contour)
			.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory()// 这是为了降低刷新过程中闪烁的时间
			.cacheOnDisc().build();
	ImageLoadingListener simpleImageListener = new TyuImageLoadingListener();
	private ImageView user_img;
	static public File image_picked = null;
	final UserInfoEntity  ue=UserControl.getUserInfo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fv_user_info_layout);
		

//		findViewById(R.id.save_info).setOnClickListener(this);

		mEditName = (EditText) findViewById(R.id.name);
		sureText = (TextView) findViewById(R.id.suretext);
		sureText.setOnClickListener(this);

		mEditPhone = (EditText) findViewById(R.id.phone);
		if (UserControl.isLogin()) {
			if (!TextUtils.isEmpty(UserControl.getUserInfo().getUserName()))
				mEditName.append(UserControl.getUserInfo().getUserName());

			if (!TextUtils.isEmpty(UserControl.getUserInfo().getUserPhone()))
				mEditPhone.append(UserControl.getUserInfo().getUserPhone());
		}
		
		user_img = (ImageView) findViewById(R.id.user_img);
		String url = UserControl.getUserInfo().getUserImage();
		if (!TextUtils.isEmpty(url)) {
			imageLoader.displayImage(url, user_img, options);
		}
		user_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TyuCameraActicity.type = 0;
				TyuCameraActicity.shortcut = true;
				image_picked = new File(TyuFileUtils.getValidPath(), "cache/"
						+ System.currentTimeMillis() + "_temp.jpg");
				image_picked.getParentFile().mkdirs();
				startActivity(new Intent(TyuUserInfoActivity.this,
						TyuCameraActicity.class));
				
				
				
//				showPhotoPick();
			}
		});
		initTitleBar();
	}


	void initTitleBar() {
		View title = findViewById(R.id.newlistbar);
	
		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
		bartxt.setText("个人信息");
		
		bartxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
		
		
		
		barimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (image_picked!=null&&image_picked.exists()) {
			user_img.setImageDrawable(null);
			imageLoader.displayImage(
					Scheme.FILE.wrap(image_picked.getAbsolutePath()), user_img,
					options);
		}
	}

	public void try2Update() {
		
	
		
		String phone = mEditPhone.getText().toString();

		if (!TextUtils.isEmpty(phone)) {
			ue.setUserPhone(phone);
		}
		String name = mEditName.getText().toString();
		if (!TextUtils.isEmpty(name)) {
			ue.setUserName(name);
			
		}

		final ProgressDialog dlg = new ProgressDialog(this);
		dlg.setMessage("正在验证...");
		dlg.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final boolean res =UserControl.updateUser(ue, image_picked);
				Log.i("dirlist", "picked="+image_picked);
				
				
				
				if (dlg != null && dlg.isShowing())
					dlg.dismiss();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (res) {
							
							//TODO:MTA use_info
							StatService.trackCustomEvent(ctx, "user_info", "");		
							
//							startActivity(new Intent(TyuUserInfoActivity.this,
//									S2ndMainActivity.class));
							finish();
											
						
							
						} else {
							
							try {
								TyuCommon.showToast(TyuUserInfoActivity.this,
										"保存失败,请确认网络是否顺畅");
								
							} catch (Exception e) {
								TyuCommon.showToast(TyuUserInfoActivity.this,
										"保存异常,请确认网络是否顺畅");
							}
						}
					}
				});

			}
		}).start();
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.suretext:
			try2Update();
			break;
		default:
			break;
		}
	}

	public void showPhotoPick() {

//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片
//		// aspectX aspectY 是宽高的比例
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		intent.putExtra("outputX", 300);
//		intent.putExtra("outputY", 300);
//		Intent wrapperIntent = Intent.createChooser(intent, "图片选择"); // 开始
//																		// 并设置标题
//		startActivityForResult(wrapperIntent, 1); // 设返回 码为 1 onActivityResult
													// 中的 requestCode 对应
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1) {
			Bundle extras = data.getExtras();
			Bitmap bitmap = (Bitmap) extras.get("data");
			if (bitmap != null) {
				TyuFileUtils.saveImg(bitmap,
						TyuUserInfoActivity.image_picked.getAbsolutePath(),Bitmap.CompressFormat.JPEG);
				
			
			  
				
		
				
				
				
			}

		}
		if (image_picked.exists()) {
			user_img.setImageDrawable(null);
			imageLoader.displayImage(
					Scheme.FILE.wrap(image_picked.getAbsolutePath()), user_img,
					options);
		}
	}
}

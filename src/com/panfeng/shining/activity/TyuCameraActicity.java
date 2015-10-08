package com.panfeng.shining.activity;

import java.io.File;

import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuContextKeeper;
import tyu.common.utils.TyuFileUtils;
import tyu.common.utils.TyuImageUtils;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.panfeng.shinning.R;

public class TyuCameraActicity extends Activity {

	public static final int NONE = 0;

	public static final int PHOTOHRAPH = 1;// 拍照

	public static final int PHOTOZOOM = 2; // 缩放

	public static final int PHOTORESOULT = 3;// 结果

	public static final int PHOTO_TAKE = 101;
	public static final int PHOTO_PICK = 102;
	public static final int PHOTO_END = 103;
	public static final String IMAGE_UNSPECIFIED = "image/*";

	static public final String action = "com.vchuang.photo_take";

	ImageView mImage;

	static String mImgFile;

	// 0选择图片，1拍照
	public static boolean enable_zoom = true;
	public static int type = 0;
	public static boolean shortcut = false;

	// static {
	// if (isSDCARDMounted()) {
	// mImgFile = Environment.getExternalStorageDirectory() + "/temp.jpg";
	// } else {
	// mImgFile = TyuContextKeeper.getInstance().getFilesDir()
	// .getAbsolutePath()
	// + "/temp.jpg";
	// }
	// }
	static public class Config {
		public String path = null;
		public int width_limit = 0;
		public int height_limit = 0;
	}

	static public String getValidFile() {
		if (isSDCARDMounted()) {
			mImgFile = Environment.getExternalStorageDirectory() + "/"
					+ System.currentTimeMillis() + "_temp.jpg";
		} else {
			mImgFile = TyuContextKeeper.getInstance().getFilesDir()
					.getAbsolutePath()
					+ "/" + System.currentTimeMillis() + "_temp.jpg";
		}
		return mImgFile;
	}

	// image loader 相关
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	// static String TEMP_PHOTO_FILE =
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PHOTO_TAKE: {
				TyuCommon.showTwoBtnDialog(TyuContextKeeper.getInstance(),
						"拍照", "是否对照片进行裁剪?",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								zoomPhoto(Uri.fromFile(new File(mImgFile)));
							}
						}, null);
			}

				break;
			case PHOTO_PICK:

				break;
			default:
				break;
			}
		};
	};

	private String uriString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		setContentView(R.layout.camera_layout);
		mImage = (ImageView) findViewById(R.id.img_result);
		if (type == 0) {
			pickPhoto();
		} else {
			takePhoto();
		}

		// 初始化imgloader选项
		options = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
				.build();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (enable_zoom) {
			handleActivtyResult(requestCode, resultCode, data);
		} else {
			handleActivtyResult2(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	void handleActivtyResult2(final int requestCode, final int resultCode,
			final Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = null;
			if (requestCode == PHOTO_TAKE) {

				uri = Uri.fromFile(new File(mImgFile));
				// TyuImageUtils.autoAdjustImageOrientation(mImgFile);
			} else if (requestCode == PHOTO_PICK) {
				uri = data.getData();
				// zoomPhoto(data.getData());
			} else if (requestCode == PHOTO_END) {
				uri = data.getData();
			}
			if (uri != null) {
				DisplayMetrics dm = getResources().getDisplayMetrics();
				uriString = uri.toString();
				Log.v("uriString", uriString);
				convertAndSave();
			} else {
				finish();
			}
		} else {
			finish();
		}

	}

	void convertAndSave() {
		Runnable run = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DisplayMetrics dm = getResources().getDisplayMetrics();
				// 如果还是原始uri内容，做二次转化
				Log.v("convertAndSave", uriString);
				if (uriString != null) {
					File image = TyuUserInfoActivity.image_picked;
					Bitmap bitmap = null;
					if (shortcut) {
						bitmap = TyuImageUtils.getFitableBitmap(250, 250,
								Uri.parse(uriString));
						shortcut = false;
					} else {
						bitmap = TyuImageUtils.getFitableBitmap(dm.widthPixels,
								dm.heightPixels, Uri.parse(uriString));
					}

					if (bitmap == null) {
						finish();
						return;
					}
					int w = bitmap.getWidth();
					int h = bitmap.getHeight();
					TyuFileUtils.saveImg(bitmap, image.getAbsolutePath(),
							Bitmap.CompressFormat.JPEG); //
					bitmap.recycle();
					bitmap = null;
					// UploadGirlImageActivity.shareImagePath =
					// image.getAbsolutePath();
					if (uriString.contains("file://")) {
						String tmp = uriString.replace("file://", "");
						new File(tmp).delete();
					}
				}

				finish();
			}
		};
		new Thread(run).start();
	}

	void handleActivtyResult(final int requestCode, final int resultCode,
			final Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PHOTO_TAKE) {
				new Thread() {
					@Override
					public void run() {
						try {
							sleep(1000);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						zoomPhoto(Uri.fromFile(new File(mImgFile)));
					};
				}.start();

			} else if (requestCode == PHOTO_PICK) {
				zoomPhoto(data.getData());
			} else if (requestCode == PHOTO_END) {
				Bundle extras = data.getExtras();
				Bitmap bitmap = (Bitmap) extras.get("data");
				if (bitmap != null) {
					TyuFileUtils.saveImg(bitmap,
							TyuUserInfoActivity.image_picked.getAbsolutePath(),
							Bitmap.CompressFormat.JPEG);
				}
				finish();

			}
		} else {
			finish();
		}

	}

	void pickPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				IMAGE_UNSPECIFIED);

		startActivityForResult(intent, PHOTO_PICK);
	}

	void takePhoto() {
		// //强制切横
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(getValidFile())));
		startActivityForResult(intent, TyuCameraActicity.PHOTO_TAKE);
	}

	void zoomPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
//		intent.putExtra("scale", "false");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

		// intent.putExtra("output", getTempUri());
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO_END);

	}

	private Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}

	private File getTempFile() {
		return new File(mImgFile);
	}

	static public boolean isSDCARDMounted() {
		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}
}

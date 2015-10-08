package tyu.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;

public class TyuImageUtils {

	/**
	 * 要注意的是,这个接口只能处理jpeg格式的图片文件.
	 * 
	 * @param aFile
	 * @return
	 */
	static public ExifInterface getExif(String aFile) {
		ExifInterface face = null;
		try {
			face = new ExifInterface(aFile);
			return face;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 这个接口只能处理jpeg格式的图片文件.
	 * 
	 * @param filepath
	 * @return
	 */
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {

		}

		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}

	static public Options getBitmapOptions(String aFile) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			FileInputStream fis = new FileInputStream(aFile);
			BitmapFactory.decodeStream(fis, null, opts);
			fis.close();
			return opts;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	static public Options getBitmapOptions(InputStream aInput) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(aInput, null, opts);
			return opts;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	static public void testApi() {
		Context context = TyuContextKeeper.getInstance();
		try {
			InputStream input = context.getAssets().open("guide.jpg");
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			TyuFileUtils.saveImg(bitmap, "/sdcard/guide.jpg",
					CompressFormat.JPEG);
			bitmap.recycle();
			input.close();

			//
			autoAdjustImageOrientation("/sdcard/guide.jpg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 自动处理图片旋转(横向切换竖向)
	 */
	static public void autoAdjustImageOrientation(String aFile) {

		try {
			// ExifInterface face = new ExifInterface(aFile);
			// face.setAttribute(ExifInterface.TAG_ORIENTATION, 0+"");
			// face.saveAttributes();

			int degree = getExifOrientation(aFile);
			if (degree != 0) {
				Bitmap bitmap = BitmapFactory.decodeFile(aFile);
				Bitmap target = rotate(bitmap, degree);
				TyuFileUtils.saveImg(target, aFile, Bitmap.CompressFormat.JPEG);
				if (!target.isRecycled())
					target.recycle();
				ExifInterface face = new ExifInterface(aFile);
				face.setAttribute(ExifInterface.TAG_ORIENTATION, 0 + "");
				face.saveAttributes();
			}

		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static public Bitmap getFitableBitmap(int aBaseW, int aBaseH, Uri aUri) {
		try {
			// 获取图片尺寸信息
			InputStream input = TyuContextKeeper.getInstance()
					.getContentResolver().openInputStream(aUri);
			Options opt = TyuImageUtils.getBitmapOptions(input);
			input.close();
			// 计算缩放比例，以适配屏幕尺寸为准
			int scale = opt.outWidth * 100 / aBaseW;
			Options readOpt = new Options();
			readOpt.inSampleSize = scale / 100;
			readOpt.inPreferredConfig = Bitmap.Config.RGB_565;
			// 解码指定缩放比的图片
			input = TyuContextKeeper.getInstance().getContentResolver()
					.openInputStream(aUri);
			Bitmap bitmap = BitmapFactory.decodeStream(input, null, readOpt);
			input.close();
			if (aUri.toString().contains("file://")) {
				int degree = getExifOrientation(aUri.getPath());
				Bitmap target = rotate(bitmap, degree);

				return target;
			} else {
				return bitmap;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	static public Bitmap getFitableBitmap(int aBaseW, int aBaseH, String aFile) {
		try {
			InputStream input = TyuContextKeeper.getInstance()
					.getContentResolver()
					.openInputStream(Uri.fromFile(new File(aFile)));
			Options opt = TyuImageUtils.getBitmapOptions(input);
			input.close();
			int scale = opt.outWidth * 100 / aBaseW;
			Options readOpt = new Options();
			readOpt.inSampleSize = scale / 100;
			readOpt.inPreferredConfig = Bitmap.Config.RGB_565;
			input = new FileInputStream(aFile);
			Bitmap bitmap = BitmapFactory.decodeStream(input, null, readOpt);
			input.close();

			int degree = getExifOrientation(aFile);
			if (degree != 0) {
				Bitmap target = rotate(bitmap, degree);
				return target;
			}

			return bitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap rotate(Bitmap b, int degrees) {
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth() / 2,
					(float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				// We have no memory to rotate. Return the original bitmap.
			}
		}
		return b;
	}
	public static Drawable loadImageFromAssets(Context context, String aName)
			throws Exception {
		InputStream ins = context.getAssets().open(aName);

		Drawable res = new BitmapDrawable(ins);
		ins.close();
		return res;

	}

	public static Bitmap loadBitmapImageFromAssets(Context context, String aName)
			throws Exception {
		InputStream ins = context.getAssets().open(aName);

		Bitmap res = BitmapFactory.decodeStream(ins);
		ins.close();
		return res;

	}
}

package tyu.common.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;

public class CircleBitmapDisplayer implements BitmapDisplayer {

	public CircleBitmapDisplayer() {

	}

	@Override
	public Bitmap display(Bitmap bitmap, ImageView imageView) {
		Bitmap roundedBitmap = circleBitmap(bitmap, imageView);
		imageView.setImageBitmap(roundedBitmap);
		return roundedBitmap;
	}

	public static Bitmap circleBitmap(Bitmap bitmap, ImageView imageView) {
		Bitmap roundBitmap;

		int bw = bitmap.getWidth();
		int bh = bitmap.getHeight();
		int vw = imageView.getWidth();
		int vh = imageView.getHeight();
		if (vw <= 0)
			vw = bw;
		if (vh <= 0)
			vh = bh;

		int width, height;
		Rect srcRect;
		Rect destRect;
		switch (imageView.getScaleType()) {
		case CENTER_INSIDE:
			float vRation = (float) vw / vh;
			float bRation = (float) bw / bh;
			int destWidth;
			int destHeight;
			if (vRation > bRation) {
				destHeight = Math.min(vh, bh);
				destWidth = (int) (bw / ((float) bh / destHeight));
			} else {
				destWidth = Math.min(vw, bw);
				destHeight = (int) (bh / ((float) bw / destWidth));
			}
			int x = (vw - destWidth) / 2;
			int y = (vh - destHeight) / 2;
			srcRect = new Rect(0, 0, bw, bh);
			destRect = new Rect(x, y, x + destWidth, y + destHeight);
			width = vw;
			height = vh;
			break;
		case FIT_CENTER:
		case FIT_START:
		case FIT_END:
		default:
			vRation = (float) vw / vh;
			bRation = (float) bw / bh;
			if (vRation > bRation) {
				width = (int) (bw / ((float) bh / vh));
				height = vh;
			} else {
				width = vw;
				height = (int) (bh / ((float) bw / vw));
			}
			srcRect = new Rect(0, 0, bw, bh);
			destRect = new Rect(0, 0, width, height);
			break;
		case CENTER_CROP:
			vRation = (float) vw / vh;
			bRation = (float) bw / bh;
			int srcWidth;
			int srcHeight;
			if (vRation > bRation) {
				srcWidth = bw;
				srcHeight = (int) (vh * ((float) bw / vw));
				x = 0;
				y = (bh - srcHeight) / 2;
			} else {
				srcWidth = (int) (vw * ((float) bh / vh));
				srcHeight = bh;
				x = (bw - srcWidth) / 2;
				y = 0;
			}
			width = Math.min(vw, bw);
			height = Math.min(vh, bh);
			srcRect = new Rect(x, y, x + srcWidth, y + srcHeight);
			destRect = new Rect(0, 0, width, height);
			break;
		case FIT_XY:
			width = vw;
			height = vh;
			srcRect = new Rect(0, 0, bw, bh);
			destRect = new Rect(0, 0, width, height);
			break;
		case CENTER:
		case MATRIX:
			width = Math.min(vw, bw);
			height = Math.min(vh, bh);
			x = (bw - width) / 2;
			y = (bh - height) / 2;
			srcRect = new Rect(x, y, x + width, y + height);
			destRect = new Rect(0, 0, width, height);
			break;
		}

		try {
			roundBitmap = getCircleBitmap(bitmap, srcRect, destRect, width,
					height);
		} catch (OutOfMemoryError e) {
			L.e(e,
					"Can't create bitmap with rounded corners. Not enough memory.");
			roundBitmap = bitmap;
		}

		return roundBitmap;
	}

	private static Bitmap getCircleBitmap(Bitmap bitmap, Rect srcRect,
			Rect destRect, int width, int height) {
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final RectF destRectF = new RectF(destRect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(0xFF000000);
		float radius = Math.min(width/2, height/2);
		float cx = width / 2;
		float cy = height / 2;
		canvas.drawCircle(cx, cy, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, srcRect, destRectF, paint);

		return output;
	}

}

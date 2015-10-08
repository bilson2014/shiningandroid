package com.panfeng.shining.slw.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;


import com.panfeng.shining.TyuApp;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.slw.utils.FileUtils;
import com.panfeng.shining.slw.utils.LogUtils;
import com.panfeng.shinning.R;


import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import tyu.common.net.TyuDefine;


public class VidoeListAdapter extends BaseAdapter<VideoEntity> {

    private String imagepath;

    public VidoeListAdapter(Context context, List<VideoEntity> datas,
                            int itemLayoutResId) {
        super(context, datas, itemLayoutResId);
    }

    @Override
    public void convert(final ViewHolder viewHolder, final VideoEntity item) {

        viewHolder.setText(R.id.textitemcontext, item.getVideo_content())
                .setText(R.id.textitemname, item.getVideo_name());

        String VideoFileNameString = item.getVideo_fIleName();
//        Log.i("ddx", "-------------------");
//        Log.i("ddx", VideoFileNameString + "");
//        Log.i("ddx", item.getVideo_name() + "");
//        Log.i("ddx", "-------------------");
        String imageName = FileUtils.videoNameConvertImageName(VideoFileNameString);
        File f = new File(DefindConstant.saveDownaLoadImagePath + imageName);
        imagepath = f.getAbsolutePath();
        if (f.exists()) {
            //ShiningApplication.bitmapUtils.display((ImageView) viewHolder.getView(R.id.imageView1), imagepath, new CustomBitmapLoadLocaCallBack());
            Bitmap bm = BitmapFactory.decodeFile(imagepath);
            ((ImageView) viewHolder.getView(R.id.imageView1)).setImageBitmap(bm);
        } else {
            String imagePahtString = TyuDefine.HOST + "media_base/" + imageName;
            TyuApp.getBitmapUtils().display((ImageView) viewHolder.getView(R.id.imageView1), imagePahtString, new CustomBitmapLoadNetCallBack());
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

    public class CustomBitmapLoadLocaCallBack extends
            DefaultBitmapLoadCallBack<ImageView> {

        public CustomBitmapLoadLocaCallBack() {
        }

        @Override
        public void onLoading(ImageView container, String uri,
                              BitmapDisplayConfig config, long total, long current) {
        }

        @Override
        public void onLoadCompleted(ImageView container, String uri,
                                    Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
            super.onLoadCompleted(container, uri, bitmap, config, from);
            fadeInDisplay(container, bitmap);

        }
    }

    private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(android.R.color.transparent);

    private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
        final TransitionDrawable transitionDrawable = new TransitionDrawable(
                new Drawable[]{TRANSPARENT_DRAWABLE,
                        new BitmapDrawable(imageView.getResources(), bitmap)});
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(500);
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
            //e.printStackTrace();
            LogUtils.writeErrorLog("保存下载图片", "", e, null);
        }
    }

}

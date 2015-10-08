package com.panfeng.shining.Impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import tyu.common.net.TyuHttpClientUtils;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.panfeng.shining.TyuApp;
import com.panfeng.shining.activity.newactivity.ShowAllMediaActivity;
import com.panfeng.shining.activity.newactivity.ShowLocalVideo;
import com.panfeng.shining.activity.s2nd.FindMyMediaProductionActivity;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.interfaces.toSet;
import com.panfeng.shining.interfaces.volumnInterface;
import com.panfeng.shining.tools.CommonTools;
import com.panfeng.shining.tools.FileCopy;
import com.panfeng.shining.tools.MtaTools;
import com.panfeng.shining.tools.ShareTools;
import com.panfeng.shinning.R;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class localSetImpl implements toSet {

	Dialog showto, dialog;
	ImageView save;
	String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	String newPath = pathString + "/" + "com.panfeng.shinning" + "/"// 我的闪铃
			+ "myShinVideo/myShin.mp4";
	Context ctxs;

	@Override
	public void showSet(final Context ctx, final String path) {
		ctxs = ctx;
		// 自定义布局
		View dlgv = LayoutInflater.from(ctx).inflate(R.layout.setvideo, null);
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

				DBAdapter db = new DBAdapter(ctx);
				db.open();

				Cursor c = db.getMyVideo();

				if (c.moveToFirst() && c.getInt(0) == 1) {
					db.updateMyVideo(1, 1);
				}

				else {

					db.insertMyVideo(1);
				}

				db.close();

				try {
					toSetVideo(ctx);
					FileCopy f = new FileCopy();
					f.fileCopy(path, newPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	public void toSetVideo(final Context ctx) {

		final SHARE_MEDIA[] share_ids = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.SINA, };

		View dlgv = LayoutInflater.from(ctx).inflate(R.layout.tolook, null);
		dialog = new Dialog(ctx, R.style.activity_theme_transparent);

		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(dlgv);
		dialog.setCancelable(false);
		dialog.show();

		SharedPreferences preference = ctx.getSharedPreferences("configs",
				Context.MODE_PRIVATE);
		int isrInteger = preference.getInt("isNoMore", 0);
		int isMi = preference.getInt("isMIUI", 0);
		int isMei = preference.getInt("isMeiZu", 0);
		int isHol = preference.getInt("isHol", 0);

		CommonTools ct = new CommonTools();

		if (isrInteger != 1) {
			if (isMi == 1 || isMei == 1 || isHol == 1)
				ct.showDialog(ctx, 0);
		}

		TextView reToSet = (TextView) dlgv.findViewById(R.id.tolookText);
		TextView reToSets = (TextView) dlgv.findViewById(R.id.tolookText5);
		reToSet.setText("已成功设置为我的闪铃！");
		reToSets.setText("快去我的闪铃看看吧！");

		ImageView reback = (ImageView) dlgv
				.findViewById(R.id.my_surface_cancle);

		ImageView wb = (ImageView) dlgv.findViewById(R.id.wb);

		ImageView py = (ImageView) dlgv.findViewById(R.id.py);

		ImageView weixin = (ImageView) dlgv.findViewById(R.id.weixin);

		ImageView qq = (ImageView) dlgv.findViewById(R.id.qq);
		View view = (View) dlgv.findViewById(R.id.lookline);
		TextView tv = (TextView) dlgv.findViewById(R.id.tolookText9);

		wb.setVisibility(View.GONE);
		py.setVisibility(View.GONE);
		weixin.setVisibility(View.GONE);
		qq.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		tv.setVisibility(View.GONE);

		reback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
				ShowLocalVideo.toClose();

			}
		});

	}

}

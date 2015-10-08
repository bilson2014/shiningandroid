package com.panfeng.shining.widgets;

import java.io.File;

import org.json.JSONObject;

import com.panfeng.shinning.R;
import com.tencent.stat.StatService;

import tyu.common.net.DownloadListener;
import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import tyu.common.utils.TyuContextKeeper;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateManager {

	static String getSavePath(int vc) {
		Context context = TyuContextKeeper.getInstance();
		if (Environment.getExternalStorageDirectory().exists()) {
			File path = new File(Environment.getExternalStorageDirectory(),
					context.getPackageName() + "/v" + vc + "update.apk");
			path.getParentFile().mkdirs();
			return path.getAbsolutePath();

		} else {
			File path = new File(context.getFilesDir(),
					context.getPackageName() + "/v" + vc + "update.apk");
			path.getParentFile().mkdirs();
			return path.getAbsolutePath();
		}

	}

	static void updateProgress(final TextView txt, final ProgressBar pro,
			final String info, final int progress) {
		TyuContextKeeper.getHandler().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				txt.setText(info);
				pro.setProgress(progress);
			}
		});
	}

	static public void installApk(String aFile) {
		Context context = TyuContextKeeper.getInstance();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(aFile)),
				"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	static public boolean checkNewVersion(StringBuffer sb) {
		//String pname = TyuContextKeeper.getInstance().getPackageName();
		// int type = 0;
		// if (pname.equals("com.vchuang.tyuvandriver")) {
		// type = 1;
		// }
		String url = TyuDefine.HOST + "smc/swupdate";
		final String res = TyuHttpClientUtils.postInfo(url, "");
		if (TyuHttpClientUtils.isValidResult(res)) {
			try {
				PackageInfo pack_info = TyuContextKeeper
						.getInstance()
						.getPackageManager()
						.getPackageInfo(
								TyuContextKeeper.getInstance().getPackageName(),
								0);
				JSONObject jobj = new JSONObject(res);
				if (jobj.getInt("vcode") > pack_info.versionCode) {
					sb.append(jobj.toString());
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	static public void showUpdateInfo(final Activity aParent, String aJson)
			throws Exception {
		
		Toast.makeText(aParent, aParent+"", Toast.LENGTH_LONG);
		
		JSONObject obj = new JSONObject(aJson);
		View aboutView = aParent.getLayoutInflater().inflate(
				R.layout.update_view, null);
		final Dialog dialog = new Dialog(aParent, R.style.selectorDialog);
		PackageInfo pack_info = aParent.getPackageManager().getPackageInfo(
				aParent.getPackageName(), 0);
		dialog.setCanceledOnTouchOutside(false);
		final TextView info = (TextView) aboutView.findViewById(R.id.info);
		final ProgressBar pb = (ProgressBar) aboutView
				.findViewById(R.id.progressBar1);
		final int vcode = obj.getInt("vcode");
		if (vcode > pack_info.versionCode) {
			final String url = obj.getString("downpath");
			info.setText(String.format("发现新版本，版本号%s:\r\n%s",
					obj.getString("vname"), obj.getString("content")));
			final Button btn_sure = (Button) aboutView
					.findViewById(R.id.dialog_sure);
			final Button btn_cancel = (Button) aboutView
					.findViewById(R.id.dialog_cancel);
			btn_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (dialog != null && dialog.isShowing())
						dialog.dismiss();
				}
			});
			btn_sure.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					info.setText("开始下载:0%");
					pb.setVisibility(View.VISIBLE);
					btn_sure.setVisibility(View.GONE);
					
					
					//TODO:MTA updtae
					StatService.trackCustomEvent(aParent, "update_version", "");
					
					// btn_sure.setText("取消");
					new Thread() {
						@Override
						public void run() {
							TyuHttpClientUtils.downLoadFile(url,
									getSavePath(vcode), "",
									new DownloadListener() {
										@Override
										public void onProgressChanged(
												String name, int progress) {
											// TODO Auto-generated method stub
											updateProgress(info, pb, "开始下载:"
													+ progress + "%", progress);
										}

										@Override
										public void onError(String error) {
											// TODO Auto-generated method stub
											TyuContextKeeper.getHandler().post(
													new Runnable() {

														@Override
														public void run() {
															// TODO
															// Auto-generated
															// method stub
															info.setText("下载失败");
															btn_sure.setText("重试");
															btn_sure.setVisibility(View.VISIBLE);
														}
													});

										}

										@Override
										public void onSucess(String name) {
											// TODO Auto-generated method stub
											dialog.dismiss();
											installApk(getSavePath(vcode));
										}
									});
						}

					}.start();
				}
			});
		} else {
			info.setText("您当前已经是最新版本");
			aboutView.findViewById(R.id.dialog_sure).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});
			aboutView.findViewById(R.id.dialog_cancel).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (dialog != null && dialog.isShowing())
						dialog.dismiss();
				}
			});
		}

		dialog.setContentView(aboutView);
		dialog.show();
	}

	static public void showUpdateDialog(final Activity aParent) {
		final ProgressDialog dlg = new ProgressDialog(aParent);
		dlg.setMessage("正在检查更新");
		dlg.show();
		new Thread() {
			@Override
			public void run() {
				String pname = TyuContextKeeper.getInstance().getPackageName();
				String url = TyuDefine.HOST + "smc/swupdate";
				final String res = TyuHttpClientUtils.postInfo(url, "");
				if (dlg != null && dlg.isShowing())
					dlg.dismiss();
				if (TyuHttpClientUtils.isValidResult(res)) {
					TyuContextKeeper.getHandler().post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								showUpdateInfo(aParent, res);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
			};
		}.start();
	}
}

package com.panfeng.shining.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import tyu.common.utils.PhoneNumVerify;
import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuContextKeeper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.panfeng.shining.entity.UserInfoEntity;
import com.panfeng.shining.widgets.UpdateManager;
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;

public class TyuTools extends Activity {

	static Context ctx;
	static HashSet<String> veriSet = new HashSet<String>();
	

	static public void showUserVerify(final Activity aActivity, String message,
			final Runnable aCallback) {

		ctx = aActivity;

		SharedPreferences preference = ctx.getSharedPreferences("isStart",
				Context.MODE_PRIVATE);
		int isrInteger = preference.getInt("isSet", 0);
		String isrPhone= preference.getString("isPhone", "");

		LayoutInflater mLayoutInflater = (LayoutInflater) aActivity
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// 自定义布局
		ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.s2nd_user_verify_layout, null);
		final Dialog dialog = new Dialog(aActivity, R.style.selectorDialog);
		// 当然也可以手动设置PopupWindow大小。
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(menuView);
		dialog.show();

		// final TextView mMessage = (TextView) menuView
		// .findViewById(R.id.textView1);
		// mMessage.setText(message);
		final EditText mEditPhone = (EditText) menuView
				.findViewById(R.id.phone);
		final EditText mEditPwd = (EditText) menuView.findViewById(R.id.pwd);
		final View mVerifyBtn = menuView.findViewById(R.id.login);
		final View mCancelBtn = menuView.findViewById(R.id.close);
		final TextView mRecountBtn = (TextView) menuView
				.findViewById(R.id.recount);

		
		Log.i("lutao", "hul="+isrInteger);
		if (isrInteger > 1) {

			startTime(dialog, mRecountBtn);
			mEditPhone.setText(isrPhone);

		}

		OnClickListener clk = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.login: {
					String phone = mEditPhone.getText().toString();
					if (!new PhoneNumVerify().isVaildPhone(phone)) {
						TyuCommon.showToast(aActivity, "手机号码不正确,请重新输入");
						return;
					}
					String pwd = mEditPwd.getText().toString();
					if (TextUtils.isEmpty(pwd) || !veriSet.contains(pwd)) {
						TyuCommon.showToast(aActivity,
								"验证码不正确,请确认手机号码无误，并尝试重新获取验证码");
						return;
					}
					UserControl.getUserInfo().setUserPhone(phone);
					final ProgressDialog dlg = new ProgressDialog(aActivity);
					dlg.setMessage("正在验证...");
					dlg.show();
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							final boolean res = UserControl.updateUser(null, null);
							if (dlg != null && dlg.isShowing())
								dlg.dismiss();

							TyuContextKeeper.getHandler().post(new Runnable() {

								@Override
								public void run() {
									
									
									
									Properties prop = new Properties();
									prop.setProperty("how", "login");
									prop.setProperty("tel", mEditPhone
											.getText().toString());

									StatService.trackCustomBeginKVEvent(
											aActivity, "login", prop);
									
									SharedPreferences preference = ctx
											.getSharedPreferences("isStart",
													Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = preference.edit();
									editor.putInt("isSet", 1);
									editor.commit();
									
									UserInfoEntity  ue=UserControl.getUserInfo();
									ue.setUserPhone(mEditPhone
											.getText().toString());
									
									UserControl.updateUser(ue, null);
									// TODO Auto-generated method stub
									if (res) {
										dialog.dismiss();
										if (aCallback != null) {
											aCallback.run();
										}
										// TODO:LOGIN

							
										
										
									} else {
										
										
										
										
										TyuCommon.showToast(aActivity,"验证失败,请确认手机号码无误,以及网络是否顺畅");
									}
								}
							});

						}
					}).start();
				}

					break;
				case R.id.close:
					dialog.dismiss();
					break;
				case R.id.recount:

					final String phone = mEditPhone.getText().toString();

					if (!new PhoneNumVerify().isVaildPhone(phone)) {
						TyuCommon.showToast(aActivity, "手机号码不正确,请重新输入");
						return;
					}

					String tmp = System.currentTimeMillis() + "";
					tmp = tmp.substring(tmp.length() - 4, tmp.length());
					veriSet.add(tmp);
					final String veri_code = tmp;
					new Thread() {
						@Override
						public void run() {
							TyuTools.trySendVerifySms(phone, veri_code);
						};
					}.start();
					startRecount(dialog, mRecountBtn,mEditPhone, this);

					break;
				default:
					break;
				}

			}
		};

		mVerifyBtn.setOnClickListener(clk);

		mCancelBtn.setOnClickListener(clk);

		mRecountBtn.setOnClickListener(clk);

	}

	static int count = 0;
	

	

	static void startRecount(final Dialog aDlg, final TextView aBtn,final EditText aTex,
			final OnClickListener aClk) {
		count = 120;

		// aBtn.setBackgroundResource(R.drawable.btn_pwd);
		aBtn.setText(count + "");
		aBtn.setOnClickListener(null);

		Runnable run = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (aDlg != null && aDlg.isShowing()) {
					count--;
					
				

					if (count > 0) {

						SharedPreferences preference = ctx
								.getSharedPreferences("isStart",
										Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = preference.edit();
						editor.putInt("isSet", count);
						editor.putString("isPhone", aTex.getText().toString());
						editor.commit();
					}

					if (count <= 0) {
						// aBtn.setBackgroundResource(R.drawable.btn_pwd_p);
						aBtn.setText("验证码");
						aBtn.setOnClickListener(aClk);
						return;
					} else {
						aBtn.setText(count + "");
						TyuContextKeeper.getHandler().postDelayed(this, 1000);
					}
				}
			}
		};
		TyuContextKeeper.getHandler().postDelayed(run, 1000);
	}
	
	
	static void startTime(final Dialog aDlg, final TextView aBtn) {
		
		
		SharedPreferences preference = ctx.getSharedPreferences("isStart",
				Context.MODE_PRIVATE);
		int isrInteger = preference.getInt("isSet", 0);

		count = isrInteger;
		// aBtn.setBackgroundResource(R.drawable.btn_pwd);
		aBtn.setText(count + "");
	

		Runnable run = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (aDlg != null && aDlg.isShowing()) {
					count--;

					if (count > 0) {

						SharedPreferences preference = ctx
								.getSharedPreferences("isStart",
										Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = preference.edit();
						editor.putInt("isSet", count);
						editor.commit();
					}

					if (count <= 0) {
						// aBtn.setBackgroundResource(R.drawable.btn_pwd_p);
						aBtn.setText("验证码");
					
						return;
					} else {
						aBtn.setText(count + "");
						TyuContextKeeper.getHandler().postDelayed(this, 1000);
					}
				}
			}
		};
		TyuContextKeeper.getHandler().postDelayed(run, 1000);
	}
	
	
	
	
	
	
	
	
	
	

	static public void checkNewVersion(final Activity aActivity) {
		if (TyuCommon.isAnotherDay()) {
			new Thread() {
				@Override
				public void run() {
					final StringBuffer sb = new StringBuffer();
					boolean res = UpdateManager.checkNewVersion(sb);
					if (res) {
						TyuContextKeeper.doUiTask(new Runnable() {
							@Override
							public void run() {
								try {
									UpdateManager.showUpdateInfo(aActivity,
											sb.toString());

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

	static public void trySendVerifySms(String aNumber, String aVeriCode) {
		HashMap<String, Object> result = null;

		// 初始化SDK
		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

		// ******************************注释*********************************************
		// *初始化服务器地址和端口 *
		// *沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
		// *生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883"); *
		// *******************************************************************************
		restAPI.init("app.cloopen.com", "8883");

		// ******************************注释*********************************************
		// *初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN *
		// *ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
		// *参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。 *
		// *******************************************************************************
		restAPI.setAccount("8a48b5514abd771c014abdf8fc340061",
				"15e8b1c0b32d44fbb402a36877c43f50");

		// ******************************注释*********************************************
		// *初始化应用ID *
		// *测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID *
		// *应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
		// *******************************************************************************
		restAPI.setAppId("8a48b5514b0b8727014b2a4c94041bf0");
		// restAPI.setAppId("8a48b5514abea937014abeb701fa000b");

		// ******************************注释****************************************************************
		// *调用发送模板短信的接口发送短信 *
		// *参数顺序说明： *
		// *第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号 *
		// *第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。 *
		// *系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
		// *第三个参数是要替换的内容数组。 *
		// **************************************************************************************************

		// **************************************举例说明***********************************************************************
		// *假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为
		// *
		// *result = restAPI.sendTemplateSMS("13800000000","1" ,new
		// String[]{"6532","5"}); *
		// *则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入 *
		// *********************************************************************************************************************
		result = restAPI.sendTemplateSMS(aNumber, "12307", new String[] {
				aVeriCode, "10" });

		System.out.println("SDKTestGetSubAccounts result=" + result);
		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>) result
					.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				System.out.println(key + " = " + object);
			}
		} else {
			// 异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") + " 错误信息= "
					+ result.get("statusMsg"));
		}
	}
	


}

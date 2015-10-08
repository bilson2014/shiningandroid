package com.panfeng.shining.activity.s2nd;

import java.util.Properties;

import tyu.common.net.TyuDefine;
import tyu.common.utils.TyuCommon;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.panfeng.shining.data.TyuShinningData.VideoItemData;
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

public class S2ndShareActivity extends Activity {
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	static VideoItemData dataCache = null;
	String share_url = TyuDefine.URL + "share.jsp";
	String share_content = "";
	Context ctx = S2ndShareActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		if (dataCache == null) {
			finish();
			return;
		}
		share_url = share_url + "?id=" + dataCache.mb_id;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.s2nd_share_layout);
		findViewById(R.id.space).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_down_out,
						R.anim.push_down_out);
			}
		});

		findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_down_out,
						R.anim.push_down_out);
			}
		});
		configPlatforms();

		setShareContent();
		int[] ids = { R.id.share_item_1, R.id.share_item_2, R.id.share_item_3,
				R.id.share_item_4, R.id.share_item_5, };
		final SHARE_MEDIA[] share_ids = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.SINA, };

		// performShare(share_ids[index]);
		for (int i = 0; i < ids.length; i++) {
			final int index = i;
			if (i != (ids.length - 1)) {
				findViewById(ids[index]).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								performShare(share_ids[index]);
								
							
								
								
							}
						});

			} else {
				findViewById(ids[i]).setOnClickListener(new OnClickListener() {

					@SuppressLint("NewApi") @SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
						c.setText(share_content);// 设置Clipboard 的内容
						TyuCommon.showToast(getActivity(), "分享内容已复制到剪切板");
						
						//TODO:MTA
						Properties prop = new Properties();
						prop.setProperty("type", "剪切板");
						prop.setProperty("url", ""+dataCache.mb_id);
						StatService.trackCustomKVEvent(ctx, "share", prop);
					}
				});

			}
		}
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent() {
		String template = "您的好友 %s 给你分享一个好玩的视频，速速来看【%s】";
		String name = "";
//		if (TyuUserInfo.getInstance().name != null) {
//			name = TyuUserInfo.getInstance().name;
//		}
		String content = String.format(template, name, share_url);
		share_content = content;
		//
		mController.setShareContent(content);
	
		// 微信
	
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(content);
		weixinContent.setTitle("闪铃");
		weixinContent.setTargetUrl(share_url);
		weixinContent.setShareMedia(new UMImage(this, dataCache.image_url));
		mController.setShareMedia(weixinContent);
		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		circleMedia.setTitle("闪铃");
		circleMedia.setTargetUrl(share_url);
		circleMedia.setShareMedia(new UMImage(this, dataCache.image_url));
		mController.setShareMedia(circleMedia);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(content);

		qzone.setTargetUrl(share_url);
		qzone.setTitle("闪铃");
		qzone.setShareMedia(new UMImage(this, dataCache.image_url));
		mController.setShareMedia(qzone);
		// qq分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setTitle("闪铃");

		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(share_url);
		qqShareContent.setShareMedia(new UMImage(this, dataCache.image_url));
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
	
	//返回按键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/* 返回键 */
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		finish();
		overridePendingTransition(R.anim.push_down_out,
				R.anim.push_down_out);
		}
		return false;
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
					
					//TODO:MTA
					Properties prop = new Properties();
					prop.setProperty("type", ""+platform);
					prop.setProperty("url", ""+dataCache.mb_id);
					StatService.trackCustomKVEvent(ctx, "share", prop);
					
					
				} else {
					showText += "平台分享失败";
				}
				TyuCommon.showToast(getActivity(), showText);
				finish();
			}
		});
	}
}

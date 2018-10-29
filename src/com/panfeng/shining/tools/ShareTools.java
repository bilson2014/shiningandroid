package com.panfeng.shining.tools;

import tyu.common.utils.TyuCommon;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.panfeng.shinning.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
/*import com.umeng.socialize.bean.SocializeEntity;*/
import com.umeng.socialize.bean.StatusCode;
/*import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;*/
/*import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;*/
import com.umeng.socialize.media.UMImage;
/*import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent; 友盟*/

public class ShareTools {
	
	/*private static UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");*/

	/**
	 * @param 分享
	 */
	
	
	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	public static void setShareContent(Context ctx,String share_url,String share_content) {
		String template = "您的好友 %s 给你分享一个好玩的视频，速速来看【%s】";
		String name = "";
		if (UserControl.getUserInfo().getUserName() != null) {
			name = UserControl.getUserInfo().getUserName();
		}

		String content = String.format(template, name, share_url);
		share_content = content;
		//
		//mController.setShareContent(content);

		// 微信
		Log.i("slw", "content=" + share_content);
		Log.i("slw", "share=" + share_url);
		UMImage umImage = new UMImage(ctx, R.drawable.app_share);

		/*WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(content);
		weixinContent.setTitle("闪铃");
		weixinContent.setTargetUrl(share_url);
		weixinContent.setShareImage(umImage);
		mController.setShareMedia(weixinContent);
		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		circleMedia.setTitle("闪铃");
		circleMedia.setTargetUrl(share_url);
		circleMedia.setShareMedia(umImage);
		mController.setShareMedia(circleMedia);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(content);

		qzone.setTargetUrl(share_url);
		qzone.setTitle("闪铃");
		qzone.setShareMedia(umImage);
		mController.setShareMedia(qzone);
		// qq分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setTitle("闪铃");

		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(share_url);
		
		// 设置分享图片, 参数2为图片的url地址
		qqShareContent.setShareMedia(umImage);
		mController.setShareMedia(qqShareContent);*/
	}

	/**
	 * 配置分享平台参数</br>
	 */
	public static void configPlatforms(Context ctx) {
	/*	mController.getConfig().closeToast();
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());*/
	
		// 添加腾讯微博SSO授权
		//mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		// // 添加人人网SSO授权
		// RenrenSsoHandler renrenSsoHandler = new
		// RenrenSsoHandler(getActivity(),
		// "201874", "28401c0964f04a72a14c812d6132fcef",
		// "3bf66e42db1e4fa9829b955cc300b737");
		// mController.getConfig().setSsoHandler(renrenSsoHandler);

		// 添加QQ、QZone平台
		addQQQZonePlatform(ctx);

		// 添加微信、微信朋友圈平台
		addWXPlatform(ctx);
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	public static void addWXPlatform(Context ctx) {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wxaf12b567e6be5abf";
		String appSecret = "a60cd6661f912d1aee285b0043332875";
		// 添加微信平台
	/*	UMWXHandler wxHandler = new UMWXHandler(ctx, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(ctx, appId,
				appSecret);*/
	/*	wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();*/
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	public static void addQQQZonePlatform(Context ctx) {
		String appId = "1104334973";
		String appKey = "c7394704798a158208a74ab60104f0ba";
		// 添加QQ支持, 并且设置QQ分享内容的target url
	//	UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) ctx, appId,appKey);

	//	qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
	//	QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) ctx,
	//			appId, appKey);
	//	qZoneSsoHandler.addToSocialSDK();
	}



	/*public static void performShare(SHARE_MEDIA platform,final Context ctx) {
		mController.postShare(ctx, platform, new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					showText += "平台分享成功";

					String type = platform + "";

				} else if(eCode == StatusCode.ST_CODE_ERROR){
					showText += "分享异常";
				}
				
				 else if(eCode == StatusCode.ST_CODE_ERROR_WEIXIN){
						showText += "微信分享异常";
					}
				
				
				
				TyuCommon.showToast(ctx, showText);
				//((Activity) ctx).finish();
			}
		});
	}*/
	

	
	


}

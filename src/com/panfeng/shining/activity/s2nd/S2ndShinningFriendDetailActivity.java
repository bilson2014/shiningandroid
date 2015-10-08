package com.panfeng.shining.activity.s2nd;

//import java.util.Properties;
//
//import tyu.common.utils.TyuCommon;
//import tyu.common.utils.TyuContextKeeper;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.panfeng.shining.TyuApp;
//import com.panfeng.shining.TyuVideoPlayTask2nd;
//import com.panfeng.shining.TyuApp.TyuImageLoadingListener;
//import com.panfeng.shining.data.TyuShinningData;
//import com.panfeng.shining.data.TyuUserInfo;
//import com.panfeng.shining.data.TyuShinningData.ContactFriendItemData;
//import com.panfeng.shining.data.TyuShinningData.ShinningFriendItemData;
//import com.panfeng.shining.data.TyuShinningData.VideoItemData;
//import com.panfeng.shining.widgets.TyuWaitingBar;
//import com.panfeng.shinning.R;
//import com.tencent.stat.StatService;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class S2ndShinningFriendDetailActivity extends Activity {
//	static public ShinningFriendItemData dataCache = null;
//	static public boolean showMyShinning = false;
//
//	private ViewGroup mediaFrame;
//	private ViewGroup videoPlayer;
//	private ViewGroup frame;
//
//	final int TOP = 0;
//	final int CENTER = 1;
//	final int BOTTOM = 2;
//	private DisplayImageOptions options;
//	private TyuImageLoadingListener simpleImageListener;
//	private ImageLoader imageLoader;
//	private TyuVideoPlayTask2nd videoTask;
//	private View[] part2_group;
//	private View show_note;
//	
//	Context ctx = S2ndShinningFriendDetailActivity.this; 
//			
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		if (dataCache == null && !showMyShinning) {
//			finish();
//			return;
//		} else if (showMyShinning) {
//			initMine();
//		}
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.s2nd_shinning_friends_item_detail);
//		// 处理提示框，分为两种，一种给好友，一种给
//		show_note = findViewById(R.id.show_note);
//		TextView pick_one = (TextView) show_note.findViewById(R.id.pick_one);
//		TextView fr_info = (TextView) show_note.findViewById(R.id.fr_info);
//		pick_one.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//		pick_one.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				startActivity(new Intent(getActivity(),
//						S2ndShinningStoreActivity.class));
//				finish();
//			}
//		});
//		if (showMyShinning) {
//			pick_one.setVisibility(View.VISIBLE);
//			ImageView img = (ImageView)findViewById(R.id.imageView2);
//			img.setImageResource(R.drawable.s2nd_my_shinning_note_icon);
//		} else {
//			pick_one.setVisibility(View.INVISIBLE);
//			fr_info.setText("您的好友未设置闪铃");
//		}
//		
//		
//		show_note.setVisibility(View.INVISIBLE);
//		initTitleBar();
//		if (!isValidData(dataCache)) {
//			show_note.setVisibility(View.VISIBLE);
//			return;
//		}
//
//		options = TyuApp.getCommonConfig();
//		simpleImageListener = new TyuImageLoadingListener();
//		imageLoader = ImageLoader.getInstance();
//
//		mediaFrame = (ViewGroup) getLayoutInflater().inflate(
//				R.layout.s2nd_video_item, null);
//		ViewGroup vf = (ViewGroup) findViewById(R.id.content_frame);
//		vf.addView(mediaFrame);
//
//		videoPlayer = (ViewGroup) getLayoutInflater().inflate(
//				R.layout.s2nd_video_player, null);
////		videoPlayer.setVisibility(View.GONE);
//		frame = (ViewGroup) mediaFrame.findViewById(R.id.frame);
//		// 设置高度
//		LayoutParams lp = frame.getLayoutParams();
//		if (lp != null) {
//			lp.height = getResources().getDisplayMetrics().widthPixels;
//			frame.setLayoutParams(lp);
//		}
//
//		ViewGroup part1 = (ViewGroup) frame.findViewById(R.id.part1);
//		part1.addView(videoPlayer);
//		//视频图片
//		ImageView video_image = (ImageView) videoPlayer
//				.findViewById(R.id.img);
//		video_image.setVisibility(View.VISIBLE);
//		video_image.setImageBitmap(null);
//		if(dataCache.mb_info!=null&&dataCache.mb_info.image_url!=null){
//			imageLoader.displayImage(dataCache.mb_info.image_url, video_image, options);
//		}
//		
//		videoTask = new TyuVideoPlayTask2nd(videoPlayer);
//		videoTask.setupData(dataCache.mb_info);
//
//		part2_group = new View[] { mediaFrame.findViewById(R.id.part2_top),
//				mediaFrame.findViewById(R.id.part2_center),
//				mediaFrame.findViewById(R.id.part2_bottom) };
//
//		initVideoDetail();
//	}
//
//	boolean isValidData(ShinningFriendItemData aData) {
//		if (aData.mb_info != null && aData.mb_info.video_url != null) {
//			return true;
//		}
//		return false;
//	}
//
//	void initMine() {
//		dataCache = new ShinningFriendItemData();
//		dataCache.name = "我";
//		dataCache.number = TyuUserInfo.getInstance().phone;
//		dataCache.user_image = TyuUserInfo.getInstance().user_image;
//		dataCache.mb_info = TyuShinningData.getInstance().getCurShinning();
//		dataCache.local_info = new ContactFriendItemData();
//		dataCache.local_info.name = "我";
//		dataCache.local_info.number = TyuUserInfo.getInstance().phone;
//	}
//
//	void initVideoDetail() {
//
//		final VideoItemData vData = dataCache.mb_info;
//		final View contentView = mediaFrame;
//		videoTask.setupData(vData);
//		// final int index = position;
//		// 处理交互逻辑
//		// 名次
//		TextView redTxt = (TextView) part2_group[TOP]
//				.findViewById(R.id.red_txt_info);
//
//		redTxt.setText("");
//
//		// 分享按钮
////		View share_btn = part2_group[TOP].findViewById(R.id.share_btn);
////		share_btn.setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View v) {
////				// TODO Auto-generated method stub
////				S2ndShareActivity.dataCache = vData;
////				launchActivity(S2ndShareActivity.class);
////				overridePendingTransition(R.anim.bottom_in, 0);
////			}
////		});
//
//		// 视频图片
//		ImageView video_image = (ImageView) contentView
//				.findViewById(R.id.img);
//		if (!TextUtils.isEmpty(vData.image_url))
//			imageLoader.displayImage(vData.image_url, video_image, options);
//		// 视频信息
//		TextView media_name = (TextView) part2_group[TOP]
//				.findViewById(R.id.media_name);
//		
//		media_name.setText(vData.name);
//		TextView author_name = (TextView) part2_group[TOP]
//				.findViewById(R.id.author_name);
//		
//		author_name.setText(TyuShinningData.getFakeAuthorName(vData.video_url));
//		// 播放按钮
//
//		View playButton = part2_group[CENTER].findViewById(R.id.play_btn);
//		ImageView shinning_setting = (ImageView) contentView
//				.findViewById(R.id.shinning_setting);
//
//		if (showMyShinning) {
//			shinning_setting
//					.setImageResource(R.drawable.s2nd_shinning_delete_btn);
//			if (part2_group[TOP].getVisibility() == View.VISIBLE) {
//				showVideoItemParts(part2_group);
//			} else {
//				hideVideoItemParts(part2_group);
//			}
//			shinning_setting.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					tryDeleteShinning();
//				}
//			});
//		} else {
//			if (part2_group[TOP].getVisibility() == View.VISIBLE) {
//				showVideoItemParts(part2_group);
//				shinning_setting
//						.setImageResource(R.drawable.s2nd_shinning_setting_1_btn);
//			} else {
//				hideVideoItemParts(part2_group);
//				shinning_setting
//						.setImageResource(R.drawable.s2nd_shinning_setting_2_btn);
//
//			}
//			shinning_setting.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					trySetShinning(contentView, vData);
//				}
//			});
//		}
//
//		playButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// playPostion = index;
//
//				hideVideoItemParts(part2_group);
//				installVideoPlugin(contentView, vData, true);
//				
//				
//				//TODO:MTA play自己 and friend
//				int v_id = vData.mb_id;
//				if(showMyShinning){
//				
//				
//				Properties prop = new Properties();
//			    prop.setProperty("play_my_video", ""+v_id);
//				StatService.trackCustomKVEvent(ctx, "play_video",
//						prop);
//				}
//				
//				else{
//					Properties prop = new Properties();
//				    prop.setProperty("play_friend_video", ""+v_id);
//					StatService.trackCustomKVEvent(ctx, "play_video",
//							prop);
//					
//					
//					
//				}
//
//			}
//		});
//	}
//
//	void initTitleBar() {
//		View title = findViewById(R.id.title_bar);
//		ImageView back = (ImageView) title.findViewById(R.id.back);
//		TextView title_txt = (TextView) title.findViewById(R.id.txt);
//		if (showMyShinning) {
//			title_txt.setText("我的闪铃");
//		} else {
//			if (dataCache.local_info != null
//					&& dataCache.local_info.name != null) {
//				title_txt.setText(dataCache.local_info.name);
//			} else
//				title_txt.setText(dataCache.name);
//		}
//
//		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//防止播放器崩溃
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				finish();
//				overridePendingTransition(R.anim.push_right_in,
//						R.anim.push_right_out);
//			}
//		});
//
//	}
//
//	void installVideoPlugin(View aMediaItem, VideoItemData aData,
//			boolean startNow) {
//		videoTask.stop();
//		if (startNow) {
//			videoPlayer.setVisibility(View.VISIBLE);
//			videoPlayer.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					uninstallVideoPlugin();
//					showVideoItemParts(part2_group);
//					
//					
//					
//				}
//			});
//			
//			
//			videoTask.playvideo();
//		} else {
//			videoPlayer.setVisibility(View.GONE);
//		}
//	}
//
//	void uninstallVideoPlugin() {
//		if (videoTask != null && videoPlayer != null) {
//			videoTask.stop();
//			videoPlayer.setVisibility(View.GONE);
//		}
//
//	}
//
//	public void hideVideoItemParts(View[] parts) {
//		for (int i = 0; i < parts.length; i++) {
//			if (i == BOTTOM) {
//				parts[i].findViewById(R.id.part2_bottom_sub).setVisibility(
//						View.GONE);
//			} else {
//				parts[i].setVisibility(View.INVISIBLE);
//			}
//		}
//	}
//
//	public void trySetShinning(View contentView, VideoItemData aData) {
//		S2ndShinningSettingActivity.data_cache = aData;
//		launchActivity(S2ndShinningSettingActivity.class);
//	}
//
//	public void tryDeleteShinning() {
//		final Dialog dlg = TyuWaitingBar.build(this);
//		dlg.show();
//		new Thread() {
//			@Override
//			public void run() {
//				try {
//					
//					
//					//TUDO:MTA del video
//					Properties prop = new Properties();
//					prop.setProperty("del_video_id",""+TyuUserInfo.getInstance().current_mb_id ); 
//					StatService.trackCustomKVEvent(ctx, "del_video", prop);
//					
//					
//					TyuUserInfo.getInstance().current_mb_id = 0;
//					TyuUserInfo.getInstance().cancelShinning();
//					TyuShinningData.getInstance().setCurShinning(null);
//					TyuContextKeeper.doUiTask(new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							TyuCommon.showToast(getActivity(), "删除闪铃成功");
//							// 刷新页面
//							// initMine();
//							// initVideoDetail();
//							mediaFrame.setVisibility(View.GONE);
//							show_note.setVisibility(View.VISIBLE);
//							if (dlg != null && dlg.isShowing())
//								dlg.dismiss();
//						}
//
//					});
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			};
//		}.start();
//
//	}
//
//	private Context getActivity() {
//		// TODO Auto-generated method stub
//		return this;
//	}
//
//	public void showVideoItemParts(View[] parts) {
//		if (parts == null)
//			return;
//		for (int i = 0; i < parts.length; i++) {
//			if (parts[i] == null)
//				return;
//			if (i == BOTTOM) {
//				parts[i].findViewById(R.id.part2_bottom_sub).setVisibility(
//						View.VISIBLE);
//			} else {
//				parts[i].setVisibility(View.VISIBLE);
//			}
//		}
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		uninstallVideoPlugin();
//		if (part2_group != null) {
//			showVideoItemParts(part2_group);
//		}
//	}
//
//	void launchActivity(Class aClass) {
//
//		startActivity(new Intent(getAvtivity(), aClass));
//		overridePendingTransition(R.anim.push_left_in, 0);
//	}
//
//	private Activity getAvtivity() {
//		// TODO Auto-generated method stub
//		return this;
//	}
//}

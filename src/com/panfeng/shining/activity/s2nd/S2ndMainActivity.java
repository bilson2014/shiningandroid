package com.panfeng.shining.activity.s2nd;

import java.util.List;

import tyu.common.utils.CircleBitmapDisplayer;
import tyu.common.utils.TyuContextKeeper;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.helpdeskdemo.activity.LoginActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.panfeng.shining.TyuApp.TyuImageLoadingListener;
import com.panfeng.shining.activity.TyuUserInfoActivity;
import com.panfeng.shining.activity.newactivity.LocalVideoActivity;
import com.panfeng.shining.activity.newactivity.WebViewActivity;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.data.TyuShinningData.ShinningFriendItemData;
import com.panfeng.shining.data.TyuShinningData.VideoItemData;
import com.panfeng.shining.data.TyuShinningFriendIndex;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.entity.UserInfoEntity;
import com.panfeng.shining.service.TyuShinningFriendSubService;
import com.panfeng.shining.tools.FVMetaData;
import com.panfeng.shining.tools.TyuTools;
import com.panfeng.shining.tools.UserControl;
import com.panfeng.shinning.R;

public class S2ndMainActivity extends Activity implements OnClickListener {
	private ImageView userIcon, layoutIcon, layoutAfIcon, system_setting;
	private TextView userName, layout;
	private TextView userInfo, que;
	private static int userCountBase = 80000;
	private static final String TAG = "lutao";
	private Context ctx = S2ndMainActivity.this;
	
	

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new CircleBitmapDisplayer())
			.showImageForEmptyUri(R.drawable.s2nd_default_user_icon)
			.showImageOnFail(R.drawable.s2nd_default_user_icon)
			.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory()// 这是为了降低刷新过程中闪烁的时间
			.cacheOnDisc().build();
	private TyuImageLoadingListener simpleImageListener = new TyuImageLoadingListener();;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	final Handler mUserInfoHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			doUserInfoTask();

		}
	};

	private BroadcastReceiver receiver;
	private ImageView hot_dot;

	UserInfoEntity uc ;
	mHander hander = new mHander();

	
	
	protected void onSaveInstanceState(){
		super.onSaveInstanceState(null);
		
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.s2nd_main_layout);
		initUI();
		initTitleBar();
		
		
	
		try {
			
			uc =  UserControl.getUserInfo();
			
		
		} catch (Exception e) {
			
		}
		
		
		
	//	FVMetaData.getContactNameFromPhoneBook(this,"18801376524");
		
		

	
		
		
		
	//	initBottomBar();

//		if (TyuPreferenceManager.isFirstOpen("newPlayer")) {
//
//			LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//			// 自定义布局
//			ViewGroup dlgView = (ViewGroup) mLayoutInflater.inflate(
//					R.layout.needlook_layout, null);
//			final Dialog dialog = new Dialog(this, R.style.selectorDialog);
//
//			dialog.setCanceledOnTouchOutside(false);
//			dialog.setContentView(dlgView);
//			dialog.show();
//
//			ImageView my_shinning = (ImageView) dlgView
//					.findViewById(R.id.need_img);
//			my_shinning.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					dialog.dismiss();
//
//					Intent intent = new Intent(S2ndMainActivity.this,
//							S2ndShinningStoreActivity.class);
//
//					startActivity(intent);
//				
//				}
//			});
//
//		}

		

		// 闪铃好友更新检查
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				checkNewSfInfo();
			}

		};
		TyuShinningFriendSubService.regReceiver(this, receiver);
		TyuTools.checkNewVersion(this);
	}
	
	
	
	void initTitleBar() {
		View title = findViewById(R.id.newlistbar);
	
		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		ImageView toset = (ImageView) title.findViewById(R.id.bar_set);
		toset.setVisibility(View.VISIBLE);
		barimg.setBackgroundResource(R.drawable.tosetlayout);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
		bartxt.setText("闪星人");
		
		toset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				launchActivity(s2ndSystemSettingActivity.class);
			}
		});
		
	
		
		
		
	}
	
	
	
	

	void stopUserInfoTask() {
		mUserInfoHandler.removeMessages(0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopUserInfoTask();
		TyuShinningFriendSubService.unregReceiver(this, receiver);
	}

	void checkNewSfInfo() {
		List<ShinningFriendItemData> data = TyuShinningFriendSubService
				.getShinningFrData();
		if (TyuShinningFriendIndex.getInstance().hasNewInfo(data)) {
			hot_dot.setVisibility(View.VISIBLE);

		} else {
			hot_dot.setVisibility(View.INVISIBLE);
		}
	}

	int state_step = 0;

	void doUserInfoTask() {
		new Thread() {
			@Override
			public void run() {
				if (uc!=null&&!UserControl.isLogin()) {
					// 显示编号
					int id = UserControl.getUserInfo().getUserId();
					String txt = "";
					if (id <= 0) {
						// 制作虚假编号
						String tmp = System.currentTimeMillis() + "";
						tmp = tmp.substring(tmp.length() - 6);
						txt = String.format("闪星来客%s号", tmp);
					} else {
						txt = String.format("闪星来客%d号", id);
					}
					final String info = txt;
					TyuContextKeeper.doUiTask(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							userInfo.setText(info);
						}
					});

				} else {
					if (state_step == 0) {
						// 获取用户数量
						final int count = TyuShinningData.getInstance()
								.getShinningUserCount() + userCountBase;
						TyuContextKeeper.doUiTask(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (count > 0) {
									userInfo.setText(count + "闪星人在线");
								} else {
									String tmp = System.currentTimeMillis()
											+ "";
									tmp = tmp.substring(tmp.length() - 5);
									userInfo.setText(tmp + "闪星人在线");
								}
							}
						});

					} else if (state_step == 1) {
						List<ShinningFriendItemData> data = TyuShinningFriendSubService
								.getShinningFrData();
						final List<ShinningFriendItemData> res = TyuShinningFriendIndex
								.getInstance().SFNewState(data);
						if (res != null && res.size() > 0) {
							TyuContextKeeper.doUiTask(new Runnable() {
								@Override
								public void run() {
									userInfo.setText(res.size() + "个好友更新了闪铃");
								}
							});
						}
					} else if (state_step == 2) {
						List<ShinningFriendItemData> data = TyuShinningFriendSubService
								.getShinningFrData();
						final List<ShinningFriendItemData> res = TyuShinningFriendIndex
								.getInstance().SFNewUser(data);
						if (res != null && res.size() > 0) {
							TyuContextKeeper.doUiTask(new Runnable() {
								@Override
								public void run() {
									ShinningFriendItemData user = res.get(0);
									if (user.local_info != null
											&& user.local_info.name != null) {
										userInfo.setText("好友 "
												+ user.local_info.name + " 加入了");
									}
								}
							});
						}
					} else if (state_step == 3) {
						// 获取今日新增闪铃数量
						final List<VideoItemData> res = TyuShinningData
								.getInstance().getMediaByToday();
						if (res != null && res.size() > 0) {
							TyuContextKeeper.doUiTask(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									userInfo.setText("今日新增了" + res.size()
											+ "条闪铃");

								}
							});
						}
					}
					state_step = (state_step + 1) % 4;
				}

				mUserInfoHandler.sendEmptyMessageDelayed(0, 1000 * 10);
			};
		}.start();

	}

	void initUI() {
		userIcon = (ImageView) findViewById(R.id.user_icon);
		userIcon.setOnClickListener(this);
		userName = (TextView) findViewById(R.id.user_name);
		userName.setOnClickListener(this);
		userInfo = (TextView) findViewById(R.id.user_info);
		userInfo.setOnClickListener(this);
		system_setting = (ImageView) findViewById(R.id.system_setting);
		system_setting.setOnClickListener(this);

		ImageView systemSetting = (ImageView) findViewById(R.id.system_setting);
		systemSetting.setOnClickListener(this);

		SettingItemType1 myShing = new SettingItemType1(R.id.item1_1);
		myShing.layout.setText("我的闪铃");
		myShing.layoutIcon.setBackgroundResource(R.drawable.myshin);
		myShing.layoutAfIcon.setBackgroundResource(R.drawable.myshinicon);
		myShing.content.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View v) {
				
//				   SharedPreferences	preference = ctx.getSharedPreferences("configs",
//							Context.MODE_PRIVATE);
//				int	isrInteger = preference.getInt("isSet", 0);
				
			    DBAdapter	db = new DBAdapter(ctx);
				db.open();
				
				Cursor c=db.getMyVideo();
				
				if(c.moveToFirst()&&c.getInt(1)==1){
					

					launchActivity(ShowMyMediaActivity.class);
					
				}
				
				else{
					 Toast.makeText(ctx, "您还没有设置视频", Toast.LENGTH_LONG).show();
					
				}
				
				db.close();
				
				
					
//				if(isrInteger == 0){
//				
//				 Toast.makeText(ctx, "您还没有设置视频", Toast.LENGTH_LONG).show();
//					
//					
//				}
//				else{
//					launchActivity(ShowMyMediaActivity.class);
//				}

			

			}
		});

		SettingItemType1 myAllVideo = new SettingItemType1(R.id.item1_2);
		myAllVideo.layout.setText("我的收藏");
		myAllVideo.layoutIcon.setBackgroundResource(R.drawable.allvideo);
		myAllVideo.layoutAfIcon.setBackgroundResource(R.drawable.toicon);
		myAllVideo.content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			//	launchActivity(FindMyMediaAllVideoActivity.class);
				launchActivity(FindMyMediaAllVideoActivity.class);
			//	launchActivity(LocalVideoActivity.class);

			}
		});

		SettingItemType1 myVideo = new SettingItemType1(R.id.item1_3);
		myVideo.layout.setText("我的作品");
		myVideo.layoutIcon.setBackgroundResource(R.drawable.myvideo);
		myVideo.layoutAfIcon.setBackgroundResource(R.drawable.toicon);
		myVideo.content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				launchActivity(FindMyMediaProductionActivity.class);

			}
		});

		SettingItemType1 que = new SettingItemType1(R.id.item1_4);
		que.layout.setText("常见问题");
		que.layoutIcon.setBackgroundResource(R.drawable.que);
		que.layoutAfIcon.setBackgroundResource(R.drawable.toicon);
		que.content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				launchActivity(S2ndShiningAnswer.class);
				

			}
		});

		SettingItemType1 feedback = new SettingItemType1(R.id.item1_5);
		feedback.layout.setText("问题反馈");
		feedback.layoutIcon.setBackgroundResource(R.drawable.feedback);
		feedback.layoutAfIcon.setBackgroundResource(R.drawable.toicon);
		feedback.content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				if(uc!=null){
				
				String name = UserControl.getUserInfo().getUserName();
				TelephonyManager mTelephonyMgr = (TelephonyManager) ctx
						.getSystemService(Context.TELEPHONY_SERVICE);
				String imei = mTelephonyMgr.getDeviceId();
				if (name == null) {

					Toast.makeText(ctx, "请您先注册闪铃用户~", Toast.LENGTH_LONG).show();
				}

				else {

					if (imei == null || imei.length() == 0) {
						Toast.makeText(ctx, "您的设备不支持反馈 请加QQ群49038471",
								Toast.LENGTH_LONG).show();
					}

					else {

						// feedback.infoiv.setVisibility(View.GONE);
						startActivity(new Intent(getActivity(),LoginActivity.class));
						
						}
				
			}
			}
				else{
					
					Toast.makeText(ctx, "用户信息异常 请尝试重新登录",
							Toast.LENGTH_LONG).show();
				}
			}
			
		
		});

		
		SettingItemType1 miui = new SettingItemType1(R.id.item1_4_info);
		miui.layout.setText("小米活动--提建议中大奖！");
		miui.layoutIcon.setBackgroundResource(R.drawable.miui);
		miui.layoutAfIcon.setBackgroundResource(R.drawable.toicon);
		miui.content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				startActivity(new Intent(getActivity(),WebViewActivity.class));
				

			}
		});
		
		SettingItemType1 video = new SettingItemType1(R.id.item1_5_info);
		video.layout.setText("video");
		video.layoutIcon.setBackgroundResource(R.drawable.miui);
		video.layoutAfIcon.setBackgroundResource(R.drawable.toicon);
		video.content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				startActivity(new Intent(getActivity(),WebViewActivity.class));
				

			}
		});
		
		
		
		
		
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(uc!=null){
		refreshUserState();
		// 启动循环任务
		mUserInfoHandler.removeMessages(0);
		state_step = 0;
		doUserInfoTask();
		}
	}

	void refreshUserState() {
		if (!UserControl.isLogin()) {
			userName.setVisibility(View.VISIBLE);
			userName.setText("求真相");
			// userInfo.setVisibility(View.INVISIBLE);
			// s2nd_default_user_icon
			userInfo.setText("");
			userIcon.setImageResource(R.drawable.newlogin);
		} else {
			String name = UserControl.getUserInfo().getUserName();
			if (!TextUtils.isEmpty(name)) {
				userName.setVisibility(View.VISIBLE);
				userName.setText("" + name);

			} else {
				userName.setText("欢迎回来");
			}
			String info = UserControl.getUserInfo().getUserPhone();
			if (!TextUtils.isEmpty(info)) {
				userInfo.setText(info);
				// userInfo.setVisibility(View.VISIBLE);
			}

			String user_img_path = UserControl.getUserInfo().getUserImage();
			Log.i("dirlist", "user_img_path"+user_img_path);
			if (!TextUtils.isEmpty(user_img_path)) {
//				imageLoader.displayImage(
//						Scheme.FILE.wrap(user_img_path), userIcon,
//						options);
				
				imageLoader.displayImage(user_img_path, userIcon, options);
			}

		}
		// 交互逻辑
		userIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				Log.i("slwslt", UserControl.getUserInfo().toString());

				// TODO Auto-generated  登录
				if (!UserControl.isLogin()) {
					hander.sendEmptyMessage(2);

				} else {
					startActivity(new Intent(getAvtivity(),
							TyuUserInfoActivity.class));

				}
			}
		});
	}

	Activity getAvtivity() {
		return this;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_icon:
			break;
		case R.id.user_name:

			break;
		case R.id.user_info:

			break;
		case R.id.my_shinning:
//			S2ndShinningFriendDetailActivity.showMyShinning = true;
//			launchActivity(S2ndShinningFriendDetailActivity.class);
			break;

		case R.id.system_setting:
			launchActivity(s2ndSystemSettingActivity.class);
			break;

		default:
			break;
		}
	}

	Activity getActivity() {
		return this;
	}

	void launchActivity(Class aClass) {
		startActivity(new Intent(getAvtivity(), aClass));
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
//
//	void initBottomBar() {
//
//		View title = findViewById(R.id.item2_1);
//		ImageView back = (ImageView) title.findViewById(R.id.tocamera);
//		ImageView setting = (ImageView) title.findViewById(R.id.toseting);
//		ImageView videolist = (ImageView) title.findViewById(R.id.tovideolist);
//
//		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				Intent intent = new Intent();
//				intent.setClass(ctx, ChooseMusic.class);
//				startActivity(intent);
//				finish();
//				
//
//			}
//		});
//
//		setting.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				// Intent intent = new Intent();
//				// intent.setClass(ctx, S2ndMainActivity.class);
//				// startActivity(intent);
//
//			}
//		});
//
//		videolist.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				Intent intent = new Intent();
//				intent.setClass(ctx, S2ndShinningStoreActivity.class);
//				startActivity(intent);
//				finish();
//
//			}
//		});
//
//	}

	class SettingItemType1 {
		public SettingItemType1(int aId) {
			content = findViewById(aId);
			layout = (TextView) content.findViewById(R.id.txt1);
			layoutIcon = (ImageView) content.findViewById(R.id.seticon);
			layoutAfIcon = (ImageView) content.findViewById(R.id.infoicon);
		}

		public View content;
		public TextView layout;
		public ImageView layoutIcon;
		public ImageView layoutAfIcon;

	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

//			if(secondDel())
//				Toast.makeText(S2ndMainActivity.this, "再按一次退出",Toast.LENGTH_SHORT).show();
//				
//			
//			
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	private long clickTime = 0;
	// 双击重置
	public boolean secondDel() {

		if ((System.currentTimeMillis() - clickTime) > 2000) {
			clickTime = System.currentTimeMillis();
			
			return true;
		} else {
			
			finish();
			return false;
		}
	}
	
	class mHander extends Handler {

		boolean loadMore = true;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(msg.what==1){
			Toast.makeText(ctx, "用户信息异常", Toast.LENGTH_SHORT).show();
			}
			
			else if (msg.what==2){
				TyuTools.showUserVerify(getAvtivity(), "登录验证",
						new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								if(uc!=null){
								
								refreshUserState();
								startActivity(new Intent(getAvtivity(),
										TyuUserInfoActivity.class));
								}
								
								else{
									hander.sendEmptyMessage(1);
									
								
									
								}

							}
						});
				
			}

		}

	}
	

}

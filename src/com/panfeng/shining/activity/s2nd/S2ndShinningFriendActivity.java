//package com.panfeng.shining.activity.s2nd;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import tyu.common.utils.CircleBitmapDisplayer;
//import tyu.common.utils.TyuContextKeeper;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.panfeng.shining.TyuApp;
//import com.panfeng.shining.TyuApp.TyuImageLoadingListener;
//import com.panfeng.shining.data.TyuShinningData;
//import com.panfeng.shining.data.TyuShinningData.ContactFriendItemData;
//import com.panfeng.shining.data.TyuShinningData.ShinningFriendItemData;
//import com.panfeng.shining.data.TyuShinningFriendIndex;
//import com.panfeng.shining.data.TyuUserInfo;
//import com.panfeng.shining.tools.TyuTools;
//import com.panfeng.shining.widgets.TyuWaitingBar;
//import com.panfeng.shinning.R;
//
//public class S2ndShinningFriendActivity extends Activity {
//	private View show_note;
//	private ListView friends_list;
//
//	ImageLoader imageLoader = ImageLoader.getInstance();
//	DisplayImageOptions options = TyuApp.getCommonConfig();
//	DisplayImageOptions circle_options = new DisplayImageOptions.Builder()
//			.bitmapConfig(Bitmap.Config.RGB_565)
//			.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory()// 这是为了降低刷新过程中闪烁的时间
//			.displayer(new CircleBitmapDisplayer()).cacheOnDisc().build();
//	ImageLoadingListener simpleImageListener = new TyuImageLoadingListener();
//	private ArrayList<ContactFriendItemData> local;
//	private ArrayList<ShinningFriendItemData> shinning;
//	private S2ndFriendAdpter adpter;
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		//adpter.notifyDataSetChanged();
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.s2nd_shinning_friends_layout);
//		local = new ArrayList<ContactFriendItemData>();
//		shinning = new ArrayList<ShinningFriendItemData>();
//
//		show_note = findViewById(R.id.show_note);
//		show_note.setVisibility(View.INVISIBLE);
//		TextView nooos = (TextView) show_note.findViewById(R.id.veri_number);
//		nooos.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
////		if (TyuUserInfo.getInstance().isLogin()) {
////			nooos.setVisibility(View.GONE);
////		} else {
////			nooos.setVisibility(View.VISIBLE);
////			nooos.setOnClickListener(new OnClickListener() {
////
////				@Override
////				public void onClick(View v) {
////					// TODO Auto-generated method stub
////					TyuTools
////							.showUserVerify(getActivity(), "", null);
////				}
////			});
////		}
//		show_note.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				show_note.setVisibility(View.INVISIBLE);
//				refreshDataAndUi();
//				// finish();
//				// startActivity(new Intent(getActivity(),
//				// S2ndShinningStoreActivity.class));
//			}
//
//		});
//		friends_list = (ListView) findViewById(R.id.friends_list);
//		adpter = new S2ndFriendAdpter();
//		friends_list.setAdapter(adpter);
//
//		initTitleBar();
//		refreshDataAndUi();
//	}
//
//	private Activity getActivity() {
//		// TODO Auto-generated method stub
//		return this;
//	}
//
//	void initTitleBar() {
//		View title = findViewById(R.id.title_bar);
//		ImageView back = (ImageView) title.findViewById(R.id.back);
//
//		ImageView more = (ImageView) title.findViewById(R.id.more);
//		more.setImageResource(R.drawable.s2nd_invite);
//		more.setVisibility(View.VISIBLE);
//		more.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				inviteFriend(getActivity());
//			}
//		});
//		TextView title_txt = (TextView) title.findViewById(R.id.txt);
//
//		title_txt.setText("我的铃友");
//		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				finish();
//				overridePendingTransition(R.anim.push_right_in,
//						R.anim.push_right_out);
//			}
//		});
//
//	}
//
//	static public void inviteFriend(Activity aCon) {
//		// String url = "http://shiningmovie.com/shiningApp/shining.apk";
//
//		String tmp = "%s正在使用“闪铃”－好玩的视频铃声！是视频版的彩铃唷，" +
//				"免费免流量，碉堡了，愉快的玩耍起来吧！http://www.shiningmovie.com";
//		String prefix = "您的好盆友  ";
////		if (!TextUtils.isEmpty(TyuUserInfo.getInstance().name)) {
////			prefix += TyuUserInfo.getInstance().name + " ";
////		}
//		String info = String.format(tmp, prefix);
//		Intent intent = new Intent(Intent.ACTION_SEND);
//		intent.setType("text/plain");
//		intent.putExtra(Intent.EXTRA_SUBJECT, "邀请");
//		intent.putExtra(Intent.EXTRA_TEXT, info);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		aCon.startActivity(Intent.createChooser(intent, aCon.getTitle()));
//	}
//
//	void refreshDataAndUi() {
//		// 获取数据
//		final Dialog dlg = TyuWaitingBar.build(this);
//		dlg.show();
//		new Thread() {
//			@Override
//			public void run() {
//				getData();
//				TyuContextKeeper.doUiTask(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						adpter.notifyDataSetChanged();
//
//						if (shinning == null || shinning.size() == 0) {
//							show_note.setVisibility(View.VISIBLE);
//						}
//						if (dlg != null && dlg.isShowing())
//							dlg.dismiss();
//					}
//				});
//			};
//		}.start();
//	}
//
//	void getData() {
//
//		TyuShinningData.getInstance().getFriendsInfo(local, shinning);
//		HashMap<String, ContactFriendItemData> map = new HashMap<String, TyuShinningData.ContactFriendItemData>();
//		for (ContactFriendItemData localData : local) {
//			map.put(localData.number, localData);//根据电话号码 去重复
//		}
//		for (int i = 0; i < shinning.size(); i++) {
//			shinning.get(i).local_info = map.get(shinning.get(i).number);
//			map.remove(shinning.get(i).number);
//		}
//		local.clear();
//		local.addAll(map.values());
//
//		// 添加闪铃好友
//
//	}
//
//	class S2ndFriendAdpter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return shinning.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			if (convertView == null) {
//				convertView = getLayoutInflater().inflate(
//						R.layout.s2nd_shinning_friends_item, null);
//			}
//			final ShinningFriendItemData itemdata = shinning.get(position);
//
//			ImageView hot_dot = (ImageView) convertView
//					.findViewById(R.id.imageView2);
//			ImageView userIcon = (ImageView) convertView
//					.findViewById(R.id.imageView1);
//
//			if (TyuShinningFriendIndex.getInstance().isInHistory(itemdata)) {
//				hot_dot.setVisibility(View.INVISIBLE);
//			} else if (TyuShinningFriendIndex.getInstance().isEmptyShinning(
//					itemdata)) {
//				hot_dot.setVisibility(View.INVISIBLE);
//			} else {
//				hot_dot.setVisibility(View.VISIBLE);
//			}
//			// 设置头像
//			if (!TextUtils.isEmpty(itemdata.user_image)) {
//				imageLoader.displayImage(itemdata.user_image, userIcon,
//						circle_options);
//			} else {
//				userIcon.setImageResource(R.drawable.s2nd_shinning_default_user_icon);
//			}
//			TextView name = (TextView) convertView.findViewById(R.id.fr_name);
//			if (itemdata.local_info != null && itemdata.local_info.name != null)
//				name.setText(itemdata.local_info.name);
//			else
//				name.setText(itemdata.name);
//			convertView.setBackgroundResource(R.drawable.s2nd_clicked_gray_btn);
//			convertView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					S2ndShinningFriendDetailActivity.dataCache = itemdata;
//					S2ndShinningFriendDetailActivity.showMyShinning = false;
//					// 添加历史记录
//					TyuShinningFriendIndex.getInstance().addHistory(itemdata);
//				
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					launchActivity(S2ndShinningFriendDetailActivity.class);
//					adpter.notifyDataSetChanged();
//				}
//			});
//			return convertView;
//		}
//
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

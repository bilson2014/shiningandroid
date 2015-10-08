package com.panfeng.shining.activity.s2nd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tyu.common.utils.TyuContextKeeper;
import tyu.common.utils.TyuPreferenceManager;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.TyuApp.TyuImageLoadingListener;
import com.panfeng.shining.TyuVideoPlayTask2nd;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.data.TyuShinningData.VideoItemData;
import com.panfeng.shining.widgets.HorizontalListView;
import com.panfeng.shining.widgets.TyuWaitingBar;
import com.panfeng.shinning.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class S2ndShinningStoreActivity_Bk extends Activity implements
		OnClickListener, IXListViewListener, OnScrollListener {
	private HorizontalListView keyList;

	private View netErrorNote;
	private View settingErrorNote;

	// 视频list数据
	private int page = 0;
	private int page_size = 5;
	private XListView mediaList;
	private MediaItemAdpter mediaListAdpter;
	ArrayList<View> itemViewList = new ArrayList<View>();
	// 关键字基础数据
	String[] keys = { "每日更新", "无节操", "搞笑", "萌萌哒", "治愈", "二次元", "神曲", "怀旧",
			"大陆", "港台", "日韩", "欧美", };
	private int keySelectedPosition = 0;
	List<String> keywordArray = Arrays.asList(keys);

	private View videoPlugin;
	// private VideoView videoView;
	private ViewGroup mainContentView;
	private TyuVideoPlayTask2nd videoPlayTask;
	boolean wifi_auto = TyuPreferenceManager.isChecked("wifi_auto_play");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mainContentView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.s2nd_shinning_store_layout, null);
		setContentView(mainContentView);
		initTitleBar();
		initUI();

		onRefresh();
	}

	void initUI() {
		// 获取UI组件
		keyList = (HorizontalListView) findViewById(R.id.shinning_keywords_list);
		keySelectedPosition = 0;
		// 处理关键字
		setupKeys(keywordArray);
		// 处理2个提示，设定失败提示和网络连接失败提示
		netErrorNote = findViewById(R.id.net_error_note);
		netErrorNote.setVisibility(View.GONE);
		netErrorNote.setOnClickListener(this);
		settingErrorNote = findViewById(R.id.setting_error_warning);
		settingErrorNote.findViewById(R.id.close_setting_note)
				.setOnClickListener(this);
		settingErrorNote.setVisibility(View.GONE);
		TextView tryAgain = (TextView) settingErrorNote
				.findViewById(R.id.try_again);
		tryAgain.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tryAgain.setOnClickListener(this);

		// 处理list内容
		mediaList = (XListView) this.findViewById(R.id.video_list);
		mediaList.setXListViewListener(this);

		mediaList.setPullRefreshEnable(true);
		mediaList.setPullLoadEnable(true);
		mediaListAdpter = new MediaItemAdpter();
		mediaList.setAdapter(mediaListAdpter);
		mediaList.setOnScrollListener(this);
		// videoview Plugin 初始化
		videoPlugin = getLayoutInflater().inflate(R.layout.s2nd_video_player,
				null);

		videoPlugin.setVisibility(View.GONE);
		mainContentView.addView(videoPlugin);
		// videoView = (VideoView) videoPlugin.findViewById(R.id.video);

		videoPlayTask = new TyuVideoPlayTask2nd(videoPlugin);
	}

	void installVideoPlugin(View aMediaItem, VideoItemData aData,
			boolean startNow) {
		ViewGroup parent = (ViewGroup) videoPlugin.getParent();
		if (parent != null)
			parent.removeView(videoPlugin);

		ViewGroup frame = (ViewGroup) aMediaItem.findViewById(R.id.part1);
		frame.addView(videoPlugin);
		videoPlayTask.stop();
		videoPlayTask.setupData(aData);
		// videoView.stopPlayback();
		// videoView.setVideoPath(path);

		if (startNow) {
			videoPlugin.setVisibility(View.VISIBLE);
			videoPlugin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					uninstallVideoPlugin();
				}
			});
			// videoView.start();
			videoPlayTask.playvideo();
		} else {
			videoPlugin.setVisibility(View.GONE);
		}
	}
	
	void uninstallVideoPlugin() {
		// videoView.stopPlayback();
		videoPlayTask.stop();
		mediaListAdpter.playPostion = -1;

		videoPlugin.setVisibility(View.GONE);
		ViewGroup parent = (ViewGroup) videoPlugin.getParent();
		if (parent != null) {
			parent.removeView(videoPlugin);

		}
		mediaListAdpter.notifyDataSetChanged();

	}

	void checkMediaData(List<VideoItemData> aData) {
		netErrorNote.setVisibility(View.GONE);
		mediaList.setVisibility(View.VISIBLE);
		if (aData == null || aData.size() == 0) {
			if (mediaListAdpter.getCount() == 0) {
				mediaList.setVisibility(View.INVISIBLE);
				netErrorNote.setVisibility(View.VISIBLE);

			} else {
				// 这里应该页面编号减一，但是暂时不处理
			}
		} else {

		}
	}

	void getKeys() {

	}

	void getMediaData() {
		final Dialog dlg = TyuWaitingBar.build(this);
		dlg.show();
		new Thread() {
			@Override
			public void run() {

				// 获取数据
				final List<VideoItemData> data = TyuShinningData.getInstance()
						.getMediaS2nd(page, page_size, keySelectedPosition,
								keywordArray);

				TyuContextKeeper.doUiTask(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						checkMediaData(data);
						mediaListAdpter.addData(data);
						onLoadEnd();
						if (dlg != null && dlg.isShowing())
							dlg.dismiss();
					}
				});
			};
		}.start();
	}

	void setupKeys(final List<String> aKeys) {
		if (aKeys == null) {
			return;
		}
		// TextView contKey = (TextView) findViewById(R.id.const_key);

		keyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// TextView txt = (TextView) view.findViewById(R.id.txt);
				// txt.setTextColor(getResources().getColor(
				// R.color.s2nd_app_main_color));
				// if(keySelectedPosition!=)
				keySelectedPosition = position;
				((BaseAdapter) keyList.getAdapter()).notifyDataSetChanged();

				onRefresh();
			}

		});
		keyList.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null) {
					convertView = getLayoutInflater().inflate(
							R.layout.s2nd_keyword_item, null);
				}
				TextView txt = (TextView) convertView.findViewById(R.id.txt);
				txt.setText(aKeys.get(position));
				if (keySelectedPosition == position) {
//					TextPaint tp = txt.getPaint(); 
//					tp.setFakeBoldText(true); 
//					
//					txt.setTextSize(14);
					
					txt.setTextColor(getResources().getColor(
							R.color.s2nd_app_main_color));
				} else {
//					TextPaint tp = txt.getPaint(); 
//					tp.setFakeBoldText(true); 
//					txt.setTextSize(14);
					txt.setTextColor(getResources().getColor(
							R.color.s2nd_text_color_lv1));
				}
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return aKeys.size();
			}
		});
	}

	void initTitleBar() {
		View title = findViewById(R.id.title_bar);
		ImageView back = (ImageView) title.findViewById(R.id.back);
		TextView title_txt = (TextView) title.findViewById(R.id.txt);

		title_txt.setText("闪铃库");
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.try_again:

			break;
		case R.id.close_setting_note:

			break;
		case R.id.net_error_note:
			netErrorNote.setVisibility(View.GONE);
			onRefresh();
			break;
		default:
			break;
		}
	}

	boolean onLoad = false;

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		page = 0;
		// uninstallVideoPlugin();
		onLoad = true;
		mediaListAdpter.resetData();
		getMediaData();
	}

	private void onLoadEnd() {
		// TODO Auto-generated method stub
		mediaList.stopRefresh();
		mediaList.stopLoadMore();
		mediaList.setRefreshTime("刚刚");
		onLoad = false;
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		page++;
		onLoad = true;
		// uninstallVideoPlugin();
		getMediaData();
	}

	class MediaItemAdpter extends BaseAdapter {
		ArrayList<VideoItemData> data = new ArrayList<TyuShinningData.VideoItemData>();
		final int TOP = 0;
		final int CENTER = 1;
		final int BOTTOM = 2;
		public int playPostion = -1;
		final String[] RANK_NAME = { "第一名", "第二名", "第三名", "第四名", "第五名", "第六名",
				"第七名", "第八名", "第九名", "第十名", };
		private DisplayImageOptions options;
		private TyuImageLoadingListener simpleImageListener;
		private ImageLoader imageLoader;

		public MediaItemAdpter() {
			options = TyuApp.getCommonConfig();
			simpleImageListener = new TyuImageLoadingListener();
			imageLoader = ImageLoader.getInstance();
		}

		public void setData(List<VideoItemData> aData) {
			data.clear();
			data.addAll(aData);
			notifyDataSetChanged();
		}

		public void resetData() {
			data.clear();
			notifyDataSetChanged();
		}

		public void addData(List<VideoItemData> aData) {
			data.addAll(aData);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.s2nd_video_item, null);
				View frame = convertView.findViewById(R.id.frame);
				LayoutParams lp = frame.getLayoutParams();
				if (lp != null) {
					lp.height = getResources().getDisplayMetrics().widthPixels;
					frame.setLayoutParams(lp);
				}
				itemViewList.add(convertView);
			}
			convertView.setTag(position);
			final View[] part2_group = new View[] {
					convertView.findViewById(R.id.part2_top),
					convertView.findViewById(R.id.part2_center),
					convertView.findViewById(R.id.part2_bottom) };
			final VideoItemData vData = data.get(position);
			final View contentView = convertView;
			final int index = position;
			// 处理交互逻辑
			// 名次
			TextView redTxt = (TextView) part2_group[TOP]
					.findViewById(R.id.red_txt_info);
			if (index < RANK_NAME.length) {
				redTxt.setText(RANK_NAME[index]);
			} else {
				redTxt.setText("");
			}
			// 分享按钮
			View share_btn = part2_group[TOP].findViewById(R.id.share_btn);
			share_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					S2ndShareActivity.dataCache = vData;
					launchActivity(S2ndShareActivity.class);
				}
			});

			// 视频图片
			ImageView video_image = (ImageView) convertView
					.findViewById(R.id.img);
			video_image.setVisibility(View.VISIBLE);
			imageLoader.displayImage(vData.image_url, video_image, options);
			// 视频信息
			TextView media_name = (TextView) part2_group[BOTTOM]
					.findViewById(R.id.media_name);
			media_name.setText(vData.name);
			TextView author_name = (TextView) part2_group[BOTTOM]
					.findViewById(R.id.author_name);
			author_name.setText(TyuShinningData
					.getFakeAuthorName(vData.video_url));
			// 播放按钮

			View playButton = part2_group[CENTER].findViewById(R.id.play_btn);
			ImageView shinning_setting = (ImageView) convertView
					.findViewById(R.id.shinning_setting);
			if (playPostion != position) {
				// videoPlugin.setVisibility(View.go)
				showVideoItemParts(part2_group);
				shinning_setting
						.setImageResource(R.drawable.s2nd_shinning_setting_1_btn);
			} else {
				hideVideoItemParts(part2_group);
				shinning_setting
						.setImageResource(R.drawable.s2nd_shinning_setting_2_btn);

			}
			shinning_setting.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					trySetShinning(contentView, vData);
				}
			});
			playButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					playPostion = index;
					Log.v("onplay", index + "");
					hideVideoItemParts(part2_group);
					installVideoPlugin(contentView, vData, true);
					notifyDataSetChanged();
				}
			});
			return convertView;
		}

		public void hideVideoItemParts(View[] parts) {
			for (int i = 0; i < parts.length; i++) {
				if (i == BOTTOM) {
					parts[i].findViewById(R.id.part2_bottom_sub).setVisibility(
							View.GONE);
				} else {
					parts[i].setVisibility(View.INVISIBLE);
				}
			}
		}

		public void trySetShinning(View contentView, VideoItemData aData) {
			S2ndShinningSettingActivity.data_cache = aData;
			launchActivity(S2ndShinningSettingActivity.class);
		}

		public void showVideoItemParts(View[] parts) {
			for (int i = 0; i < parts.length; i++) {
				if (i == BOTTOM) {
					parts[i].findViewById(R.id.part2_bottom_sub).setVisibility(
							View.VISIBLE);
				} else {
					parts[i].setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		Log.v("onScrollStateChanged", scrollState + "");
	}

	long ts = 0;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

		// if (mediaListAdpter.playPostion >= firstVisibleItem
		// && mediaListAdpter.playPostion <= (firstVisibleItem +
		// visibleItemCount)) {
		//
		// } else {
		// uninstallVideoPlugin();
		//
		// }
		// 用位置进行判断
		if (onLoad)
			return;
		if(launching)
			return;
		if (wifi_auto) {
			WifiAutoPlay();
		} else {
			if (videoPlugin.getParent() != null
					&& mediaListAdpter.playPostion >= 0) {

				int[] location = new int[2];
				mediaList.getLocationInWindow(location);

				int[] location2 = new int[2];
				videoPlugin.getLocationInWindow(location2);

				int videoTop = location2[1];
				int videoUpLine = (int) (videoTop + videoPlugin.getHeight() * 0.1f);
				int videoDownLine = (int) (videoTop + videoPlugin.getHeight() * 0.9f);
				int videoBottom = location2[1] + videoPlugin.getHeight();

				int listTop = location[1];
				int listBottom = location[1] + mediaList.getHeight();
				// if (videoBottom <= listTop ||
				// videoTop >= listBottom) {
				if (videoDownLine <= listTop || videoUpLine >= listBottom) {
					Log.v("uninstallVideoPlugin", "1");

					uninstallVideoPlugin();

					Log.v("uninstallVideoPlugin", "2");
				}
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		wifi_auto = TyuPreferenceManager.isChecked("wifi_auto_play");
		launching = false;
		mediaListAdpter.notifyDataSetChanged();
	}

	void WifiAutoPlay() {
		// if(ts==0){
		// ts=System.currentTimeMillis();
		// }
		// if((ts-System.currentTimeMillis())<100){
		// return;
		// }else{
		// ts = System.currentTimeMillis();
		// }
		int[] location = new int[2];
		mediaList.getLocationInWindow(location);

		int listTop = location[1];
		int listBottom = location[1] + mediaList.getHeight();
		int listCenter = location[1] + mediaList.getHeight() / 2;
		View tarView = null;
		for (int i = 0; i < itemViewList.size(); i++) {
			View item = itemViewList.get(i);
			int[] location2 = new int[2];
			item.getLocationInWindow(location2);

			int itemTop = location2[1];
			int itemUpLine = (int) (itemTop + item.getHeight() * 0.05f);
			int itemDownLine = (int) (itemTop + item.getHeight() * 0.95f);
			int itemBottom = location2[1] + item.getHeight();
			if (itemUpLine >= listTop && itemDownLine <= listBottom) {
				final int tag = (Integer) item.getTag();
				if (mediaListAdpter.playPostion != tag) {
					tarView = item;
					break;
				}
			}
		}
		if (tarView != null) {
			final int tag = (Integer) tarView.getTag();
			if (mediaListAdpter.playPostion != tag) {
				Log.v("WifiAutoPlay", "1");
				// uninstallVideoPlugin();
				Log.v("WifiAutoPlay", "2");
				installVideoPlugin(tarView, mediaListAdpter.data.get(tag), true);
				Log.v("WifiAutoPlay", "3");
				mediaListAdpter.playPostion = tag;
				Log.v("WifiAutoPlay", "4");
				mediaListAdpter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// uninstallVideoPlugin();
		videoPlayTask.stop();
	}
	boolean launching = false;
	void launchActivity(Class aClass) {
		launching = true;
		uninstallVideoPlugin();
		startActivity(new Intent(getAvtivity(), aClass));
		overridePendingTransition(R.anim.push_left_in, 0);
	}

	private Activity getAvtivity() {
		// TODO Auto-generated method stub
		return this;
	}
}

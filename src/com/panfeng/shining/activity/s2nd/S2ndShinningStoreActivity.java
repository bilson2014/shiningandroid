package com.panfeng.shining.activity.s2nd;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import tyu.common.net.TyuHttpClientUtils;
import tyu.common.utils.TyuCommon;
import tyu.common.utils.TyuContextKeeper;
import tyu.common.utils.TyuPreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.TyuApp.TyuImageLoadingListener;
import com.panfeng.shining.TyuVideoPlayTask2nd;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.data.TyuShinningData.VideoItemData;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.widgets.HorizontalListView;
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;

public class S2ndShinningStoreActivity extends Activity implements
		OnClickListener, IXListViewListener {
	private HorizontalListView keyList;

	private View netErrorNote;
	private View settingErrorNote;
	Context ctx = S2ndShinningStoreActivity.this;

	// 视频list数据
	private int page = 0;
	private int page_size = 5;
	private View more;
	List<VideoItemData> data;
	private XListView mediaList;
	private MediaItemAdpter mediaListAdpter;
	private RelativeLayout funny, l8090, gold, after00, crazy, lovely, unlimit,
			daydayup, movie, miku;
	DBAdapter db = new DBAdapter(S2ndShinningStoreActivity.this);
	ArrayList<View> itemViewList = new ArrayList<View>();
	// 关键字基础数据
	// String[] keys = { "每日更新", "无节操", "搞笑", "萌萌哒", "治愈", "二次元", "神曲", "怀旧",
	// "大陆", "港台", "日韩", "欧美" };
	String[] key = { "每日鲜", "排行榜", "精选集" };
	private int keySelectedPosition = 1;
	List<String> keywordArray = Arrays.asList(key);
	// List<String> keywordArray1 = Arrays.asList(key);
	private int playvideo = 0;

	// private View videoPlugin;
	// private VideoView videoView;
	private ViewGroup mainContentView;
	// private TyuVideoPlayTask2nd videoPlayTask;
	boolean wifi_auto = TyuPreferenceManager.isChecked("wifi_auto_play");
	myhandler myh = new myhandler();

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

		more = findViewById(R.id.store_more);
		// 获取UI组件
		keyList = (HorizontalListView) findViewById(R.id.shinning_keywords_list);

		if (TyuPreferenceManager.isFirstOpen("keySelection")) {
			keySelectedPosition = 1;
		} else {
			keySelectedPosition = 0;

		}

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

		// 精选集
		funny = (RelativeLayout) more.findViewById(R.id.refunny);
		funny.setOnClickListener(this);

//		l8090 = (RelativeLayout) more.findViewById(R.id.resl8090);
//		l8090.setOnClickListener(this);
//
//		gold = (RelativeLayout) more.findViewById(R.id.resgold);
//		gold.setOnClickListener(this);
//
//		after00 = (RelativeLayout) more.findViewById(R.id.resafter00);
//		after00.setOnClickListener(this);
//
//		crazy = (RelativeLayout) more.findViewById(R.id.recrazy);
//		crazy.setOnClickListener(this);
//
//		lovely = (RelativeLayout) more.findViewById(R.id.relovely);
//		lovely.setOnClickListener(this);
//
//		daydayup = (RelativeLayout) more.findViewById(R.id.resdaydayup);
//		daydayup.setOnClickListener(this);
//
//		unlimit = (RelativeLayout) more.findViewById(R.id.reunlimitts);
//		unlimit.setOnClickListener(this);
//
//		movie = (RelativeLayout) more.findViewById(R.id.resmovie);
//		movie.setOnClickListener(this);
//
//		miku = (RelativeLayout) more.findViewById(R.id.resmiku);
		miku.setOnClickListener(this);

		// 处理list内容
		mediaList = (XListView) this.findViewById(R.id.video_list);
		mediaList.setXListViewListener(this);

		mediaList.setPullRefreshEnable(true);
		mediaList.setPullLoadEnable(true);
		mediaListAdpter = new MediaItemAdpter();
		mediaList.setAdapter(mediaListAdpter);
		mediaList.setOnScrollListener(mediaListAdpter);
		// videoview Plugin 初始化
		// videoPlugin = getLayoutInflater().inflate(R.layout.s2nd_video_player,
		// null);

		// videoPlugin.setVisibility(View.GONE);
		// mainContentView.addView(videoPlugin);

		// videoPlayTask = new TyuVideoPlayTask2nd(videoPlugin);
	}

	/*
	 * void installVideoPlugin(View aMediaItem, VideoItemData aData, boolean
	 * startNow) { ViewGroup parent = (ViewGroup) videoPlugin.getParent(); if
	 * (parent != null) parent.removeView(videoPlugin);
	 * 
	 * ViewGroup frame = (ViewGroup) aMediaItem.findViewById(R.id.part1);
	 * frame.addView(videoPlugin); videoPlayTask.stop();
	 * videoPlayTask.setupData(aData); // videoView.stopPlayback(); //
	 * videoView.setVideoPath(path);
	 * 
	 * if (startNow) { videoPlugin.setVisibility(View.VISIBLE);
	 * videoPlugin.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub uninstallVideoPlugin(); } }); // videoView.start();
	 * videoPlayTask.playvideo(); } else { videoPlugin.setVisibility(View.GONE);
	 * } }
	 * 
	 * void uninstallVideoPlugin() { // videoView.stopPlayback();
	 * videoPlayTask.stop(); mediaListAdpter.playPostion = -1;
	 * 
	 * videoPlugin.setVisibility(View.GONE); ViewGroup parent = (ViewGroup)
	 * videoPlugin.getParent(); if (parent != null) {
	 * parent.removeView(videoPlugin);
	 * 
	 * } mediaListAdpter.notifyDataSetChanged();
	 * 
	 * }
	 */

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

	void getMediaData() {
		// final Dialog dlg = TyuWaitingBar.build(this);
		// dlg.show();
		new Thread() {
			@Override
			public void run() {

				Message ms = new Message();

				if (keySelectedPosition == 0) {
					ms.what = HIDE_SORT;
					data = TyuShinningData.getInstance().getMediaByPage(page,
							page_size, 0, false);
					
					
					Log.i("store", "return="+data);
					
					

				} else if (keySelectedPosition == 1) {
					ms.what = HIDE_SORT;
					data = TyuShinningData.getInstance().getMediaByPage(page,
							page_size, 0, true);

				} else if (keySelectedPosition == 2) {
					ms.what = SHOW_SORT;
					mHandler.sendMessage(ms);
					return;
				}
				mHandler.sendMessage(ms);

				// 获取数据

				TyuContextKeeper.doUiTask(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						checkMediaData(data);
						mediaListAdpter.addData(data);
						onLoadEnd();
						// if (dlg != null && dlg.isShowing())
						// dlg.dismiss();
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

				keySelectedPosition = position;
				((BaseAdapter) keyList.getAdapter()).notifyDataSetChanged();

				if (keySelectedPosition == 0) {

					Properties prop = new Properties();
					prop.setProperty("name", "每日鲜");
					StatService.trackCustomKVEvent(ctx, "videotag", prop);

				}

				else if (keySelectedPosition == 1) {

					Properties prop = new Properties();
					prop.setProperty("name", "排行版");
					StatService.trackCustomKVEvent(ctx, "videotag", prop);

				}

				onRefresh();



			}

		});
		keyList.setAdapter(new BaseAdapter() {

			@SuppressLint("NewApi")
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
					txt.setTextColor(getResources().getColor(
							R.color.color_white));
					txt.setBackgroundResource(R.drawable.wordb);

				} else {
					txt.setTextColor(getResources().getColor(
							R.color.s2nd_text_color_lv1));
					txt.setBackground(null);
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
		back.setVisibility(View.INVISIBLE);
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
		case R.id.refunny:

			Intent intent = new Intent();
			intent.putExtra("pos", 0);
			intent.putExtra("postext", "搞笑班");
			intent.setClass(ctx, S2ndMediaListActivity.class);
			startActivity(intent);

			// TODO:MTA
			Properties prop = new Properties();
			prop.setProperty("name", "搞笑班");
			StatService.trackCustomKVEvent(ctx, "videotag", prop);

			break;

//		case R.id.resl8090:
//
//			Intent intent1 = new Intent();
//			intent1.putExtra("pos", 1);
//			intent1.putExtra("postext", "80,90的青春");
//			intent1.setClass(ctx, S2ndMediaListActivity.class);
//			startActivity(intent1);
//
//			Properties prop1 = new Properties();
//			prop1.setProperty("name", "80,90的青春");
//			StatService.trackCustomKVEvent(ctx, "videotag", prop1);
//
//			break;
//
//		case R.id.resgold:
//
//			Intent intent2 = new Intent();
//			intent2.putExtra("pos", 2);
//			intent2.putExtra("postext", "怀旧金曲");
//			intent2.setClass(ctx, S2ndMediaListActivity.class);
//			startActivity(intent2);
//
//			Properties prop2 = new Properties();
//			prop2.setProperty("name", "怀旧金曲");
//			StatService.trackCustomKVEvent(ctx, "videotag", prop2);
//
//			break;
//
//		case R.id.resafter00:
//
//			Intent intent3 = new Intent();
//			intent3.putExtra("pos", 3);
//			intent3.putExtra("postext", "蛋蛋00后");
//			intent3.setClass(ctx, S2ndMediaListActivity.class);
//			startActivity(intent3);
//
//			Properties prop3 = new Properties();
//			prop3.setProperty("name", "蛋蛋00后");
//			StatService.trackCustomKVEvent(ctx, "videotag", prop3);
//
//			break;
//
//		case R.id.recrazy:
//
//			Intent intent4 = new Intent();
//			intent4.putExtra("pos", 4);
//			intent4.putExtra("postext", "神曲疯了");
//			intent4.setClass(ctx, S2ndMediaListActivity.class);
//			startActivity(intent4);
//
//			Properties prop4 = new Properties();
//			prop4.setProperty("name", "神曲疯了");
//			StatService.trackCustomKVEvent(ctx, "videotag", prop4);
//
//			break;
//
//		case R.id.relovely:
//
//			Intent intent5 = new Intent();
//			intent5.putExtra("pos", 5);
//			intent5.putExtra("postext", "萌萌哒集中营");
//			intent5.setClass(ctx, S2ndMediaListActivity.class);
//			startActivity(intent5);
//
//			Properties prop5 = new Properties();
//			prop5.setProperty("name", "萌萌哒集中营");
//			StatService.trackCustomKVEvent(ctx, "videotag", prop5);
//
//			break;
//
//		case R.id.reunlimitts:
//
//			Intent intent6 = new Intent();
//			intent6.putExtra("pos", 6);
//			intent6.putExtra("postext", "节操无下限");
//			intent6.setClass(ctx, S2ndMediaListActivity.class);
//			startActivity(intent6);
//
//			Properties prop6 = new Properties();
//			prop6.setProperty("name", "节操无下限");
//			StatService.trackCustomKVEvent(ctx, "videotag", prop6);
//
//			break;
//
//		case R.id.resdaydayup:
//
//			Intent intent7 = new Intent();
//			intent7.putExtra("pos", 7);
//			intent7.putExtra("postext", "天天涨姿势");
//			intent7.setClass(ctx, S2ndMediaListActivity.class);
//			startActivity(intent7);
//
//			Properties prop7 = new Properties();
//			prop7.setProperty("name", "天天涨姿势");
//			StatService.trackCustomKVEvent(ctx, "videotag", prop7);
//
//			break;
//
//		case R.id.resmovie:
//
//			Intent intent8 = new Intent();
//			intent8.putExtra("pos", 8);
//			intent8.putExtra("postext", "电影咔咔剪");
//			intent8.setClass(ctx, S2ndMediaListActivity.class);
//			startActivity(intent8);
//
//			Properties prop8 = new Properties();
//			prop8.setProperty("name", "电影咔咔剪");
//			StatService.trackCustomKVEvent(ctx, "videotag", prop8);
//
//			break;
//		case R.id.resmiku:
//
//			Intent intent9 = new Intent();
//			intent9.putExtra("pos", 9);
//			intent9.putExtra("postext", "二次元研究院");
//			intent9.setClass(ctx, S2ndMediaListActivity.class);
//			startActivity(intent9);
//
//			Properties prop9 = new Properties();
//			prop9.setProperty("name", "二次元研究院");
//			StatService.trackCustomKVEvent(ctx, "videotag", prop9);
//
//			break;

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
		mediaListAdpter.stopPlay(mediaListAdpter.playPostion);
		mediaListAdpter.resetData();
		// mediaListAdpter.notifyDataSetChanged();
		getMediaData();

		// TODO: MTA video_list
		StatService.trackCustomEvent(ctx, "video_list", "");
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
		mediaListAdpter.stopPlay(mediaListAdpter.playPostion);
		// uninstallVideoPlugin();
		getMediaData();
	}

	class MediaItemAdpter extends BaseAdapter implements OnScrollListener {
		ArrayList<VideoItemData> data = new ArrayList<TyuShinningData.VideoItemData>();
		final int TOP = 0;
		final int CENTER = 1;
		final int BOTTOM = 2;
		public int playPostion = -1;
		public int pressed2Stop = -1;
		final String[] RANK_NAME = { "第1名", "第2名", "第3名", "第4名", "第5名", "第6名",
				"第7名", "第8名", "第9名", "第10名", };
		private DisplayImageOptions options;
		private TyuImageLoadingListener simpleImageListener;
		private ImageLoader imageLoader;

		SparseArray<TyuVideoPlayTask2nd> videoTaskList = new SparseArray<TyuVideoPlayTask2nd>();

		public MediaItemAdpter() {
			options = TyuApp.getCommonConfig();
			simpleImageListener = new TyuImageLoadingListener();
			imageLoader = ImageLoader.getInstance();
		}

		public void setData(List<VideoItemData> aData) {
			data.clear();
			// videoTaskList.clear();
			data.addAll(aData);
			notifyDataSetChanged();
		}

		public void resetData() {
			data.clear();
			videoTaskList.clear();
			itemViewList.clear();
			playPostion = -1;
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
					lp.height = getResources().getDisplayMetrics().heightPixels;
					frame.setLayoutParams(lp);
				}

			} else {
				// 已经现有的，要判断一下它对应的任务是否运行中,它是否被复用来处理新页面
				// int old_index= (Integer) convertView.getTag();
				// TyuVideoPlayTask2nd old_task = videoTaskList.get(old_index);
				// if(old_task!=null&&old_index!=position){
				// old_task.stop();
				// }
			}
			if (!itemViewList.contains(convertView)) {
				Log.i("add", "add itemViewList");
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

			View videoPlugin = convertView.findViewById(R.id.video_plugin);
			TyuVideoPlayTask2nd videoTask = videoTaskList.get(index);
			// 如果没有改任务，则创建
			if (videoTask == null) {
				videoTask = new TyuVideoPlayTask2nd();
				videoTask.setupUI(videoPlugin);

				videoTask.setupData(data.get(index));

				videoTaskList.append(index, videoTask);

			} else {
				videoTask.setupUI(videoPlugin);
				// videoTask.init();
				videoTask.setupData(data.get(index));
			}
			// 重新设置一下view和data

			// 基础videoview事件逻辑
			videoPlugin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pressed2Stop = index;
					stopPlay(index);
					// showVideoItemParts(itemView)
					notifyDataSetChanged();

				}
			});

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
			View share_btn = part2_group[BOTTOM].findViewById(R.id.share_btn);
			share_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					S2ndShareActivity.dataCache = vData;
					launchActivity(S2ndShareActivity.class);
					overridePendingTransition(R.anim.bottom_in, 0);
				}
			});

			// 视频图片
			ImageView video_image = (ImageView) videoPlugin
					.findViewById(R.id.img);
			video_image.setVisibility(View.VISIBLE);
			video_image.setImageBitmap(null);
			imageLoader.displayImage(vData.image_url, video_image, options);
			// 视频信息
			TextView media_name = (TextView) part2_group[TOP]
					.findViewById(R.id.media_name);
			media_name.setText(vData.name);
			TextView author_name = (TextView) part2_group[TOP]
					.findViewById(R.id.author_name);
			// author_name.setText(TyuShinningData
			// .getFakeAuthorName(vData.video_url));
			if (!TextUtils.isEmpty(vData.author)) {
				author_name.setText(vData.author);
			} else
				author_name.setText("");
			// 播放按钮

			View playButton = part2_group[CENTER].findViewById(R.id.play_btn);
			ImageView shinning_setting = (ImageView) convertView
					.findViewById(R.id.shinning_setting);
			final ImageView save_video = (ImageView) convertView
					.findViewById(R.id.save_video);

			VideoItemData vid = data.get(index);
			String mbid = vid.mb_id + "";
			Log.i("slw", "sss=----->" + mbid);

			db.open();
			Cursor c = db.getAllPeople(vid.mb_id + "");
			// 已经收藏
			if (c.moveToFirst()) {
				save_video.setBackgroundResource(R.drawable.saved_video);
			} else {
				// 未收藏
				save_video.setBackgroundResource(R.drawable.save_video);
			}
			db.close();

			if (playPostion != position) {
				// videoPlugin.setVisibility(View.go)
				showVideoItemParts(convertView);
				// videoPlugin.setVisibility(View.INVISIBLE);
				shinning_setting
						.setImageResource(R.drawable.setling);
				// save_video.setImageResource(R.drawable.savev);
			} else {
				hideVideoItemParts(convertView);
				// videoPlugin.setVisibility(View.VISIBLE);
				shinning_setting
						.setImageResource(R.drawable.setling);
				// save_video.setImageResource(R.drawable.savevbg);

			}
			shinning_setting.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					trySetShinning(contentView, vData);
				}
			});

			save_video.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String pathString = Environment
							.getExternalStorageDirectory().getAbsolutePath();

					final String imageLocaPath = pathString + File.separator
							+ "com.panfeng.shinning" + File.separator
							+ "videoImage/";

					final String videoLocaPath = pathString + File.separator
							+ "com.panfeng.shinning" + File.separator
							+ "mySave/";
					int id = vData.mb_id;
					db.open();
					Cursor cursor = db.getAllPeople(id + "");

					if (cursor.moveToFirst()) {
						// 取消收藏
						db.deleteDb(cursor.getLong(0));
						save_video.setBackgroundResource(R.drawable.save_video);
						File file = new File(videoLocaPath + id + ".mp4");
						file.delete();
						db.close();

					} else {
						db.close();
						new Thread(new Runnable() {
							@Override
							public void run() {

								Message msg = new Message();
								msg.what = UPDATEIMAGE;
								msg.obj = save_video;
								myh.sendMessage(msg);
								String videoPath = vData.video_url;
								String imagePath = vData.image_url;

								int res = TyuHttpClientUtils.downLoadFile(
										videoPath, videoLocaPath + vData.mb_id
												+ ".mp4", null, null);

								res = TyuHttpClientUtils.downLoadFile(
										imagePath, imageLocaPath + vData.mb_id
												+ ".jpg", null, null);

								Log.i("slw", "imageLocaPath=" + imagePath);
								Log.i("slw", "videoLocaPath=" + videoPath);
								Log.i("slw", "imagePath=" + videoLocaPath
										+ vData.mb_id + ".jpg");
								Log.i("slw", "videoPath=" + videoLocaPath
										+ vData.mb_id + ".mp4"
										+ "  1231231231312" + res);

								if (res >= 0) {

									DBAdapter db = new DBAdapter(
											S2ndShinningStoreActivity.this);
									db.open();
									long l = db.insertDb(vData.mb_id + "", "",
											"", 0);
									Log.i("slw", vData.mb_id + "   121231   "
											+ l);
									db.close();
									 msg = new Message();
									msg.what = REVIER;
									msg.obj = save_video;
									myh.sendMessage(msg);
								}
							}
						}).start();
					}

				}
			});

			playButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Log.v("onplay", index + "");
					playvideo = 0;
					// hideVideoItemParts(getCachedItemView(index));
					startPlay(index);

					// installVideoPlugin(contentView, vData, true);
					// notifyDataSetChanged();
				}
			});
			return convertView;
		}

		public View getCachedItemView(int index) {
			for (View itemView : itemViewList) {
				int position = (Integer) itemView.getTag();

				if (position == index)
					return itemView;
			}
			return null;
		}

		public void startPlay(int index) {
			playPostion = index;
			// 先全部停止
			for (int i = 0; i < data.size(); i++) {
				TyuVideoPlayTask2nd task = videoTaskList.get(i);
				if (task != null) {
					if (i == index) {
						playPostion = index;
						task.playvideo();

						if (playvideo == 0) {
							// TODO:MTA 点击播放

							Properties prop = new Properties();
							prop.setProperty("play", "" + data.get(i).mb_id);
							StatService.trackCustomKVEvent(ctx, "play_video",
									prop);
						} else {
							// TODO:MTA 自动播放
							Properties prop = new Properties();
							prop.setProperty("wifi_play", ""
									+ data.get(i).mb_id);
							StatService.trackCustomKVEvent(ctx, "play_video",
									prop);

						}
					} else {
						if (!task.isStopped())
							task.stop();
					}

				}
			}
			// 关闭附加栏，然后其他所有条目显示附加栏
			for (View itemView : itemViewList) {
				int tag = (Integer) itemView.getTag();
				if (tag == playPostion) {
					hideVideoItemParts(itemView);
				} else {
					showVideoItemParts(itemView);
				}
			}
		}

		public void stopPlay(int index) {

			try {
				if (playPostion == index)
					playPostion = -1;
				TyuVideoPlayTask2nd task = videoTaskList.get(index);
				if (task != null) {
					task.stop();
				}

				// 显示附加栏,其他的条目不管
				for (View itemView : itemViewList) {
					int tag = (Integer) itemView.getTag();
					if (tag == playPostion) {
						showVideoItemParts(itemView);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void showVideoItemParts(View itemView) {
			final View[] parts = new View[] {
					itemView.findViewById(R.id.part2_top),
					itemView.findViewById(R.id.part2_center),
					itemView.findViewById(R.id.part2_bottom) };
			ImageView shinning_setting = (ImageView) itemView
					.findViewById(R.id.shinning_setting);
			shinning_setting
					.setImageResource(R.drawable.s2nd_shinning_setting_1_btn);
			for (int i = 0; i < parts.length; i++) {
				if (i == BOTTOM) {
					final View temp = parts[i]
							.findViewById(R.id.part2_bottom_sub);
					// Animation anim = AnimationUtils.loadAnimation(
					// getAvtivity(), R.anim.alpha_in);
					// temp.startAnimation(anim);

					temp.setVisibility(View.VISIBLE);
				} else {
					// Animation anim = AnimationUtils.loadAnimation(
					// getAvtivity(), R.anim.alpha_in);
					// parts[i].startAnimation(anim);
					parts[i].setVisibility(View.VISIBLE);
				}
			}
		}

		public void hideVideoItemParts(View itemView) {
			if (itemView == null)
				return;
			ImageView shinning_setting = (ImageView) itemView
					.findViewById(R.id.shinning_setting);
			shinning_setting
					.setImageResource(R.drawable.s2nd_shinning_setting_2_btn);
			final View[] parts = new View[] {
					itemView.findViewById(R.id.part2_top),
					itemView.findViewById(R.id.part2_center),
					itemView.findViewById(R.id.part2_bottom) };
			int index = (Integer) itemView.getTag();
			for (int i = 0; i < parts.length; i++) {
				if (i == BOTTOM) {
					final View temp = parts[i]
							.findViewById(R.id.part2_bottom_sub);
					if (index == playPostion) {
						Animation anim = AnimationUtils.loadAnimation(
								getAvtivity(), R.anim.alpha_out);
						temp.startAnimation(anim);
					}
					temp.setVisibility(View.GONE);
					// anim.setAnimationListener(new SimpleAnimationListener() {
					// @Override
					// public void onAnimationEnd(Animation animation) {
					// // TODO Auto-generated method stub
					// temp.setVisibility(View.GONE);
					// }
					// });

				} else {
					if (index == playPostion) {
						Animation anim = AnimationUtils.loadAnimation(
								getAvtivity(), R.anim.alpha_out);
						parts[i].startAnimation(anim);
					}

					parts[i].setVisibility(View.INVISIBLE);
				}
			}
		}

		public void trySetShinning(View contentView, VideoItemData aData) {
			S2ndShinningSettingActivity.data_cache = aData;
			launchActivity(S2ndShinningSettingActivity.class);
			overridePendingTransition(R.anim.bottom_in, 0);
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			Log.v("onScrollStateChanged", scrollState + "");
			// if(scrollState==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
			// ||scrollState==OnScrollListener.SCROLL_STATE_FLING){
			// pressed2Stop = -1;
			// }
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			// 用位置进行判断
			// if (onLoad)
			// return;
			// if (launching)
			// return;
			if (wifi_auto) {
				// scrollAutoClose();
				mHandler.removeMessages(START_AUTO_PLAY);
				mHandler.sendEmptyMessageDelayed(START_AUTO_PLAY, 0);
			} else {

				// scrollAutoClose();
				mHandler.removeMessages(SCROLL_AUTO_CLOSE);
				mHandler.sendEmptyMessageDelayed(SCROLL_AUTO_CLOSE, 0);
			}

		}

		final float top_line = 0.1f;
		final float bottom_line = 0.9f;

		void scrollAutoClose() {
			View item = null;
			// 查找当前播放的view的位置
			int index = -1;
			for (int i = 0; i < itemViewList.size(); i++) {
				View val = itemViewList.get(i);
				int temp = (Integer) val.getTag();
				if (temp == playPostion) {
					index = temp;
					item = val;
					break;
				}
			}
			Log.i("slt", index + "");
			// 判断位置情况,关闭task
			if (item != null) {
				int[] location = new int[2];
				mediaList.getLocationInWindow(location);

				int listTop = location[1];
				int listBottom = location[1] + mediaList.getHeight();
				// int listCenter = location[1] + mediaList.getHeight() / 2;

				int[] location2 = new int[2];
				item.getLocationInWindow(location2);

				int itemTop = location2[1];
				int itemUpLine = (int) (itemTop + item.getHeight() * top_line);
				int itemDownLine = (int) (itemTop + item.getHeight() * 0.3f
						* bottom_line);
				Log.i("slt", "0_0");
				Log.i("slt", "itemDownLine:" + itemDownLine);
				Log.i("slt", "itemUpLine:" + itemUpLine);

				Log.i("slt", "listTop:" + listTop);
				Log.i("slt", "listBottom:" + listBottom);
				if (itemDownLine < listTop || itemUpLine > listBottom * 0.3f) {
					Log.i("slt", "1");
					TyuVideoPlayTask2nd task = videoTaskList.get(index);
					if (task != null) {
						Log.i("slt", "2");
						task.stop();
					}
					showVideoItemParts(item);
				}

			}

		}

		void WifiAutoPlay() {

			playvideo = 1;

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
				if (mediaListAdpter.playPostion != tag && pressed2Stop != tag) {
					pressed2Stop = -1;
					startPlay(tag);
					mediaListAdpter.playPostion = tag;

					// mHandler.removeMessages(10);
					// Message msg = mHandler.obtainMessage();
					// msg.what = 10;
					// msg.obj = tag;
					// mHandler.sendMessageDelayed(msg, 200);
				}
			}
		}
	}

	static final int START_AUTO_PLAY = 10;
	static final int SCROLL_AUTO_CLOSE = 11;
	static final int SHOW_SORT = 0x12;
	static final int HIDE_SORT = 0x13;
	final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case START_AUTO_PLAY: {
				mediaListAdpter.WifiAutoPlay();
			}
				break;
			case SCROLL_AUTO_CLOSE: {
				mediaListAdpter.scrollAutoClose();
				break;
			}
			case SHOW_SORT: {
				mediaList.setVisibility(View.GONE);
				more.setVisibility(View.VISIBLE);
				break;
			}
			case HIDE_SORT: {
				more.setVisibility(View.GONE);
				break;
			}

			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		wifi_auto = TyuPreferenceManager.isChecked("wifi_auto_play")
				&& (TyuCommon.getNetState() == ConnectivityManager.TYPE_WIFI);

		launching = false;
		onRefresh();
		mediaListAdpter.notifyDataSetChanged();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// uninstallVideoPlugin();
		// videoPlayTask.stop();
	}

	boolean launching = false;

	void launchActivity(Class aClass) {
		launching = true;
		// uninstallVideoPlugin();
		mediaListAdpter.stopPlay(mediaListAdpter.playPostion);
		startActivity(new Intent(getAvtivity(), aClass));

	}

	private Activity getAvtivity() {
		// TODO Auto-generated method stub
		return this;
	}

	class SimpleAnimationListener implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if(secondDel())
				Toast.makeText(S2ndShinningStoreActivity.this, "再按一次退出",Toast.LENGTH_SHORT).show();
				
			
			
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
	
	
	
	

	private int UPDATEIMAGE = 0x123;
	private int REVIER = 0x1234;

	class myhandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == UPDATEIMAGE) {
				Toast.makeText(S2ndShinningStoreActivity.this, "收藏成功",
						Toast.LENGTH_SHORT).show();
				ImageView im = (ImageView) msg.obj;
				im.setBackgroundResource(R.drawable.saved_video);
				im.setEnabled(false);
			}else if(msg.what==REVIER)
			{
			
				ImageView im = (ImageView) msg.obj;
				im.setEnabled(true);
			}

		}
	}

}

package com.panfeng.shining.fragment;

import in.srain.cube.mints.base.TitleBaseFragment;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.activity.newactivity.FestailActivityV2;
import com.panfeng.shining.adapter.EveryDayListAdapter;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.drocode.swithcer.GuideGallery;
import com.panfeng.shining.slw.activity.ActivityShowVideo;
import com.panfeng.shining.slw.activity.fragment.Show_Video_Fragment;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.tools.NetTools;
import com.panfeng.shinning.R;

@SuppressLint("InflateParams")
public class EveryDayFragment extends TitleBaseFragment {

	// 数据源操作

	
	mHander everydayUpdateHandler = new mHander();
	private EveryDayListAdapter talkAdapter;
	private Context ctx;
	View mLayout;
	RelativeLayout neterror;
//	getResouceInterface grf = new everyDayImel();

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = TyuApp.getCommonConfig();

	String jsonString;
	JSONArray jsonarray;
	final String pathString = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	final String imageLocaPath = pathString + File.separator
			+ "com.panfeng.shinning" + File.separator + "videoImage/";// 图片

	// 轮播
	public HashMap<String, Bitmap> imagesCache = new HashMap<String, Bitmap>();

	public List<HashMap<String, String>> list;
	HashMap<String, String> hashmap;
	public GuideGallery images_ga;
	private Thread timeThread = null;
	public boolean timeFlag = true, first = true;
	private boolean isExit = false;
	public ImageTimerTask timeTaks = null;
	private TextView txt_gallerytitle;
	int gallerypisition = 0;
	Timer autoGallery = new Timer();

	private ImageAdapter self;
	public volatile boolean timeCondition = true;
	Uri uri;
	Intent intent;
	VideoEntity ve;

	RotateAnimation animation;

	private PtrClassicFrameLayout mPtrFrame;

	public final static int REFRESHDATA = 0x1234;
	public final static int NODATA = 0x1235;
	public final static int TITLEDATA = 0x1236;
	View headerView;
	View footView;
	GridViewWithHeaderAndFooter gridView;

	boolean isload = true;

	@Override
	public void onResume() {
		gridView.setSelection(Show_Video_Fragment.positionx - 1);
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ctx = getActivity();
		DefindConstant.EVERYDAY_NEW.clear();

		
		
		View view = inflater.inflate(R.layout.every_frament, null);
		

		
	

		gridView = (GridViewWithHeaderAndFooter) view
				.findViewById(R.id.grid_view_by_everyday);

		headerView = inflater.inflate(R.layout.everyday_new_head, null);
		footView = inflater.inflate(R.layout.everyday_new_foot, null);

		headerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		footView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		//page = 0;

//		talkAdapter = new EveryDayListAdapter(getActivity(),
//				DefindConstant.EVERYDAY_NEW);
		
		// 轮播
		initData();

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ve = (VideoEntity) view.getTag(R.id.MoreMediaAdapter);
				String videoName = ve.getVideo_fIleName();
				int videoID = Integer.parseInt(ve.getVideo_id());
				String videoUrl = TyuDefine.HOST + "media_base/"
						+ videoName.substring(0, videoName.lastIndexOf('.'))
						+ ".mp4";
				String imgUrl = TyuDefine.HOST + "media_base/"
						+ videoName.substring(0, videoName.lastIndexOf('.'))
						+ ".jpg";

				String names = videoName.substring(0,
						videoName.lastIndexOf('.'));
				Intent intent = new Intent();
				intent.putExtra("position", position);
//				intent.putExtra("page", EveryDayActivity.page);
				intent.putExtra("url", videoUrl);
				intent.putExtra("imgurl", imgUrl);
				intent.putExtra("videoID", videoID);
				intent.putExtra("videoName", names);
				intent.setClass(ctx, ActivityShowVideo.class);
				ctx.startActivity(intent);

			}
		});

		gridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {

				// 滚动时

				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					
					
					footView.setVisibility(View.VISIBLE);
					// if (gridView.getFirstVisiblePosition() > (gridView
					// .getCount() * (0.7))) {
					//
					// }
					//
					// if (gridView.getLastVisiblePosition() == (gridView
					// .getCount() - 1))
					//
					// {
					// }

					break;

				// 当不滚动时

				case OnScrollListener.SCROLL_STATE_IDLE:

					if (gridView.getFirstVisiblePosition() > (gridView
							.getCount() * (0.7))) {
						footView.setVisibility(View.VISIBLE);
						new GetDataTask().execute();

					}

					if (gridView.getLastVisiblePosition() == (gridView
							.getCount() - 1)) {
						footView.setVisibility(View.VISIBLE);
						new GetDataTask().execute();
					}

					break;
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		// mPullToRefreshListView.setItemsCanFocus(true);

		neterror = (RelativeLayout) view.findViewById(R.id.re_error);

		if (!NetTools.checkNetwork(ctx)) {
			neterror.setVisibility(View.VISIBLE);
		}

		gridView.addHeaderView(headerView);
		gridView.addFooterView(footView);
		gridView.setNumColumns(3);
		gridView.setAdapter(talkAdapter);
		mPtrFrame = (PtrClassicFrameLayout) view
				.findViewById(R.id.more_new_head_frame);
		mPtrFrame.setKeepHeaderWhenRefresh(true);
		mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {

				//page = -1;
				DefindConstant.EVERYDAY_NEW.clear();
				gridView.setAdapter(talkAdapter);
				footView.setVisibility(View.GONE);
				new GetDataTask().execute();

			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return super.checkCanDoRefresh(frame, content, header);
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {

			return null;

		}

		@Override
		protected void onPostExecute(String[] result) {

			if (isload) {
				isload = false;
				//page++;
				updateMessage();
			}

			super.onPostExecute(result);
		}
	}

	private void updateMessage() {

		new Thread(new Runnable() {
			@SuppressLint("UseSparseArrays")
			@Override
			public void run() {
				try {
//					if (DefindConstant.EVERYDAY_NEW.size() > 0) {
//						if (grf.getValues(page, null,
//								DefindConstant.EVERYDAY_NEW.get(0)
//										.getVideo_id())) {
//							everydayUpdateHandler.loadMore = true;
//							everydayUpdateHandler.sendEmptyMessage(REFRESHDATA);
//
//						} else {
//							everydayUpdateHandler.loadMore = false;
//							everydayUpdateHandler.sendEmptyMessage(NODATA);
//						}
//
//					} else {
//						if (grf.getValues(page, null, null)) {
//							everydayUpdateHandler.loadMore = true;
//							everydayUpdateHandler.sendEmptyMessage(REFRESHDATA);
//						} else {
//							everydayUpdateHandler.loadMore = false;
//							everydayUpdateHandler.sendEmptyMessage(NODATA);
//						}
//					}
				} catch (Exception e) {
					everydayUpdateHandler.loadMore = false;
					everydayUpdateHandler.sendEmptyMessage(NODATA);
				}

			}
		}).start();

	}

	public void initData() {

		new Thread(new Runnable() {
			@SuppressLint("UseSparseArrays")
			@Override
			public void run() {

				try {

					jsonString = TyuShinningData.getInstance()
							.getMediaByPage_3();
					jsonarray = new JSONArray(jsonString);

					list = new ArrayList<HashMap<String, String>>();

					Log.i("xlxlx", "jsonString" + jsonString);

					for (int i = 0; i < jsonarray.length(); i++) {

						String name = jsonarray.getJSONObject(i).getString(
								"ms_name");
						String imgLueName = jsonarray.getJSONObject(i)
								.getString("ms_lueimageurl");
						String imgChangName = jsonarray.getJSONObject(i)
								.getString("ms_changimageurl");
						String url = "http://182.92.154.162:8080/shiningCenterService/video_sort_image/"
								+ imgLueName;
						String changUrl = "http://182.92.154.162:8080/shiningCenterService/video_sort_image/"
								+ imgChangName;

						hashmap = new HashMap<String, String>();
						hashmap.put("content", name);
						hashmap.put("url", url);
						list.add(hashmap);

						String path = imageLocaPath + imgLueName;
						String changPath = imageLocaPath + imgChangName;

						File f = new File(path);
						if (!f.exists()) {
							TyuHttpClientUtils.downLoadFile(url, imageLocaPath
									+ imgLueName, null, null);
						}

						File fs = new File(changPath);
						if (!fs.exists()) {
							TyuHttpClientUtils.downLoadFile(changUrl,
									imageLocaPath + imgChangName, null, null);
						}
					}

					updateMessage();
					everydayUpdateHandler.sendEmptyMessage(TITLEDATA);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

	public void init() {
		Bitmap image = BitmapFactory.decodeResource(getResources(),
				R.drawable.ifnot);

		imagesCache.put("background_non_load", image);

		images_ga = (GuideGallery) headerView
				.findViewById(R.id.image_wall_gallery);
		txt_gallerytitle = (TextView) headerView
				.findViewById(R.id.txt_gallerytitle);
		images_ga.setActivity(getActivity(), autoGalleryHandler, timeFlag,
				timeCondition);

		self = new ImageAdapter(list);
		images_ga.setAdapter(self);
		LinearLayout pointLinear = (LinearLayout) headerView
				.findViewById(R.id.gallery_point_linear);
		pointLinear.setBackgroundColor(Color.argb(200, 135, 135, 152));

		for (int i = 0; i < list.size() - 1; i++) {
			ImageView pointView = new ImageView(ctx);
			if (i == 0) {
				pointView.setBackgroundResource(R.drawable.ic_launcher);
			} else {
				pointView.setBackgroundResource(R.drawable.ic_launcher);
			}
			pointLinear.addView(pointView);
		}

		images_ga.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				try {

					String name = jsonarray.getJSONObject(arg2 % list.size())
							.getString("ms_name");
					String path = imageLocaPath
							+ jsonarray.getJSONObject(arg2 % list.size())
									.getString("ms_lueimageurl");

					Intent tofes = new Intent();
					tofes.putExtra("festailName", name);
					tofes.putExtra("pos", 25);
					tofes.putExtra("festailPath", path);

					tofes.setClass(ctx, FestailActivityV2.class);
					startActivity(tofes);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		timeTaks = new ImageTimerTask();
		autoGallery.scheduleAtFixedRate(timeTaks, 5000, 5000);
		timeThread = new Thread() {
			public void run() {
				while (!isExit) {

					synchronized (timeTaks) {
						if (!timeFlag) {
							timeTaks.timeCondition = true;
							timeTaks.notifyAll();
						}
					}
					timeFlag = true;
				}
			};
		};
		timeThread.start();

	}

	class mHander extends Handler {

		boolean loadMore = true;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case EveryDayFragment.REFRESHDATA:
				mPtrFrame.refreshComplete();
				talkAdapter.notifyDataSetChanged();
				footView.setVisibility(View.VISIBLE);
				isload = true;
				break;
			case EveryDayFragment.NODATA:
				if (loadMore) {
					Toast.makeText(ctx, "没有更多啦", Toast.LENGTH_SHORT).show();
					mPtrFrame.refreshComplete();
					footView.setVisibility(View.GONE);
					isload = true;
				} else {
					Toast.makeText(ctx, "没有更多啦", Toast.LENGTH_SHORT).show();
				//	page--;
					mPtrFrame.refreshComplete();
					footView.setVisibility(View.GONE);
					isload = true;
				}
				break;
			case EveryDayFragment.TITLEDATA:
				init();
				break;
			}

		}

	}

	final Handler autoGalleryHandler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			switch (message.what) {
			case 0:
				self.notifyDataSetChanged();
				break;
			case 1:
				images_ga.setSelection(message.getData().getInt("pos"));
				break;
			}
		}
	};

	class ImageTimerTask extends TimerTask {

		protected boolean timeCondition;

		public void run() {

			// synchronized (this) {
			// while (!timeCondition) {
			// try {
			// Thread.sleep(100);
			// wait();
			// } catch (InterruptedException e) {
			// Thread.interrupted();
			// }
			// }
			// }
			try {

				gallerypisition = images_ga.getSelectedItemPosition() + 1;

				Message msg = new Message();
				Bundle date = new Bundle();
				date.putInt("pos", gallerypisition);
				msg.setData(date);
				msg.what = 1;
				autoGalleryHandler.sendMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class ImageAdapter extends BaseAdapter {
		private List<String> imageUrls;
		private List<HashMap<String, String>> list;

		ImageView imageView;

		public ImageAdapter(List<HashMap<String, String>> list) {
			this.list = list;
		}

		public int getCount() {
			return Integer.MAX_VALUE;
		}

		public Object getItem(int position) {
			return list.get(position % list.size());
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			Bitmap image;
			if (convertView == null) {
				convertView = LayoutInflater.from(ctx).inflate(R.layout.item,
						null);
				@SuppressWarnings("deprecation")
				Gallery.LayoutParams params = new Gallery.LayoutParams(
						Gallery.LayoutParams.MATCH_PARENT,
						Gallery.LayoutParams.WRAP_CONTENT);
				convertView.setLayoutParams(params);
				image = imagesCache.get(list.get(position % list.size()).get(
						"url"));
				if (image == null) {
					image = imagesCache.get("background_non_load");
					LoadImageTask task = new LoadImageTask(convertView);
					task.execute(list.get(position % list.size()).get("url"));
				}

				convertView.setTag(imageUrls);

			} else {
				image = (Bitmap) convertView.getTag();
			}

			imageView = (ImageView) convertView
					.findViewById(R.id.gallery_image);

			try {
				String imgName;
				imgName = jsonarray.getJSONObject(position % list.size())
						.getString("ms_changimageurl");
				String path = imageLocaPath + imgName;

				File f = new File(path);

				if (f.exists()) {

					Bitmap bm = BitmapFactory.decodeFile(path);
					imageView.setImageBitmap(bm);
				} else {

					imageView.setImageBitmap(image);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return convertView;

		}

		class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
			private View resultView;

			LoadImageTask(View resultView) {
				this.resultView = resultView;
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				resultView.setTag(bitmap);
			}

			@Override
			protected Bitmap doInBackground(String... params) {
				Bitmap image = null;
				try {
					URL url = new URL(params[0]);
					URLConnection conn = url.openConnection();
					conn.connect();
					InputStream is = conn.getInputStream();
					image = BitmapFactory.decodeStream(is);
					is.close();
	//				((EveryDayActivity) ctx).imagesCache.put(params[0], image);
					Message m = new Message();
					m.what = 0;
					autoGalleryHandler.sendMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return image;
			}
		}
	}

}

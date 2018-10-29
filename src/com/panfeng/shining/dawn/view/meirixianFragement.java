package com.panfeng.shining.dawn.view;

import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.mints.base.TitleBaseFragment;
import in.srain.cube.request.FailData;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestHandler;
import in.srain.cube.request.RequestJsonHandler;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import in.srain.cube.views.list.ListViewDataAdapter;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.panfeng.shining.dawn.net.NetUtisl;
import com.panfeng.shining.dawn.viewholder.ImagePagerAdapter;
import com.panfeng.shining.dawn.viewholder.MeiRiXianViewHolder;
import com.panfeng.shining.entity.TypeEntity;
import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shining.slw.activity.ActivityShowVideo;
import com.panfeng.shining.slw.activity.fragment.Show_Video_Fragment;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.tools.NetTools;
import com.panfeng.shinning.R;

public class meirixianFragement extends TitleBaseFragment {

	public static int pageId = 0;

	private ImageLoader mImageLoader;

	// view
	private GridViewWithHeaderAndFooter gridView;
	private View headView;
	private AutoScrollViewPager viewPager;
	private View footView;
	private View footProgressBar;
	private View footTextView;
	private View netStatus;

	// 状态记录
	private int pageSize = 12;
	private int firstVideoID = -1;
	private boolean first = false;
	private static int LUNBOTIME = 2000;// ms

	// 资源
	private List<TypeEntity> imgViews = new ArrayList<TypeEntity>();
	private ListViewDataAdapter<VideoEntityLu> listViewDataAdapter;
	private List<VideoEntityLu> videoEntityLusList;
	private PtrClassicFrameLayout mPtrFrame;
	private Context context;
	private NetUtisl netUtisl;

	@SuppressLint("InflateParams")
	View view;

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();	
		netUtisl = new NetUtisl();
		mImageLoader = ImageLoaderFactory.create(getContext());
		initlunbo();
		view = inflater.inflate(R.layout.every_frament, null);
		gridView = (GridViewWithHeaderAndFooter) view
				.findViewById(R.id.grid_view_by_everyday);
		netStatus = (RelativeLayout) view.findViewById(R.id.re_error);

		mPtrFrame = (PtrClassicFrameLayout) view
				.findViewById(R.id.more_new_head_frame);
		mPtrFrame.setKeepHeaderWhenRefresh(true);
		mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				// 恢复出厂设置
				Log.e("xyz", "开始刷新");

				if (NetTools.checkNetwork(context)){
					netStatus.setVisibility(View.GONE);
					headView.setVisibility(View.VISIBLE);
				}
				else{
					netStatus.setVisibility(View.VISIBLE);
				}
				pageId = 1;
				firstVideoID = -1;
				videoEntityLusList.clear();
				first = true;
				netUtisl.getValues(new requestHandler(requestJsonHandler),
						pageId, pageSize, firstVideoID, null);
				gridView.setAdapter(listViewDataAdapter);
				footView.setVisibility(View.GONE);
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return super.checkCanDoRefresh(frame, content, header);
			}
		});
		headView = inflater.inflate(R.layout.everyday_new_head, null);
		viewPager = (AutoScrollViewPager) headView
				.findViewById(R.id.view_pager_2);

		footView = inflater.inflate(R.layout.everyday_new_foot, null);
		footView.setOnClickListener(footoncClickListener);
		footProgressBar = footView
				.findViewById(R.id.ptr_classic_header_rotate_view_progressbar);
		footTextView = footView.findViewById(R.id.endmeg);
		gridView.addHeaderView(headView);
		gridView.addFooterView(footView);
		gridView.setOnScrollListener(gridViewOnScrollListener);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("position", position);
				intent.setClass(context, ActivityShowVideo.class);
				context.startActivity(intent);
			}
		});

		listViewDataAdapter = new ListViewDataAdapter<VideoEntityLu>();
		listViewDataAdapter.setViewHolderClass(this, MeiRiXianViewHolder.class,
				mImageLoader);

		gridView.setAdapter(listViewDataAdapter);
		gridView.setNumColumns(3);
		videoEntityLusList = listViewDataAdapter.getDataList();
		DefindConstant.EVERYDAY_NEW = listViewDataAdapter.getDataList();

		pageId = 1;
		firstVideoID = -1;
		videoEntityLusList.clear();
		first = true;

		if (NetTools.checkNetwork(context))
			netUtisl.getValues(new requestHandler(requestJsonHandler), pageId,
					pageSize, firstVideoID, null);
		else
			netStatus.setVisibility(View.VISIBLE);

		gridView.setAdapter(listViewDataAdapter);
		return view;
	}

	private void initlunbo() {
		netUtisl.getLunBo(new requestHandler(lunboRequestJsonHandler));
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		gridView.setSelection(Show_Video_Fragment.positionx - 1);
		super.onResume();
	}

	// 底部加载view点击事件
	private OnClickListener footoncClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};
	// 滑动加载事件
	private OnScrollListener gridViewOnScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			// 滚动时
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

				footView.setVisibility(View.VISIBLE);
				break;
			// 当不滚动时
			case OnScrollListener.SCROLL_STATE_IDLE:
				Log.e("xyz", "jinlaila" + gridView.getFirstVisiblePosition());
				if (gridView.getLastVisiblePosition() > (gridView.getCount() * 0.7)) {

					netUtisl.getValues(new requestHandler(requestJsonHandler),
							pageId, pageSize, firstVideoID, null);

				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	// ///////////////////////////////////getdata//////////////////////////////
	// 获取数据完毕后回调
	// 九宫格图片
	private class requestHandler implements RequestHandler<JsonData> {
		private RequestJsonHandler requestJsonHandler;

		public requestHandler(RequestJsonHandler requestJsonHandler) {
			super();
			this.requestJsonHandler = requestJsonHandler;
		}

		@Override
		public void onRequestFinish(JsonData data) {
			requestJsonHandler.onRequestFinish(data);
		}

		@Override
		public JsonData processOriginData(JsonData jsonData) {
			return jsonData;
		}

		@Override
		public void onRequestFail(FailData failData) {
			if (failData.getErrorType() == 2) {
				Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				headView.setVisibility(View.GONE);
				netStatus.setVisibility(View.VISIBLE);
				mPtrFrame.refreshComplete();
			} else {
				footProgressBar.setVisibility(View.GONE);
				footTextView.setVisibility(View.VISIBLE);
				((TextView) footTextView).setText("没有更多了");
				mPtrFrame.refreshComplete();

			}
		}

	}

	private RequestJsonHandler requestJsonHandler = new RequestJsonHandler() {

		@Override
		public void onRequestFinish(JsonData data) {
			// json 数据转换
			List<VideoEntityLu> list = data.asList(VideoEntityLu.jsonConverter);
			Log.e("xyz", "获取数据大小：" + list.size() + "");

			if (list.size() < 1) {
				// footProgressBar.setVisibility(View.GONE);
				// footTextView.setVisibility(View.VISIBLE);
				// Toast.makeText(context, "到底啦", Toast.LENGTH_SHORT).show();
				// mPtrFrame.refreshComplete();
				// footView.setVisibility(View.GONE);
			} else {
				videoEntityLusList.addAll(list);
				listViewDataAdapter.notifyDataSetChanged();
				footView.setVisibility(View.GONE);
				pageId++;
				mPtrFrame.refreshComplete();
				if (first) {
					firstVideoID = videoEntityLusList.get(0).getVideoID();
					first = false;
				}
			}
		}
	};
	// 轮播图

	private RequestJsonHandler lunboRequestJsonHandler = new RequestJsonHandler() {

		@Override
		public void onRequestFinish(JsonData data) {
			List<TypeEntity> list = data.asList(TypeEntity.jsonConverter);
			TypeEntity typeEntity;
			for (int i = 0; i < list.size(); i++) {
				// 1为活动分类，2为静态分类
				typeEntity = list.get(i);
				if (typeEntity.getState().equals("1")) {
					imgViews.add(typeEntity);
				}
			}
			viewPager.setAdapter(new ImagePagerAdapter(context, imgViews,
					mImageLoader).setInfiniteLoop(true));
			viewPager.setInterval(LUNBOTIME);
			viewPager.startAutoScroll();
		}
	};
}

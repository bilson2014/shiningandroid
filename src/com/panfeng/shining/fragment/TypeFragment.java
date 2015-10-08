package com.panfeng.shining.fragment;

import in.srain.cube.image.CubeImageView;
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

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.shining.activity.newactivity.ShowAllMediaActivity;
import com.panfeng.shining.dawn.net.NetUtisl;
import com.panfeng.shining.dawn.viewholder.ImageSize;
import com.panfeng.shining.dawn.viewholder.MeiRiXianViewHolder;
import com.panfeng.shining.entity.TypeEntity;
import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shining.slw.activity.fragment.Show_Video_Fragment;
import com.panfeng.shinning.R;

public class TypeFragment extends TitleBaseFragment {

	public int pageId = 1;

	private ImageLoader mImageLoader;

	// view
	private GridViewWithHeaderAndFooter gridView;
	private View headView;
	private View footView;
	private View footProgressBar;
	private View footTextView;

	// 状态记录
	private int pageSize = 12;
	private int firstVideoID = -1;
	private boolean isupdate = false;

	// 资源
	private ListViewDataAdapter<VideoEntityLu> listViewDataAdapter;
	private List<VideoEntityLu> videoEntityLusList;
	private PtrClassicFrameLayout mPtrFrame;
	private Context context;
	private NetUtisl netUtisl;
	private TypeEntity typeEntity;
	CubeImageView topimg;
	TextView toptext, bottext;

	@SuppressLint({ "InflateParams", "ResourceAsColor" })
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		pageId = 1;
		typeEntity = (TypeEntity) super.mDataIn;
		context = getActivity();
		netUtisl = new NetUtisl();
		mImageLoader = ImageLoaderFactory.create(getContext());

		View view = inflater.inflate(R.layout.every_frament, null);
		gridView = (GridViewWithHeaderAndFooter) view
				.findViewById(R.id.grid_view_by_everyday);

		mPtrFrame = (PtrClassicFrameLayout) view
				.findViewById(R.id.more_new_head_frame);
		mPtrFrame.setKeepHeaderWhenRefresh(true);
		mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				// 恢复出厂设置
				mPtrFrame.refreshComplete();
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return super.checkCanDoRefresh(frame, content, header);
			}
		});

		//
		if (typeEntity.getState().equals("1")) {
			headView = inflater.inflate(R.layout.more_new_head_v2, null);
			topimg = (CubeImageView) headView.findViewById(R.id.more_top_img);
			topimg.loadImage(mImageLoader, typeEntity.getSmallImageUrl(),
					ImageSize.sGridImageReuseInfo);

		} else {
			headView = inflater.inflate(R.layout.more_new_head, null);
			topimg = (CubeImageView) headView.findViewById(R.id.more_top_img);
			toptext = (TextView) headView.findViewById(R.id.more_top_text);
			bottext = (TextView) headView.findViewById(R.id.more_bottom_text);
			toptext.setText(typeEntity.getTypeName());
			bottext.setText(typeEntity.getTypeContents());
			topimg.loadImage(mImageLoader, typeEntity.getSmallImageUrl());

		}
		Log.e("xyz", typeEntity.getSmallImageUrl());

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
				 intent.putExtra("obj", videoEntityLusList.get(position));
				 intent.setClass(context, ShowAllMediaActivity.class);
				 context.startActivity(intent);

			}
		});

		listViewDataAdapter = new ListViewDataAdapter<VideoEntityLu>();
		listViewDataAdapter.setViewHolderClass(this, MeiRiXianViewHolder.class,
				mImageLoader);

		gridView.setAdapter(listViewDataAdapter);
		gridView.setNumColumns(3);
		videoEntityLusList = listViewDataAdapter.getDataList();
		netUtisl.getValues(new requestHandler(requestJsonHandler), pageId,
				pageSize, 974, typeEntity.getTypeName());
		return view;
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
				if (!isupdate) {
					if (gridView.getLastVisiblePosition() > (gridView
							.getCount() * 0.7)) {
						netUtisl.getValues(new requestHandler(
								requestJsonHandler), pageId, pageSize,
								firstVideoID, typeEntity.getTypeName());
						isupdate = true;
					}
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};

	// ///////////////////////////////////getdata//////////////////////////////
	// 获取数据完毕后回调
	// 九宫格图片
	private class requestHandler implements RequestHandler<JsonData> {
		private RequestJsonHandler requestJsonHandler;

		public requestHandler(RequestJsonHandler requestJsonHandler) {
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
			Log.e("xyz", failData.getErrorType()+"type");
			isupdate = false;
			//网络异常
			if(failData.getErrorType()==2)
			{
				Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				mPtrFrame.refreshComplete();
			}else
			{
				footProgressBar.setVisibility(View.GONE);
				footTextView.setVisibility(View.VISIBLE);
				((TextView)footTextView).setText("没有更多了");
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
				
			} else {
				videoEntityLusList.addAll(list);
				listViewDataAdapter.notifyDataSetChanged();
				footView.setVisibility(View.GONE);
				pageId++;
				isupdate = false;
				mPtrFrame.refreshComplete();
			}

		}
	};
}

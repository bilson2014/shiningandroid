package com.panfeng.shining.fragment;

import in.srain.cube.mints.base.TitleBaseFragment;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import java.io.File;

import tyu.common.net.TyuDefine;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.panfeng.shining.activity.newactivity.ShowAllMediaActivity;
import com.panfeng.shining.adapter.MoreMediaAdapter;
import com.panfeng.shining.interfaces.getResouceInterface;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shinning.R;

@SuppressLint("InflateParams")
public class FestailFragment extends TitleBaseFragment {

	Context ctx;
	GridViewWithHeaderAndFooter gridView;
	View headerView,footView;
	private PtrClassicFrameLayout mPtrFrame;
	private MoreMediaAdapter moreAdapter;
	VideoEntity ve;
	boolean isEnd = false;
	boolean isLoad = false;
	int page = 0;
	mHander m = new mHander();
	String name, path;
	ImageView img;

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ctx = getActivity();
		DefindConstant.SORT.clear();

		Bundle bundle = (Bundle) super.mDataIn;
		name = bundle.getString("name");
		path = bundle.getString("path");

		View view = inflater.inflate(R.layout.festail_frament, null);
		headerView = inflater.inflate(R.layout.festail_new_head, null);
		img = (ImageView) headerView.findViewById(R.id.festail_top_img);
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

		File f = new File(path);
		Bitmap bm = BitmapFactory.decodeFile(path);

		if (f.exists()) {

			// Bitmap bm = BitmapFactory.decodeFile(path);
			img.setImageBitmap(bm);
		} else {
			// topimg.setImageBitmap(bm);
			img.setVisibility(View.GONE);
		}

		gridView = (GridViewWithHeaderAndFooter) view
				.findViewById(R.id.grid_view_by_festail);

		updateMessage();
		moreAdapter = new MoreMediaAdapter(ctx, DefindConstant.SORT);

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
				String name = videoName.substring(0, videoName.lastIndexOf('.'));
				Intent intent = new Intent();
				intent.putExtra("url", videoUrl);
				intent.putExtra("imgurl", imgUrl);
				intent.putExtra("videoID", videoID);
				intent.putExtra("videoName", name);
				intent.putExtra("fenlei", "festail");
				intent.putExtra("position", position);
				intent.setClass(ctx, ShowAllMediaActivity.class);
				startActivity(intent);

			}
		});

		gridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {

				// 滚动时

				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					m.sendEmptyMessage(hit);
					break;

				// 当不滚动时

				case OnScrollListener.SCROLL_STATE_IDLE:

					if (gridView.getFirstVisiblePosition() > (gridView
							.getCount() / 3) && !isEnd && !isLoad) {

						isLoad = true;
						m.sendEmptyMessage(getMore);

					}

					if (gridView.getLastVisiblePosition() == (gridView
							.getCount() - 1) && !isEnd && !isLoad)

					{
						m.sendEmptyMessage(getMore);

					}

					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		gridView.addHeaderView(headerView);

		gridView.setNumColumns(3);
		gridView.setAdapter(moreAdapter);
		mPtrFrame = (PtrClassicFrameLayout) view
				.findViewById(R.id.more_new_head_frame);
		mPtrFrame.setKeepHeaderWhenRefresh(true);
		mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
			public void onRefreshBegin(PtrFrameLayout frame) {
				// requestData();

			}

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

	private void updateMessage() {

		new Thread(new Runnable() {
			@SuppressLint("UseSparseArrays")
			@Override
			public void run() {
				try {

//					grf.getValues(page, name, null);

					m.sendEmptyMessage(nitifyData);

				} catch (Exception e) {

					m.sendEmptyMessage(notMore);
				}
			}
		}).start();

	}

	private int nitifyData = 0x123;
	private int getMore = 0x1234;
	private int hit = 0x12345;
	private int notMore = 0x123456;

	class mHander extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == nitifyData) {

				moreAdapter.notifyDataSetChanged();
				Toast.makeText(ctx, "success", Toast.LENGTH_SHORT).show();
				isLoad = false;
				return;

			}

			else if (msg.what == getMore) {
				page++;
				updateMessage();
				return;

			}

			else if (msg.what == notMore) {

				if (!isEnd) {
					Toast.makeText(ctx, "没有更多了", Toast.LENGTH_SHORT).show();
				}
				isEnd = true;

				return;

			}

		}

	}

}

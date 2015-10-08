package com.panfeng.shining.slw.activity;

import in.srain.cube.request.FailData;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestHandler;
import in.srain.cube.request.RequestJsonHandler;

import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.panfeng.shining.dawn.net.NetUtisl;
import com.panfeng.shining.dawn.view.meirixianFragement;
import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shining.slw.activity.fragment.Show_Video_Fragment;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.tools.CommonTools;
import com.panfeng.shinning.R;

@ContentView(R.layout.activity_show_video)
public class ActivityShowVideo extends FragmentActivity {

	@ViewInject(R.id.show_video_ViewPager)
	private ViewPager viewPager;
	private MyViewPagerAdapter mAdapter;
	private int position;
	private int c;
	JSONArray jsonarray;
	String jsonString;

	int lastValue = -1;
	VideoEntityLu ve;
	mHander everydayUpdateHandler = new mHander();
	private boolean left = false;
	private boolean right = false;
	private boolean isScrolling = false;
	private boolean loadEnd = true;
	Context ctx = ActivityShowVideo.this;
	int pos;
	private NetUtisl netUtisl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		netUtisl=new NetUtisl();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		Window window = ActivityShowVideo.this.getWindow();
		window.setFlags(flag, flag);
		ViewUtils.inject(this);
		position = getIntent().getExtras().getInt("position");

		Log.i("luslw", position + "position");

		try {

			pos = DefindConstant.EVERYDAY_NEW.size();
			mAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
			viewPager.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
			viewPager.setCurrentItem(position);
			viewPager.setOnPageChangeListener(op);

		} catch (Exception e) {

		}

	}

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
			Toast.makeText(ctx, "哎呀！网络不太好啊！！", Toast.LENGTH_SHORT).show();
		}

	}

	private RequestJsonHandler requestJsonHandler = new RequestJsonHandler() {

		@Override
		public void onRequestFinish(JsonData data) {
			// json 数据转换
			List<VideoEntityLu> list = data.asList(VideoEntityLu.jsonConverter);
			Log.e("xyz", "获取数据大小：" + list.size() + "");
			DefindConstant.EVERYDAY_NEW.addAll(list);
			everydayUpdateHandler.sendEmptyMessage(2);
			
		}
	};
	boolean pre = false;
	OnPageChangeListener op = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

			try {

				if (pre && arg0 == 0 && arg1 == 0.0 && loadEnd) {
					if (meirixianFragement.pageId == 0) {
						everydayUpdateHandler.sendEmptyMessage(topEnd);
					} else {

						loadEnd = false;
						everydayUpdateHandler.sendEmptyMessage(leftMessage);

					}
				}

				else if (pre && arg0 == DefindConstant.EVERYDAY_NEW.size() - 1
						&& arg1 == 0.0 && loadEnd) {

					loadEnd = false;
					everydayUpdateHandler.sendEmptyMessage(rightMessage);

				}

				pre = false;
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

			if (arg0 == 1)
				pre = true;
			else if (arg0 == 2)
				pre = false;
		}
	};

	private int pageSize = 12;
	private int firstVideoID = -1;
	final static int leftMessage = 0x2f;
	final static int rightMessage = 0xfc;
	final static int topEnd = 0x3f;
	final static int botEnd = 0x4f;

	class mHander extends Handler {

		boolean loadMore = true;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {

				// viewPager.removeAllViews();

				mAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
				viewPager.setAdapter(mAdapter);
				// mAdapter.notifyDataSetChanged();
				viewPager.setCurrentItem(5);
				loadEnd = true;

				return;
			}

			else if (msg.what == 2) {

				pos = DefindConstant.EVERYDAY_NEW.size();
				// mAdapter = new
				// MyViewPagerAdapter(getSupportFragmentManager());
				// viewPager.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				int current = (meirixianFragement.pageId-1) * 12;
				viewPager.setCurrentItem(current);
				// mAdapter.notifyDataSetChanged();
				loadEnd = true;

				return;
			} else if (msg.what == leftMessage) {

				meirixianFragement.pageId--;
				left = true;
				right = false;
				netUtisl.getValues(new requestHandler(requestJsonHandler),
						meirixianFragement.pageId, pageSize, firstVideoID, null);

				return;
			}

			else if (msg.what == rightMessage) {

				meirixianFragement.pageId++;

				left = false;
				right = true;
				netUtisl.getValues(new requestHandler(requestJsonHandler),
						meirixianFragement.pageId, pageSize, firstVideoID, null);

				return;
			}

			else if (msg.what == topEnd) {

				String str = "已经是最新视频了";
				CommonTools.showToast(ctx, str);

				return;
			}

			else if (msg.what == botEnd) {

				String str = "没有更多视频了";
				CommonTools.showToast(ctx, str);

				return;
			}

		}

	}

	/**
	 * 自定义ViewPager适配器
	 */
	class MyViewPagerAdapter extends FragmentStatePagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int positionx) {

			// Log.i("lefttime", "vvvvvvvvvvvvvvvvvvvvv");
			// Log.i("lefttime", "positionx" +positionx);
			// Log.i("dd", positionx
			// + " /public Fragment getItem(int positionx) {" + position);
			Bundle bundle = new Bundle();
			bundle.putInt(Show_Video_Fragment.TAG_VIDEO_POSITION, positionx);
			// bundle.putInt(Show_Video_Fragment.TAG_VIDEO_POSITION_START,
			// position);
			Show_Video_Fragment fragment = new Show_Video_Fragment();
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getCount() {

			return pos;
		}
	}

}

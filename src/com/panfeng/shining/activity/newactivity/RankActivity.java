package com.panfeng.shining.activity.newactivity;

import in.srain.cube.request.FailData;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestHandler;
import in.srain.cube.request.RequestJsonHandler;

import java.util.ArrayList;
import java.util.List;

import tyu.common.net.TyuDefine;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.shining.adapter.RankListAdapter;
import com.panfeng.shining.dawn.net.NetUtisl;
import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.tools.NetTools;
import com.panfeng.shinning.R;

public class RankActivity extends Activity {

	NetUtisl netUtisl = new NetUtisl();
	private Context context;
	/*
	 * 排行版
	 */

	// 数据源

	int page = 0;

	mHander m = new mHander();

	
	Context ctx = RankActivity.this;
	private RankListAdapter rankAdapter;
	private ListView listview;
	RelativeLayout neterror;
	private List<VideoEntityLu> mTalks = new ArrayList<VideoEntityLu>();

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rank);
		neterror = (RelativeLayout) findViewById(R.id.re_error);
		// 无网提示
		if (!NetTools.checkNetwork(ctx))
			neterror.setVisibility(View.VISIBLE);
		listview = (ListView) findViewById(R.id.rank_list);
		initTitleBar();
		DefindConstant.RANKING.clear();
		netUtisl.getPaiHang(new requestHandler(requestJsonHandler));
		rankAdapter = new RankListAdapter(this, mTalks);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
				Intent intent = new Intent();
				intent.putExtra("obj", mTalks.get(position));
				
				intent.setClass(ctx, ShowAllMediaActivity.class);
				startActivity(intent);
			}
		});
	}

	void initTitleBar() {
		View title = findViewById(R.id.rank_bar);
		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);

		barimg.setBackgroundResource(R.drawable.rankimg);
		bartxt.setText("排行榜");
		ImageView barbot = (ImageView) title.findViewById(R.id.bar_set);

		barbot.setVisibility(View.VISIBLE);

		barbot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RankActivity.this,
						SearchActivity.class);
				startActivity(intent);
			}
		});

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
			if (failData.getErrorType() == 2) {
				Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				neterror.setVisibility(View.VISIBLE);
			} else {
				

			}
		}
	}

	private RequestJsonHandler requestJsonHandler = new RequestJsonHandler() {

		@Override
		public void onRequestFinish(JsonData data) {
			// json 数据转换
			List<VideoEntityLu> list = data.asList(VideoEntityLu.jsonConverter);
			Log.e("xyz", "获取数据大小：" + list.size() + "");
			mTalks.addAll(list);
			m.sendEmptyMessage(123);
		}
	};

	class mHander extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 123) {
				listview.setAdapter(rankAdapter);
				rankAdapter.notifyDataSetChanged();

			}
		}

	}

}

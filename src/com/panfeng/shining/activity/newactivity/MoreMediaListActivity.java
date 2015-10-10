package com.panfeng.shining.activity.newactivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;

import tyu.common.net.TyuDefine;
import android.annotation.SuppressLint;
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

import com.panfeng.shining.adapter.MoreListlAdapter;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.entity.TypeEntity;
import com.panfeng.shinning.R;
import com.tencent.stat.StatService;

public class MoreMediaListActivity extends Activity {

	/**
	 * 分类列表
	 */

	private View more;
	private RelativeLayout funny, l8090, gold, after00, crazy, lovely, unlimit,
			daydayup, movie, miku;
	private Context ctx = MoreMediaListActivity.this;
	ListView list;
	public static List<TypeEntity> typeList = new ArrayList<TypeEntity>();
	JSONArray jsonarray;
	String jsonString;

	MoreListlAdapter mlAdapter;
	mHander m = new mHander();

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			// if(secondDel())
			// Toast.makeText(S2ndMainActivity.this,
			// "再按一次退出",Toast.LENGTH_SHORT).show();
			//
			//
			//
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moremedialist);
		initTitleBar();
		init();
		Log.i("showDia", "init");
		updateMessage();

		mlAdapter = new MoreListlAdapter(MoreMediaListActivity.this, typeList);
		list.setAdapter(mlAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(MoreMediaListActivity.this,
						MoreMeidaActivityV2.class);
				Log.e("xyz", typeList.get(position).getState() + "123123");
				intent.putExtra("obj", typeList.get(position));

				startActivity(intent);

				Properties prop = new Properties();
				prop.setProperty("name", typeList.get(position).getTypeName());
				StatService.trackCustomKVEvent(ctx, "videotag", prop);

			}
		});

	}

	private void updateMessage() {

		new Thread(new Runnable() {
			@SuppressLint("UseSparseArrays")
			@Override
			public void run() {

				try {
					jsonString = TyuShinningData.getInstance().getTypeList();
					Log.i("showDia", "jsonString" + jsonString);
					jsonarray = new JSONArray(jsonString);

					for (int i = 0; i < jsonarray.length(); i++) {
						TypeEntity te = new TypeEntity();
						String type = jsonarray.getJSONObject(i).getString(
								"ms_name");
						String content = jsonarray.getJSONObject(i).getString(
								"ms_remark");

						int state = jsonarray.getJSONObject(i).getInt(
								"ms_state");

						if (state == 2) {
							te.setTypeSmallImage(jsonarray.getJSONObject(i)
									.getString("ms_lueimageurl"));
							te.setTypeBigImage(jsonarray.getJSONObject(i)
									.getString("ms_changimageurl"));
							te.setTypeName(type);
							te.setTypeContents(content);
							te.setState(state + "");
							typeList.add(te);

						}

					}
					m.sendEmptyMessage(123);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}).start();

	}

	private void init() {
		typeList.clear();
		list = (ListView) findViewById(R.id.moremedia_list);

	}

	void initTitleBar() {
		View title = findViewById(R.id.newbar);
		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);

		bartxt.setText("精选集");
		ImageView barbot = (ImageView) title.findViewById(R.id.bar_set);

		barbot.setVisibility(View.VISIBLE);

		barbot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MoreMediaListActivity.this,
						SearchActivity.class);
				startActivity(intent);

				// TODO:MTA

			}
		});

	}

	class mHander extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 123) {

				mlAdapter.notifyDataSetChanged();

			}
		}
	}

}

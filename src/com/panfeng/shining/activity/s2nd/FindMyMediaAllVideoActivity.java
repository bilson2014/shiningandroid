package com.panfeng.shining.activity.s2nd;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.panfeng.shinning.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FindMyMediaAllVideoActivity extends Activity {

	// 路径
	String mediaPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/mySave";
	String imagePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/videoImage/";

	int delId;// 所需删除视频id
	String justGe = "/";// 转义

	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	GridView list;
	SimpleAdapter adapter;
	Context ctx = FindMyMediaAllVideoActivity.this;

	List<dirMap> dirList = new ArrayList<FindMyMediaAllVideoActivity.dirMap>();
	File[] home;// File 类型 查找用
	String[] homes;// String 类型 填充集合用

	@Override
	protected void onResume() {

		super.onResume();

		SharedPreferences preference = ctx.getSharedPreferences("configs",
				Context.MODE_PRIVATE);
		int isrInteger = preference.getInt("isDel", 0);

		if (isrInteger != 0) {

			//
			listItems.remove(delId);
			dirList.remove(delId);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			SharedPreferences.Editor editor = preference.edit();
			editor.putInt("isDel", 0);
			editor.commit();

		}

		// home = new File(mediaPath).listFiles();
		// initTitleBar();
		//
		// if (home.length < 1) {
		//
		// } else {
		// listItems.clear();
		// adapter.notifyDataSetChanged();
		// updateSongList();
		// }

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_my_media_grid_layout);

		initTitleBar();
		list = (GridView) findViewById(R.id.findlist);
		home = new File(mediaPath).listFiles();
		homes = new File(mediaPath).list();

		if (home.length < 1) {

		} else {

			updateInfo();
		}

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				delId = position;

				Intent intent = new Intent(FindMyMediaAllVideoActivity.this,
						ShowMediaActivity.class);
				intent.putExtra("path", dirList.get(position).getPath());
				intent.putExtra("videoname", new File(dirList.get(position)
						.getPath()).getName());

				// intent.putExtra("path",mediaPath + "/" +
				// home[position].getName());
				// intent.putExtra("videoname", home[position].getName());
				intent.putExtra("where", 1);
				intent.putExtra("isup", "false");
				intent.putExtra("isCopy", "false");
				startActivity(intent);

			}
		});

	}

	void initTitleBar() {
		View title = findViewById(R.id.newlistbar);

		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
		bartxt.setText("我的收藏");

		bartxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

		barimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

	}

	public void updateInfo() {

		List<String> fileList = Arrays.asList(homes);

		for (int i = 0; i <= fileList.size() - 1; i++) {

			String locaFile = fileList.get(i);

			String videoName = fileList.get(i);

			dirList.add(new dirMap(mediaPath + justGe + videoName));

		}

		for (int j = 0; j < dirList.size(); j++) {

			String str = new File(dirList.get(j).getPath()).getName();
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("image",
					imagePath + str.substring(0, str.lastIndexOf('.')) + ".jpg");

			listItems.add(item);

		}
		adapter = new SimpleAdapter(this, listItems, R.layout.find_item_2,
				new String[] { "image" }, new int[] { R.id.music_btn });

		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		// for (int i = 0; i < home.length; i++) {
		//
		// String str = home[i].getName();
		// String filePath = home[i].getAbsolutePath();
		//
		//
		//
		// String strName = null;
		// String strh = null;
		// String img = str.substring(str.lastIndexOf('.'), str.length());
		// // Log.i("lutao", str);
		// // Log.i("lutao", "img"+str.substring(0,
		// // str.lastIndexOf('.'))+".jpeg");
		// Log.i("lutao", imagePath + str.substring(0, str.lastIndexOf('.'))
		// + ".jpg");
		//
		// Map<String, Object> item = new HashMap<String, Object>();
		// item.put("image",
		// imagePath + str.substring(0, str.lastIndexOf('.')) + ".jpg");
		// item.put("music", str);
		// item.put("author", "葛大爷");
		// listItems.add(item);
		//
		// adapter = new SimpleAdapter(this, listItems, R.layout.find_item,
		// new String[] { "image" }, new int[] { R.id.music_btn });
		//
		// list.setAdapter(adapter);
		//
		// }

	}

	class dirMap {
		private String path;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public dirMap(String path) {
			super();
			this.path = path;

		}

		public dirMap() {
			super();
		}

	}

}

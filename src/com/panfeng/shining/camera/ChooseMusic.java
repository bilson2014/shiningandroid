package com.panfeng.shining.camera;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tyu.common.utils.TyuPreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.panfeng.shining.activity.newactivity.SearchActivity;
import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.tools.MtaTools;
import com.panfeng.shinning.R;

class Mp3Filter implements FilenameFilter {
	@Override
	public boolean accept(File dir, String name) {
		return (name.endsWith(".mp3"));
	}
}

public class ChooseMusic extends Activity implements OnClickListener {

	/*
	 * 音乐选择页
	 */

	String audioPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/videoMusic";

	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	private ListView list;
	Context ctx = ChooseMusic.this;

	String audioName;

	// 选择分类
	LinearLayout song_chinese, song_english, song_dou, song_katong, song_god,
			song_movie;

	DBAdapter db;

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
	protected void onResume() {

		super.onResume();

		Log.i("luslw", "change");

		if (listItems.size() < 0) {

		}

		else {

			listItems.clear();
			updateSongList();

		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.songlist);
		Log.i("lop", "" + audioPath);
		db = new DBAdapter(ChooseMusic.this);

		initTitleBar();
		init();
		newPath();

		updateSongList();

		// // 填充进数据库
		// if (TyuPreferenceManager.isFirstOpen("copEnd")) {
		// copyResToSdcard();
		// db.open();
		// db.insertMusic("happywifi.m4a", "喜欢你", "邓紫棋");
		// db.insertMusic("silver.m4a", "匆匆那年", "王菲");
		// db.insertMusic("high.m4a", "1+1=小可爱", "MiKu");
		// db.insertMusic("tou.m4a", "夜空中最美的星", "逃跑计划");
		// db.insertMusic("morered.m4a", "See You Again",
		// "Wiz Khalifa、Charlie Puth");
		// db.close();
		//
		// }

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView musicName = (TextView) view
						.findViewById(R.id.song_name);
				TextView textName = (TextView) view
						.findViewById(R.id.song_path);
				String filename = textName.getText().toString();
				String name = musicName.getText().toString();

				MtaTools.MtaMusic(ctx, name);

				Intent intent = new Intent(ChooseMusic.this, MySurface.class);
				intent.putExtra("path", audioPath + "/" + filename);


				startActivity(intent);

			}
		});

	}

	public void init() {

		song_chinese = (LinearLayout) findViewById(R.id.song_chinese);
		song_chinese.setOnClickListener(this);

		song_english = (LinearLayout) findViewById(R.id.song_english);
		song_english.setOnClickListener(this);

		song_dou = (LinearLayout) findViewById(R.id.song_dou);
		song_dou.setOnClickListener(this);

		song_katong = (LinearLayout) findViewById(R.id.song_katong);
		song_katong.setOnClickListener(this);

		song_god = (LinearLayout) findViewById(R.id.song_god);
		song_god.setOnClickListener(this);

		song_movie = (LinearLayout) findViewById(R.id.song_movie);
		song_movie.setOnClickListener(this);

		list = (ListView) findViewById(R.id.songlistview);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.song_chinese:

			Intent intent = new Intent(ChooseMusic.this, NetMusic.class);
			intent.putExtra("needId", "5");
			intent.putExtra("needName", "中文");
			startActivity(intent);

			break;

		case R.id.song_english:

			Intent intent1 = new Intent(ChooseMusic.this, NetMusic.class);
			intent1.putExtra("needId", "6");
			intent1.putExtra("needName", "外语");
			startActivity(intent1);

			break;

		case R.id.song_dou:

			Intent intent2 = new Intent(ChooseMusic.this, NetMusic.class);
			intent2.putExtra("needId", "7");
			intent2.putExtra("needName", "搞笑");
			startActivity(intent2);

			break;

		case R.id.song_katong:

			Intent intent3 = new Intent(ChooseMusic.this, NetMusic.class);
			intent3.putExtra("needId", "8");
			intent3.putExtra("needName", "动漫");
			startActivity(intent3);

			break;

		case R.id.song_god:

			Intent intent4 = new Intent(ChooseMusic.this, NetMusic.class);
			intent4.putExtra("needId", "9");
			intent4.putExtra("needName", "神曲");
			startActivity(intent4);

			break;

		case R.id.song_movie:

			Intent intent5 = new Intent(ChooseMusic.this, NetMusic.class);
			intent5.putExtra("needId", "10");
			intent5.putExtra("needName", "影视");
			startActivity(intent5);

			break;

		}
	}

	public void newPath() {

		String pathString = Environment.getExternalStorageDirectory()
				.getAbsolutePath();

		String videoString = pathString + File.separator
				+ "com.panfeng.shinning" + File.separator + "videoMusic";
		File filevideo = new File(videoString);
		if (!filevideo.exists()) {
			filevideo.mkdirs();
		}

	}

	public void initTitleBar() {

		View title = findViewById(R.id.music_bar);
		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);

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

	// public void copyResToSdcard() {// name为sd卡下制定的路径
	// String pathString = Environment.getExternalStorageDirectory()
	// .getAbsolutePath();
	//
	// String videoString = pathString + File.separator
	// + "com.panfeng.shinning" + File.separator + "videoMusic";
	// String videoBase = pathString + File.separator + "com.panfeng.shinning"
	// + File.separator + "baseVideo";
	// Field[] raw = R.raw.class.getFields();
	// for (Field r : raw) {
	// try {
	// // System.out.println("R.raw." + r.getName());
	// int id = getResources().getIdentifier(r.getName(), "raw",
	// getPackageName());
	// if (!r.getName().equals("base")) {
	// String path = videoString + "/" + r.getName() + ".m4a";
	// Log.i("video", "you" + path);
	// BufferedOutputStream bufEcrivain = new BufferedOutputStream(
	// (new FileOutputStream(new File(path))));
	// BufferedInputStream VideoReader = new BufferedInputStream(
	// getResources().openRawResource(id));
	// byte[] buff = new byte[20 * 1024];
	// int len;
	// while ((len = VideoReader.read(buff)) > 0) {
	// bufEcrivain.write(buff, 0, len);
	// }
	// bufEcrivain.flush();
	// bufEcrivain.close();
	// VideoReader.close();
	// }
	//
	// else {
	//
	// String path = videoBase + "/" + r.getName() + ".mp4";
	// Log.i("video", "you" + path);
	// BufferedOutputStream bufEcrivain = new BufferedOutputStream(
	// (new FileOutputStream(new File(path))));
	// BufferedInputStream VideoReader = new BufferedInputStream(
	// getResources().openRawResource(id));
	// byte[] buff = new byte[20 * 1024];
	// int len;
	// while ((len = VideoReader.read(buff)) > 0) {
	// bufEcrivain.write(buff, 0, len);
	// }
	// bufEcrivain.flush();
	// bufEcrivain.close();
	// VideoReader.close();
	//
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }

	public void updateSongList() {

		db.open();

		Cursor c = db.getAllMusic();

		if (c.moveToFirst()) {
			do {

				String audio_id = c.getString(0);
				String audioPath = c.getString(1);
				String audioName = c.getString(2);
				String audioAuthor = c.getString(3);

				
				
				Map<String, Object> item = new HashMap<String, Object>();

				item.put("music", audioName);
				item.put("path", audioPath);
				item.put("author", audioAuthor);

				listItems.add(item);

			} while (c.moveToNext());
		}
		db.close();

		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.songlist_item, new String[] { "music", "author",
						"path" }, new int[] { R.id.song_name, R.id.song_author,
						R.id.song_path });

		list.setAdapter(adapter);

	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	//
	// if (secondDel())
	// Toast.makeText(ChooseMusic.this, "再按一次退出", Toast.LENGTH_SHORT)
	// .show();
	//
	// return false;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

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

}
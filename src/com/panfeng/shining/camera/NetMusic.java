package com.panfeng.shining.camera;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import tyu.common.utils.TyuFileUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.shining.db.DBAdapter;
import com.panfeng.shining.tools.MtaTools;
import com.panfeng.shinning.R;

public class NetMusic extends Activity implements OnClickListener {

	/*
	 * 网络音乐明细
	 */
	private static final String MEDIA_PATH = new String("/sdcard/");
	private List<String> songs = new ArrayList<String>();
	private MediaPlayer mp = new MediaPlayer();
	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	private ListView list, netlist;
	private Button showSelf, netSelf;
	private boolean ischeck = true, isnetcheck = true;
	private int SHOW = 0x1, FINISH = 0x2;
	private JSONArray jsonarray;
	private myHandler myH;
	// private DBAdapter db = new DBAdapter(NetMusic.this);;
	private String aLocalPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ "com.panfeng.shinning"
			+ File.separator + "videoMusic" + File.separator;
	private int fileId;
	String getId;// 分类id
	String layoutName;
	TextView tesName, musicName, authorName;
	String mediaPathName, musicUseName, musicAuthor;
	Context ctx = NetMusic.this;
	DBAdapter db = new DBAdapter(ctx);

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.netsonglist);

		db.open();

		Cursor c = db.getAllMusic();

		if (c.moveToFirst()) {

			Log.i("video", "if");

			do {

				String audio_id = c.getString(0);
				String audioName = c.getString(1);
				String audioAuthor = c.getString(2);
				String Author = c.getString(3);
				Log.i("video", "name" + audioName);
				Log.i("video", "file" + audioAuthor);
				Log.i("video", "Author" + Author);

			} while (c.moveToNext());

		}

		db.close();

		myH = new myHandler();

		Intent intent = this.getIntent();
		getId = intent.getStringExtra("needId");
		layoutName = intent.getStringExtra("needName");

		initTitleBar();
		init();
		updateSongList();

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Toast.makeText(NetMusic.this, "准备中~", Toast.LENGTH_SHORT)
						.show();

				fileId = position;
				tesName = (TextView) view.findViewById(R.id.musicpath);
				musicName = (TextView) view.findViewById(R.id.music_name);
				authorName = (TextView) view
						.findViewById(R.id.author_music_name);

				mediaPathName = tesName.getText().toString();
				musicUseName = musicName.getText().toString();
				musicAuthor = authorName.getText().toString();
				
				
				MtaTools.MtaMusic(ctx, musicUseName);

			

				// 判断是否下载过

				db.open();
				Cursor c = db.getMusic(mediaPathName);

				if (c.moveToFirst()) {

					String mediaPathName = c.getString(1);

					db.close();

					Intent intent = new Intent(NetMusic.this, MySurface.class);
					intent.putExtra("path", aLocalPath + mediaPathName);
					intent.putExtra("what", "0");
					startActivity(intent);
					finish();

				}

				else {

					// 没有下载过则下载
					new Thread(new Runnable() {

						@Override
						public void run() {

							String netUrl = TyuDefine.HOST + "audio_base_list"
									+ File.separator + mediaPathName;
							int res = TyuHttpClientUtils.downLoadFile(netUrl,
									aLocalPath + mediaPathName, null, null);

							if (res == 1 || res == 0) {

								Log.i("add", "author" + musicAuthor);

								db.open();
								db.insertMusic(mediaPathName, musicUseName,
										musicAuthor);
								db.close();

								Log.i("add", "path=" + mediaPathName);
								Log.i("add", "name=" + musicUseName);

								Intent intent = new Intent(NetMusic.this,
										MySurface.class);
								intent.putExtra("path", aLocalPath
										+ mediaPathName);
								intent.putExtra("what", "0");
								startActivity(intent);
								finish();

							} else {
								TyuFileUtils.deleteDirectory(aLocalPath
										+ mediaPathName);
							}

						}

					}).start();
				}

			}
		});

	}

	public void init() {

		list = (ListView) findViewById(R.id.netvideolist);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// switch (v.getId()) {
		//
		//
		// break;
		//
		// default:
		// break;
		// }
	}

	void initTitleBar() {
		View title = findViewById(R.id.newlistbar);

		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
		bartxt.setText(layoutName);

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

	public void updataAdapter() {

		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.song_item, new String[] { "image", "music", "author",
						"path" }, new int[] { R.id.music_btn, R.id.music_name,
						R.id.author_music_name, R.id.musicpath });

		list.setAdapter(adapter);

		adapter.notifyDataSetChanged();

	}

	public void updateSongList() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.i("slw", "aaa");
					String check = TyuHttpClientUtils.postInfo(TyuDefine.HOST
							+ "smc/getAudioList", "");
					// JSONObject object;
					// JSONArray jsonarray;

					if (check != null && !check.equals("")) {

						jsonarray = new JSONArray(check);
						Message msg = new Message();
						msg.what = SHOW;
						myH.sendMessage(msg);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	class myHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == SHOW) {

				for (int i = 0; i < jsonarray.length(); i++) {

					try {

						JSONObject object = jsonarray.optJSONObject(i);
						String audioName;
						String audioAuthor;
						String audioAuthorName;
						String checkId;
						audioName = object.optString("audio_name");
						audioAuthor = object.optString("audio_author");
						audioAuthorName = object.optString("audio_audioname");
						checkId = object.optString("audio_sort_id");

						if (checkId.equals(getId)) {

							Map<String, Object> item = new HashMap<String, Object>();
							item.put("image", R.drawable.myshinicon);
							item.put("music", audioName);
							item.put("author", audioAuthor);
							item.put("path", audioAuthorName);

							listItems.add(item);
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				updataAdapter();

			} else if (msg.what == FINISH) {

				Toast.makeText(NetMusic.this, "已经下载过了", Toast.LENGTH_LONG)
						.show();

			}

		}

	}

}

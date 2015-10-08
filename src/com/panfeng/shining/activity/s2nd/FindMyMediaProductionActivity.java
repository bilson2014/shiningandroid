package com.panfeng.shining.activity.s2nd;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.shining.tools.UserControl;
import com.panfeng.shining.widgets.TyuWaitingBar;
import com.panfeng.shinning.R;

public class FindMyMediaProductionActivity extends Activity {

	// String mediaPath = Environment.getExternalStorageDirectory()
	// .getAbsolutePath() + "/com.panfeng.shinning" + "/video";
	String mylistPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/myProduction";
	String justGe = "/", check, videoName;
	String imagePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/com.panfeng.shinning" + "/videoImage/";

	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	List<dirMap> dirList = new ArrayList<FindMyMediaProductionActivity.dirMap>();// 对比完数据
	List<String> fileList;// 填充本地数据
	List<String> netList;// 填充对比数据
	List<dirNetMap> dirNetList = new ArrayList<FindMyMediaProductionActivity.dirNetMap>(); // 对比数据
	List<dirNetMap> dirLocalList = new ArrayList<FindMyMediaProductionActivity.dirNetMap>(); // 本地数据

	GridView list;
	SimpleAdapter adapter;
	Context ctx = FindMyMediaProductionActivity.this;

	// 分页
	int AllCount = dirList.size(); // 总数
	int page_size = 10;
	int page_id = 0;
	int coun = 0; // 确认最后一页

	long videoSize;
	boolean first = true;
	Dialog dlg;
	dirMap dm = null;
	String path = "";
	int firsttime = 0;
	myhandler myh = new myhandler();
	int page_i = 0;
	private int delId;// 删除网络视频

	private Context context = FindMyMediaProductionActivity.this;
	String[] production;
	File[] home = new File(imagePath).listFiles();

	@Override
	protected void onResume() {

		super.onResume();

		SharedPreferences preference = context.getSharedPreferences("configs",
				Context.MODE_PRIVATE);
		int isrInteger = preference.getInt("isDel", 0);

		
		//1.视频删除时 2.视频上传时
	
		if (isrInteger == 1) {

			listItems.remove(delId);
			dirList.remove(delId);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			SharedPreferences.Editor editor = preference.edit();
			editor.putInt("isDel", 0);
			editor.commit();

		}

	
		else if (isrInteger == 2) {
			listItems.clear();
			dirLocalList.clear();
			dirLocalList.clear();
			dirList.clear();
			adapter.notifyDataSetChanged();
			page_id = 0;
			getNet();
			SharedPreferences.Editor editor = preference.edit();
			editor.putInt("isDel", 0);
			editor.commit();

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_my_media_grid_layout);
		
		
		getNet();
		initTitleBar();

		adapter = new SimpleAdapter(this, listItems, R.layout.find_item,
				new String[] { "image", "imageicon" }, new int[] {
						R.id.music_btn, R.id.iconmm });

		list = (GridView) findViewById(R.id.findlist);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Log.i("lutao", "position" + position);
				delId = position;
				Intent intent = new Intent(FindMyMediaProductionActivity.this,
						ShowMediaActivity.class);
				intent.putExtra("path", dirList.get(position).getPath());
				intent.putExtra("where", 0);
				intent.putExtra("videoname", new File(dirList.get(position)
						.getPath()).getName());
				intent.putExtra("isup", dirList.get(position).isState());
				intent.putExtra("isCopy", true);
				startActivity(intent);

			}
		});



		list.setAdapter(adapter);
		list.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// 当不滚动时

				case OnScrollListener.SCROLL_STATE_IDLE:

					if (list.getFirstVisiblePosition() > (list.getCount() / 3)) {
						updateSongList();

					}

					if (list.getLastVisiblePosition() == (list.getCount() - 1))

					{
						updateSongList();

					}

					// 判断滚动到顶部

					if (list.getFirstVisiblePosition() == 0) {

					}

					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

	}

	void initTitleBar() {
		View title = findViewById(R.id.newlistbar);

		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
		bartxt.setText("我的作品");

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

	private boolean checkNetwork() {
		ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = conn.getActiveNetworkInfo();
		if (net != null && net.isConnected()) {
			return true;
		}
		return false;
	}

	public void updateSongList() {

		Log.i("dirlist", dirList.size() + "");

		if (dirList.size() < 1) {

		} else if (first) {

			int needNum = page_id * page_size;
			int end = needNum + page_size;

			if (end >= coun) {
				end = coun;
				first = false;

			} else {
				page_id++;
			}
			List<dirMap> lists = dirList.subList(needNum, end);

			for (dirMap dm : lists) {
				path = dm.getPath();
				String str = new File(dm.getPath()).getName();
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("image",
						imagePath + str.substring(0, str.lastIndexOf('.'))
								+ ".jpg");
				if (dm.isState()) {
					item.put("imageicon", R.drawable.noup);
				} else {

					item.put("imageicon", R.drawable.alup);
				}
				listItems.add(item);
			}
			adapter.notifyDataSetChanged();
		}

	}

	@SuppressWarnings("resource")
	public long getFileSize(String f) throws Exception {

		File dF = new File(f);

		FileInputStream fis;

		fis = new FileInputStream(dF);

		long fileLen = fis.available(); // 这就是文件大小

		return fileLen;

	}

	private void getNet() {

		Message msg = new Message();
		msg.what = START;
		myh.sendMessage(msg);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
				String res = TyuHttpClientUtils.postInfo(TyuDefine.HOST
						+ "smc/getUserVideoByUserID?userId="
						+ UserControl.getUserInfo().getUserId(), "");
				String videoNames;
				String videoSizes;
				Message msgs = new Message();

				if (res != null) {


						JSONArray jsonarray = new JSONArray(res);

						for (int i = 0; i < jsonarray.length(); i++) {

							videoNames = jsonarray.getJSONObject(i).getString(
									"videoName");
							videoSizes = jsonarray.getJSONObject(i).getString(
									"size");

							dirNetList.add(new dirNetMap(videoNames, videoSizes));

						}

						msgs.what = END;
						myh.sendMessage(msgs);

					

				}

				else {

					msgs.what = END;
					myh.sendMessage(msgs);

				}
				} catch (Exception e) {
                             
				}

			}
		}).start();

	}

	public void getLocal() {

		try {

			production = new File(mylistPath).list();

			fileList = Arrays.asList(production);

			for (int i = 0; i < fileList.size(); i++) {

				String name = fileList.get(i);

				String size;

				size = getFileSize(mylistPath + "/" + name) + "";

				dirLocalList.add(new dirNetMap(name, size));

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("add", "error");
		}

	}

	private void initDir() {

		for (int i = 0; i <= dirLocalList.size() - 1; i++) {

			String locaFileName = dirLocalList.get(i).getName();
			String locaFileSize = dirLocalList.get(i).getSize();

			Log.i("size", "dirNetList" + dirNetList.size());

			for (int j = 0; j <= dirNetList.size() - 1; j++) {

				if (dirNetList.get(j).getName().equals(locaFileName)
						&& dirNetList.get(j).getSize().equals(locaFileSize)) {
					dirList.add(new dirMap(mylistPath + justGe + locaFileName,
							false));
					break;
				} else if (j == dirNetList.size() - 1) {
					dirList.add(new dirMap(mylistPath + justGe + locaFileName,
							true));
					break;
				}

			}

			if (dirNetList.size() == 0) {
				dirList.add(new dirMap(mylistPath + justGe + locaFileName, true));
			}

		}

		Log.i("dirList", "size=" + dirList.size() + "");

		coun = dirList.size();
		Message msg = new Message();
		msg.what = UPDATE;
		myh.sendMessage(msg);

	}

	class dirMap {
		private String path;
		private boolean state;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public boolean isState() {
			return state;
		}

		public void setState(boolean state) {
			this.state = state;
		}

		public dirMap(String path, boolean state) {
			super();
			this.path = path;
			this.state = state;
		}

		public dirMap() {
			super();
		}

	}

	private int UPDATE = 0x123;
	private int END = 0x124;
	private int START = 0x125;

	@SuppressLint("HandlerLeak")
	class myhandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == UPDATE) {
				updateSongList();
			}

			else if (msg.what == START) {
				dlg = TyuWaitingBar.build(ctx);
				dlg.show();

			}

			else if (msg.what == END) {

				dlg.cancel();
				getLocal();
				initDir();

			}

		}
	}

	class dirNetMap {
		private String size;
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
			this.name = name;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public String getSize() {
			return size;
		}

		public dirNetMap(String name, String size) {
			super();
			this.name = name;
			this.size = size;

		}

		public dirNetMap() {
			super();
		}

	}

}

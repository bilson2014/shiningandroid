package com.panfeng.shining.activity.newactivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.panfeng.shining.adapter.LocalAdapter;
import com.panfeng.shinning.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class LocalVideoActivity extends Activity {

	String LocalVideoPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/DCIM" + "/Camera/";

	String MeiPaiVideoPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/DCIM" + "/.meipaiDrafts/";

	String DCIMPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/DCIM/";

	ArrayList<File> fileLis;

	List<File> mFileList;
	List<String> fileList;
	List<String> meipaiList;
	GridView list;
	Cursor cursor;
	SimpleAdapter adapter;
	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	LocalAdapter localAdapter;
	private Context ctx = LocalVideoActivity.this;

	public static List<diyLocalMap> DIY = new ArrayList<diyLocalMap>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_main);

//		String[] local = new File(LocalVideoPath).list();
//		String[] mei = new File(MeiPaiVideoPath).list();

//		fileList = Arrays.asList(local);
//		meipaiList = Arrays.asList(mei);

		list = (GridView) findViewById(R.id.locallist);
		initTitleBar();
		DIY.clear();

		
		doSearch(DCIMPath);
//		getLocal();
//		getMeiPai();

		localAdapter = new LocalAdapter(LocalVideoActivity.this, DIY);
		list.setAdapter(localAdapter);
		localAdapter.notifyDataSetChanged();

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String path = DIY.get(position).getVideoPath();

				Intent intent = new Intent();
				intent.putExtra("url", path);
				intent.setClass(ctx, ShowLocalVideo.class);
				startActivity(intent);

			}
		});
		
	}
	
	

	void initTitleBar() {
		View title = findViewById(R.id.locallistbar);

		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
	//	bartxt.setText(title_text[keySelectedPosition]);

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

	private void doSearch(String path) {
		ArrayList<String> fileTempList = new ArrayList<String>();
		File file = new File(path);

		if (file.exists()) {
			if (file.isDirectory()) {
				File[] fileArray = file.listFiles();
				for (File f : fileArray) {
					if (f.isDirectory()) {
						doSearch(f.getPath());
					} else {
						if (f.getName().endsWith("mp4")) {
							fileTempList.add(f.getAbsolutePath());
							String name = f.getName();
							String paths = f.getAbsolutePath();
							MediaMetadataRetriever media = new MediaMetadataRetriever();
							media.setDataSource(paths);
                            Bitmap bitmap = media.getFrameAtTime();
                            
                            if(bitmap!=null){
                            
							diyLocalMap diy = new diyLocalMap();
							diy.setName(name);
							diy.setVideoPath(paths);
							// diy.setPath(getVideoThumbnail(ctx, path));
							diy.setPath(bitmap);
							DIY.add(diy);
                            }
							
						}
					}
				}
			}
		}

	}

	private void getLocal() {

		for (int i = 0; i < fileList.size(); i++) {

			String name = fileList.get(i);
			String path = LocalVideoPath + fileList.get(i);
			// String videoPath = path.substring(path.indexOf(".") + 1,
			// path.length());
			if (name.contains("mp4"))

			{

				MediaMetadataRetriever media = new MediaMetadataRetriever();
				media.setDataSource(path);

				Bitmap bitmap = media.getFrameAtTime();

				diyLocalMap diy = new diyLocalMap();
				diy.setName(fileList.get(i));
				diy.setVideoPath(path);
				// diy.setPath(getVideoThumbnail(ctx, path));
				diy.setPath(bitmap);
				DIY.add(diy);
			}

		}

	}

	private void getMeiPai() {

		try {

			for (int i = 0; i < meipaiList.size(); i++) {
				String name = meipaiList.get(i);
				String path = MeiPaiVideoPath + meipaiList.get(i);

				if (name.contains("mp4"))

				{

					MediaMetadataRetriever media = new MediaMetadataRetriever();
					media.setDataSource(path);

					Bitmap bitmap = media.getFrameAtTime();

					diyLocalMap diy = new diyLocalMap();
					diy.setName(meipaiList.get(i));
					diy.setVideoPath(path);
					// diy.setPath(getVideoThumbnail(ctx, path));
					diy.setPath(bitmap);
					DIY.add(diy);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(ctx, "文件异常请联系客服", Toast.LENGTH_SHORT);
		}

	}

	// 缩略图
	public static Bitmap getVideoThumbnail(Context context, String testVideopath) {
		// final String testVideopath = "/mnt/sdcard/sidamingbu.mp4";
		ContentResolver testcr = context.getContentResolver();
		String[] projection = { MediaStore.Video.Media.DATA,
				MediaStore.Video.Media._ID, };
		String whereClause = MediaStore.Video.Media.DATA + " = '"
				+ testVideopath + "'";
		Cursor cursor = testcr.query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
				whereClause, null, null);
		int _id = 0;
		String videoPath = "";
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		if (cursor.moveToFirst()) {

			int _idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID);
			int _dataColumn = cursor
					.getColumnIndex(MediaStore.Video.Media.DATA);

			do {
				_id = cursor.getInt(_idColumn);
				videoPath = cursor.getString(_dataColumn);
				System.out.println(_id + " " + videoPath);
			} while (cursor.moveToNext());
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(testcr, _id,
				Images.Thumbnails.MICRO_KIND, options);
		return bitmap;
	}

	public class diyLocalMap {
		private String name;
		private Bitmap path;
		private String videoPath;

		public String getVideoPath() {
			return videoPath;
		}

		public void setVideoPath(String videoPath) {
			this.videoPath = videoPath;

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;

		}

		public void setPath(Bitmap path) {
			this.path = path;
		}

		public Bitmap getPath() {
			return path;
		}

		public diyLocalMap(String name, Bitmap path, String videoPath) {
			super();
			this.name = name;
			this.path = path;
			this.videoPath = videoPath;

		}

		public diyLocalMap() {
			super();
		}

	}

}

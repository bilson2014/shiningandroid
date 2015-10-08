package com.panfeng.shining.activity.newactivity;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.adapter.MoreMediaAdapter;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shinning.R;

public class MoreMeidaActivity extends Activity {

	/**
	 * 分类明细
	 */

	// 数据源
	JSONArray jsonarray;
	String jsonString;
	DisplayImageOptions options = TyuApp.getCommonConfig();
	ImageLoader imageLoader = ImageLoader.getInstance();



//	List<String> keywordArray = Arrays.asList(keys);

	// 分类标示
	int page = 0;
	int keySelectedPosition = 0;
//	List<VideoEntityLu> listx  = new ArrayList<VideoEntityLu>();; 

	mHander m = new mHander();

	private MoreMediaAdapter moreAdapter;
	private GridView moreGrid;
	VideoEntity ve;
	ImageView topimg;
	TextView toptext, bottext;
	private Context ctx = MoreMeidaActivity.this;
	boolean isEnd=false;
	boolean isLoad=false;
	String img,name,content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_media);
		DefindConstant.SORT.clear();

		moreGrid = (GridView) findViewById(R.id.rank_grid);
	
		init();
		initTitleBar();
		updateMessage();
		
	
		
		moreAdapter = new MoreMediaAdapter(MoreMeidaActivity.this,
				DefindConstant.SORT);
		moreGrid.setAdapter(moreAdapter);
		
		

		moreGrid.setOnItemClickListener(new OnItemClickListener() {
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
				intent.putExtra("fenlei", "fenlei");
				intent.putExtra("position", position);
				intent.setClass(ctx, ShowAllMediaActivity.class);
				startActivity(intent);

			}
		});

		moreGrid.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				
				//滚动时
				
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					m.sendEmptyMessage(hit);
					break;
				
				// 当不滚动时

				case OnScrollListener.SCROLL_STATE_IDLE:

					if (moreGrid.getFirstVisiblePosition() > (moreGrid
							.getCount() / 3)&&!isEnd&&!isLoad) {
						
						isLoad=true;
						m.sendEmptyMessage(getMore);

						

					}

					if (moreGrid.getLastVisiblePosition() == (moreGrid
							.getCount() - 1)&&!isEnd&&!isLoad)

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

	}

	void initTitleBar() {
		View title = findViewById(R.id.newlistbar);

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

	private void init() {

		Intent intent = this.getIntent();
		keySelectedPosition = intent.getExtras().getInt("pos");
		topimg = (ImageView) findViewById(R.id.more_top_img);
		toptext = (TextView) findViewById(R.id.more_top_text);
		bottext = (TextView) findViewById(R.id.more_bottom_text);
		img = intent.getExtras().getString("image");
		name = intent.getExtras().getString("name");
		content = intent.getExtras().getString("content");

		Log.i("showDia", img);
		
		if (img.equals("")) {
			topimg.setImageDrawable(this.getResources()
					.getDrawable(R.drawable.ic_launcher));
		} else {
			imageLoader.displayImage(img, topimg, options);

		}
		
		
		toptext.setText(name);
		bottext.setText(content);

	}

	private void updateMessage() {

		new Thread(new Runnable() {
			@SuppressLint("UseSparseArrays")
			@Override
			public void run() {
				try {
					

					
			//		grf.getValues(page,name,null);

				
					m.sendEmptyMessage(nitifyData);

				} catch (Exception e) {

					m.sendEmptyMessage(notMore);
				}
			}
		}).start();

	}

	private int nitifyData =0x123;
	private int getMore = 0x1234;
	private int hit = 0x12345;
	private int notMore = 0x123456;

	class mHander extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == nitifyData) {

				
				moreAdapter.notifyDataSetChanged();
			
				isLoad =false;
				return;

			}

			else if (msg.what == getMore) {
				    page++;
			        updateMessage();
			        return;
			       

			}
			
			else if(msg.what == hit){
				    topimg.setVisibility(View.GONE);
					toptext.setVisibility(View.GONE);
					bottext.setVisibility(View.GONE);
					return;
				
			}
			
			else if(msg.what == notMore){
				
			
				if(!isEnd){
				Toast.makeText(ctx, "没有更多了", Toast.LENGTH_SHORT).show();
				}
				isEnd=true;
				
				return;
			
		}
		
			
		}

	}

}

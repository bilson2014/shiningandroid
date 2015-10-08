package com.panfeng.shining.activity.s2nd;

import com.panfeng.shining.adapter.MyAdapter;
import com.panfeng.shinning.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class S2ndShiningAnswer extends Activity {

	private Button btn;
	private ImageView imgf,closelist;
	private ExpandableListView expandableListView;
	boolean videoVisible = true;
	boolean  listVisible = true;
 	private RelativeLayout re;
	private View view;
	private TextView word;
    
	LayoutInflater inflater;

	// private String[] groups = new String[]{"","",""};
	// private String[][] buddy = new String[][]{
	// {"夏侯惇", "甄姬", "许褚", "郭嘉", "司马懿", "杨修"},
	// { "马超", "张飞", "刘备", "诸葛亮", "黄月英", "赵云"},
	// {"吕蒙", "陆逊", "孙权", "周瑜", "孙尚香"},};
	int[] logos = new int[] { R.drawable.answer0, R.drawable.answer1,
			R.drawable.answer2, R.drawable.answer3, R.drawable.answer8 , R.drawable.answer6 };
	// 子视图图片
	public int[][] generallogos = new int[][] {

	{ R.drawable.que0 }, { R.drawable.que1 }, { R.drawable.que2 },
			{ R.drawable.que3 }, { R.drawable.que4 }, {R.drawable.que5},

	};
	
	public int[][] secondallogos = new int[][] {

	{ 0 }, { 0 }, {  R.drawable.que11 },
			{ 0 }, { 0 }, { 0 },

	};
	
	
	
	private MyAdapter myAdapter;
	private VideoView vv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainss);
		
		
		initTitleBar();

//		View title = findViewById(R.id.video_title);
//		ImageView back = (ImageView) title.findViewById(R.id.back);
//
//		word = (TextView) title.findViewById(R.id.txt);
//		closelist = (ImageView)findViewById(R.id.task);
//		word.setText("常见问题");
//		
		
//		vv = (VideoView) findViewById(R.id.video_set);
//		re = (RelativeLayout) findViewById(R.id.reVideo);
//		btn = (Button) findViewById(R.id.play_video);
//		imgf = (ImageView) findViewById(R.id.video_find);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		expandableListView.setGroupIndicator(null);
		expandableListView.setDivider(null);
//		btn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				playvideo();
//			}
//		});
//
//		imgf.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showVideo();
//			}
//		});
//
//		re.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				pauseVideo();
//			}
//		});

//		back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				back();
//			}
//		});
		
//		closelist.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				closelist();
//			}
//		});
		
		
		
		
		
		
		
		
		

//		Uri uri = Uri
//				.parse("http://182.92.154.162:8080/shiningCenterService/user_image/shining_guide.mp4 ");
//		vv.setVideoURI(uri);

		myAdapter = new MyAdapter(this, logos, generallogos,secondallogos);
		expandableListView.setAdapter(myAdapter);

		expandableListView
				.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {

					}
				});
		expandableListView
				.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {

					}
				});
		expandableListView
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {

						return false;
					}
				});
	}

	public void playvideo() {
		vv.start();
		vv.requestFocus();
		btn.setVisibility(View.GONE);

	}

	public void showVideo() {

		if (videoVisible) {
			vv.pause();
			vv.setVisibility(View.GONE);
			btn.setVisibility(View.GONE);
			videoVisible = false;
		} else {
			vv.setVisibility(View.VISIBLE);
			btn.setVisibility(View.VISIBLE);
			videoVisible = true;
		}

	}
	
	
	void initTitleBar() {
		View title = findViewById(R.id.newlistbar);
	
		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
		bartxt.setText("常见问题");
		
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

	public void pauseVideo() {

		if (vv.isPlaying()) {
			vv.pause();
			btn.setVisibility(View.VISIBLE);

		}

	}
	
	public void closelist() {

	
		
			if(listVisible==true){
			expandableListView.setVisibility(View.GONE);
			listVisible =false;
			}
			else{
				expandableListView.setVisibility(View.VISIBLE);
				listVisible =true;
				
			

		}

	}
	
	
	

	public void back() {

		finish();

	}

}

package com.panfeng.shining.activity.newactivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import tyu.common.net.TyuDefine;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.panfeng.shining.adapter.SearchListAdapter;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.entity.UserInfoEntity;
import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.tools.NetTools;
import com.panfeng.shining.tools.UserControl;
import com.panfeng.shining.widgets.TyuWaitingBar;
import com.panfeng.shinning.R;

public class SearchActivity extends Activity implements OnClickListener {

	/*
	 * 排行版
	 */

	// 数据源
	JSONArray jsonarray;
	String jsonString;
	int page = 0;

	mHander m = new mHander();
	VideoEntity ve;
	Context ctx = SearchActivity.this;
	private SearchListAdapter searchAdapter;
	private ListView listview;
	UserInfoEntity ue = new UserInfoEntity();
	UserInfoEntity uec;
	RelativeLayout neterror;
	EditText et;
	ImageView search;
	Dialog dlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		initTitleBar();

		neterror = (RelativeLayout) findViewById(R.id.re_error);
		et = (EditText) findViewById(R.id.search_name);
		
	
	

		search = (ImageView) findViewById(R.id.search_start);
		search.setOnClickListener(this);

		// 无网提示
		if (!NetTools.checkNetwork(ctx))
			neterror.setVisibility(View.VISIBLE);

		listview = (ListView) findViewById(R.id.rank_list);

		initTitleBar();
		// updateMessage();

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ve = (VideoEntity) view.getTag(R.id.RankListAdapter);
				VideoEntityLu videoEntityLu=new VideoEntityLu();
				videoEntityLu.setVideoID(Integer.parseInt(ve.getVideo_id()));
				videoEntityLu.setVideoContext(ve.getVideo_content());
				videoEntityLu.setVideoAuthor(ve.getVideo_author());
				videoEntityLu.setVideoFileName(ve.getVideo_fIleName());
				videoEntityLu.setVideoName(ve.getVideo_name());
				Intent intent=new Intent();
				intent.putExtra("obj", videoEntityLu);
				intent.setClass(SearchActivity.this, ShowAllMediaActivity.class);
				startActivity(intent);
			}
		});
		
		
		et.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        EditText _v=(EditText)v;
		        if (!hasFocus) {// 失去焦点
		            _v.setHint(_v.getTag().toString());
		        } else {
		            String hint=_v.getHint().toString();
		            _v.setTag(hint);
		            _v.setHint("");
		        }
		    }
		});
		
		et.setOnTouchListener(new View.OnTouchListener() {
			//按住和松开的标识
			
			public boolean onTouch(View v, MotionEvent event) {
				   et.setFocusable(true);
				    et.setFocusableInTouchMode(true);
				    et.requestFocus();
				return false;
			}
		});
		
		
		
		
		

	}



	private void updateMessage() {

		new Thread(new Runnable() {
			@SuppressLint("UseSparseArrays")
			@Override
			public void run() {
				try {
				
					
					
					jsonString = TyuShinningData.getInstance()
							.getMediaByPage_find(page, 10,
									et.getText().toString());

					
				
					jsonarray = new JSONArray(jsonString);
					VideoEntity ve;

					List<VideoEntity> listx = new ArrayList<VideoEntity>();
					
					if (jsonString == null || jsonString.equals("")||jsonarray.length()<1){
						m.sendEmptyMessage(1234);
						return;
						
					}

					SimpleDateFormat sdf;
					Log.i("map", "RESULT=" + jsonarray.length());
					
					
					
					for (int i = 0; i < jsonarray.length(); i++) {
						sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
						ve = new VideoEntity();
						ve.setVideo_name(jsonarray.getJSONObject(i).getString(
								"mb_name"));
						ve.setVideo_fIleName(jsonarray.getJSONObject(i)
								.getString("mb_video_name"));
						
						ve.setVideo_content(jsonarray.getJSONObject(i)
								.getString("mb_content"));
						ve.setVideo_author(jsonarray.getJSONObject(i)
								.getString("mb_author"));
						ve.setVideo_id(jsonarray.getJSONObject(i).getInt("mb_id")+ "");
						Date d = sdf.parse(jsonarray.getJSONObject(i)
								.getString("mb_upload_time"));
						ve.setVideo_date(d);


						DefindConstant.FINDING.add(ve);

					}

					searchAdapter = new SearchListAdapter(SearchActivity.this,
							DefindConstant.FINDING);
					m.sendEmptyMessage(123);

				} catch (Exception e) {
					
					m.sendEmptyMessage(1234);
				
					
				}
			}
		}).start();

	}

	class mHander extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 123) {
				listview.setAdapter(searchAdapter);
				searchAdapter.notifyDataSetChanged();
				et.setText("");
				dlg.dismiss();
                   return;
                   
			}
			
			else if (msg.what == 1234){
				dlg.dismiss();
				Toast.makeText(ctx, "没有查找到内容", Toast.LENGTH_SHORT).show();
				
				return;
				
			}
			
		}
		

	}
	
	void initTitleBar() {
		View title = findViewById(R.id.rank_bar);

		ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
		TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
		bartxt.setText("搜索");

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
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.search_start:
			dlg = TyuWaitingBar.build(ctx);
			dlg.show();

			DefindConstant.FINDING.clear();

			
			
			try {
				
			
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			inputMethodManager.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

			}
			catch (Exception e) {
				// TODO: handle exception
			}

			updateMessage();
		

			break;

		}

	}

}

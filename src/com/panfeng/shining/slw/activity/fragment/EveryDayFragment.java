package com.panfeng.shining.slw.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.DbUtils;


import com.panfeng.shining.activity.newactivity.SearchActivity;

import com.panfeng.shining.adapter.EveryDayListAdapter;
import com.panfeng.shining.camera.MySurface;
import com.panfeng.shining.data.TyuShinningData;
import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shining.slw.adapter.VidoeListAdapter;

import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shining.slw.utils.DefindConstant;
import com.panfeng.shining.tools.NetTools;
import com.panfeng.shinning.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

public class EveryDayFragment extends Fragment implements IXListViewListener {
   
   
    private VidoeListAdapter adapter;
    // private String tag = "dawn";

    private DbUtils dbUtils = null;

    public List<VideoEntity> listadapter = null;
    private Intent it;
	JSONArray jsonarray;
	String jsonString;
	public static int page = -1;
	private boolean hasDate = true;// 有无数据
    mHander everydayUpdateHandler = new mHander();
	private EveryDayListAdapter talkAdapter;
	private XListView listview;
	VideoEntity ve;
	private Context ctx ;
	View mLayout;
	RelativeLayout neterror;
	 View view;
	 
	 
	 public void onAttach(Activity activity) {
		 ctx= activity;
		 
	        super.onAttach(activity);
	    }
	 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        everydayUpdateHandler = new mHander();
      
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	
    	
        view = inflater.inflate(R.layout.everyday, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SuppressLint("WrongViewCast") public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    
            listview = (XListView)view.findViewById(R.id.rank_list);
    		listview.setItemsCanFocus(true);
            neterror = (RelativeLayout) view.findViewById(R.id.re_error);

    		initTitleBar();

    		onLoadMore();

    		if (!NetTools.checkNetwork(ctx))
    			neterror.setVisibility(View.VISIBLE);

    		listview.setXListViewListener(this);
    		listview.setPullRefreshEnable(true);
    		listview.setPullLoadEnable(true);
    		listview.setOnScrollListener((OnScrollListener) talkAdapter);

    		listview.setOnItemClickListener(new OnItemClickListener() {

    			@Override
    			public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {

    				Intent intent = new Intent(ctx,
    						MySurface.class);

    				startActivity(intent);

    			}
    		});
      
    }





void initTitleBar() {
	View title = view.findViewById(R.id.every_bar);
	ImageView barimg = (ImageView) title.findViewById(R.id.bar_img);
	TextView bartxt = (TextView) title.findViewById(R.id.bar_text);
    barimg.setBackgroundResource(R.drawable.everyday);
	bartxt.setText("每日鲜");
	ImageView barbot = (ImageView) title.findViewById(R.id.bar_set);
	barbot.setBackgroundResource(R.drawable.findmore);
	barbot.setVisibility(View.VISIBLE);
	
	barbot.setOnClickListener(new OnClickListener() {
		
					@Override
					public void onClick(View v) {
					 Intent intent= new Intent(ctx,SearchActivity.class); 
		             startActivity(intent);
		
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
						.getMediaByPage_2s(page,12, 0, false,null);

			
				jsonarray = new JSONArray(jsonString);
				if (jsonarray.length() <= 0)
					throw new NullPointerException("fanhui shuju weiling");
				Log.i("video", "json+dsasdadwdasda=" + jsonarray + "");

				//DefindConstant.EVERYDAY_NEW = new ArrayList<VideoEntity>();

				List<VideoEntityLu> listx = new ArrayList<VideoEntityLu>();
				// ArrayList<VideoEntity> veList = new
				// ArrayList<VideoEntity>();
				// Map<Integer, List<VideoEntity>> sortMap = new
				// LinkedHashMap<Integer, List<VideoEntity>>();
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
					ve.setVideo_id(""
							+ jsonarray.getJSONObject(i).getInt("mb_id"));
					Date d = sdf.parse(jsonarray.getJSONObject(i)
							.getString("mb_upload_time"));
					ve.setVideo_date(d);

					// listx.add(ve);
					//DefindConstant.EVERYDAY_NEW.add(ve);

					// listx.add(ve);
					// DefindConstant.EVERYDAY_NEW.add(ve);

					// if (sortMap.containsKey(d.getDay())) {
					// sortMap.get(d.getDay()).add(ve);
					// } else {
					// List<VideoEntity> l = new
					// ArrayList<VideoEntity>();
					// l.add(ve);
					// sortMap.put(d.getDay(), l);
					// }
				}

				Log.i("slw",
						"RESULT=" + listx.toString() + "slt" + listx.size());
				// Set<Integer> s = sortMap.keySet();
				//
				// Iterator<Integer> iterator = s.iterator();
				//
				// while (iterator.hasNext()) {
				// Integer i = iterator.next();
				// List<VideoEntity> l = sortMap.get(i);
				// listx.add(l);
				// break;
				// }
				if (DefindConstant.EVERYDAY_NEW.size() < 5)
					throw new NullPointerException("不足数量");
				//talkAdapter = new EveryDayListAdapter(
				//		ctx, DefindConstant.EVERYDAY_NEW);

				// success
				everydayUpdateHandler.sendEmptyMessage(1);

			}

			catch (Exception e) {
				// e.printStackTrace();

				everydayUpdateHandler.sendEmptyMessage(0);

			}
		}
	}).start();

}


    @SuppressLint("HandlerLeak")
	class mHander extends Handler {

		boolean loadMore = true;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				listview.setAdapter(talkAdapter);
				talkAdapter.notifyDataSetChanged();
				Log.i("luslw", "no more");
				return;
			} else if (msg.what == 0) {
				if (loadMore) {
					page--;
					onLoadEnd();
					Toast.makeText(ctx, "没有更多啦", Toast.LENGTH_SHORT).show();
					return;
				} else
					page++;
				return;

			}

		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		everydayUpdateHandler.loadMore = false;
		if (page <= 0) {
			Log.d("video", "no");
			Toast.makeText(ctx, "已经到顶啦", Toast.LENGTH_SHORT).show();
			onLoadEnd();
			return;
		} else {
			page--;
			updateMessage();
			onLoadEnd();
			return;
		}

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		everydayUpdateHandler.loadMore = true;
		page++;
		updateMessage();

	}

	private void onLoadEnd() {
		// TODO Auto-generated method stub
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");

	}
}

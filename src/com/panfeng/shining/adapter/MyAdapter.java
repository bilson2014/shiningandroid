package com.panfeng.shining.adapter;


import com.panfeng.shinning.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

public class MyAdapter extends BaseExpandableListAdapter{

//	private String[] mGroups;
//	private String[][] mBuddy;
	private Context mContext;
	private LayoutInflater inflater;
	private int[] log;
	private int[][] glogo;
	private int[][] slogo;
	VideoView vv; 
	  FrameLayout fl;
	
	public MyAdapter(Context context,int[]logos,int[][] generallogos,int[][] secondlogos) {
		mContext = context;
		inflater = LayoutInflater.from(context);
//		mGroups = groups;
//		mBuddy = buddy;
		log = logos;
		glogo = generallogos;
		slogo = secondlogos;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return glogo[groupPosition][childPosition];
	}
	
	public int getglogo(int groupPosition , int childPosition) {
		return glogo[groupPosition][childPosition];
	}
	
	public int getslogo(int groupPosition , int childPosition) {
		return slogo[groupPosition][childPosition];
	}


	@Override
	public long getChildId(int groupPosition, int childPosition) {
		
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		Log.i("lutao", "2s//"+getglogo(groupPosition, childPosition));
		Log.i("lutao", "3s//"+getglogo(groupPosition, childPosition));
		
		
		
		convertView = inflater.inflate(R.layout.listview_group_child_item, null);
//		TextView nickTextView = (TextView) convertView.findViewById(R.id.listview_group_child_nick);
//		nickTextView.setText(getChild(groupPosition, childPosition).toString());
		ImageView View = (ImageView) convertView.findViewById(R.id.listview_group_child_avatar);
		View.setBackgroundResource(getglogo(groupPosition, childPosition));
		
		ImageView ViewName = (ImageView) convertView.findViewById(R.id.listview_group_child_second_avatar);
		ViewName.setBackgroundResource(getslogo(groupPosition, childPosition));
		

		
		
		
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return glogo[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return log[groupPosition];
	}
	
	public int getLog(int groupPosition) {
		return log[groupPosition];
	}
	
	
	
	
	

	@Override
	public int getGroupCount() {
		return log.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		

		  
		       
		       
		     
	
			
		
		
		
		convertView = inflater.inflate(R.layout.listview_group_item, null);
//		TextView groupName = (TextView) convertView.findViewById(R.id.listview_group_name);
//		groupName.setText(getGroup(groupPosition).toString());
		
		
		
		ImageView imageName = (ImageView) convertView.findViewById(R.id.listview_group_image);
		imageName.setBackgroundResource(getLog(groupPosition));
		
		
		
		
		
		
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
 
	  

       
	
	

}

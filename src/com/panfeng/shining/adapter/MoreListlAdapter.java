package com.panfeng.shining.adapter;

import java.util.List;
import java.util.Random;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.entity.TypeEntity;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shinning.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import android.content.Context;
import android.graphics.Color;

/**
 * 
 * 
 * @author Administrator
 * @param <diyLocalMap>
 * 
 */

public class MoreListlAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context mContext;
	private List<TypeEntity> mTalks;
	DisplayImageOptions options = TyuApp.getCommonConfig();
	ImageLoader imageLoader = ImageLoader.getInstance();

	public MoreListlAdapter(Context context, List<TypeEntity> talks) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		mTalks = talks;
	}

	@Override
	public int getCount() {
		return mTalks == null ? 0 : mTalks.size();
	}

	@Override
	public Object getItem(int position) {
		return mTalks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.select_more, parent, false);
			holder.more_img = (ImageView) view.findViewById(R.id.more_image);
			holder.more_text = (TextView) view.findViewById(R.id.more_type);
			holder.more_content = (TextView) view.findViewById(R.id.more_content);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		try {

			TypeEntity talk = mTalks.get(position);
			
			
			
			if (talk.getTypeSmallImage().equals("")) {
				holder.more_img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));
			} else {
				imageLoader.displayImage(talk.getSmallImageUrl(), holder.more_img, options);
			}
			
			holder.more_text.setText(talk.getTypeName());
			holder.more_content.setText(talk.getTypeContents());
			
			
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	class ViewHolder {
		ImageView more_img;
		TextView more_text;
		TextView more_content;

	}
}
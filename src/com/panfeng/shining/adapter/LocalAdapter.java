package com.panfeng.shining.adapter;

import java.util.List;
import java.util.Random;

import com.panfeng.shinning.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.panfeng.shining.activity.newactivity.LocalVideoActivity.diyLocalMap;

import android.content.Context;
import android.graphics.Color;

/**
 * 
 * 
 * @author Administrator
 * @param <diyLocalMap>
 * 
 */

public class LocalAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context mContext;
	private List<diyLocalMap> mTalks;

	public LocalAdapter(Context context, List<diyLocalMap> talks) {
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
			view = inflater.inflate(R.layout.video_item, parent, false);
			holder.video_img = (ImageView) view.findViewById(R.id.thumb_image);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		try {

			holder.video_img.setImageBitmap(mTalks.get(
					(int) getItemId(position)).getPath());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	class ViewHolder {
		ImageView video_img;

	}
}
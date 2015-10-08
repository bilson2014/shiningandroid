package com.panfeng.shining.adapter;


import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;


import tyu.common.net.TyuDefine;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import com.panfeng.shining.TyuApp;

import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shinning.R;

import android.content.Context;


/**
 * 排行版适配器
 * 
 * @author Administrator
 * 
 */

public class SearchListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context mContext;
	DisplayImageOptions options = TyuApp.getCommonConfig();
	private List<VideoEntity> mTalks;
	ImageLoader imageLoader = ImageLoader.getInstance();

	public SearchListAdapter(Context context, List<VideoEntity> talks) {
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
			view = inflater.inflate(R.layout.search_item, parent, false);
			holder.rank_count = (TextView) view.findViewById(R.id.rank_count);

			holder.rank_videoname = (TextView) view
					.findViewById(R.id.rank_videoname);
			holder.rank_author = (TextView) view.findViewById(R.id.rank_author);
			holder.rank_content = (TextView) view
					.findViewById(R.id.rank_content);
			holder.rank_look = (TextView) view.findViewById(R.id.rank_look);
			holder.rank_set = (TextView) view.findViewById(R.id.rank_set);
			holder.rank_img = (ImageView) view.findViewById(R.id.rank_img);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		try {
			VideoEntity talk = mTalks.get(position);

			view.setTag(R.id.RankListAdapter, talk);

			String videoname = talk.getVideo_fIleName();
			String imgtop = TyuDefine.HOST + "media_base/"
					+ videoname.substring(0, videoname.lastIndexOf('.'))
					+ ".jpg";

			if (imgtop.equals("")) {
				holder.rank_img.setImageDrawable(mContext.getResources()
						.getDrawable(R.drawable.ic_launcher));
			} else {
				imageLoader.displayImage(imgtop, holder.rank_img, options);
			}

			Log.i("add", "url=" + imgtop);

			holder.rank_videoname.setText(talk.getVideo_name());
			holder.rank_author.setText(talk.getVideo_author());

			Log.i("dirlist", "author" + talk.getVideo_author());

			
			
			Log.i("sslltt", "pos+"+getItemId(position));
			
			if (getItemId(position) > 8) {
				holder.rank_count.setText((getItemId(position))+1+"");
				Log.i("dblutao", "top");
			} else {
				holder.rank_count.setText("0" + (getItemId(position)+1));
				Log.i("dblutao", "bottom");
			}
			holder.rank_content.setText(talk.getVideo_content());
			// holder.rank_look.setText(talk.getVideoLoook());
			// holder.rank_set.setText(talk.getVideoSeting());
			// holder.rank_save.setText(talk.getVideoSave());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	class ViewHolder {
		TextView rank_count;
		TextView rank_videoname;
		TextView rank_author;
		TextView rank_content;
		TextView rank_look;
		TextView rank_set;
		TextView rank_save;
		ImageView rank_img;

	}
}
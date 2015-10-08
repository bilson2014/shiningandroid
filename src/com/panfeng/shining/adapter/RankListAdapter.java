package com.panfeng.shining.adapter;

import java.util.List;
import java.util.Random;

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
import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shinning.R;

import android.content.Context;
import android.graphics.Color;

/**
 * 排行版适配器
 * 
 * @author Administrator
 * 
 */

public class RankListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context mContext;
	DisplayImageOptions options = TyuApp.getCommonConfig();
	ImageLoader imageLoader = ImageLoader.getInstance();
	private List<VideoEntityLu> mTalks;

	public RankListAdapter(Context context, List<VideoEntityLu> talks) {
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
			view = inflater.inflate(R.layout.rank_item, parent, false);
			holder.rank_count = (TextView) view.findViewById(R.id.rank_count);

			holder.rank_videoname = (TextView) view
					.findViewById(R.id.rank_videoname);
			holder.rank_author = (TextView) view.findViewById(R.id.rank_author);
			holder.rank_content = (TextView) view
					.findViewById(R.id.rank_content);
			holder.rank_look = (TextView) view.findViewById(R.id.rank_play);
			holder.rank_set = (TextView) view.findViewById(R.id.rank_set);
			holder.rank_img = (ImageView) view.findViewById(R.id.rank_img);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		try {
			VideoEntityLu talk = mTalks.get(position);

			view.setTag(R.id.RankListAdapter, talk);

			String videoname = talk.getVideoFileName();
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

			holder.rank_videoname.setText(talk.getVideoName());
			holder.rank_author.setText(talk.getVideoAuthor());
			
			
			Random rd = new Random();
			
			int play =Integer.parseInt(talk.getVideoLoook())*10+rd.nextInt(10);
			int set =Integer.parseInt(talk.getVideoSeting());
			
		
	
			
			holder.rank_look.setText(play+"");
			holder.rank_set.setText(set+"");

			if(getItemId(position) < 3){
				holder.rank_count.setText("0" + (getItemId(position)+1));
				holder.rank_count.setTextColor(Color.parseColor("#FE5453"));
			}
			else if (getItemId(position) > 8) {
				holder.rank_count.setText((getItemId(position))+1+"");
				holder.rank_count.setTextColor(Color.parseColor("#5a5f5e"));
				
			} else {
				holder.rank_count.setText("0" + (getItemId(position)+1));
				holder.rank_count.setTextColor(Color.parseColor("#5a5f5e"));
			}
			//holder.rank_content.setText(talk.getVideo_content());
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
	
		ImageView rank_img;

	}
}
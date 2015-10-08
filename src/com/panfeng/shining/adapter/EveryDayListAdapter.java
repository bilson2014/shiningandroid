package com.panfeng.shining.adapter;

import java.util.List;
import java.util.Random;

import tyu.common.net.TyuDefine;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.panfeng.shining.TyuApp;
import com.panfeng.shining.slw.entity.VideoEntity;
import com.panfeng.shinning.R;

/**
 * 每日鲜适配器
 * 
 * @author Administrator
 * 
 */

public class EveryDayListAdapter extends BaseAdapter  {
	private LayoutInflater inflater;
	private Context mContext;

	private List<VideoEntity> mTalks;
	ImageLoadingListener simpleImageListener = new TyuApp.TyuImageLoadingListener();
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = TyuApp.getCommonConfig();
	String videoname1, videoname2, videoname3, videoname4, videoname5,videoname6;
	int videonameID1, videonameID2, videonameID3, videonameID4, videonameID5,videonameID6;

	public EveryDayListAdapter(Context context, List<VideoEntity> listx) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		mTalks = listx;


	 
	    
	    
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.more_media_item, parent, false);
			holder.more_media_look = (TextView) view
					.findViewById(R.id.more_media_look);
			holder.more_media_name = (TextView) view
					.findViewById(R.id.more_media_name);
			holder.moremedia_img = (ImageView) view
					.findViewById(R.id.moremedia_img);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		try {
			
			VideoEntity talk = mTalks.get(position);

			String videoname = talk.getVideo_fIleName();
			String imgtop = TyuDefine.HOST + "media_base/"
					+ videoname.substring(0, videoname.lastIndexOf('.'))
					+ ".jpg";

			if (imgtop.equals("")) {
				holder.moremedia_img.setImageDrawable(mContext.getResources()
						.getDrawable(R.drawable.ic_launcher));
			} else {
				imageLoader.displayImage(imgtop, holder.moremedia_img, options);
			}

			Log.i("showDia", talk.getVideo_name());
			view.setTag(R.id.MoreMediaAdapter, talk);
			

			holder.more_media_name.setText(talk.getVideo_name());

			
			Random rd = new Random();
			int play =Integer.parseInt(talk.getVideo_plays())*10+rd.nextInt(10);
			

  			holder.more_media_look.setText(play+"");
		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}
	
	/*
	 * 跳转
	 */



	class ViewHolder {
		TextView more_media_look;
		TextView more_media_name;
		ImageView moremedia_img;

	}
}
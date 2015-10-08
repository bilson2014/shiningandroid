package com.panfeng.shining.dawn.viewholder;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.views.list.ViewHolderBase;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.panfeng.shining.entity.VideoEntityLu;
import com.panfeng.shinning.R;

public class MeiRiXianViewHolder extends ViewHolderBase<VideoEntityLu> {
	private CubeImageView mImageView;

	private TextView mediaName;
	private TextView lookCount;
	private ImageLoader mImageLoader;

	public MeiRiXianViewHolder(ImageLoader imageLoader) {
		mImageLoader = imageLoader;
	}

	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater layoutInflater) {
		View view = layoutInflater.inflate(R.layout.more_media_item, null);
		mImageView = (CubeImageView) view.findViewById(R.id.moremedia_img);
		mediaName = (TextView) view.findViewById(R.id.more_media_name);
		lookCount = (TextView) view.findViewById(R.id.more_media_look);
		return view;
	}

	@Override
	public void showData(int position, VideoEntityLu itemData) {
		
		mImageView.loadImage(mImageLoader, itemData.getImageUrl(),
				ImageSize.sGridImageReuseInfo);
		mediaName.setText(itemData.getVideoName());
		lookCount.setText(itemData.getVideoLoook());
	}

}

package com.panfeng.shining.dawn.viewholder;

/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.panfeng.shining.activity.newactivity.MoreMediaListActivity;
import com.panfeng.shining.activity.newactivity.MoreMeidaActivityV2;
import com.panfeng.shining.dawn.head.ListUtils;
import com.panfeng.shining.dawn.head.RecyclingPagerAdapter;
import com.panfeng.shining.entity.TypeEntity;

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {
	private ImageLoader mImageLoader;
	private Context context;
	private List<TypeEntity> imageIdList;

	private int size;
	private boolean isInfiniteLoop;

	public ImagePagerAdapter(Context context, List<TypeEntity> imageIdList,
			ImageLoader imageLoader) {
		this.context = context;
		this.imageIdList = imageIdList;
		this.size = ListUtils.getSize(imageIdList);
		isInfiniteLoop = false;
		this.mImageLoader=imageLoader;
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils
				.getSize(imageIdList);
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = holder.imageView = new CubeImageView(context);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// holder.imageView
		holder.imageView.loadImage(mImageLoader, imageIdList.get(getPosition(position)).getBigImageUrl());
		holder.imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						MoreMeidaActivityV2.class);
				intent.putExtra("obj", imageIdList.get(getPosition(position)));
				context.startActivity(intent);
			}
		});
		return view;
	}

	private static class ViewHolder {

		CubeImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}
}

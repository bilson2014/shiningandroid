package com.panfeng.shining.slw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;



import java.util.List;
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

	/**
	 * 数据源
	 */
	protected List<T> datas = null;

	/**
	 * 上下文对象
	 */
	protected Context context = null;

	/**
	 * item布局文件的资源ID
	 */
	protected int itemLayoutResId = 0;

	public BaseAdapter(final Context context,final List<T> datas,final int itemLayoutResId) {
		this.context = context;
		this.datas = datas;
		this.itemLayoutResId = itemLayoutResId;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public T getItem(final int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position,final View convertView,final ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.getViewHolder(context,
				itemLayoutResId, position, convertView, parent);

		convert(viewHolder, getItem(position));
		//viewHolder.getConvertView().setTag(R.id.tag_video_entity,getItem(position));

		return viewHolder.getConvertView();
	}

	public abstract void convert(final ViewHolder viewHolder,final T item);

}

package com.panfeng.shining.slw.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
/**
 * 音乐分类记录表
 * @author dawn
 *
 */
@Table(name = "AudioSort_Table")
public class AudioSortEntity extends EntityBase {

	@Column(column = "Sort_id")
	private String Sort_id;

	@Column(column = "Sort_name")
	private String Sort_name;

	@Column(column = "Sort_imagepath")
	private String Sort_imagepath;

	@Column(column = "Sort_imagemd5")
	private String Sort_imagemd5;

	@Column(column = "Sort_weight")
	private String Sort_weight;

	public AudioSortEntity() {
		super();
	}

	public AudioSortEntity(String sort_id, String sort_name,
			String sort_imagepath, String sort_imagemd5, String sort_weight) {
		super();
		Sort_id = sort_id;
		Sort_name = sort_name;
		Sort_imagepath = sort_imagepath;
		Sort_imagemd5 = sort_imagemd5;
		Sort_weight = sort_weight;
	}

	public String getSort_id() {
		return Sort_id;
	}

	public void setSort_id(String sort_id) {
		Sort_id = sort_id;
	}

	public String getSort_name() {
		return Sort_name;
	}

	public void setSort_name(String sort_name) {
		Sort_name = sort_name;
	}

	public String getSort_imagepath() {
		return Sort_imagepath;
	}

	public void setSort_imagepath(String sort_imagepath) {
		Sort_imagepath = sort_imagepath;
	}

	public String getSort_imagemd5() {
		return Sort_imagemd5;
	}

	public void setSort_imagemd5(String sort_imagemd5) {
		Sort_imagemd5 = sort_imagemd5;
	}

	public String getSort_weight() {
		return Sort_weight;
	}

	public void setSort_weight(String sort_weight) {
		Sort_weight = sort_weight;
	}

}

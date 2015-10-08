package com.panfeng.shining.slw.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * 核心数据结构，储存视频信息
 * 
 * @author dawn
 * 
 */
// CREATE UNIQUE INDEX index_name ON
// Video_Table(Video_id)创建唯一索引
// Video_name,Video_id,Video_fIleName
@Table(name = "Video_Table")
public class VideoEntity extends EntityBase implements Serializable {

	private static final long serialVersionUID =11L;
	// 服务器存储的视频id
	@Column(column = "Video_id")
	private String Video_id;

	// 服务器端存储的视频名称
	@Column(column = "Video_name")
	private String Video_name;

	// 服务器端储存视频文件名
	@Column(column = "Video_fIleName")
	private String Video_fIleName;

	// 服务器端视频介绍
	@Column(column = "Video_content")
	private String Video_content;

	// 储存视频分类信息
	@Column(column = "Video_key")
	private String Video_key;

	// 视频权重
	@Column(column = "Video_weight")
	private String Video_weight;

	// 视频作者
	@Column(column = "Video_author")
	private String Video_author;

	// 视频是否被设置
	@Column(column = "Video_collection")
	private String Video_collection;

	// Video_plays
	@Column(column = "Video_plays")
	private String Video_plays;

	// Video_md5
	@Column(column = "Video_md5")
	private String Video_md5;

	// Video_state
	@Column(column = "Video_state")
	private String Video_state;
	
	@Column(column = "Video_date")
	private Date Video_date;

	public VideoEntity() {
		super();
	}

	public VideoEntity(String video_id, String video_name,
			String video_fIleName, String video_content, String video_key,
			String video_weight, String video_author, String video_collection,
			String video_plays, String video_md5, String video_state, Date video_date) {
		super();
		Video_id = video_id;
		Video_name = video_name;
		Video_fIleName = video_fIleName;
		Video_content = video_content;
		Video_key = video_key;
		Video_weight = video_weight;
		Video_author = video_author;
		Video_collection = video_collection;
		Video_plays = video_plays;
		Video_md5 = video_md5;
		Video_state = video_state;
		Video_date = video_date;
	}

	public String getVideo_id() {
		return Video_id;
	}

	public void setVideo_id(String video_id) {
		Video_id = video_id;
	}

	public String getVideo_name() {
		return Video_name;
	}

	public void setVideo_name(String video_name) {
		Video_name = video_name;
	}

	public String getVideo_fIleName() {
		return Video_fIleName;
	}

	public void setVideo_fIleName(String video_fIleName) {
		Video_fIleName = video_fIleName;
	}

	public String getVideo_content() {
		return Video_content;
	}

	public void setVideo_content(String video_content) {
		Video_content = video_content;
	}

	public String getVideo_key() {
		return Video_key;
	}

	public void setVideo_key(String video_key) {
		Video_key = video_key;
	}

	public String getVideo_weight() {
		return Video_weight;
	}

	public void setVideo_weight(String video_weight) {
		Video_weight = video_weight;
	}

	public String getVideo_author() {
		return Video_author;
	}

	public void setVideo_author(String video_author) {
		Video_author = video_author;
	}

	public String getVideo_collection() {
		return Video_collection;
	}

	public void setVideo_collection(String video_collection) {
		Video_collection = video_collection;
	}

	public String getVideo_plays() {
		return Video_plays;
	}

	public void setVideo_plays(String video_plays) {
		Video_plays = video_plays;
	}

	public String getVideo_md5() {
		return Video_md5;
	}

	public void setVideo_md5(String video_md5) {
		Video_md5 = video_md5;
	}

	public String getVideo_state() {
		return Video_state;
	}

	public void setVideo_state(String video_state) {
		Video_state = video_state;
	}
	
	public Date getVideo_date() {
		return Video_date;
	}

	public void setVideo_date(Date video_date) {
		Video_date = video_date;
	}

	

}

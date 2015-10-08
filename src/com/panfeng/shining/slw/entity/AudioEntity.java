package com.panfeng.shining.slw.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
/**
 * 音频记录表
 * @author dawn
 *
 */
@Table(name = "Audio_Table")
public class AudioEntity extends EntityBase {

	// 服务器音频id
	@Column(column = "Audio_id")
	private String Audio_id;

	// 服务器视频名称
	@Column(column = "Audio_name")
	private String Audio_name;

	// 服务器音乐名称
	@Column(column = "Audio_fileName")
	private String Audio_fileName;

	// 音频作者
	@Column(column = "Audio_author")
	private String Audio_author;

	// 音频详细介绍
	@Column(column = "Audio_context")
	private String Audio_context;

	// 分类id
	@Column(column = "Audio_sort_id")
	private String Audio_sort_id;

	// 音频本地状态
	@Column(column = "Audio_state")
	private String Audio_state;

	// 音频长度
	@Column(column = "Audio_md5")
	private String Audio_md5;
	
	@Column(column = "Audio_weight")
	private String Audio_weight;

	public AudioEntity() {
		super();
	}

	public AudioEntity(String audio_id, String audio_name,
			String audio_fileName, String audio_author, String audio_context,
			String audio_sort_id, String audio_state, String audio_md5,
			String audio_weight) {
		super();
		Audio_id = audio_id;
		Audio_name = audio_name;
		Audio_fileName = audio_fileName;
		Audio_author = audio_author;
		Audio_context = audio_context;
		Audio_sort_id = audio_sort_id;
		Audio_state = audio_state;
		Audio_md5 = audio_md5;
		Audio_weight = audio_weight;
	}

	public String getAudio_id() {
		return Audio_id;
	}

	public void setAudio_id(String audio_id) {
		Audio_id = audio_id;
	}

	public String getAudio_name() {
		return Audio_name;
	}

	public void setAudio_name(String audio_name) {
		Audio_name = audio_name;
	}

	public String getAudio_fileName() {
		return Audio_fileName;
	}

	public void setAudio_fileName(String audio_fileName) {
		Audio_fileName = audio_fileName;
	}

	public String getAudio_author() {
		return Audio_author;
	}

	public void setAudio_author(String audio_author) {
		Audio_author = audio_author;
	}

	public String getAudio_context() {
		return Audio_context;
	}

	public void setAudio_context(String audio_context) {
		Audio_context = audio_context;
	}

	public String getAudio_sort_id() {
		return Audio_sort_id;
	}

	public void setAudio_sort_id(String audio_sort_id) {
		Audio_sort_id = audio_sort_id;
	}

	public String getAudio_state() {
		return Audio_state;
	}

	public void setAudio_state(String audio_state) {
		Audio_state = audio_state;
	}

	public String getAudio_md5() {
		return Audio_md5;
	}

	public void setAudio_md5(String audio_md5) {
		Audio_md5 = audio_md5;
	}

	public String getAudio_weight() {
		return Audio_weight;
	}

	public void setAudio_weight(String audio_weight) {
		Audio_weight = audio_weight;
	}
	
}

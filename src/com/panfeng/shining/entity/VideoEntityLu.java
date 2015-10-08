package com.panfeng.shining.entity;

import in.srain.cube.request.JsonData;
import in.srain.cube.request.JsonData.JsonConverter;

import java.io.Serializable;
import java.util.Date;

import com.panfeng.shining.TyuApp;

import tyu.common.net.TyuDefine;

public class VideoEntityLu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 11546515L;
	private int videoID;
	private String videoName;
	private String videoFileName;
	private String videoContext;
	private Date videoDate;
	private String videoAuthor;
	private String videoSave;
	private String videoLoook;
	private String videoSeting;

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoFileName() {
		return videoFileName;
	}

	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}

	public String getVideoContext() {
		return videoContext;
	}

	public void setVideoContext(String videoContext) {
		this.videoContext = videoContext;
	}

	public Date getVideoDate() {
		return videoDate;
	}

	public void setVideoDate(Date videoDate) {
		this.videoDate = videoDate;
	}

	public String getVideoAuthor() {
		return videoAuthor;
	}

	public void setVideoAuthor(String videoAuthor) {
		this.videoAuthor = videoAuthor;
	}

	public String getVideoSave() {
		return videoSave;
	}

	public void setVideoSave(String videoSave) {
		this.videoSave = videoSave;
	}

	public String getVideoLoook() {
		return videoLoook;
	}

	public void setVideoLoook(String videoLoook) {
		this.videoLoook = videoLoook;
	}

	public String getVideoSeting() {
		return videoSeting;
	}

	public void setVideoSeting(String videoSeting) {
		this.videoSeting = videoSeting;
	}

	public int getVideoID() {
		return videoID;
	}

	public void setVideoID(int videoID) {
		this.videoID = videoID;
	}

	public VideoEntityLu(int videoID, String videoName, String videoFileName,
			String videoContext, Date videoDate, String videoAuthor,
			String videoSave, String videoLoook, String videoSeting) {
		super();
		this.videoID = videoID;
		this.videoName = videoName;
		this.videoFileName = videoFileName;
		this.videoContext = videoContext;
		this.videoDate = videoDate;
		this.videoAuthor = videoAuthor;
		this.videoSave = videoSave;
		this.videoLoook = videoLoook;
		this.videoSeting = videoSeting;
	}

	public VideoEntityLu() {
		super();
	}

	@Override
	public boolean equals(Object o) {
		super.equals(o);
		if (o instanceof VideoEntityLu) {
			VideoEntityLu vex = (VideoEntityLu) o;

			if (vex.getVideoID() == this.getVideoID()
					&& vex.getVideoName().equals(this.getVideoName())
					&& vex.getVideoFileName().equals(this.getVideoFileName())
					&& vex.getVideoContext().equals(this.getVideoContext())
					&& vex.getVideoDate().equals(this.getVideoDate())
					&& vex.getVideoAuthor().equals(this.getVideoAuthor())
					&& vex.getVideoSave().equals(this.getVideoSave())
					&& vex.getVideoLoook().equals(this.getVideoLoook())
					&& vex.getVideoSeting().equals(this.getVideoSeting()))

				return true;

		}
		return false;

	}

	@Override
	public String toString() {
		super.toString();
		StringBuffer sb = new StringBuffer();
		sb.append("videoName:");
		sb.append(this.videoName);
		sb.append(this.videoFileName);
		sb.append(this.videoID);
		return sb.toString();
	}

	//json转换器
	public static JsonConverter<VideoEntityLu> jsonConverter = new JsonConverter<VideoEntityLu>() {
		@Override
		public VideoEntityLu convert(JsonData item) {
			VideoEntityLu videoEntity = new VideoEntityLu();
			videoEntity.setVideoID(item.optInt("mb_id"));
			videoEntity.setVideoName(item.optString("mb_name"));
			videoEntity.setVideoAuthor(item.optString("mb_author"));
			videoEntity.setVideoFileName(item.optString("mb_video_name"));
			videoEntity.setVideoContext(item.optString("mb_content"));
			videoEntity.setVideoLoook(item.optString("mb_plays"));
			videoEntity.setVideoSeting(item.optString("mb_settingtimes"));
			return videoEntity;
		}
	};

	public String getImageUrl() {
		if (this.getVideoFileName() != null
				&& !this.getVideoFileName().equals("")) {
			String imgUrl = TyuDefine.HOST+"media_base/"
					+ this.getVideoFileName().substring(0,
							this.getVideoFileName().lastIndexOf('.')) + ".jpg";
			return imgUrl;
		} else {
			return "";
		}
	}

	public String getVideoUrl() {
		String url=TyuDefine.HOST+"smc/getVideo?id="+this.videoID;
		return url;
	}
}

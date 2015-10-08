package com.panfeng.shining.entity;

import java.io.Serializable;

public class UserInfoEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8630882001477861458L;
	

	private int userId=-1;
	private int userMbId=-1;// 用户设定的闪铃
	private String userPhone="";
	private String userUid="";
	private String userImei="";
	private String userName="";
	private String userImage="";

	public String getUserImei() {
		return userImei;
	}

	public void setUserImei(String userImei) {
		this.userImei = userImei;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public int getUserMbId() {
		return userMbId;
	}

	public void setUserMbId(int userMbId) {
		this.userMbId = userMbId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public UserInfoEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserInfoEntity(String userImei, String userUid, int userId, String userPhone,
			int userMbId, String userName, String userImage) {
		super();
		this.userImei = userImei;
		this.userUid = userUid;
		this.userId = userId;
		this.userPhone = userPhone;
		this.userMbId = userMbId;
		this.userName = userName;
		this.userImage = userImage;
	}

	@Override
	public String toString() {
		// StringBuffer(m,l)

		StringBuffer sp = new StringBuffer();
		sp.append("imei=");
		sp.append(this.userImei);
		sp.append("|");
		sp.append("uid=");
		sp.append(this.userUid);
		sp.append("|");
		sp.append("id=");
		sp.append(this.userId);
		sp.append("|");
		sp.append("phone");
		sp.append(this.userPhone);
		sp.append("|");
		sp.append("mbid=");
		sp.append(this.userMbId);
		sp.append("|");
		sp.append("name=");
		sp.append(this.userName);
		sp.append("|");
		sp.append("imagePath=");
		sp.append(this.userImage);
		sp.append("|");

		return sp.toString();
	}

	@Override
	public boolean equals(Object o) {

		if (o instanceof UserInfoEntity) {
			UserInfoEntity vex = (UserInfoEntity) o;

			if (vex.getUserId() == this.getUserId()
					&& vex.getUserImage().equals(this.getUserId())
					&& vex.getUserImei().equals( this.getUserImei())
					&& vex.getUserMbId() == this.getUserMbId()
					&& vex.getUserName().equals(this.getUserId())
					&& vex.getUserPhone().equals(this.getUserPhone())
					&& vex.getUserUid().equals(this.getUserUid()))

				return true;

		}

		return false;
	}

}

package com.panfeng.shining.entity;

import java.io.Serializable;

public class PhoneInfoEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8630882001477861121L;
	


	private String callPhone="";

	public String getCallPhone() {
		return callPhone;
	}

	public void setCallPhone(String callPhone) {
		this.callPhone = callPhone;
	}

	

	public PhoneInfoEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhoneInfoEntity(String callPhone) {
		super();
		this.callPhone = callPhone;

	}



	@Override
	public boolean equals(Object o) {

	

		return false;
	}

}

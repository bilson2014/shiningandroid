package com.panfeng.camera.entity;

public class ImageData {

	private byte[] data;
	// 保证时序
	private long time;


	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}



	public ImageData(byte[] data, long time) {
		super();
		this.data = data;
		this.time = time;
	}

	public ImageData() {
		super();
	}
}

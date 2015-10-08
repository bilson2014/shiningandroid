package com.panfeng.servicerestart;

public class ServiceRestart {
	static {
		System.loadLibrary("shininglib");
	}
	public native void forkService(String time,int sdkVersion,String pid);
}

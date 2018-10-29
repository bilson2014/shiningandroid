package tyu.common.net;

import java.io.File;

import tyu.common.utils.TyuFileUtils;


public class TyuDefine {
	
	//用户信息
	static public String LOCALUSERPATH = File.separator+"userInfo.dat";
	static public String LOCALPATH= TyuFileUtils.getValidPath();
	
	 // 应用服务器
	//http://182.92.154.162:8080/shiningCenterService/
	
	
	//static public String xmppHost = "182.92.154.162";
	static public String xmppHost = "123.59.86.227";
	/*static public String HOST = "http://"+xmppHost+":8080/shiningCenterService/";
	static public String URL = "http://share.shiningmovie.com:8080/shiningCenterService/";*/
	static public String HOST = "http://"+xmppHost+":8080/shiningCenterService/";
	static public String URL = "http://share.shiningring.net:8080/shiningCenterService/";
	
//	static public String xmppHost = "123.57.70.185";
//	static public String HOST = "http://123.57.70.185:8080/shiningCenterService/";
//	static public String URL = HOST;
}

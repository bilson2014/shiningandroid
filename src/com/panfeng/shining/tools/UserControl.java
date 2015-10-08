package com.panfeng.shining.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONObject;

import tyu.common.net.TyuDefine;
import tyu.common.net.TyuHttpClientUtils;
import tyu.common.utils.TyuCommon;
import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.panfeng.shining.data.UpdateUser;
import com.panfeng.shining.entity.UserInfoEntity;

public class UserControl {

	private static File f;

	private static UserInfoEntity readObjectFile()

	{

		UserInfoEntity result = null;

		try {
			f = new File(TyuDefine.LOCALPATH, TyuDefine.LOCALUSERPATH);
			FileInputStream fos = new FileInputStream(f);
			byte[] b = new byte[fos.available()];

			fos.read(b);
			fos.close();

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					b);
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);
			result = (UserInfoEntity) objectInputStream.readObject();

			//Log.e("xlxlx", result+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}
	

	

	
	public static boolean isLogin() {

		String phone = getUserInfo().getUserPhone();

		if (phone.isEmpty()) {
			return false;
		}

		return true;

	}

	private static void writeObjectFile(UserInfoEntity uie) {

		try {

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					byteArrayOutputStream);
			objectOutputStream.writeObject(uie);
			objectOutputStream.flush();
			objectOutputStream.close();

			FileOutputStream fos = new FileOutputStream(f);
			fos.write(byteArrayOutputStream.toByteArray());
			fos.flush();
			fos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void createUser(UserInfoEntity uie) {

		try {
			f = new File(TyuDefine.LOCALPATH);
			if (!f.exists())
				f.mkdirs();

			f = new File(TyuDefine.LOCALPATH, TyuDefine.LOCALUSERPATH);

			if (!f.exists()) {

				UserInfoEntity ui = register(uie, null);

				if (ui != null) {
					f.createNewFile();
					UserControl.writeObjectFile(ui);
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static UserInfoEntity getUserInfo() {

		UserInfoEntity ue = null;
		if (UserControl.readObjectFile() == null
				|| UserControl.readObjectFile().getUserId() <= 0) {

			try {
				ExecutorService es = Executors.newFixedThreadPool(1);
				Future<UserInfoEntity> future = es.submit(new UpdateUser());
				ue = future.get();

				if (ue != null)
					writeObjectFile(ue);
				es.shutdown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			ue = readObjectFile();
		}

		
		return ue;

		

	}

	public static boolean updateUser(UserInfoEntity ue, File image) {

		UserInfoEntity res = register(ue, image);
		
		Log.i("dirlist", "slw+res"+res.toString());
		
		if (res != null) {

			f = new File(TyuDefine.LOCALPATH, TyuDefine.LOCALUSERPATH);
			f.delete();
			
			createUser(ue);
			return true;
		}
		return false;

	}
	
	

	public static UserInfoEntity register(UserInfoEntity ue, File image) {

		String imei = TyuCommon.getImei();
		String uuid = TyuCommon.getTyuUid();

		Log.i("video", "imei" + imei);
		Log.i("video", "uuid" + uuid);

		String url = TyuDefine.HOST + "smc/post_user_data";
		HashMap<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("imei", imei);
		valueMap.put("uuid", uuid);
		if (ue != null) {

			if (ue.getUserId() > 0) {
				valueMap.put("id", ue.getUserId() + "");
			}
			if (ue.getUserMbId() > 0) {
				valueMap.put("mb_id", ue.getUserMbId() + "");
			}
			if (ue.getUserPhone() != null)
				valueMap.put("phone", ue.getUserPhone());
			if (!TextUtils.isEmpty(ue.getUserName()))
				valueMap.put("name", ue.getUserName());
			if (image != null && image.exists())
				valueMap.put("file", image);

		}

		String res = TyuHttpClientUtils.postMutiPart(url, valueMap, null);

		Log.i("video", "regester" + res);

		try {
			JSONObject obj = new JSONObject(res);

			ue = new UserInfoEntity();
			if (!obj.isNull("ui_id"))
				ue.setUserId(obj.optInt("ui_id"));
			if (!obj.isNull("ui_mb_id"))
				ue.setUserMbId(obj.optInt("ui_mb_id"));
			if (!obj.isNull("ui_imei"))
				ue.setUserImei(obj.optString("ui_imei"));
			if (!obj.isNull("ui_uuid"))
				ue.setUserUid(obj.optString("ui_uuid"));
			if (!obj.isNull("ui_phone"))
				ue.setUserPhone(obj.optString("ui_phone"));
			if (!obj.isNull("ui_name"))
				ue.setUserName(obj.optString("ui_name"));
			if (!obj.isNull("ui_image"))
				ue.setUserImage(obj.optString("ui_image"));

			Log.i("testfind", ue.toString());
			return ue;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			
			
			

		}
		return ue;

	}

	public static void exit() {
	
		
	 UserInfoEntity uii = getUserInfo();
     uii.setUserImage("");
     uii.setUserName("");
     uii.setUserPhone("");
     uii.setUserUid("");
     
     writeObjectFile(uii);
		
	}

	// public void loadUserInfo() {
	// imei = TyuCommon.getImei();
	// uuid = TyuCommon.getTyuUid();
	//
	// File file = new File(TyuShinningData.user_shinning_dir, "user_info.dat");
	// if (file.exists()) {
	// try {
	// String res = TyuFileUtils.loadFromFile(file.getAbsolutePath());
	// JSONObject obj = new JSONObject(res);
	// loadData(obj);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }

}

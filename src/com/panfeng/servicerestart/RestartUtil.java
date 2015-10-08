package com.panfeng.servicerestart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RestartUtil {

	public static List<String> getCurrentProcess(String command, String checkKey) {
		List<String> resultList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			Process Linuxprocess = Runtime.getRuntime().exec(command);
			// Linuxprocess.waitFor();
			br = new BufferedReader(new InputStreamReader(
					Linuxprocess.getInputStream()));
			String lineString;
			while ((lineString = br.readLine()) != null) {
				if (lineString.contains(checkKey)) {
					resultList.add(lineString);
				}
			}
			Linuxprocess.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	public static List<String> getCurrentProcess() {
		List<String> resultList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			Process Linuxprocess = Runtime.getRuntime().exec("ps");
			Linuxprocess.waitFor();
			br = new BufferedReader(new InputStreamReader(
					Linuxprocess.getInputStream()));
			String lineString;
			while ((lineString = br.readLine()) != null) {
				resultList.add(lineString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	public static void startService(String pid) {

		List<String> res = RestartUtil.getCurrentProcess("ps",
				"com.panfeng.shinning");
		if (res.size() <= 1) {
			ServiceRestart service = new ServiceRestart();
			service.forkService("60", android.os.Build.VERSION.SDK_INT,pid);
		}

	}
}

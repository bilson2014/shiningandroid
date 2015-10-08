package com.panfeng.shining.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopy {

	
	public boolean fileExists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
	public boolean fileCopy(String oldFilePath, String newFilePath)
			throws IOException {
		// 如果原文件不存在
		if (fileExists(oldFilePath) == false) {
			return false;
		}

		// 获得原文件流
		FileInputStream inputStream = new FileInputStream(new File(oldFilePath));
		byte[] data = new byte[1024];
		// 输出流
		FileOutputStream outputStream = new FileOutputStream(new File(
				newFilePath));
		// 开始处理流
		while (inputStream.read(data) != -1) {
			outputStream.write(data);
		}
		inputStream.close();
		outputStream.close();
		return true;
	}

}

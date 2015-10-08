package com.panfeng.shining.slw.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	protected static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			// 初始化失败
			LogUtils.writeErrorLog("md5获取失败", "", null,null);
		}
	}

	/**
	 * 生成字符串的md5校验值
	 * 
	 * @param s
	 * @return
	 */
	public static String getMD5String(final String s) {
		return getMD5String(s.getBytes());
	}

	/**
	 * 判断字符串的md5校验码是否与一个已知的md5码相匹配
	 * 
	 * @param password
	 *            要校验的字符串
	 * @param md5PwdStr
	 *            已知的md5校验码
	 * @return
	 */
	public static boolean checkPassword(final String md5, final String md5PwdStr) {
		return md5.equals(md5PwdStr);
	}

	/**
	 * 生成文件的md5校验值
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String(final File file) {
		try {
			InputStream fis;
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				messagedigest.update(buffer, 0, numRead);
			}
			fis.close();
		} catch (Exception e) {
			LogUtils.writeErrorLog("获取文件MD5失败", "",e,null);
		}
		return bufferToHex(messagedigest.digest());
	}

	public static String getMD5String(final byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(final byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(final byte bytes[], final int m,
			final int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHex(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHex(final byte bete,
			final StringBuffer stringbuffer) {
		// & and与运算
		// 相同位的两个数字都为1，则为1；若有一个不为1，则为0。
		// 0xf0------>11110000
		// ---------->&&&&&&&&
		// 10101011-->10101011
		// result---->10100000
		// >>运算右移动4位
		// 计算高四位--->10100000
		// 高四位结果--->10100000--->00001010=1010
		// 计算第四位
		// 0xf------->1111
		// ---------->&&&&
		// ------>10101011
		// result---->1011
		// 高位1010 低位 1011 原1010 1011
		// 二进制转换10进制
		// 0000-->0
		// 1111-->15 -->1x2E0+1x2E1+1x2E2+1x2E3=1+2+4+8=15
		// 0123456789abcdef

		char c0 = hexDigits[(bete & 0xf0) >> 4];
		char c1 = hexDigits[bete & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		File file = new File("E:/a.txt");
		String md5 = getFileMD5String(file);
		System.out.println("MD5:" + md5);
		long end = System.currentTimeMillis();
		System.out.println("md5:" + md5 + " time:" + ((end - begin)) + "ss");
	}
}

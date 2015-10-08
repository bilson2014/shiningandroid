package com.panfeng.shining.slw.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.panfeng.shining.slw.myinterface.LogInterface;

public class LogUtils {
	/**
	 * 收集错误信息
	 * 
	 * @param marker
	 *            标记名称
	 * @param mymessage
	 *            自定义信息
	 * @param e
	 *            错误堆栈对象
	 */
	public static void writeErrorLog(final String marker,
			final String mymessage, final Exception e,LogInterface log) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(DateUtils.getDateString());
		sBuffer.append("\n");
		sBuffer.append("marker: ");
		sBuffer.append(marker);
		sBuffer.append("\n");
		sBuffer.append("自定义消息： ");
		sBuffer.append(mymessage);
		sBuffer.append("\n");
		sBuffer.append("堆栈信息：");
		sBuffer.append(printfStackTrace(e));
		sBuffer.append("\n");
		sBuffer.append("---------------------------------------------------------");

		FileUtils.writeFile(DefindConstant.saveLogPath, sBuffer.toString());
		
		if(log!=null)
		{
			log.logCallBack();
		}
	}

	/**
	 * 以string方式打印堆栈错误信息
	 * 
	 * @param ParseException
	 * @return
	 */
	public static String printfStackTrace(final Exception e) {
		if (e != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.close();
			try {
				sw.close();
			} catch (IOException e1) {
				// e1.printStackTrace();
			}
			return sw.toString();
		}else {
			return "";
		}
		
	}
}

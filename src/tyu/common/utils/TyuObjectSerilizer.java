package tyu.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class TyuObjectSerilizer {
	static Object tmpValue = new Object();

	
	/**
	 * @param aObj
	 * @param filename
	 * @return
	 */
	static public boolean writeObject(Object aObj,String filename) {
		boolean res = false;
		synchronized (tmpValue) {
			try {
				File file = new File(filename);
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(file));
				oos.writeObject(aObj);
				oos.flush();
				oos.close();
				res = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		}

		return res;
	}

	static public Object readObject(String fileName) {
		Object res = null;
		synchronized (tmpValue) {
			try {
				File file = new File(fileName);
				if (file.exists()) {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(file));
					res = ois.readObject();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				res = null;
			}
		}
		return res;
	}
	static public Object readObject(File file) {
		Object res = null;
		synchronized (tmpValue) {
			try {
				if (file.exists()) {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(file));
					res = ois.readObject();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				res = null;
			}
		}
		return res;
	}
}

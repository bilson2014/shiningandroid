package tyu.common.utils;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class TyuJsonMap {
	public HashMap<String, Object> mValues = new HashMap<String, Object>();

	public TyuJsonMap(JSONObject aObject) {
		if (aObject != null) {
			Iterator iter = aObject.keys();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				if (!aObject.isNull(key)) {
					try {
						mValues.put(key, aObject.get(key));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public String getString(String aKey) {
		if (!mValues.containsKey(aKey))
			return null;
		return (String) mValues.get(aKey);
	}

	public int getInt(String aKey, int aDefault) {
		if (!mValues.containsKey(aKey))
			return aDefault;
		return (Integer) mValues.get(aKey);
	}
	public long getLong(String aKey, long aDefault) {
		if (!mValues.containsKey(aKey))
			return aDefault;
		return (Long) mValues.get(aKey);
	}
}

package com.panfeng.shining.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tyu.common.utils.TyuContextKeeper;
import tyu.common.utils.TyuFileUtils;
import tyu.common.utils.TyuObjectSerilizer;

import android.content.Intent;
import android.util.Pair;

import com.panfeng.shining.data.TyuShinningData.ShinningFriendItemData;
import com.panfeng.shining.service.TyuShinningFriendSubService;

public class TyuShinningFriendIndex {
	static TyuShinningFriendIndex instance = null;
	HashMap<String, Integer> readHistory = new HashMap<String, Integer>();

	static public TyuShinningFriendIndex getInstance() {
		if (instance == null) {
			instance = new TyuShinningFriendIndex();
			instance.loadHistory();
		}
		return instance;
	}

	public HashMap<String, Integer> buildMap(List<ShinningFriendItemData> aData) {
		HashMap<String, Integer> res = new HashMap<String, Integer>();
		if (aData != null && aData.size() > 0) {
			for (ShinningFriendItemData shinningFriendItemData : aData) {
				Pair<String, Integer> pair = buildPair(shinningFriendItemData);
				if (pair != null) {
					res.put(pair.first, pair.second);
				}
			}
		}
		return res;
	}

	public Pair<String, Integer> buildPair(ShinningFriendItemData aData) {
		if (aData != null && aData.mb_info != null) {
			if (aData.mb_info.mb_id > 0) {
				String key = aData.number + "|" + aData.imei;
				int id = aData.mb_info.mb_id;
				return new Pair<String, Integer>(key, id);
			}

		}
		return null;
	}

	public boolean isInHistory(ShinningFriendItemData aData) {
		Pair<String, Integer> pair = buildPair(aData);
		if (pair != null && readHistory.containsKey(pair.first)) {
			int val = readHistory.get(pair.first);
			if (val == pair.second) {
				return true;
			}
		}
		return false;
	}

	public boolean isNewSf(ShinningFriendItemData aData) {
		Pair<String, Integer> pair = buildPair(aData);
		if (pair != null && !readHistory.containsKey(pair.first)) {
			return true;
		}
		return false;
	}

	public boolean isEmptyShinning(ShinningFriendItemData aData) {
		if (aData != null && aData.mb_info != null) {
			if (aData.mb_info.mb_id > 0) {
				return false;
			}
		}
		return true;
	}

	public boolean hasNewInfo(List<ShinningFriendItemData> aData) {
		if (aData != null) {
			for (ShinningFriendItemData shinningFriendItemData : aData) {
				if (!isInHistory(shinningFriendItemData)
						&& !isEmptyShinning(shinningFriendItemData)) {
					return true;
				}
			}
		}
		return false;
	}

	public List<ShinningFriendItemData> SFNewState(
			List<ShinningFriendItemData> aData) {
		List<ShinningFriendItemData> ret = new ArrayList<TyuShinningData.ShinningFriendItemData>();
		if (aData != null) {
			for (ShinningFriendItemData shinningFriendItemData : aData) {
				if (!isInHistory(shinningFriendItemData)
						&& !isEmptyShinning(shinningFriendItemData)) {
					ret.add(shinningFriendItemData);
				}
			}
		}
		return ret;
	}

	public List<ShinningFriendItemData> SFNewUser(
			List<ShinningFriendItemData> aData) {
		List<ShinningFriendItemData> ret = new ArrayList<TyuShinningData.ShinningFriendItemData>();
		if (aData != null) {
			for (ShinningFriendItemData shinningFriendItemData : aData) {
				if (isNewSf(shinningFriendItemData)
						&& !isEmptyShinning(shinningFriendItemData)) {
					ret.add(shinningFriendItemData);
				}
			}
		}
		return ret;
	}

	public boolean addHistory(ShinningFriendItemData aData) {
		Pair<String, Integer> pair = buildPair(aData);
		if (pair != null) {
			readHistory.put(pair.first, pair.second);
			saveHistory();
			// 发送刷新广播
			TyuContextKeeper.getInstance().sendBroadcast(
					new Intent(TyuShinningFriendSubService.action));
			return true;
		}
		return false;
	}

	public void saveHistory() {
		File file = new File(TyuFileUtils.getValidPath(), "config/sf_index.dat");
		file.getParentFile().mkdirs();
		if (readHistory != null) {
			TyuObjectSerilizer.writeObject(readHistory, file.getAbsolutePath());
		}
	}

	public void loadHistory() {
		File file = new File(TyuFileUtils.getValidPath(), "config/sf_index.dat");
		if (file.exists()) {
			Object obj = TyuObjectSerilizer.readObject(file);
			if (obj != null) {
				readHistory = (HashMap<String, Integer>) obj;
			}
		}
	}
}

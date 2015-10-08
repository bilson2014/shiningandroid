package tyu.common.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.telephony.SmsManager;
import android.text.TextUtils;

public class PhoneNumVerify {
	public String veri_code = null;

	public PhoneNumVerify() {
		String tmp = System.currentTimeMillis() + "";
		veri_code = tmp.substring(tmp.length() - 5, tmp.length());
	}

	public boolean isVaildPhone(String mobiles) {
		if (TextUtils.isEmpty(mobiles))
			return false;
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public void sendSms(String phone, String msg) {
		SmsManager smsManager = SmsManager.getDefault();
		if (msg.length() > 70) {
			List<String> contents = smsManager.divideMessage(msg);
			for (String sms : contents) {
				smsManager.sendTextMessage(phone, null, sms, null, null);
			}
		} else {
			smsManager.sendTextMessage(phone, null, msg, null, null);
		}
	}
}

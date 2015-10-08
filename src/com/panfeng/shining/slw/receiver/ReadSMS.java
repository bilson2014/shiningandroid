package com.panfeng.shining.slw.receiver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadSMS {
    private Context context;
    private Handler handler;

    public static final int FULL_SMS_MESSAGE = 0x1213;

    private SmsObserver smsObserver;
    public ReadSMS(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        smsObserver = new SmsObserver(context, handler);
        context.getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);
    }


    private Uri SMS_INBOX = Uri.parse("content://sms/");

    public void getSmsFromPhone() {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"body", "address", "person"};// "_id", "address",
        // "person",, "date",
        // "type
        String where = " date >  "
                + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            String body = cur.getString(cur.getColumnIndex("body"));

            Log.i("dawn", ">>>>>>>>>>>>>>>>短信的内容：" + body);
            //【闪铃app】欢迎您使用闪铃，您的验证码是2243，请于10分钟内输入

            if(body.indexOf("欢迎您使用闪铃")!=-1)
            {
                Message msg = new Message();
                msg.what = ReadSMS.FULL_SMS_MESSAGE;
                msg.obj = getNum(body);
                handler.sendMessage(msg);
            }
        }
    }
    private String getNum(String body){
        String result=null;
        String re2=".*?(\\d+)";
        Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(body);
        if (m.find())
        {
            result=m.group(1);
        }
        return result;
    }

    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getSmsFromPhone();
        }
    }

}

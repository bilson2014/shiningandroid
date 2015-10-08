package com.panfeng.shining.slw.receiver;

//import com.panfeng.shining.slw.customcomponents.windows.Windows;
//import com.panfeng.shining.slw.utils.DefindConstant;
//
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.telephony.PhoneStateListener;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//
//
//
///**
// * Created by dawn on 2015/7/15.
// */
//public class InterceptCall extends BroadcastReceiver {
//    private Windows windows;
//
//    private boolean flags=false;
//    private boolean play=false;
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.i("BroadcastReceiver", "action" + intent.getAction());
//        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
//            //如果是去电（拨出）
//          //  Log.i("BroadcastReceiver", "拨出");
//        } else {
//          //  Log.i("BroadcastReceiver", "来电");
//            final String 陆涛= DefindConstant.saveDownaLoadVideoPath+"123.mp4";
//            windows =new Windows(context, 陆涛);
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
//            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
//            //设置一个监听器
//            flags=true;
//            play=true;
//        }
//    }
//
//    PhoneStateListener listener = new PhoneStateListener() {
//
//        @Override
//        public void onCallStateChanged(int state, String incomingNumber) {
//            super.onCallStateChanged(state, incomingNumber);
//            switch (state) {
//                case TelephonyManager.CALL_STATE_IDLE:
//                    if(flags&&play)
//                    {
//                        play=false;
//                        windows.stopWindows();
//                    }
//                    Log.i("BroadcastReceiver", "空闲中");
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    if(flags&&play)
//                    {
//                        play=false;
//                        windows.stopWindows();
//                    }
//                    Log.i("BroadcastReceiver", "电话通话的状态");
//                    break;
//                case TelephonyManager.CALL_STATE_RINGING:
//                    Log.i("BroadcastReceiver", "电话响铃的状态" + incomingNumber);
//                    break;
//            }
//        }
//
//    };
//}
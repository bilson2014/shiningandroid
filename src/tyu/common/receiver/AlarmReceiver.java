/**    
 * @Title: AlarmReceiver.java  
 * @Package com.anguanjia.safe.battery.rendent  
 * @Description: TODO  
 * @author wuxu    
 * @date 2012-5-16 下午03:59:50  
 * @version V1.0    
 */
package tyu.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**  
 * @name AlarmReceiver.java  
 * @author wuxu  
 * @Description: TODO
 * @date 2012-5-16   
 */
public class AlarmReceiver extends BroadcastReceiver {
	
    public static final String ERR_TAG = "AlarmReceiver.java";
	@Override
	public void onReceive(Context context, Intent in) {
		String action = in.getAction();
//		if("repeating".equals(action)){
//			Intent intent= new Intent();
//			intent.setAction(UpdateManager.updaue_action);
//			context.sendBroadcast(intent);
//		}else if("short".equals(action)){
//			Intent intent= new Intent();
//			intent.setAction(ExchangeStallManager.EXCHANGE_STALL_ACTION);
//			context.sendBroadcast(intent);
//		}
		
	}
}

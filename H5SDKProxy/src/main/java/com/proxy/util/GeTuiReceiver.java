package com.proxy.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.proxy.sdk.module.GeTuiPushModule;

;

public class GeTuiReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        
        Log.e("KNSDKProxy"," 个推onReceive() action=" + bundle.getInt("action"));
        LogUtil.log("GeTuiPushModule.CMD_ACTION:"+GeTuiPushModule.CMD_ACTION);
        int intResult = bundle.getInt(GeTuiPushModule.CMD_ACTION);
        LogUtil.log("intResult"+intResult);
        
        if( GeTuiPushModule.get_GET_MSG_DATA() == intResult ){
        	byte[] payload = bundle.getByteArray("payload");
	        String taskid = bundle.getString("taskid");
	        String messageid = bundle.getString("messageid");
	        boolean result =  GeTuiPushModule.getInstance().sendFeedbackMessage(context, taskid, messageid);
          
	        if (payload != null) {
	            String data = new String(payload);
	            Log.e("KNSDKProxy","receiver payload : " + data);
	            payloadData.append(data);
	            payloadData.append("\n");
	        }
        	
        }else if( GeTuiPushModule.get_GET_CLIENTID() == intResult ){
        	
        	String cid = bundle.getString("clientid");
        	LogUtil.log("clientid : sss " + cid);
        	
        }else if( GeTuiPushModule.get_THIRDPART_FEEDBACK() == intResult ){
        	
        	LogUtil.log("THIRDPART_FEEDBACK");
        	
        }else{
        	
        	LogUtil.log("OTHER");
        	
        } 
        
    }
}

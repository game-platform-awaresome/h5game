package com.proxy.sdk.module;

import android.app.Activity;
import android.content.Context;

import com.proxy.util.LogUtil;
import com.proxy.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GeTuiPushModule {
	
	private static GeTuiPushModule instance = null;
	
	private Class<?> clazz = null;
	
	private Object clazzInstance = null;
	
	private Method onCreateMethod = null;
	private Method getClientId = null;
	private Method turnOnorOffPush = null;
	private Method sendFeedbackMessage = null;
	
	public static String CMD_ACTION = null;
	public static int GET_MSG_DATA = 0 ;
	public static int GET_CLIENTID = 0 ;
	public static int THIRDPART_FEEDBACK = 0 ;
	
	private boolean isOpen = false;
	
	private GeTuiPushModule(){
		init("com.kuniu.sdk.getui.GeTuiPushSDK");
	}
	
	private void init(String classPath){
		try {
			clazz = Class.forName(classPath);
			if(clazz != null){
				this.isOpen = true;
			}else{
				return;
			}
			clazzInstance = clazz.getMethod("getInstance").invoke(clazz);
			
			onCreateMethod = clazz.getMethod("onCreate", Activity.class );
			
			getClientId = clazz.getMethod("getClientId");
			
			turnOnorOffPush = clazz.getMethod("turnOnorOffPush",boolean.class);
			
			sendFeedbackMessage = clazz.getMethod("sendFeedbackMessage", Context.class , String.class , String.class );
			
			Field field  = clazz.getDeclaredField("CMD_ACTION") ;
			field.setAccessible(true);
			CMD_ACTION = field.get(null).toString();
			LogUtil.log("CMD_ACTION:"+CMD_ACTION);
			
			field  = clazz.getDeclaredField("GET_MSG_DATA") ;
			field.setAccessible(true);
			GET_MSG_DATA = Integer.parseInt( field.get(null).toString()) ;
			LogUtil.log("GET_MSG_DATA:"+GET_MSG_DATA);
			
			field  = clazz.getDeclaredField("GET_CLIENTID") ;
			field.setAccessible(true);
			GET_CLIENTID = Integer.parseInt( field.get(null).toString()) ;
			LogUtil.log("GET_CLIENTID:"+GET_CLIENTID);
			
			field  = clazz.getDeclaredField("THIRDPART_FEEDBACK") ;
			field.setAccessible(true);
			THIRDPART_FEEDBACK = Integer.parseInt( field.get(null).toString()) ;
			LogUtil.log("THIRDPART_FEEDBACK:"+THIRDPART_FEEDBACK);
			
		} catch (Exception e) {
//			e.printStackTrace();
			LogUtil.w("GeTuiPushModule is unabled");
			this.isOpen = false;
		}
	}
	
	public static final int get_GET_MSG_DATA(){
		return GET_MSG_DATA ; 
	}
	
	public static final int get_GET_CLIENTID(){
		return GET_CLIENTID ; 
	}
	
	public static final int get_THIRDPART_FEEDBACK(){
		return THIRDPART_FEEDBACK ; 
	}
	
	public static GeTuiPushModule getInstance(){
		
		if(instance == null){
			instance = new GeTuiPushModule();
		}
		return instance;
	}
	
	public boolean isOpen(){
		return isOpen;
	}
	
	public void onCreate(Activity activity){
		if( this.isOpen() ){
			LogUtil.i("<=========== has GeTuiPushModule ===========>");
			ReflectUtil.invoke(onCreateMethod, clazzInstance, activity );
		}
	}
	
	public String getClientId(){
		String pushId = "";
		if( this.isOpen() ){
			Object obj = ReflectUtil.invoke(getClientId, clazzInstance);
			if(obj!=null)
				pushId = obj.toString();
		}
		return pushId;
	}
	
	
	public void turnOnorOffPush(boolean on){
		if( this.isOpen() ){
			ReflectUtil.invoke(turnOnorOffPush, clazzInstance,on);
		}
	}
	
	public boolean sendFeedbackMessage( Context context, String taskid, String messageid ){
		boolean result = false ;
		if( this.isOpen() ){
			Object  res = ReflectUtil.invoke( sendFeedbackMessage, clazzInstance, context , taskid , messageid  ); 
			if(res!=null){
				result = Boolean.parseBoolean(res.toString());
			}
		}
		return result ; 
	}
	
}

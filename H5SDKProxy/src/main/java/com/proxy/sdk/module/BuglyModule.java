package com.proxy.sdk.module;

import android.app.Activity;
import android.text.TextUtils;

import com.proxy.sdk.channel.SDKConfig;
import com.proxy.util.LogUtil;
import com.proxy.util.ReflectUtil;
import com.proxy.util.Util;

import java.lang.reflect.Method;

public class BuglyModule {
	
	private static BuglyModule instance = null;
	
	private Class<?> clazz = null;
	
	private Object clazzInstance = null;
	
	private Method onCreateMethod = null;
	private Method setUserId = null;
	
	private boolean isOpen = false;
	
	
	private static String getAppId(){
		
//		KnGameInfo gameInfo = KnData.getInstance().getGameInfo();
//		
//		String appId = "900001134";			//外网的  bugly  AppId
//		
//		if( gameInfo!=null &&  "-2".equals(gameInfo.getAdChannel()) ){
//			appId = "900001578";			//内网的  bugly  AppId
//		}else if( gameInfo!=null &&  "1001".equals(gameInfo.getAdChannel()) ){
//			appId = "900001634";			//测试的  bugly  AppId
//		}
		
		String appId = "";
		try {
			appId = SDKConfig.class.getDeclaredField("bugly_appid").get(null).toString();
		} catch (Exception e) {
			LogUtil.e("no bugly_appid");
		}
		
		return appId;
	}
	
	private BuglyModule(){
		init("com.kuniu.sdk.bugly.BuglySDK");
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
			
			onCreateMethod = clazz.getMethod("onCreate", Activity.class , String.class , String.class , String.class );
			
			setUserId = clazz.getMethod("setUserId", String.class );
			
		} catch (Exception e) {
//			e.printStackTrace();
			LogUtil.w("BuglyModule is unabled");
			this.isOpen = false;
		}
		
	}
	
	public static BuglyModule getInstance(){
		
		if(instance == null){
			instance = new BuglyModule();
		}
		return instance;
	}
	
	private boolean isOpen(){
		return isOpen && !TextUtils.isEmpty(getAppId());
	}
	
	
	public void onCreate(Activity activity , String adChannel , String version){
		
		if( Util.fileExits( activity ,"SDKFile/config.png")){
			LogUtil.e("使用了config版本");
			ReflectUtil.invoke(onCreateMethod, clazzInstance, activity , adChannel , version ,  Util.getBuglyappId(activity) );
			
		}else{
			
			if( this.isOpen() ){
				LogUtil.e("使用了正式版本");
				LogUtil.i("<=========== has BuglyModule ===========>");
				LogUtil.e("adChannel:"+adChannel+";version:"+version+";appId:"+getAppId());
				ReflectUtil.invoke(onCreateMethod, clazzInstance, activity , adChannel , version , getAppId());
			}
			
		}
		
	
	}
	
	public void setUserId(String userId){
		if( this.isOpen() ){
			ReflectUtil.invoke(setUserId, clazzInstance , userId);
		}
	}
	
}

package com.proxy.sdk.module;

import android.content.Context;

import com.proxy.sdk.channel.SDKConfig;
import com.proxy.util.Util;

import java.lang.reflect.Field;

public class SdkConfigModule {
	
	public static String   getApiappId( Context ctx ){
		
		String appId = "";
		
		if( Util.fileExits( ctx ,"SDKFile/config.png")){
			
			appId = Util.getApiappId(ctx);
			
		}else{
			
			try {	
				
					Field field  = SDKConfig.class.getDeclaredField("appid") ;
					field.setAccessible(true);
					appId = field.get(null).toString();
			} catch (Exception e) {
				System.out.println("not found the filed of appid  121 ");
			}
			
		}
		
		return appId; 
		
	}
	
	public static String   getApiPrivateKey(  Context ctx ){
		
		String privateKey = "";
		
		if( Util.fileExits( ctx ,"SDKFile/config.png")){
			
			privateKey = Util.getApiprivateKey(ctx);
			
		}else{
			
			try {	
				
					Field field  = SDKConfig.class.getDeclaredField("privateKey") ;
					field.setAccessible(true);
					privateKey = field.get(null).toString();
			} catch (Exception e) {
					System.out.println("not found the filed of privateKey  121 ");
			}
			
		}
		
	
		return privateKey; 
		
	}
	
	public static String   getApiPublicKey( Context ctx ){
		
		String publicKey = "";
		
		if( Util.fileExits( ctx ,"SDKFile/config.png")){
			
			publicKey = Util.getApipublicKey(ctx);
			
		}else{
			
			try {	
				
				Field field  = SDKConfig.class.getDeclaredField("publicKey") ;
				field.setAccessible(true);
				publicKey = field.get(null).toString();
			} catch (Exception e) {
				System.out.println("not found the filed of publicKey  121 ");
			}
			
		}
		

		return publicKey; 
		
	}
	
	

	
}

package com.proxy.sdk.module;

import android.app.Activity;
import android.text.TextUtils;

import com.proxy.sdk.channel.SDKConfig;
import com.proxy.util.LogUtil;
import com.proxy.util.ReflectUtil;
import com.proxy.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class DataEyeModule {
	
	//使用反射判断DataEye库是否存在，且各个方法能够使用
	
	private static DataEyeModule m_instance = null ;
	private Class<?>      		  m_classzz  = null ;
	private boolean               m_flag     = false ;
	
	
	private static Object         getInstanceFunc = null ;
	private static Method         initFunc   = null;
	private static Method         onResumeFunc = null ;
	private static Method         onPauseFunc  = null ;
	private static Method         setDebugModeFunc = null ;
	private static Method         onDestoryFunc    = null ;
	private static Method         commonFunction   = null ;
	
	private static Method         getDateEyeUidFunc   = null ;
	private static Method         getDevicedUidFunc   = null ;
	private static Method         getParameterBooleanFunc = null ;
	private static Method         getParameterLongFunc    = null ;
	private static Method         getParameterIntFunc     = null ; 
	private static Method         getParameterStringFunc  = null ; 
	
	
	public static DataEyeModule getInstance(){
		
		if( null == m_instance ){
			
			m_instance = new DataEyeModule();
			
		}
		
		return m_instance;
		
	}
	
	private DataEyeModule(){
		
		initDataEye();
		
	}
	
	private void initDataEye(){
		try {
			m_classzz =  Class.forName("com.kuniu.sdk.dataeye.DataEyeSDK");
			
			if(m_classzz!=null){
				m_flag = true;
			}else{
				return ;
			}
			
			try {
				
				getInstanceFunc 	= m_classzz.getMethod("getInstance").invoke(m_classzz);
				setDebugModeFunc 	= m_classzz.getMethod("setDebugMode",boolean.class);
				initFunc 			= m_classzz.getMethod("init",Activity.class,String.class,String.class,boolean.class,int.class);
				onResumeFunc 		= m_classzz.getMethod("onResume");
				onPauseFunc     	= m_classzz.getMethod("onPause");
				onDestoryFunc       = m_classzz.getMethod("onDestory");
				commonFunction      = m_classzz.getMethod("commonFunction",Map.class);
				
				getDateEyeUidFunc   = m_classzz.getMethod("getUID");
				getDevicedUidFunc   = m_classzz.getMethod("getUID", Activity.class);
				getParameterBooleanFunc = m_classzz.getMethod("getParameterBoolean", Map.class);
				getParameterIntFunc     = m_classzz.getMethod("getParameterInt", Map.class );
				getParameterLongFunc    = m_classzz.getMethod("getParameterLong",  Map.class );
				getParameterStringFunc  = m_classzz.getMethod("getParameterString",  Map.class );
				
				
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private String         getEyEAppId(){
		
		String DataEyeAppId = "";
		try {	
				
				Field field  = SDKConfig.class.getDeclaredField("DataEyEAppId") ;
				field.setAccessible(true);
				DataEyeAppId = field.get(null).toString();
		} catch (Exception e) {
				System.out.println("not found the filed of DataEyeAppId  121 ");
		}
		return DataEyeAppId; 
		
	}
	
	private boolean isOpen(){
		
		return m_flag && !TextUtils.isEmpty( getEyEAppId() ) ;
		
	}
	
	public boolean isDataOk( final Activity act ){
		String dataEyeAppId = Util.getDataEyEAppId(act);
		return isOpen() || !TextUtils.isEmpty( dataEyeAppId ) ;
	}
	
	public void init( final Activity act , final boolean mode , final  int upLoadtimes ){
		
		if( Util.fileExits(act,"SDKFile/config.png"))
		{
			try {
				
				String channleName = Util.getChannle(act) + "_" + Util.getAdchannle(act) ;
				
				ReflectUtil.invoke(initFunc, getInstanceFunc ,act, Util.getDataEyEAppId(act) , channleName ,mode,upLoadtimes);
				
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
				
			}
			
			
		}
		else
		{
			LogUtil.e("正式打包版本无配置文件模式==");
			if( isOpen() )
			{
				try {
					
					String channleName = Util.getChannle(act) + "_" + Util.getAdchannle(act) ;
					LogUtil.e("<=========== has DataeyeModule ===========>");
					LogUtil.e("channleName="+channleName);
					ReflectUtil.invoke(initFunc, getInstanceFunc ,act, getEyEAppId() ,  channleName  , mode , upLoadtimes );
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("nono21");
			}
			
		}
	
	}
	
	public void onResume(Activity act){
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				ReflectUtil.invoke(onResumeFunc, getInstanceFunc);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
		}else{
			
			if( isOpen() ){
				try {
					ReflectUtil.invoke(onResumeFunc, getInstanceFunc);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
			
		}
		
	
	}
	
	public void onPause( Activity act ){
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				ReflectUtil.invoke(onPauseFunc, getInstanceFunc);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
		}else{
			
			if( isOpen() ){
				try {
					ReflectUtil.invoke(onPauseFunc, getInstanceFunc);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
			
		}
		
		
	}
	
	public void setDebugMode( Activity act ,   boolean debug ){
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				ReflectUtil.invoke(setDebugModeFunc, getInstanceFunc,debug);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
		}else{
			
			if( isOpen() ){
				try {
					ReflectUtil.invoke(setDebugModeFunc, getInstanceFunc,debug);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
			
		}
	
		
	}
	
	public void onDestory( Activity act ){
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				ReflectUtil.invoke(onDestoryFunc, getInstanceFunc);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
		}else{
			
			if( isOpen() ){
				try {
					ReflectUtil.invoke(onDestoryFunc, getInstanceFunc);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
			
		}
		
	
	}
	
	public void commonFunction( Activity act , Map<String,String> params ){
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				ReflectUtil.invoke(commonFunction, getInstanceFunc,params);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			
		}else{
		
			if( isOpen() ){
				try {
					ReflectUtil.invoke(commonFunction, getInstanceFunc,params);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
		
		}
	
		
	}
	
	public String getDateEyeUid(  Activity act ){
		
		String uid = null ;
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				uid = (String)ReflectUtil.invoke(getDateEyeUidFunc, getInstanceFunc);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
		}else{
			
			if( isOpen() ){
				try {
					uid = (String)ReflectUtil.invoke(getDateEyeUidFunc, getInstanceFunc);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
			
		}
		
		return uid ; 
		
	}
	
	public String getDevicedUid(  Activity act  ){
		
		String uid = null ;
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				uid = (String)ReflectUtil.invoke(getDevicedUidFunc, getInstanceFunc , act);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
		}else{
			
			if( isOpen() ){
				try {
					uid = (String)ReflectUtil.invoke(getDevicedUidFunc, getInstanceFunc , act);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
			
		}
		
		return uid ; 
		
	}
	
	public boolean getParameterBoolean( Activity act , Map<String,String> params ){
		
		boolean   ret = false ;
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				ret = (Boolean)ReflectUtil.invoke(getParameterBooleanFunc, getInstanceFunc,params);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			
		}else{
		
			if( isOpen() ){
				try {
					ret = (Boolean)ReflectUtil.invoke(getParameterBooleanFunc, getInstanceFunc,params);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
		
		}
		
	
		return ret ;
		
	}
	
	public long getParameterLong(  Activity act , Map<String,String> params ){
		
		long   ret =  0 ;
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				ret = (Long)ReflectUtil.invoke(getParameterLongFunc, getInstanceFunc,params);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			
		}else{
		
			if( isOpen() ){
				try {
					ret = (Long)ReflectUtil.invoke(getParameterLongFunc, getInstanceFunc,params);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
		
		}
		
	
		return ret ;		
		
	}
	
	public int getParameterInt( Activity act , Map<String,String> params ){
		
		int   ret =  0 ;
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				ret = (Integer)ReflectUtil.invoke(getParameterIntFunc, getInstanceFunc,params);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			
		}else{
		
			if( isOpen() ){
				try {
					ret = (Integer)ReflectUtil.invoke(getParameterIntFunc, getInstanceFunc,params);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
		
		}
		
	
		return ret ;
		
	}
	
	public String getParameterString( Activity act , Map<String,String> params ){
		
		String   ret = null ;
		
		if( Util.fileExits(act,"SDKFile/config.png")){
			
			try {
				ret = (String)ReflectUtil.invoke(getParameterStringFunc, getInstanceFunc,params);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			
		}else{
		
			if( isOpen() ){
				try {
					ret = (String)ReflectUtil.invoke(getParameterStringFunc, getInstanceFunc,params);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
		
		}
		
		return ret ;
		
	}
	
	
	

}

package com.proxy.sdk.module;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.proxy.util.LogUtil;
import com.proxy.util.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//import cn.sharesdk.framework.i;

public class UmengModule {

	private static UmengModule m_instance = null 	;
	private Class<?>      		  m_classzz  = null 	;
	private static boolean        m_flag     = false 	;
	private static Activity       m_activity = null 	;
	
	private static Object         getInstanceFunc = null ;
	private static Method         initFunc   = null;
	private static Method         onResumeFunc = null ;
	private static Method         onPauseFunc  = null ;
	
	private static Method         onProfileSignInFunc  = null ;
	private static Method         onProfileSignInMutileFunc  = null ;
	private static Method         payFunc  = null ;
	private static Method         payPropsFunc  = null ;
	private static Method         onProfileSignOffFunc  = null ;
	
	private UmengModule(){
		
		initUmeng();
		
	}
	
	private void initUmeng(){
		
		try {
			m_classzz =  Class.forName("com.kuniu.sdk.umeng.UmengSdk");
			if(m_classzz!=null){
				m_flag = true;
			}else{
				return ;
			}
			
			try {
				LogUtil.e("getInstanceFunc");
				getInstanceFunc 			= m_classzz.getMethod("getInstance").invoke(m_classzz);
				LogUtil.e("initFunc");
				initFunc 					= m_classzz.getMethod("onInit",Activity.class);
				onResumeFunc 				= m_classzz.getMethod("onResume");
				onPauseFunc     			= m_classzz.getMethod("onPause");
				onProfileSignInFunc			= m_classzz.getMethod("onProfileSignIn", String.class);
				onProfileSignInMutileFunc	= m_classzz.getMethod("onProfileSignInMutile",String.class,String.class);
				payFunc						= m_classzz.getMethod("pay",double.class,double.class,int.class);
				payPropsFunc				= m_classzz.getMethod("payProps",double.class,String.class,int.class,double.class,int.class);
				onProfileSignOffFunc		= m_classzz.getMethod("onProfileSignOff");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public static UmengModule getInstance(){
		
		if(null==m_instance){
			
			m_instance = new UmengModule();
			
		}else{
			
		}
		
		return m_instance ;
		
	}
	
	public boolean isOpen(){
		
		LogUtil.e("umeng is open???");
		boolean  flag = false ;
		ApplicationInfo ai;
		try {
			
			LogUtil.e("umeng is open11???");
			if(null==m_activity){
				LogUtil.e("umeng is open11 activity is null ");
			}else{
				
				LogUtil.e("umeng is open11 activity is not null ");
				ai = m_activity.getPackageManager().getApplicationInfo(m_activity.getPackageName(), PackageManager.GET_META_DATA);
				
				if(null==ai){
					LogUtil.e("umeng is open11 ai is null ");
				}else{
					LogUtil.e("umeng is open11 ai is not null ");
					Bundle bundle = ai.metaData;
					if(null==bundle){
						flag = false ;
						LogUtil.e("bundle is null");
					}else{
						if(bundle.containsKey("UMENG_APPKEY")&&bundle.containsKey("UMENG_CHANNEL")){
							LogUtil.e("umeng is open!!");
							flag = true ;
						}else{
							LogUtil.e("umeng is null");
							flag = false ;
						}
					}
					
				}
				
			}
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return   flag&&m_flag ;
	}
	
	/**
	 * 友盟数据采集初始化
	 * @param activity
	 */
	public void onInit( final Activity activity ){
		
		LogUtil.log(" Umeng init ");
		m_activity = activity ;
		
		if(isOpen()){
			LogUtil.i("<=========== has UmengModule ===========>");
			ReflectUtil.invoke(initFunc, getInstanceFunc ,activity);
		}else{
			LogUtil.e("<=========== no UmengModule ===========>");
			System.out.println(" no umeng in ");
		}
		
	}
	
	public void onResume(){
		
		if(isOpen()){
			LogUtil.e("<=========== UmengModule onResume ===========>");
			ReflectUtil.invoke(onResumeFunc, getInstanceFunc);
		}else{
			System.out.println(" no umeng in ");
		}
		
	}
	
	public void onPause(){
		
		if(isOpen()){
			LogUtil.e("<=========== UmengModule onPause ===========>");
			ReflectUtil.invoke(onPauseFunc, getInstanceFunc);
		}else{
			System.out.println(" no umeng in ");
		}
		
	}
	
	public void pay( final double money , final double coin , final int source  ){
		
		if(isOpen()){
			LogUtil.e("<=========== UmengModule pay ===========>");
			ReflectUtil.invoke(payFunc, getInstanceFunc,money,coin,source);
		}else{
			System.out.println(" no umeng in ");
		}
		
	}
	
	public void payProps( final double money , final String item ,final int number , final double price , final int source  ){
		
		if(isOpen()){
			LogUtil.e("<=========== UmengModule payProps ===========>");
			ReflectUtil.invoke(payPropsFunc, getInstanceFunc,money,item,number,price,source);
		}else{
			System.out.println(" no umeng in ");
		}
		
	}
	
	public void onProfileSignIn( final String Id ){
		
		if(isOpen()){
			LogUtil.e("<=========== UmengModule onProfileSignIn ===========>");
			ReflectUtil.invoke(onProfileSignInFunc, getInstanceFunc,Id);
		}else{
			System.out.println(" no umeng in ");
		}
		
		
	}
	
	
	public void  onProfileSignInMutile(String Provider, String ID){
	
		if(isOpen()){
			LogUtil.e("<=========== UmengModule onProfileSignInMutile ===========>");
			ReflectUtil.invoke(onProfileSignInMutileFunc, getInstanceFunc,Provider,ID);
		}else{
			System.out.println(" no umeng in ");
		}
		
	}
	
	public void onProfileSignOff(){
		
		if(isOpen()){
			LogUtil.e("<=========== UmengModule onProfileSignOff ===========>");
			ReflectUtil.invoke(onProfileSignOffFunc, getInstanceFunc);
		}else{
			System.out.println(" no umeng in ");
		}
		
	}
	
	
	
	
	
}

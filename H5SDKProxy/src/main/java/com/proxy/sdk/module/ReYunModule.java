package com.proxy.sdk.module;

import android.app.Activity;

import com.proxy.util.LogUtil;
import com.proxy.util.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReYunModule {

	private  static ReYunModule m_instance=null;
	private Class<?>      		  m_classzz  = null ;
	private boolean               m_flag     = false ;
	private static Object         getInstanceFunc = null ;
	private static Method         initFunc   = null;
	private static Method         onResumeFunc = null ;
	private static Method         onStopFunc  = null ;
	private static Method         setRegisterWithAccountIDFunc  = null ;
	private static Method         setLoginSuccessBusinessFunc  = null ;
	private static Method         setPaymentStartFunc  = null ;
	private static Method         setPaymentFunc  = null ;
	private static Method         exitFunc  = null ;
	
	public static ReYunModule getInstance(){
		
		if( null == m_instance ){
			
			m_instance = new ReYunModule();
			
		}
		return m_instance;
	}
	
	private ReYunModule(){
		
		initReYun();
		
	}
	
	
	private void initReYun() {
		try {
			m_classzz = Class.forName("com.kuniu.sdk.reyun.ReYunSDK");
			if(m_classzz!=null){
				m_flag = true;
			}else{
				return ;
			}
			try {
				
				getInstanceFunc 	= m_classzz.getMethod("getInstance").invoke(m_classzz);
				initFunc = m_classzz.getMethod("init", Activity.class);
				onResumeFunc = m_classzz.getMethod("onResume",Activity.class);
				onStopFunc = m_classzz.getMethod("onStop",Activity.class);
				setRegisterWithAccountIDFunc =  m_classzz.getMethod("setRegisterWithAccountID",String.class);
				setLoginSuccessBusinessFunc = m_classzz.getMethod("setLoginSuccessBusiness",String.class);
				setPaymentStartFunc = m_classzz.getMethod("setPaymentStart",String.class,String.class,String.class,float.class);
				setPaymentFunc = m_classzz.getMethod("setPayment",String.class,String.class,String.class,float.class);
				exitFunc = m_classzz.getMethod("exit");
				
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
	
//	private String         getReYunAppId(){
//		
//		String ReYunAppId = "";
//		try {	
//				
//				Field field  = SDKConfig.class.getDeclaredField("ReYunAppId") ;
//				field.setAccessible(true);
//				ReYunAppId = field.get(null).toString();
//		} catch (Exception e) {
//				System.out.println("not found the filed of ReYunAppId  121 ");
//		}
//		return ReYunAppId; 
//		
//	}
	
	public boolean isOpen(){
		
		return true;
		
	}
	
//	public boolean isDataOk( final Activity act ){
//		String reYunAppId = KnUtil.getDataEyEAppId(act);
//		return isOpen() || !TextUtils.isEmpty( reYunAppId ) ;
//	}
	
	public void init( final Activity act){
		LogUtil.e("ReflectUtil.invoke(热云init, getInstanceFunc,act)+++++++isOpen = "+isOpen());
			if( isOpen() )
			{
				try {
					
					ReflectUtil.invoke(initFunc, getInstanceFunc ,act);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("nono21");
			}
	
	}
	
	public void onResume(){
			
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
	
	public void onStop(){
		if(isOpen()){
			try{
				ReflectUtil.invoke(onStopFunc, getInstanceFunc);
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}else{
			System.out.println("nono21");
		}
	}
	
	public void setRegisterWithAccountID(String accoutId){
		LogUtil.e("ReflectUtil.invoke(热云setRegisterWithAccountID, getInstanceFunc,act)+++++++");
		if(isOpen()){
			try{
				ReflectUtil.invoke(setRegisterWithAccountIDFunc, getInstanceFunc,accoutId);
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}else{
			System.out.println("nono21");
		}
	}
	
	public void setLoginSuccessBusiness(String accoutId){
		LogUtil.e("ReflectUtil.invoke(热云setLoginSuccessBusiness, getInstanceFunc,act)+++++++");
		if(isOpen()){
			try{
				ReflectUtil.invoke(setLoginSuccessBusinessFunc, getInstanceFunc,accoutId);
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}else{
			System.out.println("nono21");
		}
	}
	
	public void exit(){
		if(isOpen()){
			try{
				ReflectUtil.invoke(exitFunc, getInstanceFunc);
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}else{
			System.out.println("nono21");
		}
	}
}

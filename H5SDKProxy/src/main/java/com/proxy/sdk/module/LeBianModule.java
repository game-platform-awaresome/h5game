package com.proxy.sdk.module;

import android.app.Activity;

import com.proxy.util.LogUtil;
import com.proxy.util.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LeBianModule {

	private  static LeBianModule m_instance=null;
	private Class<?>      		  m_classzz  = null ;
	private boolean               m_flag     = false ;
	private static Object         getInstanceFunc = null ;
	private static Method         queryUpdate   = null;

	
	public static LeBianModule getInstance(){
		
		if( null == m_instance ){
			
			m_instance = new LeBianModule();
			
		}
		return m_instance;
	}
	
	private LeBianModule(){
		
		initLeBian();
		
	}
	
	
	private void initLeBian() {
		try {
			m_classzz = Class.forName("com.kuniu.sdk.lebian.LeBian");
			if(m_classzz!=null){
				m_flag = true;
			}else{
				return ;
			}
			try {
				
				getInstanceFunc 	= m_classzz.getMethod("getInstance").invoke(m_classzz);
				queryUpdate = m_classzz.getMethod("queryUpdate", Activity.class);
				
				
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
	
	
	private boolean isOpen(){
		
		return m_flag;
		
	}
	
	
	public void queryUpdate(Activity act){
			
			if( isOpen() ){
				try {
					LogUtil.e("ReflectUtil.invoke(queryUpdate, getInstanceFunc,act)+++++++");
					ReflectUtil.invoke(queryUpdate, getInstanceFunc,act);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("nono21");
			}
			
	}
	
}

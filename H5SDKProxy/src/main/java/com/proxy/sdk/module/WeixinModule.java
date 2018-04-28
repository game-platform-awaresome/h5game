package com.proxy.sdk.module;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.proxy.sdk.channel.SDKConfig;
import com.proxy.util.LogUtil;
import com.proxy.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WeixinModule {
	
	private static WeixinModule m_instance = null ;
	private Class<?>      		 m_classzz  = null ;
	private boolean              m_flag     = false ;
	
	private static Object         getInstanceFunc = null ;
	private static Object         getWxapiFunc = null ;
	
	private static Method         initFunc   = null;
	private static Method         sharedTextFunc   = null;
	
	private static Method         sharedImgByteFunc   = null;
	private static Method         sharedImgByteDescFunc   = null;
	private static Method         sharedImgFunc   = null;
	private static Method         sharedImgFromUrlFunc   = null;
	
	private static Method         sharedMusicUrlFunc   = null;
	private static Method         sharedMusicUrlLowFunc   = null;
	
	private static Method         sharedVedioUrlFunc   = null;
	private static Method         sharedVedioUrlLowFunc   = null;
	
	private static Method         sharedUrlFunc   = null;
	
	private static Method         sharedAppDataFunc   = null;
	private static Method         sharedAppDataPathFunc   = null;
	private static Method         sharedAppDataTextFunc   = null;
	
	private static Method         sharedFaceFunc   = null;
	private static Method         sharedFaceByteFunc   = null;
	
	public static WeixinModule getInstance(){
		if( null == m_instance ){
			m_instance = new WeixinModule();
		}
		return m_instance;
	}
	
	private WeixinModule(){
		initWeixin();
	}
	
	public void initWeixin(){
		try {
			m_classzz =  Class.forName("com.kuniu.sdk.weixin.WxSdk");
			if(m_classzz!=null){
				m_flag = true;
			}else{
				return ;
			}
			try {
				getInstanceFunc 	= m_classzz.getMethod("getInstance").invoke(m_classzz);
				getWxapiFunc        = m_classzz.getMethod("getWXapi").invoke(m_classzz);
				initFunc 			= m_classzz.getMethod("init",Activity.class,String.class);
				sharedTextFunc 	    = m_classzz.getMethod("sharedText",String.class);
				
				sharedImgByteFunc   = m_classzz.getMethod("sharedImgByte",Bitmap.class,int.class,int.class) ;
				sharedImgByteDescFunc = m_classzz.getMethod("sharedImgByteDesc",Bitmap.class,String.class,String.class,int.class,int.class) ;
				sharedImgFunc   = m_classzz.getMethod("sharedImgByte",String.class,int.class,int.class) ;
				sharedImgFromUrlFunc   = m_classzz.getMethod("sharedImgFromUrl",String.class,int.class,int.class) ;
				
				sharedMusicUrlFunc   = m_classzz.getMethod("sharedMusicUrl",String.class,String.class,String.class,Bitmap.class) ;
				sharedMusicUrlLowFunc   = m_classzz.getMethod("sharedMusicUrlLow",String.class,Bitmap.class) ;
				
				sharedVedioUrlFunc   = m_classzz.getMethod("sharedVedioUrl",String.class,String.class,String.class,Bitmap.class) ;
				sharedVedioUrlLowFunc   = m_classzz.getMethod("sharedVedioUrlLow",String.class,String.class,String.class);
				
				sharedUrlFunc   = m_classzz.getMethod("sharedUrl",String.class,String.class,String.class,Bitmap.class) ;
				
				sharedAppDataFunc   = m_classzz.getMethod("sharedAppData") ;
				sharedAppDataPathFunc   = m_classzz.getMethod("sharedAppDataPath",String.class) ;
				sharedAppDataTextFunc   = m_classzz.getMethod("sharedAppDataText",String.class,String.class,String.class) ;
				
				sharedFaceFunc   = m_classzz.getMethod("sharedFace",String.class,String.class) ;
				sharedFaceByteFunc   = m_classzz.getMethod("sharedFaceByte",String.class,String.class) ;
						
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
	
	public String  getWXAppId(){
		
		String WXAppId = "";
		try {	
				
				Field field  = SDKConfig.class.getDeclaredField("WXAppId") ;
				field.setAccessible(true);
				WXAppId = field.get(null).toString();
		} catch (Exception e) {
				System.out.println("not found the filed of DataEyeAppId  121 ");
		}
		return WXAppId; 
	}
	
	public boolean isOpen(){
		return m_flag && !TextUtils.isEmpty(getWXAppId()) ;
	}
	
	public void init( final Activity act){
		if(isOpen()){
			LogUtil.i("<=========== has WeixinModule ===========>");
			ReflectUtil.invoke(initFunc, getInstanceFunc ,act,getWXAppId());
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedText( final String text ){
		if(isOpen()){
			ReflectUtil.invoke(sharedTextFunc, getInstanceFunc , text );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public  void sharedImgByte( final Bitmap bmp , final int width , final int height  ){
		if(isOpen()){
			ReflectUtil.invoke(sharedImgByteFunc, getInstanceFunc , bmp , width , height );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public  void sharedImgByteDesc( final Bitmap bmp , final String title , final String desc , final int width , final int height  ){
		if(isOpen()){
			ReflectUtil.invoke(sharedImgByteDescFunc, getInstanceFunc ,  bmp , title , desc , width , height );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public  void sharedImg( final String path , final int width , final int height ){
		if(isOpen()){
			ReflectUtil.invoke(sharedImgFunc, getInstanceFunc , path , width , height );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public  void sharedImgFromUrl( final String imgUrl , final int width , final int height ){
		if(isOpen()){
			ReflectUtil.invoke(sharedImgFromUrlFunc, getInstanceFunc , imgUrl , width , height );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedMusicUrl( final String musicUrl ,final String musicTitle , final String musicDesc , final Bitmap thumb  ){
		if(isOpen()){
			ReflectUtil.invoke(sharedMusicUrlFunc, getInstanceFunc , musicUrl , musicTitle , musicDesc , thumb );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedMusicUrlLow( final String musicUrl ,  final Bitmap thumb  ){
		if(isOpen()){
			ReflectUtil.invoke(sharedMusicUrlLowFunc, getInstanceFunc , musicUrl , thumb  );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedVedioUrl( final String vedioUrl , final String vedioTitle , final String vedioDesc , final Bitmap thumb ){
		if(isOpen()){
			ReflectUtil.invoke(sharedVedioUrlFunc, getInstanceFunc , vedioUrl , vedioTitle , vedioDesc , thumb  );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedVedioUrlLow( final String vedioUrl , final String vedioTitle , final String vedioDesc ){
		if(isOpen()){
			ReflectUtil.invoke(sharedVedioUrlLowFunc, getInstanceFunc , vedioUrl , vedioTitle , vedioDesc );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedUrl( final String webUrl , final String webTitle , final String webDesc , final Bitmap thumb ){
		if(isOpen()){
			ReflectUtil.invoke(sharedUrlFunc, getInstanceFunc , webUrl , webTitle , webDesc , thumb);
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedAppData(){
		if(isOpen()){
			ReflectUtil.invoke(sharedAppDataFunc, getInstanceFunc );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedAppDataPath( final String path ){
		if(isOpen()){
			ReflectUtil.invoke(sharedAppDataPathFunc, getInstanceFunc , path );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedAppDataText( final String text , final String title , final String desc ){
		if(isOpen()){
			ReflectUtil.invoke(sharedAppDataTextFunc, getInstanceFunc ,text, title , desc);
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedFace( final String facePath , final String faceBgPath ){
		if(isOpen()){
			ReflectUtil.invoke(sharedFaceFunc, getInstanceFunc ,facePath,faceBgPath);
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedFaceByte(  final String facePath , final String faceBgPath ){
		if(isOpen()){
			ReflectUtil.invoke(sharedFaceByteFunc, getInstanceFunc ,facePath,faceBgPath);
		}else{
			System.out.println("error function not found");
		}
	}

}

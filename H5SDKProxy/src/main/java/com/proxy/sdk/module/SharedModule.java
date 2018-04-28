package com.proxy.sdk.module;

import android.app.Activity;
import android.graphics.Bitmap;

import com.proxy.util.LogUtil;
import com.proxy.util.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.sharesdk.framework.PlatformActionListener;

public class SharedModule {
	
	private static SharedModule m_instance = null ;
	private Class<?>      		 m_classzz  = null ;
	private boolean              m_flag     = false ;
	
	private static Object         getInstanceFunc = null ;
	private static Method         initFunc   = null;
	private static Method         sharedText     = null ;
	private static Method         sharedImageUrl     = null ;
	private static Method         sharedTextBypassedFunc = null ;
	private static Method         sharedTextFunc = null ;
	private static Method         sharedVedioFunc = null ;
	private static Method         sharedWebFunc = null ;
	private static Method         sharedMusicFunc = null ;
	private static Method         sharedAppsFunc = null ;
	private static Method         sharedFilesFunc = null ; 
	private static Method         sharedEmojiFunc = null ;
	
	private static Method         shared1     = null ;
	private static Method         shared2     = null ;
	
	
	public static SharedModule getInstance(){
		
		if( null==m_instance ){
			m_instance = new SharedModule();
		}
		
		return m_instance ;
	}
	
	private SharedModule(){
		initShared();
	}
	
	private void initShared(){
		try {
			m_classzz =  Class.forName("com.kuniu.sdk.sharedsdk.SharedSdk");
			if(m_classzz!=null){
				m_flag = true;
			}else{
				return ;
			}
			try {
				getInstanceFunc 		= m_classzz.getMethod("getInstance").invoke(m_classzz);
				initFunc 				= m_classzz.getMethod("init",Activity.class);
				sharedText 				= m_classzz.getMethod("sharedText",Activity.class,String.class,String.class,String.class,String.class,PlatformActionListener.class);
				shared1 				= m_classzz.getMethod("shared",Activity.class,String.class,String.class,String.class,String.class,PlatformActionListener.class);
				shared2 				= m_classzz.getMethod("shared",Activity.class,String.class,String.class,String.class,PlatformActionListener.class);
				sharedImageUrl 			= m_classzz.getMethod("sharedImageUrl",Activity.class,String.class,String.class,String.class,String.class,PlatformActionListener.class);
				sharedTextBypassedFunc 	= m_classzz.getMethod("sharedTextBypassed",Activity.class,String.class,String.class,String.class);
				sharedTextFunc 			= m_classzz.getMethod("sharedText",Activity.class,String.class,String.class,String.class,PlatformActionListener.class);
				sharedVedioFunc      	= m_classzz.getMethod("sharedVedio",Activity.class,String.class,String.class,String.class,String.class,PlatformActionListener.class);
				sharedWebFunc			= m_classzz.getMethod("sharedWeb",Activity.class,String.class,String.class,String.class,String.class,PlatformActionListener.class);
				sharedMusicFunc			= m_classzz.getMethod("sharedMusic",Activity.class,String.class,String.class,String.class,String.class,PlatformActionListener.class);
				sharedAppsFunc			= m_classzz.getMethod("sharedApps",Activity.class,String.class,String.class,String.class,String.class,String.class,PlatformActionListener.class);
				sharedFilesFunc			= m_classzz.getMethod("sharedFiles",Activity.class,String.class,String.class,String.class,String.class,String.class,PlatformActionListener.class);
				sharedEmojiFunc  		= m_classzz.getMethod("sharedEmoji",Activity.class,String.class,String.class,Bitmap.class,String.class,String.class,PlatformActionListener.class);
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
	
	public void init( final Activity act ){
		if(isOpen()){
			LogUtil.i("<=========== has sharedSdkModule ===========>");
			ReflectUtil.invoke(initFunc, getInstanceFunc ,act);
		}else{
			System.out.println("error function not found");
		}
	}
	
	public boolean isOpen(){
		return m_flag ;
	}
	
	public void sharedTextBypassed( final Activity act , final String title , final String text ,final String pngPath ){
		if(isOpen()){
			ReflectUtil.invoke(sharedTextBypassedFunc, getInstanceFunc , act , title , text , pngPath );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void shared( final Activity act , String title , String content , String imagePath , final PlatformActionListener listener ){
		
		if(isOpen()){
			ReflectUtil.invoke(shared2, getInstanceFunc , act , title , content , imagePath  , listener );
		}else{
			System.out.println("error function not found");
		}
		
	}
	
	public void shared( final Activity act , String title , String content , String imagePath , String url ,  final PlatformActionListener listener ){
		
		if(isOpen()){
			ReflectUtil.invoke(shared1, getInstanceFunc , act , title , content , imagePath , url , listener );
		}else{
			System.out.println("error function not found");
		}
		
	}
	
	public void sharedText(final Activity act , String title , String content , String imageUrl , String url ,  final PlatformActionListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(sharedText, getInstanceFunc , act , title , content , imageUrl , url , listener );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedImageUrl(final Activity act , String title , String content , String imageUrl , String url ,  final PlatformActionListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(sharedImageUrl, getInstanceFunc , act , title , content , imageUrl , url , listener );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedText( final Activity act , final String title , final String text ,final String pngPath , final PlatformActionListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(sharedTextFunc, getInstanceFunc , act , title , text , pngPath , listener );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedVedio( final Activity act , final String pngPath , final String url , final String title , final String text , final PlatformActionListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(sharedVedioFunc, getInstanceFunc , act , pngPath , url , title , text , listener );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedWeb( final Activity act , final String pngPath , final String url , final String title , final String text , final PlatformActionListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(sharedWebFunc, getInstanceFunc , act , pngPath , url , title , text , listener );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedMusic( final Activity act , final String pngPath , final String url , final String title , final String text , final PlatformActionListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(sharedMusicFunc, getInstanceFunc , act , pngPath , url , title , text , listener );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedApps( final Activity act , final String pngPath , final String url , final String apkPath , final String title , final String text , final PlatformActionListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(sharedAppsFunc, getInstanceFunc , act , pngPath , url , apkPath ,  title , text , listener );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedFiles( final Activity act , final String pngPath , final String url , final String filePath , final String title , final String text , final PlatformActionListener listener){
		if(isOpen()){
			ReflectUtil.invoke(sharedFilesFunc, getInstanceFunc , act , pngPath , url , filePath ,  title , text , listener );
		}else{
			System.out.println("error function not found");
		}
	}
	
	public void sharedEmoji( final Activity act , final String pngPath , final String url , final Bitmap imageData , final String title , final String text , final PlatformActionListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(sharedEmojiFunc, getInstanceFunc , act , pngPath , url , imageData ,  title , text , listener );
		}else{
			System.out.println("error function not found");
		}
	}
	
}

package com.proxy.sdk.module;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.proxy.sdk.channel.SDKConfig;
import com.proxy.util.LogUtil;
import com.proxy.util.ReflectUtil;
import com.proxy.util.Util;
import com.yunva.im.sdk.lib.event.MessageEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class YunwaModule {
	
	private static YunwaModule m_instance = null ;
	private Class<?>           m_classzz  = null ;
	private boolean            m_flag     = false ;
	private static Object      getInstanceFunc = null ;
	private static Method      initDefaultFunc   = null;
	private static Method      initFunc   = null;
	private static Method      onDestoryFunc    = null ;
	private static Method      onSetAppversionFunc    = null ;
	private static Method      onYunWaLoginFunc    = null ;
	private static Method      onYunWaLogoutFunc    = null ;
	private static Method      onSetRecordConfigFunc    = null ;
	private static Method      onStartAudioRecordFunc    = null ;
	private static Method      onStopAudioRecordFunc    = null ;
	private static Method      onPlayAudioFunc    = null ;
	private static Method      onStopPlayAudioFunc    = null ;
	private static Method      onUploadFileFunc    = null ;
	private static Method      onDownloadFileReqFunc    = null ;
	private static Method      onSetVoiceLinstenerFunc    = null ;
	private static Method      onIsCacheFileExistFunc    = null ;
	private static Method      onClearCacheFunc    = null ;
	
	private static Method      onRemoveLinstenerFunc    = null ;
	private static Method      onRemoveListenerSingleFunc    = null ;
	private static Method      onRemoveAllLinstenerFunc    = null ;
	private static Method      onLoginChannelFunc    = null ;
	private static Method      onLogoutChannelFunc    = null ;
	private static Method      onStartAudioRecognizeeFunc    = null ;
	private static Method      ontartAudioRecognizeAndReturnUrlFunc    = null ;
	private static Method      onStartAudioRecognizeUrlFunc    = null ;
	private static Method      onSendChannelVoiceMessageFunc    = null ;
	private static Method      onInitApplicationOnCreateFunc    = null ;
	private static Method      onSendChannelTxtMessageFunc    = null ;
	private static Activity    m_act = null ;
	
	
	
	private void initYunwa(){
		try {
			m_classzz =  Class.forName("com.org.yaya.YunWa");
			if(m_classzz!=null){
				m_flag = true;
			}else{
				return ;
			}
			try {	
				getInstanceFunc 			= m_classzz.getMethod("getInstance").invoke(m_classzz);
				initDefaultFunc     		= m_classzz.getMethod("initComplex",Activity.class,String.class,String.class,boolean.class);
				initFunc            		= m_classzz.getMethod("init",Activity.class,String.class);
				onSetAppversionFunc 		= m_classzz.getMethod("setAppversion",Activity.class);
				onSetRecordConfigFunc 		= m_classzz.getMethod("setRecordConfig", int.class,boolean.class,byte.class);
				onYunWaLoginFunc    		= m_classzz.getMethod("yunWaLogin",String.class,String.class,List.class);
				onDestoryFunc       		= m_classzz.getMethod("destroy");
				
				
				onYunWaLogoutFunc   		= m_classzz.getMethod("logOut");
				
				onStartAudioRecordFunc 		= m_classzz.getMethod("startAudioRecord", String.class,String.class); 
				onStopAudioRecordFunc       = m_classzz.getMethod("stopAudioRecord");
				onPlayAudioFunc             = m_classzz.getMethod("playAudio",String.class,String.class,String.class);
				onStopPlayAudioFunc         = m_classzz.getMethod("stopPlayAudio");
				onUploadFileFunc            = m_classzz.getMethod("uploadFile", String.class,String.class);
				onSendChannelVoiceMessageFunc           = m_classzz.getMethod("sendChannelVoiceMessage",String.class,int.class,String.class,String.class,String.class);
				onInitApplicationOnCreateFunc           = m_classzz.getMethod("initApplicationOnCreate",Application.class,String.class);
				onDownloadFileReqFunc       = m_classzz.getMethod("downloadFileReq",String.class,String.class,String.class);
				onSetVoiceLinstenerFunc     = m_classzz.getMethod("setVoiceLinstener", int.class,MessageEventListener.class);
				onIsCacheFileExistFunc      = m_classzz.getMethod("isCacheFileExist",String.class);
				onClearCacheFunc            = m_classzz.getMethod("clearCache");
				
				onRemoveLinstenerFunc		= m_classzz.getMethod("removeLinstener", int.class , MessageEventListener.class );
				onRemoveListenerSingleFunc	= m_classzz.getMethod("removeListenerSingle", MessageEventListener.class );
				onRemoveAllLinstenerFunc	= m_classzz.getMethod("removeAllLinstener");
				onLoginChannelFunc			= m_classzz.getMethod("loginChannel", String.class,List.class);
				onLogoutChannelFunc		  	= m_classzz.getMethod("logoutChannel");
				onStartAudioRecognizeeFunc	= m_classzz.getMethod("startAudioRecognize",String.class,String.class);
				ontartAudioRecognizeAndReturnUrlFunc	= m_classzz.getMethod("startAudioRecognizeAndReturnUrl", String.class,String.class);
				onStartAudioRecognizeUrlFunc			= m_classzz.getMethod("startAudioRecognizeUrl",String.class,String.class);
				
				
				onSendChannelTxtMessageFunc             = m_classzz.getMethod("sendChannelTxtMessage",String.class,String.class,String.class);
				
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
	
	private YunwaModule(){
		initYunwa();
	}
	
	public static YunwaModule getInstance(){
		if(null==m_instance){
			m_instance = new YunwaModule();
		}else{
			
		}
		return m_instance ;
	}
	
	private String    getYunwaAppId(){
		String YunwaAppId = null ;
		try {	
				Field field  = SDKConfig.class.getDeclaredField("YunwaAppId") ;
				field.setAccessible(true);
				YunwaAppId = field.get(null).toString();
		} catch (Exception e) {
				System.out.println("not found the filed of YunwaAppId  121 ");
		}
		return YunwaAppId ; 
	}
	
	private boolean isOpen( ){
		return m_flag ;
	}
	
	public boolean init( final Activity act , final String dbPath  , final boolean isTest ){
		boolean   	ret = false ;
		String      appId = "" ;
		if( Util.fileExits(act,"SDKFile/config.png")){
			appId = Util.getYunwaAppId(act);
		}else{
			appId = getYunwaAppId();
		}
		if(null==appId || ""==appId || TextUtils.isEmpty(appId) ){
			LogUtil.e(" YunwaAppId error ");
			System.out.println("error function not found");
		}else{
			LogUtil.e("yunya appid:"+appId);
			LogUtil.i("<=========== has YunwaModule ===========>");
			ret=(Boolean) ReflectUtil.invoke(initDefaultFunc,getInstanceFunc,act,appId,dbPath,isTest);
		}
		return ret ;
	}
	
	public boolean init( final Activity act){
		m_act  = act ;
		boolean   	ret = false ;
		String      appId = "" ;
		if( Util.fileExits(act,"SDKFile/config.png")){
			LogUtil.e("config.png 测试版本+");
			appId = Util.getYunwaAppId(act);
		}else{
			LogUtil.e("直接获取字段+");
			appId = getYunwaAppId();
		}
		if(null==appId){
			System.out.println("error function not found");
			ret = false ;
		}else{
			LogUtil.i("<=========== has YunwaModule ===========>");
			ret=(Boolean) ReflectUtil.invoke(initFunc,getInstanceFunc,act,appId);
		}
		return ret ;
	}
	
	public void destroy(){
		if(isOpen()){
			ReflectUtil.invoke(onDestoryFunc, getInstanceFunc);
		}
	}
	
	public boolean setAppversion( final Activity act ){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onSetAppversionFunc, getInstanceFunc,act);
		}
		return ret ; 
	}
	
	public boolean yunWaLogin( final String ywUserinfo , final String serverId , final List<String> channelList ){
		boolean   	ret = false ;
		if(isOpen()){
			if(null==onYunWaLoginFunc){
				LogUtil.e("null==onYunWaLoginFunc");
				ret = false ;
			}else{
				ret = (Boolean) ReflectUtil.invoke(onYunWaLoginFunc, getInstanceFunc,ywUserinfo,serverId,channelList);
			}
			
		}
		return ret ;
	}
	
	public boolean YunwalogOut(){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onYunWaLogoutFunc, getInstanceFunc);
		}
		return 		ret ;
	}
	
	public boolean setRecordConfig( final  int times , final  boolean openVolume , final  byte rate ){
		boolean   	ret = false ;
		if(isOpen()){
			if(null==onSetRecordConfigFunc){
				ret = false ;
			}else{
				ret = (Boolean) ReflectUtil.invoke(onSetRecordConfigFunc, getInstanceFunc,times,openVolume,rate);
			}	
		}
		return 		ret ;
	}
	
	public boolean startAudioRecord( final String filePath , final String ext ){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onStartAudioRecordFunc, getInstanceFunc,filePath,ext);
		}
		return 		ret ;
	}
	
	public boolean stopAudioRecord(){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onStopAudioRecordFunc, getInstanceFunc);
		}
		return 		ret ;
	}
	
	public boolean playAudio( final String url , final String filePath , final String ext ){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onPlayAudioFunc, getInstanceFunc,url,filePath,ext);
		}
		return 		ret ;
	}
	
	public boolean stopPlayAudio(){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onStopPlayAudioFunc, getInstanceFunc);
		}
		return 		ret ;
	}
	
	public boolean uploadFile( final String filePath , final String fileId ){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onUploadFileFunc, getInstanceFunc,filePath,fileId);
		}
		return 		ret ;
	}
	
	public boolean downloadFileReq( final String url , final String filePath , final String fileId ){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onDownloadFileReqFunc, getInstanceFunc,url,filePath,fileId);
		}
		return 		ret ;
	}
	
	public void setVoiceLinstener( final int type , MessageEventListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(onSetVoiceLinstenerFunc, getInstanceFunc,type,listener);
		}
	}
	
	public boolean isCacheFileExist( final String url ){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onIsCacheFileExistFunc,getInstanceFunc,url);
		}
		return 		ret ;
	}
	
	public boolean clearCache(){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onClearCacheFunc,getInstanceFunc);
		}
		return 		ret ;
	}
	
	public boolean isDataOk( final Activity act ){
		String yunWaAppId = Util.getYunwaAppId(act);
		return isOpen() || !TextUtils.isEmpty(yunWaAppId) ;
	}
	
	public void removeLinstener( final int type , MessageEventListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(onRemoveLinstenerFunc, getInstanceFunc,type,listener);
		}
	}
	
	
	public void removeListenerSingle( MessageEventListener listener ){
		if(isOpen()){
			ReflectUtil.invoke(onRemoveListenerSingleFunc, getInstanceFunc,listener);
		}
	}
	
	
	public void removeAllLinstener(){
		if(isOpen()){
			ReflectUtil.invoke(onRemoveAllLinstenerFunc, getInstanceFunc);
		}
	}
	
	
	public boolean loginChannel( final String gameServerId , final List<String> channelList ){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onLoginChannelFunc,getInstanceFunc,gameServerId,channelList);
		}
		return 		ret ;
	}
	
	public boolean  logoutChannel(){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onLogoutChannelFunc,getInstanceFunc);
		}
		return 		ret ;
	}
	
	public boolean startAudioRecognize( String filePath, String tag ){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onStartAudioRecognizeeFunc,getInstanceFunc,filePath,tag);
		}
		return 		ret ;
	}
	
	public boolean startAudioRecognizeAndReturnUrl(String filePath, String tag){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(ontartAudioRecognizeAndReturnUrlFunc,getInstanceFunc,filePath,tag);
		}
		return 		ret ;
	}
	
	public boolean startAudioRecognizeUrl(String url, String tag){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onStartAudioRecognizeUrlFunc,getInstanceFunc,url,tag);
		}
		return 		ret ;
	}
	
	public boolean sendChannelVoiceMessage( final String filePath, final int voiceDuration, final String wildcard, final String txt, final String expand ){
		LogUtil.e(""+filePath+voiceDuration+wildcard+txt+expand);
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onSendChannelVoiceMessageFunc,getInstanceFunc,filePath, voiceDuration, wildcard, txt, expand);
		}
		return 		ret ;
	}
	
	public boolean sendChannelTxtMessage( final String  data,  final String expand ,  final String wildcard ){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onSendChannelVoiceMessageFunc,getInstanceFunc,data,expand,wildcard);
		}
		return 		ret ;
	}
	
	public boolean initApplicationOnCreate( final Application application , final String appId ){
		boolean   	ret = false ;
		if(isOpen()){
			ret = (Boolean) ReflectUtil.invoke(onInitApplicationOnCreateFunc,getInstanceFunc,application,appId);
		}
		return 		ret ;
	}
	
}

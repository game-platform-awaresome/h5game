package com.proxy.sdk;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.proxy.Constants;
import com.proxy.Data;
import com.proxy.OpenSDK;
import com.proxy.Splash;
import com.proxy.bean.FuncButton;
import com.proxy.bean.KnPayInfo;
import com.proxy.listener.SplashListener;
import com.proxy.sdk.channel.SdkChannel;
import com.proxy.sdk.module.BuglyModule;
import com.proxy.sdk.module.DataEyeModule;
import com.proxy.sdk.module.GeTuiPushModule;
import com.proxy.sdk.module.LeBianModule;
import com.proxy.sdk.module.ReYunModule;
import com.proxy.sdk.module.SharedModule;
import com.proxy.sdk.module.UmengModule;
import com.proxy.sdk.module.WeixinModule;
import com.proxy.sdk.module.YunwaModule;
import com.proxy.util.LogUtil;
import com.proxy.util.Util;

import java.util.Map;

public class SdkCenter {
	
	private static SdkCenter instance = null;
	
	private SdkProxy sdkProxy = null;
	private Data kndata = Data.getInstance();
	private DataEyeModule  m_dataEye = DataEyeModule.getInstance();
	private WeixinModule   m_wxModule = WeixinModule.getInstance();
	private SharedModule m_shModule = SharedModule.getInstance();
	private YunwaModule    m_yunwaModule = YunwaModule.getInstance();
	private LeBianModule m_leBian = LeBianModule.getInstance();
	private ReYunModule    m_reYun = ReYunModule.getInstance();
	
	private boolean queried = false;
	private static Activity m_activity = null ;
	
	public SdkProxy getSdkProxy() {
		return sdkProxy;
	}

	public void setSdkProxy(SdkProxy sdkProxy) {
		this.sdkProxy = sdkProxy;
	}

	public static SdkCenter getInstance(){
		if(instance == null)
			instance = new SdkCenter();
		return instance;
	}
	
	public void onCreate(final Activity activity){
		
		m_activity = activity;
		
		//添加buglysdk模块
		BuglyModule.getInstance().onCreate(activity , kndata.getGameInfo().getAdChannel() , OpenSDK.getInstance().getProxyVersion());
		GeTuiPushModule.getInstance().onCreate(activity);
				
		//添加DataEye模块
	    m_dataEye.setDebugMode(activity,true);
		m_dataEye.init(activity,true,60);
		
		//添加ReYun模块
//		m_reYun.init(activity);
		LogUtil.e("热云init --");
		OpenSDK.getInstance().ReYunInit(m_activity);
		
		
		//添加微信分享模块
		m_wxModule.init(activity);
		//添加sharedsdk分享模块
		m_shModule.init(activity);
		
		//添加语音接口
		boolean m_yuyinInit = m_yunwaModule.init(activity);
		if(true == m_yuyinInit){
			LogUtil.e("语音初始化成功ccccccccccccccccccccccccccccc");
			m_yunwaModule.setAppversion(activity);
		}else{
			LogUtil.e("语音初始化失败");
		}
		
		//添加友盟接口(初始化)
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LogUtil.e("umeng初始化");
				UmengModule.getInstance().onInit(activity);
			}
		});
	

		
		setSdkProxy( SdkChannel.getInstance() );

		if(Util.getSplash(activity).equals("1")){
			
		
			LogUtil.e("闪屏=="+Util.getSplash(activity));
			LogUtil.e("gameId:"+kndata.getGameInfo().getGameId());
			LogUtil.e("channel:"+Util.getChannel(activity));
			LogUtil.e("直接优先执行SDK初始化++channel:"+Util.getChannle(activity));
			
			if(Util.getChannle(activity).equals("37wan")||Util.getChannle(activity).equals("")){
				
				sdkProxy.onCreate(activity);
				
			}else{
				 
				//静态闪屏，获取打包系统配置闪屏图片  
					
					if(kndata.getGameInfo().getGameId().equals("guhuozainew")){
							Splash.getInstance(activity).splash(true , new SplashListener() {
							
							@Override
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								activity.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										sdkProxy.onCreate(activity);
									}
								});
								
							}
							
							@Override
							public void onFail(Object result) {
								// TODO Auto-generated method stub
								
							}
						});
						
					}else{
						LogUtil.log("splashSDK..............");
					Splash.getInstance(activity).splashSDK(new SplashListener() {
						
						@Override
						public void onSuccess(Object result) {
							// TODO Auto-generated method stub
							LogUtil.log("splashSDK..............");
							activity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									sdkProxy.onCreate(activity);
								}
							});
							
						}
						
						@Override
						public void onFail(Object result) {
							// TODO Auto-generated method stub
							
						}
					} );
					}
				
					
			}
		}else{
			
			sdkProxy.onCreate(activity);
			
		}
		
		
		
			
	}
	
	public boolean canEnterGame() {
		return sdkProxy.canEnterGame();
	}

	public void onEnterGame(Map<String, Object> data) {
		sdkProxy.onEnterGame(data);
		try {
			
			String userId1 	   = data.get(Constants.USER_ID)!=null?data.get(Constants.USER_ID).toString():"";
			//buglysdkmodule 设置 userId
			BuglyModule.getInstance().setUserId( String.valueOf(userId1));
			UmengModule.getInstance().onProfileSignIn(userId1);
			
			String accountTpye = data.get(Constants.USER_ACCOUT_TYPE)!=null?data.get(Constants.USER_ACCOUT_TYPE).toString():"";
			String sex    	   = data.get(Constants.USER_SEX)!=null?data.get(Constants.USER_SEX).toString():"";
			String age		   = data.get(Constants.USER_AGE)!=null?data.get(Constants.USER_AGE).toString():"";
			String gameServer  = data.get(Constants.SERVER_NAME)!=null?data.get(Constants.SERVER_NAME).toString():"";
			String level       = data.get(Constants.USER_LEVEL)!=null?data.get(Constants.USER_LEVEL).toString():"";
			
			String sceneId     = data.get(Constants.SCENE_ID)!=null?data.get(Constants.SCENE_ID).toString():"";
			String isNewRole     = data.get(Constants.IS_NEW_ROLE)!=null?data.get(Constants.IS_NEW_ROLE).toString():"";
			LogUtil.e("sceneId = "+sceneId+"   isNewRole = " +isNewRole);
			/*ReYun 数据上报*/
			//进入游戏场景
			if(sceneId == null || sceneId.equals("")){
				
			}else if(sceneId == "1"||sceneId.equals("1")){
//				KnLog.e("热云登录上报 userid = "+userId1);
//				ReYunModule.getInstance().setLoginSuccessBusiness(userId1);
//				if(isNewRole == "true" || isNewRole.equals("true")){
//					ReYunModule.getInstance().setRegisterWithAccountID(userId1);
//				}
				LogUtil.e("热云login --");
				OpenSDK.getInstance().ReYunSetLoginSuccessBusiness(userId1);
			}else if(sceneId == "2"||sceneId.equals("2")){
//				KnLog.e("热云注册上报 userid = "+userId1);
//				ReYunModule.getInstance().setRegisterWithAccountID(userId1);
				LogUtil.e("热云Register --");
				OpenSDK.getInstance().ReYunSetRegisterWithAccountID(userId1);
				OpenSDK.getInstance().ReYunSetLoginSuccessBusiness(userId1);
			}
			
			
			
			String userId      = "";
			if(null==Data.getInstance().getUser()){
			}else{
				userId = Data.getInstance().getUser().getOpenId() ;
			}
			
			
			
			if(Data.getInstance().isNewMode()){
				//dataeye        login数据上报
				OpenSDK.getInstance().loginDe(userId);
				//dataeye		进入游戏数据上报
				OpenSDK.getInstance().enterGame(accountTpye, sex, age, gameServer, level);
				//dateeye		游戏玩家等级变化
				OpenSDK.getInstance().levelChangeDe(level);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showUserCenter() {
		sdkProxy.showUserCenter();
	}
	
	
	public void onGameLevelChanged(int newlevel) {
		sdkProxy.onGameLevelChanged(newlevel);
		//dateeye		游戏玩家等级变化
		if(Data.getInstance().isNewMode()){
			OpenSDK.getInstance().levelChangeDe(Integer.toString(newlevel));
		}
	}

	public void onResume() {
		
		if(m_activity!=null){
			UmengModule.getInstance().onResume();
//			ReYunModule.getInstance().onResume();
			OpenSDK.getInstance().ReYunOnResume();
			if(Data.getInstance().isNewMode()){
				if(DataEyeModule.getInstance().isDataOk(m_activity)){
					LogUtil.e("deresume");
					m_dataEye.onResume( m_activity );
				}
			}
		}
		
		if(sdkProxy!=null){
			sdkProxy.onResume();	
		}
	}

	public void onPause() {
		
		if(m_activity!=null){
			UmengModule.getInstance().onPause();
			if(Data.getInstance().isNewMode()){
				if(DataEyeModule.getInstance().isDataOk(m_activity)){
					LogUtil.e("depause");
					m_dataEye.onPause( m_activity );
				}
			}
		}
		
		
		
		sdkProxy.onPause();
	}

	public void onStop() {
		if(m_activity!=null){
			OpenSDK.getInstance().ReYunOnStop();
		}
		sdkProxy.onStop();
	}

	public void onDestroy() {
		
		if(m_activity!=null){
			if(Data.getInstance().isNewMode()){
				if(DataEyeModule.getInstance().isDataOk(m_activity)){
					LogUtil.e("dedestroy");
					m_dataEye.onDestory( m_activity );
				}
			}
//			ReYunModule.getInstance().exit();
			OpenSDK.getInstance().ReYunExit();
		}
		sdkProxy.onDestroy();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		sdkProxy.onActivityResult(requestCode, resultCode, data);
	}

	public void onRestart() {
		sdkProxy.onRestart();
	}
	
	public void onConfigurationChanged(Configuration newConfig){
		sdkProxy.onConfigurationChanged(newConfig);
	}

	public void login(final Activity activity,final Map<String, Object> params) {
		//乐变 热更新接口
		if (!queried) {
			queried = true;
			m_leBian.queryUpdate(activity);
		}
		LogUtil.e("登录接口login ++");
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				
				
				sdkProxy.login(activity , params);
			}
		});
	}

	public void pay(final Activity activity ,final KnPayInfo knPayInfo) {
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				sdkProxy.pay(activity , knPayInfo);
			}
		});
	}

	public boolean hasThirdPartyExit() {
		return sdkProxy.hasThirdPartyExit();
	}

	public boolean hasSwitchUserView() {
		return sdkProxy.hasSwitchUserView();
	}

	public void onThirdPartyExit() {
		kndata.getGameActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				sdkProxy.onThirdPartyExit();
			}
		});
	}

	public String getChannelVersion() {
		return sdkProxy.getChannelVersion();
	}

	public String getChannelName() {
		return sdkProxy.getChannelName();
	}

	public void onNewIntent(Intent intent) {
		sdkProxy.onNewIntent(intent);
	}

	public void onStart() {
		sdkProxy.onStart();
	}
	
	public FuncButton[] getSettingItems(){
		return sdkProxy.getSettingItems();
	}
	
	public void callSettingView() {
		sdkProxy.callSettingView();
	}

	public void onSaveInstanceState(Bundle outState) {
		sdkProxy.onSaveInstanceState(outState);
	}
	
	public boolean getuiIsOpen(){
		return GeTuiPushModule.getInstance().isOpen();
	}
	
	public String getGetuiClientId(){
		return GeTuiPushModule.getInstance().getClientId();
	}
	
	public void turnOnorOffGeTuiPush(boolean on){
		GeTuiPushModule.getInstance().turnOnorOffPush(on);
	}

	public String getAdChannel() {
		return sdkProxy.getAdChannel();
	}
	
	public void pushData( final Activity activity , final Map<String,Object> data ){
		
		activity.runOnUiThread( new Runnable() {
			
			@Override
			public void run() {
		
				sdkProxy.pushData(activity, data);
				
			}
		} );
		
	}
	
	
	public void pushActivation( final  Activity activity , final Map<String,Object> data ){
		
		activity.runOnUiThread( new Runnable() {
				
				@Override
				public void run() {
			
					sdkProxy.pushActivation(activity,data);
					
				}
			} );
		
	}
	
	public void activation(final  Activity activity){
		activity.runOnUiThread( new Runnable() {
			
			@Override
			public void run() {
		
				sdkProxy.activation(activity);
				
			}
		} );
	}
	
	public void finish(){
		sdkProxy.finish();
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
		sdkProxy.onWindowFocusChanged(hasFocus);
	}
	
	public void switchAccount(){
		sdkProxy.switchAccount();
	}
	
	public void logout(){
		sdkProxy.logout();
	}
	
	public void onBackPressed() {
		// TODO Auto-generated method stub
		sdkProxy.onBackPressed();
	}
	
}

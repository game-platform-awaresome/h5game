package com.proxy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.proxy.activity.Invitation;
import com.proxy.activity.StartWebView;
import com.proxy.bean.FuncButton;
import com.proxy.bean.GameInfo;
import com.proxy.bean.KnPayInfo;
import com.proxy.listener.BaseListener;
import com.proxy.listener.ExitListener;
import com.proxy.listener.InitListener;
import com.proxy.listener.InvitationListener;
import com.proxy.listener.LoginListener;
import com.proxy.listener.LogoutListener;
import com.proxy.listener.PayListener;
import com.proxy.listener.PushActivationListener;
import com.proxy.listener.PushDataListener;
import com.proxy.listener.RoleReportListener;
import com.proxy.listener.WeixinListener;
import com.proxy.sdk.SdkCenter;
import com.proxy.sdk.module.DataEyeModule;
import com.proxy.sdk.module.SharedModule;
import com.proxy.sdk.module.YunwaModule;
import com.proxy.service.HttpService;
import com.proxy.task.CommonAsyncTask;
import com.proxy.util.DeviceUtil;
import com.proxy.util.LogUtil;
import com.proxy.util.Util;
import com.proxy.util.WxTools;
import com.yunva.im.sdk.lib.event.MessageEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.sharesdk.framework.PlatformActionListener;

//import android.util.Log;
//import com.reyun.sdk.ReYunChannel;

/**
 * 酷牛sdk
 * @author Administrator
 *
 */
public class OpenSDK {

	private static OpenSDK instance = null;
	private SdkCenter sdkCenter = SdkCenter.getInstance();
	private Listener knListener = Listener.getInstance();
	private static Activity   mActivity = null ;
	private boolean isInited = false;
	boolean isActive  = true;
	
	
	public static OpenSDK getInstance() {

		if (instance == null)
			instance = new OpenSDK();

		return instance;
	}
	
	/**
	 * 是否可以进入游戏
	 * @return		false	不可以进入游戏</br>
	 * 				true	可以进入游戏
	 */
	public boolean canEnterGame() {
		return sdkCenter.canEnterGame();
	}
	
	public boolean isInited() {
		return isInited;
	}
	
	/**
	 * proxy初始化
	 * @param activity 游戏的主Activity
	 */
	public void init(final Activity activity ,InitListener initlistenr){
		
		mActivity = activity ;
		GameInfo mgameinfo = new GameInfo();

		sdkLogInit(mActivity);
		
		
		LogUtil.log("init");
		Util.writeErrorLog("init");
		Util.writeInfoLog("SDK接入测试结果如下:");
		
		if(null==initlistenr){
			Util.writeInfoLog("\tSDK初始化方法init中初始化监听不能为空");
		}
		
		LogUtil.setLogEnable(true);
		knListener.setInitListener(initlistenr);
		Data.getInstance().setGameActivity(activity);
		LogUtil.e("setappkey :"+mgameinfo.getAppKey());
		
		Data.getInstance().setGameInfo(mgameinfo);
		this.onCreate(activity);
		
		isInited = true ;
		
		
		
		
		
		
	}
	
	//	开始创建SDK检测工具日志
	private void sdkLogInit( Activity activity ){
		Util.sdklog( activity , Util.LOGFILEPATH ,  Util.LOGFILE , Util.RESULT_INFO , Util.RESULT_INFO1 , Util.RESULT_INFO2 ) ;
	}
	
	/**
	 * 游戏进入游戏首页的时候调用
	 * @param data
	 */
	public void onEnterGame(Map<String, Object> data) {
		
		LogUtil.e(" func is entergame data: "+data);
		
		Util.writeErrorLog("onEnterGame");
		sdkCenter.onEnterGame(data);
		
		Util.writeErrorLog("onEnterGame");
		Util.writeInfoLog("SDK接入测试结果如下:");
	
		Object userId = data.get(Constants.USER_ID);
		if(null==userId||""==userId){
			Util.writeInfoLog("\t在调用onEnterGame方法中键名uid对应的值不能为空");
		}
		Object serverId  = data.get(Constants.SERVER_ID);
		if(null == serverId  || "" == serverId ){
			Util.writeInfoLog("\t在调用onEnterGame方法中键名sid对应的值不能为空");
		}
		Object userLv  = data.get(Constants.USER_LEVEL);
		if(null == userLv  || "" == userLv ){
			Util.writeInfoLog("\t在调用onEnterGame方法中键名userLv对应的值不能为空");
		}
		Object serverName   = data.get(Constants.SERVER_NAME);
		if(null == serverName   || "" == serverName  ){
			Util.writeInfoLog("\t在调用onEnterGame方法中键名serverName对应的值不能为空");
		}
		Object roleName   = data.get(Constants.ROLE_NAME);
		if(null == roleName   || "" == roleName  ){
			Util.writeInfoLog("\t在调用onEnterGame方法中键名roleName对应的值不能为空");
		}
		Object vipLevel   = data.get(Constants.VIP_LEVEL);
		if(null == vipLevel   || "" == vipLevel  ){
			Util.writeInfoLog("\t在调用onEnterGame方法中键名vipLevel对应的值不能为空");
		}
		Object role_id   = data.get(Constants.ROLE_ID);
		if(null == role_id   || "" == role_id  ){
			Util.writeInfoLog("\t在调用onEnterGame方法中键名role_id对应的值不能为空");
		}
		Object diamondLeft   = data.get(Constants.BALANCE);
		if(null == diamondLeft   || "" == diamondLeft  ){
			Util.writeInfoLog("\t在调用onEnterGame方法中键名diamondLeft对应的值不能为空");
		}
	}
	
	/**
	 * 游戏角色等级改变的时候调用
	 * @param newlevel		最新的角色等级
	 */
	public void onGameLevelChanged(int newlevel) {
		sdkCenter.onGameLevelChanged(newlevel);
	}
	
	//显示用户中心    聚宝盆渠道
	public void showUserCenter() {
		sdkCenter.showUserCenter();
	}
	
	/**
	 * 		微信分享
	 */
	
	public void setWeixinListener( WeixinListener listener ){
		knListener.setWeixinListener(listener);
		
		if(null==listener){
			Util.writeInfoLog("\tSDK初始化方法setWeixinListener中初始化监听不能为空");
		}
	}
	
	public WeixinListener getWeixinListener(){
		return knListener.getWeixinListener();
	}
	
	/**
	 * 设置登陆回调
	 * @param listener
	 */
	public void setLogoinListener(LoginListener listener) {
		Util.writeErrorLog("setLogoinListener");
		if(null==listener){
			Util.writeInfoLog("\tSDK登录方法中监听不能为空");
		}
		knListener.setLoginListener(listener);
	}
	
	/**
	 * 设置注销回调
	 * @param listener
	 */
	public void setLogoutListener(LogoutListener listener) {
		Util.writeErrorLog("setLogoutListener");
		knListener.setLogoutListener(listener);
	}
	
	/**
	 * 设置初始化回调
	 * @param listener
	 */
	public void setInitListener(InitListener listener) {
		Util.writeErrorLog("setInitListener");
		knListener.setInitListener(listener);
	}
	
	/**
	 * 设置支付回调
	 * @param listener
	 */
	public void setPayListener(PayListener listener) {
		Util.writeErrorLog("setPayListener");
		
		if(null == listener){
			Util.writeInfoLog("\tSDK支付方法中监听不能为空");
		}
		knListener.setPayListener(listener);
	}
	
	/**
	 * 设置 退出回调
	 * @param listener
	 */
	public void setExitListener(ExitListener listener) {
		Util.writeErrorLog("setExitListener");
		knListener.setExitListener(listener);
	}

	/**
	 * 上报数据回调
	 * @param listener
	 */
	public void setRoleReportListener(RoleReportListener listener){
		Util.writeErrorLog("setRoleReportListener");
		knListener.setRoleReportListener(listener);
	}


	private void onCreate(final Activity activity) {
		

		// 这里必须是我们的游戏log初始化完毕之后才能够执行
		sdkCenter.onCreate(activity);
		
	}

	public void onResume() {
		Util.writeErrorLog("onResume");
		sdkCenter.onResume();
	}

	public void onPause() {
		Util.writeErrorLog("onPause");
		sdkCenter.onPause();
	}
	
	public void onStop() {
		Util.writeErrorLog("onStop");
		sdkCenter.onStop();
	}
	
	public void onRestart(){
		Util.writeErrorLog("onRestart");
		sdkCenter.onRestart();
	}

	public void onDestroy() {
		Util.writeErrorLog("onDestroy");
		sdkCenter.onDestroy();
	}
	
	public void onStart(){
		Util.writeErrorLog("onStart");
		sdkCenter.onStart();
	}
	
	public void onNewIntent(Intent intent) {
		sdkCenter.onNewIntent(intent);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		sdkCenter.onActivityResult(requestCode, resultCode, data);
	}
	
	public void onConfigurationChanged(Configuration newConfig){
		sdkCenter.onConfigurationChanged(newConfig);
	}
	
	public void onSaveInstanceState(Bundle outState) {
		sdkCenter.onSaveInstanceState(outState);
	}
	
	/**
	 * 返回 proxy 版本号
	 * @return  proxy 版本号
	 */
	public String getProxyVersion(){
		return "1.0.0";
	}
	
	/**
	 * @return sdk渠道的版本号
	 */
	public String getChannelVersion(){
		return sdkCenter.getChannelVersion();
	}
	
	/**
	 * 登录
	 * @param activity		游戏的Activity
	 */
	public void login(Activity activity){
		Util.writeErrorLog("login");
		LogUtil.e("login()+++");
		
	
		sdkCenter.login(activity , null);
		Util.writeErrorLog("login");
		Util.writeInfoLog("SDK接入测试结果如下:");
	}
	
	/**
	 * 支付
	 * @param activity		游戏的Activity
	 * @param knPayInfo	 	支付信息
	 */
	public void pay( Activity activity , KnPayInfo knPayInfo) {
		
		Util.writeErrorLog("pay");
		Util.writeInfoLog("SDK接入测试结果如下:");
		
		sdkCenter.pay(activity , knPayInfo);
	}
	
	/**
	 * @return false:第三方sdk没有自己的退出</br>
	 * 		   true :第三方sdk拥有自己的退出
	 */
	public boolean hasThirdPartyExit() {
		LogUtil.e("hasThirdPartyExit()+++"+sdkCenter.hasThirdPartyExit());
		return sdkCenter.hasThirdPartyExit();
	}
		
	/**
	 * 点击返回按钮时，调用的第三方退出接口
	 */
	public void onThirdPartyExit(){
		sdkCenter.onThirdPartyExit();
		LogUtil.e("onThirdPartyExit()+++");
		Util.writeErrorLog("onThirdPartyExit");
		Util.writeInfoLog("SDK接入测试结果如下:");
	}
	
	/**
	 * 
	 * @param enable			true 可以打印出调试信息，false则不
	 */
	public void isDebugModel(boolean enable){
		LogUtil.mLogEnable = enable;
	}
	
	/**
	 * @return 返回渠道名称
	 */
	public String getChannelName(){
		return sdkCenter.getChannelName();
	}
	
	public String getAdChannel(){
		return sdkCenter.getAdChannel();
	}
	
	/**
	 * 设置buglyAppid</br>
	 * 如果不需要接入buglysdk则不需要填写
	 * @param appId 申请到的buglysdk的AppID
	 */
	public void setBuglySdkAppId(String appId){
		
	}
	/**
	 * 
	 * @return 返回null则表示没有sdk设置页面
	 */
	public FuncButton[] getSettingItems(){
		return sdkCenter.getSettingItems();
	}
	
	/**
	 * 调用sdk设置页面
	 */
	public void callSettingView() {
		sdkCenter.callSettingView();
	}
	
	public boolean getuiIsOpen(){
		return sdkCenter.getuiIsOpen();
	}
	
	public String getGetuiClientId(){
		return sdkCenter.getGetuiClientId();
	}
	
	public void turnOnorOffGeTuiPush(boolean on){
		sdkCenter.turnOnorOffGeTuiPush(on);
	}
	
	/*
	
	此处添加接口:pushData(
		ip,
		phone_type,
		sign,
		gameId,
		appKey,
		imei,
		platform,
		ad_channel,
	)
	
	sign = md5(gameId+appKey+imei)
	
	完成数据上传成功或者完毕之后游戏方可进行登录(或者说完成初始化成功之后方可进行游戏登录)
	 
	 */
	
	public void pushData( final  Activity activity ,  final Map<String,Object> data ){
		
		HttpService.doPushData(activity,data, new BaseListener() {
			
			@Override
			public void onSuccess(Object result) {
				
				LogUtil.e("result:"+result.toString());
				
			}
			
			@Override
			public void onFail(Object result) {
				
				LogUtil.e("result:"+result.toString());
				
			}
		} );
	
		sdkCenter.pushData(activity, data);
	}
	
	public void setPushDataListener(PushDataListener listener){
		knListener.setPushDataListener(listener);
	}
	
	public void setActivationListener( PushActivationListener listener){
		knListener.setPushActivationListenr(listener);
	}
	
	//	激活码礼品
	public void pushActivation( final  Activity activity , final Map<String,Object> data ){
		
		sdkCenter.pushActivation(activity,data);
		Object userId = data.get(Constants.USER_ID);
		if(null==userId||""==userId){
			Util.writeInfoLog("\t在调用pushActivation方法中键名uid对应的值不能为空");
		}
		Object serverId  = data.get(Constants.SERVER_ID);
		if(null == serverId  || "" == serverId ){
			Util.writeInfoLog("\t在调用pushActivation方法中键名sid对应的值不能为空");
		}
		Object userLv  = data.get(Constants.SERVER_ID);
		if(null == userLv  || "" == userLv ){
			Util.writeInfoLog("\t在调用pushActivation方法中键名userLv对应的值不能为空");
		}
		Object serverName   = data.get(Constants.SERVER_ID);
		if(null == serverName   || "" == serverName  ){
			Util.writeInfoLog("\t在调用pushActivation方法中键名serverName对应的值不能为空");
		}
		Object roleName   = data.get(Constants.SERVER_ID);
		if(null == roleName   || "" == roleName  ){
			Util.writeInfoLog("\t在调用pushActivation方法中键名roleName对应的值不能为空");
		}
		Object vipLevel   = data.get(Constants.SERVER_ID);
		if(null == vipLevel   || "" == vipLevel  ){
			Util.writeInfoLog("\t在调用pushActivation方法中键名vipLevel对应的值不能为空");
		}
		Object role_id   = data.get(Constants.SERVER_ID);
		if(null == role_id   || "" == role_id  ){
			Util.writeInfoLog("\t在调用pushActivation方法中键名role_id对应的值不能为空");
		}
		Object diamondLeft   = data.get(Constants.SERVER_ID);
		if(null == diamondLeft   || "" == diamondLeft  ){
			Util.writeInfoLog("\t在调用pushActivation方法中键名diamondLeft对应的值不能为空");
		}
		
	}
	
	public void activation( final  Activity activity ){
		sdkCenter.activation(activity);
	}

		
	public void finish(){
		sdkCenter.finish();
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
		sdkCenter.onWindowFocusChanged(hasFocus);
	}
	
	public void switchAccount(){
		LogUtil.e("switchAccount+++++");
		sdkCenter.switchAccount();
	}
	
	public void logout(){
		LogUtil.e("游戏登出--");
		sdkCenter.logout();
	}
	
	public void activationGame( Activity activity){
		
		//		游戏激活
		
		GameInfo mGameInfo = Data.getInstance().getGameInfo();
		Map<String, Object>  data = new HashMap<String, Object>();
		LogUtil.e("初始化"+mGameInfo.getGameId()) ;
		data.put("game_id",mGameInfo.getGameId());
		data.put("ip", DeviceUtil.getMacAddress());
		data.put("app_key",mGameInfo.getAppKey());
		data.put("imei", DeviceUtil.getDeviceId());
		data.put("platform",mGameInfo.getPlatform());
		LogUtil.e("初始化"+mGameInfo.getAdChannel()) ;
		data.put("ad_channel",mGameInfo.getAdChannel());
		data.put("phone_type", DeviceUtil.getPhoneType());
		LogUtil.e("初始化"+mGameInfo.getChannel()) ;
		data.put("channel",mGameInfo.getChannel());
		
		pushData(activity,data);
		
	}
	
	//跳转激活码页面
	public void invitation( final Activity activity , InvitationListener listener ){
		Listener.getInstance().setInvivationListener(listener);
		Intent intent = new Intent(activity.getApplicationContext(), Invitation.class);
		activity.startActivity(intent);	
	}
	//	是否打开Debug模式
	public void DebugOpen( boolean flag ){
		LogUtil.setLogEnable(flag);
	}
	
//	/*ReYun相关对外接口 */
//	/*	loginRy(String accountId)   玩家成功登入账号*/
//	public void loginRy( String accountId ){
//		ReYunModule.getInstance().setLoginSuccessBusiness(accountId);
//	}
//	
//	/*玩家成功注册接口*/
//	public void RegisterRy( String accountId ){
//		ReYunModule.getInstance().setRegisterWithAccountID(accountId);
//	}
	
	/*ReYun相关对外接口 */
	public void ReYunInit( final Activity act){
		Log.e("SDKProxy","热云初始化");
//		ReYunChannel.initWithKeyAndChannelId(act.getApplicationContext(),"");
	}
	
	public void ReYunOnResume() {
		// TODO Auto-generated method stub
		if( !isActive){  // app 从后台唤醒到前台
//			ReYunChannel.postSessionData();
			Log.e("SDKProxy","热云onResume");
//			ReYunChannel.startHeartBeat(mActivity);
			isActive =true;
		}
	}
	
	public void ReYunOnStop() {
		// TODO Auto-generated method stub
//		if(ReYunChannel.isAppOnForeground() == false){
//			Log.e("SDKProxy","热云onStop");
//			//app 进入后台
//			isActive = false;
//		}
	}
	
	public void ReYunSetRegisterWithAccountID(String accoutId){
		LogUtil.e("热云setRegisterWithAccountID  accoutId = "+accoutId);
//		ReYunChannel.setRegisterWithAccountID(accoutId);
	}
	
	public void ReYunSetLoginSuccessBusiness(String accoutId){
		LogUtil.e("热云setLoginSuccessBusiness  accoutId = "+accoutId);
//		ReYunChannel.setLoginSuccessBusiness(accoutId);
	}
	
	public void ReYunExit(){
//		ReYunChannel.exitSdk();
	}
	
	/* DataEyE相关对外接口	*/
	
	/*	loginDe(String accountId)   玩家成功登入账号*/
	public void loginDe( String accountId ){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DELOGIN);
			params.put(DEConstants.DEACCOUNT,accountId);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
		
	}
	
	/*	logoutDe( )*/
	private void logoutDe(){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DELOGOUT);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	public void isSupportNew( final boolean mode ){
		Util.writeErrorLog("isSupportNew");
		Data.getInstance().setNewMode(mode);
	}
	
	public void doDebug( final boolean mode ){
		Data.getInstance().setDeBugMode(mode);
	}
	
	public void logOut(){
		Util.writeErrorLog("logOut");
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DELOGOUT);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	/*	enterGame( String accountType , String sex ,String age , String gameServer , String level ) */
	public void enterGame( String accountType , String sex ,String age , String gameServer , String level ){
		Util.writeErrorLog("enterGame");
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DEENTERGAME);
			params.put(DEConstants.DEACCOUNTYPE,accountType);
			params.put(DEConstants.DESEX,sex);
			params.put(DEConstants.DEAGE,age);
			params.put(DEConstants.DEGAMESERVER,gameServer);
			params.put(DEConstants.DELEVEL,level);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	/*	levelChangeDe( String level ) */
	public void levelChangeDe( String level ){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DELEVELCHANGE);
			params.put(DEConstants.DELEVEL,level);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	/*	addTagDe( String nodeTag , String subTag ) */
	public void addTagDe( String nodeTag , String subTag ){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DEADDTAG);
			params.put(DEConstants.DENTAG,nodeTag);
			params.put(DEConstants.DESTAG,subTag);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	/*	removeTagDe( String nodeTag , String subTag ) */
	public void removeTagDe( String nodeTag , String subTag ){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DEREMOVETAG);
			params.put(DEConstants.DENTAG,nodeTag);
			params.put(DEConstants.DESTAG,subTag);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	/*	levelBeginDe( String levelId ) */
	public void levelBeginDe( String levelId ){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DELEVELBEGIN);
			params.put(DEConstants.DELEVELID,levelId);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	/*	levelCompleteDe( String levelId ) */
	public void levelCompleteDe( String levelId ){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DELEVELCOMPLETE);
			params.put(DEConstants.DELEVELID,levelId);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	/*	levelFaildDe( String levelId , String faildReason  ) */
	public void levelFaildDe( String levelId , String faildReason  ){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DELEVELFAILD);
			params.put(DEConstants.DELEVELID,levelId);
			params.put(DEConstants.DEREASON,faildReason);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	/*	taskBegin( String taskId , String playerType ) */
	public void taskBegin( String taskId , String playerType ){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DETASKBEGIN);
			params.put(DEConstants.DETASKID,taskId);
			params.put(DEConstants.DEPLAYERTYPE,playerType);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	/*	taskCompleteDe( String taskId  ) */
	public void taskCompleteDe( String taskId  ){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DETASKCOMPLETE);
			params.put(DEConstants.DETASKID,taskId);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	/*	taskFaild( String taskId , String faildReason ) */
	public void taskFaild( String taskId , String faildReason ){
		if(DataEyeModule.getInstance().isDataOk(mActivity)){
			Map<String,String> params = new HashMap<String, String>();
			params.put(DEConstants.DETYPE, DEConstants.DETASKFAILD);
			params.put(DEConstants.DETASKID,taskId);
			params.put(DEConstants.DEREASON,faildReason);
			DataEyeModule.getInstance().commonFunction( mActivity , params );
		}
	}
	
	
	
	
	
	//直接调用的方式
	public void sharedImageWx( final Activity act , final String title , final String text ,final String pngPath , final PlatformActionListener listener ){
		SharedModule.getInstance().shared(act, title, text, pngPath, listener);
	}
	
	public void sharedImageWx( final Activity act , String title , String content , String imagePath , String url ,  final PlatformActionListener listener ){
		SharedModule.getInstance().shared(act, title, content , imagePath , url ,  listener);
	}
	
	
	public void sharedTextWx( final Activity act , final String title , final String text ,final String pngPath , final PlatformActionListener listener ){
		SharedModule.getInstance().sharedText(act, title, text, pngPath, listener);
	}
	

	public void shared(final Activity act , String title , String content , String imageUrl , String url ,  final PlatformActionListener listener ){
		SharedModule.getInstance().shared(act, title, content, imageUrl, url, listener);
	}
	
	public void sharedImageUrl(final Activity act , String title , String content , String imageUrl , String url ,  final PlatformActionListener listener ){
		SharedModule.getInstance().sharedImageUrl(act, title, content, imageUrl, url, listener);
	}
	
	public void sharedVedio( final Activity act , final String pngPath , final String url , final String title , final String text , final PlatformActionListener listener ){
		SharedModule.getInstance().sharedVedio(mActivity, pngPath, url, title, text, listener);
	}

	//		是否是模拟器
	public boolean isEmulator( Context ctx ){
		return FindEmulator.isEmulator(ctx);
	}
	
	//		是否是手游岛模拟器
	public boolean isSydEmu( Context ctx ){
		return FindEmulator.isSydEmu(ctx);
	}
	
	//			打开web
	public void openWeb(Activity act , String url ,String wxUrl){
		Intent intent = new Intent(act,StartWebView.class);
		intent.putExtra("url",  url );
		intent.putExtra("wxUrl",  wxUrl );
		act.startActivity( intent );
	}
	
	public void onBackPressed() {
		// TODO Auto-generated method stub
		sdkCenter.onBackPressed();
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event){
		View m_baseView = WxTools.getIntance().getM_View();
		if(m_baseView!=null){
			((ViewGroup)m_baseView.getParent()).removeView(m_baseView);
			WxTools.getIntance().setM_View(null);
			return true;
		}else{
			return false ;
		}
	}
	
	/**
	 * 
	 * @param act
	 * @param dbPath
	 * @param isTest
	 * @return
	 */
	public boolean Yunwainit( final Activity act , final String dbPath  , final boolean isTest ){
		return YunwaModule.getInstance().init(act, dbPath, isTest);
	}
	
	/**
	 * 
	 * @param act
	 * @return
	 */
	public boolean Yunwainit( final Activity act){
		return YunwaModule.getInstance().init(act);
	}
	
	/**
	 * 
	 */
	public void destroy(){
		YunwaModule.getInstance().destroy();
	}
	
	/**
	 * 
	 * @param act
	 * @return
	 */
	public boolean setAppversion( final Activity act ){
		return YunwaModule.getInstance().setAppversion(act);
	}
	
	/**
	 * 
	 * @param
	 * @param serverId
	 * @param channelList
	 * @return
	 */
	public boolean yunWaLogin( final String ywUserinfo , final String serverId , final List<String> channelList ){
		return YunwaModule.getInstance().yunWaLogin(ywUserinfo, serverId, channelList);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean YunwalogOut(){
		return YunwaModule.getInstance().YunwalogOut();
	}
	
	/**
	 * 
	 * @param times
	 * @param openVolume
	 * @param rate
	 * @return
	 */
	public boolean setRecordConfig( final int times , final boolean openVolume , final byte rate ){
		LogUtil.e("--setRecordConfig--");
		return YunwaModule.getInstance().setRecordConfig(times, openVolume, rate);
	}
	
	public boolean startAudioRecord( final String filePath , final String ext ){
		return YunwaModule.getInstance().startAudioRecord(filePath, ext);
	}
	
	public boolean stopAudioRecord(){
		return YunwaModule.getInstance().stopAudioRecord();
	}
	
	public boolean playAudio( final String url , final String filePath , final String ext ){
		return YunwaModule.getInstance().playAudio(url, filePath, ext);
	}

	public boolean stopPlayAudio(){
		return YunwaModule.getInstance().stopPlayAudio();
	}
	
	public boolean uploadFile( final String filePath , final String fileId ){
		return YunwaModule.getInstance().uploadFile(filePath, fileId);
	}
	
	public boolean downloadFileReq( final String url , final String filePath , final String fileId ){
		return YunwaModule.getInstance().downloadFileReq(url, filePath, fileId);
	}
	
	public void setVoiceLinstener( final int type , MessageEventListener listener ){
		YunwaModule.getInstance().setVoiceLinstener(type, listener);
	}
	
	public boolean isCacheFileExist( final String url ){
		return YunwaModule.getInstance().isCacheFileExist(url);
	}
	
	public boolean clearCache(){
		return YunwaModule.getInstance().clearCache();
	}
	
	public void removeLinstener( final int type , MessageEventListener listener ){
		YunwaModule.getInstance().removeLinstener(type, listener);
	}
	
	
	public void removeListenerSingle( MessageEventListener listener ){
		YunwaModule.getInstance().removeListenerSingle(listener);
	}
	
	
	public void removeAllLinstener(){
		YunwaModule.getInstance().removeAllLinstener();
	}
	
	
	public boolean loginChannel( final String gameServerId , final List<String> channelList ){
		return YunwaModule.getInstance().loginChannel(gameServerId, channelList);
	}
	
	public boolean  logoutChannel(){
		return YunwaModule.getInstance().logoutChannel();
	}
	
	public boolean startAudioRecognize( String filePath, String tag ){
		return YunwaModule.getInstance().startAudioRecognize(filePath, tag);
	}
	
	public boolean startAudioRecognizeAndReturnUrl(String filePath, String tag){
		return YunwaModule.getInstance().startAudioRecognizeAndReturnUrl(filePath, tag);
	}
	
	public boolean startAudioRecognizeUrl(String url, String tag){
		return YunwaModule.getInstance().startAudioRecognizeUrl(url, tag);
	}
	
	public boolean sendChannelVoiceMessage( final String filePath, final int voiceDuration, final String wildcard, final String txt, final String expand ){
		LogUtil.e(""+filePath+voiceDuration+wildcard+txt+expand);
		return YunwaModule.getInstance().sendChannelVoiceMessage(filePath, voiceDuration, wildcard, txt, expand);
	}
	
	public boolean sendChannelTxtMessage( final String  data,  final String expand ,  final String wildcard ){
		return YunwaModule.getInstance().sendChannelTxtMessage(data, expand, wildcard);
	}
	
	public boolean initApplicationOnCreate( final Application application , final String appId ){
		return YunwaModule.getInstance().initApplicationOnCreate(application, appId);
	}
	
	
	//显示邀请码
	public void inviteShows(final Activity activity , final InvitationListener listener ){
		
		Listener.getInstance().setInvivationListener(listener);
		
		String app_secret = "3d759cba73b253080543f8311b6030bf";
		final String PROXY_VERSION = "1.0.1" ;
		String imei = DeviceUtil.getDeviceId();
		String gameId= Data.getInstance().getGameInfo().getGameId();

		//获取时间
		 SimpleDateFormat formatter   =   new   SimpleDateFormat("yyyyMMddHHmmss");
	     Date curDate =  new Date(System.currentTimeMillis());
	     String   time  =   formatter.format(curDate);
		
		
		Map<String, String> params = new TreeMap<String, String>( new Comparator<String>() {

			@Override
			public int compare(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return arg0.compareTo(arg1);
			}
		} );
		
		params.put("msi", imei);
		params.put("time", String.valueOf(time));
		params.put("proxyVersion",PROXY_VERSION);
		params.put("game_id",gameId);

		Map<String, String> update_params1 = Util.getSign( params , app_secret );
		new CommonAsyncTask(mActivity,Constants.URL.ISACTIVATIONS,new BaseListener() {
			
			@Override
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub
				
				LogUtil.log("是否已经使用了激活码接:"+result.toString());
				Listener.getInstance().getInvivationListener().onSuccess("已经激活了");
				
			}
			
			@Override
			public void onFail(Object result) {
				// TODO Auto-generated method stub
				LogUtil.log("激活失败:"+result.toString());
				Listener.getInstance().getInvivationListener().onFail("激活失败");
				
				Intent intent = new Intent(activity, Invitation.class);
				activity.startActivity(intent);
				
			}
		}).execute(new Map[] { update_params1 , null, null });;		
		
		
	}
	
	
	
	
	public void inviteShow( final String sid , final Activity activity , final InvitationListener listener    ){
		
		Listener.getInstance().setInvivationListener(listener);
		
		String app_id     = "1009";
		String app_secret = "5c7f4ea46f0d1c6c5c30693f24016374";
		
		Map<String, String> params = new TreeMap<String, String>( new Comparator<String>() {

			@Override
			public int compare(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return arg0.compareTo(arg1);
			}
		} );
		
		params.put("m", "knsdk");
		params.put("game", Data.getInstance().getGameInfo().getGameId());
		params.put("a", "checkInviteStatus");
		params.put("open_id", Data.getInstance().getUser().getOpenId());
		String ad_channel = Data.getInstance().getGameInfo().getAdChannel();
		params.put("server_id",sid);
		params.put("ad_channel",ad_channel);
		params.put("app_id",app_id);
		
		Map<String, String> update_params1 = Util.getSign( params , app_secret );
		
		LogUtil.log(update_params1.toString());
		new CommonAsyncTask(mActivity,Constants.URL.ACTIVATION, new BaseListener() {

			@Override
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub
				LogUtil.log("开始显示邀请码界面UI:"+result.toString());
				try {
					JSONObject  json = new JSONObject(result.toString());
					String      version = json.getString("version");
					JSONObject  verJson = new JSONObject(version);
					String      code = verJson.getString("code");
					
					
					if(code.equals("0")){
						LogUtil.log("已经激活了");
						Listener.getInstance().getInvivationListener().onSuccess("已经激活了");
					}else if(code.equals("1")){
						Intent intent = new Intent(activity.getApplicationContext(), Invitation.class);
						intent.putExtra("server_id", sid);
						activity.startActivity(intent);
					}else if(code.equals("2")){
						String      msg  = verJson.getString("msg");
						Util.ShowTips(activity, msg);
						Listener.getInstance().getInvivationListener().onFail("非法请求");
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			@Override
			public void onFail(Object result) {
				// TODO Auto-generated method stub
				LogUtil.log("不用显示邀请码界面UI");
			}
		}).execute(new Map[] { update_params1 , null, null });;	
	}
}

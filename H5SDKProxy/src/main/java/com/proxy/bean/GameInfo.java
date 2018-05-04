package com.proxy.bean;

import android.content.pm.ActivityInfo;
import android.text.TextUtils;

import com.proxy.Constants;
import com.proxy.Data;
import com.proxy.config.ConfigKeys;
import com.proxy.config.McProxy;
import com.proxy.util.LogUtil;
import com.proxy.util.Util;

public class GameInfo {
	
	private String gameName;
	private String appKey;
	private String gameId;
	private String channel;
	private String platform; //平台号，如：android，android1 等
	private String adChannel;
	private int screenOrientation = 0;
	private String gid;
	
	private String adChannelTxt;
	
	/**
	 * 
	 * @param gameName							游戏名称
	 * @param appKey							AppKey
	 * @param gameId							游戏ID
	 * @param screenOrientation					横竖屏   			KnConstants.LANDSCAPE：横屏  ， KnConstants.PORTRAIT	竖屏
	 */
	public GameInfo(String gameName, String appKey, String gameId,
			int screenOrientation) {
		super();
		this.gameName = gameName;
		this.appKey = appKey;
		this.gameId = gameId;
		this.screenOrientation = screenOrientation;
	}

	public GameInfo() {
		super();
		this.gameName = McProxy.getConfiguration(ConfigKeys.GAME_NAME);
		this.appKey =  McProxy.getConfiguration(ConfigKeys.GAME_KEY);
		this.gameId = McProxy.getConfiguration(ConfigKeys.GAME_ID);
		this.screenOrientation = McProxy.getConfiguration(ConfigKeys.GAME_ORIEN);
	}



	public String getAdChannel() {
		LogUtil.e("ad_channel="+adChannel);
		return adChannel;
	}
	public void setAdChannel(String adChannel) {
		LogUtil.e("ad_channel="+adChannel);
		this.adChannel = adChannel;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public int getScreenOrientation() {
		return screenOrientation;
	}
	public void setScreenOrientation(int screenOrientation) {
		this.screenOrientation = 
				(screenOrientation == ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE ? ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public String getAdChannelTxt() {
		return adChannelTxt;
	}

	public void setAdChannelTxt(String adChannelTxt) {
		this.adChannelTxt = adChannelTxt;
	}

	public static void initGameInfo(GameInfo gameInfo){
		
		//获取本地asset目录下adChannel.txt中的参数
		String result = Util.getAssetsFileContent(Data.getInstance().getApplicationContex(),"SDKFile/adChannel.png");
		
		gameInfo.setAdChannelTxt(result);
		
//		String adChannel="2200040";
		String adChannel = Util.getAdchannle(null);
		
		String channel = Util.getJsonStringByName(result, "channel");
//		String channel="kaopu";
		
		String gid = Util.getJsonStringByName(result, "gid");
		
		String domain_login = Util.getJsonStringByName(result, "domain_login");
		String domain_pay = Util.getJsonStringByName(result, "domain_pay");
		String domain_api = Util.getJsonStringByName(result, "domain_api");
		
		if( !TextUtils.isEmpty(domain_login) ){
			Constants.URL.LOGIN = "http://"+domain_login+"/api/login_check.php";
		}
		if( !TextUtils.isEmpty(domain_pay) ){
			Constants.URL.APPLY_ORDER = "http://"+domain_pay+"/api/apply_order.php";
		}
		if( !TextUtils.isEmpty(domain_api) ){
			Constants.URL.ENTER_GAME = "http://"+domain_api+"/api/open_platform/datacenter/sendlv.php";
		}
		LogUtil.e("adChannel:"+adChannel);
        gameInfo.setAdChannel(adChannel);
        gameInfo.setChannel(channel);
        gameInfo.setGid(gid);
        
        if(adChannel.equals("14000471")||adChannel.equals("22000623")){
        	//女皇与宰相 聚宝盆Platform
        	gameInfo.setPlatform("android2");
        }else if (adChannel.equals("2302066")) {
        	gameInfo.setPlatform("android3"); //景迅—安卓—传世OL（中网游）
        	
		}else if (adChannel.equals("24020113")) {
        	gameInfo.setPlatform("android2"); //微游汇—安卓—决斩沙城
        	
		}else if (adChannel.equals("2402070")) {
        	gameInfo.setPlatform("android1"); //奇点—安卓—传世OL
        	
		}else if (adChannel.equals("24020910")) {
        	gameInfo.setPlatform("android1");//爱上游戏—安卓—重庆传奇
        	
		}else if (adChannel.equals("24020125")) {
        	gameInfo.setPlatform("android1"); //奇天官网—安卓—血战苍穹
        	
		}else if (adChannel.equals("24020138")) {
        	gameInfo.setPlatform("android2"); //奇天官网1—安卓—皇族霸业
        	
		}else if (adChannel.equals("240201050")) {
        	gameInfo.setPlatform("android1"); //齐毅1—安卓—战狼霸业
        	
		}else if (adChannel.equals("24020142")) {
        	gameInfo.setPlatform("android1"); //优象YX—安卓—皇族霸业
        	
		}else if (adChannel.equals("2802006")) {
        	gameInfo.setPlatform("android1"); //奇天OPPO—安卓—皇族霸业
        	
		}else if (adChannel.equals("2000000")) {
        	gameInfo.setPlatform("android"); //
        	
		}else if (adChannel.equals("220300188")) {
        	gameInfo.setPlatform("android5"); //奇天官网—盛世传奇—皇族霸业
        	
		}else if (adChannel.equals("220300190")) {
        	gameInfo.setPlatform("android1"); //逗游吧—皇族霸业_专服
        	
		}else if (adChannel.equals("2802012")) {
        	gameInfo.setPlatform("android3"); //奇天官网—皇族霸业_专服
        	
		}else if (adChannel.equals("28020109")) {
			//天宇游—安卓—贪狼传奇
        	gameInfo.setPlatform("android1"); 
        	
		}else if (adChannel.equals("24020119")) {
        	gameInfo.setPlatform("android1"); //皇族霸业_机锋
        	
		}else if (adChannel.equals("28020112")) {
        	gameInfo.setPlatform("android1"); //贪狼传奇-乐嗨嗨
        	
		}
        
        else{
        	gameInfo.setPlatform("android");
        }
	}


	
	
	
}

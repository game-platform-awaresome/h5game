package com.proxy.service;

import android.app.Activity;
import android.util.Log;

import com.proxy.Constants;
import com.proxy.Data;
import com.proxy.OpenSDK;
import com.proxy.ResultCode;
import com.proxy.bean.GameInfo;
import com.proxy.bean.GameUser;
import com.proxy.bean.KnPayInfo;
import com.proxy.bean.Result;
import com.proxy.bean.User;
import com.proxy.listener.BaseListener;
import com.proxy.listener.LoginListener;
import com.proxy.sdk.module.GeTuiPushModule;
import com.proxy.task.CommonAsyncTask;
import com.proxy.util.DeviceUtil;
import com.proxy.util.LoadingDialog;
import com.proxy.util.LogUtil;
import com.proxy.util.Md5Util;
import com.proxy.util.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpService {


	public static void doLogin(Activity activity,  
			String content , BaseListener listener) {

		try {
			GameInfo gameInfo = Data.getInstance().getGameInfo();
			HashMap<String , String> params = getCommonParams();
			LogUtil.e("params:"+params.toString());
			String game_id = gameInfo.getGameId();
			String platform = gameInfo.getPlatform();
			String channel = gameInfo.getChannel();
			
			params.put("content", content);
			
			params.put(
					"sign",
					Md5Util.getMd5(game_id + channel
							+ platform + content.toString()
							+ gameInfo.getAppKey()));
			LogUtil.e("AppKey"+gameInfo.getAppKey());
			LogUtil.e("game_id"+Data.getInstance().getGameInfo().getGameId());
			LogUtil.e("game_id = "+game_id+"  channel="+channel+"  AppKey="+gameInfo.getAppKey());
			new CommonAsyncTask(activity , Constants.URL.LOGIN, listener).execute(new Map[] { params, null, null });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void applyOrder( Activity activity ,KnPayInfo knPayInfo, BaseListener listener) {
		try {

			User userInfo = Data.getInstance().getUser();
			GameInfo gameInfo = Data.getInstance().getGameInfo();
			GameUser gamuser = Data.getInstance().getGameUser();

			HashMap<String,String> params = getCommonParams();

			String open_id = userInfo != null ? userInfo.getOpenId():"";
			
			String uid = gamuser!=null ? gamuser.getUid():"";
			int server_id = gamuser!=null ?  gamuser.getServerId() : 0;
			
			String game_id = gameInfo != null ? gameInfo.getGameId() : "";
			String platform = gameInfo != null ? gameInfo.getPlatform():"";
			String channel = gameInfo != null ? gameInfo.getChannel():"";
LogUtil.e("=======uid"+uid+"::::server_id"+server_id+"------gameInfo"+gameInfo);
			params.put("extra_info", knPayInfo.getOrderNo());
			if (Util.getChannle(activity).equals("nubia")) {
				
				
				params.put("productName", knPayInfo.getProductName());
				
				
			}if (Util.getChannle(activity).equals("dalv")) {
				
				params.put("productName",(knPayInfo.getPrice()/10+"元宝"));
				params.put("amount",String.valueOf((knPayInfo.getPrice()/100)));
				params.put("extend","充值元宝");
				params.put("appid","et51ba58d87527a539");
				params.put("gameArea",Data.getInstance().getGameUser().getServerName());
				params.put("gameAreaId",String.valueOf(Data.getInstance().getGameUser().getServerId()));
				params.put("roleId",Data.getInstance().getGameUser().getUid());
				params.put("userRole",Data.getInstance().getGameUser().getUsername());
				params.put("gameLevel",String.valueOf(Data.getInstance().getGameUser().getUserLevel()));
								
			}if (Util.getChannle(activity).equals("uc")) {
				
				double price=knPayInfo.getPrice();
				params.put("amount",String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(price))/100));				
				params.put("accountId",userInfo.getExtraInfo1());
				params.put("callbackInfo","自定义信息");
				
				LogUtil.log("支付请求参数accountId====="+userInfo.getExtraInfo1()+"amount="+String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(price))/100));
			
			}
			
			if (Util.getChannle(activity).equals("mz")) {
					
				
				double price=knPayInfo.getPrice();
				params.put("mztotal_price",String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(price))/100)); //金额总数			
				params.put("mzproduct_per_price",String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(price))/100));//游戏道具单价，默认值：总金额
				params.put("mzapp_id",knPayInfo.getExtraInfo());//appid
				
				LogUtil.log("appid= "+knPayInfo.getExtraInfo());
				params.put("mzuid",gamuser.getExtraInfo()); //sdk登录成功后的 uid
				params.put("mzproduct_id",knPayInfo.getProductId());//CP 游戏道具 ID,
				params.put("mzproduct_subject",knPayInfo.getProductName());//订单标题,格式为：”购买 N 枚金币”
				params.put("mzproduct_unit","元");//游戏道具的单位，默认值：””
				params.put("mzproduct_body","钻石");//游戏道具说明，默认值：””
				params.put("mzbuy_amount",String.valueOf(1));//道具购买的数量，默认值：”1”
    			params.put("mzcreate_time",userInfo.getExtenInfo());//创建时间戳
				params.put("mzpay_type",String.valueOf(0));//支付方式，默认值：”0”（即定额支付）
				params.put("mzuser_info","");//CP 自定义信息，默认值：””
				params.put("mzsign_type","md5");//签名算法，默认值：”md5”(不能为空)
				
				
				LogUtil.log("支付请求参数mzuid====="+gamuser.getExtraInfo()+" 创建时间戳="+userInfo.getExtenInfo()+"订单名称="+knPayInfo.getProductName()+" 总额="
				+String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(price))/100)+" product_id="+knPayInfo.getProductId()+
				" product_subject="+knPayInfo.getProductName()+" pay_type"+String.valueOf(0));
			
				
				
				
			
			}if (Util.getChannle(activity).equals("duoku")) {
				
				LogUtil.log("百度下单");
				
				params.put("dkuid",userInfo.getExtraInfo2());//支付传递的uid
				params.put("dkextraInfo",userInfo.getExtraInfo1());//透传
				
				
			
				
				LogUtil.log("下单发送游戏服务器dkuid="+userInfo.getExtraInfo2()+"透传="+userInfo.getExtraInfo1());
				
			}if (Util.getChannle(activity).equals("sanxing")) { //三星
				
				double price=knPayInfo.getPrice(); //金额					
				params.put("SxPrice", String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(price))/100));
				params.put("Sxappuserid",gamuser.getUid()); //用户在商户应用的唯一标识，建议为用户帐号。
				params.put("SxCurrency","RMB"); //货币类型
				params.put("SxWaresid",knPayInfo.getProductId()); //商品编号
				
			}/*if(Util.getChannle(activity).equals("jinli")){//金立
				
				double price=knPayInfo.getPrice();
				params.put("jluid",userInfo.getExtraInfo1()); //sdk登录成功返回的user_id。
				params.put("jlsubject",knPayInfo.getProductName()); //商品名称
				params.put("jltotal_fee",String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(price))/100)); //需支付金额
				params.put("jldeliver_type","1"); //付款方式：1为立即付款，2为货到付款
				params.put("jldeal_price",String.valueOf(Integer.parseInt(new java.text.DecimalFormat("0").format(price))/100)); //商品总金额
				//params.put("jladChannel","2802003"); //后台兼容新老支付接口判断标识
                
				
				
			}*/
			
			params.put("price" , String.valueOf(knPayInfo.getPrice()));						
			params.put("extraInfo", knPayInfo.getExtraInfo());
			
			
			params.put(
					"sign",
					Md5Util.getMd5(game_id + channel + platform + uid + open_id
							+ server_id + gameInfo.getAppKey()));
			
			if(!Util.isNetWorkAvailable(activity)){
				LoadingDialog.dismiss();
				Util.ShowTips(activity,"请检查网络是否连接");
				return ;
			}
			
			new CommonAsyncTask(activity , Constants.URL.APPLY_ORDER, listener)
					.execute(new Map[] { params, null, null });
		} catch (Exception e) {
			LoadingDialog.dismiss();
			listener.onFail(new Result(ResultCode.FAIL, "申请订单号失败"));
			e.printStackTrace();
		}
	}
	
	//发送等级url ,不需要sig签名
	public static void enterGame(BaseListener listener) {
		try {

			GameUser gameUser = Data.getInstance().getGameUser();
			User user = Data.getInstance().getUser();
			GameInfo gameInfo = Data.getInstance().getGameInfo();
			String channel = gameInfo.getChannel();//渠道
			String adchannel = gameInfo.getAdChannel();//广告渠道
			String mis = DeviceUtil.getDeviceId(); //IMEI码

			String game_id =  gameInfo.getGameId(); //游戏品牌
			String uid = gameUser.getUid();//游戏uid
			String open_id = user.getOpenId();//游戏openid
			int   serverId = gameUser.getServerId();//服务区id
			int  lv = gameUser.getUserLevel();// 游戏等级

			Log.d("ttt","channel="+channel+"  adchannel="+adchannel
					+"  mis="+mis+" game_id"+game_id+ "  uid="+uid+"  open_id="+open_id+" serverId="+serverId+" lv"+lv);

			HashMap<String,String> params =new HashMap<String, String>();

			if(gameUser!=null){

				params.put("game_id",game_id);
				params.put("uid",uid);
				params.put("open_id", open_id);
				params.put("server_id",String.valueOf(serverId));
				params.put("lv", String.valueOf(lv));
				params.put("msi",mis);
				params.put("ad_channel",adchannel);
				params.put("channel", channel);


			}

			params.put("getuiClientId", GeTuiPushModule.getInstance().getClientId());
			
			LogUtil.e("params="+params.toString());
			LogUtil.e("game_id"+Data.getInstance().getGameInfo().getGameId());
			LogUtil.e("enter_url"+Constants.URL.ENTER_GAME);
			new CommonAsyncTask(null, Constants.URL.ENTER_GAME, listener)
					.execute(new Map[] { params, null, null });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static HashMap<String, String> getCommonParams(){
		HashMap<String, String> params = new HashMap<String, String>();
		
		User userInfo = Data.getInstance().getUser();
		GameInfo gameInfo = Data.getInstance().getGameInfo();
		GameUser gamuser = Data.getInstance().getGameUser();
		
		String open_id="",game_id="",channel="",ad_channel="",platform="",gid="";
		String uid="",server_id="";
		
		String msi = DeviceUtil.getDeviceId();
		
		if(gameInfo!=null){
			game_id = gameInfo.getGameId();
			channel = gameInfo.getChannel();
			LogUtil.e("ad_channel="+ad_channel);
			ad_channel = gameInfo.getAdChannel();
			platform = gameInfo.getPlatform();
		}
		
		if(userInfo!=null){
			open_id = userInfo.getOpenId();
		}
		
		if(gamuser!=null){
			uid = gamuser.getUid();
			server_id = String.valueOf(gamuser.getServerId());
		}
		
		params.put("gid", gameInfo.getGid());
		params.put("game_id", game_id);
		params.put("channel", channel);
		params.put("ad_channel", ad_channel);
		params.put("uid", uid+"");
		params.put("open_id", open_id);
		params.put("server_id", server_id);
		params.put("mac", DeviceUtil.getMacAddress());
		params.put("platform", platform);
		params.put("phoneType", DeviceUtil.getPhoneType());
		params.put("netType", DeviceUtil.getNetWorkType());
		
		params.put("msi", msi );
		
		params.put("channelVersion", OpenSDK.getInstance().getChannelVersion());
		params.put("proxyVersion", OpenSDK.getInstance().getProxyVersion());
		
		String appInfo = Util.getAppInfo( Data.getInstance().getGameActivity() );
		params.put("packageName", Util.getJsonStringByName(appInfo, "packageName") );
		params.put("versionName", Util.getJsonStringByName(appInfo, "versionName") );
		params.put("versionCode", Util.getJsonStringByName(appInfo, "versionCode") );
		
		return params;
		
	}
	
	public static void loginVerfiy(Activity activity , String content , final LoginListener loginListener){
		LoadingDialog.show(activity, "正在登录...", false);
		HttpService.doLogin(activity,content,new BaseListener() {
			@Override
			public void onSuccess(Object result) {
					LoadingDialog.dismiss();
					JSONObject obj = null;
    				User user = null;
    				try {
    					obj = new JSONObject(result.toString());
    					user = new User();
    					user.setOpenId( obj.getString("open_id") );
    					user.setSid( obj.getString("sid") );
    					user.setSign( obj.getString("sign") );
    					user.setIsIncompany( Integer.parseInt(obj.getString("iscompany")) );
    					user.setLogin(true);
    					Data.getInstance().setUser(user);
    				}catch(Exception e){
    					e.printStackTrace();
    				}
    				loginListener.onSuccess(user);
			}
			@Override
			public void onFail(Object result) {
				LoadingDialog.dismiss();
				loginListener.onFail(result.toString());
			}
		});
	}

	
	//游戏开始push数据地址
	public static void doPushData( final  Activity activity, Map<String,Object> data , BaseListener listener){
		
		try{
			
			HashMap<String , String> params = new HashMap<String, String>();
			
			params.put("game_id",(String) data.get("game_id"));
			params.put("app_key",(String) data.get("app_key"));
			params.put("imei",(String) data.get("imei"));
			params.put("platform",(String) data.get("platform"));
			params.put("ad_channel",(String) data.get("ad_channel"));
			params.put("channel",(String) data.get("channel"));
			params.put("ip",(String) data.get("ip"));
			params.put("phone_type",data.get("phone_type").toString());
			
			String game_id = (String) data.get("game_id");
			String appkey = (String) data.get("app_key");
			String imei    = (String) data.get("imei");
	
			
			params.put("sign", Md5Util.getMd5(game_id+appkey+imei));
			
			new CommonAsyncTask(null, Constants.URL.PUSH_DATA, listener).execute(new Map[] { params, null, null });
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	//游戏邀请码数据请求地址
	public static void doPushActivation(final Activity activity , Map<String,Object> data ,  BaseListener listener){
		
		try{
				
//				KnLog.e(data.toString());
		
				HashMap<String , String> params = new HashMap<String, String>();
				
				params.put("m",(String) data.get("m"));
				params.put("a",(String) data.get("a"));
				params.put("uid",(String) data.get("uid"));
				params.put("game",(String) data.get("game"));
				params.put("channel",(String) data.get("channel"));
				params.put("zone",(String)data.get("zone"));
				params.put("server_id",(String)data.get("server_id"));
				params.put("cdkey",(String)data.get("cdkey"));
				params.put("vip",(String)data.get("vip"));
				params.put("level",(String)data.get("level"));
				
				params.put("app_id",(String)data.get("app_id"));
				params.put("open_id",(String)data.get("open_id"));
				params.put("send",(String)data.get("send"));
				params.put("sign",(String)data.get("sign"));
				
				
				LogUtil.e("params:"+params.toString());
				
				new CommonAsyncTask(activity, Constants.URL.ACTIVATION, listener).execute(new Map[] { params, null, null });
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}


	//热血传奇获取打包测试url
	public static void doHtmlUrl(final Activity activity , Map<String,Object> data ,  BaseListener listener){

		try{

			HashMap<String , String> params = new HashMap<String, String>();

			params.put("game_id",(String) data.get("game_id"));
			params.put("app_key",(String) data.get("app_key"));
			params.put("platform",(String) data.get("platform"));
			params.put("channel",(String) data.get("channel"));
			params.put("time",(String) data.get("time"));
			params.put("proxyVersion","1.0.0");

			String game_id = (String) data.get("game_id");
			String appkey = (String) data.get("app_key");
			String channel =(String) data.get("channel");
			String platform    = (String) data.get("platform");
			String time = (String) data.get("time");

			String getH5url ="http://oms.u7game.cn/api/get_h5_url.php";

			params.put("sign", Md5Util.getMd5(game_id+channel+platform+time+appkey));

			new CommonAsyncTask(null,getH5url, listener).execute(new Map[] { params, null, null });

		}catch(Exception e){
			e.printStackTrace();
		}


	}



	
	public static void payData(Activity activity,  
			String content , BaseListener listener) {

		try {
			
			HashMap<String , String> params = new HashMap<String, String>();
			LogUtil.e("params:"+params.toString());
			
			params.put("content", content);
				
			new CommonAsyncTask(activity , Constants.URL.PAYDATAURL, listener).execute(new Map[] { params, null, null });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

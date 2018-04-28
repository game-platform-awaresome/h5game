package com.mc.game.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import com.mc.game.R;
import com.proxy.Constants;
import com.proxy.Data;
import com.proxy.OpenSDK;
import com.proxy.bean.GameInfo;
import com.proxy.bean.KnPayInfo;
import com.proxy.bean.User;
import com.proxy.listener.BaseListener;
import com.proxy.listener.InitListener;
import com.proxy.listener.LoginListener;
import com.proxy.listener.LogoutListener;
import com.proxy.listener.PayListener;
import com.proxy.listener.RoleReportListener;
import com.proxy.service.HttpService;
import com.proxy.util.LoadingDialog;
import com.proxy.util.LogUtil;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import android.R.color;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Xml.Encoding;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class MainActivity extends Activity {

    private WebView mweview;
    private LinearLayout linearLayout;
    private ImageView tx;


    OpenSDK m_proxy = OpenSDK.getInstance();
    private String m_appKey = "uVkyGhiKWm7T2B43n5rEafHleXwPzjRU";
    private String m_gameId = "rxcqh5";
    private String m_gameName = "rxcqh5";
    private int m_screenOrientation = 1;

    private GameInfo m_gameInfo = new GameInfo(m_gameName, m_appKey, m_gameId, m_screenOrientation);
    public static String HtmlUrl;
    private String roleDate;
    private String roledata;

    private boolean isInit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.pmc_hactivity_main);

        initview();
        sdkProxyinit();
        webviewinit();

    }

    private void initview(){

        mweview = (WebView) findViewById(R.id.wb);
        mweview.setBackgroundColor(getResources().getColor(R.color.colorbag));
        linearLayout =(LinearLayout)findViewById(R.id.ll);
        tx = (ImageView) findViewById(R.id.cx);

    }



    //初始化中间件读取assets资源文件
    private void sdkProxyinit(){

        //初始化读取adChannel文件中的数据
        Data.getInstance().setApplicationContex(MainActivity.this);
        Data.getInstance().setGameInfo(m_gameInfo);

    }

    //webview
    private void webviewinit() {

        getHtmlUrl();


    }

    //SDK服务器获取H5游戏地址
    private void getHtmlUrl() {

        Map<String, Object> data = new HashMap<String, Object>();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        data.put("game_id", m_gameInfo.getGameId());
        data.put("app_key", m_gameInfo.getAppKey());
        data.put("platform", m_gameInfo.getPlatform());
        data.put("channel", m_gameInfo.getChannel());
        data.put("time", date);

        HttpService.doHtmlUrl(MainActivity.this, data, new BaseListener() {
            @Override
            public void onSuccess(Object result) {

                LogUtil.log("获取h5地址result=" + result.toString());

                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    HtmlUrl = jsonObject.getString("loginUrl");

                    LogUtil.log("获取h5地址=" + HtmlUrl);

                    //主线程显示WebView
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showHtml(HtmlUrl);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Object result) {

                LogUtil.log("网络请求失败："+result.toString());
                runOnUiThread(new Runnable() { //UI线程显示
                    public void run() {
                        linearLayout.setVisibility(View.VISIBLE);
                        mweview.setVisibility(View.GONE);
                        linearLayout.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                LogUtil.log("重新请求");
                                linearLayout.setVisibility(View.GONE);
                                webviewinit();
                            }
                        });
                    }
                });


            }

        });


    }


    //显示HTML页面
    private void showHtml(String htmlUrl) {

        //设置WebSettings属性
        WebSettingss();
        //设置webView监听回调
        WebViewListener();
        mweview.loadUrl(htmlUrl); //加载h5页面
        //  String platform ="platform=android";
        // mweview.postUrl(htmlUrl,EncodingUtils.getBytes(platform, "BASE64"));
        mweview.setBackgroundColor(Color.BLACK);

    }


    //设置WebSettings属性
    private void WebSettingss(){

        WebSettings webSettings = mweview.getSettings();
        webSettings.setJavaScriptEnabled(true); //在WebView中启用JavaScript
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 设置允许JS弹窗

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //Http与Https混合内容
            //两者都可以
            webSettings.setMixedContentMode(webSettings.getMixedContentMode());

        }

        //启用或禁止WebView访问文件数据
        webSettings.setAllowFileAccess(true);
        //设置是否支持变焦
        webSettings.setSupportZoom(true);
        //无限放大
        webSettings.setBuiltInZoomControls(true);
        //适应手机屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //关闭密码保存提醒(密码会被明文保到 /data/data/com.package.name/databases/webview.db)
        webSettings.setSavePassword(false);
        //Dom缓存
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //优先缓存
        // LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        // LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        // LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        // LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 允许加载本地 html 文件/false
        webSettings.setAllowFileAccess(true);
        // 允许通过 file url 加载的 Javascript 读取其他的本地文件,Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        webSettings.setAllowFileAccessFromFileURLs(true);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
    }


    //设置webView监听回调
    private void WebViewListener(){

        mweview.setWebViewClient(new WebViewClient() {

            //不启动浏览器加载html
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.log("加载url："+url);
                mweview.loadUrl(url);
                return true;
            }


            //onPageStarted会在WebView开始加载网页时调用
            @Override
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);

                LoadingDialog.show(MainActivity.this, "拼命加载游戏中....", false);
                //与js协议接口
                mweview.addJavascriptInterface(new InitGame(), "MCBridge");
            }

            //加载结束时调用
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                LogUtil.log("WebView加载结束时调用url:"+url);
                LoadingDialog.dismiss();

            }


            //该方法传回了错误码，根据错误类型可以进行不同的错误分类处理
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                LogUtil.log("错误码：" + errorCode);
            }




        });


        mweview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) { //页面加载进度

                LogUtil.log("加载进度=" + newProgress);

                super.onProgressChanged(view, newProgress);
            }
        });

    }


    public class InitGame {


        @JavascriptInterface
        public void activate() {

            LogUtil.log("点击初始化按钮");

            m_proxy.isSupportNew(true);
            m_proxy.doDebug(true);
            //中间件

            m_proxy.init(MainActivity.this, m_gameInfo, new InitListener() {

                @Override
                public void onSuccess(Object msg) {
                    // TODO Auto-generated method stub
                    LogUtil.log("游戏初始化成功="+msg);

                    if(msg!=null){
                        isInit =true;
                        activateCallback();
                    }




                }

                @Override
                public void onFail(Object arg0) {
                    // TODO Auto-generated method stub
                    LogUtil.log("游戏初始化失败");
                }
            });

            //登录回调
            m_proxy.setLogoinListener(new LoginListener() {

                @Override
                public void onSuccess(User user) {

                    loginCallback(user);


                }

                @Override
                public void onFail(String result) {
                    LogUtil.log("登录失败:" + result);
                }
            });

            //上报数据回调
            m_proxy.setRoleReportListener(new RoleReportListener() {
                @Override
                public void onSuccess(Object result) {

                    roleDate = result.toString();
                    int i = 1;
                    LogUtil.log((++i) + "上报数据成功返回参数=" + roleDate.toString());

                    //roleData(roleDate);


                }

                @Override
                public void onFail(Object result) {

                    LogUtil.log("上报数据失败返回参数=" + result.toString());
                }
            });

            //支付回调
            m_proxy.setPayListener(new PayListener() {

                @Override
                public void onSuccess(Object result) {
                    LogUtil.log("setPayListener 支付回调成功=" + result.toString());


                    payCallback();

                }

                @Override
                public void onFail(Object result) {
                    LogUtil.log("setPayListener 支付回调失败:" + result);
                }
            });


            //登出回调
            m_proxy.setLogoutListener(new LogoutListener() {

                @Override
                public void onSuccess(Object result) {
                    LogUtil.log("sdk游戏登出成功===========:" + result.toString());
                    //游戏账号注销，返回到登录界面
                    //调用js回调

                    logoutCallback();



                }
                @Override
                public void onFail(Object result) {
                    LogUtil.log("游戏登出失败:" + result);
                }
            });


        }


        @JavascriptInterface
        public void login() {

            LogUtil.log("点击登录");

            if(!isInit){
                return;
            }else {
                m_proxy.login(MainActivity.this);
            }






        }


        /**
         * 支付
         * @param payContent
         */
        @JavascriptInterface
        public void pay(String payContent) {
            try {
                JSONObject jsonObject = new JSONObject(payContent.toString());

                String OrderNo = jsonObject.getString("extra_info"); //游戏订单
                String Price = jsonObject.getString("price"); //商品价格
                String encodeOrderNo=null;
                try {
                    encodeOrderNo = URLDecoder.decode(OrderNo,"utf-8");
                    LogUtil.log("游戏传递的支付参数=" + jsonObject + " 商品价格=" + Price + " 游戏订单=" + encodeOrderNo);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }



                String CoinName = jsonObject.getString("coinName");
                String CoinRate = jsonObject.getString("coinRate");
                String ProductId = jsonObject.getString("productId");

                final KnPayInfo payInfo = new KnPayInfo();
                payInfo.setProductName("钻石");                //商品名称
                payInfo.setCoinName(CoinName);                        //货币名称	如:元宝
                payInfo.setCoinRate(Integer.valueOf(CoinRate));        //游戏货币的比率	如:1元=10元宝 就传10
                payInfo.setPrice(Double.valueOf(Price) * 100);                //商品价格  分
                payInfo.setProductId(ProductId);                    //商品Id,没有可以填null
                payInfo.setOrderNo(OrderNo);  //订单号
                payInfo.setDesc("购买钻石");


                m_proxy.pay(MainActivity.this, payInfo);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        /**
         * js传递过来的参数={"senceType":"1","userId":"1","serverId":"3","userLv":"4",
         * "serverName":"战","roleName":"xiao","vipLevel":"0","roleCTime":"roleCTime"}
         * @param roleReportContent
         */

        @JavascriptInterface
        public void roleReport(String roleReportContent) {

            LogUtil.log("js传递过来的参数=" + roleReportContent);
            roledata = roleReportContent;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONObject jsonObject = new JSONObject(roledata.toString());
                        LogUtil.log("js传递过来的参数=" + jsonObject.toString());

                        String userId = jsonObject.getString("userId");//用户id
                        String serverId = jsonObject.getString("serverId"); //服务器Id
                        String gameLv = jsonObject.getString("lv"); //游戏等级

                        String serverName = jsonObject.getString("serverName"); //玩家所在服区名称
                        String roleName = jsonObject.getString("roleName"); //游戏角色名称
                        String roleCTime = jsonObject.getString("roleCTime"); //游戏角色创建时间（时间戳）
                        String vipLevel = jsonObject.getString("vipLevel"); //玩家VIP等级
                        String user_sex = jsonObject.getString("user_sex"); //玩家性别
                        String user_age = jsonObject.getString("user_age"); //玩家年龄
                        String factionName = jsonObject.getString("factionName"); //用户所在帮派名称
                        String senceType = jsonObject.getString("senceType"); ///场景ID(值为1则是进入游戏场景，值为2则是创建角色场景，值为4则是提升等级场景)
                        String diamondLeft = jsonObject.getString("diamondLeft"); //玩家货币余额
                        String extraInfo = jsonObject.getString("extraInfo"); //玩家信息拓展字段


                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put(Constants.USER_ID, userId);            //游戏玩家ID
                        data.put(Constants.SERVER_ID, serverId);        //游戏玩家所在的服务器ID
                        data.put(Constants.USER_LEVEL, gameLv);        //游戏玩家等级
                        data.put(Constants.ROLE_ID, userId);            //角色ID


                        //  int senceType =1; //场景ID
                        // String  extraInfo = "";      			//玩家信息拓展字段
                        // String vipLevell ="0";          				//玩家VIP等级
                        // String factionName="";     					//用户所在帮派名称
                        //场景ID;//(值为1则是进入游戏场景，值为2则是创建角色场景，值为4则是提升等级场景)
                        //  String  diamondLeft = diamondLeft;        			//玩家货币余额
                        data.put(Constants.EXPEND_INFO, extraInfo);    //扩展字段
                        data.put(Constants.SERVER_NAME, serverName);    //所在服务器名称
                        data.put(Constants.ROLE_NAME, roleName);//角色名称
                        data.put(Constants.VIP_LEVEL, vipLevel);        //VIP等级
                        data.put(Constants.FACTION_NAME, factionName);//帮派名称
                        data.put(Constants.SCENE_ID, senceType);        //场景ID
                        data.put(Constants.ROLE_CREATE_TIME, roleCTime);//角色创建时间
                        data.put(Constants.BALANCE, diamondLeft);        //剩余货币
                        data.put(Constants.IS_NEW_ROLE, Integer.valueOf(senceType) == 2 ? true : false);    //是否是新角色
                        data.put(Constants.USER_ACCOUT_TYPE, "1");        //玩家账号类型账号类型，0:未知用于来源1:游戏自身注册用户2:新浪微博用户3:QQ用户4:腾讯微博用户5:91用户(String)
                        data.put(Constants.USER_SEX, user_sex);                //玩家性别，0:未知性别1:男性2:女性；(String)
                        data.put(Constants.USER_AGE, user_age);            //玩家年龄；(String)

                        m_proxy.onEnterGame(data);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });


        }


    }


    //调用js接口（初始化成功回调方法）
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void activateCallback() {

        LogUtil.log("js调用初始化=====");

        final int version = Build.VERSION.SDK_INT;
        if (version < 18) {

            //调用js初始化回调
            mweview.loadUrl("javascript:activateCallback('" + getJson().toString() + "')");

        } else { // 该方法在 Android 4.4 版本才可使用，


            //调用js初始化回调
            mweview.evaluateJavascript("javascript:activateCallback('" + getJson().toString() + "')",new ValueCallback<String>() {

                @Override
                public void onReceiveValue(String value) {
                    // TODO Auto-generated method stub

                }
            });
        }

    }


    //调用js接口（登录回调方法）
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void loginCallback(User user) {

        final int version = Build.VERSION.SDK_INT;
        if (version < 18) {

            //调用js初始化回调
            mweview.loadUrl("javascript:loginCallback('" + getJson(user) + "')");

        } else { // 该方法在 Android 4.4 版本才可使用，

            //调用js初始化回调
            mweview.evaluateJavascript("javascript:loginCallback('" + getJson(user) + "')", new ValueCallback<String>() {

                @Override
                public void onReceiveValue(String value) {
                    // TODO Auto-generated method stub

                }
            });

        }
    }


    //js接口（游戏回到登录界面）
    private void logoutCallback(){
        final int version = Build.VERSION.SDK_INT;
        if (version < 18) {

            //调用js初始化回调
            mweview.loadUrl("javascript:logoutCallback()");

        } else { // 该方法在 Android 4.4 版本才可使用，

            //调用js初始化回调
            mweview.evaluateJavascript("javascript:logoutCallback()", new ValueCallback<String>() {

                @Override
                public void onReceiveValue(String value) {
                    // TODO Auto-generated method stub

                }
            });

        }

    }


    //js接口（支付回调）
    private void payCallback(){
        final int version = Build.VERSION.SDK_INT;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reason", "支付成功");
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (version < 18) {

            //调用js支付回调
            mweview.loadUrl("javascript:payCallback('" + jsonObject.toString() + "')");


        } else { // 该方法在 Android 4.4 版本才可使用，

            //调用js初始化回调
            mweview.evaluateJavascript("javascript:payCallback('" + jsonObject.toString() + "')",new ValueCallback<String>() {

                @Override
                public void onReceiveValue(String value) {
                    // TODO Auto-generated method stub

                }
            });
        }

    }




    //sdk登录成功返回的对象数据
    private JSONObject getJson(User user) {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("open_id", user.getOpenId());
            jsonObject.put("sid", user.getSid());
            LogUtil.log("登录成功,user=" + jsonObject.toString() + " open_id:" + user.getOpenId() + ",sid:" + user.getSid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    //初始化成功返回的对象数据
    private JSONObject getJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reason", "初始化成功");
            jsonObject.put("code", 0);

            LogUtil.log("sdk初始化成功返回数据=" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }





    //调用js接口（上报回调方法）,目前不需要此方法
    private void roleData(String data) {

        final int version = Build.VERSION.SDK_INT;
        if (version < 18) {

            mweview.loadUrl("javascript:roleReportCallback('" + data + "')");


        } else { // 该方法在 Android 4.4 版本才可使用，
            //开线程进行js方法调用
            mweview.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {

                    mweview.evaluateJavascript("javascript:roleReportCallback('" + roleDate + "')", new ValueCallback<String>() {

                        @Override
                        public void onReceiveValue(String value) {
                            // TODO Auto-generated method stub

                        }
                    });
                }
            });

        }


    }






    @Override
    protected void onPause() {
        mweview.onPause();
        mweview.pauseTimers();//调用pauseTimers()全局停止Js
        super.onPause();
        m_proxy.onPause();

    }

    @Override
    protected void onResume() {
        mweview.onResume();
        mweview.resumeTimers(); //调用onResume()恢复js
        super.onResume();
        m_proxy.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        m_proxy.onStop();

    }

    @Override
    protected void onDestroy() {
        if (mweview != null) {
            mweview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mweview.clearHistory();
            ((ViewGroup) mweview.getParent()).removeView(mweview);
            mweview.destroy();
            mweview = null;
        }
        super.onDestroy();
        m_proxy.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if(keyCode == KeyEvent.KEYCODE_BACK && mweview.canGoBack()){

            LogUtil.log("webView返回上一页");
            mweview.goBack(); //返回上一页
            return true;

        }else  if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){ //防止按音量键调用退出（减小键监听）

            //LogUtil.log("按了音量减");





        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){  ////防止按音量键调用退出（音量增加键监听）
            // LogUtil.log("按了音量加");

        }

        else{

            if(m_proxy.hasThirdPartyExit()){

                m_proxy.onThirdPartyExit();

                return true;

            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("游戏");
                builder.setMessage("真的忍心退出游戏么？");
                builder.setPositiveButton("忍痛退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("手误点错", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();

                return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }


}

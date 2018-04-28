package com.proxy.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.proxy.OpenSDK;
import com.proxy.R;
import com.proxy.util.DeviceUtil;
import com.proxy.util.JsInterface;
import com.proxy.util.JsInterface.wvClientClickListener;
import com.proxy.util.LoadingDialog;
import com.proxy.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class StartWebView extends Activity implements OnClickListener {

	private WebView webView;
	private FrameLayout video_fullView;// 全屏时视频加载view
	private View xCustomView;
	private CustomViewCallback xCustomViewCallback;
	private myWebChromeClient xwebchromeclient;
	private static String m_url = null;
	private com.proxy.util.JsInterface JsInterface = new JsInterface();
	private static Activity m_activity = null;
	private ProgressDialog waitdialog = null;
	private static long mTime = System.currentTimeMillis();
	private Button m_backBtn = null;
	private TextView m_backText = null;
	private View m_titlebg = null;
	public static int WIDTH = 0;
	public static int HEIGHT = 0;
	private Activity mActivity;
	private static String m_wxUrl;

	private TextView tv_back;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogUtil.e("ccccxxxx");

		m_activity = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉应用标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.pmc_webvidio);
		tv_back =  (TextView) findViewById(R.id.tv_back_id);
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		m_url = getIntent().getExtras().getString("url");
		m_wxUrl = getIntent().getExtras().getString("wxUrl");
		LogUtil.e("url:" + m_url+"  wxUrl="+m_wxUrl);
		//
		// waitdialog = new ProgressDialog(this);
		// waitdialog.setTitle("提示");
		// waitdialog.setMessage("页面加载中...");
		// waitdialog.setIndeterminate(true);
		// waitdialog.setCancelable(true);
		// waitdialog.show();

		LoadingDialog.show(m_activity, "请稍等...", true);

		if (DeviceUtil.isFastMobileNetwork(m_activity)
				|| DeviceUtil.isWifi(m_activity)) {
			// 高速网络忽略
			LogUtil.e("高速网络");
		} else {
			// 延时三秒停止
			LogUtil.e("非高速网络");
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				public void run() {
					waitdialog.dismiss();
				}

			}, 3000);
		}

		// 判断是否wifi如果不是wifi活着其他的就延时

		webView = (WebView) findViewById(R.id.webView);
		video_fullView = (FrameLayout) findViewById(R.id.video_fullView);

		WebSettings ws = webView.getSettings();
//		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		ws.setUseWideViewPort(true);// 可任意比例缩放
		ws.setSavePassword(true);
		ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptEnabled(true);
		ws.setDomStorageEnabled(true);
		ws.setSupportMultipleWindows(true);// 新加
		// 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
		ws.setJavaScriptEnabled(true);
		ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

		xwebchromeclient = new myWebChromeClient();
		webView.setWebChromeClient(xwebchromeclient);
		//webView.setWebViewClient(new myWebViewClient());
		webView.setWebViewClient(new MyWebViewClient());
		webView.addJavascriptInterface(JsInterface, "JsInterface");
		JsInterface.setWvClientClickListener(new WebviewClick());// 这里就是js调用java端的具体实现
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);

		webView.loadUrl(m_url);

//		addBackBtn();

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		WIDTH = dm.widthPixels;
		HEIGHT = dm.heightPixels;

	}

	/*public class myWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			LogUtil.e("shouldOverrideUrlLoading  url = " + url);
//			 if (url.startsWith(m_wxUrl.substring(0, 10))) {  
//				 LogUtil.e("拦截到的微信支付  "+m_wxUrl.substring(0, 10));
////				 view.loadUrl(url);
//             } else {  
//            	 view.loadUrl(url);
//             }       
			 view.loadUrl(url);

			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			// waitdialog.dismiss();
			LoadingDialog.dismiss();
		}

	}
	
	*/
	
	
	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			LogUtil.e("shouldOverrideUrlLoading  url = " + url);
	
			 // 如下方案可在非微信内部WebView的H5页面中调出微信支付
           if (url.startsWith("weixin://wap/pay?")){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
               return true;
            }else {

                //请求头
                Map extraHeaders = new HashMap();
                extraHeaders.put("Referer","https://pay.ipaynow.cn");
                view.loadUrl(url,extraHeaders);
            }

			
            return super.shouldOverrideUrlLoading(view, url);
            
			// view.loadUrl(url);

		//	return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			LoadingDialog.dismiss();
		}

	}

	
	
	
	
	
	
	
	

	public class myWebChromeClient extends WebChromeClient {

		private View xprogressvideo;

		// 播放网络视频时全屏会被调用的方法
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			webView.setVisibility(View.GONE);
			// 如果一个视图已经存在，那么立刻终止并新建一个
			if (xCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			video_fullView.setVisibility(View.VISIBLE);
			video_fullView.addView(view);
			xCustomView = view;
			xCustomViewCallback = callback;
			video_fullView.setVisibility(View.VISIBLE);
			showBackBtn();

		}

		// 视频播放退出全屏会被调用的
		@Override
		public void onHideCustomView() {
			if (xCustomView == null)// 不是全屏播放状态
				return;
			hideBackBtn();
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			xCustomView.setVisibility(View.GONE);
			video_fullView.removeView(xCustomView);
			xCustomView = null;
			video_fullView.setVisibility(View.GONE);
			xCustomViewCallback.onCustomViewHidden();
			webView.setVisibility(View.VISIBLE);
		}

		@Override
		public View getVideoLoadingProgressView() {
			// TODO Auto-generated method stub
			if (xprogressvideo == null) {
				LayoutInflater inflater = LayoutInflater.from(m_activity);
				xprogressvideo = inflater.inflate(
						R.layout.pmc_video_loading_progress, null);
			}
			return xprogressvideo;
		}

	}

	/**
	 * 判断是否是全屏
	 * 
	 * @return
	 */
	public boolean inCustomView() {
		return (xCustomView != null);
	}

	/**
	 * 全屏时按返加键执行退出全屏方法
	 */
	public void hideCustomView() {
		xwebchromeclient.onHideCustomView();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtil.e("onResume");
		
		LogUtil.log("=========onResume");
		
		if (webView != null) {
			LogUtil.e("webView is not null");

			// try {
			// //
			// webView.getClass().getMethod("onResume").invoke(webView,(Object[])null);
			// } catch (IllegalAccessException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IllegalArgumentException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (InvocationTargetException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (NoSuchMethodException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			webView.onResume();
			webView.resumeTimers();
			/**
			 * 设置为横屏
			 */
//			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtil.e("onPause");
		LogUtil.log("=========onPause");
		
		if (webView != null) {
			LogUtil.e("webView is not null");
			webView.onPause();
			webView.pauseTimers();
			
			
			
			// try {
			// webView.getClass().getMethod("onPause").invoke(webView,(Object[])null);
			// } catch (IllegalAccessException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IllegalArgumentException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (InvocationTargetException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (NoSuchMethodException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		LogUtil.log("=========onStoproy");
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		
		
		LogUtil.log("=========onDestroy");
		
		video_fullView.removeAllViews();
		webView.loadUrl("about:blank");
		webView.stopLoading();
		webView.setWebChromeClient(null);
		webView.setWebViewClient(null);
		webView.destroy();
		webView = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LogUtil.e("返回++");
			hideBackBtn();
			if (inCustomView()) {
				// webViewDetails.loadUrl("about:blank");
				hideCustomView();

				return true;
			} else {
				webView.loadUrl("about:blank");
				StartWebView.this.finish();
			}
		}
		return false;
	}

	class WebviewClick implements wvClientClickListener {

		@Override
		public void wvHasClickEnvent(String title, String content,
				String imageUrl, String url) {

			LogUtil.e("title:" + title + "content:" + content + "imageUrl:"
					+ imageUrl + "url:" + url);
		}

		@Override
		public void wvCloseWebEvent() {
			// TODO Auto-generated method stub
			m_activity.finish();
			m_activity = null;
		}

		@Override
		public void wvWxWebPayEvent() {
			// TODO Auto-generated method stub
			LogUtil.e("wvWxWebPayEvent 回调");
//			video_fullView.removeAllViews();
//			webView.loadUrl("about:blank");
//			webView.stopLoading();
//			webView.setWebChromeClient(null);
//			webView.setWebViewClient(null);
//			webView.destroy();
//			webView = null;	
		/*	
			Intent intent = new Intent();  
            intent.setAction(Intent.ACTION_VIEW);  
            intent.setData(Uri.parse(m_wxUrl));  
            startActivity(intent);
			*/
			
			
			   webView.loadUrl(m_wxUrl);
	           Map extraHeaders = new HashMap();
	           extraHeaders.put("Referer", "https://pay.ipaynow.cn");//例如 http://www.baidu.com 
	           webView.loadUrl(m_wxUrl, extraHeaders);//targetUrl为微信下单地址
			 


		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
	}

	// 添加返回键按钮
	public void addBackBtn() {

		final RelativeLayout rl = new RelativeLayout(m_activity);
		RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		// 添加返回按钮
		m_backBtn = new Button(m_activity);
		RelativeLayout rl4 = new RelativeLayout(m_activity);
		m_backBtn.setBackgroundResource(R.drawable.pmc_game_back);
		m_backBtn.setLayoutParams(new LayoutParams(90, 60));
		m_backBtn.setPadding(0, 0, 0, 0);
		m_backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
				xCustomView.setVisibility(View.GONE);
				video_fullView.removeView(xCustomView);
				xCustomView = null;
				video_fullView.setVisibility(View.GONE);
				xCustomViewCallback.onCustomViewHidden();
				webView.setVisibility(View.VISIBLE);
				hideBackBtn();
			}
		});

		RelativeLayout rl5 = new RelativeLayout(m_activity);
		m_titlebg = new View(m_activity);
		m_titlebg.setBackgroundColor(Color.BLACK);
		m_titlebg
				.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 70));
		m_titlebg.getBackground().setAlpha(150);

		// m_backText = new TextView(m_activity);
		// m_backText.setText("返回");
		// m_backText.setLayoutParams( new LayoutParams(90,60) );
		// m_backText.setPadding(0,0,0,0);
		// m_backText.setOnClickListener( new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// xCustomView.setVisibility(View.GONE);
		// video_fullView.removeView(xCustomView);
		// xCustomView = null;
		// video_fullView.setVisibility(View.GONE);
		// xCustomViewCallback.onCustomViewHidden();
		// webView.setVisibility(View.VISIBLE);
		// hideBackBtn();
		//
		// }
		// } );

		rl4.addView(m_backBtn);
		// rl5.addView(m_backText);
		rl5.addView(m_titlebg);

		RelativeLayout.LayoutParams relLayoutParams4 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		relLayoutParams4.setMargins(10, 10, 0, 0);
		rl4.setLayoutParams(relLayoutParams4);

		RelativeLayout.LayoutParams relLayoutParams5 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		relLayoutParams5.setMargins(0, 0, 0, 0);
		rl5.setLayoutParams(relLayoutParams5);

		rl.addView(rl4);
		rl.addView(rl5);

		m_activity.addContentView(rl, relLayoutParams);

		hideBackBtn();

	}

	public void showBackBtn() {
		if (m_backBtn != null) {
			m_backBtn.setVisibility(View.VISIBLE);
		}
		if (m_titlebg != null) {
			m_titlebg.setVisibility(View.VISIBLE);
		}

	}

	public void hideBackBtn() {
		if (m_backBtn != null) {
			m_backBtn.setVisibility(View.INVISIBLE);
		}
		if (m_titlebg != null) {
			m_titlebg.setVisibility(View.INVISIBLE);
		}
	}

}

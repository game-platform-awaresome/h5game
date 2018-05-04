package com.mc.game;

import com.proxy.Constants;
import com.proxy.config.McProxy;
import com.proxy.util.LogUtil;
import com.tencent.smtt.sdk.QbSdk;

import android.app.Activity;
import android.app.Application;

public class BaseApplication extends Application {

	private Activity mActivity;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				LogUtil.log("app加载X5内核是否成功=" + arg0);
			}

			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb);

		//初始化配置
		McProxy.init(this)
				.withApiHost(Constants.OMD_URLS)
				.withActivity(mActivity)
				.withGameID("rxcqh5") //游戏分配的gameId
				.withGamekey("uVkyGhiKWm7T2B43n5rEafHleXwPzjRU") //游戏分配的gamekey
				.withGameName("rxcqh5") //游戏分配的gameName
				.withGameOrientation(1) //游戏横竖屏：   0：横屏   1：竖屏
				.configure();



	}

}

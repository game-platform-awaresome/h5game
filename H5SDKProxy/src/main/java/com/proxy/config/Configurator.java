package com.proxy.config;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * app配置类，单例模式
 */

public final class Configurator {

    private static final HashMap<Object,Object> MC_CONFIGS = new HashMap<>();
    private static final Handler HANDLER = new Handler();



    private Configurator(){

        MC_CONFIGS.put(ConfigKeys.CONFIG_READY,false);//默认是设置未初始化
        MC_CONFIGS.put(ConfigKeys.HANDLER,HANDLER);//添加Handler
    }


    static Configurator getInstance(){
        return Holder.INSTANCE;
    }

    //静态内部类单例模式
     private static class Holder{

        private static final Configurator INSTANCE = new Configurator();

     }

    final HashMap<Object, Object> getMcConfigs() {
        return MC_CONFIGS;
    }


    public final void configure(){

        Logger.addLogAdapter(new AndroidLogAdapter()); //设置loding
        MC_CONFIGS.put(ConfigKeys.CONFIG_READY,true);//app配置初始化完成
        //Utils工具获取app全局上下文
        Utils.init(McProxy.getApplicationContext());
    }


    public final Configurator withApiHost(String host) {
        MC_CONFIGS.put(ConfigKeys.API_HOST, host);
        return this;
    }

    //网络加载延时时间
    /*public final Configurator withLoaderDelayed(long delayed) {
        MC_CONFIGS.put(ConfigKeys.LOADER_DELAYED, delayed);
        return this;
    }*/


    public final Configurator withGameID(String gameId){
        MC_CONFIGS.put(ConfigKeys.GAME_ID,gameId);
        return this;
    }
    public final Configurator withGameName(String gameName){
        MC_CONFIGS.put(ConfigKeys.GAME_NAME,gameName);
        return this;
    }

    public final Configurator withGamekey(String gamekey){
        MC_CONFIGS.put(ConfigKeys.GAME_KEY,gamekey);
        return this;
    }


    public final Configurator withGameOrientation(int Orientation){
        MC_CONFIGS.put(ConfigKeys.GAME_ORIEN,Orientation);
        return this;
    }


    public final Configurator withActivity(Activity activity) {
        MC_CONFIGS.put(ConfigKeys.ACTIVITY, activity);
        return this;
    }


    //查询配置初始化是否完成
    private void checkConfiguration(){

        final boolean isReady = (boolean) MC_CONFIGS.get(ConfigKeys.CONFIG_READY);
        if (!isReady){
            throw new RuntimeException("初始化未完成");
        }
    }

    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key){

        checkConfiguration();
        final Object value = MC_CONFIGS.get(key);
        if (value == null){
            throw new NullPointerException(key.toString()+"IS NULL");
        }
        return (T) MC_CONFIGS.get(key);

    }






}





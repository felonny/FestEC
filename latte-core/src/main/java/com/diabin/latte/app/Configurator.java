package com.diabin.latte.app;

import java.util.ArrayList;
import java.util.WeakHashMap;

import okhttp3.Interceptor;

/**
 * Created by tangyuchen on 17/11/29.
 * 配置文件，配置应用所需要的信息，主机地址，上下文等，可以拓展
 * 采用静态内部类的方式创建单例
 * Weakhashmap 比起hashmap它在内存不足的情况下会回收
 *
 * LATTE_CONFIGS:存储所有配置信息
 * getLatteConfigs():返回配置信息的map
 * configure():具体准备
 * withApiHost(String):配置host信息
 */

public class Configurator {
    private static final WeakHashMap<Object,Object> LATTE_CONFIGS = new WeakHashMap<>();
    //没有后台用拦截器模拟
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();

    private Configurator(){

        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(),false);
    }

    //静态内部类单例模式，获取配置类的实例
    public static Configurator getInstance(){

        return Holder.INSTANCE;
    }

    //静态内部类单例模式，获取配置类的实例
    private static class Holder{
        private static final Configurator INSTANCE = new Configurator();
    }

    final WeakHashMap<Object,Object> getLatteConfigs(){

        return LATTE_CONFIGS;
    }

    //配置文件初始准备
    public final void configure(){

        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(),true);
    }

    //配置主机地址
    public final Configurator withApiHost(String host){
        LATTE_CONFIGS.put(ConfigType.API_HOST.name(),host);
        return this;
    }

    //配置拦截器
    public final Configurator withInterceptor(Interceptor interceptor){
        INTERCEPTORS.add(interceptor);
        LATTE_CONFIGS.put(ConfigType.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    //配置拦截器
    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors){
        INTERCEPTORS.addAll(interceptors);
        LATTE_CONFIGS.put(ConfigType.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    private void checkConfiguration(){
        final boolean isReady = (boolean)LATTE_CONFIGS.get(ConfigType.CONFIG_READY.name());
        if(!isReady){
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }

    //获取配置文件的信息
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Enum<ConfigType> key){
        checkConfiguration();
        return (T)LATTE_CONFIGS.get(key.name());
    }

}

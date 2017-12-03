package com.diabin.latte.app;

import android.app.Application;
import android.content.Context;

import java.util.WeakHashMap;

/**
 * Created by tangyuchen on 17/11/29.
 */
//不被继承
public final class Latte {

    public static Configurator init(Context context) {
        getConfigurations().put(ConfigType.APPLICATION_CONTEXT.name(),context.getApplicationContext());
        return Configurator.getInstance();
    }

    public static WeakHashMap<Object,Object> getConfigurations(){
        return Configurator.getInstance().getLatteConfigs();
    }

    public static Configurator getConfigurator(){
        return Configurator.getInstance();
    }

    public static final <T> T getConfiguration(Enum<ConfigType> key){

        return getConfigurator().getConfiguration(key);
    }

    public static Context getApplicationContext(){
        return getConfiguration(ConfigType.APPLICATION_CONTEXT);
    }
}

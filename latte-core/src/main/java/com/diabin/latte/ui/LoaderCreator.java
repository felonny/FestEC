package com.diabin.latte.ui;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * Created by tangyuchen on 17/12/1.
 *
 * 当前类是改进AVLoadingindicator类库的类。目的是在每次load的时候不会重复生成动画，利用反射机制，在indicator存在的情况下，直接用缓存中的
 */

public final class LoaderCreator {

    private static final WeakHashMap<String,Indicator> LOADING_MAP = new WeakHashMap<>();

    static AVLoadingIndicatorView creat(String type, Context context){
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if(LOADING_MAP.get(type) == null){
            final Indicator indicator = getIndicator(type);
            LOADING_MAP.put(type,indicator);
        }
        //每一个AVLoadingIndicatorView都是由indicator唯一标识
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;
    }

    //Indicator是drawable类，利用反射机制可以创建indicator实例
    private static Indicator getIndicator(String name){
        if(name == null||name.isEmpty()){
            return null;
        }
        final StringBuilder drawableClassName = new StringBuilder();
        if(!name.contains(".")){
            //反射
            final String defaultPackageName = AVLoadingIndicatorView.class.getPackage().getName();
            drawableClassName.append(defaultPackageName)
                    .append(".indicators")
                    .append(".");
        }
        drawableClassName.append(name);
        try {
            final Class<?> drawableClass = Class.forName(drawableClassName.toString());
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

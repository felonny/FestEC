package com.diabin.latte.net.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by tangyuchen on 17/12/3.
 * 拦截器是服务器端的，用它可以获取（get）请求的url，参数等，但是在此我们不花时间写服务端，所以利用拦截器模拟获取url
 */

public class BaseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }
}

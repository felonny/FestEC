package com.diabin.latte.net;

import com.diabin.latte.app.ConfigType;
import com.diabin.latte.app.Latte;

import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by tangyuchen on 17/11/30.
 * Build mode
 * 得到 RestService 的返回实例 Retrofit是以接口的形式做网络操作的，不能直接new实例化service,
 * class RestServiceHolder 通过这个类可以创建service实例
 * class RetrofitHolder 上面的类是依靠这个类的create方法创建的实例
 * class OKHttpHolder retrofitHolder的client是封装的OkHttp的client,在OkHttp中可以初始化拦截器
 *
 */

public class RestCreator {

    public static RestService getRestService(){
        return RestServiceHolder.REST_SERVICE;
    }

    private static final class ParamsHolder{
        public static final WeakHashMap<String,Object> PARAMS = new WeakHashMap<>();
    }

    public static WeakHashMap<String,Object> getParams(){
        return ParamsHolder.PARAMS;
    }

    private static final class RetrofitHolder{
        private static final String BASE_URL = (String) Latte.getConfigurations().get(ConfigType.API_HOST.name());
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    private static final class OKHttpHolder{
        private static final int TIME_OUT = 60;
        private static final ArrayList<Interceptor> INTERCEPTORS = Latte.getConfiguration(ConfigType.INTERCEPTOR);
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static OkHttpClient.Builder addInterceptor(){
            if(INTERCEPTORS!= null && !INTERCEPTORS.isEmpty()){
                for(Interceptor interceptor:INTERCEPTORS){
                    BUILDER.addInterceptor(interceptor);
                }
            }
            return BUILDER;
        }
        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    private static final class RestServiceHolder{
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }
}

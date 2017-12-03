package com.diabin.latte.net;

import android.content.Context;
import android.support.v4.os.IResultReceiver;

import com.diabin.latte.net.Callback.IError;
import com.diabin.latte.net.Callback.IFailure;
import com.diabin.latte.net.Callback.IRequest;
import com.diabin.latte.net.Callback.ISuccess;
import com.diabin.latte.ui.LoaderStyle;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by tangyuchen on 17/11/30.
 * 当前类负责具体的传值操作，build设计模式的组成部分
 */

public class RestClientBuilder {

    private  String mUrl;
    private  static final Map<String,Object> PARAMS = RestCreator.getParams();
    private  IRequest mIRequest = null;
    private  String mDownloadDir = null;
    private  String mExtension = null;
    private  String mName = null;
    private  ISuccess mISuccess = null;
    private  IFailure mIFailure = null;
    private  IError mIError = null;
    private  RequestBody mBody = null;
    private  File mFile = null;
    private  LoaderStyle mLoaderStyle = null;
    private  Context mContext = null;

    //only RestClientBuilder can use this class
    RestClientBuilder(){

    }

    //URL
    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    //请求参数
    public final RestClientBuilder params(WeakHashMap<String,Object> params){
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key,Object value){
        PARAMS.put(key,value);
        return this;
    }

    //RequestBody
    public final RestClientBuilder raw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }

    public final RestClientBuilder request(IRequest iRequest){
        this.mIRequest = iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess){
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure){
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError){
        this.mIError = iError;
        return this;
    }

    //上传的文件
    public final RestClientBuilder file(File file){
        this.mFile = file;
        return this;
    }

    //上传文件地址
    public final RestClientBuilder file(String file){
        this.mFile = new File(file);
        return this;
    }

    //下载文件的路径
    public final RestClientBuilder dir(String dir){
        this.mDownloadDir = dir;
        return this;
    }

    //下载文件后缀名
    public final RestClientBuilder extension(String extension){
        this.mExtension = extension;
        return this;
    }

    //下载文件的名称,完整文件名称
    public final RestClientBuilder name(String name){
        this.mName = name;
        return this;
    }

    //在网络中加入加载ui
    public final RestClientBuilder loader(Context context,LoaderStyle style){
        this.mLoaderStyle = style;
        this.mContext = context;
        return this;
    }

    //使用默认的loader
    public final RestClientBuilder loader(Context context){
        this.mLoaderStyle = LoaderStyle.BallSpinFadeLoaderIndicator;
        this.mContext = context;
        return this;
    }

    //build模式在此进行初始化RestClient,将参数传入
    public final Restclient build(){
        return new Restclient(mUrl,PARAMS,mIRequest,mDownloadDir,mExtension,mName,mISuccess,mIFailure,mIError,mBody,mFile,mLoaderStyle,mContext);
    }


}

package com.diabin.latte.net;

import android.content.Context;
import android.content.pm.FeatureInfo;

import com.diabin.latte.net.Callback.IError;
import com.diabin.latte.net.Callback.IFailure;
import com.diabin.latte.net.Callback.IRequest;
import com.diabin.latte.net.Callback.ISuccess;
import com.diabin.latte.net.Callback.RequestCallbacks;
import com.diabin.latte.net.download.DownloadHandler;
import com.diabin.latte.ui.LatteLoader;
import com.diabin.latte.ui.LoaderStyle;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by tangyuchen on 17/11/30.
 * 网络框架
 * 建造者模式比较合适，传什么建造什么
 */

public class Restclient {

    private final String URL;
    private static final WeakHashMap<String,Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;
    private final File FILE;


    public Restclient(String url,
                      Map<String, Object> params,
                      IRequest request,
                      String download_dir,
                      String extension,
                      String name,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      RequestBody body,
                      File file,
                      LoaderStyle loaderStyle,
                      Context context) {
        this.URL = url;
        PARAMS.putAll(params);
        this.REQUEST = request;
        this.DOWNLOAD_DIR = download_dir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.FILE = file;
        this.CONTEXT = context;
        this.LOADER_STYLE = loaderStyle;
    }

    public static RestClientBuilder builder(){
        return new RestClientBuilder();
    }

    private void request(HttpMethod method){
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;

        if(REQUEST != null){
            REQUEST.onRequestStart();
        }

        if(LOADER_STYLE != null){
            LatteLoader.showLoading(CONTEXT,LOADER_STYLE);
        }
        switch(method){
            case GET:
                call = service.get(URL,PARAMS);
                break;
            case POST:
                call = service.post(URL,PARAMS);
                break;
            case POST_RAW:
                call = service.postRaw(URL,BODY);
                break;
            case PUT:
                call = service.put(URL,PARAMS);
                break;
            case PUT_RAW:
                call = service.putRaw(URL,BODY);
            case DELETE:
                call = service.delete(URL,PARAMS);
                break;
            //上传文件
            case UPLOAD:
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                //以表单的形式提交
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file",FILE.getName(),requestBody);
                call = RestCreator.getRestService().upload(URL,body);
                break;
            default:
                break;
        }

        if(call != null){
            //异步
            call.enqueue(getRequestCallback());
        }
    }

    private Callback<String> getRequestCallback(){
        return new RequestCallbacks(REQUEST,SUCCESS,FAILURE,ERROR,LOADER_STYLE);
    }

    public final void get(){

        request(HttpMethod.GET);
    }

    public final void post(){
        if(BODY == null){
            request(HttpMethod.POST);
        }else{
            if(!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.POST_RAW);
        }

    }

    public final void put(){
        if(BODY == null){
            request(HttpMethod.PUT);
        }else{
            if(!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.PUT_RAW);
        }

    }

    public final void delete(){

        request(HttpMethod.DELETE);
    }

    public final void upload(){

        request(HttpMethod.UPLOAD);
    }

    public final void download(){

        new DownloadHandler(URL,REQUEST,DOWNLOAD_DIR,EXTENSION,NAME,SUCCESS,FAILURE,ERROR).handleDownload();
    }
}

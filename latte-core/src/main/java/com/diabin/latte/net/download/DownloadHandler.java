package com.diabin.latte.net.download;

import android.os.AsyncTask;

import com.diabin.latte.net.Callback.IError;
import com.diabin.latte.net.Callback.IFailure;
import com.diabin.latte.net.Callback.IRequest;
import com.diabin.latte.net.Callback.ISuccess;
import com.diabin.latte.net.RestCreator;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tangyuchen on 17/12/2.
 */

public class DownloadHandler {

    private final String url;
    private static final WeakHashMap<String,Object> params = RestCreator.getParams();
    private final IRequest request;
    private final String download_dir;
    private final String extension;
    private final String name;
    private final ISuccess success;
    private final IFailure failure;
    private final IError error;

    public DownloadHandler(
            String url,
            IRequest request,
            String download_dir,
            String extension,
            String name,
            ISuccess success,
            IFailure failure,
            IError error) {
        this.url = url;
        this.request = request;
        this.download_dir = download_dir;
        this.extension = extension;
        this.name = name;
        this.success = success;
        this.failure = failure;
        this.error = error;
    }

    public final void handleDownload(){
        if(request != null){
            request.onRequestStart();
        }
        //在download方法里面用来@streaming指的是一边下载，一边写入文件，所以要用异步
        RestCreator.getRestService().download(url,params)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if(response!= null){
                            ResponseBody responseBody = response.body();
                            final SaveFileTask task = new SaveFileTask(request,success);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,download_dir,extension,responseBody,name);

                            //一定要注意判断，否则文件有可能下载不全
                            if(task.isCancelled()){
                                if(request != null){
                                    request.onRequestEnd();
                                }
                            }
                        }else{
                            if(error != null){
                                error.onError(response.code(),response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        if(failure!= null){
                            failure.onFailure();
                        }
                    }
                });
    }
}

package com.diabin.festec.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.diabin.latte.delegate.LatteDelegate;
import com.diabin.latte.net.Callback.IError;
import com.diabin.latte.net.Callback.IFailure;
import com.diabin.latte.net.Callback.ISuccess;
import com.diabin.latte.net.Restclient;

/**
 * Created by tangyuchen on 17/11/30.
 */

public class ExampleDelegate extends LatteDelegate {

    @Override
    public  Object setLayout(){
        return R.layout.delegate_example;
    }

    @Override
    public  void onBindView(@Nullable Bundle savedInstanceState, View rootView){
        testRestClient();
    }

    private void testRestClient(){
        Restclient.builder()
                .url("http://news.baidu.com/")
                .loader(getContext())
                //.params("","")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .build().get();


    }
}

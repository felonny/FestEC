package com.diabin.latte.activities;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.ContentFrameLayout;

import com.diabin.latte.R;
import com.diabin.latte.delegate.LatteDelegate;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by tangyuchen on 17/11/29.
 * activity作为放置fragment的容器
 * 利用fragmentation这个框架，activity必须继承它的SupportActivity
 */

public abstract class ProxyActivity extends SupportActivity {

    public abstract LatteDelegate setRootDelegate();//一个fragment抽象对象

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContainer(savedInstanceState);
    }

    private void initContainer(@Nullable Bundle savedInstanceState){
        final ContentFrameLayout container = new ContentFrameLayout(this);
        container.setId(R.id.delegate_container);//R is diabin.latte.delegate's file
        setContentView(container);
        if(savedInstanceState == null){

            loadRootFragment(R.id.delegate_container,setRootDelegate());//load the fragment in the container
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        System.runFinalization();
    }
}

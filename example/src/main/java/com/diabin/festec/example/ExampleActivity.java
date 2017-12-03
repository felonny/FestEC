package com.diabin.festec.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.diabin.latte.activities.ProxyActivity;
import com.diabin.latte.delegate.LatteDelegate;

public class ExampleActivity extends ProxyActivity {

    @Override
    public  LatteDelegate setRootDelegate(){
        return new ExampleDelegate();
    }
}

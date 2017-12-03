package com.diabin.latte.ui;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.diabin.latte.R;
import com.diabin.latte.util.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by tangyuchen on 17/12/1.
 *
 * AVLoading的方法是将加载圈直接显示在view上，我们希望它显示在dialog上
 */

public class LatteLoader {

    private static final int LOADER_SIZE_SCALE = 8;
    private static final int LOADER_OFFSET_SCALE = 10;

    //方便统一管理，如果不需要loader时，只需要遍历集合，全部删除即可
    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();

    private static final String DEFAULT_LOADER = LoaderStyle.BallSpinFadeLoaderIndicator.name();

    public static void showLoading(Context context,Enum<LoaderStyle> type){
        showLoading(context,type.name());
    }

    public static void showLoading(Context context,String type){
        //use appCompat中的方法，兼容更好
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);

        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.creat(type, context);
        //将加载圈（avLoadingIndicatorView）作为分视图加载进dialog
        dialog.setContentView(avLoadingIndicatorView);

        int deviceWidth = DimenUtil.getScreenWidth();
        int deviceHeight = DimenUtil.getScreenHeight();

        final Window dialogWindow = dialog.getWindow();
        if(dialogWindow != null){
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = deviceWidth/LOADER_SIZE_SCALE;
            lp.height = deviceHeight/LOADER_SIZE_SCALE;
            lp.height = lp.height+deviceHeight/LOADER_OFFSET_SCALE;
            lp.gravity = Gravity.CENTER;
        }
        LOADERS.add(dialog);
        dialog.show();

    }

    public static  void showLoading(Context context){
        showLoading(context,DEFAULT_LOADER);
    }

    public static void stopLoading(){
        for(AppCompatDialog dialog :LOADERS){
            if(dialog!= null){
                if(dialog.isShowing()){
                    dialog.cancel();
                    //dialog.dismiss();
                }
            }
        }
    }
}

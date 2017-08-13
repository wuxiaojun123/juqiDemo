package cn.com.sfn.juqi;

import android.app.Application;

/**
 * Created by wuxiaojun on 2017/8/12.
 */

public class App extends Application{
    private static App mApp;


    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static App getApplication(){
        return mApp;
    }
}

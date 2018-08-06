package com.assignment.listview_images;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


/**
 * Custom Application class to maintain singleton instance
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    /**
     * Creating instance of the class
     * @return
     */
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }


}

package com.assignment.listview_images;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.assignment.listview_images.utils.AppUtil;
import com.assignment.listview_images.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Custom Application class to maintain singleton instance
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static Retrofit retrofit = null;

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
        initClasses();
    }

    private void initClasses() {
        new AppUtil(mInstance);

    }

    /**
     * Retrofit client creation
     */
    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.Url.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}

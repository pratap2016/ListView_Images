package com.assignment.listview_images;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.assignment.listview_images.presenter.APIInterface;
import com.assignment.listview_images.utils.AppUtil;
import com.assignment.listview_images.utils.Constants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

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
    private static ImageLoader imageLoader = null;

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
        // Apply configuration
        ImageLoader.getInstance().init(MyApplication.getInstance().getImageLoadingConfiguration());
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

    public static APIInterface getRetrofitService(){
        return getClient().create(APIInterface.class);
    }

    /**
     * UIL Display Option
     * @return
     */
    private DisplayImageOptions getDisplayOption(){
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_image_480)
                .showImageOnFail(R.drawable.ic_failed)
                .showImageOnLoading(R.drawable.ic_loading).build();

    }


    /**
     * UIL Image loading configuration
     * @return
     */
    private ImageLoaderConfiguration getImageLoadingConfiguration(){
        return new ImageLoaderConfiguration.Builder(mInstance)
                .defaultDisplayImageOptions(MyApplication.getInstance().getDisplayOption())
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .imageDownloader(new BaseImageDownloader(mInstance)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
    }


}

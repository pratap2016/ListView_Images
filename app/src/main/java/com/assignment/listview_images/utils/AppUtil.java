package com.assignment.listview_images.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * util class for the Application
 */

public class AppUtil {

     static Context mContext;


    /**
     *  Checking if device is connected to internet or not
     * @return
     * @param context
     */
    public static boolean isInternetConnected(Context context) {
        mContext = context;
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        if (null == cm.getActiveNetworkInfo()) {
            return false;
        }

        return isInternetAvailable();
    }

    /**
     *  Checking if internet connect or not for SDK >= Marshmallow
     * @return
     */
    private synchronized static boolean isInternetAvailable() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            final ConnectivityManager connectivityManager = (ConnectivityManager)mContext.
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            final Network network = connectivityManager.getActiveNetwork();
            final NetworkCapabilities capabilities = connectivityManager
                    .getNetworkCapabilities(network);

            return capabilities != null
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }
        else
            return false;
    }

    public static int getScreenResolutionWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;

        return width ;
    }

    public static int getScreenResolutionHeight(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int height = metrics.heightPixels;

        return  height ;
    }
}

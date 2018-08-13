package com.assignment.listview_images.utils;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.assignment.listview_images.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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

    /**
     * Checking internet connection on Device with SDK <= Lollipop
     * Using async task to check url to check if its return true or false
     */
    public static class checkInternet extends AsyncTask<String, Void, Boolean> {

        NetworkCall networkCall;
        public checkInternet(NetworkCall networkCall) {
            this.networkCall  = networkCall;
        }

        // Call back to update about internet present or not
        public interface NetworkCall{
            void getNetwork(boolean status);
        }

        // Background Call to the URL to check if internet present
        protected Boolean doInBackground(String... xyz) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(mContext.getResources()
                        .getString(R.string.str_google)).openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000);
                urlc.setReadTimeout(4000);
                urlc.connect();
                // networkcode2 = urlc.getResponseCode();
                networkCall.getNetwork((urlc.getResponseCode() == 200));
                return (urlc.getResponseCode() == 200);

            } catch (IOException e) {
                networkCall.getNetwork(false);
                return false;
            }

        }

    }

    /**
     * Check if All Uses Permissions are granted by User or not
     * If granted return true else return false
     * @return
     */
    public static boolean verifyAllPermissions(int[] permissions) {
        for (int result : permissions) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if external storage is available.
     * If available return true else return false
     * @return
     */
    public static boolean hasSelfPermission(Activity activity, String[] permissions) {

        // Verify that all the permissions.
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(null != activity)
                    if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
            }
        }
        return true;
    }

    /**
     * Getting device screen resolution for Image caching
     * @param context
     * @return
     */
    public static int getScreenResolutionWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;

        return width ;
    }

    /**
     * Getting device screen resolution for Image caching
     * @param context
     * @return
     */
    public static int getScreenResolutionHeight(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int height = metrics.heightPixels;

        return  height ;
    }

    /**
     * Getting screen density of device
     * @param mContext
     * @return
     */
    public static String getScreenDensity(Context mContext){
        int density= mContext.getResources().getDisplayMetrics().densityDpi;
        String screenDensity = null;
        switch(density)
        {
            case DisplayMetrics.DENSITY_LOW:
                screenDensity = mContext.getResources().getString(R.string.ldpi);

                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                screenDensity = mContext.getResources().getString(R.string.mdpi);

                break;
            case DisplayMetrics.DENSITY_HIGH:
                screenDensity = mContext.getResources().getString(R.string.hdpi);

                break;
            case DisplayMetrics.DENSITY_XHIGH:
                screenDensity = mContext.getResources().getString(R.string.xhdpi);

                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                screenDensity = mContext.getResources().getString(R.string.xxhdpi);

                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                screenDensity = mContext.getResources().getString(R.string.xxxhdpi);

                break;
        }
        return screenDensity;
    }
}

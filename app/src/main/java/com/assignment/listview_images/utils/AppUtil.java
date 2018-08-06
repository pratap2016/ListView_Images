package com.assignment.listview_images.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Build;

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
     *  Application Util class parameterized constructor to initialize
     *  required components to use the Util class
     * @param context
     */
    public AppUtil(Context context) {
        mContext = context;
    }

    /**
     *  Checking if device is connected to internet or not
     * @return
     */
    public static boolean isInternetConnected() {
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
}

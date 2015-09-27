package com.codepath.gridimagesearch.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by shehba.shahab on 9/27/15.
 */
public class NetworkUtil {

    public static Boolean isNetworkAvailable(Context con) {
        ConnectivityManager connectivityManager;
        NetworkInfo wifiInfo, mobileInfo;
        try {
            connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
        }

        return false;
    }
}
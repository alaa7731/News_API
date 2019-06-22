package com.android.news.network.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import com.android.news.dagger.BaseApplication;

/**
 * Created by Alaa on 12/4/2017.
 * Modified by FADEL on 9/11/2017.
 */
public class InternetConnectionStatus {

    private static InternetConnectionStatus mInstance;

    private static final Object SINGLETON_LOCK = new Object();
    @Nullable
    private final ConnectivityManager connectivityManager;
    @Nullable
    private final WifiManager wifiManager;
    @Nullable
    private final TelephonyManager telephonyManager;

    private static class ConnectionStatusLoader {
        private static final InternetConnectionStatus mInstance =
                new InternetConnectionStatus();
    }

    private Status curStatus;

    public Status getCurStatus() {
        return curStatus;
    }

    private InternetConnectionStatus() {

        if (mInstance != null) {
            throw new IllegalStateException("Cannot create another instance from this singleton class");
        }

        connectivityManager = (ConnectivityManager) (BaseApplication.Companion.getAppContext())
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        wifiManager = (WifiManager) (BaseApplication.Companion.getAppContext())
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        telephonyManager = (TelephonyManager) (BaseApplication.Companion.getAppContext())
                .getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);


        curStatus = Status.CONNECTED;
    }

    public static InternetConnectionStatus getInstance() {
        InternetConnectionStatus instance = mInstance;
        if (instance == null) {
            synchronized (SINGLETON_LOCK) {
                instance = mInstance;
                if (instance == null) {
                    mInstance = instance = new InternetConnectionStatus();
                }
            }
        }
        return instance;
    }

    private void getConnectivityStatus() {
        if (connectivityManager != null) {
            if (wifiManager != null) {
                if (wifiManager.isWifiEnabled()) { // Wifi os ON.
                    curStatus = checkTraffic(connectivityManager.getActiveNetworkInfo());
                    return;
                }
            }

            if (telephonyManager != null) {
                boolean mobileConnected = (telephonyManager.getDataState() ==
                        TelephonyManager.DATA_CONNECTED);
                if (mobileConnected) { // Mobile data os ON.
                    curStatus = checkTraffic(connectivityManager.getActiveNetworkInfo());
                    return;
                }
            }
        }

        curStatus = Status.NO_INTERNET_CONNECTION; // Wifi is OFF, Mobile Data is ON.
    }

    private Status checkTraffic(NetworkInfo activeNetwork) {
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) ?
                Status.CONNECTED : Status.WAITING_FOR_NETWORK;
    }

    public boolean isConnected() {
        getConnectivityStatus();
        return curStatus != Status.NO_INTERNET_CONNECTION;
    }

    public enum Status {
        NO_INTERNET_CONNECTION, WAITING_FOR_NETWORK, RECONNECTING, CONNECTED
    }
}

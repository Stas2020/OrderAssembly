package com.westas.orderassembly.WiFi;

import static android.content.Context.WIFI_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class TUtilsWiFi extends BroadcastReceiver {

    private Context context;
    TStatusWiFi status;
    public TUtilsWiFi(Context context_)
    {
        context = context_;
    }
    public void StatrMonitoring(TStatusWiFi status_)
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        context.registerReceiver(this,intentFilter);

        status = status_;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifi = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (wifi.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifi.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                status.OnLost();
            }
            status.OnConnected();
        }
        else {
            status.OnLost();
        }

    }
}

package com.betcade.helper.downloadmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null && info.isConnected()) {
            Toast.makeText(context, "Wifi Connected", Toast.LENGTH_LONG).show();
            Intent intentBetcadeDownloadManager = new Intent("com.betcade.services.BetcadeDownloadManager.INTENT_FILTER");
            intentBetcadeDownloadManager.setPackage(context.getPackageName());
            context.startService(intentBetcadeDownloadManager);
        }
    }
}
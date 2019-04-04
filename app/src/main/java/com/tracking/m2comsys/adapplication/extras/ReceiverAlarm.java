package com.tracking.m2comsys.adapplication.extras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;




public class ReceiverAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Email receiver", Toast.LENGTH_SHORT).show();
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {

            Log.d("writing", "Conversion_started");
        Intent background = new Intent(context, BackgroundService.class);

        context.startService(background);
    }

            else{
        Intent background = new Intent(context, BackgroundService.class);
        context.stopService(background);

    }}}


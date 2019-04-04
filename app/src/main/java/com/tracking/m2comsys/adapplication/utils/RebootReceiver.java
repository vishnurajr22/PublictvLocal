package com.tracking.m2comsys.adapplication.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tracking.m2comsys.adapplication.Activity.MainActivity;
import com.tracking.m2comsys.adapplication.Activity.SplashScreen;

/**
 * Created by m2 on 12/9/17.
 */

public class RebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */
            Toast.makeText(context, "Boot complete event", Toast.LENGTH_SHORT).show();
            if(CommonDataArea.getAlredyStarted()==200) return;;
            CommonDataArea.bootEvent = true;
            Intent intent1 = new Intent(context.getApplicationContext(), SplashScreen.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent1);
        }
    }
}
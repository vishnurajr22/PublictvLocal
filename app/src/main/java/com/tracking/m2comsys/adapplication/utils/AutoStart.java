package com.tracking.m2comsys.adapplication.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tracking.m2comsys.adapplication.Activity.MainActivity;
import com.tracking.m2comsys.adapplication.Activity.SplashScreen;

/**
 * Created by m2comsys on 27/6/17.
 */

public class AutoStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent start = new Intent(context, SplashScreen.class);
               context.startActivity(start);
    }
}

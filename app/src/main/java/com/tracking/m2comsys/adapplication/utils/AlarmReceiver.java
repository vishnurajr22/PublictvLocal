package com.tracking.m2comsys.adapplication.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.google.api.client.util.DateTime;
import com.tracking.m2comsys.adapplication.Activity.MainActivity;
import com.tracking.m2comsys.adapplication.Activity.SplashScreen;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by m2 on 12/9/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isAppRunning = isAppForground(context);
        if (!isAppRunning) {
            ++CommonDataArea.appNotRunningCount;
            LogWriter.writeLogCheckLive("OnAlarm","App not running(foreground)->"+CommonDataArea.appNotRunningCount);
            if (CommonDataArea.appNotRunningCount > 12) {
                LogWriter.writeLogCheckLive("OnAlarm","Starting app->"+CommonDataArea.appNotRunningCount);
                Intent intent1 = new Intent(context.getApplicationContext(), SplashScreen.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent1);
            }
        } else {
            LogWriter.writeLogCheckLive("OnAlarm","App Running");
            CommonDataArea.appNotRunningCount = 0;
        }

        //Send HB to server
        if((System.currentTimeMillis()-CommonDataArea.lastLogSendTime)>CommonDataArea.hbTimeDelay){
           sendHB(context);
        }
        //Update campaign status
        ++CommonDataArea.updateStatusDelay;
        if(CommonDataArea.updateStatusDelay>10) {
            sendHB(context);
            CommonDataArea.updateStatusDelay=0;
            UpdatePlayCount.updateCompltedPendPlayCountAndChangeSts();
            UpdatePlayCount.updateAndCloseCompltedAdvts();
        }

        Date currentTime = Calendar.getInstance().getTime();
        if (currentTime.getHours() == 0) {
            LogWriter.writeLogCheckLive("OnAlarm","Midnight check");
            if ((CommonDataArea.startedAt - System.currentTimeMillis()) / (1000 * 60 * 60) > 4) {
                LogWriter.writeLogCheckLive("OnAlarm","Midnight Restart");
                Intent mStartActivity = new Intent(context, SplashScreen.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);

            }
        }
    }

    public static void sendHB(Context context){
        long adjustedDevTimeMills=Utils.getAdjustedTimeMills(context);

        String statusToSend = "PlayMethod->"+CommonDataArea.curSysStatus_Playmethod + " Adjusted Time ->"+adjustedDevTimeMills+" Slot->"+ CommonDataArea.slotName;
        Utils.sendLogToServer("HB:"+statusToSend,context);
    }

    public boolean isAppForground(Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return false;
            }
        }

        return true;
    }
}

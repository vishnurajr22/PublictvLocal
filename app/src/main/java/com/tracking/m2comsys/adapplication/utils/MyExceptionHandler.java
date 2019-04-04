package com.tracking.m2comsys.adapplication.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.tracking.m2comsys.adapplication.Activity.SplashScreen;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by m2comsys on 11/7/17.
 */

public class MyExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private final Context myContext;
    private final Class<?> myActivityClass;

    public MyExceptionHandler(Context context, Class<?> c) {

        myContext = context;
        myActivityClass = c;
    }

    public void uncaughtException(Thread thread, Throwable exception) {

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);
        String s = stackTrace.toString();

        LogWriter.writeUncaughtLog("Unacaught exp Stack Cause",exception.getCause().getMessage());
        LogWriter.writeUncaughtLog("Unacaught exp Stack Trace",stackTrace.toString());

        Intent mStartActivity = new Intent(myContext, SplashScreen.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(myContext, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) myContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, mPendingIntent);
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}

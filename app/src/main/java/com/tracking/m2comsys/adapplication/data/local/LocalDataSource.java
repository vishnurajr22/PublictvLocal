package com.tracking.m2comsys.adapplication.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessaging;
import com.tracking.m2comsys.adapplication.data.DataSource;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by tikson on 7/5/18.
 */

public class LocalDataSource implements DataSource {
    private static LocalDataSource INSTANCE = null;
    public static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static String preference = "Settings";
    public static String INTIALIZED = "intialized";
    public static String VERSION_CODE = "version_code";

    private LocalDataSource(Context context) {
    }

    public static LocalDataSource getInstance(Context context) {

        if (INSTANCE == null) INSTANCE = new LocalDataSource(context);
        sharedPreferences = context.getSharedPreferences(preference, MODE_PRIVATE);
        return INSTANCE;
    }

    @Override
    public void checkVersionIntialization(CallbackResult callback) {
        if (!sharedPreferences.contains(INTIALIZED)) {
            callback.onSuccess(false);
        } else
            callback.onSuccess(true);
    }

    @Override
    public void intializeVersion() {
        FirebaseMessaging.getInstance().subscribeToTopic("PTV");
        editor = sharedPreferences.edit();
        editor.putBoolean(INTIALIZED, true);
        editor.putInt(VERSION_CODE, 0);
        editor.commit();

    }

    @Override
    public void checkVersionChange(int version, CallbackResult callback) {
        int ver = sharedPreferences.getInt(VERSION_CODE, 0);
        if (ver == version)
            callback.onSuccess(false);
        else
            callback.onSuccess(true);
    }
}

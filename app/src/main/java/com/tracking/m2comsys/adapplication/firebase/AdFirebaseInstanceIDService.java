package com.tracking.m2comsys.adapplication.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tracking.m2comsys.adapplication.utils.LogWriter;


public class AdFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "AdFbIIDSvc";
    private String ad = "cbTbRTL6tWs:APA91bEoesa9SY6wHl-WftNT3FoKdBG1jPIf1DVhhW41pvmIjkQf63d3ppPnLKqM9lxuEVoIt7rldPE-ER__nS01Ptp9c86BvtV-jblPtkdzSwAZZznQgtSGYnQHcEOjlUAs88PIZlNC";

    @Override
    public void onTokenRefresh() {
        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);
        }catch (Exception exp){
            LogWriter.writeLogException("Firebase tockn",exp);
        }
    }
}

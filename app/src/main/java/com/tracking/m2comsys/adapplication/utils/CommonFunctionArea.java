package com.tracking.m2comsys.adapplication.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tracking.m2comsys.adapplication.Activity.SplashScreen;

import static android.content.Context.MODE_PRIVATE;
import static com.tracking.m2comsys.adapplication.Activity.MainActivity.ModeOfPlay;
import static com.tracking.m2comsys.adapplication.Activity.MainActivity.getYoutubeVideoId;
import static com.tracking.m2comsys.adapplication.Activity.MainActivity.stopPosition;
import static com.tracking.m2comsys.adapplication.utils.CommonDataArea.chnlDrtn;
import static com.tracking.m2comsys.adapplication.utils.Utils.preference;

public class CommonFunctionArea {
    public static int channelDuration = 1;
    public static int statusmode = 0;
    public static int NumberOfAds = 1;
    public static String contactDetails;
    String videoPositionPref = "VideoPosition";
    String videoSeekPositionPref = "VideoSeekPosition";
    int videoPosition;
    Context context;
    public static SharedPreferences sharedPreferences;
    public static String mode = "Mode";
    public int noAds;
    String adNo = "NoOfAdvertisements";
    String chnlId = "ChannelId";
    public static String VIDEO_ID = "MyrkXKJfNV4";
    public String id;

    public CommonFunctionArea(Context context) {
        this.context = context;

    }

    public void setMode(String dataStr) {
        ModeOfPlay = (dataStr.substring(10, dataStr.length())).trim();
        LogWriter.writeLogUSB("USBDataProc", "mode changed to " + ModeOfPlay);
        modeSelected(ModeOfPlay);

    }

    public void restartFromBackground() {
      /*  PackageManager pm=context.getPackageManager();
        Intent launchIntent=pm.getLaunchIntentForPackage("com.tracking.m2comsys.adapplication");
        PendingIntent contentintent=PendingIntent.getActivity(context,0,launchIntent,0);*/
        Intent mStartActivity = new Intent(context, SplashScreen.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, mPendingIntent);
        Process.killProcess(Process.myPid());
        System.exit(0);

    }

    public void restartApplication() {
        System.out.println("call restart = ");

//        long min = getMinitues(timeStamp);
//        if (min < 5) {
//            if (uuidFound) {

        Intent mIntent = new Intent(context, SplashScreen.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
        Utils.sendLogToServer("Firebase Command->restart calling from firebase", context);
        Log.d("firebasemessages", "calling restart from firebase");
//            }
//        }
    }

    public void modeSelected(String mod) {
        if (!mod.equals("")) {
            sharedPreferences = context.getSharedPreferences(preference, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(mode, mod);
            editor.commit();
            getSharedPreference();
            //  toastShow("Mode Changed To " + ModeOfPlay);
            if (ModeOfPlay.equals("YouTube") || ModeOfPlay.equalsIgnoreCase("sdcard")) {
                Log.d("signalstrack", "sending AD from modeSelected " + ModeOfPlay);
                Utils.SendToUsb("AD\r\n");

            } else if (ModeOfPlay.equals("Channel")) {
                Log.d("signalstrack", "sending TV from modeSelected " + ModeOfPlay);
                Utils.SendToUsb("TV\r\n");
            } else if (ModeOfPlay.equalsIgnoreCase("YoutubeLive")) {
                Log.d("signalstrack", "Mode>" + ModeOfPlay);
                Utils.SendToUsb("AD\r\n");

            }

            //  Toast.makeText(getBaseContext(), "Mode Changed To " + ModeOfPlay, Toast.LENGTH_SHORT).show();


//            }
        }
//        restartApplication();
    }

    public void toastShow(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        ViewGroup viewGroup = (ViewGroup) toast.getView();
        TextView textView = (TextView) viewGroup.getChildAt(0);
        textView.setTextSize(40);
        toast.show();
    }

    public void getUUID() {
        LogWriter.writeLog("USBDataProc", "device uuid " + CommonDataArea.uuid);
        for (int i = 0; i < 10; i++) {
            //   toastShow("Device UUID " + CommonDataArea.uuid);
//

        }
        String uuid = CommonDataArea.uuid;
        //    usbCon.SendDataToDevice(uuid + "\r\n");

    }

    public void getSharedPreference() {
        channelDuration = sharedPreferences.getInt(chnlDrtn, 1);
        if (!sharedPreferences.getString(chnlId, "MyrkXKJfNV4").equals("")) {
            VIDEO_ID = sharedPreferences.getString(chnlId, "MyrkXKJfNV4");
        }
        statusmode = sharedPreferences.getInt(CommonDataArea.playAutomationMode, CommonDataArea.PLAY_MODE_AUTO);
        NumberOfAds = sharedPreferences.getInt(adNo, 1);
        ModeOfPlay = sharedPreferences.getString(mode, "Channel");
        contactDetails = sharedPreferences.getString(CommonDataArea.contactDetails, "Contact to your local franchise");
        Log.d("flow", "getSharedPreference");
        Log.d("sharedpref", "StopPosition" + String.valueOf(stopPosition));
        Log.d("sharedpref", "video Postion" + String.valueOf(videoPosition));
        videoPosition = sharedPreferences.getInt(videoPositionPref, 0);
        stopPosition = sharedPreferences.getInt(videoSeekPositionPref, 0);
        CommonDataArea.wifiName = sharedPreferences.getString("WifiName", "");
    }

    public void setNumOfAdvts(int noad) {
        //  noAds = Integer.parseInt((dataStr.substring(17, dataStr.length())).trim());
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(adNo, noad);
        editor.commit();
        LogWriter.writeLogUSB("USBDataProc", "Number of advts changed to " + String.valueOf(noAds));

        //   toastShow("Number of advts changed to " + String.valueOf(noAds));
        // Toast.makeText(getBaseContext(), "Number of advts changed to " + String.valueOf(noAds), Toast.LENGTH_LONG).show();
    }

    public void saveSharedprefmodeautomanual(String title) {
        if (title.equalsIgnoreCase("MANUALMODE")) {
            sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(CommonDataArea.playAutomationMode, CommonDataArea.PLAY_MODE_MANUAL);
            editor.commit();
            Log.d("channeldurationtime", "manual mode chanelduration :" + sharedPreferences.getInt(CommonDataArea.chnlDrtn, 1));
        } else if (title.equalsIgnoreCase("AUTOMODE")) {
            sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(CommonDataArea.playAutomationMode, CommonDataArea.PLAY_MODE_MANUAL);
            editor.commit();
            Log.d("channeldurationtime", "auto mode chanelduration :" + sharedPreferences.getInt(CommonDataArea.chnlDrtn, 1));
        }
    }

    public void setVideoURL(String dataStr) {
        //  String url = (dataStr.substring(15, dataStr.length())).trim();
        id = getYoutubeVideoId(dataStr);
        //  toastShow("video URL changed to" + id);
        //Toast.makeText(getBaseContext(), "video URL changed to" + id, Toast.LENGTH_LONG).show();

        if (id.equals("")) {
            sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(chnlId, id);
            VIDEO_ID = dataStr;
            editor.apply();


        } else {

            sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(chnlId, id);
            editor.apply();
            VIDEO_ID = id;

            LogWriter.writeLog("USBDataProc", "VideoURL changed to " + id);


        }
        restartApplication();
    }

    public void setChannelDuration(String data) {

        // channelDuration = Integer.parseInt((data.substring(15, data.length())).trim());
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(chnlDrtn, Integer.parseInt(data));
        editor.putInt(CommonDataArea.TempChannelduration, channelDuration);

        LogWriter.writeLog("USBDataProc", "Channel duration set to" + channelDuration);
        editor.commit();
        //    mToastToShow = Toast.makeText(getBaseContext(), "Channel duration set to" + channelDuration, Toast.LENGTH_LONG);
        //toastShow("Channel duration set to" + channelDuration);


    }
}

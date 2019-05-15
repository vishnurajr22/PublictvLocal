package com.tracking.m2comsys.adapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.tracking.m2comsys.adapplication.data.model.SettingsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static com.tracking.m2comsys.adapplication.utils.CommonDataArea.adNo;
import static com.tracking.m2comsys.adapplication.utils.CommonDataArea.chnlDrtn;
import static com.tracking.m2comsys.adapplication.utils.CommonDataArea.serverURL;

/**
 * Created by admin on 09/05/2017.
 */

public class Utils {
    public static SharedPreferences sharedPreferences;
    public static String preference = "Settings";
    public static String version;
    public static String VERSION_CODE = "version_code";
    public static SharedPreferences.Editor editor;


    public static boolean wifiCheck() {
        WifiManager wifiManager = (WifiManager) CommonDataArea.mainActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        android.net.wifi.SupplicantState s = wifiManager.getConnectionInfo().getSupplicantState();
        NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(s);
        if (state != NetworkInfo.DetailedState.CONNECTED) {
            LogWriter.writeLogWifi("UtilsWifiCheck", "Wifi not connected");
            return false;
        } else {
            LogWriter.writeLogWifi("UtilsWifiCheck", "Wifi connected");
            return true;
        }
    }

    public static boolean wifiReconnect() {
        WifiManager wifiManager = (WifiManager) CommonDataArea.mainActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        android.net.wifi.SupplicantState s = wifiManager.getConnectionInfo().getSupplicantState();
        NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(s);
        if (state != NetworkInfo.DetailedState.CONNECTED) {
            wifiManager.reconnect();
            return false;
        } else {
            return true;
        }
    }

    public static boolean internetCheck() {
        if (CommonDataArea.mainActivity == null) return false;
        ConnectivityManager cm =
                (ConnectivityManager) CommonDataArea.mainActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else
            return false;

    }

    public static long getAvailableSpace() {
        StatFs stat = new StatFs(CommonDataArea.rootFile.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return (long) (availableBlocks * blockSize);
    }

    public static void updateLastContactedTime() {
        //http://publictvads.in/WebServiceLive/UpdateLastContactedTime.php?did=3ab7afe13f5996e8

        String commenPath = "publictvads.in/WebServiceLiveTest/UpdateLastContactedTime.php?did=" + CommonDataArea.uuid;
        try {
            URL uRl = new URL(commenPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) uRl.openConnection();
            httpURLConnection.connect();
            Log.d("connect", "Connected");
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }
        } catch (Exception exp) {

        }
    }

    static List<String> list =
            Collections.synchronizedList(new ArrayList<String>());
    static Thread sendToServer = null;
    static Object syncObj = new Object();

    public static void sendLogToServer(final String log, final Context context) {
        //sendLog(CommonDataArea.uuid, Uri.encode(log));
        list.add(log);
        if ((sendToServer != null) && (sendToServer.isAlive())) {
            synchronized (syncObj) {
                syncObj.notify();
            }
            return;
        }
        System.out.println("Call Log = ");
        //http://publictvads.in/WebServiceLive/UpdateLastContactedTime.php?did=3ab7afe13f5996e8
        sendToServer = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean callRepeatForSettingsRead = false;
                while (true) {
                    if (list.isEmpty()) {
                        synchronized (syncObj) {
                            try {
                                syncObj.wait();
                            } catch (Exception exp) {

                            }
                        }
                    }
                    String logStr = list.remove(0);

                    do {
                        callRepeatForSettingsRead = SendToServerFunc(logStr, context);
                        if (callRepeatForSettingsRead) {
                            logStr = "SettingsRead->Reading settings";
                        }
                    } while (callRepeatForSettingsRead);
                }
            }
        });
        sendToServer.start();

    }

    public static synchronized boolean SendToServerFunc(final String log, final Context context) {
        System.out.println("Call Thread = ");
        try {
            if (!internetCheck()) {
                System.out.println("Call internet = ");
                return false;
            }
            int currentVers = 0;
            sharedPreferences = context.getSharedPreferences(preference, MODE_PRIVATE);
            currentVers = sharedPreferences.getInt(VERSION_CODE, 0);
            String commenPath = "http://publictvads.in/WebServiceLiveTest/UpdateLog.php?did=" + CommonDataArea.uuid + "&info=" + Uri.encode(log) + "&ver=" + currentVers;
            URL uRl = new URL(commenPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) uRl.openConnection();
            httpURLConnection.connect();
            Log.d("connect", "Connected");
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("sb.toString() = " + sb.toString());
            CommonDataArea.lastLogSendTime = System.currentTimeMillis();
            if (versionCodeChange(Integer.parseInt(sb.toString()), context)) {
                return true;
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("SendToServer", exp);
        }
        return false;
    }

    public static void updatePlayHist(final String allocId, final String videoId, final String playedTime) {
        //http://publictvads.in/WebServicelive/UpdatePlayHist.php?devID=47041a2851f28dd7&videoID=46&allocID=412&dateTime='09-27-2017 12:30:23'
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String commenPath = "http://publictvads.in/WebServicelive/UpdatePlayHist.php?devId=" + CommonDataArea.uuid + "&videoID=" + videoId + "&allocID=" + allocId + "&dateTime='" + Uri.encode(playedTime) + "'";
                    URL uRl = new URL(commenPath);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) uRl.openConnection();
                    httpURLConnection.connect();
                    Log.d("connect", "Connected");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        // Append server response in string
                        sb.append(line + "\n");
                    }
                } catch (Exception exp) {

                }
            }
        }).start();

    }

    public static void SendToUsb(String cmd) {
        if (CommonDataArea.usbType == CommonDataArea.UsbType.FTDI) {
            if (CommonDataArea.usbCon != null) {
                CommonDataArea.usbCon.SendEmergencyMessage(cmd);
            }
        } else if (CommonDataArea.usbType == CommonDataArea.UsbType.ESP) {
            if (CommonDataArea.usbConEsp != null)
                CommonDataArea.usbConEsp.SendEmergencyMessage(cmd);
        }

    }

    public void checkVersion(String versionCode) {
        // sharedPreferences =getSharedPreferences(preference, MODE_PRIVATE);
    }

    public static boolean versionCodeChange(final int version, final Context context) {
        System.out.println("version = " + version);
        int currentVers = 0;
        sharedPreferences = context.getSharedPreferences(preference, MODE_PRIVATE);
        currentVers = sharedPreferences.getInt(VERSION_CODE, 0);
        System.out.println("currentVers = " + currentVers);
        System.out.println("version = " + version);

        if (currentVers != version) {
            System.out.println("version Not equal = ");
            LogWriter.writeLogPlay("Version Change ", "Version change :CurVersion->" + currentVers + " New Version->" + version);
            LogWriter.writeLog("Settings Version Check", "Version change :CurVersion->" + currentVers + " New Version->" + version);

            System.out.println("Call Thread = ");
            try {
                if (!internetCheck()) {
                    System.out.println("Call internet = ");
                    return false;
                }
                String commenPath = "http://publictvads.in/WebServiceLiveTest/DownloadSettings.php?did=" + CommonDataArea.uuid + "&ver=" + version;
                URL uRl = new URL(commenPath);
                HttpURLConnection httpURLConnection = (HttpURLConnection) uRl.openConnection();
                httpURLConnection.connect();
                Log.d("connect", "Connected");
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                parseSettings(sb.toString(), version, context);
                return true;
            } catch (Exception exp) {
                return false;
            }

        }
        return false;
    }

    static void parseSettings(String setting, int version, Context con) {
        LogWriter.writeLog("Settings Collected from server", setting);
        try {
            InputStream stream = new ByteArrayInputStream(setting.getBytes(StandardCharsets.UTF_8));
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(stream);
            SettingsModel settings = gson.fromJson(reader, SettingsModel.class);
            if (settings.getCommand().getCommandid().equalsIgnoreCase("deviceslots")) {
                LogWriter.writeLog("Settings Device Slot", setting);
                saveNormal(settings.getCommand().getNormal().get(0), con);
                savePeak1(settings.getCommand().getPeak1().get(0), con);
                savePeak2(settings.getCommand().getPeak2().get(0), con);
                savePeak3(settings.getCommand().getPeak3().get(0), con);
                editor = sharedPreferences.edit();
                editor.putInt(VERSION_CODE, version);
                editor.commit();

                String contactDetails = settings.getCommand().getContactDetails();
                editor = sharedPreferences.edit();
                editor.putString(CommonDataArea.contactDetails, contactDetails);
                editor.putInt(VERSION_CODE, version);
                editor.commit();

            } else if (settings.getCommand().getCommandid().equalsIgnoreCase("credentials")) {
                LogWriter.writeLog("Settings Credentials", setting);
                String email = settings.getCommand().getEmailid();
                String password = settings.getCommand().getPassword();
                editor = sharedPreferences.edit();
                editor.putString(CommonDataArea.EmailId, email);
                editor.putString(CommonDataArea.PasswordEmail, password);
                editor.putInt(VERSION_CODE, version);
                editor.commit();
            } else if (settings.getCommand().getCommandid().equalsIgnoreCase("textmesg")) {
                LogWriter.writeLog("Settings Text Mesg", setting);
                saveNormal(settings.getCommand().getNormal().get(0), con);
                savePeak1(settings.getCommand().getPeak1().get(0), con);
                savePeak2(settings.getCommand().getPeak2().get(0), con);
                savePeak3(settings.getCommand().getPeak3().get(0), con);
                editor = sharedPreferences.edit();
                editor.putInt(VERSION_CODE, version);
                editor.commit();

                String contactDetails = settings.getCommand().getContactDetails();
                editor = sharedPreferences.edit();
                editor.putString(CommonDataArea.contactDetails, contactDetails);
                editor.putInt(VERSION_CODE, version);
                editor.commit();

            } else {
                LogWriter.writeLog("Settings Default", setting);
                editor = sharedPreferences.edit();
                editor.putInt(VERSION_CODE, version);
                editor.commit();
                FirebaseMessaging.getInstance().subscribeToTopic(settings.getCommand().getDevTopic());
            }


        } catch (Exception e) {
            LogWriter.writeLogException("Parse settings", e);
            sendLogToServer("Malformed Settings String-Version->" + version, con);
        }
    }

    public static void saveNormal(SettingsModel.Normal normal, Context context) {
        String peak = "Normal";

        String[] times = normal.getDL_Normal().split("to");
        times[0] = times[0].trim();
        times[1] = times[1].trim();

        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(peak + "_StartTime", times[0]);
        editor.putString(peak + "_EndTime", times[1]);
        editor.putString(peak + "_PlayTime", "");
        editor.putString(peak + "_IdleTime", "");
        if ((times.length > 0) && (times[0].length() > 5) && (times[1].length() > 5)) {
            editor.putBoolean(peak + "_Valid", true);
        } else editor.putBoolean(peak + "_Valid", false);
        editor.commit();

    }

    public static void savePeak1(SettingsModel.Peak1 peak1, Context context) {
        String peak = "Peak1";

        String[] times = peak1.getDL_Peak1().split("to");
        times[0] = times[0].trim();
        times[1] = times[1].trim();

        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(peak + "_StartTime", times[0]);
        editor.putString(peak + "_EndTime", times[1]);
        editor.putString(peak + "_PlayTime", peak1.getDL_PlayTime1());
        editor.putString(peak + "_IdleTime", peak1.getDL_Idle1());
        if ((times.length > 0) && (times[0].length() > 5) && (times[1].length() > 5)) {
            editor.putBoolean(peak + "_Valid", true);
        } else editor.putBoolean(peak + "_Valid", false);
        editor.commit();
    }


    public static void savePeak2(SettingsModel.Peak2 peak2, Context context) {
        String peak = "Peak2";
        String[] times = peak2.getDL_Peak2().split("to");
        times[0] = times[0].trim();
        times[1] = times[1].trim();

        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(peak + "_StartTime", times[0]);
        editor.putString(peak + "_EndTime", times[1]);
        editor.putString(peak + "_PlayTime", peak2.getDL_PlayTime2());
        editor.putString(peak + "_IdleTime", peak2.getDL_Idle2());
        if ((times.length > 0) && (times[0].length() > 5) && (times[1].length() > 5)) {
            editor.putBoolean(peak + "_Valid", true);
        } else editor.putBoolean(peak + "_Valid", false);
        editor.commit();
    }

    public static void savePeak3(SettingsModel.Peak3 peak3, Context context) {
        String peak = "Peak3";
        String[] times = peak3.getDL_Peak3().split("to");
        times[0] = times[0].trim();
        times[1] = times[1].trim();

        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(peak + "_StartTime", times[0]);
        editor.putString(peak + "_EndTime", times[1]);
        editor.putString(peak + "_PlayTime", peak3.getDL_PlayTime3());
        editor.putString(peak + "_IdleTime", peak3.getDL_Idle3());
        if ((times.length > 0) && (times[0].length() > 5) && (times[1].length() > 5)) {
            editor.putBoolean(peak + "_Valid", true);
        } else editor.putBoolean(peak + "_Valid", false);
        editor.commit();
    }

    /*rtctime check*/
    public static long getAdjustedTimeMills(Context con) {
        try {
            long currentNormal = 0;
            long adjustedDevTimeMills = 0;

            if (CommonDataArea.timeStatus == CommonDataArea.TIMESTATUS_USEDEVICE_TIME) {
                //adjustedDevTimeMills = System.currentTimeMillis();
                adjustedDevTimeMills = new Date().getTime();
            }
            if (CommonDataArea.timeStatus == CommonDataArea.TIMESTATUS_USESERVER_TIME) {

                // adjustedDevTimeMills = Utils.getSavedServerTime(con);
                adjustedDevTimeMills = CommonDataArea.GoogleTime.getTime();
            }
            if (CommonDataArea.timeStatus == CommonDataArea.TIMESTATUS_USEADJUST_ZONE) {
                TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
                long offset1 = tz.getRawOffset();
                Calendar c = Calendar.getInstance();
                TimeZone tz2 = c.getTimeZone();
                long offset2 = tz2.getRawOffset();
                if ((offset2 > 0) && (offset2 < offset1)) offset1 = (offset2 - offset1);
                if ((offset2 > 0) && (offset2 > offset1)) offset1 = (offset1 - offset2);
                if (offset2 < 0) offset1 = (offset1 - offset2);
                adjustedDevTimeMills = c.getTimeInMillis();
                adjustedDevTimeMills += offset1;
            }
            if (CommonDataArea.timeStatus == CommonDataArea.TIMESTATUS_RTC_TIME) {
                if (CommonDataArea.rtcTime != null)
                    adjustedDevTimeMills = CommonDataArea.rtcTime.getTime();
                else {
                    CommonDataArea.timeStatus = CommonDataArea.TIMESTATUS_DEF_PLAY;
                    adjustedDevTimeMills = 0;
                }
            }
            if (CommonDataArea.timeStatus == CommonDataArea.TIMESTATUS_DEF_PLAY) {
                adjustedDevTimeMills = 0;
            }
            return adjustedDevTimeMills;
        } catch (Exception exp) {
            return System.currentTimeMillis();
        }
    }

    static String[] slotNames = {"Normal", "Peak1", "Peak2", "Peak3"};

    public static void checkSlot(Context con) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");

            boolean inSlot = false;
            String startDateStr = "";
            String endDateStr = "";
            String playTime;
            String idleTime;
            int validConfCount = 0;
            boolean validCon;

            for (int i = 0; i < 4; ++i) {
                sharedPreferences = con.getSharedPreferences(preference, MODE_PRIVATE);
                validCon = sharedPreferences.getBoolean(slotNames[i] + "_Valid", false);
                startDateStr = sharedPreferences.getString(slotNames[i] + "_StartTime", "09:30 AM");
                endDateStr = sharedPreferences.getString(slotNames[i] + "_EndTime", "09:30 PM");
                playTime = sharedPreferences.getString(slotNames[i] + "_PlayTime", "1");
                idleTime = sharedPreferences.getString(slotNames[i] + "_IdleTime", "1");
                if (!validCon) continue;

                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = sdf.parse(startDateStr);
                    endDate = sdf.parse(endDateStr);
                } catch (ParseException exp) {
                    LogWriter.writeLogPlay("CheckSlot", exp.getMessage());
                    LogWriter.writeLogException("CheckSlot", exp);
                    continue;
                }
                //generating current time in minutes from mid night Hour*60+minutes

                long serTimeMills;
                serTimeMills = getAdjustedTimeMills(con);
                Calendar dateNow = Calendar.getInstance();
                dateNow.setTimeInMillis(serTimeMills);
                int curDateVal = dateNow.get(Calendar.HOUR_OF_DAY) * 60 + dateNow.get(Calendar.MINUTE);
                String curHourMin = dateNow.get(Calendar.HOUR_OF_DAY) + ":" + dateNow.get(Calendar.MINUTE);

                //Generating slot start time in minutes from midnight
                Calendar startTime = Calendar.getInstance();
                startTime.setTime(startDate);
                int startTimeVal = startTime.get(Calendar.HOUR_OF_DAY) * 60 + startTime.get(Calendar.MINUTE);

                //Generating slot end time in minutes from midnight
                Calendar endTime = Calendar.getInstance();
                endTime.setTime(endDate);
                int endTimeVal = endTime.get(Calendar.HOUR_OF_DAY) * 60 + endTime.get(Calendar.MINUTE);

                LogWriter.writeLogPlay("CheckingSlot", "SlotName->" + slotNames[i] + " StartTime->" + startDateStr + "End Time->" + endDateStr + "Adjusted Cur Time->" + curHourMin);
                if ((curDateVal > startTimeVal) && (curDateVal < endTimeVal)) {
                    if (slotNames[i].contains("Normal")) {
                        sharedPreferences = con.getSharedPreferences(preference, MODE_PRIVATE);
                        CommonDataArea.slotName = slotNames[i];
                        CommonDataArea.chanDuration = sharedPreferences.getInt(CommonDataArea.chnlDrtn, 3);
                        CommonDataArea.numAddsToPlay = sharedPreferences.getInt(CommonDataArea.adNo, 1);
                        CommonDataArea.slotStartTime = startDateStr;
                        CommonDataArea.slotEndTime = endDateStr;
                        inSlot = true;
                        ++validConfCount;
                        LogWriter.writeLogPlay("CheckingSlot", "Normal slot valid");
                    } else {
                        try {
                            int numTimes = 1;
                            int idleTimeMin = 0;
                            numTimes = Integer.parseInt(playTime);
                            idleTimeMin = Integer.parseInt(idleTime);
                            CommonDataArea.slotName = slotNames[i];
                            CommonDataArea.chanDuration = idleTimeMin;
                            CommonDataArea.numAddsToPlay = numTimes;
                            CommonDataArea.slotStartTime = startDateStr;
                            CommonDataArea.slotEndTime = endDateStr;
                            inSlot = true;
                            validConfCount++;
                            LogWriter.writeLogPlay("CheckingSlot", CommonDataArea.slotName + " Valid");
                        } catch (Exception exp) {
                            LogWriter.writeLogPlay("CheckingSlot", "Due to exception setting default");
                            sharedPreferences = con.getSharedPreferences(preference, MODE_PRIVATE);
                            CommonDataArea.slotName = "NA";
                            CommonDataArea.chanDuration = sharedPreferences.getInt(CommonDataArea.chnlDrtn, 3);
                            CommonDataArea.numAddsToPlay = sharedPreferences.getInt(CommonDataArea.adNo, 1);
                        }
                    }
                } else {
                    validConfCount++;
                    LogWriter.writeLogPlay("CheckingSlot", slotNames[i] + " Not Valid");
                }
            }
            //BMJO used a separate variable to avoid frequent reset which can upset other process
            if (validConfCount > 0) {
                if (inSlot) {
                    LogWriter.writeLogPlay("checkSlot", "Play settings Slot name " + CommonDataArea.slotName + " Duration->" + CommonDataArea.chanDuration + " Numadvts ->" + CommonDataArea.numAddsToPlay + " Start Time:" + startDateStr + "  End Time:" + endDateStr + "Slot->" + inSlot);
                    Utils.sendLogToServer("Play Slot-> Slot name " + CommonDataArea.slotName + " Channel Duration :" + CommonDataArea.numAddsToPlay + "Numadvts :" + CommonDataArea.numAddsToPlay + " Start Time:" + startDateStr + "  End Time:" + endDateStr, con);

                    CommonDataArea.isInSlot = true;
                    if ((CommonDataArea.numAddsToPlay == 0) && (CommonDataArea.chanDuration == 0)) {
                        CommonDataArea.isContinious = true;
                    } else CommonDataArea.isContinious = false;
                    LogWriter.writeLogPlay("checkSlot", "In Slot");
                } else {
                    CommonDataArea.slotName = "NotInSlot";
                    CommonDataArea.slotStartTime = "NA";
                    CommonDataArea.slotEndTime = "NA";
                    CommonDataArea.isInSlot = false;
                    LogWriter.writeLogPlay("checkSlot", "Play settings Slot name " + CommonDataArea.slotName + " Duration->" + CommonDataArea.chanDuration + " Numadvts ->" + CommonDataArea.numAddsToPlay + " Start Time:" + startDateStr + "  End Time:" + endDateStr + "Slot->" + inSlot);
                    Utils.sendLogToServer("Play Slot-> Slot name " + CommonDataArea.slotName + " Channel Duration :" + CommonDataArea.numAddsToPlay + "Numadvts :" + CommonDataArea.numAddsToPlay + " Start Time:" + startDateStr + "  End Time:" + endDateStr, con);

                    LogWriter.writeLogPlay("CheckSlot", "Not In Slot");
                }
            } else { //No Valid configurations so use default
                CommonDataArea.slotName = "No Valid Config";
                CommonDataArea.isInSlot = true;
                CommonDataArea.slotStartTime = "NA";
                CommonDataArea.slotEndTime = "NA";
                CommonDataArea.chanDuration = sharedPreferences.getInt(CommonDataArea.chnlDrtn, 3);
                CommonDataArea.numAddsToPlay = sharedPreferences.getInt(CommonDataArea.adNo, 1);
                Utils.sendLogToServer("Play Slot-> Slot name " + CommonDataArea.slotName + " Channel Duration :" + CommonDataArea.numAddsToPlay + "Numadvts :" + CommonDataArea.numAddsToPlay + " Start Time:" + startDateStr + "  End Time:" + endDateStr, con);

            }
        } catch (Exception exp) {
            LogWriter.writeLogException("Peek slots Read Exception", exp);
        }
    }


    static long maxTimeDiffTH;//=(long)(5184000000l);// (60l/*DAYS*/*24l/*HOURS*/*60l/*MINUTE*/*60l/*SEC*/*1000l/*MILLS*/);//(5184000000l);
    static long lesThan24H;//= (long)(86400000);//(1l/*DAYS*/*24l/*HOURS*/*60l/*MINUTE*/*60l/*SEC*/*1000l/*MILLS*/);
    static long lesThan2H;//=(long)(7200000l);//( 2l/*HOURS*/*60l/*MINUTE*/*60l/*SEC*/*1000l/*MILLS*/);

    public static void initConstants() {
        maxTimeDiffTH = (long) (5184000000l);
        lesThan24H = (long) (86400000 + 60 * 60 * 1000);
        lesThan2H = (long) (7200000l);
    }

    public static void getServerTime(final Context context) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {


//                    try {
//                        HttpClient httpclient = new DefaultHttpClient();
//                        HttpResponse response = httpclient.execute(new HttpGet("https://google.com/"));
//                        StatusLine statusLine = response.getStatusLine();
//                        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
//                            String dateStr = response.getFirstHeader("Date").getValue();
//                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
//                            Date date2 = (Date) formatter.parse(dateStr);
//
//                            System.out.println(date2);
//
//                        } else {
//
//                            response.getEntity().getContent().close();
//                            throw new IOException(statusLine.getReasonPhrase());
//                        }
//                    } catch (ClientProtocolException e) {
//                        Log.d("Response", e.getMessage());
//                    } catch (IOException e) {
//                        Log.d("Response", e.getMessage());
//                    } catch (Exception e) {
//                        Log.d("Response", e.getMessage());
//                    }


                try {
                    String urlServer = serverURL + "getTime.php?did=" + CommonDataArea.uuid;
                    URL uRl = new URL(urlServer);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) uRl.openConnection();
                    httpURLConnection.connect();
                    Log.d("connect", "Connected");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    String result = sb.toString();
                    JSONArray jsonObject = new JSONArray(result);
                    String dataJson = jsonObject.getString(0);//getString("TimeZone");
                    JSONObject jsobObj = new JSONObject(dataJson);
                    String timeZone = jsobObj.getString("TimeZone");
                    String timeDiff = jsobObj.getString("GMT_TIME_DIFF");
                    String serverTime = jsobObj.getString("ServerTime");
                    CommonDataArea.serverTimeString = serverTime;
                    Calendar devTimeCal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date servTime = sdf.parse(serverTime);
                    //If server time less than 2018 set to Zero
                    if (servTime.getTime() < CommonDataArea.startOf2019Mills) {
                        saveServerTime(context, 0);
                        return;
                    }
                    if (servTime.getTime() > getSavedServerTime(context)) {
                        saveServerTime(context, servTime.getTime());
                    }
                    LogWriter.writeLogRTC("ServerTime", "Check RTC present");
                    if (CommonDataArea.rtcPresent) {
                        LogWriter.writeLogRTC("ServerTime", "Check RTC null");
                        if (CommonDataArea.rtcTime != null) {
                            LogWriter.writeLogRTC("ServerTime", "Server time request");
                            long millsDiff = servTime.getTime() - CommonDataArea.rtcTime.getTime();
                            if (millsDiff < 0) millsDiff = (0 - millsDiff); //converting to positive
                            LogWriter.writeLogRTC("ServerTime", "Server time diff with RTC->" + millsDiff);
                            if (millsDiff > CommonDataArea.twoHoursMills) {
                                String pattern = "dd/MM/yyyy hh:mm:ss aa";
                                SimpleDateFormat formter = new SimpleDateFormat(pattern);
                                String timeStr = formter.format(servTime);
                                LogWriter.writeLogRTC("ServerTime", "Setting Server time to RTC ->" + timeStr);
                                String cmd = "DTW=" + timeStr + "\r\n";
                                Thread.sleep(4000);
                                Utils.SendToUsb(cmd);
                                LogWriter.writeLogRTC("ServerTime", "Setting Server time to RTC ->" + cmd);
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        LogWriter.writeLogRTC("ServerTime", "Requesting Time from RTC to verify");
                                        Utils.SendToUsb("DTR\r\n");
                                    }
                                }, 10000);
                            }
                        }
                    }
                    String devTimeStr = sdf.format(devTimeCal.getTime());
                    LogWriter.writeLogPlayAlloc("getServerTime", "Serv Time-> " + serverTime + " Device Time->" + devTimeStr);

                } catch (Exception exp) {
                    saveServerTime(context, 0);
                    LogWriter.writeLogException("getServerTime", exp);

                }

            }
        }).start();

    }


    //This function checks the device time & Server Time
    //and make decision how to play
    public static void adjustPlayMethod(Context context) {
        try {
            if (internetCheck()) {
                GoogleTime time = new GoogleTime();
                final Thread t1 = new Thread(time);
                t1.start();
                t1.join();

            }


            if (CommonDataArea.rtcTime != null) {
                Log.d("timeused>>", "rtc_time");
                CommonDataArea.timeStatus = CommonDataArea.TIMESTATUS_RTC_TIME;
                LogWriter.writeLogPlayMethod("Playmethod", "TIMESTATUS_RTC_TIME");
                CommonDataArea.curSysStatus_Playmethod = "TIMESTATUS_RTC_TIME";
            } else if (CommonDataArea.GoogleTime != null) {
                Log.d("timeused>>", "google_time");
                CommonDataArea.timeStatus = CommonDataArea.TIMESTATUS_USESERVER_TIME;
                LogWriter.writeLogPlayMethod("Playmethod", "TIMESTATUS_USESERVER_TIME");
                CommonDataArea.curSysStatus_Playmethod = "TIMESTATUS_USESERVER_TIME";
            } else {
                Log.d("timeused>>", "System_time");
                CommonDataArea.timeStatus = CommonDataArea.TIMESTATUS_USEDEVICE_TIME;
                LogWriter.writeLogPlayMethod("Playmethod", "TIMESTATUS_USEDEVICE_TIME");
                CommonDataArea.curSysStatus_Playmethod = "TIMESTATUS_USEDEVICE_TIME";
            }


//            long serverTime = getSavedServerTime(context);
//
//            if (Calendar.getInstance().getTimeInMillis() > CommonDataArea.startOf2019Mills) {
//                Calendar cal = Calendar.getInstance();
//                String tz = cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);
//                if (tz.contains("+05:30")) {
//                    CommonDataArea.timeStatus = CommonDataArea.TIMESTATUS_USEDEVICE_TIME;
//                    LogWriter.writeLogPlayMethod("Playmethod", "TIMESTATUS_USEDEVICE_TIME");
//                    CommonDataArea.curSysStatus_Playmethod = "TIMESTATUS_USEDEVICE_TIME";
//                } else {
//                    CommonDataArea.timeStatus = CommonDataArea.TIMESTATUS_USEADJUST_ZONE;
//                    LogWriter.writeLogPlayMethod("Playmethod", "TIMESTATUS_USEADJUST_ZONE");
//                    CommonDataArea.curSysStatus_Playmethod = "TIMESTATUS_USEADJUST_ZONE";
//                }
//
//                return;
//            } else if (CommonDataArea.rtcTime.getTime() > CommonDataArea.startOf2019Mills) {//failed to get time from server
//                CommonDataArea.timeStatus = CommonDataArea.TIMESTATUS_RTC_TIME;
//                LogWriter.writeLogPlayMethod("Playmethod", "TIMESTATUS_RTC_TIME");
//                CommonDataArea.curSysStatus_Playmethod = "TIMESTATUS_RTC_TIME";
//            }else if (serverTime > CommonDataArea.startOf2019Mills) {
//                CommonDataArea.timeStatus = CommonDataArea.TIMESTATUS_USESERVER_TIME;
//                LogWriter.writeLogPlayMethod("Playmethod", "TIMESTATUS_USESERVER_TIME");
//                CommonDataArea.curSysStatus_Playmethod = "TIMESTATUS_USESERVER_TIME";
//            }  else {
//                LogWriter.writeLogPlayMethod("Playmethod", "TIMESTATUS_DEF_PLAY");
//                CommonDataArea.timeStatus = CommonDataArea.TIMESTATUS_DEF_PLAY;
//                CommonDataArea.curSysStatus_Playmethod = "TIMESTATUS_DEF_PLAY";
//            }
        } catch (Exception exp) {
            LogWriter.writeLogPlayMethod("Playmethod", "TIMESTATUS_DEF_PLAY");
            CommonDataArea.timeStatus = CommonDataArea.TIMESTATUS_DEF_PLAY;
            CommonDataArea.curSysStatus_Playmethod = "TIMESTATUS_DEF_PLAY";
        }
    }

    public static boolean serverTimeFeched = false;

    public static void saveServerTime(Context context, long mills) {
        //  noAds = Integer.parseInt((dataStr.substring(17, dataStr.length())).trim());
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(CommonDataArea.storedTime, mills);
        editor.commit();
        serverTimeFeched = true; //server time saved after start
    }

    public static long getSavedServerTime(Context context) {
        //  noAds = Integer.parseInt((dataStr.substring(17, dataStr.length())).trim());
        //  if (!serverTimeFeched) return 0; //server time not fetched after starting
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(CommonDataArea.storedTime, CommonDataArea.startOf2019Mills);
    }

    public static void saveDeviceTime(Context context, long mills) {
        //  noAds = Integer.parseInt((dataStr.substring(17, dataStr.length())).trim());
        if (mills > CommonDataArea.startOf2019Mills) {
            sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(CommonDataArea.storedDeviceTime, mills);
            editor.commit();
        }
    }

    public static long getSavedDeviceTime(Context context) {
        //  noAds = Integer.parseInt((dataStr.substring(17, dataStr.length())).trim());
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(CommonDataArea.storedDeviceTime, CommonDataArea.startOf2019Mills);
    }

    public static void setNumOfAdvts(Context context, int noad) {
        //  noAds = Integer.parseInt((dataStr.substring(17, dataStr.length())).trim());
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(adNo, noad);
        editor.commit();
    }

    public static void setChannelDuration(Context context, int data) {

        // channelDuration = Integer.parseInt((data.substring(15, data.length())).trim());
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(chnlDrtn, data);
        editor.commit();
        //    mToastToShow = Toast.makeText(getBaseContext(), "Channel duration set to" + channelDuration, Toast.LENGTH_LONG);
        //toastShow("Channel duration set to" + channelDuration);


    }
    public static void setChannelDurationw(Context context, int data) {

        // channelDuration = Integer.parseInt((data.substring(15, data.length())).trim());
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(chnlDrtn, data);
        editor.commit();
}}
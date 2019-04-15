package com.tracking.m2comsys.adapplication.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tracking.m2comsys.adapplication.BroadcastReceivers.BootService;
import com.tracking.m2comsys.adapplication.Database.AdHistory;
import com.tracking.m2comsys.adapplication.Database.DataBaseHelper;
import com.tracking.m2comsys.adapplication.R;
import com.tracking.m2comsys.adapplication.extras.ReceiverAlarm;
import com.tracking.m2comsys.adapplication.usb.UsbControllerESP;
import com.tracking.m2comsys.adapplication.usb.UsbControllerFTDI;
import com.tracking.m2comsys.adapplication.utils.AdapterGridView;
import com.tracking.m2comsys.adapplication.utils.AlarmReceiver;
import com.tracking.m2comsys.adapplication.utils.CommonDataArea;
import com.tracking.m2comsys.adapplication.utils.CommonFunctionArea;
import com.tracking.m2comsys.adapplication.utils.DownloadAdvts;
import com.tracking.m2comsys.adapplication.utils.LogWriter;
import com.tracking.m2comsys.adapplication.utils.MyExceptionHandler;
import com.tracking.m2comsys.adapplication.utils.UpdatePlayCount;
import com.tracking.m2comsys.adapplication.utils.Utils;
import com.tracking.m2comsys.adapplication.utils.VideoDetailsModel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.GONE;
import static com.tracking.m2comsys.adapplication.utils.CommonDataArea.Video_Name;
import static com.tracking.m2comsys.adapplication.utils.CommonDataArea.Video_NoOfTimesPlayed;
import static com.tracking.m2comsys.adapplication.utils.CommonDataArea.adNo;
import static com.tracking.m2comsys.adapplication.utils.CommonDataArea.chnlDrtn;


public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    public static final String API_KEY = "AIzaSyDEHhrSmQOMJuAjTEw2ztnH2ovv4XQtVOU";
    public static int channelDuration = 1;
    public static String VIDEO_ID = "MyrkXKJfNV4";
    public static int NumberOfAds = 1;
    public static YouTubePlayer youTubePlayerC;
    public static String mode = "Mode";
    public static String contactDetails;
    public static String ModeOfPlay = "Channel";
    public static int statusmode = 0;
    GridView gridViewRecords;
    public ArrayList<HashMap<String, String>> vlist;
    public static int i = 1;
    public static int impressions = 0;
    public static int videoSize = 0;
    ArrayList<VideoDetailsModel> stringArrayList;
    public String id;
    private static Cursor cursor;
    public int duration;
    public int noAds;
    public Uri uri;
    public static int stopPosition;
    MediaController mediaController;
    public Cursor getDetailsCursor;
    public DataBaseHelper dataBaseHelper;
    LinearLayout linr;
    public static int _id;
    public static SharedPreferences sharedPreferences;
    String preference = "Settings";
    WebView ywebview;
    String videoPositionPref = "VideoPosition";
    String videoSeekPositionPref = "VideoSeekPosition";
    String chnlId = "ChannelId";
    private static final String Video_PlayedPercent = "PercentPlayed";
    private static final String Impressions = "Impressions";
    YouTubePlayerView youTubePlayerView;
    Toolbar toolbar;
    ImageView imageLogo;
    TextView txt_contact;
    FrameLayout sdCardLayout;
    VideoView sdCardVideoView;
    AdHistory adHistory;
    LinearLayout mainBlockLayout;
    RelativeLayout playHistoryLayout;
    LinearLayout youtubeLayout;
    File entertainmentVideoFile;
    int videoPosition;
    public static String path = "";
    //   UsbControllerESP usbCon = null;
    WifiManager mainWifiObj;
    TextView wifiText;
    NetworkInfo wifiCheck;
    boolean isWifiConnected;
    TextView statusInfo;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    CommonFunctionArea commonFunctionArea;
    long lastLoged = 0;
    MyWebClient myWebClient;
    public static int stopPosition1;
    //UsbControllerFTDI usbCon = null;
    //UsbControllerFTDI usbCon;
    // UsbControllerESP usbConEsp;
    SharedPreferences sharedpreferences;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Toast mToastToShow;
    private YouTubePlayer.PlaybackEventListener playBackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {
        }

        @Override
        public void onSeekTo(int i) {
        }
    };
    private YouTubePlayer.PlayerStateChangeListener playerStateChangelistener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override

        public void onLoading() {
        }

        @Override
        public void onLoaded(String s) {
            if (youTubePlayerC != null) {
                youTubePlayerC.play();
            }
        }

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onVideoStarted() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
        }
    };

    public static String getYoutubeVideoId(String youtubeUrl) {
        String video_id = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http")) {

            String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d("MainActivityLOG", "Starting");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            /*Emailcheck*/
            ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


            if (mWifi.isConnected()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this,"previous mails",Toast.LENGTH_LONG).show();
                        Intent background = new Intent(MainActivity.this, BootService.class);

                        MainActivity.this.startService(background);
                    }
                }, 20 * 1000);
            }


            CommonDataArea.uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            CommonDataArea.mainActivity = this;
            Utils.initConstants();
            Log.d("MainActivityLOG", "Rendered");
            if (ModeOfPlay.equals("YouTube") || ModeOfPlay.equals("sdcard")) {
                Log.d("signalstrack", "sending AD from oncreate " + ModeOfPlay);
                Utils.SendToUsb("AD\r\n");

            }

            CommonDataArea.setAlredyStarted(200);
            statusInfo = (TextView) findViewById(R.id.statusinfobox);

            isWifiConnected = wifiConnected();
            wifiText = (TextView) findViewById(R.id.wifitext);
            statusInfo = (TextView) findViewById(R.id.statusinfobox);
            CommonDataArea.appContext = getApplicationContext();
            //   Utils.sendLogToServer("MainActivity->Starting", MainActivity.this);
            LogWriter.writeLogActivity("Main Activity", "starting");

            Log.d("MainActivityLOG", "USB Init");
            commonFunctionArea = new CommonFunctionArea(MainActivity.this);
            LogWriter.writeLogUSB("USB", "starting");
            initUSB();
            LogWriter.writeLogUSB("USB", "Usb Init Completed");

            mainBlockLayout = (LinearLayout) findViewById(R.id.main_block);
            playHistoryLayout = (RelativeLayout) findViewById(R.id.play_history_block);
            youtubeLayout = (LinearLayout) findViewById(R.id.linearyoutubelayout);
            Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, MainActivity.class));
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setActionBar(toolbar);
            mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            LogWriter.writeLog("Main Activity", "starting");
            sharedPreferences = getSharedPreferences(preference, MODE_PRIVATE);
            ReadSettings();
            ReadTimings();

            LogWriter.writeLog("Main Activity", "Checking and creating folder advts");
            String path = Environment.getExternalStorageDirectory().toString() + "/advts";
            CommonDataArea.rootFile = new File(path);
            CommonDataArea.rootFile.mkdir();
            entertainmentVideoFile = new File(Environment.getExternalStorageDirectory(), "EntertaimentVideo");

            if (!entertainmentVideoFile.exists()) {
                entertainmentVideoFile.mkdirs();
            }
            gridViewRecords = (GridView) findViewById(R.id.gridview);
            LogWriter.writeLogActivity("Main Activity", "Starting DB");
            Log.d("MainActivityLOG", "Starting DB Activity");

            dataBaseHelper = new DataBaseHelper(this, "AdvertisementDetails", null, CommonDataArea.DATABASE_VERSION);
            CommonDataArea.dataBaseHelper = dataBaseHelper;
            //allocateDailyImp();

            adHistory = new AdHistory(this, "AdvertisementDetails", null, 1);
            sdCardLayout = (FrameLayout) findViewById(R.id.sdCardLayout);
            sdCardVideoView = (VideoView) findViewById(R.id.sdCardVideoView);
            imageLogo = (ImageView) findViewById(R.id.img_logo);
            txt_contact = (TextView) findViewById(R.id.txt_contact_details);
            contatdetails();
            youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeplayerview);
            ywebview = (WebView) findViewById(R.id.webview);
            linr = (LinearLayout) findViewById(R.id.linear);


            if (CommonDataArea.uuid != null) {
                LogWriter.writeLog("MainActivity:Create", "Starting download thread");
                initVideodownLoadTask();
                Log.d("onCreate", "called async");
            }

            getAppVersion();
            broadcastReceiverAlarm();

            initRTC();

            ConnectivityManager connManager2 = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi2 = connManager2.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi2.isConnected()) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        createAlarm();
                    }
                }, 80 * 1000);
            }

        } catch (Exception exp) {
            LogWriter.writeLogException("Create", exp);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {

        Log.d("MainActivityLOG", "New Activity Intent");
        if (CommonDataArea.bootEvent) {
            CommonDataArea.bootEvent = false;

            return;
        }
        Intent mStartActivity = new Intent(this, SplashScreen.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, mPendingIntent);
        finish();
        System.exit(0);
    }

    void initRTC() {
        if (CommonDataArea.rtcAdj == null) {
            LogWriter.writeLogRTC("Init", "Requesting RTC Time");
            Utils.SendToUsb("DTR\r\n");
            CommonDataArea.rtcAdj = new Timer();
            CommonDataArea.rtcAdj.schedule(new TimerTask() {
                @Override
                public void run() {
                    LogWriter.writeLogRTC("RTCUpdateTimer", "TimeUpdating");
                    if (CommonDataArea.rtcTimeValid) {
                        boolean logIt = false;
                        long millis = CommonDataArea.rtcTime.getTime();
                        if ((millis - lastLoged) > 15000) {
                            lastLoged = millis;
                            logIt = true;
                        }
                        if (logIt)
                            LogWriter.writeLogRTC("RTCUpdateTimer", "BeforeUpdate-RTC Timer->" + millis);
                        millis += 15000;
                        CommonDataArea.rtcTime.setTime(millis);
                        String pattern = "dd/MM/yyyy hh:mm:ss aa";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        String disp = simpleDateFormat.format(CommonDataArea.rtcTime);
                        if (logIt)
                            LogWriter.writeLogRTC("RTCUpdateTimer", "AfterUpdate->RTC Timer->" + millis + " TimeStr->" + disp);
                        if (CommonDataArea.lastRTCReadTime < (millis - CommonDataArea.twoHoursMills)) {
                            LogWriter.writeLogRTC("RTCUpdateTimer", "Sync Time with RTC after 2 Hours");
                            Utils.SendToUsb("DTR\r\n");
                        }
                    } else {
                        CommonDataArea.rtcRetry++;
                        if (CommonDataArea.rtcRetry % 2 == 0) {
                            LogWriter.writeLogRTC("RTCUpdateTimer", "Try request again");
                            Utils.SendToUsb("DTR\r\n");
                        }
                    }
                }
            }, 15000, 15000);
        }
    }

    void updateStatusBox(final String info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusInfo.setText(info);
            }
        });
    }

    class wifiCheck implements Runnable {
        @Override
        public void run() {
            try {
                CommonDataArea.wifiCheckRunning = true;
                NetworkInfo wifiCheck;
                LogWriter.writeLogWifi("Wifi check", "collecting wifi service interface");
                ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                LogWriter.writeLogWifi("Wifi check", "Checking wifi connected");

                Thread.sleep(30000);

                if (wifiCheck.isConnected()) {
                    LogWriter.writeLogWifi("First Wifi check", "wifi connected");
                    CommonDataArea.wifiCheckRunning = false;
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();
                    sharedPreferences = getSharedPreferences(preference, MODE_PRIVATE);
                    String netSSID = sharedPreferences.getString("NetSSID", "#NA#");
                    if (ssid.contains(netSSID))
                        return;
                }

                do {

                    LogWriter.writeLogWifi("Wifi check", "wifi connecting");
                    sharedPreferences = getSharedPreferences(preference, MODE_PRIVATE);
                    String netSSID = sharedPreferences.getString("NetSSID", "#NA#");
                    String netPass = sharedPreferences.getString("NetPASS", "#NA#");
                    if (netSSID.contains("#NA#")) {
                        LogWriter.writeLogWifi("wifichk", "Wifi Not Configured");
                        break;
                    }
                    updateStatusBox("wifi connecting->" + netSSID);
                    LogWriter.writeLogWifi("Wifi check", "wifi connecting->" + netSSID + "--" + netPass);
                    finallyConnect(netPass, netSSID);
                    Thread.sleep(30000);
                    LogWriter.writeLogWifi("Wifi check", "Connect call done");
                    wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                } while (!wifiCheck.isConnected());

                if (wifiCheck.isConnected()) {
                    LogWriter.writeLogWifi("Wifi check", "wifi connected");
                    updateStatusBox("wifi connected");
                    CommonDataArea.wifiCheckRunning = false;
                    return;
                }
                CommonDataArea.wifiCheckRunning = false;
                LogWriter.writeLogWifi("Wifi check", "Exiting thread");
            } catch (Exception exp) {
                LogWriter.writeLogWifi("Wifi check", "wifi check exception->" + exp.getMessage());
                LogWriter.writeLogException("Wifi", exp);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  sharedPreferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("permission", true);
                    editor.commit();
                  /*  if (permFail && permFailUSB) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SplashScreen.this, "Allow All permissions", Toast.LENGTH_SHORT).show();
                    }*/
                }
            }
        }
    }

    void getAppVersion() {
        try {
            CommonDataArea.startedAt = System.currentTimeMillis();
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int versioId = pInfo.versionCode;
            Utils.sendLogToServer("Application starting Version->" + version + " Code->" + versioId, MainActivity.this);
        } catch (PackageManager.NameNotFoundException e) {
            Utils.sendLogToServer("Application starting Version-> unknown", MainActivity.this);
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogWriter.writeLogActivity("Main Activity", "On Resume");
        try {
            LogWriter.writeLogPlay("Resume", "Starting Play Thread");
            initAdvtPLayThread();
            sysInfo();
            contatdetails();
        } catch (Exception exp) {
            LogWriter.writeLogException("Resume", exp);
        }

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        try {
            youTubePlayerC = youTubePlayer;
            youTubePlayer.setPlayerStateChangeListener(playerStateChangelistener);
            youTubePlayer.setPlaybackEventListener(playBackEventListener);
            if (!b) {
                youTubePlayer.loadVideo(VIDEO_ID);
                youTubePlayer.play();
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("onInitializationSuccess", exp);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        //   Toast.makeText(MainActivity.this, "failed to onitialize", Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getMenu() {
        toolbar.inflateMenu(R.menu.settings);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.channelDuration_popup:
                        channelDurationPopup();
                        break;
                    case R.id.channelId_popup:
                        channelIdPopup();
                        break;
                    case R.id.noOfAds_popup:
                        noOfAds();
                        break;
                    case R.id.mode:
                        modePopUp();
                        break;
                    case R.id.uuid:
                        showUUID();
                        break;
                    case R.id.remAds:
                        dataBaseHelper.deleteAllVids();
                        break;
                    case R.id.settings:
                        settings();
                        break;
                    default:
                        return onOptionsItemSelected(item);
                }

                return true;
            }
        });
    }

    Thread videoThread;
    boolean toggleFlag = false;
    static boolean lastTimePlayed = true;

    public void initAdvtPLayThread() {
        Log.d("onCreate", "inside advt Play thread");
        //Channel :-Use AV Input/HDMI as entertainment Filler source
        if (ModeOfPlay.equals("Channel")) {
            imageLogo.setVisibility(View.VISIBLE);
            txt_contact.setVisibility(View.VISIBLE);
            playHistoryLayout.setVisibility(GONE);
            sysInfo();
            youTubePlayerView.setVisibility((GONE));
            try {
                if (videoThread != null) {
                    videoThread.join((channelDuration * 2 * 30) * 1000);
                }
            } catch (Exception exp) {
                LogWriter.writeLogException("initAdvtPLayThread", exp);
            }

            videoThread = new Thread() {
                public void run() {
                    Log.d("onCreate", "inside initialize channel thread");
                    try {
                        Utils.getServerTime(getApplicationContext());

                        //Start loop to wait for advt time starts
                        boolean IsVideoAvailableToPlay = false;
                        do {
                            if (!Utils.wifiCheck()) {
                                ++CommonDataArea.wifiNotConnetedCount;
                                if ((CommonDataArea.wifiNotConnetedCount > 3) && (!CommonDataArea.wifiCheckRunning)) {
                                    CommonDataArea.wifiNotConnetedCount = 0;
                                    Thread wifiCheckThread = new Thread((new wifiCheck()));
                                    wifiCheckThread.start();
                                }
                            } else {
                                CommonDataArea.wifiNotConnetedCount = 0;
                            }
                            updateStatusHourly();
                            long timestart = System.currentTimeMillis();
                            LogWriter.writeLogPlay("InitAdvtPLayThread", "Start Waiting for channel duaration->" + channelDuration);
                            Utils.sendLogToServer("Play->Idle time starts :" + String.valueOf(channelDuration), MainActivity.this);
                            //If Chanel Duration set to zero play advts continiosly
                            channelDuration = CommonDataArea.chanDuration;
                            // If duration set zero but no videos to play wait
                            if ((channelDuration == 0) && (!lastTimePlayed))
                                sleep((channelDuration * 60) * 1000);
                            // If duration > zero wait for channelDuration in minutes
                            if (channelDuration > 0) {
                                //get server time while sleeping
                                Utils.getServerTime(getApplicationContext());
                                sleep((channelDuration * 60) * 1000);
                            }
                            //Check and allocate daily impressions
                            allocateDailyImp();
                            updateSysInfo();
                            //Check any time slot active , otherwise continue wait
                            if (CommonDataArea.timeStatus != CommonDataArea.TIMESTATUS_DEF_PLAY) {
                                Utils.checkSlot(getBaseContext());
                                if (!CommonDataArea.isInSlot) {
                                    LogWriter.writeLogPlay("initAdvtPLayThread", "Not In slot");
                                    Utils.sendLogToServer("Play->Not in any play slot", MainActivity.this);
                                    cursor = null;
                                    Utils.SendToUsb("TV\r\n");//Not in clot continue in TV
                                    continue;
                                }
                                ;
                            }
                            LogWriter.writeLogPlay("initAdvtPLayThread", "In Slot->" + CommonDataArea.slotName);

                            long timestart2 = System.currentTimeMillis();
                            Utils.sendLogToServer("Play->Idle time completed :" + (timestart2 - timestart) / 1000, MainActivity.this);
                            LogWriter.writeLogPlay("initAdvtPLayThread", "Waiting completed->" + (timestart2 - timestart) / 1000);
                            cursor = dataBaseHelper.getVideoToPlay();
                            lastTimePlayed = false;
                            //If any video ready play exit loop and start advt play activity
                            if ((cursor != null) && (cursor.getCount() > 0)) {
                                LogWriter.writeLogActivity("Main Activity", "Advt found -Start playing");
                                lastTimePlayed = true;
                                IsVideoAvailableToPlay = true;
                                cursor.close();
                                break;
                            } else {
                                //No Video to play continue wait
                                IsVideoAvailableToPlay = false;
                                LogWriter.writeLogPlay("Main Activity", "No Advt to play");
                                Utils.sendLogToServer("Play->No Video to play", MainActivity.this);
                                Log.d("cycle", "onResume initPlayThread inside channel thread");
                                Utils.SendToUsb("TV\r\n");//No Video continue in TV
                                if (cursor != null) cursor.close();
                            }
                        }
                        while (!IsVideoAvailableToPlay);
                        //If any reason came out from loop with out video to play
                        if (!IsVideoAvailableToPlay) {
                            LogWriter.writeLogPlay("initAdvtPLayThread", "No Advt to play");
                            Log.d("cycle", "onResume initPlayThread inside youtube thread");
                            initAdvtPLayThread();
                            return;
                        }
                        LogWriter.writeLogPlay("Main Activity", "Starting advt play activity");
                        ReadSettings();
                        //Check status of play is set to auto mode, If manual mode is set continue waiting
                        if (statusmode == CommonDataArea.PLAY_MODE_AUTO) {
                            Intent intent = new Intent(MainActivity.this, Advertisement.class);
                            startActivity(intent);

                            Log.d("signalstrack", "sending AD from initAdvtPLayThread " + ModeOfPlay);
                            LogWriter.writeLogPlay("initAdvtPLayThread", "No Advt to play");
                            Utils.SendToUsb("AD\r\n");

                        } else {
                            initAdvtPLayThread();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LogWriter.writeLogException("initAdvtPLayThread", e);
                    }
                }
            };
            videoThread.start();
        } else if (ModeOfPlay.equals("YouTube")) {

            imageLogo.setVisibility(GONE);
            ywebview.setVisibility(View.GONE);
            txt_contact.setVisibility(GONE);
            playHistoryLayout.setVisibility(GONE);
            mainBlockLayout.setVisibility(GONE);
            sdCardLayout.setVisibility(GONE);
            youTubePlayerView.setVisibility((View.VISIBLE));
            youTubePlayerView.initialize(API_KEY, this);


            try {
                if (videoThread != null) {
                    videoThread.join((channelDuration * 2 * 30) * 1000);
                }

            } catch (Exception exp) {
                LogWriter.writeLogException("initAdvtPLayThread", exp);
            }

            videoThread = new Thread() {
                public void run() {

                    try {
                        Log.d("cycle", "thread youtube");
                        Log.d("onCreate", "inside initialize youtube thread");

                        sleep((channelDuration * 60) * 1000);

                        allocateDailyImp();

                        Log.d("ActivityTrans", "Main to Ad");
                        cursor = dataBaseHelper.getVideoToPlay();
                        if ((cursor == null) || (cursor.getCount() == 0)) {
                            LogWriter.writeLog("initAdvtPLayThread", "No Advt to play");
                            Log.d("cycle", "onResume initPlayThread inside youtube thread");

                            initAdvtPLayThread();
                            return;
                        }
                        ReadSettings();
                        if (statusmode == CommonDataArea.PLAY_MODE_AUTO) {
                            Intent intent = new Intent(MainActivity.this, Advertisement.class);
                            startActivity(intent);
                            finish();
                            Log.d("signalstrack", "sending AD from initAdvtPLayThread " + ModeOfPlay);
                            Utils.SendToUsb("AD\r\n");
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            };
            videoThread.start();
        } else if (ModeOfPlay.equals("sdcard")) {

            mainBlockLayout.setVisibility(GONE);
            txt_contact.setVisibility(GONE);
            youTubePlayerView.setVisibility(GONE);
            playHistoryLayout.setVisibility(GONE);
            sdCardLayout.setVisibility(View.VISIBLE);
            ywebview.setVisibility(View.GONE);
            playFromSDCard();


        } else if (ModeOfPlay.equalsIgnoreCase("YouTubeLive")) {
            toolbar.setVisibility(View.VISIBLE);
            youTubePlayerView.setVisibility(GONE);
            imageLogo.setVisibility(GONE);
            txt_contact.setVisibility(GONE);
            playHistoryLayout.setVisibility(GONE);
            mainBlockLayout.setVisibility(GONE);
            sdCardLayout.setVisibility(GONE);
            ywebview.setSystemUiVisibility(View.VISIBLE);

            ywebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    emulateClick(view);


                }
            });

            ywebview.setWebViewClient(new Browser());
            myWebClient = new MyWebClient();

            ywebview.setWebChromeClient(myWebClient);
            ywebview.getSettings().setJavaScriptEnabled(true);
            ywebview.getSettings().setLoadWithOverviewMode(true);

//            ywebview.getSettings().getUseWideViewPort();

            ywebview.getSettings().setUseWideViewPort(true);
            ywebview.getSettings().setMediaPlaybackRequiresUserGesture(false);
            String data = "UCD2Uk96ldedeYs4yQv8yd-A";
            ywebview.loadData("", "mp4/video", "utf-8");


            ywebview.loadUrl("https://www.youtube.com/channel/UCD2Uk96ldedeYs4yQv8yd-A/live");


            final Handler ha = new Handler();
            ha.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //call function
                    ywebview.loadUrl("https://www.youtube.com/channel/UCD2Uk96ldedeYs4yQv8yd-A/live");
                    ha.postDelayed(this, 30 * 60 * 1000);
                }
            }, 30);

        }

    }

    static long lastUpdatedTime = System.currentTimeMillis();

    void updateStatusHourly() {
        if ((System.currentTimeMillis() - lastUpdatedTime) > 15 * 60 * 1000) {
            lastUpdatedTime = System.currentTimeMillis();
            UpdatePlayCount playCount = new UpdatePlayCount(CommonDataArea.CALLING_FROM_PLAYTHREAD);
            playCount.execute();
        }
    }

    public void ReadSettings() {

        if (!sharedPreferences.getString(chnlId, "MyrkXKJfNV4").equals("")) {
            VIDEO_ID = sharedPreferences.getString(chnlId, "MyrkXKJfNV4");
        }
        statusmode = sharedPreferences.getInt(CommonDataArea.playAutomationMode, CommonDataArea.PLAY_MODE_AUTO);
        ModeOfPlay = sharedPreferences.getString(mode, "Channel");
        contactDetails = sharedPreferences.getString(CommonDataArea.contactDetails, "Contact to your local franchise");
        Log.d("flow", "getSharedPreference");
        Log.d("sharedpref", "StopPosition" + String.valueOf(stopPosition));
        Log.d("sharedpref", "video Postion" + String.valueOf(videoPosition));
        videoPosition = sharedPreferences.getInt(videoPositionPref, 0);
        stopPosition = sharedPreferences.getInt(videoSeekPositionPref, 0);
        CommonDataArea.wifiName = sharedPreferences.getString("WifiName", "");
    }

    public void ReadTimings() {
        channelDuration = sharedPreferences.getInt(CommonDataArea.chnlDrtn, 1);
        CommonDataArea.chanDuration = channelDuration;
        CommonDataArea.numAddsToPlay = sharedPreferences.getInt(CommonDataArea.adNo, 1);
    }


    public void settings() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void channelIdPopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.popup);
        edt.setText(VIDEO_ID);
        edt.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogBuilder.setTitle("VIDEO URL");
        dialogBuilder.setMessage("Enter URL");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (edt.getText().toString() != null) {
                    String url = edt.getText().toString();
                    id = getYoutubeVideoId(url);
                    if (id.equals("")) {
                        sharedPreferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(chnlId, url);
                        VIDEO_ID = url;
                        editor.commit();
                        ReadSettings();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        //    initializeYouTube();

                    } else {
                        sharedPreferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(chnlId, id);
                        editor.commit();
                        VIDEO_ID = id;
                        ReadSettings();
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void channelDurationPopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.popup);
        edt.setText(String.valueOf(channelDuration), TextView.BufferType.EDITABLE);
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogBuilder.setTitle("Channel Duration ");
        dialogBuilder.setMessage("Enter Channel ChannelDuration in minitues");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (edt.getText().toString() != null) {
                    duration = Integer.parseInt(edt.getText().toString());
                    channelDuration = duration;
                    sharedPreferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(chnlDrtn, duration);
                    editor.commit();
                    // Toast.makeText(MainActivity.this, "channel will play " + duration + " minutes", Toast.LENGTH_SHORT).show();

                }

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void noOfAds() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.popup);
        edt.setText(String.valueOf(NumberOfAds), TextView.BufferType.EDITABLE);
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogBuilder.setTitle("No Of Advertisements");
        dialogBuilder.setMessage("Enter No Of Advertisements");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (edt.getText().toString() != null) {
                    noAds = Integer.parseInt(edt.getText().toString());
                    sharedPreferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(adNo, noAds);
                    editor.commit();
                    //  Toast.makeText(MainActivity.this, noAds + " advertisement will play", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void showUUID() {
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.popup);
        edt.setText(CommonDataArea.uuid, TextView.BufferType.NORMAL);
        edt.setFreezesText(true);
        dialogBuilder.setTitle("Device UUID");
        dialogBuilder.setMessage("UUID");
        dialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    public void modePopUp() {

        final android.app.AlertDialog.Builder Dialog = new android.app.AlertDialog.Builder(this);
        Dialog.setTitle("Select Option");

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = li.inflate(R.layout.popupspinner, null);
        Dialog.setView(dialogView);
        Spinner spinnercategory = (Spinner) dialogView
                .findViewById(R.id.popSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercategory.setAdapter(adapter);
        spinnercategory.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext));
        spinnercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                ModeOfPlay = parent.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        Dialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        new CommonFunctionArea(MainActivity.this).modeSelected(ModeOfPlay);
                        startActivity(getIntent());
                    }
                });

        Dialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        Dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void initVideodownLoadTask() {
        try {
            TimerTask doAsynchronousTask;
            final Handler handler = new Handler();
            Timer timer = new Timer();
            doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {

                                if (CommonDataArea.downloadFiles != null) {
                                    if (CommonDataArea.downloadFiles.isAlive()) {
                                        LogWriter.writeLogDownload("handler", "Video download thread alredy running");
                                        return;
                                    }
                                } else {
                                    LogWriter.writeLogDownload("handler", "Starting video donwload thread");
                                    CommonDataArea.downloadFiles = new Thread(new DownloadAdvts(MainActivity.this));
                                    CommonDataArea.downloadFiles.start();
                                }

                            } catch (Exception e) {
                                LogWriter.writeLogException("initVideodownLoadTask", e);
                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 0, 5 * 60 * 1000);//execute in every 10000 ms*/
        } catch (Exception exp) {
            LogWriter.writeLogException("initVideodownLoadTask", exp);
        }
    }

    public void showPlayHistory() {
        imageLogo.setVisibility(GONE);
        txt_contact.setVisibility(GONE);
        playHistoryLayout.setVisibility(View.VISIBLE);
        try {
            stringArrayList = new ArrayList<VideoDetailsModel>();

            AdapterGridView adapterGridView;

            String videoName = "";
            int totalImpression = 0;
            int nofPlayed = 0;
            int percentage = 0;
            int errorCount;
            String status;
            Cursor cursor = null;
            cursor = CommonDataArea.dataBaseHelper.getAllFileList();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        VideoDetailsModel model = new VideoDetailsModel();
                        videoName = cursor.getString(cursor.getColumnIndex(Video_Name));
                        totalImpression = cursor.getInt(cursor.getColumnIndex(Impressions));
                        nofPlayed = cursor.getInt(cursor.getColumnIndex(Video_NoOfTimesPlayed));
                        percentage = cursor.getInt(cursor.getColumnIndex(Video_PlayedPercent));
                        status = cursor.getString(cursor.getColumnIndex(CommonDataArea.VideoStatus));
                        errorCount = cursor.getInt(cursor.getColumnIndex(CommonDataArea.PlayErrorCount));
                        model.setVideo_Name(videoName);
                        model.setImpressions(totalImpression);
                        model.setVideo_NoOfTimesPlayed(nofPlayed);
                        model.setPercentage(percentage);
                        model.setStatus(status);
                        model.setErrorCount(errorCount);

                        stringArrayList.add(model);

                    } while (cursor.moveToNext());
                    adapterGridView = new AdapterGridView(this, stringArrayList);
                    gridViewRecords.setAdapter(adapterGridView);
                }
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("GridView", exp);
        }
    }

    public void broadcastReceiverAlarm() {
        try {

            PendingIntent pendingIntent;
            Intent alarmintent = new Intent(getApplicationContext(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1555, alarmintent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            int interval = 10000;
            Calendar cal = Calendar.getInstance();
            alarmManager.setInexactRepeating(AlarmManager.RTC, cal.getTimeInMillis() + 5000, interval, pendingIntent);

        } catch (Exception exp) {
            LogWriter.writeLogException("AlarmRecv", exp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.channelDuration_popup:
                channelDurationPopup();
                break;
            case R.id.channelId_popup:
                channelIdPopup();
                break;
            case R.id.noOfAds_popup:
                noOfAds();
                break;
            case R.id.mode:
                modePopUp();
                break;
            case R.id.uuid:
                showUUID();
                break;
            case R.id.remAds:
                dataBaseHelper.deleteAllVids();
                break;
            case R.id.settings:
                settings();
                break;
            case R.id.showPLayHistory:
                if (!ModeOfPlay.equals("YouTube")) {
                    showPlayHistory();
                }
                break;
            case R.id.exportPlayHist:
                /*dataBaseHelper.logPlayHistory();*/
                showDatePicker();
                break;

            case R.id.SetMailId:
                setMailid();
            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    private void setMailid() {
        sharedpreferences = getSharedPreferences("mypreference",
                Context.MODE_PRIVATE);
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.emailid, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.emailid);

        dialogBuilder.setTitle("Set Email Id For Reports");
        dialogBuilder.setMessage("Email");
        dialogBuilder.setPositiveButton("set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Email", edt.getText().toString());

                editor.commit();
            }
        });
        dialogBuilder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        android.app.AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void showDatePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        CharSequence strDate = null;
                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, monthOfYear, year);
                        long dtDob = chosenDate.toMillis(true);

                        strDate = DateFormat.format("dd-MM-yyyy", dtDob);

                        String Date = strDate.toString();
                        //Toast.makeText(MainActivity.this, Date, Toast.LENGTH_LONG).show();
                        dataBaseHelper.getallHistory(Date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void playFromSDCard() {

        int videoFileSize = entertainmentVideoFile.listFiles().length;
        if (videoPosition < videoFileSize) {
            playVideo();
        } else {
            videoPosition = 0;
            sharedpreferences = getSharedPreferences("mypreference",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("vidposition", 0);
            editor.commit();
            playVideo();

        }

    }

    public void playVideo() {

        final File playVideoFile;

        Log.d("length", entertainmentVideoFile.listFiles().toString());
        File file[] = entertainmentVideoFile.listFiles();
        Log.d("length", file[videoPosition].getName());
        sharedpreferences = getSharedPreferences("mypreference",
                Context.MODE_PRIVATE);
        videoPosition = sharedpreferences.getInt("vidposition", 0);
        path = Environment.getExternalStorageDirectory().toString() + "/EntertaimentVideo" + "/" + file[videoPosition].getName();

        playVideoFile = new File(path);
        if (playVideoFile.exists()) {
            Log.d("signalstrack", "sending AD from playVideo " + ModeOfPlay);
            Utils.SendToUsb("AD\r\n");
            uri = Uri.parse(path);
            mediaController = new MediaController(this);
            sdCardVideoView.setMediaController(mediaController);
            sdCardVideoView.setVideoURI(uri);
            //            if (stopPosition != 0) {
            //                sdCardVideoView.seekTo(stopPosition);
            //    sdCardVideoView.start();
            // }

            sdCardVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public MediaPlayer mep;

                @Override
                public void onPrepared(final MediaPlayer mp) {
                    this.mep = mp;
                    sdCardVideoView.bringToFront();
                    sdCardVideoView.setFocusable(true);
                    sdCardVideoView.start();


                    try {
                        if (videoThread != null) {
                            videoThread.join();
                        }

                    } catch (Exception exp) {
                        LogWriter.writeLogException("initAdvtPLayThread", exp);
                    }

                    videoThread = new Thread() {
                        public void run() {

                            try {
                                Log.d("cycle", "thread youtube");
                                Log.d("onCreate", "inside initialize youtube thread");

                                sleep((channelDuration * 60) * 1000);
                                stopPosition1 = mep.getCurrentPosition();

                                //sdCardVideoView.pause();
                                sharedpreferences = getSharedPreferences("mypreference",
                                        Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putInt("time", stopPosition1);
                                editor.commit();
                                allocateDailyImp();

                                Log.d("ActivityTrans", "Main to Ad");
                                cursor = dataBaseHelper.getVideoToPlay();
                                if ((cursor == null) || (cursor.getCount() == 0)) {
                                    LogWriter.writeLog("initAdvtPLayThread", "No Advt to play");
                                    Log.d("cycle", "onResume initPlayThread inside youtube thread");

                                    initAdvtPLayThread();
                                    return;
                                }
                                ReadSettings();
                                if (statusmode == CommonDataArea.PLAY_MODE_AUTO) {
                                    Intent intent = new Intent(MainActivity.this, Advertisement.class);
                                    startActivity(intent);
                                    //   finish();
                                    Log.d("signalstrack", "sending AD from initAdvtPLayThread " + ModeOfPlay);
                                    Utils.SendToUsb("AD\r\n");
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    };
                    videoThread.start();
                    sharedpreferences = getSharedPreferences("mypreference",
                            Context.MODE_PRIVATE);


                    sdCardVideoView.seekTo(sharedpreferences.getInt("time", 0));
                    sdCardVideoView.start();

                }
            });

            sdCardVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {


                    sharedpreferences = getSharedPreferences("mypreference",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt("time", 0);
                    editor.putInt("vidposition", ++videoPosition);
                    editor.commit();

                    playFromSDCard();

                }
            });

            sdCardVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    ++videoPosition;
                    playFromSDCard();
                    return true;
                }

            });

        } else {
            ++videoPosition;
            playFromSDCard();
        }

    }

    /*@Override
    protected void onPause() {
        try {
            super.onPause();
            stopPosition = sdCardVideoView.getCurrentPosition();
            sdCardVideoView.pause();
            Log.d("cycle", "onPause");
            Log.d("flow", "StopPosition" + String.valueOf(stopPosition));
            Log.d("flow", "video Postion" + String.valueOf(videoPosition));
            sharedPreferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(videoPositionPref, videoPosition);

            editor.commit();
        } catch (Exception exp) {
            LogWriter.writeLogException("OnPause", exp);
        }
    }*/

    void initUSB() {
        try {
            if (CommonDataArea.getUsbType() == CommonDataArea.UsbType.FTDI) {
                LogWriter.writeLogUSB("USB Init", "FTDI");
                initUSBFTDI();
                initUSBFTDI();
            } else if (CommonDataArea.getUsbType() == CommonDataArea.UsbType.ESP) {
                LogWriter.writeLogUSB("USB Init", "ESP");
                initUSBESP();
                initUSBESP();
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("InitUSB", exp);
        }
    }

    public void initUSBFTDI() {
        if (CommonDataArea.usbCon != null) return;
        CommonDataArea.usbCon = new UsbControllerFTDI(MainActivity.this);

        CommonDataArea.usbCon.InitUSB(new UsbControllerFTDI.UsbDataRecvd() {
            @Override
            public void ReceivedDataSink(final String dataStr) {
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      try {
                                          LogWriter.writeLogUSB("USBCMDProc", dataStr);
                                          // toastShow(dataStr);
                                          // Toast.makeText(getBaseContext(), dataStr, Toast.LENGTH_SHORT).show();
                                          String mainCommand = getCommandPart(dataStr);
                                          //DateTime set command from RTC
                                          if (mainCommand.equals("DTS")) {
                                              int index = mainCommand.indexOf("DTS=");
                                              String timeStr = mainCommand.substring(index + 4);
                                              parseRTCTime(timeStr);
                                          } else if (mainCommand.equals("cmd")) {
                                              String command = getCommandToSplit(dataStr);
                                              switch (command) {
                                                  case "1":
                                                      Log.d("usbcommands", "setChannelDuration");
                                                      setChannelDuration(dataStr);
                                                      LogWriter.writeLogUSB("USB Init FTDI", "set channel duration");
                                                      break;
                                                  case "2":
                                                      Log.d("usbcommands", "setVideoUrl");
                                                      setVideoURL(id);
                                                      ReadSettings();
                                                      LogWriter.writeLogUSB("USB Init FTDI", "set video url ");

                                                      startActivity(getIntent());

                                                      break;
                                                  case "3":
                                                      Log.d("usbcommands", "device unique id");
                                                      getUUID();
                                                      LogWriter.writeLogUSB("USB Init FTDI", "get unique id ");

                                                      break;
                                                  case "4":
                                                      setMode(dataStr);
                                                      LogWriter.writeLogUSB("USB Init FTDI", "set mode ");

                                                      Log.d("usbcommands", "set mode");
                                                      break;
                                                  case "5":
                                                      noAds = Integer.parseInt((dataStr.substring(17, dataStr.length())).trim());
                                                      LogWriter.writeLogUSB("USB Init FTDI", "set number of advts ");

                                                      Log.d("usbcommands", "set number of advertisements");
                                                      setNumOfAdvts(noAds);
                                                      break;
                                                  case "6":
                                                      Log.d("usbcommands", "set wifi configuration");
                                                      LogWriter.writeLogUSB("USB Init FTDI", "set wifi configuration ");

                                                      setWifiConfig(dataStr);
                                                      break;
                                                  case "7":
                                                      Log.d("usbcommands", "switch to tv");
                                                      LogWriter.writeLogUSB("USB Init FTDI", "switch to tv ");

                                                      switchToPtv();
                                                      break;
                                                  case "8":
                                                      Log.d("usbcommands", "switch to device");
                                                      LogWriter.writeLogUSB("USB Init FTDI", "switch to ptv ");

                                                      switchToDevice();
                                                      break;
                                                  case "9":
                                                      if (statusmode != CommonDataArea.PLAY_MODE_MANUAL) {
                                                          Log.d("usbcommands", "stop advertisements");

                                                          new CommonFunctionArea(MainActivity.this).saveSharedprefmodeautomanual("MANUALMODE");
                                                          Toast.makeText(MainActivity.this, "locked", Toast.LENGTH_LONG).show();
                                                          final Handler handler = new Handler();
                                                          handler.postDelayed(new Runnable() {
                                                              @Override
                                                              public void run() {
                                                                  new CommonFunctionArea(MainActivity.this).saveSharedprefmodeautomanual("AUTOMODE");
                                                                  Toast.makeText(MainActivity.this, "unlocked", Toast.LENGTH_LONG).show();
                                                              }
                                                          }, 30000);
                                                      }
                                                      break;
                                              }//end switch
                                          }
                                      } catch (Exception exp) {
                                          LogWriter.writeLogException("USB", exp);
                                      }
                                  }
                                  //catch
                              }
                );

            }

        });
    }

    public void setMode(String dataStr) {
        ModeOfPlay = (dataStr.substring(10, dataStr.length())).trim();
        LogWriter.writeLogUSB("USBDataProc", "mode changed to " + ModeOfPlay);
        modeSelected(ModeOfPlay);
    }

    public void modeSelected(String mod) {
        if (!mod.equals("")) {
            sharedPreferences = this.getSharedPreferences(preference, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(mode, ModeOfPlay);
            editor.commit();
            toastShow("Mode Changed To " + ModeOfPlay);
            if (ModeOfPlay.equals("YouTube") || ModeOfPlay.equals("sdcard")) {
                Log.d("signalstrack", "sending AD from modeSelected " + ModeOfPlay);
                Utils.SendToUsb("AD\r\n");

            } else if (ModeOfPlay.equals("Channel")) {
                Log.d("signalstrack", "sending TV from modeSelected " + ModeOfPlay);
                Utils.SendToUsb("TV\r\n");
            }
            startActivity(getIntent());

        }
    }

    public void getUUID() {
        LogWriter.writeLog("USBDataProc", "device uuid " + CommonDataArea.uuid);
        for (int i = 0; i < 10; i++) {
            toastShow("Device UUID " + CommonDataArea.uuid);
        }
        String uuid = CommonDataArea.uuid;
        //    usbCon.SendDataToDevice(uuid + "\r\n");

    }

    public void setNumOfAdvts(int noad) {
        //  noAds = Integer.parseInt((dataStr.substring(17, dataStr.length())).trim());
        sharedPreferences = this.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(adNo, noAds);
        editor.commit();
        LogWriter.writeLogUSB("USBDataProc", "Number of advts changed to " + String.valueOf(noAds));

        toastShow("Number of advts changed to " + String.valueOf(noAds));
        // Toast.makeText(getBaseContext(), "Number of advts changed to " + String.valueOf(noAds), Toast.LENGTH_LONG).show();
    }

    public void setVideoURL(String dataStr) {
        String url = (dataStr.substring(15, dataStr.length())).trim();
        id = getYoutubeVideoId(url);
        toastShow("video URL changed to" + id);
        //Toast.makeText(getBaseContext(), "video URL changed to" + id, Toast.LENGTH_LONG).show();

        if (id.equals("")) {
            sharedPreferences = this.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(chnlId, url);
            VIDEO_ID = url;
            editor.apply();


        } else {

            sharedPreferences = this.getSharedPreferences(preference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(chnlId, id);
            editor.apply();
            VIDEO_ID = id;

            LogWriter.writeLog("USBDataProc", "VideoURL changed to " + id);


        }
    }

    public void initUSBESP() {
        if (CommonDataArea.usbConEsp != null) return;
        CommonDataArea.usbConEsp = new UsbControllerESP(MainActivity.this);

        CommonDataArea.usbConEsp.InitUSB(new UsbControllerESP.UsbDataRecvd() {
            @Override
            public void ReceivedDataSink(final String dataStr) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LogWriter.writeLogUSB("USBDataProc", dataStr);
                            // toastShow(dataStr);
                            //    Toast.makeText(getBaseContext(), dataStr, Toast.LENGTH_SHORT).show();
                            String mainCommand = getCommandPart(dataStr);
                            //DateTime set command from RTC
                            if (mainCommand.equals("DTS")) {
                                int index = dataStr.indexOf("DTS=");
                                String timeStr = dataStr.substring(index + 4);
                                parseRTCTime(timeStr);
                            } else if (mainCommand.equals("cmd")) {
                                String command = getCommandToSplit(dataStr);
                                switch (command) {
                                    case "1":
                                        setChannelDuration(dataStr);
                                        Log.d("usbcommands", "setChannelDuration");
                                        break;
                                    case "2":
                                        commonFunctionArea.setVideoURL(dataStr);
                                        Log.d("usbcommands", "set video url");
                                        ReadSettings();
                                        startActivity(getIntent());
                                        break;
                                    case "3":
                                        Log.d("usbcommands", "set device unique id");
                                        getUUID();
                                        break;
                                    case "4":
                                        setMode(dataStr);
                                        Log.d("usbcommands", "set mode");
                                        startActivity(getIntent());
                                        break;
                                    case "5":
                                        Log.d("usbcommands", "set number of ads");
                                        noAds = Integer.parseInt((dataStr.substring(17, dataStr.length())).trim());
                                        setNumOfAdvts(noAds);
                                        break;
                                    case "6":
                                        Log.d("usbcommands", "set wifi config");
                                        setWifiConfig(dataStr);
                                        break;
                                    case "7":
                                        Log.d("usbcommands", "switch to device");
                                        switchToPtv();
                                        break;
                                    case "8":
                                        Log.d("usbcommands", "switch to device");
                                        switchToDevice();
                                        break;
                                    case "9":
                                        ReadSettings();
                                        if (statusmode != CommonDataArea.PLAY_MODE_MANUAL) {
                                            Log.d("usbcommands", "stop advertisements");

                                            new CommonFunctionArea(MainActivity.this).saveSharedprefmodeautomanual("MANUALMODE");
                                            toastShow("Locked");
                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    new CommonFunctionArea(MainActivity.this).saveSharedprefmodeautomanual("AUTOMODE");
                                                    toastShow("unlocked");
                                                }
                                            }, 300000);
                                        }
                                        break;
                                }
                            }

                        } catch (Exception exp) {
                            LogWriter.writeLogException("USB", exp);
                        }
                    }
                });

            }
        });
    }

    void parseRTCTime(String timeStr) {
        try {
            CommonDataArea.rtcPresent = true;
            Date rtcDate = null;
            boolean parseError = false;
            LogWriter.writeLogRTC("RTCUpdateRcvd", "Recvd Time Str ->" + timeStr);
            try {
                String pattern = "dd/MM/yyyy hh:mm:ss aa";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                rtcDate = simpleDateFormat.parse(timeStr);
            } catch (ParseException exp) {
                LogWriter.writeLogRTC("RTCTimeFormatErr", "Failed to Parse AM/PM Mode");
                parseError = true;
            }
            if (parseError) {
                try {
                    String pattern = "dd/MM/yyyy HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    rtcDate = simpleDateFormat.parse(timeStr);

                } catch (ParseException exp) {
                    LogWriter.writeLogRTC("RTCTimeFormatErr", "Failed to Parse 24H Mode");
                    parseError = false;
                }
            }
            if (rtcDate != null) {
                // if (rtcDate.getTime() > CommonDataArea.startOf2019Mills)
                CommonDataArea.rtcTimeValid = true;
                CommonDataArea.rtcTime = rtcDate;
                CommonDataArea.lastRTCReadTime = CommonDataArea.rtcTime.getTime();
                LogWriter.writeLogRTC("TimeUpdateRecvd-RTC", "Time Recvd->" + timeStr);
                //   }
            } else {
                LogWriter.writeLogRTC("TimeUpdateError", "Failed process time recvd");

            }
        } catch (Exception exp) {
            LogWriter.writeLogRTC("TimeUpdateError", "Failed process time recvd->" + exp.getMessage());
            LogWriter.writeLogException("parseRTCTime", exp);
        }
    }

    private void switchToDevice() {
        toastShow("Switched to device");
        LogWriter.writeLogUSB("USB", "Switched to Device");
        Log.d("signalstrack", "sending AD from switchDevice " + ModeOfPlay);
        // Toast.makeText(getBaseContext(), "Switched to device", Toast.LENGTH_LONG).show();
        Utils.SendToUsb("AD\r\n");
    }

    private void switchToPtv() {

        LogWriter.writeLogUSB("USB", "Switched to TV");
        Log.d("signalstrack", "sending TV from switchToPtv " + ModeOfPlay);
        Utils.SendToUsb("TV\r\n");
    }

    Thread wifiConnect = null;

    private void setWifiConfig(String dataStr) {
        //  toastShow("inside fun "+dataStr);
        LogWriter.writeLogWifi("SetWifiConfig", "Wifi Config->" + dataStr);
        String ssid = dataStr.substring(15, dataStr.indexOf("&password="));
        String password = (dataStr.substring(dataStr.indexOf("&password=") + 10, dataStr.length())).trim();
        LogWriter.writeLogWifi("SetWifiConfig", "Parsed data ssid =" + ssid + "Passwd =" + password);

        int spaceindx = password.indexOf(' ');
        LogWriter.writeLogWifi("SetWifiConfig", "Space index =" + spaceindx);
        if (spaceindx != -1) password = password.substring(0, spaceindx);
        String result = password;//.replaceAll("[|?*<\":>+\\[\\]/']", "");
        LogWriter.writeLogWifi("SetWifiConfig", "Password after filtering=" + password);

        LogWriter.writeLogWifi("SetWifiConfig", "Saving Config");
        sharedPreferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NetSSID", ssid);
        editor.putString("NetPASS", password);
        editor.commit();
        toastShow("Wifi changed to " + ssid + " Password " + password);

        LogWriter.writeLogWifi("SetWifiConfig", "Removing network");
        RemoveWifiNetworks();
        LogWriter.writeLogWifi("SetWifiConfig", "Removing current network");
        removeNetwork();

        LogWriter.writeLogWifi("SetWifiConfig", "Connect to wifi");
        finallyConnect(password, ssid);
        LogWriter.writeLogWifi("SetWifiConfig", "finallyConnect call comeplted");


    }

    class WifiConnect implements Runnable {
        public String ssid;
        public String password;

        @Override
        public void run() {
            LogWriter.writeLogUSB("USB", "Wifi changed to " + ssid + " Password " + password);

            LogWriter.writeLogWifi("SetWifiConfig", "Removing network");
            RemoveWifiNetworks();
            LogWriter.writeLogWifi("SetWifiConfig", "Removing current network");
            removeNetwork();
            LogWriter.writeLogWifi("SetWifiConfig", "Connect to wifi");
            finallyConnect(password, ssid);
            LogWriter.writeLogWifi("SetWifiConfig", "finallyConnect call comeplted");
        }
    }

    //Format of  mbile app commands cmd=4$xxxxx=xxxx$mmmm=rrrrr\r
    //Format of Timme commands DTS=dd/mm/yyyy hh:mm:ss AM/PM
    public String getCommandPart(String data) {
        String result = data.substring(0, 3);
        return result;
    }

    public String getCommandToSplit(String data) {

        String result = data.substring(4, 5);
        return result;

    }

    public void setChannelDuration(String data) {

        channelDuration = Integer.parseInt((data.substring(15, data.length())).trim());
        sharedPreferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(chnlDrtn, channelDuration);
        editor.putInt(CommonDataArea.TempChannelduration, channelDuration);

        LogWriter.writeLog("USBDataProc", "Channel duration set to" + channelDuration);
        editor.commit();
        //    mToastToShow = Toast.makeText(getBaseContext(), "Channel duration set to" + channelDuration, Toast.LENGTH_LONG);
        toastShow("Channel duration set to" + channelDuration);


    }

    int getNetworkID(String networkSSID) {
        try {
            List<WifiConfiguration> wifis = mainWifiObj.getConfiguredNetworks();
            if (wifis == null) return -1;
            for (WifiConfiguration wifi1 : wifis) {
                if (wifi1.SSID.contains(networkSSID)) {
                    return wifi1.networkId;
                }
            }
            return -1;
        } catch (Exception exp) {
            return -1;
        }
    }

    private void RemoveWifiNetworks() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            int networkId = wifiManager.getConnectionInfo().getNetworkId();
            LogWriter.writeLogWifi("WifiRemove", "Removing network id->" + networkId + " SSID= " + i.SSID);
            if (wifiManager.removeNetwork(i.networkId))
                LogWriter.writeLogWifi("WifiRemove", "Removed network->" + i.SSID);
            else LogWriter.writeLogWifi("WifiRemove", "Failed to Removed network->" + i.SSID);
        }
        wifiManager.saveConfiguration();

    }

    void removeNetwork() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int networkId = wifiManager.getConnectionInfo().getNetworkId();
        LogWriter.writeLogWifi("WifiRem", "Current network->" + networkId);
        if (networkId != -1) {
            LogWriter.writeLogWifi("WifiRem", "Removing network->" + networkId);
            wifiManager.removeNetwork(networkId);
            wifiManager.saveConfiguration();
        }

    }

    private boolean finallyConnect(String networkPass, String networkSSID) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);
        mainWifiObj.disconnect();
        updateStatusBox("Disconnecting from current network");

        int curNetID = getNetworkID(networkSSID);
        if (curNetID != -1) mainWifiObj.removeNetwork(curNetID);
        if (curNetID != -1) {
            LogWriter.writeLogWifi("Wifi check", "Removing current network");
            updateStatusBox("Removing current network");
            mainWifiObj.removeNetwork(curNetID);
        }
        LogWriter.writeLogWifi("Wifi check", "Adding network->" + networkSSID);
        int netId = mainWifiObj.addNetwork(wifiConfig);
        LogWriter.writeLogWifi("Wifi check", "Added network id->" + netId);
        if (netId == -1) {
            LogWriter.writeLogWifi("Wifi check", "Failed to add network");
            return false;
        }
        LogWriter.writeLogWifi("Wifi check", "Disconnect");
        mainWifiObj.disconnect();

        updateStatusBox("Enabling network " + wifiConfig.SSID + " " + netId);
        LogWriter.writeLogWifi("Wifi check", "Enable");
        mainWifiObj.enableNetwork(netId, true);
        LogWriter.writeLog("Wifi check", "Reconnect");
        updateStatusBox("Connecting network " + wifiConfig.SSID);
        LogWriter.writeLogWifi("Wifi check", "Reconnect");
        mainWifiObj.reconnect();

        return true;

    }

//    public void modeSelected(String mod) {
//        if (!mod.equals("")) {
//            sharedPreferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(mode, ModeOfPlay);
//            editor.commit();
//            toastShow("Mode Changed To " + ModeOfPlay);
//            if (ModeOfPlay.equals("YouTube") || ModeOfPlay.equals("sdcard")) {
//                Log.d("signalstrack", "sending AD from modeSelected " + ModeOfPlay);
//                Utils.SendToUsb("AD\r\n");
//
//            } else if (ModeOfPlay.equals("Channel")) {
//                Log.d("signalstrack", "sending TV from modeSelected " + ModeOfPlay);
//                Utils.SendToUsb("TV\r\n");
//            }
//
//            //  Toast.makeText(getBaseContext(), "Mode Changed To " + ModeOfPlay, Toast.LENGTH_SHORT).show();
//
//            startActivity(getIntent());
//
////            }
//        }
//    }

    public void toastShow(String message) {
        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
        ViewGroup viewGroup = (ViewGroup) toast.getView();
        TextView textView = (TextView) viewGroup.getChildAt(0);
        textView.setTextSize(40);
        toast.show();
    }

    public void updateSysInfo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    sysInfo();
                } catch (Exception exp) {
                    LogWriter.writeLogException("UpdateUI", exp);
                }
            }
        });
    }

    public void sysInfo() {
        String sysInfo = "";
        final WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            LogWriter.writeLogWifi("WifiCheck", "wifi status check");
            isWifiConnected = wifiConnected();
            if (isWifiConnected) {
                String name = getWifiName(this).toString();
                sysInfo = "Wifi connected to  : " + name;
                LogWriter.writeLogWifi("sysInfo WifiCheck", "wifi connected ->" + name);
            } else {
                LogWriter.writeLogWifi("sysInfo WifiCheck", "wifi not connected");
                sysInfo = "No wifi connected";
            }

        } else {

            sysInfo = "No wifi not enabled";
        }

        String versionName = "NA";
        int versionCode = 0;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            sysInfo += " Version Name = " + versionName + " Version Num :" + versionCode;
            sysInfo += " Time Slot :" + CommonDataArea.slotName + " Idle Time: " + CommonDataArea.chanDuration + " Advt batch :" + CommonDataArea.numAddsToPlay + " StartTime:" + CommonDataArea.slotStartTime + " EndTime:" + CommonDataArea.slotEndTime;
        } catch (Exception ex) {

        }
        wifiText.setText(sysInfo);
        getWifi();
    }

    public String getWifiName(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return null;
    }

    public boolean wifiConnected() {
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiCheck.isConnected();
    }

    private void getWifi() {

        mainWifiObj.startScan();
        Log.e("Test 3", "Start Wifi Scan");

    }

    void logServerNLocTimes(int toWhere) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long servTime = Utils.getSavedServerTime(getApplicationContext());
        long adjustedDevTimeMills = Utils.getAdjustedTimeMills(getApplicationContext());
        long devTime = System.currentTimeMillis();

        String adjTimeStr = format.format(adjustedDevTimeMills);
        String adjServStr = format.format(servTime);
        String adjDevStr = format.format(devTime);
        String rtcTime = "NA";
        if (CommonDataArea.rtcTimeValid) {
            rtcTime = format.format(CommonDataArea.rtcTime);
        }
        Utils.sendLogToServer("Allocate daily-> Adjusted Time =" + adjTimeStr + " Server Time =" + adjServStr + " Dev Time =" + adjDevStr, this);
        if (toWhere == 0) {
            LogWriter.writeLogPlayAlloc("Different Times", "********Device Time=" + adjDevStr + " Server Time=" + adjServStr + " Adjusted Dev Time=" + adjTimeStr + " RTC Time= " + rtcTime);
        } else if (toWhere == 1) {
            LogWriter.writeLogDailyPlayAlloc("Different Times", "********Device Time=" + adjDevStr + " Server Time=" + adjServStr + " Adjusted Dev Time=" + adjTimeStr + " RTC Time= " + rtcTime);
        }
    }

    public void allocateDailyImp() {
        try {
            Cursor cursor = null;
            Utils.adjustPlayMethod(getApplicationContext());
            logServerNLocTimes(0);
            long adjustedDevTimeMills = Utils.getAdjustedTimeMills(getApplicationContext());

            if (CommonDataArea.timeStatus == CommonDataArea.TIMESTATUS_DEF_PLAY) return;
            ;
            dataBaseHelper.cursorDisplyAdvtTable(1);
            //  dataBaseHelper.setStatusDayNA();
            cursor = dataBaseHelper.checkDailyImpression(adjustedDevTimeMills);
            if (cursor == null) {
                LogWriter.writeLogPlayAlloc("allocateDailyImp", "Retuned null Db exception");
                return;
            }
            if (cursor.getCount() <= 0) {
                LogWriter.writeLogPlayAlloc("allocateDailyImp", "No record to allocate or already allocated");
                Utils.sendLogToServer("Allocate daily->No record to allocate or already allocated", this);
                return;
            }
            //Copying records to array lsit to avoid database lock
            ArrayList<ContentValues> rows = new ArrayList<ContentValues>();
            if (cursor.moveToFirst()) {
                do {
                    ContentValues cv = new ContentValues();
                    cv.put(DataBaseHelper.Alloc_Id, cursor.getString(cursor.getColumnIndex("mapId")));
                    cv.put(DataBaseHelper._Id, cursor.getInt(cursor.getColumnIndex("_id")));
                    cv.put(DataBaseHelper.Video_DwlDate, cursor.getString(cursor.getColumnIndex(DataBaseHelper.Video_DwlDate)));
                    cv.put(DataBaseHelper.EndDate, cursor.getString(cursor.getColumnIndex("EndDate")));
                    cv.put(DataBaseHelper.TotalImpression, cursor.getInt(cursor.getColumnIndex("TotalImpression")));
                    cv.put(DataBaseHelper.Rem_imp, cursor.getInt(cursor.getColumnIndex("Remaining_Impressions")));
                    cv.put(DataBaseHelper.StatusOfDays, cursor.getString(cursor.getColumnIndex("StatusofDays")));
                    rows.add(cv);
                }
                while (cursor.moveToNext());
            } else {
                LogWriter.writeLogPlayAlloc("AllocateDailyImp", "No Records found for daily advt alocation");
            }
            cursor.close();
            dataBaseHelper.close();
            ;
            TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
            for (ContentValues value : rows) {
                String sdfhdfgh = value.getAsString(DataBaseHelper.Alloc_Id);// cursor.getString(cursor.getColumnIndex("mapId"));
                _id = value.getAsInteger(DataBaseHelper._Id);
                String endDate = value.getAsString(DataBaseHelper.EndDate);
                String dnldDate = value.getAsString(DataBaseHelper.Video_DwlDate);
                int totalImpression = value.getAsInteger(DataBaseHelper.TotalImpression);
                int remainingImpression = value.getAsInteger(DataBaseHelper.Rem_imp);
                String daysOfStatus = value.getAsString(DataBaseHelper.StatusOfDays);

                LogWriter.writeLogPlayAlloc("AllocateDailyImp", "Processing Alloc ID->" + sdfhdfgh + " Id->" + _id);

                long curDevTimeMills = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Adding 1 to end date to avoid fail in comparisionof last day
                Calendar c = Calendar.getInstance(tz);
                c.setTime(format.parse(endDate));
                c.add(Calendar.DATE, 1);
                Date EndDate = new Date(c.getTimeInMillis());

                Date DnldDate = format.parse(dnldDate);
                String devTime = sdf.format(curDevTimeMills);
                String adjustedEndDate = format.format(EndDate.getTime());
                String curdaye = format.format(adjustedDevTimeMills);
                Date currentDate = new Date();
                currentDate.setTime(adjustedDevTimeMills);

                long diffInMillies = EndDate.getTime() - adjustedDevTimeMills;

                String devAdjTime = sdf.format(adjustedDevTimeMills);
                long days = (TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)) + 1;
                long maxDaysDiff = EndDate.getTime() - DnldDate.getTime();
                long maxDays = 0;
                if (maxDaysDiff > 0)
                    maxDays = (TimeUnit.DAYS.convert(maxDaysDiff, TimeUnit.MILLISECONDS));

                if (diffInMillies < 0) {
                    LogWriter.writeLogPlayAlloc("AllocateDailyImp", "Adjusted End Date ->" + adjustedEndDate + " Adjusted Cur Date-> " + curdaye + " Adjusted End Date Mills ->" + EndDate.getTime() + " Adj Dev Time Mills->" + adjustedDevTimeMills);
                    LogWriter.writeLogPlayAlloc("AllocateDailyImp", "NEGATIVE Difference ->" + diffInMillies);
                    LogWriter.writeLogPlayAlloc("AllocateDailyImp", "Days Difference ->" + days);
                    LogWriter.writeLogPlayAlloc("AllocateDailyImp", "Download to Enddate diff->" + maxDays);
                    LogWriter.writeLogPlayAlloc("AllocateDailyImp", "Alloc ID->" + sdfhdfgh + " Id->" + _id);
                    LogWriter.writeLogPlayAlloc("AllocateDailyImp", "Alloc ID->" + sdfhdfgh + " Id->" + _id);
                    LogWriter.writeLogDailyPlayAlloc("EndDate Over", "Closing ->" + _id + "End Date ->" + sdf.format(EndDate) + " Downld Date->" + dnldDate);
                    dataBaseHelper.closeByRowId(_id, value);
                    continue;
                }
                if (days > maxDays) days = maxDays;
                if (days <= 0) days = 0;

                LogWriter.writeLogPlayAlloc("AllocateDailyImp", "mapid " + sdfhdfgh + " EndDate " + endDate + " total impression " + totalImpression + " Remaining impression " + remainingImpression + " days of status " + daysOfStatus + " days diff " + String.valueOf(days) + " current date " + curdaye);
                Utils.sendLogToServer("Allocate daily-> campaign Parameters---> mapid " + sdfhdfgh + " EndDate " + endDate + " total impression " + totalImpression + " Remaining impression " + remainingImpression + " days of status " + daysOfStatus + " days remaining " + String.valueOf(days) + " current date " + curdaye, MainActivity.this);
                LogWriter.writeLogPlayAlloc("Time Variations", "Last fetched server time-> " + CommonDataArea.serverTimeString + " Current Dev Time->" + devTime + " Adjusted Dev Time->" + devAdjTime);
                LogWriter.writeLogPlayAlloc("Decision Making Params", " EndDate ->" + endDate + " Remaining Days->" + days + " Remaining Impression ->" + remainingImpression);
                if (remainingImpression > 0) {
                    if (currentDate.before(EndDate) || days > 0) {
                        LogWriter.writeLogPlayAlloc("Condition Checked", "Endate before and days greater than zero");
                        float daily = 0;
                        if (days <= 0) daily = remainingImpression;
                        else daily = remainingImpression / days;
                        if (Math.round(daily) < 1) {
                            Utils.sendLogToServer("Allocate daily- Calculated Values ---> mapid Calculated daily is less than 1 ", MainActivity.this);
                            daily = remainingImpression;
                        }
                        int dailyImp = Integer.parseInt(String.valueOf(Math.round(daily)));
                        LogWriter.writeLogPlayAlloc("Allocate daily- About update DB", "#######################DB Update Check Daily Log#######################");
                        Utils.sendLogToServer("Allocate daily- Calculated Values ---> mapid " + sdfhdfgh + " daily impression " + String.valueOf(dailyImp) + " EndDate " + endDate + " total impression " + totalImpression + " Remaining impression " + remainingImpression + " days of status " + daysOfStatus + " days diff " + String.valueOf(days) + " current date " + curdaye, MainActivity.this);
                        logServerNLocTimes(1);
                        LogWriter.writeLogDailyPlayAlloc("Allocating for mapid " + sdfhdfgh, " Daily impression " + String.valueOf(dailyImp) + " total impression " + totalImpression + " Remaining impression " + remainingImpression + " Down load Date " + dnldDate + " EndDate " + endDate + " days diff " + String.valueOf(days) + " current date " + curdaye);
                        dataBaseHelper.updateDailyImpression(_id, curdaye, dailyImp, remainingImpression, "ready", 0.0);
                    }
                    long daysAdj = (TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)) + 1;
                    if (currentDate.after(EndDate) || daysAdj < 0) {
                        LogWriter.writeLogPlayAlloc("Condition Checked", "Endate after and days less than zero");
                        int dailyImp = 0;
                        LogWriter.writeLogPlayAlloc("Alloted dailyImp", "Marking campaign as completed->" + dailyImp + " _id->" + _id);
                        //dataBaseHelper.updateDailyImpression(_id, curdaye, dailyImp, remainingImpression, "completed", 100.0);
                        Utils.sendLogToServer("Allocate daily- Deleting Campaign end date reached or remaining days <0->" + _id, MainActivity.this);
                        LogWriter.writeLogPlayAlloc("Marking campaign completed", "Campaign ID->" + _id);
                        LogWriter.writeLogDailyPlayAlloc("Curdate bigger than end date ", "Closing ->" + _id + "End Date ->" + sdf.format(EndDate) + " Cur date->" + sdf.format(currentDate));

                        dataBaseHelper.closeByRowId(_id, value);
                    }
                    dataBaseHelper.cursorDisplyAdvtTable(1);
                } else {
                    LogWriter.writeLogPlayAlloc("Marking campaign completed", "Remaining impression ZERO , Closing");
                    Utils.sendLogToServer("Allocate daily- Deleting Campaign as remaining impression zero->" + _id, MainActivity.this);
                    LogWriter.writeLogDailyPlayAlloc("Remaining impression zero", "Camp->" + _id + "Reaminig Imp->" + remainingImpression);

                    dataBaseHelper.closeByRowId(_id, value);
                }

            }

        } catch (Exception exp) {
            LogWriter.writeLogException("DailyImpression", exp);
        }
    }

    public void contatdetails() {
        txt_contact.setText("To advertise on this TV contact : " + contactDetails);
    }

    public void createAlarm() {
        final int REQUEST_CODE = 101;
        Date dat = new Date();//initializes to now
        //Toast.makeText(getApplicationContext(), "Date alm"+dat, Toast.LENGTH_SHORT).show();
        Log.d("alarm", "alarmmailstarted");
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY, 21);//set the alarm time
        cal_alarm.set(Calendar.MINUTE, 30);
        cal_alarm.set(Calendar.SECOND, 0);

        if (cal_alarm.before(cal_now)) {
            //if its in the past increment

            cal_alarm.add(Calendar.DATE, 1);
        } else {
            ComponentName receiver = new ComponentName(this, ReceiverAlarm.class);
            PackageManager pm = this.getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

            //SET YOUR AlarmManager here
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, com.tracking.m2comsys.adapplication.extras.ReceiverAlarm.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
        }
    }

    class Browser
            extends WebViewClient {
        Browser() {
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            paramWebView.loadUrl(paramString);
            return true;
        }
    }

    public class MyWebClient
            extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public MyWebClient() {
            View view = getVideoLoadingProgressView();


        }

        public Bitmap getDefaultVideoPoster() {
            if (MainActivity.this == null) {
                return null;
            }
            return BitmapFactory.decodeResource(MainActivity.this.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) MainActivity.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            MainActivity.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {

                onHideCustomView();
                return;
            }

            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = MainActivity.this.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = MainActivity.this.getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;

            ((FrameLayout) MainActivity.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }

    private void emulateClick(final WebView webview) {
        long delta = 100;
        long downTime = SystemClock.uptimeMillis();
        float x = webview.getLeft() + webview.getWidth(); //in the middle of the webview
        float y = webview.getTop() + webview.getHeight();

        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime + delta, MotionEvent.ACTION_DOWN, 30, 30, 0);
        // change the position of touch event, otherwise, it'll show the menu.
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime + delta, MotionEvent.ACTION_UP, -1, -1, 0);

        webview.post(new Runnable() {
            @Override
            public void run() {
                if (webview != null) {
                    webview.dispatchTouchEvent(downEvent);
                    webview.dispatchTouchEvent(upEvent);
                }
            }
        });
    }
}



package com.tracking.m2comsys.adapplication.Activity;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tracking.m2comsys.adapplication.Database.DataBaseHelper;
import com.tracking.m2comsys.adapplication.R;
import com.tracking.m2comsys.adapplication.utils.CommonDataArea;
import com.tracking.m2comsys.adapplication.utils.LogWriter;
import com.tracking.m2comsys.adapplication.utils.UpdatePlayCount;
import com.tracking.m2comsys.adapplication.utils.Utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;


public class Advertisement extends AppCompatActivity {

    public DataBaseHelper dataBaseHelper;

    private static final String DATABASE_NAME = "AdvertisementDetails";
    public static int i = 1;
    public int databaseentry = 0;
    public static String tag = "";
    public static String path = "";
    public Uri uri;
    MediaController mediaController;
    VideoView videoView;
    SharedPreferences sharedPreferences;
    String preference = "Settings";

    public static int noOfAdPlay = 1;
    static int advtBatch = 0;
    public static int videoId;
    public static String videoName;
    Timer timer;

    private static final String Video_Name = "Name";
    private static final String Video_Extn = "Extension";
    public static Cursor getSelectedADvtsCursor;
    public static Cursor getSelectedADvtsHistoryCursor;
    public static int impressions;
    public static int noOfTimesPlayed;
    public static int _id;
    public static int rem_imp;
    public static int totalImpression;
    public static int map_id;
    private static final String Alloc_Id = "mapId";
    public static int playErrorCount;
    public static String lastPlayed;
    private static final String Impressions = "Impressions";
    private static final String Video_LastPlayed = "LastPlayed";
    private static final String Video_Server_Id = "VideoServerId";
    private static final String Video_NoOfTimesPlayed = "NoOfTimesPlayed";
    private static final String PlayErrorCount = "PlayErrorCount";
    private static final String Video_DriveId = "DriveId";
    private static int statusmode = 0;
    public static String uuid;
    public static String ModeOfPlay = "Channel";
    public static String mode = "Mode";
    public File file;
    public static int errorCheck = 0;
    int lastPlayPos = 0;
    int curPos;
    int notChangedCount = 0;
    String videoPositionPref = "VideoPosition";
    String videoSeekPositionPref = "VideoSeekPosition";
    LocalBroadcastManager localBroadcastManager;
//    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(intent.getAction().equals("com.tracking.m2comsys.adapplication.firebase")){
//                finish();
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.durga.action.close");
        // localBroadcastManager.registerReceiver(broadcastReceiver, mIntentFilter);
        LogWriter.writeLogActivity("AdvertisementActivity", "Creting activity");

        videoView = (VideoView) findViewById(R.id.videoViewDb);
        dataBaseHelper = new DataBaseHelper(this, DATABASE_NAME, null, CommonDataArea.DATABASE_VERSION);
        Log.d("flow", "advtActivity");
        uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        sharedPreferences = getSharedPreferences(preference, MODE_PRIVATE);
        int a = CommonDataArea.numAddsToPlay;
        statusmode = sharedPreferences.getInt(CommonDataArea.playAutomationMode, CommonDataArea.PLAY_MODE_AUTO);
        int videoPosition = sharedPreferences.getInt(videoPositionPref, 0);
        int stopPosition = sharedPreferences.getInt(videoSeekPositionPref, 0);
        Log.d("flow", "Advertisment");
        Log.d("flow", "StopPosition" + String.valueOf(stopPosition));
        Log.d("flow", "video Postion" + String.valueOf(videoPosition));
        timer = new Timer();

        timer.scheduleAtFixedRate(new PlayMonitorTimer(), 10000, 10000);

        if (a != 0) {
            noOfAdPlay = a;
        }
        ModeOfPlay = sharedPreferences.getString(mode, "Channel");
        try {
            if (!playVideo()) {
                goBack();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                long date = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
                lastPlayed = sdf.format(date);
                noOfTimesPlayed += 1;
                dataBaseHelper.updateTable(_id, impressions, lastPlayed, noOfTimesPlayed, playErrorCount, rem_imp);
                float percentPlayed = 0;
                if ((impressions > 0) && (noOfTimesPlayed > 0)) {
                    percentPlayed = (float) (((float) noOfTimesPlayed / (float) impressions) * (float) 100);
                }
                dataBaseHelper.insertAdvtPlayHistEntry(Advertisement.this, videoName, map_id, videoId, totalImpression, rem_imp, noOfTimesPlayed, percentPlayed);
                Log.d("databasevalues", "id " + String.valueOf(_id) + " , impressions " + String.valueOf(impressions) + " lastPlayed " + lastPlayed + ", nooftimesPlayed " + String.valueOf(noOfTimesPlayed) + " playErrorCount " + String.valueOf(playErrorCount));
                Utils.sendLogToServer("Play completed Vid->" + videoId + " Played " + (advtBatch + 1) + " advertisement out of " + noOfAdPlay + " in the batch. No of times played today->" + noOfTimesPlayed + " Total impression for today->" + impressions, Advertisement.this);
                LogWriter.writeDailyPlayLog("Play completed", "Vid->" + videoId + " Played " + (advtBatch + 1) + " advertisement out of " + noOfAdPlay + " in the batch. No of times played today->" + noOfTimesPlayed + " Total impression for today->" + impressions + "Total Impression->" + totalImpression + "Total Remaining->" + rem_imp);
                LogWriter.writeLogActivity("AdvertisementActivity", "number of advertisement during an interval->" + advtBatch + " Play completed Vid->" + videoId + "no of times played today->" + noOfTimesPlayed + " total impression for today ->" + impressions + "  mapid  " + String.valueOf(map_id));
                if (++advtBatch >= noOfAdPlay) {
                    advtBatch = 0;
                    LogWriter.writeLogActivity("AdvertisementActivity", "Play Batch completed");
                    try {
                        goBack();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (!playVideo()) {
                            LogWriter.writeLogActivity("AdvertisementActivity", "Play Error");
                            try {
                                goBack();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                LogWriter.writeLogActivity("AdvertisementActivity", "Playing one video errored");
                i++;
                ++playErrorCount;
                dataBaseHelper.updateTable(_id, impressions, lastPlayed, noOfTimesPlayed, playErrorCount, rem_imp);
                Utils.sendLogToServer("Play->Error playing back" + videoId, Advertisement.this);
                if (++errorCheck >= 3) {
                    LogWriter.writeLogActivity("AdvertisementActivity", "#######Error count exceeded #####");
                    errorCheck = 0;
                    try {
                        goBack();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (!playVideo()) goBack();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    public boolean playVideo() throws ParseException {
        Cursor cursor = null;
//        cursor=dataBaseHelper.checkDailyImpression();
//        if (cursor == null) {
//            LogWriter.writeLogActivity("AdvertisementActivity", "No details in database");
//            return false;
//        }
//        if(cursor.moveToFirst())
//        {
//            _id = cursor.getInt(cursor.getColumnIndex("_id"));
//            String endDate=cursor.getString(cursor.getColumnIndex("EndDate"));
//            int totalImpression=cursor.getInt(cursor.getColumnIndex("TotalImpression"));
//            int remainingImpression=cursor.getInt(cursor.getColumnIndex("Remaining_Impressions"));
//            String daysOfStatus=cursor.getString(cursor.getColumnIndex("StatusofDays"));
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            Date EndDate = format.parse(endDate);
//            long curr=System.currentTimeMillis();
//            String curdaye = format.format(curr);
//            Date currentDate=format.parse(curdaye);
//            long diffInMillies = EndDate.getTime() - currentDate.getTime();
//            long days=TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
//            if(currentDate.before(EndDate)||days>0)
//            {
//float daily=remainingImpression/days;
//         int dailyImp=Integer.parseInt(String.valueOf(Math.round(daily)));
//
//dataBaseHelper.updateDailyImpression(_id,curdaye,dailyImp);
//            }
//            else if(currentDate.equals(EndDate)||days==0)
//            {  int dailyImp=remainingImpression;
//
//                dataBaseHelper.updateDailyImpression(_id,curdaye,dailyImp);
//
//            }
//
//        }
        try {

            path = Environment.getExternalStorageDirectory().toString() + "/advts";
            file = new File(path);
            LogWriter.writeLogActivity("AdvertisementActivity", "In Play video function");
            if (!file.exists()) return false;

            cursor = dataBaseHelper.getVideoToPlay();
            if (cursor == null) {
                LogWriter.writeLogActivity("AdvertisementActivity", "No Video to play");
                return false;
            }
            if (cursor.moveToFirst()) {
                Log.d("cursorVslue", "cursorCount " + cursor.getCount());
                Log.d("cursorVslue", "video name " + cursor.getString(cursor.getColumnIndex(Video_Name)));

                _id = cursor.getInt(cursor.getColumnIndex("_id"));
                rem_imp = cursor.getInt(cursor.getColumnIndex("Remaining_Impressions"));
                totalImpression = cursor.getInt(cursor.getColumnIndex("TotalImpression"));
                tag = cursor.getString(cursor.getColumnIndex(Video_DriveId)) + "." + cursor.getString(cursor.getColumnIndex(Video_Extn));
                impressions = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Impressions)));
                videoId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Video_Server_Id)));
                videoName = cursor.getString(cursor.getColumnIndex(Video_Name));
                noOfTimesPlayed = cursor.getInt(cursor.getColumnIndex(Video_NoOfTimesPlayed));
                playErrorCount = cursor.getInt(cursor.getColumnIndex(PlayErrorCount));
                map_id = cursor.getInt(cursor.getColumnIndex(Alloc_Id));
                cursor.close();
                dataBaseHelper.close();
                path = Environment.getExternalStorageDirectory().toString() + "/advts" + "/" + tag;
                file = new File(path);
                if (file.exists()) {
                    uri = Uri.parse(path);
                    mediaController = new MediaController(this);
                    videoView.setMediaController(mediaController);
                    videoView.setVideoURI(uri);
                    videoView.start();
                    lastPlayPos = 0;
                    curPos = 0;
                    LogWriter.writeLogActivity("AdvertisementActivity", "Playing video");
                    LogWriter.writeDailyPlayLog("Play->PLaying Video:", videoName);
                    Utils.sendLogToServer("Play->PLaying Video:" + videoName, Advertisement.this);
                    return true;
                } else {
                    ++playErrorCount;
                    LogWriter.writeLogActivity("AdvertisementActivity", "Play error video not exist");
                    dataBaseHelper.updateTable(_id, impressions, lastPlayed, noOfTimesPlayed, playErrorCount, rem_imp);
                    Utils.sendLogToServer("Play->Error  video not exist :" + videoName, Advertisement.this);
                    LogWriter.writeDailyPlayLog("Play->Error  video not exist :", videoName);
                }
            }
            return false;
        } catch (Exception exp) {
            Utils.sendLogToServer("PLay->Error :" + exp.getMessage() + "video name :" + videoName, Advertisement.this);
            Log.e("Error", exp.getMessage());
            return false;
        }
    }

    public void goBack() throws ParseException {
        if (statusmode == 0 || !playVideo()) {
            if (ModeOfPlay.equals("Channel")) {
                Log.d("signalstrack", "sending TV command from goBack " + ModeOfPlay);
                // if(CommonDataArea.usbCon!=null)
                if (!CommonDataArea.isContinious)
                    Utils.SendToUsb("TV\r\n");//SendDataToDevice("TV\r\n");
                //  new UsbPort().InitUSB(Advertisement.this, "TV\r\n");

            } else if (ModeOfPlay.equals("YouTube") || ModeOfPlay.equals("sdcard")) {
                Log.d("signalstrack", "sending AD command from goback " + ModeOfPlay);
                Utils.SendToUsb("AD\r\n");
            }
            UpdatePlayCount playCount = new UpdatePlayCount(CommonDataArea.CALLING_FROM_ADVERTISE_COMPLETE);
            playCount.execute();
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            Log.d("flow", "ad to main complted");
            //Intent intent = new Intent(Advertisement.this, MainActivity.class);
            //startActivity(intent);
            LogWriter.writeLogActivity("AdvertisementActivity", "Closing activity");
            Utils.sendLogToServer("Play->Complete playing advt batch", Advertisement.this);
            finish();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    class PlayMonitorTimer extends TimerTask {
        @Override
        public void run() {
            float percent = 0;
            curPos = videoView.getCurrentPosition();
            int vidLen = videoView.getDuration();
            if (vidLen > 0)
                percent = ((float) curPos / (float) vidLen) * 100;
            // Utils.sendLogToServer("Play->Play monitor :" + videoName + " Percent :" + percent, Advertisement.this);
            if (curPos != lastPlayPos) {
                lastPlayPos = curPos;
                notChangedCount = 0;
            } else ++notChangedCount;
            if (notChangedCount > 10) {
                Utils.sendLogToServer("Play->Error-Play monitor found playback stuck Canceling :" + videoName, Advertisement.this);
                LogWriter.writeLogActivity("AdvertisementActivity", "Playing video");
                Utils.sendLogToServer("Play->Error-Playmonitor Closing the activity because advt play hanged", Advertisement.this);
                try {
                    goBack();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

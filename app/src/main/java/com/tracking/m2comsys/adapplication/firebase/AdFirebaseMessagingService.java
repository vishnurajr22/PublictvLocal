package com.tracking.m2comsys.adapplication.firebase;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.tracking.m2comsys.adapplication.Activity.Advertisement;
import com.tracking.m2comsys.adapplication.Activity.MainActivity;
import com.tracking.m2comsys.adapplication.Database.DataBaseHelper;
import com.tracking.m2comsys.adapplication.data.model.FirebaseModel;
import com.tracking.m2comsys.adapplication.extras.GMailSender;
import com.tracking.m2comsys.adapplication.utils.CommonDataArea;
import com.tracking.m2comsys.adapplication.utils.CommonFunctionArea;
import com.tracking.m2comsys.adapplication.utils.DownloadAdvts;
import com.tracking.m2comsys.adapplication.utils.LogWriter;
import com.tracking.m2comsys.adapplication.utils.UpdatePlayCount;
import com.tracking.m2comsys.adapplication.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.tracking.m2comsys.adapplication.PtvApplication.ptvApplication;


public class AdFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationManager notificationManager;
    private SharedPreferences sharedPreferences;
    private String preference = "Settings";
    private String modeOfPlay = "";
    public static String mode = "Mode";
    private String title;
    private String timeStamp = "";
    private List<FirebaseModel.Deviceuuid> deviceuuids;
    public boolean uuidFound = false;
    String uuid = "";
    String fileName = "";


    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        sharedPreferences = this.getSharedPreferences(preference, Context.MODE_PRIVATE);
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            InputStream stream = new ByteArrayInputStream(remoteMessage.getData().toString().getBytes(StandardCharsets.UTF_8));
            String te = remoteMessage.getData().toString();
            Log.d("strvgfbhg", te);
            LogWriter.writeLogFirebase("Mesg", "Mesg Recvd");
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(stream);
            FirebaseModel settings = gson.fromJson(reader, FirebaseModel.class);
            System.out.println("settings = " + settings.getTitle());
            Log.e("Settings", "Settings");
            title = settings.getTitle();
            timeStamp = settings.getBody().getTimestamp();
            deviceuuids = settings.getBody().getDeviceUuid();
            try {

                if (deviceuuids.size() > 0) {
                    TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    uuid = Settings.Secure.getString(ptvApplication.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    for (int i = 0; i < deviceuuids.size(); i++) {
                        //     System.out.println("uuid = " + uuid);
                        //    System.out.println("deviceuuids.get(i).toString() = " + deviceuuids.get(i).getDL_DeviceUUID().toString());
                        if (uuid.equalsIgnoreCase(deviceuuids.get(i).getDL_DeviceUUID().toString())) {
                            uuidFound = true;
                            break;
                        } else
                            continue;
                    }
                }
            } catch (Exception e) {
                LogWriter.writeLogException("fromFirebase", e);
            }
            if (!uuidFound) {
                LogWriter.writeLogFirebase("MesgRcvd", "Mesg Not for this device");
                return;
            }
            System.out.println("deviceuuids.size() = " + deviceuuids.size());
            modeOfPlay = sharedPreferences.getString(mode, "Channel");
            System.out.println("modeOfPlay = " + modeOfPlay);
            String firstLetter = getFirstLetter(title);
            if (title.equalsIgnoreCase("START") || title.equalsIgnoreCase("STOP")) {
                LogWriter.writeLogFirebase("Mesg Recvd", "START/STOP message recvd");
                broadcastVideoNotification(modeOfPlay, title);
            } else if (title.equalsIgnoreCase("MANUALMODE") || title.equalsIgnoreCase("AUTOMODE")) {
                LogWriter.writeLogFirebase("Mesg Recvd", "MANUALMODE/AUTOMODE message recvd");
                new CommonFunctionArea(this).saveSharedprefmodeautomanual(title);
            } else if (title.equalsIgnoreCase("RESTART")) {
                LogWriter.writeLogFirebase("Mesg Recvd", "Restart message recvd");
                if (checkUUIDTimeStamp(timeStamp))
                    new CommonFunctionArea(this).restartFromBackground();
            } else if (title.equalsIgnoreCase("LOG")) {
                LogWriter.writeLogFirebase("Mesg Recvd", "LOG message recvd");
                if (checkUUIDTimeStamp(timeStamp))
                    getLog();
            } else if (title.equalsIgnoreCase("RESET")) {
                LogWriter.writeLogFirebase("Mesg Recvd", "RESET message recvd");
                if (checkUUIDTimeStamp(timeStamp))
                    resetDevice();
            } else if (title.equalsIgnoreCase("RESETALL")) {
                LogWriter.writeLogFirebase("Mesg Recvd", "RESETALL message recvd");
                if (checkUUIDTimeStamp(timeStamp)){
                    new DownloadAdvts(this).deleteAllAdvts();
                    new DataBaseHelper(this).DeleteAllCampaigns();

                }
            } else if (firstLetter.equalsIgnoreCase("D") || firstLetter.equalsIgnoreCase("V") || firstLetter.equalsIgnoreCase("E") || firstLetter.equalsIgnoreCase("T")) {
                split(title);
            }
        }catch(Exception exp){
            LogWriter.writeLogException("firebase",exp);
        }
    }

    public boolean checkUUIDTimeStamp(String timeStamp) {
        boolean istrue = false;
        long min = getMinitues(timeStamp);
        if (uuidFound && min < 5) {
            istrue = true;
        } else {
            istrue = false;
        }
        return istrue;
    }



    /*
     * Broadcast the Video Notification to the Activity
     */
    private void broadcastVideoNotification(String modeOfPlay, String status) {
        System.out.println("call start = ");
        if (status.equalsIgnoreCase("start")) {
            if (modeOfPlay.equalsIgnoreCase("Channel")) {
                Intent mIntent = new Intent(this, Advertisement.class);
                Utils.SendToUsb("AD\r\n");
                mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(mIntent);
            } else {
                Intent mIntent = new Intent(this, Advertisement.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(mIntent);
            }
        } else if (status.equalsIgnoreCase("stop")) {
            if (modeOfPlay.equalsIgnoreCase("Channel")) {
                Intent mIntent = new Intent(this, MainActivity.class);
                Utils.SendToUsb("TV\r\n");
//                LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(AdFirebaseMessagingService.this);
//                localBroadcastManager.sendBroadcast(new Intent("com.tracking.m2comsys.adapplication.firebase"));
                mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(mIntent);
            } else {
                Intent mIntent = new Intent(this, MainActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(mIntent);
            }
        }
    }


    private void getLog() {
//        System.out.println("call Log = ");
//        long min = getMinitues(timeStamp);
//        System.out.println("min = " + min);
//        System.out.println("uuidFound = " + uuidFound);
//        if (min < 5) {
//            if (uuidFound) {

        String path = Environment.getExternalStorageDirectory().toString() + "/PTV";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        fileName = "";
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            EmailThread thread = new EmailThread(files[i].getName());
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//                }
//
//            }
        }
        Utils.sendLogToServer("Firebase Commnd->Processing log request", this);
        Log.d("firebasemessages", "calling take log from firebase");
    }

    public long getMinitues(String timestmp) {
        long time = System.currentTimeMillis();
        System.out.println("timeStamp = " + timestmp);
        System.out.println("timeStamp = " + time);
        long diffMs = time - Long.parseLong(timestmp);
        long diffSec = diffMs / 1000;
        long min = diffSec / 60;
        return min;
    }

    private void resetDevice() {
        try {
            System.out.println("call reset = ");
            DataBaseHelper db = new DataBaseHelper(ptvApplication);
            db.clearAdvtTable();
            Utils.sendLogToServer("Firebase Commnd->Processing Reset Request", this);
            db.cursorDisplyAdvtTable(1);
            Log.d("firebasemessages", "calling reset from firebase");
        }catch(Exception exp){
            LogWriter.writeLogException("Exception",exp);
        }
    }

    class EmailThread extends Thread {
        String fileName;
        String emailId = sharedPreferences.getString(CommonDataArea.EmailId, "publictvstore@gmail.com");
        String password = sharedPreferences.getString(CommonDataArea.PasswordEmail, "100$bill");

        EmailThread(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void run() {
            try {

                GMailSender sender = new GMailSender(

                        emailId,

                        password);


                sender.addAttachment(Environment.getExternalStorageDirectory().toString() + "/PTV/" + fileName);

                sender.sendMail(fileName, "This mail has been sent from PTV app along with attachment",

                        emailId,

                        emailId);

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean split(String splittword) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        String currentString = splittword;
        String separated1 = currentString.substring(0,2);
        String separated2 = currentString.substring(2,currentString.length()).trim();
        if (separated1.equals("DC")) {
            Utils.sendLogToServer("Firebase Commnd->Delete campaign "+separated2, this);
           boolean res=dataBaseHelper.deleteByCampainID(Integer.parseInt(separated2));
            return res;

        }
        if (separated1.equalsIgnoreCase("EM")) {
            new CommonFunctionArea(this).modeSelected(separated2);
            Utils.sendLogToServer("Firebase Commnd->Change mode "+separated2, this);
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        if (separated1.equalsIgnoreCase("VU")) {
            Utils.sendLogToServer("Firebase Commnd->Change video url "+separated2, this);
            new CommonFunctionArea(this).setVideoURL(separated2);
            return true;
        }
        if (separated1.equalsIgnoreCase("VN")) {
            Utils.sendLogToServer("Firebase Commnd->Set number of advertisements "+separated2, this);
            new CommonFunctionArea(this).setNumOfAdvts(Integer.parseInt(separated2));
            return true;
        }
        if (separated1.equalsIgnoreCase("VD")) {
            Utils.sendLogToServer("Firebase Commnd->Set channel duration "+separated2, this);
            new CommonFunctionArea(this).setChannelDuration(separated2);
            return true;
        }
        if (separated1.equalsIgnoreCase("TR")) {
            Utils.sendLogToServer("Firebase Commnd->Update play count request", this);
            UpdatePlayCount playCount = new UpdatePlayCount(CommonDataArea.CALLING_FROM_FIREBASE_DEL);
            playCount.execute();
            return true;
        }
        return false;
    }

    public String getFirstLetter(String firstletter) {


        String test = firstletter;
        char first = test.charAt(0);

        return String.valueOf(first);
    }
}

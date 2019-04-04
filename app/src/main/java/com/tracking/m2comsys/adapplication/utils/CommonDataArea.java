package com.tracking.m2comsys.adapplication.utils;

import android.content.Context;

import com.tracking.m2comsys.adapplication.Activity.MainActivity;
import com.tracking.m2comsys.adapplication.Database.DataBaseHelper;

import com.tracking.m2comsys.adapplication.usb.UsbControllerESP;
import com.tracking.m2comsys.adapplication.usb.UsbControllerFTDI;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;


/**
 * Created by admin on 08/22/2017....
 */

public class CommonDataArea {
    public static Thread downloadFiles;
    public static String uuid;
    public static MainActivity mainActivity;
    public static Context appContext;
    public static File rootFile;

    public static DataBaseHelper dataBaseHelper;
    public static int wifiNotConnetedCount=0;
    public static boolean wifiCheckRunning=false;
    public static String wifiName  ="";

    public static long startedAt=0;
    public static long appNotRunningCount=0;
    public static long updateStatusDelay=0;
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "AdvertisementDetails";
    private static int alredyStarted=0;
    public static final String Video_Id = "_id";
    public static final String Video_Name = "Name";
    public static final String Video_Extn = "Extension";
    public static final String Video_DwlDate = "DownloadingDate";
    public static final String Video_DriveId = "DriveId";
    public static final String VideoStatus = "VideoStatus";
    public static final String PlayErrorCount = "PlayErrorCount";
    public static final String contactDetails = "Contact";
    public static final String storedTime  = "StoredTime";
    public static final String storedDeviceTime  = "StoredDevTime";
    public int idModel;
    public static final String Alloc_Id = "mapId";
    public static final String Video_Server_Id = "VideoServerId";
    public static final String Video_LastPlayed = "LastPlayed";
    public static final String Video_NoOfTimesPlayed = "NoOfTimesPlayed";
    public static final String Status  = "status";
    public static final String EmailId  = "Emailid";
    public static final String PasswordEmail  = "EmailPassword";
    public static final String TempChannelduration  = "TempChannelDur";
    public static final String chnlDrtn  = "ChannelDuration";
    public static final String playAutomationMode  = "statusmode";
    public static final String adNo = "NoOfAdvertisements";
    public static final String serverURL = "http://publictvads.in/WebServiceLiveTest/";

    public static String curSysStatus_Playmethod;

    public static boolean isInSlot=false;
    public static int numAddsToPlay;
    public static int chanDuration;
    public static String slotName;
    public static String slotStartTime;
    public static String slotEndTime;
    public static boolean isContinious=false;

    public static UsbControllerFTDI usbCon;
    public static void setAlredyStarted(int alredyStarted) {
        LogWriter.writeLogUSB("USB","Setting AlredyStarted as ->"+alredyStarted);
        CommonDataArea.alredyStarted = alredyStarted;
    }
    public static int getAlredyStarted() {
        return alredyStarted;
    }
    public static UsbControllerESP usbConEsp;
    public static UsbType usbType = UsbType.ESP;
    public enum UsbType{
        FTDI,
        ESP
    }
    public static UsbType getUsbType() {
        return usbType;
    }

    public static void setUsbType(UsbType usbType) {
        LogWriter.writeLogUSB("USB","Setting type as ->"+usbType.toString());
        CommonDataArea.usbType = usbType;
    }
    public static boolean usbConEvent = false;
    public static boolean bootEvent = false;
    public static long serverTime = 0;

    public static String serverTimeString = "";

    public static long startOf2019Mills = Long.valueOf("1546281000000"); //actually 2019 -1546281000000
    public static long istZoneOffsetMills = 5*60*60*1000+30*60*1000;
    public static long twoHoursMills = 2l*60l*60l*1000l;
    public static long hbTimeDelay = 60*1000l;
    public static int timeStatus =0;
    public static Date rtcTime;
    public static boolean rtcTimeValid=false;
    public static boolean rtcPresent =false;
    public static long lastRTCReadTime=0;
    public static int rtcRetry=0;

    public static int TIMESTATUS_USEDEVICE_TIME =1;
    public static int TIMESTATUS_USESERVER_TIME =2;
   //public static int TIMESTATUS_USEADJUST_TIME =3;
    public static int TIMESTATUS_USEADJUST_ZONE =3;
    public static int TIMESTATUS_RTC_TIME =4;
    public static int TIMESTATUS_DEF_PLAY =5;

    public static int PLAY_MODE_AUTO=0;
    public static int PLAY_MODE_MANUAL=1;

    public static int CALLING_FROM_ADVERTISE_COMPLETE=0;
    public static int CALLING_FROM_FIREBASE_DEL=1;
    public static int CALLING_FROM_PLAYTHREAD=2;


    public static String TIME_ZONE = "GMT+05:30";
    public static  Timer rtcAdj=null;
    public static long lastLogSendTime =0;
    public static Object lockObj = new Object();
}

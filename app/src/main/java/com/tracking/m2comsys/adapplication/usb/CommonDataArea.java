package com.tracking.m2comsys.adapplication.usb;

/**
 * Created by admin on 4/19/2016.
 */
public class CommonDataArea
{
    /* UI -><- Service commands

     */

    public final static int REQUEST_CON_CONNECT_TO_USB =1;
    public final static int REQUEST_CON_SEND_RECIPE = 2;
    public final static int REQUEST_CON_RECIPE_RUN = 3;
    public final static int REQUEST_CON_EMERGENCY_STOP =4;
    public final static int REQUEST_CON_RECIPE_STOP=5;
    public final static int REQUEST_CON_GETSTATUS =6;
    public final static int REQUEST_CON_REMTIME = 7;
    public final static int REQUEST_CON_MASTRESET = 8;
    public final static int REQUEST_CON_COILON = 9; //Message parameter COILNUM, ONTIME ->"08:00" , OFFTIME ->"10:00" times relative recipe run, TEMP
    public final static int REQUEST_CON_FANON = 10; //Message parameter COILNUM, ONTIME ->inseconds, OFFTIME ->"10:00"
    public final static int OVENSTAT_SYSTEM_READY = 11;
    public final static int OVENSTAT_POWER_FAIL = 12;
    public final static int OVENSTAT_RECIPE_STARTED = 14;
    public final static int OVENSTAT_RECIPE_COMPLETED =15;
    public final static int OVENSTAT_RECIPE_FAILED =16;
    public final static int OVENSTAT_EMERGENCY_STOP =17;
    public final static int OVENSTAT_HB_REQUEST =18;
    public final static int REQUEST_CON_PREHEAT=19;
    public final static int OVENSTAT_PREHEAT_READY = 20;
    public final static int OVENSTAT_TEMPARATURE = 21;
    public final static int REQUEST_USB_STATUS =22;
    public final static int OVENSTAT_HBREPLY =23; //requesting controller to send HB

    public final static int REQUEST_STATUS_SUCCESS = 1; //Success
    public final static int REQUEST_STATUS_FAIL = 0; //Fail
    public final static int REQUEST_STATUS_PENDING = -1;


    public static String MESGPARAM_REQUEST_CMD = "CMD";
    public static String MESGPARAM_REQUEST_COILNUM = "COILNUM";
    public static String MESGPARAM_REQUEST_ONTIME= "ONTIME";
    public static String MESGPARAM_REQUEST_OFFTIME= "OFFTIME";
    public static String MESGPARAM_REQUEST_TEMPERATURE= "TEMP";
    public static String MESGPARAM_REQUEST_RECIPE= "RECIPE";
    public static String MESGPARAM_STATUS_CMDSTATUSVAL = "CMDSTATUS";
    public static String MESGPARAM_STATUS_CMDSTATUSMESG = "CMDSTATUSMESG";
    public static String MESGPARAM_STATUS_COILS = "COILSTATUS";
    public static String MESGPARAM_STATUS_REMTIME = "COILSTATUS";
    public static String MESGPARAM_STATUS_OBJECT = "OBJECT";
    public static String MESGPARAM_STATUS_OBJECT_STATUS = "OBJECT";
    public static String MESGPARAM_STATUS_COIL1= "COIL1";
    public static String MESGPARAM_STATUS_COIL2= "COIL2";
    public static String MESGPARAM_STATUS_FAN= "FAN";

    public static final String NOTIFICATIONTOAPP = "smartoven.phtl.com.ui";
    public static final String NOTIFICATIONTOSERVICE = "smartoven.phtl.com.controller";



    public static String OVEN_CMD_RECIPE_RUN = "START_RECIPE";
    public static String OVEN_CMD_RECIPE_STOP = "STOP";
    public static String OVEN_CMD_OVENSTATUS = "STATUS";
    public static String OVEN_CMD_RECIPE_REMTIME = "REMAIN_TIME";
    public static String OVEN_CMD_RECIPE_PREHEAT= "PREHEAT";

    public static String OVENREPLY_RECIPE_RECEIVED = "RECIPE_RECIVED";
    public static String OVENREPLY_RECIPE_STARTED = "RECIPE_STARTED";
    public static String OVENREPLY_RECIPE_COMPLETED = "RECIPE_COMPLETED";
    public static String OVENREPLY_RECIPE_FAILED = "RECIPE_FAILED";
    public static String OVENREPLY_EMERGENCY_STOP = "EMERGENCY_STOP ";
    public static String OVENREPLY_OVEN_STATUS = "STATUS";
    public static String OVENREPLY_OVEN_HBREQUEST = "HB";
    public static String OVENREPLY_SYSTEM_READY  = "SYSTEM_READY";
    public static String OVENREPLY_PREHEAT_READY  ="PREHEAT_READY";
    public static String OVENREPLY_REMAIN_TIME  ="REMAIN_TIME";
    public static String OVENREPLY_TEMPETATURE  ="TEMP";

    public static boolean ovenConnected= false;


}

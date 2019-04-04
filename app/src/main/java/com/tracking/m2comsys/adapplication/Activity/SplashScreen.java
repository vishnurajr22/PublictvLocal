package com.tracking.m2comsys.adapplication.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tracking.m2comsys.adapplication.R;
//import com.tracking.m2comsys.adapplication.usb.CommonDataArea;
import com.tracking.m2comsys.adapplication.data.Injection;
import com.tracking.m2comsys.adapplication.extras.SplashContract;
import com.tracking.m2comsys.adapplication.extras.SplashPresenter;
import com.tracking.m2comsys.adapplication.usb.UsbControllerFTDI;
import com.tracking.m2comsys.adapplication.utils.CommonDataArea;
import com.tracking.m2comsys.adapplication.utils.LogWriter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SplashScreen extends AppCompatActivity implements SplashContract.SplashView {
    public static SharedPreferences sharedPreferences;
    String preference = "Settings";
    boolean permFail=false;
    boolean permFailUSB;
    boolean permUsbProgress = false;
    protected static final String ACTION_USB_PERMISSION = "ch.serverbox.android.USB";
    public SplashContract.SplashPres presenter;
    static String mainActivityPackageName = "com.tracking.m2comsys.adapplication.Activity.MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        CommonDataArea.usbType = CommonDataArea.UsbType.ESP;
        Log.i("SplashScreen", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = getIntent();
        if(intent!=null) {
            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if(device!=null) {
                CommonDataArea.usbConEvent = true;

            }
        }
        sharedPreferences = getSharedPreferences(preference, MODE_PRIVATE);
        presenter = new SplashPresenter(this, Injection.provideRepositiry(this));
        presenter.checkVersionIntialization();
        LogWriter.writeLog("SplashScreen", "starting");
        Log.d("SplashScreen", "Render Complete");
        try {
        /*    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();**/
            permFail= false;
            if (!permissionUSB()) {
                permFail = true;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("permissionUSB", permFailUSB);
                editor.commit();
            } else {
                permUsbProgress=false;
                Log.d("SplashScreen", "Detect Device");
                detectDevice();
                Log.d("SplashScreen", "Starting Activity");
            }
            if(!permUsbProgress) {
                if (!checkPermissions()) {
                    getPermissions();
                    permFail = true;
                }
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("Start", exp);
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!permFail) {
                    Intent intent2 = new Intent(SplashScreen.this, MainActivity.class);

                    startActivity(intent2);
                }

            }
        },100);

        if(!permFail)  finish();
    }
    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (mainActivityPackageName.equals(task.baseActivity.getClassName())) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashScreen.this, "Already Running", Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            }
        }

        return false;
    }

    public boolean isServiceRunning() {

        if (CommonDataArea.getAlredyStarted() == 200) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SplashScreen.this, "Already Running", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }
        return false;
    }

    private String VID_PID_ESP = "10C4:EA60";
    private String VID1_FTDI2_MODULE = "0403:6015";
    private String VID_FTDI_MODULE = "0403:6001";//Bijoy Dev FTDI module

    void detectDevice() {
        UsbManager usbManager = (UsbManager) getSystemService(USB_SERVICE);
        HashMap<String, UsbDevice> devlist = usbManager.getDeviceList();
        Iterator<UsbDevice> deviter = devlist.values().iterator();
        UsbDevice dv;
        while (deviter.hasNext()) {
            dv = deviter.next();
            if (String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID_PID_ESP)) //|| (String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID1_PID1_ESP))) {
            {
                CommonDataArea.usbType = CommonDataArea.UsbType.ESP;
            }

            if ((String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID_FTDI_MODULE))) {
                CommonDataArea.usbType = CommonDataArea.UsbType.FTDI;
            }

            if ((String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID1_FTDI2_MODULE))) {
                CommonDataArea.usbType = CommonDataArea.UsbType.FTDI;
            }
        }
    }

    public boolean permissionUSB() {
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devList = manager.getDeviceList();
        Iterator<UsbDevice> deviter = devList.values().iterator();
        UsbDevice dv = null;
        while (deviter.hasNext()) {
            dv = deviter.next();
            if (String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID_PID_ESP)) //|| (String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID1_PID1_ESP))) {
            {
                CommonDataArea.usbType = CommonDataArea.UsbType.ESP;
                break;
            }

            if ((String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID_FTDI_MODULE))) {
                CommonDataArea.usbType = CommonDataArea.UsbType.FTDI;
                break;
            }

            if ((String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID1_FTDI2_MODULE))) {
                CommonDataArea.usbType = CommonDataArea.UsbType.FTDI;
                break;
            }
        }
        if (dv != null && !manager.hasPermission(dv)) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            registerReceiver(mUsbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
            permUsbProgress=true;
            manager.requestPermission(dv, pendingIntent);
            return false;
        }

        return true;
    }


    boolean checkPermissions(){
        boolean permFail = false;
        permFail= true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permFail = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permFail = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permFail = false;
        return permFail;
    }

    public void getPermissions() {
         boolean permFail = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permFail = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permFail = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permFail = true;
        if (permFail) {
            // if anyone fails, request all. android will request only needed
            //Toast.makeText(SplashScreen.this, "Permission Error->Pleasengrand permission either in Settings app or in the app popup",Toast.LENGTH_LONG).show();
            //Manifest.permission.CALL_PHONE--removed by bmjo
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, perms, 100);
            permFail = true;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("permissions", false);
            editor.commit();

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("permissions", true);
            editor.commit();
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @TargetApi(12)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                permUsbProgress=false;
                if (!intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    LogWriter.writeLog("USGB", "Permission not granted");
                }
                if(!checkPermissions()){
                    getPermissions();
                    permFail = true;
                }
            }
        }
    };

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
                    // if (permFail && permFailUSB) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                  /*  } else {
                        Toast.makeText(SplashScreen.this, "Allow All permissions", Toast.LENGTH_SHORT).show();
                    }*/
                }
            }
        }
    }

    @Override
    public void checkVersionIntializationRes(boolean check) {
        System.out.println("check = " + check);
        if (!check)
            presenter.intializeVersion();
    }

    @Override
    public void setPresenter(SplashContract.SplashPres presenter) {

    }

    @Override
    public void makeToast(String message) {

    }
}

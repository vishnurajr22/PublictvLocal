package com.tracking.m2comsys.adapplication.BroadcastReceivers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tracking.m2comsys.adapplication.Database.DataBaseHelper;
import com.tracking.m2comsys.adapplication.extras.SendMail;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class BootService extends Service {
    String pdate;
    SharedPreferences sharedpreferences;
    static int i = 0;
    String PlayedDate;
    int j = 0;
    WritableSheet sheet;
    public String sheetTitle = "";
    public static int count = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BootCompletedsuccess", "gdfgdfggfgfgsgsdgfgg");
        // Toast.makeText(this, "BootCompleted", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        File sd = new File(Environment.getExternalStorageDirectory().toString() + "/EMAIL");
        String csvFile = "CheckEmail.xls";

        File directory = new File(sd.getAbsolutePath());
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);

            WritableSheet sheet2 = workbook.createSheet("CurrentStatus", j + 1);
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            DataBaseHelper dataBaseHelper2 = new DataBaseHelper(this);
            Cursor cursor = dataBaseHelper.CheckEmail();
            Cursor cursor1 = dataBaseHelper.CheckAdvt();

            if ((cursor != null) && (cursor.getCount() > 0)) {

                if (cursor.moveToFirst()) {
                    do {
                        if (!sheetTitle.equalsIgnoreCase(cursor.getString(3))) {
                            sheetTitle = cursor.getString(3);
                            sheet = workbook.createSheet(sheetTitle, count);
                            count++;
                            i = 0;

                        }


                        String id = String.valueOf(cursor.getInt(0));
                        String videoname = cursor.getString(1);
                        long PlayedTimeMills = cursor.getLong(2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

                        // Create a calendar object that will convert the date and time value in milliseconds to date.
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(PlayedTimeMills);
                        String hms = formatter.format(calendar.getTime()).substring(10);
                        PlayedDate = cursor.getString(3);
                        String VideoIDServ = cursor.getString(4);
                        String CampaignID = String.valueOf(cursor.getInt(5));
                        String TotalNumImpression = String.valueOf(cursor.getInt(6));
                        String RemainingImpression = String.valueOf(cursor.getInt(7));
                        String TotalPlayed = String.valueOf(cursor.getInt(27));
                        String TodaysImpression = String.valueOf(cursor.getInt(8));
                        String PlayedToday = String.valueOf(cursor.getInt(9));
                        String status = String.valueOf(cursor.getInt(10));
                        String EndDate = cursor.getString(25);
                        String extension = cursor.getString(29);


                        sheet.addCell(new Label(0, 0, "ID"));
                        sheet.addCell(new Label(1, 0, "VideoName"));
                        sheet.addCell(new Label(2, 0, "PlayedTime"));
                        sheet.addCell(new Label(3, 0, "PlayedDate"));
                        sheet.addCell(new Label(4, 0, "VideoIDServ"));
                        sheet.addCell(new Label(5, 0, "CampaignID"));
                        sheet.addCell(new Label(6, 0, "TotalNumImpression"));
                        sheet.addCell(new Label(7, 0, "RemainingImpression"));
                        sheet.addCell(new Label(8, 0, "TodaysImpression"));
                        sheet.addCell(new Label(9, 0, "PlayedToday"));
                        sheet.addCell(new Label(10, 0, "TotalPlayed"));
                        sheet.addCell(new Label(11, 0, "EndDate"));


                        i++;
                        sheet.addCell(new Label(0, i, id));
                        sheet.addCell(new Label(1, i, videoname + "." + extension));
                        sheet.addCell(new Label(2, i, hms));
                        sheet.addCell(new Label(3, i, PlayedDate));
                        sheet.addCell(new Label(4, i, VideoIDServ));
                        sheet.addCell(new Label(5, i, CampaignID));
                        sheet.addCell(new Label(6, i, TotalNumImpression));
                        sheet.addCell(new Label(7, i, RemainingImpression));
                        sheet.addCell(new Label(8, i, TodaysImpression));
                        sheet.addCell(new Label(9, i, PlayedToday));
                        sheet.addCell(new Label(10, i, TotalPlayed));
                        sheet.addCell(new Label(11, i, EndDate));


                    } while (cursor.moveToNext());

                }

                if ((cursor1 != null) && (cursor1.getCount() > 0)) {

                    if (cursor1.moveToFirst()) {
                        do {
                            String vidnam = cursor1.getString(10);
                            String dailyimpression = String.valueOf(cursor1.getInt(4));
                            String playedtoday = String.valueOf(cursor1.getInt(16));
                            String remainingtoday = String.valueOf(cursor1.getInt(4) - cursor1.getInt(16));
                            String totalimpression = String.valueOf(cursor1.getInt(5));
                            String totalremaining = String.valueOf(cursor1.getInt(6));

                            sheet2.addCell(new Label(0, 0, "VideoName"));
                            sheet2.addCell(new Label(1, 0, "DailyImpression"));
                            sheet2.addCell(new Label(2, 0, "PlayedToday"));
                            sheet2.addCell(new Label(3, 0, "RemainingToday"));
                            sheet2.addCell(new Label(4, 0, "TotalImpressions"));
                            sheet2.addCell(new Label(5, 0, "TotalRemaining"));

                            int i = cursor1.getPosition() + 1;
                            sheet2.addCell(new Label(0, i, vidnam));
                            sheet2.addCell(new Label(1, i, dailyimpression));
                            sheet2.addCell(new Label(2, i, playedtoday));
                            sheet2.addCell(new Label(3, i, remainingtoday));
                            sheet2.addCell(new Label(4, i, totalimpression));
                            sheet2.addCell(new Label(5, i, totalremaining));

                        } while (cursor1.moveToNext());

                    }
                } else {
                    Log.d("N2_", "nodata2");

                }

                cursor.close();
                workbook.write();
                workbook.close();
                dataBaseHelper.close();
                dataBaseHelper2.close();
                dataBaseHelper2.updateData(sheetTitle);
                sharedpreferences = getSharedPreferences("mypreference",
                        Context.MODE_PRIVATE);
                SendMail sm = new SendMail(this, sharedpreferences.getString("Email", ""), "subject", "message", Environment.getExternalStorageDirectory().toString() + "/EMAIL" + "/CheckEmail.xls");
                sm.execute();
            } else {
                Log.d("N_", "nodata");

            }

            /*cursor.close();
            workbook.write();
            workbook.close();*/


        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

        stopSelf();
        return START_STICKY;
    }


}

package com.tracking.m2comsys.adapplication.utils;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by admin on 08/28/2017.
 */

public class UpdatePlayCount extends AsyncTask<Void, Void, Void> {
    public int callingFrom;
    // update to server total number of times advertisement played
    private static final String Rem_imp = "Remaining_Impressions";


    public UpdatePlayCount(int callingFrom) {
        this.callingFrom = callingFrom;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {

            if(callingFrom==CommonDataArea.CALLING_FROM_FIREBASE_DEL)
            {
                takeReportsOfAllAdvts();
            }
            else if(callingFrom==CommonDataArea.CALLING_FROM_ADVERTISE_COMPLETE)
            {
                updateAndCloseCompltedAdvts();
                updateAdvtsPlayCount();
            }else {
                 updateAndCloseCompltedAdvts();
                 updateCompltedPendPlayCountAndChangeSts();
             }

            return null;
        } catch (Exception exp) {
            LogWriter.writeLogException("UpdatePlayCount", exp);
        }
        return null;
    }

    public static boolean updateInProgress=false;
   public static void updateAndCloseCompltedAdvts() {
        try {
            synchronized (CommonDataArea.lockObj){
                if(updateInProgress) return;;
                updateInProgress=true;
            }
            Cursor cursorget = CommonDataArea.dataBaseHelper.getCompletedFilesListToUpdate();
            if ((cursorget != null) && (cursorget.getCount() > 0)) {
                if (cursorget.moveToFirst()) {
                    do {
                        int _id = cursorget.getInt(cursorget.getColumnIndex("_id"));
                        String vdoid = cursorget.getString(cursorget.getColumnIndex(CommonDataArea.Video_Server_Id));
                        String timesPlayed = cursorget.getString(cursorget.getColumnIndex(Rem_imp));
                        String campaignid = cursorget.getString(cursorget.getColumnIndex(CommonDataArea.Alloc_Id));
                        LogWriter.writeLog("UpdateClode-Pend", "Mark as completd localy---VidID->" + vdoid + "Num Played->" + timesPlayed);
                        String commenPath = "http://publictvads.in/WebServiceLiveTest/UpdateImpression.php?vid=" + vdoid + "&count=" + timesPlayed + "&did=" + CommonDataArea.uuid + "&mapid=" + campaignid;
                        Log.d("databasevalues", "updating count close and complete " + commenPath);
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
                                sb.append(line);
                            }
                            Log.d("databasevalues", "updated count response close and complte" + sb.toString());
                            String res = sb.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            String parseres = jsonObject.getString("status");
                            //to do remove comment
                            if (parseres.equals("1")) {
                                CommonDataArea.dataBaseHelper.closeAfterUpdate(_id);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    while (cursorget.moveToNext());
                    CommonDataArea.dataBaseHelper.close();
                }
            }
        }catch (Exception exp){
            LogWriter.writeLogException("UpdateClose",exp);
        }
        finally {
            synchronized (CommonDataArea.lockObj){
                updateInProgress=false;
            }
        }
    }
  public static void updateCompltedPendPlayCountAndChangeSts() {
            try {
                synchronized (CommonDataArea.lockObj){
                    if(updateInProgress) return;;
                    updateInProgress=true;
                }
                Cursor cursorget = CommonDataArea.dataBaseHelper.getCompletedPendFilesListToUpdate();
                if ((cursorget != null) && (cursorget.getCount() > 0)) {
                    if (cursorget.moveToFirst()) {
                        do {
                            int _id = cursorget.getInt(cursorget.getColumnIndex("_id"));
                            String vdoid = cursorget.getString(cursorget.getColumnIndex(CommonDataArea.Video_Server_Id));
                            String timesPlayed = cursorget.getString(cursorget.getColumnIndex(Rem_imp));
                            String campaignid = cursorget.getString(cursorget.getColumnIndex(CommonDataArea.Alloc_Id));
                            LogWriter.writeLog("UpdateCountFor-complted-pend", "VidID->" + vdoid + "Num Played->" + timesPlayed);
                            String commenPath = "http://publictvads.in/WebServiceLiveTest/UpdateImpression.php?vid=" + vdoid + "&count=" + timesPlayed + "&did=" + CommonDataArea.uuid + "&mapid=" + campaignid;

                            try {
                                URL uRl = new URL(commenPath);
                                Log.d("databasevalues", "updating play count " + commenPath);
                                HttpURLConnection httpURLConnection = (HttpURLConnection) uRl.openConnection();
                                httpURLConnection.connect();
                                Log.d("connect", "Connected");
                                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                                StringBuilder sb = new StringBuilder();
                                String line = null;
                                // Read Server Response
                                while ((line = reader.readLine()) != null) {
                                    // Append server response in string
                                    sb.append(line);
                                }
                                String res = sb.toString();
                                JSONObject jsonObject = new JSONObject(res);
                                String parseres = jsonObject.getString("status");
                                //to do remove comment
                                if (parseres.equals("1")) {
                                    CommonDataArea.dataBaseHelper.completeAfterUpdate(_id);
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        while (cursorget.moveToNext());

                    }
                }
                CommonDataArea.dataBaseHelper.close();

            } catch (Exception exp) {
                LogWriter.writeLogException("UpdateCount", exp);
            } finally {
                synchronized (CommonDataArea.lockObj){
                    updateInProgress=false;
                }
            }
    }
    void updateAdvtsPlayCount() {
        try {
            Cursor cursorget = CommonDataArea.dataBaseHelper.getReadyFilesListToUpdate();
            if ((cursorget != null) && (cursorget.getCount() > 0)) {
                if (cursorget.moveToFirst()) {
                    do {
                        int _id = cursorget.getInt(cursorget.getColumnIndex("_id"));
                        String vdoid = cursorget.getString(cursorget.getColumnIndex(CommonDataArea.Video_Server_Id));
                        String timesPlayed = cursorget.getString(cursorget.getColumnIndex(Rem_imp));
                        String campaignid = cursorget.getString(cursorget.getColumnIndex(CommonDataArea.Alloc_Id));
                        LogWriter.writeLog("UpdateCount", "VidID->" + vdoid + "Num Played->" + timesPlayed);
                        String commenPath = "http://publictvads.in/WebServiceLiveTest/UpdateImpression.php?vid=" + vdoid + "&count=" + timesPlayed + "&did=" + CommonDataArea.uuid + "&mapid="+campaignid;

                        try {
                            URL uRl = new URL(commenPath);
                            Log.d("databasevalues","updating play count "+commenPath);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) uRl.openConnection();
                            httpURLConnection.connect();
                            Log.d("connect", "Connected");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            // Read Server Response
                            while ((line = reader.readLine()) != null) {
                                // Append server response in string
                                sb.append(line);
                            }
                            String res=sb.toString();
                            JSONObject jsonObject=new JSONObject(res);
                            String parseres= jsonObject.getString("status");
                            Log.d("databasevalues","result of updating impressions "+sb.toString());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    while (cursorget.moveToNext());

                }
            }
            CommonDataArea.dataBaseHelper.close();
        } catch (Exception exp) {
            LogWriter.writeLogException("UpdateCount", exp);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
    void takeReportsOfAllAdvts() {
        Cursor cursorget = CommonDataArea.dataBaseHelper.getAdvtsReport();
//        if ((cursorget != null) && (cursorget.getCount() > 0)) {
            if (cursorget.moveToFirst()) {
                do {
                    int _id = cursorget.getInt(cursorget.getColumnIndex("_id"));
                    String vdoid = cursorget.getString(cursorget.getColumnIndex(CommonDataArea.Video_Server_Id));
                    String timesPlayed = cursorget.getString(cursorget.getColumnIndex(Rem_imp));
                    String campaignid = cursorget.getString(cursorget.getColumnIndex(CommonDataArea.Alloc_Id));
                    LogWriter.writeLog("UpdateCount", "Mark as completd localy---VidID->" + vdoid + "Num Played->" + timesPlayed);
                    String commenPath = "http://publictvads.in/WebServiceLiveTest/UpdateImpression.php?vid=" + vdoid + "&count=" + timesPlayed + "&did=" + CommonDataArea.uuid+ "&mapid="+campaignid;
                    Log.d("databasevalues", "updating count close and complete " + commenPath);
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
                            sb.append(line);
                        }
                        Log.d("databasevalues", "updated count response close and complte" + sb.toString());
                        String res=sb.toString();
                        JSONObject jsonObject=new JSONObject(res);
                        String parseres= jsonObject.getString("status");

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                while (cursorget.moveToNext());
                CommonDataArea.dataBaseHelper.close();
            }
      //  }
    }

}

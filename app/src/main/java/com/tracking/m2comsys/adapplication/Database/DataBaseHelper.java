package com.tracking.m2comsys.adapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.tracking.m2comsys.adapplication.utils.CommonDataArea;
import com.tracking.m2comsys.adapplication.utils.LogWriter;
import com.tracking.m2comsys.adapplication.utils.Utils;
import com.tracking.m2comsys.adapplication.utils.VideoDetailsModel;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

/**
 * Created by m2comsys on 11/7/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "AdvertisementDetails";
    public static final String Advertisement_List = "Advertisement_Video_List";
    public static final String DEVICE_SETTINGS = "DeviceSettings";
    public static final String _Id = "_id";
    public static final String Alloc_Id = "mapId";
    public static final String Campaign_Id = "campaignid";
    public static final String Video_Name = "Name";
    public static final String Video_Extn = "Extension";
    public static final String Device_id = "Device_Id";
    public static final String Settings_id = "SettingsId";
    public static final String Settings_Version = "SettingsVersion";
    public static final String PlayBatch = "PlayBatch";
    public static final String FillerPLayGAp = "FillerPlayGap";
    public static final String DefaultStartTime = "DefaultStartTime";
    public static final String DefaultEndTime = "DefaultEndTime";
    public static final String DeleteAll = "DeleteAll";

    public static final String Video_DwlDate = "DownloadingDate";
    public static final String Video_DriveId = "DriveId";
    public static final String Video_Server_Id = "VideoServerId";
    public static final String VideoStatus = "VideoStatus"; //ready - Ready to play ,
                                                            // completed_pend - Completed daily impression server update may pending,
                                                            // completed_completed - Updated to servr
                                                            // closed-pend - Campaign finished, server update pending
                                                            // closed-completed - Completed and updated in server,ready to delete
    public static final String EndDate = "EndDate";
    public static final String TotalImpression = "TotalImpression";
    public static final String StatusOfDays = "StatusofDays";

    public static final String Impressions = "Impressions";
    public static final String VideoSize = "VideoSize";

    public static final String Video_LastPlayed = "LastPlayed";
    public static final String Video_NoOfTimesPlayed = "NoOfTimesPlayed";
    public static final String Video_PlayedPercent = "PercentPlayed";
    public static final String PlayErrorCount = "PlayErrorCount";
    public static final int qryImprsns = 0;
    public static final String SHOW_TIME_LIST = "ShowTimeList";
    public static final String HIST_PLAY_LIST = "PlayTimeList";
    public static final String Type = "Type";
    public static final String Setting_Name = "SettingNAme";
    public static final String StartTime = "StartTime";
    public static final String End_Time = "EndTime";
    public static final String Advt_PlayTme = "AdvtPlayTime";
    public static final String Rem_imp = "Remaining_Impressions";

    public static final String VideoName = "VideoName";
    public static final String PlayedTimeMills = "PalyedTimeMills";
    public static final String PlayedDate = "PlayedDate";
    public static final String VideoIDServ = "VideoIDServ";
    public static final String CampaignID = "CampaignID";
    public static final String TotalNumImpression  = "TotalImpression";
    public static final String RemainingImpression  = "RemImpression";
    public static final String TodaysImpression  = "TodaysImpression";
    public static final String PlayedToday  = "PlayedToday";
    public static final String PercentPlayed  = "PercentPlayed";

    public static final String SENT_EMAIL = "sent_mail";
    public static final String VideoName2 = "VideoName2";
    public static final String PlayedTimeMills2 = "PalyedTimeMills2";
    public static final String PlayedDate2 = "PlayedDate2";
    public static final String VideoIDServ2 = "VideoIDServ2";
    public static final String CampaignID2 = "CampaignID2";
    public static final String TotalNumImpression2  = "TotalImpression2";
    public static final String RemainingImpression2  = "RemImpression2";
    public static final String TodaysImpression2  = "TodaysImpression2";
    public static final String PlayedToday2  = "PlayedToday2";
    public static final String PercentPlayed2  = "PercentPlayed2";
    public static final String EmailStatus2  = "EmailStatus";


    private SQLiteDatabase database;
    Context context;

    private static final String CREATE_TABLE_FILE_LIST = "CREATE TABLE "
            + Advertisement_List + "(" + _Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + Alloc_Id + " INTEGER," + Campaign_Id + " INTEGER," + Video_Server_Id + " TEXT," + Impressions + " INTEGER ," + TotalImpression + " INTEGER," + Rem_imp + " INTEGER ," + Video_PlayedPercent + " REAL DEFAULT 0.0 ," + VideoSize + " INTEGER," + PlayErrorCount + " INTEGER DEFAULT 0," + Video_Name
            + " TEXT," + Video_DriveId + " TEXT," + Video_DwlDate + " TEXT," + VideoStatus + " TEXT," + EndDate + " TEXT," + StatusOfDays + " TEXT," + Video_NoOfTimesPlayed + " INTEGER ," + Video_LastPlayed + " DATE," + Video_Extn
            + " TEXT" + " )";
    private static final String CREATE_DEVICE_SETTINGS = "CREATE TABLE " + DEVICE_SETTINGS + "(" + _Id + " INTEGER PRIMARY KEY ," + Device_id + " INTEGER ," + Settings_id + " INTEGER," + Settings_Version + " INTEGER," + PlayBatch + " INTEGER," + FillerPLayGAp + " INTEGER," + DefaultStartTime + " INTEGER," + DefaultEndTime + " INTEGER," + DeleteAll + " INTEGER," + Advt_PlayTme + " DOUBLE" + ")";
    private static final String CREATE_SHOW_TIME_LIST = "CREATE TABLE " + SHOW_TIME_LIST + "(" + _Id + " INTEGER," + Setting_Name + " TEXT," + Type + " INTEGER," + Device_id + " TEXT," + StartTime + " REAL," + End_Time + " REAL," + PlayBatch + " INTEGER," + FillerPLayGAp + " INTEGER," + " FOREIGN KEY (" + _Id + ") REFERENCES " + DEVICE_SETTINGS + "(" + _Id + ")" + ")";
    private static final String CREATE_PLAY_HIST_LIST = "CREATE TABLE " + HIST_PLAY_LIST + "(" + _Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + VideoName + " TEXT," + PlayedTimeMills + " INTEGER," + PlayedDate + " TEXT," + VideoIDServ + " TEXT," + CampaignID + " INTEGER,"+ TotalNumImpression + " INTEGER,"+ RemainingImpression +" INTEGER," + TodaysImpression +" INTEGER," + PlayedToday +" INTEGER,"+ PercentPlayed+" REAL DEFAULT 0.0 )";
    private static final String CREATE_EMAIL = "CREATE TABLE " + SENT_EMAIL + "(" + _Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + VideoName2 + " TEXT," + PlayedTimeMills2 + " INTEGER," + PlayedDate2 + " TEXT," + VideoIDServ2 + " TEXT," + CampaignID2 + " INTEGER,"+ TotalNumImpression2 + " INTEGER,"+ RemainingImpression2 +" INTEGER," + TodaysImpression2 +" INTEGER," + PlayedToday2 +" INTEGER,"+ EmailStatus2+" INTEGER DEFAULT 0)";



    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public DataBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FILE_LIST);
        db.execSQL(CREATE_DEVICE_SETTINGS);
        db.execSQL(CREATE_SHOW_TIME_LIST);
        db.execSQL(CREATE_PLAY_HIST_LIST);
        db.execSQL(CREATE_EMAIL);
        Log.d("db", "advtvdo list created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
           // db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_FILE_LIST);
           // db.execSQL("DROP TABLE IF EXISTS " + CREATE_DEVICE_SETTINGS);
          //  db.execSQL("DROP TABLE IF EXISTS " + CREATE_SHOW_TIME_LIST);
            if ((oldVersion == 1) && (newVersion > 1)) {
                db.execSQL(CREATE_PLAY_HIST_LIST);
                db.execSQL(CREATE_EMAIL);
            }
            onCreate(db);
        }catch(Exception exp){
            LogWriter.writeLogException("Update",exp);
        }

    }

    public void close() {
        if (database != null) database.close();
    }

    public long insertAdvtPlayEntry(VideoDetailsModel videoDetailsModel) {
        long id;
        if (database != null) database.close();
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Alloc_Id, videoDetailsModel.getVidMapId());
        values.put(Campaign_Id, videoDetailsModel.getCampaignId());
        values.put(Video_Name, videoDetailsModel.getVideo_Name());
        values.put(Video_DriveId, videoDetailsModel.getVideo_DriveId());
        values.put(Video_DwlDate, videoDetailsModel.getVideo_DwlDate());
        values.put(Video_Extn, videoDetailsModel.getVideo_Extn());
        values.put(Video_Server_Id, videoDetailsModel.getVideo_ServerId());
        values.put(Impressions, videoDetailsModel.getImpressions());
        values.put(VideoSize, videoDetailsModel.getSize());
        values.put(EndDate, videoDetailsModel.getEndDate());
        values.put(TotalImpression, videoDetailsModel.getTotalImpression());
        values.put(Rem_imp, videoDetailsModel.getRemImpressions());
        values.put(StatusOfDays, videoDetailsModel.getStatusOfDays());
        values.put(Video_LastPlayed, videoDetailsModel.getVideo_LastPlayed());
      //  if (videoDetailsModel.getStatus() == "1")
            values.put(VideoStatus, "ready");
        values.put(Video_NoOfTimesPlayed, videoDetailsModel.getVideo_NoOfTimesPlayed());
        values.put(Video_PlayedPercent, 0.0);

        id = database.insert(Advertisement_List, null, values);
        database.close();
        logAdvtStatus();
        return id;
    }


    public long insertAdvtPlayHistEntry( Context con,String videoName,int campaignID,int videoServID,int totalImp,int remImp,int numPlayed, float percent) {
        try {
            long id;
            if (database != null) database.close();
            database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            ContentValues values2 = new ContentValues();
            values.put(VideoName, videoName);
            values.put(CampaignID, campaignID);
            values.put(VideoIDServ, videoServID);
            long adjustedDevTimeMills = Utils.getAdjustedTimeMills(con);
            Date date = new Date();
            date.setTime(adjustedDevTimeMills);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            values.put(PlayedTimeMills, adjustedDevTimeMills);
            values.put(PlayedDate, format.format(date));
            values.put(TotalNumImpression, totalImp);
            values.put(RemainingImpression, remImp);
            values.put(PlayedToday, numPlayed);
            values.put(PercentPlayed, percent);

            values2.put(VideoName2, videoName);
            values2.put(CampaignID2, campaignID);
            values2.put(VideoIDServ2, videoServID);
            values2.put(PlayedTimeMills2, adjustedDevTimeMills);
            values2.put(PlayedDate2, format.format(date));
            values2.put(TotalNumImpression2, totalImp);
            values2.put(RemainingImpression2, remImp);
            values2.put(PlayedToday2, numPlayed);
            values2.put(EmailStatus2,0);

            id = database.insert(HIST_PLAY_LIST, null, values);
            database.insert(SENT_EMAIL, null, values2);
            database.close();

            return id;
        }catch(Exception exp){
            return -1;
        }
    }

    public boolean deleteByCampainID(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        boolean status = database.delete(Advertisement_List, Campaign_Id + "=" + id, null) > 0;
        database.close();

        return status;
    }

    public boolean closeByRowId(int id,ContentValues cv) {
        SQLiteDatabase database = this.getWritableDatabase();
        LogWriter.writeLogDailyPlayAlloc("Cloded-pend","Marking closed-pend for ->"+id);
        cv.put(VideoStatus,"closed-pend");
        boolean status = database.update(Advertisement_List, cv,_Id + "=" + id,null)>0;
        database.close();

        return status;
    }

    public boolean closeAfterUpdate(int _idVal){
        ContentValues cv = new ContentValues();
        LogWriter.writeLogDailyPlayAlloc("closed-complete","Marking closed-completed for ->"+_idVal);
        cv.put(VideoStatus, "closed-completed");
        SQLiteDatabase database = this.getWritableDatabase();
        int affecteRows= database.update(Advertisement_List, cv,_Id + "=" + _idVal,null);
        LogWriter.writeLogDailyPlayAlloc("closed-complete","Marking closed-completed affected rows->"+affecteRows);
        database.close();
        return affecteRows>0;
    }

    public boolean setStatusDayNA(){
        ContentValues cv = new ContentValues();

        cv.put(StatusOfDays, "NA");
        SQLiteDatabase database = this.getWritableDatabase();
        boolean status = database.update(Advertisement_List, cv,_Id + "> 0" ,null)>0;
        database.close();
        return status;
    }

    public boolean completeAfterUpdate(int _idVal){
        ContentValues cv = new ContentValues();
        LogWriter.writeLogDailyPlayAlloc("Complted-complete","Marking Complted-complete for ->"+_idVal);
        cv.put(VideoStatus, "completed_completed");
        SQLiteDatabase database = this.getWritableDatabase();
        int numRec  = database.update(Advertisement_List, cv,_Id + "=" + _idVal,null);
        LogWriter.writeLogDailyPlayAlloc("Complted-complete","Marking Complted-complete affected rows"+numRec);
        database.close();
        return numRec>0;
    }
    //todo BMJO excluded deleted
    public Cursor checkDailyImpression(long adjustedTimeMills) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(adjustedTimeMills);
            LogWriter.writeLogPlayAlloc("allocateDailyImp", "Check for pending allocation for day->" + dateString);
            Utils.sendLogToServer("Allocate daily->Check for pending allocation for day->" + dateString, CommonDataArea.mainActivity);
            try {
                //String selectQuery = "SELECT  * FROM " + Advertisement_List + " where " + StatusOfDays + " not like '" + dateString + "%'";// and " + VideoStatus + " not like 'closed%'";
                String selectQuery = "SELECT  * FROM " + Advertisement_List + " where " + StatusOfDays + " not like '" + dateString + "%' and " + VideoStatus + " not like 'closed%'";
                if (database != null) database.close();
                database = this.getReadableDatabase();
                Cursor cursor = database.rawQuery(selectQuery, null);
                if (cursor.getCount() > 0) {
                    LogWriter.writeLogPlayAlloc("allocateDailyImp", "Found records");
                }
                return cursor;
            } catch (Exception exp) {
                return null;
            }
        }catch(Exception exp){
            return null;
        }
    }

    public void updateDailyImpression(int _id, String statusDay, int dailyImp, int rem, String status, Double percentage) {
        try {
            if ((database != null) && (database.isOpen())) {
                database.close();
                database = null;
            }
            database = this.getWritableDatabase();
            ContentValues cv = getAdvtValue(_id);
            // database.beginTransaction();
            // ContentValues cv = new ContentValues();
            cv.put(StatusOfDays, statusDay);
            cv.put(Impressions, dailyImp);
            cv.put(VideoStatus, status);
            cv.put(Video_PlayedPercent, percentage);
            cv.put(Video_NoOfTimesPlayed, 0);
            // cv.put(TotalImpression, rem);
            long ret = database.replace(Advertisement_List, null, cv);
            //int idhj = database.updateWithOnConflict(Advertisement_List, cv, "_id =" + _id, null,SQLiteDatabase.CONFLICT_IGNORE);
            if (ret != -1) {
                LogWriter.writeLogDailyPlayAlloc("updateDailyImpression", "Update success");
                Utils.sendLogToServer("Allocate daily- Updating Daily Impression -->Success", CommonDataArea.mainActivity);
            } else {
                LogWriter.writeLogDailyPlayAlloc("updateDailyImpression", "Update failed");
                Utils.sendLogToServer("Allocate daily- Updating Daily Impression -->Failed", CommonDataArea.mainActivity);
            }
            database.close();
        } catch (Exception exp) {
            LogWriter.writeLogException("UpdateDailyImpression", exp);
            Log.e("Error", exp.getMessage());
        }
    }

    public boolean deleteAllVids() {
        if (database != null) database.close();
        database = this.getWritableDatabase();
        if (database.delete(Advertisement_List, null, null) > 0) {
            database.close();
            return true;
        }
        database.close();
        return false;
    }

    public Cursor getAdvtsReport() {
        try {
            String selectQuery = "SELECT  * FROM " + Advertisement_List+ " where " + VideoStatus +" not like 'closed%'";
            //    if (database != null) database.close();
            database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            return cursor;
        } catch (Exception exp) {
            return null;
        }

    }

    public Cursor getCompletedFilesListToUpdate() {
        try {
            String selectQuery = "SELECT  * FROM " + Advertisement_List + " where VideoStatus like 'closed-pend%'";
            if (database != null) database.close();
            database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            return cursor;
        } catch (Exception exp) {
            return null;
        }
    }

    public Cursor getClosedFilesListToUpdate() {
        try {
            String selectQuery = "SELECT  * FROM " + Advertisement_List + " where VideoStatus like 'closed-pend%'";
            if (database != null) database.close();
            database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            return cursor;
        } catch (Exception exp) {
            return null;
        }
    }

    public boolean canDeleteFile(String fileDriveID) {
        try {
            String selectQuery = "SELECT  * FROM " + Advertisement_List + " where VideoStatus not like 'closed%' and " + Video_DriveId + " like '%" + fileDriveID + "%'";
            if (database != null) database.close();
            database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            if ((cursor != null) && (cursor.getCount() > 0)) return false;
            else return true;
        } catch (Exception exp) {
            return false;
        }
    }


    public Cursor getReadyFilesListToUpdate() {
        try {
            String selectQuery = "SELECT  * FROM " + Advertisement_List + " where VideoStatus like 'ready%'";
            if (database != null) database.close();
            database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            return cursor;
        } catch (Exception exp) {
            return null;
        }
    }

    public Cursor getCompletedPendFilesListToUpdate() {
        try {
            String selectQuery = "SELECT  * FROM " + Advertisement_List + " where VideoStatus like 'completed_pend%'";
            if (database != null) database.close();
            database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            return cursor;
        } catch (Exception exp) {
            return null;
        }
    }

    public boolean checkAlreadyDownloaded(int mapID) {
        if (database != null) database.close();
        String selectRecordQuery = "SELECT  * FROM Advertisement_Video_List WHERE  mapId =" + mapID;
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectRecordQuery, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            database.close();
            return true;
        } else {
            database.close();
            return false;
        }

    }

    public boolean checkAlreadyDownloadedToday(int mapID) {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        if (database != null) database.close();

        String selectRecordQuery = "SELECT  * FROM Advertisement_Video_List WHERE  mapId = " + mapID;
        // String selectRecordQuery ="SELECT  * FROM Advertisement_Video_List WHERE  mapId = " +mapID +" AND DownloadingDate like "+dateString+"%";
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectRecordQuery, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            if (cursor.moveToFirst()) {
                do {

                    String sdfhdfgh = cursor.getString(cursor.getColumnIndex("mapId"));
                    String dateeee = cursor.getString(cursor.getColumnIndex("DownloadingDate"));
                    Log.d("mapid", sdfhdfgh);
                    Log.d("date", dateeee);
                    database.close();
                }
                while (cursor.moveToNext());

            }
            database.close();
            Utils.sendLogToServer("Download->Already downloaded", context);
            return true;
        } else {
            database.close();
            return false;
        }

    }

    public Cursor logAdvtStatus() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        String selectRecordQuery = "SELECT  * FROM " + HIST_PLAY_LIST ;//+ " where VideoStatus not like 'closed%' and PlayErrorCount<5 and PercentPlayed <100 ORDER by PercentPlayed ASC";
        //   String selectRecordQuery = "SELECT  * FROM " + Advertisement_List;// + " WHERE _id = " + id ;//+" AND "+ Video_DwlDate+" like '%"+dateString+"%'";
        if ((database != null) && (database.isOpen())) database.close();
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectRecordQuery, null);
        if (cursor == null)
            if (cursor.getCount() > 0) LogWriter.writeLog("AfterUpdate", "found");
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                int timesP = cursor.getInt(cursor.getColumnIndex(Video_NoOfTimesPlayed));
                int imp = cursor.getInt(cursor.getColumnIndex(Impressions));
                float percent = cursor.getFloat(cursor.getColumnIndex(Video_PlayedPercent));
                LogWriter.writeLog("AfterUpdate", "ID->" + _id + " :AllocID->" + cursor.getInt(cursor.getColumnIndex(Alloc_Id)) + " :NumPLayed->" + timesP + " :Imp->" + imp + " :Percent->" + percent + " VidName->" + cursor.getString(cursor.getColumnIndex(Video_Name)));
            } while (cursor.moveToNext());
        }
        return cursor;
    }


    public void logPlayHistory() {
        try {
            int row = 0;
            String fileData="";
            String selectRecordQuery = "SELECT  * FROM " + HIST_PLAY_LIST;//+ " where VideoStatus like 'ready%' and PlayErrorCount<5 and PercentPlayed <100 ORDER by PercentPlayed ASC";
            if ((database != null) && (database.isOpen())) database.close();
            database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectRecordQuery, null);
            int columns = cursor.getColumnCount();
            for (int i = 0; i < columns; ++i) {

                fileData+= ("\"" + cursor.getColumnName(i) + "\"," );

            }
            fileData+="\r\n";
            LogWriter.writeCSV(fileData);
            cursor.moveToFirst();
            if(cursor.getCount()>0) {
                do {
                    for (int i = 0; i < columns; ++i) {
                        if(i>0)fileData+=",";
                        fileData+=  ("\""+cursor.getString(i)+"\"");
                    }
                    fileData+="\r\n";
                    LogWriter.writeCSV(fileData);
                } while (cursor.moveToNext());
            }
        }catch(Exception exp){
            LogWriter.writeLogException("Table contenst Display",exp);
        }
    }

    public ContentValues getAdvtValue(int id) {
        String selectQuery = "SELECT  * FROM " + Advertisement_List + " where _id =" + id;
        if (database != null) database.close();
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int columns = cursor.getColumnCount();
        ContentValues cv = new ContentValues();
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            for (int i = 0; i < columns; ++i) {
                cv.put(cursor.getColumnName(i), cursor.getString(i));
            }
            return cv;
        }
        return null;
    }

    public void cursorDisplyAdvtTable(int logTo) {
        try {
            int row = 0;
            String selectRecordQuery = "SELECT  * FROM " + Advertisement_List;//+ " where VideoStatus like 'ready%' and PlayErrorCount<5 and PercentPlayed <100 ORDER by PercentPlayed ASC";
            if ((database != null) && (database.isOpen())) database.close();
            database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectRecordQuery, null);
            int columns = cursor.getColumnCount();
            cursor.moveToFirst();
            if(cursor.getCount()>0) {
                do {
                    for (int i = 0; i < columns; ++i) {
                        if (logTo == 1) {
                            LogWriter.writeLogPlayAlloc("Record->" + row, "Column Name->" + cursor.getColumnName(i) + " = " + cursor.getString(i));
                        } else
                            LogWriter.writeLogPlay("Record->" + row, "Column Name->" + cursor.getColumnName(i) + " = " + cursor.getString(i));

                    }
                } while (cursor.moveToNext());
            }
        }catch(Exception exp){
            LogWriter.writeLogException("Table contenst Display",exp);
        }
    }

    public Cursor getVideoToPlay() {
        try {

            if (CommonDataArea.timeStatus != CommonDataArea.TIMESTATUS_DEF_PLAY) {
                LogWriter.writeLogPlay("getVideoToPlay", "Normal play");
                String selectRecordQuery = "SELECT  * FROM " + Advertisement_List + " where VideoStatus like 'ready%' and PlayErrorCount<5 and PercentPlayed <100 ORDER by PercentPlayed ASC";
                if ((database != null) && (database.isOpen())) database.close();
                database = this.getReadableDatabase();
                Cursor cursor = database.rawQuery(selectRecordQuery, null);
                if (cursor.getCount() == 0) {
                    LogWriter.writeLogPlay("getVideoToPlay", "No Vidio to play");
                    cursorDisplyAdvtTable(2);
                }
                return cursor;
            } else {

                LogWriter.writeLogPlay("getVideoToPlay", "Default play");
                String selectRecordQuery = "SELECT  * FROM " + Advertisement_List + " where VideoStatus not like 'closed%' and Remaining_Impressions >0  ORDER by PercentPlayed ASC";
                if ((database != null) && (database.isOpen())) database.close();
                database = this.getReadableDatabase();
                Cursor cursor = database.rawQuery(selectRecordQuery, null);
                if (cursor.getCount() == 0) {
                    LogWriter.writeLogPlay("getVideoToPlay", "No Vidio to play");
                    cursorDisplyAdvtTable(2);
                }
                return cursor;
            }
        } catch (Exception exp) {
            LogWriter.writeLogException("getVideoToPly", exp);
            return null;
        }

    }

    public void updateTable(int _id, int impressions, String lastPlayed, int numTimesPLayed, int playErrorCount, int remimp) {
        //reduce played impression from reamining impression;
        int rem = remimp - 1;
        try {
            float percentPlayed = 0;
            if ((impressions > 0) && (numTimesPLayed > 0)) {
                percentPlayed = (float) (((float) numTimesPLayed / (float) impressions) * (float) 100);
            } else {
                if (impressions < 0) impressions = 1;
                if (numTimesPLayed <= 0) numTimesPLayed = 1;
            }
            if ((database != null) && (database.isOpen())) database.close();
            database = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            //ContentValues cv = getAdvtValue(_id);
            cv.put(Impressions, impressions);
            cv.put(Video_LastPlayed, lastPlayed);
            cv.put(Video_NoOfTimesPlayed, numTimesPLayed);
            cv.put(Video_PlayedPercent, percentPlayed);
            cv.put(PlayErrorCount, playErrorCount);
            cv.put(Rem_imp, rem);
            if (percentPlayed >= 100) cv.put(VideoStatus, "completed-pend");

            int idhj = database.update(Advertisement_List, cv, "_id =" + _id, null);
            database.close();
            cursorDisplyAdvtTable(2);
        } catch (Exception exp) {

        }
    }

    public Cursor getAllFileList() {
        try {
            String selectQuery = "SELECT  * FROM " + Advertisement_List;
            if (database != null) database.close();
            database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            return cursor;
        } catch (Exception exp) {
            return null;
        }
    }

    public void clearAdvtTable() {
        database = this.getWritableDatabase();
        database.delete(Advertisement_List, null, null);
    }

    public void check() {
        // if (!BuildConfig.DEBUG)  return;;
        String selectRecordQuery1 = "SELECT  * FROM Advertisement_Video_List ";
        // String selectRecordQuery1 ="SELECT  * FROM Advertisement_Video_List ";
        database = this.getReadableDatabase();
        Cursor cursor1 = database.rawQuery(selectRecordQuery1, null);
        // Cursor cursor1 = selectRecordQuery1;
        if ((cursor1 != null) && (cursor1.getCount() > 0)) {
            if (cursor1.moveToFirst()) {
                do {

                    String sdfhdfgh = cursor1.getString(cursor1.getColumnIndex("mapId"));
                    String dateeee = cursor1.getString(cursor1.getColumnIndex("DownloadingDate"));
                    int totalimpression = cursor1.getInt(cursor1.getColumnIndex(TotalImpression));
                    int remimpressions = cursor1.getInt(cursor1.getColumnIndex(Rem_imp));
                    int impressions = cursor1.getInt(cursor1.getColumnIndex(Impressions));
                    int noof = cursor1.getInt(cursor1.getColumnIndex(Video_NoOfTimesPlayed));

                    int per = cursor1.getInt(cursor1.getColumnIndex(Video_PlayedPercent));
                    String Enddate = cursor1.getString(cursor1.getColumnIndex(EndDate));
                    String statustoda = cursor1.getString(cursor1.getColumnIndex(StatusOfDays));
                    LogWriter.writeLog("mapid " + sdfhdfgh + " EndDate " + Enddate + " total impression " + String.valueOf(totalimpression) + " Remaining impression " + String.valueOf(remimpressions) + " days of status " + statustoda, "checking all datas in table ");
                    Utils.sendLogToServer("check function--> mapid " + sdfhdfgh + " EndDate " + Enddate + " total impression " + String.valueOf(totalimpression) + " Remaining impression " + String.valueOf(remimpressions) + " days of status " + statustoda, context);
                    Log.d("mapid", sdfhdfgh);
                    Log.d("date", dateeee);
                    Log.d("totalimpression", String.valueOf(totalimpression));
                    Log.d("remainingimpression", String.valueOf(remimpressions));
                    Log.d("impressions", String.valueOf(impressions));
                    Log.d("nooftimes", String.valueOf(noof));
                    Log.d("Enddate", String.valueOf(Enddate));
                    Log.d("statustoda", String.valueOf(statustoda));
                    Log.d("perce", String.valueOf(per));

                }
                while (cursor1.moveToNext());
                cursor1.close();
            }


        }
    }
    public void    getallHistory(String Date){

        SQLiteDatabase db=this.getReadableDatabase();
        // final String MY_QUERY = "SELECT * FROM PlayTimeList a INNER JOIN Advertisement_Video_List b ON a.CampaignID=b.Alloc_Id WHERE a.PlayedDate =?";
        String rawQuery = "SELECT * FROM " + HIST_PLAY_LIST + " s" +" JOIN " + Advertisement_List+ " p"
                + " ON s." + CampaignID+ " =p. " + Alloc_Id
                + " WHERE s." + PlayedDate + " = '" +  Date+"'";

        Cursor cursor=db.rawQuery(rawQuery,null);
        File sd= new File(Environment.getExternalStorageDirectory().toString() + "/PTV");
        String csvFile="LogData.xls";
        File directory=new File(sd.getAbsolutePath());
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {
            File file=new File(directory,csvFile);
            WorkbookSettings wbSettings=new WorkbookSettings();
            wbSettings.setLocale(new Locale("en","EN"));
            WritableWorkbook workbook;
            workbook= Workbook.createWorkbook(file,wbSettings);
            WritableSheet sheet=workbook.createSheet("LogData",0);
            sheet.addCell(new Label(0,0,"ID"));
            sheet.addCell(new Label(1,0,"VideoName"));
            sheet.addCell(new Label(2,0,"PlayedTime"));
            sheet.addCell(new Label(3,0,"PlayedDate"));
            sheet.addCell(new Label(4,0,"VideoIDServ"));
            sheet.addCell(new Label(5,0,"CampaignID"));
            sheet.addCell(new Label(6,0,"TotalNumImpression"));
            sheet.addCell(new Label(7,0,"RemainingImpression"));
            sheet.addCell(new Label(8,0,"TodaysImpression"));
            sheet.addCell(new Label(9,0,"PlayedToday"));
            sheet.addCell(new Label(10,0,"PercentPlayed"));
            sheet.addCell(new Label(11,0,"TotalPlayed"));
            sheet.addCell(new Label(12,0,"EndDate"));

            if ((cursor != null) && (cursor.getCount() > 0)) {
                if (cursor.moveToFirst()) {
                    do {
                        String id =String.valueOf(cursor.getInt(0)) ;
                        String videoname = cursor.getString(1);
                        long PlayedTimeMills = cursor.getLong(2);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                        // Create a calendar object that will convert the date and time value in milliseconds to date.
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(PlayedTimeMills);
                        String hms= formatter.format(calendar.getTime()).substring(10);
                        String PlayedDate = cursor.getString(3);
                        String VideoIDServ = cursor.getString(4);
                        String CampaignID = String.valueOf(cursor.getInt(5));
                        String TotalNumImpression =String.valueOf(cursor.getInt(6));
                        String RemainingImpression = String.valueOf(cursor.getInt(7));
                        String TotalPlayed=String.valueOf(cursor.getInt(27));
                        String TodaysImpression = String.valueOf(cursor.getInt(8));
                        String PlayedToday = String.valueOf(cursor.getInt(9));
                        String PercentPlayed = String.valueOf(cursor.getInt(10));
                        String EndDate= cursor.getString(25);
                        String extension= cursor.getString(29);

                        int i = cursor.getPosition() + 1;
                        sheet.addCell(new Label(0, i, id));
                        sheet.addCell(new Label(1, i, videoname+"."+extension));
                        sheet.addCell(new Label(2, i, hms));
                        sheet.addCell(new Label(3, i, PlayedDate));
                        sheet.addCell(new Label(4, i, VideoIDServ));
                        sheet.addCell(new Label(5, i, CampaignID));
                        sheet.addCell(new Label(6, i, TotalNumImpression));
                        sheet.addCell(new Label(7, i, RemainingImpression));
                        sheet.addCell(new Label(8, i, TodaysImpression));
                        sheet.addCell(new Label(9, i, PlayedToday));
                        sheet.addCell(new Label(10, i, PercentPlayed));
                        sheet.addCell(new Label(11, i, TotalPlayed));
                        sheet.addCell(new Label(12, i, EndDate));

                    } while (cursor.moveToNext());

                }
                cursor.close();
                workbook.write();
                workbook.close();
            }
            else {
                Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                Log.d("N_","nodata");}
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }
    public Cursor sentEmail(String date){

        SQLiteDatabase db=this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + HIST_PLAY_LIST + " s" +" JOIN " + Advertisement_List+ " p"
                + " ON s." + CampaignID+ " =p. " + Alloc_Id
                + " WHERE s." + PlayedDate + " = '" +  date+"'";

        Cursor cursor=db.rawQuery(rawQuery,null);
        return cursor;
    }
    public Cursor  CheckEmail(){
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String todayString = formatter.format(todayDate);

        SQLiteDatabase db=this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + SENT_EMAIL + " s" +" JOIN " + Advertisement_List+ " p"
                + " ON s." + CampaignID2+ " =p. " + Alloc_Id
                + " WHERE s." + EmailStatus2 + " = '" +  0 +"'" +"AND s." + PlayedDate2 + " NOT LIKE "+ "'%"+todayString+"%'";

        Cursor cursor=db.rawQuery(rawQuery,null);


        return cursor;
    }
    public void updateData(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmailStatus2,1);
        // db.update(SENT_EMAIL, contentValues, PlayedDate2 + " = " +String.valueOf(date),null);
        db.update(SENT_EMAIL, contentValues, "PlayedDate2 =? " , new String[] { date });
        db.close();

    }
    public Cursor CheckAdvt(){
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);
        SQLiteDatabase db=this.getReadableDatabase();
        String querry=" SELECT * FROM "+ Advertisement_List  ;
        Cursor cursor=db.rawQuery(querry,null);
        return cursor;
    }
    public void DeleteAllCampaigns(){
        SQLiteDatabase db = this.getWritableDatabase();
        String rawQuery = "SELECT * FROM " + Advertisement_List  ;

        Cursor cursor=db.rawQuery(rawQuery,null);

        if ((cursor != null) && (cursor.getCount() > 0)) {
            if (cursor.moveToFirst()) {
                do {
                    String id = String.valueOf(cursor.getInt(0));
                    String mapId = String.valueOf(cursor.getInt(1));
                    String campaignId = String.valueOf(cursor.getInt(2));
                    Date todayDate = Calendar.getInstance().getTime();

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    String todayString = formatter.format(todayDate);
                    todayString = todayString.replace(" ", "");
                    String deviceuuid=CommonDataArea.uuid;
                    String commenPath = "http://publictvads.in/modified/webservicelive/update_campaign_status.php?campaignid="+campaignId+"&mapid="+mapId+ "&deviceid="+CommonDataArea.uuid+"&campaignStatus="+1+ "&timestamp="+todayString;
                    Log.d("zzz",commenPath);
                    ArrayList<String> camp = new ArrayList<String>();
                    camp.add(commenPath);
                    Log.d("aaaa",String.valueOf(camp));


                    //db.execSQL("delete from "+ Advertisement_List +" WHERE " + _Id + "=" + id );
                    int hh=db.delete(Advertisement_List, _Id + "=" +id, null);
                    Log.d("qqqqq",String.valueOf(hh));
                    if(hh==1 && campaignId!=null && mapId !=null && deviceuuid!=null &&  todayString !=null){
                        CampaignDetails cam=new CampaignDetails();
                        cam.execute(camp);
                    }
                }
                while (cursor.moveToNext());
            }}


    }

    public class CampaignDetails extends AsyncTask<ArrayList<String>, Void, Void> {


        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> camp = arrayLists[0];


            String commenPath = camp.get(0);
            System.out.print(commenPath);
            Log.d("url",commenPath);

            try{
                URL uRl = new URL(commenPath);
                HttpURLConnection httpURLConnection = (HttpURLConnection) uRl.openConnection();
                httpURLConnection.connect();
                httpURLConnection.getResponseCode();
                System.out.print("Response="+httpURLConnection.getResponseCode());
                Log.d("ssss","Response="+httpURLConnection.getResponseCode());
                //BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            }
            catch (Exception e){e.printStackTrace();}
            return null;
        }
    }
}


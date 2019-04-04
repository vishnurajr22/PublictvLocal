package com.tracking.m2comsys.adapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tracking.m2comsys.adapplication.utils.VideoDetailsModel;

/**
 * Created by m2 on 28/7/17.
 */

public class AdHistory extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AdvertisementDetails";
    private static final String TABLE_NAME = "AdHistory";
    private static final String Video_Id = "_id";

    private static final String Video_DwlDate = "DownloadingDate";
    private static final String Video_LastPlayed = "LastPlayed";
    private static final String Video_NoOfTimesPlayed = "NoOfTimesPlayed";

    private static final String Video_Server_Id = "VideoServerId";
    private static final String Active_Video = "ActiveVideo";
    //private static final String Create_Table = "Create table " + TABLE_NAME + "(" + Video_Id + " Integer ," + Video_Server_Id + " Integer ," + Video_LastPlayed + " DATE ," + Video_NoOfTimesPlayed + " Integer " + ")";
    private static final String Create_Table = "CREATE TABLE "
            + TABLE_NAME + "(" + Video_Id + " INTEGER PRIMARY KEY," + Video_Server_Id + "INTEGER," + Video_LastPlayed
            + " TEXT," + Video_NoOfTimesPlayed + " INTEGER," + Video_DwlDate + " DATE" + " )";
    public AdHistory(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Create_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Create_Table);
        onCreate(sqLiteDatabase);

    }

    public long insertDetaild(VideoDetailsModel detailsModel) {
        long id;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Video_LastPlayed, detailsModel.getVideo_LastPlayed());
        values.put(Video_DwlDate, detailsModel.getVideo_DwlDate());
        values.put(Video_NoOfTimesPlayed, detailsModel.getVideo_NoOfTimesPlayed());
        values.put(Video_Server_Id, detailsModel.getVideo_Server_Id());

        id = database.insert(TABLE_NAME, null, values);
        database.close();
        return id;
    }


}

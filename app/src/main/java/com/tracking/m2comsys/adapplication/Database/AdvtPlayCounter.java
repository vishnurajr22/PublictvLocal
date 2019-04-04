package com.tracking.m2comsys.adapplication.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by m2 on 31/7/17.
 */

public class AdvtPlayCounter extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AdvertisementDetails";
    private static final String TABLE_NAME = "AdvtCounterPLay";
    private static final String Video_Id = "_id";
    private static final String Video_ServerId = "ServerId";
    private static final String Req_Impressions = "RequiredImpressions";
    private static final String Played_Impressions = "PlayedImpressions";
    private static final String Error_Count = "ErrorCount";
    private static final String Create_Table ="Create table" +TABLE_NAME+"("+Video_Id+" Integer ,"+Video_ServerId+" Integer ,"+Req_Impressions+" Integer ,"+Error_Count+" Integer ,"+Played_Impressions+" Integer "+")";
    public AdvtPlayCounter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
}

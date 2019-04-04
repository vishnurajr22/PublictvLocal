package com.tracking.m2comsys.adapplication.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tracking.m2comsys.adapplication.data.local.LocalDataSource;
import com.tracking.m2comsys.adapplication.data.remote.RemoteDataSource;

/**
 * Created by tikson on 24/1/18.
 */

public class Injection {
    public static DataSource provideRepositiry(@NonNull Context context) {
        return Repository.getInstance(RemoteDataSource.getInstance(), LocalDataSource.getInstance(context));
    }
}

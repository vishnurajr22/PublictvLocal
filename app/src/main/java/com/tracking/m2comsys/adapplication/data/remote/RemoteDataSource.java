package com.tracking.m2comsys.adapplication.data.remote;

import com.tracking.m2comsys.adapplication.data.DataSource;

/**
 * Created by tikson on 7/5/18.
 */

public class RemoteDataSource implements DataSource {
    private static RemoteDataSource remoteDataSource;
    private static RemoteDataSource INSTANCE = null;

    private RemoteDataSource() {
    }


    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void checkVersionIntialization(CallbackResult callback) {

    }

    @Override
    public void intializeVersion() {

    }

    @Override
    public void checkVersionChange(int version, CallbackResult callback) {

    }
}

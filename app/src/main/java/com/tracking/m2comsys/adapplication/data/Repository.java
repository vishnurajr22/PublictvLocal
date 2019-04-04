package com.tracking.m2comsys.adapplication.data;

/**
 * Created by tikson on 7/5/18.
 */

public class Repository implements DataSource {
    private final DataSource mRemoteDataSource;
    private final DataSource mLocalDataSource;
    private static Repository INSTANCE;

    private Repository(DataSource mRemoteDataSource, DataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
        this.mRemoteDataSource = mRemoteDataSource;
    }

    public static Repository getInstance(DataSource remoteDataSource, DataSource locaDataSource) {

        if (INSTANCE == null) INSTANCE = new Repository(remoteDataSource, locaDataSource);
        return INSTANCE;
    }

    @Override
    public void checkVersionIntialization(final CallbackResult callback) {
        mLocalDataSource.checkVersionIntialization(new CallbackResult<Object>() {
            @Override
            public void onSuccess(Object data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }

        });
    }

    @Override
    public void intializeVersion() {
        mLocalDataSource.intializeVersion();
    }

    @Override
    public void checkVersionChange(int version, final CallbackResult callback) {
        mLocalDataSource.checkVersionChange(version, new CallbackResult<Object>() {
            @Override
            public void onSuccess(Object data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }

        });
    }
}

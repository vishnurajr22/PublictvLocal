package com.tracking.m2comsys.adapplication.data;

/**
 * Created by tikson on 7/5/18.
 */

public interface DataSource {
    interface CallbackResult<T> {

        void onSuccess(T data);

        void onFailure(Throwable t);
    }

    void checkVersionIntialization(DataSource.CallbackResult callback);

    void intializeVersion();

    void checkVersionChange(int version, DataSource.CallbackResult callback);
}

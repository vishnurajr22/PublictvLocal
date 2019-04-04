package com.tracking.m2comsys.adapplication.extras;

import com.tracking.m2comsys.adapplication.data.DataSource;

/**
 * Created by tikson on 7/5/18.
 */

public class SplashPresenter implements SplashContract.SplashPres {
    private DataSource dataSource;
    private SplashContract.SplashView splashView;

    public SplashPresenter(SplashContract.SplashView splashView, DataSource dataSource) {
        this.splashView = splashView;
        this.dataSource = dataSource;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void checkVersionIntialization() {
        dataSource.checkVersionIntialization(new DataSource.CallbackResult() {
            @Override
            public void onSuccess(Object data) {
                boolean check = (boolean) data;
                splashView.checkVersionIntializationRes(check);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void intializeVersion() {
        dataSource.intializeVersion();
    }
}


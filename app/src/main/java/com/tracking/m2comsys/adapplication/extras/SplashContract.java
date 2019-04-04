package com.tracking.m2comsys.adapplication.extras;


/**
 * Created by tikson on 7/5/18.
 */

public interface SplashContract {
    interface SplashView extends BaseView<SplashPres> {
        void checkVersionIntializationRes(boolean check);
    }

    /**
     * Contract for the presenter
     */
    interface SplashPres extends BasePresenter {
        void checkVersionIntialization();

        void intializeVersion();
    }
}

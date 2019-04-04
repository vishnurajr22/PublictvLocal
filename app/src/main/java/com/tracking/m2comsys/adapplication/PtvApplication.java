package com.tracking.m2comsys.adapplication;

import android.app.Application;

public class PtvApplication extends Application {
    public static PtvApplication ptvApplication;

    @Override
    public void onCreate() {

        super.onCreate();
        ptvApplication = this;
  /*      Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);*/

    }
}

package com.apps.karums.kemitor;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

/**
 * Created by karums on 2/19/2017.
 */

public class KemitorApplication extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        KemitorApplication.mContext = getApplicationContext();
        // Setup handler for uncaught exceptions.
//        Thread.setDefaultUncaughtExceptionHandler (new KemitorRuntimeExceptionHandler());
    }

    public static Context getAppContext() {
        return mContext;
    }
}

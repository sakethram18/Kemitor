package com.example.karums1.kemitor;

import android.app.Application;
import android.content.Context;

/**
 * Created by karums1 on 2/19/2017.
 */

public class KemitorApplication extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        KemitorApplication.mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
}

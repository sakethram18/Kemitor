package com.example.karums1.kemitor;

import android.graphics.drawable.Drawable;

/**
 * Created by karums1 on 12/26/2016.
 */

/**
 * Describes a single application entity
 */
public class AppModel {

    private String packageName = "";
    private String appName = "";
    private Drawable appIcon;

    public AppModel(String packageName, String appName , Drawable appIcon) {
        this.packageName = packageName;
        this.appName = appName;
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }
}

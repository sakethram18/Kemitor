package com.example.karums1.kemitor.data_access;

import com.example.karums1.kemitor.Utils;

import java.util.ArrayList;

/**
 * Created by karums1 on 2/13/2017.
 */

public class DataModel {
    private static DataModel mModel = null;
    // This should get refreshed every time the user saves preferences from the list of apps
    // It acts as a cache to avoid db calls every time
    private ArrayList<AppModel> mAppsList;
    private ArrayList<AppModel> mLauncherApps = new ArrayList<>();

    public static synchronized DataModel getInstance() {
        if (mModel == null) {
            mModel = new DataModel();
        }
        return mModel;
    }

    private DataModel() {
        if (mAppsList == null) {
            mAppsList = new ArrayList<>();
        }
    }

    public AppModel getAppModel(String packageName) {
        if (mAppsList != null) {
            for (AppModel model: mAppsList) {
                if (model.getPackageName().equalsIgnoreCase(packageName)) {
                    return model;
                }
            }
            throw new IllegalArgumentException("Package not found: " + packageName);
        } else {
            throw new IllegalArgumentException("Attempting to access the following package when " +
                    "service not running: " + packageName);
        }
    }

    public boolean getIsLauncherApp(String packageName) {
        return mLauncherApps.contains(getAppModel(packageName));
    }

    public void clearAppsList() {
        mAppsList.clear();
    }

    public void updateAppsList(ArrayList<AppModel> appsList) {
        if (mAppsList.size() == 0) {
            mAppsList.addAll(appsList);
        } else {
            throw new IllegalArgumentException("Apps list cache should be cleared before updating" +
                    ".");
        }
    }

    public ArrayList<AppModel> getLauncherApps(boolean isLoadAgain) {
        if (isLoadAgain || mLauncherApps.size() == 0) {
            mLauncherApps.clear();
            mLauncherApps = Utils.getLauncherApps();
        }
        return mLauncherApps;
    }

    public ArrayList<AppModel> getAppsList() {
        return mAppsList;
    }
}

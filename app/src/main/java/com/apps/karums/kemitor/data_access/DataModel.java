package com.apps.karums.kemitor.data_access;

import android.util.Log;

import com.apps.karums.kemitor.Utils;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;

/**
 * Created by karums on 2/13/2017.
 */

public class DataModel {
    private static DataModel mModel = null;
    // This should get refreshed every time the user saves preferences from the list of apps
    // It acts as a cache to avoid db calls every time
    private ArrayList<IAppModel> mAppsList;
    private ArrayList<IAppModel> mLauncherApps = new ArrayList<>();
    private static final String TAG = "DataModel";
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

    public IAppModel getAppModel(String packageName) {
        if (mAppsList != null) {
            for (IAppModel model: mAppsList) {
                if (model.getPackageName().equalsIgnoreCase(packageName)) {
                    return model;
                } else {
                    FirebaseCrash.logcat(Log.VERBOSE, TAG, "getAppModel - Model name: " + model
                            .getPackageName() + " | Input package name: " + packageName);
                }
            }
            FirebaseCrash.report(new Throwable("Testing DataModel"));
            throw new IllegalArgumentException("Package not found: " + packageName);
        } else {
            throw new IllegalArgumentException("Attempting to access the following package when " +
                    "service not running: " + packageName);
        }
    }

    public boolean getIsLauncherApp(String packageName) {
        for (IAppModel model: mAppsList) {
            if (model.getPackageName().equalsIgnoreCase(packageName)) {
                return model.getIsLauncherApp();
            }
        }
        return false;
    }

    public ArrayList<IAppModel> getSelectedApps() {
        ArrayList<IAppModel> resultList = new ArrayList<>();
        for(IAppModel model: mAppsList) {
            if (model.isSelected()) {
                resultList.add(model);
            }
        }
        return resultList;
    }

    public void clearAppsList() {
        mAppsList.clear();
    }

    public void updateAppsList(ArrayList<IAppModel> appsList) {
//        if (mAppsList.size() == 0) {
        mAppsList.addAll(appsList);
//        } else {
//            throw new IllegalArgumentException("Apps list cache should be cleared before updating" +
//                    ".");
//        }
    }

    public ArrayList<IAppModel> getLauncherApps(boolean isLoadAgain) {
        if (isLoadAgain || mLauncherApps.size() == 0) {
            mLauncherApps.clear();
            mLauncherApps = Utils.getLauncherApps();
        }
        return mLauncherApps;
    }

    public ArrayList<IAppModel> getAppsList() {
        return mAppsList;
    }
}

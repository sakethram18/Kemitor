package com.example.karums1.kemitor;

import android.Manifest;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by karums1 on 12/23/2016.
 */

public class Utils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    public static final String TAG = Utils.class.getSimpleName();

    public static int checkUsagePermission(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.PACKAGE_USAGE_STATS);
        return permissionCheck;
    }

    public static List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.MINUTE, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime));
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime);
        return usageStatsList;
    }

    public static UsageEvents getUsageEventsList(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.MINUTE, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime));
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        UsageEvents usageEventsList = usm.queryEvents(startTime, endTime);

        Log.d(TAG, "Printing Usage events....");
        while (usageEventsList.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEventsList.getNextEvent(event);
            Log.d(TAG, "Pkg: " + event.getPackageName() + "\t" + "Type: " + event.getEventType());
        }
        return usageEventsList;
    }

    public static void printUsageStats(List<UsageStats> usageStatsList) {
        for (UsageStats u : usageStatsList) {
            Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                    + u.getTotalTimeInForeground());
        }

    }

    private static UsageStatsManager getUsageStatsManager(Context context) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        return usm;
    }

    public static boolean isAccessibilityEnabled(Context context) {
        int accessibilityEnabled = 0;
        final String KEMITOR_ACCESSIBILITY_SERVICE = context.getPackageName() + "/" +
                KemitorAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(), android.provider
                    .Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.d(TAG, "ACCESSIBILITY: " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.d(TAG, "***ACCESSIBILIY IS ENABLED***: ");

            String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings
                    .Secure.ENABLED_ACCESSIBILITY_SERVICES);
            Log.d(TAG, "Setting: " + settingValue);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessibilityService = splitter.next();
                    Log.d(TAG, "Setting: " + accessibilityService);
                    if (accessibilityService.equalsIgnoreCase(KEMITOR_ACCESSIBILITY_SERVICE)) {
                        Log.d(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
            Log.d(TAG, "***END***");
        } else {
            Log.d(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    public static List<AppModel> getInstalledApps(Context context) {
        List<PackageInfo> myApps = context.getPackageManager().getInstalledPackages(0);
        List<AppModel> listOfApps = new ArrayList<>();
        for (int i = 0; i < myApps.size(); i++) {
            PackageInfo p = myApps.get(i);
            String packageName = p.packageName;
            String appName = p.applicationInfo.loadLabel(context.getPackageManager())
                    .toString();
            Drawable icon = p.applicationInfo.loadIcon(context.getPackageManager());
            AppModel model = new AppModel(packageName, appName, icon);
            listOfApps.add(model);
        }
        Log.d(TAG, "Loading list of installed apps");
        return listOfApps;
    }
}

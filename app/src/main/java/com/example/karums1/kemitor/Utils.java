package com.example.karums1.kemitor;

import android.Manifest;
import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.karums1.kemitor.data_access.KemitorDataResolver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public static ArrayList<AppModel> getInstalledApps(Context context) {
        List<PackageInfo> myApps = context.getPackageManager().getInstalledPackages(0);
        ArrayList<AppModel> listOfApps = new ArrayList<>();
        for (int i = 0; i < myApps.size(); i++) {
            PackageInfo p = myApps.get(i);
            String packageName = p.packageName;
            String appName = p.applicationInfo.loadLabel(context.getPackageManager())
                    .toString();
            Drawable icon = p.applicationInfo.loadIcon(context.getPackageManager());
            AppModel model = new AppModel(packageName, appName, icon);
            // TODO: Add system apps with a warning to the user
//            if ((p.applicationInfo.flags & (p.applicationInfo.FLAG_SYSTEM | p.applicationInfo
//                    .FLAG_UPDATED_SYSTEM_APP)) == 0) {
                listOfApps.add(model);
//            }
        }
        Log.d(TAG, "Loading list of installed apps");
        Collections.sort(listOfApps, new Comparator<AppModel>() {
            @Override
            public int compare(AppModel lhs, AppModel rhs) {
                return lhs.getAppName().compareTo(rhs.getAppName());
            }
        });
        return updateStatusInstalledApps(context, listOfApps);
    }


    private static ArrayList<AppModel> updateStatusInstalledApps(Context context,
                                                                 ArrayList<AppModel>
            listOfApps) {
        KemitorDataResolver resolver = new KemitorDataResolver(context);
        Map<AppModel, Boolean> dbAllApps = resolver.getAllAppModels();

        for (AppModel model: listOfApps) {
            if (dbAllApps.containsKey(model)) {
                model.setSelected(dbAllApps.get(model));
            }
        }
        return listOfApps;
    }

    public static String getTopAppName(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String strName = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                strName = getLollipopFGAppPackageName(context);
            } else {
                strName = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strName;
    }


    private static String getLollipopFGAppPackageName(Context ctx) {

        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) ctx.getSystemService(Context.USAGE_STATS_SERVICE);
            long milliSecs = 60 * 1000;
            Date date = new Date();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, date.getTime() - milliSecs, date.getTime());
            if (queryUsageStats.size() > 0) {
                Log.d(TAG, "queryUsageStats size: " + queryUsageStats.size());
            }
            long recentTime = 0;
            String recentPkg = "";
            for (int i = 0; i < queryUsageStats.size(); i++) {
                UsageStats stats = queryUsageStats.get(i);
                if (i == 0 && !"org.pervacio.pvadiag".equals(stats.getPackageName())) {
                    Log.d(TAG, "PackageName: " + stats.getPackageName() + " " + stats
                            .getLastTimeStamp());
                }
                if (stats.getLastTimeStamp() > recentTime) {
                    recentTime = stats.getLastTimeStamp();
                    recentPkg = stats.getPackageName();
                }
            }
            return recentPkg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

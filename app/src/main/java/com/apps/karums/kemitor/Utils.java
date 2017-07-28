package com.apps.karums.kemitor;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.apps.karums.kemitor.data_access.AppModel;
import com.apps.karums.kemitor.data_access.IAppModel;
import com.apps.karums.kemitor.data_access.IProfileModel;
import com.apps.karums.kemitor.data_access.KemitorDataResolver;
import com.apps.karums.kemitor.data_access.LauncherAppModel;
import com.apps.karums.kemitor.presentation.receivers.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.apps.karums.kemitor.AppConstants.MAX_DAYS_IN_A_WEEK;

/**
 * Created by karums on 12/23/2016.
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

    public static ArrayList<IAppModel> getLauncherApps() {
        PackageManager pm = KemitorApplication.getAppContext().getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN);
        // Get all home launcher applications
        i.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> lst = pm.queryIntentActivities(i, 0);
        ArrayList<IAppModel> launcherApps = new ArrayList<>();
        for (ResolveInfo resolveInfo : lst) {
            String packageName = resolveInfo.activityInfo.packageName;
            String appName = (String) resolveInfo.activityInfo.loadLabel(pm);
            Drawable icon = resolveInfo.loadIcon(pm);
            IAppModel app = new LauncherAppModel(packageName, appName, icon);
            launcherApps.add(app);
        }
        // Also add System UI if it doesn't exist.
        String sUI = "com.android.systemui";
        try {
            IAppModel systemUI = new LauncherAppModel(sUI, "System UI", pm.getApplicationIcon(sUI));
            if (!launcherApps.contains(systemUI)) {
                launcherApps.add(systemUI);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Package not found: " + sUI);
            e.printStackTrace();
        }
        return launcherApps;
    }

    public static ArrayList<IAppModel> getInstalledApps(Context context) {
        ArrayList<IAppModel> listOfApps = getLauncherApps();

        PackageManager pm = KemitorApplication.getAppContext().getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        // Get all launchable and usable applications
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities( mainIntent, 0);

        for (int i = 0; i < pkgAppsList.size(); i++) {
            ResolveInfo r = pkgAppsList.get(i);
            String packageName = r.activityInfo.packageName;
            String appName = r.activityInfo.loadLabel(pm).toString();
            Drawable icon = r.loadIcon(pm);
            IAppModel model = new AppModel(packageName, appName, icon);
            // TODO: Add system apps with a warning to the user
//            if ((p.applicationInfo.flags & (p.applicationInfo.FLAG_SYSTEM | p.applicationInfo
//                    .FLAG_UPDATED_SYSTEM_APP)) == 0) {
            // This is because a home launcher application can also be a launchable application
            if (!listOfApps.contains(model)) {
                listOfApps.add(model);
            }
//            }
        }
//        List<PackageInfo> myApps = context.getPackageManager().getInstalledPackages(0);
//        for (int i = 0; i < myApps.size(); i++) {
//            PackageInfo p = myApps.get(i);
//            String packageName = p.packageName;
//            String appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
//            Drawable icon = p.applicationInfo.loadIcon(context.getPackageManager());
//            IAppModel model = new AppModel(packageName, appName, icon);
//            listOfApps.add(model);
//        }
        Log.d(TAG, "Loading list of installed apps");
        Collections.sort(listOfApps, new Comparator<IAppModel>() {
            @Override
            public int compare(IAppModel lhs, IAppModel rhs) {
                return lhs.getAppName().toUpperCase().compareTo(rhs.getAppName().toUpperCase());
            }
        });
        return listOfApps;
    }

    public static ArrayList<IProfileModel> getSavedProfilesBasicInfo(Context context) {
        KemitorDataResolver resolver = new KemitorDataResolver(context);
        return resolver.getAllProfileModelsBasicInfo();
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

    public static void scheduleNextAlarm(Context context) {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        //TODO: Test using wakeup and non wakeup version
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() +
                        60 * 1000, alarmIntent);
    }

    public static int getShortenedDaysOfWeek(boolean[] stateList) {
        int result = 0;
        for (int i = 0; i < MAX_DAYS_IN_A_WEEK; i++) {
            if (stateList[i]) {
                result += Math.pow(2, (MAX_DAYS_IN_A_WEEK - i - 1));
            }
        }
        return result;
    }

    public static boolean[] getExpandedDaysOfWeek(int daysOfWeek) {
        boolean[] stateList = new boolean[MAX_DAYS_IN_A_WEEK];
        String daysState = Integer.toBinaryString(daysOfWeek);
        String repeated = new String(new char[MAX_DAYS_IN_A_WEEK - daysState.length()]).replace("\0", "0");
        String finalDaysState = repeated + daysState;
        for (int i = 0; i < MAX_DAYS_IN_A_WEEK; i++) {
            stateList[i] = finalDaysState.charAt(i) == '1';
        }
        return stateList;
    }


}

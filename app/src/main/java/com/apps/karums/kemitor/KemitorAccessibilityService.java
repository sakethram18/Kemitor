package com.apps.karums.kemitor;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.apps.karums.kemitor.data_access.DataModel;
import com.apps.karums.kemitor.presentation.activities.AlertDialogActivity;
import com.apps.karums.kemitor.presentation.widgets.KemitorOverlayAlert;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.apps.karums.kemitor.AppConstants.KEMITOR_ACCESSIBILITY_SERVICE_ENABLED;

public class KemitorAccessibilityService extends AccessibilityService implements KemitorOverlayAlert.AlertDialogListener {

    Map<String, Integer> mNotificationIdMap = new HashMap<>();
    AtomicInteger mNotificationId = new AtomicInteger();
    private static final String TAG = "KemitorAccessibilityService";
    KemitorOverlayAlert mOverlayAlert = null;
    boolean mIsLauncherApp = true;
    boolean mIsUserChosenEnter = false;
    static final long SNOOZE_TIME = 30 * 1000; // 30 secs
    ConcurrentHashMap<String, AppData> mAppData = new ConcurrentHashMap<>();
    private static final int MAX_SNOOZES = 3;
    public KemitorAccessibilityService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Accessibility service starting", Toast.LENGTH_SHORT).show();
        FirebaseCrash.logcat(Log.VERBOSE, TAG, "onStartCommand - Accessibility service starting");
        mOverlayAlert = KemitorOverlayAlert.getOverlayAlert();
        mOverlayAlert.addAlertDialogListener(this);
        setServiceConfiguration(intent);
        // If we get killed, after returning from here, restart and redeliver the intent
        return START_REDELIVER_INTENT;
    }

    private void setServiceConfiguration(Intent intent) {
        //TODO: Optimize AccessibilityServiceInfo object creation
        if (intent != null) {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "setServiceConfiguration - Intent not null");
            boolean isEnabled = intent.getBooleanExtra(KEMITOR_ACCESSIBILITY_SERVICE_ENABLED, false);
            if (!isEnabled) {
                FirebaseCrash.logcat(Log.VERBOSE, TAG, "setServiceConfiguration - Starting " +
                        "service with empty configuration");
                this.setServiceInfo(new AccessibilityServiceInfo());
            } else {
                ArrayList<String> selectedAppsStr = intent.getStringArrayListExtra(AppConstants
                        .LIST_OF_SELECTED_APPS);
                initializeAppData(selectedAppsStr);
                FirebaseCrash.logcat(Log.VERBOSE, TAG, "setServiceConfiguration - Starting " +
                        "service with configuration: " + selectedAppsStr.toString());
                AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//            info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED |
//                    AccessibilityEvent.TYPE_VIEW_FOCUSED |
//                    AccessibilityEvent.TYPE_VIEW_CLICKED |
//                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;

                //TODO: The service should also listen to any new launcher apps being installed
                // while it is running in the background
                info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

//                      |  AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
//                info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
                info.packageNames = (selectedAppsStr.toArray(new String[0]));
                info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
                info.notificationTimeout = 100;
                this.setServiceInfo(info);
            }
        } else {
            mAppData.clear();
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "setServiceConfiguration - Starting " +
                    "service with null intent");
        }
    }

    private void initializeAppData(ArrayList<String> packageNames) {
        for (String packageName: packageNames) {
            mAppData.put(packageName, new AppData());
        }
    }

    private void setLauncherOnTop() {
        for (AppData appData : mAppData.values()) {
            appData.setIsAppOnTop(false);
        }
    }

        /* Algorithm - pseudo code

        *** Assuming only window state changed events are received ***

        Map 1:
        ConcurrentHashMap<Package name,
        <Last snooze click event (initialize with INT_MIN),
        No of snoozes until now,
        boolean for is app on top>>

        Map 2:
        ConcurrentHashMap<Package name,
        a thread that keeps checking if the launcher is on top for every minute during the snooze
        time>

        *** OnAccessibilityEvent ***
        if (launcher app) {
            // Update boolean for is app on top for all packages to false in Map 1
        } else { // normal app
            // Update boolean for is app on top for that packages to true in Map 1
            // Conditions to display dialog:
                1. If the user entered for the first time -> if last snooze click event is
                INT_MIN, display dialog and skip steps below
                2. If the user snoozed, closed and again uses app after snooze time -> if elapsed
                time minus last click time is greater than snooze time, display dialog and skip
                below steps
                    i. Can remain in the app for the entire snooze time
                    ii. Can quit/reenter the app multiple times during the snooze time and can
                    stay in the app during snooze time or enters app after snooze time
            if (conditions to display dialog) {
                // Display dialog with quit and snooze or just quit
            } else {
                // Don't display anything
            }
        }

        *** Inside run of each thread ***
        Post run after snooze time
            if (boolean for is app on top true for that package) {
                Display dialog
            }
        }

        *** On dialog button click events ***
        Snooze clicked:
            1. Start the thread for that package in Map 2, if it is not currently running and
            dismiss dialog
            2. Update last clicked time for that package in Map 1
            3. Increment snooze counter in Map 1
        Quit clicked:
            1. Dismiss dialog

         */





    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        String description = event.toString();
        buildNotif(packageName, description);
//        showOverlayDialog(packageName);
        DataModel dataModel = DataModel.getInstance();
        FirebaseCrash.logcat(Log.VERBOSE, TAG, "onAccessibilityEvent - Event received for: " + packageName);
        if (dataModel.getIsLauncherApp(packageName)) {
//            mIsLauncherApp = true;
//            mIsUserChosenEnter = false;
            setLauncherOnTop();
        } else {
            AppData data = mAppData.get(packageName);
            data.setIsAppOnTop(true);
            if ((SystemClock.elapsedRealtime() - data.getLastClickTime() > SNOOZE_TIME)) {
//                showOverlayDialog(packageName, MAX_SNOOZES <= data.getNoOfSnoozes());
                Intent dialogIntent = new Intent(this, AlertDialogActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dialogIntent.putExtra(AlertDialogActivity.PACKAGE_NAME, packageName);
                dialogIntent.putExtra(AlertDialogActivity.IS_STRICT, MAX_SNOOZES <= data
                        .getNoOfSnoozes());
                startActivity(dialogIntent);
            }
//            if (Integer.MIN_VALUE == data.getLastClickTime()) {
//                showOverlayDialog(packageName, MAX_SNOOZES <= data.getNoOfSnoozes());
//            } else if ((SystemClock.elapsedRealtime() - data.getLastClickTime() > SNOOZE_TIME) &&
//                    data.getNoOfSnoozes() <= MAX_SNOOZES ){
//                showOverlayDialog(packageName, MAX_SNOOZES <= data.getNoOfSnoozes());
//            }
        }

//            if (mIsLauncherApp) {
////                if (!mIsUserChosenEnter && !mOverlayAlert.isAlertShowing()) {
//                if (!mOverlayAlert.isAlertShowing()) {
//                    if (SystemClock.elapsedRealtime() - mPreviousClickTime < SNOOZE_TIME) {
//                        return;
//                    }
//                    showOverlayDialog(packageName);
//                }
//            }
//        }
    }

    @Override
    public void onInterrupt() {

    }



    private void buildNotif(String packageName, String text) {
        NotificationCompat.Builder mBuilder;

        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.check)
                        .setContentTitle(packageName)
                        .setContentText(text)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(text));


        // Sets an ID for the notification
        int notifId;
        if (mNotificationIdMap.containsKey(packageName)) {
            notifId = mNotificationIdMap.get(packageName);
        } else {
            notifId = mNotificationId.incrementAndGet();
            mNotificationIdMap.put(packageName, notifId);
        }
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(notifId, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Accessibility service done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoneSelected(final String packageName) {
        AppData data = mAppData.get(packageName);
        data.setLastClickTime(SystemClock.elapsedRealtime());
        data.setNoOfSnoozes(data.getNoOfSnoozes() + 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KemitorAccessibilityService.AppData data = mAppData.get(packageName);
                if (data.isAppOnTop()) {
                    Intent dialogIntent = new Intent(KemitorAccessibilityService.this,
                            AlertDialogActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogIntent.putExtra(AlertDialogActivity.PACKAGE_NAME, packageName);
                    dialogIntent.putExtra(AlertDialogActivity.IS_STRICT, MAX_SNOOZES <= data
                            .getNoOfSnoozes());
                    startActivity(dialogIntent);
                }
            }
        }, SNOOZE_TIME);

    }

    @Override
    public void onCancelSelected() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private class AppData {
        private long mLastClickTime = Integer.MIN_VALUE;
        private boolean mIsAppOnTop;
        private  int mNoOfSnoozes = 0;

        long getLastClickTime() {
            return mLastClickTime;
        }

        void setLastClickTime(long mLastClickTime) {
            this.mLastClickTime = mLastClickTime;
        }

        boolean isAppOnTop() {
            return mIsAppOnTop;
        }

        void setIsAppOnTop(boolean mIsAppOnTop) {
            this.mIsAppOnTop = mIsAppOnTop;
        }

        int getNoOfSnoozes() {
            return mNoOfSnoozes;
        }

        void setNoOfSnoozes(int mNoOfSnoozes) {
            this.mNoOfSnoozes = mNoOfSnoozes;
        }
    }
}

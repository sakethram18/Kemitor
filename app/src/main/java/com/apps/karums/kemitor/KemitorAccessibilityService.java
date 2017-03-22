package com.apps.karums.kemitor;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.apps.karums.kemitor.data_access.DataModel;
import com.apps.karums.kemitor.presentation.widgets.KemitorOverlayAlert;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.apps.karums.kemitor.AppConstants.KEMITOR_ACCESSIBILITY_SERVICE_ENABLED;

public class KemitorAccessibilityService extends AccessibilityService {

    Map<String, Integer> mNotificationIdMap = new HashMap<>();
    AtomicInteger mNotificationId = new AtomicInteger();
    private static final String TAG = "KemitorAccessibilityService";
    KemitorOverlayAlert mOverlayAlert = null;
    boolean mIsLauncherApp = true;
    boolean mIsUserChosenEnter = false;

    public KemitorAccessibilityService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Accessibility service starting", Toast.LENGTH_SHORT).show();
        FirebaseCrash.logcat(Log.VERBOSE, TAG, "onStartCommand - Accessibility service starting");
        mOverlayAlert = KemitorOverlayAlert.getOverlayAlert(this);
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
                info.packageNames = (selectedAppsStr.toArray(new String[0]));
                info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
                info.notificationTimeout = 100;
                this.setServiceInfo(info);
            }
        } else {
            FirebaseCrash.logcat(Log.VERBOSE, TAG, "setServiceConfiguration - Starting " +
                    "service with null intent");
        }
        FirebaseCrash.report(new Throwable("Testing KemitorAccessibilityService"));
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        String description = event.toString();
        buildNotif(packageName, description);
//        showOverlayDialog(packageName);
        DataModel dataModel = DataModel.getInstance();
        FirebaseCrash.logcat(Log.VERBOSE, TAG, "onAccessibilityEvent - Event received for: " + packageName);
        if (dataModel.getIsLauncherApp(packageName)) {
            mIsLauncherApp = true;
            mIsUserChosenEnter = false;
        } else {
            if (mIsLauncherApp) {
                if (!mIsUserChosenEnter && !mOverlayAlert.isAlertShowing()) {
                    showOverlayDialog(packageName);
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void showOverlayDialog(String packageName) {
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                PixelFormat.TRANSLUCENT);
//        params.gravity = Gravity.LEFT;
//
//        FrameLayout frameLayout = new FrameLayout(this);
//        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        // Here is the place where you can inject whatever layout you want.
//        View window = layoutInflater.inflate(R.layout.overlay_window, frameLayout);
//        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        windowManager.addView(window, params);

        String appName = DataModel.getInstance().getAppModel(packageName).getAppName();
        String message = String.format(getString(R.string.sure_enter_app_description), appName);
        mOverlayAlert.createOverlayAlert(getString(R.string.enter_app), message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mIsUserChosenEnter = true;
                dialogInterface.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                dialogInterface.dismiss();
            }
        });
        mOverlayAlert.showAlert();
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
}

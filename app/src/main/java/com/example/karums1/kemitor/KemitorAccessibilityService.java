package com.example.karums1.kemitor;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.example.karums1.kemitor.data_access.DataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.karums1.kemitor.AppConstants.KEMITOR_ACCESSIBILITY_SERVICE_ENABLED;

public class KemitorAccessibilityService extends AccessibilityService {

    Map<String, Integer> mNotificationIdMap = new HashMap<>();
    AtomicInteger mNotificationId = new AtomicInteger();

    public KemitorAccessibilityService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Accessibility service starting", Toast.LENGTH_SHORT).show();
        setServiceConfiguration(intent);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    private void setServiceConfiguration(Intent intent) {
        //TODO: Optimize AccessibilityServiceInfo object creation
        boolean isEnabled = intent.getBooleanExtra(KEMITOR_ACCESSIBILITY_SERVICE_ENABLED, false);
        if (!isEnabled) {
            this.setServiceInfo(new AccessibilityServiceInfo());
        } else {
            ArrayList<String> selectedAppsStr = intent.getStringArrayListExtra(AppConstants
                    .LIST_OF_SELECTED_APPS);
//            ArrayList<AppModel> selectedApps = new Gson().fromJson(selectedAppsStr,
//                    new TypeToken<List<AppModel>>(){}.getType());

            AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//            info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED |
//                    AccessibilityEvent.TYPE_VIEW_FOCUSED |
//                    AccessibilityEvent.TYPE_VIEW_CLICKED |
//                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;

            info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

            info.packageNames = (selectedAppsStr.toArray(new String[0]));

            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
            info.notificationTimeout = 100;
            this.setServiceInfo(info);
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventId = event.getEventType();
        String packageName = event.getPackageName().toString();
        String description = event.toString();
        String topAppName = Utils.getTopAppName(this);
//        if (packageName.equalsIgnoreCase(topAppName)) {
            buildNotif(packageName, description);
        showOverlayDialog(packageName);
//        }
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

        String appName = DataModel.getInstance().getAppName(packageName);
        String message = String.format(getString(R.string.sure_enter_app_description), appName);
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.OverlayDialog)
                .setTitle(getString(R.string.enter_app))
                .setMessage(message)
                .setPositiveButton("Enter", null)
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                    }
                })
                .create();

        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();

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

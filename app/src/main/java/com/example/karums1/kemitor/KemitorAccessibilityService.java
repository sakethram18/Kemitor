package com.example.karums1.kemitor;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

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

    private ArrayList<String> getPackageNameList(ArrayList<AppModel> selectedApps) {
        ArrayList<String> packageNamesList = new ArrayList<>();
        for(AppModel model: selectedApps) {
            packageNamesList.add(model.getPackageName());
        }
        return packageNamesList;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventId = event.getEventType();
        String title = event.getPackageName().toString();
        String description = event.toString();
        String topAppName = Utils.getTopAppName(this);
//        if (title.equalsIgnoreCase(topAppName)) {
            buildNotif(title, description);
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
}

package com.example.karums1.kemitor;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import static com.example.karums1.kemitor.AppConstants.KEMITOR_ACCESSIBILITY_SERVICE_ENABLED;

public class KemitorAccessibilityService extends AccessibilityService {
    public KemitorAccessibilityService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Accessibility service starting", Toast.LENGTH_SHORT).show();
        boolean isEnabled = intent.getBooleanExtra(KEMITOR_ACCESSIBILITY_SERVICE_ENABLED, false);
        setServiceConfiguration(isEnabled);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    private void setServiceConfiguration(boolean isEnabled) {
        if (!isEnabled) {
            this.setServiceInfo(new AccessibilityServiceInfo());
        } else {
            AccessibilityServiceInfo info = new AccessibilityServiceInfo();
            info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED |
                    AccessibilityEvent.TYPE_VIEW_FOCUSED |
                    AccessibilityEvent.TYPE_VIEW_CLICKED |
                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;

            info.packageNames = new String[]{"com.google.android.youtube",
                    "com.google.android.apps.maps"};

            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
            info.notificationTimeout = 100;
            this.setServiceInfo(info);
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventId = event.getEventType();
        String title = "App launched";
        String packageName = event.getPackageName().toString();
        buildNotif(title, packageName);
    }

    @Override
    public void onInterrupt() {

    }

    private void buildNotif(String title, String text) {
        NotificationCompat.Builder mBuilder;

        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.check)
                        .setContentTitle(title)
                        .setContentText(text);


        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Accessibility service done", Toast.LENGTH_SHORT).show();
    }
}

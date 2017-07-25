package com.apps.karums.kemitor.presentation.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import com.apps.karums.kemitor.Utils;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        //TODO: Update the accessibility service with list of apps
        //TODO: Check for the next alarm and schedule it
        Utils.scheduleNextAlarm(context);
    }


}

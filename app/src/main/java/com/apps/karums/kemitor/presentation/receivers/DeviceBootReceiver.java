package com.apps.karums.kemitor.presentation.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.apps.karums.kemitor.Utils;

/**
 * Created by karums1 on 7/25/2017.
 */
//TODO: Enable disable receiver in manifest: https://developer.android.com/training/scheduling/alarms.html
public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */
            Utils.scheduleNextAlarm(context);
        }
    }
}

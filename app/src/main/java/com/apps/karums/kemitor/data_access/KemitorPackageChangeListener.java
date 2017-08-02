package com.apps.karums.kemitor.data_access;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by karums1 on 7/16/2017.
 */

public class KemitorPackageChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        KemitorDataResolver resolver = new KemitorDataResolver(context);
//        intent.getAction()
    }
}

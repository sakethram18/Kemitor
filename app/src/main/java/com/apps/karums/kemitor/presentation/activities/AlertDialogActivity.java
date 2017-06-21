package com.apps.karums.kemitor.presentation.activities;

import android.app.Activity;
import android.os.Bundle;

import com.apps.karums.kemitor.R;
import com.apps.karums.kemitor.data_access.DataModel;
import com.apps.karums.kemitor.presentation.widgets.KemitorOverlayAlert;

public class AlertDialogActivity extends Activity implements KemitorOverlayAlert
        .AlertDialogListener {

    public static final String PACKAGE_NAME = "package_name";
    public static final String IS_STRICT = "is_strict";
    KemitorOverlayAlert mOverlayAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        String packageName = data.getString(PACKAGE_NAME);
        boolean isStrict = data.getBoolean(IS_STRICT, false);
        showOverlayDialog(packageName, isStrict);
    }

    private synchronized void showOverlayDialog(final String packageName, boolean isStrict) {

        String appName = DataModel.getInstance().getAppModel(packageName).getAppName();
        String message = String.format(getString(R.string.sure_enter_app_description), appName);
        mOverlayAlert = KemitorOverlayAlert.getOverlayAlert();
        mOverlayAlert.addAlertDialogListener(this);
        mOverlayAlert.createOverlayAlert(packageName, getString(R.string.enter_app), message,
                isStrict, this);
        if (!mOverlayAlert.isAlertShowing()) {
            mOverlayAlert.showAlert();
            mOverlayAlert.setIsShowing(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mOverlayAlert.dismissAlert();
        //TODO: Fix activity being shown in recent apps while the dialog is showing
    }

    @Override
    public void onDoneSelected(String packageName) {
        finish();
    }

    @Override
    public void onCancelSelected() {
        finish();
    }
}

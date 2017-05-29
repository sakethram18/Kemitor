package com.apps.karums.kemitor.presentation.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.apps.karums.kemitor.R;

/**
 * Created by karums on 2/19/2017.
 */

public class KemitorOverlayAlert {

    //TODO: Fix this static variable to avoid memory leaks
    private static KemitorOverlayAlert mOverlayAlert = null;
    private AlertDialog mDialog = null;
    private boolean mIsShowing = false;

    private KemitorOverlayAlert() {
    }

    public static KemitorOverlayAlert getOverlayAlert() {
        if (mOverlayAlert == null) {
            mOverlayAlert = new KemitorOverlayAlert();
        }
        return mOverlayAlert;
    }

    public void createOverlayAlert(String title, String message, DialogInterface.OnClickListener
            onDoneListener, DialogInterface.OnClickListener onCancelListener, boolean isStrict,
                                   Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(inflater.inflate(R.layout.alert_dialog, null));
        if (isStrict) {
            builder.setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(context.getString(R.string.quit_button), onCancelListener);
        } else {
            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(context.getString(R.string.snooze_button), onDoneListener)
                    .setNegativeButton(context.getString(R.string.quit_button), onCancelListener);
        }
        // Disables back key when dialog is displayed
        mDialog = builder.create();
        mDialog.setCancelable(false);
        // Disables touching outside
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    public void showAlert() {
        if (mDialog != null) {
            mDialog.show();
        } else {
            throw new RuntimeException("Instantiate the dialog before showing it");
        }
    }

    public void dismissAlert() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
            mIsShowing = false;
        }
    }

    public boolean isAlertShowing() {
        return mIsShowing;
    }

    public void setIsShowing(boolean isShowing) {
        mIsShowing = isShowing;
    }

}

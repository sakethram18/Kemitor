package com.example.karums1.kemitor.presentation.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

import com.example.karums1.kemitor.R;

/**
 * Created by karums1 on 2/19/2017.
 */

public class KemitorOverlayAlert {

    private static KemitorOverlayAlert mOverlayAlert = null;
    private AlertDialog mDialog = null;
    private Context mContext;

    private KemitorOverlayAlert(Context context) {
        mContext = context;
    }

    public static KemitorOverlayAlert getOverlayAlert(Context context) {
        if (mOverlayAlert == null) {
            mOverlayAlert = new KemitorOverlayAlert(context.getApplicationContext());
        }
        return mOverlayAlert;
    }

    public void createOverlayAlert(String title, String message, DialogInterface.OnClickListener
            onDoneListener, DialogInterface.OnClickListener onCancelListener) {
        mDialog = new AlertDialog.Builder(mContext, R.style.OverlayDialog)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Enter", onDoneListener)
                .setNegativeButton("Quit", onCancelListener)
                .create();
        // Disables back key when dialog is displayed
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
        }
    }

    public boolean isAlertShowing() {
        return mDialog != null && mDialog.isShowing();
    }


}

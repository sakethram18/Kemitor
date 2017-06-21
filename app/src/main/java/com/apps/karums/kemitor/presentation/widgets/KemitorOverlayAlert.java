package com.apps.karums.kemitor.presentation.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.apps.karums.kemitor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karums on 2/19/2017.
 */

public class KemitorOverlayAlert {

    //TODO: Fix this static variable to avoid memory leaks
    private static KemitorOverlayAlert mOverlayAlert = null;
    private AlertDialog mDialog = null;
    private boolean mIsShowing = false;
    private List<AlertDialogListener> mAlertDialogListeners;

    private KemitorOverlayAlert() {
        mAlertDialogListeners = new ArrayList<>();
    }

    public void addAlertDialogListener(AlertDialogListener listener) {
        mAlertDialogListeners.add(listener);
    }

    public static KemitorOverlayAlert getOverlayAlert() {
        if (mOverlayAlert == null) {
            mOverlayAlert = new KemitorOverlayAlert();
        }
        return mOverlayAlert;
    }

    public void createOverlayAlert(final String packageName, String title, String message, boolean isStrict,
                                   Context context) {
        DialogInterface.OnClickListener doneListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setIsShowing(false);
                for (AlertDialogListener listener: mAlertDialogListeners) {
                    listener.onDoneSelected(packageName);
                }
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setIsShowing(false);
                for (AlertDialogListener listener: mAlertDialogListeners) {
                    listener.onCancelSelected();
                }
            }
        };

        AlertDialogView dialogView = new AlertDialogView(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        if (isStrict) {
            builder.setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(context.getString(R.string.quit_button), cancelListener);
        } else {
            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(context.getString(R.string.snooze_button), doneListener)
                    .setNegativeButton(context.getString(R.string.quit_button), cancelListener);
        }
        mDialog = builder.create();
        // Disables back key when dialog is displayed
        mDialog.setCancelable(false);
        // Disables touching outside
        mDialog.setCanceledOnTouchOutside(false);
        //TODO: Push the dialog up when keyboard is opened
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
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

    public interface AlertDialogListener {
        void onDoneSelected(String packageName);
        void onCancelSelected();
    }
}

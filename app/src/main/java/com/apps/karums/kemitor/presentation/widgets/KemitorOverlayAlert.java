package com.apps.karums.kemitor.presentation.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

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
        AlertDialogView dialogView = new AlertDialogView(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
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
        mDialog = builder.create();
        // Disables back key when dialog is displayed
        mDialog.setCancelable(false);
        // Disables touching outside
        mDialog.setCanceledOnTouchOutside(false);
        //TODO: Push the dialog up when keyboard is opened
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);


        EditText editText=(EditText) mDialog.findViewById(R.id.et_user_typed_quotation);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                            .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
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

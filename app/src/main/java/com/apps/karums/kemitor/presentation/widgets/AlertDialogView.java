package com.apps.karums.kemitor.presentation.widgets;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.karums.kemitor.R;

/**
 * Created by karums1 on 5/29/2017.
 */

public class AlertDialogView extends LinearLayout {

    TextView mQuotationText;
    TextView mStatusText;
    EditText mUserTypedQuotationText;

    public AlertDialogView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog, this);
        mQuotationText = (TextView) findViewById(R.id.tv_quotation);
        mStatusText = (TextView) findViewById(R.id.tv_status);
        mUserTypedQuotationText = (EditText) findViewById(R.id.et_user_typed_quotation);
        setListeners();
    }

    private void setListeners() {
        mUserTypedQuotationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userText = mUserTypedQuotationText.getText().toString();
                String quotation = mQuotationText.getText().toString();
                if (quotation.equalsIgnoreCase(userText)) {
                    mStatusText.setText(getResources().getString(R.string.match));
                    mStatusText.setTextColor(Color.GREEN);
                } else {
                    mStatusText.setText(getResources().getString(R.string.mismatch));
                    mStatusText.setTextColor(Color.RED);
                }
            }
        });
    }
}

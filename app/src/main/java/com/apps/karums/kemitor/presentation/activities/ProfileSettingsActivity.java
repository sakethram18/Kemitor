package com.apps.karums.kemitor.presentation.activities;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apps.karums.kemitor.R;
import com.apps.karums.kemitor.presentation.widgets.DayOfTheWeekSelector;

import java.util.Calendar;

public class ProfileSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    DayOfTheWeekSelector mDaySelector;
    TextView mStartTimePicker;
    TextView mEndTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        setTitle(getString(R.string.edit_profile_settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDaySelector = (DayOfTheWeekSelector) findViewById(R.id.dSelector);
        mStartTimePicker = (TextView) findViewById(R.id.profileStartTime);
        mEndTimePicker = (TextView) findViewById(R.id.profileEndTime);
        mStartTimePicker.setOnClickListener(this);
        mEndTimePicker.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileStartTime:
                handleStartTimePicker();
                break;
            case R.id.profileEndTime:
                handleEndTimePicker();
                break;
        }
    }

    private void handleStartTimePicker() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hourToDisplay = String.format("%02d", (hourOfDay % 12));
                String minToDisplay = String.format("%02d", (minute));
                String amPm = hourOfDay < 12 ? "AM" : "PM";
                mStartTimePicker.setText(hourToDisplay + ":" + minToDisplay + " " + amPm );
            }
        }, hour, min, false);
        dialog.show();
    }

    private void handleEndTimePicker() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hourToDisplay = String.format("%02d", (hourOfDay % 12));
                String minToDisplay = String.format("%02d", (minute));
                String amPm = hourOfDay < 12 ? "AM" : "PM";
                mEndTimePicker.setText(hourToDisplay + ":" + minToDisplay + " " + amPm );
            }
        }, hour, min, false);
        dialog.show();
    }
}

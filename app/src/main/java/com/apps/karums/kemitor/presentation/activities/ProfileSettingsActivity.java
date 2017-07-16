package com.apps.karums.kemitor.presentation.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apps.karums.kemitor.R;
import com.apps.karums.kemitor.data_access.IProfileModel;
import com.apps.karums.kemitor.data_access.KemitorDataResolver;
import com.apps.karums.kemitor.presentation.widgets.DayOfTheWeekSelector;

import java.util.Calendar;

public class ProfileSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    DayOfTheWeekSelector mDaySelector;
    TextView mStartTimePicker;
    TextView mEndTimePicker;
    Button mSelectAppsButton;
    String mProfileId;
    IProfileModel mCurrentProfileModel;
    KemitorDataResolver mDataResolver;
    protected static final String PROFILE_ID = "profile_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        setTitle(getString(R.string.edit_profile_settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String profileId = getIntent().getStringExtra(PROFILE_ID);
        mDataResolver = new KemitorDataResolver(this);
        mCurrentProfileModel = mDataResolver.getProfileModel(profileId);
        mDaySelector = (DayOfTheWeekSelector) findViewById(R.id.dSelector);
        mStartTimePicker = (TextView) findViewById(R.id.profileStartTime);
        mEndTimePicker = (TextView) findViewById(R.id.profileEndTime);
        mSelectAppsButton = (Button) findViewById(R.id.chooseApps);
        mStartTimePicker.setOnClickListener(this);
        mEndTimePicker.setOnClickListener(this);
        mSelectAppsButton.setOnClickListener(this);
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
            case R.id.chooseApps:
                Intent intent = new Intent(this, AppsListActivity.class);
                intent.putExtra(PROFILE_ID, mCurrentProfileModel.getUniqueId());
                startActivity(intent);
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

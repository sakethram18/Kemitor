package com.apps.karums.kemitor.presentation.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apps.karums.kemitor.R;
import com.apps.karums.kemitor.presentation.widgets.DayOfTheWeekSelector;

public class ProfileSettingsActivity extends AppCompatActivity {

    DayOfTheWeekSelector mDaySelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        mDaySelector = (DayOfTheWeekSelector) findViewById(R.id.dSelector);
    }
}

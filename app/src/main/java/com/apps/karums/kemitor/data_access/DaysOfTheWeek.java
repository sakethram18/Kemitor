package com.apps.karums.kemitor.data_access;

/**
 * Created by karums1 on 7/27/2017.
 */

enum DaysOfTheWeek {
    Sunday(0),
    Monday(1),
    Tuesday(2),
    Wednesday(3),
    Thursday(4),
    Friday(5),
    Saturday(6);

    private int mDayValue;

    DaysOfTheWeek(int value) {
        mDayValue = value;
    }

    public int getDayValue() {
        return mDayValue;
    }
}

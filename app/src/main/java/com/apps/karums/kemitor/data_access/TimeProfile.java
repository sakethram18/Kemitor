package com.apps.karums.kemitor.data_access;

import java.util.Date;

/**
 * Created by karums1 on 8/2/2017.
 */
class TimeProfile {
    private int mStartTime;
    private int mEndTime;

    TimeProfile(Date startTime, Date endTime) {

//        mStartTime = startTime;
//        mEndTime = endTime;
    }

    TimeProfile(int startTime, int endTime) {
        mStartTime = startTime;
        mEndTime = endTime;
    }

    int getStartTime() {
        return mStartTime;
    }

    int getEndTime() {
        return mEndTime;
    }
}

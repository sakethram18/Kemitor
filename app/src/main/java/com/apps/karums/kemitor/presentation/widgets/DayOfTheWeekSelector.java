package com.apps.karums.kemitor.presentation.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.karums.kemitor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karums1 on 6/22/2017.
 */

public class DayOfTheWeekSelector extends LinearLayout {

    private List<TextView> mTvs;
    private boolean[] mStateList;
    private final int MAX_DAYS = 7;

    public DayOfTheWeekSelector(Context context) {
        super(context);
        initView();
    }

    public DayOfTheWeekSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DayOfTheWeekSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DayOfTheWeekSelector(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.day_selector, this);
        TextView mTv1 = (TextView) findViewById(R.id.tv_sunday);
        TextView mTv2 = (TextView) findViewById(R.id.tv_monday);
        TextView mTv3 = (TextView) findViewById(R.id.tv_tuesday);
        TextView mTv4 = (TextView) findViewById(R.id.tv_wednesday);
        TextView mTv5 = (TextView) findViewById(R.id.tv_thursday);
        TextView mTv6 = (TextView) findViewById(R.id.tv_friday);
        TextView mTv7 = (TextView) findViewById(R.id.tv_saturday);
        mTvs = new ArrayList<>();
        mTvs.add(mTv1);
        mTvs.add(mTv2);
        mTvs.add(mTv3);
        mTvs.add(mTv4);
        mTvs.add(mTv5);
        mTvs.add(mTv6);
        mTvs.add(mTv7);
        setListeners();
        setDaysSelectedState(115);
    }

    public void setDaysSelectedState(int state) {
        String daysState = Integer.toBinaryString(state);
        String repeated = new String(new char[MAX_DAYS - daysState.length()]).replace("\0", "0");
        String finalDaysState = repeated + daysState;
        mStateList = new boolean[MAX_DAYS];
        for (int i = 0; i < MAX_DAYS; i++) {
            mStateList[i] = finalDaysState.charAt(i) == '1';
            setDaySelected(i, mStateList[i]);
        }
    }

    private void setListeners() {
        for (int i = 0; i < MAX_DAYS; i++) {
            final int index = i;
            final TextView textView = mTvs.get(index);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStateList[index] = !mStateList[index];
                    setDaySelected(index, mStateList[index]);
                }
            });
        }
    }

    private void setDaySelected(int index, boolean isSelected) {
        if (isSelected) {
            mTvs.get(index).setBackgroundResource(R.drawable.chip_background);
            mTvs.get(index).setTextColor(getResources().getColor(R.color.primary_light));
        } else {
            mTvs.get(index).setBackgroundResource(R.drawable.chip_no_background);
            mTvs.get(index).setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private int getDaysSelectedState() {
        int result = 0;
        for (int i = 0; i < MAX_DAYS; i++)
            if (mStateList[i])
                result += Math.pow(2, (MAX_DAYS - i - 1));
        return result;
    }
}

package com.apps.karums.kemitor.presentation.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.karums.kemitor.R;
import com.apps.karums.kemitor.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.apps.karums.kemitor.AppConstants.MAX_DAYS_IN_A_WEEK;

/**
 * Created by karums1 on 6/22/2017.
 */

public class DayOfTheWeekSelector extends LinearLayout {

    private List<TextView> mTvs;
    private boolean[] mStateList;
    private OnDaysChangedListener mDaysChangedListener;

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
        setDaysSelectedState(0);
    }

    public void setOnDaysChangedListener(OnDaysChangedListener listener) {
        mDaysChangedListener = listener;
    }

    public void setDaysSelectedState(int state) {
        mStateList = Utils.getExpandedDaysOfWeek(state);
        for (int i = 0; i < MAX_DAYS_IN_A_WEEK; i++) {
            setDaySelected(i, mStateList[i]);
        }
    }

    private void setListeners() {
        for (int i = 0; i < MAX_DAYS_IN_A_WEEK; i++) {
            final int index = i;
            final TextView textView = mTvs.get(index);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStateList[index] = !mStateList[index];
                    setDaySelected(index, mStateList[index]);
                    mDaysChangedListener.onSelectedDaysChanged(
                            Utils.getShortenedDaysOfWeek(mStateList));
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

    public interface OnDaysChangedListener {
        void onSelectedDaysChanged(int selection);
    }
}

package com.apps.karums.kemitor.data_access;

/**
 * Created by karums1 on 7/10/2017.
 */

public interface IProfileModel {
    String getProfileName();

    void setProfileName(String mProfileName);

    String getUniqueId();

    void setUniqueId(String mUniqueId);

    boolean isEnabled();

    void setIsEnabled(boolean mIsEnabled);

    boolean isProfileLevelSetting();

    void setIsProfileLevelSetting(boolean mIsProfileLevelSetting);

    BlockLevel getProfileBlockLevel();

    void setProfileBlockLevel(BlockLevel profileBlockLevel);

    void setDaysOfTheWeek(int daysOfTheWeek);

    int getDaysOfTheWeek();
}

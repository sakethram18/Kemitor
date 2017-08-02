package com.apps.karums.kemitor.data_access;

/**
 * Created by karums on 3/9/2017.
 */

public class ProfileModel implements IProfileModel {

    private String mUniqueId = "";
    private String mProfileName = "";
    private boolean mIsEnabled;
    private boolean mIsProfileLevelSetting;
    private BlockLevel mProfileBlockLevel = BlockLevel.LevelOne;
    private int mDaysOfTheWeek;

    ProfileModel(String uniqueId, String profileName, boolean isEnabled, boolean
            isProfileLevelSetting, BlockLevel profileBlockLevel) {
        mUniqueId = uniqueId;
        mProfileName = profileName;
        mIsEnabled = isEnabled;
        mIsProfileLevelSetting = isProfileLevelSetting;
        mProfileBlockLevel = profileBlockLevel;
    }

    public ProfileModel(String profileName, boolean isProfileLevelSetting, boolean isEnabled) {
        mProfileName = profileName;
        mIsProfileLevelSetting = isProfileLevelSetting;
        mIsEnabled = isEnabled;
    }

    @Override
    public String getProfileName() {
        return mProfileName;
    }

    @Override
    public void setProfileName(String mProfileName) {
        this.mProfileName = mProfileName;
    }

    @Override
    public String getUniqueId() {
        return mUniqueId;
    }

    @Override
    public void setUniqueId(String mUniqueId) {
        this.mUniqueId = mUniqueId;
    }

    @Override
    public boolean isEnabled() {
        return mIsEnabled;
    }

    @Override
    public void setIsEnabled(boolean mIsEnabled) {
        this.mIsEnabled = mIsEnabled;
    }

    @Override
    public boolean isProfileLevelSetting() {
        return mIsProfileLevelSetting;
    }

    @Override
    public void setIsProfileLevelSetting(boolean mIsProfileLevelSetting) {
        this.mIsProfileLevelSetting = mIsProfileLevelSetting;
    }

    @Override
    public BlockLevel getProfileBlockLevel() {
        return mProfileBlockLevel;
    }

    @Override
    public void setProfileBlockLevel(BlockLevel profileBlockLevel) {
        this.mProfileBlockLevel = profileBlockLevel;
    }

    @Override
    public void setDaysOfTheWeek(int daysOfTheWeek) {
        mDaysOfTheWeek = daysOfTheWeek;
    }

    @Override
    public int getDaysOfTheWeek() {
        return mDaysOfTheWeek;
    }

}

package com.example.karums1.kemitor.data_access;

/**
 * Created by karums1 on 3/9/2017.
 */

public class ProfileModel {

    private String mUniqueId = "";
    private String mProfileName = "";
    private boolean mIsEnabled;
    private boolean mIsProfileLevelSetting;
    private BlockLevel profileBlockLevel = BlockLevel.DefaultNotSet;

    public String getProfileName() {
        return mProfileName;
    }

    public void setProfileName(String mProfileName) {
        this.mProfileName = mProfileName;
    }

    public String getUniqueId() {
        return mUniqueId;
    }

    public void setUniqueId(String mUniqueId) {
        this.mUniqueId = mUniqueId;
    }

    public boolean isEnabled() {
        return mIsEnabled;
    }

    public void setIsEnabled(boolean mIsEnabled) {
        this.mIsEnabled = mIsEnabled;
    }

    public boolean isProfileLevelSetting() {
        return mIsProfileLevelSetting;
    }

    public void setmIsProfileLevelSetting(boolean mIsProfileLevelSetting) {
        this.mIsProfileLevelSetting = mIsProfileLevelSetting;
    }

    public BlockLevel getProfileBlockLevel() {
        return profileBlockLevel;
    }

    public void setProfileBlockLevel(BlockLevel profileBlockLevel) {
        this.profileBlockLevel = profileBlockLevel;
    }

}
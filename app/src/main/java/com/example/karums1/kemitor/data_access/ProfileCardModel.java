package com.example.karums1.kemitor.data_access;

/**
 * Created by karums1 on 3/5/2017.
 */

public class ProfileCardModel {
    private String mProfileName;
    private String mSettingsType;
    private boolean mIsSelected;

    public ProfileCardModel(String profileName, String settingsType, boolean isSelected) {
        mProfileName = profileName;
        mSettingsType = settingsType;
        mIsSelected = isSelected;
    }

    public String getProfileName() {
        return mProfileName;
    }

    public void setProfileName(String mProfileName) {
        this.mProfileName = mProfileName;
    }

    public String getSettingsType() {
        return mSettingsType;
    }

    public void setSettingsType(String mSettingsType) {
        this.mSettingsType = mSettingsType;
    }

    public boolean isProfileSelected() {
        return mIsSelected;
    }

    public void setIsProfileSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

}

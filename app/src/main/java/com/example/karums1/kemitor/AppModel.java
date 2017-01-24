package com.example.karums1.kemitor;

import android.graphics.drawable.Drawable;

/**
 * Created by karums1 on 12/26/2016.
 */

/**
 * Describes a single application entity
 */
public class AppModel {

    private String mUniqueId = "";
    private String mPackageName = "";
    private String mAppName = "";
    private Drawable mAppIcon;
    private boolean mIsSelected;

    public AppModel(String packageName, String appName , Drawable appIcon) {
        this.mPackageName = packageName;
        this.mAppName = appName;
        this.mAppIcon = appIcon;
    }

    public AppModel(String packageName, String appName , Drawable appIcon, boolean isSelected) {
        this.mPackageName = packageName;
        this.mAppName = appName;
        this.mAppIcon = appIcon;
        this.mIsSelected = isSelected;
    }

    public AppModel(String uniqueId, String packageName, boolean isSelected) {
        this.mUniqueId = uniqueId;
        this.mPackageName = packageName;
        this.mIsSelected = isSelected;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getAppName() {
        return mAppName;
    }

    public Drawable getAppIcon() {
        return mAppIcon;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    public void setUniqueId(String id) {
        mUniqueId = id;
    }

    public String getUniqueId() {
        return mUniqueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof AppModel)) {
            return false;
        } else {
            AppModel model = (AppModel)o;
            return this.mPackageName.equals(model.getPackageName());
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mPackageName.hashCode();
        return result;
    }

}

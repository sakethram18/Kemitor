package com.apps.karums.kemitor.data_access;

import android.graphics.drawable.Drawable;

/**
 * Created by karums on 12/26/2016.
 */

/**
 * Describes a single application entity
 */
public class AppModel implements IAppModel {

    private String mUniqueId = "";
    private String mPackageName = "";
    private String mAppName = "";
    private Drawable mAppIcon;
    private boolean mIsSelected;
    private String mProfileId = "";
    private BlockLevel mBlockLevel = BlockLevel.LevelOne;

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

    public AppModel(String uniqueId, String packageName, boolean isSelected, String profileIds,
                    BlockLevel blockLevel) {
        this.mUniqueId = uniqueId;
        this.mPackageName = packageName;
        this.mIsSelected = isSelected;
        // TODO: Make sure to handle multiple profile Id's in this case
        this.mProfileId = profileIds;
        this.mBlockLevel = blockLevel;
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

    public String getProfileId() {
        return mProfileId;
    }

    public void setProfileId(String mProfileId) {
        this.mProfileId = mProfileId;
    }

    public BlockLevel getBlockLevel() {
        return mBlockLevel;
    }

    public void setBlockLevel(BlockLevel mBlockLevel) {
        this.mBlockLevel = mBlockLevel;
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

    @Override
    public boolean getIsLauncherApp() {
        return false;
    }
}

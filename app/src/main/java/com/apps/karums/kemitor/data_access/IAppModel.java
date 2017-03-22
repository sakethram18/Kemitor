package com.apps.karums.kemitor.data_access;

import android.graphics.drawable.Drawable;

/**
 * Created by karums1 on 3/21/2017.
 */

public interface IAppModel {
    String getPackageName();
    String getAppName();
    Drawable getAppIcon();
    boolean isSelected();
    String getProfileId();
    String getUniqueId();
    BlockLevel getBlockLevel();
    boolean getIsLauncherApp();

    void setSelected(boolean selected);
    void setUniqueId(String id);
    void setProfileId(String mProfileId);
    void setBlockLevel(BlockLevel mBlockLevel);

}

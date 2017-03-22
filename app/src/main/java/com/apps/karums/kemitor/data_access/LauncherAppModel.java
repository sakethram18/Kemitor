package com.apps.karums.kemitor.data_access;

import android.graphics.drawable.Drawable;

/**
 * Created by karums1 on 3/21/2017.
 */

public class LauncherAppModel extends AppModel {
    public LauncherAppModel(String packageName, String appName, Drawable appIcon) {
        super(packageName, appName, appIcon);
    }

    public LauncherAppModel(String uniqueId, String packageName, String profileIds, BlockLevel blockLevel) {
        super(uniqueId, packageName, true, profileIds, blockLevel);
    }

    @Override
    public boolean isSelected() {
        return true;
    }

    @Override
    public boolean getIsLauncherApp() {
        return true;
    }
}

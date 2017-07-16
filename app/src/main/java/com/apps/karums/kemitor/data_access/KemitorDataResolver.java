package com.apps.karums.kemitor.data_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by karums on 1/11/2017.
 */

public class KemitorDataResolver {

    private static final String TAG = KemitorDataResolver.class.getSimpleName();
    private Context mContext;

    public KemitorDataResolver(Context context) {
        this.mContext = context;
    }

    /**
     * Method to insert App Model in the database
     *
     * @param model
     * @return
     */
    public boolean insertAppModel(IAppModel model) {
        if (model != null) {
            Log.d(TAG, "Inserting app model...");
            ContentValues values = new ContentValues();
            UUID uniqueId = UUID.randomUUID();
            values.put(ContractConstants.PACKAGES_COLUMN_ID, uniqueId.toString());
            values.put(ContractConstants.PACKAGES_COLUMN_PACKAGE_NAME, model.getPackageName());
            values.put(ContractConstants.PACKAGES_COLUMN_IS_ENABLED, model.isSelected() ? 1:0);
            values.put(ContractConstants.PACKAGES_COLUMN_PROFILE_IDS, model.getProfileId());
            values.put(ContractConstants.PACKAGES_COLUMN_BLOCK_LEVEL, model.getBlockLevel().getLevel());
            values.put(ContractConstants.PACKAGES_COLUMN_IS_LAUNCHER, model.getIsLauncherApp());
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI), ContractConstants
                    .TABLE_PACKAGES);
            Uri resultUri = mContext.getContentResolver().insert(uri, values);
            if (resultUri != null) {
                Log.d(TAG, "App model insertion successful");
                model.setUniqueId(uniqueId.toString());
                return true;
            }
        }
        return false;
    }

    /**
     * Method to insert Profile Model in the database
     *
     * @param model
     * @return
     */
    public boolean insertProfileModel(IProfileModel model) {
        if (model != null) {
            Log.d(TAG, "Inserting profile model...");
            ContentValues values = new ContentValues();
            UUID uniqueId = UUID.randomUUID();
            values.put(ContractConstants.PROFILES_COLUMN_ID, uniqueId.toString());
            values.put(ContractConstants.PROFILES_COLUMN_PROFILE_NAME, model.getProfileName());
            values.put(ContractConstants.PROFILES_COLUMN_IS_ENABLED, model.isEnabled() ? 1:0);
            values.put(ContractConstants.PROFILES_COLUMN_IS_PROFILE_LEVEL_SETTING, model
                    .isProfileLevelSetting() ? 1:0);
            values.put(ContractConstants.PROFILES_COLUMN_BLOCK_LEVEL, model.getProfileBlockLevel().getLevel());
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI), ContractConstants
                    .TABLE_PROFILES);
            Uri resultUri = mContext.getContentResolver().insert(uri, values);
            if (resultUri != null) {
                Log.d(TAG, "Profile model insertion successful");
                model.setUniqueId(uniqueId.toString());
                return true;
            }
        }
        return false;
    }

    public int bulkInsertAppModels(List<IAppModel> appModels) {
        if (appModels != null && !appModels.isEmpty()) {
            ContentValues[] listValues = new ContentValues[appModels.size()];
            for (int i = 0; i < appModels.size(); i++) {
                IAppModel model = appModels.get(i);
                ContentValues values = new ContentValues();
                UUID uniqueId = UUID.randomUUID();
                values.put(ContractConstants.PACKAGES_COLUMN_ID, uniqueId.toString());
                values.put(ContractConstants.PACKAGES_COLUMN_PACKAGE_NAME, model.getPackageName());
                values.put(ContractConstants.PACKAGES_COLUMN_IS_ENABLED, model.isSelected() ? 1:0);
                values.put(ContractConstants.PACKAGES_COLUMN_PROFILE_IDS, model.getProfileId());
                values.put(ContractConstants.PACKAGES_COLUMN_BLOCK_LEVEL, model.getBlockLevel().getLevel());
                values.put(ContractConstants.PACKAGES_COLUMN_IS_LAUNCHER, model.getIsLauncherApp());
                model.setUniqueId(uniqueId.toString());
                listValues[i] = values;
            }
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI), ContractConstants
                    .TABLE_PACKAGES);
            int rowsInserted = mContext.getContentResolver().bulkInsert(uri, listValues);
            if (rowsInserted == appModels.size()) {
                Log.d(TAG, "App model bulk insertion successful...");
                return rowsInserted;
            }
        }
        return 0;
    }

    public int insertProfileAppModelMap(IProfileModel IProfileModel, List<IAppModel> appModels) {
        if (IProfileModel != null && appModels != null && !appModels.isEmpty()) {
            ContentValues[] listValues = new ContentValues[appModels.size()];
            for (int i = 0; i < appModels.size(); i++) {
                IAppModel model = appModels.get(i);
                ContentValues values = new ContentValues();
                UUID uniqueId = UUID.randomUUID();
                values.put(ContractConstants.PP_MAP_ID, uniqueId.toString());
                values.put(ContractConstants.PP_MAP_PACKAGE_ID, IProfileModel.getUniqueId());
                values.put(ContractConstants.PP_MAP_PACKAGE_ID, model.getUniqueId());
                listValues[i] = values;
            }
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                    ContractConstants.TABLE_PP_MAP);
            int rowsInserted = mContext.getContentResolver().bulkInsert(uri, listValues);
            if (rowsInserted == appModels.size()) {
                Log.d(TAG, "Profile - Package map updation successful...");
                return rowsInserted;
            }
        }
        return 0;
    }

    /**
     * Method to update App Model in the database
     *
     * @param model
     * @return
     */
    public int updateAppModel(IAppModel model) {
        if (model != null) {
            ContentValues values = new ContentValues();
            values.put(ContractConstants.PACKAGES_COLUMN_PACKAGE_NAME, model.getPackageName());
            values.put(ContractConstants.PACKAGES_COLUMN_IS_ENABLED, model.isSelected() ? 1:0);
            values.put(ContractConstants.PACKAGES_COLUMN_PROFILE_IDS, model.getProfileId());
            values.put(ContractConstants.PACKAGES_COLUMN_BLOCK_LEVEL, model.getBlockLevel().getLevel());
            values.put(ContractConstants.PACKAGES_COLUMN_IS_LAUNCHER, model.getIsLauncherApp());

            String selection = ContractConstants.PACKAGES_COLUMN_ID + "=?";
            String[] args = new String[1];
            args[0] = model.getUniqueId();
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI), ContractConstants
                    .TABLE_PACKAGES);
            int rowsUpdated = mContext.getContentResolver().update(uri, values, selection, args);
            if (rowsUpdated > 0) {
                Log.d(TAG, "App model updation successful");
                return rowsUpdated;
            }
        }
        return 0;
    }

    /**
     * Method to update Profile Model in the database
     *
     * @param model
     * @return
     */
    public int updateProfileModel(IProfileModel model) {
        if (model != null) {
            ContentValues values = new ContentValues();
            values.put(ContractConstants.PROFILES_COLUMN_PROFILE_NAME, model.getProfileName());
            values.put(ContractConstants.PROFILES_COLUMN_IS_ENABLED, model.isEnabled() ? 1:0);
            values.put(ContractConstants.PROFILES_COLUMN_IS_PROFILE_LEVEL_SETTING, model
                    .isProfileLevelSetting() ? 1:0);
            values.put(ContractConstants.PROFILES_COLUMN_BLOCK_LEVEL, model.getProfileBlockLevel().getLevel());

            String selection = ContractConstants.PROFILES_COLUMN_ID + "=?";
            String[] args = new String[1];
            args[0] = model.getUniqueId();
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI), ContractConstants
                    .TABLE_PROFILES);
            int rowsUpdated = mContext.getContentResolver().update(uri, values, selection, args);
            if (rowsUpdated > 0) {
                Log.d(TAG, "Profile model updation successful");
                return rowsUpdated;
            }
        }
        return 0;
    }

    /**
     * Method to delete app model from the database
     * @param model
     * @return
     */
    public int deleteAppModel(IAppModel model) {
        if (model != null) {
            if (model.getUniqueId().length() != 0) {
                Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                        ContractConstants.TABLE_PACKAGES + "/" + model.getUniqueId());
                return mContext.getContentResolver().delete(uri, null, null);
            }
        }
        return 0;
    }

    /**
     * Method to delete profile model from the database
     * @param model
     * @return
     */
    public int deleteProfileModel(IProfileModel model) {
        if (model != null) {
            if (model.getUniqueId().length() != 0) {
                Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                        ContractConstants.TABLE_PROFILES + "/" + model.getUniqueId());
                return mContext.getContentResolver().delete(uri, null, null);
            }
        }
        return 0;
    }

    /**
     * Selects all records belonging to a profile model
     * @param model
     * @return
     */
    public int deleteRecordsOfProfileModel(IProfileModel model) {
        if (model != null) {
            if (model.getUniqueId().length() != 0) {
                Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                        ContractConstants.TABLE_PP_MAP);
                String selection = ContractConstants.PP_MAP_PROFILE_ID + "=?";
                String[] args = new String[1];
                args[0] = model.getUniqueId();
                return  mContext.getContentResolver().delete(uri, selection, args);
            }
        }
        // Return 0 if none deleted
        return 0;
    }


    /**
     * Method to delete app model from the database
     * @return
     */
    public int deleteAllAppModels() {
        Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                ContractConstants.TABLE_PACKAGES );
        return mContext.getContentResolver().delete(uri, null, null);
    }

    public ArrayList<IAppModel> getAllAppModels() {
        Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                ContractConstants.TABLE_PACKAGES);
        Cursor cursor = mContext.getContentResolver().query(uri, ContractConstants
                .PACKAGES_ALL_COLUMNS, null, null, ContractConstants.DEFAULT_PACKAGES_SORT_ORDER);
        return cursorToAppModel(cursor);
    }

    private ArrayList<IAppModel> cursorToAppModel(Cursor cursor) {
        ArrayList<IAppModel> packages = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                int index = 0;
                String uniqueId = cursor.getString(index++);
                String packageName = cursor.getString(index++);
                int isSelected = cursor.getInt(index);
                String profileIds = cursor.getString(index++);
                int packageBlockLevel = cursor.getInt(index++);
                int isLauncherApp = cursor.getInt(index);

                IAppModel model;
                if (isLauncherApp == 1) {
                    model = new LauncherAppModel(uniqueId, packageName, profileIds,
                            BlockLevel.getBlockLevelFromValue(packageBlockLevel));
                } else {
                    model = new AppModel(uniqueId, packageName, isSelected == 1, profileIds,
                            BlockLevel.getBlockLevelFromValue(packageBlockLevel));
                }
                packages.add(model);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return packages;
    }

    public ArrayList<IAppModel> getAppModelsForProfileModel(IProfileModel profileModel) {
        ArrayList<IAppModel> listOfAppsForProfile = null;
        if (profileModel != null && profileModel.getUniqueId().length() != 0) {
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                    ContractConstants.TABLE_PP_MAP);
            String selection = ContractConstants.PP_MAP_PROFILE_ID + "=?";
            String[] args = new String[1];
            args[0] = profileModel.getUniqueId();
            Cursor cursor = mContext.getContentResolver().query(uri,
                    new String[]{ContractConstants.PP_MAP_PACKAGE_ID}, selection, args,
                    ContractConstants.DEFAULT_PP_MAP_SORT_ORDER);
            ArrayList<String> packageIds = cursorToAppId(cursor);
            String[] appArgs = packageIds.toArray(new String[packageIds.size()]);
            Uri appsUri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                    ContractConstants.TABLE_PACKAGES);
            StringBuilder appsSelection = new StringBuilder();
            appsSelection.append(ContractConstants.PACKAGES_COLUMN_ID + " IN (");
            for (String id: packageIds) {
                appsSelection.append("?,");
            }
            appsSelection.deleteCharAt(appsSelection.length() - 1);
            appsSelection.append(")");

            Cursor appsCursor = mContext.getContentResolver().query(appsUri,
                    ContractConstants.PACKAGES_ALL_COLUMNS, appsSelection.toString(), appArgs,
                    ContractConstants.DEFAULT_PACKAGES_SORT_ORDER);
            listOfAppsForProfile = cursorToAppModel(appsCursor);
        } else {
            FirebaseCrash.logcat(Log.ERROR, TAG, "Profile id to retrieve the list of apps should " +
                    "not be null or empty");
        }
        return listOfAppsForProfile;
    }

    private ArrayList<String> cursorToAppId(Cursor cursor) {
        ArrayList<String> appIds = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String appId = cursor.getString(0);
                appIds.add(appId);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return appIds;
    }

    public ArrayList<IProfileModel> getAllProfileModel() {
        Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                ContractConstants.TABLE_PROFILES);
        Cursor cursor = mContext.getContentResolver().query(uri, ContractConstants
                .PROFILES_ALL_COLUMNS, null, null, ContractConstants.DEFAULT_PROFILES_SORT_ORDER);
        return cursorToProfileModel(cursor);
    }

    private ArrayList<IProfileModel> cursorToProfileModel(Cursor cursor) {
        ArrayList<IProfileModel> profiles = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                int index = 0;
                String uniqueId = cursor.getString(index++);
                String profileName = cursor.getString(index++);
                int isEnabled = cursor.getInt(index++);
                int isProfileLevelSetting = cursor.getInt(index++);
                int profileBlockLevel = cursor.getInt(index);

                IProfileModel model = new ProfileModel(uniqueId, profileName, isEnabled == 1,
                        isProfileLevelSetting == 1, BlockLevel.getBlockLevelFromValue
                        (profileBlockLevel));
                profiles.add(model);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return profiles;
    }
}

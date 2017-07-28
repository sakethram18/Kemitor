package com.apps.karums.kemitor.data_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.apps.karums.kemitor.Utils;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.apps.karums.kemitor.data_access.DaysOfTheWeek.Friday;
import static com.apps.karums.kemitor.data_access.DaysOfTheWeek.Monday;
import static com.apps.karums.kemitor.data_access.DaysOfTheWeek.Saturday;
import static com.apps.karums.kemitor.data_access.DaysOfTheWeek.Sunday;
import static com.apps.karums.kemitor.data_access.DaysOfTheWeek.Thursday;
import static com.apps.karums.kemitor.data_access.DaysOfTheWeek.Tuesday;
import static com.apps.karums.kemitor.data_access.DaysOfTheWeek.Wednesday;

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
    @SuppressWarnings("SimplifiableIfStatement") // More readable this way
    public boolean insertProfileModel(IProfileModel model) {
        // Inserts the profile model into profiles table.
        if (insertProfileModelBasic(model)) {
            // Insert the days of week data into profiles dow table. We should have profile id at
            // this point
            return insertProfileModelDow(model);
        }
        return false;
    }

    /**
     * Inserts the profile model into the profile table
     * @param model
     * @return
     */
    private boolean insertProfileModelBasic(IProfileModel model) {
        if (model != null) {
            Log.d(TAG, "Inserting profile model in profiles table...");
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

    /**
     * Inserts the profile days of the week data into the dow table
     * @param model
     * @return
     */
    private boolean insertProfileModelDow(IProfileModel model) {
        if (model != null) {
            Log.d(TAG, "Inserting profile model in profiles dow table...");
            ContentValues values = new ContentValues();
            UUID uniqueId = UUID.randomUUID();
            values.put(ContractConstants.PROFILES_DOW_ID, uniqueId.toString());
            values.put(ContractConstants.PROFILES_DOW_PROFILE_ID, model.getUniqueId());
            boolean[] daysOfWeek = Utils.getExpandedDaysOfWeek(model.getDaysOfTheWeek());
            values.put(ContractConstants.PROFILES_DOW_SUNDAY, daysOfWeek[0] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_MONDAY, daysOfWeek[1] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_TUESDAY, daysOfWeek[2] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_WEDNESDAY, daysOfWeek[3] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_THURSDAY, daysOfWeek[4] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_FRIDAY, daysOfWeek[5] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_SATURDAY, daysOfWeek[6] ? 1:0);
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI), ContractConstants
                    .TABLE_PROFILES_DOW);
            Uri resultUri = mContext.getContentResolver().insert(uri, values);
            if (resultUri != null) {
                Log.d(TAG, "Profile doe row insertion successful");
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

    public int insertProfileAppModelMap(String profileUniqueId, List<IAppModel> appModels) {
        if (profileUniqueId.length() > 0 && appModels != null && !appModels.isEmpty()) {
            ContentValues[] listValues = new ContentValues[appModels.size()];
            for (int i = 0; i < appModels.size(); i++) {
                IAppModel model = appModels.get(i);
                ContentValues values = new ContentValues();
                UUID uniqueId = UUID.randomUUID();
                values.put(ContractConstants.PP_MAP_ID, uniqueId.toString());
                values.put(ContractConstants.PP_MAP_PROFILE_ID, profileUniqueId);
                values.put(ContractConstants.PP_MAP_PACKAGE_NAME, model.getPackageName());
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
     * Method to update Profile Model table and profile Dow table in the database
     *
     * @param model
     * @return
     */
    public int updateProfileModel(IProfileModel model) {
        // Update profile table first
        if (updateProfileModelBasic(model) > 0) {
            // Update profile dow table next
            int rowsUpdated = updateProfileModelDow(model);
            if (rowsUpdated > 0) {
                return rowsUpdated;
            }
        }
        return 0;
    }

    private int updateProfileModelBasic(IProfileModel model) {
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

    private int updateProfileModelDow(IProfileModel model) {
        if (model != null) {
            ContentValues values = new ContentValues();
            boolean[] daysOfWeek = Utils.getExpandedDaysOfWeek(model.getDaysOfTheWeek());
            values.put(ContractConstants.PROFILES_DOW_SUNDAY, daysOfWeek[0] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_MONDAY, daysOfWeek[1] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_TUESDAY, daysOfWeek[2] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_WEDNESDAY, daysOfWeek[3] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_THURSDAY, daysOfWeek[4] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_FRIDAY, daysOfWeek[5] ? 1:0);
            values.put(ContractConstants.PROFILES_DOW_SATURDAY, daysOfWeek[6] ? 1:0);

            String selection = ContractConstants.PROFILES_DOW_PROFILE_ID + "=?";
            String[] args = new String[1];
            args[0] = model.getUniqueId();
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI), ContractConstants
                    .TABLE_PROFILES_DOW);
            int rowsUpdated = mContext.getContentResolver().update(uri, values, selection, args);
            if (rowsUpdated > 0) {
                Log.d(TAG, "Profile dow table updation successful");
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
    // TODO: Make sure to clear Dow table and Map table at this point
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
     * @param profileUniqueId
     * @return
     */
    public int deleteMapRecordsOfProfileModel(String profileUniqueId) {
        if (profileUniqueId.length() != 0) {
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                    ContractConstants.TABLE_PP_MAP);
            String selection = ContractConstants.PP_MAP_PROFILE_ID + "=?";
            return  mContext.getContentResolver().delete(uri, selection,
                    new String[]{profileUniqueId});
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

    public ArrayList<String> getAppModelsForProfileModel(String profileUniqueId) {
        if (profileUniqueId.length() != 0) {
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                    ContractConstants.TABLE_PP_MAP);
            String selection = ContractConstants.PP_MAP_PROFILE_ID + "=?";
            String[] args = new String[1];
            args[0] = profileUniqueId;
            Cursor cursor = mContext.getContentResolver().query(uri,
                    new String[]{ContractConstants.PP_MAP_PACKAGE_NAME}, selection, args,
                    ContractConstants.DEFAULT_PP_MAP_SORT_ORDER);
            return cursorToAppPackageNames(cursor);
        } else {
            FirebaseCrash.logcat(Log.ERROR, TAG, "Profile id to retrieve the list of apps should " +
                    "not be null or empty");
        }
        return new ArrayList<>();
    }

    private ArrayList<String> cursorToAppPackageNames(Cursor cursor) {
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

    public ArrayList<IProfileModel> getAllProfileModelsBasicInfo() {
        Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                ContractConstants.TABLE_PROFILES);
        Cursor cursor = mContext.getContentResolver().query(uri, ContractConstants
                .PROFILES_ALL_COLUMNS, null, null, ContractConstants.DEFAULT_PROFILES_SORT_ORDER);
        return cursorToProfileModelBasic(cursor);
    }

    public IProfileModel getDetailedProfileModel(String profileModelId) {
        Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                ContractConstants.TABLE_PROFILES);
        String selection = ContractConstants.PROFILES_COLUMN_ID + "=?";
        Cursor cursor = mContext.getContentResolver().query(uri,
                ContractConstants.PROFILES_ALL_COLUMNS, selection, new String[]{profileModelId},
                ContractConstants.DEFAULT_PROFILES_SORT_ORDER);
        ArrayList<IProfileModel> profileModels = cursorToProfileModelBasic(cursor);
        ArrayList<IProfileModel> detailedProfileModels = getProfileModelsDow(profileModels);
        if (detailedProfileModels.size() != 1) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "Multiple profiles exist for a profile id or " +
                    "multiple dow's exist for a profile id");
        }
        return detailedProfileModels.get(0);

    }

    private ArrayList<IProfileModel> getProfileModelsDow(ArrayList<IProfileModel> models) {
        for (IProfileModel model: models) {
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                    ContractConstants.TABLE_PROFILES_DOW);
            String selection = ContractConstants.PROFILES_DOW_PROFILE_ID + "=?";
            Cursor cursor = mContext.getContentResolver().query(uri,
                    ContractConstants.PROFILES_DOW_ALL_COLUMNS, selection, new String[]{model.getUniqueId()},
                    ContractConstants.DEFAULT_PROFILES_DOW_SORT_ORDER);
            int daysOfWeek = cursorToProfileDow(cursor).get(0); // assuming only one record exists
            model.setDaysOfTheWeek(daysOfWeek);
        }
        return models;
    }

    private ArrayList<Integer> cursorToProfileDow(Cursor cursor) {
        ArrayList<Integer> daysOfWeek = new ArrayList<>();
        final int maxDays = 7;
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                boolean[] stateList = new boolean[maxDays];
                int index = 0;
                index += 2; // unique id and profile id are not needed, so skip them
                stateList[0] = cursor.getInt(index++) == 1; // sunday
                stateList[1] = cursor.getInt(index++) == 1; // monday
                stateList[2] = cursor.getInt(index++) == 1; // tuesday
                stateList[3] = cursor.getInt(index++) == 1; // wednesday
                stateList[4] = cursor.getInt(index++) == 1; // thursday
                stateList[5] = cursor.getInt(index++) == 1; // friday
                stateList[6] = cursor.getInt(index) == 1; // saturday

                int result = Utils.getShortenedDaysOfWeek(stateList);
                daysOfWeek.add(result);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return daysOfWeek;
    }

    private ArrayList<IProfileModel> cursorToProfileModelBasic(Cursor cursor) {
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

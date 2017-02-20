package com.example.karums1.kemitor.data_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by karums1 on 1/11/2017.
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
    public boolean insertAppModel(AppModel model) {
        if (model != null) {
            Log.d(TAG, "Inserting app model...");
            ContentValues values = new ContentValues();
            UUID uniqueId = UUID.randomUUID();
            values.put(ContractConstants.PACKAGES_COLUMN_ID, uniqueId.toString());
            values.put(ContractConstants.PACKAGES_COLUMN_PACKAGE_NAME, model.getPackageName());
            values.put(ContractConstants.PACKAGES_COLUMN_IS_ENABLED, model.isSelected() ? 1:0);
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI), ContractConstants
                    .TABLE_PACKAGES);
            Uri resultUri = mContext.getContentResolver().insert(uri, values);
            if (resultUri != null) {
                Log.d(TAG, "Insertion successful");
                model.setUniqueId(uniqueId.toString());
                return true;
            }
        }
        return false;
    }

    public int bulkInsertAppModels(List<AppModel> appModels) {
        if (appModels != null && !appModels.isEmpty()) {
            ContentValues[] listValues = new ContentValues[appModels.size()];
            for (int i = 0; i < appModels.size(); i++) {
                AppModel model = appModels.get(i);
                ContentValues values = new ContentValues();
                UUID uniqueId = UUID.randomUUID();
                values.put(ContractConstants.PACKAGES_COLUMN_ID, uniqueId.toString());
                values.put(ContractConstants.PACKAGES_COLUMN_PACKAGE_NAME, model.getPackageName());
                values.put(ContractConstants.PACKAGES_COLUMN_IS_ENABLED, model.isSelected() ? 1:0);
                model.setUniqueId(uniqueId.toString());
                listValues[i] = values;
            }
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI), ContractConstants
                    .TABLE_PACKAGES);
            int rowsInserted = mContext.getContentResolver().bulkInsert(uri, listValues);
            if (rowsInserted == appModels.size()) {
                Log.d(TAG, "Insertion successful");
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
    public int updateAppModel(AppModel model) {
        if (model != null) {
            ContentValues values = new ContentValues();
            values.put(ContractConstants.PACKAGES_COLUMN_PACKAGE_NAME, model.getPackageName());
            values.put(ContractConstants.PACKAGES_COLUMN_IS_ENABLED, model.isSelected() ? 1:0);

            String selection = ContractConstants.PACKAGES_COLUMN_ID + "=?";
            String[] args = new String[1];
            args[0] = model.getUniqueId();
            Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI), ContractConstants
                    .TABLE_PACKAGES);
            int rowsUpdated = mContext.getContentResolver().update(uri, values, selection, args);
            if (rowsUpdated > 0) {
                Log.d(TAG, "Updation successful");
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
    public int deleteAppModel(AppModel model) {
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
     * Method to delete app model from the database
     * @return
     */
    public int deleteAllAppModels() {
        Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                ContractConstants.TABLE_PACKAGES );
        return mContext.getContentResolver().delete(uri, null, null);
    }

    public Map<AppModel, Boolean> getAllAppModels() {
        Uri uri = Uri.withAppendedPath(Uri.parse(ContractConstants.CONTENT_URI),
                ContractConstants.TABLE_PACKAGES);
        Cursor cursor = mContext.getContentResolver().query(uri, ContractConstants
                .PACKAGES_ALL_COLUMNS, null, null, ContractConstants.DEFAULT_PACKAGES_SORT_ORDER);
        return cursorToAppModel(cursor);
    }

    private Map<AppModel, Boolean> cursorToAppModel(Cursor cursor) {
        Map<AppModel, Boolean> packages = new HashMap<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                int index = 0;
                String uniqueId = cursor.getString(index++);
                String packageName = cursor.getString(index++);
                int isEnabled = cursor.getInt(index);

                AppModel model = new AppModel(uniqueId, packageName, isEnabled == 1);
                packages.put(model, model.isSelected());
                cursor.moveToNext();
            }
            cursor.close();
        }
        return packages;
    }
}

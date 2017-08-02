package com.apps.karums.kemitor.data_access;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by karums on 1/8/2017.
 */

public class KemitorDataProvider extends ContentProvider {

    private static final String TAG = KemitorDataProvider.class.getSimpleName();
    private KemitorOpenHelper mKemitorHelper;

    private static final UriMatcher URI_MATCHER;
    private static final int PACKAGES = 1;
    private static final int PACKAGES_BY_ID = 2;
    private static final int PROFILES = 3;
    private static final int PROFILES_BY_ID = 4;
    private static final int PP_MAP = 5;
    private static final int PP_MAP_BY_ID = 6;
    private static final int PROFILES_DOW = 7;
    private static final int PROFILES_DOW_BY_ID = 8;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(ContractConstants.AUTHORITY, ContractConstants.TABLE_PACKAGES, PACKAGES);
        URI_MATCHER.addURI(ContractConstants.AUTHORITY, ContractConstants.TABLE_PACKAGES + "/*",
                PACKAGES_BY_ID);
        URI_MATCHER.addURI(ContractConstants.AUTHORITY, ContractConstants.TABLE_PROFILES, PROFILES);
        URI_MATCHER.addURI(ContractConstants.AUTHORITY, ContractConstants.TABLE_PROFILES + "/*",
                PROFILES_BY_ID);
        URI_MATCHER.addURI(ContractConstants.AUTHORITY, ContractConstants.TABLE_PP_MAP, PP_MAP);
        URI_MATCHER.addURI(ContractConstants.AUTHORITY, ContractConstants.TABLE_PROFILES + "/*",
                PP_MAP_BY_ID);
        URI_MATCHER.addURI(ContractConstants.AUTHORITY, ContractConstants.TABLE_PROFILES_DOW, PROFILES_DOW);
        URI_MATCHER.addURI(ContractConstants.AUTHORITY, ContractConstants.TABLE_PROFILES_DOW + "/*",
                PROFILES_DOW_BY_ID);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate - Data provider is initialized");
        mKemitorHelper = new KemitorOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriMatch = URI_MATCHER.match(uri);
        switch (uriMatch) {
            case PACKAGES:
                Log.d(TAG, "Querying Packages table...");
                checkPackagesColumns(projection);
                queryBuilder.setTables(ContractConstants.TABLE_PACKAGES);
                break;
            case PACKAGES_BY_ID:
                Log.d(TAG, "Querying Packages table by package ID...");
                checkPackagesColumns(projection);
                queryBuilder.setTables(ContractConstants.TABLE_PACKAGES);
                queryBuilder.appendWhere(ContractConstants.PACKAGES_COLUMN_ID + "=?");
                selectionArgs = new String[1];
                selectionArgs[0] = uri.getLastPathSegment();
                break;
            case PROFILES:
                Log.d(TAG, "Querying Profiles table...");
                checkProfilesColumns(projection);
                queryBuilder.setTables(ContractConstants.TABLE_PROFILES);
                break;
            case PROFILES_BY_ID:
                Log.d(TAG, "Querying Profiles table by profile ID...");
                checkProfilesColumns(projection);
                queryBuilder.setTables(ContractConstants.TABLE_PROFILES);
                queryBuilder.appendWhere(ContractConstants.PROFILES_COLUMN_ID + "=?");
                selectionArgs = new String[1];
                selectionArgs[0] = uri.getLastPathSegment();
                break;
            case PP_MAP:
                Log.d(TAG, "Querying PP map table...");
                checkPpMapColumns(projection);
                queryBuilder.setTables(ContractConstants.TABLE_PP_MAP);
                break;
            case PP_MAP_BY_ID:
                Log.d(TAG, "Querying PP map table by pp map ID...");
                checkPpMapColumns(projection);
                queryBuilder.setTables(ContractConstants.TABLE_PP_MAP);
                queryBuilder.appendWhere(ContractConstants.PP_MAP_ID + "=?");
                selectionArgs = new String[1];
                selectionArgs[0] = uri.getLastPathSegment();
                break;
            case PROFILES_DOW:
                Log.d(TAG, "Querying profiles dow map...");
                checkProfileDowColumns(projection);
                queryBuilder.setTables(ContractConstants.TABLE_PROFILES_DOW);
                break;
            case PROFILES_DOW_BY_ID:
                Log.d(TAG, "Querying profiles dow map by profiles dow id...");
                checkProfileDowColumns(projection);
                queryBuilder.setTables(ContractConstants.TABLE_PROFILES_DOW);
                queryBuilder.appendWhere(ContractConstants.PROFILES_DOW_ID + "=?");
                selectionArgs = new String[1];
                selectionArgs[0] = uri.getLastPathSegment();
                break;
            default:
                Log.e(TAG, "Query initiated with unknown URI Type:\n URI: " + uri + "\nURI " +
                        "Type: " + uriMatch);
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase database = mKemitorHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null,
                null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d(TAG, "Query successfully executed, returning cursor to the caller");
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Log.d(TAG, "Insert operation initiated...");
        SQLiteDatabase database = mKemitorHelper.getWritableDatabase();
        String table;
        int uriMatch = URI_MATCHER.match(uri);
        long id = 0;
        Uri returnValue = null;
        switch (uriMatch) {
            case PACKAGES:
                table = ContractConstants.TABLE_PACKAGES;
                id = database.insertOrThrow(table, null, values);
                returnValue = Uri.parse(ContractConstants.TABLE_PACKAGES + "/" + id);
                break;
            case PROFILES:
                table = ContractConstants.TABLE_PROFILES;
                id = database.insertOrThrow(table, null, values);
                returnValue = Uri.parse(ContractConstants.TABLE_PROFILES + "/" + id);
                break;
            case PP_MAP:
                table = ContractConstants.TABLE_PP_MAP;
                id = database.insert(table, null, values);
                returnValue = Uri.parse(ContractConstants.TABLE_PP_MAP + "/" + id);
                break;
            case PROFILES_DOW:
                table = ContractConstants.TABLE_PROFILES_DOW;
                id = database.insert(table, null, values);
                returnValue = Uri.parse(ContractConstants.TABLE_PROFILES_DOW + "/" + id);
            default:
                Log.e(TAG, "Inserting into an unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d(TAG, "Insert successful.");

        return returnValue;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, ContentValues[] values){
        int numInserted = 0;
        String table = "";

        int uriType = URI_MATCHER.match(uri);

        switch (uriType) {
            case PACKAGES:
                table = ContractConstants.TABLE_PACKAGES;
                break;
            case PROFILES:
                table = ContractConstants.TABLE_PROFILES;
                break;
            case PP_MAP:
                table = ContractConstants.TABLE_PP_MAP;
                break;
            case PROFILES_DOW:
                table = ContractConstants.TABLE_PROFILES_DOW;
                break;
            default:
                Log.e(TAG, "Inserting into an unknown URI: " + uri);
        }
        SQLiteDatabase database = mKemitorHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            for (ContentValues cv : values) {
                long newID = database.insertOrThrow(table, null, cv);
                if (newID <= 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
            }
            database.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            numInserted = values.length;
        } finally {
            database.endTransaction();
        }
        return numInserted;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriMatch = URI_MATCHER.match(uri);
        SQLiteDatabase database = mKemitorHelper.getWritableDatabase();
        int rowsDeleted;
        switch (uriMatch) {
            case PACKAGES:
                rowsDeleted = database.delete(ContractConstants.TABLE_PACKAGES, selection,
                        selectionArgs);
                break;
            case PACKAGES_BY_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(
                            ContractConstants.TABLE_PACKAGES,
                            ContractConstants.PACKAGES_COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = database.delete(
                            ContractConstants.TABLE_PACKAGES,
                            ContractConstants.PACKAGES_COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case PROFILES:
                rowsDeleted = database.delete(ContractConstants.TABLE_PROFILES, selection,
                        selectionArgs);
                break;
            case PROFILES_BY_ID:
                String profileId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(
                            ContractConstants.TABLE_PROFILES,
                            ContractConstants.PROFILES_COLUMN_ID + "=" + profileId,
                            null);
                } else {
                    rowsDeleted = database.delete(
                            ContractConstants.TABLE_PROFILES,
                            ContractConstants.PROFILES_COLUMN_ID + "=" + profileId
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case PP_MAP:
                rowsDeleted = database.delete(ContractConstants.TABLE_PP_MAP, selection,
                        selectionArgs);
                break;
            case PP_MAP_BY_ID:
                String ppMapId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(
                            ContractConstants.TABLE_PP_MAP,
                            ContractConstants.PP_MAP_ID + "=" + ppMapId,
                            null);
                } else {
                    rowsDeleted = database.delete(
                            ContractConstants.TABLE_PP_MAP,
                            ContractConstants.PP_MAP_ID + "=" + ppMapId
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case PROFILES_DOW:
                rowsDeleted = database.delete(ContractConstants.TABLE_PROFILES_DOW, selection,
                        selectionArgs);
                break;
            case PROFILES_DOW_BY_ID:
                String profileDowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(
                            ContractConstants.TABLE_PROFILES_DOW,
                            ContractConstants.PROFILES_DOW_ID + "=" + profileDowId,
                            null);
                } else {
                    rowsDeleted = database.delete(
                            ContractConstants.TABLE_PROFILES_DOW,
                            ContractConstants.PROFILES_DOW_ID + "=" + profileDowId
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriMatch = URI_MATCHER.match(uri);
        SQLiteDatabase database = mKemitorHelper.getWritableDatabase();
        int rowsUpdated;
        switch (uriMatch) {
            case PACKAGES:
                rowsUpdated = database.update(ContractConstants.TABLE_PACKAGES,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PACKAGES_BY_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(ContractConstants.TABLE_PACKAGES,
                            values,
                            ContractConstants.PACKAGES_COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = database.update(ContractConstants.TABLE_PACKAGES,
                            values,
                            ContractConstants.PACKAGES_COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case PROFILES:
                rowsUpdated = database.update(ContractConstants.TABLE_PROFILES,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PROFILES_BY_ID:
                String profileId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(ContractConstants.TABLE_PROFILES,
                            values,
                            ContractConstants.PROFILES_COLUMN_ID + "=" + profileId,
                            null);
                } else {
                    rowsUpdated = database.update(ContractConstants.TABLE_PROFILES,
                            values,
                            ContractConstants.PROFILES_COLUMN_ID + "=" + profileId
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case PP_MAP:
                rowsUpdated = database.update(ContractConstants.TABLE_PP_MAP,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PP_MAP_BY_ID:
                String ppMapId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(ContractConstants.TABLE_PP_MAP,
                            values,
                            ContractConstants.PP_MAP_ID + "=" + ppMapId,
                            null);
                } else {
                    rowsUpdated = database.update(ContractConstants.TABLE_PP_MAP,
                            values,
                            ContractConstants.PP_MAP_ID + "=" + ppMapId
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case PROFILES_DOW:
                rowsUpdated = database.update(ContractConstants.TABLE_PROFILES_DOW,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PROFILES_DOW_BY_ID:
                String profileDowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(ContractConstants.TABLE_PROFILES_DOW,
                            values,
                            ContractConstants.PROFILES_DOW_ID + "=" + profileDowId,
                            null);
                } else {
                    rowsUpdated = database.update(ContractConstants.TABLE_PROFILES_DOW,
                            values,
                            ContractConstants.PROFILES_DOW_ID + "=" + profileDowId
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkPackagesColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> allColumns = new HashSet<>(Arrays.asList(ContractConstants
                    .PACKAGES_ALL_COLUMNS));
            HashSet<String> projectionColumns = new HashSet<>(Arrays.asList(projection));

            if (!allColumns.containsAll(projectionColumns)) {
                Log.e(TAG, "Unknown columns in the packages projection");
                throw new IllegalArgumentException("Unknown columns in the packages projection");
            }
            Log.d(TAG, "Packages projection columns verified");
        }
    }

    private void checkProfilesColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> allColumns = new HashSet<>(Arrays.asList(ContractConstants
                    .PROFILES_ALL_COLUMNS));
            HashSet<String> projectionColumns = new HashSet<>(Arrays.asList(projection));

            if (!allColumns.containsAll(projectionColumns)) {
                Log.e(TAG, "Unknown columns in the profiles projection");
                throw new IllegalArgumentException("Unknown columns in the profiles projection");
            }
            Log.d(TAG, "Profiles projection columns verified");
        }
    }

    private void checkPpMapColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> allColumns = new HashSet<>(Arrays.asList(ContractConstants
                    .PP_MAP_ALL_COLUMNS));
            HashSet<String> projectionColumns = new HashSet<>(Arrays.asList(projection));

            if (!allColumns.containsAll(projectionColumns)) {
                Log.e(TAG, "Unknown columns in the profiles-package map projection");
                throw new IllegalArgumentException("Unknown columns in the profiles-package map " +
                        "projection");
            }
            Log.d(TAG, "Profiles-package map projection columns verified");
        }
    }

    private void checkProfileDowColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> allColumns = new HashSet<>(Arrays.asList(ContractConstants
                    .PROFILES_DOW_ALL_COLUMNS));
            HashSet<String> projectionColumns = new HashSet<>(Arrays.asList(projection));

            if (!allColumns.containsAll(projectionColumns)) {
                Log.e(TAG, "Unknown columns in the profiles dow projection");
                throw new IllegalArgumentException("Unknown columns in the dow " +
                        "projection");
            }
            Log.d(TAG, "Profiles dow projection columns verified");
        }
    }
}

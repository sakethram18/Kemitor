package com.apps.karums.kemitor.data_access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.apps.karums.kemitor.data_access.ContractConstants.TABLE_PACKAGES;
import static com.apps.karums.kemitor.data_access.ContractConstants.TABLE_PP_MAP;
import static com.apps.karums.kemitor.data_access.ContractConstants.TABLE_PROFILES;
import static com.apps.karums.kemitor.data_access.ContractConstants.TABLE_PROFILES_DOW;
import static com.apps.karums.kemitor.data_access.ContractConstants.TABLE_PROFILES_TOD;

/**
 * Created by karums on 1/8/2017.
 */

class KemitorOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "kemitor.db";
    private static final int DB_VERSION = ContractConstants.DATABASE_VERSION;
    private static final String TAG = KemitorOpenHelper.class.getSimpleName();

    private static final String CREATE_PACKAGES_TABLE = "CREATE TABLE " + TABLE_PACKAGES + " (" +
            ContractConstants.PACKAGES_COLUMN_ID + " TEXT NOT NULL PRIMARY KEY, " +
            ContractConstants.PACKAGES_COLUMN_PACKAGE_NAME + " TEXT NOT NULL UNIQUE, " +
            ContractConstants.PACKAGES_COLUMN_IS_ENABLED + " INTEGER, " +
            ContractConstants.PACKAGES_COLUMN_PROFILE_IDS + " TEXT, " +
            ContractConstants.PACKAGES_COLUMN_BLOCK_LEVEL + " INTEGER, " +
            ContractConstants.PACKAGES_COLUMN_IS_LAUNCHER + " INTEGER);";

    private static final String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + " (" +
            ContractConstants.PROFILES_COLUMN_ID + " TEXT NOT NULL PRIMARY KEY, " +
            ContractConstants.PROFILES_COLUMN_PROFILE_NAME + " TEXT NOT NULL, " +
            ContractConstants.PROFILES_COLUMN_IS_ENABLED + " INTEGER, " +
            ContractConstants.PROFILES_COLUMN_IS_PROFILE_LEVEL_SETTING + " INTEGER, " +
            ContractConstants.PROFILES_COLUMN_BLOCK_LEVEL + " INTEGER);";

    private static final String CREATE_PP_MAP_TABLE = "CREATE TABLE " + TABLE_PP_MAP + " (" +
            ContractConstants.PP_MAP_ID + " TEXT NOT NULL PRIMARY KEY, " +
            ContractConstants.PP_MAP_PROFILE_ID + " TEXT NOT NULL, " +
            ContractConstants.PP_MAP_PACKAGE_NAME + " TEXT NOT NULL, " +
            "FOREIGN KEY(" + ContractConstants.PP_MAP_PROFILE_ID + ") REFERENCES " +
            ContractConstants.TABLE_PROFILES + "(" +
            ContractConstants.PROFILES_COLUMN_ID + "))";
    //TODO: Investigate if this foreign key constraint is needed
//            "FOREIGN KEY(" + ContractConstants.PP_MAP_PACKAGE_NAME + ") REFERENCES " +
//            ContractConstants.TABLE_PACKAGES + "(" +
//            ContractConstants.PACKAGES_COLUMN_PACKAGE_NAME + "));";

    private static final String CREATE_PROFILES_DOW_TABLE = "CREATE TABLE " + TABLE_PROFILES_DOW + " (" +
            ContractConstants.PROFILES_DOW_ID + " TEXT NOT NULL PRIMARY KEY, " +
            ContractConstants.PROFILES_DOW_PROFILE_ID + " TEXT NOT NULL, " +
            ContractConstants.PROFILES_DOW_SUNDAY + " INTEGER, " +
            ContractConstants.PROFILES_DOW_MONDAY + " INTEGER, " +
            ContractConstants.PROFILES_DOW_TUESDAY + " INTEGER, " +
            ContractConstants.PROFILES_DOW_WEDNESDAY + " INTEGER, " +
            ContractConstants.PROFILES_DOW_THURSDAY + " INTEGER, " +
            ContractConstants.PROFILES_DOW_FRIDAY + " INTEGER, " +
            ContractConstants.PROFILES_DOW_SATURDAY + " INTEGER, " +
            "FOREIGN KEY(" + ContractConstants.PROFILES_DOW_PROFILE_ID + ") REFERENCES " +
            ContractConstants.TABLE_PROFILES + "(" +
            ContractConstants.PROFILES_COLUMN_ID + "));";

    private static final String CREATE_PROFILES_TOD_TABLE = "CREATE TABLE " + TABLE_PROFILES_TOD
            + " (" + ContractConstants.PROFILES_TOD_ID + " TEXT NOT NULL PRIMARY KEY, " +
            ContractConstants.PROFILES_TOD_PROFILE_ID + " TEXT NOT NULL, " +
            ContractConstants.PROFILES_TOD_START_TIME + " INTEGER, " +
            ContractConstants.PROFILES_TOD_END_TIME + " INTEGER, " +
            "FOREIGN KEY(" + ContractConstants.PROFILES_TOD_PROFILE_ID + ") REFERENCES " +
            ContractConstants.TABLE_PROFILES + "(" + ContractConstants.PROFILES_COLUMN_ID + "));";

    KemitorOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PACKAGES_TABLE);
        db.execSQL(CREATE_PROFILES_TABLE);
        db.execSQL(CREATE_PP_MAP_TABLE);
        db.execSQL(CREATE_PROFILES_DOW_TABLE);
        db.execSQL(CREATE_PROFILES_TOD_TABLE);
        Log.d(TAG, "Kemitor database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PACKAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PP_MAP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES_DOW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES_TOD);
        onCreate(db);
        Log.d(TAG, "Upgrading database from " + oldVersion + "to " + newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}

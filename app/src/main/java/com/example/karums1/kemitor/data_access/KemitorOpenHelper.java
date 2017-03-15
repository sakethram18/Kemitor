package com.example.karums1.kemitor.data_access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.karums1.kemitor.data_access.ContractConstants.TABLE_PROFILES;

/**
 * Created by karums1 on 1/8/2017.
 */

public class KemitorOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "kemitor.db";
    private static final int DB_VERSION = ContractConstants.DATABASE_VERSION;
    private static final String TAG = KemitorOpenHelper.class.getSimpleName();

    private static final String TABLE_PACKAGES = ContractConstants.TABLE_PACKAGES;

    private static final String CREATE_PACKAGES_TABLE = "CREATE TABLE " + TABLE_PACKAGES + " (" +
            ContractConstants.PACKAGES_COLUMN_ID + " TEXT NOT NULL PRIMARY KEY, " +
            ContractConstants.PACKAGES_COLUMN_PACKAGE_NAME + " TEXT NOT NULL, " +
            ContractConstants.PACKAGES_COLUMN_IS_ENABLED + " INTEGER, " +
            ContractConstants.PACKAGES_COLUMN_PROFILE_IDS + " TEXT, " +
            ContractConstants.PACKAGES_COLUMN_BLOCK_LEVEL + " INTEGER);";

    private static final String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + " (" +
            ContractConstants.PROFILES_COLUMN_ID + " TEXT NOT NULL PRIMARY KEY, " +
            ContractConstants.PROFILES_COLUMN_PROFILE_NAME + " TEXT NOT NULL, " +
            ContractConstants.PROFILES_COLUMN_IS_ENABLED + " INTEGER, " +
            ContractConstants.PROFILES_COLUMN_IS_PROFILE_LEVEL_SETTING + " INTEGER, " +
            ContractConstants.PROFILES_COLUMN_BLOCK_LEVEL + " INTEGER);";


    public KemitorOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PACKAGES_TABLE);
        db.execSQL(CREATE_PROFILES_TABLE);
        Log.d(TAG, "Kemitor database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PACKAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        onCreate(db);
        Log.d(TAG, "Upgrading database from " + oldVersion + "to " + newVersion);
    }
}

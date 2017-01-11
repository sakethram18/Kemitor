package com.example.karums1.kemitor.data_access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by karums1 on 1/8/2017.
 */

public class KemitorOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "kemitor.db";
    private static final int DB_VERSION = ContractConstants.DATABASE_VERSION;
    private static final String TAG = KemitorOpenHelper.class.getSimpleName();

    private static final String TABLE_PACKAGES = ContractConstants.TABLE_PACKAGES;

    private static final String CREATE_PACKAGES_TABLE = "CREATE " + TABLE_PACKAGES + " (" +
            ContractConstants.PACKAGES_COLUMN_ID              + " TEXT NOT NULL PRIMARY KEY, "    +
            ContractConstants.PACKAGES_COLUMN_PACKAGE_NAME    + " TEXT NOT NULL, "                +
            ContractConstants.PACKAGES_COLUMN_IS_ENABLED      + " INTEGER);";


    public KemitorOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PACKAGES_TABLE);
        Log.d(TAG, "Kemitor database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PACKAGES);
        onCreate(db);
        Log.d(TAG, "Upgrading database from " + oldVersion + "to " + newVersion);
    }
}

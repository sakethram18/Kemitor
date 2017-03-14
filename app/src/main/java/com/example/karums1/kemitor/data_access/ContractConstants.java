package com.example.karums1.kemitor.data_access;

/**
 * Created by karums1 on 1/8/2017.
 */

public class ContractConstants {

    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_PACKAGES = "packages";
    public static final String PACKAGES_COLUMN_ID = "packages_id";
    public static final String PACKAGES_COLUMN_PACKAGE_NAME = "package_name";
    public static final String PACKAGES_COLUMN_IS_ENABLED = "package_is_enabled";
    public static final String PACKAGES_COLUMN_PROFILE_IDS = "package_profile_ids";
    public static final String PACKAGES_COLUMN_BLOCK_LEVEL = "packages_block_level";
    public static final String AUTHORITY            = "com.example.karums1.kemitordataprovider";
    public static final String CONTENT_URI          = "content://" + AUTHORITY;
    public static final String[] PACKAGES_ALL_COLUMNS = {PACKAGES_COLUMN_ID, PACKAGES_COLUMN_PACKAGE_NAME,
            PACKAGES_COLUMN_IS_ENABLED, PACKAGES_COLUMN_PROFILE_IDS, PACKAGES_COLUMN_BLOCK_LEVEL};

    public static final String COMMA_SPACE = ", ";
    public static final String DEFAULT_PACKAGES_SORT_ORDER = PACKAGES_COLUMN_ID + COMMA_SPACE +
            PACKAGES_COLUMN_PACKAGE_NAME + COMMA_SPACE + PACKAGES_COLUMN_IS_ENABLED + COMMA_SPACE
            + PACKAGES_COLUMN_BLOCK_LEVEL + COMMA_SPACE + PACKAGES_COLUMN_PROFILE_IDS;

}

package com.apps.karums.kemitor.data_access;

/**
 * Created by karums on 1/8/2017.
 */

public class ContractConstants {

    public static final int DATABASE_VERSION = 1;
    public static final String COMMA_SPACE = ", ";

    // Packages table constants
    public static final String TABLE_PACKAGES = "packages";
    public static final String PACKAGES_COLUMN_ID = "packages_id";
    public static final String PACKAGES_COLUMN_PACKAGE_NAME = "package_name";
    public static final String PACKAGES_COLUMN_IS_ENABLED = "package_is_enabled";
    public static final String PACKAGES_COLUMN_PROFILE_IDS = "package_profile_ids";
    public static final String PACKAGES_COLUMN_BLOCK_LEVEL = "packages_block_level";
    public static final String PACKAGES_COLUMN_IS_LAUNCHER = "packages_is_launcher";
    public static final String[] PACKAGES_ALL_COLUMNS = {PACKAGES_COLUMN_ID, PACKAGES_COLUMN_PACKAGE_NAME,
            PACKAGES_COLUMN_IS_ENABLED, PACKAGES_COLUMN_PROFILE_IDS, PACKAGES_COLUMN_BLOCK_LEVEL,
            PACKAGES_COLUMN_IS_LAUNCHER};
    public static final String DEFAULT_PACKAGES_SORT_ORDER = PACKAGES_COLUMN_ID + COMMA_SPACE +
            PACKAGES_COLUMN_PACKAGE_NAME + COMMA_SPACE + PACKAGES_COLUMN_IS_ENABLED + COMMA_SPACE
            + PACKAGES_COLUMN_BLOCK_LEVEL + COMMA_SPACE + PACKAGES_COLUMN_IS_LAUNCHER +
            COMMA_SPACE + PACKAGES_COLUMN_PROFILE_IDS;

    // Profile table constants
    public static final String TABLE_PROFILES = "profiles";
    public static final String PROFILES_COLUMN_ID = "profiles_id";
    public static final String PROFILES_COLUMN_PROFILE_NAME = "profile_name";
    public static final String PROFILES_COLUMN_IS_ENABLED = "profile_is_enabled";
    public static final String PROFILES_COLUMN_IS_PROFILE_LEVEL_SETTING =
            "profile_is_profile_level_setting";
    public static final String PROFILES_COLUMN_BLOCK_LEVEL = "profile_block_level";
    public static final String[] PROFILES_ALL_COLUMNS = {PROFILES_COLUMN_ID,
    PROFILES_COLUMN_PROFILE_NAME, PROFILES_COLUMN_IS_ENABLED,
    PROFILES_COLUMN_IS_PROFILE_LEVEL_SETTING, PROFILES_COLUMN_BLOCK_LEVEL};

    public static final String DEFAULT_PROFILES_SORT_ORDER = PROFILES_COLUMN_ID + COMMA_SPACE +
            PROFILES_COLUMN_PROFILE_NAME + COMMA_SPACE + PROFILES_COLUMN_IS_ENABLED + COMMA_SPACE
            + PROFILES_COLUMN_IS_PROFILE_LEVEL_SETTING + COMMA_SPACE + PROFILES_COLUMN_BLOCK_LEVEL;

    public static final String AUTHORITY            = "com.apps.karums.kemitordataprovider";
    public static final String CONTENT_URI          = "content://" + AUTHORITY;

    // Profile-Package table constants
    public static final String TABLE_PP_MAP = "profile_package_map";
    public static final String PP_MAP_ID = "pp_map_id";
    public static final String PP_MAP_PROFILE_ID = "pp_map_profile_id";
    public static final String PP_MAP_PACKAGE_NAME = "pp_map_package_name";
    public static final String[] PP_MAP_ALL_COLUMNS = {PP_MAP_ID, PP_MAP_PROFILE_ID,
            PP_MAP_PACKAGE_NAME};
    public static final String DEFAULT_PP_MAP_SORT_ORDER = PP_MAP_ID + COMMA_SPACE +
            PP_MAP_PROFILE_ID + COMMA_SPACE + PP_MAP_PACKAGE_NAME;

}

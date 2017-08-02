package com.apps.karums.kemitor.data_access;

/**
 * Created by karums on 1/8/2017.
 */

class ContractConstants {

    static final int DATABASE_VERSION = 1;
    private static final String COMMA_SPACE = ", ";

    // Packages table constants
    static final String TABLE_PACKAGES = "packages";
    static final String PACKAGES_COLUMN_ID = "packages_id";
    static final String PACKAGES_COLUMN_PACKAGE_NAME = "package_name";
    static final String PACKAGES_COLUMN_IS_ENABLED = "package_is_enabled";
    static final String PACKAGES_COLUMN_PROFILE_IDS = "package_profile_ids";
    static final String PACKAGES_COLUMN_BLOCK_LEVEL = "packages_block_level";
    static final String PACKAGES_COLUMN_IS_LAUNCHER = "packages_is_launcher";
    static final String[] PACKAGES_ALL_COLUMNS = {PACKAGES_COLUMN_ID, PACKAGES_COLUMN_PACKAGE_NAME,
            PACKAGES_COLUMN_IS_ENABLED, PACKAGES_COLUMN_PROFILE_IDS, PACKAGES_COLUMN_BLOCK_LEVEL,
            PACKAGES_COLUMN_IS_LAUNCHER};
    static final String DEFAULT_PACKAGES_SORT_ORDER = PACKAGES_COLUMN_ID + COMMA_SPACE +
            PACKAGES_COLUMN_PACKAGE_NAME + COMMA_SPACE + PACKAGES_COLUMN_IS_ENABLED + COMMA_SPACE
            + PACKAGES_COLUMN_BLOCK_LEVEL + COMMA_SPACE + PACKAGES_COLUMN_IS_LAUNCHER +
            COMMA_SPACE + PACKAGES_COLUMN_PROFILE_IDS;

    // Profile table constants
    static final String TABLE_PROFILES = "profiles";
    static final String PROFILES_COLUMN_ID = "profiles_id";
    static final String PROFILES_COLUMN_PROFILE_NAME = "profile_name";
    static final String PROFILES_COLUMN_IS_ENABLED = "profile_is_enabled";
    static final String PROFILES_COLUMN_IS_PROFILE_LEVEL_SETTING =
            "profile_is_profile_level_setting";
    static final String PROFILES_COLUMN_BLOCK_LEVEL = "profile_block_level";
    static final String[] PROFILES_ALL_COLUMNS = {PROFILES_COLUMN_ID,
            PROFILES_COLUMN_PROFILE_NAME, PROFILES_COLUMN_IS_ENABLED,
            PROFILES_COLUMN_IS_PROFILE_LEVEL_SETTING, PROFILES_COLUMN_BLOCK_LEVEL};

    static final String DEFAULT_PROFILES_SORT_ORDER = PROFILES_COLUMN_ID + COMMA_SPACE +
            PROFILES_COLUMN_PROFILE_NAME + COMMA_SPACE + PROFILES_COLUMN_IS_ENABLED + COMMA_SPACE
            + PROFILES_COLUMN_IS_PROFILE_LEVEL_SETTING + COMMA_SPACE + PROFILES_COLUMN_BLOCK_LEVEL;

    static final String AUTHORITY = "com.apps.karums.kemitordataprovider";
    static final String CONTENT_URI = "content://" + AUTHORITY;

    // Profile-Package table constants
    static final String TABLE_PP_MAP = "profile_package_map";
    static final String PP_MAP_ID = "pp_map_id";
    static final String PP_MAP_PROFILE_ID = "pp_map_profile_id";
    static final String PP_MAP_PACKAGE_NAME = "pp_map_package_name";
    static final String[] PP_MAP_ALL_COLUMNS = {PP_MAP_ID, PP_MAP_PROFILE_ID,
            PP_MAP_PACKAGE_NAME};
    static final String DEFAULT_PP_MAP_SORT_ORDER = PP_MAP_ID + COMMA_SPACE +
            PP_MAP_PROFILE_ID + COMMA_SPACE + PP_MAP_PACKAGE_NAME;

    // Profile days of the week table
    static final String TABLE_PROFILES_DOW = "profiles_days_of_week";
    static final String PROFILES_DOW_ID = "profiles_dow_id";
    static final String PROFILES_DOW_PROFILE_ID = "profiles_dow_profile_id";
    static final String PROFILES_DOW_SUNDAY = "profiles_dow_sunday";
    static final String PROFILES_DOW_MONDAY = "profiles_dow_monday";
    static final String PROFILES_DOW_TUESDAY = "profiles_dow_tuesday";
    static final String PROFILES_DOW_WEDNESDAY = "profiles_dow_wednesday";
    static final String PROFILES_DOW_THURSDAY = "profiles_dow_thursday";
    static final String PROFILES_DOW_FRIDAY = "profiles_dow_friday";
    static final String PROFILES_DOW_SATURDAY = "profiles_dow_saturday";
    static final String[] PROFILES_DOW_ALL_COLUMNS = {PROFILES_DOW_ID,
            PROFILES_DOW_PROFILE_ID,
            PROFILES_DOW_SUNDAY, PROFILES_DOW_MONDAY, PROFILES_DOW_TUESDAY, PROFILES_DOW_WEDNESDAY,
            PROFILES_DOW_THURSDAY, PROFILES_DOW_FRIDAY, PROFILES_DOW_SATURDAY};
    static final String DEFAULT_PROFILES_DOW_SORT_ORDER = PROFILES_DOW_ID + COMMA_SPACE +
            PROFILES_DOW_PROFILE_ID +
            COMMA_SPACE + PROFILES_DOW_SUNDAY + COMMA_SPACE + PROFILES_DOW_MONDAY + COMMA_SPACE +
            PROFILES_DOW_TUESDAY + COMMA_SPACE + PROFILES_DOW_WEDNESDAY + COMMA_SPACE +
            PROFILES_DOW_THURSDAY + COMMA_SPACE + PROFILES_DOW_FRIDAY + COMMA_SPACE
            + PROFILES_DOW_SATURDAY;

    // Profile times of the day table
    static final String TABLE_PROFILES_TOD = "profiles_times_of_the_day";
    static final String PROFILES_TOD_ID = "profiles_tod_id";
    static final String PROFILES_TOD_PROFILE_ID = "profiles_tod_profile_id";
    static final String PROFILES_TOD_START_TIME = "profiles_tod_start_time";
    static final String PROFILES_TOD_END_TIME = "profiles_tod_end_time";
    static final String[] PROFILES_TOD_ALL_COLUMNS = {PROFILES_TOD_ID, PROFILES_TOD_PROFILE_ID,
            PROFILES_TOD_START_TIME, PROFILES_TOD_END_TIME};
    static final String DEFAULT_PROFILES_TOD_SORT_ORDER = PROFILES_TOD_ID + COMMA_SPACE +
            PROFILES_TOD_PROFILE_ID + COMMA_SPACE + PROFILES_TOD_START_TIME + COMMA_SPACE +
            PROFILES_TOD_END_TIME;
}

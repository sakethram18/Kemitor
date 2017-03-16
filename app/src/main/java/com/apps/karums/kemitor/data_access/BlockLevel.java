package com.apps.karums.kemitor.data_access;

/**
 * Created by karums on 3/9/2017.
 */
public enum BlockLevel {

    LevelOne(0), // Less stricter
    LevelTwo(1),
    LevelThree(2),
    LevelFour(3); // Much stricter

    private final int mBlockLevel;

    BlockLevel(int level) {
        mBlockLevel = level;
    }

    public int getLevel() {
        return mBlockLevel;
    }

    public static BlockLevel getBlockLevelFromValue(int value) {
        switch (value) {
            case 0: return LevelOne;
            case 1: return LevelTwo;
            case 2: return LevelThree;
            case 3: return LevelFour;
            default:
                throw new IllegalArgumentException("Block level not valid");
        }
    }

}

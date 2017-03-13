package com.example.karums1.kemitor.data_access;

/**
 * Created by karums1 on 3/9/2017.
 */
public enum BlockLevel {

    DefaultNotSet(0),
    LevelOne(1), // Less stricter
    LevelTwo(2),
    LevelThree(3),
    LevelFour(4); // Much stricter

    private final int mBlockLevel;

    BlockLevel(int level) {
        mBlockLevel = level;
    }

    public int getLevel() {
        return mBlockLevel;
    }

}

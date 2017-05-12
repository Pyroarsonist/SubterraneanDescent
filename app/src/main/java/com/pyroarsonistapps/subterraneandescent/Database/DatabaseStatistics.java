package com.pyroarsonistapps.subterraneandescent.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseStatistics extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_STAT = "TABLE_NAME_STAT";
    public static final String STAT_TURN = "STAT_TURN";
    public static final String STAT_GOBLINS = "STAT_GOBLINS";
    public static final String STAT_ARCHERS = "STAT_ARCHERS";
    public static final String STAT_MAGES = "STAT_MAGES";
    public static final String STAT_LEVELS = "STAT_LEVELS";
    public static final String STAT_WINNED_LEVELS = "STAT_WINNED_LEVELS";
    public static final String STAT_RESTORED_ABILITY_TAKEN = "STAT_RESTORED_ABILITY_TAKEN";
    public static final String STAT_OBTAIN_ABILITY_TAKEN = "STAT_OBTAIN_ABILITY_TAKEN";

    public DatabaseStatistics(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String command_stat = "CREATE TABLE + TABLE_NAME_STAT (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " COLUMN_STAT_TURN INTEGER, COLUMN_STAT_GOBLINS INTEGER, COLUMN_STAT_ARCHERS INTEGER, COLUMN_STAT_MAGES INTEGER," +
                " COLUMN_STAT_LEVELS INTEGER, COLUMN_STAT_WINNED_LEVELS INTEGER, COLUMN_STAT_RESTORED_ABILITY_TAKEN INTEGER," +
                " COLUMN_STAT_OBTAIN_ABILITY_TAKEN INTEGER);";
        db.execSQL(command_stat);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

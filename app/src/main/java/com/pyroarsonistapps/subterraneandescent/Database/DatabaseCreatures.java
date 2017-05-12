package com.pyroarsonistapps.subterraneandescent.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseCreatures extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_CREATURES = "TABLE_NAME_CREATURES";
    public static final String IDENTITY = "IDENTITY";
    public static final String CURRENT_HP = "CURRENT_HP";
    public static final String MAX_HP = "MAX_HP";
    public static final String X = "X";
    public static final String Y = "Y";
    public static final String VECTOR = "VECTOR";
    public static final String LAST_X = "LAST_X";
    public static final String LAST_Y = "LAST_Y";
    public static final String IS_ALIVE = "IS_ALIVE";

    public DatabaseCreatures(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String command_creatures = "CREATE TABLE + TABLE_NAME_CREATURES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " COLUMN_IDENTITY INTEGER, COLUMN_CURRENT_HP INTEGER, COLUMN_MAX_HP INTEGER, COLUMN_X INTEGER," +
                " COLUMN_Y INTEGER, COLUMN_VECTOR INTEGER, COLUMN_LAST_X TEXT," +
                " COLUMN_LAST_Y TEXT, COLUMN_IS_ALIVE INTEGER);";
        db.execSQL(command_creatures);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

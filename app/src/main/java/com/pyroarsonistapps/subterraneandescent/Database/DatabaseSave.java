package com.pyroarsonistapps.subterraneandescent.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSave extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_SAVE = "TABLE_NAME_SAVE";
    public static final String LEVEL = "LEVEL";
    public static final String TURN = "TURN";

    public DatabaseSave(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String command_save = "CREATE TABLE + TABLE_NAME_SAVE (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " COLUMN_LEVEL INTEGER, COLUMN_TURN INTEGER);";
        db.execSQL(command_save);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

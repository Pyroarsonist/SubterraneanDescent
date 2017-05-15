package com.pyroarsonistapps.subterraneandescent.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseLevel extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // current level
    private static final String TABLE_NAME_SAVE = "TABLE_NAME_SAVE";
    private static final String LEVEL = "LEVEL";
    private static final String TURN = "TURN";
    private static final String GOBLINS = "GOBLINS";
    private static final String ARCHERS = "ARCHERS";
    private static final String MAGES = "MAGES";
    //TODO double and triple kills

    public DatabaseLevel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
        ContentValues newValues = new ContentValues();
        newValues.put(LEVEL, 0);
        newValues.put(TURN, 0);
        newValues.put(GOBLINS, 0);
        newValues.put(ARCHERS, 0);
        newValues.put(MAGES, 0);
        db.insert(TABLE_NAME_SAVE, null, newValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable(db);
        onCreate(db);
    }


    @Override
    public void onDowngrade
            (SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable(db);
        onCreate(db);
    }

    public static int getInfo(SQLiteDatabase db, boolean newValue, String info) {
        int infoValue;
        if (newValue) {
            infoValue = 0;
        } else {
            Cursor cursor = db.query(TABLE_NAME_SAVE,
                    new String[]{info},
                    null, null, null, null, null);
            cursor.moveToFirst();
            infoValue = cursor.getInt(cursor.getColumnIndex(info));
            cursor.close();
        }
        return infoValue;
    }

    public static void setInfo(SQLiteDatabase db, String info, int infoValue) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(info, infoValue);
        db.update(TABLE_NAME_SAVE, updatedValues, null, null);
    }

    public static void incrementInfo(SQLiteDatabase db, String info) {
        int infoValue = getInfo(db, false, info) + 1;
        setInfo(db, info, infoValue);
    }

    private void deleteTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SAVE);
    }

    private void createTable(SQLiteDatabase db) {
        String command_save = "CREATE TABLE TABLE_NAME_SAVE (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "LEVEL INTEGER,TURN INTEGER);";
        db.execSQL(command_save);
    }

    public static String getTableNameSave() {
        return TABLE_NAME_SAVE;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public static String getARCHERS() {
        return ARCHERS;
    }

    public static String getGOBLINS() {
        return GOBLINS;
    }

    public static String getLEVEL() {
        return LEVEL;
    }

    public static String getMAGES() {
        return MAGES;
    }

    public static String getTURN() {
        return TURN;
    }

}

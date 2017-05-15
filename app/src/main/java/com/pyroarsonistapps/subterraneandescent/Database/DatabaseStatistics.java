package com.pyroarsonistapps.subterraneandescent.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseStatistics extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_STAT = "TABLE_NAME_STAT";
    private static final String STAT_TURN = "STAT_TURN";
    private static final String STAT_GOBLINS = "STAT_GOBLINS";
    private static final String STAT_ARCHERS = "STAT_ARCHERS";
    private static final String STAT_MAGES = "STAT_MAGES";
    private static final String STAT_LEVELS = "STAT_LEVELS";
    private static final String STAT_WINNED_LEVELS = "STAT_WINNED_LEVELS";
    private static final String STAT_RESTORED_ABILITY_TAKEN = "STAT_RESTORED_ABILITY_TAKEN";
    private static final String STAT_OBTAIN_ABILITY_TAKEN = "STAT_OBTAIN_ABILITY_TAKEN";

    public DatabaseStatistics(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
        ContentValues newValues = new ContentValues();
        newValues.put(STAT_TURN, 0);
        newValues.put(STAT_GOBLINS, 0);
        newValues.put(STAT_ARCHERS, 0);
        newValues.put(STAT_MAGES, 0);
        newValues.put(STAT_LEVELS, 0);
        newValues.put(STAT_WINNED_LEVELS, 0);
        newValues.put(STAT_RESTORED_ABILITY_TAKEN, 0);
        newValues.put(STAT_OBTAIN_ABILITY_TAKEN, 0);
        db.insert(TABLE_NAME_STAT, null, newValues);
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
            Cursor cursor = db.query(TABLE_NAME_STAT,
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
        db.update(TABLE_NAME_STAT, updatedValues, null, null);
    }

    public static void incrementInfo(SQLiteDatabase db, String info) {
        int infoValue = getInfo(db, false, info) + 1;
        setInfo(db, info, infoValue);
    }

    private void deleteTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STAT);
    }


    private void createTable(SQLiteDatabase db) {
        String command_stat = "CREATE TABLE TABLE_NAME_STAT (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "STAT_TURN INTEGER, STAT_GOBLINS INTEGER, STAT_ARCHERS INTEGER, STAT_MAGES INTEGER," +
                "STAT_LEVELS INTEGER, STAT_WINNED_LEVELS INTEGER, STAT_RESTORED_ABILITY_TAKEN INTEGER," +
                " STAT_OBTAIN_ABILITY_TAKEN INTEGER);";
        db.execSQL(command_stat);
    }

    public static String getTableNameStat() {
        return TABLE_NAME_STAT;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public static String getStatArchers() {
        return STAT_ARCHERS;
    }

    public static String getStatGoblins() {
        return STAT_GOBLINS;
    }

    public static String getStatLevels() {
        return STAT_LEVELS;
    }

    public static String getStatMages() {
        return STAT_MAGES;
    }

    public static String getStatObtainAbilityTaken() {
        return STAT_OBTAIN_ABILITY_TAKEN;
    }

    public static String getStatRestoredAbilityTaken() {
        return STAT_RESTORED_ABILITY_TAKEN;
    }

    public static String getStatTurn() {
        return STAT_TURN;
    }

    public static String getStatWinnedLevels() {
        return STAT_WINNED_LEVELS;
    }

}

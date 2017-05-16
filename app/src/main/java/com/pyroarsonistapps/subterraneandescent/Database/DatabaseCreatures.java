package com.pyroarsonistapps.subterraneandescent.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pyroarsonistapps.subterraneandescent.Logic.Creature;

import java.util.ArrayList;

public class DatabaseCreatures extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_CREATURES = "TABLE_NAME_CREATURES";
    private static final String IDENTITY = "IDENTITY";
    private static final String CURRENT_HP = "CURRENT_HP";
    private static final String MAX_HP = "MAX_HP";
    private static final String X = "X";
    private static final String Y = "Y";
    private static final String VECTOR = "VECTOR";
    private static final String LAST_X = "LAST_X";
    private static final String LAST_Y = "LAST_Y";
    private static final String IS_ALIVE = "IS_ALIVE";

    public DatabaseCreatures(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
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

    public static void createSave(ArrayList<Creature> creatures, SQLiteDatabase dbCreatures) { //TODO now just creates always new
        deleteTable(dbCreatures);
        createTable(dbCreatures);
        if (creatures != null) {
            for (int i = 0; i < creatures.size(); i++) {
                Creature c = creatures.get(i);
                String lastX = "";
                String lastY = "";
                for (int j = 0; j < c.getLastX().length; j++) {
                    if (c.getLastX()[j] == -1)
                        lastX += "-";
                    else
                        lastX += c.getLastX()[j];
                    if (c.getLastY()[j] == -1)
                        lastY += "-";
                    else
                        lastY += c.getLastY()[j];
                }
                int alive = c.getAlive() ? 1 : 0;
                ContentValues newValues = new ContentValues();
                newValues.put(IDENTITY, c.getIdentity());
                newValues.put(CURRENT_HP, c.getCurrentHP());
                newValues.put(MAX_HP, c.getMaxHP());
                newValues.put(X, c.getX());
                newValues.put(Y, c.getY());
                newValues.put(VECTOR, c.getVector());
                newValues.put(LAST_X, lastX);
                newValues.put(LAST_Y, lastY);
                newValues.put(IS_ALIVE, alive);
                dbCreatures.insert(TABLE_NAME_CREATURES, null, newValues);
                /*String q="INSERT INTO TABLE_NAME_CREATURES(IDENTITY,CURRENT_HP,MAX_HP,X,Y,VECTOR,LAST_X,LAST_Y,IS_ALIVE) VALUES ("+
                        c.getIdentity()+","+c.getCurrentHP()+","+c.getMaxHP()+","+c.getX()+","+c.getY()+","+c.getVector()+","+lastX+","+lastY+","+alive+")";
                dbCreatures.execSQL(q);*/
            }
        }
        getSave(dbCreatures);
    }

    public static ArrayList<Creature> getSave(SQLiteDatabase dbCreatures) {
        ArrayList<Creature> creatures = new ArrayList<>();
        Cursor cursor = dbCreatures.query(TABLE_NAME_CREATURES,
                new String[]{IDENTITY, CURRENT_HP, MAX_HP, X, Y, VECTOR, LAST_X, LAST_Y, IS_ALIVE},
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int identity = cursor.getInt(cursor.getColumnIndex(IDENTITY));
            int currentHP = cursor.getInt(cursor.getColumnIndex(CURRENT_HP));
            int maxHP = cursor.getInt(cursor.getColumnIndex(MAX_HP));
            int x = cursor.getInt(cursor.getColumnIndex(X));
            int y = cursor.getInt(cursor.getColumnIndex(Y));
            int vector = cursor.getInt(cursor.getColumnIndex(VECTOR));
            String lastX = cursor.getString(cursor.getColumnIndex(LAST_X));
            String lastY = cursor.getString(cursor.getColumnIndex(LAST_Y));
            int isAlive = cursor.getInt(cursor.getColumnIndex(IS_ALIVE));
            //Log.i("dan", "iteration check " + identity + " " + currentHP + " " + maxHP + " " + x + " " + y + " " + vector + " " + lastX + " " + lastY + " " + isAlive + " ");
            Creature c = new Creature(identity, currentHP, maxHP, x, y, vector, lastX, lastY, isAlive);
            creatures.add(c);
            cursor.moveToNext();
        }
            cursor.close();
        return creatures;
    }

    /*public static void (SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_NAME_CREATURES, new String[]{CURRENT_HP}, null, null, null, null, null);
        cursor.moveToFirst();
        int hp=cursor.getInt(cursor.getColumnIndex(CURRENT_HP));
        int counter = mSettings.getInt(APP_PREFERENCES_RESTORED_ABILITY_TAKEN, 0) + 1;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_RESTORED_ABILITY_TAKEN, counter);
        editor.apply();
    }*/

    public static void deleteTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CREATURES);
    }

    private static void createTable(SQLiteDatabase db) {
        String command_creatures = "CREATE TABLE TABLE_NAME_CREATURES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "IDENTITY INTEGER, CURRENT_HP INTEGER,MAX_HP INTEGER, X INTEGER," +
                "Y INTEGER,VECTOR INTEGER,LAST_X TEXT," +
                "LAST_Y TEXT,IS_ALIVE INTEGER);";
        db.execSQL(command_creatures);
    }


    public static String getTableNameCreatures() {
        return TABLE_NAME_CREATURES;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }
}

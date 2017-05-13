package com.pyroarsonistapps.subterraneandescent.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

        String command_creatures = "CREATE TABLE TABLE_NAME_CREATURES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "IDENTITY INTEGER, CURRENT_HP INTEGER,MAX_HP INTEGER, X INTEGER," +
                "Y INTEGER,VECTOR INTEGER,LAST_X TEXT," +
                "LAST_Y TEXT,IS_ALIVE INTEGER);";
        db.execSQL(command_creatures);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createSave(ArrayList<Creature> creatures, SQLiteDatabase dbCreatures) {
        if (creatures != null) {
            for (int i = 0; i < creatures.size(); i++) {
                Creature c = creatures.get(i);
                String lastX = "";
                String lastY = "";
                for (int j = 0; j < c.getLastX().length; j++) {
                    lastX += c.getLastX()[j];
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

               /* dbCreatures.update(TABLE_NAME_CREATURES,
                        newValues,
                        null,
                        null);*/
                dbCreatures.insert(TABLE_NAME_CREATURES, null, newValues);
                Log.i("dan", "done db");
            }
        }
    }


    public static String getTableNameCreatures() {
        return TABLE_NAME_CREATURES;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }
}

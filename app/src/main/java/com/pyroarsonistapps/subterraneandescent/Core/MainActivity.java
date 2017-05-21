package com.pyroarsonistapps.subterraneandescent.Core;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pyroarsonistapps.subterraneandescent.Database.DatabaseCreatures;
import com.pyroarsonistapps.subterraneandescent.Database.DatabaseLevel;
import com.pyroarsonistapps.subterraneandescent.Database.DatabaseStatistics;
import com.pyroarsonistapps.subterraneandescent.R;

import java.io.File;

public class MainActivity extends Activity {
    AlertDialog.Builder continueGameOrNot;
    protected static DatabaseCreatures creaturesOpen;
    protected static DatabaseLevel saveLevel;
    protected static DatabaseStatistics statisticsOpen;
    protected static SQLiteDatabase dbCreatures, dbLevel, dbStatistics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDB();
        setContentView(R.layout.activity_main);
    }

    private void initDB() {
        creaturesOpen = new DatabaseCreatures(getApplicationContext(), DatabaseCreatures.getTableNameCreatures(), null, DatabaseCreatures.getDatabaseVersion());
        saveLevel = new DatabaseLevel(getApplicationContext(), DatabaseLevel.getTableNameLevel(), null, DatabaseLevel.getDatabaseVersion());
        statisticsOpen = new DatabaseStatistics(getApplicationContext(), DatabaseStatistics.getTableNameStat(), null, DatabaseStatistics.getDatabaseVersion());

        try {
            dbCreatures = creaturesOpen.getWritableDatabase();
            dbLevel = saveLevel.getWritableDatabase();
            dbStatistics = statisticsOpen.getWritableDatabase();
        } catch (SQLiteException ex) {
            dbCreatures = creaturesOpen.getReadableDatabase();
            dbLevel = saveLevel.getReadableDatabase();
            dbStatistics = statisticsOpen.getReadableDatabase();
        }
    }

    private void init() {
        setDialog();
        Button startNewGame = (Button) findViewById(R.id.start_new_game);
        Button continueGame = (Button) findViewById(R.id.continue_game);
        final boolean canContinue = checkExistingOfSaveFile();
        continueGame.setEnabled(canContinue);
        startNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canContinue)
                    continueGameOrNot.show();
                else
                    startLevelActivity(true);
            }


        });
        continueGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLevelActivity(false);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void startLevelActivity(boolean startNewGame) {
        Intent myIntent = new Intent(MainActivity.this, LoadingScreen.class);
        myIntent.putExtra("isNewLevel", startNewGame);
        MainActivity.this.startActivity(myIntent);
    }

    private void setDialog() {
        final String message = getResources().getString(R.string.question_alert);
        final String yesString = getResources().getString(R.string.start_new_game_answer);
        final String noString = getResources().getString(R.string.continue_answer);

        continueGameOrNot = new AlertDialog.Builder(this);
        continueGameOrNot.setMessage(message);
        continueGameOrNot.setPositiveButton(yesString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                startLevelActivity(true);
            }
        });
        continueGameOrNot.setNegativeButton(noString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                startLevelActivity(false);
            }
        });
        continueGameOrNot.setCancelable(true);
        continueGameOrNot.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });
    }

    private boolean checkExistingOfSaveFile() {
        //Log.i("dan", "" + DatabaseCreatures.saveExists(dbCreatures));
        return DatabaseCreatures.saveExists(dbCreatures);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }


}
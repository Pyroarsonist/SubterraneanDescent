package com.pyroarsonistapps.subterraneandescent.Core;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Creature;
import com.pyroarsonistapps.subterraneandescent.R;
import com.pyroarsonistapps.subterraneandescent.Save;

import java.io.IOException;
import java.util.ArrayList;


public class LevelActivity extends Activity {
    private int level;
    private int turn;
    private int heroHP;
    private int initMaxHeroHP;
    private boolean needToGetSave;
    private ArrayList<Creature> creatures = null;
    private DrawView dv;

    private SharedPreferences mSettings;

    private AlertDialog.Builder gifts;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        needToGetSave = getIntent().getBooleanExtra("needToGetSave", false);
        mSettings = getSharedPreferences(Save.APP_PREFERENCES, Context.MODE_PRIVATE);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onPause() {
        super.onPause();
        needToGetSave = true;
    }

    public void end() {
        boolean won = dv.getWon();
        level = dv.getLevel();
        if (level == dv.getMAXLEVEL()) { //TODO call results
            return;
        }
        if (won) {
            level++;
            heroHP = dv.getHeroHP();
            initMaxHeroHP = dv.getInitMaxHeroHP();
            setGiftDialog();
        }
    }

    private void setGiftDialog() {
        final String message = getResources().getString(R.string.gifts_alert);
        final String restore = getResources().getString(R.string.gifts_restore);
        final String increment_hp = getResources().getString(R.string.gifts_increment_hp);

        gifts = new AlertDialog.Builder(this);
        gifts.setMessage(message);
        gifts.setPositiveButton(restore, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                restoreHP(); //TODO check for levels
                createSplashActivity();
                finish();
            }
        });
        gifts.setNegativeButton(increment_hp, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                incrementHP();
                createSplashActivity();
                finish();
            }
        });
        gifts.setCancelable(false);
        gifts.show();
    }

    private void restoreHP() {
        heroHP = initMaxHeroHP;
        Save.saveRestored(mSettings);
    }

    private void incrementHP() {
        initMaxHeroHP++;
        Save.saveObtain(mSettings);
    }


    private void initFromIntent() {
        if (level == -1 | level == 1) {
            if (needToGetSave) {
                try { //from save
                    Object[] getLevelAndTurnAndCreatures = Save.parseFromSaveFile(getApplicationContext(), creatures);
                    level = (int) getLevelAndTurnAndCreatures[0];
                    turn = (int) getLevelAndTurnAndCreatures[1];
                    creatures = (ArrayList<Creature>) getLevelAndTurnAndCreatures[2];

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                level = 1; //new game
                turn = 1;
                heroHP = 3;
                initMaxHeroHP = 3;
            }
        } else {
            needToGetSave = false; //new level
            heroHP = getIntent().getIntExtra("heroHP", -1);
            turn = 1;
            initMaxHeroHP = getIntent().getIntExtra("initMaxHeroHP", -1);
        }
    }

    private void init() {
        level = getIntent().getIntExtra("onNextLevel", -1);
        initFromIntent();
        if (!needToGetSave) {
            Save.saveCounterOfLevels(mSettings);
            dv = new DrawView(this, level, turn, heroHP, initMaxHeroHP);
        }
        else
            dv = new DrawView(this, level, turn, creatures);
        setContentView(dv);
    }

    private void createSplashActivity() {
        Intent myIntent = new Intent(LevelActivity.this, LoadingScreen.class);
        myIntent.putExtra("onNextLevel", level);
        myIntent.putExtra("heroHP", heroHP);
        myIntent.putExtra("initMaxHeroHP", initMaxHeroHP);
        LevelActivity.this.startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        createPause();
    }

    private void createPause() {
        dv.drawThread.createPauseDialog();
    }

    public SharedPreferences getSettings() {
        return mSettings;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Level Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

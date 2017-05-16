package com.pyroarsonistapps.subterraneandescent.Core;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pyroarsonistapps.subterraneandescent.Database.DatabaseCreatures;
import com.pyroarsonistapps.subterraneandescent.Database.DatabaseLevel;
import com.pyroarsonistapps.subterraneandescent.Database.DatabaseStatistics;
import com.pyroarsonistapps.subterraneandescent.Logic.Creature;
import com.pyroarsonistapps.subterraneandescent.R;

import java.util.ArrayList;

import static com.pyroarsonistapps.subterraneandescent.Core.MainActivity.dbCreatures;
import static com.pyroarsonistapps.subterraneandescent.Core.MainActivity.dbLevel;
import static com.pyroarsonistapps.subterraneandescent.Core.MainActivity.dbStatistics;


public class LevelActivity extends Activity {
    private int level;
    private int turn;
    private int heroHP;
    private int initMaxHeroHP;
    private boolean isNewLevel;
    private boolean going=true;
    private ArrayList<Creature> creatures = null;
    private DrawView dv;

    private AlertDialog.Builder gifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNewLevel = getIntent().getBooleanExtra("isNewLevel", false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isNewLevel = false;
        if(going)
        dv.drawThread.saveGame(getApplicationContext(), dv.drawThread.getLevel(), dv.drawThread.getTurn(), dv.drawThread.getCreatures());
    }

    public void end() {
        boolean won = dv.getWon();
        if (won) {
            level = dv.getLevel();
            if (level == dv.getMAXLEVEL()) { //TODO call results
                going=false;
                this.finish();
                return;
            }
            level++;
            heroHP = dv.getHeroHP();
            initMaxHeroHP = dv.getInitMaxHeroHP();
            setGiftDialog();
        } else {
            this.finish();
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
        DatabaseStatistics.incrementInfo(dbStatistics, DatabaseStatistics.getStatRestoredAbilityTaken());
    }

    private void incrementHP() {
        initMaxHeroHP++;
        DatabaseStatistics.incrementInfo(dbStatistics, DatabaseStatistics.getStatObtainAbilityTaken());
    }


    private void initFromIntent() {
        if (!isNewLevel) {//from save
            level = DatabaseLevel.getInfo(dbLevel, false, DatabaseLevel.getLEVEL());
            turn = DatabaseLevel.getInfo(dbLevel, false, DatabaseLevel.getTURN());
            creatures = DatabaseCreatures.getSave(dbCreatures);
        } else {
            level = getIntent().getIntExtra("onNextLevel", 1);//new level
            turn = 0;
            heroHP = getIntent().getIntExtra("heroHP", 3);
            initMaxHeroHP = getIntent().getIntExtra("initMaxHeroHP", 3);
        }

    }

    private void init() {
       // Log.i("dan", level + " init " + isNewLevel);
        initFromIntent();
        if (isNewLevel) {
            DatabaseStatistics.incrementInfo(dbStatistics, DatabaseStatistics.getStatLevels());
            dv = new DrawView(this, level, turn, heroHP, initMaxHeroHP);
        } else
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


}

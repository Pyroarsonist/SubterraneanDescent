package com.pyroarsonistapps.subterraneandescent.Core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Creature;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        needToGetSave = getIntent().getBooleanExtra("needToGetSave", false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
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
            createSplashActivity();
        }
    }

    private void initFromIntent() {
        if (level == -1 | level == 1) {
            if (needToGetSave) {
                try {
                    Object[] getLevelAndTurnAndCreatures = Save.parseFromSaveFile(getApplicationContext(), creatures);
                    level = (int) getLevelAndTurnAndCreatures[0];
                    turn = (int) getLevelAndTurnAndCreatures[1];
                    creatures = (ArrayList<Creature>) getLevelAndTurnAndCreatures[2];

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                level = 1;
                turn = 1;
                heroHP = 3;
                initMaxHeroHP = 3;
            }
        } else {
            needToGetSave = false;
            heroHP = getIntent().getIntExtra("heroHP", -1);
            turn = 1;
            initMaxHeroHP = getIntent().getIntExtra("initMaxHeroHP", -1);
        }
    }

    private void init() {
        level = getIntent().getIntExtra("onNextLevel", -1);
        initFromIntent();
        if (!needToGetSave)
            dv = new DrawView(this, level,turn, heroHP, initMaxHeroHP);
        else
            dv = new DrawView(this, level,turn, creatures);
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

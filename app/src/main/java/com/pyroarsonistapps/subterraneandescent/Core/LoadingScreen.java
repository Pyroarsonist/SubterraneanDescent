package com.pyroarsonistapps.subterraneandescent.Core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.pyroarsonistapps.subterraneandescent.R;


public class LoadingScreen extends Activity {
    private final int TIME_OF_LOADING = 500; //0.5 sec
    private int level;
    private int heroHP;
    private int initMaxHeroHP;
    private boolean isNewLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        init();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(TIME_OF_LOADING);
                } catch (InterruptedException e) {
                }
                startApp();
                finish();
            }
        }).start();
    }

    private void init() {
        isNewLevel = getIntent().getBooleanExtra("isNewLevel", true);
        heroHP = getIntent().getIntExtra("heroHP", 3);
        initMaxHeroHP = getIntent().getIntExtra("initMaxHeroHP", 3);
        level = getIntent().getIntExtra("onNextLevel", 1);
    }


    private void startApp() {
        Intent myIntent = new Intent(LoadingScreen.this, LevelActivity.class);
        myIntent.putExtra("isNewLevel", isNewLevel);
        myIntent.putExtra("onNextLevel", level);
        myIntent.putExtra("heroHP", heroHP);
        myIntent.putExtra("initMaxHeroHP", initMaxHeroHP);
        LoadingScreen.this.startActivity(myIntent);
    }

}


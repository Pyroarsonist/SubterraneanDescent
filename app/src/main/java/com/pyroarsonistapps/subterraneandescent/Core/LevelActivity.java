package com.pyroarsonistapps.subterraneandescent.Core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Archer;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Creature;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Goblin;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Hero;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Mage;
import com.pyroarsonistapps.subterraneandescent.Save;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static com.pyroarsonistapps.subterraneandescent.Core.MainActivity.LEVELSAVEFILE;


public class LevelActivity extends Activity {
    private int level;
    private int heroHP;
    private int initMaxHeroHP;
    private boolean needToGetSave;
    private ArrayList<Creature> creatures = null;
    private DrawView dv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        needToGetSave = getIntent().getBooleanExtra("needToGetSave", false);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        needToGetSave=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //System.exit(0);
    }

    private void initFromIntent() {
        if (needToGetSave) {
            try {
                Object[] getLevelAndCreatures = Save.parseFromSaveFile(getApplicationContext(),creatures);
                level = (int) getLevelAndCreatures[0];
                creatures = (ArrayList<Creature>) getLevelAndCreatures[1];

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            level = 1;
            heroHP = 3;
            initMaxHeroHP = 3;
        }
    }

    private void init() {
        initFromIntent();
        if (!needToGetSave)
            dv=new DrawView(this, level, heroHP, initMaxHeroHP);
        else
            dv=new DrawView(this, level, creatures);
        setContentView(dv);
    }

    @Override
    public void onBackPressed() {
        createPause();
    }

    private void createPause() {
        dv.drawThread.createPauseDialog();
    }

}

package com.pyroarsonistapps.subterreneandescent.Core;

import android.app.Activity;
import android.os.Bundle;


public class LevelActivity extends Activity {
    private int level;
    private int heroHP;
    private int initMaxHeroHP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFromIntent();
        setContentView(new DrawView(this, level, heroHP, initMaxHeroHP));
    }

    private void initFromIntent() {
        level = 1;
        heroHP = 3;
        initMaxHeroHP = 3;
    }

}

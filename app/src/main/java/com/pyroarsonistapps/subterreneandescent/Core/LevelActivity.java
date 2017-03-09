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
        level = getIntent().getIntExtra("level", 1);
        heroHP = getIntent().getIntExtra("heroHP", 0);
        initMaxHeroHP = getIntent().getIntExtra("initMaxHeroHP", 0);
    }

}

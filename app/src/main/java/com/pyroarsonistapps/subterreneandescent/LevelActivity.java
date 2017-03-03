package com.pyroarsonistapps.subterreneandescent;

import android.app.Activity;
import android.os.Bundle;


public class LevelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));
    }

}

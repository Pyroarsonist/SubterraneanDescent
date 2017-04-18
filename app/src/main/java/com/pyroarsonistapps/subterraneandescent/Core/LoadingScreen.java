package com.pyroarsonistapps.subterraneandescent.Core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.pyroarsonistapps.subterraneandescent.R;


public class LoadingScreen extends Activity {
    final int TIME_OF_LOADING = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

    private void doWork() {
        try {
            Thread.sleep(TIME_OF_LOADING);
        } catch (InterruptedException e) {
        }
    }
    private void startApp() {
        Intent intent = new Intent(LoadingScreen.this, LevelActivity.class); //TODO need put var!
        /* some of putted vars*/
        startActivity(intent);
    }

}


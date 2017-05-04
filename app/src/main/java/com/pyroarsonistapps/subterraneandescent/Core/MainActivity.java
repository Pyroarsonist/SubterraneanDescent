package com.pyroarsonistapps.subterraneandescent.Core;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pyroarsonistapps.subterraneandescent.R;

import java.io.File;

import static com.pyroarsonistapps.subterraneandescent.Save.LEVELSAVEFILE;

public class MainActivity extends Activity {
    AlertDialog.Builder continueGameOrNot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    //createSave(MainActivity.this, 0, null);  //TODO use if else statement later to check if save exists
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
        myIntent.putExtra("needToGetSave", !startNewGame);
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
        File f = new File(getApplicationContext().getFilesDir(), LEVELSAVEFILE);
        // Log.i("dan", "checkExistingOfSaveFile: " + f.exists());
        return f.exists();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
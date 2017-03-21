package com.pyroarsonistapps.subterreneandescent.Core;


import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pyroarsonistapps.subterreneandescent.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    private int level = 1;
    private int heroHP;
    private int initMaxHeroHP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startNewGame = (Button) findViewById(R.id.start_new_game);
        startNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, LevelActivity.class);
                myIntent.putExtra("level", level);
                myIntent.putExtra("heroHP", heroHP);
                myIntent.putExtra("initMaxHeroHP", initMaxHeroHP);
                MainActivity.this.startActivity(myIntent);
            }
        });
        try {
            Log.i("dan", String.valueOf(getLevelFromAssetFile(this)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    int getLevelFromAssetFile(Activity activity) throws IOException {
        int level;
        String filename = "Saves/Level.txt";
        try {
            AssetManager assetManager = this.getAssets();
            InputStreamReader is = new InputStreamReader(assetManager.open(filename));
            BufferedReader in = new BufferedReader(is);
            String word = in.readLine();
            in.close();
            try {
                level = Integer.parseInt(word);
            } catch (Exception e) {
                e.printStackTrace();
                level = 0;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            level = 0;
        }
        return level;
    }


}
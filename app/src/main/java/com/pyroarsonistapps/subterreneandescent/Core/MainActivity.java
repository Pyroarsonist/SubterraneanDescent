package com.pyroarsonistapps.subterreneandescent.Core;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pyroarsonistapps.subterreneandescent.Logic.Creatures.Creature;
import com.pyroarsonistapps.subterreneandescent.R;
import com.pyroarsonistapps.subterreneandescent.Save;

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

public class MainActivity extends Activity implements Save {
    public static final String LEVELSAVE = "saves.txt";
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
                //createSave(MainActivity.this, 0, null);  //TODO use if else statement later to check if save exists
                Intent myIntent = new Intent(MainActivity.this, LevelActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        //TEST

        try {
            Log.i("dan", "getSave: "+getSave(this));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("dan", Arrays.deepToString(this.fileList()));

    }

    @Override
    public int parseFromSaveFile(Context context, ArrayList<Creature> creatures) throws IOException {
        int level;
        String save = getSave(context);
        try {
            level = Integer.parseInt(save);
        } catch (Exception e) {
            e.printStackTrace();
            level = 0;
            Log.i("dan", "Exception from getting level save");
        }
        return level;
    }

    @Override
    public void createSave(Context context, int level, ArrayList<Creature> creatures) {
        final String filename = LEVELSAVE;
        File file = new File(context.getFilesDir(), filename);
        StringBuilder sb = new StringBuilder();
        sb.append(level);
        if (level != 0 & creatures != null) {
            sb.append("\n");
            for (int i = 0; i < creatures.size(); i++) {
                //iteration for i'th creature
                Creature c = creatures.get(i);
                String identity = c.getIdentity() + " ";
                String currentHP = c.getCurrentHP() + " ";
                String HP = c.getHP() + " ";
                String x = c.getX() + " ";
                String y = c.getY() + " ";
                String vector = c.getVector() + " ";
                String lastX = "";
                for (int j = 0; j < c.getLastX().length; j++) {
                    lastX += c.getLastX()[j];
                }
                lastX += " ";
                String lastY = "";
                for (int j = 0; j < c.getLastY().length; j++) {
                    lastY += c.getLastY()[j];
                }
                lastY += " ";
                String isAlive = (c.getAlive()) ? "1" : "0";
                StringBuilder creatureProperty = new StringBuilder();
                creatureProperty.append(identity);
                creatureProperty.append(currentHP);
                creatureProperty.append(HP);
                creatureProperty.append(x);
                creatureProperty.append(y);
                creatureProperty.append(vector);
                creatureProperty.append(lastX);
                creatureProperty.append(lastY);
                creatureProperty.append(isAlive);
                sb.append(creatureProperty);
                sb.append("\n");
            }
        }
        String string = sb.toString();
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getSave(Context context) throws IOException {
        final String filename = LEVELSAVE;
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }



}
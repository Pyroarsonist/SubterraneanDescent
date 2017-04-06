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
import java.util.Objects;
import java.util.Scanner;

import static com.pyroarsonistapps.subterraneandescent.Core.MainActivity.LEVELSAVEFILE;


public class LevelActivity extends Activity implements Save {  //TODO just keep working
    private int level;
    private int heroHP;
    private int initMaxHeroHP;
    private boolean needToGetSave;
    private ArrayList<Creature> creatures = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFromIntent();
        if (!needToGetSave)
            setContentView(new DrawView(this, level, heroHP, initMaxHeroHP));
        else
            setContentView(new DrawView(this, level, creatures));
    }

    private void initFromIntent() {
        needToGetSave = getIntent().getBooleanExtra("needToGetSave", false);
        if (needToGetSave) {
            try {
                Object[] getLevelAndCreatures = parseFromSaveFile(getApplicationContext());
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


    public Object[] parseFromSaveFile(Context context) throws IOException {  //TODO not working
        Object[] getLevelAndCreatures = new Object[2];
        int level;
        Scanner sc = new Scanner(getSave(context));
        try {
            level = sc.nextInt();
            if (sc.hasNext()) {
                creatures = new ArrayList<>();
                while (sc.hasNext()) {
                    Creature c = new Creature();
                    int identity = sc.nextInt();
                    int currentHP = sc.nextInt();
                    int HP = sc.nextInt();
                    int x = sc.nextInt();
                    int y = sc.nextInt();
                    int vector = sc.nextInt();
                    int lastX = sc.nextInt();
                    int lastY = sc.nextInt();
                    int isAlive = sc.nextInt();
                    Log.i("dan", "iteration check " + identity + " " + currentHP + " " + HP + " " + x + " " + y + " " + vector + " " + lastX + " " + lastY + " " + isAlive + " ");
                    switch (identity) {
                        case 0:
                            c = new Hero();
                            break;
                        case 1:
                            c = new Goblin();
                            break;
                        case 2:
                            c = new Archer();
                            break;
                        case 3:
                            c = new Mage();
                            break;
                    }
                    c.setIdentity(identity);
                    c.setCurrentHP(currentHP);
                    c.setHP(HP);
                    c.setX(x);
                    c.setY(y);
                    c.setVector(vector);
                    c.setLastX(lastX);
                    c.setLastY(lastY);
                    c.setAlive(isAlive == 1);
                    creatures.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            level = 0;
            creatures = null;
            Log.i("dan", "Exception from getting level save");
        }
        getLevelAndCreatures[0]=level;
        getLevelAndCreatures[1]=creatures;
        return getLevelAndCreatures;
    }

    @Override
    public void createSave(Context context, int level, ArrayList<Creature> creatures) {
        final String filename = LEVELSAVEFILE;
        File file = new File(context.getFilesDir(), filename);
        Log.i("dan", "createSave: " + file.exists());
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
        final String filename = LEVELSAVEFILE;
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

    @Override
    public int parseFromSaveFile(Context context, ArrayList<Creature> creatures) throws IOException {
        return 0;
    }

}

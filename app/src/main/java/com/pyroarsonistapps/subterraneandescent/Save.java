package com.pyroarsonistapps.subterraneandescent;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Archer;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Creature;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Goblin;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Hero;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Mage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Save {
    public static final String LEVELSAVEFILE = "saves.txt";
    public static final String APP_PREFERENCES = "all_data";
    public static final String APP_PREFERENCES_TURN = "TURN";
    public static final String APP_PREFERENCES_GOBLINS = "GOBLINS";
    public static final String APP_PREFERENCES_ARCHERS = "ARCHERS";
    public static final String APP_PREFERENCES_MAGES = "MAGES";
    public static final String APP_PREFERENCES_LEVELS = "LEVELS";
    public static final String APP_PREFERENCES_WINNED_LEVELS = "WINNED_LEVELS";
    public static final String APP_PREFERENCES_RESTORED_ABILITY_TAKEN = "RESTORE";
    public static final String APP_PREFERENCES_OBTAIN_ABILITY_TAKEN = "OBTAIN";

    public static void saveTurn(SharedPreferences mSettings) {
        int turn = mSettings.getInt(APP_PREFERENCES_TURN, 0) + 1;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_TURN, turn);
        editor.apply();
        //Log.i("dan", "allTurns: " + turn);
    }

    public static void saveCounterOfEnemies(SharedPreferences mSettings, int identity) {
        String app_preference = null;
        switch (identity) {
            case 1: {
                app_preference = APP_PREFERENCES_GOBLINS;
                break;
            }
            case 2: {
                app_preference = APP_PREFERENCES_ARCHERS;
                break;
            }
            case 3: {
                app_preference = APP_PREFERENCES_MAGES;
                break;
            }
        }
        int counter = mSettings.getInt(app_preference, 0) + 1;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(app_preference, counter);
        editor.apply();
        //Log.i("dan", app_preference + " and COUNTER: " + counter);
    }

    public static void saveCounterOfWinnedLevels(SharedPreferences mSettings) {
        int levels = mSettings.getInt(APP_PREFERENCES_WINNED_LEVELS, 0) + 1;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_WINNED_LEVELS, levels);
        editor.apply();
        // Log.i("dan", "winned levels: " + levels);
    }

    public static void saveCounterOfLevels(SharedPreferences mSettings) {
        int levels = mSettings.getInt(APP_PREFERENCES_LEVELS, 0) + 1;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_LEVELS, levels);
        editor.apply();
        //Log.i("dan", "levels: " + levels);
    }

    public static void saveRestored(SharedPreferences mSettings) {
        int counter = mSettings.getInt(APP_PREFERENCES_RESTORED_ABILITY_TAKEN, 0) + 1;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_RESTORED_ABILITY_TAKEN, counter);
        editor.apply();
    }

    public static void saveObtain(SharedPreferences mSettings) {
        int counter = mSettings.getInt(APP_PREFERENCES_OBTAIN_ABILITY_TAKEN, 0) + 1;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_OBTAIN_ABILITY_TAKEN, counter);
        editor.apply();
    }

    public static void createSave(Context context, int level, int turn, ArrayList<Creature> creatures) {
        final String filename = LEVELSAVEFILE;
        StringBuilder sb = new StringBuilder();
        sb.append(level);
        sb.append("\n");
        sb.append(turn);
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
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSave(Context context) throws IOException {
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

    public static Object[] parseFromSaveFile(Context context, ArrayList<Creature> creatures) throws IOException {
        Object[] getLevelAndTurnAndCreatures = new Object[3];
        int level;
        int turn;
        Scanner sc = new Scanner(getSave(context));
        try {
            level = sc.nextInt();
            turn = sc.nextInt();
            if (sc.hasNext()) {
                creatures = new ArrayList<>();
                while (sc.hasNext()) {
                    String identity = sc.next();
                    String currentHP = sc.next();
                    String HP = sc.next();
                    String x = sc.next();
                    String y = sc.next();
                    String vector = sc.next();
                    String lastX = sc.next();
                    String lastY = sc.next();
                    String isAlive = sc.next();
                    //Log.i("dan", "iteration check " + identity + " " + currentHP + " " + HP + " " + x + " " + y + " " + vector + " " + lastX + " " + lastY + " " + isAlive + " ");
                    saveCreature(creatures, identity, currentHP, HP, x, y, vector, lastX, lastY, isAlive);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            level = 0;
            turn = 1;
            creatures = null;
            Log.i("dan", "Exception from parsing level save");
        }
        getLevelAndTurnAndCreatures[0] = level;
        getLevelAndTurnAndCreatures[1] = turn;
        getLevelAndTurnAndCreatures[2] = creatures;
        return getLevelAndTurnAndCreatures;
    }

    public static void saveCreature(ArrayList<Creature> creatures, String identity, String currentHP,
                                    String HP, String x, String y, String vector, String lastX, String lastY,
                                    String isAlive) {
        Creature c = new Creature();
        int identityINT = Integer.parseInt(identity);
        switch (identityINT) {
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
        int currentHPINT = Integer.parseInt(currentHP);
        int HPINT = Integer.parseInt(HP);
        int xINT = Integer.parseInt(x);
        int yINT = Integer.parseInt(y);
        int vectorINT = Integer.parseInt(vector);
        int[] lastXINT = new int[9];
        int[] lastYINT = new int[9];
        int pointer = 0;
        for (int i = 0; i < lastX.length(); i++) {
            if (lastX.charAt(i) == '-') {
                lastXINT[pointer++] = -1;
                i++;
                continue;
            }
            lastXINT[pointer++] = Integer.parseInt(lastX.charAt(i) + "");
        }
        pointer = 0;
        for (int i = 0; i < lastY.length(); i++) {
            if (lastY.charAt(i) == '-') {
                lastYINT[pointer++] = -1;
                i++;
                continue;
            }
            lastXINT[pointer++] = Integer.parseInt(lastX.charAt(i) + "");
        }
        boolean isAliveBOOLEAN = (isAlive.equals("1"));
        c.setCurrentHP(currentHPINT);
        c.setHP(HPINT);
        c.setX(xINT);
        c.setY(yINT);
        c.setVector(vectorINT);
        c.setLastX(lastXINT);
        c.setLastY(lastYINT);
        c.setAlive(isAliveBOOLEAN);
        creatures.add(c);
    }

}

package com.pyroarsonistapps.subterraneandescent;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Archer;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Creature;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Goblin;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Hero;
import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Mage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.pyroarsonistapps.subterraneandescent.Core.MainActivity.LEVELSAVEFILE;

public class Save {
    public static void createSave(Context context, int level, ArrayList<Creature> creatures) {
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
                Log.i("dan",lastX+" BAD "+c.getLastX()[0]); //TODO this problem now why getlast == -1
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

    public static Object[] parseFromSaveFile(Context context,ArrayList<Creature> creatures) throws IOException {
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
                    //Log.i("dan", "iteration check " + identity + " " + currentHP + " " + HP + " " + x + " " + y + " " + vector + " " + lastX + " " + lastY + " " + isAlive + " ");
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
        getLevelAndCreatures[0] = level;
        getLevelAndCreatures[1] = creatures;
        return getLevelAndCreatures;
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
        //TODO need rework parse lastXY
        int pointer = 0;
        for (int i = 0; i < lastX.length(); i++) {
            if (lastX.charAt(i) == '-') {
                lastXINT[pointer] = -1;
                i++;
            }
            lastXINT[pointer] = Integer.parseInt(lastX.charAt(i) + "");
            pointer++;
        }
        pointer=0;
        for (int i = 0; i < lastY.length(); i++) {
            if (lastY.charAt(i) == '-') {
                lastYINT[pointer] = -1;
                i++;
            }
            lastYINT[pointer] = Integer.parseInt(lastY.charAt(i) + "");
            pointer++;
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

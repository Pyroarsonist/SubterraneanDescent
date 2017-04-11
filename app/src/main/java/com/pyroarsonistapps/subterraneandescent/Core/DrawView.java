package com.pyroarsonistapps.subterraneandescent.Core;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Creature;
import com.pyroarsonistapps.subterraneandescent.Logic.Square;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private ArrayList<Creature> creatures;
    protected DrawThread drawThread;
    private int level;
    private int heroHP;
    private int initMaxHeroHP;

    private double targetX, targetY;

    private Square[][] squares;
    private ArrayList<Integer> identities = new ArrayList<>();
    public ArrayList<Integer> valueX = new ArrayList<>();//squares
    public ArrayList<Integer> valueY = new ArrayList<>();
    private int numSqH = 9;
    private int numSqW = 7;
    private boolean[][] availableToGenerate = new boolean[numSqH][numSqW];
    private boolean continued = true;

    private boolean needGenerate = true;


    public DrawView(Context context, int level, int heroHP, int initMaxHeroHP) {
        super(context);
        this.level = level;
        this.heroHP = heroHP;
        this.initMaxHeroHP = initMaxHeroHP;
        needGenerate = true;
        getHolder().addCallback(this);
    }

    public DrawView(Context context, int level, ArrayList<Creature> creatures) {
        super(context);
        this.level = level;
        needGenerate = false;
        this.creatures = creatures;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        //drawThread.saveGame();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (needGenerate) {
            initCreature(0, -1, -1);
            generateMap(level);
            drawThread = new DrawThread(getHolder(), getContext(), heroHP, initMaxHeroHP, identities, valueX, valueY, level);
        } else {
            drawThread = new DrawThread(getHolder(), getContext(), level, creatures, needGenerate);
        }
        squares = drawThread.getSquares();
        drawThread.setRunning(true);
        drawThread.start();
    }

    private void initCreature(int identity, int x, int y) {
        identities.add(identity);
        valueX.add(x);
        valueY.add(y);
    }

    private void generateCreature(int identity, int num) {
        for (int i = 0; i < num; i++) {
            Random rn = new Random();
            int cX = rn.nextInt(numSqW);
            int cY = rn.nextInt(numSqH);
            if (availableToGenerate[cY][cX]) {
                initCreature(identity, cY, cX);
                availableToGenerate[cY][cX] = false;
            } else {
                i--;
            }
        }
    }

    private void generateMap(int level) {
        for (int i = 1; i < numSqH - 3; i++) {
            for (int j = 1; j < numSqW - 2; j++) {
                availableToGenerate[i][j] = true; //TODO need check gen
            }
        }
        switch (level) {
            case 1: {
                generateCreature(1, 2);
                break;
            }

            case 2: {
                generateCreature(1, 2);
                generateCreature(2, 1);
                break;
            }
            case 3: {
                generateCreature(1, 3);
                generateCreature(2, 1);
                break;
            }
            case 4: {
                generateCreature(1, 3);
                generateCreature(2, 2);
                break;
            }
            case 5: {
                generateCreature(1, 3);
                generateCreature(2, 2);
                generateCreature(3, 1);
                break;
            }
//CARE!

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //drawThread.saveGame();
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //checking end of the game

        checkGameEnd();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            targetX = event.getX();
            targetY = event.getY();
            // Log.i("dan", "dot: " + targetX + " " + targetY);
            //drawing = true;
            //xy[0]=x square xy[1]=y;
            int[] xy = drawThread.getSquareNum(targetX, targetY);

            if (xy != null) {
                //Toast.makeText(this.getContext(), xy[0] + " " + xy[1], Toast.LENGTH_SHORT).show();
                if (drawThread.getPaintingSuggestingMoveSquareX() == xy[0] & drawThread.getPaintingSuggestingMoveSquareY() == xy[1] & drawThread.neighboringTiles(xy[0], xy[1])) {
                    if (!drawThread.getOnTile()[drawThread.getPaintingSuggestingMoveSquareY()][drawThread.getPaintingSuggestingMoveSquareX()])
                        //!drawThread.getBanned()
                        drawThread.moveHero(drawThread.getPaintingSuggestingMoveSquareX(), drawThread.getPaintingSuggestingMoveSquareY());
                } else {
                    //  Log.i("dan", "getOnTile: " + drawThread.getOnTile()[xy[1]][xy[0]]);
                    drawThread.paintSquare(xy[0], xy[1]);
                }
            } else {
                //Toast.makeText(this.getContext(), "reset", Toast.LENGTH_SHORT).show();
                drawThread.paintSquare(-1, -1);
            }
            drawThread.setPaintSuggestingMoveSquare(true);
            drawThread.setRepaint(true);
        }

        return true;
    }

    private void checkGameEnd() {
        if (!drawThread.isAlive() & continued)
            end(drawThread.getAllEnemiesDead());
    }

    public void end(boolean allEnemiesDead) {
        continued = false;
        drawThread.saveGame(getContext(), drawThread.getLevel(), drawThread.getCreatures());
        boolean won = allEnemiesDead;
        if (won) {
            Log.i("dan", "WON LEVEL");
            Toast.makeText(this.getContext(), "You won! Congrats!", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("dan", "LOST LEVEL");
            Toast.makeText(this.getContext(), "You lost...", Toast.LENGTH_SHORT).show();
        }
        try {
            drawThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LevelActivity myActivity = (LevelActivity) getContext();
        myActivity.finish();
    }


}
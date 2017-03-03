package com.pyroarsonistapps.subterreneandescent;


import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by A8 on 2/6/2017.
 */

class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;

    private double initX, initY;
    private double targetX, targetY;

    private Square[][] squares;
   /* public ArrayList<Creature> creatures = new ArrayList<>();
    public Creature heroC;*/


    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder(), getContext());
        squares = drawThread.getSquares();
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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
            Log.i("dan", "dot: " + targetX + " " + targetY);
            //drawing = true;
            //xy[0]=x square xy[1]=y;
            int[] xy = drawThread.getSquareNum(targetX, targetY);

            if (xy != null) {
                //Toast.makeText(this.getContext(), xy[0] + " " + xy[1], Toast.LENGTH_SHORT).show();
                if (drawThread.getPaintingSuggestingMoveSquareX() == xy[0] & drawThread.getPaintingSuggestingMoveSquareY() == xy[1] & drawThread.neighboringTiles(xy[0], xy[1])) {
                    drawThread.moveHero(drawThread.getPaintingSuggestingMoveSquareX(), drawThread.getPaintingSuggestingMoveSquareY());
                } else
                    drawThread.paintSquare(xy[0], xy[1]);
            } else {
                //Toast.makeText(this.getContext(), "reset", Toast.LENGTH_SHORT).show();
                drawThread.paintSquare(-1, -1);
            }
            drawThread.setPaintSuggestingMoveSquare(true);
            drawThread.setRepaint(true); //TODO
        }

        return true;
    }

    private void checkGameEnd() {
       /* if (!heroC.isAlive) {
            end();
        }*/
        if (!drawThread.isAlive())
            end();
    }

    public void end() {
        LevelActivity myActivity = (LevelActivity) getContext();
        myActivity.finish();
    }
}
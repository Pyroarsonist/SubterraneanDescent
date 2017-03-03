package com.pyroarsonistapps.subterreneandescent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.util.ArrayList;


class DrawThread extends Thread {

    private Bitmap square, suggestingSquare, hero, goblin, stairs;
    private Context context;
    private int numSqH = 9;
    private int numSqW = 7;
    private Square[][] squares = new Square[numSqH][numSqW]; // [0][0],[0][1]...

    private boolean allEnemiesDead = false;

    private boolean running = false;

    private boolean repaint = true;

    private boolean paintSuggestingMoveSquare = false;

    // hero's x and y == squares x and y
    private int initHeroX = (numSqW - 1) / 2;
    private int initHeroY = numSqH - 1;
    private int initHeroHP;
    private int initMaxHeroHP;

    private int paintingSuggestingMoveSquareX = initHeroX;
    private int paintingSuggestingMoveSquareY = initHeroY;

    private SurfaceHolder surfaceHolder;

    private int possibleMovement = 1;
    private ArrayList<Creature> creatures = new ArrayList<>();

    private int canvasW;
    private int canvasH;
    private final int HPsizeText = 110;


    private Creature heroC;

    private void init(ArrayList<Integer> identities, ArrayList<Integer> valueX, ArrayList<Integer> valueY) {
        if (initHeroHP == 0 & initMaxHeroHP == 0)
            creatures.add(new Hero(initHeroX, initHeroY));
        else
            creatures.add(new Hero(initHeroX, initHeroY, initHeroHP, initMaxHeroHP));
        for (int i = 1; i < identities.size(); i++) {
            if (identities.get(i) == 1)
                creatures.add(new Goblin(valueX.get(i), valueY.get(i)));
        }
        heroC = creatures.get(0);
        Log.i("dan", heroC.getCurrentHP() + " HP!");
        square = BitmapFactory.decodeResource(context.getResources(), R.drawable.square);
        suggestingSquare = BitmapFactory.decodeResource(context.getResources(), R.drawable.testsq);  //TODO
        hero = BitmapFactory.decodeResource(context.getResources(), R.drawable.herox);
        goblin = BitmapFactory.decodeResource(context.getResources(), R.drawable.goblin);
        stairs = BitmapFactory.decodeResource(context.getResources(), R.drawable.stairs);
    }

    DrawThread(SurfaceHolder surfaceHolder, Context c, int HeroHP, int initMaxHeroHP, ArrayList<Integer> identities, ArrayList<Integer> valueX, ArrayList<Integer> valueY) {
        this.surfaceHolder = surfaceHolder;
        initHeroHP = HeroHP;
        this.initMaxHeroHP = initMaxHeroHP;
        context = c;
        init(identities, valueX, valueY);
    }

    void setPaintSuggestingMoveSquare(boolean painting) {
        paintSuggestingMoveSquare = painting;
    }


    void setRunning(boolean running) {
        this.running = running;
    }

    void setRepaint(boolean repainting) {
        this.repaint = repainting;
    }

    public void setAllEnemiesDead(boolean allEnemiesDead) {
        if (!allEnemiesDead)
            Toast.makeText(context, "Now descent on stairs to continue", Toast.LENGTH_SHORT).show(); //TODO opening stairs
        this.allEnemiesDead = allEnemiesDead;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (running) {
            if (repaint) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas == null)
                        continue;
                    setCanvasProps(canvas);
                    setSquareMap(canvas);
                    putSquareMap(canvas);
                    stairsPainting(canvas);
                    heroPaint(canvas);
                    enemyPaint(canvas);
                    suggestMove(canvas);
                    HPPainting(canvas);

                    //ending of painting
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                setRepaint(false);
            }
        }

    }

    private void stairsPainting(Canvas canvas) {
        if (allEnemiesDead) {
            int stairsX = (numSqW - 1) / 2;
            int stairsY = 1;
            float currX = squares[stairsY][stairsX].getX();
            float currY = squares[stairsY][stairsX].getY();
            canvas.drawBitmap(stairs, currX, currY, null);
            if (heroC.getX() == stairsX & heroC.getY() == stairsY) {
                wonGame();
            }
        }
    }

    private void wonGame() {
        Log.i("dan", "WON GAME");//TODO winning legshot
        /*Toast.makeText(this.context, "You won! Congrats!", Toast.LENGTH_SHORT).show();
        try {
            this.sleep(Toast.LENGTH_SHORT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        setRunning(false);
    }

    private void HPPainting(Canvas canvas) { //TODO rework hp
        Log.i("dan", "ENTERED HP");
        float h = squares[numSqH - 1][0].getY() + 3 * square.getHeight();
        float w = squares[numSqH - 1][0].getX();
        Paint p = new Paint();
       /* p.setAntiAlias(true);
        p.setTextSize(16 * context.getResources().getDisplayMetrics().density);
        p.setColor(0xFF000000);
        //p.setColor(Color.RED);*/

        p.setColor(Color.RED);
        p.setTextSize(HPsizeText);
        String text = "HP " + heroC.getCurrentHP() + "/" + heroC.getHP();
        canvas.drawText(text, w, h, p);
    }

    private void enemyPaint(Canvas canvas) {
        for (Creature enemy : creatures) {
            if (enemy.getIdentity() != 0) {
                if (enemy.getAlive())
                    canvas.drawBitmap(goblin, (int) squares[enemy.getY()][enemy.getX()].getX(),
                            (int) squares[enemy.getY()][enemy.getX()].getY(), null);
            }
        }

    }


    private void setCanvasProps(Canvas canvas) {
        canvasW = canvas.getWidth();
        canvasH = canvas.getHeight();
    }

    private void setSquareMap(Canvas canvas) {
        for (int i = 0; i < numSqH; i++) {
            for (int j = 0; j < numSqW; j++) {
                squares[i][j] = new Square(canvas.getWidth() / 2 + square.getWidth() * (j - numSqW / 2) - square.getWidth() / 2,
                        canvas.getHeight() / 2 + square.getHeight() * (i - numSqH / 2) - square.getHeight() / 2);
            }
        }
    }

    private void putSquareMap(Canvas canvas) {
        canvas.drawColor(Color.rgb(79, 80, 81));
        for (int i = 1; i <= numSqH; i++) {
            for (int j = 1; j <= numSqW; j++) {
                canvas.drawBitmap(square, (int) squares[i - 1][j - 1].getX(), (int) squares[i - 1][j - 1].getY(), null);
            }
        }
    }

    private void suggestMove(Canvas canvas) {
        if (paintSuggestingMoveSquare) {
            if (paintingSuggestingMoveSquareX != -1 & paintingSuggestingMoveSquareY != -1)
                if (neighboringTiles(paintingSuggestingMoveSquareX, paintingSuggestingMoveSquareY))
                    canvas.drawBitmap(suggestingSquare, (int) squares[paintingSuggestingMoveSquareY][paintingSuggestingMoveSquareX].getX(),
                            (int) squares[paintingSuggestingMoveSquareY][paintingSuggestingMoveSquareX].getY(), null);
            setPaintSuggestingMoveSquare(false);
            Log.i("dan", "CLOSED PAINT");
        }
    }


    boolean neighboringTiles(int x, int y) {
        if (Math.abs(x - heroC.getX()) <= possibleMovement & Math.abs(y - heroC.getY()) <= possibleMovement) {
            Log.i("dan", "neighboring");
            return true;
        } else
            return false;
    }

    Square[][] getSquares() {
        return squares;
    }

    int[] getSquareNum(double x, double y) {
        for (int i = 0; i < numSqH; i++) {
            for (int j = 0; j < numSqW; j++) {
                // Log.i("dan", "CHECKING: " + i + " " + squares[i][j].getX() + " " + j + " " + squares[i][j].getY());
                if ((x > squares[i][j].getX() & x < squares[i][j].getX() + square.getWidth()) &
                        (y > squares[i][j].getY() & y < squares[i][j].getY() + square.getHeight())) {
                    //Log.i("dan", "OKAY: " + i + " " + j);
                    return new int[]{j, i};  //[0]=x; [1]=y;
                }
            }
        }
        return null;
    }

    void paintSquare(int x, int y) {
        paintingSuggestingMoveSquareX = x;
        paintingSuggestingMoveSquareY = y;
    }

    private void heroPaint(Canvas canvas) {
        canvas.drawBitmap(hero, (int) squares[heroC.getY()][heroC.getX()].getX(),
                (int) squares[heroC.getY()][heroC.getX()].getY(), null);
    }

    int getPaintingSuggestingMoveSquareX() {
        return paintingSuggestingMoveSquareX;
    }

    int getPaintingSuggestingMoveSquareY() {
        return paintingSuggestingMoveSquareY;
    }

    void moveHero(int x, int y) {
        Log.i("dan", "MOVING: " + heroC.getX() + " " + heroC.getY() + " TO " + x + " " + y);
        // vector needed to aim
        setVector(x, y, heroC);
        setLastXYArray(heroC);
        Log.i("dan", "SET VECTOR TO : " + heroC.getVector());
        creatures.get(0).setX(x);
        creatures.get(0).setY(y);
        paintSquare(-1, -1);
        checkHeroKilling();
        checkEnemyDeath();
        enemyTurn();
    }

    private void setLastXYArray(Creature c) { //TODO GOBLINS
        int[] tempX = new int[9];
        int[] tempY = new int[9];
        settingXY9Array(c.getX(), c.getY(), tempX, tempY);
        c.setLastX(tempX);
        c.setLastY(tempY);
    }

    private void setVector(int x, int y, Creature c) {
        int tempX[] = new int[9];
        int tempY[] = new int[9];
        settingXY9Array(c.getX(), c.getY(), tempX, tempY);
        int now = 4;
        for (int i = 0; i < 9; i++) {
            if (x - c.getX() == tempX[i] - tempX[now] & y - c.getY() == tempY[i] - tempY[now]) {
                c.setVector(i);
                break;
            }
        }
    }

    private void checkHeroKilling() {
        int[] tempX = new int[9];
        int[] tempY = new int[9];
        settingXY9Array(heroC.getX(), heroC.getY(), tempX, tempY);
        //check vector killing
        for (Creature enemy : creatures) {
            if (enemy.getIdentity() != 0)
                if (tempX[heroC.getVector()] == enemy.getX() & tempY[heroC.getVector()] == enemy.getY()) {
                    enemy.setCurrentHP(enemy.getCurrentHP() - 1);
                }
        }
        //another
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tempX[i] == heroC.getLastX()[j] & tempY[i] == heroC.getLastY()[j])
                    for (Creature enemy : creatures) {
                        if (enemy.getIdentity() != 0)
                            if (tempX[i] == enemy.getX() & tempY[i] == enemy.getY()) {
                                enemy.setCurrentHP(enemy.getCurrentHP() - 1);
                            }
                    }
            }
        }
    }


    private void checkEnemyDeath() {
        for (Creature enemy : creatures) {
            if (enemy.getIdentity() != 0) {
                if (enemy.getCurrentHP() <= 0)
                    enemy.setAlive(false);
            }
        }
        checkIfAllAreDead();
    }

    private void enemyTurn() {
        for (Creature enemy : creatures) {
            if (enemy.getAlive()) {
                if (enemy.getIdentity() == 1)
                    checkingGoblinTurn(enemy);
            }
        }
    }

    private void checkingGoblinTurn(Creature g) {
        //how is goblin moving
        int tempX[] = new int[9];  //TODO 2 creatures in 1 tile legshot
        int tempY[] = new int[9];
        settingXY9Array(g.getX(), g.getY(), tempX, tempY);
        int dist = (tempX[4] - heroC.getX()) * (tempX[4] - heroC.getX()) + (tempY[4] - heroC.getY()) * (tempY[4] - heroC.getY());
        int min = 4;
        for (int i = 0; i < 9; i++) {
            int currDist = (tempX[i] - heroC.getX()) * (tempX[i] - heroC.getX()) + (tempY[i] - heroC.getY()) * (tempY[i] - heroC.getY());
            if (dist > currDist) {
                dist = currDist;
                min = i;
            }
        }
        for (Creature c :
                creatures) {
            if (tempX[min] == c.getX() & tempY[min] == c.getY())
                min = 4;
        }

        for (int i = 8; i > -1; i--) {
            int currDist = (tempX[i] - heroC.getX()) * (tempX[i] - heroC.getX()) + (tempY[i] - heroC.getY()) * (tempY[i] - heroC.getY());
            if (dist > currDist) {
                dist = currDist;
                min = i;
            }
        }

        for (Creature c :
                creatures) {
            if (tempX[min] == c.getX() & tempY[min] == c.getY())
                min = 4;
        }

        //premove
        setVector(tempX[min], tempY[min], g);
        setLastXYArray(g);

        g.setX(tempX[min]);
        g.setY(tempY[min]);
        Log.i("dan", "MOVING GOBLING: " + tempX[min] + " " + tempY[min]);
        checkGoblinKilling(g);
    }

    private void checkGoblinKilling(Creature g) {
        int[] tempX = new int[9];
        int[] tempY = new int[9];
        settingXY9Array(g.getX(), g.getY(), tempX, tempY);
        //check vector killing

        if (tempX[g.getVector()] == heroC.getX() & tempY[g.getVector()] == heroC.getY()) {
            decrementHerosHp();
        }
        //another
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tempX[i] == g.getLastX()[j] & tempY[i] == g.getLastY()[j])
                    if (tempX[i] == heroC.getX() & tempY[i] == heroC.getY()) {
                        decrementHerosHp();
                    }
            }
        }
    }

    private void checkIfAllAreDead() {
        for (Creature c :
                creatures) {
            if (c.getIdentity() != 0)
                if (c.getAlive())
                    return;
        }
        Log.i("dan", "enemies killed,opening the stairs");
        setAllEnemiesDead(true);
    }

    private void decrementHerosHp() {
        heroC.setCurrentHP(heroC.getCurrentHP() - 1);
        Log.i("dan", "DAMAGING HERO, NOW HERO'S HP: " + heroC.getCurrentHP() + "/" + heroC.getHP());
        checkHerosDeath();
    }

    private void checkHerosDeath() {
        if (heroC.getCurrentHP() <= 0) {
            heroC.setAlive(false);
            endGame();
        }

    }

    private void endGame() {
        Log.i("dan", "END OF GAME");
        Toast.makeText(this.context, "You lost...", Toast.LENGTH_SHORT).show();
        setRunning(false);
    }

    private void settingXY9Array(int x, int y, int[] arrX, int[] arrY) {
        //      [0] [1] [2]
        //      [3] [4] [5]
        //      [6] [7] [8]
        for (int i = 0; i < 9; i++) {
            arrY[i] = y + i / 3 - 1;
        }
        arrX[0] = x - 1;
        arrX[1] = x;
        arrX[2] = x + 1;
        arrX[3] = x - 1;
        arrX[4] = x;
        arrX[5] = x + 1;
        arrX[6] = x - 1;
        arrX[7] = x;
        arrX[8] = x + 1;
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    public boolean getAllEnemiesDead() {
        return allEnemiesDead;
    }
}
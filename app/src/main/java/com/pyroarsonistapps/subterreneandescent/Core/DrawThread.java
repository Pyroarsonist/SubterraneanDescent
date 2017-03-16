package com.pyroarsonistapps.subterreneandescent.Core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.pyroarsonistapps.subterreneandescent.Logic.Creatures.*;
import com.pyroarsonistapps.subterreneandescent.R;
import com.pyroarsonistapps.subterreneandescent.Logic.Square;

import java.util.ArrayList;


class DrawThread extends Thread {

    private Bitmap square, suggestingSquare, hero, goblin, stairs, banned_square, mage, archer;
    private Context context;
    private int numSqH = 9;
    private int numSqW = 7;
    private Square[][] squares = new Square[numSqH][numSqW]; // [0][0],[0][1]...
    private boolean[][] onTile = new boolean[numSqH][numSqW];
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
        setOnTile(initHeroX, initHeroY, true);
        for (int i = 1; i < identities.size(); i++) {
            if (identities.get(i) == 1) {
                creatures.add(new Goblin(valueX.get(i), valueY.get(i)));
                setOnTile(valueX.get(i), valueY.get(i), true);
            }
            if (identities.get(i) == 2) {
                creatures.add(new Archer(valueX.get(i), valueY.get(i)));
                setOnTile(valueX.get(i), valueY.get(i), true);
            }
            if (identities.get(i) == 3) {
                creatures.add(new Mage(valueX.get(i), valueY.get(i)));
                setOnTile(valueX.get(i), valueY.get(i), true);
            }
        }
        heroC = creatures.get(0);
        Log.i("dan", heroC.getCurrentHP() + " HP!");
        square = BitmapFactory.decodeResource(context.getResources(), R.drawable.square);
        suggestingSquare = BitmapFactory.decodeResource(context.getResources(), R.drawable.suggesting_square);
        hero = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero);
        goblin = BitmapFactory.decodeResource(context.getResources(), R.drawable.goblin);
        stairs = BitmapFactory.decodeResource(context.getResources(), R.drawable.stairs);
        banned_square = BitmapFactory.decodeResource(context.getResources(), R.drawable.banned_square);
        archer = BitmapFactory.decodeResource(context.getResources(), R.drawable.archer);
        mage = BitmapFactory.decodeResource(context.getResources(), R.drawable.mage);
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

    public void setOnTile(int x, int y, boolean whats) {
        onTile[y][x] = whats;
    }

    public boolean[][] getOnTile() {
        return onTile;
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
                    creaturePaint(canvas);
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
        //Log.i("dan", "WON GAME");//TODO winning legshot
        /*Toast.makeText(this.context, "You won! Congrats!", Toast.LENGTH_SHORT).show();
        try {
            this.sleep(Toast.LENGTH_SHORT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        setRunning(false);
    }

    private void HPPainting(Canvas canvas) { //TODO rework hp
        //Log.i("dan", "ENTERED HP");
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

    private void creaturePaint(Canvas canvas) {
        for (Creature cr : creatures) {
            if (cr.getAlive()) {
                Bitmap creaturesBitmap;
                switch (cr.getIdentity()) {
                    case 1:
                        creaturesBitmap = goblin;
                        break;
                    case 2:
                        creaturesBitmap = archer;
                        break;
                    case 3:
                        creaturesBitmap = mage;
                        break;
                    default:
                        creaturesBitmap = hero;
                        break;
                }
                canvas.drawBitmap(creaturesBitmap, (int) squares[cr.getY()][cr.getX()].getX(),
                        (int) squares[cr.getY()][cr.getX()].getY(), null);
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
        if (paintSuggestingMoveSquare & neighboringTiles(paintingSuggestingMoveSquareX, paintingSuggestingMoveSquareY)) {
            if (paintingSuggestingMoveSquareX != -1 & paintingSuggestingMoveSquareY != -1) {
                Bitmap current;
                if (!onTile[paintingSuggestingMoveSquareY][paintingSuggestingMoveSquareX]) {
                    current = suggestingSquare;
                } else {
                    current = banned_square;
                }
                canvas.drawBitmap(current, (int) squares[paintingSuggestingMoveSquareY][paintingSuggestingMoveSquareX].getX(),
                        (int) squares[paintingSuggestingMoveSquareY][paintingSuggestingMoveSquareX].getY(), null);
                setPaintSuggestingMoveSquare(false);
            }
        }
    }


    boolean neighboringTiles(int x, int y) {
        if (Math.abs(x - heroC.getX()) <= possibleMovement & Math.abs(y - heroC.getY()) <= possibleMovement) {
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


    int getPaintingSuggestingMoveSquareX() {
        return paintingSuggestingMoveSquareX;
    }

    int getPaintingSuggestingMoveSquareY() {
        return paintingSuggestingMoveSquareY;
    }

    void moveHero(int x, int y) {
        //Log.i("dan", "MOVING: " + heroC.getX() + " " + heroC.getY() + " TO " + x + " " + y);
        setOnTile(heroC.getX(), heroC.getY(), false);
        // vector needed to aim
        setVector(x, y, heroC);
        setLastXYArray(heroC);
        //Log.i("dan", "SET VECTOR TO : " + heroC.getVector());
        creatures.get(0).setX(x);
        creatures.get(0).setY(y);
        setOnTile(x, y, true);
        paintSquare(-1, -1);
        checkHeroHarrasing();
        checkEnemyDeath();
        enemyTurn();
    }

    private void setLastXYArray(Creature c) {
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

    private void checkHeroHarrasing() {
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
                if (enemy.getCurrentHP() <= 0 & enemy.getAlive()) {
                    setOnTile(enemy.getX(), enemy.getY(), false);
                    enemy.setAlive(false);
                }
            }
        }
        checkIfAllAreDead();
    }

    private void enemyTurn() {
        for (Creature enemy : creatures) {
            if (enemy.getAlive()) {
                if (enemy.getIdentity() == 1)
                    checkingGoblinTurn(enemy);
                else if (enemy.getIdentity() == 2)
                    checkingArcherTurn(enemy);
                else if (enemy.getIdentity() == 3)
                    checkingMageTurn(enemy);
            }
        }
    }


    private int getWayToSquare(int[] tempX, int[] tempY, int x, int y) {
        int dist = (tempX[4] - x) * (tempX[4] - x) + (tempY[4] - y) * (tempY[4] - y);
        int min = 4;
        for (int i = 0; i < 9; i++) {
            if (tempX[i] == -1 | tempY[i] == -1)
                continue;
            if (onTile[tempY[i]][tempX[i]])
                continue;
            int currDist = (tempX[i] - x) * (tempX[i] - x) + (tempY[i] - y) * (tempY[i] - y);
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
            if (tempX[i] == -1 | tempY[i] == -1)
                continue;
            if (onTile[tempY[i]][tempX[i]])
                continue;
            int currDist = (tempX[i] - x) * (tempX[i] - x) + (tempY[i] - y) * (tempY[i] - y);
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
        return min;
    }

    private void checkingGoblinTurn(Creature g) {
        boolean canHarassHero = canGoblinHarrasing(g);
        if (!canHarassHero) {
            setOnTile(g.getX(), g.getY(), false);
            //how is goblin moving
            int tempX[] = new int[9];
            int tempY[] = new int[9];
            settingXY9Array(g.getX(), g.getY(), tempX, tempY);
            //changing way
            int way = getWayToSquare(tempX, tempY, heroC.getX(), heroC.getY());
            //premove
            setVector(tempX[way], tempY[way], g);
            g.setX(tempX[way]);
            g.setY(tempY[way]);
            // Log.i("dan", "MOVING GOBLING: " + tempX[min] + " " + tempY[min]);
            setOnTile(g.getX(), g.getY(), true);
        } else {
            checkGoblinHarrasing(g);
            g.setVector(4);
        }
        setLastXYArray(g);
    }


    private void checkingArcherTurn(Creature a) {
        //TODO more hard checking needed
        boolean canHarassHero = canArcherHarrasing(a);
        if (!canHarassHero) {
            boolean nonwaiting = true;
            setOnTile(a.getX(), a.getY(), false);
            //how is archer moving
            int tempX[] = new int[9];
            int tempY[] = new int[9];
            settingXY9Array(a.getX(), a.getY(), tempX, tempY);
            //receiving x and y of hero
            int x = heroC.getX();
            int y = heroC.getY();
            //MAKING FIELD
            //init tangent squares
            ArrayList<Integer> neededSquareX = new ArrayList<>();
            ArrayList<Integer> neededSquareY = new ArrayList<>();
            for (int i = 0; i < numSqH; i++) {
                for (int j = 0; j < numSqW; j++) {
                    if (onTile[i][j]) {
                        if (i == y | j == x) {
                            neededSquareX.add(j);
                            neededSquareY.add(i);
                        }
                    }
                }
            }
            if (neededSquareX.isEmpty()) {
                nonwaiting = false;
            }
            //changing way

            int way = 4;
            if (nonwaiting) {
                int index = 0;
                int dist = (tempX[4] - neededSquareX.get(0)) * (tempX[4] - neededSquareX.get(0)) + (tempY[4] - neededSquareY.get(0)) * (tempY[4] - neededSquareY.get(0));
                for (int w : neededSquareX) {
                    int currentIndex = neededSquareX.indexOf(w);
                    int h = neededSquareY.get(currentIndex);
                    int currentDistance = (tempX[4] - w) * (tempX[4] - w) + (tempY[4] - h) * (tempY[4] - h);
                    if (currentDistance <= dist) {
                        index = currentIndex;
                    }
                }

                //where we moving
                int toSquareX = neededSquareX.get(index);
                int toSquareY = neededSquareY.get(index);
                way = getWayToSquare(tempX, tempY, toSquareX, toSquareY);
            }
            //premove
            setVector(tempX[way], tempY[way], a);
            a.setX(tempX[way]);
            a.setY(tempY[way]);
            Log.i("dan", "MOVING ARCHER: " + tempX[way] + " " + tempY[way]);
            setOnTile(a.getX(), a.getY(), true);
        } else {
            checkArcherHarrasing(a);
            a.setVector(4);
        }
        setLastXYArray(a);
    }

    private void checkingMageTurn(Creature m) {
        //TODO MORE hard checking needed
        //now retarded system
        //TODO need rework
        boolean canHarassHero = canMageHarrasing(m);
        if (!canHarassHero) {
            boolean nonwaiting = true;
            setOnTile(m.getX(), m.getY(), false);
            //how is mage moving
            int tempX[] = new int[9];
            int tempY[] = new int[9];
            settingXY9Array(m.getX(), m.getY(), tempX, tempY);
            //receiving x and y of hero
            int x = heroC.getX();
            int y = heroC.getY();
            //MAKING FIELD
            //init tangent squares
            ArrayList<Integer> neededSquareX = new ArrayList<>();
            ArrayList<Integer> neededSquareY = new ArrayList<>();
            for (int i = 0; i < numSqH; i++) {
                for (int j = 0; j < numSqW; j++) {
                    if (onTile[i][j]) {
                        if ((x - j) * (x - j) == (i - y) * (i - y)) {
                            neededSquareX.add(j);
                            neededSquareY.add(i);
                        }
                    }
                }
            }
            if (neededSquareX.isEmpty()) {
                nonwaiting = false;
            }
            //changing way

            int way = 4;
            if (nonwaiting) {
                int index = 0;
                int dist = (tempX[4] - neededSquareX.get(0)) * (tempX[4] - neededSquareX.get(0)) + (tempY[4] - neededSquareY.get(0)) * (tempY[4] - neededSquareY.get(0));
                for (int w : neededSquareX) {
                    int currentIndex = neededSquareX.indexOf(w);
                    int h = neededSquareY.get(currentIndex);
                    int currentDistance = (tempX[4] - w) * (tempX[4] - w) + (tempY[4] - h) * (tempY[4] - h);
                    if (currentDistance <= dist) {
                        index = currentIndex;
                    }
                }

                //where we moving
                int toSquareX = neededSquareX.get(index);
                int toSquareY = neededSquareY.get(index);
                way = getWayToSquare(tempX, tempY, toSquareX, toSquareY);
            }
            //premove
            setVector(tempX[way], tempY[way], m);
            m.setX(tempX[way]);
            m.setY(tempY[way]);
            Log.i("dan", "MOVING MAGE: " + tempX[way] + " " + tempY[way]);
            setOnTile(m.getX(), m.getY(), true);
        } else {
            checkMageHarrasing(m);
            m.setVector(4);
        }
        setLastXYArray(m);
    }

    private boolean canGoblinHarrasing(Creature g) {
        int[] tempX = new int[9];
        int[] tempY = new int[9];
        settingXY9Array(g.getX(), g.getY(), tempX, tempY);
        //check vector killing

        /*if (tempX[g.getVector()] == heroC.getX() & tempY[g.getVector()] == heroC.getY()) { //too imbalanced
            decrementHerosHp();
        }*/
        //another
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tempX[i] == g.getLastX()[j] & tempY[i] == g.getLastY()[j])
                    if (tempX[i] == heroC.getX() & tempY[i] == heroC.getY()) {
                        return true;
                    }
            }
        }
        return false;
    }

    private boolean canArcherHarrasing(Creature a) {
        //check x and y of hero
        return heroC.getX() == a.getX() | heroC.getY() == a.getY();
    }

    private boolean canMageHarrasing(Creature m) {
        //check x and y of hero
        return (heroC.getX() - m.getX()) * (heroC.getX() - m.getX()) == (heroC.getY() - m.getY()) * (heroC.getY() - m.getY());
    }

    private void checkGoblinHarrasing(Creature g) {
        if (canGoblinHarrasing(g)) {
            decrementHerosHp();
        }
    }

    private void checkArcherHarrasing(Creature a) {
        if (canArcherHarrasing(a)) {
            Log.i("dan", "ARCHER HARASS from: " + a.getX() + " " + a.getY());
            decrementHerosHp();
        }
    }

    private void checkMageHarrasing(Creature m) {
        if (canMageHarrasing(m)) {
            Log.i("dan", "MAGE HARASS from: " + m.getX() + " " + m.getY());
            decrementHerosHp();
        }
    }

    private void checkIfAllAreDead() {
        for (Creature c :
                creatures) {
            if (c.getIdentity() != 0)
                if (c.getAlive())
                    return;
        }
        //  Log.i("dan", "enemies killed,opening the stairs");
        setAllEnemiesDead(true);
    }

    private void decrementHerosHp() {
        heroC.setCurrentHP(heroC.getCurrentHP() - 1);
        // Log.i("dan", "DAMAGING HERO, NOW HERO'S HP: " + heroC.getCurrentHP() + "/" + heroC.getHP());
        checkHerosDeath();
    }

    private void checkHerosDeath() {
        if (heroC.getCurrentHP() <= 0) {
            heroC.setAlive(false);
            endGame();
        }

    }

    private void endGame() {
        //  Log.i("dan", "END OF GAME");
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
        for (int i = 0; i < arrX.length; i++) {
            if (arrX[i] >= numSqW | arrX[i] < 0 | arrY[i] < 0 | arrX[i] >= numSqH) {
                arrX[i] = -1;
                arrY[i] = -1;
            }
        }
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    public boolean getAllEnemiesDead() {
        return allEnemiesDead;
    }
}
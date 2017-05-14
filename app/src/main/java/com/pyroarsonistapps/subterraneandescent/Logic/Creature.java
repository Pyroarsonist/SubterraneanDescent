package com.pyroarsonistapps.subterraneandescent.Logic;

public class Creature {
    protected int identity;
    protected int maxHP;
    protected int currentHP;

    protected int x; //on squares
    protected int y;

    protected int vector = 4;
    protected int[] lastX = new int[9]; // 0123456789 (real values) and - for null value
    protected int[] lastY = new int[9];

    protected boolean isAlive = true;

    public Creature(int x, int y, int currentHP, int maxHP, int identity) {
        this.x = x;
        this.y = y;
        this.currentHP = currentHP;
        this.maxHP = maxHP;
        this.identity = identity;
    }


    public Creature() {

    }

    public Creature(int identity, int currentHP, int maxHP, int x, int y, int vector, String lastX, String lastY, int isAlive) {
        this.identity = identity;
        this.currentHP = currentHP;
        this.maxHP = maxHP;
        this.x = x;
        this.y = y;
        this.vector = vector;
        this.lastX = setLast(lastX);
        this.lastY = setLast(lastY);
        this.isAlive = (isAlive == 1);
    }


    public int getCurrentHP() {
        return currentHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getIdentity() {
        return identity;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean getAlive() {
        return isAlive;
    }

    public int getVector() {
        return vector;
    }

    public int[] getLastX() {
        return lastX;
    }

    public int[] getLastY() {
        return lastY;
    }

    public void setLastX(int[] lastX) {
        this.lastX = lastX;
    }

    public void setLastY(int[] lastY) {
        this.lastY = lastY;
    }

    public void setVector(int vector) {
        this.vector = vector;
    }


    public int[] setLast(String last) {
        int[] arr = new int[9];
        for (int i = 0; i < arr.length; i++) {
            if (last.indexOf(i) == -1)
                arr[i] = -1;
            else
                arr[i] = Integer.parseInt(String.valueOf(String.valueOf(last).indexOf(i)));
        }
        return arr;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }
}

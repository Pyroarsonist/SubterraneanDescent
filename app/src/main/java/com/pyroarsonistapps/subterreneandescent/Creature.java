package com.pyroarsonistapps.subterreneandescent;

abstract class Creature {
    int identity;
    int HP;
    int currentHP;

    int x; //on squares
    int y;

    int vector = 4;
    int[] lastX = new int[9];
    int[] lastY = new int[9];

    boolean isAlive = true;

    Creature(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getCurrentHP() {
        return currentHP;
    }

    public int getHP() {
        return HP;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }

    int getIdentity() {
        return identity;
    }

    void setAlive(boolean alive) {
        isAlive = alive;
    }

    boolean getAlive() {
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


}

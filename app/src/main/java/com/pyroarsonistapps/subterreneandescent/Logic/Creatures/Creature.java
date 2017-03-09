package com.pyroarsonistapps.subterreneandescent.Logic.Creatures;

public abstract class Creature {
    protected int identity;
    protected int HP;
    protected int currentHP;

    protected int x; //on squares
    protected int y;

    protected int vector = 4;
    protected int[] lastX = new int[9];
    protected int[] lastY = new int[9];

    protected boolean isAlive = true;

    public Creature(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getHP() {
        return HP;
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

    public void setHP(int HP) {
        this.HP = HP;
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


}

package com.pyroarsonistapps.subterreneandescent;

/**
 * Created by A8 on 2/20/2017.
 */

public class Goblin extends Creature {
    Goblin(int x, int y) {
        identity = 1;
        HP = 1;
        this.x = x;
        this.y = y;
        currentHP = HP;
    }
}

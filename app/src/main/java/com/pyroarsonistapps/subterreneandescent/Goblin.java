package com.pyroarsonistapps.subterreneandescent;

/**
 * Created by A8 on 2/20/2017.
 */

public class Goblin extends Creature {
    Goblin(int x, int y) {
        super(x,y);
        identity = 1;
        HP = 1;
        currentHP = HP;
    }
}

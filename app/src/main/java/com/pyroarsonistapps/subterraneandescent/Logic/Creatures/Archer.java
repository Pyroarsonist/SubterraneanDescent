package com.pyroarsonistapps.subterraneandescent.Logic.Creatures;

public class Archer extends Creature {
    public  Archer(int x, int y) {
        super(x, y);
        identity = 2;
        HP = 1;
        currentHP = HP;
    }

    public Archer() {
        identity = 2;
        HP = 1;
        currentHP = HP;
    }
}

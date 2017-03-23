package com.pyroarsonistapps.subterreneandescent.Logic.Creatures;

public class Mage extends Creature {
    public Mage(int x, int y) {
        super(x, y);
        identity = 3;
        HP = 1;
        currentHP = HP;
    }

    public Mage() {
        super();
    }
}

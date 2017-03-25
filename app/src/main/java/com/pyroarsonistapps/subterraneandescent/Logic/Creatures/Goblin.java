package com.pyroarsonistapps.subterraneandescent.Logic.Creatures;


public class Goblin extends Creature {
    public  Goblin(int x, int y) {
        super(x,y);
        identity = 1;
        HP = 1;
        currentHP = HP;
    }

    public Goblin() {
        super();
    }
}

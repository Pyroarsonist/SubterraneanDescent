package com.pyroarsonistapps.subterreneandescent.Logic.Creatures;

public class Hero extends Creature {

    public Hero(int x, int y, int initHeroHP, int initMaxHeroHP) {
        super(x, y);
        identity = 0;
        HP = initMaxHeroHP;
        currentHP = initHeroHP;
    }

    public Hero(int x, int y) {
        super(x, y);
        identity = 0;
        HP = 3;
        currentHP = HP;
    }

    public Hero() {
        super();
    }
}

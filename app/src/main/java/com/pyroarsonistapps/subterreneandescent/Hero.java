package com.pyroarsonistapps.subterreneandescent;

class Hero extends Creature {

    Hero(int x, int y, int initHeroHP, int initMaxHeroHP) {
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
}

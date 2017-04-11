package com.pyroarsonistapps.subterraneandescent;


import android.content.Context;

import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Creature;

import java.io.IOException;
import java.util.ArrayList;

public interface Save {
    void createSave(Context context, int level, ArrayList<Creature> creatures);

    String getSave(Context context) throws IOException;

    Object[] parseFromSaveFile(Context context) throws IOException;

    void saveCreature(ArrayList<Creature> creatures, String identity, String currentHP, String HP, String x, String y, String vector, String lastX, String lastY, String isAlive);
}

package com.pyroarsonistapps.subterraneandescent;


import android.content.Context;

import com.pyroarsonistapps.subterraneandescent.Logic.Creatures.Creature;

import java.io.IOException;
import java.util.ArrayList;

public interface Save {
    void createSave(Context context, int level, ArrayList<Creature> creatures);

    String getSave(Context context) throws IOException;

    int parseFromSaveFile(Context context, ArrayList<Creature> creatures) throws IOException;
}

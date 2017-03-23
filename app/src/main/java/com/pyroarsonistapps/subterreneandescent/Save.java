package com.pyroarsonistapps.subterreneandescent;


import android.content.Context;

import com.pyroarsonistapps.subterreneandescent.Logic.Creatures.Creature;

import java.io.IOException;
import java.util.ArrayList;

public interface Save {
    void createSave(Context context, int level, ArrayList<Creature> creatures);

    String getSave(Context contex) throws IOException;

    int parseFromSaveFile(Context context, ArrayList<Creature> creatures) throws IOException;
}

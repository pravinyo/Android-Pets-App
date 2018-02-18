package com.is_great.pro.pets.syncUtils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by tripa on 2/18/2018.
 */

public class PetsSyncIntentService extends IntentService{

    public PetsSyncIntentService(){
        super("PetsSyncIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        PetsSyncTask.syncPet(this);
    }
}

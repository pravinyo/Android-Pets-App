package com.is_great.pro.pets.syncUtils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import java.util.concurrent.TimeUnit;

/**
 * Created by tripa on 2/18/2018.
 */

public class PetsSyncUtils {
    private static final int SYNC_INTERVAL_HOURS=3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;
    private static final String PETS_SYNC_TAG = "pets-sync";

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context){
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncPetsJob = dispatcher.newJobBuilder()
                .setService(PetsFirebaseJobService.class)
                .setTag(PETS_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS+SYNC_FLEXTIME_SECONDS
                ))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncPetsJob);
    }

    public static void startImmediateSync(@NonNull final Context context){
        Intent intentToSyncImmeiately = new Intent(context,PetsSyncIntentService.class);
        context.startService(intentToSyncImmeiately);
    }
}

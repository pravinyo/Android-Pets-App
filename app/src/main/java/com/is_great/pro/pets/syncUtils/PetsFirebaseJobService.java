package com.is_great.pro.pets.syncUtils;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by tripa on 2/18/2018.
 */

public class PetsFirebaseJobService extends JobService {

    private AsyncTask<Void,Void,Void> mSyncPetsTask;
    @Override
    public boolean onStartJob(final JobParameters job) {
        mSyncPetsTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                PetsSyncTask.syncPet(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job,false);
            }
        };

        mSyncPetsTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mSyncPetsTask !=null){
            mSyncPetsTask.cancel(true);
        }
        return true;
    }
}

package com.is_great.pro.pets.syncUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.is_great.pro.pets.utils.NetworkUtils;

import java.net.URL;

/**
 * Created by tripa on 2/18/2018.
 */

public class PetsSyncTask {
    synchronized public static void syncPet(Context context){
        URL serverRequestUrl = NetworkUtils.getUrl(context);

        // generate json format of the data locally stored
        //encrypt the data
        //send the data to the server
        //get status signal from server
        //store the status signal and last sync metadata
        //use content resolver to store the data locally
        //design algorithm that manages content in same order as in remote db
        //like todolist
        //feedback user about success or failure of sync

        Log.i("CheckThis","Yet to be done");
    }
}

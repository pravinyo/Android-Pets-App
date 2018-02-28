package com.is_great.pro.pets.syncUtils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.is_great.pro.pets.dataUtils.PetContract;
import com.is_great.pro.pets.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tripa on 2/18/2018.
 */

public class PetsSyncTask {

    private static String serverRequestUrl;
    private static final String TAG = "PetSyncTask_SYNC";
    //private static RequestQueue queue;
    private static Context mCurrentContext;
    synchronized public static void syncPet(Context context){
        mCurrentContext = context;
        serverRequestUrl = NetworkUtils.getStringUrl(mCurrentContext);

        //queue = Volley.newRequestQueue(mCurrentContext);
        if (PetsSyncTask.checkConnectivity(mCurrentContext)){
            Log.i(TAG,"Inside PetSync Call");
            /*

            String[] projection={
                    PetContract.PetEntry._ID,
                    PetContract.PetEntry.COLUMN_PET_NAME,
                    PetContract.PetEntry.COLUMN_PET_BREED,
                    PetContract.PetEntry.COLUMN_PET_GENDER,
                    PetContract.PetEntry.COLUMN_PET_WEIGHT,
                    PetContract.PetEntry.COLUMN_PET_STATUS
            };

            String selection = PetContract.PetEntry.COLUMN_PET_STATUS + " = ?";
            String[] SelectionArgs = {"0"};
            Cursor mCursor=mCurrentContext.getContentResolver().query(
                    PetContract.PetEntry.CONTENT_URI,
                    projection,
                    selection,
                    SelectionArgs,
                    null,
                    null);

            if(null == mCursor){
                Log.e(TAG,"Internal Error");
            }else if (mCursor.getCount()<1){
                Log.i(TAG,"All in Sync");
            }else{
                //start sync with the server
                if(mCursor.moveToFirst()){
                    do{
                        saveEntry(
                                mCursor.getInt(mCursor.getColumnIndex(PetContract.PetEntry._ID)),
                                mCursor.getString(mCursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME)),
                                mCursor.getString(mCursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED)),
                                mCursor.getInt(mCursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER)),
                                mCursor.getDouble(mCursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT)),
                                mCursor.getInt(mCursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_STATUS)));
                    }while (mCursor.moveToNext());
                }
            }
            */
        }

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

    private static void saveEntry(final int id, final String name, final String breed, final int gender, final double weight, int status) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverRequestUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //file is saved successfully
                                ContentValues params = new ContentValues();
                                params.put(PetContract.PetEntry._ID,id+"");
                                params.put(PetContract.PetEntry.COLUMN_PET_NAME,name);
                                params.put(PetContract.PetEntry.COLUMN_PET_BREED,breed);
                                params.put(PetContract.PetEntry.COLUMN_PET_GENDER,gender+"");
                                params.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,weight+"");

                                Uri mCurrentUri = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI,id);
                                int rowsAffected = mCurrentContext.getContentResolver()
                                        .update(
                                               mCurrentUri ,
                                                params,
                                                null,
                                                null);
                                if (rowsAffected ==0)
                                    Log.e(TAG,"Update failed");
                                else
                                    Log.i(TAG,"Updated for id :"+id );

                            }else {
                                Log.e(TAG,obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put(PetContract.PetEntry._ID,id+"");
                params.put(PetContract.PetEntry.COLUMN_PET_NAME,name);
                params.put(PetContract.PetEntry.COLUMN_PET_BREED,breed);
                params.put(PetContract.PetEntry.COLUMN_PET_GENDER,gender+"");
                params.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,weight+"");

                return params;
            }
        };

        //queue.add(stringRequest);
    }

    private static boolean checkConnectivity(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null){
            //Check type of connectivity
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                return true;
            }
        }
        return false;
    }
}

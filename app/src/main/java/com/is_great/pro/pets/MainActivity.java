package com.is_great.pro.pets;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.is_great.pro.pets.dataUtils.PetContract.PetEntry;
import com.is_great.pro.pets.syncUtils.PetsSyncUtils;
import com.is_great.pro.pets.utils.SwipeDetector;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final static int LOADER_ID=0;

    PetCursorAdapter mCursorAdapter;
    SwipeDetector swipeDetector;
    private Uri mCurrentPetUri=null;
    private View mCurrentView=null;
    private Animation animationLR=null;
    private Animation animationRL=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        final ListView petListView =(ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        mCursorAdapter = new PetCursorAdapter(this,null);
        petListView.setAdapter(mCursorAdapter);

        swipeDetector = new SwipeDetector();
        petListView.setOnTouchListener(swipeDetector);


        animationLR = AnimationUtils.loadAnimation(this,
                R.anim.slide_out_from_left_to_right);
        animationLR.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeListItem();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animationRL = AnimationUtils.loadAnimation(this,
                R.anim.slide_out_from_right_to_left);

        animationRL.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(MainActivity.this,"Right to left swipe",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,PetProfileActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // set Item Click Listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);
                    if(swipeDetector.swipeDetected()){
                        mCurrentPetUri=currentPetUri;
                        mCurrentView=view;
                        handleAction();
                    }else{
                        Intent intent = new Intent(MainActivity.this,EditorActivity.class);
                        intent.setData(currentPetUri);
                        startActivity(intent);
                    }
            }
        });

        Intent intent = getIntent();
        getLoaderManager().initLoader(LOADER_ID,null,this);
        handleIntent(intent);

        PetsSyncUtils.startImmediateSync(this);
    }

    private void handleAction(){
        if(swipeDetector.getAction() == SwipeDetector.Action.LR){
            mCurrentView.startAnimation(animationLR);
        }else if(swipeDetector.getAction() == SwipeDetector.Action.RL){
            mCurrentView.startAnimation(animationRL);
        }else {
            Toast.makeText(MainActivity.this," either Left or Right swipe",Toast.LENGTH_SHORT).show();
        }
    }

    private void removeListItem(){
        Toast.makeText(MainActivity.this,"Left to Right swipe",Toast.LENGTH_SHORT).show();
        String[] projection={
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
                //PetEntry.COLUMN_PET_STATUS
        };

        Cursor mCursor=getContentResolver().query(mCurrentPetUri,projection,null,null,null,null);

        final ContentValues values = new ContentValues();
        if(mCursor.moveToFirst()){
            values.put(PetEntry.COLUMN_PET_NAME, mCursor.getString(mCursor.getColumnIndex(PetEntry.COLUMN_PET_NAME)));
            values.put(PetEntry.COLUMN_PET_BREED,mCursor.getString(mCursor.getColumnIndex(PetEntry.COLUMN_PET_BREED)));
            values.put(PetEntry.COLUMN_PET_GENDER,mCursor.getString(mCursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER)));
            values.put(PetEntry.COLUMN_PET_WEIGHT,mCursor.getString(mCursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT)));
        }

        getContentResolver().delete(mCurrentPetUri, null, null);

        Snackbar.make(findViewById(R.id.content),"Item Deleted",Snackbar.LENGTH_LONG)
                .setAction("UNDO",new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        getContentResolver().insert(PetEntry.CONTENT_URI,values);
                        Snackbar.make(findViewById(R.id.content),"Item Restored",Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void insertDummyData(){
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME,"Tommy");
        values.put(PetEntry.COLUMN_PET_BREED,"Doberman");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT,14);
        //values.put(PetEntry.COLUMN_PET_STATUS,PetEntry.STATUS_NOT_SYNC);

        String log=""+getContentResolver().insert(PetEntry.CONTENT_URI,values);
        Log.i("MainActivity",log);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            Toast.makeText(this,query, Toast.LENGTH_SHORT).show();
            String[] projection={
                    PetEntry._ID,
                    PetEntry.COLUMN_PET_NAME,
                    PetEntry.COLUMN_PET_BREED,
                    PetEntry.COLUMN_PET_GENDER,
                    PetEntry.COLUMN_PET_WEIGHT
                    //PetEntry.COLUMN_PET_STATUS
            };

            String selection=PetEntry.COLUMN_PET_NAME+" = ?";
            String[] selectionArgs={query};
            Cursor mCursor=getContentResolver().query(PetEntry.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null);

            mCursorAdapter.swapCursor(mCursor);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getLoaderManager().restartLoader(LOADER_ID,null,MainActivity.this);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertDummyData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAll();
                return true;
            // Respond to click on sync all entries
            case R.id.action_sync_all_entries:
                startSyncService();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSyncService() {
        PetsSyncUtils.startImmediateSync(this);
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT,
                //PetEntry.COLUMN_PET_STATUS
        };
        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);
    }

    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAll() {
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }
}

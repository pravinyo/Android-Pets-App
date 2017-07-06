package com.is_great.pro.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.is_great.pro.pets.data.PetContract.PetEntry;
/**
 * Created by Pravinyo on 1/3/2017.
 */

public class PetDBHelper extends SQLiteOpenHelper{
    private final static String DATABASE_NAME="shelter2.db";
    private final static int DATBASE_VERSION=1;

    private static final String SQL_CREATE_ENTERIES="CREATE TABLE "+ PetEntry.TABLE_NAME+"("+
            PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL,"+
            PetEntry.COLUMN_PET_BREED + " TEXT NOT NULL,"+
            PetEntry.COLUMN_PET_GENDER + " INTEGER,"+
            PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL)";

    public PetDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATBASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTERIES);
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}

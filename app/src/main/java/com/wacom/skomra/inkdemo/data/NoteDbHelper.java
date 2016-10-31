package com.wacom.skomra.inkdemo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by aas on 10/31/16.
 */

public class NoteDbHelper extends SQLiteOpenHelper {

    private static final String TAG = NoteDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "Note.db" ;
    private static final int DATABASE_VERSION = 1 ;

    private static final String _ID = "_id";
    private static final String _NAME = "name";

    public NoteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " + NoteContract.NoteEntry.NOTES_TABLE_NAME + "("
                +_ID + " INTEGER PRIMARY KEY, "
                + _NAME + " TEXT,"
                + ")";
        Log.i(TAG, "db on create " + CREATE_NOTES_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

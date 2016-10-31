package com.wacom.skomra.inkdemo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aas on 10/31/16.
 */

public class NoteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Note.db" ;
    private static final int DATABASE_VERSION = 1 ;

    public NoteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

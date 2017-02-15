package com.wacom.skomra.inkdemo.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by aas on 10/31/16.
 */

public class NoteProvider extends ContentProvider {


    private static String TAG = NoteProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int NOTE = 102;
    private static final int NOTES = 101;

    NoteDbHelper mOpenHelper;
    @Override
    public boolean onCreate() {
        mOpenHelper = new NoteDbHelper(getContext());
        return true;
    }

/*    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }*/

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case NOTE:
            {
                selection = NoteContract.NoteEntry.COLUMN_ID + " = " +uri.getLastPathSegment();
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NoteContract.NoteEntry.NOTES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;
        switch (match) {

            case NOTE:
            case NOTES:
                long row = db.insertWithOnConflict(
                        NoteContract.NoteEntry.NOTES_TABLE_NAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                if (row > 0)
                    returnUri = NoteContract.NoteEntry.buildNoteUri(row);

                else
                    throw new UnsupportedOperationException("Failed to insert row into: " +uri);
                break;
        default:
        throw new UnsupportedOperationException("Unknown uri "+ uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return returnUri;
}

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTE:
                return NoteContract.NoteEntry.CONTENT_ITEM_TYPE;
            case NOTES:
                return NoteContract.NoteEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " +uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (selection == null)
            selection = "1'";
        switch(match){
            case NOTE:
                rowsDeleted = db.delete(
                        NoteContract.NoteEntry.NOTES_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+ uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        if (selection == null)
            selection = "1'";
        switch(match){
            case NOTE:
                rowsUpdated = db.update(
                        NoteContract.NoteEntry.NOTES_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+ uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        try {
            Log.i(TAG, "bulk insert");
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            switch (match) {
                case NOTES:
                    db.beginTransaction();
                    int returnCount = 0;
                    try {
                        for (ContentValues value : values) {

                            long _id = db.insert(
                                    NoteContract.NoteEntry.NOTES_TABLE_NAME,
                                    null,
                                    value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                    return returnCount;
                default:
                    return super.bulkInsert(uri, values);
            }
        } catch (android.database.sqlite.SQLiteConstraintException e){
            Log.e(TAG, " UNIQUE constraint failed:" );
        }
        Log.e(TAG, "REACHED RETURN 0");
        return 0;
    }


    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NoteContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, NoteContract.PATH_NOTES, NOTE);
        //matcher.addURI(authority, NoteContract.PATH_NOTES + "/#" , NOTE);
        return matcher;
    }
}

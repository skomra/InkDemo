package com.wacom.skomra.inkdemo.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by aas on 10/31/16.
 */

public class NoteContract {
    public static final String CONTENT_AUTHORITY = "com.wacom.skomra.inkdemo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NOTES = "notes";

    public static class NoteEntry implements BaseColumns {

        public static final Uri NOTES_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();

        public static final String NOTES_TABLE_NAME = "notes";

        public static final String CONTENT_ITEM_TYPE = CONTENT_AUTHORITY + "/" + PATH_NOTES;
        public static final String CONTENT_TYPE = CONTENT_AUTHORITY + "/" + PATH_NOTES;

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";

        public static Uri buildNoteUri(long id) {
            return ContentUris.withAppendedId(NOTES_URI, id);
        }
    }

}

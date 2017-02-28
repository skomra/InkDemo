package com.wacom.skomra.inkdemo;

import android.net.Uri;
import android.util.Log;

/**
 * Created by mocaw on 2/28/17.
 */

public class Utilities {

    private static final String TAG = "Utilities";

    protected static String getFilename(Uri uri){
        String fullName = uri.toString();
        int index = fullName.indexOf("%3A");
        if (index != -1){
            String nm = fullName.substring(index + 3);
            Log.i(TAG, " susbtring " + nm);
            return nm;
        }
        //FIXME throw ...
        return "";
    }
}

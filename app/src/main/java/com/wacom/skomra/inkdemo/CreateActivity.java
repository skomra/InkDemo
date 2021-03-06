package com.wacom.skomra.inkdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateActivity";
    public Uri mUri;

    //TODO move to shared preference
    protected boolean erase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_activity_toolbar);
        setSupportActionBar(toolbar);

        verifyStoragePermissions(this);

        mUri = getIntent().getData();
        if (mUri != null)
            Log.i(TAG, "uri in create activity " + mUri.toString());
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        CreateActivityFragment fragment = (CreateActivityFragment)
                getSupportFragmentManager().findFragmentById(R.id.create_fragment);
        if (mUri != null)
            fragment.setUri(mUri);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        Log.i(TAG, "verify storage perms");
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.erase) {
            CreateActivityFragment fragment = (CreateActivityFragment)
                    getSupportFragmentManager().findFragmentById(R.id.create_fragment);
            if (erase == false) {
                fragment.switchToEraser();
                erase = true;
            } else {
                fragment.switchToInk();
                erase = false;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

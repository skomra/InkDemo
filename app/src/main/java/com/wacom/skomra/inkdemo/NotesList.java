package com.wacom.skomra.inkdemo;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wacom.skomra.inkdemo.data.NoteContract;

import java.io.File;

import static com.wacom.skomra.inkdemo.data.NoteContract.NoteEntry.CONTENT_ITEM_TYPE;

public class NotesList extends AppCompatActivity implements NotesListFragment.OnListFragmentInteractionListener{

    private static final String TAG = NotesList.class.getSimpleName();
    private static final int SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
                //Uri uri = getOnClickUri(id);
                //intent.setData(uri);
                startActivityForResult(intent,0);

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.import_file) {
            importFile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * cursor with one item
     * @param id
     */
    @Override
    public void onListFragmentInteraction(final int id) {
        Uri uri = getOnClickUri(id);
        startIntentWithUri(uri);
    }

    private Uri getOnClickUri(int position){
        Uri uri = Uri.parse("content://" + CONTENT_ITEM_TYPE + "/" + position);
        return uri;
    }

    private void importFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), SELECT_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Install a File Manager to use import function", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "on activity result " + requestCode);

        if (requestCode == SELECT_FILE) {
            if (data == null)
                return;
            Uri uri = data.getData();
            Log.i(TAG, "uri" + uri.toString());

            ContentValues noteValues = new ContentValues();

            if (uri.toString().contains("externalstorage")){ // FIXME Fragile
                String filename = Utilities.getFilename(uri);
                File file = new File(Environment.getExternalStorageDirectory() + "/" + filename);

                noteValues.put(NoteContract.NoteEntry.COLUMN_NAME, file.toString());
                Uri ourUri = getApplicationContext().getContentResolver().insert(NoteContract.NoteEntry.NOTES_URI, noteValues);
                Log.i(TAG, "our uri " + ourUri.toString());
                startIntentWithUri(ourUri);
                //.notifyDataChange();
            }


        }
    }

    private void startIntentWithUri(Uri uri){
        Intent intent = new Intent(this, CreateActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, 0);
    }
}

package com.wacom.skomra.inkdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wacom.skomra.inkdemo.data.NoteContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = NotesListFragment.class.getSimpleName();
    private static final int VALUE_NOT_USED = -1;
    private OnListFragmentInteractionListener mListener;
    private int mColumnCount = 1;

    MyEndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    MyNoteRecyclerViewAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    public NotesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mAdapter == null)
            mAdapter = new MyNoteRecyclerViewAdapter(getContext(), null, mListener, this);

        //loadMore();//onLoadInitialData();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //TODO check if loader is already running?
        getLoaderManager().initLoader(VALUE_NOT_USED, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_notes_list, container, false);
        View layout = inflater.inflate(R.layout.fragment_notes_list, container, false);

        // Set the adapter
        if (layout instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) layout;
            Context context = layout.getContext();
            recyclerView.setAdapter(mAdapter);

            if (mColumnCount <= 1) {
                mLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mLayoutManager);
                mEndlessRecyclerViewScrollListener =
                        new MyEndlessRecyclerViewScrollListener(mLayoutManager);
                recyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);
                //recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
            } else {
                Log.e(TAG, "Too Many Columns");
            }
        }
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;

        Log.i(TAG, "on create loader");
        //SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME,0);
        //Log.i(TAG, "preferences.contains(\"sort_by_name\")" + preferences.contains("sort_by_name"));
        //boolean sortByName = preferences.getBoolean("sort_by_name", true);
        //Log.i(TAG, "create loader got boolean sortByName " + sortByName);

        //actually getting my cursor in onBindViewHolder?
        //if i comment this out no data

        if (true /*sortByName*/) {
            String sortOrder = NoteContract.NoteEntry.COLUMN_NAME + " ASC";
            Uri usersUri = NoteContract.NoteEntry.getNotesUri();
            loader = new CursorLoader(
                    this.getActivity(),
                    usersUri,
                    null,
                    null,
                    null,
                    sortOrder);
        }

        mAdapter.notifyDataSetChanged();
        return loader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void notifyDataChange(){
        mAdapter.notifyDataSetChanged();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(final int position);
    }

    private class MyEndlessRecyclerViewScrollListener extends EndlessRecyclerViewScrollListener{

        public MyEndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            //loadMore();
        }

    }
}

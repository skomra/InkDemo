package com.wacom.skomra.inkdemo;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aas on 10/28/16.
 */
public class MyNoteRecyclerViewAdapter {
    private static final String TAG = MyNoteRecyclerViewAdapter.class.getSimpleName();
    private final NotesListFragment.OnListFragmentInteractionListener mListener;
    private Cursor mCursor;
    //CursorAdapter mCursorAdapter;
    Context mContext;
    private DataSetObserver mDataSetObserver;
    Fragment mUserFragment;

    /**
     * Constructor
     * @param context
     * @param cur
     * @param listener
     */
    public MyNoteRecyclerViewAdapter(Context context, Cursor cur, NotesListFragment.OnListFragmentInteractionListener listener, Fragment userFragment) {
        mUserFragment = userFragment;
        mContext = context;
        mListener = listener;
        mCursor = cur;
        mDataSetObserver = new DataSetObserver() {};
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_create, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public String toString() {
            //return super.toString() + usernameView.getText() + " '" + ageView.getText() + "'";+
            return super.toString();
        }

    }
}

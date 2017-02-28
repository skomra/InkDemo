package com.wacom.skomra.inkdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.media.ThumbnailUtils;
import android.provider.UserDictionary;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wacom.skomra.inkdemo.data.NoteContract;

import java.io.File;

/**
 * Created by aas on 10/28/16.
 */
public class MyNoteRecyclerViewAdapter  extends RecyclerView.Adapter<MyNoteRecyclerViewAdapter.ViewHolder>{
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
        Cursor cursor = mCursor;
        boolean b = cursor.moveToPosition(position);
        final int id;

        TextView mDocumentName = (TextView) holder.mDocumentName;
        ImageView mThumbnail = (ImageView) holder.itemView.findViewById(R.id.listImage);

        if (cursor != null) {
            int index_id = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_ID);
            int index_doc_name = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME);
            //int index_thumbnail = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_THUMBNAIL);

            final String name = cursor.getString(index_doc_name);
            id = cursor.getInt(index_id);

            String pic_filename = cursor.getString(index_doc_name);
            File image = new File(pic_filename);
            //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

            //Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), THUMBSIZE, THUMBSIZE);

//            Bitmap thumbImage = decodeSampledBitmapFromResource(image.getAbsolutePath(), 100, 100);
            
//            mThumbnail.setImageBitmap(thumbImage);
            mDocumentName.setText(name);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {

                        // Notify the active callbacks that an item has been selected.
                        mListener.onListFragmentInteraction(id); //FIXME fix references to cursor here
                    }
                }
            });

            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.delete)
                            .setMessage(R.string.really_delete)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteListItem(name);
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .show();
                    return true;
                }
            });

        } else {
            Log.i(TAG, "onbindviewholder " + b);
        }
    }

    private void deleteListItem(String name){
        File file = new File(name);
        //delete from db
        String where = "name=?";
        String[] args = {name,};

        int rowsDeleted = mContext.getContentResolver().delete(
                NoteContract.NoteEntry.getNotesUri(),
                where,
                args);

        file.delete();
        this.notifyDataSetChanged();
        Log.i(TAG, " " + name + " deleted x" + rowsDeleted);
    }

    //Based on https://developer.android.com/training/displaying-bitmaps/load-bitmap.html
    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path);
    }

    //Taken from https://developer.android.com/training/displaying-bitmaps/load-bitmap.html
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor cursor){
        if (mCursor == cursor)
            return null;

        Cursor oldCursor = mCursor;
        this.mCursor = cursor;
        if (cursor != null){
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note_thumbnail, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public final View mThumbnail;
        public final TextView mDocumentName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbnail = (ImageView) view.findViewById(R.id.listImage);
            mDocumentName = (TextView) view.findViewById(R.id.document_name);
        }

        @Override
        public String toString() {
            return super.toString() + mDocumentName.getText();
        }

    }
}

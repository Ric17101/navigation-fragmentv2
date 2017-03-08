package admin4.techelm.com.techelmtechnologies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.RecordingWrapper;

/**
 * Created by admin 4 on 21/02/2017.
 */

public class RecordingDBUtil extends DatabaseAccess {

    private static final String LOG_TAG = "RecordingDBUtil";

    private OnDatabaseChangedListener mCallback;

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "servicejob_recordings";
        public static final String COLUMN_NAME_RECORDING_ID = "id";
        public static final String COLUMN_NAME_RECORDING_NAME = "recording_name";
        public static final String COLUMN_NAME_RECORDING_FILE_PATH = "file_path";
        public static final String COLUMN_NAME_RECORDING_LENGTH = "length";
        public static final String COLUMN_NAME_TIME_ADDED = "time_added";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewRecordingsEntryAdded(String fileName);
        void onRecordingsEntryRenamed(String fileName);
        void onRecordingsEntryDeleted();
    }
    /**
     * Private constructor to avoid object creation from outside classes.
     * @param context
     */
    public RecordingDBUtil(Context context) {
        super(context);
        try {
            mOnDatabaseChangedListener = (OnDatabaseChangedListener) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(LOG_TAG, "Must implement the CallbackInterface in the Activity", ex);
        }
        System.gc();
    }

    public List<RecordingWrapper> getAllRecordings() {
        ArrayList<RecordingWrapper> list = new ArrayList<RecordingWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RecordingWrapper recordItem = new RecordingWrapper();
                recordItem.setId(Integer.parseInt(cursor.getString(0)));
                recordItem.setName(cursor.getString(1));
                recordItem.setFilePath(cursor.getString(2));
                recordItem.setLength(cursor.getInt(3));
                recordItem.setTime(cursor.getLong(4));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public List<RecordingWrapper> getAllRecordingsByID(String  id) {
        ArrayList<RecordingWrapper> list = new ArrayList<RecordingWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RecordingWrapper recordItem = new RecordingWrapper();
                recordItem.setId(Integer.parseInt(cursor.getString(0)));
                recordItem.setName(cursor.getString(1));
                recordItem.setFilePath(cursor.getString(2));
                recordItem.setLength(cursor.getInt(3));
                recordItem.setTime(cursor.getLong(4));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public RecordingWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        RecordingWrapper recordItem = new RecordingWrapper();
        if (cursor.moveToPosition(position)) {
            recordItem.setId(Integer.parseInt(cursor.getString(0)));
            recordItem.setName(cursor.getString(1));
            recordItem.setFilePath(cursor.getString(2));
            recordItem.setLength(cursor.getInt(3));
            recordItem.setTime(cursor.getLong(4));
        } else {
            recordItem = null;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return recordItem;
    }

    public void removeItemWithId(int id) {
        SQLiteDatabase db = getDB();
        String[] whereArgs = { String.valueOf(id) };
        db.delete(DBHelperItem.TABLE_NAME,
                DBHelperItem.COLUMN_NAME_RECORDING_ID + "=?", whereArgs);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onRecordingsEntryDeleted();
        }
        Log.e(LOG_TAG, "addRecording " + id);
    }

    public int getCount() {
        SQLiteDatabase db = getDB();
        String[] projection = { DBHelperItem.COLUMN_NAME_RECORDING_ID };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public class RecordingComparator implements Comparator<RecordingWrapper> {
        public int compare(RecordingWrapper item1, RecordingWrapper item2) {
            Long o1 = item1.getTime();
            Long o2 = item2.getTime();
            return o2.compareTo(o1);
        }
    }

    public int addRecording(String recordingName, String filePath, long length) {

        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH, length);
        cv.put(DBHelperItem.COLUMN_NAME_TIME_ADDED, System.currentTimeMillis());
        db.insert(DBHelperItem.TABLE_NAME, null, cv);
        int rowId = 1;
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewRecordingsEntryAdded(recordingName);
        }

        return rowId;
    }

    public void renameItem(RecordingWrapper item, String recordingName, String filePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_RECORDING_ID + "=" + item.getId(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onRecordingsEntryRenamed(item.getName());
        }
    }

    public long restoreRecording(RecordingWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, item.getName());
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, item.getFilePath());
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH, item.getLength());
        cv.put(DBHelperItem.COLUMN_NAME_TIME_ADDED, item.getTime());
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_ID, item.getId());
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        if (mOnDatabaseChangedListener != null) {
            //mOnDatabaseChangedListener.onNewRecordingsEntryAdded();
        }
        return rowId;
    }

}

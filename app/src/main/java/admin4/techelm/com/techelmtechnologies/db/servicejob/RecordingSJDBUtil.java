package admin4.techelm.com.techelmtechnologies.db.servicejob;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.db.DatabaseAccess;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobRecordingWrapper;
import admin4.techelm.com.techelmtechnologies.utility.FileUtility;

/**
 * Created by admin 4 on 21/02/2017.
 */

public class RecordingSJDBUtil extends DatabaseAccess {

    private static final String LOG_TAG = "RecordingSJDBUtil";

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "servicejob_recordings";
        public static final String COLUMN_NAME_RECORDING_ID = "id";
        public static final String COLUMN_NAME_RECORDING_SERVICE_ID = "servicejob_id";
        public static final String COLUMN_NAME_RECORDING_NAME = "recording_name";
        public static final String COLUMN_NAME_RECORDING_FILE_PATH = "file_path";
        public static final String COLUMN_NAME_RECORDING_LENGTH = "length";
        public static final String COLUMN_NAME_RECORDING_TIME_ADDED = "time_added";
        public static final String COLUMN_NAME_RECORDING_TAKEN = "taken";
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
    public RecordingSJDBUtil(Context context) {
        super(context);
        try {
            mOnDatabaseChangedListener = (OnDatabaseChangedListener) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(LOG_TAG, "Must implement the CallbackInterface in the Activity", ex);
        }
        System.gc();
    }

    /**
     * This can be used if you don't want to implement the interfaces
     * or You are using a non-activity class
     * @param context - context you passed in
     * @param message - message from the calling class of instantiation
     */
    public RecordingSJDBUtil(Context context, String message) {
        super(context);
        Log.e(LOG_TAG, message);
    }

    public List<ServiceJobRecordingWrapper> getAllRecordings() {
        ArrayList<ServiceJobRecordingWrapper> list = new ArrayList<ServiceJobRecordingWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobRecordingWrapper recordItem = new ServiceJobRecordingWrapper();
                recordItem.setId(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceId(cursor.getString(1));
                recordItem.setName(cursor.getString(2));
                recordItem.setFilePath(cursor.getString(3));
                recordItem.setLength(cursor.getInt(4));
                recordItem.setTime(cursor.getLong(5));
                recordItem.setTaken(cursor.getString(6));
                if (FileUtility.isFileExist(recordItem.getFilePath())) { // Skip adding this record if File Doesn't Exist or being Deleted on the Memory Device
                    list.add(recordItem);
                }
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }



    public List<ServiceJobRecordingWrapper> getAllRecordingsBySJID_ByTaken(int id, String taken) {
        ArrayList<ServiceJobRecordingWrapper> list = new ArrayList<ServiceJobRecordingWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE servicejob_id=" + id
                + " AND taken='" + taken + "'";
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobRecordingWrapper recordItem = new ServiceJobRecordingWrapper();
                recordItem.setId(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceId(cursor.getString(1));
                recordItem.setName(cursor.getString(2));
                recordItem.setFilePath(cursor.getString(3));
                recordItem.setLength(cursor.getInt(4));
                recordItem.setTime(cursor.getLong(5));
                recordItem.setTaken(cursor.getString(6));
                if (FileUtility.isFileExist(recordItem.getFilePath())) { // Skip adding this record if File Doesn't Exist or being Deleted on the Memory Device
                    list.add(recordItem);
                }
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public List<ServiceJobRecordingWrapper> getAllRecordingsBySJID(int id) {
        ArrayList<ServiceJobRecordingWrapper> list = new ArrayList<ServiceJobRecordingWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE servicejob_id=" + id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobRecordingWrapper recordItem = new ServiceJobRecordingWrapper();
                recordItem.setId(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceId(cursor.getString(1));
                recordItem.setName(cursor.getString(2));
                recordItem.setFilePath(cursor.getString(3));
                recordItem.setLength(cursor.getInt(4));
                recordItem.setTime(cursor.getLong(5));
                recordItem.setTaken(cursor.getString(6));
                if (FileUtility.isFileExist(recordItem.getFilePath())) { // Skip adding this record if File Doesn't Exist or being Deleted on the Memory Device
                    list.add(recordItem);
                }
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public ServiceJobRecordingWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        ServiceJobRecordingWrapper recordItem = new ServiceJobRecordingWrapper();
        if (cursor.moveToPosition(position)) {
            recordItem.setId(Integer.parseInt(cursor.getString(0)));
            recordItem.setServiceId(cursor.getString(1));
            recordItem.setName(cursor.getString(2));
            recordItem.setFilePath(cursor.getString(3));
            recordItem.setLength(cursor.getInt(4));
            recordItem.setTime(cursor.getLong(5));
            recordItem.setTaken(cursor.getString(6));
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

    public class RecordingComparator implements Comparator<ServiceJobRecordingWrapper> {
        public int compare(ServiceJobRecordingWrapper item1, ServiceJobRecordingWrapper item2) {
            Long o1 = item1.getTime();
            Long o2 = item2.getTime();
            return o2.compareTo(o1);
        }
    }

    public int addRecording(String recordingName, String filePath, long length, int serviceId, String taken) {

        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_SERVICE_ID, serviceId);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH, length);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_TIME_ADDED, System.currentTimeMillis());
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_TAKEN, taken);
        long idInserted = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        int rowId = (int)idInserted;
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewRecordingsEntryAdded(recordingName);
        }
        return rowId;
    }

    public void renameItem(ServiceJobRecordingWrapper item, String recordingName, String filePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_RECORDING_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onRecordingsEntryRenamed(item.getRecordingName());
        }
    }

    public long restoreRecording(ServiceJobRecordingWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, item.getRecordingName());
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, item.getFilePath());
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH, item.getLength());
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_TIME_ADDED, item.getTime());
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_ID, item.getID());
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        if (mOnDatabaseChangedListener != null) {
            //mOnDatabaseChangedListener.onNewRecordingsEntryAdded();
        }
        return rowId;
    }

    /**
     * Has inserted at least one recordings in the DB
     * This is called before submitting the Files to the web
     * @param servicejob_id
     * @return
     */
    public boolean hasInsertedRecordings(int servicejob_id) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE "+ DBHelperItem.COLUMN_NAME_RECORDING_SERVICE_ID + "=" + servicejob_id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        // String recordItem = cursor.getString(1);
        boolean result;
        if (cursor.moveToFirst()) {
            result = true;
        } else { // no data
            result = false;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return result;
    }
}

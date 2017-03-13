package admin4.techelm.com.techelmtechnologies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.ServiceJobPartsWrapper;

/**
 * Created by admin 4 on 13/03/2017.
 * File Replacements
 */

public class PartsDBUtil extends DatabaseAccess {

    private static final String LOG_TAG = "PartsDBUtil";

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "servicejob_parts";
        public static final String COLUMN_NAME_PARTS_ID = "id";
        public static final String COLUMN_NAME_PARTS_SERVICE_ID = "servicejob_id";
        public static final String COLUMN_NAME_PARTS_NAME = "parts_name";
        public static final String COLUMN_NAME_PARTS_FILE_PATH = "file_path";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewUploadsEntryAdded(String fileName);
        void onUploadsEntryRenamed(String fileName);
        void onUploadsEntryDeleted();
    }
    /**
     * Private constructor to avoid object creation from outside classes.
     * @param context
     */
    public PartsDBUtil(Context context) {
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
    public PartsDBUtil(Context context, String message) {
        super(context);
        Log.e(LOG_TAG, message);
    }

    public List<ServiceJobPartsWrapper> getAllParts() {
        ArrayList<ServiceJobPartsWrapper> list = new ArrayList<ServiceJobPartsWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobPartsWrapper recordItem = new ServiceJobPartsWrapper();
                recordItem.setId(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceId(Integer.parseInt(cursor.getString(1)));
                recordItem.setPartName(cursor.getString(2));
                recordItem.setFilePath(cursor.getString(3));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public List<ServiceJobPartsWrapper> getAllPartsBySJID(int id) {
        ArrayList<ServiceJobPartsWrapper> list = new ArrayList<ServiceJobPartsWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME + " WHERE servicejob_id="+id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobPartsWrapper recordItem = new ServiceJobPartsWrapper();
                recordItem.setId(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceId(Integer.parseInt(cursor.getString(1)));
                recordItem.setPartName(cursor.getString(2));
                recordItem.setFilePath(cursor.getString(3));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public ServiceJobPartsWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        ServiceJobPartsWrapper recordItem = new ServiceJobPartsWrapper();
        if (cursor.moveToPosition(position)) {
            recordItem.setId(Integer.parseInt(cursor.getString(0)));
            recordItem.setServiceId(Integer.parseInt(cursor.getString(1)));
            recordItem.setPartName(cursor.getString(2));
            recordItem.setFilePath(cursor.getString(3));
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
                DBHelperItem.COLUMN_NAME_PARTS_ID + "=?", whereArgs);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onUploadsEntryDeleted();
        }
        Log.e(LOG_TAG, "addParts " + id);
    }

    public int getCount() {
        SQLiteDatabase db = getDB();
        String[] projection = { DBHelperItem.COLUMN_NAME_PARTS_ID };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    // public int addUpload(String uploadName, String filePath, int serviceId) {
    public int addUpload(ServiceJobPartsWrapper item) {

        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_SERVICE_ID, item.getServiceId());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_NAME, item.getUploadName());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_FILE_PATH, item.getFilePath());
        // cv.put(DBHelperItem.COLUMN_NAME_PARTS_ID, length);
        long idInserted = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        int rowId = (int)idInserted;
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewUploadsEntryAdded(item.getUploadName());
        }
        return rowId;
    }

    public void renameItem(ServiceJobPartsWrapper item, String recordingName, String filePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_FILE_PATH, filePath);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_PARTS_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onUploadsEntryRenamed(item.getUploadName());
        }
    }

    public long restoreRecording(ServiceJobPartsWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_NAME, item.getUploadName());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_FILE_PATH, item.getFilePath());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_SERVICE_ID, item.getServiceId());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_ID, item.getID());
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewUploadsEntryAdded(item.getUploadName());
        }
        return rowId;
    }

}

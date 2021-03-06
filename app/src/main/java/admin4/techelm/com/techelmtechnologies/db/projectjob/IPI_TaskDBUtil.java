package admin4.techelm.com.techelmtechnologies.db.projectjob;

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
 * Created by admin 4 on 26/04/2017.
 */

public class IPI_TaskDBUtil extends DatabaseAccess {

    private static final String TAG = IPI_TaskDBUtil.class.getSimpleName();

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "projectjob_ipi_tasks";

        public static final String COLUMN_NAME_IPI_TASK_ID = "id";
        public static final String COLUMN_NAME_IPI_TASK_SERVICE_ID = "projectjob_id";
        public static final String COLUMN_NAME_IPI_TASK_NAME = "serial_no";
        public static final String COLUMN_NAME_IPI_TASK_FILE_PATH = "description";
        public static final String COLUMN_NAME_IPI_TASK_LENGTH = "status";
        public static final String COLUMN_NAME_IPI_TASK_TIME_ADDED = "non_conformance";
        public static final String COLUMN_NAME_IPI_TASK_TAKEN = "corrective_actions";
        public static final String COLUMN_NAME_IPI_TASK_TAKEN_ = "target_completion_date";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewIPI_TaskEntryAdded(String fileName);
        void onIPI_TaskEntryRenamed(String fileName);
        void onIPI_TaskEntryDeleted();
    }
    /**
     * Private constructor to avoid object creation from outside classes.
     * @param context
     */
    public IPI_TaskDBUtil(Context context) {
        super(context);
        try {
            mOnDatabaseChangedListener = (OnDatabaseChangedListener) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(TAG, "Must implement the ProjectJobListener in the Activity", ex);
        }
        System.gc();
    }

    /**
     * This can be used if you don't want to implement the interfaces
     * or You are using a non-activity class
     * @param context - context you passed in
     * @param message - message from the calling class of instantiation
     */
    public IPI_TaskDBUtil(Context context, String message) {
        super(context);
        Log.e(TAG, message);
    }

    public List<ServiceJobRecordingWrapper> getAllRecordings() {
        ArrayList<ServiceJobRecordingWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobRecordingWrapper item = new ServiceJobRecordingWrapper();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setServiceId(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setFilePath(cursor.getString(3));
                item.setLength(cursor.getInt(4));
                item.setTime(cursor.getLong(5));
                item.setTaken(cursor.getString(6));
                if (FileUtility.isFileExist(item.getFilePath())) { // Skip adding this record if File Doesn't Exist or being Deleted on the Memory Device
                    list.add(item);
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
        ArrayList<ServiceJobRecordingWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE servicejob_id=" + id
                + " AND taken='" + taken + "'";
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobRecordingWrapper item = new ServiceJobRecordingWrapper();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setServiceId(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setFilePath(cursor.getString(3));
                item.setLength(cursor.getInt(4));
                item.setTime(cursor.getLong(5));
                item.setTaken(cursor.getString(6));
                if (FileUtility.isFileExist(item.getFilePath())) { // Skip adding this record if File Doesn't Exist or being Deleted on the Memory Device
                    list.add(item);
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
        ArrayList<ServiceJobRecordingWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE servicejob_id=" + id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobRecordingWrapper item = new ServiceJobRecordingWrapper();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setServiceId(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setFilePath(cursor.getString(3));
                item.setLength(cursor.getInt(4));
                item.setTime(cursor.getLong(5));
                item.setTaken(cursor.getString(6));
                if (FileUtility.isFileExist(item.getFilePath())) { // Skip adding this record if File Doesn't Exist or being Deleted on the Memory Device
                    list.add(item);
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
        ServiceJobRecordingWrapper item = new ServiceJobRecordingWrapper();
        if (cursor.moveToPosition(position)) {
            item.setId(Integer.parseInt(cursor.getString(0)));
            item.setServiceId(cursor.getString(1));
            item.setName(cursor.getString(2));
            item.setFilePath(cursor.getString(3));
            item.setLength(cursor.getInt(4));
            item.setTime(cursor.getLong(5));
            item.setTaken(cursor.getString(6));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return item;
    }

    public void removeItemWithId(int id) {
        SQLiteDatabase db = getDB();
        String[] whereArgs = { String.valueOf(id) };
        db.delete(DBHelperItem.TABLE_NAME,
                DBHelperItem.COLUMN_NAME_IPI_TASK_ID + "=?", whereArgs);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onIPI_TaskEntryDeleted();
        }
        Log.e(TAG, "addRecording " + id);
    }

    public int getCount() {
        SQLiteDatabase db = getDB();
        String[] projection = { DBHelperItem.COLUMN_NAME_IPI_TASK_ID };
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
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_SERVICE_ID, serviceId);
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_FILE_PATH, filePath);
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_LENGTH, length);
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_TIME_ADDED, System.currentTimeMillis());
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_TAKEN, taken);
        long idInserted = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        int rowId = (int)idInserted;
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewIPI_TaskEntryAdded(recordingName);
        }
        return rowId;
    }

    public void renameItem(ServiceJobRecordingWrapper item, String recordingName, String filePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_FILE_PATH, filePath);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_IPI_TASK_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onIPI_TaskEntryRenamed(item.getRecordingName());
        }
    }

    public long restoreRecording(ServiceJobRecordingWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_NAME, item.getRecordingName());
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_FILE_PATH, item.getFilePath());
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_LENGTH, item.getLength());
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_TIME_ADDED, item.getTime());
        cv.put(DBHelperItem.COLUMN_NAME_IPI_TASK_ID, item.getID());
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
                + " WHERE "+ DBHelperItem.COLUMN_NAME_IPI_TASK_SERVICE_ID + "=" + servicejob_id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        // String item = cursor.getString(1);
        boolean result;
        if (cursor.moveToFirst()) {
            result = true;
            /*FileUtility fileUtil = new FileUtility(getContext());
            do {
                ServiceJobRecordingWrapper item = new ServiceJobRecordingWrapper();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setServiceId(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setFilePath(cursor.getString(3));
                item.setLength(cursor.getInt(4));
                item.setTime(cursor.getLong(5));
                fileUtil.isFileExist();
            } while (cursor.moveToNext());*/
        } else { // no data
            result = false;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return result;
    }
}

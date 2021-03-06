package admin4.techelm.com.techelmtechnologies.db.projectjob;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.db.DatabaseAccess;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_CActionWrapper_NOT_USED;

/**
 * Created by admin 4 on 26/04/2017.
 */

public class IPI_CorrectionActionDBUtil extends DatabaseAccess {

    private static final String LOG_TAG = IPI_CorrectionActionDBUtil.class.getSimpleName();

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "projectjob_ipi_corrective_actions";

        public static final String COLUMN_NAME_CORRECTIVE_ACTION_ID = "id";
        public static final String COLUMN_NAME_CORRECTIVE_ACTION_SERVICE_ID = "projectjob_id";
        public static final String COLUMN_NAME_CORRECTIVE_ACTION_NAME = "serial_no";
        public static final String COLUMN_NAME_CORRECTIVE_ACTION_FILE_PATH = "car_no";
        public static final String COLUMN_NAME_CORRECTIVE_ACTION_LENGTH = "description";
        public static final String COLUMN_NAME_CORRECTIVE_ACTION_TIME_ADDED = "target_remedy_date";
        public static final String COLUMN_NAME_CORRECTIVE_ACTION_TAKEN = "completion_date";
        public static final String COLUMN_NAME_CORRECTIVE_ACTION_REMARKS = "remarks";
        public static final String COLUMN_NAME_CORRECTIVE_ACTION_DISPOSTION = "disposition";
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
    public IPI_CorrectionActionDBUtil(Context context) {
        super(context);
        try {
            mOnDatabaseChangedListener = (OnDatabaseChangedListener) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(LOG_TAG, "Must implement the ProjectJobListener in the Activity", ex);
        }
        System.gc();
    }

    /**
     * This can be used if you don't want to implement the interfaces
     * or You are using a non-activity class
     * @param context - context you passed in
     * @param message - message from the calling class of instantiation
     */
    public IPI_CorrectionActionDBUtil(Context context, String message) {
        super(context);
        Log.e(LOG_TAG, message);
    }

    public List<IPI_CActionWrapper_NOT_USED> getAllRecordings() {
        ArrayList<IPI_CActionWrapper_NOT_USED> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IPI_CActionWrapper_NOT_USED recordItem = new IPI_CActionWrapper_NOT_USED();
                recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setProjectJob_ID(cursor.getInt(1));
                recordItem.setSerialNo(cursor.getString(2));
                recordItem.setCarNo(cursor.getString(3));
                recordItem.setDescription(cursor.getString(4));
                recordItem.setTargetRemedyDate(cursor.getString(5));
                recordItem.setCompletionDate(cursor.getString(6));
                recordItem.setRemarks(cursor.getString(7));
                recordItem.setDispostion(cursor.getString(8));
                /*if (FileUtility.isFileExist(recordItem.getFilePath())) { // Skip adding this record if File Doesn't Exist or being Deleted on the Memory Device
                    list.add(recordItem);
                }*/
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }



    public List<IPI_CActionWrapper_NOT_USED> getAllRecordingsBySJID_ByTaken(int id, String taken) {
        ArrayList<IPI_CActionWrapper_NOT_USED> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE servicejob_id=" + id
                + " AND taken='" + taken + "'";
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IPI_CActionWrapper_NOT_USED recordItem = new IPI_CActionWrapper_NOT_USED();
                recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setProjectJob_ID(cursor.getInt(1));
                recordItem.setSerialNo(cursor.getString(2));
                recordItem.setCarNo(cursor.getString(3));
                recordItem.setDescription(cursor.getString(4));
                recordItem.setTargetRemedyDate(cursor.getString(5));
                recordItem.setCompletionDate(cursor.getString(6));
                recordItem.setRemarks(cursor.getString(7));
                recordItem.setDispostion(cursor.getString(8));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public List<IPI_CActionWrapper_NOT_USED> getAllRecordingsBySJID(int id) {
        ArrayList<IPI_CActionWrapper_NOT_USED> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE servicejob_id=" + id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                IPI_CActionWrapper_NOT_USED recordItem = new IPI_CActionWrapper_NOT_USED();
                recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setProjectJob_ID(cursor.getInt(1));
                recordItem.setSerialNo(cursor.getString(2));
                recordItem.setCarNo(cursor.getString(3));
                recordItem.setDescription(cursor.getString(4));
                recordItem.setTargetRemedyDate(cursor.getString(5));
                recordItem.setCompletionDate(cursor.getString(6));
                recordItem.setRemarks(cursor.getString(7));
                recordItem.setDispostion(cursor.getString(8));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public IPI_CActionWrapper_NOT_USED getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        IPI_CActionWrapper_NOT_USED recordItem = new IPI_CActionWrapper_NOT_USED();
        if (cursor.moveToPosition(position)) {
            recordItem.setID(Integer.parseInt(cursor.getString(0)));
            recordItem.setProjectJob_ID(cursor.getInt(1));
            recordItem.setSerialNo(cursor.getString(2));
            recordItem.setCarNo(cursor.getString(3));
            recordItem.setDescription(cursor.getString(4));
            recordItem.setTargetRemedyDate(cursor.getString(5));
            recordItem.setCompletionDate(cursor.getString(6));
            recordItem.setRemarks(cursor.getString(7));
            recordItem.setDispostion(cursor.getString(8));
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
                DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_ID + "=?", whereArgs);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onIPI_TaskEntryDeleted();
        }
        Log.e(LOG_TAG, "addRecording " + id);
    }

    public int getCount() {
        SQLiteDatabase db = getDB();
        String[] projection = { DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_ID };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int addRecording(String recordingName, String filePath, long length, int serviceId, String taken) {

        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_SERVICE_ID, serviceId);
        cv.put(DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_FILE_PATH, filePath);
        cv.put(DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_LENGTH, length);
        cv.put(DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_TIME_ADDED, System.currentTimeMillis());
        cv.put(DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_TAKEN, taken);
        long idInserted = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        int rowId = (int)idInserted;
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewIPI_TaskEntryAdded(recordingName);
        }
        return rowId;
    }

    public void renameItem(IPI_CActionWrapper_NOT_USED item, String recordingName, String filePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_FILE_PATH, filePath);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onIPI_TaskEntryRenamed(item.getCarNo());
        }
    }

    /**
     * Has inserted at least one recordings in the DB
     * This is called before submitting the Files to the web
     * @param servicejob_id
     * @return
     */
    public boolean hasInsertedRecordings(int servicejob_id) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE "+ DBHelperItem.COLUMN_NAME_CORRECTIVE_ACTION_SERVICE_ID + "=" + servicejob_id;
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

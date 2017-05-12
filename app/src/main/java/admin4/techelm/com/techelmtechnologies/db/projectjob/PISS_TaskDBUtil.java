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
import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;

/**
 * Created by admin 4 on 26/04/2017.
 * DB Util for PISS Task Table
 */

public class PISS_TaskDBUtil extends DatabaseAccess {

    private static final String TAG = PISS_TaskDBUtil.class.getSimpleName();

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "projectjob_piss_tasks";

        public static final String COLUMN_NAME_PISS_TASK_ID = "id";
        public static final String COLUMN_NAME_PISS_TASK_PROJECT_JOB_ID = "projectjob_id";
        public static final String COLUMN_NAME_PISS_TASK_SERIAL_NO = "serial_no";
        public static final String COLUMN_NAME_PISS_TASK_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PISS_TASK_CONFORMANCE = "conformance";
        public static final String COLUMN_NAME_PISS_TASK_COMMENTS = "comments";
        public static final String COLUMN_NAME_PISS_TASK_STATUS = "status";
        public static final String COLUMN_NAME_PISS_TASK_DRAWING_BEFORE = "drawing_before";
        public static final String COLUMN_NAME_PISS_TASK_DRAWING_AFTER = "drawing_after";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewPISS_TaskEntryAdded(String fileName);
        void onPISS_TaskEntryRenamed(String fileName);
        void onPISS_TaskEntryDeleted();
    }
    /**
     * Private constructor to avoid object creation from outside classes.
     * @param context
     */
    public PISS_TaskDBUtil(Context context) {
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
    public PISS_TaskDBUtil(Context context, String message) {
        super(context);
        Log.e(TAG, message);
    }

    public PISSTaskWrapper getDetailsByProjectJobID(int projectJobID) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE " + DBHelperItem.COLUMN_NAME_PISS_TASK_PROJECT_JOB_ID + "=" +projectJobID;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        PISSTaskWrapper item = new PISSTaskWrapper();
        if (cursor.moveToFirst()) {
            item.setID(Integer.parseInt(cursor.getString(0)));
            item.setProjectID(Integer.parseInt(cursor.getString(1)));
            item.setSerialNo(cursor.getString(2));
            item.setDescription(cursor.getString(3));
            item.setConformance(cursor.getString(4));
            item.setComments(cursor.getString(5));
            item.setStatus(cursor.getString(6));
            item.setDrawingBefore(cursor.getString(7));
            item.setDrawingAfter(cursor.getString(8));
        }
        // Log.e(TAG, "getAllJSDetailsByServiceJobID: " + item.toString());

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return item;
    }

    public List<PISSTaskWrapper> getAllTask() {
        ArrayList<PISSTaskWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PISSTaskWrapper item = new PISSTaskWrapper();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setProjectID(Integer.parseInt(cursor.getString(1)));
                item.setSerialNo(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setConformance(cursor.getString(4));
                item.setComments(cursor.getString(5));
                item.setStatus(cursor.getString(6));
                item.setDrawingBefore(cursor.getString(7));
                item.setDrawingAfter(cursor.getString(8));
                list.add(item);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }



    public List<PISSTaskWrapper> getAllRecordingsBySJID_ByTaken(int id, String taken) {
        ArrayList<PISSTaskWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE servicejob_id=" + id
                + " AND taken='" + taken + "'";
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PISSTaskWrapper item = new PISSTaskWrapper();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setProjectID(Integer.parseInt(cursor.getString(1)));
                item.setSerialNo(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setConformance(cursor.getString(4));
                item.setComments(cursor.getString(5));
                item.setStatus(cursor.getString(6));
                item.setDrawingBefore(cursor.getString(7));
                item.setDrawingAfter(cursor.getString(8));
                list.add(item);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public List<PISSTaskWrapper> getAllRecordingsBySJID(int id) {
        ArrayList<PISSTaskWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE servicejob_id=" + id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PISSTaskWrapper item = new PISSTaskWrapper();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setProjectID(Integer.parseInt(cursor.getString(1)));
                item.setSerialNo(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setConformance(cursor.getString(4));
                item.setComments(cursor.getString(5));
                item.setStatus(cursor.getString(6));
                item.setDrawingBefore(cursor.getString(7));
                item.setDrawingAfter(cursor.getString(8));
                list.add(item);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public PISSTaskWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        PISSTaskWrapper item = new PISSTaskWrapper();
        if (cursor.moveToPosition(position)) {
            item.setID(Integer.parseInt(cursor.getString(0)));
            item.setProjectID(Integer.parseInt(cursor.getString(1)));
            item.setSerialNo(cursor.getString(2));
            item.setDescription(cursor.getString(3));
            item.setConformance(cursor.getString(4));
            item.setComments(cursor.getString(5));
            item.setStatus(cursor.getString(6));
            item.setDrawingBefore(cursor.getString(7));
            item.setDrawingAfter(cursor.getString(8));
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
                DBHelperItem.COLUMN_NAME_PISS_TASK_ID + "=?", whereArgs);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onPISS_TaskEntryDeleted();
        }
        Log.e(TAG, "addRecording " + id);
    }

    public int getCount() {
        SQLiteDatabase db = getDB();
        String[] projection = { DBHelperItem.COLUMN_NAME_PISS_TASK_ID };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int addRecording(String recordingName, String filePath, long length, int serviceId, String taken) {

        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_PROJECT_JOB_ID, serviceId);
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_SERIAL_NO, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_DESCRIPTION, filePath);
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_CONFORMANCE, length);
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_COMMENTS, System.currentTimeMillis());
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_STATUS, taken);
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_DRAWING_BEFORE, taken);
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_DRAWING_AFTER, taken);
        long idInserted = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        int rowId = (int)idInserted;
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewPISS_TaskEntryAdded(recordingName);
        }
        return rowId;
    }

    public int addPISSTask(PISSTaskWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_PROJECT_JOB_ID, item.getProjectID());
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_SERIAL_NO, item.getSerialNo());
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_DESCRIPTION, item.getDescription());
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_CONFORMANCE, item.getConformance());
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_COMMENTS, item.getComments());
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_STATUS, item.getStatus());
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_DRAWING_BEFORE, item.getDrawingBefore());
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_DRAWING_AFTER, item.getDrawingAfter());
        // cv.put(DBHelperItem.COLUMN_NAME_UPLOADS_ID, length);

        if (db.insert(DBHelperItem.TABLE_NAME, null, cv) < 0) { // Update if Already existed on the SQLite DB
            int rowAffected = db.update(DBHelperItem.TABLE_NAME, cv,
                    DBHelperItem.COLUMN_NAME_PISS_TASK_ID + "=" + item.getID(), null);
            Log.e(TAG, "addPISSTask ROWS AFFECTED " + rowAffected);
        }

        Log.e(TAG, "addPISSTask INSERTED ID " + item.getID());
        Log.e(TAG, "addPISSTask INSERTED " +item.getID());
        return item.getID();
    }

    public void renameItem(PISSTaskWrapper item, String recordingName, String filePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_SERIAL_NO, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_PISS_TASK_DESCRIPTION, filePath);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_PISS_TASK_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onPISS_TaskEntryRenamed(item.getComments());
        }
    }

    /**
     * Has inserted at least one drawing by projectjob_id in the DB
     * This is called before submitting the Files to the web
     * @param projectjob_id
     * @return
     */
    public boolean hasInsertedDrawings(int projectjob_id) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE "+ DBHelperItem.COLUMN_NAME_PISS_TASK_PROJECT_JOB_ID + "=" + projectjob_id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        // String item = cursor.getString(1);

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

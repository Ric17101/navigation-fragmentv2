package admin4.techelm.com.techelmtechnologies.db.toolboxmeeting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.db.DatabaseAccess;
import admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.utility.FileUtility;

/**
 * Created by admin 3 on 22/05/2017.
 */

public class UploadsTMDBUtil extends DatabaseAccess {

    private static final String LOG_TAG = "UploadsTMDBUtil";

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "toolboxmeeting_uploads";
        public static final String COLUMN_NAME_UPLOADS_ID = "id";
        public static final String COLUMN_NAME_UPLOADS_PROJECTJOB_ID = "projectjob_id";
        public static final String COLUMN_NAME_UPLOADS_NAME = "upload_name";
        public static final String COLUMN_NAME_UPLOADS_FILE_PATH = "file_path";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewUploadsDBEntryAdded(String fileName);
        void onUploadsDBEntryRenamed(String fileName);
        void onUploadsDBEntryDeleted();
    }
    /**
     * Private constructor to avoid object creation from outside classes.
     * @param context
     */
    public UploadsTMDBUtil(Context context) {
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
    public UploadsTMDBUtil(Context context, String message) {
        super(context);
        Log.e(LOG_TAG, message);
    }

    /*public List<ServiceJobUploadsWrapper> getAllRecordings() {
        ArrayList<ServiceJobUploadsWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + UploadsTMDBUtil.DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobUploadsWrapper item = new ServiceJobUploadsWrapper();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setServiceId(Integer.parseInt(cursor.getString(1)));
                item.setUploadName(cursor.getString(2));
                item.setFilePath(cursor.getString(3));
                item.setTaken(cursor.getString(4));
                if (FileUtility.isFileExist(item.getFilePath() + "/" + item.getUploadName())) { // Skip adding this record if File Doesn't Exist or being Deleted on the Memory Device
                    list.add(item);
                }
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }*/

    public List<ToolboxMeetingUploadsWrapper> getAllUploadsByTMID_ByTaken(int id) {
        ArrayList<ToolboxMeetingUploadsWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + UploadsTMDBUtil.DBHelperItem.TABLE_NAME
                + " WHERE projectjob_id = " + id;
        Log.e("QUERY:",selectQuery);
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        Log.e("QUERY:",cursor.getCount() +"");
        int ctr= 0;
        if (cursor.moveToFirst()) {
            do {
                ToolboxMeetingUploadsWrapper item = new ToolboxMeetingUploadsWrapper();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setProjectjobID(Integer.parseInt(cursor.getString(1)));
                item.setUploadName(cursor.getString(2));
                item.setFilePath(cursor.getString(3));
                Log.e("DB:",item.getID() +"~"+ item.getProjectjobID() +"~"+ item.getUploadName() +"~"+ item.getFilePath());
                if (FileUtility.isFileExist(item.getFilePath())) { // Skip adding this record if File Doesn't Exist or being Deleted on the Memory Device

                }
                list.add(item);
                ctr++;
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        Log.wtf("LENGTH: ",ctr+"");
        return list;
    }

    /*public List<ServiceJobUploadsWrapper> getAllUploadsBySJID(int id) {
        ArrayList<ServiceJobUploadsWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + UploadsTMDBUtil.DBHelperItem.TABLE_NAME
                + " WHERE "+ UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_SERVICE_ID +"=" + id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobUploadsWrapper item = new ServiceJobUploadsWrapper();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setServiceId(Integer.parseInt(cursor.getString(1)));
                item.setUploadName(cursor.getString(2));
                item.setFilePath(cursor.getString(3));
                item.setTaken(cursor.getString(4));
                if (FileUtility.isFileExist(item.getFilePath() + "/" + item.getUploadName())) { // Skip adding this record if File Doesn't Exist or being Deleted on the Memory Device
                    list.add(item);
                }
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }*/

    /*public ServiceJobUploadsWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + UploadsTMDBUtil.DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        ServiceJobUploadsWrapper item = new ServiceJobUploadsWrapper();
        if (cursor.moveToPosition(position)) {
            item.setID(Integer.parseInt(cursor.getString(0)));
            item.setServiceId(Integer.parseInt(cursor.getString(1)));
            item.setUploadName(cursor.getString(2));
            item.setFilePath(cursor.getString(3));
            item.setTaken(cursor.getString(4));
        } else {
            item = null;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return item;
    }*/

    public void removeItemWithId(int id) {
        SQLiteDatabase db = getDB();
        String[] whereArgs = { String.valueOf(id) };
        db.delete(UploadsTMDBUtil.DBHelperItem.TABLE_NAME,
                UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_ID + "=?", whereArgs);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onUploadsDBEntryDeleted();
        }
        Log.e(LOG_TAG, "addRecording " + id);
    }

    public int getCount() {
        SQLiteDatabase db = getDB();
        String[] projection = { UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_ID };
        Cursor c = db.query(UploadsTMDBUtil.DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    // public int addNewPart(String uploadName, String filePath, int serviceId) {
    public int addUpload(ToolboxMeetingUploadsWrapper item) {

        SQLiteDatabase db = getDB();

        db.delete(UploadsTMDBUtil.DBHelperItem.TABLE_NAME,"projectjob_id = " + item.getProjectjobID(), null);

        ContentValues cv = new ContentValues();
        cv.put(UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_PROJECTJOB_ID, item.getProjectjobID());
        cv.put(UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_NAME, item.getUploadName());
        cv.put(UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_FILE_PATH, item.getFilePath());
        // cv.put(DBHelperItem.COLUMN_NAME_UPLOADS_ID, length);
        Log.wtf("DATA:", UploadsTMDBUtil.DBHelperItem.TABLE_NAME +"/"+ item.getProjectjobID() +"/"+ item.getUploadName() +"/"+ item.getFilePath());
        long idInserted = db.insert(UploadsTMDBUtil.DBHelperItem.TABLE_NAME, null, cv);
        int rowId = (int)idInserted;
        /*if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewUploadsDBEntryAdded(item.getUploadName());
        }*/
        return rowId;
    }

    /*public void renameItem(ServiceJobUploadsWrapper item, String recordingName, String filePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_NAME, recordingName);
        cv.put(UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_FILE_PATH, filePath);
        db.update(UploadsTMDBUtil.DBHelperItem.TABLE_NAME, cv,
                UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onUploadsDBEntryRenamed(item.getUploadName());
        }
    }*/

    /*public long restoreRecording(ServiceJobUploadsWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_NAME, item.getUploadName());
        cv.put(UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_FILE_PATH, item.getFilePath());
        cv.put(UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_SERVICE_ID, item.getServiceId());
        cv.put(UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_ID, item.getID());
        long rowId = db.insert(UploadsTMDBUtil.DBHelperItem.TABLE_NAME, null, cv);
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewUploadsDBEntryAdded(item.getUploadName());
        }
        return rowId;
    }*/

    // Has At least one Recordings
    /*public boolean hasInsertedRecordings(int servicejob_id) {
        String selectQuery = "SELECT * FROM " + UploadsTMDBUtil.DBHelperItem.TABLE_NAME
                + " WHERE "+ UploadsTMDBUtil.DBHelperItem.COLUMN_NAME_UPLOADS_SERVICE_ID + "=" + servicejob_id;
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
    }*/
}

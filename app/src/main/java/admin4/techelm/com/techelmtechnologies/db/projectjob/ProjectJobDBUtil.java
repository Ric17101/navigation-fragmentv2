package admin4.techelm.com.techelmtechnologies.db.projectjob;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.db.DatabaseAccess;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;

/**
 * Created by admin 4 on 21/02/2017.
 */

public class ProjectJobDBUtil extends DatabaseAccess {

    private static final String LOG_TAG = "ProjectJobDBUtil";

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "projectjob";
        public static final String COLUMN_NAME_PJ_ID = "id";
        public static final String COLUMN_NAME_PJ_SERVICE_NO = "proj_ref";
        public static final String COLUMN_NAME_PJ_CUSTOMER_ID = "project_site";
        public static final String COLUMN_NAME_PJ_SERVICE_ID = "target_completion_date";
        public static final String COLUMN_NAME_PJ_ENGINEER_ID = "first_inspector";
        public static final String COLUMN_NAME_PJ_PRICE_ID = "second_inspector";
        public static final String COLUMN_NAME_PJ_COMPLAINT = "third_inspector";
        public static final String COLUMN_NAME_PJ_REMARKS = "customer_id";
        public static final String COLUMN_NAME_PJ_CUSTOMER_NAME = "customer_name";
        public static final String COLUMN_NAME_PJ_START_DATE = "start_date";
        public static final String COLUMN_NAME_PJ_END_DATE = "end_date";
        public static final String COLUMN_NAME_PJ_STATUS = "status_flag";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewPJEntryAdded(String serviceNum);
        void onPJEntryUpdated(String remarks);
        void onPJEntryDeleted();
    }
    /**
     * Private constructor to avoid object creation from outside classes.
     * @param context
     */
    public ProjectJobDBUtil(Context context) {
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
    public ProjectJobDBUtil(Context context, String message) {
        super(context);
        Log.e(LOG_TAG, message);
    }

    public int addServiceJob(ProjectJobWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PJ_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_NO, item.getProjectRef());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_CUSTOMER_ID, item.getProjectSite());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_ID, item.getTargetCompletionDate());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_ENGINEER_ID, item.getFirstInspector());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_PRICE_ID, item.getSecondInspector());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_COMPLAINT, item.getSecondInspector());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_REMARKS, item.getThirdInspector());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_START_DATE, item.getCustomerID());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_END_DATE, item.getCustomerName());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_NO, item.getStartDate());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_CUSTOMER_NAME, item.getEndDate());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_STATUS, item.getStatus());

        if (db.insert(DBHelperItem.TABLE_NAME, null, cv) < 0) { // Update if Already existed on the SQLite DB
            int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                    DBHelperItem.COLUMN_NAME_PJ_ID + "=" + item.getID(), null);
            Log.e(LOG_TAG, "addServiceJob ROWS AFFECTED " + rowaffected);
        }

        Log.e(LOG_TAG, "addServiceJob INSERTED ID " + item.getID());
        Log.e(LOG_TAG, "addServiceJob INSERTED " + getAllJSDetailsByServiceJobID(item.getID()) + "");
        return item.getID();
    }

    public void removeServiceJob(String serviceID) {
        SQLiteDatabase db = getDB();
        String[] whereArgs = { String.valueOf(serviceID) };
        db.delete(DBHelperItem.TABLE_NAME,
                DBHelperItem.COLUMN_NAME_PJ_SERVICE_ID + "=?", whereArgs);

        Log.e(LOG_TAG, "removeServiceJob " + serviceID);
    }

    /*public List<ProjectJobWrapper> getAllDetailsOfServiceJob() {
        ArrayList<ProjectJobWrapper> translationList = new ArrayList<>();
        int x = 0;
        do {
            ProjectJobWrapper alpha = new ProjectJobWrapper();
            alpha.setID(Integer.parseInt(x + ""));
            alpha.setOthers(x + "");
            alpha.setStartDate("TestDate" + x);
            alpha.setServiceNumber("00" + x);
            alpha.setCustomerID("Customer" + x);
            alpha.setEngineerID("Engineer" + x);
            alpha.setStatus((x % 2 == 1) ? "Pending" : "Completed");
            translationList.add(alpha);
            x++;
        } while (x != 10);
        return translationList;
    }*/

    public List<ProjectJobWrapper> getAllProjects() {
        ArrayList<ProjectJobWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ProjectJobWrapper recordItem = new ProjectJobWrapper();
                recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setProjectRef(cursor.getString(1));
                recordItem.setProjectSite(cursor.getString(2));
                recordItem.setTargetCompletionDate(cursor.getString(3));
                recordItem.setFirstInspector(cursor.getString(4));
                recordItem.setSecondInspector(cursor.getString(5));
                recordItem.setThirdInspector(cursor.getString(6));
                recordItem.setCustomerID(cursor.getInt(7));
                recordItem.setCustomerName(cursor.getString(8));
                recordItem.setStartDate(cursor.getString(9));
                recordItem.setEndDate(cursor.getString(10));
                recordItem.setStatus(cursor.getInt(11));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    private String getCustomerNameByID(String id) {
        String selectQuery = "SELECT * FROM customer WHERE id=" + id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        String customerName = "";
        if (cursor.moveToFirst()) {
            customerName = cursor.getString(1);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return customerName;
    }


    public List<ProjectJobWrapper> getAllJSDetailsByID(int id) {
        ArrayList<ProjectJobWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME + " WHERE id="+id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ProjectJobWrapper recordItem = new ProjectJobWrapper();
                recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setProjectRef(cursor.getString(1));
                recordItem.setProjectSite(cursor.getString(2));
                recordItem.setTargetCompletionDate(cursor.getString(3));
                recordItem.setFirstInspector(cursor.getString(4));
                recordItem.setSecondInspector(cursor.getString(5));
                recordItem.setThirdInspector(cursor.getString(6));
                recordItem.setCustomerID(cursor.getInt(7));
                recordItem.setCustomerName(cursor.getString(8));
                recordItem.setStartDate(cursor.getString(9));
                recordItem.setEndDate(cursor.getString(10));
                recordItem.setStatus(cursor.getInt(11));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public ProjectJobWrapper getAllJSDetailsByServiceJobID(int serviceJobID) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE " + DBHelperItem.COLUMN_NAME_PJ_ID + "=" +serviceJobID;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        ProjectJobWrapper recordItem = new ProjectJobWrapper();
        if (cursor.moveToFirst()) {
            recordItem.setID(Integer.parseInt(cursor.getString(0)));
            recordItem.setProjectRef(cursor.getString(1));
            recordItem.setProjectSite(cursor.getString(2));
            recordItem.setTargetCompletionDate(cursor.getString(3));
            recordItem.setFirstInspector(cursor.getString(4));
            recordItem.setSecondInspector(cursor.getString(5));
            recordItem.setThirdInspector(cursor.getString(6));
            recordItem.setCustomerID(cursor.getInt(7));
            recordItem.setCustomerName(cursor.getString(8));
            recordItem.setStartDate(cursor.getString(9));
            recordItem.setEndDate(cursor.getString(10));
            recordItem.setStatus(cursor.getInt(11));
        }
        // Log.e(LOG_TAG, "getAllJSDetailsByServiceJobID: " + recordItem.toString());

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return recordItem;
    }

    /**
     * Update remarks on the Service Job FRGMT 1
     * @param id
     * @param remarks
     */
    public void updateRequestIDRemarks(int id, String remarks) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PJ_REMARKS, remarks);
        int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_PJ_ID + "=" + id, null);
        Log.e(LOG_TAG, "updateRequestIDRemarks ROWS AFFECTED " + rowaffected);
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onPJEntryUpdated(remarks);
        }
    }

    /**
     * Update remarks_before on the Service Job FRGMT 1_AFTER
     * @param id - sjid
     * @param remarks - after remarks
     */
    public void updateRequestIDRemarks_AFTER(int id, String remarks) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        // cv.put(DBHelperItem.COLUMN_NAME_PJ_REMARKS_AFTER, remarks);
        int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_PJ_ID + "=" + id, null);
        Log.e(LOG_TAG, "updateRequestIDRemarks_AFTER ROWS AFFECTED " + rowaffected);
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onPJEntryUpdated(remarks);
        }
    }

    public void updateRequestIDSignature(int requestID, String signatureName, String signatureFilePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
//        cv.put(DBHelperItem.COLUMN_NAME_PJ_SIGNATURE_NAME, signatureName);
//        cv.put(DBHelperItem.COLUMN_NAME_PJ_SIGNATURE_FILE_PATH, signatureFilePath);
        int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_PJ_ID + "=" + requestID, null);
        Log.e(LOG_TAG, "updateRequestIDSignature ROWS AFFECTED " + rowaffected);

        if (mOnDatabaseChangedListener != null) {
            // mOnDatabaseChangedListener.onIPI_PWDEntryUpdated(item.getServiceNumber());
        }
    }

    public ProjectJobWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        ProjectJobWrapper recordItem = new ProjectJobWrapper();
        if (cursor.moveToPosition(position)) {
            recordItem.setID(Integer.parseInt(cursor.getString(0)));
            recordItem.setProjectRef(cursor.getString(1));
            recordItem.setProjectSite(cursor.getString(2));
            recordItem.setTargetCompletionDate(cursor.getString(3));
            recordItem.setFirstInspector(cursor.getString(4));
            recordItem.setSecondInspector(cursor.getString(5));
            recordItem.setThirdInspector(cursor.getString(6));
            recordItem.setCustomerID(cursor.getInt(7));
            recordItem.setCustomerName(cursor.getString(8));
            recordItem.setStartDate(cursor.getString(9));
            recordItem.setEndDate(cursor.getString(10));
            recordItem.setStatus(cursor.getInt(11));
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
                DBHelperItem.COLUMN_NAME_PJ_ID + "=?", whereArgs);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onPJEntryDeleted();
        }
        Log.e(LOG_TAG, "addRecording " + id);
    }

    public int getCount() {
        SQLiteDatabase db = getDB();
        String[] projection = { DBHelperItem.COLUMN_NAME_PJ_ID };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int addRecording(ProjectJobWrapper item) {
        SQLiteDatabase db = getDB();

        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PJ_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_NO, item.getProjectRef());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_CUSTOMER_ID, item.getProjectSite());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_ID, item.getTargetCompletionDate());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_ENGINEER_ID, item.getFirstInspector());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_PRICE_ID, item.getSecondInspector());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_COMPLAINT, item.getSecondInspector());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_REMARKS, item.getThirdInspector());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_START_DATE, item.getCustomerID());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_END_DATE, item.getCustomerName());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_NO, item.getStartDate());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_CUSTOMER_NAME, item.getEndDate());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_STATUS, item.getStatus());
        long idInserted = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        int rowId = (int)idInserted;
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewPJEntryAdded(item.getProjectRef());
        }
        return rowId;
    }

    public void renameItem(ProjectJobWrapper item, String serviceNum, String startDate) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_NO, serviceNum);
        cv.put(DBHelperItem.COLUMN_NAME_PJ_START_DATE, startDate);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_PJ_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onPJEntryUpdated(item.getProjectRef());
        }
    }

    /**
     * Test if ServiceJob has Remarks
     *  This is called before submitting the Files to the web
     * @param id
     * @return
     */
    public boolean hasRemarks(int id) {
        String selectQuery = "SELECT remarks FROM " + DBHelperItem.TABLE_NAME
                + " WHERE " + DBHelperItem.COLUMN_NAME_PJ_ID + "=" + id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        boolean result;
        if (cursor.moveToFirst()) {
            String recordItem = cursor.getString(0);
            if ("".equals(recordItem) || recordItem == null) {
                result = false;
            } else {
                result = true;
            }
        } else { // no data
            result = false;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return result;
    }
}

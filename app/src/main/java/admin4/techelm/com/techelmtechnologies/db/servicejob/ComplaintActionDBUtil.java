package admin4.techelm.com.techelmtechnologies.db.servicejob;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.db.DatabaseAccess;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaintWrapper;

/**
 * Created by admin 4 on 13/03/2017.
 * File Replacements
 */

public class ComplaintActionDBUtil extends DatabaseAccess {

    private static final String TAG = ComplaintActionDBUtil.class.getSimpleName();

    // TODO: Will be used if and only if momre than one fragments calss use this
    private int setFragmentState(String fragmentName) {
        int fragmentState = 0;
        if (fragmentName == "FRAGMENT1") {
            fragmentState = 1;
        } else if (fragmentName == "FRAGMENT2") {
            fragmentState = 2;
        }
        return fragmentState;
    }
    /*
        CREATE TABLE `servicejob_complaints` (
            `id`	INTEGER,
            `servicejob_id`	INTEGER,
            `servicejob_cm_cf_id`	INTEGER,
            `complaint_fault_id`	INTEGER,
            `complaint_mobile_id`	INTEGER,
            `category_id`	INTEGER,
            `category`	TEXT,
            `action_id`	INTEGER,
            `action`	TEXT
        );
     */

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "servicejob_complaints";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SERVICE_JOB_ID = "servicejob_id";
        public static final String COLUMN_NAME_SERVICE_JOB_CMCF_ID = "servicejob_cm_cf_id";
        public static final String COLUMN_NAME_SERVICE_JOB_COMPLAINT = "complaint";
        public static final String COLUMN_NAME_CF_ID = "complaint_fault_id";
        public static final String COLUMN_NAME_CM_ID = "complaint_mobile_id";//
        public static final String COLUMN_NAME_CATEGORY_ID = "category_id";//
        public static final String COLUMN_NAME_CATEGORY = "category";//
        public static final String COLUMN_NAME_ACTION_ID = "action_id";
        public static final String COLUMN_NAME_ACTION = "action";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewActionEntryAdded(String partName);
        void onActionEntryDeleted();
    }
    /**
     * Private constructor to avoid object creation from outside classes.
     * @param context
     */
    public ComplaintActionDBUtil(Context context) {
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
    public ComplaintActionDBUtil(Context context, String message) {
        super(context);
        Log.e(TAG, message);
    }

    public List<ServiceJobComplaintWrapper> getAllParts() {
        ArrayList<ServiceJobComplaintWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobComplaintWrapper item = new ServiceJobComplaintWrapper();
                item.setID(cursor.getInt(0));
                item.setServiceJobID(Integer.parseInt(cursor.getString(1)));
                item.setComplaint(cursor.getString(2));
                item.setSJ_CM_CF_ID(Integer.parseInt(cursor.getString(3)));
                item.setComplaintFaultID(Integer.parseInt(cursor.getString(4)));
                item.setComplaintMobileID(Integer.parseInt(cursor.getString(5)));
                item.setCategoryID(Integer.parseInt(cursor.getString(6)));
                item.setCategory(cursor.getString(7));
                item.setActionID(Integer.parseInt(cursor.getString(8)));
                item.setAction(cursor.getString(9));
                list.add(item);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public ArrayList<ServiceJobComplaintWrapper> getAllActionsBySJID(int serviceJobID) {
        ArrayList<ServiceJobComplaintWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME + " WHERE servicejob_id="+serviceJobID;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobComplaintWrapper item = new ServiceJobComplaintWrapper();
                item.setID(cursor.getInt(0));
                item.setServiceJobID(Integer.parseInt(cursor.getString(1)));
                item.setComplaint(cursor.getString(2));
                item.setSJ_CM_CF_ID(Integer.parseInt(cursor.getString(3)));
                item.setComplaintFaultID(Integer.parseInt(cursor.getString(4)));
                item.setComplaintMobileID(Integer.parseInt(cursor.getString(5)));
                item.setCategoryID(Integer.parseInt(cursor.getString(6)));
                item.setCategory(cursor.getString(7));
                item.setActionID(Integer.parseInt(cursor.getString(8)));
                item.setAction(cursor.getString(9));
                list.add(item);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public ServiceJobComplaintWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        ServiceJobComplaintWrapper item = new ServiceJobComplaintWrapper();
        if (cursor.moveToPosition(position)) {
            item.setID(cursor.getInt(0));
            item.setServiceJobID(Integer.parseInt(cursor.getString(1)));
            item.setComplaint(cursor.getString(2));
            item.setSJ_CM_CF_ID(Integer.parseInt(cursor.getString(3)));
            item.setComplaintFaultID(Integer.parseInt(cursor.getString(4)));
            item.setComplaintMobileID(Integer.parseInt(cursor.getString(5)));
            item.setCategoryID(Integer.parseInt(cursor.getString(6)));
            item.setCategory(cursor.getString(7));
            item.setActionID(Integer.parseInt(cursor.getString(8)));
            item.setAction(cursor.getString(9));
        } else {
            item = null;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return item;
    }

    public void removeItemWithId(ServiceJobComplaintWrapper item) {
        SQLiteDatabase db = getDB();

        String whereClause =
                DBHelperItem.COLUMN_NAME_SERVICE_JOB_CMCF_ID + "=" + item.getSJ_CM_CF_ID() + " AND " +
                DBHelperItem.COLUMN_NAME_CM_ID + "=" + item.getComplaintMobileID() + " AND " +
                DBHelperItem.COLUMN_NAME_ACTION_ID + "=" + item.getActionID() + " AND " +
                DBHelperItem.COLUMN_NAME_SERVICE_JOB_ID + "=" + item.getServiceJobID();

        int id = db.delete(DBHelperItem.TABLE_NAME, whereClause, null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onActionEntryDeleted();
        }
        Log.e(TAG, "addParts " + id);
    }

    public int getCount() {
        SQLiteDatabase db = getDB();
        String[] projection = { DBHelperItem.COLUMN_NAME_ID };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int addNewAction(ServiceJobComplaintWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();

        // cv.put(DBHelperItem.COLUMN_NAME_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_SERVICE_JOB_ID, item.getServiceJobID());
        cv.put(DBHelperItem.COLUMN_NAME_SERVICE_JOB_CMCF_ID, item.getSJ_CM_CF_ID());
        cv.put(DBHelperItem.COLUMN_NAME_SERVICE_JOB_COMPLAINT, item.getComplaint());
        cv.put(DBHelperItem.COLUMN_NAME_CF_ID, item.getComplaintFaultID());
        cv.put(DBHelperItem.COLUMN_NAME_CM_ID, item.getComplaintMobileID());
        cv.put(DBHelperItem.COLUMN_NAME_CATEGORY_ID, item.getCategoryID());
        cv.put(DBHelperItem.COLUMN_NAME_CATEGORY, item.getCategory());
        cv.put(DBHelperItem.COLUMN_NAME_ACTION_ID, item.getActionID());
        cv.put(DBHelperItem.COLUMN_NAME_ACTION, item.getAction());

        long rowAffected;
        if (hasAction(item)) {
            rowAffected = db.update(DBHelperItem.TABLE_NAME, cv,
                    DBHelperItem.COLUMN_NAME_SERVICE_JOB_CMCF_ID + "=" + item.getSJ_CM_CF_ID() + " AND " +
                    DBHelperItem.COLUMN_NAME_CM_ID + "=" + item.getComplaintMobileID() + " AND " +
                    DBHelperItem.COLUMN_NAME_ACTION_ID + "=" + item.getActionID() + " AND " +
                    DBHelperItem.COLUMN_NAME_SERVICE_JOB_ID + "=" + item.getServiceJobID(),
                    null);
            Log.e(TAG, "addNewAction ROWS UPDATED " + rowAffected);
        } else {
            rowAffected = db.insert(DBHelperItem.TABLE_NAME, null, cv);
            Log.e(TAG, "NewAction ROWS INSERTED " + rowAffected);
        }

        return (int) rowAffected;
    }

    public boolean hasAction(ServiceJobComplaintWrapper item) {
        boolean hasAction = false;
        String selectQuery = "SELECT " + DBHelperItem.COLUMN_NAME_CATEGORY
                + " FROM " + DBHelperItem.TABLE_NAME + " WHERE " +
                DBHelperItem.COLUMN_NAME_SERVICE_JOB_CMCF_ID + "=" + item.getSJ_CM_CF_ID() + " AND " +
                DBHelperItem.COLUMN_NAME_CM_ID + "=" + item.getComplaintMobileID() + " AND " +
                DBHelperItem.COLUMN_NAME_ACTION_ID + "=" + item.getActionID() + " AND " +
                DBHelperItem.COLUMN_NAME_SERVICE_JOB_ID + "=" + item.getServiceJobID();
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            hasAction = true;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return hasAction;
    }

    public void editPart(ServiceJobComplaintWrapper item, String fragmentName) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_SERVICE_JOB_ID, item.getServiceJobID());
        cv.put(DBHelperItem.COLUMN_NAME_SERVICE_JOB_CMCF_ID, item.getSJ_CM_CF_ID());
        cv.put(DBHelperItem.COLUMN_NAME_SERVICE_JOB_COMPLAINT, item.getComplaint());
        cv.put(DBHelperItem.COLUMN_NAME_CF_ID, item.getComplaintFaultID());
        cv.put(DBHelperItem.COLUMN_NAME_CM_ID, item.getComplaintMobileID());
        cv.put(DBHelperItem.COLUMN_NAME_CATEGORY_ID, item.getCategoryID());
        cv.put(DBHelperItem.COLUMN_NAME_CATEGORY, item.getCategory());
        cv.put(DBHelperItem.COLUMN_NAME_ACTION_ID, item.getActionID());
        cv.put(DBHelperItem.COLUMN_NAME_ACTION, item.getAction());
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewActionEntryAdded(item.getAction());
        }
    }

}

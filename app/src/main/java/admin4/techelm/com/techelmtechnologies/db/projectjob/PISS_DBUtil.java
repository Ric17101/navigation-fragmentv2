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
import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSWrapper;

/**
 * Created by admin 4 on 26/04/2017.
 */

public class PISS_DBUtil extends DatabaseAccess {

    private static final String LOG_TAG = "PISS_DBUtil";

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "projectjob_piss";
        public static final String COLUMN_NAME_PJ_ID = "id";
        public static final String COLUMN_NAME_PJ_SERVICE_NO = "projectjob_id";
        public static final String COLUMN_NAME_PJ_CUSTOMER_ID = "car_park_code";
        public static final String COLUMN_NAME_PJ_SERVICE_ID = "property_officer";
        public static final String COLUMN_NAME_PJ_ENGINEER_ID = "tc_lew";
        public static final String COLUMN_NAME_PJ_PRICE_ID = "property_officer_telNo";
        public static final String COLUMN_NAME_PJ_COMPLAINT = "property_officer_mobileNo";
        public static final String COLUMN_NAME_PJ_REMARKS = "property_officer_branch";
        public static final String COLUMN_NAME_PJ_CUSTOMER_NAME = "tc_lew_telNo";
        public static final String COLUMN_NAME_PJ_START_DATE = "tc_lew_mobileNo";
        public static final String COLUMN_NAME_PJ_END_DATE = "tc_lew_email";
        public static final String COLUMN_NAME_PJ_STATUS = "remarks";
        public static final String COLUMN_NAME_PJ_STATUS_ = "date_site_walk ";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewPISSEntryAdded(String serviceNum);
        void onPISSEntryUpdated(String remarks);
        void onPISSEntryDeleted();
    }
    /**
     * Private constructor to avoid object creation from outside classes.
     * @param context
     */
    public PISS_DBUtil(Context context) {
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
    public PISS_DBUtil(Context context, String message) {
        super(context);
        Log.e(LOG_TAG, message);
    }

    public int addServiceJob(PISSWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PJ_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_NO, item.getProjectJobID());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_CUSTOMER_ID, item.getCarParkCode());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_ID, item.getPropertyOfficer());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_ENGINEER_ID, item.getTCLew());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_PRICE_ID, item.getPropertyOfficerTelNo());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_COMPLAINT, item.getPropertyOfficerMobileNo());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_REMARKS, item.getPropertyOfficerBranch());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_START_DATE, item.getTCLewTelNo());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_END_DATE, item.getTCLewMobileNo());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_STATUS, item.getTCLewEmail());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_CUSTOMER_NAME, item.getRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_STATUS_, item.getDateSiteWalk());

        if (db.insert(DBHelperItem.TABLE_NAME, null, cv) < 0) { // Update if Already existed on the SQLite DB
            int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                    DBHelperItem.COLUMN_NAME_PJ_ID + "=" + item.getID(), null);
            Log.e(LOG_TAG, "addServiceJob ROWS AFFECTED " + rowaffected);
        }

        Log.e(LOG_TAG, "addServiceJob INSERTED ID " + item.getID());
        Log.e(LOG_TAG, "addServiceJob INSERTED " + getAllJSDetailsByServiceJobID(item.getID()).toString());
        return item.getID();
    }

    public void removeServiceJob(String serviceID) {
        SQLiteDatabase db = getDB();
        String[] whereArgs = { String.valueOf(serviceID) };
        db.delete(DBHelperItem.TABLE_NAME,
                DBHelperItem.COLUMN_NAME_PJ_SERVICE_ID + "=?", whereArgs);

        Log.e(LOG_TAG, "removeServiceJob " + serviceID);
    }

    /*public List<PISSWrapper> getAllDetailsOfServiceJob() {
        ArrayList<PISSWrapper> translationList = new ArrayList<>();
        int x = 0;
        do {
            PISSWrapper alpha = new PISSWrapper();
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

    public List<PISSWrapper> getAllRecordings() {
        ArrayList<PISSWrapper> list = new ArrayList<PISSWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PISSWrapper recordItem = new PISSWrapper();
                recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setProjectJobID(cursor.getString(1));
                recordItem.setCarParkCode(cursor.getString(2));
                recordItem.setPropertyOfficer(cursor.getString(3));
                recordItem.setTCLew(cursor.getString(4));
                recordItem.setPropertyOfficerTelNo(cursor.getString(5));
                recordItem.setPropertyOfficerMobileNo(cursor.getString(6));
                recordItem.setPropertyOfficerBranch(cursor.getString(7));
                recordItem.setTCLewTelNo(cursor.getString(8));
                recordItem.setTCLewMobileNo(cursor.getString(9));
                recordItem.setTCLewEmail(cursor.getString(10));
                recordItem.setRemarks(cursor.getString(11));
                recordItem.setDateSiteWalk(cursor.getString(12));
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


    public List<PISSWrapper> getAllJSDetailsByID(int id) {
        ArrayList<PISSWrapper> list = new ArrayList<PISSWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME + " WHERE id="+id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PISSWrapper recordItem = new PISSWrapper();
                recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setProjectJobID(cursor.getString(1));
                recordItem.setCarParkCode(cursor.getString(2));
                recordItem.setPropertyOfficer(cursor.getString(3));
                recordItem.setTCLew(cursor.getString(4));
                recordItem.setPropertyOfficerTelNo(cursor.getString(5));
                recordItem.setPropertyOfficerMobileNo(cursor.getString(6));
                recordItem.setPropertyOfficerBranch(cursor.getString(7));
                recordItem.setTCLewTelNo(cursor.getString(8));
                recordItem.setTCLewMobileNo(cursor.getString(9));
                recordItem.setTCLewEmail(cursor.getString(10));
                recordItem.setRemarks(cursor.getString(11));
                recordItem.setDateSiteWalk(cursor.getString(12));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public PISSWrapper getAllJSDetailsByServiceJobID(int serviceJobID) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE " + DBHelperItem.COLUMN_NAME_PJ_ID + "=" +serviceJobID;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        PISSWrapper recordItem = new PISSWrapper();
        if (cursor.moveToFirst()) {
            recordItem.setID(Integer.parseInt(cursor.getString(0)));
            recordItem.setProjectJobID(cursor.getString(1));
            recordItem.setCarParkCode(cursor.getString(2));
            recordItem.setPropertyOfficer(cursor.getString(3));
            recordItem.setTCLew(cursor.getString(4));
            recordItem.setPropertyOfficerTelNo(cursor.getString(5));
            recordItem.setPropertyOfficerMobileNo(cursor.getString(6));
            recordItem.setPropertyOfficerBranch(cursor.getString(7));
            recordItem.setTCLewTelNo(cursor.getString(8));
            recordItem.setTCLewMobileNo(cursor.getString(9));
            recordItem.setTCLewEmail(cursor.getString(10));
            recordItem.setRemarks(cursor.getString(11));
            recordItem.setDateSiteWalk(cursor.getString(12));
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
            mOnDatabaseChangedListener.onPISSEntryUpdated(remarks);
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
            mOnDatabaseChangedListener.onPISSEntryUpdated(remarks);
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

    public PISSWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        PISSWrapper recordItem = new PISSWrapper();
        if (cursor.moveToPosition(position)) {
            recordItem.setID(Integer.parseInt(cursor.getString(0)));
            recordItem.setProjectJobID(cursor.getString(1));
            recordItem.setCarParkCode(cursor.getString(2));
            recordItem.setPropertyOfficer(cursor.getString(3));
            recordItem.setTCLew(cursor.getString(4));
            recordItem.setPropertyOfficerTelNo(cursor.getString(5));
            recordItem.setPropertyOfficerMobileNo(cursor.getString(6));
            recordItem.setPropertyOfficerBranch(cursor.getString(7));
            recordItem.setTCLewTelNo(cursor.getString(8));
            recordItem.setTCLewMobileNo(cursor.getString(9));
            recordItem.setTCLewEmail(cursor.getString(10));
            recordItem.setRemarks(cursor.getString(11));
            recordItem.setDateSiteWalk(cursor.getString(12));
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
            mOnDatabaseChangedListener.onPISSEntryDeleted();
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

    public int addRecording(PISSWrapper item) {
        SQLiteDatabase db = getDB();

        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PJ_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_NO, item.getProjectJobID());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_CUSTOMER_ID, item.getCarParkCode());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_ID, item.getPropertyOfficer());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_ENGINEER_ID, item.getTCLew());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_PRICE_ID, item.getPropertyOfficerTelNo());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_COMPLAINT, item.getPropertyOfficerMobileNo());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_REMARKS, item.getPropertyOfficerBranch());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_START_DATE, item.getTCLewTelNo());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_END_DATE, item.getTCLewMobileNo());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_CUSTOMER_NAME, item.getTCLewEmail());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_STATUS, item.getRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_PJ_STATUS_, item.getDateSiteWalk());
        long idInserted = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        int rowId = (int)idInserted;
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewPISSEntryAdded(item.getCarParkCode());
        }
        return rowId;
    }

    public void renameItem(PISSWrapper item, String serviceNum, String startDate) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PJ_SERVICE_NO, serviceNum);
        cv.put(DBHelperItem.COLUMN_NAME_PJ_START_DATE, startDate);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_PJ_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onPISSEntryUpdated(item.getCarParkCode());
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

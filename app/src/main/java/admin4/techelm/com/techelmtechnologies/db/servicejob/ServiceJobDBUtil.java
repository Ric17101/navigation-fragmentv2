package admin4.techelm.com.techelmtechnologies.db.servicejob;

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
import admin4.techelm.com.techelmtechnologies.db.UserDBUtil;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.UserLoginWrapper;

/**
 * Created by admin 4 on 21/02/2017.
 */

public class ServiceJobDBUtil extends DatabaseAccess {

    private static final String TAG = ServiceJobDBUtil.class.getSimpleName();

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "servicejob";
        public static final String COLUMN_NAME_SJ_ID = "id";
        public static final String COLUMN_NAME_SJ_SERVICE_NO = "service_no";
        public static final String COLUMN_NAME_SJ_CUSTOMER_ID = "customer_id";
        public static final String COLUMN_NAME_SJ_SERVICE_ID = "service_id";
        public static final String COLUMN_NAME_SJ_TYPEOFSERVICE = "type_of_service";
        public static final String COLUMN_NAME_SJ_COMPLAINT = "complaint";
        public static final String COLUMN_NAME_SJ_ENGINEER_ID = "engineer_id";
        public static final String COLUMN_NAME_SJ_LOCKED_TO = "locked_to";
        public static final String COLUMN_NAME_SJ_REMARKS = "remarks";
        public static final String COLUMN_NAME_SJ_REMARKS_BEFORE = "remarks_before";
        public static final String COLUMN_NAME_SJ_REMARKS_AFTER = "remarks_after";
        public static final String COLUMN_NAME_SJ_EQUIPMENT_TYPE = "equipment_type";
        public static final String COLUMN_NAME_SJ_SERIAL_NO = "serial_no";
        public static final String COLUMN_NAME_SJ_START_DATE = "start_date";
        public static final String COLUMN_NAME_SJ_END_DATE = "end_date";
        public static final String COLUMN_NAME_SJ_STATUS = "status";
        public static final String COLUMN_NAME_SJ_SIGNATURE_NAME = "signature_name";
        public static final String COLUMN_NAME_SJ_START_DATE_TASK = "start_date_task";
        public static final String COLUMN_NAME_SJ_END_DATE_TASK = "end_date_task";
        public static final String COLUMN_NAME_SJ_DATE_CREATED = "date_created";
        public static final String COLUMN_NAME_SJ_CUSTOMER_NAME = "customer_name";
        public static final String COLUMN_NAME_SJ_JOB_SITE = "job_site";

        public static final String COLUMN_NAME_SJ_FAX = "fax";
        public static final String COLUMN_NAME_SJ_TELEPHONE = "telephone";
        public static final String COLUMN_NAME_SJ_PHONENO = "phone_no";

        public static final String COLUMN_NAME_SJ_ENGINEER_NAME = "engineer_name";
        public static final String COLUMN_NAME_SJ_SIGNATURE_FILE_PATH = "signature_file_path";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewSJEntryAdded(String serviceNum);
        void onSJEntryUpdated(String remarks);
        void onSJEntryDeleted();
    }
    /**
     * Private constructor to avoid object creation from outside classes.
     * @param context
     */
    public ServiceJobDBUtil(Context context) {
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
    public ServiceJobDBUtil(Context context, String message) {
        super(context);
        Log.e(TAG, message);
    }

    public int addServiceJob(ServiceJobWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_NO, item.getServiceNumber());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_ID, item.getCustomerID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_ID, item.getServiceID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TYPEOFSERVICE, item.getTypeOfService());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_COMPLAINT, item.getComplaintsOrSymptoms());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ENGINEER_ID, item.getEngineer());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_LOCKED_TO, item.getLockedToUser());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS, item.getRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS_BEFORE, item.getBeforeRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS_AFTER, item.getAfterRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_EQUIPMENT_TYPE, item.getEquipmentType());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERIAL_NO, item.getModelOrSerial());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_START_DATE, item.getStartDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_END_DATE, item.getEndDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_STATUS, item.getStatus());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_NAME, item.getSignatureName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_START_DATE_TASK, item.getStartDateTask());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_END_DATE_TASK, item.getEndDateTask());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_DATE_CREATED, item.getDateCreated());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_NAME, item.getCustomerName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_JOB_SITE, item.getJobSite());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_FAX, item.getFax());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TELEPHONE, item.getTelephone());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_PHONENO, item.getPhone());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ENGINEER_NAME, item.getEngineerName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_FILE_PATH, item.getSignaturePath());

        if (db.insert(DBHelperItem.TABLE_NAME, null, cv) < 0) { // Update if Already existed on the SQLite DB
            int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                    DBHelperItem.COLUMN_NAME_SJ_ID + "=" + item.getID(), null);
            Log.e(TAG, "addServiceJob ROWS AFFECTED " + rowaffected);
        }

        Log.e(TAG, "addServiceJob INSERTED ID " + item.getID());
        Log.e(TAG, "addServiceJob SJ NUM " + item.getServiceNumber());
        Log.e(TAG, "addServiceJob SJ ID " + item.getServiceID());
        Log.e(TAG, "addServiceJob INSERTED " + getAllJSDetailsByServiceJobID(item.getID()).toString());
        return item.getID();
    }

    public void removeServiceJob(String serviceID) {
        SQLiteDatabase db = getDB();
        String[] whereArgs = { String.valueOf(serviceID) };
        db.delete(DBHelperItem.TABLE_NAME,
                DBHelperItem.COLUMN_NAME_SJ_SERVICE_ID + "=?", whereArgs);

        Log.e(TAG, "removeServiceJob " + serviceID);
    }

    public List<ServiceJobWrapper> getAllDetailsOfServiceJob() {
        ArrayList<ServiceJobWrapper> translationList = new ArrayList<>();
        int x = 0;
        do {
            ServiceJobWrapper alpha = new ServiceJobWrapper();
            alpha.setID(Integer.parseInt(x + ""));
            alpha.setStartDate("TestDate" + x);
            alpha.setServiceNumber("00" + x);
            alpha.setCustomerID("Customer" + x);
            alpha.setEngineerID("Engineer" + x);
            alpha.setStatus((x % 2 == 1) ? "Pending" : "Completed");
            translationList.add(alpha);
            x++;
        } while (x != 10);
        return translationList;
    }

    public List<ServiceJobWrapper> getAllRecordings() {
        ArrayList<ServiceJobWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobWrapper item = new ServiceJobWrapper();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setServiceNumber(cursor.getString(1));
                item.setCustomerID(cursor.getString(2));
                item.setServiceID(cursor.getString(3));
                item.setTypeOfService(cursor.getString(4));
                item.setEngineerID(cursor.getString(5));
                item.setLockedToUser(cursor.getString(6));
                item.setComplaintsOrSymptoms(cursor.getString(7));
                item.setRemarks(cursor.getString(8));
                item.setBeforeRemarks(cursor.getString(9));
                item.setAfterRemarks(cursor.getString(10));
                item.setEquipmentType(cursor.getString(11));
                item.setModelOrSerial(cursor.getString(12));
                item.setStartDate(cursor.getString(13));
                item.setEndDate(cursor.getString(14));
                item.setStatus(cursor.getString(15));
                item.setSignatureName(cursor.getString(16));
                item.setStartDateTask(cursor.getString(17));
                item.setEndDateTask(cursor.getString(18));
                item.setDateCreated(cursor.getString(19));
                item.setCustomerName(cursor.getString(20));
                item.setJobSite(cursor.getString(21));
                item.setFax(cursor.getString(22));
                item.setTelephone(cursor.getString(23));
                item.setPhone(cursor.getString(24));
                item.setEngineerName(cursor.getString(25));
                item.setSignaturePath(cursor.getString(26));

                list.add(item);
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

    private UserLoginWrapper getUserDetails(String engineerID, SQLiteDatabase db) {
        UserLoginWrapper user = new UserDBUtil(getContext()).getUserInfoByEngineerID(engineerID, db);
        Log.e(TAG, "getUserDetails: " + user.toString());
        return user;
    }

    public List<ServiceJobWrapper> getAllJSDetailsByID(int id) {
        ArrayList<ServiceJobWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME + " WHERE id="+id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobWrapper item = new ServiceJobWrapper();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setServiceNumber(cursor.getString(1));
                item.setCustomerID(cursor.getString(2));
                item.setServiceID(cursor.getString(3));
                item.setTypeOfService(cursor.getString(4));
                item.setEngineerID(cursor.getString(5));
                item.setLockedToUser(cursor.getString(6));
                item.setComplaintsOrSymptoms(cursor.getString(7));
                item.setRemarks(cursor.getString(8));
                item.setBeforeRemarks(cursor.getString(9));
                item.setAfterRemarks(cursor.getString(10));
                item.setEquipmentType(cursor.getString(11));
                item.setModelOrSerial(cursor.getString(12));
                item.setStartDate(cursor.getString(13));
                item.setEndDate(cursor.getString(14));
                item.setStatus(cursor.getString(15));
                item.setSignatureName(cursor.getString(16));
                item.setStartDateTask(cursor.getString(17));
                item.setEndDateTask(cursor.getString(18));
                item.setDateCreated(cursor.getString(19));
                item.setCustomerName(cursor.getString(20));
                item.setJobSite(cursor.getString(21));
                item.setFax(cursor.getString(22));
                item.setTelephone(cursor.getString(23));
                item.setPhone(cursor.getString(24));
                item.setEngineerName(cursor.getString(25));
                item.setSignaturePath(cursor.getString(26));

                list.add(item);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public ServiceJobWrapper getAllJSDetailsByServiceJobID(int serviceJobID) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME
                + " WHERE " + DBHelperItem.COLUMN_NAME_SJ_ID + "=" +serviceJobID;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        ServiceJobWrapper item = new ServiceJobWrapper();
        if (cursor.moveToFirst()) {
            item.setID(Integer.parseInt(cursor.getString(0)));
            item.setServiceNumber(cursor.getString(1));
            item.setCustomerID(cursor.getString(2));
            item.setServiceID(cursor.getString(3));
            item.setTypeOfService(cursor.getString(4));
            item.setEngineerID(cursor.getString(5));
            item.setLockedToUser(cursor.getString(6));
            item.setComplaintsOrSymptoms(cursor.getString(7));
            item.setRemarks(cursor.getString(8));
            item.setBeforeRemarks(cursor.getString(9));
            item.setAfterRemarks(cursor.getString(10));
            item.setEquipmentType(cursor.getString(11));
            item.setModelOrSerial(cursor.getString(12));
            item.setStartDate(cursor.getString(13));
            item.setEndDate(cursor.getString(14));
            item.setStatus(cursor.getString(15));
            item.setSignatureName(cursor.getString(16));
            item.setStartDateTask(cursor.getString(17));
            item.setEndDateTask(cursor.getString(18));
            item.setDateCreated(cursor.getString(19));
            item.setCustomerName(cursor.getString(20));
            item.setJobSite(cursor.getString(21));
            item.setFax(cursor.getString(22));
            item.setTelephone(cursor.getString(23));
            item.setPhone(cursor.getString(24));
            item.setEngineerName(cursor.getString(25));
            item.setSignaturePath(cursor.getString(26));
        }
        // Log.e(TAG, "getAllJSDetailsByServiceJobID: " + item.toString());

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return item;
    }

    /**
     * Update remarks on the Service Job FRGMT 1
     * @param id
     * @param remarks
     */
    public void updateRequestIDRemarks(int id, String remarks) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS, remarks);
        int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_SJ_ID + "=" + id, null);
        Log.e(TAG, "updateRequestIDRemarks ROWS AFFECTED " + rowaffected);
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onSJEntryUpdated(remarks);
        }
    }

    /**
     * Update remarks_before on the Service Job FRGMT 1_BEFORE
     * Can be run on thread
     * @param id - sjid
     * @param remarks - before remarks
     */
    public void updateRequestIDRemarks_BEFORE(int id, final String remarks) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS_BEFORE, remarks);
        int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_SJ_ID + "=" + id, null);
        Log.e(TAG, "updateRequestIDRemarks_BEFORE ROWS AFFECTED " + rowaffected);
        if (mOnDatabaseChangedListener != null) {
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mOnDatabaseChangedListener.onSJEntryUpdated(remarks);
                }
            });
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
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS_AFTER, remarks);
        int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_SJ_ID + "=" + id, null);
        Log.e(TAG, "updateRequestIDRemarks_AFTER ROWS AFFECTED " + rowaffected);
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onSJEntryUpdated(remarks);
        }
    }

    public void updateRequestIDSignature(int requestID, String signatureName, String signatureFilePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_NAME, signatureName);
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_FILE_PATH, signatureFilePath);
        int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_SJ_ID + "=" + requestID, null);
        Log.e(TAG, "updateRequestIDSignature ROWS AFFECTED " + rowaffected);

        if (mOnDatabaseChangedListener != null) {
            // mOnDatabaseChangedListener.onIPI_DEntryUpdated(item.getServiceNumber());
        }
    }

    public ServiceJobWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        ServiceJobWrapper item = new ServiceJobWrapper();
        if (cursor.moveToPosition(position)) {
            item.setID(Integer.parseInt(cursor.getString(0)));
            item.setServiceNumber(cursor.getString(1));
            item.setCustomerID(cursor.getString(2));
            item.setServiceID(cursor.getString(3));
            item.setTypeOfService(cursor.getString(4));
            item.setEngineerID(cursor.getString(5));
            item.setLockedToUser(cursor.getString(6));
            item.setComplaintsOrSymptoms(cursor.getString(7));
            item.setRemarks(cursor.getString(8));
            item.setBeforeRemarks(cursor.getString(9));
            item.setAfterRemarks(cursor.getString(10));
            item.setEquipmentType(cursor.getString(11));
            item.setModelOrSerial(cursor.getString(12));
            item.setStartDate(cursor.getString(13));
            item.setEndDate(cursor.getString(14));
            item.setStatus(cursor.getString(15));
            item.setSignatureName(cursor.getString(16));
            item.setStartDateTask(cursor.getString(17));
            item.setEndDateTask(cursor.getString(18));
            item.setDateCreated(cursor.getString(19));
            item.setCustomerName(cursor.getString(20));
            item.setJobSite(cursor.getString(21));
            item.setFax(cursor.getString(22));
            item.setTelephone(cursor.getString(23));
            item.setPhone(cursor.getString(24));
            item.setEngineerName(cursor.getString(25));
            item.setSignaturePath(cursor.getString(26));
        } /*else {
            item = null;
        }*/

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return item;
    }

    public void removeItemWithId(int id) {
        SQLiteDatabase db = getDB();
        String[] whereArgs = { String.valueOf(id) };
        db.delete(DBHelperItem.TABLE_NAME,
                DBHelperItem.COLUMN_NAME_SJ_ID + "=?", whereArgs);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onSJEntryDeleted();
        }
        Log.e(TAG, "addRecording " + id);
    }

    public int getCount() {
        SQLiteDatabase db = getDB();
        String[] projection = { DBHelperItem.COLUMN_NAME_SJ_ID };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int addRecording(ServiceJobWrapper item) {
        SQLiteDatabase db = getDB();

        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_NO, item.getServiceNumber());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_ID, item.getCustomerID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_ID, item.getServiceID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TYPEOFSERVICE, item.getTypeOfService());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_COMPLAINT, item.getComplaintsOrSymptoms());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ENGINEER_ID, item.getEngineer());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_LOCKED_TO, item.getLockedToUser());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS, item.getRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS_BEFORE, item.getBeforeRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS_AFTER, item.getAfterRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_EQUIPMENT_TYPE, item.getEquipmentType());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERIAL_NO, item.getModelOrSerial());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_START_DATE, item.getStartDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_END_DATE, item.getEndDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_STATUS, item.getStatus());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_NAME, item.getSignatureName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_START_DATE_TASK, item.getStartDateTask());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_END_DATE_TASK, item.getEndDateTask());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_DATE_CREATED, item.getDateCreated());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_NAME, item.getCustomerName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_JOB_SITE, item.getJobSite());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_FAX, item.getFax());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TELEPHONE, item.getTelephone());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_PHONENO, item.getPhone());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ENGINEER_NAME, item.getEngineerName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_FILE_PATH, item.getSignaturePath());
        long idInserted = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        int rowId = (int)idInserted;
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewSJEntryAdded(item.getServiceNumber());
        }
        return rowId;
    }

    public void renameItem(ServiceJobWrapper item, String serviceNum, String startDate) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_NO, serviceNum);
        cv.put(DBHelperItem.COLUMN_NAME_SJ_START_DATE, startDate);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_SJ_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onSJEntryUpdated(item.getServiceNumber());
        }
    }

    public long restoreRecording(ServiceJobWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_NO, item.getServiceNumber());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_ID, item.getCustomerID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_ID, item.getServiceID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TYPEOFSERVICE, item.getTypeOfService());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_COMPLAINT, item.getComplaintsOrSymptoms());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ENGINEER_ID, item.getEngineer());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_LOCKED_TO, item.getLockedToUser());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS, item.getRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS_BEFORE, item.getBeforeRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS_AFTER, item.getAfterRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_EQUIPMENT_TYPE, item.getEquipmentType());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERIAL_NO, item.getModelOrSerial());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_START_DATE, item.getStartDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_END_DATE, item.getEndDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_STATUS, item.getStatus());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_NAME, item.getSignatureName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_START_DATE_TASK, item.getStartDateTask());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_END_DATE_TASK, item.getEndDateTask());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_DATE_CREATED, item.getDateCreated());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_NAME, item.getCustomerName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_JOB_SITE, item.getJobSite());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_FAX, item.getFax());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TELEPHONE, item.getTelephone());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_PHONENO, item.getPhone());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ENGINEER_NAME, item.getEngineerName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_FILE_PATH, item.getSignaturePath());
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewSJEntryAdded(item.getServiceNumber());
        }
        return rowId;
    }

    /**
     * Test if ServiceJob has Remarks
     *  This is called before submitting the Files to the web
     * @param id
     * @return
     */
    public boolean hasRemarks(int id) {
        String selectQuery = "SELECT remarks FROM " + DBHelperItem.TABLE_NAME
                + " WHERE " + DBHelperItem.COLUMN_NAME_SJ_ID + "=" + id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        boolean result;
        if (cursor.moveToFirst()) {
            String item = cursor.getString(0);
            if ("".equals(item) || item == null) {
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

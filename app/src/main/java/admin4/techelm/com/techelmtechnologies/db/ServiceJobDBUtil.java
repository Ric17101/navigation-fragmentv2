package admin4.techelm.com.techelmtechnologies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.UserLoginWrapper;

/**
 * Created by admin 4 on 21/02/2017.
 */

public class ServiceJobDBUtil extends DatabaseAccess {

    private static final String LOG_TAG = "ServiceJobDBUtil";

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "servicejob";
        public static final String COLUMN_NAME_SJ_ID = "id";
        public static final String COLUMN_NAME_SJ_SERVICE_NO = "service_no";
        public static final String COLUMN_NAME_SJ_CUSTOMER_ID = "customer_id";
        public static final String COLUMN_NAME_SJ_SERVICE_ID = "service_id";
        public static final String COLUMN_NAME_SJ_ENGINEER_ID = "engineer_id";
        public static final String COLUMN_NAME_SJ_PRICE_ID = "price_id";
        public static final String COLUMN_NAME_SJ_COMPLAINT = "complaint";
        public static final String COLUMN_NAME_SJ_REMARKS = "remarks";
        public static final String COLUMN_NAME_SJ_EQUIPMENT_TYPE = "equipment_type";
        public static final String COLUMN_NAME_SJ_SERIAL_NO = "serial_no";
        public static final String COLUMN_NAME_SJ_START_DATE = "start_date";
        public static final String COLUMN_NAME_SJ_END_DATE = "end_date";
        public static final String COLUMN_NAME_SJ_STATUS = "status";

        public static final String COLUMN_NAME_SJ_CONTRACT_SERVICING = "contract_servicing";
        public static final String COLUMN_NAME_SJ_WARRANTY_SERVICING = "warranty_servicing";
        public static final String COLUMN_NAME_SJ_CHARGES = "charges";
        public static final String COLUMN_NAME_SJ_CONTRACT_REPAIR = "contract_repair";
        public static final String COLUMN_NAME_SJ_WARRANTY_REPAIR = "warranty_repair";
        public static final String COLUMN_NAME_SJ_OTHERS = "others";
        public static final String COLUMN_NAME_SJ_SIGNATURE_NAME = "signature_name";
        public static final String COLUMN_NAME_SJ_CUSTOMER_NAME = "fullname";
        public static final String COLUMN_NAME_SJ_JOB_SITE = "job_site";

        public static final String COLUMN_NAME_SJ_TELEPHONE = "telephone";
        public static final String COLUMN_NAME_SJ_FAX = "fax";
        public static final String COLUMN_NAME_SJ_RACE = "race";
        public static final String COLUMN_NAME_SJ_TYPEOFSERVICE = "type_of_service";
        public static final String COLUMN_NAME_SJ_SIGNATURE_FILE_PATH = "signature_file_path";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewSJEntryAdded(String serviceNum);
        void onSJEntryRenamed(String fileName);
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
    public ServiceJobDBUtil(Context context, String message) {
        super(context);
        Log.e(LOG_TAG, message);
    }

    public int addServiceJob(ServiceJobWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_NO, item.getServiceNumber());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_ID, item.getCustomerID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_ID, item.getServiceID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ENGINEER_ID, item.getEngineer());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_PRICE_ID, item.getPriceID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_COMPLAINT, item.getComplaintsOrSymptoms());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS, item.getActionsOrRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_EQUIPMENT_TYPE, item.getEquipmentType());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERIAL_NO, item.getModelOrSerial());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_START_DATE, item.getStartDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_END_DATE, item.getEndDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_STATUS, item.getStatus());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CONTRACT_SERVICING, item.getContractServicing());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_WARRANTY_SERVICING, item.getWarrantyServicing());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CHARGES, item.getCharges());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CONTRACT_REPAIR, item.getContractRepair());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_WARRANTY_REPAIR, item.getWarrantyRepair());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_OTHERS, item.getOthers());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_NAME, item.getCustomerName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_JOB_SITE, item.getJobSite());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TELEPHONE, item.getTelephone());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_FAX, item.getFax());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TYPEOFSERVICE, item.getTypeOfService());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_NAME, item.getSignatureName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_FILE_PATH, item.getSignaturePath());
        // cv.put(DBHelperItem.COLUMN_NAME_UPLOADS_ID, length);

        if (db.insert(DBHelperItem.TABLE_NAME, null, cv) < 0) { // Update if Already existed on the SQLite DB
            int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                    DBHelperItem.COLUMN_NAME_SJ_ID + "=" + item.getID(), null);
            Log.e(LOG_TAG, "addServiceJob ROWS AFFECTED " + rowaffected);
        }

        Log.e(LOG_TAG, "addServiceJob INSERTED ID " + item.getID());
        Log.e(LOG_TAG, "addServiceJob SJ NUM " + item.getServiceNumber());
        Log.e(LOG_TAG, "addServiceJob SJ ID " + item.getServiceID());
        return item.getID();
    }

    public void removeServiceJob(String serviceID) {
        SQLiteDatabase db = getDB();
        String[] whereArgs = { String.valueOf(serviceID) };
        db.delete(DBHelperItem.TABLE_NAME,
                DBHelperItem.COLUMN_NAME_SJ_SERVICE_ID + "=?", whereArgs);

        Log.e(LOG_TAG, "removeServiceJob " + serviceID);
    }

    public List<ServiceJobWrapper> getAllDetailsOfServiceJob() {
        ArrayList<ServiceJobWrapper> translationList = new ArrayList<>();
        int x = 0;
        do {
            ServiceJobWrapper alpha = new ServiceJobWrapper();
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
    }

    public List<ServiceJobWrapper> getAllRecordings() {
        ArrayList<ServiceJobWrapper> list = new ArrayList<ServiceJobWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobWrapper recordItem = new ServiceJobWrapper();
                recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceNumber(cursor.getString(1));
                recordItem.setCustomerID(cursor.getString(2));
                recordItem.setServiceID(cursor.getString(3));
                recordItem.setEngineerID(cursor.getString(4));
                recordItem.setPriceID(cursor.getString(5));
                recordItem.setComplaintsOrSymptoms(cursor.getString(6));
                recordItem.setActionsOrRemarks(cursor.getString(7));
                recordItem.setEquipmentType(cursor.getString(8));
                recordItem.setModelOrSerial(cursor.getString(9));
                recordItem.setStartDate(cursor.getString(10));
                recordItem.setEndDate(cursor.getString(11));
                recordItem.setStatus(cursor.getString(12));
                recordItem.setContractServicing(cursor.getString(13));
                recordItem.setWarrantyServicing(cursor.getString(14));
                recordItem.setCharges(cursor.getString(15));
                recordItem.setContractRepair(cursor.getString(16));
                recordItem.setWarrantyRepair(cursor.getString(17));
                recordItem.setOthers(cursor.getString(18));
                recordItem.setSignatureName(cursor.getString(19));
                recordItem.setTelephone(cursor.getString(20));
                recordItem.setFax(cursor.getString(21));
                recordItem.setTypeOfService(cursor.getString(22));
                recordItem.setSignaturePath(cursor.getString(23));
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

    private UserLoginWrapper getUserDetails(String engineerID, SQLiteDatabase db) {
        UserLoginWrapper user = new UserDBUtil(getContext()).getUserInfoByEngineerID(engineerID, db);
        Log.e(LOG_TAG, "getUserDetails: " + user.toString());
        return user;
    }

    public List<ServiceJobWrapper> getAllJSDetailsByID(int id) {
        ArrayList<ServiceJobWrapper> list = new ArrayList<ServiceJobWrapper>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME + " WHERE id="+id;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobWrapper recordItem = new ServiceJobWrapper();
                recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceNumber(cursor.getString(1));
                recordItem.setCustomerID(cursor.getString(2));
                recordItem.setServiceID(cursor.getString(3));
                recordItem.setEngineerID(cursor.getString(4));
                recordItem.setPriceID(cursor.getString(5));
                recordItem.setComplaintsOrSymptoms(cursor.getString(6));
                recordItem.setActionsOrRemarks(cursor.getString(7));
                recordItem.setEquipmentType(cursor.getString(8));
                recordItem.setModelOrSerial(cursor.getString(9));
                recordItem.setStartDate(cursor.getString(10));
                recordItem.setEndDate(cursor.getString(11));
                recordItem.setStatus(cursor.getString(12));
                recordItem.setContractServicing(cursor.getString(13));
                recordItem.setWarrantyServicing(cursor.getString(14));
                recordItem.setCharges(cursor.getString(15));
                recordItem.setContractRepair(cursor.getString(16));
                recordItem.setWarrantyRepair(cursor.getString(17));
                recordItem.setOthers(cursor.getString(18));
                recordItem.setSignatureName(cursor.getString(19));
                recordItem.setSignaturePath(cursor.getString(20));
                recordItem.setTelephone(cursor.getString(21));
                recordItem.setFax(cursor.getString(22));
                recordItem.setTypeOfService(cursor.getString(23));
                recordItem.setJobSite(cursor.getString(24));
                recordItem.setCustomerName(cursor.getString(25));

                /*recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceNumber(cursor.getString(1));
                recordItem.setCustomerID(getCustomerNameByID(cursor.getString(2)));
                recordItem.setServiceID(cursor.getString(3));

                String engineerID = cursor.getString(4);
                recordItem.setEngineerID(engineerID);

                recordItem.setPriceID(cursor.getString(5));
                recordItem.setComplaintsOrSymptoms(cursor.getString(6));
                recordItem.setActionsOrRemarks(cursor.getString(7));
                recordItem.setEquipmentType(cursor.getString(8));
                recordItem.setModelOrSerial(cursor.getString(9));
                recordItem.setStartDate(cursor.getString(10));
                recordItem.setEndDate(cursor.getString(11));
                recordItem.setStatus(cursor.getString(12));
                recordItem.setContractServicing(cursor.getString(13));
                recordItem.setWarrantyServicing(cursor.getString(14));
                recordItem.setCharges(cursor.getString(15));
                recordItem.setContractRepair(cursor.getString(16));
                recordItem.setWarrantyRepair(cursor.getString(17));
                recordItem.setOthers(cursor.getString(18));
                recordItem.setSignatureName(cursor.getString(19));

                SQLiteDatabase db = getDB();
                UserLoginWrapper user = getUserDetails(engineerID, db);
                recordItem.setTelephone(user.getPhoneNo());
                recordItem.setFax(user.getFax());
                recordItem.setRace(user.getRace());*/

                list.add(recordItem);
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

        ServiceJobWrapper recordItem = new ServiceJobWrapper();
        if (cursor.moveToFirst()) {
                recordItem.setID(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceNumber(cursor.getString(1));
                recordItem.setCustomerID(cursor.getString(2));
                recordItem.setServiceID(cursor.getString(3));
                recordItem.setEngineerID(cursor.getString(4));
                recordItem.setPriceID(cursor.getString(5));
                recordItem.setComplaintsOrSymptoms(cursor.getString(6));
                recordItem.setActionsOrRemarks(cursor.getString(7));
                recordItem.setEquipmentType(cursor.getString(8));
                recordItem.setModelOrSerial(cursor.getString(9));
                recordItem.setStartDate(cursor.getString(10));
                recordItem.setEndDate(cursor.getString(11));
                recordItem.setStatus(cursor.getString(12));
                recordItem.setContractServicing(cursor.getString(13));
                recordItem.setWarrantyServicing(cursor.getString(14));
                recordItem.setCharges(cursor.getString(15));
                recordItem.setContractRepair(cursor.getString(16));
                recordItem.setWarrantyRepair(cursor.getString(17));
                recordItem.setOthers(cursor.getString(18));
                recordItem.setSignatureName(cursor.getString(19));
                recordItem.setSignaturePath(cursor.getString(20));
                recordItem.setTelephone(cursor.getString(21));
                recordItem.setFax(cursor.getString(22));
                recordItem.setTypeOfService(cursor.getString(23));
                recordItem.setJobSite(cursor.getString(24));
                recordItem.setCustomerName(cursor.getString(25));
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
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS, remarks);
        int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_SJ_ID + "=" + id, null);
        Log.e(LOG_TAG, "updateRequestIDRemarks ROWS AFFECTED " + rowaffected);
        if (mOnDatabaseChangedListener != null) {
            // mOnDatabaseChangedListener.onSJEntryRenamed(item.getServiceNumber());
        }
    }

    public void updateRequestIDSignature(int requestID, String signatureName, String signatureFilePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_NAME, signatureName);
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_FILE_PATH, signatureFilePath);
        int rowaffected = db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_SJ_ID + "=" + requestID, null);
        Log.e(LOG_TAG, "updateRequestIDSignature ROWS AFFECTED " + rowaffected);

        if (mOnDatabaseChangedListener != null) {
            // mOnDatabaseChangedListener.onSJEntryRenamed(item.getServiceNumber());
        }
    }

    public ServiceJobWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        ServiceJobWrapper recordItem = new ServiceJobWrapper();
        if (cursor.moveToPosition(position)) {
            recordItem.setID(Integer.parseInt(cursor.getString(0)));
            recordItem.setServiceNumber(cursor.getString(1));
            recordItem.setCustomerID(cursor.getString(2));
            recordItem.setServiceID(cursor.getString(3));
            recordItem.setEngineerID(cursor.getString(4));
            recordItem.setPriceID(cursor.getString(5));
            recordItem.setComplaintsOrSymptoms(cursor.getString(6));
            recordItem.setActionsOrRemarks(cursor.getString(7));
            recordItem.setEquipmentType(cursor.getString(8));
            recordItem.setModelOrSerial(cursor.getString(9));
            recordItem.setStartDate(cursor.getString(10));
            recordItem.setEndDate(cursor.getString(11));
            recordItem.setStatus(cursor.getString(12));
            recordItem.setContractServicing(cursor.getString(13));
            recordItem.setWarrantyServicing(cursor.getString(14));
            recordItem.setCharges(cursor.getString(15));
            recordItem.setContractRepair(cursor.getString(16));
            recordItem.setWarrantyRepair(cursor.getString(17));
            recordItem.setOthers(cursor.getString(18));
            recordItem.setSignatureName(cursor.getString(19));
            recordItem.setSignaturePath(cursor.getString(20));
            recordItem.setTelephone(cursor.getString(21));
            recordItem.setFax(cursor.getString(22));
            recordItem.setTypeOfService(cursor.getString(23));
            recordItem.setJobSite(cursor.getString(24));
            recordItem.setCustomerName(cursor.getString(25));
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
                DBHelperItem.COLUMN_NAME_SJ_ID + "=?", whereArgs);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onSJEntryDeleted();
        }
        Log.e(LOG_TAG, "addRecording " + id);
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
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ENGINEER_ID, item.getEngineer());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_PRICE_ID, item.getPriceID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_COMPLAINT, item.getComplaintsOrSymptoms());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS, item.getActionsOrRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_EQUIPMENT_TYPE, item.getEquipmentType());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERIAL_NO, item.getModelOrSerial());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_START_DATE, item.getStartDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_END_DATE, item.getEndDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_STATUS, item.getStatus());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CONTRACT_SERVICING, item.getContractServicing());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_WARRANTY_SERVICING, item.getWarrantyServicing());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CHARGES, item.getCharges());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CONTRACT_REPAIR, item.getContractRepair());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_WARRANTY_REPAIR, item.getWarrantyRepair());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_OTHERS, item.getOthers());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_NAME, item.getSignatureName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_NAME, item.getCustomerName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_JOB_SITE, item.getJobSite());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TELEPHONE, item.getTelephone());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_FAX, item.getFax());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TYPEOFSERVICE, item.getTypeOfService());
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
            mOnDatabaseChangedListener.onSJEntryRenamed(item.getServiceNumber());
        }
    }

    public long restoreRecording(ServiceJobWrapper item) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ID, item.getID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_NO, item.getServiceNumber());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_ID, item.getCustomerID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERVICE_ID, item.getServiceID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_ENGINEER_ID, item.getEngineer());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_PRICE_ID, item.getPriceID());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_COMPLAINT, item.getComplaintsOrSymptoms());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_REMARKS, item.getActionsOrRemarks());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_EQUIPMENT_TYPE, item.getEquipmentType());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SERIAL_NO, item.getModelOrSerial());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_START_DATE, item.getStartDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_END_DATE, item.getEndDate());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_STATUS, item.getStatus());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CONTRACT_SERVICING, item.getContractServicing());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_WARRANTY_SERVICING, item.getWarrantyServicing());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CHARGES, item.getCharges());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CONTRACT_REPAIR, item.getContractRepair());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_WARRANTY_REPAIR, item.getWarrantyRepair());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_OTHERS, item.getOthers());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_NAME, item.getSignatureName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_CUSTOMER_NAME, item.getCustomerName());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_JOB_SITE, item.getJobSite());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TELEPHONE, item.getTelephone());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_FAX, item.getFax());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_TYPEOFSERVICE, item.getTypeOfService());
        cv.put(DBHelperItem.COLUMN_NAME_SJ_SIGNATURE_FILE_PATH, item.getSignaturePath());
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewSJEntryAdded(item.getServiceNumber());
        }
        return rowId;
    }

}

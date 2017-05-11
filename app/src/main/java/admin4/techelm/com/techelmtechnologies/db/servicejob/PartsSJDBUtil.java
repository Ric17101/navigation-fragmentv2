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
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewPartsWrapper;

/**
 * Created by admin 4 on 13/03/2017.
 * File Replacements
 */

public class PartsSJDBUtil extends DatabaseAccess {

    private static final String LOG_TAG = "PartsSJDBUtil";

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

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "servicejob_parts";
        public static final String COLUMN_NAME_PARTS_ID = "id";
        public static final String COLUMN_NAME_PARTS_SERVICE_ID = "servicejob_id";
        public static final String COLUMN_NAME_PARTS_NAME = "parts_name";
        public static final String COLUMN_NAME_PARTS_QUANTITY = "quantity";
        public static final String COLUMN_NAME_PARTS_UNIT_PRICE = "unit_price";
        public static final String COLUMN_NAME_PARTS_TOTAL_PRICE = "total_price";
    }

    private static OnDatabaseChangedListener mOnDatabaseChangedListener;

    /**
     * Listen for add/rename items in database
     * Can be called outside the class as OnDatabaseChangedListener.java
     */
    public interface OnDatabaseChangedListener {
        void onNewPartsEntryAdded(String partName);
        void onPartsEntryRenamed(String partName);
        void onPartsEntryDeleted();
    }
    /**
     * Private constructor to avoid object creation from outside classes.
     * @param context
     */
    public PartsSJDBUtil(Context context) {
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
    public PartsSJDBUtil(Context context, String message) {
        super(context);
        Log.e(LOG_TAG, message);
    }

    public List<ServiceJobNewPartsWrapper> getAllParts() {
        ArrayList<ServiceJobNewPartsWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobNewPartsWrapper recordItem = new ServiceJobNewPartsWrapper();
                recordItem.setId(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceId(Integer.parseInt(cursor.getString(1)));
                recordItem.setReplacementPartName(cursor.getString(2));
                recordItem.setQuantity(cursor.getString(3));
                recordItem.setUnitPrice(cursor.getString(4));
                recordItem.setTotalPrice(cursor.getString(5));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public List<ServiceJobNewPartsWrapper> getAllPartsBySJID(int serviceJobID) {
        ArrayList<ServiceJobNewPartsWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME + " WHERE servicejob_id="+serviceJobID;
        Cursor cursor = getDB().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ServiceJobNewPartsWrapper recordItem = new ServiceJobNewPartsWrapper();
                recordItem.setId(Integer.parseInt(cursor.getString(0)));
                recordItem.setServiceId(Integer.parseInt(cursor.getString(1)));
                recordItem.setReplacementPartName(cursor.getString(2));
                recordItem.setQuantity(cursor.getString(3));
                recordItem.setUnitPrice(cursor.getString(4));
                recordItem.setTotalPrice(cursor.getString(5));
                list.add(recordItem);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }

    public Double getTotalPriceOfNewPartsBySJID(int serviceJobID) {
        String selectQuery = "SELECT " + DBHelperItem.COLUMN_NAME_PARTS_TOTAL_PRICE + " FROM " + DBHelperItem.TABLE_NAME + " WHERE servicejob_id=" +serviceJobID;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        Double totalPrice = 0.00;
        if (cursor.moveToFirst()) {
            do {
            totalPrice += Double.parseDouble(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return totalPrice;
    }

    public ServiceJobNewPartsWrapper getItemAt(int position) {
        String selectQuery = "SELECT * FROM " + DBHelperItem.TABLE_NAME;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        ServiceJobNewPartsWrapper recordItem = new ServiceJobNewPartsWrapper();
        if (cursor.moveToPosition(position)) {
            recordItem.setId(Integer.parseInt(cursor.getString(0)));
            recordItem.setServiceId(Integer.parseInt(cursor.getString(1)));
            recordItem.setReplacementPartName(cursor.getString(2));
            recordItem.setQuantity(cursor.getString(3));
            recordItem.setUnitPrice(cursor.getString(4));
            recordItem.setTotalPrice(cursor.getString(5));
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
            mOnDatabaseChangedListener.onPartsEntryDeleted();
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

    // public int addNewPart(String uploadName, String filePath, int serviceId) {
    public int addNewPart(ServiceJobNewPartsWrapper item, String fragmentName) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_SERVICE_ID, item.getServiceJobId());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_NAME, item.getPartName());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_QUANTITY, item.getQuantity());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_UNIT_PRICE, item.getUnitPrice());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_TOTAL_PRICE, item.getTotalPrice());
        // cv.put(DBHelperItem.COLUMN_NAME_PARTS_ID, length);
        long idInserted = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        int rowId = (int)idInserted;
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewPartsEntryAdded(item.getPartName());
        }
        return rowId;
    }

    public void editPart(ServiceJobNewPartsWrapper item, String fragmentName) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_NAME, item.getPartName());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_QUANTITY, item.getQuantity());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_UNIT_PRICE, item.getUnitPrice());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_TOTAL_PRICE, item.getTotalPrice());
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_PARTS_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewPartsEntryAdded(item.getPartName());
        }
    }

    // Not used, to be configured
    public void renameItem(ServiceJobNewPartsWrapper item, String recordingName, String filePath) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_QUANTITY, filePath);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem.COLUMN_NAME_PARTS_ID + "=" + item.getID(), null);

        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onPartsEntryRenamed(item.getPartName());
        }
    }

    public long restoreNewParts(ServiceJobNewPartsWrapper item, String fragmentName) {
        SQLiteDatabase db = getDB();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_NAME, item.getPartName());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_QUANTITY, item.getQuantity());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_SERVICE_ID, item.getServiceJobId());
        cv.put(DBHelperItem.COLUMN_NAME_PARTS_ID, item.getID());
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        if (mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onNewPartsEntryAdded(item.getPartName());
        }
        return rowId;
    }

}

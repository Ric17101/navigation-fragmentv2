package admin4.techelm.com.techelmtechnologies.db;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceWrapper;
import admin4.techelm.com.techelmtechnologies.model.WorkCalendarWrapper;

/**
 * Created by admin 4 on 27/02/2017.
 */

public class Calendar_ServiceJob_DBUtil extends DatabaseAccess {

    private static final String TABLE_WORK = "work";
    private static final String TABLE_SERVICE = "service";

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Calendar_ServiceJob_DBUtil(Context context) {
        super(context);
    }

    /**************************
     * WORK Details Query
     **************************/
    public List<WorkCalendarWrapper> getAllWorks() {
        ArrayList<WorkCalendarWrapper> userList = new ArrayList<WorkCalendarWrapper>();
        String selectQuery = "SELECT * FROM " + TABLE_WORK;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        WorkCalendarWrapper user = new WorkCalendarWrapper();

        if (cursor.moveToFirst()) {
            do {
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.settServiceNo(cursor.getString(1));
                user.setCustomerID(cursor.getInt(2));
                user.setServiceID(cursor.getInt(3));
                user.setCarCode(cursor.getString(4));
                user.setEngineerID(cursor.getInt(5));
                user.setStartDate(cursor.getString(6));
                user.setEndDate(cursor.getString(7));
                user.setStatus(cursor.getInt(8));
                userList.add(user);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return userList;
    }



    /**************************
     * SERVICE Details Query
     **************************/
    public List<ServiceWrapper> getAllServices() {
        ArrayList<ServiceWrapper> userList = new ArrayList<ServiceWrapper>();
        String selectQuery = "SELECT * FROM " + TABLE_SERVICE;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        ServiceWrapper user = new ServiceWrapper();

        if (cursor.moveToFirst()) {
            do {
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setCategoryID(cursor.getInt(1));
                user.setServiceNanem(cursor.getString(2));
                user.setDescription(cursor.getString(3));
                user.setDefaultUnitPrice(cursor.getString(4));
                user.setStatus(cursor.getInt(5));
                user.setCreatedAt(cursor.getString(6));
                user.setCreatedBy(cursor.getInt(7));
                user.setUpdatedAt(cursor.getString(8));
                user.setUpdatedBy(cursor.getInt(9));
                userList.add(user);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return userList;
    }


    /***********************************
     * TEST
     ***********************************/

    // Getting Details Data of all ALPHABET
    public List<ServiceJobWrapper> getAllDetailsOfServiceJob() {
        ArrayList<ServiceJobWrapper> translationList = new ArrayList<>();
        int x = 0;
        do {
            ServiceJobWrapper alpha = new ServiceJobWrapper();
            alpha.setID(Integer.parseInt(x + ""));
            alpha.setDay(x + "");
            alpha.setDate("TestDate" + x);
            alpha.setServiceNumber("00" + x);
            alpha.setCustomer("Customer" + x);
            alpha.setEngineer("Engineer" + x);
            alpha.setStatus((x % 2 == 1) ? "Pending" : "Completed");
            translationList.add(alpha);
            x++;
        } while (x != 10);
        return translationList;
    }
}

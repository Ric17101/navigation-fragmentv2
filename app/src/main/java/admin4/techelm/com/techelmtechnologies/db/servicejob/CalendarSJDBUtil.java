package admin4.techelm.com.techelmtechnologies.db.servicejob;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.db.DatabaseAccess;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceWrapper;
import admin4.techelm.com.techelmtechnologies.model.WorkCalendarWrapper;

/**
 * Created by admin 4 on 27/02/2017.
 */

public class CalendarSJDBUtil extends DatabaseAccess {

    private static final String TABLE_WORK = "work";
    private static final String TABLE_SERVICE = "service";

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public CalendarSJDBUtil(Context context) {
        super(context);
    }

    /**************************
     * WORK Details Query     *
     **************************/
    // TODO: make clean up code
    public List<WorkCalendarWrapper> getAllWorks() {
        ArrayList<WorkCalendarWrapper> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_WORK;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        WorkCalendarWrapper item = new WorkCalendarWrapper();

        if (cursor.moveToFirst()) {
            do {
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.settServiceNo(cursor.getString(1));
                item.setCustomerID(cursor.getInt(2));
                item.setServiceID(cursor.getInt(3));
                item.setCarCode(cursor.getString(4));
                item.setEngineerID(cursor.getInt(5));
                item.setStartDate(cursor.getString(6));
                item.setEndDate(cursor.getString(7));
                item.setStatus(cursor.getInt(8));
                list.add(item);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return list;
    }



    /**************************
     * SERVICE Details Query  *
     **************************/
    public List<ServiceWrapper> getAllServices() {
        ArrayList<ServiceWrapper> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SERVICE;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        ServiceWrapper item = new ServiceWrapper();

        if (cursor.moveToFirst()) {
            do {
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setCategoryID(cursor.getInt(1));
                item.setServiceNanem(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setDefaultUnitPrice(cursor.getString(4));
                item.setStatus(cursor.getInt(5));
                item.setCreatedAt(cursor.getString(6));
                item.setCreatedBy(cursor.getInt(7));
                item.setUpdatedAt(cursor.getString(8));
                item.setUpdatedBy(cursor.getInt(9));
                userList.add(item);
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

    // Getting Details Data of all ServiceJob
    public List<ServiceJobWrapper> getAllDetailsOfServiceJob() {
        ArrayList<ServiceJobWrapper> translationList = new ArrayList<>();
        int x = 0;
        do {
            ServiceJobWrapper item = new ServiceJobWrapper();
            item.setID(Integer.parseInt(x + ""));
            item.setEndDate(x + "");
            item.setStartDate("TestDate" + x);
            item.setServiceNumber("00" + x);
            item.setCustomerID("Customer" + x);
            item.setEngineerID("Engineer" + x);
            item.setStatus((x % 2 == 1) ? "Pending" : "Completed");
            translationList.add(item);
            x++;
        } while (x != 10);
        return translationList;
    }
}

package admin4.techelm.com.techelmtechnologies.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.UserGroupWrapper;
import admin4.techelm.com.techelmtechnologies.model.UserLoginWrapper;

/**
 * Created by admin 4 on 21/02/2017.
 */

public class UserDBUtil extends DatabaseAccess {

    private static final String TABLE_USER = "user";
    private static final String TABLE_USER_GROUP = "user_group";

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public UserDBUtil(Context context) {
        super(context);
    }

    /**************************
     * USER Credentials Query
     **************************/
    public List<String> getUsers() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getDB().rawQuery("SELECT id FROM " + TABLE_USER, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**
     * Get the list of Credentials of the user from user TABLE
     * Used to check if user who logging in exist and to login
     * @return list - String in format [email]:[password]:[user_group_id]
     */
    public List<String> getUserCredentials() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getDB().rawQuery("SELECT username, password, user_group_id FROM " + TABLE_USER, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0) + ":" + cursor.getString(1) + ":" + cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<UserLoginWrapper> getAllUser() {
        ArrayList<UserLoginWrapper> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        UserLoginWrapper user = new UserLoginWrapper();

        if (cursor.moveToFirst()) {
            do {
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setUserGroupId(cursor.getString(1));
                user.setRole(cursor.getString(2));
                user.setFullname(cursor.getString(3));
                user.setUsername(cursor.getString(4));
                user.setPassword(cursor.getString(5));
                user.setAuthKey(cursor.getString(6));
                user.setPasswordHash(cursor.getString(7));
                user.setPasswordResetToken(cursor.getString(8));
                user.setPhoto(cursor.getString(9));
                user.setStatus(Integer.parseInt(cursor.getString(10)));
                user.setDeleted(Integer.parseInt(cursor.getString(11)));
                userList.add(user);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return userList;
    }

    public UserLoginWrapper getUserInfoByEngineerID(String engineer_id, SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM user WHERE id=" + engineer_id;
        Cursor cursor = db.rawQuery(selectQuery, null); //getDB()

        UserLoginWrapper user = new UserLoginWrapper();
        if (cursor.moveToFirst()) {
            user.setID(Integer.parseInt(cursor.getString(0)));
            user.setUserGroupId(cursor.getString(1));
            user.setRole(cursor.getString(2));
            user.setFullname(cursor.getString(3));
            user.setUsername(cursor.getString(4));
            user.setPassword(cursor.getString(5));
            user.setEmail(cursor.getString(6));
            user.setFax(cursor.getString(7));
            user.setPhoneNo(cursor.getString(8));
            user.setRace(cursor.getString(9));
            user.setAuthKey(cursor.getString(10));
            user.setPasswordHash(cursor.getString(11));
            user.setPasswordResetToken(cursor.getString(12));
            user.setPhoto(cursor.getString(13));
            user.setStatus(Integer.parseInt(cursor.getString(14)));
            user.setDeleted(Integer.parseInt(cursor.getString(15)));
            user.setCreatedAt(cursor.getString(16));
            user.setCreatedBy(cursor.getString(17));
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return user;
    }

    /**************************
     * USER GROUP Query
     **************************/
    public List<String> getUserGroup() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getDB().rawQuery("SELECT * FROM " + TABLE_USER_GROUP, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1)); // name column
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<UserGroupWrapper> getAllUserGroup() {
        ArrayList<UserGroupWrapper> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER_GROUP;
        Cursor cursor = getDB().rawQuery(selectQuery, null);
        UserGroupWrapper user = new UserGroupWrapper();

        if (cursor.moveToFirst()) {
            do {
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setDescription(cursor.getString(2));
                user.setCreatedAt(cursor.getString(3));
                user.setCreatedBy(Integer.parseInt(cursor.getString(4)));
                user.setUpdatedAt(cursor.getString(5));
                user.setUpdatedBy(Integer.parseInt(cursor.getString(6)));
                userList.add(user);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return userList;
    }
}

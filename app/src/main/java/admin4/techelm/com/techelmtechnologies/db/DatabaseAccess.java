package admin4.techelm.com.techelmtechnologies.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.TranslateModel;

public class DatabaseAccess {

    private static DatabaseAccess instance;
    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;
    private Context mContext;
    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public DatabaseAccess(Context context) {
        this.mContext = context;
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Get the database to use to open a query to the Utility Classe(s)
     *
     * @return the database has been opened
     */
    public SQLiteDatabase getDB() {
        return this.database;
    }

    /*
    * String selectQuery = "SELECT *, LENGTH(arabic) - LENGTH(REPLACE(arabic, ' ', '')) as LEN " +
                " FROM " + TABLE_DICTIONARY + " WHERE arabic=\"" + word + "\" or arabic LIKE '%" + word + "'";

    * */

}
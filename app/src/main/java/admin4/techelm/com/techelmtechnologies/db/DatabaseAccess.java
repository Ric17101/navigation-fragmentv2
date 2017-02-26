package admin4.techelm.com.techelmtechnologies.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private static final String TABLE_USER = "user";
    private static final String TABLE_USER_GROUP = "user_group";
    private static final String TABLE_DICTIONARY = "dictionary";
    private static DatabaseAccess instance;
    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
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


    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getQuotes() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_USER_GROUP, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    // Getting All Data
    public List<TranslateModel> getAllTranslations() {
        ArrayList<TranslateModel> translationList = new ArrayList<TranslateModel>();
        String selectQuery = "SELECT * FROM " + TABLE_DICTIONARY;

        Cursor cursor = database.rawQuery(selectQuery, null);
        TranslateModel translation = new TranslateModel();
        if (cursor.moveToFirst()) {
            do {
                translation.setID(Integer.parseInt(cursor.getString(0)));
                translation.setArabic(cursor.getString(1));
                translation.setEnglish(cursor.getString(2));
                translation.setType(cursor.getString(3));
                translation.setDefinition(cursor.getString(4));
                translation.setPronunciation(cursor.getString(5));
                translationList.add(translation);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return translationList;
    }

    // Getting Arabic word Data for ListView
    public List<String> getArabicTranslations() {
        ArrayList<String> translationList = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_DICTIONARY;

        Cursor cursor = database.rawQuery(selectQuery, null);
        String translation;
        if (cursor.moveToFirst()) {
            do {
                translation = cursor.getString(1);
//				translation.setID(Integer.parseInt(cursor.getString(0)));
//				translation.setEnglish(cursor.getString(1));
//				translation.setArabic(cursor.getString(2));
//				translation += "    ";
//				translation += cursor.getString(1);
//				translation += "    ";
                //translation += cursor.getString(2);
//				translation += "    ";
//				translation += cursor.getString(3);
                translationList.add(translation);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        // return data list
        return translationList;
    }

    // Getting Details Data WORD
    public List<TranslateModel> getAllDetailsByWord_OLD(String word) {
        ArrayList<TranslateModel> translationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_DICTIONARY + " WHERE arabic=\"" + word + "\"";

        Cursor cursor = database.rawQuery(selectQuery, null);
        TranslateModel alpha = new TranslateModel();
        if (cursor.moveToFirst()) {
            do {
                alpha.setID(Integer.parseInt(cursor.getString(0)));
                alpha.setArabic(cursor.getString(1));
                alpha.setEnglish(cursor.getString(2));
                alpha.setType(cursor.getString(3));
                alpha.setDefinition(cursor.getString(4));
                alpha.setPronunciation(cursor.getString(5));
                translationList.add(alpha);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return translationList;
    }

    // Getting Details Data ALPHABET
    public List<TranslateModel> getAllDetailsByWord(String word) {
        ArrayList<TranslateModel> translationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_DICTIONARY + " WHERE arabic=\"" + word + "\"";

        Cursor cursor = database.rawQuery(selectQuery, null);
        TranslateModel alpha = new TranslateModel();
        if (cursor.moveToFirst()) {
            do {
                alpha.setID(Integer.parseInt(cursor.getString(0)));
                alpha.setArabic(cursor.getString(1));
                alpha.setType(cursor.getString(3));
                alpha.setEnglish(cursor.getString(2));
                alpha.setDefinition(cursor.getString(4));
                alpha.setPronunciation(cursor.getString(5));
                translationList.add(alpha);
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return translationList;
    }

    public TranslateModel getTranslationToArabic(String word) {
        String selectQuery = "SELECT *, LENGTH(arabic) - LENGTH(REPLACE(arabic, ' ', '')) as LEN " +
                " FROM " + TABLE_DICTIONARY + " WHERE arabic=\"" + word + "\" or arabic LIKE '%" + word + "'";

        Cursor cursor = database.rawQuery(selectQuery, null);
        TranslateModel alpha = new TranslateModel();
        if (cursor.moveToFirst()) {
            do {
                alpha.setID(Integer.parseInt(cursor.getString(0)));
                alpha.setArabic(cursor.getString(1));
                alpha.setType(cursor.getString(3));
                alpha.setEnglish(cursor.getString(2));
                alpha.setDefinition(cursor.getString(4));
                alpha.setPronunciation(cursor.getString(5));
                alpha.setLength(Integer.parseInt(cursor.getString(6)));
                break;
            } while (cursor.moveToNext());
        }

        Log.d("CLSS", alpha.toString());

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return alpha;
    }

    public TranslateModel getTranslationToArabic_OLD(String word) {
        Cursor cursor = database.query(TABLE_DICTIONARY, new String[]{"id",
                        "english", "arabic", "type", "definition", "pronunciation"}, "arabic" + "=?",
                new String[]{word}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TranslateModel translation = new TranslateModel(Integer.parseInt(
                cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));
        // return data
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return translation;
    }

}
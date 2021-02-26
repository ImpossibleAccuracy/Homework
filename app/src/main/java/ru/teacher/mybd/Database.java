package ru.teacher.mybd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Database {
    private static final String DATABASE_NAME = "Contact.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "MY_CONTACT";

    public static final String _ID = "_ID";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String SECOND_NAME = "SECOND_NAME";
    public static final String PHONE = "PHONE";

    public static final int NUM__ID = 0;
    public static final int NUM_FIRST_NAME = 1;
    public static final int NUM_SECOND_NAME = 2;
    public static final int NUM_PHONE = 3;

    private final DBHelper helper;

    private Database(Context context) {
        helper = new DBHelper(context);
    }

    public boolean isEmpty() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        );

        long count = c.getCount();
        c.close();

        return (count == 0);
    }

    public Contact select(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor myCursor = db.query(
            TABLE_NAME,
            null,
            _ID + " = ?",
            new String[]{String.valueOf(id)},
            null,
            null,
            null
        );

        Contact result;
        if (!myCursor.isAfterLast()) {
            myCursor.moveToFirst();
            result= new Contact(
                    myCursor.getLong(NUM__ID),
                    myCursor.getString(NUM_FIRST_NAME),
                    myCursor.getString(NUM_SECOND_NAME),
                    myCursor.getString(NUM_PHONE));

            Log.i("Data", result.toString() + " select in DB");
        } else
            result = null;

        myCursor.close();
        return result;
    }
    public ArrayList<Contact> selectAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor myCursor = db.query(
                TABLE_NAME,     //TableName
                null,
                null,
                null,
                null,
                null,
                null,//FIRST_NAME,     //orderBy
                null
        );
        myCursor.moveToFirst();
        ArrayList<Contact> temp = new ArrayList<>();
        if (!myCursor.isAfterLast()) {
            do {
                temp.add(new Contact(
                        myCursor.getLong(NUM__ID),
                        myCursor.getString(NUM_FIRST_NAME),
                        myCursor.getString(NUM_SECOND_NAME),
                        myCursor.getString(NUM_PHONE)
                ));
            } while (myCursor.moveToNext());
        }
        myCursor.close();
        return temp;
    }

    public long insert(Contact contact) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIRST_NAME, contact.getFirstName());
        cv.put(SECOND_NAME, contact.getSecondName());
        cv.put(PHONE, contact.getPhoneNumber());
        Log.i("Data", contact.toString() + " insert in DB");
        return db.insert(TABLE_NAME, null, cv);
    }

    public long update(Contact contact) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FIRST_NAME, contact.getFirstName());
        cv.put(SECOND_NAME, contact.getSecondName());
        cv.put(PHONE, contact.getPhoneNumber());

        return db.update(
            TABLE_NAME,
            cv,
            _ID + " = ?",
            new String[] { String.valueOf(contact.getId()) }
        );
    }
    public long updateFirstName(Contact oldContact, Contact newContact) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor myCursor = db.query(
                TABLE_NAME,
                null,
                FIRST_NAME + " = ? AND " + SECOND_NAME + " = ? AND " + PHONE + " = ?",
                new String[]{oldContact.getFirstName(), oldContact.getSecondName(), oldContact.getPhoneNumber()},//args
                null, null, null
        );
        myCursor.moveToFirst();
        ContentValues cv = new ContentValues();
        cv.put(FIRST_NAME, newContact.getFirstName());
        return db.update(
                TABLE_NAME,
                cv,
                _ID + " = ?",
                new String[]{String.valueOf(myCursor.getLong(NUM__ID))});
    }

    public void deleteAll() {
        helper
            .getWritableDatabase()
            .delete(TABLE_NAME, null, null);
    }
    public void delete(long id) {
        helper
            .getWritableDatabase()
            .delete(
                TABLE_NAME, _ID + " = ?",
                new String[]{String.valueOf(id)}
            );
    }

    private static Database instance;
    public static Database GetInstance(Context context) {
        if (instance == null)
            instance = new Database(context);

        return instance;
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME + "(" +
                            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                            FIRST_NAME + " TEXT NOT NULL," +
                            SECOND_NAME + " TEXT NOT NULL," +
                            PHONE + "  TEXT NOT NULL UNIQUE ON CONFLICT ROLLBACK);"
            );
//            String query = "CREATE TABLE " + TABLE_NAME + "(" +
//                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    FIRST_NAME + " TEXT NOT NULL," +
//                    SECOND_NAME + " TEXT NOT NULL," +
//                    PHONE + "  TEXT NOT NULL);";
//            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
//            if (oldVersion == 1) {
//            }
//            if (oldVersion < 3) {
//            }
        }
    }
}

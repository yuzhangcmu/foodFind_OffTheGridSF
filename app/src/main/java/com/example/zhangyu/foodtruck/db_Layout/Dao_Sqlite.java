package com.example.zhangyu.foodtruck.db_Layout;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.zhangyu.foodtruck.entities.Event;
import com.example.zhangyu.foodtruck.entities.Vender;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Dao_Sqlite extends SQLiteOpenHelper {
    private static final String TAG = "Dao_Sqlite";

    // Database Version
    public static final int DATABASE_VERSION = 2;

    // Database table name
    private static final String TABLE_EVENT = "events";

    private static final String KEY_VENDER_NAME = "vender";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_TIMEZONE = "timezone";
    private static final String KEY_EVENT_ID = "event_id";

    SQLiteDatabase myDB;

    public Dao_Sqlite(Context context) {
        super(context, "test", null, DATABASE_VERSION);
        myDB = getWritableDatabase();
    }

    public LinkedList<Event> getContactList() {
        LinkedList<Event> contactList = new LinkedList<Event>();

        String sql = "SELECT * FROM " + TABLE_EVENT;
        Log.d(TAG, sql);

        Cursor c = myDB.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Event event = new Event(
                        c.getString(c.getColumnIndex(KEY_VENDER_NAME)),
                        Long.parseLong(c.getString(c.getColumnIndex(KEY_START_TIME))),
                        c.getString(c.getColumnIndex(KEY_LOCATION)),
                        c.getString(c.getColumnIndex(KEY_EVENT_ID))
                );

                // adding to contact list.
                //contactList.add(event);
            } while (c.moveToNext());
        }

        return contactList;
    }

    // return the occurrence of the vendors in 30 days.
    public Cursor getEventsCursor() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date result = cal.getTime();

        // convert the date to unix epoch
        long timeInMillisSinceEpoch = result.getTime();
        long timeUnix = timeInMillisSinceEpoch / (1000);

        String sql = "SELECT "
                + KEY_VENDER_NAME + ","
                + KEY_START_TIME + ","
                + KEY_LOCATION
                + " FROM " + TABLE_EVENT
                + " WHERE " + KEY_START_TIME + ">=" + timeUnix
                + " ORDER BY " + KEY_START_TIME;
        Log.d(TAG, sql);

        Cursor c = myDB.rawQuery(sql, null);
        return c;
    }

    // return the times of occurrences in Last 30 days.
    public long getVendorOccurrences(String name) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date result = cal.getTime();

        // convert the date to unix epoch
        long timeInMillisSinceEpoch = result.getTime();
        long timeUnix = timeInMillisSinceEpoch / (1000);

        Log.d(TAG, "Current date:" + result.toString());
        String sql = "SELECT COUNT(*) FROM " + TABLE_EVENT
                + " WHERE "
                + KEY_VENDER_NAME + "=" + "'" + name+ "'"
                + " AND " + KEY_START_TIME + ">=" + timeUnix
                ;
        SQLiteStatement statement = myDB.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public Cursor getVendorsCursor() {
        String sql = "SELECT "
                + KEY_VENDER_NAME + "," + "COUNT(" + KEY_VENDER_NAME + ") AS NumberOfOccurrence "
                + " FROM " + TABLE_EVENT
                + " GROUP BY " + KEY_VENDER_NAME
                + " ORDER BY " + "NumberOfOccurrence" + " DESC";
        Log.d(TAG, sql);

        Cursor c = myDB.rawQuery(sql, null);
        return c;
    }

    public Cursor addVendorsToArrayList(List<Vender> venders) {
        Cursor c = getVendorsCursor();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                int column = c.getColumnIndex(KEY_VENDER_NAME);
                if (column < 0) {
                    return c;
                }

                String venderName = c.getString(column);

                column = c.getColumnIndex("NumberOfOccurrence");
                if (column < 0) {
                    return c;
                }
                int count = c.getInt(column);

                Vender vendor = new Vender(
                        venderName,
                        count
                );

                // adding to event list.
                venders.add(vendor);
            } while (c.moveToNext());
        }

        return c;
    }

    public Cursor addEventsToArrayList(List<Event> events) {
        Cursor c = getEventsCursor();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                int column = c.getColumnIndex(KEY_VENDER_NAME);
                if (column < 0) {
                    return c;
                }

                String venderName = c.getString(column);

                column = c.getColumnIndex(KEY_START_TIME);
                if (column < 0) {
                    return c;
                }
                String startTime = c.getString(column);

                column = c.getColumnIndex(KEY_LOCATION);
                if (column < 0) {
                    return c;
                }
                String location = c.getString(column);

                column = c.getColumnIndex(KEY_EVENT_ID);
                if (column < 0) {
                    return c;
                }
                String id = c.getString(column);

                Event event = new Event(
                        venderName,
                        Long.parseLong(startTime),
                        location,
                        id
                );

                // adding to event list.
                events.add(event);
            } while (c.moveToNext());
        }

        return c;
    }

    public void close() {
        myDB.close();
    }

    public void cleanTable() {
        try {
            // drop the contact table.
            String sql = "DROP TABLE " + TABLE_EVENT + ";";
            myDB.execSQL(sql);

        } catch (Exception e) {
            Log.e("Error", "Error", e);
        }
    }

    public void addEvent(Event event) {
        try {
            // Get the database if database is not exists create new database
            // Database name is " test "
            StringBuilder sb = new StringBuilder();

            // Send all output to the Appendable object sb
            Formatter formatter = new Formatter(sb, Locale.US);

            // use "" because the string contains "'".
            formatter.format("REPLACE INTO %s ("
                            + KEY_VENDER_NAME + ","
                            + KEY_START_TIME + ","
                            + KEY_LOCATION + ","
                            + KEY_EVENT_ID
                            +") VALUES (\"%s\", \"%s\", \"%s\", \"%s\");",
                            TABLE_EVENT,
                            event.getName(),
                            event.getStartTime(),
                            event.getLocation(),
                            event.getId()
            );

            formatter.close();
            String sql = sb.toString();
            myDB.execSQL(sql);
            //Log.d(TAG, sql);

        } catch (Exception e) {
            Log.e("Error", "Error", e);
        }
    }

    public void deleteContact(String userid) {
//        myDB.delete(TABLE_EVENT, KEY_USERID + " = ?",
//                new String[] { userid });
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        try {
            /*
                Create the table of events.
            * */
            DB.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EVENT
                    + " ("
                    + KEY_START_TIME + " INTEGER, "
                    + KEY_END_TIME + " INTEGER, "
                    + KEY_VENDER_NAME + " varchar(1000) , "
                    + KEY_LOCATION + " varchar(1000),"
                    + KEY_TIMEZONE + " varchar(255),"
                    + KEY_EVENT_ID + " varchar(255) PRIMARY KEY"
                    + ");");

        } catch (Exception e) {
            Log.e("Error", "Error", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);

        // create new tables
        onCreate(db);
    }
}
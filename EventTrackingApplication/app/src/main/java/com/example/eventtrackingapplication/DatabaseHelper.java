package com.example.eventtrackingapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EventTrackingApp.db";
    private static final int DATABASE_VERSION = 1;

    // Table for user accounts
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Table for events
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_EVENT_ID = "id";
    public static final String COLUMN_EVENT_NAME = "name";
    public static final String COLUMN_EVENT_LOCATION = "location";
    public static final String COLUMN_EVENT_TIME = "time";
    public static final String COLUMN_EVENT_DESCRIPTION = "description";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT);";

    private static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS + " (" +
                    COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EVENT_NAME + " TEXT, " +
                    COLUMN_EVENT_LOCATION + " TEXT, " +
                    COLUMN_EVENT_TIME + " TEXT, " +
                    COLUMN_EVENT_DESCRIPTION + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }
}

package com.example.moviedb.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_MOVIES =
            "CREATE TABLE " + DbContract.MovieEntry.TABLE_MOVIES + " (" +
                    DbContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    DbContract.MovieEntry.COLUMN_NAME_ID + " INTEGER," +
                    DbContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT," +
                    DbContract.MovieEntry.COLUMN_NAME_RATING + " DOUBLE," +
                    DbContract.MovieEntry.COLUMN_NAME_PATH_IMG + " TEXT)";

    private static final String SQL_CREATE_SERIES =
            "CREATE TABLE " + DbContract.MovieEntry.TABLE_SERIES + " (" +
                    DbContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    DbContract.MovieEntry.COLUMN_NAME_ID + " INTEGER," +
                    DbContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT," +
                    DbContract.MovieEntry.COLUMN_NAME_RATING + " DOUBLE," +
                    DbContract.MovieEntry.COLUMN_NAME_PATH_IMG + " TEXT)";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MovieDB.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES);
        sqLiteDatabase.execSQL(SQL_CREATE_SERIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void deleteAll(String table) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ table);
        db.close();
    }
}

package com.ropkastudios.android.thegeographyapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SavedWebBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "savedWebsites.db";

    public SavedWebBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DbSchema.SavedWebTable.NAME +
                "(" + " _id integer primary key autoincrement, " +
                DbSchema.SavedWebTable.Cols.UUID + " INTEGER, " +
                DbSchema.SavedWebTable.Cols.TITLE + " TEXT, " +
                DbSchema.SavedWebTable.Cols.DESCRIPTION + " TEXT, " +
                DbSchema.SavedWebTable.Cols.URL + " TEXT, " +
                DbSchema.SavedWebTable.Cols.IMG_URL + " TEXT, " +
                DbSchema.SavedWebTable.Cols.DATE_VAL + " INTEGER, " +
                DbSchema.SavedWebTable.Cols.CATEGORY + " TEXT, " +
                DbSchema.SavedWebTable.Cols.DATE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

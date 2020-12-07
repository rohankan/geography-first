package com.ropkastudios.android.thegeographyapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RandomValuesBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "randomValuesRandom.db";

    public RandomValuesBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DbSchema.RandomValuesTable.NAME +
                "(" + " _id integer primary key autoincrement, " +
                DbSchema.RandomValuesTable.Cols.UUID + " INTEGER, " +
                DbSchema.RandomValuesTable.Cols.INTS + " INTEGER, " +
                DbSchema.RandomValuesTable.Cols.BOOLEANS + " INTEGER, " +
                DbSchema.RandomValuesTable.Cols.STRINGS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

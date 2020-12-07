package com.ropkastudios.android.thegeographyapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InformationCardBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "informationCardBase.db";

    public InformationCardBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DbSchema.InformationCardTable.NAME + "(" + " _id integer primary key autoincrement, " +
                            DbSchema.InformationCardTable.Cols.UUID + " INTEGER, " +
                            DbSchema.InformationCardTable.Cols.CARD_INFORMATION_TITLE + " TEXT, " +

                            DbSchema.InformationCardTable.Cols.PARAGRAPH + " TEXT, " +
                            DbSchema.InformationCardTable.Cols.BUTTON_TITLE + " TEXT, " +
                            DbSchema.InformationCardTable.Cols.DELETABLE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

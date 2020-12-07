package com.ropkastudios.android.thegeographyapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InfoSubCGBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "infoSubCGCardBase.db";

    public InfoSubCGBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DbSchema.InformationSubCategoryCardTable.NAME +
                "(" + " _id integer primary key autoincrement, " +
                DbSchema.InformationSubCategoryCardTable.Cols.UUID + " INTEGER, " +
                DbSchema.InformationSubCategoryCardTable.Cols.CARD_TITLE + " TEXT, " +
                DbSchema.InformationSubCategoryCardTable.Cols.CATEGORY + " TEXT, " +
                DbSchema.InformationSubCategoryCardTable.Cols.DELETABLE + " INTEGER, " +
                DbSchema.InformationSubCategoryCardTable.Cols.CONTENTS + " TEXT, " +
                DbSchema.InformationSubCategoryCardTable.Cols.NOTES + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

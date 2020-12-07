package com.ropkastudios.android.thegeographyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ropkastudios.android.thegeographyapp.database.AppCursorWrapper;
import com.ropkastudios.android.thegeographyapp.database.DbSchema;
import com.ropkastudios.android.thegeographyapp.database.RandomValuesBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class RandomValuesLab {
    private static RandomValuesLab sRandomValueLab;
    private SQLiteDatabase mDatabase;

    public static RandomValuesLab get(Context context) {
        if(sRandomValueLab == null) {
            sRandomValueLab = new RandomValuesLab(context);
        }
        return sRandomValueLab;
    }

    private RandomValuesLab(Context context) {
        Context mContext = context.getApplicationContext();
        mDatabase = new RandomValuesBaseHelper(mContext).getWritableDatabase();
    }

    public void addRandomValue(RandomValuesHolder info) {
        ContentValues values = getContentValues(info);

        mDatabase.insert(DbSchema.RandomValuesTable.NAME, null, values);
    }

    public List<RandomValuesHolder> getRandomValues() {
        List<RandomValuesHolder> randomValuesList = new ArrayList<>();

        AppCursorWrapper cursor = queryCardInfos(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                randomValuesList.add(cursor.getValues());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return randomValuesList;
    }

    private static ContentValues getContentValues(RandomValuesHolder information) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.RandomValuesTable.Cols.UUID, information.getUUID().toString());
        values.put(DbSchema.RandomValuesTable.Cols.INTS, information.getInt());
        if (information.getBoolean()) {
            values.put(DbSchema.RandomValuesTable.Cols.BOOLEANS, 1);
        } else {
            values.put(DbSchema.RandomValuesTable.Cols.BOOLEANS, 0);
        }
        values.put(DbSchema.RandomValuesTable.Cols.STRINGS, information.getString());
        return values;
    }

    private AppCursorWrapper queryCardInfos(String whereClause,String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DbSchema.RandomValuesTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new AppCursorWrapper(cursor);
    }
}

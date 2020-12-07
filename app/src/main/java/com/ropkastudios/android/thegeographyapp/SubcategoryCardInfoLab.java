package com.ropkastudios.android.thegeographyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ropkastudios.android.thegeographyapp.database.AppCursorWrapper;
import com.ropkastudios.android.thegeographyapp.database.DbSchema;
import com.ropkastudios.android.thegeographyapp.database.InfoSubCGBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class SubcategoryCardInfoLab {
    private static SubcategoryCardInfoLab sCardInfoLab;
    private SQLiteDatabase mDatabase;

    public static SubcategoryCardInfoLab get(Context context) {
        if(sCardInfoLab == null) {
            sCardInfoLab = new SubcategoryCardInfoLab(context);
        }
        return sCardInfoLab;
    }
    private SubcategoryCardInfoLab(Context context) {
        Context mContext = context.getApplicationContext();
        mDatabase = new InfoSubCGBaseHelper(mContext).getWritableDatabase();
    }

    public void addCardInfo(SubcategoryCardInfoHolder info) {
        ContentValues values = getContentValues(info);

        mDatabase.insert(DbSchema.InformationSubCategoryCardTable.NAME, null, values);
    }

    public void deleteCardInfo(UUID uuid) {
        mDatabase.delete(DbSchema.InformationSubCategoryCardTable.NAME, "SUB_CAT_UUID = ?", new String[]{uuid.toString()});
    }

    public void deleteCardInfos(String[] uuids) {
        for (int i = 0; i < uuids.length; ++i) {
            mDatabase.delete(DbSchema.InformationSubCategoryCardTable.NAME, "SUB_CAT_UUID = ?", new String[]{uuids[i]});
        }
    }

    public List<SubcategoryCardInfoHolder> getCardInfos() {
        List<SubcategoryCardInfoHolder> cardInformationList = new ArrayList<>();

        AppCursorWrapper cursor = queryCardInfos(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cardInformationList.add(cursor.getSubcategoryCard());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return cardInformationList;
    }

    public List<SubcategoryCardInfoHolder> getCategoryCardInfos(String category) {
        List<SubcategoryCardInfoHolder> cardInformationList = new ArrayList<>();

        AppCursorWrapper cursor = queryCardInfos("SUB_CAT_CATEGORY = ?", new String[]{category.toLowerCase()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cardInformationList.add(cursor.getSubcategoryCard());
                cursor.moveToNext();
            }

            Collections.sort(cardInformationList, new Comparator<SubcategoryCardInfoHolder>() {
                @Override
                public int compare(final SubcategoryCardInfoHolder object1, final SubcategoryCardInfoHolder object2) {
                    return object1.getTitle().compareTo(object2.getTitle());
                }
            });
        } finally {
            cursor.close();
        }

        return cardInformationList;
    }

    public boolean isTitleUnique(String newTitle, String category) {
        List<SubcategoryCardInfoHolder> list = getCategoryCardInfos(category);
        for (SubcategoryCardInfoHolder i : list) {
            if (i.getTitle().toLowerCase().equals(newTitle.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public SubcategoryCardInfoHolder getSpecificCardInfo(UUID id) {
        AppCursorWrapper cursor = queryCardInfos(
                DbSchema.InformationSubCategoryCardTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getSubcategoryCard();
        } finally {
            cursor.close();
        }
    }

    public void updateCardInfo(SubcategoryCardInfoHolder info) {
        String uuidString = info.getId().toString();
        ContentValues values = getContentValues(info);
        mDatabase.update(DbSchema.InformationSubCategoryCardTable.NAME, values,
                DbSchema.InformationSubCategoryCardTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    private static ContentValues getContentValues(SubcategoryCardInfoHolder cardInformation) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.InformationSubCategoryCardTable.Cols.UUID, cardInformation.getId().toString());
        values.put(DbSchema.InformationSubCategoryCardTable.Cols.CARD_TITLE, cardInformation.getTitle());
        values.put(DbSchema.InformationSubCategoryCardTable.Cols.CATEGORY, cardInformation.getCategory().toLowerCase());
        if (cardInformation.getDeleteable()) {
            values.put(DbSchema.InformationSubCategoryCardTable.Cols.DELETABLE, 1);
        } else {
            values.put(DbSchema.InformationSubCategoryCardTable.Cols.DELETABLE, 0);
        }
        values.put(DbSchema.InformationSubCategoryCardTable.Cols.CONTENTS, cardInformation.getContents());
        values.put(DbSchema.InformationSubCategoryCardTable.Cols.NOTES, cardInformation.getNotes());

        return values;
    }

    private AppCursorWrapper queryCardInfos(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DbSchema.InformationSubCategoryCardTable.NAME,
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

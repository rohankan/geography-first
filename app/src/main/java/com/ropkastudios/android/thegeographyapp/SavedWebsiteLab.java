package com.ropkastudios.android.thegeographyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ropkastudios.android.thegeographyapp.database.AppCursorWrapper;
import com.ropkastudios.android.thegeographyapp.database.DbSchema;
import com.ropkastudios.android.thegeographyapp.database.SavedWebBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
public class SavedWebsiteLab {
    private static SavedWebsiteLab sSavedWebsiteLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static final String NAT_GEO_NEWS = "nat_geo_news";
    public static final String BBC_NEWS = "bbc_news";
    public static final String CNN_NEWS = "cnn_news";
    public static final String SCIENCE_DAILY_NEWS = "science_daily_news";
    public static final String ALL = "all_news";

    public static SavedWebsiteLab get(Context context) {
        if(sSavedWebsiteLab == null) {
            sSavedWebsiteLab = new SavedWebsiteLab(context);
        }
        return sSavedWebsiteLab;
    }

    private SavedWebsiteLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new SavedWebBaseHelper(mContext).getWritableDatabase();
    }

    public void addSavedWeb(SavedWebsiteHolder info) {
        ContentValues values = getContentValues(info);

        mDatabase.insert(DbSchema.SavedWebTable.NAME, null, values);
    }

    public void deleteSavedWeb(UUID uuid) {
        mDatabase.delete(DbSchema.SavedWebTable.NAME, "savweb_uuid = ?", new String[]{uuid.toString()});
    }

    public List<SavedWebsiteHolder> getSavedWebs() {
        List<SavedWebsiteHolder> cardInformationList = new ArrayList<>();

        AppCursorWrapper cursor = queryCardInfos(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cardInformationList.add(cursor.getSavedWebsite());
                cursor.moveToNext();
            }
            Collections.sort(cardInformationList, new Comparator<SavedWebsiteHolder>() {
                @Override
                public int compare(final SavedWebsiteHolder object1, final SavedWebsiteHolder object2) {
                    return object1.getDateVal().compareTo(object2.getDateVal());
                }
            });
        } finally {
            cursor.close();
        }

        return cardInformationList;
    }

    public static List<SavedWebsiteHolder> sortByCategory(List<SavedWebsiteHolder> holderList, String category) {
        for (int i = holderList.size()-1; i >= 0; i--) {
            if (!holderList.get(i).getCategory().equals(category)) {
                holderList.remove(i);
            }
        }
        return holderList;
    }

    private static ContentValues getContentValues(SavedWebsiteHolder cardInformation) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.SavedWebTable.Cols.UUID, cardInformation.getUuid().toString());
        values.put(DbSchema.SavedWebTable.Cols.TITLE, cardInformation.getTitle());
        values.put(DbSchema.SavedWebTable.Cols.URL, cardInformation.getUrl());
        values.put(DbSchema.SavedWebTable.Cols.IMG_URL, cardInformation.getImageUrl());
        values.put(DbSchema.SavedWebTable.Cols.DATE, cardInformation.getDate());
        values.put(DbSchema.SavedWebTable.Cols.DATE_VAL, cardInformation.getDateVal());
        values.put(DbSchema.SavedWebTable.Cols.CATEGORY, cardInformation.getCategory());
        values.put(DbSchema.SavedWebTable.Cols.DESCRIPTION, cardInformation.getDescription());

        return values;
    }

    private AppCursorWrapper queryCardInfos(String whereClause,String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DbSchema.SavedWebTable.NAME,
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

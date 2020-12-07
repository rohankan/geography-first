package com.ropkastudios.android.thegeographyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ropkastudios.android.thegeographyapp.database.AppCursorWrapper;
import com.ropkastudios.android.thegeographyapp.database.DbSchema;
import com.ropkastudios.android.thegeographyapp.database.InformationCardBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
public class CardInfoLab {

    private static CardInfoLab sCardInfoLab;
    private SQLiteDatabase mDatabase;

    public static CardInfoLab get(Context context) {
        if(sCardInfoLab == null) {
            sCardInfoLab = new CardInfoLab(context);
        }
        return sCardInfoLab;
    }

    private CardInfoLab(Context context) {
        Context mContext = context.getApplicationContext();
        mDatabase = new InformationCardBaseHelper(mContext).getWritableDatabase();
    }

    public void addCardInfo(CardInfoHolder info) {
        ContentValues values = getContentValues(info);

        mDatabase.insert(DbSchema.InformationCardTable.NAME, null, values);
    }

    public void deleteCardInfo(UUID uuid) {
        mDatabase.delete(DbSchema.InformationCardTable.NAME, "UUID = ?", new String[]{uuid.toString()});
    }

    public List<CardInfoHolder> getCardInfos() {
        List<CardInfoHolder> cardInformationList = new ArrayList<>();

        AppCursorWrapper cursor = queryCardInfos(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cardInformationList.add(cursor.getCard());
                cursor.moveToNext();
            }
            Collections.sort(cardInformationList, new Comparator<CardInfoHolder>() {
                @Override
                public int compare(final CardInfoHolder object1, final CardInfoHolder object2) {
                    return object1.getTitle().compareTo(object2.getTitle());
                }
            });
        } finally {
            cursor.close();
        }

        return cardInformationList;
    }

    public boolean isTitleUnique(String newTitle) {
        List<CardInfoHolder> list = getCardInfos();
        for (CardInfoHolder i : list) {
            if (i.getTitle().toLowerCase().equals(newTitle.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public CardInfoHolder getSpecificCardInfo(UUID id) {
        AppCursorWrapper cursor = queryCardInfos(
                DbSchema.InformationCardTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCard();
        } finally {
            cursor.close();
        }
    }

    public void updateCardInfo(CardInfoHolder info) {
        String uuidString = info.getId().toString();
        ContentValues values = getContentValues(info);

        mDatabase.update(DbSchema.InformationCardTable.NAME, values, DbSchema.InformationCardTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private static ContentValues getContentValues(CardInfoHolder cardInformation) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.InformationCardTable.Cols.UUID, cardInformation.getId().toString());
        values.put(DbSchema.InformationCardTable.Cols.CARD_INFORMATION_TITLE, cardInformation.getTitle());
        values.put(DbSchema.InformationCardTable.Cols.PARAGRAPH, cardInformation.getParagraph());
        values.put(DbSchema.InformationCardTable.Cols.BUTTON_TITLE, cardInformation.getButtonText());
        if (cardInformation.getDeleteable()) {
            values.put(DbSchema.InformationCardTable.Cols.DELETABLE, 1);
        } else {
            values.put(DbSchema.InformationCardTable.Cols.DELETABLE, 0);
        }

        return values;
    }

    private AppCursorWrapper queryCardInfos(String whereClause,String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DbSchema.InformationCardTable.NAME, null, whereClause, whereArgs, null, null, null
        );

        return new AppCursorWrapper(cursor);
    }
}













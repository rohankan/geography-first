package com.ropkastudios.android.thegeographyapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.ropkastudios.android.thegeographyapp.CardInfoHolder;
import com.ropkastudios.android.thegeographyapp.RandomValuesHolder;
import com.ropkastudios.android.thegeographyapp.SavedWebsiteHolder;
import com.ropkastudios.android.thegeographyapp.SubcategoryCardInfoHolder;

import java.util.UUID;

public class AppCursorWrapper extends CursorWrapper {
    public AppCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CardInfoHolder getCard() {
        String uuidString = getString(getColumnIndex(DbSchema.InformationCardTable.Cols.UUID));
        String title = getString(getColumnIndex(DbSchema.InformationCardTable.Cols.CARD_INFORMATION_TITLE));
        String paragraph = getString(getColumnIndex(DbSchema.InformationCardTable.Cols.PARAGRAPH));
        String buttonTitle = getString(getColumnIndex(DbSchema.InformationCardTable.Cols.BUTTON_TITLE));
        int isDeletable = getInt(getColumnIndex(DbSchema.InformationCardTable.Cols.DELETABLE));

        CardInfoHolder cardInfo = new CardInfoHolder(UUID.fromString(uuidString));
        cardInfo.setTitle(title);
        cardInfo.setParagraph(paragraph);
        cardInfo.setButtonText(buttonTitle);
        if (isDeletable == 1) {
            cardInfo.setDeletable(true);
        } else {
            cardInfo.setDeletable(false);
        }

        return cardInfo;
    }

    public SavedWebsiteHolder getSavedWebsite() {
        String uuidString = getString(getColumnIndex(DbSchema.SavedWebTable.Cols.UUID));
        String title = getString(getColumnIndex(DbSchema.SavedWebTable.Cols.TITLE));
        String url = getString(getColumnIndex(DbSchema.SavedWebTable.Cols.URL));
        String imageUrl = getString(getColumnIndex(DbSchema.SavedWebTable.Cols.IMG_URL));
        String date = getString(getColumnIndex(DbSchema.SavedWebTable.Cols.DATE));
        String category = getString(getColumnIndex(DbSchema.SavedWebTable.Cols.CATEGORY));
        String description = getString(getColumnIndex(DbSchema.SavedWebTable.Cols.DESCRIPTION));
        int dateVal = getInt(getColumnIndex(DbSchema.SavedWebTable.Cols.DATE_VAL));

        return new SavedWebsiteHolder(UUID.fromString(uuidString), title, url, imageUrl, date, category, dateVal, description);
    }

    public SubcategoryCardInfoHolder getSubcategoryCard() {
        String uuidString = getString(getColumnIndex(DbSchema.InformationSubCategoryCardTable.Cols.UUID));
        String title = getString(getColumnIndex(DbSchema.InformationSubCategoryCardTable.Cols.CARD_TITLE));
        String category = getString(getColumnIndex(DbSchema.InformationSubCategoryCardTable.Cols.CATEGORY));
        int isDeletable = getInt(getColumnIndex(DbSchema.InformationSubCategoryCardTable.Cols.DELETABLE));
        String contents = getString(getColumnIndex(DbSchema.InformationSubCategoryCardTable.Cols.CONTENTS));
        String notes = getString(getColumnIndex(DbSchema.InformationSubCategoryCardTable.Cols.NOTES));

        SubcategoryCardInfoHolder cardInfo = new SubcategoryCardInfoHolder(UUID.fromString(uuidString));
        cardInfo.setTitle(title);
        cardInfo.setCategory(category);
        if (isDeletable == 1) {
            cardInfo.setDeletable(true);
        } else {
            cardInfo.setDeletable(false);
        }
        cardInfo.setContents(contents);
        cardInfo.setNotes(notes);

        return cardInfo;
    }

    public RandomValuesHolder getValues() {
        String uuidString = getString(getColumnIndex(DbSchema.RandomValuesTable.Cols.UUID));
        int integer = getInt(getColumnIndex(DbSchema.RandomValuesTable.Cols.INTS));
        int bool = getInt(getColumnIndex(DbSchema.RandomValuesTable.Cols.BOOLEANS));
        String text = getString(getColumnIndex(DbSchema.RandomValuesTable.Cols.STRINGS));
        boolean bol = (bool == 1);

        return new RandomValuesHolder(UUID.fromString(uuidString), integer, bol, text);
    }
}

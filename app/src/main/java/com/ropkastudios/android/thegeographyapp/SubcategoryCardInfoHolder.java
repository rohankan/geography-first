package com.ropkastudios.android.thegeographyapp;

import java.util.UUID;
public class SubcategoryCardInfoHolder {
    private UUID mCardId;
    private String mTitle;
    private boolean mDeleteable;
    private String mCategory;
    private String mContents;
    private String mNotes;
    public SubcategoryCardInfoHolder(String title, boolean deletable, String category, String contents, String notes) {
        this.mCardId = UUID.randomUUID();
        this.mTitle = title;
        this.mDeleteable = deletable;
        this.mCategory = category;
        this.mContents = contents;
        this.mNotes = notes;
    }
    public SubcategoryCardInfoHolder(UUID uuid) {
        this.mCardId = uuid;
    }
    public SubcategoryCardInfoHolder(UUID uuid, String title, boolean deletable, String category, String contents, String notes) {
        this.mCardId = uuid;
        this.mTitle = title;
        this.mDeleteable = deletable;
        this.mCategory = category;
        this.mContents = contents;
        this.mNotes = notes;
    }
    public UUID getId() {
        return mCardId;
    }
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    public boolean getDeleteable() {
        return mDeleteable;
    }
    public void setDeletable(boolean deletable) {
        this.mDeleteable = deletable;
    }
    public String getCategory() {
        return mCategory;
    }
    public void setCategory(String newCategory) {
        this.mCategory = newCategory;
    }
    public String getContents() {
        return mContents;
    }
    public void setContents(String newContents) {
        this.mContents = newContents;
    }
    public String getNotes() {
        return mNotes;
    }
    public void setNotes(String mNotes) {
        this.mNotes = mNotes;
    }
}

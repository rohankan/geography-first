package com.ropkastudios.android.thegeographyapp;

import java.util.UUID;
public class CardInfoHolder {
    private UUID mCardId;
    private String mTitle;
    private String mParagraph;
    private String mButtonText;
    private boolean mDeleteable;

    public CardInfoHolder(String title, String paragraph, String buttonText, boolean deletable) {
        this.mCardId = UUID.randomUUID();
        this.mTitle = title;
        this.mParagraph = paragraph;
        this.mButtonText = buttonText;
        this.mDeleteable = deletable;
    }
    public CardInfoHolder(UUID uuid) {
        this.mCardId = uuid;
    }
    public CardInfoHolder(UUID uuid, String title, String paragraph, String buttonText, boolean deletable) {
        this.mCardId = uuid;
        this.mTitle = title;
        this.mParagraph = paragraph;
        this.mButtonText = buttonText;
        this.mDeleteable = deletable;
    }

    public UUID getId() {
        return mCardId;
    }

    public String getButtonText() {
        return mButtonText;
    }
    public void setButtonText(String mButtonText) {
        this.mButtonText = mButtonText;
    }
    public String getParagraph() {
        return mParagraph;
    }
    public void setParagraph(String mParagraph) {
        this.mParagraph = mParagraph;
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
}

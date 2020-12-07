package com.ropkastudios.android.thegeographyapp;

import java.util.UUID;
public class SavedWebsiteHolder {
    UUID uuid;
    String title;
    String url;
    String img_url;
    String description;
    String date;
    String category;
    Integer dateVal;

    public static int getDateValue(String date) {
        int total = 0;
        date = date.substring(5);

        total += Integer.parseInt(date.substring(0, 2));
        date = date.substring(3);

        total += getMonthValue(date.substring(0, 3));
        date = date.substring(4);

        total += (Integer.parseInt(date) * 365);
        return total;
    }

    public static int getMonthValue(String month) {
        switch (month) {
            case "Jan":
                return 0;
            case "Feb":
                return 31;
            case "Mar":
                return 59;
            case "Apr":
                return 90;
            case "May":
                return 120;
            case "Jun":
                return 151;
            case "Jul":
                return 181;
            case "Aug":
                return 212;
            case "Sep":
                return 243;
            case "Oct":
                return 273;
            case "Nov":
                return 304;
            case "Dec":
                return 334;
            default:
                return 0;
        }
    }

    public SavedWebsiteHolder(String Title, String Url, String Img_Url, String Date, String Category, int DateVal, String Description) {
        uuid = UUID.randomUUID();
        title = Title;
        url = Url;
        img_url = Img_Url;
        date = Date;
        category = Category;
        dateVal = DateVal;
        description = Description;
    }

    public SavedWebsiteHolder(UUID Uuid, String Title, String Url, String Img_Url, String Date, String Category, int DateVal,
                              String Description) {
        uuid = Uuid;
        title = Title;
        url = Url;
        img_url = Img_Url;
        date = Date;
        category = Category;
        dateVal = DateVal;
        description = Description;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Integer getDateVal() {
        return dateVal;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return img_url;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}

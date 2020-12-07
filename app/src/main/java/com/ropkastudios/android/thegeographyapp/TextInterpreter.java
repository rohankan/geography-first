package com.ropkastudios.android.thegeographyapp;

import java.util.ArrayList;
import java.util.List;
public class TextInterpreter {
    public static final char TEXT_INDICATOR = '╞';
    public static final char IMAGE_INDICATOR = '↨';
    public static final char CONTENT_INDICATOR = '⻱';
    public static final char MAPS_INDICATOR = '评';
    public static final String CAMERA_INDICATOR = "|geocamera";
    public static final String VISIBILITY_GONE_INDICATOR = "goobmn☺♥fhyy";
    public static final String TXT_NOT_GONE_INDICATOR = "Z̐͜͞ͅ";

    public static String stripToText(String string) {
        String rStr = "";
        while (string.contains(String.valueOf(IMAGE_INDICATOR))) {
            String holder = string.substring(string.indexOf(TEXT_INDICATOR)+1, string.indexOf(IMAGE_INDICATOR));
            string = string.replaceFirst(String.valueOf(TEXT_INDICATOR), "");
            string = string.replaceFirst(String.valueOf(IMAGE_INDICATOR), "");
            rStr += (holder.equals(TXT_NOT_GONE_INDICATOR)) ? "" : holder;
            rStr += "\n\n";
        }

        return rStr;
    }

    public static String convertContentCardContent(StringArrayHolder pair) {
        String returnContentString = "";
        List<String> arrayOne = pair.getFirstArray();
        List<String> arrayTwo = pair.getSecondArray();
        List<String> arrayThree = pair.getThirdArray();
        List<String> arrayFour = pair.getFourthArray();
        for (int i = 0; i < arrayTwo.size(); ++i) {
            returnContentString += TEXT_INDICATOR;
            returnContentString += arrayOne.get(i);
            returnContentString += IMAGE_INDICATOR;
            returnContentString += arrayTwo.get(i);
            returnContentString += CONTENT_INDICATOR;
            returnContentString += arrayThree.get(i);
            returnContentString += MAPS_INDICATOR;
            returnContentString += arrayFour.get(i);
        }

        return returnContentString + TEXT_INDICATOR;
    }

    public static StringArrayHolder interpretContentCardContents(String content) {
        boolean titleCheck = false;
        boolean paraCheck = false;
        boolean contentCheck = false;
        boolean mapsCheck = false;

        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<String> paraList = new ArrayList<>();
        ArrayList<String> contentList = new ArrayList<>();
        ArrayList<String> mapsList = new ArrayList<>();
        String currentString = "";

        for (int i = 0; i < content.length(); ++i) {
            char currentChar = content.charAt(i);
            if (currentChar == TEXT_INDICATOR) {
                titleCheck = true;
                if (mapsCheck) {
                    mapsList.add(currentString);
                    currentString = "";
                }
                mapsCheck = false;
            } else if (currentChar == IMAGE_INDICATOR) {
                paraCheck = true;
                if (titleCheck) {
                    titleList.add(currentString);
                    currentString = "";
                }
                titleCheck = false;
            } else if (currentChar == CONTENT_INDICATOR) {
                contentCheck = true;
                if (paraCheck) {
                    paraList.add(currentString);
                    currentString = "";
                }
                paraCheck = false;
            } else if (currentChar == MAPS_INDICATOR) {
                mapsCheck = true;
                if (contentCheck) {
                    contentList.add(currentString);
                    currentString = "";
                }
                contentCheck = false;
            } else {
                currentString += currentChar;
            }
        }

        if (titleList.size() > paraList.size()) {
            paraList.add("");
        }
        if (titleList.size() > contentList.size()) {
            contentList.add("");
        }
        if (titleList.size() > mapsList.size()) {
            mapsList.add("");
        }

        return new StringArrayHolder(titleList, paraList, contentList, mapsList);
    }
}

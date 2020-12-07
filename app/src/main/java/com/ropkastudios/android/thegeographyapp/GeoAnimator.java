package com.ropkastudios.android.thegeographyapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
public class GeoAnimator {
    public static Bitmap rotateBitmapWithCondition(Bitmap bitmap, int degrees) {
        if (bitmap.getWidth() > bitmap.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }
    }

    public static void makeDurationToast(Context mContext, String title, int gravity, int leftOffset, int downOffset, int duration) {
        final Toast toast = Toast.makeText(mContext, title, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, leftOffset, downOffset);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
    }

    public static String capitalizeTitle(String string) {
        String returnStr = "";
        boolean checker = true;
        for (int i = 0; i < string.length(); i++) {
            if (checker) {
                returnStr += String.valueOf(string.charAt(i)).toUpperCase();
                checker = false;
            } else if (string.charAt(i) == ' ') {
                returnStr += " ";
                checker = true;
            } else {
                returnStr += string.charAt(i);
            }
        }
        return returnStr;
    }

    public static boolean ultimateIsNull(String string) {
        return string == null || string.equals("null") || string.isEmpty();
    }
}

package com.ropkastudios.android.thegeographyapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapOperations {
    public static int calculateInSampleSizeFromBitmap (Bitmap bitmap, int requestedWidth, int requestedHeight) {
        final int height = bitmap.getWidth();
        final int width = bitmap.getHeight();
        int inSampleSize = 1;

        if (height > requestedHeight || width > requestedWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= requestedHeight
                    && (halfWidth / inSampleSize) >= requestedWidth) {
                inSampleSize += 1;
            }
        }

        return inSampleSize;
    }

    public static Bitmap scaleDownBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        int inSampleSize = calculateInSampleSizeFromBitmap(bitmap, reqWidth, reqHeight);

        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / inSampleSize,
                bitmap.getHeight() / inSampleSize,
                false);
    }
}

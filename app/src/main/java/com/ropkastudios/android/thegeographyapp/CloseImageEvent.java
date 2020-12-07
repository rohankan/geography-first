package com.ropkastudios.android.thegeographyapp;

public class CloseImageEvent {
    private CloseImageListener mCloseImageListener;
    private boolean isOpened;
    private int mOrder;

    public CloseImageEvent(int order) {
        this.mOrder = order;
    }

    public int getOrder() {
        return mOrder;
    }

    public void setOnCloseImageListener(CloseImageListener listener) {
        mCloseImageListener = listener;
    }

    public void setIsOpened(boolean open) {
        this.isOpened = open;
    }

    public void activateCloseImage() {
        if (mCloseImageListener != null && isOpened)
            mCloseImageListener.onCloseImage();
    }
}

package com.cmps121.asgn2.models;

import android.graphics.Bitmap;

public class Item {
    private int iID;
    private String iTitle;
    private Bitmap iBitmap;

    public Item() {
        // empty constructor
    }

    public Item(int iID, String iTitle, Bitmap iBitmap) {
        this.iID = iID;
        this.iTitle = iTitle;
        this.iBitmap = iBitmap;
    }
    public void setID(int iID) {
        this.iID = iID;
    }

    public int getID() {
        return this.iID;
    }

    public String getTitle() {
        return this.iTitle;
    }

    public void setTitle(String iTitle) {
        this.iTitle = iTitle;
    }

    public Bitmap getBitmap() {
        return this.iBitmap;
    }

    public void setBitmap(Bitmap iBitmap) {
        this.iBitmap = iBitmap;
    }
}

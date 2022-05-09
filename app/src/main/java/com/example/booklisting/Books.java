package com.example.booklisting;

import android.graphics.drawable.Drawable;

public class Books {

    /** title of the book **/
    private String mTitle ;

    private String mPreview;

    private int mPages;

    private String mAuthor;

    private String mImage;

    public Books(String title ,String preview ,int pages, String author,String image){
        mPages = pages;
        mPreview = preview;
        mTitle = title;
        mAuthor = author;
        mImage = image;
    }

    public int getmPages() {
        return mPages;
    }

    public String getmPreview() {
        return mPreview;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmImage() {
        return mImage;
    }
}

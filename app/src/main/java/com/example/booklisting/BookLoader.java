package com.example.booklisting;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Books>> {

    private String murl;
    private static final String TAG = BookLoader.class.getName();
    public BookLoader(@NonNull Context context,String url) {
        super(context);
        murl = url;
    }

    @Nullable
    @Override
    public List<Books> loadInBackground() {
        if (murl == null || murl.length() < 1){
            return null;
        }
        List<Books> books = QueryUtlis.FetchBooks(murl);
        return books;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

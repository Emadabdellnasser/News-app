package com.example.emad.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by EMAD on 1/28/2018.
 */

public class Asynclass extends AsyncTaskLoader<List<News_object>> {
    String url;

    public Asynclass(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News_object> loadInBackground() {
        if (url == null) {
            return null;
        }
        List<News_object> NEWSLIST = QueryUtils.fetch(url);
        return NEWSLIST;
    }
}

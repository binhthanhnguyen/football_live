package com.example.binh.jsoupdemo.data.models;


import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by binh on 5/19/16.
 */
public class Match implements Serializable {
    private String mTitle;
    private String mUrl;
    private String mTimestamp;
    private String mImageUrl;

    public Match(String title, String href, String timestamp) {
        this.mTitle = title;
        this.mUrl = href;
        this.mTimestamp = timestamp;
    }
    public Match(String title, String href, String timestamp, String imageUrl) {
        this(title, href, timestamp);
        this.mImageUrl = imageUrl;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}

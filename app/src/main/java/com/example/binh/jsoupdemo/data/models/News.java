package com.example.binh.jsoupdemo.data.models;

/**
 * Created by binh on 5/20/16.
 */
public class News {
    private String mTitle;
    private String mDescription;
    private String mImageUrl;
    private String mUrl;
    private String mTimestamp;

    public News(String title, String description) {
        this.mTitle = title;
        this.mDescription = description;
    }
    public News(String title, String description, String imageUrl, String url, String timestamp) {
        this(title, description);
        this.mImageUrl = imageUrl;
        this.mTimestamp = timestamp;
        this.mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {return mDescription;}

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public String toString() {
        return mTitle;
    }


}

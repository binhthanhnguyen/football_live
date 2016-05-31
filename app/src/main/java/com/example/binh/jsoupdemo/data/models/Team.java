package com.example.binh.jsoupdemo.data.models;

/**
 * Created by binh on 5/28/16.
 */
public class Team {
    private int mPosition;
    private String mName;
    private String mImageUrl;
    private int mPst;
    private int mGp;
    private int mWins;
    private int mDraws;
    private int mLosses;

    public Team(int position, String name, String imageUrl, int pst) {
        mPosition = position;
        mName = name;
        mImageUrl = imageUrl;
        mPst = pst;
    }

    public Team(int position, String name, String imageUrl, int pst, int gp, int wins, int draws, int losses ) {
        this(position, name, imageUrl, pst);
        mGp = gp;
        mWins = wins;
        mDraws = draws;
        mLosses = losses;
    }


    public int getPosition() {
        return mPosition;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public int getPst() {
        return mPst;
    }

    public int getGp() {
        return mGp;
    }

    public int getWins() {
        return mWins;
    }

    public int getDraws() {
        return mDraws;
    }

    public int getLosses() {
        return mLosses;
    }

}

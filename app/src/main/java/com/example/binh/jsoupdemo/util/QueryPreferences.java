package com.example.binh.jsoupdemo.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by binh on 5/24/16.
 */
public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "PREF_SEARCH_QUERY";

    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(PREF_SEARCH_QUERY, query).apply();
    }
}

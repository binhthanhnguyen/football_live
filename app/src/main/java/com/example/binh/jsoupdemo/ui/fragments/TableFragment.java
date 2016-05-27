package com.example.binh.jsoupdemo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.binh.jsoupdemo.R;

/**
 * Created by binh on 5/18/16.
 */
public class TableFragment extends Fragment {
    public static final String TAG = "TableFragment";

    public static TableFragment newInstance() {
        TableFragment fragment = new TableFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table, container, false);
    }
}

package com.example.binh.jsoupdemo.ui.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.binh.jsoupdemo.data.models.News;
import com.example.binh.jsoupdemo.ui.activities.MainActivity;
import com.example.binh.jsoupdemo.R;
import com.example.binh.jsoupdemo.ui.adapters.MatchAdapter;
import com.example.binh.jsoupdemo.ui.adapters.NewsAdapter;
import com.example.binh.jsoupdemo.util.FootballFetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by binh on 5/19/16.
 */
public class NewsFragment extends Fragment {
    public static final String TAG = "NewsFragment";
    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;
    private List<News> mNewses = new ArrayList<>();


    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchNewsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        setupAdapter();
        return view;
    }

    private class FetchNewsTask extends AsyncTask<Void, Void, List<News>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected List<News> doInBackground(Void... params) {
            return new FootballFetcher().fetchNewsItems();
        }

        @Override
        protected void onPostExecute(List<News> items) {
            mNewses = items;
            setupAdapter();
            mProgressDialog.dismiss();
        }
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new NewsAdapter(mNewses));
        }
    }
}

package com.example.binh.jsoupdemo.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.binh.jsoupdemo.R;
import com.example.binh.jsoupdemo.data.models.Team;
import com.example.binh.jsoupdemo.ui.adapters.TableAdapter;
import com.example.binh.jsoupdemo.util.FootballFetcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binh on 5/18/16.
 */
public class TableFragment extends Fragment {
    public static final String TAG = "TableFragment";

    private RecyclerView mRecyclerView;
    private List<Team> mTeams = new ArrayList<>();
    public static TableFragment newInstance() {
        TableFragment fragment = new TableFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchTableTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        setupAdapter();
        return view;
    }

    private class FetchTableTask extends AsyncTask<Void, Void, List<Team>>{

        @Override
        protected List<Team> doInBackground(Void... params) {
            return new FootballFetcher().fetchTeamItems();
        }

        @Override
        protected void onPostExecute(List<Team> teams) {
            super.onPostExecute(teams);
            mTeams = teams;
            setupAdapter();
        }
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new TableAdapter(mTeams));
        }
    }
}

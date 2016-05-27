package com.example.binh.jsoupdemo.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.example.binh.jsoupdemo.R;
import com.example.binh.jsoupdemo.data.models.Match;
import com.example.binh.jsoupdemo.ui.adapters.MatchAdapter;
import com.example.binh.jsoupdemo.util.FootballFetcher;
import com.example.binh.jsoupdemo.util.QueryPreferences;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by binh on 5/19/16.
 */
public class MatchListFragment extends Fragment {
    public static final String TAG = "MatchListFragment";
    private RecyclerView mRecyclerView;
    private List<Match> mMatches = new ArrayList<>();


    public static MatchListFragment newInstance() {
        MatchListFragment fragment = new MatchListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        updateMatchItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        setupAdapter();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        final MenuItem searchItem = (MenuItem) menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "QueryTextSubmit: " + query);
                QueryPreferences.setStoredQuery(getActivity(), query);
                updateMatchItems();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG,"QueryTextChange: " + newText);
                if (newText == null) {
                    QueryPreferences.setStoredQuery(getActivity(), null);
                    updateMatchItems();
                }
                return true;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = QueryPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query, false);
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                QueryPreferences.setStoredQuery(getActivity(), null);
                updateMatchItems();
                return true;
            }
        });
    }

    private class FetchMatchListTask extends AsyncTask<Void, Void, List<Match>> {
        private String mQuery;

        public FetchMatchListTask(String query) {
            mQuery = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Match> doInBackground(Void... params) {
            if (mQuery == null) {
                return new FootballFetcher().fetchRecentMatchItems();
            } else  {
                return new FootballFetcher().searchMatchItems(mQuery);
            }

        }

        @Override
        protected void onPostExecute(List<Match> items) {
            mMatches = items;
            setupAdapter();
        }
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new MatchAdapter(mMatches));
        }
    }

    private void updateMatchItems() {
        String query = QueryPreferences.getStoredQuery(getActivity());
        new FetchMatchListTask(query).execute();
    }
}

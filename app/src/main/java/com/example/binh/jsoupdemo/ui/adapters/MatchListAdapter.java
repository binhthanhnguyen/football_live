package com.example.binh.jsoupdemo.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.binh.jsoupdemo.R;
import com.example.binh.jsoupdemo.data.models.Match;
import com.example.binh.jsoupdemo.ui.activities.MatchDetailActivity;

import java.util.List;

/**
 * Created by binh on 5/19/16.
 */
public class MatchListAdapter extends ArrayAdapter<Match> {

    public MatchListAdapter(Context context, List<Match> matches) {
        super(context, 0, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Match match = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.match_list_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.text_match_title);
        textView.setText(match.getTitle());

        TextView textTimestamp = (TextView) convertView.findViewById(R.id.text_match_timestamp);
        textTimestamp.setText(match.getTimestamp());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, MatchDetailActivity.class);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}

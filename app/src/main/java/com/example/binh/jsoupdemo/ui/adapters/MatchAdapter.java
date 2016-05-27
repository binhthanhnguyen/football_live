package com.example.binh.jsoupdemo.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.binh.jsoupdemo.R;
import com.example.binh.jsoupdemo.data.models.Match;
import com.example.binh.jsoupdemo.ui.activities.MatchDetailActivity;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by binh on 5/21/16.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchHolder> {
    private List<Match> matches;

    public MatchAdapter(List<Match> matches) {
        this.matches = matches;
    }

    @Override
    public MatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.match_list_item, parent, false);
        return new MatchHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchHolder holder, int position) {
        final Match match = (Match) matches.get(position);
        holder.bindMathItem(match);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MatchDetailActivity.newIntent(v.getContext(), match);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }


    public class MatchHolder extends RecyclerView.ViewHolder{
        private View mView;
        private Context mContext;
        private ImageView mImageMatch;
        private TextView mTextMatchTitle;
        private TextView mTextMatchTimestamp;

        public MatchHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mContext = itemView.getContext();
            mTextMatchTitle = (TextView) itemView.findViewById(R.id.text_match_title);
            mTextMatchTimestamp = (TextView) itemView.findViewById(R.id.text_match_timestamp);
            mImageMatch = (ImageView) itemView.findViewById(R.id.image_match);

        }

        public void bindMathItem(Match item) {
            mTextMatchTitle.setText(item.getTitle());
            mTextMatchTimestamp.setText(item.getTimestamp());
            Glide.with(mContext).load(item.getImageUrl()).into(mImageMatch);

        }
    }
}

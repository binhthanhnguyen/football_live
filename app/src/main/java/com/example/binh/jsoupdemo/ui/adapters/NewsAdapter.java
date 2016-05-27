package com.example.binh.jsoupdemo.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.binh.jsoupdemo.R;
import com.example.binh.jsoupdemo.data.models.News;
import com.example.binh.jsoupdemo.ui.activities.NewsDetailActivity;
import java.util.List;

/**
 * Created by binh on 5/20/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> mNewses;

    public NewsAdapter(List<News> newses) {
        mNewses = newses;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        final News news = (News) mNewses.get(position);
        holder.bindNewsItem(news);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = NewsDetailActivity.newIntent(context);
                intent.putExtra(NewsDetailActivity.EXTRA_NEWS_TITLE, news.getTitle());
                intent.putExtra(NewsDetailActivity.EXTRA_NEWS_IMAGE_URL, news.getImageUrl());
                intent.putExtra(NewsDetailActivity.EXTRA_NEWS_URL, news.getUrl());
                intent.putExtra(NewsDetailActivity.EXTRA_NEWS_DESC, news.getDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private CardView mCardView;
        private ImageView mImageNews;
        private TextView mTextTile;
        private TextView mTextDescription;
        private TextView mTextTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            mImageNews = (ImageView) itemView.findViewById(R.id.image_news);
            mTextTile = (TextView) itemView.findViewById(R.id.text_news_title);
            mTextDescription = (TextView) itemView.findViewById(R.id.text_news_description);
        }

        public void bindNewsItem(News item) {
            mTextTile.setText(item.getTitle());
            mTextDescription.setText(item.getDescription());
            Glide.with(mView.getContext()).load(item.getImageUrl()).into(mImageNews);
        }
    }
}

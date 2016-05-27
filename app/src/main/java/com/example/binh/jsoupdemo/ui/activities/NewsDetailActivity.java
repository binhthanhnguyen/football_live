package com.example.binh.jsoupdemo.ui.activities;



import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.binh.jsoupdemo.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by binh on 5/23/16.
 */
public class NewsDetailActivity extends AppCompatActivity {
    public static final String EXTRA_NEWS_TITLE = "EXTRA_NEWS_TITLE";
    public static final String EXTRA_NEWS_URL = "EXTRA_NEWS_URL";
    public static final String EXTRA_NEWS_IMAGE_URL = "EXTRA_NEWS_IMAGE_URL";
    public static final String EXTRA_NEWS_DESC = "EXTRA_NEWS_DESC";

    private String newsUrl;
    private String newsContent;
    private String newsDesc;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String newsTitle = intent.getStringExtra(NewsDetailActivity.EXTRA_NEWS_TITLE);
        final String imageUrl = intent.getStringExtra(NewsDetailActivity.EXTRA_NEWS_IMAGE_URL);
        newsUrl = intent.getStringExtra(NewsDetailActivity.EXTRA_NEWS_URL);
        newsDesc = intent.getStringExtra(NewsDetailActivity.EXTRA_NEWS_DESC);
        new FetchNewsContentTask().execute();

        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(newsTitle);

        final ImageView imageView = (ImageView) findViewById(R.id.image_news);
        Glide.with(this).load(imageUrl).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchNewsContentTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document document = Jsoup.connect(newsUrl).get();
                Elements elements = document.select("div.content");
                newsContent = elements.text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TextView textDesc = (TextView) findViewById(R.id.text_news_description);
            textDesc.setText(newsDesc);
            TextView textView = (TextView) findViewById(R.id.text_news_content);
            textView.setText(newsContent);
        }
    }


}

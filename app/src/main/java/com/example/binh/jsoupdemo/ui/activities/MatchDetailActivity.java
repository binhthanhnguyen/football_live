package com.example.binh.jsoupdemo.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.binh.jsoupdemo.R;
import com.example.binh.jsoupdemo.data.models.Match;
import com.example.binh.jsoupdemo.util.ImaPlayer;
import com.example.binh.jsoupdemo.util.QueryPreferences;
import com.example.binh.jsoupdemo.util.VideoController;
import com.google.android.libraries.mediaframework.exoplayerextensions.PlayerControlCallback;
import com.google.android.libraries.mediaframework.exoplayerextensions.Video;
import com.google.android.libraries.mediaframework.layeredvideo.PlaybackControlLayer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by binh on 5/20/16.
 */
public class MatchDetailActivity extends AppCompatActivity implements PlaybackControlLayer.FullscreenCallback{
    public static final String TAG = "MatchDetailActivity";
    public static final String EXTRA_MATCH = "EXTRA_MATCH";



    private String mMatchUrl;
    private String firstHalfURL;
    private String secondHalfURL;
    private TextView textMathTitle;
    private Match mMatch;

    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;
    private RelativeLayout mMatchDetailLayout;
    private FrameLayout mVideoPlayerContainer;
    private ImaPlayer mImaPlayer;

    public static Intent newIntent(Context context, Match match) {
        Intent intent = new Intent(context, MatchDetailActivity.class);
        intent.putExtra(EXTRA_MATCH, match);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);

        android.app.ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        mMatch= (Match) intent.getSerializableExtra(EXTRA_MATCH);
        textMathTitle = (TextView) findViewById(R.id.text_match_title);
        textMathTitle.setText(mMatch.getTitle());

        mMatchDetailLayout = (RelativeLayout) findViewById(R.id.match_detail);
        mVideoPlayerContainer = (FrameLayout) findViewById(R.id.video_frame);


        mMatchUrl = mMatch.getUrl();


        new FetchVideoLinksTask().execute();


        Button buttonPlayFirstHalf = (Button) findViewById(R.id.button_play_first_half);
        buttonPlayFirstHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playFirstHalf();
            }
        });

        Button buttonPlaySecondHalf = (Button) findViewById(R.id.button_play_second_half);
        buttonPlaySecondHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSecondHalf();
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (mImaPlayer != null) {
            mImaPlayer.release();
        }
        super.onDestroy();
    }

    private void playFirstHalf() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mImaPlayer != null) {
            mImaPlayer.release();
        }
        mVideoPlayerContainer.removeAllViews();
        Video video = new Video(firstHalfURL,
                Video.VideoType.MP4);
        mImaPlayer = new ImaPlayer(this, mVideoPlayerContainer, video, mMatch.getTitle(), null);
        mImaPlayer.setFullscreenCallback(this);
        mImaPlayer.play();
    }

    private void playSecondHalf() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mImaPlayer != null) {
            mImaPlayer.release();
        }

        mVideoPlayerContainer.removeAllViews();
        Video video = new Video(secondHalfURL, Video.VideoType.MP4);
        mImaPlayer = new ImaPlayer(this, mVideoPlayerContainer, video, mMatch.getTitle(), null);
        mImaPlayer.setFullscreenCallback(this);
        mImaPlayer.play();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGoToFullscreen() {
        mMatchDetailLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onReturnFromFullscreen() {
        mMatchDetailLayout.setVisibility(View.VISIBLE);
    }

    private class FetchVideoLinksTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MatchDetailActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(mMatchUrl).get();
                Elements scriptElements = document.select("div.learn-more-content>script");
                firstHalfURL = parseLink(scriptElements.first().html());
                secondHalfURL = parseLink(scriptElements.last().html());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            playFirstHalf();
        }

        private String parseLink(String html) {
            String urlPattern = "(http?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
            Pattern pattern = Pattern.compile(urlPattern);
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                return  matcher.group();
            }
            return null;
        }
    }

}

package com.example.binh.jsoupdemo.util;

import android.util.Log;

import com.example.binh.jsoupdemo.data.models.Match;
import com.example.binh.jsoupdemo.data.models.News;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by binh on 5/24/16.
 */
public class FootballFetcher {
    public static final String TAG = "FootballFetcher";

    public static final String Match_Url = "http://tructiepbongda.com/full-match";
    public static final String News_Url = "http://bongdaplus.vn/tin-tuc/anh/ngoai-hang-anh/";


    public List<Match> fetchRecentMatchItems() {
        List<Match> matches = new ArrayList<>();
        try {
            Document document = Jsoup.connect(Match_Url).get();
            Elements elements = document.select("div.thumbnail>a[href]");
            parseMatchItem(matches, elements);
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch match items", e);
        }
        return matches;
    }

    public List<Match> searchMatchItems(String query) {
        List<Match> matches = new ArrayList<>();
        try {
            Document document = Jsoup.connect(Match_Url).get();
            Elements elements = document.select("div.thumbnail>a[title*= " + query + "]");
            parseMatchItem(matches, elements);
        } catch (IOException e) {
            Log.e(TAG,"Failed to search match: ", e);
        }
        return matches;
    }

    private void parseMatchItem(List<Match> matches, Elements elements) {
        for (Element element : elements) {
            String title = element.attr("title");
            String href = element.attr("href");
            String timestamp = element.parent().parent().select("div.time").text();
            String imageUrl = element.select("img").attr("src");
            matches.add(new Match(title, href, timestamp, imageUrl));
        }
    }

    public List<News> fetchNewsItems() {
        List<News> newses = new ArrayList<>();
        try {
            Document document = Jsoup.connect(News_Url).get();
            parseNewsItem(newses, document);
        } catch (IOException e) {
            Log.e(TAG, "Failed to parse News item", e);
        }
        return newses;

    }

    private void parseNewsItem(List<News> newses, Document document) {
        Elements elements = document.select("div.nwslst>ul>li>a[href]");
        for (Element element : elements) {
            String href = element.attr("href");
            href = "http://bongdaplus.vn/" + href;

            String title = element.select("img").attr("alt");
            String desc = element.parent().ownText();
            String imageUrl =  element.select("img").attr("src");
            newses.add(new News(title,desc,imageUrl, href, "23/4/2016"));
        }
    }

}

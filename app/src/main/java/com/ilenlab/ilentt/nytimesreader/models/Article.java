package com.ilenlab.ilentt.nytimesreader.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by ADMIN on 3/18/2016.
 */
@Parcel
public class Article {

    public String webUrl;
    public HeadLine headline;
    public String snippet;

    public static final String NYT_URL = "http://nytimes.com/";
    public ArrayList<Media> multimedia;

    public Article() {
        multimedia = new ArrayList<>();
    }

    public boolean hasThumbnail() {
        return multimedia.size() > 0;
    }

    public String getThumbnail() {
        if(multimedia.size() > 0) {
            return NYT_URL + multimedia.get(0).url;
        } else {
            return "";
        }
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline.main;
    }

    public String getSnippet() {
        return snippet;
    }
}

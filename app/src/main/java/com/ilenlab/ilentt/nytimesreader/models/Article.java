package com.ilenlab.ilentt.nytimesreader.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by ADMIN on 3/18/2016.
 */
@Parcel
public class Article {

    public String webUrl;
    public String headLine;
    public String snippet;

    public final static String NYT_URL = "http://nytimes.com/";
    public ArrayList<String> media;

    public Article() {
        media = new ArrayList<>();
    }

    public boolean hasThumbnail() {
        return media.size() > 0;
    }

    public String getThumbnail() {
        if(media.size() > 0) {
            return NYT_URL + media.get(0);
        } else {
            return "";
        }
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getSnippet() {
        return snippet;
    }
}

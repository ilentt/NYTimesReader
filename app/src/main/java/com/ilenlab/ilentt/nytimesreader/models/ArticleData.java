package com.ilenlab.ilentt.nytimesreader.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ADMIN on 3/21/2016.
 */
public class ArticleData {
    @SerializedName("docs")
    public ArrayList<Article> articles;

    public ArticleData() {
        articles = new ArrayList<>();
    }
}

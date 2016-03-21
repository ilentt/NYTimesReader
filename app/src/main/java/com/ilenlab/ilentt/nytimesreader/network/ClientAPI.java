package com.ilenlab.ilentt.nytimesreader.network;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ilenlab.ilentt.nytimesreader.models.Article;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

/**
 * Created by ADMIN on 3/18/2016.
 */
public class ClientAPI {

    private static final String API_KEY = "9b6eba264e6565103841b71090219b99:8:74731185";
    private static final String URL_JSON = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

    private AsyncHttpClient client;
    private Context context;
    private Gson gson;

    public interface ArticlesHandle {
        public void onNewArticles(ArrayList<Article> articles);
    }

    public ClientAPI(Context context) {
        this.context = context;
        this.client = new AsyncHttpClient();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.create();
    }

    // access search api
    public void fetchPopularData() {

    }
}

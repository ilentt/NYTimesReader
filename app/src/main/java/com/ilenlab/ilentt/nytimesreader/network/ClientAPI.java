package com.ilenlab.ilentt.nytimesreader.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ilenlab.ilentt.nytimesreader.activities.ArticleSearchActivity;
import com.ilenlab.ilentt.nytimesreader.models.Article;
import com.ilenlab.ilentt.nytimesreader.models.ArticleData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ADMIN on 3/18/2016.
 */
public class ClientAPI {

    private static final String API_KEY = "9b6eba264e6565103841b71090219b99:8:74731185";
    private static final String API_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String INTERNET_FAILED_MESSAGE = "Cannot retrieve articles. Please check your Internet connection.";
    private static final String GET_DATA_FAILED_MESSAGE = "Unexpected issue while retrieving articles. Please try again later.";

    private AsyncHttpClient client;
    private Context context;
    Gson gson;

    public interface ArticlesHandle {
        public void onNewArticles(ArrayList<Article> articles);
    }

    public ClientAPI(Context context) {
        this.context = context;
        this.client = new AsyncHttpClient();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gson = gsonBuilder.create();
    }

    // access search api
    public void getArticles(String query, int page, Calendar beginDate,
                            ArticleSearchActivity.ArticleOrder order,
                            HashMap<ArticleSearchActivity.Categories, Boolean> categories,
                            final ClientAPI.ArticlesHandle articlesHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("api-key", API_KEY);
        requestParams.put("page", page);
        requestParams.put("q", query);

        if(!beginDate.equals(ArticleSearchActivity.OLDEST_DATE)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String string = dateFormat.format(beginDate.getTime());
            requestParams.put("begin_date", string);
        }

        if(order == ArticleSearchActivity.ArticleOrder.OLDEST) {
            requestParams.put("sort", "oldest");
        } else {
            requestParams.put("sort", "newest");
        }

        String catValue = "";
        for(Map.Entry<ArticleSearchActivity.Categories, Boolean> entry : categories.entrySet()) {
            ArticleSearchActivity.Categories catKey = entry.getKey();
            boolean value = entry.getValue();
            if(value) {
                catValue += '"' + catKey.toString() + "\" ";
            }
        }

        if(!TextUtils.isEmpty(catValue)) {
            catValue = "new_desk:(" + catValue + ")";
            requestParams.put("fq", catValue);
        }

        Log.d("DEBUG", requestParams.toString());

        if(!isNetworkAvailable() || !isOnline()) {
            Toast.makeText(context, INTERNET_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
            return;
        }

        client.get(API_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("response");
                    Log.d("DEBUG", jsonObject.toString());

                    ArticleData articleData = gson.fromJson(jsonObject.toString(), ArticleData.class);
                    articlesHandle.onNewArticles(articleData.articles);

                    Log.d("DEBUG", articleData.articles.toString());

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(context, GET_DATA_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}

package com.ilenlab.ilentt.nytimesreader.activities;

import android.support.v7.app.AppCompatActivity;

import com.ilenlab.ilentt.nytimesreader.models.Article;
import com.ilenlab.ilentt.nytimesreader.network.ClientAPI;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ADMIN on 3/18/2016.
 */
public class ArticleSearchActivity extends AppCompatActivity implements ClientAPI.ArticlesHandle {

    public enum ArticleOrder {
        NEWEST("NEWEST", 0),
        OLDEST("OLDEST", 1);

        private int index;
        private String strValue;

        ArticleOrder(String value, int i) {
            strValue = value;
            index = i;
        }

        @Override
        public String toString() {
            return strValue;
        }
    }

    public enum Section {
        ARTS("Arts"),
        FASHION("Fashion & Style"),
        SPORT("Sport");

        private String strValue;

        Section(String value) {
            strValue = value;
        }


        @Override
        public String toString() {
            return strValue;
        }
    }

    public static Calendar OLDEST_DATE = Calendar.getInstance();
    
    @Override
    public void onNewArticles(ArrayList<Article> articles) {

    }
}

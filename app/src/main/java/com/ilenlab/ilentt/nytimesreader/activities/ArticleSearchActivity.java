package com.ilenlab.ilentt.nytimesreader.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ilenlab.ilentt.nytimesreader.R;
import com.ilenlab.ilentt.nytimesreader.adapters.ArticleAdapter;
import com.ilenlab.ilentt.nytimesreader.models.Article;
import com.ilenlab.ilentt.nytimesreader.network.ClientAPI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;

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

    public enum ArticleCategories {
        ARTS("Arts"),
        FASHION("Fashion & Style"),
        SPORT("Sport");

        private String strValue;

        ArticleCategories(String value) {
            strValue = value;
        }


        @Override
        public String toString() {
            return strValue;
        }
    }

    public static Calendar OLDEST_DATE = Calendar.getInstance();

    ArrayList<Article> articles;
    ArticleAdapter articleAdapter;

    ClientAPI clientAPI;

    // Reducing View Boilerplate with Butterknife
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.rvArticles)
    RecyclerView rvArticles;

    @Bind(R.id.tvBeginDate)
    TextView tvBeginDate;

    @Bind(R.id.tvOrderBy)
    TextView tvOrderBy;

    String query =  "";
    Calendar beginDate = Calendar.getInstance();
    ArticleOrder articleOrder = ArticleOrder.NEWEST;
    HashMap<ArticleCategories, Boolean> categories = new HashMap<>();

    @Override
    public void onNewArticles(ArrayList<Article> articles) {

    }
}

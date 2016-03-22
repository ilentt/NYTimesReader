package com.ilenlab.ilentt.nytimesreader.activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.ilenlab.ilentt.nytimesreader.R;
import com.ilenlab.ilentt.nytimesreader.adapters.ArticleAdapter;
import com.ilenlab.ilentt.nytimesreader.fragments.ArticleDecoration;
import com.ilenlab.ilentt.nytimesreader.fragments.RecycleViewScrolling;
import com.ilenlab.ilentt.nytimesreader.models.Article;
import com.ilenlab.ilentt.nytimesreader.network.ClientAPI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    public enum Categories {
        ARTS("Arts"),
        FASHION("Fashion & Style"),
        SPORT("Sport");

        private String strValue;

        Categories(String value) {
            strValue = value;
        }


        @Override
        public String toString() {
            return strValue;
        }
    }

    public static Calendar OLDEST_DATE = Calendar.getInstance();
    public static final int BEGIN_YEAR = 1990;
    public static final int BEGIN_DAY = 1;

    // Reducing View Boilerplate with Butterknife
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.rvArticles)
    RecyclerView rvArticles;

    @Bind(R.id.tvBeginDate)
    TextView tvBeginDate;

    @Bind(R.id.tvOrderBy)
    TextView tvOrderBy;

    ArrayList<Article> articles;
    ArticleAdapter articleAdapter;

    ClientAPI clientAPI;

    String searchQuery =  "";
    Calendar beginDate = Calendar.getInstance();
    ArticleOrder articleOrder = ArticleOrder.NEWEST;
    HashMap<Categories, Boolean> categories = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        clientAPI = new ClientAPI(this);
        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(articles);

        OLDEST_DATE.set(BEGIN_YEAR, Calendar.JANUARY, BEGIN_DAY, 0, 0, 0);
        setBeginDate(BEGIN_YEAR, Calendar.JANUARY, BEGIN_DAY);
        setCategories(Categories.ARTS, false);
        setCategories(Categories.FASHION, false);
        setCategories(Categories.SPORT, false);

        // setup the recycle view
        rvArticles.setAdapter(articleAdapter);
        rvArticles.setHasFixedSize(true);
        rvArticles.addItemDecoration(new ArticleDecoration(10));
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(gridLayoutManager);

        rvArticles.addOnScrollListener(new RecycleViewScrolling(gridLayoutManager) {
            @Override
            public void onLoadMode(int page, int totalItem) {
                clientAPI.getArticles(searchQuery, page, beginDate, articleOrder, categories, ArticleSearchActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_articles_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                searchView.clearFocus();
                searchItem.collapseActionView();

                toolbar.setTitle(query);
                refreshArticle();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void refreshArticle() {
        articleAdapter.clean();
        clientAPI.getArticles(searchQuery, 0, beginDate, articleOrder, categories, ArticleSearchActivity.this);
    }

    @Override
    public void onNewArticles(ArrayList<Article> articles) {
        articleAdapter.addAll(articles);
    }

    private void setBeginDate(int day, int month, int year) {
        this.beginDate.set(day, month, year, 0, 0, 0);
        tvBeginDate.setText(String.format("%d/%d/%d", day, month + 1, year));
    }

    private void setArticleOrder(ArticleOrder articleOrder) {
        this.articleOrder = articleOrder;
    }

    private void setCategories(Categories key, boolean value) {
        this.categories.put(key, value);
    }
}

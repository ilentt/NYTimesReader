package com.ilenlab.ilentt.nytimesreader.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import butterknife.OnClick;

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
    @Bind(R.id.rvArticles)
    RecyclerView rvArticles;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tvBeginDate)
    TextView tvBeginDate;

    @Bind(R.id.tvOrderBy)
    TextView tvOrderBy;

    ArrayList<Article> articles;
    ArticleAdapter articleAdapter;

    ClientAPI clientAPI;

    String searchQuery = "";
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
        setBeginDate(BEGIN_DAY, Calendar.JANUARY, BEGIN_YEAR);
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

    @OnClick(R.id.filterDate)
    public void pickDate(LinearLayout layout) {
        int beginDay = beginDate.get(Calendar.DAY_OF_MONTH);
        int beginMonth = beginDate.get(Calendar.MONTH);
        int beginYear = beginDate.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setBeginDate(dayOfMonth, monthOfYear, year);
                refreshArticle();
            }
        }, beginDay, beginMonth, beginDay);
        datePickerDialog.getDatePicker().setMinDate(OLDEST_DATE.getTime().getTime());
        datePickerDialog.show();
    }

    @OnClick(R.id.filterCategories)
    public void pickSection(LinearLayout layout) {
        PopupMenu popupMenu = new PopupMenu(this, layout);
        popupMenu.getMenuInflater().inflate(R.menu.menu_categories, popupMenu.getMenu());
        popupMenu.getMenu().getItem(0).setChecked(categories.get(Categories.ARTS));
        popupMenu.getMenu().getItem(1).setChecked(categories.get(Categories.FASHION));
        popupMenu.getMenu().getItem(2).setChecked(categories.get(Categories.SPORT));

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mnuArts:
                        setCategories(Categories.ARTS, !categories.get(Categories.ARTS));
                        break;
                    case R.id.mnuFashion:
                        setCategories(Categories.FASHION, !categories.get(Categories.FASHION));
                        break;
                    case R.id.mnuSport:
                        setCategories(Categories.SPORT, !categories.get(Categories.SPORT));
                        break;
                }
                refreshArticle();
                return true;
            }

        });
        popupMenu.show();
    }

    @OnClick(R.id.filterOrder)
    public void pickOrder(LinearLayout layout) {
        PopupMenu popupMenu = new PopupMenu(this, layout);
        popupMenu.getMenuInflater().inflate(R.menu.menu_order, popupMenu.getMenu());
        popupMenu.getMenu().getItem(articleOrder.index).setChecked(true);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mnuOldest:
                        setArticleOrder(ArticleOrder.OLDEST);
                        break;
                    case  R.id.mnuNewest:
                        setArticleOrder(ArticleOrder.NEWEST);
                        break;
                }
                refreshArticle();
                return true;
            }
        });
        popupMenu.show();
    }
}
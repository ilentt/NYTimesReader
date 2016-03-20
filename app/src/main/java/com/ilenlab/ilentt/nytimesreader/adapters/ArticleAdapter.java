package com.ilenlab.ilentt.nytimesreader.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.ilenlab.ilentt.nytimesreader.models.Article;

import java.util.List;

/**
 * Created by ADMIN on 3/18/2016.
 */
public class ArticleAdapter extends ArrayAdapter{
    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }
}

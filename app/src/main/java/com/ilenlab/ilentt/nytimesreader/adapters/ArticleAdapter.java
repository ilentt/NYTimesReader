package com.ilenlab.ilentt.nytimesreader.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ilenlab.ilentt.nytimesreader.R;
import com.ilenlab.ilentt.nytimesreader.activities.ArticleDetailActivity;
import com.ilenlab.ilentt.nytimesreader.activities.ArticleSearchActivity;
import com.ilenlab.ilentt.nytimesreader.models.Article;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ADMIN on 3/18/2016.
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Article> articles;

    public enum ArticleType {
        IMAGE(0),
        TEXT(1);
        private int value;
        ArticleType(int val) {
            value = val;
        }
    }

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.ivThumbnail)
        ImageView ivThumbnail;

        @Bind(R.id.tvHeadline)
        TextView tvHeadLine;
        Context context;

        Article article;

        // Image ViewHolder
        public ImageViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        public void setArticle(Article article) {
            this.article = article;
            tvHeadLine.setText(article.getHeadLine());
            String thumbnail = article.getThumbnail();
            if(!TextUtils.isEmpty(thumbnail)) {
                Glide.with((ArticleSearchActivity) context).load(thumbnail).into(ivThumbnail);
            } else {
                ivThumbnail.setImageResource(0);
            }
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, ArticleDetailActivity.class);
            i.putExtra("article", Parcels.wrap(article));
            context.startActivity(i);
        }

    }

    // Text ViewHolder
    public static class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tvHeadline)
        TextView tvHeadline;

        @Bind(R.id.tvSnippet)
        TextView tvSnippet;
        Context context;

        Article article;

        public TextViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setArticle(Article article) {
            this.article = article;
            tvHeadline.setText(article.getHeadLine());
            tvSnippet.setText(article.getSnippet());
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, ArticleDetailActivity.class);
            i.putExtra("article", Parcels.wrap(article));
            context.startActivity(i);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int resource = 0;
        if(viewType == ArticleType.IMAGE.value) {
            resource = R.layout.item_image_article;
        } else {
            resource = R.layout.item_text_article;
        }

        RecyclerView.ViewHolder viewHolder;
        View articleView = inflater.inflate(resource, parent, false);
        if(viewType == ArticleType.IMAGE.value) {
            viewHolder = new ImageViewHolder(context, articleView);
        } else {
            viewHolder = new TextViewHolder(context, articleView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = articles.get(position);
        int viewType = holder.getItemViewType();
        if(viewType == ArticleType.IMAGE.value) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            imageViewHolder.setArticle(article);
        } else {
            TextViewHolder textViewHolder = (TextViewHolder) holder;
            textViewHolder.setArticle(article);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Article article = articles.get(position);
        if(article.hasThumbnail()) {
            return ArticleType.IMAGE.value;
        } else {
            return ArticleType.TEXT.value;
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void addAll(ArrayList<Article> articles) {
        for(Article article : articles) {
            this.articles.add(article);
            notifyItemInserted(this.articles.size() -1);
        }
    }

    public void clean() {
        int size = articles.size();
        articles.clear();
        notifyItemRangeRemoved(0, size);
    }
}

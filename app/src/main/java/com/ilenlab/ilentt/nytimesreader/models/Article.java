package com.ilenlab.ilentt.nytimesreader.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ADMIN on 3/18/2016.
 */
public class Article implements Parcelable{

    private String webUrl;
    private String headLine;
    private String thumbNail;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeString(this.headLine);
        dest.writeString(this.thumbNail);
    }

    public Article() {
    }

    protected Article(Parcel in) {
        this.webUrl = in.readString();
        this.headLine = in.readString();
        this.thumbNail = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}

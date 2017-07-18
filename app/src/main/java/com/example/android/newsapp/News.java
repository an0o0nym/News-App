package com.example.android.newsapp;

import java.util.Date;

/**
 * Created by an0o0nym on 18/07/17.
 */

public class News {
    private String title;
    private String category;
    private String url;
    private Date publishedDate = null;
    private String author = null;

    public News(String title, String category) {
        this.title = title;
        this.category = category;
    }

    public News(String title, String category, String url) {
        this.title = title;
        this.category = category;
        this.url = url;
    }

    public News(String title, String category, String url, Date publishedDate) {
        this.title = title;
        this.category = category;
        this.url = url;
        this.publishedDate = publishedDate;
    }

    public News(String title, String category, String url, Date publishedDate, String author) {
        this.title = title;
        this.category = category;
        this.url = url;
        this.publishedDate = publishedDate;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public Date getPublished() {
        return publishedDate;
    }

    public String getUrl() {
        return url;
    }

    public boolean hasPublishedDate() {
        return publishedDate != null;
    }

    public boolean hasAuthor() {
        return author != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nTitle: " + getTitle());
        sb.append("\nCategory: " + getCategory());
        if (this.hasAuthor()) {
            sb.append("\nAuthor: " + getAuthor());
        } else {
            sb.append("\nAuthor: Unknown");
        }
        if(this.hasPublishedDate()) {
            sb.append("\nPublishedDate: " + getPublished());
        } else {
            sb.append("\nPublishedDate: Unknown");
        }
        sb.append("\nURL: "  + getUrl());

        return sb.toString();
    }
}

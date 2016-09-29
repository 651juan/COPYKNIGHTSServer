package com.eu.article.bd;

/**
 * Created by Juan on 29/09/2016.
 */
public class Article {
    private String title;
    private String description;

    public Article() {}

    public Article(String t, String d) {
        this.title = t;
        this.description = d;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

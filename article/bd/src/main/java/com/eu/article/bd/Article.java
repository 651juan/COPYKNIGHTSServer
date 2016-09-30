package com.eu.article.bd;

/**
 * Created by Juan on 29/09/2016.
 */
public class Article {
    private String title;
    private String description;
    private int pageid;
    // When true artle only contains title and pageid
    private boolean shortArticle;

    public Article() {}

    public Article(String t, int p, String d, boolean s) {
        this.title = t;
        this.description = d;
        this.shortArticle = s;
        this.pageid = p;
    }

    public Article(String t, int p, boolean s) {
        this.title = t;
        this.pageid = p;
        this.shortArticle = s;
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

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public Boolean getShortArticle() {
        return shortArticle;
    }

    public void setShortArticle(Boolean shortArticle) {
        this.shortArticle = shortArticle;
    }
}

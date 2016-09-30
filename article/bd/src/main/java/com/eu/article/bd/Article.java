package com.eu.article.bd;

/**
 * Created by Juan on 29/09/2016.
 */
public class Article {
    private String title;
    private String description;
    private String pageid;
    // When true artle only contains title and pageid
    private boolean shortArticle;

    public Article() {}

    public Article(String t, String p, String d, boolean s) {
        this.title = t;
        this.description = d;
        this.shortArticle = s;
        this.pageid = p;
    }

    public Article(String t, String p, boolean s) {
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

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public Boolean getShortArticle() {
        return shortArticle;
    }

    public void setShortArticle(Boolean shortArticle) {
        this.shortArticle = shortArticle;
    }
}

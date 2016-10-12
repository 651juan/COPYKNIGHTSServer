package com.eu.wiki.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 01/10/2016.
 */
public class ArticleList {
    private List<Article> articleList;
    private String cmcontinue;
    private boolean aHasWarning;
    private String aWarning;

    public ArticleList() {
        this.articleList = new ArrayList<>();
    }

    public ArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public ArticleList(String warning) {
        this();
        this.aWarning = "WARNING: " + warning;
        this.aHasWarning = true;
    }
    public ArticleList(List<Article> articleList, String warning) {
        this(articleList);
        this.aWarning = "WARNING: " + warning;
        this.aHasWarning = true;
    }

    public List<Article> getArticles() {
        this.checkHasWarning();
        return articleList;
    }

    public void setArticles(List<Article> articleList) {
        this.checkHasWarning();
        this.articleList = articleList;
    }

    public String getCmContinue() {
        this.checkHasWarning();
        return cmcontinue;
    }

    public void setCmContinue(String cmcontinue) {
        this.checkHasWarning();
        this.cmcontinue = cmcontinue;
    }

    public boolean getHasWarning() {
        return this.aHasWarning;
    }

    public void setHasWarning(boolean warning) {
        this.aHasWarning = warning;
    }

    public String getWarning() {
        return this.aWarning;
    }

    public void setWarning(String warning) {
        this.aWarning = "WARNING: " + warning;
        this.aHasWarning = true;
    }

    public void disableWarning() {
        this.aHasWarning = false;
    }

    private void checkHasWarning() {
        if(this.aHasWarning) {
            throw new RuntimeException(this.aWarning);
        }
    }
}

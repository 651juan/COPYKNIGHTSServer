package com.eu.wiki.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 01/10/2016.
 */
public class QueryResult {
    public List<Article> pagesList;
    public String cmcontinue;
    private boolean aHasWarning;
    private String aWarning;

    public QueryResult() {
        this.pagesList = new ArrayList<>();
    }

    public QueryResult(List<Article> articleList) {
        this.pagesList = articleList;
    }

    public QueryResult(String warning) {
        this();
        this.aWarning = "WARNING: " + warning;
        this.aHasWarning = true;
    }
    public QueryResult(List<Article> articleList, String warning) {
        this(articleList);
        this.aWarning = "WARNING: " + warning;
        this.aHasWarning = true;
    }

    public List<Article> getPagesList() {
        this.checkHasWarning();
        return pagesList;
    }

    public void setPageList(List<Article> pageList) {
        this.checkHasWarning();
        this.pagesList = pageList;
    }

    public String getCmcontinue() {
        this.checkHasWarning();
        return cmcontinue;
    }

    public void setCmcontinue(String cmcontinue) {
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

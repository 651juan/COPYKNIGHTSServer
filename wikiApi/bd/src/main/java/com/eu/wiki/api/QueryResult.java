package com.eu.wiki.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 01/10/2016.
 */
public class QueryResult {
    public List<Article> pagesList;
    public String cmcontinue;

    public QueryResult() {
        this.pagesList = new ArrayList<>();
    }

    public List<Article> getPagesList() {
        return pagesList;
    }

    public void setPageList(List<Article> pageList) {
        this.pagesList = pageList;
    }

    public String getCmcontinue() {
        return cmcontinue;
    }

    public void setCmcontinue(String cmcontinue) {
        this.cmcontinue = cmcontinue;
    }
}

package com.eu.wiki.api;

import com.eu.wiki.api.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 01/10/2016.
 */
public class ArticleList {
    private List<Article> articles;
    private String cmContinue;

    public ArticleList() {
        articles = new ArrayList<>();
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getCmContinue() {
        return cmContinue;
    }

    public void setCmContinue(String cmContinue) {
        this.cmContinue = cmContinue;
    }
}

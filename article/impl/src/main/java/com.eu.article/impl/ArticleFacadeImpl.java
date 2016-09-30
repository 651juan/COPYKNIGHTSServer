package com.eu.article.impl;

import com.eu.article.bd.Article;
import com.eu.article.bd.ArticleFacade;
import info.bliki.api.Connector;
import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.api.query.Query;
import info.bliki.wiki.model.WikiModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan on 29/09/2016.
 */
public class ArticleFacadeImpl implements ArticleFacade {

    User user;

    public ArticleFacadeImpl(String username, String password, String url) {
        user = new User(username, password, url);
    }

    public List<Article> pagesToArticles(List<Page> pages){
        return pages.stream().map(x -> new Article(x.getTitle(), x.getCurrentContent(), false)).collect(Collectors.toList());
    }

    public List<Article> pagesToShortArticles(List<Page> pages){
        return pages.stream().map(x -> new Article(x.getTitle(), Integer.valueOf(x.getPageid()), true)).collect(Collectors.toList());
    }

    @Override
    public List<Article> getArticles(String articleTitle) {
        user.login();
        return pagesToArticles(user.queryContent(articleTitle));
    }

    @Override
    public List<Article> getArticlesById(String articleIds) {
        Query query = new Query();
        int[] pageIds = Arrays.stream(articleIds.split(","))
                .mapToInt(Integer::valueOf)
                .toArray();
        query.pageids(pageIds);
        Connector c = new Connector();
        return pagesToShortArticles(c.query(user,query));
    }
}

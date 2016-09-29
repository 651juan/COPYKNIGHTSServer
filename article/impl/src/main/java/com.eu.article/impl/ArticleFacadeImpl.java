package com.eu.article.impl;

import com.eu.article.bd.Article;
import com.eu.article.bd.ArticleFacade;
import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.wiki.model.WikiModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan on 29/09/2016.
 */
public class ArticleFacadeImpl implements ArticleFacade {

    public List<Article> pagesToArticles(List<Page> pages){
        return pages.stream().map(x -> new Article(x.getTitle(), x.getCurrentContent())).collect(Collectors.toList());
    }

    @Override
    public List<Article> getArticles(String articleTitle) {
        User user = new User("", "", "http://www.copyrightevidence.org/evidence-wiki/api.php");
        user.login();
        return pagesToArticles(user.queryContent(articleTitle));
    }
}

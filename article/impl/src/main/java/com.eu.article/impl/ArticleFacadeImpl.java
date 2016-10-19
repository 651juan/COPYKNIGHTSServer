package com.eu.article.impl;

import com.eu.article.bd.ArticleFacade;
import com.eu.wiki.api.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Juan on 29/09/2016.
 */
public class ArticleFacadeImpl implements ArticleFacade {

    String url;
    QueryCacheFacadeImpl facade;

    public ArticleFacadeImpl(String username, String password, String url, LocalDateTime expiry) {
        this.url = url;
        facade = new QueryCacheFacadeImpl(expiry,url);
    }

    @Override
    public ArticleList getArticlesByTitle(String articleTitles) {
        Query q = new Query();
        String decoded = null;
        try {
             decoded = URLDecoder.decode(articleTitles, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (decoded != null) {
            q.setTitles(Arrays.asList(decoded.replaceAll("_", " ").split(",")));
            return facade.query(q);
        }
        return null;
    }

    @Override
    public ArticleList getArticlesById(String articleIds) {
        Query query = new Query();
        List<Integer> pageIds = Arrays.stream(articleIds.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        query.setPageids(pageIds);
        return facade.query(query);
    }

    @Override
    public ArticleList getAllArticles() {
        Query q = new Query();
        q.setAll(true);
        return facade.query(q);
    }

    @Override
    public ArticleList getArticlesInYear(int year) {
        List<Article> allArticles =  this.getAllArticles().getArticles();
        List<Article> result = allArticles.stream().filter(article -> article.getIntYear() == year).collect(Collectors.toList());

        return new ArticleList(result);
    }

    @Override
    public Map<String, Integer> getArticleYearCount() {
        List<Article> allArticles =  this.getAllArticles().getArticles();
        Map<String, Integer> tmpResult = new HashMap<>();

        for(Article article : allArticles) {
            String tmpYear = article.getYear();

            incrementCountToMap(tmpResult, tmpYear);
        }

        return tmpResult;
    }

    private void incrementCountToMap(Map<String, Integer> map, String tmpValue) {
        if(map.containsKey(tmpValue)) {
            int tmp = map.get(tmpValue);
            tmp ++;
            map.put(tmpValue, tmp);
        }else{
            map.put(tmpValue,1);
        }
    }

    @Override
    public ArticleList getArticlesbyAuthor(String author) {
        List<Article> allArticles =  this.getAllArticles().getArticles();
        List<Article> result = allArticles.stream().filter(article -> Arrays.asList(article.getAuthors()).contains(author)
        ).collect(Collectors.toList());

        return new ArticleList(result);
    }

    @Override
    public Map<String, Integer> getArticleAuthorCount() {
        List<Article> allArticles =  this.getAllArticles().getArticles();
        Map<String, Integer> tmpResult = new HashMap<>();

        for(Article article : allArticles) {
            String[] tmpAuthors = article.getAuthors();
            for (String tmpAuthor : tmpAuthors) {
                incrementCountToMap(tmpResult, tmpAuthor);
            }
        }

        return tmpResult;
    }
}

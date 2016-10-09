package com.eu.article.impl;

import com.eu.article.bd.ArticleFacade;
import com.eu.wiki.api.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

    private ArticleList resultToArticles(QueryResult queryResult){
        //ArticleParser myParser = new ArticleParser();
        ArticleList list = new ArticleList();
        list.setArticles(queryResult.getPagesList());
        //list.setArticles(queryResult.getPagesList().stream().map(myParser::parse).collect(Collectors.toList()));
        if (queryResult.getCmcontinue() != null)
        list.setCmContinue(queryResult.getCmcontinue());
        return list;
    }

    @Override
    public ArticleList getArticles(String articleTitles) {
        Query q = new Query();
        q.setTitles(Arrays.asList(articleTitles.split(",")));
        return resultToArticles(facade.query(q));
    }

    @Override
    public ArticleList getArticlesById(String articleIds, boolean getContent) {
        Query query = new Query();
        List<Integer> pageIds = Arrays.stream(articleIds.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        query.setPageids(pageIds);
        QueryResult queryResult = facade.query(query);
        if (getContent) {
            return getFullArticles(queryResult);
        } else {
            return resultToArticles(queryResult);
        }
    }

    @Override
    public ArticleList getArticlesByCategory(String category, String cmContinue, int limit, boolean getContent) {
        Query query = new Query();
        query.setList("categorymembers");
        query.setCmtitle("Category:Studies");
        if (!cmContinue.equals("")) {
            query.setCmContinue(cmContinue);
        }
        query.setLimit(Integer.toString(limit));
        query.setNcontinue(true);
        QueryResult queryResult = facade.query(query);
        if (getContent) {
            return getFullArticles(queryResult);
        } else {
            return resultToArticles(queryResult);
        }
    }

    private ArticleList getFullArticles(QueryResult queryResult) {
        Query q = new Query();
        if (queryResult.getPagesList().size() > 0) {
            q.setPageids(queryResult.getPagesList().stream()
                    .map(Article::getPageid)
                    .collect(Collectors.toList()));
            return resultToArticles(facade.query(q));

        } else {
            return null;
        }
    }
}

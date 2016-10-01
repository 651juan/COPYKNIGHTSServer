package com.eu.article.impl;

import com.eu.article.bd.Article;
import com.eu.article.bd.ArticleFacade;
import com.eu.article.bd.ArticleList;
import info.bliki.api.Connector;
import info.bliki.api.Page;
import info.bliki.api.QueryResult;
import info.bliki.api.User;
import info.bliki.api.query.Query;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Juan on 29/09/2016.
 */
public class ArticleFacadeImpl implements ArticleFacade {

    String url;
    User user;

    public ArticleFacadeImpl(String username, String password, String url) {
        this.url = url;
        user = new User(username, password, url);
    }

    private ArticleList pagesToArticles(QueryResult queryResult){
        ArticleParser myParser = new ArticleParser();
        ArticleList list = new ArticleList();
        list.setArticles(queryResult.getPagesList().stream().map(myParser::parse).collect(Collectors.toList()));
        return list;
    }

    private ArticleList pagesToShortArticles(QueryResult queryResult){
        ArticleParser myParser = new ArticleParser();
        ArticleList list = new ArticleList();
        list.setArticles(queryResult.getPagesList().stream().map(myParser::parse).collect(Collectors.toList()));
        return list;
    }

    @Override
    public ArticleList getArticles(String articleTitles) {
        user.login();
        return pagesToArticles(user.queryContent(articleTitles));
    }

    @Override
    public ArticleList getArticlesById(String articleIds, boolean getContent) {
        Query query = new Query();
        int[] pageIds = Arrays.stream(articleIds.split(","))
                .mapToInt(Integer::valueOf)
                .toArray();
        query.pageids(pageIds);
        Connector c = new Connector();
        QueryResult queryResult = c.query(user, query);
        if (getContent) {
            return getFullArticles(queryResult);
        } else {
            return pagesToShortArticles(queryResult);
        }
    }

    @Override
    public ArticleList getArticlesByCategory(String category) {
        Query query = new Query();
        query.list("categorymembers");
        query.putPipedString("cmtitle", "Category:Studies");
        query.putPipedString("continue", "");
        Connector c = new Connector();
        return pagesToShortArticles(c.query(user,query));
    }

    private ArticleList getFullArticles(QueryResult queryResult) {
        return pagesToArticles(user.queryContent(queryResult.getPagesList().stream()
                .map(Page::getTitle)
                .collect(Collectors.toList())));
    }
}

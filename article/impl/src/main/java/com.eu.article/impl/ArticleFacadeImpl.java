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

    @Override
    public ArticleList getArticlesByTitle(String articleTitles) {
        Query q = new Query();
        q.setTitles(Arrays.asList(articleTitles.split(",")));
        return facade.query(q);
    }

    @Override
    public ArticleList getArticlesById(String articleIds, boolean getContent) {
        Query query = new Query();
        List<Integer> pageIds = Arrays.stream(articleIds.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        query.setPageids(pageIds);
        ArticleList queryResult = facade.query(query);
        if (getContent) {
            return getFullArticles(queryResult);
        } else {
            return queryResult;
        }
    }

    @Override
    public ArticleList getArticlesByCategory(String category, String cmContinue, int limit, boolean getContent) {
        Query query = new Query();
        query.setList("categorymembers");
        query.setCmtitle(category);
        if (!cmContinue.equals("")) {
            query.setCmContinue(cmContinue);
        }
        query.setLimit(Integer.toString(limit));
        query.setNcontinue(true);
        ArticleList articleList = facade.query(query);
        if (getContent) {
            return getFullArticles(articleList);
        } else {
            return articleList;
        }
    }

    @Override
    public ArticleList getAllArticles(boolean getContent) {
        ArticleList output = getArticlesByCategory("Category:Studies", "", 50, getContent);
        while(output.getCmContinue() != null) {
            ArticleList innerTemp = getArticlesByCategory("Category:Studies", output.getCmContinue(),50, getContent);
            output.getArticles().addAll(innerTemp.getArticles());
            output.setCmContinue(innerTemp.getCmContinue());
        }
        return output;
    }

    private ArticleList getFullArticles(ArticleList queryResult) {
        return getFullArticleInIncrements(queryResult);
    }

    private ArticleList getFullArticleInIncrements(ArticleList queryResult) {
        int size = queryResult.getArticles().size();
        ArticleList articleList = new ArticleList();
        if (size > 0) {
            for (int i = 0; i < Math.ceil((double) size / 50); i++) {
                Query q = new Query();
                q.setPageids(queryResult.getArticles().stream()
                        .skip(i * 50)
                        .limit(50)
                        .map(Article::getPageid)
                        .collect(Collectors.toList()));

                ArticleList tempArticle = facade.query(q);
                articleList.getArticles().addAll(tempArticle.getArticles());
            }
            if (queryResult.getCmContinue() != null) {
                articleList.setCmContinue(queryResult.getCmContinue());
            }
        }
        return articleList;
    }
}

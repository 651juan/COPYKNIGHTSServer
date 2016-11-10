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
    public ArticleList getArticlesInIndustry(String industry) {
        List<Article> allArticles =  this.getAllArticles().getArticles();
        List<Article> result = allArticles.stream().filter(article ->
        {
            if (article.getDatasets() != null) {
                if (industry.equals( "null")) {
                    return Arrays.asList(article.getDatasets().getIndustry()).contains("");
                } else{
                    return Arrays.asList(article.getDatasets().getIndustry()).contains(industry);
                }
            } else {
                if (industry.equals("null")) return true;
            }
            return false;
        }).collect(Collectors.toList());

        return new ArticleList(result);
    }

    @Override
    public ArticleList getArticlesInFundamental(String fundamental) {
        List<Article> allArticles =  this.getAllArticles().getArticles();
        List<Article> result = allArticles.stream().filter(article -> Arrays.asList(article.getFundamentalIssues())
                .contains(FundamentalIssue.valueOf(fundamental)))
                .collect(Collectors.toList());

        return new ArticleList(result);
    }

    @Override
    public ArticleList getArticlesByEvidenceBasedPolicy(String evidence) {
        List<Article> allArticles =  this.getAllArticles().getArticles();
        List<Article> result = allArticles.stream().filter(article -> Arrays.asList(article.getEvidenceBasedPolicies())
                .contains(EvidenceBasedPolicy.valueOf(evidence)))
                .collect(Collectors.toList());
        return new ArticleList(result);
    }

    @Override
    public ArticleList getArticlesByCountry(String country) {
        List<Article> allArticles =  this.getAllArticles().getArticles();
        List<Article> result = allArticles.stream().filter(article ->
        {
            if (article.getDatasets() != null) {
                if (country.equals( "null")) {
                    return Arrays.asList(article.getDatasets().getCountries()).contains("");
                } else{
                    return Arrays.asList(article.getDatasets().getCountries()).contains(country);
                }
            } else {
                if (country.equals("null")) return true;
            }
            return false;
        }).collect(Collectors.toList());

        return new ArticleList(result);
    }

    @Override
    public Map<String, Integer> getArticleYearCount() {
        return facade.getYearCount();
    }

    @Override
    public ArticleList getArticlesByAuthor(String author) {
        List<Article> allArticles =  this.getAllArticles().getArticles();
        List<Article> result = allArticles.stream().filter(article -> Arrays.asList(article.getAuthors()).contains(author)
        ).collect(Collectors.toList());

        return new ArticleList(result);
    }

    @Override
    public Map<String, Integer> getArticleAuthorCount() {
        return facade.getAuthorCount();
    }

    @Override
    public Map<String, Integer> getArticleIndustryCount() {
        return facade.getIndustryCount();
    }

    @Override
    public Map<String, Integer> getArticleFundamentalIssueCount() {
        return facade.getFundamentalIssueCount();
    }

    @Override
    public ArticleList getSimilarArticles(int pageID, double threshold) {
        List<Article> allArticles =  this.getAllArticles().getArticles();
        Article toCompare = null;

        //Get the article to compare with the provided pageID
        for(Article article : allArticles) {
            if(article.getPageid() == pageID) {
                toCompare = article;
                break;
            }
        }

        return this.getSimilarArticles(toCompare, threshold);
    }

    @Override
    public ArticleList getSimilarArticles(Article toComapre, double threshold) {
        List<Article> tmpResult = new ArrayList<>();
        List<Article> allArticles =  this.getAllArticles().getArticles();

        for(Article article : allArticles) {
            if(!article.equals(toComapre)) {
                double similarity = toComapre.getSimilatiry(article);
                if (similarity >= threshold) {
                    tmpResult.add(article);
                }
            }
        }

        return new ArticleList(tmpResult);
    }

    @Override
    public Map<String, Integer> getArticleEvidenceBasedPolicyCount() {
        return facade.getEvidenceBasedPolicyCount();
    }

    @Override
    public Map<String, Integer> getArticleCountryCount() {
        return facade.getCountryCount();
    }
}

package com.eu.wiki.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Juan on 08/10/2016.
 */
public class QueryCacheFacadeImpl implements QueryCacheFacade {
    private HashMap<Integer, Article> cache;
    private LocalDateTime  lastUpdated;
    private LocalDateTime expiryPeriod;
    private QueryFacade queryFacade;

    public QueryCacheFacadeImpl(LocalDateTime expiryPeriod, String url) {
        lastUpdated = LocalDateTime.MIN;
        this.expiryPeriod = expiryPeriod;
        this.queryFacade = new QueryFacadeImpl(url);
        this.cache = new HashMap<>();
        initialiseCache();
    }

    @Override
    public ArticleList query(Query q) {
        if (checkExpiry())  {
            initialiseCache();
        }
        ArticleList list = new ArticleList();
        if (!q.getPageids().isEmpty()) {
            list.setArticles(q.getPageids().stream()
                    .map(x -> cache.get(x))
                    .collect(Collectors.toList())
            );
        } else if (q.isAll()) {
            list.setArticles(
                    cache.entrySet()
                            .stream()
                            .map(Map.Entry::getValue)
                            .collect(Collectors.toList())
            );
        } else if (!q.getTitles().isEmpty()) {
            list.setArticles(
                    cache.entrySet()
                            .stream()
                            .map(Map.Entry::getValue)
                            .filter(x -> q.getTitles().contains(x.getName()))
                            .collect(Collectors.toList())
            );
        }
        return list;
    }

    /**
     * Checks if the cache has expired by adding the current time with the expiry period.
     * @return true if expired, false otherwise.
     */
    private boolean checkExpiry() {
        LocalDateTime addition = lastUpdated.plusSeconds(expiryPeriod.getSecond());
        addition = addition.plusMinutes(expiryPeriod.getMinute());
        addition = addition.plusHours(expiryPeriod.getHour());
        addition = addition.plusDays(expiryPeriod.getDayOfYear() - addition.getDayOfYear());
        addition = addition.plusYears(expiryPeriod.getYear() - addition.getYear());
        return LocalDateTime.now().isAfter(addition);
    }

    /**
     * Gets all articles from the wiki and stores them in the cache.
     */
    private void initialiseCache() {
        lastUpdated = LocalDateTime.now();
        Query query = generateInitialQuery("");
        ArticleList initialList = queryFacade.query(query);
        ArticleList output = queryFacade.query(generateFullArticleQuery(initialList));
        output.setCmContinue(initialList.getCmContinue());
        while(output.getCmContinue() != null && !output.getCmContinue().equals("")) {
            ArticleList categoryResult = queryFacade.query(generateInitialQuery(output.getCmContinue()));
            ArticleList FullResult = queryFacade.query(generateFullArticleQuery(categoryResult));
            output.getArticles().addAll(FullResult.getArticles());
            output.setCmContinue(categoryResult.getCmContinue());
        }
        cache.putAll(output.getArticles().stream()
                .collect(Collectors.toMap(Article::getPageid, Function.identity())));
    }

    /**
     * Generates a query to be used to obtain all studies.
     * @param cmcontinue the continue string to be added to the query.
     * @return the Query with the continue string.
     */
    private Query generateInitialQuery(String cmcontinue) {
        Query query = new Query();
        query.setList("categorymembers");
        query.setCmtitle("Category:Studies");
        query.setLimit(Integer.toString(50));
        query.setNcontinue(true);
        query.setCmContinue(cmcontinue);
        return query;
    }

    /**
     * Generates a query with the page ids from the article list to get the full article from the short article obtained.
     * @param articleList the short articles obtained.
     * @return the query with the full @{@link Article}(s)
     */
    private Query generateFullArticleQuery(ArticleList articleList) {
        Query q = new Query();
        q.setPageids(articleList.getArticles().stream()
                .map(Article::getPageid)
                .collect(Collectors.toList()));
        return q;
    }
 }

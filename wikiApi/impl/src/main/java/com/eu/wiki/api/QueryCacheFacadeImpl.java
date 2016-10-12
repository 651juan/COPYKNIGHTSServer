package com.eu.wiki.api;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Created by Juan on 08/10/2016.
 */
public class QueryCacheFacadeImpl implements QueryCacheFacade {
    private HashMap<String, ArticleList> currentResult;
    private LocalDateTime  lastUpdated;
    private LocalDateTime expiryPeriod;
    private QueryFacade queryFacade;

    public QueryCacheFacadeImpl(LocalDateTime expiryPeriod, String url) {
        currentResult = new HashMap<>();
        lastUpdated = LocalDateTime.MIN;
        this.expiryPeriod = expiryPeriod;
        this.queryFacade = new QueryFacadeImpl(url);
    }

    @Override
    public ArticleList query(Query q) {
        String url = queryFacade.generateUrl(q);
        if (currentResult.get(url) == null || this.checkExpiry()) {
            getAndUpdate(q);
        }
        return currentResult.get(url);
    }

    private void getAndUpdate(Query q) {
        currentResult.put(queryFacade.generateUrl(q), queryFacade.query(q));
        lastUpdated = LocalDateTime.now();
    }

    private boolean checkExpiry() {
        LocalDateTime addition = lastUpdated.plusSeconds(expiryPeriod.getSecond());
        addition = addition.plusMinutes(expiryPeriod.getMinute());
        addition = addition.plusHours(expiryPeriod.getHour());
        addition = addition.plusDays(expiryPeriod.getDayOfYear() - addition.getDayOfYear());
        addition = addition.plusYears(expiryPeriod.getYear() - addition.getYear());
        return LocalDateTime.now().isAfter(addition);
    }
}

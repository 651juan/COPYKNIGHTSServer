package com.eu.wiki.api;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Created by Juan on 08/10/2016.
 */
public class QueryCacheFacadeImpl implements QueryCacheFacade {
    private HashMap<String, QueryResult> currentResult;
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
    public QueryResult query(Query q) {
        if (currentResult.get(queryFacade.generateUrl(q)) == null || this.checkExpiry()) {
            getAndUpdate(q);
        } else {
            return currentResult.get(queryFacade.generateUrl(q));
        }
        return currentResult.get(queryFacade.generateUrl(q));
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

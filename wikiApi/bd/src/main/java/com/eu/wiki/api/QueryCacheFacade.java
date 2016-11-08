package com.eu.wiki.api;

import java.util.Map;

/**
 * This Facade manages the caching of Articles. On initialisation the cache updates to by obtaining all studies from the
 * wiki and parses them. The cache is valid for 1 Day.
 *
 * Created by Juan on 08/10/2016.
 */
interface QueryCacheFacade {

    /**
     * It the cache has expired it queries the wiki else the query is retrieved from cache.
     * @param q the @{@link Query} request.
     * @return The List of @{@link Article}(s) that match the query.
     */
    ArticleList query(Query q);

    /**
     * Returns a @{@link Map} Containing the year as a key and the count
     * of the articles that where published in that year in as values. If the cache is not expired
     * it returns the cache else it will refresh the cache before.
     * @return a @{@link Map}
     */
    Map<String, Integer> getYearCount();

    /**
     * Returns a @{@link Map} Containing the author as a key and the count
     * of the articles that where published by the author as values. If the cache is not expired
     * it returns the cache else it will refresh the cache before.
     * @return a @{@link Map}
     */
    Map<String, Integer> getAuthorCount();

    /**
     * Returns a @{@link Map} Containing the industry as a key and the count
     * of the articles that are related in that industry. If the cache is not expired
     * it returns the cache else it will refresh the cache before.
     * @return a @{@link Map}
     */
    Map<String, Integer> getIndustryCount();

    /**
     * Returns a @{@link Map} Containing the @{@link FundamentalIssue} as a key and the count
     * of the articles that are related to that issue. If the cache is not expired
     * it returns the cache else it will refresh the cache before.
     * @return a @{@link Map}
     */
    Map<String, Integer> getFundamentalIssueCount();

}

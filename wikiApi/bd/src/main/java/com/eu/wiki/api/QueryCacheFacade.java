package com.eu.wiki.api;

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
}

package com.eu.wiki.api;

/**
 * Created by Juan on 08/10/2016.
 */
interface QueryCacheFacade {

    ArticleList query(Query q);
}

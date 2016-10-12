package com.eu.wiki.api;

/**
 * A facade that enables direct request to the wiki server.
 *
 * Created by Juan on 01/10/2016.
 */
public interface QueryFacade {
    String TITLE_ID = "titles=";
    String PAGES_ID = "pageids=";
    String LIST_ID = "list=";
    String CMTITLE_ID = "cmtitle=";
    String CMCONTINUE_ID = "cmcontinue=";
    String LIMIT_ID = "cmlimit=";
    String FORMAT_ID = "format=";
    String SORT_ID = "cmsort=";
    String DIR_ID = "cmdir=";
    String CONTINUE_ID = "continue=";

    /**
     * Queries the wiki by generating a url from the @{@link Query} and sending a request to the wiki.
     * @param q the query to execute.
     * @return the Article list obtained from the wiki
     */
    ArticleList query(Query q);

    /**
     * Generates the url utilising the base Url passed on initialisation and the @{@link Query} passed
     * @param q contains the data required to generate the url request.
     * @return the generated url as per media wiki api framework.
     */
    String generateUrl(Query q);

}

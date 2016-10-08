package com.eu.wiki.api;

/**
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

    QueryResult query(Query q);

    String generateUrl(Query q);

}

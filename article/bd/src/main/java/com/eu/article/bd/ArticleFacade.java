package com.eu.article.bd;

import info.bliki.api.Page;
import java.util.List;

/**
 * Created by Juan on 29/09/2016.
 */
public interface ArticleFacade {
    String getRawArticle(String url);
    List<Page> getPages(String articleTitle);
    Article pageToArticle(Page page);
}

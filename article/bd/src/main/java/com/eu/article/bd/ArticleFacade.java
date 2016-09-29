package com.eu.article.bd;

import info.bliki.api.Page;
import java.util.List;

/**
 * Facade to expose Articles.
 * Created by Juan on 29/09/2016.
 */
public interface ArticleFacade {
    /**
     * Gets a @{@link List} of @{@link Article} by searching the titles.
     * @param articleTitle @{@link List} of page titles as @{@link String}
     * @return @{@link List} of @{@link Article}
     */
    public List<Article> getArticles(String articleTitle);

}

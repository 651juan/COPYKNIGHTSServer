package com.eu.article.bd;

import java.util.List;

/**
 * Facade to expose Articles.
 * Created by Juan on 29/09/2016.
 */
public interface ArticleFacade {
    /**
     * Gets a @{@link List} of @{@link Article} incl. description, by searching the titles.
     * @param articleTitles @{@link List} of page titles as a comma separated @{@link String}
     * @return @{@link List} of @{@link Article}
     */
    public List<Article> getArticles(String articleTitles);

    /**
     * Gets a @{@link List} of short @{@link Article} not including description. by searching for ids.
     * @param articleIds @{@link List} of page ids as a comma separated @{@link String}
     * @param getContent shows if you want the content of the article
     * @return @{@link List} of @{@link Article}
     */
    public List<Article> getArticlesById(String articleIds, boolean getContent);

    /**
     * Gets a @{@link List} of @{@link Article}  by searching for categories.
     * @param categories @{@link List} of categories as a comma separated @{@link String}
     * @return @{@link List} of @{@link Article}
     */
    public List<Article> getArticlesByCategory(String categories);
}

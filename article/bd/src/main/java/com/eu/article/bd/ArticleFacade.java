package com.eu.article.bd;
import java.util.List;
import java.util.Map;

import com.eu.wiki.api.ArticleList;

/**
 * Facade to expose Articles.
 * Created by Juan on 29/09/2016.
 */
public interface ArticleFacade {
    /**
     * Gets a @{@link List} of @{@link com.eu.wiki.api.Article} incl. description, by searching the titles.
     * @param articleTitles @{@link List} of page titles as a comma separated @{@link String}
     * @return {@link ArticleList} of matching data.
     */
    ArticleList getArticlesByTitle(String articleTitles);

    /**
     * Gets a @{@link List} of short @{@link com.eu.wiki.api.Article} not including description. by searching for ids.
     * @param articleIds @{@link List} of page ids as a comma separated @{@link String}
     * @return {@link ArticleList} of matching data.
     */
    ArticleList getArticlesById(String articleIds);

    /**
     * Obtains all the articles in a the category "Category:Studies"
     * @return {@link ArticleList}
     */
    ArticleList getAllArticles();

    /**
     * Returns an {@link ArticleList} containing all the articles published in a given year
     * @param year the year to query
     * @return An {@link ArticleList} containing all the articles of a given year
     */
    ArticleList getArticlesInYear(int year);

    /**
     * Returns a @{@link Map} Containing the year as a key and the count
     * of the articles that where published in that year in as values
     * @return a @{@link Map}
     */
    Map<String, Integer> getArticleYearCount();

    /**
     * Returns an {@link ArticleList} containing all the articles published by the author.
     * @param author the author to query
     * @return An {@link ArticleList} containing all the articles of a given year
     */
    ArticleList getArticlesbyAuthor(String author);

    /**
     * Returns a @{@link Map} Containing the author as a key and the count
     * of the articles that where published in that year in as values
     * @return a @{@link Map}
     */
    Map<String, Integer> getArticleAuthorCount();

}

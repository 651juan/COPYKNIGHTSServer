package com.eu.article.bd;
import java.util.List;
import com.eu.wiki.api.ArticleList;

/**
 * Facade to expose Articles.
 * Created by Juan on 29/09/2016.
 */
public interface ArticleFacade {
    /**
     * Gets a @{@link List} of @{@link com.eu.wiki.api.Article} incl. description, by searching the titles.
     * @param articleTitles @{@link List} of page titles as a comma separated @{@link String}
     * @return @{@link List} of @{@link com.eu.wiki.api.Article}
     */
    public ArticleList getArticles(String articleTitles);

    /**
     * Gets a @{@link List} of short @{@link com.eu.wiki.api.Article} not including description. by searching for ids.
     * @param articleIds @{@link List} of page ids as a comma separated @{@link String}
     * @param getContent shows if you want the content of the article
     * @return @{@link List} of @{@link com.eu.wiki.api.Article}
     */
    public ArticleList getArticlesById(String articleIds, boolean getContent);

    /**
     * Gets a @{@link List} of @{@link com.eu.wiki.api.Article}  by searching for categories.
     * @param categories @{@link List} of categories as a comma separated @{@link String}
     * @param cmContinue a String with the data required for pagination.
     * @param limit number of pages to return.
     *@param getContent shows if you want the content of the article
     * @return @{@link List} of @{@link com.eu.wiki.api.Article}
     */
    public ArticleList getArticlesByCategory(String categories, String cmContinue, int limit, boolean getContent);
}

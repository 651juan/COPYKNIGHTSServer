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
}

package com.eu.article.bd;
import java.util.List;
import java.util.Map;

import com.eu.wiki.api.Article;
import com.eu.wiki.api.ArticleList;

/**
 * Facade to expose Articles.
 * Created by Juan on 29/09/2016.
 */
public interface ArticleFacade {
    /**
     * Gets a {@link List} of {@link com.eu.wiki.api.Article} incl. description, by searching the titles.
     * @param articleTitles {@link List} of page titles as a comma separated {@link String}
     * @return {@link ArticleList} of matching data.
     */
    ArticleList getArticlesByTitle(String articleTitles);

    /**
     * Gets a {@link List} of short {@link com.eu.wiki.api.Article} not including description. by searching for ids.
     * @param articleIds {@link List} of page ids as a comma separated {@link String}
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
     * Returns an {@link ArticleList} containing all the articles with a dataset published in an industry
     * @param industry the industry to query
     * @return An {@link ArticleList} containing all the articles with a dataset published in an industry
     */
    ArticleList getArticlesInIndustry(String industry);

    /**
     * Returns an {@link ArticleList} containing all the articles published in an fundamental issue.
     * @param fundamental the fundamental issue to query
     * @return An {@link ArticleList} containing all the articles published in an fundamental issue
     */
    ArticleList getArticlesInFundamental(String fundamental);

    /**
     * Returns an {@link ArticleList} containing all the articles published that are using the {@link com.eu.wiki.api.EvidenceBasedPolicy}.
     * @param evidence The Evidence Based Policy
     * @return An {@link ArticleList} containing all the articles published using the Evidence based policy.
     */
    ArticleList getArticlesByEvidenceBasedPolicy(String evidence);

    /**
     * Returns an {@link ArticleList} containing all the articles published using a dataset from the country.
     * @param country the country
     * @return An {@link ArticleList} containing all the articles published using the country.
     */
    ArticleList getArticlesByCountry(String country);


    /**
     * Returns a {@link Map} Containing the year as a key and the count
     * of the articles that where published in that year in as values
     * @return a {@link Map}
     */
    Map<String, Integer> getArticleYearCount();

    /**
     * Returns an {@link ArticleList} containing all the articles published by the author.
     * @param author the author to query
     * @return An {@link ArticleList} containing all the articles of a given year
     */
    ArticleList getArticlesByAuthor(String author);

    /**
     * Returns an {@link ArticleList} containing all the articles in the keyword.
     * @param word the key word to get.
     * @return An {@link ArticleList} containing all the articles containing the keyword.
     */
    ArticleList getArticlesByKeyword(String word);

    /**
     * Returns a {@link Map} Containing the author as a key and the count
     * of the articles that where published by that Author as values
     * @return a {@link Map}
     */
    Map<String, Integer> getArticleAuthorCount();

    /**
     * Returns {@link Map} Containing the {@link com.eu.wiki.api.Industry} as a key and the count of
     * the articles of that {@link com.eu.wiki.api.Industry}.
     * @return a {@link Map}
     */
    Map<String, Integer> getArticleIndustryCount();

    /**
     * Returns {@link Map} Containing the {@link com.eu.wiki.api.FundamentalIssue} as a key and the count of
     * the articles of that {@link com.eu.wiki.api.FundamentalIssue}.
     * @return a {@link Map}
     */
    Map<String, Integer> getArticleFundamentalIssueCount();

    /**
     * Returns all the similar articles to the article with the provided id
     * @param pageID The page id of the article to find similar articles to
     * @param threshold threshold The similarity threshold
     * @return a {@link ArticleList}
     */
    ArticleList getSimilarArticles(int pageID, double threshold);

    /**
     * Returns all the similar articles to the provided articles
     * @param toComapre The article to find similar articles to
     * @param threshold The similarity threshold
     * @return a {@link ArticleList}
     */
    ArticleList getSimilarArticles(Article toComapre, double threshold);
    /**
     * Return a {@link Map} Containing the {@link com.eu.wiki.api.EvidenceBasedPolicy} as a key and the count of the
     * articles of that {@link com.eu.wiki.api.EvidenceBasedPolicy}
     * @return a {@link Map}
     */
    Map<String, Integer> getArticleEvidenceBasedPolicyCount();

    /**
     * Return a {@link Map} Containing the Country as a key and the count of the
     * articles of that Country
     * @return a {@link Map}
     */
    Map<String, Integer> getArticleCountryCount();
}

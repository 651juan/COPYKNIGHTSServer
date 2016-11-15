package com.eu.article.web;

import com.eu.article.bd.ArticleFacade;
import com.eu.article.impl.ArticleFacadeImpl;
import com.eu.wiki.api.ArticleList;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

/**
 * The controller for requests relating to the parsing of Articles.
 *
 * Created by Juan on 29/09/2016.
 */
@RestController
public class ArticleController {

    private static final String URL_TO_WIKI = "http://www.copyrightevidence.org/evidence-wiki/api.php";
    private static final LocalDateTime expiry = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.now());
    private ArticleFacade facade;

    /**
     * Look at {@link ArticleFacade#getAllArticles()}
     * @return An {@link ArticleList} of all the articles in the system.
     */
    @RequestMapping(value="/article")
    public ArticleList allArticles() {
        initialiseFacade();
        return facade.getAllArticles();
    }

    /**
     * Look at {@link ArticleFacade#getArticlesById(String)}
     * @param ids a list of id's separated by commas (,).
     * @return an {@link ArticleList} filled with the requested id's.
     */
    @RequestMapping(value="/article", params={"pageids"})
    public ArticleList getArticleById(@RequestParam("pageids") String ids) {
        initialiseFacade();
        return facade.getArticlesById(ids);
    }

    /**
     * Look at {@link ArticleFacade#getArticlesByTitle(String)}
     * @param titles a list of titles separated by a comma (,).
     * @return an {@link ArticleList} filled with the articles requested.
     */
    @RequestMapping(value="/article", params="title")
    public ArticleList getArticleByTitle(@RequestParam("title") String titles) {
        initialiseFacade();
        return facade.getArticlesByTitle(titles);
    }

    /**
     * Look at {@link ArticleFacade#getArticlesByAuthor(String)}
     * @param author the author that need to be fetched.
     * @return An {@link ArticleList} filled with the articles written by that author.
     */
    @RequestMapping(value = "/author/{authorToGet}", method=RequestMethod.GET)
    public ArticleList getArticleByAuthor(@PathVariable("authorToGet") String author) {
        initialiseFacade();
        return facade.getArticlesByAuthor(author);
    }

    /**
     * Look at {@link ArticleFacade#getArticlesByCountry(String)}}
     * @param country the country to fetch.
     * @return An {@link ArticleList} filled with the articles that data was gathered in that country.
     */
    @RequestMapping(value = "/country/{countryToGet}", method=RequestMethod.GET)
    public ArticleList getArticleByCountry(@PathVariable("countryToGet") String country) {
        initialiseFacade();
        return facade.getArticlesByCountry(country);
    }

    /**
     * Look at {@link ArticleFacade#getArticlesInYear(int)}
     * @param year The year to fetch
     * @return An {@link ArticleList} filled with the articles that was done during that year.
     */
    @RequestMapping(value = "/year/{yearToGet}", method=RequestMethod.GET)
    public ArticleList getArticleByYear(@PathVariable("yearToGet") int year) {
        initialiseFacade();
        return facade.getArticlesInYear(year);
    }

    /**
     * Look at {@link ArticleFacade#getArticlesInFundamental(String)}
     * @param fundamental a {@link com.eu.wiki.api.FundamentalIssue} Key.
     * @return An {@link ArticleList} that contains the fundamental issue provided.
     */
    @RequestMapping(value = "/fundamental/{fundamentalToGet}", method=RequestMethod.GET)
    public ArticleList getArticleByFundamental(@PathVariable("fundamentalToGet") String fundamental) {
        initialiseFacade();
        return facade.getArticlesInFundamental(fundamental);
    }

    /**
     * Look at @{@link ArticleFacade#getArticlesByEvidenceBasedPolicy(String)}
     * @param evidence a {@link com.eu.wiki.api.EvidenceBasedPolicy} Key.
     * @return An {@link ArticleList} that contains evidence based policy provided.
     */
    @RequestMapping(value = "/evidence/{evidenceToGet}", method=RequestMethod.GET)
    public ArticleList getArticleByEvidence(@PathVariable("evidenceToGet") String evidence) {
        initialiseFacade();
        return facade.getArticlesByEvidenceBasedPolicy(evidence);
    }

    /**
     * Look at @{@link ArticleFacade#getArticlesInIndustry(String)}
     * @param industry a {@link com.eu.wiki.api.Industry} Key.
     * @return An {@link ArticleList} that contains Articles with the industry provided.
     */
    @RequestMapping(value = "/industry/{industryToGet}", method=RequestMethod.GET)
    public ArticleList getArticlesInIndustry(@PathVariable("industryToGet") String industry) {
        initialiseFacade();
        return facade.getArticlesInIndustry(industry);
    }

    /**
     * Look at {@link ArticleFacade#getArticlesByKeyword(String)}
     * @param keyWord a keyword Key.
     * @return An {@link ArticleList} that contains Articles with the industry provided.
     */
    @RequestMapping(value = "/wordcloud/{keyWordToGet}", method=RequestMethod.GET)
    public ArticleList getArticlesByKeyWord(@PathVariable("keyWordToGet") String keyWord) {
        initialiseFacade();
        return facade.getArticlesByKeyword(keyWord);
    }

    /**
     * Look at @{@link ArticleFacade#getArticleAuthorCount()}
     * @return A {@link Map} of authors as a key and the count as a value.
     */
    @RequestMapping(value = "/author")
    public Map<String, Integer> getArticleAuthorCount() {
        initialiseFacade();
        return facade.getArticleAuthorCount();
    }

    /**
     * Look at {@link ArticleFacade#getArticleCountryCount()}
     * @return A {@link Map} of countries as a key and the count as a value.
     */
    @RequestMapping(value = "/country")
    public Map<String, Integer> getCountryCount() {
        initialiseFacade();
        return facade.getArticleCountryCount();
    }

    /**
     * Look at {@link ArticleFacade#getArticleYearCount()}
     * @return A {@link ArticleList} of years as a key and the count as a value
     */
    @RequestMapping(value = "/year")
    public Map<String, Integer> getArticleYearCount() {
        initialiseFacade();
        return facade.getArticleYearCount();
    }

    /**
     * Look at @{@link ArticleFacade#getArticleFundamentalIssueCount()}
     * @return An {@link Map} of {@link com.eu.wiki.api.FundamentalIssue} keys and the count as a value.
     */
    @RequestMapping(value = "/fundamental")
    public Map<String, Integer> getFundamentalIssueCount() {
        initialiseFacade();
        return facade.getArticleFundamentalIssueCount();
    }

    /**
     * Look at {@link ArticleFacade#getArticleEvidenceBasedPolicyCount()}
     * @return A {@link Map} of {@link com.eu.wiki.api.EvidenceBasedPolicy} keys and the count as a value.
     */
    @RequestMapping(value = "/evidence")
    public Map<String, Integer> getEvidenceBasedPolicyCount() {
        initialiseFacade();
        return facade.getArticleEvidenceBasedPolicyCount();
    }

    /**
     * Look at @{@link ArticleFacade#getArticleIndustryCount()}
     * @return A {@link Map} of {@link com.eu.wiki.api.Industry} keys and the count as a value.
     */
    @RequestMapping(value = "/industry")
    public Map<String, Integer> getIndustryCount() {
        initialiseFacade();
        return facade.getArticleIndustryCount();
    }

    /**
     * Returns all the similar articles to the article with the provided id.
     * The similarity is calculated by comparing the cosine distance of each article's vector.
     * The vector is calculated by using the values of the following fields:
     *  {@link com.eu.wiki.api.FundamentalIssue}, {@link com.eu.wiki.api.EvidenceBasedPolicy}, {@link com.eu.wiki.api.Industry}
     *  {@link com.eu.wiki.api.MethodOfCollection}, {@link com.eu.wiki.api.MethodOfCollection}.
     * @param pageID The page id of the article to find similar articles to
     * @return a {@link ArticleList}
     */
    @RequestMapping(value = "/similar/{similarPageId}", method=RequestMethod.GET)
    public ArticleList getSimilarity(@PathVariable("similarPageId") int pageID) {
        initialiseFacade();
        return facade.getSimilarArticles(pageID,0.75);
    }

    private void initialiseFacade() {
        if (facade == null) {
            facade = new ArticleFacadeImpl("", "", URL_TO_WIKI, expiry);
        }
    }
}

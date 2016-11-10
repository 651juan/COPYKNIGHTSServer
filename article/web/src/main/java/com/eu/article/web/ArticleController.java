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

    @RequestMapping(value = "/similar/{similarPageId}", method=RequestMethod.GET)
    public ArticleList getSimilarity(@PathVariable("similarPageId") int pageID) {
        initialiseFacade();
        return facade.getSimilarArticles(pageID,0.75);
    }

    /**
     * Look at {@link ArticleFacade#getArticleCountryCount()}
     * @return
     */
    @RequestMapping(value = "/country")
    public Map<String, Integer> getCountryCount() {
        initialiseFacade();
        return facade.getArticleCountryCount();
    }
    /**
     * Look at {@link ArticleFacade#getArticleEvidenceBasedPolicyCount()}
     * @return
     */
    @RequestMapping(value = "/evidence")
    public Map<String, Integer> getEvidenceBasedPolicyCount() {
        initialiseFacade();
        return facade.getArticleEvidenceBasedPolicyCount();
    }

    /**
     * Look at @{@link ArticleFacade#getArticleFundamentalIssueCount()}
     * @return
     */
    @RequestMapping(value = "/fundamental")
    public Map<String, Integer> getFundamentalIssueCount() {
        initialiseFacade();
        return facade.getArticleFundamentalIssueCount();
    }

    /**
     * Look at @{@link ArticleFacade#getArticlesByCountry(String)}}
     * @param country
     * @return
     */
    @RequestMapping(value = "/country/{countryToGet}", method=RequestMethod.GET)
    public ArticleList getArticleByCountry(@PathVariable("countryToGet") String country) {
        initialiseFacade();
        return facade.getArticlesByCountry(country);
    }

    /**
     * Look at @{@link ArticleFacade#getArticlesByEvidenceBasedPolicy(String)}
     * @param evidence
     * @return
     */
    @RequestMapping(value = "/evidence/{evidenceToGet}", method=RequestMethod.GET)
    public ArticleList getArticleByEvidence(@PathVariable("evidenceToGet") String evidence) {
        initialiseFacade();
        return facade.getArticlesByEvidenceBasedPolicy(evidence);
    }

    /**
     * Look at @{@link ArticleFacade#getArticlesInFundamental(String)}
     * @param fundamental
     * @return
     */
    @RequestMapping(value = "/fundamental/{fundamentalToGet}", method=RequestMethod.GET)
    public ArticleList getArticlesByFundamental(@PathVariable("fundamentalToGet") String fundamental) {
        initialiseFacade();
        return facade.getArticlesInFundamental(fundamental);
    }

    /**
     * Look at @{@link ArticleFacade#getArticleIndustryCount()}
     * @return
     */
    @RequestMapping(value = "/industry")
    public Map<String, Integer> getIndustryCount() {
        initialiseFacade();
        return facade.getArticleIndustryCount();
    }

    /**
     * Look at @{@link ArticleFacade#getArticlesInIndustry(String)}
     * @param industry
     * @return
     */
    @RequestMapping(value = "/industry/{industryToGet}", method=RequestMethod.GET)
    public ArticleList getArticlesInIndustry(@PathVariable("industryToGet") String industry) {
        initialiseFacade();
        return facade.getArticlesInIndustry(industry);
    }

    /**
     * Look at {@link ArticleFacade#getArticleYearCount()}
     * @return
     */
    @RequestMapping(value = "/year")
    public Map<String, Integer> getArticleYearCount() {
        initialiseFacade();
        return facade.getArticleYearCount();
    }

    /**
     * Look at @{@link ArticleFacade#getArticlesInYear(int)}
     * @param year
     * @return
     */
    @RequestMapping(value = "/year/{yearToGet}", method=RequestMethod.GET)
    public ArticleList getArticlesInYear(@PathVariable("yearToGet") int year) {
        initialiseFacade();
        return facade.getArticlesInYear(year);
    }

    /**
     * Look at @{@link ArticleFacade#getArticleAuthorCount()}
     * @return
     */
    @RequestMapping(value = "/author")
    public Map<String, Integer> getArticleAuthorCount() {
        initialiseFacade();
        return facade.getArticleAuthorCount();
    }

    /**
     * Look at {@link ArticleFacade#getArticlesByAuthor(String)}
     * @param author
     * @return
     */
    @RequestMapping(value = "/author/{authorToGet}", method=RequestMethod.GET)
    public ArticleList getArticlesInYear(@PathVariable("authorToGet") String author) {
        initialiseFacade();
        return facade.getArticlesByAuthor(author);
    }

    /**
     * Look at {@link ArticleFacade#getArticlesByTitle(String)}
     * @param titles
     * @return
     */
    @RequestMapping(value="/article", params="title")
    public ArticleList articleByTitle(@RequestParam("title") String titles) {
        initialiseFacade();
        return facade.getArticlesByTitle(titles);
    }

    /**
     * Look at {@link ArticleFacade#getArticlesById(String)}
     * @param ids
     * @return
     */
    @RequestMapping(value="/article", params={"pageids"})
    public ArticleList articleById(@RequestParam("pageids") String ids) {
        initialiseFacade();
        return facade.getArticlesById(ids);
    }

    /**
     * Look at {@link ArticleFacade#getAllArticles()}
     * @return
     */
    @RequestMapping(value="/article")
    public ArticleList allArticles() {
        initialiseFacade();
        return facade.getAllArticles();
    }

    private void initialiseFacade() {
        if (facade == null) {
            facade = new ArticleFacadeImpl("", "", URL_TO_WIKI, expiry);
        }
    }
}

package com.eu.article.web;

import com.eu.article.bd.ArticleFacade;
import com.eu.article.impl.ArticleFacadeImpl;
import com.eu.wiki.api.ArticleList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by Juan on 29/09/2016.
 */
@RestController
public class ArticleController {

    private static final String URL_TO_WIKI = "http://www.copyrightevidence.org/evidence-wiki/api.php";
    private static final LocalDateTime expiry = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,10));
    private ArticleFacade facade;

    @RequestMapping(value="/article", params="title")
    public ArticleList articleByTitle(@RequestParam("title") String titles) {
        initialiseFacade();
        return facade.getArticlesByTitle(titles);
    }

    @RequestMapping(value="/article", params={"pageids","getContent"})
    public ArticleList articleById(@RequestParam("pageids") String ids,
                                   @RequestParam(value="getContent", defaultValue = "false") boolean getContent) {
        initialiseFacade();
        return facade.getArticlesById(ids, getContent);
    }

    @RequestMapping(value="/article", params={"category", "cmContinue", "limit","getContent"})
    public ArticleList articleByCategory(@RequestParam("category") String categories,
                                         @RequestParam(value="cmContinue",defaultValue = "") String cmContinue,
                                         @RequestParam(value="limit",defaultValue="10") int limit,
                                         @RequestParam(value="getContent", defaultValue = "false") boolean getContent) {
        initialiseFacade();
        return facade.getArticlesByCategory(categories, cmContinue, limit,getContent);
    }

    @RequestMapping(value="/article", params={"getContent"})
    public ArticleList allArticles(@RequestParam(value="getContent", defaultValue = "false") boolean getContent) {
        initialiseFacade();
        return facade.getAllArticles(getContent);
    }

    private void initialiseFacade() {
        if (facade == null) {
            facade = new ArticleFacadeImpl("", "", URL_TO_WIKI, expiry);
        }
    }
}

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
 * The controller for requests relating to the parsing of Articles.
 *
 * Created by Juan on 29/09/2016.
 */
@RestController
public class ArticleController {

    private static final String URL_TO_WIKI = "http://www.copyrightevidence.org/evidence-wiki/api.php";
    private static final LocalDateTime expiry = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.now());
    private ArticleFacade facade;

    @RequestMapping(value="/article", params="title")
    public ArticleList articleByTitle(@RequestParam("title") String titles) {
        initialiseFacade();
        return facade.getArticlesByTitle(titles);
    }

    @RequestMapping(value="/article", params={"pageids"})
    public ArticleList articleById(@RequestParam("pageids") String ids) {
        initialiseFacade();
        return facade.getArticlesById(ids);
    }

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

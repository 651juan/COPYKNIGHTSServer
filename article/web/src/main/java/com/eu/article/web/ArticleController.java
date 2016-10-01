package com.eu.article.web;

import com.eu.article.bd.Article;
import com.eu.article.bd.ArticleFacade;
import com.eu.article.bd.ArticleList;
import com.eu.article.impl.ArticleFacadeImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Juan on 29/09/2016.
 */
@RestController
public class ArticleController {

    public static final String URL_TO_WIKI = "http://www.copyrightevidence.org/evidence-wiki/api.php";
    ArticleFacade facade;

    @RequestMapping(value="/article", params="title")
    public ArticleList articleByTitle(@RequestParam("title") String titles) {
        initialiseFacade();
        return facade.getArticles(titles);
    }

    @RequestMapping(value="/article", params={"pageids","getContent"})
    public ArticleList articleById(@RequestParam("pageids") String ids, @RequestParam("getContent") boolean getContent) {
        initialiseFacade();
        return facade.getArticlesById(ids, getContent);
    }

    @RequestMapping(value="/article", params="category")
    public ArticleList articleByCategory(@RequestParam("category") String categories) {
        initialiseFacade();
        return facade.getArticlesByCategory(categories);
    }
    private void initialiseFacade() {
        if (facade == null) {
            facade = new ArticleFacadeImpl("", "", URL_TO_WIKI);
        }
    }
}

package com.eu.article.web;

import com.eu.article.bd.Article;
import com.eu.article.bd.ArticleFacade;
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

    @RequestMapping("/article")
    public List<Article> articleByTitle(@RequestParam("title") String title) {
        ArticleFacade facade = new ArticleFacadeImpl();
        return facade.getArticles(title);
    }
}

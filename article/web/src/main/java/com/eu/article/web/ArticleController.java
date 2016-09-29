package com.eu.article.web;

import com.eu.article.impl.Article;
import com.eu.article.bd.ArticleFacade;
import com.eu.article.impl.ArticleFacadeImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Juan on 29/09/2016.
 */
@RestController
public class ArticleController {

    @RequestMapping("/article")
    public Article article() {
        ArticleFacade facade = new ArticleFacadeImpl();
        facade.getRawArticle("http://www.copyrightevidence.org/evidence-wiki/api.php?action=query&prop=revisions&rvprop=content&format=json&titles=Acilar%20(2010)");
        return new Article();
    }
}

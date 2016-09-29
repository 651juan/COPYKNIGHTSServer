package com.eu.article;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * Created by Juan on 29/09/2016.
 */
@RestController
public class ArticleController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/article")
    public Article article() {
        ArticleFacade facade = new ArticleFacadeImpl();
        facade.getRawArticle("http://www.copyrightevidence.org/evidence-wiki/api.php?action=query&prop=revisions&rvprop=content&format=json&titles=Acilar%20(2010)");
        return new Article();
    }
}

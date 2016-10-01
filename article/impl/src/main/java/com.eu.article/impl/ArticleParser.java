package com.eu.article.impl;

import com.eu.wiki.api.Article;
import com.eu.wiki.api.Reference;
import com.eu.article.bd.TokenType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 29/09/2016.
 */
class ArticleParser {

    private String pRawData;

    ArticleParser() {
        this.pRawData = "";
    }

    Article parse(Article article) {
        Article parsed = new Article();
        if(article.getAbstract() == null) {
            parsed.setName(article.getTitle());
            parsed.setPageid(Integer.valueOf(article.getPageid()));
            parsed.setShortArticle(true);
        } else {
            this.pRawData = article.getAbstract();
            parsed = new Article(Integer.valueOf(article.getPageid()), false);

            parsed.setName(this.getData(TokenType.TOK_NAME_OF_STUDY));
            parsed.setTitle(this.getData(TokenType.TOK_TITLE));
            parsed.setAuthors(this.getAuthors());
            parsed.setYear(Integer.valueOf(this.getData(TokenType.TOK_YEAR)));
            parsed.setCitation(this.getData(TokenType.TOK_CITATION));
            parsed.setAbstract(this.getData(TokenType.TOK_ABSTRACT));
            parsed.setLinks(this.getLinks());
            parsed.setRefences(this.getReferences());
        }
        return parsed;
    }

    private String getData(TokenType type) {
        int idx = this.pRawData.indexOf(type.getTokenValue()) + type.getValueLength()+1;
        if(idx != -1)
            return this.pRawData.substring(idx, this.pRawData.indexOf('|', idx)).trim();
        return "";
    }

    private String[] getAuthors() {
        List<String> tmpResult = new ArrayList<>();
        String rawAuthors = this.getData(TokenType.TOK_AUTHOR);
        String[] splitAuthors = rawAuthors.split(";");

        for(String author:splitAuthors){
            if(author.contains("and")) {
                String[] furtherSplit = author.split("and");
                for(String subauthor:furtherSplit) {
                    tmpResult.add(subauthor.trim());
                }
            }else{
                tmpResult.add(author.trim());
            }
        }

        return tmpResult.toArray(new String[tmpResult.size()]);
    }

    private Reference[] getReferences() {
        List<Reference> tmpResult = new ArrayList<>();
        String rawReferences = this.getData(TokenType.TOK_REFERENCES);
        String[] splitReferences = rawReferences.split(";");

        for(String reference:splitReferences) {
            tmpResult.add(new Reference(reference, null));
        }

        return tmpResult.toArray(new Reference[tmpResult.size()]);
    }

    private URL getLinks() {
        URL tmpResult = null;
        String tmp = this.getData(TokenType.TOK_LINK);


        if(!tmp.equalsIgnoreCase("")){
            try {
                tmpResult = new URL(tmp);
            } catch (MalformedURLException e) {
                System.err.println("Malformed URL: " + tmp);
            }
        }

        return tmpResult;
    }
}

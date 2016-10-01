package com.eu.article.impl;

import com.eu.article.bd.Article;
import com.eu.article.bd.Reference;
import impl.TokenType;
import info.bliki.api.Page;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 29/09/2016.
 */
public class ArticleParser {

    private String pRawData;

    public ArticleParser() {
        this.pRawData = "";
    }

    public Article parse(Page page) {
        //Raw data to parse
        this.pRawData = page.getCurrentContent();
        //New Article
        Article parsed = new Article(Integer.valueOf(page.getPageid()), false);

        //Temporarily store raw data for now
        //parsed.setRawContent(this.pRawData);

        //Get Attributes
        parsed.setName(this.getData(TokenType.TOK_NAME_OF_STUDY));
        parsed.setTitle(this.getData(TokenType.TOK_TITLE));
        parsed.setAuthors(this.getAuthors());
        parsed.setYear(Integer.valueOf(this.getData(TokenType.TOK_YEAR)));
        parsed.setCitation(this.getData(TokenType.TOK_CITATION));
        parsed.setAbstract(this.getData(TokenType.TOK_ABSTRACT));
        parsed.setLinks(this.getLinks());
        parsed.setRefences(this.getReferences());

        //Return Result
        return parsed;
    }

    public String getData(TokenType type) {
        int idx = this.pRawData.indexOf(type.getTokenValue()) + type.getValueLength()+1;
        if(idx != -1)
            return this.pRawData.substring(idx, this.pRawData.indexOf('|', idx)).trim();
        return "";
    }

    public String[] getAuthors() {
        List<String> tmpResult = new ArrayList<String>();
        String rawAuthors = this.getData(TokenType.TOK_AUTHOR);
        String[] splitAuthors = rawAuthors.split(";");

        for(String author:splitAuthors){
            if(author.indexOf("and") != -1) {
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

    public Reference[] getReferences() {
        List<Reference> tmpResult = new ArrayList<Reference>();
        String rawReferences = this.getData(TokenType.TOK_REFERENCES);
        String[] splitReferences = rawReferences.split(";");

        for(String reference:splitReferences) {
            tmpResult.add(new Reference(reference, null));
        }

        return tmpResult.toArray(new Reference[tmpResult.size()]);
    }

    public URL getLinks() {
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

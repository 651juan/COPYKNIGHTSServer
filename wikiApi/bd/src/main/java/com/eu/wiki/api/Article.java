package com.eu.wiki.api;

import java.net.URL;

/**
 * Created by Juan on 29/09/2016.
 */
public class Article {
    private int aPageid;
    private String aName;
    private String aTitle;
    private String[] aAuthors;
    private String aYear; //Needed When year is "Working Paper"
    private String aCitation;
    private String aAbstract;
    private URL aLink;
    private Reference[] aRefences;
    private String aRawContent;
    private Datasets aDatasets;
    // When true artle only contains title and pageid
    private boolean aShortArticle;


    public Article() {}

    public Article(int pageID) {
        this.aPageid = pageID;
    }

    public Article(int pageID, boolean shortArticle) {
        this(pageID);
        this.aShortArticle = shortArticle;
    }

    public Article(int pageID, String title, boolean shortArticle) {
        this(pageID, shortArticle);
        this.aTitle = title;
    }

    public int getPageid() {
        return this.aPageid;
    }

    public void setPageid(int pageid) {
        this.aPageid = pageid;
    }

    public String getName() {
        return aName;
    }

    public void setName(String name) {
        this.aName = name;
    }

    public String getTitle() {
        return aTitle;
    }

    public void setTitle(String title) {
        this.aTitle = title;
    }

    public String[] getAuthors() {
        return aAuthors;
    }

    public void setAuthors(String[] authors) {
        this.aAuthors = authors;
    }

    public String getYear() {
        return aYear;
    }

    public void setYear(String year) {
        this.aYear = year;
    }

    public String getCitation() {
        return aCitation;
    }

    public void setCitation(String citation) {
        this.aCitation = citation;
    }

    public String getAbstract() {
        return aAbstract;
    }

    public void setAbstract(String aAbstract) {
        this.aAbstract = aAbstract;
    }

    public URL getLink() {
        return aLink;
    }

    public void setLinks(URL link) {
        this.aLink = link;
    }

    public Reference[] getRefences() {
        return aRefences;
    }

    public void setRefences(Reference[] refences) {
        this.aRefences = refences;
    }

    public String getRawContent() {
        return aRawContent;
    }

    public void setRawContent(String rawContent) {
        this.aRawContent = rawContent;
    }

    public Boolean getShortArticle() {
        return this.aShortArticle;
    }

    public void setShortArticle(boolean shortArticle) {
        this.aShortArticle = shortArticle;
    }

    public Datasets getDatasets() {
        return aDatasets;
    }

    public void setDatasets(Datasets datasets) {
        this.aDatasets = datasets;
    }
}

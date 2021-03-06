package com.eu.wiki.api;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

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
    private URL[] aLinks;
    private String[] aReferences;
    private FundamentalIssue[] aFundamentalIssues;
    private EvidenceBasedPolicy[] aEvidenceBasedPolicies;
    private String[] aDiscipline;
    private String aRawContent;
    private Datasets aDatasets;
    private byte[] aVector;
    private Map<String, Double> aWordCloud;
    private Map<String, Double> aStemmedWordCloud;
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

    public int getIntYear() {
        try{
            return Integer.valueOf(this.aYear);
        }catch(Exception e) {
            return -1;
        }
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

    public URL[] getLink() {
        return aLinks;
    }

    public void setLinks(URL[] links) {
        this.aLinks = links;
    }

    public String[] getReferences() {
        return this.aReferences;
    }

    public void setReferences(String[] refences) {
        this.aReferences = refences;
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

    public FundamentalIssue[] getFundamentalIssues() {
        return this.aFundamentalIssues;
    }

    public void setFundamentalIssues(FundamentalIssue[] fundamentalIssues) {
        this.aFundamentalIssues = fundamentalIssues;
    }

    public EvidenceBasedPolicy[] getEvidenceBasedPolicies() {
        return this.aEvidenceBasedPolicies;
    }

    public void setEvidenceBasedPolicies(EvidenceBasedPolicy[] evidenceBasedPolicies) {
        this.aEvidenceBasedPolicies = evidenceBasedPolicies;
    }

    public String[] getDiscipline() {
        return this.aDiscipline;
    }

    public void setDiscipline(String[] discipline) {
        this.aDiscipline = discipline;
    }

    public double getSimilatiry(Article toCompare){
        return this.cosineSimilarity(this.aVector, toCompare.aVector);
    }

    public byte[] getVector() {
        return this.aVector;
    }

    public void setVector(byte[] vector) {
        this.aVector = vector;
    }

    private double cosineSimilarity(byte[] v1, byte[] v2) {
        double dotProd = 0;
        double d1 = 0;
        double d2 = 0;

        for(int i = 0; i < v1.length; i++) {
            d1 += Math.pow(v1[i],2);
            d2 += Math.pow(v2[i],2);
            dotProd += (v1[i]*v2[i]);
        }

        d1 = Math.sqrt(d1);
        d2 = Math.sqrt(d2);
        double denom = d1*d2;

        if(denom == 0) {
            return 0;
        }

        return (dotProd/denom);
    }

    public void setWordCloud(Map<String, Double> wordCloud) {
        this.aWordCloud = wordCloud;
    }

    public Map<String, Double> getWordCloud() {
        return this.aWordCloud;
    }

    public Double getWordValue(String name) {
        return this.aStemmedWordCloud.get(name);
    }

    public Map<String, Double> getStemmedWordCloud() {
        return aStemmedWordCloud;
    }

    public void setStemmedWordCloud(Map<String, Double> aStemmedWordCloud) {
        this.aStemmedWordCloud = aStemmedWordCloud;
    }
}

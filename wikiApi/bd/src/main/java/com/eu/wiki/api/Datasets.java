package com.eu.wiki.api;

/**
 * Created by Admin on 04-Oct-16.
 */
public class Datasets {
    //Attributes
    private String dDescription;
    private String dYear;
    private String dType;
    private String[] dDataSources;
    private MethodOfCollection[] dMethodsOfCollection;
    private MethodOfAnalysis[] dMethodsOfAnalysis;
    private Industry[] dIndustry;
    private String[] dCountries;
    private String dCrossCountry;
    private String dComparative;
    private String dGovernmentOrPolicy;
    private String dLiteratureReview;
    private String dFundedBy;
    private Dataset[] dDatasets;

    //Constructor
    public Datasets(String description, String year, String type){
        this.dDescription = description;
        this.dYear = year;
        this.dType = type;
    }

    public Datasets(String description, String year, String type, Dataset[] datasets) {
        this(description, year, type);
        this.dDatasets = datasets;
    }

    //Getters Setters
    public String getDescription() {
        return dDescription;
    }

    public void setDescription(String description) {
        this.dDescription = description;
    }

    public String getYear() {
        return dYear;
    }

    public void setYear(String year) {
        this.dYear = year;
    }

    public String getType() {
        return dType;
    }

    public void setType(String type) {
        this.dType = type;
    }

    public Dataset[] getDatasets() {
        return dDatasets;
    }

    public void setDatasets(Dataset[] datasets) {
        this.dDatasets = datasets;
    }

    public MethodOfCollection[] getMethodOfCollection() {
        return dMethodsOfCollection;
    }

    public void setMethodOfCollection(MethodOfCollection[] methodsOfCollection) {
        this.dMethodsOfCollection = methodsOfCollection;
    }

    public String[] getDataSources() {
        return this.dDataSources;
    }

    public void setDataSources(String[] dataSources) {
        this.dDataSources = dataSources;
    }

    public MethodOfAnalysis[] getMethodOfAnalysis() {
        return this.dMethodsOfAnalysis;
    }

    public void setMethodOfAnalysis(MethodOfAnalysis[] methodsOfAnalysis) {
        this.dMethodsOfAnalysis = methodsOfAnalysis;
    }

    public Industry[] getIndustry() {
        return dIndustry;
    }

    public void setIndustry(Industry[] industry) {
        this.dIndustry = industry;
    }

    public String[] getCountries() {
        return dCountries;
    }

    public void setCountry(String[] countries) {
        this.dCountries = countries;
    }

    public String getCrossCountry() {
        return dCrossCountry;
    }

    public void setCrossCountry(String crossCountry) {
        this.dCrossCountry = crossCountry;
    }

    public String getComparative() {
        return dComparative;
    }

    public void setComparative(String comparative) {
        this.dComparative = comparative;
    }

    public String getGovernmentOrPolicy() {
        return dGovernmentOrPolicy;
    }

    public void setGovernmentOrPolicy(String governmentOrPolicy) {
        this.dGovernmentOrPolicy = governmentOrPolicy;
    }

    public String getLiteratureReview() {
        return dLiteratureReview;
    }

    public void setLiteratureReview(String literatureReview) {
        this.dLiteratureReview = literatureReview;
    }

    public String getFundedBy() {
        return dFundedBy;
    }

    public void setFundedBy(String fundedBy) {
        this.dFundedBy = fundedBy;
    }

    //Other Methods

}

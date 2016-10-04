package com.eu.wiki.api;

/**
 * Created by Admin on 04-Oct-16.
 */
public class Datasets {
    //Attributes
    private String dDescription;
    private String dYear;
    private String dType;
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

    //Other Methods

}

package com.eu.wiki.api;

/**
 * Created by Admin on 04-Oct-16.
 */
public class Dataset {
    //Attributes
    private int dSampleSize;
    private String dLevelOfAggregation;
    private String dMaterialYear;

    //Constructor
    public Dataset(int size, String loa, String my){
        this.dSampleSize = size;
        this.dLevelOfAggregation = loa;
        this.dMaterialYear = my;
    }

    //Getters/Setters
    public int getSampleSize() {
        return dSampleSize;
    }

    public void setSampleSize(int sampleSize) {
        this.dSampleSize = sampleSize;
    }

    public String getLevelOfAggregation() {
        return dLevelOfAggregation;
    }

    public void setLevelOfAggregation(String levelOfAggregation) {
        this.dLevelOfAggregation = levelOfAggregation;
    }

    public String getMaterialYear() {
        return dMaterialYear;
    }

    public void setMaterialYear(String materialYear) {
        this.dMaterialYear = materialYear;
    }

    //Other Methods

}

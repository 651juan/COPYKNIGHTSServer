package com.eu.wiki.api;

/**
 * Created by Admin on 07/11/2016.
 */
public enum MethodOfAnalysis {
    MOA_1("Quantitative Analysis Methods",0),
    MOA_2("Cluster analysis",1),
    MOA_3("Descriptive statistics (counting; means reporting; cross-tabulation)",2),
    MOA_4("Quantitative content analysis (e.g. text or data mining)",3),
    MOA_5("Correlation and Association",4),
    MOA_6("Multivariate Statistics",5),
    MOA_7("Regression Analysis",6),
    MOA_8("Social Network Analysis",7),
    MOA_9("Social Sequence Analysis",8),
    MOA_10("Structural Equation Modeling",9),
    MOA_11("Meta-Analysis",10),
    MOA_12("Calibration",11),
    MOA_13("Confirmatory Factor Analysis (CFA)",12),
    MOA_14("Factor Analysis",13),
    MOA_15("Decision Tree Method",14),
    MOA_16("Qualitative Analysis Methods",15),
    MOA_17("Textual Content Analysis",16),
    MOA_18("Visual / Other Content Analysis",17),
    MOA_19("Discourse Analysis",18),
    MOA_20("Qualitative Coding / Sorting (e.g. of interview data)",19),
    MOA_21("Ethnographic/narrative analysis",20),
    MOA_22("Triangulation",21),
    MOA_23("Legal Analysis",22),
    MOA_24("Grounded Theory",23),
    MOA_25("Abduction/Retroduction",24),
    UNKNOWN_MOA("UNKNOWN",24);

    private final String MOA_VALUE;
    private final int MOA_IDX;

    MethodOfAnalysis(String value, int idx) {
        this.MOA_VALUE = value;
        this.MOA_IDX = idx;
    }

    public String getValue() {
        return this.MOA_VALUE;
    }

    public int getIndex() {
        return this.MOA_IDX;
    }

    public boolean equals(String toCompare) {
        return this.MOA_VALUE.equalsIgnoreCase(toCompare.trim());
    }
}

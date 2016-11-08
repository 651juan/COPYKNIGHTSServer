package com.eu.wiki.api;

/**
 * Created by Admin on 07/11/2016.
 */
public enum MethodOfCollection {
    MOC_1("Quantitative Collection Methods",0),
    MOC_2("Survey Research (quantitative; e.g. sales/income reporting)",1),
    MOC_3("Experimental (Laboratory)",2),
    MOC_4("Experimental (Field)",3),
    MOC_5("Experimental (Natural)",4),
    MOC_6("Web analytic (online user trace data)",5),
    MOC_7("Quantitative data/text mining",6),
    MOC_8("Longitudinal Study",7),
    MOC_9("Snowball sampling",8),
    MOC_10("Qualitative Collection Methods",9),
    MOC_11("Survey Research (qualitative; e.g. consumer preferences)",10),
    MOC_12("Case Study",11),
    MOC_13("Ethnography",12),
    MOC_14("Life History",13),
    MOC_15("Unstructured Interview",14),
    MOC_16("Semi-Structured Interview",15),
    MOC_17("Structured Interview",16),
    MOC_18("Archival Research",17),
    MOC_19("Focus Groups",18),
    MOC_20("Historical Methods",19),
    MOC_21("Card Sorting",20),
    MOC_22("Document Research",21),
    MOC_23("Qualitative content/text mining",22),
    MOC_24("Visual Ethnography",23),
    MOC_25("Participant Observation",24),
    UNKNOWN_MOC("UNKNOWN",25);

    private final String MOC_VALUE;
    private final int MOC_IDX;

    MethodOfCollection(String value, int idx) {
        this.MOC_VALUE = value;
        this.MOC_IDX = idx;
    }

    public String getValue() {
        return this.MOC_VALUE;
    }

    public int getIndex() {
        return this.MOC_IDX;
    }

    public boolean equals(String toCompare) {
        return this.MOC_VALUE.equalsIgnoreCase(toCompare.trim());
    }
}

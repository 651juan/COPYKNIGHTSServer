package com.eu.wiki.api;

/**
 * Created by Admin on 07/11/2016.
 */
public enum MethodOfAnalysis {
    MOA_1("Quantitative Analysis Methods"),
    MOA_2("Qualitative Analysis Methods"),
    UNKNOWN_MOA("UNKNOWN");

    private final String MOA_VALUE;

    MethodOfAnalysis(String value) {
        this.MOA_VALUE = value;
    }

    public String getValue() {
        return this.MOA_VALUE;
    }

    public boolean equals(String toCompare) {
        return this.MOA_VALUE.equalsIgnoreCase(toCompare.trim());
    }
}

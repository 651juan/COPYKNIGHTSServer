package com.eu.wiki.api;

/**
 * Created by Admin on 07/11/2016.
 */
public enum MethodOfCollection {
    MOC_1("Quantitative Collection Methods"),
    MOC_2("Qualitative Collection Methods"),
    UNKNOWN_MOC("UNKNOWN");

    private final String MOC_VALUE;

    MethodOfCollection(String value) {
        this.MOC_VALUE = value;
    }

    public String getValue() {
        return this.MOC_VALUE;
    }

    public boolean equals(String toCompare) {
        return this.MOC_VALUE.equalsIgnoreCase(toCompare.trim());
    }
}

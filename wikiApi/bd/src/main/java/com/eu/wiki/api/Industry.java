package com.eu.wiki.api;

/**
 * Created by Admin on 07/11/2016.
 */
public enum Industry {
    INDUSTRY_1("Advertising"),
    INDUSTRY_2("Architectural"),
    INDUSTRY_3("Computer consultancy"),
    INDUSTRY_4("Computer programming"),
    INDUSTRY_5("Creative, arts and entertainment"),
    INDUSTRY_6("Cultural education"),
    INDUSTRY_7("Film and motion pictures"),
    INDUSTRY_8("PR and communication"),
    INDUSTRY_9("Photographic activities"),
    INDUSTRY_10("Programming and broadcasting"),
    INDUSTRY_11("Publishing of books, periodicals and other publishing"),
    INDUSTRY_12("Software publishing (including video games)"),
    INDUSTRY_13("Sound recording and music publishing"),
    INDUSTRY_14("Specialised design"),
    INDUSTRY_15("Television programmes"),
    INDUSTRY_16("Translation and interpretation"),
    UNKNOWN_INDUSTRY("UNKNOWN");

    private final String INDUSTRY_VALUE;

    Industry(String value) {
        this.INDUSTRY_VALUE = value;
    }

    public String getValue() {
        return this.INDUSTRY_VALUE;
    }

    public boolean equals(String toCompare) {
        return this.INDUSTRY_VALUE.equalsIgnoreCase(toCompare.trim());
    }
}

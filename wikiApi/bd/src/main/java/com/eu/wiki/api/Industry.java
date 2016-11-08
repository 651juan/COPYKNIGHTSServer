package com.eu.wiki.api;

/**
 * Created by Admin on 07/11/2016.
 */
public enum Industry {
    INDUSTRY_1("Advertising",0),
    INDUSTRY_2("Architectural",1),
    INDUSTRY_3("Computer consultancy",2),
    INDUSTRY_4("Computer programming",3),
    INDUSTRY_5("Creative, arts and entertainment",4),
    INDUSTRY_6("Cultural education",5),
    INDUSTRY_7("Film and motion pictures",6),
    INDUSTRY_8("PR and communication",7),
    INDUSTRY_9("Photographic activities",8),
    INDUSTRY_10("Programming and broadcasting",9),
    INDUSTRY_11("Publishing of books, periodicals and other publishing",10),
    INDUSTRY_12("Software publishing (including video games)",11),
    INDUSTRY_13("Sound recording and music publishing",12),
    INDUSTRY_14("Specialised design",13),
    INDUSTRY_15("Television programmes",14),
    INDUSTRY_16("Translation and interpretation",15),
    UNKNOWN_INDUSTRY("UNKNOWN",16);

    private final String INDUSTRY_VALUE;
    private final int INDUSTRY_IDX;

    Industry(String value, int idx) {
        this.INDUSTRY_VALUE = value;
        this.INDUSTRY_IDX = idx;
    }

    public String getValue() {
        return this.INDUSTRY_VALUE;
    }

    public int getIndex() {
        return this.INDUSTRY_IDX;
    }

    public boolean equals(String toCompare) {
        return this.INDUSTRY_VALUE.equalsIgnoreCase(toCompare.trim());
    }
}

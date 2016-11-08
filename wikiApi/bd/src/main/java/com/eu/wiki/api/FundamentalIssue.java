package com.eu.wiki.api;

/**
 * Created by Admin on 14/10/2016.
 */
public enum FundamentalIssue {
    ISSUE_1("1. Relationship between protection (subject matter/term/scope) and supply/economic development/growth/welfare",0),
    ISSUE_2("2. Relationship between creative process and protection - what motivates creators (e.g. attribution; control; remuneration; time allocation)?",1),
    ISSUE_3("3. Harmony of interest assumption between authors and publishers (creators and producers/investors)",2),
    ISSUE_4("4. Effects of protection on industry structure (e.g. oligopolies; competition; economics of superstars; business models; technology adoption)",3),
    ISSUE_5("5. Understanding consumption/use (e.g. determinants of unlawful behaviour; user-generated content; social media)",4),
    UNKNOWN_ISSUE("UNKNOWN",5);

    private final String FUNDAMENTAL_ISSUE_VALUE;
    private final int FUNDAMENTAL_ISSUE_IDX;

    FundamentalIssue(String value, int idx) {
        this.FUNDAMENTAL_ISSUE_VALUE = value;
        this.FUNDAMENTAL_ISSUE_IDX = idx;
    }

    public String getValue() {
        return this.FUNDAMENTAL_ISSUE_VALUE;
    }

    public int getIndex() {
        return this.FUNDAMENTAL_ISSUE_IDX;
    }

    public boolean equals(String toCompare) {
        return this.FUNDAMENTAL_ISSUE_VALUE.equalsIgnoreCase(toCompare.trim());
    }
}

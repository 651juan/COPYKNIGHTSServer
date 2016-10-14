package com.eu.wiki.api;

/**
 * Created by Admin on 14/10/2016.
 */
public enum FundamentalIssue {
    ISSUE_1("1. Relationship between protection (subject matter/term/scope) and supply/economic development/growth/welfare"),
    ISSUE_2("2. Relationship between creative process and protection - what motivates creators (e.g. attribution; control; remuneration; time allocation)?"),
    ISSUE_3("3. Harmony of interest assumption between authors and publishers (creators and producers/investors)"),
    ISSUE_4("4. Effects of protection on industry structure (e.g. oligopolies; competition; economics of superstars; business models; technology adoption)"),
    ISSUE_5("5. Understanding consumption/use (e.g. determinants of unlawful behaviour; user-generated content; social media)"),
    UNKNOWN_ISSUE("");

    private final String FUNDAMENTAL_ISSUE_VALUE;

    FundamentalIssue(String value) {
        this.FUNDAMENTAL_ISSUE_VALUE = value;
    }

    public String getValue() {
        return this.FUNDAMENTAL_ISSUE_VALUE;
    }

    public boolean equals(String toCompare) {
        return this.FUNDAMENTAL_ISSUE_VALUE.equalsIgnoreCase(toCompare.trim());
    }
}

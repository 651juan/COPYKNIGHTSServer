package com.eu.wiki.api;

/**
 * Created by Admin on 30-Sep-16.
 */
public enum TokenType {
    TOK_HEAD_QUERY("query"),
    TOK_HEAD_PAGES("pages"),
    TOK_HEAD_CATEGORY_MEMBERS("categorymembers"),
    TOK_HEAD_NS("ns"),
    TOK_HEAD_PAGEID("pageid"),
    TOK_HEAD_TITLE("title"),
    TOK_HEAD_REVISIONS("revisions"),

    TOK_CONTENT_FORMAT("contentformat"),
    TOK_CONTENT_MODEL("contentmodel"),
    TOK_CONTENT("*"),
    TOK_NAME_OF_STUDY("Name of Study"),
    TOK_AUTHOR("Author"),
    TOK_TITLE("Title"),
    TOK_YEAR("Year"),
    TOK_CITATION("Full Citation"),
    TOK_ABSTRACT("Abstract"),
    TOK_REFERENCES("Reference"),
    TOK_PLAINTEXT_PROPOSITION("Plain Text Proposition"),
    TOK_LINK("Authentic Link");

    private final String tokenValue;

    TokenType(String tokenVal) {
        this.tokenValue = tokenVal;
    }

    public String getTokenValue() {
        return this.tokenValue;
    }

    public int getValueLength() {
        return this.tokenValue.length();
    }
}

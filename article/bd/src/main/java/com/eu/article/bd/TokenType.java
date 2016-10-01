package com.eu.article.bd;

/**
 * Created by Admin on 30-Sep-16.
 */
public enum TokenType {
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

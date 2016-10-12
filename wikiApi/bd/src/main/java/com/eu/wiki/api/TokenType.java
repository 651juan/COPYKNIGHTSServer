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
    TOK_HEAD_CONTINUE("continue"),
    TOK_HEAD_CMCONTINUE("cmcontinue"),
    TOK_HEAD_MISSING("missing"),

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
    TOK_AUTHENTIC_LINK("Authentic Link"),
    TOK_LINK("Link="),

    TOK_DAT_DATASET("Dataset"),
    TOK_DAT_DESCRIPTION("Description of Data"),
    TOK_DAT_YEAR("Data Year"),
    TOK_DAT_TYPE("Data Type"),
    TOK_DAT_SAMPLE_SIZE("Sample Size"),
    TOK_DAT_LOG("Level of Aggregation"),
    TOK_DAT_DMY("Data Material Year"),
    TOK_DAT_MOC("Method of Collection"),
    TOK_DAT_MOA("Method of Analysis"),
    TOK_DAT_INDUSTRY("Industry"),
    TOK_DAT_COUNTRY("Country"),
    TOK_DAT_CROSS_COUNTRY("Cross-country"),
    TOK_DAT_COMPARATIVE("Comparative"),
    TOK_DAT_GOP("Government or policy"),
    TOK_DAT_LR("Literature review"),
    TOK_DAT_FUNDED("Funded By");

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

package com.eu.wiki.api;

/**
 * Created by Admin on 14/10/2016.
 */
public enum EvidenceBasedPolicy {
    POLICY_A("A. Nature and Scope of exclusive rights (hyperlinking/browsing; reproduction right)",0),
    POLICY_B("B. Exceptions (distinguish innovation and public policy purposes; open-ended/closed list; commercial/non-commercial distinction)",1),
    POLICY_C("C. Mass digitisation/orphan works (non-use; extended collective licensing)",2),
    POLICY_D("D. Licensing and Business models (collecting societies; meta data; exchanges/hubs; windowing; crossborder availability)",3),
    POLICY_E("E. Fair remuneration (levies; copyright contracts)",4),
    POLICY_F("F. Enforcement (quantifying infringement; criminal sanctions; intermediary liability; graduated response; litigation and court data; commercial/non-commercial distinction; education and awareness)",5),
    UNKNOWN_POLICY("UNKNOWN",5);

    private final String EVIDENCE_BASED_POLICY_VALUE;
    private final int EVIDENCE_BASED_POLICY_IDX;

    EvidenceBasedPolicy(String value, int idx) {
        this.EVIDENCE_BASED_POLICY_VALUE = value;
        this.EVIDENCE_BASED_POLICY_IDX = idx;
    }

    public String getValue() {
        return this.EVIDENCE_BASED_POLICY_VALUE;
    }

    public int getIndex() {
        return this.EVIDENCE_BASED_POLICY_IDX;
    }

    public boolean equals(String toCompare) {
        return this.EVIDENCE_BASED_POLICY_VALUE.equalsIgnoreCase(toCompare.trim());
    }
}

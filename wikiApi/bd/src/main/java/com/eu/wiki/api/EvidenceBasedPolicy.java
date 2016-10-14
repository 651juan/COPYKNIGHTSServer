package com.eu.wiki.api;

/**
 * Created by Admin on 14/10/2016.
 */
public enum EvidenceBasedPolicy {
    POLICY_A("A. Nature and Scope of exclusive rights (hyperlinking/browsing; reproduction right)"),
    POLICY_B("B. Exceptions (distinguish innovation and public policy purposes; open-ended/closed list; commercial/non-commercial distinction)"),
    POLICY_C("C. Mass digitisation/orphan works (non-use; extended collective licensing)"),
    POLICY_D("D. Licensing and Business models (collecting societies; meta data; exchanges/hubs; windowing; crossborder availability)"),
    POLICY_E("E. Fair remuneration (levies; copyright contracts)"),
    POLICY_F("F. Enforcement (quantifying infringement; criminal sanctions; intermediary liability; graduated response; litigation and court data; commercial/non-commercial distinction; education and awareness)"),
    UNKNOWN_POLICY("");

    private final String EVIDENCE_BASED_POLICY_VALUE;

    EvidenceBasedPolicy(String value) {
        this.EVIDENCE_BASED_POLICY_VALUE = value;
    }

    public String getValue() {
        return this.EVIDENCE_BASED_POLICY_VALUE;
    }

    public boolean equals(String toCompare) {
        return this.EVIDENCE_BASED_POLICY_VALUE.equalsIgnoreCase(toCompare.trim());
    }
}

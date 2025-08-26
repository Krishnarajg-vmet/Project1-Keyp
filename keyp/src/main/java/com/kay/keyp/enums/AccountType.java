package com.kay.keyp.enums;

public enum AccountType {
    GMAIL("Gmail"),
    GITHUB("GitHub"),
    UPI("UPI"),
    BANKING("Banking"),
    SOCIAL_MEDIA("Social Media"),
    DEV("Development"),
    OTHERS("Others");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

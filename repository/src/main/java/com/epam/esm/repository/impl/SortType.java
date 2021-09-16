package com.epam.esm.repository.impl;

public enum SortType {
    ASC, DESC, NONE;

    public static SortType createType(String type) {
        switch (type) {
            case "+":
                return ASC;
            case "-":
                return DESC;
            default:
                return NONE;
        }
    }
}

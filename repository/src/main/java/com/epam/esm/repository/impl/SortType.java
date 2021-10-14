package com.epam.esm.repository.impl;

import java.util.Locale;

/**
 * Represents type of sorting ASC - ascending, DESC - descending, NONE - no sorting
 */
public enum SortType {
    ASC, DESC, NONE;

    public static SortType createType(String type) {
        switch (type.trim().toLowerCase(Locale.ROOT)) {
            case "asc":
                return ASC;
            case "desc":
                return DESC;
            default:
                return NONE;
        }
    }
}

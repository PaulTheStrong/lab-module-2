package com.epam.esm.repository.impl.jdbc;

import java.util.Locale;

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

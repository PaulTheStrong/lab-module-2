package com.epam.esm.repository.impl;

import java.util.Locale;

/**
 * Represents names of sorting columns.
 */
public enum SortColumn {
    DATE("createDate"), NAME("name"), NONE("");
    private final String column;

    SortColumn(String column) {
        this.column = column;
    }

    public String toString() {
        return column;
    }

    public static SortColumn createColumn(String name) {
        switch (name.toLowerCase(Locale.ROOT)) {
            case "name":
                return NAME;
            case "date":
                return DATE;
            default:
                return NONE;
        }
    }
}

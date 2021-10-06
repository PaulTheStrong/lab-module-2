package com.epam.esm.repository.impl;

import java.util.Locale;

public enum SortColumn {
    DATE("gift_certificate.create_date"), NAME("gift_certificate.name"), NONE("");
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

package com.epam.esm.repository.impl;

import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class GiftCertificatePreparedStatementCreator implements PreparedStatementCreator {

    private String tag;
    private String search;
    private final Map<SortColumn, SortType> sorts = new LinkedHashMap<>();
    private String queryString;

    @Override
    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            int counter = 1;
            if (tag != null) {
                preparedStatement.setString(counter++, tag);
            }

            if (search != null) {
                preparedStatement.setString(counter++, search);
                preparedStatement.setString(counter, search);
            }
            return preparedStatement;
    }

    public static GiftCertificateQueryBuilder createBuilder() {
        return new GiftCertificatePreparedStatementCreator().new GiftCertificateQueryBuilder();
    }

    public class GiftCertificateQueryBuilder {
        private static final String WHERE = " WHERE ";
        private static final String AND = " AND ";
        private static final String SEARCH_NAME_OR_DESCRIPTION_SUFFIX = "(gc.description LIKE CONCAT('%', ?, '%') " +
                "OR gc.name LIKE CONCAT('%', ?, '%'))";
        private static final String ORDER_BY = " ORDER BY ";
        private static final String BASE_SQL = "SELECT gc.* FROM gift_certificate gc";
        private static final String BY_TAG_SUFFIX =
                " INNER JOIN tag_certificate tc ON tc.certificate_id = gc.id " +
                        "INNER JOIN tag t ON t.id = tc.tag_id WHERE (t.name = ?)";

        private GiftCertificateQueryBuilder() {}

        public GiftCertificateQueryBuilder searchByTag(String tag) {
            GiftCertificatePreparedStatementCreator.this.tag = tag;
            return this;
        }

        public GiftCertificateQueryBuilder searchByNameOrDescription(String search) {
            GiftCertificatePreparedStatementCreator.this.search = search;
            return this;
        }

        public GiftCertificateQueryBuilder sort(SortColumn field, SortType sort) {
            if (field != SortColumn.NONE && sort != SortType.NONE) {
                sorts.put(field, sort);
            }
            return this;
        }

        public PreparedStatementCreator build() {
            GiftCertificatePreparedStatementCreator.this.queryString = buildQueryString();
            return GiftCertificatePreparedStatementCreator.this;
        }

        private String buildQueryString() {
            StringBuilder queryBuilder = new StringBuilder(BASE_SQL);
            if (tag != null) {
                queryBuilder.append(BY_TAG_SUFFIX);
            }
            if (search != null) {
                if (tag == null) {
                    queryBuilder.append(WHERE);
                } else {
                    queryBuilder.append(AND);
                }
                queryBuilder.append(SEARCH_NAME_OR_DESCRIPTION_SUFFIX);
            }
            if (!sorts.isEmpty()) {
                queryBuilder.append(ORDER_BY);
                sorts.entrySet().stream()
                        .filter(entry -> entry.getValue() != SortType.NONE && entry.getKey() != SortColumn.NONE)
                        .forEach(entry -> queryBuilder.append(entry.getKey()).append(" ").append(entry.getValue()).append(","));
                int length = queryBuilder.length();
                queryBuilder.replace(length - 1, length, "");
            }
            return queryBuilder.toString();
        }
    }
}

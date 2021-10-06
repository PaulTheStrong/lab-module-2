package com.epam.esm.repository.impl.jdbc;

import com.epam.esm.repository.impl.FilterParameters;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class GiftCertificatePreparedStatementCreator implements PreparedStatementCreator {

    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String SEARCH_NAME_OR_DESCRIPTION_SUFFIX =
            "(gift_certificate.description LIKE CONCAT('%', ?, '%') " +
                    "OR gift_certificate.name LIKE CONCAT('%', ?, '%'))";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String BASE_SQL = "SELECT gift_certificate.* FROM gift_certificate";
    private static final String BY_TAG_SUFFIX =
            " INNER JOIN tag_certificate ON tag_certificate.certificate_id = gift_certificate.id " +
                    "INNER JOIN tag ON tag.id = tag_certificate.tag_id WHERE (tag.name = ?)";

    private FilterParameters filterParameters;

    public GiftCertificatePreparedStatementCreator(FilterParameters filterParameters) {
        this.filterParameters = filterParameters;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        String queryString = buildQueryString();
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        int counter = 1;
        Set<String> tags = filterParameters.getTags();
        if (tags.size() != 0) {
            preparedStatement.setString(counter++, tags.stream().findFirst().get());
        }
        String search = filterParameters.getNameOrDescription();
        if (search != null) {
            preparedStatement.setString(counter++, search);
            preparedStatement.setString(counter, search);
        }
        return preparedStatement;
    }

    private String buildQueryString() {
        StringBuilder queryBuilder = new StringBuilder(BASE_SQL);
        String tag = filterParameters.getTags().stream().findFirst().orElse(null);
        if (tag != null) {
            queryBuilder.append(BY_TAG_SUFFIX);
        }
        String search = filterParameters.getNameOrDescription();
        if (search != null) {
            if (tag == null) {
                queryBuilder.append(WHERE);
            } else {
                queryBuilder.append(AND);
            }
            queryBuilder.append(SEARCH_NAME_OR_DESCRIPTION_SUFFIX);
        }
        Map<SortColumn, SortType> sorts = filterParameters.getSorts();
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

package com.epam.esm.repository.impl.jpa;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.repository.impl.FilterParameters;
import com.epam.esm.repository.impl.SortColumn;
import com.epam.esm.repository.impl.SortType;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* package private */
@Component
class FilterParametersQueryBuilder {

    private static final String SEARCH_BY_NAME_OR_DESCRIPTION = "(c.name LIKE concat('%', :search, '%') OR c.description LIKE concat('%', :search, '%'))";
    private static final String COUNT_PREFIX = "SELECT count(c) FROM GiftCertificate c ";
    private static final String SELECT_PREFIX = "SELECT c FROM GiftCertificate c ";
    private static final String JOIN_TAGS = " JOIN c.tags t WHERE t.name in (";
    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String GROUP_BY = " GROUP BY (c.id) HAVING COUNT(c) = ";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String SEARCH = "search";

    public TypedQuery<GiftCertificate> buildSelectQuery(EntityManager entityManager, FilterParameters filterParameters) {
        StringBuilder queryBuilder = new StringBuilder(SELECT_PREFIX);
        String queryString = appendQueryParameters(filterParameters, queryBuilder);
        TypedQuery<GiftCertificate> query = entityManager.createQuery(queryString, GiftCertificate.class);
        setQueryParameters(query, filterParameters);
        return query;
    }

    public TypedQuery<Long> buildCountQuery(EntityManager entityManager, FilterParameters filterParameters) {
        StringBuilder queryBuilder = new StringBuilder(COUNT_PREFIX);
        String queryString = appendQueryParameters(filterParameters, queryBuilder);
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        setQueryParameters(query, filterParameters);
        return query;
    }

    private String appendQueryParameters(FilterParameters filterParameters, StringBuilder queryBuilder) {
        Set<String> tags = filterParameters.getTags();
        List<String> tagParameters = new ArrayList<>();
        if (tags != null && tags.size() > 0) {
            queryBuilder.append(JOIN_TAGS);
            String tagsToAppend = tags.stream().reduce("", (tagsString, tag) -> tagsString.replace(" ", "_") + ":_" + tag + ",");
            tagsToAppend = tagsToAppend.substring(0, tagsToAppend.length() - 1);
            queryBuilder.append(tagsToAppend).append(")");
        }
        String search = filterParameters.getNameOrDescription();
        if (search != null) {
            if (tags == null || tags.size() == 0) {
                queryBuilder.append(WHERE);
            } else {
                queryBuilder.append(AND);
            }
            queryBuilder.append(SEARCH_BY_NAME_OR_DESCRIPTION);
        }

        if (search == null && (tags == null || tags.size() == 0)) {
            queryBuilder.append(" WHERE c.isAvailable = true ");
        } else {
            queryBuilder.append(" AND c.isAvailable = true ");
        }

        if (tags != null && tags.size() > 0) {
            queryBuilder.append(GROUP_BY).append(tags.size());
        }
        Map<SortColumn, SortType> sorts = filterParameters.getSorts();
        if (!sorts.isEmpty()) {
            queryBuilder.append(ORDER_BY);
            sorts.entrySet().stream()
                    .filter(entry -> entry.getValue() != SortType.NONE && entry.getKey() != SortColumn.NONE)
                    .forEach(entry -> queryBuilder.append("c.").append(entry.getKey()).append(" ").append(entry.getValue()).append(","));
            int length = queryBuilder.length();
            queryBuilder.replace(length - 1, length, "");
        }
        return queryBuilder.toString();
    }

    private void setQueryParameters(Query query, FilterParameters filterParameters) {
        Set<String> tags = filterParameters.getTags();
        String search = filterParameters.getNameOrDescription();
        if (tags != null) {
            tags.forEach(tag -> query.setParameter("_" + tag.replace(" ", "_"), tag));
        }
        if (search != null) {
            query.setParameter(SEARCH, search);
        }
    }
}

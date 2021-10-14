package com.epam.esm.repository.impl.jpa;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.repository.api.GiftCertificateRepository;
import com.epam.esm.repository.impl.FilterParameters;
import com.epam.esm.repository.impl.SortColumn;
import com.epam.esm.repository.impl.SortType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
@Profile("jpa")
public class JpaGiftCertificateRepository implements GiftCertificateRepository {

    private static final String SELECT_ALL = "SELECT giftCertificate FROM GiftCertificate giftCertificate";
    private static final String DELETE_BY_ID = "DELETE FROM GiftCertificate WHERE id =: id";
    public static final String FIND_WITH_PARAMS =
            "SELECT c, t from GiftCertificate c JOIN c.tags t " +
            "WHERE t.name in ('ok', 'wow') " +
            "AND (c.name LIKE concat('%', :search, '%') " +
                "OR c.description LIKE concat('%', :search, '%')) " +
            "GROUP BY (c.id) HAVING count(c) = 2";
    public static final String SEARCH_BY_NAME_OR_DESCRIPTION = "(c.name LIKE concat('%', :search, '%') OR c.description LIKE concat('%', :search, '%'))";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GiftCertificate> update(GiftCertificate entity) {
        GiftCertificate updated = entityManager.merge(entity);
        return Optional.of(updated);
    }

    @Override
    public List<GiftCertificate> findBySpecification(FilterParameters filterParameters) {
        TypedQuery<GiftCertificate> query = buildQuery(filterParameters);
        return query.getResultList();
    }

    @Override
    public List<GiftCertificate> findBySpecification(FilterParameters filterParameters, int pageNumber, int pageSize) {
        TypedQuery<GiftCertificate> query = buildQuery(filterParameters);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public Optional<GiftCertificate> save(GiftCertificate entity) {
        GiftCertificate merged = entityManager.merge(entity);
        merged.getTags().forEach(tag -> tag.getCertificates().add(merged));
        return Optional.of(merged);
    }

    @Override
    public Optional<GiftCertificate> findById(int id) {
        GiftCertificate certificateById = entityManager.find(GiftCertificate.class, id);
        return Optional.ofNullable(certificateById);
    }

    @Override
    public List<GiftCertificate> findAll() {
        TypedQuery<GiftCertificate> findAllQuery
                = entityManager.createQuery(SELECT_ALL, GiftCertificate.class);
        return findAllQuery.getResultList();
    }

    @Override
    public boolean delete(int id) {
        Query deleteQuery = entityManager.createQuery(DELETE_BY_ID);
        deleteQuery.setParameter("id", id);
        int rows = deleteQuery.executeUpdate();
        return rows == 1;
    }

    @Override
    public List<GiftCertificate> findAll(int pageNumber, int pageSize) {
        TypedQuery<GiftCertificate> selectCertificatesQuery
                = entityManager.createQuery(SELECT_ALL, GiftCertificate.class);
        selectCertificatesQuery.setFirstResult((pageNumber - 1) * pageSize);
        selectCertificatesQuery.setMaxResults(pageSize);
        return selectCertificatesQuery.getResultList();
    }

    private TypedQuery<GiftCertificate> buildQuery(FilterParameters filterParameters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT c FROM GiftCertificate c ");
        Set<String> tags = filterParameters.getTags();
        List<String> tagParameters = new ArrayList<>();
        if (tags != null && tags.size() > 0) {
            queryBuilder.append(" JOIN c.tags t WHERE t.name in (");
            String tagsToAppend = tags.stream().reduce("", (tagsString, tag) -> tagsString.replace(" ", "_") + ":_" + tag + ",");
            tagsToAppend = tagsToAppend.substring(0, tagsToAppend.length() - 1);
            queryBuilder.append(tagsToAppend).append(")");
        }
        String search = filterParameters.getNameOrDescription();
        if (search != null) {
            if (tags == null || tags.size() == 0) {
                queryBuilder.append(" WHERE ");
            } else {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append(SEARCH_BY_NAME_OR_DESCRIPTION);
        }
        if (tags != null && tags.size() > 0) {
            queryBuilder.append(" GROUP BY (c.id) HAVING COUNT(c) = ").append(tags.size());
        }
        Map<SortColumn, SortType> sorts = filterParameters.getSorts();
        if (!sorts.isEmpty()) {
            queryBuilder.append(" ORDER BY ");
            sorts.entrySet().stream()
                    .filter(entry -> entry.getValue() != SortType.NONE && entry.getKey() != SortColumn.NONE)
                    .forEach(entry -> queryBuilder.append("c.").append(entry.getKey()).append(" ").append(entry.getValue()).append(","));
            int length = queryBuilder.length();
            queryBuilder.replace(length - 1, length, "");
        }
        TypedQuery<GiftCertificate> query = entityManager.createQuery(queryBuilder.toString(), GiftCertificate.class);
        if (tags != null) {
            tags.forEach(tag -> query.setParameter("_" + tag.replace(" ", "_"), tag));
        }
        if (search != null) {
            query.setParameter("search", search);
        }
        return query;
    }
}

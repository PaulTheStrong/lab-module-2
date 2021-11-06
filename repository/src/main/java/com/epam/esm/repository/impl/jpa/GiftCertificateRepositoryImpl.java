package com.epam.esm.repository.impl.jpa;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.repository.api.GiftCertificateCustomRepository;
import com.epam.esm.repository.impl.FilterParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final FilterParametersQueryBuilder queryBuilder;

    @Override
    public List<GiftCertificate> findBySpecification(FilterParameters filterParameters, int pageNumber, int pageSize) {
        TypedQuery<GiftCertificate> query = queryBuilder.buildSelectQuery(entityManager, filterParameters);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public int countEntitiesBySpecification(FilterParameters filterParameters) {
        TypedQuery<Long> countQuery = queryBuilder.buildCountQuery(entityManager, filterParameters);
        long result;
        try {
            result = countQuery.getSingleResult();
        } catch (NoResultException e) {
            result = 0;
        }
        return (int) result;
    }
}

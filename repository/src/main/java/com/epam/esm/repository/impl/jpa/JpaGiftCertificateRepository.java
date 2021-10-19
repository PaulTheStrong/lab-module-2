package com.epam.esm.repository.impl.jpa;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.repository.api.GiftCertificateRepository;
import com.epam.esm.repository.impl.FilterParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaGiftCertificateRepository implements GiftCertificateRepository {

    private static final String SELECT_ALL = "SELECT gc FROM GiftCertificate gc WHERE gc.isAvailable=true";
    private static final String DELETE_BY_ID = "UPDATE GiftCertificate gc SET gc.isAvailable = false WHERE gc.id =: id AND gc.isAvailable = true";
    private static final String COUNT_CERTIFICATES = "SELECT count(gc) FROM GiftCertificate gc WHERE gc.isAvailable=true";

    @PersistenceContext
    private EntityManager entityManager;
    private final FilterParametersQueryBuilder queryBuilder;

    @Autowired
    public JpaGiftCertificateRepository(FilterParametersQueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate entity) {
        GiftCertificate updated = entityManager.merge(entity);
        return Optional.of(updated);
    }

    @Override
    public List<GiftCertificate> findBySpecification(FilterParameters filterParameters) {
        TypedQuery<GiftCertificate> query = queryBuilder.buildSelectQuery(entityManager, filterParameters);
        return query.getResultList();
    }

    @Override
    public List<GiftCertificate> findBySpecification(FilterParameters filterParameters, int pageNumber, int pageSize) {
        TypedQuery<GiftCertificate> query = queryBuilder.buildSelectQuery(entityManager, filterParameters);
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
        if (certificateById != null && !certificateById.isAvailable()) {
            return Optional.empty();
        }
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

    @Override
    public int countAll() {
        TypedQuery<Long> query = entityManager.createQuery(COUNT_CERTIFICATES, Long.class);
        return query.getSingleResult().intValue();
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

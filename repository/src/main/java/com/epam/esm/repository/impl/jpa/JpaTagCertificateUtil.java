package com.epam.esm.repository.impl.jpa;

import com.epam.esm.repository.api.TagCertificateUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class JpaTagCertificateUtil implements TagCertificateUtil {

    private static final String COUNT_ASSOCIATED_TAGS = "SELECT count(gc) From Tag tag JOIN tag.certificates gc WHERE tag.id = :id";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int countAssociatedCertificates(int tagId) {
        TypedQuery<Long> query = entityManager.createQuery(COUNT_ASSOCIATED_TAGS, Long.class);
        query.setParameter("id", tagId);
        return query.getSingleResult().intValue();
    }
}

package com.epam.esm.entities.audit.certificate;

import com.epam.esm.entities.audit.AuditRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class GiftCertificateAuditRepository implements AuditRepository<GiftCertificateAudit> {

    @PersistenceContext
    private EntityManager entityManager;

    public void auditEntity(GiftCertificateAudit giftCertificateAudit) {
        entityManager.persist(giftCertificateAudit);
    }
}

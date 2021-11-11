package com.epam.esm.entities.audit.Tag;

import com.epam.esm.entities.audit.AuditRepository;
import com.epam.esm.entities.audit.Tag.TagAudit;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class TagAuditRepository implements AuditRepository<TagAudit> {

    @PersistenceContext
    private EntityManager entityManager;

    public void auditEntity(TagAudit tagAudit) {
        entityManager.persist(tagAudit);
    }
}

package com.epam.esm.entities.audit.user;

import com.epam.esm.entities.audit.AuditRepository;
import com.epam.esm.entities.audit.user.UserAudit;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class UserAuditRepository implements AuditRepository<UserAudit> {

    @PersistenceContext
    private EntityManager entityManager;

    public void auditEntity(UserAudit userAudit) {
        entityManager.persist(userAudit);
    }
}

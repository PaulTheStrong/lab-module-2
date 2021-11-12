package com.epam.esm.entities.audit.order;

import com.epam.esm.entities.audit.AuditRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class OrderAuditRepository implements AuditRepository<OrderAudit> {

    @PersistenceContext
    private EntityManager entityManager;

    public void auditEntity(OrderAudit orderAudit) {
        entityManager.persist(orderAudit);
    }
}

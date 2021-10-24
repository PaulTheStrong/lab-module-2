package com.epam.esm.entities.audit.order;

import com.epam.esm.entities.Order;
import com.epam.esm.entities.audit.AuditRepository;
import com.epam.esm.entities.audit.certificate.GiftCertificateAuditRepository;
import com.epam.esm.entities.audit.certificate.GiftCertificateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import java.time.LocalDateTime;

@Transactional
@Repository
public class OrderListener {

    private static final String DELETE = "DELETE";
    private static final String UPDATE = "UPDATE";
    private static final String CREATE = "CREATE";

    private static AuditRepository<OrderAudit> auditRepository;

    @Autowired
    public void setAuditRepository(AuditRepository<OrderAudit> auditRepository) {
        OrderListener.auditRepository = auditRepository;
    }

    @PostRemove
    private void doPostRemove(Order order) {
        audit(order, DELETE);
    }

    @PostPersist
    private void doPostPersist(Order order) {
        audit(order, CREATE);
    }

    private void audit(Order order, String operation) {
        OrderAudit auditEntity = new OrderAudit(order, operation, LocalDateTime.now());
        auditRepository.auditEntity(auditEntity);
    }
}

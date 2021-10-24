package com.epam.esm.entities.audit.Tag;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.entities.audit.AuditRepository;
import com.epam.esm.entities.audit.certificate.GiftCertificateAudit;
import com.epam.esm.entities.audit.order.OrderAudit;
import com.epam.esm.entities.audit.order.OrderListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

@Transactional
@Repository
public class TagListener {

    private static final String DELETE = "DELETE";
    private static final String UPDATE = "UPDATE";
    private static final String CREATE = "CREATE";

    private static AuditRepository<TagAudit> auditRepository;

    @Autowired
    public void setAuditRepository(AuditRepository<TagAudit> auditRepository) {
        TagListener.auditRepository = auditRepository;
    }

    @PostRemove
    private void doPostRemove(Tag tag) {
        audit(tag, DELETE);
    }

    @PostPersist
    private void doPostPersist(Tag tag) {
        audit(tag, CREATE);
    }

    private void audit(Tag tag, String operation) {
        TagAudit auditEntity = new TagAudit(tag, operation, LocalDateTime.now());
        auditRepository.auditEntity(auditEntity);
    }
}

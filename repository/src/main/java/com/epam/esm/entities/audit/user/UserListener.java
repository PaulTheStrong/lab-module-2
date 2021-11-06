package com.epam.esm.entities.audit.user;

import com.epam.esm.entities.User;
import com.epam.esm.entities.audit.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

@Transactional
@Repository
public class UserListener {

    private static final String DELETE = "DELETE";
    private static final String UPDATE = "UPDATE";
    private static final String CREATE = "CREATE";

    private static AuditRepository<UserAudit> auditRepository;

    @Autowired
    public void setAuditRepository(AuditRepository<UserAudit> auditRepository) {
        UserListener.auditRepository = auditRepository;
    }

    @PostRemove
    private void doPostRemove(User user) {
        audit(user, DELETE);
    }

    @PostUpdate
    private void doPostUpdate(User user) {
        audit(user, UPDATE);
    }

    @PostPersist
    private void doPostPersist(User user) {
        audit(user, CREATE);
    }

    private void audit(User user, String operation) {
        UserAudit auditEntity = new UserAudit(user, operation, LocalDateTime.now());
        auditRepository.auditEntity(auditEntity);
    }
}

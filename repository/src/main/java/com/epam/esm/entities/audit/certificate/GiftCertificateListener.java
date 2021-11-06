package com.epam.esm.entities.audit.certificate;

import com.epam.esm.entities.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

@Component
public class GiftCertificateListener {

    private static final String DELETE = "DELETE";
    private static final String UPDATE = "UPDATE";
    private static final String CREATE = "CREATE";

    private static GiftCertificateAuditRepository giftCertificateAuditRepository;

    @Autowired
    public void setAuditRepository(GiftCertificateAuditRepository giftCertificateAuditRepository) {
        GiftCertificateListener.giftCertificateAuditRepository = giftCertificateAuditRepository;
    }

    @PostRemove
    private void doPostRemove(GiftCertificate giftCertificate) {
        audit(giftCertificate, DELETE);
        giftCertificate.setAvailable(false);
    }

    @PostUpdate
    private void doPostUpdate(GiftCertificate giftCertificate) {
        audit(giftCertificate, UPDATE);
        LocalDateTime now = LocalDateTime.now();
        giftCertificate.setLastUpdateDate(now);
    }

    @PostPersist
    private void doPostPersist(GiftCertificate giftCertificate) {
        audit(giftCertificate, CREATE);
        LocalDateTime now = LocalDateTime.now();
        giftCertificate.setCreateDate(now);
        giftCertificate.setAvailable(true);
    }

    private void audit(GiftCertificate giftCertificate, String operation) {
        GiftCertificateAudit auditEntity = new GiftCertificateAudit(giftCertificate, operation, LocalDateTime.now());
        giftCertificateAuditRepository.auditEntity(auditEntity);
    }
}

package com.epam.esm.entities.audit.certificate;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Identifiable;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "gift_certificate_audit")
public class GiftCertificateAudit extends Identifiable {

    @Column(name = "certificate_id")
    private int giftCertificateId;
    @Column(name = "operation_type")
    private String operationType;
    @Column(name = "operation_date")
    private LocalDateTime operationDate;

    public GiftCertificateAudit(GiftCertificate giftCertificate, String operationType, LocalDateTime operationDate) {
        super(null);
        this.giftCertificateId = giftCertificate.getId();
        this.operationType = operationType;
        this.operationDate = operationDate;
    }

    public int getGiftCertificateId() {
        return giftCertificateId;
    }

    public void setGiftCertificateId(int giftCertificateId) {
        this.giftCertificateId = giftCertificateId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operation) {
        this.operationType = operation;
    }

    public LocalDateTime getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(LocalDateTime operationDate) {
        this.operationDate = operationDate;
    }
}

package com.epam.esm.entities.audit.Tag;

import com.epam.esm.entities.Identifiable;
import com.epam.esm.entities.Tag;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "tag_audit")
public class TagAudit extends Identifiable {

    @Column(name="tag_id")
    private int tagId;

    @Column(name = "operation_type")
    private String operationType;

    @Column(name = "operation_date")
    private LocalDateTime operationDate;

    public TagAudit(Tag tag, String operationType, LocalDateTime operationDate) {
        super(null);
        this.tagId = tag.getId();
        this.operationType = operationType;
        this.operationDate = operationDate;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
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

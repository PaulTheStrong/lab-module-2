package com.epam.esm.entities.audit.user;

import com.epam.esm.entities.Identifiable;
import com.epam.esm.entities.User;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "user_audit")
public class UserAudit extends Identifiable {

    @Column(name = "user_id")
    private int userId;
    @Column(name = "operation_type")
    private String operationType;
    @Column(name = "operation_date")
    private LocalDateTime operationDate;

    public UserAudit(User user, String operation, LocalDateTime operationDate) {
        super(null);
        this.userId = user.getId();
        this.operationType = operation;
        this.operationDate = operationDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

package com.epam.esm.entities.audit.order;

import com.epam.esm.entities.Identifiable;
import com.epam.esm.entities.Order;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "order_audit")
public class OrderAudit extends Identifiable {

    @Column(name = "order_id")
    private int orderId;
    @Column(name = "operation_type")
    private String operationType;
    @Column(name = "operation_date")
    private LocalDateTime operationDate;

    public OrderAudit(Order order, String operationType, LocalDateTime operationDate) {
        super(null);
        this.orderId = order.getId();
        this.operationType = operationType;
        this.operationDate = operationDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

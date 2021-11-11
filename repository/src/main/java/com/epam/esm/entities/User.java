package com.epam.esm.entities;

import com.epam.esm.entities.audit.certificate.GiftCertificateListener;
import com.epam.esm.entities.audit.user.UserListener;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
@NoArgsConstructor
@EntityListeners(UserListener.class)
public class User extends Identifiable{

    @Column(name="username")
    @Size(max=20, min=4)
    @NotNull
    private String username;

    @Column(name="balance")
    @Min(0)
    @NotNull
    private BigDecimal balance;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Order> orders;

    public User(Integer id, String username, BigDecimal balance, List<Order> orders) {
        super(id);
        this.username = username;
        this.balance = balance;
        this.orders = orders;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }
}
